/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iti.msapp;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author mahmo
 */

public class UDPVoiceTrafficSender implements Runnable {

    private static final String MSC_HOST = "localhost"; // عنوان MSC (يمكن تغييره) [cite: 4]
    private static final int MSC_PORT = 54321; // منفذ UDP لـ MSC (يجب أن يتطابق مع MSCApp) [cite: 4]
    private static final int SAMPLE_RATE = 8000; // معدل أخذ العينات (يمكن تعديله)
    private static final int BUFFER_SIZE = 1024; // حجم المخزن المؤقت (يمكن تعديله)

    private boolean running = true;
    private File outputFile;
    private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private TargetDataLine microphone;
    private AudioInputStream audioInputStream;
    private DatagramSocket socket;
    private InetAddress mscAddress;
    private String msisdn;
    private long startTime;

    public void startSendingVoice(String msisdn) throws IOException {
        this.msisdn = msisdn; // نخزن MSISDN على مستوى الفئة
        try {
            // 1. تهيئة خط الميكروفون [cite: 4]
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();

            // 2. تهيئة مقبس UDP وعنوان MSC [cite: 4]
            socket = new DatagramSocket();
            mscAddress = InetAddress.getByName(MSC_HOST);

            // 3. بدء تشغيل الخيط لإرسال الصوت [cite: 5]
            startTime = System.currentTimeMillis();
            setupRecording(msisdn, startTime); // تهيئة التسجيل
            new Thread(this).start();

        } catch (LineUnavailableException | SocketException e) {
            System.err.println("Error initializing audio or socket: " + e.getMessage());
            throw new IOException("Failed to start sending voice", e);
        }
    }

    private void setupRecording(String msisdn, long startTime) throws IOException {
        try {
            // إعداد تنسيق الملف واسم الملف
            AudioFormat format = microphone.getFormat();
            String timestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
            // تغيير المسار هنا
            String fileName = String.format("D:\\myvoice\\voice_call_msisdn_%s_date_%s.wav", msisdn, timestamp); // تنسيق اسم الملف [cite: 10]
            outputFile = new File(fileName);

            // إنشاء AudioInputStream من TargetDataLine
            audioInputStream = new AudioInputStream(microphone);

        } catch (Exception e) {
            throw new IOException("Could not setup recording", e);
        }
    }

    @Override
    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        long elapsedMinutes = 0;

        try {
            while (running) {
                // 4. قراءة البيانات من الميكروفون وإرسالها عبر UDP [cite: 4, 5]
                bytesRead = microphone.read(buffer, 0, buffer.length);
                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, mscAddress, MSC_PORT);
                socket.send(packet);

                // كتابة البيانات الصوتية إلى الملف
                AudioSystem.write(audioInputStream, fileType, outputFile);

                // 5. طباعة الدقائق المنقضية كل دقيقة [cite: 5]
                long currentTime = System.currentTimeMillis();
                long elapsed = (currentTime - startTime) / 1000; // بالثواني
                if (elapsed / 60 > elapsedMinutes) {
                    elapsedMinutes++;
                    System.out.println(elapsedMinutes + " minutes elapsed");
                }
            }
        } catch (IOException e) {
            System.err.println("Error sending/recording voice data: " + e.getMessage());
        } finally {
            // 6. تنظيف الموارد
            if (microphone != null) {
                microphone.stop();
                microphone.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
    }

    public void stopSendingVoice() {
        running = false;
    }

    // هنا يمكن إضافة طريقة لتسجيل الصوت (MP3)
}
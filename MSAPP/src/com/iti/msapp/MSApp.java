/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iti.msapp;





import java.io.IOException;
/**
 *
 * @author mahmo
 */

public class MSApp {

    public static void main(String[] args) {
        String msisdn = processCommandLineArguments(args);
        if (msisdn == null) {
            return; // إنهاء التطبيق إذا كانت الوسيطات غير صالحة
        }

        System.out.println("Starting voice call as MSISDN " + msisdn);

        TCPSignalingClient tcpSignalingClient = new TCPSignalingClient();
        UDPVoiceTrafficSender udpVoiceTrafficSender = new UDPVoiceTrafficSender();

        try {
            tcpSignalingClient.startCall(msisdn);
            udpVoiceTrafficSender.startSendingVoice(msisdn);
        } catch (IOException e) {
            System.err.println("Error starting call: " + e.getMessage());
            // يمكن إضافة تسجيل مفصل هنا
        } finally {
            addShutdownHook(tcpSignalingClient, msisdn);
        }
    }

    private static String processCommandLineArguments(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java MSApp <MSISDN>");
            return null;
        }
        return args[0];
    }

    private static void addShutdownHook(TCPSignalingClient tcpSignalingClient, String msisdn) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                tcpSignalingClient.endCall(msisdn);
            } catch (IOException e) {
                System.err.println("Error ending call: " + e.getMessage());
                // يمكن إضافة تسجيل مفصل هنا
            }
        }));
    }
}

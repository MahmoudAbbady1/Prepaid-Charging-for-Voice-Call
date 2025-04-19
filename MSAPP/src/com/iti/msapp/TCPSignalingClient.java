/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iti.msapp;
/**
 *
 * @author mahmo
 */


import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class TCPSignalingClient {

    private static final String MSC_HOST = "localhost";
    private static final int MSC_PORT = 1234;

    public void startCall(String msisdn) throws IOException {
        try (Socket socket = new Socket(MSC_HOST, MSC_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("Start Call " + msisdn);
            System.out.println("Sent Start Call signaling message to MSC.");

        } catch (ConnectException e) {
            System.err.println("Error connecting to MSC: " + e.getMessage());
            e.printStackTrace(); // طباعة تتبع المكدس
            throw e; // إعادة رمي الاستثناء للسماح للمعالجة في مكان آخر
        } catch (IOException e) {
            System.err.println("Error sending Start Call: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void endCall(String msisdn) throws IOException {
        try (Socket socket = new Socket(MSC_HOST, MSC_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("End Call " + msisdn);
            System.out.println("Sent End Call signaling message to MSC.");

        } catch (ConnectException e) {
            System.err.println("Error connecting to MSC: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            System.err.println("Error sending End Call: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
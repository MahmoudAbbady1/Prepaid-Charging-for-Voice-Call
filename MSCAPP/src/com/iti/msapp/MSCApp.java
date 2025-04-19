/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */








package com.iti.msapp;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author mahmo
 */
/**
 * The main class for the MSC application.
 * It listens for incoming TCP connections from MSApp and starts handlers
 * for TCP signaling and UDP voice traffic.
 */

public class MSCApp {

    private static final int TCP_PORT = 1234; // Should match MSApp's TCP port
    private static final int UDP_PORT = 54321; // UDP port for voice traffic

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {
            System.out.println("Waiting for voice call Signaling start message via TCP"); //

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accept Voice call start signaling message from " + clientSocket.getInetAddress().getHostAddress()); //

                // Start TCP signaling handler in a separate thread
                startTcpSignalingHandler(clientSocket, UDP_PORT);

                // Start UDP voice traffic handler in a separate thread
                startUdpVoiceTrafficHandler(UDP_PORT);
            }

        } catch (IOException e) {
            System.err.println("MSCApp error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Starts the TCP signaling handler thread.
     *
     * @param clientSocket The socket for the TCP connection with MSApp.
     * @param udpPort      The UDP port for voice traffic.
     */
    private static void startTcpSignalingHandler(Socket clientSocket, int udpPort) {
        TCPSignalingHandler tcpHandler = new TCPSignalingHandler(clientSocket, udpPort);
        new Thread(tcpHandler).start();
    }

    /**
     * Starts the UDP voice traffic handler thread.
     *
     * @param udpPort The UDP port for voice traffic.
     */
    private static void startUdpVoiceTrafficHandler(int udpPort) {
        UDPVoiceTrafficHandler udpHandler = new UDPVoiceTrafficHandler(udpPort);
        new Thread(udpHandler).start();
    }
}


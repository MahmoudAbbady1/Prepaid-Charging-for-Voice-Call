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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Handles UDP communication with MSApp for voice traffic.
 * This class is responsible for receiving voice data packets from MSApp
 * and processing them (e.g., playing audio).
 */
public class UDPVoiceTrafficHandler implements Runnable {

    private DatagramSocket socket;
    private int udpPort;
    private byte[] buffer;
    private boolean running;

    /**
     * Constructs a UDPVoiceTrafficHandler.
     *
     * @param udpPort The UDP port used for voice traffic.
     */
    public UDPVoiceTrafficHandler(int udpPort) {
        this.udpPort = udpPort;
        this.buffer = new byte[1024]; // Buffer for UDP packets (adjust size as needed)
        this.running = true;

        try {
            socket = new DatagramSocket(udpPort);
            System.out.println("UDPVoiceTrafficHandler started on port " + udpPort);
        } catch (SocketException e) {
            System.err.println("UDPVoiceTrafficHandler: Could not create socket: " + e.getMessage());
            running = false; // Prevent further execution if socket creation fails
        }
    }

    @Override
    public void run() {
        if (!running) {
            return; // Exit if socket creation failed
        }

        System.out.println("Capturing UDP traffic and play via speaker"); //

        while (running) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                processPacket(packet);
            } catch (IOException e) {
                System.err.println("UDPVoiceTrafficHandler error: " + e.getMessage());
                break; // Exit loop on error
            }
        }
        cleanup();
    }

    /**
     * Processes a received UDP packet.
     *
     * @param packet The received DatagramPacket.
     */
    private void processPacket(DatagramPacket packet) {
        // Extract data from the packet
        byte[] receivedData = packet.getData();
        int receivedLength = packet.getLength();

        // For now, let's just print the received data as a string
        // You'll likely want to decode and play the audio here
        String receivedString = new String(receivedData, 0, receivedLength);
        System.out.println("Received UDP data: " + receivedString);

        // TODO: Implement audio playback (e.g., using javax.sound.sampled)
        // Example (Conceptual):
        // playAudio(receivedData, receivedLength);
    }

    /**
     * Stops the UDPVoiceTrafficHandler.
     * This method can be called to gracefully stop the handler.
     */
    public void stop() {
        running = false;
    }

    /**
     * Cleans up resources (closes socket).
     */
    private void cleanup() {
        if (socket != null) {
            socket.close();
        }
    }

    // TODO: Implement the playAudio method (or similar)
    // private void playAudio(byte[] data, int length) { ... }
}
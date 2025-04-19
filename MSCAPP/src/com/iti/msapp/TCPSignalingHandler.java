/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iti.msapp;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * Handles TCP communication with MSApp for call signaling.
 * This class is responsible for receiving and sending control messages
 * related to call setup and teardown.
 */
public class TCPSignalingHandler implements Runnable {

    private Socket clientSocket;
    private int udpPort;
    private String msisdn;
    private Call currentCall; // To track the current call

    /**
     * Constructs a TCPSignalingHandler.
     *
     * @param socket  The socket for communication with MSApp.
     * @param udpPort The UDP port used for voice traffic.
     */
    public TCPSignalingHandler(Socket socket, int udpPort) {
        this.clientSocket = socket;
        this.udpPort = udpPort;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from MSApp: " + inputLine);
                processInput(inputLine, out);
            }
        } catch (IOException e) {
            System.err.println("TCPSignalingHandler error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    /**
     * Processes the input received from MSApp.
     *
     * @param inputLine The line received from MSApp.
     * @param out       The PrintWriter for sending responses to MSApp.
     */
    private void processInput(String inputLine, PrintWriter out) {
        if (inputLine.startsWith("Start Call ")) {
            handleStartCall(inputLine, out);
        } else if (inputLine.equals("End Call")) {
            handleEndCall(out);
        } else {
            System.out.println("Unknown message from MSApp: " + inputLine);
        }
    }

    /**
     * Handles the "Start Call" message from MSApp.
     *
     * @param inputLine The "Start Call" message.
     * @param out       The PrintWriter for sending responses to MSApp.
     */
    private void handleStartCall(String inputLine, PrintWriter out) {
        msisdn = inputLine.substring(11); // Extract MSISDN
        System.out.println("Received Start Call signaling message from MSISDN " + msisdn); //
        out.println("ACK"); // Send acknowledgment

        // Create a new Call object to track this call
        currentCall = new Call(msisdn, LocalDateTime.now());
    }

    /**
     * Handles the "End Call" message from MSApp.
     *
     * @param out The PrintWriter for sending responses to MSApp.
     */
    private void handleEndCall(PrintWriter out) {
        System.out.println("Received End Call signaling message"); //
        out.println("ACK");

        // Process call end (calculate cost, generate CDR)
        processCallEnd();
    }

    /**
     * Processes the end of a call (calculates duration, cost, and generates CDR).
     */
    private void processCallEnd() {
        if (currentCall != null) {
            currentCall.setEndTime(LocalDateTime.now());
            long duration = currentCall.getDuration(); // Duration in minutes
            double cost = calculateCallCost(duration);

            CDRGenerator.generateCDR(
                currentCall.getMsisdn(),
                currentCall.getStartTime(),
                currentCall.getEndTime(),
                (int) duration,
                cost
            );
            currentCall = null; // Reset current call
        }
    }

    /**
     * Calculates the cost of the call.  (This is a simple example; you might
     * have more complex logic based on time of day, etc.)
     *
     * @param duration The duration of the call in minutes.
     * @return The cost of the call.
     */
    private double calculateCallCost(long duration) {
        final double COST_PER_MINUTE = 5.0; // 5 LE per minute
        return duration * COST_PER_MINUTE;
    }

    /**
     * Cleans up resources (closes socket, etc.).
     */
    private void cleanup() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }

    public String getMsisdn() {
        return msisdn;
    }
}
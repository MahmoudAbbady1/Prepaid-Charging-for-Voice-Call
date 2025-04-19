/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iti.msapp;

/**
 *
 * @author mahmo
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Generates Call Detail Records (CDRs) and writes them to a file.
 * CDRs contain information about completed calls, such as MSISDN,
 * start time, end time, duration, and cost.
 */
public class CDRGenerator {

    private static final String CDR_FILE_PATH = "D:\\mycdr\\calls.cdr"; // CDR file path - Modified to D:\mycdr\calls.cdr

    /**
     * Generates a CDR and appends it to the CDR file.
     *
     * @param msisdn    The MSISDN of the caller.
     * @param startTime The start time of the call.
     * @param endTime   The end time of the call.
     * @param duration  The duration of the call in minutes.
     * @param cost      The cost of the call.
     */
    public static void generateCDR(String msisdn, LocalDateTime startTime, LocalDateTime endTime, int duration, double cost) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CDR_FILE_PATH, true))) { // true for append
            String startTimeString = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String endTimeString = endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // CDR format: MSISDN, Start Time, End Time, Duration, Call result, Call cost, Balance after call end
            // Example: 01223456789, 2025-03-01T15:24:47.398253, 2025-03-01T15:26:47.398253, 4, Normal call Clearing, 20, 1001
            String cdrLine = String.format("%s, %s, %s, %d, Normal call Clearing, %.2f, %.2f",
                    msisdn, startTimeString, endTimeString, duration, cost, 100.00);  // Balance after call end is a placeholder

            writer.println(cdrLine);
            System.out.println("Generating CDR line: " + cdrLine); //

        } catch (IOException e) {
            System.err.println("Error writing CDR file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
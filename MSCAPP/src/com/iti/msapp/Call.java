/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iti.msapp;

/**
 *
 * @author mahmo
 */

import java.time.LocalDateTime;

/**
 * Represents a single voice call.
 * This class stores information about a call, such as MSISDN,
 * start time, and end time.
 */
public class Call {

    private String msisdn;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /**
     * Constructs a Call object.
     *
     * @param msisdn    The MSISDN of the caller.
     * @param startTime The start time of the call.
     */
    public Call(String msisdn, LocalDateTime startTime) {
        this.msisdn = msisdn;
        this.startTime = startTime;
    }

    /**
     * Gets the MSISDN of the caller.
     *
     * @return The MSISDN.
     */
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * Gets the start time of the call.
     *
     * @return The start time.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the end time of the call.
     *
     * @param endTime The end time.
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the end time of the call.
     *
     * @return The end time.
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Gets the duration of the call in minutes.
     *
     * @return The duration in minutes.
     */
    public long getDuration() {
        if (startTime != null && endTime != null) {
            return java.time.Duration.between(startTime, endTime).toMinutes();
        }
        return 0;
    }

    /**
     * Overrides the toString method to provide a string representation of the Call object.
     *
     * @return A string representation of the Call object.
     */
    @Override
    public String toString() {
        return "Call{" +
                "msisdn='" + msisdn + '\'' +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
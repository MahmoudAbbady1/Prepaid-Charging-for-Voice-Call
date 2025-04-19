/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iti.msapp;

/**
 *
 * @author mahmo
 */




/**
 * Handles database interactions for user balances and related data.
 * This class will be used to fetch user balances, deduct call costs,
 * and update balances in the database.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles database interactions for user balances and related data.
 * This class will be used to fetch user balances, deduct call costs,
 * and update balances in the database.
 */
public class DatabaseHandler {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/msc_data"; // Database URL
    private static final String DB_USER = "root"; // Database username
    private static final String DB_PASSWORD = "root"; // Database password

    /**
     * Fetches the current balance of a user.
     *
     * @param msisdn The MSISDN of the user.
     * @return The user's balance, or -1 if an error occurs.
     */
    public double getUserBalance(String msisdn) {
        double balance = -1;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT balance FROM users WHERE msisdn = ?")) {
            pstmt.setString(1, msisdn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        } catch (SQLException e) {
            System.err.println("DatabaseHandler: Error fetching balance: " + e.getMessage());
            e.printStackTrace();
        }
        return balance;
    }

    /**
     * Deducts the cost of a call from the user's balance.
     *
     * @param msisdn The MSISDN of the user.
     * @param cost   The cost of the call.
     * @return True if the deduction was successful, false otherwise.
     */
    public boolean deductFromBalance(String msisdn, double cost) {
        boolean success = false;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET balance = balance - ? WHERE msisdn = ?")) {
            pstmt.setDouble(1, cost);
            pstmt.setString(2, msisdn);
            int rowsUpdated = pstmt.executeUpdate();
            success = rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("DatabaseHandler: Error deducting from balance: " + e.getMessage());
            e.printStackTrace();
        }
        return success;
    }

    /**
     * Updates the user's balance directly (e.g., for adding balance).
     *
     * @param msisdn  The MSISDN of the user.
     * @param newBalance The new balance to set.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateUserBalance(String msisdn, double newBalance) {
        boolean success = false;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET balance = ? WHERE msisdn = ?")) {
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, msisdn);
            int rowsUpdated = pstmt.executeUpdate();
            success = rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("DatabaseHandler: Error updating balance: " + e.getMessage());
            e.printStackTrace();
        }
        return success;
    }

    // Other database-related methods can be added here
}
/*
 * Author: Oluwatamilore Derek Akisanya
 * Class: CSCI-C 212
 * Project: Group Banking Application
 * Role: Person D - Money Transfers & Transaction History
 * Description: This class defines the structure of a Transaction and formats it for CSV storage.
 * Date: April 10, 2025
 */

import java.time.LocalDateTime;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Transaction {
    private static int idCounter = 1; //simple tally to auto-generate unique transaction IDs
    private int transactionId;
    private String timestamp;
    private String fromAccount;
    private String toAccount;
    private double amount;
    private String type;
    private String note;

    static {
        // Initialize idCounter from the last transaction in the file
        try {
            File file = new File("data/transactions.csv");
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    String lastLine = null;
                    while ((line = reader.readLine()) != null) {
                        lastLine = line;
                    }
                    if (lastLine != null && !lastLine.trim().isEmpty()) {
                        String[] parts = lastLine.trim().split("\\s{2,}");
                        if (parts.length >= 1) {
                            int lastId = Integer.parseInt(parts[0].trim());
                            idCounter = lastId + 1;
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error initializing transaction ID counter: " + e.getMessage());
        }
    }

    // constructor that initializes a transaction with all required fields
    public Transaction(String fromAccount, String toAccount, double amount, String type, String note) {
        this.transactionId = idCounter++;
        this.timestamp = LocalDateTime.now().toString(); // gets the date/time of the transaction
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = type;
        this.note = note;
    }

    // constructor for loading transactions from file
    public Transaction(int transactionId, String timestamp, String fromAccount, String toAccount, double amount, String type, String note) {
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = type;
        this.note = note;
        // Update idCounter if needed
        if (transactionId >= idCounter) {
            idCounter = transactionId + 1;
        }
    }

    // Getters
    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getNote() {
        return note;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getTransactionId() {
        return transactionId;
    }

    // converts the transaction to a formatted string for csv file writing
    public String toCSV() {
        return String.format("%-15d %-25s %-20s %-20s %-10.2f %-12s %s",
                transactionId,
                timestamp,
                fromAccount,
                toAccount,
                amount,
                type,
                note
        );
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%.2f,%s,%s",
                timestamp, fromAccount, toAccount, amount, type, note);
    }
}

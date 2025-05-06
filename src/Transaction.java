/*
 * Author: Oluwatamilore Derek Akisanya
 * Class: CSCI-C 212
 * Project: Group Banking Application
 * Role: Person D - Money Transfers & Transaction History
 * Description: This class defines the structure of a Transaction and formats it for CSV storage.
 * Date: April 10, 2025
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

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
        // Initialize idCounter from the highest transaction ID in the file
        try {
            File file = new File("data/transactions.csv");
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    int highestId = 0;
                    boolean isFirstLine = true;
                    
                    while ((line = reader.readLine()) != null) {
                        // Skip header line
                        if (isFirstLine) {
                            isFirstLine = false;
                            continue;
                        }
                        
                        // Skip empty lines
                        if (line.trim().isEmpty()) {
                            continue;
                        }
                        
                        String[] parts = line.trim().split("\\s{2,}");
                        if (parts.length >= 1) {
                            try {
                                int id = Integer.parseInt(parts[0].trim());
                                if (id > highestId) {
                                    highestId = id;
                                }
                            } catch (NumberFormatException e) {
                                // Skip lines that can't be parsed
                                continue;
                            }
                        }
                    }
                    idCounter = highestId + 1;
                    System.out.println("Initialized transaction ID counter to: " + idCounter);
                }
            }
        } catch (IOException e) {
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
        return String.format("%d,%s,%s,%s,%.2f,%s,%s",
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

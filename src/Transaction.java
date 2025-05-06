/*
 * Author: Oluwatamilore Derek Akisanya
 * Class: CSCI-C 212
 * Project: Group Banking Application
 * Role: Person D - Money Transfers & Transaction History
 * Description: This class defines the structure of a Transaction and formats it for CSV storage.
 * Date: April 10, 2025
 */


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

    // converts the transaction to a coma-separted string for csv file writing

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

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
    private String sourceAccountId;
    private String destinationAccountId;
    private double amount;
    private String transactionType;
    private String note;

    // constructor that initializes a transaction with all required fields 
     public Transaction(String sourceAccountId, String destinationAccountIdString, double amount, String transactionType, String noteString){
        this.transactionId = idCounter++;
        this.timestamp = LocalDateTime.now().toString(); // gets the date/time of the transaction
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountIdString;
        this.amount = amount;
        this.transactionType = transactionType;
        this.note = noteString;
    }


    // converts the transaction to a coma-separted string for csv file writing
    
    public String toCSV() {
    return String.format("%-15d %-25s %-20s %-20s %-10.2f %-12s %s",
        transactionId,
        timestamp,
        sourceAccountId,
        destinationAccountId,
        amount,
        transactionType,
        note
    );
}

}

/*
 * Author: Oluwatamilore Derek Akisanya
 * Class: CSCI-C 212
 * Project: Group Banking Application
 * Role: Person D - Money Transfers & Transaction History
 * Description: This utility class handles writing transaction records to the transactions.csv file.
 *              Every transaction (deposit, withdrawal, transfer, interest) will be appended to the file.
 * Date: April 16, 2025
 */

import java.io.FileWriter;
import java.io.IOException;

public class TransactionLogger {
    private static final String FILE_PATH = "transactions.csv"; // path to the transaction log file

    // Writes the given Transaction object to the csv file 
    public static void log( Transaction transaction){
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(transaction.toCSV() + "\n"); // Append the transaction record to the file 
        } catch (IOException e) {
            System.out.println("Error writing transaction: " + e.getMessage());
        }
    }
}

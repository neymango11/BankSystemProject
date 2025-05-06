/*
 * Author: Oluwatamilore Derek Akisanya
 * Class: CSCI-C 212
 * Project: Group Banking Application
 * Role: Person D - Money Transfers & Transaction History
 * Description: This utility class handles writing transaction records to the transactions.csv file.
 *              Every transaction (deposit, withdrawal, transfer, interest) will be appended to the file.
 *              It also writes a header row when the file is first created.
 * Date: April 16, 2025
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TransactionLogger {
    // Path to the transaction CSV file
    private static final String FILE_PATH = "data/transactions.csv";

    /**
     * Logs a transaction to the transactions.csv file.
     * If the file is new or empty, writes a header row first.
     * @param transaction The Transaction object to be logged.
     */
    public static void log(Transaction transaction) {
        try {
            // Create data directory if it doesn't exist
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            File file = new File(FILE_PATH);
            boolean isNewFile = !file.exists() || file.length() == 0;

            // Use PrintWriter for better formatting
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
                // If new or empty, add column headers
                if (isNewFile) {
                    writer.println(String.format("%-15s %-25s %-20s %-20s %-10s %-12s %s",
                            "Transaction ID", "Timestamp", "From Account", "To Account", "Amount", "Type", "Note"));
                }

                // Write the formatted transaction line
                writer.println(transaction.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error writing transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

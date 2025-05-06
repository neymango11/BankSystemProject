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
 
 public class TransactionLogger {
     // Path to the transaction CSV file
     private static final String FILE_PATH = "data/transactions.csv";
 
     /**
      * Logs a transaction to the transactions.csv file.
      * If the file is new or empty, writes a header row first.
      * @param transaction The Transaction object to be logged.
      */
     public static void log(Transaction transaction) {
         // Create data directory if it doesn't exist
         new File("data").mkdirs();
         
         File file = new File(FILE_PATH);
 
         // Determine if the file is new or empty
         boolean fileExists = file.exists();
         boolean isNewFile = !fileExists || file.length() == 0;
 
         try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
 
             // If new or empty, add column headers
             if (isNewFile) {
                 writer.write(String.format(
                     "%-15s %-25s %-20s %-20s %-10s %-12s %s\n",
                     "Transaction ID", "Timestamp", "From Account", "To Account", "Amount", "Type", "Note"
                 ));
             }
 
             // Write the formatted transaction line
             writer.write(transaction.toCSV() + "\n");
 
         } catch (IOException e) {
             System.out.println("Error writing transaction: " + e.getMessage());
         }
     }
 }
 
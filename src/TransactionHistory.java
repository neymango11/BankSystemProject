/*
 * Author: Oluwatamilore Derek Akisanya
 * Class: CSCI-C 212
 * Project: Group Banking Application
 * Role: Person D - Money Transfers & Transaction History
 * Description: This class provides functionality to read and display all transaction history
 *              related to a specific account. It filters results based on account ID and shows
 *              only relevant transactions in a clean format in the terminal.
 * Date: April 16, 2025
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



public class TransactionHistory {
    // Reads the transactions.csv file and prints transactions that involve the gien accountId
    public static void viewByAccount(String accountId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("transactions.csv"))) {
            String line;
            System.out.println("Transaction history for account: " + accountId);
            System.out.println("-------------------------------------------------------");

            // Go line by line and show transactions that match the account ID 
            while ((line = reader.readLine()) != null) {
                if (line.contains(accountId)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println( "Eror reading transaction history: " + e.getMessage());
        }
    }
}

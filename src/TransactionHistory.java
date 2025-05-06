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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionHistory {
    private static List<Transaction> transactions = new ArrayList<>();
    private static final String FILE_PATH = "data/transactions.csv";

    private static void loadTransactions() {
        transactions.clear(); // Clear existing transactions to prevent duplicates
        System.out.println("\n=== Loading transactions from file ===");
        File file = new File(FILE_PATH);
        System.out.println("Transaction file path: " + file.getAbsolutePath());
        System.out.println("File exists: " + file.exists());
        
        if (!file.exists()) {
            System.out.println("Transaction file does not exist");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            int lineCount = 0;
            
            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (isFirstLine) {
                    System.out.println("Skipping header line");
                    isFirstLine = false;
                    continue;
                }
                
                if (line.trim().isEmpty()) {
                    System.out.println("Skipping empty line");
                    continue;
                }
                
                System.out.println("\nProcessing line " + lineCount + ": " + line);
                String[] parts = line.split(",");
                
                if (parts.length >= 7) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String timestamp = parts[1].trim();
                        String fromAccount = parts[2].trim();
                        String toAccount = parts[3].trim();
                        double amount = Double.parseDouble(parts[4].trim());
                        String type = parts[5].trim();
                        String note = parts[6].trim();
                        
                        Transaction transaction = new Transaction(id, timestamp, fromAccount, toAccount, amount, type, note);
                        transactions.add(transaction);
                        System.out.println("Successfully loaded transaction: " + id);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing transaction data: " + e.getMessage());
                    }
                } else {
                    System.err.println("Invalid transaction format: " + line);
                }
            }
            System.out.println("\nFinished loading transactions. Total loaded: " + transactions.size());
        } catch (IOException e) {
            System.err.println("Error reading transaction file: " + e.getMessage());
        }
    }

    public static void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public static List<Transaction> getAllTransactions() {
        loadTransactions(); // Reload transactions before returning
        return new ArrayList<>(transactions);
    }

    public static List<Transaction> getUserTransactions(int userID) {
        loadTransactions(); // Reload transactions before filtering
        System.out.println("\n=== Getting transactions for user ID: " + userID + " ===");
        System.out.println("Total transactions in system: " + transactions.size());
        
        List<BankAccount> userAccounts = BankAccount.getUserAccounts(userID);
        System.out.println("Found " + userAccounts.size() + " accounts for user");
        for (BankAccount acc : userAccounts) {
            System.out.println("User account: " + acc.getAccountID());
        }
        
        System.out.println("\nChecking each transaction:");
        List<Transaction> userTransactions = transactions.stream()
                .filter(t -> {
                    System.out.println("\nChecking transaction: " + t.getTransactionId());
                    System.out.println("From: " + t.getFromAccount() + ", To: " + t.getToAccount());
                    
                    // Check if the transaction involves any of the user's accounts
                    boolean matches = userAccounts.stream()
                            .anyMatch(acc -> {
                                boolean isMatch = acc.getAccountID().equals(t.getFromAccount()) ||
                                        acc.getAccountID().equals(t.getToAccount());
                                if (isMatch) {
                                    System.out.println("Match found with account: " + acc.getAccountID());
                                }
                                return isMatch;
                            });
                    
                    if (matches) {
                        System.out.println("Transaction " + t.getTransactionId() + " matches user's accounts");
                    } else {
                        System.out.println("Transaction " + t.getTransactionId() + " does not match user's accounts");
                    }
                    return matches;
                })
                .collect(Collectors.toList());
        
        System.out.println("\nFound " + userTransactions.size() + " transactions for user");
        return userTransactions;
    }

    public static List<Transaction> getAccountTransactions(String accountID) {
        loadTransactions(); // Reload transactions before filtering
        return transactions.stream()
                .filter(t ->
                        t.getFromAccount().equals(accountID) ||
                                t.getToAccount().equals(accountID)
                )
                .collect(Collectors.toList());
    }

    // Reads the transactions.csv file and prints transactions that involve the given accountId
    public static void viewByAccount(String accountId) {
        List<Transaction> accountTransactions = getAccountTransactions(accountId);

        if (accountTransactions.isEmpty()) {
            System.out.println("No transactions found for account: " + accountId);
            return;
        }

        System.out.println("Transaction history for account: " + accountId);
        System.out.println("-------------------------------------------------------");

        for (Transaction transaction : accountTransactions) {
            System.out.println("\nTransaction ID: " + transaction.getTransactionId());
            System.out.println("Date: " + transaction.getTimestamp());
            System.out.println("Type: " + transaction.getType());
            System.out.println("Amount: $" + transaction.getAmount());
            System.out.println("From: " + transaction.getFromAccount());
            System.out.println("To: " + transaction.getToAccount());
            System.out.println("Note: " + transaction.getNote());
            System.out.println("-------------------");
        }
    }
}

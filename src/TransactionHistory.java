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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.File;

public class TransactionHistory {
    private static List<Transaction> transactions = new ArrayList<>();
    private static final String FILE_PATH = "data/transactions.csv";

    static {
        loadTransactions();
    }

    private static void loadTransactions() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("No transaction history file found. A new file will be created when transactions occur.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
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

                // Parse transaction data
                // Split by multiple spaces but preserve the note field
                String[] parts = line.trim().split("\\s{2,}");
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
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing transaction: " + line);
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public static List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    public static List<Transaction> getUserTransactions(int userID) {
        return transactions.stream()
                .filter(t -> {
                    // Check if the transaction involves any of the user's accounts
                    List<BankAccount> userAccounts = BankAccount.getUserAccounts(userID);
                    return userAccounts.stream()
                            .anyMatch(acc ->
                                    acc.getAccountID().equals(t.getFromAccount()) ||
                                            acc.getAccountID().equals(t.getToAccount())
                            );
                })
                .collect(Collectors.toList());
    }

    public static List<Transaction> getAccountTransactions(String accountID) {
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

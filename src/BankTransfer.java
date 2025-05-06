/*
 * Author: Oluwatamilore Derek Akisanya
 * Class: CSCI-C 212
 * Project: Group Banking Application
 * Role: Person D - Money Transfers & Transaction History
 * Description: This class performs a secure transfer of money between two bank accounts.
 *              It updates balances using the BankAccount methods, and logs each successful transfer.
 * Date: April 16, 2025
 */

public class BankTransfer {

    /**
     * Transfers money between two bank accounts
     * @param fromAccount The source account
     * @param toAccount The destination account
     * @param amount The amount to transfer
     * @return true if transfer was successful, false otherwise
     */
    public static boolean transfer(BankAccount fromAccount, BankAccount toAccount, double amount) {
        // Input validation
        if (fromAccount == null || toAccount == null) {
            System.out.println("Transfer failed: Invalid account(s).");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Transfer failed: Amount must be greater than 0.");
            return false;
        }

        // Check if accounts are the same
        if (fromAccount.getAccountID().equals(toAccount.getAccountID())) {
            System.out.println("Transfer failed: Cannot transfer to the same account.");
            return false;
        }

        // Check if sender has enough balance
        if (fromAccount.getBalance() < amount) {
            System.out.println("Transfer failed: Insufficient funds.");
            return false;
        }

        try {
            // Perform the transfer using BankAccount's transfer method
            boolean success = fromAccount.transfer(toAccount, amount);

            if (success) {
                System.out.println("Transfer successful!");
                System.out.printf("Transferred $%.2f from %s to %s%n",
                        amount, fromAccount.getAccountID(), toAccount.getAccountID());
                System.out.printf("New balance for %s: $%.2f%n",
                        fromAccount.getAccountID(), fromAccount.getBalance());
                System.out.printf("New balance for %s: $%.2f%n",
                        toAccount.getAccountID(), toAccount.getBalance());
                return true;
            } else {
                System.out.println("Transfer failed: An error occurred during the transfer.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
            return false;
        }
    }
}

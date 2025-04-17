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

    // Transfers the specified amount between two BankAccount objects 

    public static void transfer(BankAccount fromAccount, BankAccount toAccount, double amount) {
        String fromId = fromAccount.getAccountId();
        String toId = toAccount.getAccountId();





        // check if sender has enough balance to make the transfer 
        if (fromAccount.getBalance() < amount) {
            System.out.println(" Transfer failed: insuffiecient funds.");
            return;
            
        }

        // perform withdrawal and deposit using BankAccount methods 

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        // log the succesful transfer in transaction.csv 
        Transaction transaction = new Transaction(fromId, toId, amount, "TRANSFER","Money transfer from " + fromId + " to " + toId);

        TransactionLogger.log(transaction); // Save to log
    }
    
}

/*
 * Aman Mohamed
 * -------BankAccount Class------
 * This class is in charge of creating Checking and Saving Accounts
 * and it will tie it to there userID
 * When creating there Savings and Checking they will be able to deposit money right there
 *
 * */

import java.util.List;

/**
 * BankAccount class represents a bank account in the system.
 * It handles both checking and savings accounts with their respective functionalities.
 * Each account is tied to a user ID and can perform basic banking operations.
 */
public class BankAccount {

    private String accountID;
    // Unique identifier for the account
    private int userID;
    // Reference to the user who owns this account
    private double balance;
    // Current balance in the account
    private String accountType;
    // Type of account (CHECKING or SAVING)

    /**
     Constructor to create a BankAccount to create one you need to have a accountId, userID
     balance, and accountType
     */
    public BankAccount(String accountId, int userID, double balance, String accountType) {
        this.accountID = accountId;
        this.userID = userID;
        this.balance = balance;
        this.accountType = accountType.toUpperCase(); // Store account type in uppercase
    }

    // Standard getter methods for account properties
    public String getAccountID() {
        return accountID;
    }

    public int getUserID() {
        return userID;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType(){
        return accountType;
    }

    /**
     To create a Checking Account you need a name, userId, initialDeposit
     FORMAT =  name-C-userID
     It will create an instance of the BankAccount class and write this to our CSV FILE
     */
    public static BankAccount createChecking(String name, int userID, double initialDeposit) {
        String accountID = name + "-C-" + userID;
        BankAccount account = new BankAccount(accountID, userID, initialDeposit, "CHECKING");
        BankAccountCSV.writeToCSV(account);
        return account;
    }

    /**
     * To create a savingsAccount you need a name, userId, initialDeposit
     * FORMAT = name-S-userID
     * it will create and instance of the BankAccount class and write this to our CSV FILE
     */
    public static BankAccount createSavings(String name, int userID, double initialDeposit) {
        String accountID = name + "-S-" + userID;
        BankAccount account = new SavingsAccount(accountID, userID, initialDeposit);
        BankAccountCSV.writeToCSV(account);
        return account;
    }

    /**
     * DEPOSIT METHOD
     * it will have a check if the amount is greater than 0 if so it will add it to balance
     * it will then use the transaction class to create a new transaction object and it will record it from there
     * after it will print out the transaction with the ID
     * It will update using the "this" keyword so it know we are refrencing the current object
     */
    public void deposit(double amount) {
        if (amount > 0) {
            balance = balance + amount;
            System.out.println("You have Deposited: $" + amount);

            // Create and log the deposit transaction
            System.out.println("Creating deposit transaction for account: " + this.accountID);
            Transaction depositTransaction = new Transaction(
                    "SYSTEM",           // source account (SYSTEM for deposits)
                    this.accountID,     // destination account
                    amount,            // amount
                    "DEPOSIT",         // transaction type
                    "Cash deposit"     // note
            );
            System.out.println("Deposit transaction created with ID: " + depositTransaction.getTransactionId());
            TransactionLogger.log(depositTransaction);

            // Update the account in CSV after deposit
            BankAccountCSV.updateAccount(this);
        }
        else {
            System.out.println("Invalid deposit amount. It has to be greater than 0");
        }
    }

    /**
     * Same things as Deposit but it will handle taking out money
     */
    public void withdraw(double amount) {
        if (amount > 0) {
            if (balance >= amount) {
                balance = balance - amount;
                System.out.println("You have withdrew: $" + amount);

                // Create and log the withdrawal transaction
                System.out.println("Creating withdrawal transaction for account: " + this.accountID);
                Transaction withdrawalTransaction = new Transaction(
                        this.accountID,  // source account
                        "SYSTEM",        // destination account (SYSTEM for withdrawals)
                        amount,         // amount
                        "WITHDRAWAL",   // transaction type
                        "Cash withdrawal" // note
                );
                System.out.println("Withdrawal transaction created with ID: " + withdrawalTransaction.getTransactionId());
                TransactionLogger.log(withdrawalTransaction);

                // Update the account in CSV after withdrawal
                BankAccountCSV.updateAccount(this);
            }
            else {
                System.out.println("Insufficient funds for withdrawal");
            }
        }
        else {
            System.out.println("Invalid withdrawal amount. It must be greater then 0.");
        }
    }

    /**
     * TRANSFER CLASS
     * this will handle transfering money between accounts
     * the parameters is a BankAccount object and then the amount
     * now if the amount is greater then 0 it will deposit it into the destination account
     * after that it will log it using the transfer class (creating a transfer object)
     * and then making sure you update both accounts in the CSV FILE
     */
    public boolean transfer(BankAccount destinationAccount, double amount) {
        if (amount > 0) {
            if (balance >= amount) {
                // Withdraw from this account
                balance = balance - amount;

                // Deposit to destination account
                destinationAccount.balance = destinationAccount.balance + amount;

                // Create and log the transfer transaction
                Transaction transferTransaction = new Transaction(
                        this.accountID,           // source account
                        destinationAccount.accountID, // destination account
                        amount,                  // amount
                        "TRANSFER",              // transaction type
                        "Transfer between accounts" // note
                );
                TransactionLogger.log(transferTransaction);

                // Update both accounts in CSV
                BankAccountCSV.updateAccount(this);
                BankAccountCSV.updateAccount(destinationAccount);

                return true;
            }
            else {
                System.out.println("Insufficient funds for transfer");
                return false;
            }
        }
        else {
            System.out.println("Invalid transfer amount. It must be greater than 0.");
            return false;
        }
    }

    /**
     * Deletes this account (Admin function)
     * @return true if successful, false otherwise
     */
    public boolean deleteAccount() {
        return BankAccountCSV.deleteAccount(this.accountID);
    }

    /**
     * Gets all accounts for a specific user
     * @param userID The ID of the user whose accounts to retrieve
     * @return List of BankAccount objects belonging to the user
     */
    public static List<BankAccount> getUserAccounts(int userID) {
        return BankAccountCSV.readUserAccounts(userID);
    }

    /**
     * Gets all accounts in the system (Admin function)
     * @return List of all BankAccount objects
     */
    public static List<BankAccount> getAllAccounts() {
        return BankAccountCSV.readAllAccountsAdmin();
    }
}

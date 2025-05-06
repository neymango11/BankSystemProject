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
    // Unique identifier for the account
    private String accountID;
    // Reference to the user who owns this account
    private int userID;
    // Current balance in the account
    private double balance;
    // Type of account (CHECKING or SAVING)
    private String accountType;
    // Annual Percentage Yield - interest rate for savings accounts
    private double apy;

    /**
     * Constructor to create a new bank account
     * @param accountId Unique identifier for the account
     * @param userID ID of the user who owns this account
     * @param balance Initial balance in the account
     * @param accountType Type of account (CHECKING or SAVING)
     */
    public BankAccount(String accountId, int userID, double balance, String accountType) {
        this.accountID = accountId;
        this.userID = userID;
        this.balance = balance;
        this.accountType = accountType.toUpperCase(); // Store account type in uppercase
        this.apy = calculateAPY(balance); // Calculate initial APY based on balance
    }

    /**
     * Calculates the Annual Percentage Yield (APY) based on the account balance
     * Different tiers of APY are offered based on the balance amount
     * @param balance Current balance in the account
     * @return APY rate as a decimal (e.g., 0.05 for 5%)
     */
    private double calculateAPY(double balance) {
        if (this.accountType.equals("SAVING")) {
            if (balance >= 10000) return 0.05;      // 5% APY for $10,000+
            if (balance >= 5000) return 0.03;       // 3% APY for $5,000+
            if (balance >= 1000) return 0.02;       // 2% APY for $1,000+
            return 0.01;                           // 1% APY for < $1,000
        }
        return 0.0; // No APY for checking accounts
    }

    /**
     * Updates the APY when the account balance changes
     * This ensures the correct interest rate is applied based on the current balance
     */
    private void updateAPY() {
        if (this.accountType.equals("SAVING")) {
            this.apy = calculateAPY(this.balance);
        }
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

    public double getAPY() {
        return apy;
    }

    /**
     * Creates a new checking account for a user
     * @param name Name of the account holder
     * @param userID ID of the user
     * @param initialDeposit Initial deposit amount
     * @return Newly created checking account
     */
    public static BankAccount createChecking(String name, int userID, double initialDeposit) {
        String accountID = name + "-C-" + userID;
        BankAccount account = new BankAccount(accountID, userID, initialDeposit, "CHECKING");
        BankAccountCSV.writeToCSV(account);
        return account;
    }

    /**
     * Creates a new savings account for a user
     * @param name Name of the account holder
     * @param userID ID of the user
     * @param initialDeposit Initial deposit amount
     * @return Newly created savings account
     */
    public static BankAccount createSavings(String name, int userID, double initialDeposit) {
        String accountID = name + "-S-" + userID;
        BankAccount account = new SavingsAccount(accountID, userID, initialDeposit);
        BankAccountCSV.writeToCSV(account);
        return account;
    }

    /**
     * Deposits money into the account
     * Creates a transaction record and updates the account balance
     * @param amount Amount to deposit
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

            // Update APY for savings accounts
            updateAPY();

            // Update the account in CSV after deposit
            BankAccountCSV.updateAccount(this);
        }
        else {
            System.out.println("Invalid deposit amount. It has to be greater than 0");
        }
    }

    /**
     * Withdraws money from the account
     * Creates a transaction record and updates the account balance
     * @param amount Amount to withdraw
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

                // Update APY for savings accounts
                updateAPY();

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
     * Transfers money from this account to another account
     * Creates a transaction record and updates both account balances
     * @param destinationAccount The account to transfer money to
     * @param amount The amount to transfer
     * @return true if transfer was successful, false otherwise
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

                // Update APY for both accounts if they are savings
                updateAPY();
                destinationAccount.updateAPY();

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

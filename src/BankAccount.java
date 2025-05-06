/*
* Aman Mohamed
* -------BankAccount Class------
* This class is in charge of creating Checking and Saving Accounts
* and it will tie it to there userID
* When creating there Savings and Checking they will be able to deposit money right there
*
* */

import java.util.List;

public class BankAccount {
    private String accountID;
    private int userID; // Reference from user class
    private double balance;
    private String accountType; // Stores checking or savings

    public BankAccount(String accountId, int userID, double balance, String accountType) {
        this.accountID = accountId;
        this.userID = userID;
        this.balance = balance;
        this.accountType = accountType.toUpperCase(); // method to store as a uppercase
        
        // Save the new account to CSV
        BankAccountCSV.writeToCSV(this);
    }

    //GETTERS
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

    // Creating the Checking Accounts
    // Will save them as name-C-1001
    public static BankAccount createChecking(String name, int userID, double initialDeposit) {
        String accountID = name + "-C-" + userID;
        return new BankAccount(accountID, userID, initialDeposit, "CHECKING");
    }

    // Creating the Saving Accounts
    // Will save them as name-S-1000
    public static BankAccount createSavings(String name, int userID, double initialDeposit) {
        String accountID = name + "-S-" + userID;
        return new BankAccount(accountID, userID, initialDeposit, "SAVING");
    }

    // Deposit Method
    // Checks if amount is greater then 0 if so it will add it to the balance
    public void deposit(double amount) {
        if (amount > 0) {
            balance = balance + amount;
            System.out.println("You have Deposited: $" + amount);
            
            // Create and log the deposit transaction
            Transaction depositTransaction = new Transaction(
                "SYSTEM",           // source account (SYSTEM for deposits)
                this.accountID,     // destination account
                amount,            // amount
                "DEPOSIT",         // transaction type
                "Cash deposit"     // note
            );
            TransactionLogger.log(depositTransaction);
            
            // Update the account in CSV after deposit
            BankAccountCSV.updateAccount(this);
        }
        else {
            System.out.println("Invalid deposit amount. It has to be greater than 0");
        }
    }

    // Withdrawal Method
    // Checks if the balance is greater then or equal to the amount if so it will extract it from the balance
    public void withdraw(double amount) {
        if (amount > 0) {
            if (balance >= amount) {
                balance = balance - amount;
                System.out.println("You have withdrew: $" + amount);
                
                // Create and log the withdrawal transaction
                Transaction withdrawalTransaction = new Transaction(
                    this.accountID,  // source account
                    "SYSTEM",        // destination account (SYSTEM for withdrawals)
                    amount,         // amount
                    "WITHDRAWAL",   // transaction type
                    "Cash withdrawal" // note
                );
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
     * Transfer money from this account to another account
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
     * Delete this account (Admin function)
     * @return true if successful, false otherwise
     */
    public boolean deleteAccount() {
        return BankAccountCSV.deleteAccount(this.accountID);
    }

    /**
     * Get all accounts for this user
     * @return List of BankAccount objects belonging to this user
     */
    public static List<BankAccount> getUserAccounts(int userID) {
        return BankAccountCSV.readUserAccounts(userID);
    }

    /**
     * Get all accounts in the system (Admin function)
     * @return List of all BankAccount objects
     */
    public static List<BankAccount> getAllAccounts() {
        return BankAccountCSV.readAllAccountsAdmin();
    }
}

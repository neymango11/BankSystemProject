/*
* Aman Mohamed
* -------BankAccount Class------
* This class is in charge of creating Checking and Saving Accounts
* and it will tie it to there userID
* When creating there Savings and Checking they will be able to deposit money right there
*
* */

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
            // In Progress to add a transaction object
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
                // In Progress to add a transaction object
            }
            else {
                System.out.println("Insufficient funds for withdrawal");
            }
        }
        else {
            System.out.println("Invalid withdrawal amount. It must be greater then 0.");
        }
    }

    // Method to add to CSV files

}

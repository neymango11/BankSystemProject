/*
* EXPLANATION OF THE CLASS
* Aman
* */

public class BankAccount {
    private int accountId;
    private User user; // creating a user object
    private double balance;
    private String accountType; // Stores checking or savings

    public BankAccount(int accountId, User user, double balance, String accountType) {
        this.accountId = accountId;
        this.user = user;
        this.balance = balance;
        this.accountType = accountType.toUpperCase(); // method to store as a uppercase
    }

    //GETTERS
    public int getAccountId() {
        return accountId;
    }


    public double getBalance() {
        return balance;
    }

    public String getAccountType(){
        return accountType;
    }

    //SETTERS
    public void setAccountId(int newAccountId){
        this.accountId = newAccountId;
    }

    public void setBalance(double newBalance) {
        this.balance = newBalance;
    }

    public void setAccountType(String newAccountType){
        this.accountType = newAccountType;
    }

    // Deposit Method
    public void deposit(double amount) {
        if (amount > 0) {
            balance = balance + amount;
            System.out.println("You have Deposited: $" + amount);
            //add a way to log it into csv
        }
        else {
            System.out.println("Invalid deposit amount. It has to be greater than 0");
        }
    }

    // Withdrawal Method
    public void withdraw(double amount) {
        if (amount > 0) {
            if (balance >= amount) {
                balance = balance - amount;
                System.out.println("You have withdrew: $" + amount);
                //add a way to log it into csv
            }
            else {
                System.out.println("Insufficient funds for withdrawal");
            }
        }
        else {
            System.out.println("Invalid withdrawal amount. It must be greater then 0.");
        }
    }

}

/*
* EXPLANATION OF THE CLASS
* VINNY
* */

public class BankAccount {
    private int accountId;
    private int userId;
    private double balance;
    private String accountType; // Stores checkings or savings

    public BankAccount(int accountId, int userId, double balance, String accountType) {
        this.accountId = accountId;
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

}

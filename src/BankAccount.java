/*
* EXPLANATION OF THE CLASS
* VINNY
* */

public class BankAccount {
    private int accountNumber;
    private double balance;
    private String accountType; // Stores checkings or savings

    public BankAccount(int accountNumber, double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType.toUpperCase(); // method to store as a uppercase
    }

    //GETTERS



    //SETTERS

}

/**
 * Aman Mohamed
 * SavingsAccount extends BankAccount, which also handles interest rate (APY)
 * It calculates the interest rate based on where they fall on the tier level
 * A method will automatically apply interest to the account balance
 *
 * Features:
 * - It will automatically determine interest rate based on the balance
 * - Applies daily interest to the balance using applyInterest()
 * - Inherits fields and methods from bankAccount
 * */

public class SavingsAccount extends BankAccount {
    private double interestRate;


    public SavingsAccount(int accountNumber, double balance) {
        super(accountNumber, balance, "SAVINGS");
        this.interestRate = calculateInterestRate(balance);
    }


    // Method to calculate interest rate based on their balance
    private double calculateInterestRate(double balance) {
        if (balance < 1000) {
            return 0.01;
        } else if (balance < 5000) {
            return 0.015;
        } else {
            return 0.02;
        }
    }

    // Method to apply interest to the account balance
    //public void applyInterest() {}


    // Method to display saving info


    // Method to display current interest rate


}

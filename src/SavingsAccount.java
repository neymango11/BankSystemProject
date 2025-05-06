// SavingsAccount.java
// Developed by Vincent Bayode
// This class represents a savings account and includes logic for applying interest
// based on APY tiers.

public class SavingsAccount extends BankAccount {

    // Stores the interest rate based on the account's balance
    private double interestRate;

    /**
     * Constructor for the SavingsAccount
     *
     * @param accountId  The unique ID of the account (e.g., "Vincent-S-1002")
     * @param userID     The ID of the user who owns the account
     * @param balance    The initial balance of the savings account
     */
    public SavingsAccount(String accountId, int userID, double balance) {
        // Call the constructor of BankAccount and pass in the correct values
        super(accountId, userID, balance, "SAVING");

        // Set the initial interest rate based on the starting balance
        this.interestRate = determineInterestRate(balance);
    }

    /**
     * Applies monthly interest to the account.
     * Uses the current balance to determine the rate and updates the account.
     */
    public void applyInterest() {
        // Recalculate the interest rate in case the balance changed
        interestRate = determineInterestRate(getBalance());

        // Convert APY to monthly rate
        double monthlyRate = interestRate / 12;

        // Calculate earned interest
        double interest = getBalance() * monthlyRate;

        // Add interest to balance using the deposit() method
        deposit(interest);
    }

    /**
     * Determines the correct APY based on balance.
     *
     * @param balance The current balance
     * @return The APY as a decimal (e.g., 0.01 = 1%)
     */
    private double determineInterestRate(double balance) {
        if (balance >= 5000) {
            return 0.015; // 1.5%
        } else if (balance >= 1000) {
            return 0.01;  // 1.0%
        } else {
            return 0.005; // 0.5%
        }
    }
}

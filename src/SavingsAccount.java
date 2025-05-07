// SavingsAccount.java
// Developed by Vincent Bayode
// This class represents a savings account and includes logic for applying interest
// based on APY tiers.

public class SavingsAccount extends BankAccount {

    // Stores the Annual Percentage Yield (APY) based on the account's balance
    private double apy;

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

        // Set the initial APY based on the starting balance
        this.apy = calculateAPY(balance);
    }

    /**
     * Gets the current APY for this savings account
     * @return The current APY as a decimal (e.g., 0.05 for 5%)
     */
    public double getAPY() {
        return apy;
    }

    /**
     * Applies monthly interest to the account.
     * Uses the current balance to determine the rate and updates the account.
     */
    public void applyInterest() {
        // Recalculate the APY in case the balance changed
        updateAPY();

        // Convert APY to monthly rate
        double monthlyRate = apy / 12;

        // Calculate earned interest
        double interest = getBalance() * monthlyRate;

        // Add interest to balance using the deposit() method
        deposit(interest);
    }

    /**
     * Calculates the Annual Percentage Yield (APY) based on the account balance
     * Different tiers of APY are offered based on the balance amount
     * @param balance Current balance in the account
     * @return APY rate as a decimal (e.g., 0.05 for 5%)
     */
    private double calculateAPY(double balance) {
        if (balance >= 10000) return 0.05;      // 5% APY for $10,000+
        if (balance >= 5000) return 0.03;       // 3% APY for $5,000+
        if (balance >= 1000) return 0.02;       // 2% APY for $1,000+
        return 0.01;                           // 1% APY for < $1,000
    }

    /**
     * Updates the APY when the account balance changes
     * This ensures the correct interest rate is applied based on the current balance
     */
    private void updateAPY() {
        this.apy = calculateAPY(getBalance());
    }

    @Override
    public void deposit(double amount) {
        super.deposit(amount);
        updateAPY();
    }

    @Override
    public void withdraw(double amount) {
        super.withdraw(amount);
        updateAPY();
    }

    @Override
    public boolean transfer(BankAccount destinationAccount, double amount) {
        boolean success = super.transfer(destinationAccount, amount);
        if (success) {
            updateAPY();
        }
        return success;
    }
}

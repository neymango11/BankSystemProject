
/*

 * Test class for BankAccount and BankAccountCSV
 * This class demonstrates the functionality of both classes
 */

public class BankAccountTest {
    public static void main(String[] args) {
        System.out.println("=== Starting Bank Account System Test ===\n");

        // Test 1: Create accounts
        System.out.println("Test 1: Creating accounts");
        BankAccount checkingAccount = BankAccount.createChecking("Aman", 1001, 500.0);
        BankAccount savingsAccount = BankAccount.createSavings("Aman", 1001, 1000.0);
        System.out.println("Created checking account: " + checkingAccount.getAccountID());
        System.out.println("Created savings account: " + savingsAccount.getAccountID());
        System.out.println();

        // Test 2: Test deposits
        System.out.println("Test 2: Testing deposits");
        System.out.println("Initial checking balance: $" + checkingAccount.getBalance());
        checkingAccount.deposit(100.0);
        System.out.println("After deposit balance: $" + checkingAccount.getBalance());
        System.out.println();

        // Test 3: Test withdrawals
        System.out.println("Test 3: Testing withdrawals");
        System.out.println("Initial savings balance: $" + savingsAccount.getBalance());
        savingsAccount.withdraw(200.0);
        System.out.println("After withdrawal balance: $" + savingsAccount.getBalance());
        System.out.println();

        // Test 4: Test invalid operations
        System.out.println("Test 4: Testing invalid operations");
        System.out.println("Attempting to withdraw more than balance:");
        savingsAccount.withdraw(10000.0);
        System.out.println("Attempting to deposit negative amount:");
        checkingAccount.deposit(-50.0);
        System.out.println();

        // Test 5: Test transfers
        System.out.println("Test 5: Testing transfers");
        System.out.println("Before transfer:");
        System.out.println("Checking balance: $" + checkingAccount.getBalance());
        System.out.println("Savings balance: $" + savingsAccount.getBalance());
        
        boolean transferSuccess = checkingAccount.transfer(savingsAccount, 150.0);
        System.out.println("Transfer successful: " + transferSuccess);
        
        System.out.println("After transfer:");
        System.out.println("Checking balance: $" + checkingAccount.getBalance());
        System.out.println("Savings balance: $" + savingsAccount.getBalance());
        System.out.println();

        // Test 6: Test reading accounts
        System.out.println("Test 6: Testing account reading");
        System.out.println("Reading all accounts for user 1001:");
        var userAccounts = BankAccount.getUserAccounts(1001);
        for (BankAccount account : userAccounts) {
            System.out.println("Account: " + account.getAccountID() + 
                             ", Type: " + account.getAccountType() + 
                             ", Balance: $" + account.getBalance());
        }
        System.out.println();

        // Test 7: Test admin functions
        System.out.println("Test 7: Testing admin functions");
        System.out.println("Reading all accounts in system:");
        var allAccounts = BankAccount.getAllAccounts();
        for (BankAccount account : allAccounts) {
            System.out.println("Account: " + account.getAccountID() + 
                             ", Type: " + account.getAccountType() + 
                             ", Balance: $" + account.getBalance());
        }
        System.out.println();

        // Test 8: Test account deletion
        System.out.println("Test 8: Testing account deletion");
        System.out.println("Deleting checking account...");
        boolean deleteSuccess = checkingAccount.deleteAccount();
        System.out.println("Account deletion successful: " + deleteSuccess);
        
        // Verify deletion by reading accounts again
        System.out.println("\nVerifying accounts after deletion:");
        userAccounts = BankAccount.getUserAccounts(1001);
        for (BankAccount account : userAccounts) {
            System.out.println("Account: " + account.getAccountID() + 
                             ", Type: " + account.getAccountType() + 
                             ", Balance: $" + account.getBalance());
        }

        System.out.println("\n=== Bank Account System Test Complete ===");
    }
} 
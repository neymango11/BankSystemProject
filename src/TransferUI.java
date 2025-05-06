import java.util.List;
import java.util.Scanner;

public class TransferUI {
    private static final double DAILY_TRANSFER_LIMIT = 10000.00;
    private static final double TRANSFER_FEE = 1.50;
    private static final double TRANSFER_FEE_THRESHOLD = 1000.00; // Fee applies to transfers above this amount

    private Scanner scanner;
    private User currentUser;

    public TransferUI(User currentUser) {
        this.scanner = new Scanner(System.in);
        this.currentUser = currentUser;
    }

    public void showTransferMenu() {
        while (true) {
            System.out.println("\n=== Transfer Menu ===");
            System.out.println("1. Make a Transfer");
            System.out.println("2. View Transfer History");
            System.out.println("3. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    initiateTransfer();
                    break;
                case 2:
                    viewTransferHistory();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void initiateTransfer() {
        // Get user's accounts
        List<BankAccount> userAccounts = BankAccountCSV.readUserAccounts(currentUser.getUserID());
        if (userAccounts.isEmpty()) {
            System.out.println("You don't have any accounts to transfer from.");
            return;
        }

        // Display user's accounts
        System.out.println("\nYour Accounts:");
        for (int i = 0; i < userAccounts.size(); i++) {
            BankAccount account = userAccounts.get(i);
            System.out.printf("%d. %s - $%.2f%n",
                    i + 1, account.getAccountID(), account.getBalance());
        }

        // Select source account
        System.out.print("Select source account (number): ");
        int sourceIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (sourceIndex < 0 || sourceIndex >= userAccounts.size()) {
            System.out.println("Invalid account selection.");
            return;
        }
        BankAccount sourceAccount = userAccounts.get(sourceIndex);

        // Get recipient account ID
        System.out.print("Enter recipient's account ID: ");
        String recipientAccountId = scanner.nextLine();

        // Find recipient account
        List<BankAccount> allAccounts = BankAccountCSV.readAllAccountsAdmin();
        BankAccount recipientAccount = allAccounts.stream()
                .filter(acc -> acc.getAccountID().equals(recipientAccountId))
                .findFirst()
                .orElse(null);

        if (recipientAccount == null) {
            System.out.println("Recipient account not found.");
            return;
        }

        // Get transfer amount
        System.out.print("Enter amount to transfer: $");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        // Check daily transfer limit
        if (amount > DAILY_TRANSFER_LIMIT) {
            System.out.printf("Transfer amount exceeds daily limit of $%.2f%n", DAILY_TRANSFER_LIMIT);
            return;
        }

        // Calculate total amount including fee if applicable
        double totalAmount = amount;
        if (amount > TRANSFER_FEE_THRESHOLD) {
            totalAmount += TRANSFER_FEE;
            System.out.printf("A fee of $%.2f will be applied to this transfer.%n", TRANSFER_FEE);
        }

        // Confirm transfer
        System.out.printf("\nTransfer Summary:%n");
        System.out.printf("From: %s%n", sourceAccount.getAccountID());
        System.out.printf("To: %s%n", recipientAccount.getAccountID());
        System.out.printf("Amount: $%.2f%n", amount);
        if (amount > TRANSFER_FEE_THRESHOLD) {
            System.out.printf("Fee: $%.2f%n", TRANSFER_FEE);
            System.out.printf("Total: $%.2f%n", totalAmount);
        }
        System.out.print("Confirm transfer? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            // Perform transfer
            boolean success = BankTransfer.transfer(sourceAccount, recipientAccount, totalAmount);
            if (success) {
                System.out.println("Transfer completed successfully!");
                // Update accounts in CSV
                BankAccountCSV.updateAccount(sourceAccount);
                BankAccountCSV.updateAccount(recipientAccount);
            }
        } else {
            System.out.println("Transfer cancelled.");
        }
    }

    private void viewTransferHistory() {
        System.out.println("\n=== Transfer History ===");
        List<Transaction> transactions = TransactionHistory.getUserTransactions(currentUser.getUserID());

        if (transactions.isEmpty()) {
            System.out.println("No transfer history found.");
            return;
        }

        for (Transaction transaction : transactions) {
            if (transaction.getType().equals("TRANSFER")) {
                System.out.printf("Date: %s%n", transaction.getTimestamp());
                System.out.printf("From: %s%n", transaction.getFromAccount());
                System.out.printf("To: %s%n", transaction.getToAccount());
                System.out.printf("Amount: $%.2f%n", transaction.getAmount());
                System.out.printf("Note: %s%n", transaction.getNote());
                System.out.println("-------------------");
            }
        }
    }
} 

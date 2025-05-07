import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * DemoMain class serves as the main entry point for the Bank Account System demo.
 * This class implements a command-line interface for users to interact with the banking system.
 * It handles user authentication, account management, and banking operations.
 */
public class DemoMain {
    /**
     * Main method that initializes and runs the banking system demo
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        List<User> users;

        // Load existing users from CSV file
        try {
            users = UserCSV.loadUsers();
        } catch (Exception e) {
            System.err.println("Critical error loading users: " + e.getMessage());
            System.err.println("Starting with empty user list");
            users = new ArrayList<>();
        }

        System.out.println("=== Bank Account System Demo ===");

        // Initialize admin user if not exists
        boolean adminExists = false;
        Admin admin = null;

        try {
            // Check if admin user exists
            for (User user : users) {
                if (user instanceof Admin) {
                    adminExists = true;
                    admin = (Admin) user;
                    break;
                }
            }

            // Create default admin if none exists
            if (!adminExists) {
                System.out.println("\nCreating admin user...");
                admin = new Admin("admin", "admin123", null);
                users.add(admin);
                if (!UserCSV.saveUsers(users)) {
                    System.err.println("Warning: Failed to save admin user to CSV");
                }
            }
        } catch (Exception e) {
            System.err.println("Critical error setting up admin: " + e.getMessage());
            System.exit(1);
        }

        // Main program loop
        while (running) {
            try {
                // Display main menu
                System.out.println("\n=== Main Menu ===");
                System.out.println("1. Create New User Account");
                System.out.println("2. User Login");
                System.out.println("3. Admin Login");
                System.out.println("4. Exit");
                System.out.print("Enter your choice (1-4): ");

                String input = scanner.nextLine().trim();
                int mainChoice;

                try {
                    mainChoice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid input. Please enter a number between 1 and 4.");
                    continue;
                }

                switch (mainChoice) {
                    case 1: // Create New User Account
                        try {
                            System.out.println("\n=== Create New User Account ===");
                            System.out.print("Enter username: ");
                            String username = scanner.nextLine().trim();

                            if (username.isEmpty()) {
                                System.err.println("Username cannot be empty");
                                break;
                            }

                            // Check if username already exists
                            for (User user : users) {
                                if (user.getUsername().equals(username)) {
                                    System.err.println("Username already exists");
                                    break;
                                }
                            }

                            System.out.print("Enter password: ");
                            String password = scanner.nextLine().trim();

                            if (password.isEmpty()) {
                                System.err.println("Password cannot be empty");
                                break;
                            }

                            // Create new user and save to CSV
                            User newUser = new User(username, password, "STANDARD", null);
                            users.add(newUser);

                            if (!UserCSV.saveUsers(users)) {
                                System.err.println("Warning: Failed to save new user to CSV");
                            }

                            System.out.println("User account created successfully!");

                            // Prompt for bank account creation
                            System.out.println("\n=== Create Bank Accounts ===");
                            System.out.println("1. Create Checking Account");
                            System.out.println("2. Create Savings Account");
                            System.out.println("3. Create Both Accounts");
                            System.out.print("Choose account type(s) (1-3): ");

                            String accountChoiceInput = scanner.nextLine().trim();
                            int accountChoice;

                            try {
                                accountChoice = Integer.parseInt(accountChoiceInput);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid input. Please enter a number between 1 and 3.");
                                break;
                            }

                            BankAccount checkingAccount = null;
                            BankAccount savingsAccount = null;

                            // Handle different account creation options
                            switch (accountChoice) {
                                case 1: // Create checking account only
                                    try {
                                        System.out.print("Enter initial deposit for checking account: $");
                                        String depositInput = scanner.nextLine().trim();
                                        double checkingDeposit = Double.parseDouble(depositInput);

                                        if (checkingDeposit < 0) {
                                            System.err.println("Deposit amount cannot be negative");
                                            break;
                                        }

                                        checkingAccount = BankAccount.createChecking(username, newUser.getUserID(), checkingDeposit);
                                        System.out.println("Checking account created with balance: $" + checkingDeposit);
                                    } catch (NumberFormatException e) {
                                        System.err.println("Invalid deposit amount");
                                    }
                                    break;

                                case 2: // Create savings account only
                                    try {
                                        System.out.print("Enter initial deposit for savings account: $");
                                        String depositInput = scanner.nextLine().trim();
                                        double savingsDeposit = Double.parseDouble(depositInput);

                                        if (savingsDeposit < 0) {
                                            System.err.println("Deposit amount cannot be negative");
                                            break;
                                        }

                                        savingsAccount = BankAccount.createSavings(username, newUser.getUserID(), savingsDeposit);
                                        System.out.println("Savings account created with balance: $" + savingsDeposit);
                                        System.out.println("Current APY: " + (savingsAccount.getAPY() * 100) + "%");
                                    } catch (NumberFormatException e) {
                                        System.err.println("Invalid deposit amount");
                                    }
                                    break;

                                case 3: // Create both checking and savings accounts
                                    try {
                                        System.out.print("Enter initial deposit for checking account: $");
                                        String checkDepositInput = scanner.nextLine().trim();
                                        double checkDeposit = Double.parseDouble(checkDepositInput);

                                        if (checkDeposit < 0) {
                                            System.err.println("Checking deposit amount cannot be negative");
                                            break;
                                        }

                                        System.out.print("Enter initial deposit for savings account: $");
                                        String saveDepositInput = scanner.nextLine().trim();
                                        double saveDeposit = Double.parseDouble(saveDepositInput);

                                        if (saveDeposit < 0) {
                                            System.err.println("Savings deposit amount cannot be negative");
                                            break;
                                        }

                                        checkingAccount = BankAccount.createChecking(username, newUser.getUserID(), checkDeposit);
                                        savingsAccount = BankAccount.createSavings(username, newUser.getUserID(), saveDeposit);

                                        System.out.println("Checking account created with balance: $" + checkDeposit);
                                        System.out.println("Savings account created with balance: $" + saveDeposit);
                                        System.out.println("Current APY: " + (savingsAccount.getAPY() * 100) + "%");
                                    } catch (NumberFormatException e) {
                                        System.err.println("Invalid deposit amount");
                                    }
                                    break;

                                default:
                                    System.err.println("Invalid account choice");
                            }
                        } catch (Exception e) {
                            System.err.println("Error creating user account: " + e.getMessage());
                        }
                        break;

                    case 2: // User Login
                        try {
                            System.out.println("\n=== User Login ===");
                            System.out.print("Enter username: ");
                            String loginUsername = scanner.nextLine().trim();
                            System.out.print("Enter password: ");
                            String loginPassword = scanner.nextLine().trim();

                            if (loginUsername.isEmpty() || loginPassword.isEmpty()) {
                                System.err.println("Username and password cannot be empty");
                                break;
                            }

                            // Authenticate user
                            User loggedInUser = null;
                            for (User user : users) {
                                if (user.getUsername().equals(loginUsername) &&
                                        user.getPassword().equals(loginPassword)) {
                                    loggedInUser = user;
                                    break;
                                }
                            }

                            if (loggedInUser != null) {
                                System.out.println("Login successful!");
                                handleUserOperations(loggedInUser, scanner);
                            } else {
                                System.err.println("❌ Invalid username or password.");
                            }
                        } catch (Exception e) {
                            System.err.println("Error during login: " + e.getMessage());
                        }
                        break;

                    case 3: // Admin Login
                        try {
                            System.out.println("\n=== Admin Login ===");
                            System.out.print("Enter admin username: ");
                            String adminUsername = scanner.nextLine().trim();
                            System.out.print("Enter admin password: ");
                            String adminPassword = scanner.nextLine().trim();

                            if (adminUsername.isEmpty() || adminPassword.isEmpty()) {
                                System.err.println("Username and password cannot be empty");
                                break;
                            }

                            // Authenticate admin
                            if (admin != null &&
                                    adminUsername.equals(admin.getUsername()) &&
                                    adminPassword.equals(admin.getPassword())) {
                                System.out.println("✅ Admin login successful!");
                                handleAdminOperations(admin, users, scanner);
                            } else {
                                System.err.println("❌ Invalid admin credentials.");
                            }
                        } catch (Exception e) {
                            System.err.println("Error during admin login: " + e.getMessage());
                        }
                        break;

                    case 4: // Exit
                        running = false;
                        System.out.println("Thank you for using the Bank Account System. Goodbye!");
                        break;

                    default:
                        System.err.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }

    /**
     * Handles user operations after successful login
     * @param user The logged-in user
     * @param scanner Scanner object for user input
     */
    private static void handleUserOperations(User user, Scanner scanner) {
        boolean userRunning = true;

        while (userRunning) {
            System.out.println("\n=== User Operations ===");
            System.out.println("1. View Account Balances");
            System.out.println("2. Make a Deposit");
            System.out.println("3. Make a Withdrawal");
            System.out.println("4. Transfer Money");
            System.out.println("5. View Transaction History");
            System.out.println("6. Change Password");
            System.out.println("7. Logout");
            System.out.print("Enter your choice (1-7): ");

            String input = scanner.nextLine().trim();
            int userChoice;

            try {
                userChoice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                continue;
            }

            switch (userChoice) {
                case 1:
                    displayAccountBalances(user);
                    break;
                case 2:
                    handleDeposit(user, scanner);
                    break;
                case 3:
                    handleWithdrawal(user, scanner);
                    break;
                case 4:
                    handleTransfer(user, scanner);
                    break;
                case 5:
                    handleTransactionHistory(user);
                    break;
                case 6:
                    handlePasswordChange(user, scanner);
                    break;
                case 7:
                    userRunning = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("\nInvalid choice. Please enter a number between 1 and 7.");
            }
        }
    }

    /**
     * Displays account balances for a user
     * @param user The user whose balances to display
     */
    private static void displayAccountBalances(User user) {
        List<BankAccount> userAccounts = BankAccount.getUserAccounts(user.getUserID());
        System.out.println("\n=== Account Balances ===");
        if (userAccounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            // Use a Set to track displayed account IDs to prevent duplicates
            java.util.Set<String> displayedAccounts = new java.util.HashSet<>();
            for (BankAccount account : userAccounts) {
                // Only display each account once
                if (displayedAccounts.add(account.getAccountID())) {
                    System.out.println(account.getAccountType() + " Account: $" + account.getBalance());
                    if (account.getAccountType().equals("SAVING")) {
                        System.out.println("Current APY: " + (account.getAPY() * 100) + "%");
                    }
                }
            }
        }
    }

    /**
     * Handles deposit operations for a user
     * @param user The user making the deposit
     * @param scanner Scanner object for user input
     */
    private static void handleDeposit(User user, Scanner scanner) {
        List<BankAccount> userAccounts = BankAccount.getUserAccounts(user.getUserID());
        if (userAccounts.isEmpty()) {
            System.out.println("No accounts available for deposit.");
            return;
        }
        System.out.println("\n=== Make a Deposit ===");
        for (int i = 0; i < userAccounts.size(); i++) {
            System.out.println((i + 1) + ". " + userAccounts.get(i).getAccountType() +
                    " Account - Balance: $" + userAccounts.get(i).getBalance());
        }
        System.out.print("Choose account (1-" + userAccounts.size() + "): ");
        int depositAccount = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter amount to deposit: $");
        double depositAmount = Double.parseDouble(scanner.nextLine().trim());

        BankAccount selectedAccount = userAccounts.get(depositAccount - 1);
        selectedAccount.deposit(depositAmount);
        
        if (selectedAccount.getAccountType().equals("SAVING")) {
            System.out.println("Updated APY: " +
                    (selectedAccount.getAPY() * 100) + "%");
        }
    }

    /**
     * Handles withdrawal operations for a user
     * @param user The user making the withdrawal
     * @param scanner Scanner object for user input
     */
    private static void handleWithdrawal(User user, Scanner scanner) {
        List<BankAccount> userAccounts = BankAccount.getUserAccounts(user.getUserID());
        if (userAccounts.isEmpty()) {
            System.out.println("No accounts available for withdrawal.");
            return;
        }
        System.out.println("\n=== Make a Withdrawal ===");
        for (int i = 0; i < userAccounts.size(); i++) {
            System.out.println((i + 1) + ". " + userAccounts.get(i).getAccountType() +
                    " Account - Balance: $" + userAccounts.get(i).getBalance());
        }
        System.out.print("Choose account (1-" + userAccounts.size() + "): ");
        int withdrawAccount = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter amount to withdraw: $");
        double withdrawAmount = Double.parseDouble(scanner.nextLine().trim());

        BankAccount selectedAccount = userAccounts.get(withdrawAccount - 1);
        selectedAccount.withdraw(withdrawAmount);
        
        if (selectedAccount.getAccountType().equals("SAVING")) {
            System.out.println("Updated APY: " +
                    (selectedAccount.getAPY() * 100) + "%");
        }
    }

    /**
     * Handles transfer operations between accounts
     * @param user The user making the transfer
     * @param scanner Scanner object for user input
     */
    private static void handleTransfer(User user, Scanner scanner) {
        List<BankAccount> userAccounts = BankAccount.getUserAccounts(user.getUserID());
        if (userAccounts.isEmpty()) {
            System.out.println("❌ You need at least one account to transfer money.");
            return;
        }

        System.out.println("\n=== Transfer Money ===");
        System.out.println("1. Transfer to my other account");
        System.out.println("2. Transfer to another user");
        System.out.print("Choose transfer type (1-2): ");

        int transferType;
        try {
            transferType = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter 1 or 2.");
            return;
        }

        // Display source accounts
        System.out.println("\nYour accounts:");
        for (int i = 0; i < userAccounts.size(); i++) {
            System.out.println((i + 1) + ". " + userAccounts.get(i).getAccountType() +
                    " Account - Balance: $" + userAccounts.get(i).getBalance());
        }
        System.out.print("Choose source account (1-" + userAccounts.size() + "): ");

        int fromAccount;
        try {
            fromAccount = Integer.parseInt(scanner.nextLine().trim());
            if (fromAccount < 1 || fromAccount > userAccounts.size()) {
                System.out.println("Invalid account selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        BankAccount sourceAccount = userAccounts.get(fromAccount - 1);
        BankAccount destinationAccount = null;

        if (transferType == 1) {
            // Transfer between own accounts
            if (userAccounts.size() < 2) {
                System.out.println("❌ You need at least two accounts to transfer between your own accounts.");
                return;
            }

            System.out.println("\nChoose destination account:");
            for (int i = 0; i < userAccounts.size(); i++) {
                if (i != fromAccount - 1) {
                    System.out.println((i + 1) + ". " + userAccounts.get(i).getAccountType() +
                            " Account - Balance: $" + userAccounts.get(i).getBalance());
                }
            }

            int toAccount;
            try {
                toAccount = Integer.parseInt(scanner.nextLine().trim());
                if (toAccount < 1 || toAccount > userAccounts.size() || toAccount == fromAccount) {
                    System.out.println("Invalid account selection.");
                    return;
                }
                destinationAccount = userAccounts.get(toAccount - 1);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                return;
            }
        } else {
            // Transfer to another user
            System.out.print("\nEnter recipient's username: ");
            String recipientUsername = scanner.nextLine().trim();

            // Find recipient's accounts
            List<BankAccount> allAccounts = BankAccount.getAllAccounts();
            List<BankAccount> recipientAccounts = new ArrayList<>();

            // First find the recipient user
            User recipient = null;
            List<User> allUsers = UserCSV.loadUsers();
            for (User u : allUsers) {
                if (u.getUsername().equalsIgnoreCase(recipientUsername)) {
                    recipient = u;
                    break;
                }
            }

            if (recipient == null) {
                System.out.println("❌ User not found: " + recipientUsername);
                return;
            }

            // Now find all accounts for this recipient
            for (BankAccount account : allAccounts) {
                if (account.getUserID() == recipient.getUserID()) {
                    recipientAccounts.add(account);
                }
            }

            if (recipientAccounts.isEmpty()) {
                System.out.println("❌ No accounts found for user: " + recipientUsername);
                return;
            }

            System.out.println("\nRecipient's accounts:");
            for (int i = 0; i < recipientAccounts.size(); i++) {
                System.out.println((i + 1) + ". " + recipientAccounts.get(i).getAccountType() + " Account");
            }

            int toAccount;
            try {
                toAccount = Integer.parseInt(scanner.nextLine().trim());
                if (toAccount < 1 || toAccount > recipientAccounts.size()) {
                    System.out.println("Invalid account selection.");
                    return;
                }
                destinationAccount = recipientAccounts.get(toAccount - 1);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                return;
            }
        }

        System.out.print("Enter amount to transfer: $");
        double transferAmount;
        try {
            transferAmount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
            return;
        }

        boolean success = sourceAccount.transfer(destinationAccount, transferAmount);
        if (success) {
            System.out.println("✅ Transfer successful!");
            System.out.println("New balance in your account: $" + sourceAccount.getBalance());

            // Update APY display for savings accounts
            if (sourceAccount.getAccountType().equals("SAVING")) {
                System.out.println("Your savings account APY: " + (sourceAccount.getAPY() * 100) + "%");
            }
        }
    }

    /**
     * Finds a user by their ID
     * @param userID The ID to search for
     * @return The User object if found, null otherwise
     */
    private static User findUserByID(int userID) {
        List<User> users = UserCSV.loadUsers();
        for (User user : users) {
            if (user.getUserID() == userID) {
                return user;
            }
        }
        return null;
    }

    /**
     * Handles password change operations for a user
     * @param user The user changing their password
     * @param scanner Scanner object for user input
     */
    private static void handlePasswordChange(User user, Scanner scanner) {
        System.out.println("\n=== Change Password ===");
        System.out.print("Enter current password: ");
        String currentPass = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPass = scanner.nextLine();
        user.setPassword(currentPass, newPass);
    }

    /**
     * Displays transaction history for a user
     * @param user The user whose history to display
     */
    private static void handleTransactionHistory(User user) {
        System.out.println("\n=== Transaction History ===");
        List<Transaction> transactions = TransactionHistory.getUserTransactions(user.getUserID());

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (Transaction transaction : transactions) {
            System.out.println("\nTransaction ID: " + transaction.getTransactionId());
            System.out.println("Date: " + transaction.getTimestamp());
            System.out.println("Type: " + transaction.getType());
            System.out.println("Amount: $" + transaction.getAmount());
            System.out.println("From: " + transaction.getFromAccount());
            System.out.println("To: " + transaction.getToAccount());
            System.out.println("Note: " + transaction.getNote());
            System.out.println("-------------------");
        }
    }

    /**
     * Handles admin operations after successful login
     * @param admin The logged-in admin
     * @param users List of all users in the system
     * @param scanner Scanner object for user input
     */
    private static void handleAdminOperations(Admin admin, List<User> users, Scanner scanner) {
        boolean adminRunning = true;

        while (adminRunning) {
            System.out.println("\n=== Admin Operations ===");
            System.out.println("1. View All Users");
            System.out.println("2. Create New User");
            System.out.println("3. Remove User");
            System.out.println("4. Reset User Password");
            System.out.println("5. View All Accounts");
            System.out.println("6. Admin Deposit");
            System.out.println("7. Admin Withdrawal");
            System.out.println("8. View All Transactions");
            System.out.println("9. Logout");
            System.out.print("Enter your choice (1-9): ");

            int adminChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (adminChoice) {
                case 1:
                    admin.viewAllUsers(users);
                    break;

                case 2:
                    System.out.print("Enter new username: ");
                    String newUsername = scanner.nextLine();
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    User newUser = admin.createUser(newUsername, newPassword, "STANDARD", null);
                    users.add(newUser);
                    // Save updated users list to CSV
                    UserCSV.saveUsers(users);
                    System.out.println("✅ Created new user: " + newUsername);
                    break;

                case 3:
                    System.out.print("Enter user ID to remove: ");
                    int removeId = scanner.nextInt();
                    admin.removeUserById(users, removeId);
                    break;

                case 4:
                    System.out.print("Enter user ID to reset password: ");
                    int resetId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new password: ");
                    String resetPass = scanner.nextLine();
                    for (User user : users) {
                        if (user.getUserID() == resetId) {
                            admin.resetUserPassword(user, resetPass);
                            break;
                        }
                    }
                    break;

                case 5:
                    System.out.println("\n=== All Accounts ===");
                    List<BankAccount> allAccounts = BankAccount.getAllAccounts();
                    for (BankAccount account : allAccounts) {
                        System.out.println("Account: " + account.getAccountID() +
                                ", Type: " + account.getAccountType() +
                                ", Balance: $" + account.getBalance());
                        if (account.getAccountType().equals("SAVING")) {
                            System.out.println("APY: " + (account.getAPY() * 100) + "%");
                        }
                    }
                    break;

                case 6:
                    System.out.println("\n=== Admin Deposit ===");
                    allAccounts = BankAccount.getAllAccounts();
                    for (int i = 0; i < allAccounts.size(); i++) {
                        System.out.println((i + 1) + ". " + allAccounts.get(i).getAccountID() +
                                " - Balance: $" + allAccounts.get(i).getBalance());
                    }
                    System.out.print("Choose account (1-" + allAccounts.size() + "): ");
                    int adminDepositAccount = scanner.nextInt();
                    System.out.print("Enter amount to deposit: $");
                    double adminDepositAmount = scanner.nextDouble();

                    admin.depositToUserAccount(allAccounts.get(adminDepositAccount - 1), adminDepositAmount);
                    if (allAccounts.get(adminDepositAccount - 1).getAccountType().equals("SAVING")) {
                        System.out.println("Updated APY: " +
                                (allAccounts.get(adminDepositAccount - 1).getAPY() * 100) + "%");
                    }
                    break;

                case 7:
                    System.out.println("\n=== Admin Withdrawal ===");
                    allAccounts = BankAccount.getAllAccounts();
                    for (int i = 0; i < allAccounts.size(); i++) {
                        System.out.println((i + 1) + ". " + allAccounts.get(i).getAccountID() +
                                " - Balance: $" + allAccounts.get(i).getBalance());
                    }
                    System.out.print("Choose account (1-" + allAccounts.size() + "): ");
                    int adminWithdrawAccount = scanner.nextInt();
                    System.out.print("Enter amount to withdraw: $");
                    double adminWithdrawAmount = scanner.nextDouble();

                    admin.withdrawFromUserAccount(allAccounts.get(adminWithdrawAccount - 1), adminWithdrawAmount);
                    if (allAccounts.get(adminWithdrawAccount - 1).getAccountType().equals("SAVING")) {
                        System.out.println("Updated APY: " +
                                (allAccounts.get(adminWithdrawAccount - 1).getAPY() * 100) + "%");
                    }
                    break;

                case 8:
                    handleAdminTransactionHistory();
                    break;

                case 9:
                    adminRunning = false;
                    System.out.println("Logged out successfully.");
                    break;

                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays transaction history for admin view
     */
    private static void handleAdminTransactionHistory() {
        System.out.println("\n=== All Transactions ===");
        List<Transaction> transactions = TransactionHistory.getAllTransactions();

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        for (Transaction transaction : transactions) {
            System.out.println("\nTransaction ID: " + transaction.getTransactionId());
            System.out.println("Date: " + transaction.getTimestamp());
            System.out.println("Type: " + transaction.getType());
            System.out.println("Amount: $" + transaction.getAmount());
            System.out.println("From: " + transaction.getFromAccount());
            System.out.println("To: " + transaction.getToAccount());
            System.out.println("Note: " + transaction.getNote());
            System.out.println("-------------------");
        }
    }
}

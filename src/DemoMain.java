import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DemoMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        List<User> users;
        
        try {
            users = UserCSV.loadUsers();
        } catch (Exception e) {
            System.err.println("Critical error loading users: " + e.getMessage());
            System.err.println("Starting with empty user list");
            users = new ArrayList<>();
        }

        System.out.println("=== Bank Account System Demo ===");
        
        // Create admin if not exists
        boolean adminExists = false;
        Admin admin = null;
        
        try {
            for (User user : users) {
                if (user instanceof Admin) {
                    adminExists = true;
                    admin = (Admin) user;
                    break;
                }
            }
            
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

        while (running) {
            try {
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
                            
                            User newUser = new User(username, password, "STANDARD", null);
                            users.add(newUser);
                            
                            if (!UserCSV.saveUsers(users)) {
                                System.err.println("Warning: Failed to save new user to CSV");
                            }
                            
                            System.out.println("User account created successfully!");
                            
                            // Create bank accounts for new user
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
                            
                            switch (accountChoice) {
                                case 1:
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
                                    
                                case 2:
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
                                    
                                case 3:
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
                                System.err.println("Invalid username or password.");
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
                            
                            if (admin != null && 
                                adminUsername.equals(admin.getUsername()) && 
                                adminPassword.equals(admin.getPassword())) {
                                System.out.println("Admin login successful!");
                                handleAdminOperations(admin, users, scanner);
                            } else {
                                System.err.println("Invalid admin credentials.");
                            }
                        } catch (Exception e) {
                            System.err.println("Error during admin login: " + e.getMessage());
                        }
                        break;

                    case 4:
                        running = false;
                        System.out.println("\nThank you for using the Bank Account System!");
                        break;

                    default:
                        System.err.println("\nInvalid choice. Please enter a number between 1 and 4.");
                }
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        scanner.close();
    }

    private static void handleUserOperations(User user, Scanner scanner) {
        boolean userRunning = true;
        
        while (userRunning) {
            System.out.println("\n=== User Operations ===");
            System.out.println("1. View Account Balances");
            System.out.println("2. Make a Deposit");
            System.out.println("3. Make a Withdrawal");
            System.out.println("4. Transfer Money");
            System.out.println("5. Change Password");
            System.out.println("6. Logout");
            System.out.print("Enter your choice (1-6): ");
            
            String input = scanner.nextLine().trim();
            int userChoice;
            
            try {
                userChoice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 6.");
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
                    handlePasswordChange(user, scanner);
                    break;
                case 6:
                    userRunning = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("\nInvalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

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
        
        userAccounts.get(depositAccount - 1).deposit(depositAmount);
        if (userAccounts.get(depositAccount - 1).getAccountType().equals("SAVING")) {
            System.out.println("Updated APY: " + 
                (userAccounts.get(depositAccount - 1).getAPY() * 100) + "%");
        }
    }

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
        
        userAccounts.get(withdrawAccount - 1).withdraw(withdrawAmount);
        if (userAccounts.get(withdrawAccount - 1).getAccountType().equals("SAVING")) {
            System.out.println("Updated APY: " + 
                (userAccounts.get(withdrawAccount - 1).getAPY() * 100) + "%");
        }
    }

    private static void handleTransfer(User user, Scanner scanner) {
        List<BankAccount> userAccounts = BankAccount.getUserAccounts(user.getUserID());
        if (userAccounts.size() < 2) {
            System.out.println("You need at least two accounts to transfer money.");
            return;
        }
        System.out.println("\n=== Transfer Money ===");
        for (int i = 0; i < userAccounts.size(); i++) {
            System.out.println((i + 1) + ". " + userAccounts.get(i).getAccountType() + 
                             " Account - Balance: $" + userAccounts.get(i).getBalance());
        }
        System.out.print("Choose source account (1-" + userAccounts.size() + "): ");
        int fromAccount = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Choose destination account (1-" + userAccounts.size() + "): ");
        int toAccount = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter amount to transfer: $");
        double transferAmount = Double.parseDouble(scanner.nextLine().trim());
        
        userAccounts.get(fromAccount - 1).transfer(userAccounts.get(toAccount - 1), transferAmount);
        
        // Update APY display for savings accounts
        for (BankAccount account : userAccounts) {
            if (account.getAccountType().equals("SAVING")) {
                System.out.println(account.getAccountType() + " Account APY: " + 
                    (account.getAPY() * 100) + "%");
            }
        }
    }

    private static void handlePasswordChange(User user, Scanner scanner) {
        System.out.println("\n=== Change Password ===");
        System.out.print("Enter current password: ");
        String currentPass = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPass = scanner.nextLine();
        user.setPassword(currentPass, newPass);
    }

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
            System.out.println("8. Logout");
            System.out.print("Enter your choice (1-8): ");
            
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
                    System.out.println("Created new user: " + newUsername);
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
                    adminRunning = false;
                    System.out.println("Logged out successfully.");
                    break;

                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }
    }
}

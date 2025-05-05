import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Map<String, User> users = new HashMap<>();
    static Map<Integer, BankAccount> accounts = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Welcome to the Bank System");

        // Admin preload
        users.put("admin", new Admin("admin", "admin123", 9999));

        while (true) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    loginMenu();
                    break;
                case "2":
                    createUser();
                    break;
                case "3":
                    System.out.println("Thanks for using the Bank System. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void loginMenu() {
        System.out.print("Username: ");
        String uname = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        if (users.containsKey(uname) && users.get(uname).getPassword().equals(pass)) {
            User currentUser = users.get(uname);
            System.out.println("Welcome, " + currentUser.getUsername() + "!");

            if (currentUser instanceof Admin) {
                adminMenu((Admin) currentUser);
            } else {
                userMenu(currentUser);
            }
        } else {
            System.out.println("Login failed. Invalid username or password.");
        }
    }

    private static void createUser() {
        System.out.print("Enter username: ");
        String uname = scanner.nextLine();
        System.out.print("Enter password: ");
        String pass = scanner.nextLine();

        int userID = new Random().nextInt(9000) + 1000;
        User newUser = new User(uname, pass, "STANDARD USER", userID);
        users.put(uname, newUser);
        System.out.println("✅ Account created for " + uname + " with user ID: " + userID);

        // Optional savings account creation
        System.out.print("Would you like to open a savings account now? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Initial deposit: ");
            double deposit = Double.parseDouble(scanner.nextLine());
            BankAccount acc = BankAccount.createSavings(uname, userID, deposit);
            accounts.put(userID, acc);
            System.out.println("✅ Savings account created with ID: " + acc.getAccountID());
        }
    }

    private static void userMenu(User user) {
        while (true) {
            System.out.println("\n===== USER MENU =====");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. View Transaction History");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();

            BankAccount account = accounts.get(user.getUserID());
            if (account == null) {
                System.out.println("⚠️ No account found for this user.");
                return;
            }

            switch (choice) {
                case "1":
                    System.out.print("Amount to deposit: ");
                    double deposit = Double.parseDouble(scanner.nextLine());
                    account.deposit(deposit);
                    TransactionLogger.log(new Transaction(account.getAccountID(), "-", deposit, "DEPOSIT", "User deposit"));
                    break;
                case "2":
                    System.out.print("Amount to withdraw: ");
                    double withdraw = Double.parseDouble(scanner.nextLine());
                    account.withdraw(withdraw);
                    TransactionLogger.log(new Transaction(account.getAccountID(), "-", withdraw, "WITHDRAW", "User withdrawal"));
                    break;
                case "3":
                    TransactionHistory.viewByAccount(account.getAccountID());
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. View All Users");
            System.out.println("2. View Account Balance");
            System.out.println("3. Deposit to User");
            System.out.println("4. Withdraw from User");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    admin.viewAllUsers(new ArrayList<>(users.values()));
                    break;
                case "2":
                    BankAccount account = getAccountByPrompt();
                    if (account != null) admin.viewAccountBalance(account);
                    break;
                case "3":
                    account = getAccountByPrompt();
                    if (account != null) {
                        System.out.print("Amount to deposit: ");
                        double amount = Double.parseDouble(scanner.nextLine());
                        admin.depositToUserAccount(account, amount);
                        TransactionLogger.log(new Transaction("ADMIN", account.getAccountID(), amount, "ADMIN DEPOSIT", "Admin deposited"));
                    }
                    break;
                case "4":
                    account = getAccountByPrompt();
                    if (account != null) {
                        System.out.print("Amount to withdraw: ");
                        double amount = Double.parseDouble(scanner.nextLine());
                        admin.withdrawFromUserAccount(account, amount);
                        TransactionLogger.log(new Transaction(account.getAccountID(), "ADMIN", amount, "ADMIN WITHDRAW", "Admin withdrew"));
                    }
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static BankAccount getAccountByPrompt() {
        System.out.print("Enter User ID of the account: ");
        try {
            int userId = Integer.parseInt(scanner.nextLine());
            if (accounts.containsKey(userId)) {
                return accounts.get(userId);
            } else {
                System.out.println("⚠️ Account not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Invalid input.");
        }
        return null;
    }
}


























/**
 * 1. Login or Create an account
 * -------------------------------
 *             MENU
 *  LOGIN
 *  CREATE ACCOUNT
 *  ------------------------------
 * Create Account will use the user class.
 * User user1 = new User("aman", "password123", "STANDARD USER", 1000);
 *---------------------------------
 * Login will also use USER CLASS / CSV file to confirm if username / password is in the file
 *
 *             USER MENU
 *  ----------------------------------------------------------------------------------------
 *  CREATING A BANK ACCOUNT
 *      BankAccount checking = BankAccount.createChecking(user.1.getUsername(), user1.getUserID(), 500.0);
 *
 *  DEPOSIT // Will use the BankAccount Class / Will log transaction by using BankAccountCSVS
 *      checking.deposit(200.0);
 *      Output: "You have Deposited: $200.0"
 *
 *  Withdraw // Will use the BankAccount Class / Will log transaction by using BankAccountCSV
 *      checking.withdraw(100.0);
 *      Output: "You have withdrew: $100.0"
 *

 *  View Transaction (not avaliable right now)
 *  Transfer (not avaliable right now)
 *  Change Username // Will use the User Class
 *  Change Password // Will use the User Class
 *  Log Out
 *
 *            ADMIN MENU
 * ----------------------------------------------------------------------------
 *  View all users
 *  View all accounts (checkings, savings)
 *  View all transactions (not avaliable)
 *  Create new users
 *  Delete a user or Account
 *  Deposit
 *  Withdraw
 *  Transfer (not avaliable)
 *  Log Out
 * */

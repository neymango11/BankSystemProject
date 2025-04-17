import java.util.*;

// read user input, store and manage user info, store and manage bank acc info

public class Main {
    static Scanner scanner = new Scanner (System.in);
    static Map<String, User> users = new HashMap<>();
    static Map<Integer, BankAccount> accounts = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Welcome to the Bank System");

        //admin preload
        users.put("admin", new User("admin", "admin123", "ADMIN", 9999));

        while (true) {
            // Main loop for showing the main menu repeatedly
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");
            System.out.println("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    loginMenu();  // If user chooses 1, go to login menu
                    break;
                case "2":
                    createUser(); // If user chooses 2, go to create user method
                    break;
                case "3":
                    System.out.println("Thanks for using the Bank System. Goodbye!");
                    return; // Ends the program
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void loginMenu() {
        System.out.print("Username: ");
        String uname = scanner.nextLine();
        // Ask for username
        System.out.print("Password: ");
        String pass = scanner.nextLine();
        // Ask for password

        // Check if user exists and password matches
        if (users.containsKey(uname) && users.get(uname).getPassword().equals(pass)) {
            User currentUser = users.get(uname);
            System.out.println("Welcome, " + currentUser.getUsername() + "!");

            if (currentUser.getRole().equalsIgnoreCase("ADMIN")) {
                //adminMenu(currentUser);
            } else {
                //userMenu(currentUser);
            }
        } else {
            System.out.println("Login failed. Invalid username or password.");
            // Login failed message

        }
    }
    private static void createUser() {
        System.out.print("Enter username: ");
        String uname = scanner.nextLine();
        // Get a new username from user
        System.out.print("Enter password: ");
        String pass = scanner.nextLine();
        // Get a new password from user

        int userID = new Random().nextInt(9000) + 1000;
        // Generate a random user ID between 1000 and 9999
        User newUser = new User(uname, pass, "STANDARD USER", userID);
        users.put(uname, newUser);
        // Create a new User object and add it to the users map
        System.out.println("Account created for " + uname + "wither user ID: " + userID);
        // Notify user about successful account creation (typo: "wither" should be "with")\


        // Ask if user wants to open a savings account immediately
        System.out.print("Would you like to open a savings account now? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Initial deposit: ");
            double deposit = Double.parseDouble(scanner.nextLine());
            // Read initial deposit as a double
            BankAccount acc = BankAccount.createSavings(uname, userID, deposit);
            accounts.put(userID, acc);
            // Create savings account and store it in the accounts map
            System.out.println();
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


    }
}


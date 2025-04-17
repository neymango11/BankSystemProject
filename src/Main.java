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
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");
            System.out.println("Enter your choice: ");
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

            if (currentUser.getRole().equalsIgnoreCase("ADMIN")) {
                //adminMenu(currentUser);
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
        System.out.println("Account created for " + uname + "wither user ID: " + userID);

        //or, create an acocunt right away
        System.out.print("Would you like to open a savings account now? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Initial deposit: ");
            double depoist = Double.parseDouble()
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


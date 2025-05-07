import java.util.List;

/**
 * The Admin class extends the User class to provide admin functionality in the banking system.
 * It allows admins to manage user accounts, including creating, removing, and modifying user details.
 * The class contains methods for:
 * - Creating new users: The createUser method allows the admin to create a new user account.
 * - Removing users: The removeUserById method removes a user from the system based on their userID.
 * - Managing user credentials:
 *   - resetUserPassword method allows admins to reset a user's password.
 *   - resetUserUsername method enables admins to change a user's username.
 * - Viewing users: The viewAllUsers method displays a list of all users in the system.
 *
 * Additionally, admins can manage user bank accounts with the following methods:
 * - depositToUserAccount: Allows admins to deposit funds into a user's bank account.
 * - withdrawFromUserAccount: Allows admins to withdraw funds from a user's bank account.
 * - viewAccountBalance: Displays the current balance of a user's bank account.
 */

public class Admin extends User {

    public Admin(String username, String password, Integer userID) {
        super(username, password, "ADMIN", userID);  // Calls the parent constructor with "ADMIN" role
    }

    public User createUser(String username, String password, String role, Integer userID) {
        return new User(username, password, role, userID);  // Creates a new user with the provided details
    }

    public void removeUserById(List<User> userList, int targetUserID) {
        userList.removeIf(user -> user.getUserID() == targetUserID);  // Removes the user with the specified ID
        System.out.println("Removed user with ID " + targetUserID);
    }

    public void resetUserPassword(User user, String newPassword) {
        user.resetPassword(newPassword);  // Resets the user's password to the new one
    }

    public void resetUserUsername(User user, String newUsername) {
        user.forceSetUsername(newUsername);  // Changes the user's username
        System.out.println("Changed username to: " + newUsername);
    }

    public void viewAllUsers(List<User> userList) {
        for (User user : userList) {
            System.out.println("ID: " + user.getUserID() +  // Displays each user's information
                    ", Username: " + user.getUsername() +
                    ", Role: " + user.getRole());
        }
    }

    public void depositToUserAccount(BankAccount account, double amount) {
        account.deposit(amount);  // Deposits money into the specified account
        System.out.println("Admin deposited $" + amount + " to account " + account.getAccountID());
    }

    public void withdrawFromUserAccount(BankAccount account, double amount) {
        account.withdraw(amount);  // Withdraws money from the specified account
        System.out.println("Admin withdrew $" + amount + " from account " + account.getAccountID());
    }

    public void viewAccountBalance(BankAccount account) {
        System.out.println("Account " + account.getAccountID() + " balance: $" + account.getBalance());  // Displays the account balance
    }
}

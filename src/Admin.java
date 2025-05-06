import java.util.List;

/**
 * Admin class extends User to provide administrative functionality.
 * Admins have additional privileges to manage users and accounts in the banking system.
 * They can create users, remove users, reset passwords, and manage bank accounts.
 */
public class Admin extends User {

    /**
     * Creates a new admin user
     * @param username Admin's login name
     * @param password Admin's password
     * @param userID Optional user ID. If null, a new ID will be generated
     */
    public Admin(String username, String password, Integer userID) {
        super(username, password, "ADMIN", userID);
    }

    /**
     * Creates a new standard user in the system
     * @param username New user's login name
     * @param password New user's password
     * @param role New user's role
     * @param userID Optional user ID. If null, a new ID will be generated
     * @return The newly created User object
     */
    public User createUser(String username, String password, String role, Integer userID) {
        return new User(username, password, role, userID);
    }

    /**
     * Removes a user from the system by their ID
     * @param userList List of all users in the system
     * @param targetUserID ID of the user to remove
     */
    public void removeUserById(List<User> userList, int targetUserID) {
        userList.removeIf(user -> user.getUserID() == targetUserID);
        System.out.println("✅ Removed user with ID " + targetUserID);
    }

    /**
     * Resets a user's password to a new value
     * @param user The user whose password to reset
     * @param newPassword The new password to set
     */
    public void resetUserPassword(User user, String newPassword) {
        user.resetPassword(newPassword);
    }

    /**
     * Changes a user's username to a new value
     * @param user The user whose username to change
     * @param newUsername The new username to set
     */
    public void resetUserUsername(User user, String newUsername) {
        user.forceSetUsername(newUsername);
        System.out.println("✅ Changed username to: " + newUsername);
    }

    /**
     * Displays information about all users in the system
     * @param userList List of all users to display
     */
    public void viewAllUsers(List<User> userList) {
        for (User user : userList) {
            System.out.println("👤 ID: " + user.getUserID() +
                    ", Username: " + user.getUsername() +
                    ", Role: " + user.getRole());
        }
    }

    /**
     * Deposits money into any user's bank account
     * @param account The account to deposit into
     * @param amount The amount to deposit
     */
    public void depositToUserAccount(BankAccount account, double amount) {
        account.deposit(amount);
        System.out.println("✅ Admin deposited $" + amount + " to account " + account.getAccountID());
    }

    /**
     * Withdraws money from any user's bank account
     * @param account The account to withdraw from
     * @param amount The amount to withdraw
     */
    public void withdrawFromUserAccount(BankAccount account, double amount) {
        account.withdraw(amount);
        System.out.println("✅ Admin withdrew $" + amount + " from account " + account.getAccountID());
    }

    /**
     * Displays the current balance of a bank account
     * @param account The account to check
     */
    public void viewAccountBalance(BankAccount account) {
        System.out.println("💰 Account " + account.getAccountID() + " balance: $" + account.getBalance());
    }
}

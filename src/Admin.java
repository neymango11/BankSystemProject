import java.util.List;

// Admin inherits User and can manage users and accounts
public class Admin extends User {

    public Admin(String username, String password, Integer userID) {
        super(username, password, "ADMIN", userID);
    }

    // Create a new user
    public User createUser(String username, String password, String role, Integer userID) {
        return new User(username, password, role, userID);
    }

    // Remove user by ID
    public void removeUserById(List<User> userList, int targetUserID) {
        userList.removeIf(user -> user.getUserID() == targetUserID);
        System.out.println("✅ Removed user with ID " + targetUserID);
    }

    // Admin resets password
    public void resetUserPassword(User user, String newPassword) {
        user.resetPassword(newPassword);
    }

    // Admin changes username
    public void resetUserUsername(User user, String newUsername) {
        user.forceSetUsername(newUsername);
        System.out.println("✅ Changed username to: " + newUsername);
    }

    // View all users
    public void viewAllUsers(List<User> userList) {
        for (User user : userList) {
            System.out.println("👤 ID: " + user.getUserID() +
                    ", Username: " + user.getUsername() +
                    ", Role: " + user.getRole());
        }
    }

    // Admin deposits into any user's bank account
    public void depositToUserAccount(BankAccount account, double amount) {
        account.deposit(amount);
        System.out.println("✅ Admin deposited $" + amount + " to account " + account.getAccountID());
    }

    // Admin withdraws from any user's bank account
    public void withdrawFromUserAccount(BankAccount account, double amount) {
        account.withdraw(amount);
        System.out.println("✅ Admin withdrew $" + amount + " from account " + account.getAccountID());
    }

    // View account balance
    public void viewAccountBalance(BankAccount account) {
        System.out.println("💰 Account " + account.getAccountID() + " balance: $" + account.getBalance());
    }
}

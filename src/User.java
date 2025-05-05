// Represents a basic user (not admin) of the banking system
public class User {
    private String username;
    private String password;
    private String role;
    private int userID;

    private static int idCounter = 1000;

    public User(String username, String password, String role, Integer userID) {
        this.username = username;
        this.password = password;
        this.role = "STANDARD USER";
        this.userID = idCounter++;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public int getUserID() { return userID; }

    // User changes their own password
    public void setPassword(String currentPassword, String newPassword) {
        if (!this.password.equals(currentPassword)) {
            System.out.println("❌ Incorrect current password.");
        } else if (newPassword.equals(this.password)) {
            System.out.println("❌ New password must be different.");
        } else {
            this.password = newPassword;
            System.out.println("✅ Password changed successfully.");
        }
    }

    // Admin resets password
    public void resetPassword(String newPassword) {
        this.password = newPassword;
        System.out.println("✅ Admin reset the password.");
    }

    // Admin changes username
    void forceSetUsername(String newUsername) {
        this.username = newUsername;
    }
}

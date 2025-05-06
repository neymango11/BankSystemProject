/**
 * Represents a basic user (not admin) of the banking system.
 * This class handles user authentication and basic user management functionality.
 * Each user has a unique ID, username, password, and role.
 */
public class User {
    // User's login name
    private String username;
    // User's password for authentication
    private String password;
    // User's role in the system (e.g., "STANDARD USER")
    private String role;
    // Unique identifier for the user
    private int userID;

    // Counter to generate unique user IDs
    private static int idCounter = 1000;

    /**
     * Creates a new user with the specified credentials
     * @param username The user's login name
     * @param password The user's password
     * @param role The user's role (will be set to "STANDARD USER")
     * @param userID Optional user ID. If null, a new ID will be generated
     */
    public User(String username, String password, String role, Integer userID) {
        this.username = username;
        this.password = password;
        this.role = "STANDARD USER";
        if (userID != null) {
            this.userID = userID;
            // Update idCounter if this userID is higher
            if (userID >= idCounter) {
                idCounter = userID + 1;
            }
        } else {
            this.userID = idCounter++;
        }
    }

    // Standard getter methods
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public int getUserID() { return userID; }

    /**
     * Allows a user to change their own password
     * @param currentPassword The user's current password for verification
     * @param newPassword The new password to set
     */
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

    /**
     * Allows an admin to reset a user's password
     * @param newPassword The new password to set
     */
    public void resetPassword(String newPassword) {
        this.password = newPassword;
        System.out.println("✅ Admin reset the password.");
    }

    /**
     * Allows an admin to change a user's username
     * @param newUsername The new username to set
     */
    void forceSetUsername(String newUsername) {
        this.username = newUsername;
    }
}

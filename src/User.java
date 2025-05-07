/**
 * User class is essentail for managing users accounts and permissions
 * The User class represents a user in the banking system
 * Contains user details:
 * username: The login name for the user
 * password: The user's authentication password
 * role: The role assigned to the user (e.g., "STANDARD USER")
 * userID: A unique identifier for each user
 *
 * This class provides functionality to:
 * Get the user's details through getter methods (username, password, role, and userID)
 * Change a user's password through the setPassword method, ensuring the current password is correct and the new one is different.
 * Allows admins to:
 * Reset the user's password using the resetPassword method.
 * To change the username using the forceSetUsername method.
 */


public class User {
    private String username;  // User's login name
    private String password;  // User's password for authentication
    private String role;      // User's role in the system (e.g., "STANDARD USER")
    private int userID;       // Unique identifier for the user

    private static int idCounter = 1000;  // Counter to generate unique user IDs

    public User(String username, String password, String role, Integer userID) {
        this.username = username;
        this.password = password;
        this.role = "STANDARD USER";  // Default role is "STANDARD USER"
        if (userID != null) {
            this.userID = userID;  // Use provided userID if given
            if (userID >= idCounter) {  // Ensure idCounter is updated if the provided userID is larger
                idCounter = userID + 1;
            }
        } else {
            this.userID = idCounter++;  // Generate new user ID if not provided
        }
    }

    public String getUsername() { return username; }  // Return username
    public String getPassword() { return password; }  // Return password
    public String getRole() { return role; }          // Return role
    public int getUserID() { return userID; }          // Return userID

    public void setPassword(String currentPassword, String newPassword) {
        if (!this.password.equals(currentPassword)) {  // Check if current password is correct
            System.out.println("Incorrect current password.");
        } else if (newPassword.equals(this.password)) {  // Ensure new password is different from the old one
            System.out.println("New password must be different.");
        } else {
            this.password = newPassword;  // Set new password
            System.out.println("Password changed successfully.");
        }
    }

    public void resetPassword(String newPassword) {
        this.password = newPassword;  // Admin resets password
        System.out.println("Admin reset the password.");
    }

    void forceSetUsername(String newUsername) {
        this.username = newUsername; // Admin changes username
    }
}

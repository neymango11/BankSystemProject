
public class User {
    private String username;
    private String password;
    private String role;
    private int userID;

    // Constructors & parameters
   public User(String username, String password, String role, Integer userID) {
        this.username = username; // users login name
        this.password = password; // users password
        this.role = "STANDARD USER"; // the users role (STANDARD USER or ADMIN)
        this.userID = userID; // a unique identifier for this user
    }

    // Getter
    public String getUsername() {
       return username; // retrieving username
    }
    public String getPassword() {
        return password; // retrieving password
    }
    public String getRole(){
       return role; // retrieving role
    }
    public int getUserID(){
       return userID; // retrieving userID
    }


    // Setter - User will be able to reset their own user/pass
    public void setPassword(String newPassword) {
        if (newPassword.equals(this.password)) {
            System.out.println("Please choose a different password.");
        } else {
            this.password = newPassword;
        }
    }


    // Setter method to reset username


    // Method to create account
    public static User newUser(String username, String password, String role, Integer userID) {
       return new User(username, password, role, userID);
    }


    // Method to reset username



    // Method to reset password
}



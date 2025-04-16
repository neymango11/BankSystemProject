public class User {
    private String username;
    private String password;
    private String role;
    private int userID;

   public User(String username, String password, String role, Integer userID) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.userID = userID;
    }

    //Getters
    public String getUsername() {
       return username;
    }
    public String getPassword() {
        return password;
    }
    public String getRole(){
       return role;
    }
    public int getUserID(){
       return userID;
    }
    
    // Method to create account
    public static User newUser(String username, String password, String role, Integer userID) {
       return new User(username, password, role, userID);
    }







    // Method to reset username







    // Method to reset password
}



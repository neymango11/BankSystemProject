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

    //Setters
    public void setUsername(String newUsername){
       this.username = newUsername;
    }
    public void setPassword(String newPassword){
       this.password = newPassword;
    }
    public void setRole (String newRole){
       this.role = newRole;
    }
    public void setUserID(int newUserID){
       this.userID = newUserID;
    }

}

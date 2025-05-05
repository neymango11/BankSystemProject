import java.io.*;
import java.util.*;

public class UserCSV {

    // Load users from CSV
    public static List<User> loadUsers(String filename) {
        List<User> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    String role = parts[2].trim();
                    int userID = Integer.parseInt(parts[3].trim());

                    if (role.equalsIgnoreCase("ADMIN")) {
                        users.add(new Admin(username, password, userID));
                    } else {
                        users.add(new User(username, password, role, userID));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading users from CSV: " + e.getMessage());
        }

        return users;
    }

    // Save users to CSV
    public static void saveUsers(String filename, List<User> users) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("username,password,role,userID\n");
            for (User user : users) {
                writer.write(user.getUsername() + "," +
                        user.getPassword() + "," +
                        user.getRole() + "," +
                        user.getUserID() + "\n");
            }
            System.out.println("✅ Users saved to " + filename);
        } catch (IOException e) {
            System.out.println("❌ Error saving users to CSV: " + e.getMessage());
        }
    }
}

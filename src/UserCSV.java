import java.io.*;
import java.util.*;

public class UserCSV {
    private static final String CSV_FILE_PATH = "data/users.csv";

    // Load users from CSV
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();

        try {
            // Create data directory if it doesn't exist
            File dataDir = new File("data");
            if (!dataDir.exists() && !dataDir.mkdirs()) {
                throw new IOException("Failed to create data directory");
            }

            File file = new File(CSV_FILE_PATH);
            if (!file.exists()) {
                System.out.println("No existing users file found. Will create new one when first user is added.");
                return users;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                boolean skipHeader = true;
                int lineNumber = 0;

                while ((line = br.readLine()) != null) {
                    lineNumber++;
                    if (skipHeader) {
                        skipHeader = false;
                        continue;
                    }

                    try {
                        String[] parts = line.split(",");
                        if (parts.length != 4) {
                            System.err.println("Warning: Invalid data format at line " + lineNumber + ". Skipping.");
                            continue;
                        }

                        String username = parts[0].trim();
                        String password = parts[1].trim();
                        String role = parts[2].trim();

                        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                            System.err.println("Warning: Empty fields at line " + lineNumber + ". Skipping.");
                            continue;
                        }

                        int userID;
                        try {
                            userID = Integer.parseInt(parts[3].trim());
                        } catch (NumberFormatException e) {
                            System.err.println("Warning: Invalid userID at line " + lineNumber + ". Skipping.");
                            continue;
                        }

                        if (role.equalsIgnoreCase("ADMIN")) {
                            users.add(new Admin(username, password, userID));
                        } else {
                            users.add(new User(username, password, role, userID));
                        }
                    } catch (Exception e) {
                        System.err.println("Warning: Error processing line " + lineNumber + ": " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Critical error loading users from CSV: " + e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    // Save users to CSV
    public static boolean saveUsers(List<User> users) {
        if (users == null) {
            System.err.println("❌ Error: Cannot save null users list");
            return false;
        }

        try {
            // Create data directory if it doesn't exist
            File dataDir = new File("data");
            if (!dataDir.exists() && !dataDir.mkdirs()) {
                throw new IOException("Failed to create data directory");
            }

            // Create backup of existing file if it exists
            File file = new File(CSV_FILE_PATH);
            if (file.exists()) {
                File backup = new File(CSV_FILE_PATH + ".backup");
                if (backup.exists()) {
                    backup.delete();
                }
                if (!file.renameTo(backup)) {
                    System.err.println("Warning: Could not create backup of users file");
                }
            }

            // Write new file
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("username,password,role,userID\n");
                for (User user : users) {
                    if (user == null) {
                        System.err.println("Warning: Skipping null user in save operation");
                        continue;
                    }

                    String username = user.getUsername();
                    String password = user.getPassword();
                    String role = user.getRole();
                    int userID = user.getUserID();

                    if (username == null || password == null || role == null) {
                        System.err.println("Warning: Skipping user with null fields");
                        continue;
                    }

                    writer.write(String.format("%s,%s,%s,%d%n",
                            username.replace(",", ";"), // Prevent CSV injection
                            password.replace(",", ";"),
                            role.replace(",", ";"),
                            userID));
                }
                System.out.println("✅ Users saved to " + CSV_FILE_PATH);
                return true;
            }
        } catch (IOException e) {
            System.err.println("❌ Critical error saving users to CSV: " + e.getMessage());
            e.printStackTrace();

            // Try to restore from backup
            try {
                File backup = new File(CSV_FILE_PATH + ".backup");
                if (backup.exists()) {
                    File current = new File(CSV_FILE_PATH);
                    if (current.exists()) {
                        current.delete();
                    }
                    backup.renameTo(current);
                    System.out.println("Restored users file from backup");
                }
            } catch (Exception restoreError) {
                System.err.println("Failed to restore from backup: " + restoreError.getMessage());
            }
            return false;
        }
    }
}

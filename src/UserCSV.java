import java.io.*;
import java.util.*;

/**
 * The UserCSV class raeds and writes to the users.csv file which stores all information for users/loading users.
 * The CSV file stores user information in the format: username,password,role,userID.
 * Methods:
 * Loads users from the CSV file, creates a data directory if it doesn't exist, and handles any file errors.
 * Saves the provided list of users to the CSV file, creates a backup of the file before saving.
 * It also ensures the following:
 * - Handles directory and file creation if they don't exist.
 * - Validates the data read from the CSV file to ensure proper format.
 * - Provides error handling and backup creation if the save operation fails.
 */

public class UserCSV {
    private static final String CSV_FILE_PATH = "data/users.csv";  // Path to the CSV file storing user data

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();  // List to store loaded users

        try {
            File dataDir = new File("data");
            if (!dataDir.exists() && !dataDir.mkdirs()) {
                throw new IOException("Failed to create data directory");  // Ensure data directory exists
            }

            File file = new File(CSV_FILE_PATH);
            if (!file.exists()) {
                System.out.println("No existing users file found. Will create new one when first user is added.");
                return users;  // Return empty list if no users file is found
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                boolean skipHeader = true;  // Flag to skip CSV header line
                int lineNumber = 0;  // Track line number for debugging

                while ((line = br.readLine()) != null) {
                    lineNumber++;  // Increment line counter
                    if (skipHeader) {
                        skipHeader = false;
                        continue;  // Skip the first line (header)
                    }

                    try {
                        String[] parts = line.split(",");  // Split the line into CSV columns
                        if (parts.length != 4) {
                            System.err.println("Warning: Invalid data format at line " + lineNumber + ". Skipping.");
                            continue;  // Skip lines with invalid format
                        }

                        String username = parts[0].trim();  // Extract username
                        String password = parts[1].trim();  // Extract password
                        String role = parts[2].trim();  // Extract role

                        // Skip users with empty fields
                        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                            System.err.println("Warning: Empty fields at line " + lineNumber + ". Skipping.");
                            continue;
                        }

                        int userID;
                        try {
                            userID = Integer.parseInt(parts[3].trim());  // Parse user ID
                        } catch (NumberFormatException e) {
                            System.err.println("Warning: Invalid userID at line " + lineNumber + ". Skipping.");
                            continue;  // Skip invalid user ID
                        }

                        if (role.equalsIgnoreCase("ADMIN")) {
                            users.add(new Admin(username, password, userID));  // Add admin user
                        } else {
                            users.add(new User(username, password, role, userID));  // Add regular user
                        }
                    } catch (Exception e) {
                        System.err.println("Warning: Error processing line " + lineNumber + ": " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Critical error loading users from CSV: " + e.getMessage());
            e.printStackTrace();  // Print error stack trace for debugging
        }

        return users;  // Return the list of loaded users
    }

    public static boolean saveUsers(List<User> users) {
        if (users == null) {
            System.err.println("Error: Cannot save null users list");
            return false;  // Return false if the users list is null
        }

        try {
            File dataDir = new File("data");
            if (!dataDir.exists() && !dataDir.mkdirs()) {
                throw new IOException("Failed to create data directory");  // Ensure the directory exists
            }

            File file = new File(CSV_FILE_PATH);
            if (file.exists()) {
                File backup = new File(CSV_FILE_PATH + ".backup");
                if (backup.exists()) {
                    backup.delete();  // Delete old backup if exists
                }
                if (!file.renameTo(backup)) {
                    System.err.println("Warning: Could not create backup of users file");
                }
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write("username,password,role,userID\n");  // Write the header for CSV

                for (User user : users) {
                    if (user == null) {
                        System.err.println("Warning: Skipping null user in save operation");
                        continue;  // Skip null users
                    }

                    String username = user.getUsername();
                    String password = user.getPassword();
                    String role = user.getRole();
                    int userID = user.getUserID();

                    // Skip users with null fields
                    if (username == null || password == null || role == null) {
                        System.err.println("Warning: Skipping user with null fields");
                        continue;  // Skip users with missing data
                    }

                    // Prevent CSV injection by replacing commas
                    writer.write(String.format("%s,%s,%s,%d%n",
                            username.replace(",", ";"),  // Replace commas with semicolons
                            password.replace(",", ";"),
                            role.replace(",", ";"),
                            userID));
                }
                System.out.println("Users saved to " + CSV_FILE_PATH);  // Confirm successful save
                return true;  // Return true if save is successful
            }
        } catch (IOException e) {
            System.err.println("Critical error saving users to CSV: " + e.getMessage());
            e.printStackTrace();  // Print error stack trace for debugging

            try {
                File backup = new File(CSV_FILE_PATH + ".backup");
                if (backup.exists()) {
                    File current = new File(CSV_FILE_PATH);
                    if (current.exists()) {
                        current.delete();  // Delete current file if saving fails
                    }
                    backup.renameTo(current);  // Restore from backup
                    System.out.println("Restored users file from backup");
                }
            } catch (Exception restoreError) {
                System.err.println("Failed to restore from backup: " + restoreError.getMessage());
            }
            return false;  // Return false if save fails
        }
    }
}

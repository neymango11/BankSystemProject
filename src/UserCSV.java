import java.io.*; //input and output
import java.util.*; //utility classes (lists and array lists)

// Main class for handling both loading and saving users to a CSV file
public class UserCSV {
    // Defines the path of the CSV file relative to the project root
    private static final String CSV_FILE_PATH = "data/users.csv";

    // Method to load users from the CSV file and return a list of User objects
    public static List<User> loadUsers() {
        // Create an empty list to store users
        List<User> users = new ArrayList<>();

        try {
            // Create a File object for the "data" directory
            File dataDir = new File("data");
            // Check if the directory does not exist and try to create it
            if (!dataDir.exists() && !dataDir.mkdirs()) {
                // Throw an IOException if the directory cannot be created
                throw new IOException("Failed to create data directory");
            }

            // Create a File object for the CSV file
            File file = new File(CSV_FILE_PATH);
            // Check if the CSV file does not exist
            if (!file.exists()) {
                // Print a message if the file does not exist
                System.out.println("No existing users file found. Will create new one when first user is added.");
                // Return the empty users list
                return users;
            }

            // Open the CSV file for reading using BufferedReader
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                // Declare a variable to hold each line read from the file
                String line;
                // Boolean flag to skip the header line
                boolean skipHeader = true;
                // Counter for the current line number
                int lineNumber = 0;

                // Loop through each line in the CSV file
                while ((line = br.readLine()) != null) {
                    // Increment the line number
                    lineNumber++;
                    // If the header line should be skipped
                    if (skipHeader) {
                        // Set skipHeader to false after skipping
                        skipHeader = false;
                        // Continue to the next iteration
                        continue;
                    }

                    try {
                        // Split the line by commas into an array
                        String[] parts = line.split(",");
                        // Check if the line does not have exactly 4 parts
                        if (parts.length != 4) {
                            // Print a warning for invalid data format
                            System.err.println("Warning: Invalid data format at line " + lineNumber + ". Skipping.");
                            // Skip this line
                            continue;
                        }

                        // Trim and assign each value from the CSV line
                        String username = parts[0].trim();
                        String password = parts[1].trim();
                        String role = parts[2].trim();

                        // Check if any required field is empty
                        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                            // Print a warning for empty fields
                            System.err.println("Warning: Empty fields at line " + lineNumber + ". Skipping.");
                            // Skip this line
                            continue;
                        }

                        // Declare a variable for userID
                        int userID;
                        try {
                            // Parse the userID from the fourth part
                            userID = Integer.parseInt(parts[3].trim());
                        } catch (NumberFormatException e) {
                            // Print a warning for invalid userID
                            System.err.println("Warning: Invalid userID at line " + lineNumber + ". Skipping.");
                            // Skip this line
                            continue;
                        }

                        // Check if the role is ADMIN (case-insensitive)
                        if (role.equalsIgnoreCase("ADMIN")) {
                            // Add a new Admin object to the users list
                            users.add(new Admin(username, password, userID));
                        } else {
                            // Add a new User object to the users list
                            users.add(new User(username, password, role, userID));
                        }
                    } catch (Exception e) {
                        // Print a warning if an error occurs while processing the line
                        System.err.println("Warning: Error processing line " + lineNumber + ": " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            // Print a critical error if loading users fails
            System.err.println("Critical error loading users from CSV: " + e.getMessage());
            // Print the stack trace for debugging
            e.printStackTrace();
        }

        // Return the loaded users list
        return users;
    }

    // Method to save a list of users back to the CSV file
    public static boolean saveUsers(List<User> users) {
        // Check if the users list is null
        if (users == null) {
            // Print an error if the users list is null
            System.err.println("Error: Cannot save null users list");
            // Return false to indicate failure
            return false;
        }

        try {
            // Create a File object for the "data" directory
            File dataDir = new File("data");
            // Check if the directory does not exist and try to create it
            if (!dataDir.exists() && !dataDir.mkdirs()) {
                // Throw an IOException if the directory cannot be created
                throw new IOException("Failed to create data directory");
            }

            // Create a File object for the CSV file
            File file = new File(CSV_FILE_PATH);
            // Check if the file exists
            if (file.exists()) {
                // Create a File object for the backup file
                File backup = new File(CSV_FILE_PATH + ".backup");
                // Check if the backup file exists
                if (backup.exists()) {
                    // Delete the existing backup file
                    backup.delete();
                }
                // Try to rename the current file to the backup file
                if (!file.renameTo(backup)) {
                    // Print a warning if the backup could not be created
                    System.err.println("Warning: Could not create backup of users file");
                }
            }

            // Open the CSV file for writing using FileWriter
            try (FileWriter writer = new FileWriter(file)) {
                // Write the CSV header line
                writer.write("username,password,role,userID\n");
                // Loop through each user in the users list
                for (User user : users) {
                    // Check if the user is null
                    if (user == null) {
                        // Print a warning for null user
                        System.err.println("Warning: Skipping null user in save operation");
                        // Skip this user
                        continue;
                    }

                    // Get the username from the user object
                    String username = user.getUsername();
                    // Get the password from the user object
                    String password = user.getPassword();
                    // Get the role from the user object
                    String role = user.getRole();
                    // Get the userID from the user object
                    int userID = user.getUserID();

                    // Check if any user field is null
                    if (username == null || password == null || role == null) {
                        // Print a warning for null fields
                        System.err.println("Warning: Skipping user with null fields");
                        // Skip this user
                        continue;
                    }

                    // Write the user data to the file, replacing commas to avoid CSV corruption
                    writer.write(String.format("%s,%s,%s,%d%n",
                            username.replace(",", ";"),
                            password.replace(",", ";"),
                            role.replace(",", ";"),
                            userID));
                }
                // Print a message indicating users were saved
                System.out.println("Users saved to " + CSV_FILE_PATH);
                // Return true to indicate success
                return true;
            }
        } catch (IOException e) {
            // Print a critical error if saving users fails
            System.err.println("Critical error saving users to CSV: " + e.getMessage());
            // Print the stack trace for debugging
            e.printStackTrace();

            // Try to restore from backup if writing fails
            try {
                // Create a File object for the backup file
                File backup = new File(CSV_FILE_PATH + ".backup");
                // Check if the backup file exists
                if (backup.exists()) {
                    // Create a File object for the current file
                    File current = new File(CSV_FILE_PATH);
                    // Check if the current file exists
                    if (current.exists()) {
                        // Delete the current file
                        current.delete();
                    }
                    // Rename the backup file to the current file
                    backup.renameTo(current);
                    // Print a message indicating restoration from backup
                    System.out.println("Restored users file from backup");
                }
            } catch (Exception restoreError) {
                // Print a warning if restoration from backup fails
                System.err.println("Failed to restore from backup: " + restoreError.getMessage());
            }
            // Return false to indicate failure
            return false;
        }
    }
}

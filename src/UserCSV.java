import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.*;

public class UserCSV {
    private static final String CSV_FILE_PATH = "data/users.csv";

    /**
     * Loads all users from the CSV file using OpenCSV
     * @return List of User objects loaded from the file
     */
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

            // OpenCSV reader setup
            try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
                String[] line;
                boolean skipHeader = true;

                while ((line = csvReader.readNext()) != null) {
                    if (skipHeader) {
                        skipHeader = false;
                        continue; // Skip header row
                    }

                    // Parse user data from CSV row
                    String username = line[0].trim();
                    String password = line[1].trim();
                    String role = line[2].trim();
                    int userID = Integer.parseInt(line[3].trim());

                    // Add to list based on role
                    if (role.equalsIgnoreCase("ADMIN")) {
                        users.add(new Admin(username, password, userID));
                    } else {
                        users.add(new User(username, password, role, userID));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error loading users from CSV: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Saves all users to the CSV file using OpenCSV
     * @param users List of User objects to save
     * @return true if save was successful, false otherwise
     */
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

            File file = new File(CSV_FILE_PATH);
            // Create backup of existing file if it exists
            if (file.exists()) {
                File backup = new File(CSV_FILE_PATH + ".backup");
                if (backup.exists()) {
                    backup.delete();
                }
                if (!file.renameTo(backup)) {
                    System.err.println("Warning: Could not create backup of users file");
                }
            }

            // OpenCSV writer setup
            try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
                // Write the CSV header
                writer.writeNext(new String[]{"username", "password", "role", "userID"});

                // Write each user to the CSV
                for (User user : users) {
                    if (user == null) {
                        System.err.println("Warning: Skipping null user in save operation");
                        continue;
                    }
                    writer.writeNext(new String[]{
                            user.getUsername(),
                            user.getPassword(),
                            user.getRole(),
                            String.valueOf(user.getUserID())
                    });
                }
                System.out.println("✅ Users saved to " + CSV_FILE_PATH);
                return true;
            }
        } catch (IOException e) {
            System.err.println("❌ Error saving users to CSV: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

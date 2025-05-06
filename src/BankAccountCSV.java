import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankAccountCSV {
    private static final String CSV_FILE_PATH = "data/bank_accounts.csv";

    /**
     * This class is where It will read and write from the BankAccount
     * Class. It will have the accountID, userID, balance, accountType
     * EXAMPLE - Aman-C-1001,1001,500.0,CHECKING
     * EXAMPLE - Aman-C-1001,1001,500.0,SAVING
     *
     *
     *  CREATE file path to csv file
     *
     *  CREATE a method that will write to the csv file
     *
     *  CREATE a method that will delete from csv file (ADMIN)
     *
     *  CREATE a method that will read all BankAccounts form csv file (ADMIN)
     *
     *
     * */

    /**
     * Writes a BankAccount to the CSV file
     * @param account The BankAccount to write
     * @return true if successful, false otherwise
     */
    public static boolean writeToCSV(BankAccount account) {
        try {
            // Create data directory if it doesn't exist
            new File("data").mkdirs();

            // Read existing accounts to check for duplicates
            Map<String, BankAccount> existingAccounts = new HashMap<>();
            File file = new File(CSV_FILE_PATH);
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 4) {
                        existingAccounts.put(data[0], createAccountFromData(data));
                    }
                }
                br.close();
            }

            // Check if account already exists
            if (existingAccounts.containsKey(account.getAccountID())) {
                // Update existing account
                existingAccounts.put(account.getAccountID(), account);
            } else {
                // Add new account
                existingAccounts.put(account.getAccountID(), account);
            }

            // Write all accounts back to file
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (BankAccount acc : existingAccounts.values()) {
                String line = String.format("%s,%d,%.2f,%s%n",
                        acc.getAccountID(),
                        acc.getUserID(),
                        acc.getBalance(),
                        acc.getAccountType());
                bw.write(line);
            }
            bw.close();
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a BankAccount in the CSV file
     * @param account The updated BankAccount
     * @return true if successful, false otherwise
     */
    public static boolean updateAccount(BankAccount account) {
        return writeToCSV(account); // Reuse writeToCSV as it handles updates
    }

    /**
     * Reads all BankAccounts from the CSV file (Admin function)
     * @return List of BankAccount objects
     */
    public static List<BankAccount> readAllAccountsAdmin() {
        List<BankAccount> accounts = new ArrayList<>();
        try {
            File file = new File(CSV_FILE_PATH);
            if (!file.exists()) {
                return accounts;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    accounts.add(createAccountFromData(data));
                }
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Error reading from CSV: " + e.getMessage());
        }
        return accounts;
    }

    /**
     * Reads all BankAccounts for a specific user
     * @param userID The ID of the user whose accounts to read
     * @return List of BankAccount objects belonging to the user
     */
    public static List<BankAccount> readUserAccounts(int userID) {
        List<BankAccount> userAccounts = new ArrayList<>();
        try {
            File file = new File(CSV_FILE_PATH);
            if (!file.exists()) {
                return userAccounts;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4 && Integer.parseInt(data[1]) == userID) {
                    userAccounts.add(createAccountFromData(data));
                }
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Error reading from CSV: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing account data: " + e.getMessage());
        }
        return userAccounts;
    }

    /**
     * Deletes a BankAccount from the CSV file (Admin function)
     * @param accountID The ID of the account to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteAccount(String accountID) {
        try {
            File file = new File(CSV_FILE_PATH);
            if (!file.exists()) {
                return false;
            }

            // Read all accounts except the one to delete
            Map<String, BankAccount> accounts = new HashMap<>();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4 && !data[0].equals(accountID)) {
                    accounts.put(data[0], createAccountFromData(data));
                }
            }
            br.close();

            // Write back all accounts except the deleted one
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (BankAccount acc : accounts.values()) {
                String accountLine = String.format("%s,%d,%.2f,%s%n",
                        acc.getAccountID(),
                        acc.getUserID(),
                        acc.getBalance(),
                        acc.getAccountType());
                bw.write(accountLine);
            }
            bw.close();
            return true;
        } catch (IOException e) {
            System.err.println("Error deleting from CSV: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to create a BankAccount from CSV data
     */
    private static BankAccount createAccountFromData(String[] data) {
        if (data[3].equals("SAVING")) {
            return new SavingsAccount(
                    data[0],                    // accountID
                    Integer.parseInt(data[1]),  // userID
                    Double.parseDouble(data[2]) // balance
            );
        } else {
            return new BankAccount(
                    data[0],                    // accountID
                    Integer.parseInt(data[1]),  // userID
                    Double.parseDouble(data[2]), // balance
                    data[3]                     // accountType
            );
        }
    }
}

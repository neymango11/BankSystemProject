import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
            
            // Create file if it doesn't exist
            File file = new File(CSV_FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }

            // Append the account data to the CSV file
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            
            String line = String.format("%s,%d,%.2f,%s%n",
                account.getAccountID(),
                account.getUserID(),
                account.getBalance(),
                account.getAccountType());
            
            bw.write(line);
            bw.close();
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
            return false;
        }
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
                    BankAccount account = new BankAccount(
                        data[0],                    // accountID
                        Integer.parseInt(data[1]),  // userID
                        Double.parseDouble(data[2]), // balance
                        data[3]                     // accountType
                    );
                    accounts.add(account);
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
                    BankAccount account = new BankAccount(
                        data[0],                    // accountID
                        Integer.parseInt(data[1]),  // userID
                        Double.parseDouble(data[2]), // balance
                        data[3]                     // accountType
                    );
                    userAccounts.add(account);
                }
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Error reading from CSV: " + e.getMessage());
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
            List<String> lines = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(accountID + ",")) {
                    lines.add(line);
                }
            }
            br.close();

            // Write back all accounts except the deleted one
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String l : lines) {
                bw.write(l + "\n");
            }
            bw.close();
            return true;
        } catch (IOException e) {
            System.err.println("Error deleting from CSV: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a BankAccount in the CSV file
     * @param account The updated BankAccount
     * @return true if successful, false otherwise
     */
    public static boolean updateAccount(BankAccount account) {
        try {
            File file = new File(CSV_FILE_PATH);
            if (!file.exists()) {
                return false;
            }

            // Read all accounts and update the matching one
            List<String> lines = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(account.getAccountID() + ",")) {
                    // Replace with updated account data
                    line = String.format("%s,%d,%.2f,%s",
                        account.getAccountID(),
                        account.getUserID(),
                        account.getBalance(),
                        account.getAccountType());
                }
                lines.add(line);
            }
            br.close();

            // Write back all accounts
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String l : lines) {
                bw.write(l + "\n");
            }
            bw.close();
            return true;
        } catch (IOException e) {
            System.err.println("Error updating CSV: " + e.getMessage());
            return false;
        }
    }
}

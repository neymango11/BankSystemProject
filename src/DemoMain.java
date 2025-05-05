import java.util.*;



class DemoMain {
    public static void main(String[] args) {
        String file = "users.csv"; // Path to the CSV file

        // STEP 1: Load users from the CSV file
        List<User> users = UserCSV.loadUsers(file);

        // STEP 2: Find an Admin user from the list
        Admin admin = null;
        for (User user : users) {
            if (user instanceof Admin) {
                admin = (Admin) user;
                break;
            }
        }

        if (admin == null) {
            System.out.println("❌ No admin found in users.csv.");
            return;
        }

        System.out.println("✅ Admin logged in: " + admin.getUsername());

        // STEP 3: Display all users currently loaded
        System.out.println("\n📋 Users Before Updates:");
        admin.viewAllUsers(users);

        // STEP 4: Modify a user ("john") — change username and reset password
        User john = users.stream()
                .filter(u -> u.getUsername().equals("john"))
                .findFirst()
                .orElse(null);

        if (john != null) {
            admin.resetUserPassword(john, "johnNEWpass123");
            admin.resetUserUsername(john, "john_updated");
        } else {
            System.out.println("⚠️ 'john' not found in user list.");
        }

        // STEP 5: Remove a user ("jane") if exists
        users.removeIf(u -> u.getUsername().equals("jane"));

        // STEP 6: Add a brand new user
        User bruce = admin.createUser("bruce", "bruce789", "STANDARD USER", 99);
        users.add(bruce);

        // STEP 7: Display updated users
        System.out.println("\n🆕 Users After Updates:");
        admin.viewAllUsers(users);

        // STEP 8: Save all changes back to the CSV
        UserCSV.saveUsers(file, users);
    }

}

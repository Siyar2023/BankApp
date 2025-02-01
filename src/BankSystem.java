import java.util.*;

public class BankSystem {
    private Map<String, User> users = new HashMap<>();  // A map to store users by their name

    // Method for registering a new user
    public void registerUser(String name, String password) {
        users.put(name, new User(name, password));  // Add the new user to the users map
    }

    // Method for logging in a user, returns the User object if successful, otherwise null
    public User login(String name, String password) {
        User user = users.get(name);  // Find the user by their name
        if (user != null && user.getPassword().equals(password)) {  // Check if the password matches
            return user;  // Return the user object if credentials are correct
        }
        return null;  // Return null if the user doesn't exist or the password is incorrect
    }

    // Method to transfer money between users, returns true if successful, false otherwise
    public boolean transferBetweenUsers(User fromUser, String toUserName, double amount) {
        User toUser = users.get(toUserName);  // Find the recipient of the transfer

        // Check if the recipient exists and if the sender has enough balance
        if (toUser != null && fromUser.getMainAccount().getBalance() >= amount) {
            // Perform the transfer: withdraw from the sender and deposit to the recipient
            fromUser.getMainAccount().withdraw(amount);
            toUser.getMainAccount().deposit(amount);
            return true;  // Transfer succeeded
        } else {
            return false;  // Transfer failed: either the recipient doesn't exist or the sender has insufficient funds
        }
    }
}

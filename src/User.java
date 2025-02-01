import java.util.*;

public class User {
    private String name;  // The user's name
    private String password;  // The user's password
    private List<BankAccount> accounts = new ArrayList<>();  // List of the user's bank accounts

    // Constructor for creating a new user with name, password, and an initial main account
    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.accounts.add(new BankAccount("Main Account", 100000)); // Create a main account with an initial balance of 100,000
    }

    // Getter for the user's name
    public String getName() {
        return name;
    }

    // Getter for the user's password
    public String getPassword() {
        return password;
    }

    // Getter for the list of accounts associated with the user
    public List<BankAccount> getAccounts() {
        return accounts;
    }

    // Getter for the user's main account, always returns the first account in the list
    public BankAccount getMainAccount() {
        return accounts.get(0); // For simplicity, always return the main account (the first account)
    }

    // Method for creating a new bank account with a given initial balance
    public void createNewAccount(double initialBalance) {
        accounts.add(new BankAccount("New Account", initialBalance));  // Add a new account with the specified initial balance
    }
}

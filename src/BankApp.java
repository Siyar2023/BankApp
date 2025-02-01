import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BankApp extends Application {
    // Creating an instance of BankSystem and other required fields
    private BankSystem bankSystem = new BankSystem();
    private User loggedInUser; // Stores the currently logged-in user
    private Stage primaryStage; // Stage to hold the application window

    public static void main(String[] args) {
        launch(args); // Start the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; // Initialize the primary stage
        primaryStage.setTitle("Bank App"); // Set the window title
        showLoginScreen(); // Show the login screen initially
    }

    // Method to display the login screen
    private void showLoginScreen() {
        VBox loginLayout = new VBox(10); // Layout to hold the login components
        loginLayout.setAlignment(Pos.CENTER); // Center the components

        TextField nameField = new TextField(); // Field for entering username
        nameField.setPromptText("Name"); // Prompt text
        PasswordField passField = new PasswordField(); // Field for entering password
        passField.setPromptText("Password (3-5 digits)"); // Prompt text
        Button loginButton = new Button("Login"); // Button to login
        Button registerButton = new Button("Register"); // Button to register
        Label messageLabel = new Label(); // Label to show messages

        // Action for login button
        loginButton.setOnAction(e -> {
            String name = nameField.getText(); // Get entered username
            String password = passField.getText(); // Get entered password
            loggedInUser = bankSystem.login(name, password); // Attempt to login
            if (loggedInUser != null) {
                showBankDashboard(); // If login is successful, show the dashboard
            } else {
                messageLabel.setText("Error! Please Register first then click on Login");
            }
        });

        // Action for register button
        registerButton.setOnAction(e -> {
            String name = nameField.getText(); // Get entered username
            String password = passField.getText(); // Get entered password
            if (password.matches("\\d{3,5}")) { // Validate password (3-5 digits)
                bankSystem.registerUser(name, password); // Register new user
                messageLabel.setText("User registered successfully! Now click Login");
            } else {
                messageLabel.setText("Password must be 3-5 digits!");
            }
        });

        // Add components to the login layout
        loginLayout.getChildren().addAll(nameField, passField, loginButton, registerButton, messageLabel);
        primaryStage.setScene(new Scene(loginLayout, 500, 500)); // Set the scene for the login screen
        primaryStage.show(); // Show the login screen
    }

    // Method to display the bank dashboard after successful login
    private void showBankDashboard() {
        VBox bankLayout = new VBox(10); // Layout to hold bank dashboard components
        bankLayout.setAlignment(Pos.CENTER); // Center the components
        Label welcomeLabel = new Label("Welcome, " + loggedInUser.getName()); // Display username
        Label balanceLabel = new Label("Balance: " + loggedInUser.getMainAccount().getBalance() + " USD"); // Display balance

        // Button to create a new account
        Button createAccountButton = new Button("Create New Account");
        Label accountCreatedLabel = new Label(""); // Label to show message when new account is created

        // Action for create new account button
        createAccountButton.setOnAction(e -> {
            loggedInUser.createNewAccount(100000); // Create a new account with 100,000 USD balance
            accountCreatedLabel.setText("New account created with balance of 100,000 USD!"); // Display success message
            showBankDashboard(); // Update the dashboard after account creation
        });

        // Button to transfer money
        Button transferButton = new Button("Transfer Money");
        transferButton.setOnAction(e -> showTransferScreen()); // Show transfer screen when clicked

        // Button to logout
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            loggedInUser = null; // Reset logged-in user
            showLoginScreen(); // Show the login screen again
        });

        // Add components to the bank dashboard layout
        bankLayout.getChildren().addAll(welcomeLabel, balanceLabel, createAccountButton, accountCreatedLabel, transferButton, logoutButton);
        primaryStage.setScene(new Scene(bankLayout, 400, 300)); // Set the scene for the dashboard
    }

    // Method to display the transfer screen
    private void showTransferScreen() {
        VBox transferLayout = new VBox(10); // Layout to hold transfer components
        transferLayout.setAlignment(Pos.CENTER); // Center the components

        TextField toUserField = new TextField(); // Field for recipient's username
        toUserField.setPromptText("Recipient's Name"); // Prompt text

        ChoiceBox<BankAccount> fromAccountChoiceBox = new ChoiceBox<>(); // Dropdown to choose account
        fromAccountChoiceBox.getItems().addAll(loggedInUser.getAccounts()); // Populate with user's accounts
        fromAccountChoiceBox.setValue(loggedInUser.getMainAccount()); // Default to the main account

        TextField amountField = new TextField(); // Field for entering transfer amount
        amountField.setPromptText("Amount to Transfer"); // Prompt text

        Button transferButton = new Button("Transfer");  // Button to initiate transfer

        // Action for transfer button
        transferButton.setOnAction(e -> {
            String toUserName = toUserField.getText(); // Get recipient's name
            String amountText = amountField.getText(); // Get entered transfer amount

            // Remove any commas or periods from the amount text
            amountText = amountText.replaceAll("[,.]", "");

            try {
                double amount = Double.parseDouble(amountText); // Parse the amount to a double

                // Check if the amount is valid
                if (amount <= 0) {
                    showErrorMessage("Amount must be greater than zero!");
                    return;
                }

                // Attempt to transfer money between users
                boolean success = bankSystem.transferBetweenUsers(loggedInUser, toUserName, amount);

                if (success) {
                    showBankDashboard(); // Update the dashboard after transfer
                } else {
                    showErrorMessage("Transfer failed! Please check the recipient and balance.");
                }
            } catch (NumberFormatException ex) {
                showErrorMessage("Invalid amount format!"); // Show error if amount is not valid
            }
        });

        // Add components to the transfer layout
        transferLayout.getChildren().addAll(toUserField, fromAccountChoiceBox, amountField, transferButton);
        primaryStage.setScene(new Scene(transferLayout, 400, 250)); // Set the scene for the transfer screen
    }

    // Method to show error message in an alert dialog
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Create error alert
        alert.setTitle("Error"); // Set alert title
        alert.setHeaderText(null); // Remove header
        alert.setContentText(message); // Set error message
        alert.showAndWait(); // Show and wait for user to close the alert
    }
}

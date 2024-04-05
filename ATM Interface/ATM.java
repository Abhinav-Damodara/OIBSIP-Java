import java.util.ArrayList;
import java.util.Scanner;

class User {
    private String name;
    private int balance;
    private int pin;
    private ArrayList<String> transactionHistory;

    public User(String name, int i, int j) {
        this.name = name;
        this.pin = i;
        this.balance = j;
        this.transactionHistory = new ArrayList<>();
    }

    public User(String username, String password, String string) {
        //TODO Auto-generated constructor stub
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public boolean verifyPin(int pin) {
        return this.pin == pin;
    }

    public void deposit(int amount) {
        balance += amount;
        transactionHistory.add("Deposited: " + amount + " Rupees");
    }

    public boolean withdraw(int amount) {
        if (amount > balance) {
            System.out.println("Insufficient balance");
            return false;
        }
        balance -= amount;
        transactionHistory.add("Withdrawn: " + amount + " Rupees");
        return true;
    }

    public void transfer(User recipient, int amount) {
        if (withdraw(amount)) {
            recipient.deposit(amount);
            transactionHistory.add("Transferred: " + amount + " Rupees to " + recipient.getName());
        } else {
            System.out.println("Transfer failed. Insufficient balance.");
        }
    }

    public void printTransactionHistory() {
        System.out.println("Transaction History for " + name + ":");
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }

    public void setProfile(String newProfile) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setProfile'");
    }

    public void setPassword(String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPassword'");
    }
}

public class ATM {
    public static void main(String[] args) {
        User user1 = new User("User1", 1234, 10000);
        User user2 = new User("User2", 5678, 10000);

        try (Scanner scanner = new Scanner(System.in)) {
            int currentUserPin;
            User currentUser;

            while (true) {
                System.out.println("Enter PIN:");
                currentUserPin = scanner.nextInt();
                if (user1.verifyPin(currentUserPin)) {
                    currentUser = user1;
                    break;
                } else if (user2.verifyPin(currentUserPin)) {
                    currentUser = user2;
                    break;
                } else {
                    System.out.println("Invalid PIN. Try again.");
                }
            }

            while (true) {
                System.out.println("\nWelcome, " + currentUser.getName());
                System.out.println("1. Deposit");
                System.out.println("2. Withdraw");
                System.out.println("3. Transfer");
                System.out.println("4. View Transaction History");
                System.out.println("5. Quit");

                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("Enter deposit amount:");
                        int depositAmount = scanner.nextInt();
                        currentUser.deposit(depositAmount);
                        System.out.println("Deposit successful. New balance: " + currentUser.getBalance() + " Rupees");
                        break;
                    case 2:
                        System.out.println("Enter withdrawal amount:");
                        int withdrawalAmount = scanner.nextInt();
                        if (currentUser.withdraw(withdrawalAmount)) {
                            System.out.println("Withdrawal successful. New balance: " + currentUser.getBalance() + " Rupees");
                        }
                        break;
                    case 3:
                        System.out.println("Enter recipient's name:");
                        String recipientName = scanner.next();
                        User recipient = recipientName.equalsIgnoreCase("User 1") ? user1 : user2;
                        System.out.println("Enter transfer amount:");
                        int transferAmount = scanner.nextInt();
                        currentUser.transfer(recipient, transferAmount);
                        break;
                    case 4:
                        currentUser.printTransactionHistory();
                        break;
                    case 5:
                        System.out.println("Thank you for using the ATM.");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }
}
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            User user = Login.loginUser(username, password);
            if (user != null) {
                System.out.println("Login successful!");
                Session.startSession(user);

                int choice;
                do {
                    System.out.println("Choose an option:");
                    System.out.println("1. Update Profile");
                    System.out.println("2. Select Answers for MCQs");
                    System.out.println("3. Logout");
                    System.out.print("Enter your choice: ");

                    while (!scanner.hasNextInt()) {
                        System.out.println("Invalid input. Please enter a number.");
                        scanner.nextLine(); // Consume invalid input
                        System.out.print("Enter your choice: ");
                    }
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1:
                            // Update profile
                            String newPassword, newProfile;
                            System.out.print("Enter new password: ");
                            newPassword = scanner.nextLine();
                            System.out.print("Enter new profile: ");
                            newProfile = scanner.nextLine();
                            Profile.updateProfile(user, newPassword, newProfile);
                            System.out.println("Profile updated successfully!");
                            break;
                        case 2:
                            // Start MCQ quiz
                            Timer timer = new Timer(30);
                            timer.start(); // Start timer thread
                            MCQ.startQuiz(); // Start MCQ quiz
                            timer.interrupt(); // Interrupt the timer thread if quiz is completed early
                            break;
                        case 3:
                            // Logout
                            System.out.print("Are you sure you want to logout? (Y/N): ");
                            String logoutChoice = scanner.nextLine().toUpperCase();
                            if (logoutChoice.equals("Y")) {
                                Session.closeSession();
                                Logout.logout();
                                System.out.println("Logged out successfully.");
                            }
                            break;
                        default:
                            System.out.println("Invalid choice. Please choose option 1, 2, or 3.");
                    }
                } while (choice != 3);
            } else {
                System.out.println("Invalid username or password.");
            }
        }
    }
}

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class MCQ {
    private static boolean quizCompleted = false;
    private static int correctAnswers = 0;

    public static void startQuiz() {
        try (Scanner scanner = new Scanner(System.in)) {
            Random random = new Random();

            // Generate 5 random addition questions
            int numQuestions = 5;
            correctAnswers = 0;
            for (int i = 1; i <= numQuestions && !quizCompleted; i++) { // Continue until quiz is completed
                int num1 = random.nextInt(10); // Random number between 0 and 9
                int num2 = random.nextInt(10); // Random number between 0 and 9
                int correctAnswer = num1 + num2;

                System.out.println("Question " + i + ": What is " + num1 + " + " + num2 + "?");
                System.out.print("Your answer: ");

                try {
                    int userAnswer = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (!quizCompleted) { // Check if quiz is still ongoing
                        if (userAnswer == correctAnswer) {
                            System.out.println("Correct!");
                            correctAnswers++;
                        } else {
                            System.out.println("Incorrect. The correct answer is " + correctAnswer);
                        }
                    } else {
                        System.out.println("Time's up!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter an integer.");
                    scanner.nextLine(); // Consume newline
                }
            }

            System.out.println("Quiz completed. You answered " + correctAnswers + " out of " + numQuestions + " questions correctly.");
            // You can add more functionality here, like storing the user's score, etc.
        }
    }

    public static void startQuizWithTimer() {
        Timer timer = new Timer(30); // 30 seconds
        timer.start(); // Start the timer thread
    }

    public static void autoSubmit() {
        if (!quizCompleted) {
            System.out.println("Auto-submitting quiz...");
            // Implement auto-submit logic here
            quizCompleted = true;
        }
    }

    public static void quizCompleted() {
        quizCompleted = true;
        System.out.println("Quiz time's up! Exiting...");
        System.out.println("You attained " + correctAnswers + " marks.");
        System.exit(0); // Exit the program
    }
}

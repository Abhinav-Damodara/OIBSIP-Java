import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int maxNumber = 100; // Change this to adjust the range
        int maxAttempts = 5; // Number of allowed guesses

        int randomNumber = random.nextInt(maxNumber) + 1; // Generate random number between 1 and maxNumber

        int numRounds = 1;
        int totalScore = 0;

        while (true) {
            System.out.println("\n** Round " + numRounds + " **");

            int attempts = 0;
            boolean guessedCorrectly = false;

            while (attempts < maxAttempts && !guessedCorrectly) {
                int attemptsLeft = maxAttempts - attempts;
                System.out.println("Attempts left: " + attemptsLeft);
                System.out.print("Guess a number between 1 and " + maxNumber + ": ");
                int guess = scanner.nextInt();

                attempts++;

                if (guess == randomNumber) {
                    guessedCorrectly = true;
                    System.out.println("Congratulations! You guessed the number in " + attempts + " attempts.");

                    // Calculate score based on attempts (higher attempts = lower score)
                    int score = maxAttempts - attempts + 1;
                    totalScore += score;
                    System.out.println("Score for this round: " + score);
                } else if (guess < randomNumber) {
                    System.out.println("Your guess is too low. Try again.");
                } else {
                    System.out.println("Your guess is too high. Try again.");
                }
            }

            if (!guessedCorrectly) {
                System.out.println("You ran out of attempts. The number was: " + randomNumber);
            }

            // Ask if the user wants to play another round
            System.out.print("\nDo you want to play another round? (y/n): ");
            char playAgain = scanner.next().charAt(0);

            if (playAgain != 'y' && playAgain != 'Y') {
                break;
            }

            numRounds++;
            randomNumber = random.nextInt(maxNumber) + 1; // Generate new random number
        }

        System.out.println("\n** Game Over! **");
        System.out.println("Total score: " + totalScore);

        scanner.close();
    }
}

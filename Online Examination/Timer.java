public class Timer extends Thread {
    private int seconds;

    public Timer(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public void run() {
        try {
            System.out.println("Timer started for " + seconds + " seconds.");
            for (int i = seconds; i > 0; i--) {
                if (i % 5 == 0) { // Display time remaining every 5 seconds
                    System.out.println("Time remaining: " + i + " seconds");
                }
                Thread.sleep(1000); // Sleep for 1 second
            }
            System.out.println("Time's up!");
            MCQ.quizCompleted(); // Interrupt the quiz thread
        } catch (InterruptedException e) {
            System.out.println("Timer interrupted. Exiting...");
            MCQ.quizCompleted(); // Interrupt the quiz thread
        }
    }
}

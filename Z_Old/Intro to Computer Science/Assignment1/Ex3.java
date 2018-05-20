import java.util.Scanner;

public class Ex3 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // User inputs at least three numbers.
        int largest_1 = scanner.nextInt();
        int largest_2 = scanner.nextInt();
        int largest_3 = scanner.nextInt();

        // Get first input after it.
        int input = scanner.nextInt();
        while (input != 0) {
            // See if the new number is larger than the others.
            if (input > largest_1 || input > largest_2 || input > largest_3) {
                // Find out who is the smallest number and replace it.
                if (largest_1 <= largest_2 && largest_1 <= largest_3) {
                    // largest_1 is the smallest.
                    largest_1 = input;
                }
                else if (largest_2 <= largest_1 && largest_2 <= largest_3) {
                    // largest_2 is the smallest.
                    largest_2 = input;
                }
                else if (largest_3 <= largest_1 && largest_3 <= largest_2) {
                    // largest_3 is the smallest.
                    largest_3 = input;
                }
            }

            input = scanner.nextInt();
        }

        // Get the average of the three largest numbers in the sequence.
        double average = (largest_1 + largest_2 + largest_3)  / 3.0;
        // Print it out.
        System.out.println(average);
    }
}

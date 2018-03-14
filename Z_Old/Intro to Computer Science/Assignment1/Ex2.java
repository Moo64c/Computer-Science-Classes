import java.util.Scanner;

public class Ex2 {

	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int end, current_number, total = 0;

        // Get data from user.
        // Assume that the first input is the lower number.
        current_number = scanner.nextInt();
        end =  scanner.nextInt();

        double difference = 0;
        while (current_number <= end) {
            // Check if the number is prime.
            boolean isPrime = true;
            int i = 2;
            while (isPrime && i <= Math.sqrt(current_number)) {
                // Check if the number divides by i.
                isPrime = (current_number % i != 0);
                i = i + 1;
            }

            if (isPrime) {
                // Add to the total count.
                total = total + current_number;
                difference = difference + 1;
            }

            // Check the next number.
            current_number = current_number + 1;
        }
        // Print the average.
        double average = (double) total / difference;
        System.out.println(average);
	}
}

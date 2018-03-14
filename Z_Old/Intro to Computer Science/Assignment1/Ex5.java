import java.util.Scanner;

public class Ex5 {

	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
	    int sum = 0, limit = scanner.nextInt();

        for (int current_number = 7; current_number <= limit; current_number = current_number  + 1) {
            // Check if the sum of the number's digits divide by 7.

            // Duplicate the current number so it can be worked with.
            int internal_sum = 0, internal = current_number;
            while (internal != 0) {
                internal_sum = internal_sum + internal % 10;
                internal = internal / 10;
            }

            if (internal_sum % 7 == 0) {
                // The sum divides by 7.
                sum = sum + current_number;
            }
        }

        // Print out the usm.
        System.out.println(sum);
	}
}

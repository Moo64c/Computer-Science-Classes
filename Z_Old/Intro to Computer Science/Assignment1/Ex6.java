import java.util.Scanner;

public class Ex6 {

	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get a number from the user.
        int total = 0, size = scanner.nextInt();

        // Use the series:
        // (size * size) + (b(size - 1) * (size -1)) + ... + (2 * 2) + (1 * 1)
        // To calculate how many squares are possible.
        for (int index = size; index > 0; index = index - 1) {
            total = total + (index * index);
        }

        // Print out to the user.
        System.out.println(total);
	}
}

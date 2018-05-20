import java.util.Scanner;

public class Ex4 {

	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Get limit from user.
        int c, limit = scanner.nextInt();
        double result;
        // General use output string.
        String out;

        // Loop to the limit on all number combinations possible for a and b,
        // and see if the square root of the resulting element is a whole number.
        for (int a = 3; a < limit; a = a + 1) {
            for (int b = a; b < limit; b = b + 1) {
                result = Math.sqrt(a * a + b * b);
                c = (int) Math.floor(result);
                if (c < limit && result == c) {
                    // Result is a whole number meaning a Pythagorean triplet
                    // was found.
                    out = "(" + a + "," + b + "," + c + ") : " + a + "*" + a + " + " + b + "*" + b + " = " + c + "*" + c;
                    System.out.println(out);
                }
            }
        }
    }
}

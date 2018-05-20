import java.util.Scanner;

/**
 *
 * @author Amir Arbel
 *
 * Lesson 4 in class examples.
 */
public class Lesson4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        /* gcd()
        int m = scanner.nextInt();
        int n = scanner.nextInt();
        System.out.println(gcd(m, n));
        */

        /* intArrayEq()
        int[] x = {1,2,3};
        int[] y = {1,2,3};
        System.out.println(intArrayEq(x,y));
         */

        /* swap()
        int[] x = {1,2,3};
        swap(x, 0, 2);
        for (int y : x) {
            System.out.println(y);
        }
         */


        // Example of a test class for isMinIndex(), intArrayEq()...
    }

    /**
     * Helper function to see if a precondition was violated.
     */
    public static void precondition(int minIndex, int i, int[] a) {
        if (a.length > 0 && minIndex == 0 && i ==1) {
            System.out.println("precondition ok");
        }
        else {
            System.out.println("precondition violation");
        }
    }

    /**
     * Get the greatest common divisor of two numbers.
     */
    public static int gcd(int m, int n) {
        int r = m % n;

        while (r != 0) {
            m = n;
            n = r;
            r = m % n;
        }

        return n;
    }

    /**
     * Check if an index is the minimum in an array.
     */
    public static int isMinIndex (int[] a) {
        int minIndex = 0, i = 1;
        precondition(minIndex, i, a);

        while (i < a.length) {
            minIndex = (a[i] < a[minIndex]) ? i : minIndex;

            i = i + 1;
        }

        return minIndex;
    }
    /**
     * Check if an index is the minimum in an array.
     */
    public static boolean isMinIndex (int[] a, int index) {
        boolean is_min = true;
        for (int i = 0; i< a.length; i = i + 1) {
            is_min = ! (a[i] < a[index]);
        }

        return is_min;
    }

    /**
     * Switch two values in an array.
     */
    public static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;

        return;
    }

    /**
     * Check if two arrays are equal.
     */
    public static boolean intArrayEq(int[] a, int[] b) {
        boolean result = (a.length == b.length);

        for (int i = 0; i < (a.length) && result; i = i + 1) {
            result = (a[i] == b[i]);
        }

        return result;
    }
}

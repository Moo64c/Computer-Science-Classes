/**
 *
 * @author Amir Arbel
 *
 * Lesson 6 in class examples.
 *
 * Strings and functions.
 */
public class Lesson6 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int[][] arr = {{1,2,3}, {4,5,6}, {7,8,9}};
        int[][] result = {{1,4,7}, {2,5,8}, {3,6,9}};


    }
    public static boolean isPrime(int p) {
        final int TRIES = 50;
        boolean prime = true;

        for (int i = 0;  i < TRIES && prime;  i = i+1) {
            int a = 1 + (int)(Math.random() * (p-1));
            prime = fermatTest(p,a);
        }

        return prime;
    }

    public static boolean fermatTest(int p, int a) {
        int pow = 1;
        for (int i = 0;  i < p-1;  i = i+1)
            pow = (pow * a) % p;
        return pow == 1;
    }

    public static int[] getColumn(int[][] m, int i) {
        int[] col = new int[m.length];

        for (int j = 0;  j < col.length;  j = j+1)
            col[j] = m[j][i];

        return col;
    }

    public static int[][] transpose(int[][] m) {
        int[][] t = new int[m[0].length][];

        for (int i = 0;  i < t.length;  i = i+1)
            t[i] = getColumn(m,i);

        return t;
    }

    public static int intValue(String str) {
        int base, first, value = 0, power = 1;
        if (str.length() == 1 || str.charAt(0) != '0') {
            // Normal numbers.
            base = 10;
            first = 0;
        }
        else if (str.charAt(1) != 'x') {
            // Base 8 numbers.
            base = 8;
            first = 1;
        }
        else {
            base = 16;
            first = 2;
        }

        for (int i = str.length() - 1; i >= first; i = i -1) {
            value = value + str.charAt(i) * power;
            power = power * base;
        }

        return value;
    }

    public static void printArray(int[] haystack) {
        for (int index = 0; index < haystack.length; index = index + 1) {
            System.out.print(haystack[index] + ", ");
        }
    }


    public static int counter(String haystack, char needle) {
        int counter = 0;
        for (int i = 0; i< haystack.length(); i=i+1) {
            if (haystack.charAt(i) == needle) {
                counter = counter + 1;
            }
        }
        return counter;
    }
}



import java.util.Scanner;

/**
 *
 * @author Amir Arbel
 * 
 * Lesson 2 in class examples.
 */
public class Lesson2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Run GCD().
        // Lesson2.getGCD();
        
        // Run getLength().
        // Lesson2.getLength();
        
        // Run getCommonDivisor().
//        Lesson2.getCommonDivisor();

        // Lesson2.checkPrime();
//        Lesson2.getAllPrimes();

        double i = 2;
        int index = 1;
        double sqrt2 = Math.sqrt(2);
        while (i - sqrt2 > 0.0000000000001) {
            i = i/2 + 1/i;
            System.out.println(i + ", " + index);
            index++;
        }

        System.out.println(i + ", " + index);
    }
    
    /**
     * Get how many digits are in a number.
     */
    public static void getLength() {
        // Create an input scanner.
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a number: ");
        int x = scanner.nextInt();
        int counter = 1;
        
        while (x >= 10) {
            // Remove a digit from the number.
            x = x / 10;
            counter++;
        }
        
        System.out.print("Length of the number is: ");
        System.out.println(counter);
    }
    
    /**
     * Get the greatest common divisor of two numbers.
     */
    public static void getGCD() {
            // Create an input scanner.
        Scanner scanner = new Scanner(System.in);
        
        // Define integers for the algorithem.
        System.out.print("Enter first variable: ");
        int m = scanner.nextInt();
        System.out.print("Enter second variable: ");
        int n = scanner.nextInt();
        int r = m % n;
        
        while (r != 0) {
            m = n;
            n = r;
            r = m % n;
        }
        
        System.out.print("GCD of both variables: ");
        System.out.println(n);
    }
    
    /**
     * Get the common divisor of two numbers.
     */
    public static void getCommonDivisor() {
        // Create an input scanner.
        Scanner scanner = new Scanner(System.in);
        
        // Define integers for the algorithem.
        System.out.print("Enter first number: ");
        int m = scanner.nextInt();
        System.out.print("Enter second number: ");
        int n = scanner.nextInt();
        
        // Assign the lowest number of the two to i.
        int i = (m >= n) ? n : m;
                
        while (!(m % i == 0 && n % i == 0)) {
            i = i - 1;
        }
        
        System.out.print("Common divisor of both variables: ");
        System.out.println(i);
    }
    
    /**
     * Check if a number is a prime number.
     */
    public static void checkPrime() {
        // Create an input scanner.
        Scanner scanner = new Scanner(System.in);
        
        // 1. Input m > 1.
        int m = 0;
        while (m <= 1) {
            System.out.print("Enter number bigger than 1: ");
            m = scanner.nextInt();
        }
        
        // 2. i = 2.
        int i = 2;
        boolean isPrime = true;
        
        while (isPrime & i <= Math.sqrt(m)) {
            // Check if the number divides by i.
            isPrime = (m % i != 0);
            i++;
        }
        
        System.out.println("The number " + m + " is" + (isPrime ? " " : " not ") + "a prime number.");
    }
    
    /**
     * Print all prime numbers until a given number.
     */
    public static void getAllPrimes() {
        // Create an input scanner.
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a number: ");
        int n = scanner.nextInt();
        int m = 2;
        
        System.out.print("Prime numbers found: ");
        while (m <= n) {
            boolean isPrime = true;
            int i = 2;
            
            while (i*i <= m & isPrime) {
                isPrime = (m % i != 0); 
                i = i + 1;
            } 
            
            if (isPrime) {
                System.out.print(m + ", ");
            }
            
            // Next number to check.
            m++;
        }
    }
}

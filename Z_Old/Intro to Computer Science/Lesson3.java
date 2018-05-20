import java.util.Scanner;

/**
 *
 * @author Amir Arbel
 *
 * Lesson 3 in class examples.
 */
public class Lesson3 {

    public Lesson3() {
        System.out.println("foo");
    }


    public Lesson3(int i) {
        System.out.println("bar" + i);
    }
    public static void Guess() {

        // Initialize variables.
        Scanner scanner = new Scanner(System.in);
        int solution, guess = 0;

        // Randomize a number.
        solution = 1 + (int) (100 * Math.random());
        while (guess != solution) {
            // Let the user guess.
            System.out.println("Guess a number: ");
            guess = scanner.nextInt();

            if (guess < solution) {
                System.out.println("Too small");
            }
            else if (guess > solution) {
                System.out.println("Too big");
            }
        }

        // Guess the number right.
        System.out.println("Good job!");
    }

    /**
     * Get the greatest common divisor of two numbers.
     */
    public static void getGCD() {
        // Create an input scanner.
        Scanner scanner = new Scanner(System.in);

        // 1. Input m,n > 0
        int m = scanner.nextInt();
        int n = scanner.nextInt();
        // 2. r <- m % n
        int r = m % n;

        // 3.
        while (r != 0) {
            m = n; // 3.1
            n = r; // 3.2
            r = m % n; // 3.3
        }

        // 4.
        System.out.println(n);
    }

    public static void primes() {
        System.out.print("Input a number to check if it's prime: ");
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        boolean isPrime = true;
        int i = 2;
        while(isPrime && i < m) {
            isPrime = !(m % i == 0);
            i++;
        }

        System.out.print(isPrime ? "The number is prime" : "The number is not prime");
    }
}

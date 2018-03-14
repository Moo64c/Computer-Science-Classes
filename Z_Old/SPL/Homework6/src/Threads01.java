import java.util.Random;

/**
 * SPL101 PS #01 Threads01.java
 * 
 * Creating a thread by implementing a Runnable and wrapping it.
 */

public class Threads01 {
    /**
     * Demonstrating thread creation by
     * implementing Runnable and wrapping with a Thread
     */
    public static void main(String[] a) {
        Random randomGenerator = new Random();
        Horse r1 = new Horse("Horsie ", randomGenerator);
        Thread t1 = new Thread(r1);

        Horse r2 = new Horse("Moosie ", randomGenerator);
        Thread t2 = new Thread(r2);

        Horse r3 = new Horse("Rorsie ", randomGenerator);
        Thread t3 = new Thread(r3);

        Horse r4 = new Horse("Toosie ", randomGenerator);
        Thread t4 = new Thread(r4);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}

/**
 * Example for implementing Runnable
 */
class Horse implements Runnable {
    private String name;
    private int distance;
    Random rand;

    Horse(String name, Random _rand) {
       this.name = name;
       this.distance = 0;
       this.rand = _rand;
    }

    /**
     * Main life cycle
     */
    public void run() {
		while (distance < 100) {
			int time = rand.nextInt(100);
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.distance += 1;
		}
		System.out.println(this.name + " finished the race!");
    }
}


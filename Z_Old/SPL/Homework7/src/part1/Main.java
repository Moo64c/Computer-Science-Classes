package part1;

import java.util.Random;

public class Main {

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

package part2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] a) {
    	Value val = new Value();
	    ExecutorService pool = Executors.newFixedThreadPool(10);
	    for (int index = 1; index <=10; index++ ) {
	    	pool.execute(new ValueRunnable(val));
	    }
	    pool.shutdown();
    }
}

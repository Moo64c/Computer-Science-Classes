package part1;

import java.util.Random;

/**
 * Example for implementing Runnable
 */
class Horse implements Runnable {
    private String name;
    private int distance;
    Random rand;
    Object lock;

    Horse(String name, Random _rand) {
       this.name = name;
       this.distance = 0;
       this.rand = _rand;
       lock = new Object();
    }

    /**
     * Main life cycle
     */
    public void run() {
    	synchronized (lock) {
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
}


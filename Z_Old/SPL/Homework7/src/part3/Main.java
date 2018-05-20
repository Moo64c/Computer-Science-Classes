package part3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.*;

public class Main {

    public static void main(String[] a) {

    }
}

class Cont {
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(100);
		public void sleeper(final int id) {
				final Runnable sleeperRunner = new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(id * 5);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				final ScheduledFuture<?> sleeperHandle = scheduler.scheduleAtFixedRate(sleeperRunner, 10, 10, SECONDS);
				scheduler.schedule(new Runnable() {

					public void run() {
						sleeperHandle.cancel(true); 
					}
				}, 60 * 60, SECONDS);
			}
}

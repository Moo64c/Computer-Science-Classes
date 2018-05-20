package homework8;

public class CountDownLatchA extends Thread {
	private int count;
	
	public CountDownLatchA(int _count) {
		count = _count;
	}
	
	public void countDown() {
		count--;
		if (count <= 0) {
			// Count finished.
			notify();
		}
	}
	
	public void await() {
		while(count > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

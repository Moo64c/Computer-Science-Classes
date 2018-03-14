package part2;

import java.util.Random;

public class ValueRunnable implements Runnable {
	private Value val;
	
	public ValueRunnable(Value _val) {
		val = _val;
	}
	
	@Override
	public void run() {
        Random randomGenerator = new Random();
		int index = randomGenerator.nextInt(100);
		System.out.println(val.at(index));
	}
}

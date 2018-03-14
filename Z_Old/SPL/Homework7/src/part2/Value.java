package part2;
import java.util.Random;

public final class Value {
	private int values[];
	
	Value() {
		values = new int[100];
        Random randomGenerator = new Random();
		for (int index = 0; index < values.length; index++) {
			values[index] = randomGenerator.nextInt(100);
		}
	}
	
	public int at(int index) {
		return values[index];
	}
}

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ReversiTournament {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		int games = 250;
		int wins = 0;
		
		String random = "Problem: while using random player strategy, ";
		String greedy = "Problem: while using greedy player strategy, ";
		String defensive = "Problem: while using defensive player strategy, ";
		String byLocation = "Problem: while using by location player strategy, ";
		String arrayIndexOutOfBound = "an attenpt was made to access an illegal index of an array.\n" +
				"The index is either negative or greater than or equal to the size of the array";
		String nullExc = "an attempt was made to access a null value";
        int tempwins=0;
		try{
			System.out.println("Running " + games + " games against random player");
			wins = playStrategy(wins, games, "randomPlayer");
            System.out.println("Won " + wins);
            tempwins = wins;
		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println(random+arrayIndexOutOfBound);
			e.printStackTrace();
			System.exit(1);
		} catch (NullPointerException e){
			System.out.println(random+nullExc);
			e.printStackTrace();
			System.exit(1);
		}
		try{
			System.out.println("Running " + games + " games against greedy player");
			wins = playStrategy(wins, games, "greedyPlayer");
            System.out.println("Won " + (wins - tempwins));
            tempwins = wins;
		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println(greedy+arrayIndexOutOfBound);
			e.printStackTrace();
			System.exit(1);
		} catch (NullPointerException e){
			System.out.println(greedy+nullExc);
			e.printStackTrace();
			System.exit(1);
		}
		try{
			System.out.println("Running " + games + " games against defensive player");
			wins = playStrategy(wins, games, "defensivePlayer");
            System.out.println("Won " + (wins - tempwins));
            tempwins = wins;
		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println(defensive+arrayIndexOutOfBound);
			e.printStackTrace();
			System.exit(1);
		} catch (NullPointerException e){
			System.out.println(defensive+nullExc);
			e.printStackTrace();
			System.exit(1);
		}
        try{
			System.out.println("Running " + games + " games against by location player");
			wins = playStrategy(wins, games, "byLocationPlayer");
            System.out.println("Won " + (wins - tempwins));
            tempwins = wins;
		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println(byLocation+arrayIndexOutOfBound);
			e.printStackTrace();
			System.exit(1);
		} catch (NullPointerException e){
			System.out.println(byLocation+nullExc);
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println();
		System.out.printf("Won %d games out of %d (%f)\n", wins, 4*games, wins
				* 1.0 / (4*games));
	}
	
	public static int playStrategy (int wins, int games, String methodName) /*throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException*/{
		ReversiPlay playReversi = new ReversiPlay();
		for (int i = 0; i < games; i++) {
			int[][] board = ReversiPlay.createBoard(8);
			int player = 1;
			// if i is even then myPlayer is first
			int myPlayerId = 1 + (i % 2);
			Method method = null;
			try {
				method = ReversiPlay.class.getMethod(methodName, int[][].class, int.class);
			} catch (NoSuchMethodException e) {
				System.out.println("The function " + methodName + "hasn't been implemented yet");
				e.printStackTrace();
				System.exit(1);
			} catch (SecurityException e) {
				e.printStackTrace();
				System.exit(1);
			}
			while (!ReversiPlay.gameOver(board)) {
				if (player == myPlayerId) {
					// my player
					
					int[] move = ReversiPlay.myPlayer(board, player);
					if (move != null){
						board = ReversiPlay.play(board, player, move[0], move[1]);
					}
				} else {
					Object retArr = null;
					try {
						retArr = method.invoke(playReversi, board, player);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						System.exit(1);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						System.exit(1);
					} catch (InvocationTargetException e) {
						e.printStackTrace();
						System.exit(1);
					}
					int[] move = (int[])retArr; 
					if (move != null){
						board = ReversiPlay.play(board, player, move[0], move[1]);
					}
				}
				player = player == 1 ? 2 : 1;
			}

			if (ReversiPlay.findTheWinner(board) == myPlayerId) {
				wins++;
			}
		}
		return wins;
	}

}

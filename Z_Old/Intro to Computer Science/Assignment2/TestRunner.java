import java.util.*;
import java.io.*;

public class TestRunner{

    public static void main(String[] args) {
        PrintStream out = System.out;
        runTest(out);
    }

    // return false if fail
    public static boolean runTest(PrintStream out){
        try{
            int[][] matrix = new int[4][4];
            boolean ans = false;
            int num = 10;
            int[] array = new int[3];
            ReversiPlay.printMatrix(matrix);
            ans = ReversiPlay.isEqual(matrix,matrix);
            matrix = ReversiPlay.copyMatrix(matrix);
            matrix = ReversiPlay.createBoard(8);
            ans = ReversiPlay.isLegal(matrix,1,1,1);
            matrix = ReversiPlay.play(matrix,1,1,1);
            num = ReversiPlay.benefit(matrix,1,1,1);
            matrix = ReversiPlay.possibleMoves(matrix,1);
            ans = ReversiPlay.hasMoves(matrix,1);
            num = ReversiPlay.findTheWinner(matrix);
            ans = ReversiPlay.gameOver(matrix);
            array = ReversiPlay.randomPlayer(matrix,1);
            array = ReversiPlay.greedyPlayer(matrix,1);
            array = ReversiPlay.defensivePlayer(matrix,1);
            array = ReversiPlay.byLocationPlayer(matrix,1);
            array = ReversiPlay.myPlayer(matrix,1);


        }catch(Exception e){
            out.println("ERROR: "+e.getMessage());
        }

        return false;
    }
}

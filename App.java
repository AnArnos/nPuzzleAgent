import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {

        // Scanner scan = new Scanner(System.in);
        // System.out.println("welcome the 8 puzzle game\n " +
        //         "the purpose of the game is to move numbers around and end with the empty space in the top left corner, \nand to leave every number after that in sequential order!\n");

        puzzle puzzle1 = new puzzle(3);
        nodeTree tree = new nodeTree(puzzle1);
        

        
        System.out.println(puzzle1.toString(puzzle1.currentState) + "\npossible moves: " + puzzle1.outputMoves());

        nodeTree puzzleAI = new nodeTree(puzzle1);
        Long start1 = System.nanoTime();
        String s = puzzleAI.aStar();
        Long duration = (System.nanoTime() - start1);
        System.out.println(s);
        System.out.println("Execution time: " + duration / 100_000_000.0 + " tenths of a second.");
        System.out.println("generated " + puzzleAI.nodeCount + " nodes.");

        // System.out.println(tree.breadthFirst());


        //avg testing / filling database:

        // ArrayList<Integer> avgNodes = new ArrayList<Integer>();
        // long start1 = System.currentTimeMillis();

        // for (int i = 0; i < 10; i++) {
        //     puzzle puzzleTest = new puzzle(3);
        //     nodeTree puzzleAI = new nodeTree(puzzleTest);
        //     puzzleAI.IDAStar();
        //     avgNodes.add(puzzleAI.nodeCount);
        // }

        // int total = 0;
        
        // float avg;
        
        // for (int i = 0; i < avgNodes.size(); i++) {
        //     total += avgNodes.get(i);
        // }
        // avg = total / avgNodes.size();
        // long duration = (System.currentTimeMillis() - start1);
        
        // System.out.println("avg nodes: " + avg );
        // System.out.println("avg time: " + duration /10 );
    }
}

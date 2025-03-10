import java.util.ArrayList;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws InterruptedException {
        // launch();
        Scanner scan = new Scanner(System.in);
        String list = "";
        System.out.println("welcome the 8 puzzle game\n " +
                "the purpose of the game is to move numbers around and end with the empty space in the top left corner, \nand to leave every number after that in sequential order!\n");
        // puzzle puzzle8 = new puzzle(10, 10);
        /*
         * 
         * my plan is to create an object named AI and it has an of an initial state. it
         * will then
         * create a tree of all possible moves and the states resulting from those
         * moves.
         * At first, it will just randomly solve. It will not be fast. After wew can
         * solve it, I will
         * focus on making it optimized. the tree will be accompanied by a set of some
         * kind that holds
         * states the AI has visited before.
         * 
         */

        puzzle puzzle1 = new puzzle(3);
        // nodeTree tree = new nodeTree(puzzle1);
        // System.out.println(puzzle1.toString(puzzle1.currentState) + "\npossible moves: " + puzzle1.outputMoves());
        
        // while (!puzzle.isFinished(puzzle1)) {
        //     try {
                
        //         puzzle1.move(scan.nextInt());
        //         System.out.println(puzzle1.toString(puzzle1.currentState) + "\npossible moves: " + puzzle1.outputMoves());
        //     } catch (Exception e) {
        //         System.out.println("insert int please");
        //     }
        // }
        
        



        Long start1 = System.nanoTime();
        nodeTree puzzleAI = new nodeTree(puzzle1);
        String s = puzzleAI.aStar();
        System.out.println(s);

        Long duration = (System.nanoTime() - start1);
        System.out.println("Execution time: " + duration / 100_000_000.0 + " tenths of a second.");
        System.out.println("generated " + puzzleAI.nodeCount + " nodes.");



        
        


        String[] si = s.split(",");
        int[] nu = new int[si.length];
        for (int i = 0; i < si.length; i++) {
            nu[i] = Integer.valueOf(si[i]);
        }

        for (int i = 0; i < nu.length; i++) {
            
            
            try {
                puzzle1.move(nu[i]);
            } catch (Exception e) {
                System.out.println("please insert a number!");
                scan.nextLine();
            }
            Thread.sleep(1000);
        }
        System.out.println("you have completed the " + puzzle1.highnum + " puzzle");
        


        //avg testing:

        // ArrayList<Integer> avgNodes = new ArrayList<Integer>();
        // long start1 = System.currentTimeMillis();

        // for (int i = 0; i < 1000; i++) {
        //     puzzle puzzle1 = new puzzle(3);
        //     nodeTree puzzleAI = new nodeTree(puzzle1);
        //     puzzleAI.aStar();
        //     avgNodes.add(puzzleAI.nodeCount);
        // }

        // int total = 0;
        // long totalTime =  0;
        // float avg;
        // float avgTime;
        // for (int i = 0; i < avgNodes.size(); i++) {
        //     total += avgNodes.get(i);
        // }
        // avg = total / avgNodes.size();
        // long duration = (System.currentTimeMillis() - start1);
        
        // System.out.println("avg nodes: " + avg );
        // System.out.println("avg time: " + duration /1000 );
        

    }

    // public static void clearScreen() {
    //     System.out.print("\033[H\033[2J");
    //     System.out.flush();

    

}
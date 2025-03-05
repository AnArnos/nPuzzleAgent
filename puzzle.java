

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//down then over
//1,2 is
//[] [] []
//[] []  x
//[] [] []
public class puzzle {
    String[][] currentState;
    String[][] goalState;
    String[][] initialState;
    int[] moves = new int[4];
    int highnum;
    boolean solved = false;
    int width, height;

    public puzzle(int width, int height) {
        this.width = width;
        this.height = height;
        this.highnum = (width * height) - 1;
        this.initialState = getPuzzle(width, height);
        this.currentState = this.initialState.clone();
        this.goalState = getGoalState(width, height);
        

        System.out.println(this.toString());
        // System.out.println(this.printGoalState());
        updateMoveable();
    }

    public int[] getMoves() {
        return moves;
    }

    // generates the goal state for the Npuzzle
    public String[][] getGoalState(int width, int height) {
        int highnum = (width * height) - 1;
        String[][] output = new String[width][height];
        for (int i = width - 1; i > -1; i--) {
            for (int j = height - 1; j > -1; j--) {
                if (highnum != 0) {
                    output[i][j] = String.valueOf(highnum);
                    highnum--;
                } else {
                    output[i][j] = " ";
                }
            }
        }
        output[0][0] = "[]";

        return output;
    }

    // returns the text representation of the NPuzzle
    public String toString() {
        String output = "current state: \n";

        for (int i = 0; i < this.currentState.length; i++) {
            for (int j = 0; j < this.currentState[i].length; j++) {
                output += this.currentState[i][j];
                output += "\t";
            }
            output += "\n";
        }

        return output;
    }

    // generates the goal state for the Npuzzle as a string
    public String printGoalState() {
        String output = "goal state: \n";

        for (int i = 0; i < this.currentState.length; i++) {
            for (int j = 0; j < this.currentState[i].length; j++) {
                output += this.goalState[i][j];
                output += "\t";
            }
            output += "\n";
        }

        return output;
    }

    // creates a 2d string array and an arraylist with all the numbers that will go
    // into the Npuzzle.
    /// it then grabs random indexes of the arraylist to put them in the Npuzzle.
    public String[][] getPuzzle(int width, int height) {
        Random rand = new Random();
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        ArrayList<Integer> seq = new ArrayList<Integer>();
        String[][] puzzle = new String[width][height];
        int r;
        int plchldr, plchldr2;

        // fill an ArrayList with numbers that will go into the 8puzzle
        for (int i = 1; i <= (width * height) - 1; i++) {
            numbers.add(i);
        }
        // for a 3x3 numbers should hold 1,2,3,4,5,6,7,8
        // now we will generate a random number from 0 to numbers length and put it in
        // the grid

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (numbers.size() == 1) {
                    r = numbers.get(0);
                    puzzle[i][j] = String.valueOf(r);
                    seq.add(numbers.get(0));
                    numbers.remove(0);
                    break;
                } else {
                    r = rand.nextInt(numbers.size());
                    puzzle[i][j] = String.valueOf(numbers.get(r));
                    seq.add(numbers.get(r));
                    numbers.remove(r);
                }
            }
        }
        puzzle[width - 1][height - 1] = "[]";

        int parity = checkParity(seq);
        

        if (parity %2 != 0 && width > 2 ) {
            plchldr = Integer.valueOf(puzzle[height-1][width-2]);
            plchldr2 = Integer.valueOf(puzzle[height-1][width-3]);
            puzzle[height-1][width-2] = String.valueOf(plchldr2);
            puzzle[height-1][width-3] = String.valueOf(plchldr);
        }

        return puzzle;
    }

    // checks to see if the goal state and the current state are the same/
    public static boolean isFinished(puzzle puzz) {
        return Arrays.deepEquals(puzz.currentState, puzz.goalState);
    }

    // updates the list of moveable tiles, this will atmost be 4 possible tiles
    public void updateMoveable() {
        // get coordinates of [], at the start it will be (y,x), (2,2)
        this.moves = new int[4];
        int x = 0;
        int y = 0;
        for (int i = 0; i < this.currentState.length; i++) {
            for (int j = 0; j < this.currentState[i].length; j++) {
                if (this.currentState[i][j].equals("[]")) {
                    y = i;
                    x = j;
                }
            }
        }
        try {
            this.moves[0] = Integer.valueOf(this.currentState[y - 1][x]);/// above empty
        } catch (Exception e) {

        }
        try {
            this.moves[1] = Integer.valueOf(this.currentState[y + 1][x]);// below empty
        } catch (Exception e) {

        }
        try {
            this.moves[2] = Integer.valueOf(this.currentState[y][x - 1]);// left of empty
        } catch (Exception e) {

        }
        try {
            this.moves[3] = Integer.valueOf(this.currentState[y][x + 1]);// right of empty
        } catch (Exception e) {

        }
    }

    public void move(int i) {// [above, below, left, right]
        
        // gets coords
        int x = 0;
        int y = 0;
        for (int a = 0; a < this.currentState.length; a++) {
            for (int j = 0; j < this.currentState[a].length; j++) {
                if (this.currentState[a][j].equals("[]")) {
                    y = a;
                    x = j;
                }
            }
        }

        //checks to see if the input number is in the moveable category, if not we send an error message and spit out the board again
        for (int j = 0; j < this.moves.length; j++) {
            if (this.moves[j] == i) {
                this.currentState[y][x] = String.valueOf(i);
                switch (j) {
                    case 0:
                        this.currentState[y - 1][x] = "[]";
                        System.out.println(this.toString());
                        updateMoveable();
                        return;
                    case 1:
                        this.currentState[y + 1][x] = "[]";
                        System.out.println(this.toString());
                        updateMoveable();
                        return;
                    case 2:
                        this.currentState[y][x - 1] = "[]";
                        System.out.println(this.toString());
                        updateMoveable();
                        return;
                    case 3:
                        this.currentState[y][x + 1] = "[]";
                        System.out.println(this.toString());
                        updateMoveable();
                        return;
                    default:
                        System.out.println(this.toString());
                        updateMoveable();
                        return;
                }
            }
        }
        System.out.println("please input a number that is moveable!!");
                System.out.println(this.toString());
                return;
            
    }

    public int checkParity(ArrayList<Integer> list){
        int parity = 0;
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i) > list.get(j)) {
                    parity++;
                }
            }
        }
        return parity;
    }
}
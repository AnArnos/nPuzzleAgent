
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

//od array implementation to save space in the search tree
//equivalent of [1stlayer,2ndlayer,3rdlayer] ov each width
public class puzzle {
    int[] currentState;
    int[] goalState;
    int[] initialState;
    int[] moves = new int[4];
    int highnum;
    boolean solved = false;
    int width;
    

    public puzzle(int width) {
        this.width = width;
        this.highnum = (width * width) - 1;
        this.initialState = getPuzzle(width);
        this.currentState = this.initialState.clone();
        this.goalState = getGoalState(width);
        updateMoveable();
        // System.out.println(toString(currentState) + "\npossible moves: " + outputMoves());
    }

    public String outputMoves() {
        String output = "";
        for (int i = 0; i < moves.length; i++) {
            output += moves[i] + ", ";
        }
        return output;
    }

    // generates the goal state for the Npuzzle
    public int[] getGoalState(int width) {
        int highnum = width * width - 1;
        int[] output = new int[width * width];
        output[0] = -1;

        for (int i = highnum; i >= 1; i--) {
            output[i] = i;
        }
        return output;
    }

    // returns the text representation of the NPuzzle
    public String toString(int[] state) {
        String output = "";
        for (int i = 0; i < state.length; i++) {
            if (state[i] == -1) {
                output += "[]" + "\t";
            }else{
                output += state[i] + "\t";
            }
            if ((i + 1) % width == 0) {
                output += "\n";
            }

        }
        return output;
    }

    // creates a 2d string array and an arraylist with all the numbers that will go
    // into the Npuzzle.
    /// it then grabs random indexes of the arraylist to put them in the Npuzzle.
    public int[] getPuzzle(int width) {
        int highnum = (width * width) - 1;
        Random rand = new Random();
        ArrayList<Integer> holder = new ArrayList<>();
        int[] output = new int[width * width];
        // load an arrayList with numbers that will go into the npuzzle
        for (int i = 1; i <= highnum; i++) {
            holder.add(i);
        }
        // resulting arraylist shoudl be {1,2,3,4,5,6,7,8}
        for (int i = 0; i < (width * width) - 1; i++) {
            if (holder.size() != 1) {
                int plchldr = holder.get(rand.nextInt(holder.size()));
                output[i] = plchldr;
                holder.remove(holder.indexOf(plchldr));
            } else {
                output[i] = holder.get(0);
                holder.remove(0);
            }
        }
        output[highnum] = -1;
        int parity = checkParity(output);
        if (parity % 2 != 0) {
            try {
                if (parity % 2 != 0) {
                    int swap = output[highnum - 1];
                    output[highnum - 1] = output[highnum - 2];
                    output[highnum - 2] = swap;
                }
            } catch (Exception e) {
            }
        }

        return output;
    }

    // checks to see if the goal state and the current state are the same/
    public static boolean isFinished(puzzle puzz) {
        return Arrays.equals(puzz.currentState, puzz.goalState);
    }

    // updates the list of moveable tiles, this will atmost be 4 possible tiles
    // checks in north, east, south, west fashion
    public void updateMoveable() {
        int[] moves = new int[4];
        int place = getlocation(currentState, -1);
        try {
            moves[0] =currentState[place - width];// get North if exsists
        } catch (Exception e) {
        }
        try {
            moves[1] = currentState[place + 1];// get East if exsists
        } catch (Exception e) {
        }
        try {
            moves[2] = currentState[place + width];// get Souht if exsists
        } catch (Exception e) {
        }
        try {
            moves[3] = currentState[place - 1];// get West if exsists
        } catch (Exception e) {
        }
        if (place % width == 0) {
            moves[3] = 0;
        }
        if (((place % (width)) + 1) % width == 0) {
            moves[1] = 0;
        }
        this.moves = moves;
    }

    // helper funtion that returns the location of a string s in a state
    public int getlocation(int[] state, int s) {
        for (int i = 0; i < state.length; i++) {
            
                if (state[i] == s) {
                    return i;
                }
        }
        return -1;
    }

    public void move(int i) {
        int direction = 5;
        int plchldr;
        for (int j = 0; j < moves.length; j++) {
            if (i == moves[j]) {
                direction = j;
                break;
            }
        }

        int ogPlace = getlocation(currentState, -1); // holds the index of the open space
        switch (direction) {
            case 0:// moving north
                plchldr = currentState[ogPlace - this.width];
                currentState[ogPlace - this.width] = -1;
                currentState[ogPlace] = plchldr;
                break;

            case 1:// moving east
                plchldr = currentState[ogPlace + 1];
                currentState[ogPlace + 1] = -1;
                currentState[ogPlace] = plchldr;
                break;

            case 2:// moving south
                plchldr = currentState[ogPlace + this.width];
                currentState[ogPlace + this.width] = -1;
                currentState[ogPlace] = plchldr;
                break;

            case 3:// moving west
                plchldr = currentState[ogPlace - 1];
                currentState[ogPlace - 1] = -1;
                currentState[ogPlace] = plchldr;
                break;

            default:
                break;
        }
        updateMoveable();
        System.out.println(toString(currentState) + "\npossible moves: " + outputMoves());
    }

    public int checkParity(int[] puzzle) {
        int plchldr;
        int parity = 0;
        for (int i = 0; i < puzzle.length - 1; i++) {
            
                plchldr = puzzle[i];
                if (plchldr == -1) {
                    break;
                }
            
            for (int j = i; j < puzzle.length - 1; j++) {
                
                    if (plchldr > puzzle[j]) {
                        parity++;
                    }
                

            }
        }
        return parity;
    }
}
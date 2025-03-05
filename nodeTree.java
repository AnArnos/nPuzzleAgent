
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/*
 *
 * searches I want to implement:
 * breadth first search
 * Bidirectional search
 * greedy best first search
 * a* search
 * beam search
 * depth limited search
 * 
 * and compare effieciencies of all
 * 
 * 
 * the breadth first search is good but it does not do well with larger squares.
 * Im pretty sure that is where goal states really come into play. the breadth first search is an 
 * example of a model based reflex agent, it does not take specific actions to get closer to the goal state
 * it wanders around aimlessly and has no concept of what is a better move or a worse move.
 */

public class nodeTree {
    searchTreeNode root;
    puzzle puzzle;
    HashSet<String> existingStates = new HashSet<>();
    Queue<searchTreeNode> frontier = new LinkedList<>();
    PriorityQueue<searchTreeNode> priorityFrontier = new PriorityQueue<>();
    int nodeCount = 0;

    public nodeTree(puzzle puzzle) {
        this.puzzle = puzzle;
        root = new searchTreeNode(puzzle.currentState, 0,null);
        existingStates.add(Arrays.deepToString(puzzle.currentState));

    }// root WILL NEVER BE NULL and will always have 2 children since the open space
     // is always in the
     // bottom right corner

    public String breadthFirst() {

        frontier.add(root);
        nodeCount++;

        while (!frontier.isEmpty()) {

            searchTreeNode current = frontier.poll();

            current.moves = getMoveable(current);
            for (int j = 0; j < current.moves.length; j++) {
                if (current.moves[j] != 0) {
                    String[][] plchldr = simulatedMove(current.moves[j], current);

                    if (!existingStates.contains(Arrays.deepToString(plchldr))) {
                        current.children.add(new searchTreeNode(plchldr, current.moves[j], current));
                        existingStates.add(Arrays.deepToString(plchldr));

                    }

                }

            }

            for (int i = 0; i < current.children.size(); i++) {
                if (current.children.get(i) != null) {
                    frontier.add(current.children.get(i));
                    nodeCount++;
                    current.children.get(i).previous = current;
                }
                if (isFinished(current.children.get(i).currentState)) { // finished
                    String output = "";
                    System.out.println(output += recursiveToRoot(current) + current.children.get(i).prevMove);
                    return output;
                }
            }

        }

        return "failed";
    }

    //
    public String aStar() {
        // start at root
        priorityFrontier.add(root);
    
        
        nodeCount++;

        while (!priorityFrontier.isEmpty()) {
            // grab next node
            searchTreeNode current = priorityFrontier.poll();
            // System.out.println(printState(current.currentState) + "\n" + nodeCount + "\t" + current.score);
            if (isFinished(current.currentState)) { // finished
                String output = "";
                System.out.println(output += recursiveToRoot(current));
                return output;

            }

            // get moves and therefore number of children
            current.moves = getMoveable(current);

            // for every move/child available, grade the resulting state for each move and
            // fill a
            // priority queue of best or equal to best moves
            for (int j = 0; j < current.moves.length; j++) {
                if (current.moves[j] != 0) {
                    String[][] plchldr = simulatedMove(current.moves[j], current);

                    //is exsisting states does not contain the array already, add it to exsistingstates 
                    if (!existingStates.contains(Arrays.deepToString(plchldr))) {
                        searchTreeNode child = new searchTreeNode(plchldr, current.moves[j], current);
                        current.children.add(child);
                        child.score = gradeState(child);
                        existingStates.add(Arrays.deepToString(plchldr));

                    }

                }

            }

            for (int i = 0; i < current.children.size(); i++) {
                if (current.children.get(i) != null) {
                    nodeCount++;
                    current.children.get(i).previous = current;
                    
                    priorityFrontier.add(current.children.get(i));
                }
                
            }

        }

        return "failed";
    }

    public String[][] simulatedMove(int move, searchTreeNode node) {// [above, below, left, right]
        String[][] copy = new String[puzzle.width][puzzle.height];

        for (int i = 0; i < puzzle.width; i++) {
            for (int j = 0; j < puzzle.height; j++) {
                copy[i][j] = node.currentState[i][j];
            }
        }

        // gets coords
        int x = 0;
        int y = 0;
        for (int a = 0; a < copy.length; a++) {
            for (int j = 0; j < copy[a].length; j++) {
                if (copy[a][j].equals("[]")) {
                    y = a;
                    x = j;
                }
            }
        }

        // checks to see if the input number is in the moveable category, if not we send
        // an error message and spit out the board again
        for (int j = 0; j < node.moves.length; j++) {
            if (node.moves[j] == move) {
                copy[y][x] = String.valueOf(move);

                switch (j) {
                    case 0:
                        copy[y - 1][x] = "[]";
                        return copy;
                    case 1:
                        copy[y + 1][x] = "[]";
                        return copy;
                    case 2:
                        copy[y][x - 1] = "[]";
                        return copy;
                    case 3:
                        copy[y][x + 1] = "[]";
                        return copy;
                    default:
                        return copy;
                }

            }
        }
        return copy;

    }

    // returns a list of the directions {0,0,0,0}
    public int[] getMoveable(searchTreeNode node) {
        // get coordinates of [], at the start it will be (y,x), (2,2)
        int[] moves = new int[4];
        int x = 0;
        int y = 0;
        for (int i = 0; i < node.currentState.length; i++) {
            for (int j = 0; j < node.currentState[i].length; j++) {
                if (node.currentState[i][j].equals("[]")) {
                    y = i;
                    x = j;
                }
            }
        }
        try {
            moves[0] = Integer.valueOf(node.currentState[y - 1][x]);/// above empty
        } catch (Exception e) {

        }
        try {
            moves[1] = Integer.valueOf(node.currentState[y + 1][x]);// below empty
        } catch (Exception e) {

        }
        try {
            moves[2] = Integer.valueOf(node.currentState[y][x - 1]);// left of empty
        } catch (Exception e) {

        }
        try {
            moves[3] = Integer.valueOf(node.currentState[y][x + 1]);// right of empty
        } catch (Exception e) {

        }
        return moves;
    }

    public boolean isFinished(String[][] state) {
        return Arrays.deepEquals(state, this.puzzle.getGoalState(this.puzzle.height, this.puzzle.width));
    }

    public String recursiveToRoot(searchTreeNode node) {
        if (node.previous == null) {
            return "";
        }
        return recursiveToRoot(node.previous) + node.prevMove + ",";
    }

    public String printState(String[][] s) {
        String output = "current state: \n";

        for (int i = 0; i <= s.length-1; i++) {
            for (int j = 0; j < s[i].length; j++) {
                output += s[i][j];
                output += "\t";
            }
            output += "\n";
        }

        return output;
    }

    // finds coords for any given number in the state
    public int[] getCoords(String s, String[][] state) {

        int[] coords = new int[2]; // [y,x]

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                if (state[i][j].equals(s)) {
                    coords[0] = i;
                    coords[1] = j;
                    return coords;
                }
            }
        }
        return coords;
    }

    // for A* we need to evaluate how many steps we have made and how many steps we
    // have to go
    public int gradeState(searchTreeNode node) {
        int currY, currX;
        int goalY, goalX;
        int stepsToGo = 0;
        // recursively figure out how many steps we have made
        int stepsIn = getStepsIteratively(node);

        for (int i = 0; i < node.currentState.length; i++) {
            for (int j = 0; j < node.currentState.length; j++) {
                int[] coords = getCoords(node.currentState[i][j], node.currentState);
                currY = coords[0];
                currX = coords[1];

                coords = getCoords(node.currentState[i][j], this.puzzle.goalState);
                goalY = coords[0];
                goalX = coords[1];

                stepsToGo += (Math.abs(currX - goalX) + Math.abs(currY - goalY));

            }
        }
        return stepsToGo + stepsIn;
    }

    public int getStepsIteratively(searchTreeNode node) {
        int steps = 0;
        while (node.previous != null) {
            steps++;
            node = node.previous;
        }
        return steps;
    }

}

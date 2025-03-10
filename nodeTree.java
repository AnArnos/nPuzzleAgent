
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

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
    searchTreeNode current;
    String[] goalstate;
    puzzle puzzle;
    HashSet<String> existingStates = new HashSet<>();
    Queue<searchTreeNode> frontier = new LinkedList<>();
    Stack<searchTreeNode> depthFrontier = new Stack<>();
    PriorityQueue<searchTreeNode> priorityFrontier = new PriorityQueue<>();
    int nodeCount = 0;
    int width;

    public nodeTree(puzzle puzzle) {
        this.puzzle = puzzle;
        this.goalstate = puzzle.goalState.clone();
        this.width = puzzle.width;
        root = new searchTreeNode(puzzle.currentState, 0, null);
        existingStates.add(getRep(root.currentState));

    }// root WILL NEVER BE NULL and will always have 2 children since the open space
     // is always in the
     // bottom right corner

    public String breadthFirst() {

        frontier.add(root);
        nodeCount++;

        while (!frontier.isEmpty()) {
            current = frontier.poll();
            current.moves = getMoveable(current);

            for (int j = 0; j < current.moves.length; j++) {
                if (current.moves[j] != 0) {
                    String[] plchldr = simulatedMove(current.moves[j], current);    

                    if (!existingStates.contains(getRep(plchldr))) {
                        current.children.add(new searchTreeNode(plchldr, current.moves[j], current));
                        existingStates.add(getRep(plchldr));
                        
                        
                    }
                }

            }
            for (int i = 0; i < current.children.size(); i++) {
                if (current.children.get(i) != null) {
                    frontier.add(current.children.get(i));
                    nodeCount++;
                    current.children.get(i).previous = current;
                }
                if (isFinished(current.children.get(i).currentState)) {
                    String output = "";
                    return output;
            }
            }

        }
        
        return "failed";
    }

    //A*
    public String aStar() {
        // start at root
        priorityFrontier.add(root);

        nodeCount++;

        while (!priorityFrontier.isEmpty()) {
            // grab next node
            searchTreeNode current = priorityFrontier.poll();
            // System.out.println(printState(current.currentState) + "\n" + nodeCount + "\t"
            // + current.score);
            if (isFinished(current.currentState)) { // finished
                String output = "";
                output += recursiveToRoot(current);
                return output;

            }

            // get moves and therefore number of children
            current.moves = getMoveable(current);

            // for every move/child available, grade the resulting state for each move and
            // fill a
            // priority queue of best or equal to best moves
            for (int j = 0; j < current.moves.length; j++) {
                if (current.moves[j] != 0) {
                    String[] plchldr = simulatedMove(current.moves[j], current);

                    // is exsisting states does not contain the array already, add it to
                    // exsistingstates
                    if (!existingStates.contains(getRep(plchldr))) {
                        searchTreeNode child = new searchTreeNode(plchldr, current.moves[j], current);
                        current.children.add(child);
                        child.score = gradeState(child);
                        existingStates.add(getRep(plchldr));

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

    public String[] simulatedMove(int move, searchTreeNode node) {
        String[] copy = node.currentState.clone();
        // gets coords
        int ogPlace = getlocation(node.currentState, "[]");
        String plchldr;

        // checks to see if the input number is in the moveable category, if not we send
        // an error message and spit out the board again
        for (int j = 0; j < node.moves.length; j++) {
            if (node.moves[j] == move) {

                switch (j) {
                    case 0:// moving north
                        plchldr = copy[ogPlace - this.width];
                        copy[ogPlace - this.width] = "[]";
                        copy[ogPlace] = plchldr;
                        break;

                    case 1:// moving east
                        plchldr = copy[ogPlace + 1];
                        copy[ogPlace + 1] = "[]";
                        copy[ogPlace] = plchldr;
                        break;

                    case 2:// moving south
                        plchldr = copy[ogPlace + this.width];
                        copy[ogPlace + this.width] = "[]";
                        copy[ogPlace] = plchldr;
                        break;

                    case 3:// moving west
                        plchldr = copy[ogPlace - 1];
                        copy[ogPlace - 1] = "[]";
                        copy[ogPlace] = plchldr;
                        break;
                    default:
                        return copy;
                }

            }
        }
        return copy;

    }

    // returns a list of the directions {0,0,0,0}
    public int[] getMoveable(searchTreeNode node) {
        int[] moves = new int[4];
        int place = getlocation(node.currentState, "[]");
        try {
            moves[0] = Integer.valueOf(node.currentState[place - width]);// get North if exsists
        } catch (Exception e) {
        }
        try {
            moves[1] = Integer.valueOf(node.currentState[place + 1]);// get East if exsists
        } catch (Exception e) {
        }
        try {
            moves[2] = Integer.valueOf(node.currentState[place + width]);// get Souht if exsists
        } catch (Exception e) {
        }
        try {
            moves[3] = Integer.valueOf(node.currentState[place - 1]);// get West if exsists
        } catch (Exception e) {
        }
        if (place % width == 0) {
            moves[3] = 0;
        }
        if (((place % (width)) + 1) % width == 0) {
            moves[1] = 0;
        }

        return moves;
    }

    // helper funtion that returns the location of a string s in a state
    public int getlocation(String[] state, String s) {
        for (int i = 0; i < state.length; i++) {
            try {
                if (state[i].equals(s)) {
                    return i;
                }
            } catch (Exception e) {
            }
        }
        return -1;
    }

    public boolean isFinished(String[] state) {
        if (getRep(state).equals(getRep(goalstate))) {
            return true;
        }
        return false;
    }

    public String recursiveToRoot(searchTreeNode node) {
        if (node.previous == null) {
            return "";
        }
        return recursiveToRoot(node.previous) + node.prevMove + ",";
    }

    // for A* we need to evaluate how many steps we have made and how many steps we
    // have to go
    public int gradeState(searchTreeNode node) {
        int currY, currX;
        int goalY, goalX;
        int stepsToGo = 0;
        // recursively figure out how many steps we have made
        int stepsIn = getStepsIteratively(node);

        for (int i = 1; i < node.currentState.length; i++) {
            int place = getlocation(node.currentState, String.valueOf(i));
            currX = (place % width);
            currY = (place / width);

            place = getlocation(goalstate,String.valueOf(i));
            goalX = (place % width);
            goalY = (place / width);
            
                
                stepsToGo += (Math.abs(currX - goalX) + Math.abs(currY - goalY));

            
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

    

    //to string helper
    public String getRep(String[] state) {
        String output  = "";
        for (int i = 0; i < state.length; i++) {
            output += state[i] + "\t";
            if ((i+1) % width == 0) {
                output += "\n";
            }

        }
        return output;
    }

}

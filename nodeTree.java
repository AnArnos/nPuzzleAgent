import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class nodeTree {
    int width;
    searchTreeNode root;
    searchTreeNode current;
    int nodeCount;
    int[] goalstate;
    puzzle puzzle;

    HashMap<String, Integer> existingStates = new HashMap<String, Integer>();
    Queue<searchTreeNode> frontier = new LinkedList<>();
    PriorityQueue<searchTreeNode> priorityFrontier = new PriorityQueue<>();

    HashMap<String, Integer> gradesStor = new HashMap<String, Integer>();
    HashMap<String, Integer> newGradesStor = new HashMap<String, Integer>();

    HashMap<String, Integer> firstStor = new HashMap<String, Integer>();
    HashMap<String, Integer> newFirstStor = new HashMap<String, Integer>();
    HashMap<String, Integer> secondStor = new HashMap<String, Integer>();
    HashMap<String, Integer> newSecondStor = new HashMap<String, Integer>();

    File first = new File("src\\1234.txt");
    File second = new File("src\\5678.txt");
    BufferedWriter writer1 = new BufferedWriter(new FileWriter(first,true));
    BufferedWriter writer2 = new BufferedWriter(new FileWriter(second,true));
    

    Scanner reader;

    public nodeTree(puzzle puzzle) throws IOException {
        // declarations
        this.puzzle = puzzle;
        this.goalstate = puzzle.goalState.clone();
        this.width = puzzle.width;
        root = new searchTreeNode(puzzle.currentState, 0, 0, null);
        existingStates.put(getRep(root.currentState), 0);
    

        reader = new Scanner(first);

        while (reader.hasNextLine()) {
            try {
                String s = reader.nextLine();
                int[] state = new int[this.width * this.width];
                String[] sAr = s.split(" ");
                for (int i = 0; i < sAr.length; i++) {
                    int plchldr = Integer.parseInt(sAr[i]);
                    state[i] = plchldr;
                }
                String score = reader.nextLine();
                firstStor.put(getRep(state), Integer.parseInt(score));
            } catch (Exception e) {

            }

        }
        reader.close();

        reader = new Scanner(second);

        while (reader.hasNextLine()) {
            try {
                String s = reader.nextLine();
                int[] state = new int[this.width * this.width];
                String[] sAr = s.split(" ");
                for (int i = 0; i < sAr.length; i++) {
                    int plchldr = Integer.parseInt(sAr[i]);
                    state[i] = plchldr;
                }
                String score = reader.nextLine();
                secondStor.put(getRep(state), Integer.parseInt(score));
            } catch (Exception e) {

            }

        }
        reader.close();
    }

    public String breadthFirst() {

        frontier.add(root);
        nodeCount++;

        while (!frontier.isEmpty()) {
            current = frontier.poll();
            current.moves = getMoveable(current);

            for (int j = 0; j < current.moves.length; j++) {
                if (current.moves[j] != 0) {
                    int[] plchldr = simulatedMove(current.moves[j], current);
                    //create an int[] and simulate moves for each state

                    if (!existingStates.containsKey(getRep(plchldr))) {
                        //if states arent represented in the database, add it to database
                        current.children.add(new searchTreeNode(plchldr, current.moves[j], current.depth + 1, current));
                        existingStates.put(getRep(plchldr),current.depth + 1 );
                    }
                }

            }
            for (int i = 0; i < current.children.size(); i++) {
                if (current.children.get(i) != null) {
                    frontier.add(current.children.get(i));//add to frontier
                    nodeCount++;
                    current.children.get(i).previous = current;
                }
                if (isFinished(current.children.get(i).currentState)) {
                    String output = "";
                    output += recursiveToRoot(current);
                    return output;
                }
            }

        }

        return "failed";
    }

    public String aStar() throws IOException {
        Double w1 = 1.0;
        Double w2 = 1.5;
        // start at root
        priorityFrontier.add(root);

        nodeCount++;

        while (!priorityFrontier.isEmpty()) {
            // grab next node
            searchTreeNode current = priorityFrontier.poll();
            // System.out.println(printState(current.currentState) + "\n" + nodeCount + "\t"
            // + current.score);
            if (isFinished(current.currentState)) { // finished
                gradeStateDB(current, w1, w2);

                // // write all the new states found to the databases
                // for (HashMap.Entry<String, Integer> entry : newFirstStor.entrySet()) {
                // writer1.append(entry.getKey() + "\n");
                // writer1.append(entry.getValue() + "\n");
                // }
                // writer1.flush();newFirstStor.clear();
                

                // // second database
                // for (HashMap.Entry<String, Integer> entry : newSecondStor.entrySet()) {
                // writer2.append(entry.getKey() + "\n");
                // writer2.append(entry.getValue() + "\n");
                // }
                // writer2.flush();
                // newSecondStor.clear();

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
                    int[] plchldr = simulatedMove(current.moves[j], current);

                    // is exsisting states does not contain the array already, add it to
                    // exsistingstates
                    if (!existingStates.containsKey(getRep(plchldr))) {
                        searchTreeNode child = new searchTreeNode(plchldr, current.moves[j], current.depth + 1, current);
                        current.children.add(child);
                        child.score = gradeStateDB(child, w1, w2);
                        existingStates.put(getRep(plchldr), current.depth + 1);

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

    // IDA*
    public String IDAStar() throws IOException {
        current = root;
        Double threshold = gradeState(current, 1.0,1.0);

        while (true) {
        existingStates.put(getRep(root.currentState), root.depth);

            String plchldr = thresholdSearch(current, threshold);

            if(!plchldr.contains(",")){
                threshold = Double.valueOf(plchldr);
                // System.out.println(threshold);
                continue;
            } else {
                //we got em boys
                // System.out.println(plchldr);
                return plchldr;
            }
            

        }

    }

    // helper function for IDA*
    public String thresholdSearch(searchTreeNode node, Double threshold) throws IOException {
        Double w1 = .8;
        Double w2 = 4.1;
        double nextLowest = 1000000;
        priorityFrontier.add(node);

        while (!priorityFrontier.isEmpty()) {
            current = priorityFrontier.poll();
            // System.out.println(getRep(current.currentState));

            if (isFinished(current.currentState)) {
                String output = recursiveToRoot(current);
                return output;
            }
            Double score = gradeStateDB(current, w1, w2);
            // Prune nodes with f(n) > threshold and track the next lowest f(n) that needs to be expanded
            if (score > threshold) {
                nextLowest = Math.min(nextLowest, score); // Track next threshold
                continue; // Do NOT expand this node
            }
                current.moves = getMoveable(current);

                for (int j = 0; j < current.moves.length; j++) {
                    if (current.moves[j] != 0) {
                        int[] plchldr = simulatedMove(current.moves[j], current);

                        if (!existingStates.containsKey(getRep(plchldr))) {
                            searchTreeNode child = new searchTreeNode(plchldr,  current.moves[j],current.depth + 1, current);
                            child.depth = current.depth + 1;  // Increment g-value (depth + 1)
                            current.children.add(child);
                            existingStates.put(getRep(plchldr), child.depth);  // Mark this state as visited

                        }
                    }
                }
                // if children exsist and are under the threshold, add the to the queue
                for (int i = 0; i < current.children.size(); i++) {
                    if (current.children.get(i) != null) {
                        priorityFrontier.add(current.children.get(i));
                        nodeCount++;
                        current.children.get(i).previous = current;
                    }

                }
        }

        
        return String.valueOf(nextLowest);
    }    

    public int[] simulatedMove(int move, searchTreeNode node) {
        int[] copy = node.currentState.clone();
        // gets coords
        int ogPlace = getlocation(node.currentState, -1);
        int plchldr;

        // checks to see if the input number is in the moveable category, if not we send
        // an error message and spit out the board again
        for (int j = 0; j < node.moves.length; j++) {
            if (node.moves[j] == move) {

                switch (j) {
                    case 0:// moving north
                        plchldr = copy[ogPlace - this.width];
                        copy[ogPlace - this.width] = -1;
                        copy[ogPlace] = plchldr;
                        break;

                    case 1:// moving east
                        plchldr = copy[ogPlace + 1];
                        copy[ogPlace + 1] = -1;
                        copy[ogPlace] = plchldr;
                        break;

                    case 2:// moving south
                        plchldr = copy[ogPlace + this.width];
                        copy[ogPlace + this.width] = -1;
                        copy[ogPlace] = plchldr;
                        break;

                    case 3:// moving west
                        plchldr = copy[ogPlace - 1];
                        copy[ogPlace - 1] = -1;
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
        int place = getlocation(node.currentState, -1);
        try {
            moves[0] = node.currentState[place - width];// get North if exsists
        } catch (Exception e) {
        }
        try {
            moves[1] = node.currentState[place + 1];// get East if exsists
        } catch (Exception e) {
        }
        try {
            moves[2] = node.currentState[place + width];// get Souht if exsists
        } catch (Exception e) {
        }
        try {
            moves[3] = node.currentState[place - 1];// get West if exsists
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

    // this funciton recursively follows parent nodes to the root and documents them
    public String recursiveToRoot(searchTreeNode node) {
        if (node.previous == null) {
            return "";
        }
        return recursiveToRoot(node.previous) + node.prevMove + ",";
    }

    // helper funtion that returns the location of a string s in a state
    public int getlocation(int[] state, int s) {
        for (int i = 0; i < state.length; i++) {
            try {
                if (state[i] == s) {
                    return i;
                }
            } catch (Exception e) {
            }
        }
        return -1;
    }

    // to string helper
    public String getRep(int[] state) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < state.length; i++) {
            sb.append(state[i]).append(" ");
        }
        // Remove trailing space
        return sb.toString().trim();
    }

    // gets the rep but only fills in the location of the first half, everything
    // else is zeros
    public String getRepFirst(int[] state) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < state.length; i++) {
            if (state[i] >= 1 && state[i] <= state.length / 2) {
                sb.append(state[i]).append(" ");
            } else {
                sb.append(0).append(" ");
            }

        }
        // Remove trailing space
        return sb.toString().trim();
    }

    // gets the rep but only fills in the location of the second half, everything
    // else is zeroes
    public String getRepSecond(int[] state) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < state.length; i++) {
            if (state[i] > state.length / 2 && state[i] <= state.length) {
                sb.append(state[i]).append(" ");
            } else {
                sb.append(0).append(" ");
            }

        }
        // Remove trailing space
        return sb.toString().trim();
    }

    public boolean isFinished(int[] state) {
        if (getRep(state).equals(getRep(goalstate))) {
            return true;
        }
        return false;
    }

    // for A* we need to evaluate how many steps we have made and how many steps we
    // have to go
    public double gradeState(searchTreeNode node, double w1, double w2) throws IOException {
        if (!gradesStor.containsKey(getRep(node.currentState))) {
            System.out.println("not hit");
            int currY, currX;
            int goalY, goalX;
            int stepsToGo = 0;
            // recursively figure out how many steps we have made
            int stepsIn = getStepsIteratively(node);

            for (int i = 1; i < node.currentState.length; i++) {
                int place = getlocation(node.currentState, i);
                currX = (place % width);
                currY = (place / width);

                place = getlocation(goalstate, i);
                goalX = (place % width);
                goalY = (place / width);

                stepsToGo += (Math.abs(currX - goalX) + Math.abs(currY - goalY));

            }

            double score = (w2 * stepsToGo) + (w1 * stepsIn);
            newGradesStor.put(getRep(node.currentState), stepsToGo);
            return score;
        } else {

            return (w1 * getStepsIteratively(node)) + (w2 * (int) gradesStor.get(getRep(node.currentState)));
        }

    }

    public Double gradeStateDB(searchTreeNode node, Double w1, Double w2) throws IOException {
        return (double) (w2 * (firstDB(node) + secondDB(node)) + (w1 * getStepsIteratively(node)));

    }

    // First and Second DB divide the state in two, for an 8 puzzle this results in
    // an 1234 and a 5678 database.
    // idk about 15 puzzles
    public int firstDB(searchTreeNode node) throws FileNotFoundException, IOException {
        String s = getRepFirst(node.currentState);
        if (!firstStor.containsKey(s)) {
            int currY, currX;
            int goalY, goalX;
            int stepsToGo = 0;
            // recursively figure out how many steps we have made

            for (int i = 1; i < node.currentState.length / 2; i++) {

                int place = getlocation(node.currentState, i);
                currX = (place % width);
                currY = (place / width);

                place = getlocation(goalstate, i);
                goalX = (place % width);
                goalY = (place / width);

                stepsToGo += (Math.abs(currX - goalX) + Math.abs(currY - goalY));

            }

            firstStor.put(getRepFirst(node.currentState), stepsToGo);
            
            //write to file
            writer1.append(getRepFirst(node.currentState) + "\n");
            writer1.append(Integer.toString(stepsToGo) + "\n");
            writer1.flush();
            
            
            return stepsToGo;
        } else {
            return firstStor.get(getRepFirst(node.currentState));
        }
    }

    public int secondDB(searchTreeNode node) throws FileNotFoundException, IOException {
        String s = getRepSecond(node.currentState);
        if (!secondStor.containsKey(s)) {
            int currY, currX;
            int goalY, goalX;
            int stepsToGo = 0;
            // recursively figure out how many steps we have made

            for (int i = (node.currentState.length + 2) / 2; i < node.currentState.length; i++) {

                int place = getlocation(node.currentState, i);
                currX = (place % width);
                currY = (place / width);

                place = getlocation(goalstate, i);
                goalX = (place % width);
                goalY = (place / width);

                stepsToGo += (Math.abs(currX - goalX) + Math.abs(currY - goalY));

            }

            secondStor.put(getRepSecond(node.currentState), stepsToGo);
            
            //write to file

            writer2.append( s + "\n");
            writer2.append(Integer.toString(stepsToGo) + "\n");
            writer2.flush();
            
            return stepsToGo;
        } else {
            return secondStor.get(getRepSecond(node.currentState));
        }
    }

    public int getStepsIteratively(searchTreeNode node) {
        int steps = 0;
        while (node.previous != null) {
            steps++;
            node = node.previous;
        }
        return steps;
    }

    // helper that outputs a string numbers that represents a state
    // output for a completed 8puzzle will look like
    // -1 1 2 3 4 5 6 7 8
    // 0
    public String print(int[] state) {
        String output = "";
        for (int i = 0; i < state.length; i++) {
            output += state[i] + " ";
        }

        return output;
    }

}
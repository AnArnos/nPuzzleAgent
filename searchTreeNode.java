
import java.util.ArrayList;

public class searchTreeNode implements Comparable<searchTreeNode> {
    // nodes will hold a move, a state, and a link to dependent states
    int prevMove;
    int[] currentState;
    int[] moves = new int[4];
    ArrayList<searchTreeNode> children = new ArrayList<>();
    searchTreeNode previous;
    Double score;
    int depth;

    

    public searchTreeNode(int[] state, int prevMove, int depth, searchTreeNode prevNode) {
        this.currentState = state;
        this.prevMove = prevMove;
        this.moves = null;
        previous = prevNode;
        this.score = 0.0;
        this.depth = depth;

    }

    public String printState() {
        String output = "";
        output += getRep(currentState) +"\n";
        output += "possible moves : ";
        for (int i = 0; i < 4; i++) {
            if (moves[i] != 0) {
                output += (moves[i] + ", ");
            }
        }
        return output;
    }

    //to string helper
    public String getRep(int[] state) {
        String output  = "";
        for (int i = 0; i < state.length; i++) {
            output += state[i] + "\t";
            if ((i+1)%3 == 0) {
                output += "\n";
            }

        }
        return output;
    }


    @Override
    public int compareTo(searchTreeNode other) {
        return Double.compare(this.score, other.score);

    }

    @Override
    public String toString(){
        String output = "";
        for (int i = 0; i < currentState.length; i++) {
            output += currentState[i];
        }
        return output;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}


import java.util.ArrayList;
import java.util.Arrays;

public class searchTreeNode implements Comparable<searchTreeNode> {
    // nodes will hold a move, a state, and a link to dependent states
    int prevMove;
    String[][] currentState;
    int[] moves = new int[4];
    ArrayList<searchTreeNode> children = new ArrayList<>();
    searchTreeNode previous;
    int score;

    public searchTreeNode(String[][] state, int prevMove, searchTreeNode prevNode) {
        this.currentState = state;
        this.prevMove = prevMove;
        this.moves = null;
        previous = prevNode;
        this.score = 0;

    }

    public String toString() {
        String output = "";
        output += Arrays.deepToString(currentState) + "\n";
        output += "possible moves : ";
        for (int i = 0; i < 4; i++) {
            if (moves[i] != 0) {
                output += (moves[i] + ", ");
            }
        }
        return output;
    }

    public Double getScore(String[][] state) {
        Double score;
        Double total = 0.0;
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                try {
                    list.add(Integer.valueOf(state[i][j]));
                } catch (NumberFormatException e) {

                }
            }
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            total += (list.get(i) * (i + 1));
        }

        score = total / 204;
        return score;
    }

    @Override
    public int compareTo(searchTreeNode other) {
        return Double.compare(this.score, other.score);

    }
}

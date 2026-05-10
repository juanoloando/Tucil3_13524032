package algorithm;
import model.State;

public class PriorityQueueNode implements Comparable<PriorityQueueNode> {
    private final State state;
    private final double priority;

    public PriorityQueueNode(State state, double priority) {
        this.state = state;
        this.priority = priority;
    }

    public State getState() {
        return state;
    }

    public double getPriority() {
        return priority;
    }

    @Override
    public int compareTo(PriorityQueueNode other) {
        return Double.compare(this.priority, other.priority);
    }
}
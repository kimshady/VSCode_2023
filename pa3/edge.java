public class edge {
    private Node dest;
    private int weight;

    // constructor
    public edge(Node dest, int weight) {
        this.dest = dest;
        this.weight = weight;
    }

    // node destination
    public Node getDest() {
        return dest;
    }

    // weight/value of edge
    public int getWeight() {
        return weight;
    }
}

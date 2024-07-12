import java.util.List;
import java.util.ArrayList;


public class Node {
    private int id;
    private int distance;
    
    private List<edge> edges;
    
    private Node parent;

    public Node(int id) {
        this.id = id;
        this.edges = new ArrayList<>();
        this.distance = 99999999;
        this.parent = null;
    }

    // unique node ID
    public int getId() {
        return id;
    }

    // edges connected to node
    public List<edge> getEdges() {
        return edges;
    }

    // adding valid edge to list of existing edges
    public void addEdge(Node dest, int weight) {
        edges.add(new edge(dest, weight));
    }

    // getters
    public int getDistance() {
        return distance;
    }
    public Node getParent() {
        return parent;
    }
    
    // setters
    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}

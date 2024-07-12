import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class linkstate {
// constant for a big value, Integer.maxvalue would cause -infinity and cause bugs with printing.
private static final int beeg = 99999999;

// list to store nodes
private List<Node> nodes;

// constructor--initialize list of nodes
public linkstate() {
    nodes = new ArrayList<>();
}

// add a node to the list of nodes
private void addNode(int id) {
    nodes.add(new Node(id));
}

// get a node by id from the list of nodes
private Node getNodeById(int id) {
    for (Node node : nodes) {
        if (node.getId() == id) {
            return node;
        }
    }
    return null;
}

// add an edge between two nodes with a given weight
private void addEdge(int srcId, int destId, int weight) {
    Node srcNode = getNodeById(srcId);
    Node destNode = getNodeById(destId);
    srcNode.addEdge(destNode, weight);
    destNode.addEdge(srcNode, weight);
}

// initialize the distances of all nodes to infinity except the source node
private void initializeSingleSource(int sourceId) {
    for (Node node : nodes) {
        node.setDistance(beeg);
        node.setParent(null);
    }
    getNodeById(sourceId).setDistance(0);
}

// shortens the distance between two nodes
private void shorten(Node srcNode, edge edge) {
    Node destNode = edge.getDest();
    int newDistance = srcNode.getDistance() + edge.getWeight();
    if (newDistance < destNode.getDistance()) {
        destNode.setDistance(newDistance);
        destNode.setParent(srcNode);
    }
}

// Dijkstras algorithm
private void dijkstra(int sourceId) {
    initializeSingleSource(sourceId);
    PriorityQueue<Node> pq = new PriorityQueue<>((n1, n2) -> n1.getDistance() - n2.getDistance());
    pq.addAll(nodes);
    while (!pq.isEmpty()) {
        Node srcNode = pq.poll();
        for (edge edge : srcNode.getEdges()) {
            Node destNode = edge.getDest();
            if (pq.contains(destNode)) {
                shorten(srcNode, edge);
            }
        }
    }
}

// print the routing table
private void printRoutingTable() {
    System.out.print("Step,D1,P1,D2,P2,D3,P3,D4,P4,...\n");

    for (int i = 1; i < nodes.size(); i++) {
        dijkstra(i);
        String output = i + ",";
        for (Node node : nodes) {
            if (node.getDistance() == beeg) {
                output += "0,";
            } else {
                output += node.getDistance() + ",";
            }
            if (node.getParent() != null) {
                output += node.getParent().getId() + ",";
            } else {
                output += "-,";
            }
        }
        output = output.substring(0, output.length() - 1);
        System.out.println(output);

        // print shortest paths for each node
        for (Node node : nodes) {
            if (node.getId() != i) {
                List<Node> path = getPath(node);
                System.out.print("Shortest path from node " + i + " to node " + node.getId() + ": ");
                for (Node pathNode : path) {
                    System.out.print(pathNode.getId() + " ");
                }
                System.out.println("(distance: " + node.getDistance() + ")");
            }
        }
        System.out.println();
    }
}

// get the shortest path from the source node to the specified node
private List<Node> getPath(Node destNode) {
    List<Node> path = new ArrayList<>();
    while (destNode != null) {
        path.add(destNode);
        destNode = destNode.getParent();
    }
    // reverse the list to get the path from source to destination
    Collections.reverse(path);
    return path;
}


// read input file and run the algorithm
public static void main(String[] args) {
    // check if file name is provided as an argument
    if (args.length == 0) {
        System.out.println("incorrect argument");
            return;
        }
        String fileName = args[0];
        linkstate ls = new linkstate();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                // appropriately parse node data
                int srcId = Integer.parseInt(parts[0]);
                int destId = Integer.parseInt(parts[1]);
                int weight = Integer.parseInt(parts[2]);
                if (ls.getNodeById(srcId) == null) {
                    ls.addNode(srcId);
                }
                if (ls.getNodeById(destId) == null) {
                    ls.addNode(destId);
                }
                ls.addEdge(srcId, destId, weight);
            }
            ls.printRoutingTable();
        } catch (IOException e) {
            System.out.println("Error occured when reading file" + e.getMessage());
        }
    }
}
    
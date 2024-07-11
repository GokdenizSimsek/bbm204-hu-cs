import java.util.*;

class Graph {
    private Map<String, List<Edge>> adjList = new HashMap<>();

    public void addEdge(String from, String to, double weight) {
        adjList.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, weight));
        adjList.computeIfAbsent(to, k -> new ArrayList<>()).add(new Edge(from, weight)); // For undirected graph
    }

    public List<Edge> getEdges(String node) {
        return adjList.get(node);
    }

    public Set<String> getNodes() {
        return adjList.keySet();
    }

    public boolean edgeExists(String from, String to) {
        return adjList.containsKey(from) && adjList.get(from).stream().anyMatch(edge -> edge.node.equals(to));
    }

    public boolean hasEdgeWithSameWeight(String from, String to, double weight) {
        if (adjList.containsKey(from)) {
            for (Edge edge : adjList.get(from)) {
                if (edge.node.equals(to) && edge.weight == weight) {
                    return true;
                }
            }
        }
        return false;
    }
}

class Edge {
    String node;
    double weight;

    Edge(String node, double weight) {
        this.node = node;
        this.weight = weight;
    }
}
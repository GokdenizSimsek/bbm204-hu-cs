import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

class Station {
    int x, y;

    public Station(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Edge implements Comparable<Edge> {
    int u, v;
    double weight;

    public Edge(int u, int v, double weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }
}

public class Quiz3 {
    public static void main(String[] args) throws IOException {
        // Use the first command line argument (args[0]) as the file to read the input from
        String filename = args[0];
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        int numTestCases = Integer.parseInt(reader.readLine());

        for (int t = 0; t < numTestCases; t++) {
            String[] sp = reader.readLine().split(" ");
            int S = Integer.parseInt(sp[0]); // Number of stations equipped with water drone system
            int P = Integer.parseInt(sp[1]); // Total number of stations
            ArrayList<Station> stations = new ArrayList<>();

            // Read station coordinates
            for (int i = 0; i < P; i++) {
                String[] coordinates = reader.readLine().split(" ");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                stations.add(new Station(x, y));
            }
            // Build the graph

            ArrayList<Edge> edges = new ArrayList<>();
            for (int i = 0; i < P; i++) {
                for (int j = i + 1; j < P; j++) {
                    double distance = calculateDistance(stations.get(i), stations.get(j));
                    edges.add(new Edge(i, j, distance));
                }
            }

            // Kruskal's algorithm to find minimum spanning tree
            double minT = kruskal(edges, S);

            // Print the solution
            System.out.printf("%.2f\n", minT);
        }

        reader.close();
    }

    // Function to calculate Euclidean distance between two stations
    private static double calculateDistance(Station station1, Station station2) {
        int dx = station1.x - station2.x;
        int dy = station1.y - station2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Kruskal's algorithm to find minimum spanning tree
    private static double kruskal(ArrayList<Edge> edges, int S) {
        int[] parent = new int[edges.size()];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }

        PriorityQueue<Edge> pq = new PriorityQueue<>(edges);

        int components = edges.size();
        double minT = 0;

        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            int uParent = findParent(parent, edge.u);
            int vParent = findParent(parent, edge.v);
            if (uParent != vParent) {
                parent[uParent] = vParent;
                components--;
                if (components == S) {
                    minT = edge.weight;
                    break;
                }
            }
        }

        return minT;
    }

    // Function to find the parent of a vertex in disjoint set
    private static int findParent(int[] parent, int vertex) {
        if (parent[vertex] != vertex) {
            parent[vertex] = findParent(parent, parent[vertex]);
        }
        return parent[vertex];
    }
}
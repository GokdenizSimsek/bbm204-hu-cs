import java.io.Serializable;
import java.util.*;

class UrbanTransportationApp implements Serializable {
    static final long serialVersionUID = 99L;
    
    public HyperloopTrainNetwork readHyperloopTrainNetwork(String filename) {
        HyperloopTrainNetwork hyperloopTrainNetwork = new HyperloopTrainNetwork();
        hyperloopTrainNetwork.readInput(filename);
        return hyperloopTrainNetwork;
    }

    /**
     * Function calculate the fastest route from the user's desired starting point to 
     * the desired destination point, taking into consideration the hyperloop train
     * network. 
     * @return List of RouteDirection instances
     */
    public List<RouteDirection> getFastestRouteDirections(HyperloopTrainNetwork network) {

        // TODO: Your code goes here

        Graph graph = new Graph();
        Map<String, Point> stationCoordinates = new HashMap<>();
        List<String> stationNames = new ArrayList<>();

        // Create graph from network
        for (TrainLine line : network.lines) {
            for (int i = 0; i < line.trainLineStations.size() - 1; i++) {
                Station s1 = line.trainLineStations.get(i);
                Station s2 = line.trainLineStations.get(i + 1);

                double distance = calculateDistance(s1.coordinates, s2.coordinates);
                double time = distance / network.averageTrainSpeed; // in minutes

                graph.addEdge(s1.description, s2.description, time);
                stationCoordinates.put(s1.description, s1.coordinates);
                stationCoordinates.put(s2.description, s2.coordinates);
                stationNames.add(s1.description);
                stationNames.add(s2.description);
            }
        }
        stationCoordinates.put(network.startPoint.description, network.startPoint.coordinates);
        stationCoordinates.put(network.destinationPoint.description, network.destinationPoint.coordinates);

        List<List<String>> sEdgeList = new ArrayList<>();
        List<Double> sEdgeTimeList = new ArrayList<>();
        List<List<String>> fEdgeList = new ArrayList<>();
        List<Double> fEdgeTimeList = new ArrayList<>();
        // Add start and final point connections
        for (String node1 : graph.getNodes()) {
            if (!node1.equals(network.startPoint.description)) {
                double startDistance = calculateDistance(network.startPoint.coordinates, stationCoordinates.get(node1));
                double walkingTimeS = startDistance / network.averageWalkingSpeed; // in minutes
                if (!graph.hasEdgeWithSameWeight(network.startPoint.description, node1, walkingTimeS)) {
                    List<String> tempList = new ArrayList<>();
                    tempList.add(network.startPoint.description);
                    tempList.add(node1);
                    sEdgeList.add(tempList);
                    sEdgeTimeList.add(walkingTimeS);
                }
            }
            if (!node1.equals(network.destinationPoint.description)) {
                double finalDistance = calculateDistance(network.destinationPoint.coordinates, stationCoordinates.get(node1));
                double walkingTimeF = finalDistance / network.averageWalkingSpeed; // in minutes
                if (!graph.hasEdgeWithSameWeight(network.destinationPoint.description, node1, walkingTimeF)) {
                    List<String> tempList = new ArrayList<>();
                    tempList.add(network.destinationPoint.description);
                    tempList.add(node1);
                    fEdgeList.add(tempList);
                    fEdgeTimeList.add(walkingTimeF);
                }
            }
        }
        for (int i = 0; i < sEdgeList.size(); i++) {
            graph.addEdge(sEdgeList.get(i).get(0), sEdgeList.get(i).get(1), sEdgeTimeList.get(i));
            graph.addEdge(fEdgeList.get(i).get(0), fEdgeList.get(i).get(1), fEdgeTimeList.get(i));
        }

        double distance1 = calculateDistance(network.startPoint.coordinates, network.destinationPoint.coordinates);
        double walkingTime1 = distance1 / network.averageWalkingSpeed; // in minutes
        graph.addEdge(network.startPoint.description, network.destinationPoint.description, walkingTime1);

        // Add walking connections
        for (String node1 : graph.getNodes()) {
            for (String node2 : graph.getNodes()) {
                if (!node1.equals(node2)) {
                    double distance = calculateDistance(stationCoordinates.get(node1), stationCoordinates.get(node2));
                    double walkingTime = distance / network.averageWalkingSpeed; // in minutes
                    if (!graph.hasEdgeWithSameWeight(node1, node2, walkingTime)) {
                        graph.addEdge(node1, node2, walkingTime);
                    }
                }
            }
        }

        // Use Dijkstra to find the shortest path
        String start = network.startPoint.description;
        String end = network.destinationPoint.description;

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(edge -> edge.weight));

        for (String node : graph.getNodes()) {
            distances.put(node, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);
        pq.add(new Edge(start, 0.0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            String currentNode = current.node;

            if (currentNode.equals(end)) break;

            for (Edge edge : graph.getEdges(currentNode)) {
                double newDist = distances.get(currentNode) + edge.weight;
                if (newDist < distances.get(edge.node)) {
                    distances.put(edge.node, newDist);
                    previous.put(edge.node, currentNode);
                    pq.add(new Edge(edge.node, newDist));
                }
            }
        }

        // Build the route directions
        List<RouteDirection> routeDirections = new ArrayList<>();
        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            double duration = distances.get(to) - distances.get(from);
            boolean trainRide = isTrainRide(from, to, network);
            routeDirections.add(new RouteDirection(from, to, duration, trainRide));
        }

        return routeDirections;
    }

    private double calculateDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    private boolean isTrainRide(String from, String to, HyperloopTrainNetwork network) {
        for (TrainLine line : network.lines) {
            for (int i = 0; i < line.trainLineStations.size() - 1; i++) {
                Station s1 = line.trainLineStations.get(i);
                Station s2 = line.trainLineStations.get(i + 1);
                if ((s1.description.equals(from) && s2.description.equals(to)) ||
                        (s1.description.equals(to) && s2.description.equals(from))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {
        
        // TODO: Your code goes here

        double totalDuration = 0.0;
        for (RouteDirection direction : directions) {
            totalDuration += direction.duration;
        }

        System.out.printf("The fastest route takes %d minute(s).\n", Math.round(totalDuration));
        System.out.println("Directions");
        System.out.println("----------");

        for (RouteDirection direction : directions) {
            String action = direction.trainRide ? "Get on the train" : "Walk";
            System.out.printf("%d. %s from \"%s\" to \"%s\" for %.2f minutes.\n",
                    directions.indexOf(direction) + 1, action, direction.startStationName, direction.endStationName, direction.duration);
        }
    }
}
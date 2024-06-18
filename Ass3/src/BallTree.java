import java.util.*;

public class BallTree {

    // Inner class representing a node in the Ball Tree
    public static class Node {
        double[] point; // The data point stored in this node
        Node child1, child2; // Child Nodes
        double radius; // radius defining the ball

        //Constructor for the leaf node
        Node(double[] point) {
            this.point = point;
        }

        //Constructor for the internal node
        Node(double[] point, Node child1, Node child2, double radius) {
            this.point = point;
            this.child1 = child1;
            this.child2 = child2;
            this.radius = radius;
        }
    }

    // Inner class to represent a point and its distance from a target point
    static class PointDistance {
        double[] point; // defining the data point
        double distance; // Distance from the target point

        PointDistance(double[] point, double distance) {
            this.point = point;
            this.distance = distance;
        }
    }

    //Method constructing the Ball Tree from a list of data points
    public static Node constructBallTree(List<double[]> dataPoints) {
        if (dataPoints.isEmpty()) {
            return null; // Return null if dataPoints is empty
        }

        if (dataPoints.size() == 1) {
            return new Node(dataPoints.get(0)); // Return a leaf node if only one point exists
        }

        int dimension = dataPoints.get(0).length; // defining dimension
        int greatestSpreadDimension = findGreatestSpreadDimension(dataPoints, dimension);
        dataPoints.sort(Comparator.comparingDouble(p -> p[greatestSpreadDimension]));
        int medianIndex = dataPoints.size() / 2;
        double[] pivot = dataPoints.get(medianIndex); // defining pivot position

        // data points seperated by two lists
        List<double[]> leftSet = dataPoints.subList(0, medianIndex);
        List<double[]> rightSet = dataPoints.subList(medianIndex + 1, dataPoints.size());

        Node child1 = constructBallTree(new ArrayList<>(leftSet));
        Node child2 = constructBallTree(new ArrayList<>(rightSet));

        double radius = 0;
        for (double[] point : dataPoints) {
            double distance = euclideanDistance(pivot, point);
            if (distance > radius) {
                radius = distance;
            }
        }

        return new Node(pivot, child1, child2, radius);
    }

    // Method to find the dimension with the greatest spread
    private static int findGreatestSpreadDimension(List<double[]> dataPoints, int dimension) {
        double[] min = new double[dimension];
        double[] max = new double[dimension];
        Arrays.fill(min, Double.MAX_VALUE);
        Arrays.fill(max, Double.MIN_VALUE);

        for (double[] point : dataPoints) {
            for (int i = 0; i < dimension; i++) {
                if (point[i] < min[i]) {
                    min[i] = point[i];
                }
                if (point[i] > max[i]) {
                    max[i] = point[i];
                }
            }
        }

        int greatestSpreadDimension = 0;
        double greatestSpread = 0;
        for (int i = 0; i < dimension; i++) {
            double spread = max[i] - min[i];
            if (spread > greatestSpread) {
                greatestSpread = spread;
                greatestSpreadDimension = i;
            }
        }

        return greatestSpreadDimension;
    }

    // Method to find the dimension with the greatest spread
    private static double euclideanDistance(double[] p1, double[] p2) {
        double sum = 0;
        for (int i = 0; i < p1.length; i++) {
            double diff = p1[i] - p2[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    // Method to find the k nearest neighbors of a target point using a Ball Tree
    public static List<double[]> kNearestNeighbors(Node root, double[] target, int k) {
        PriorityQueue<PointDistance> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> -a.distance));
        kNearestNeighbors(root, target, k, pq);
        List<double[]> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(pq.poll().point);
        }
        Collections.reverse(result);
        return result;
    }

    // Helper method for kNearestNeighbors
    private static void kNearestNeighbors(Node node, double[] target, int k, PriorityQueue<PointDistance> pq) {
        if (node == null) {
            return;
        }

        double distance = euclideanDistance(node.point, target);
        if (pq.size() < k) {
            pq.add(new PointDistance(node.point, distance));
        } else if (distance < pq.peek().distance) {
            pq.poll();
            pq.add(new PointDistance(node.point, distance));
        }

        if (node.child1 != null || node.child2 != null) {
            double distanceToPivot = euclideanDistance(node.point, target);
            if (node.child1 != null && distanceToPivot - node.child1.radius <= pq.peek().distance) {
                kNearestNeighbors(node.child1, target, k, pq);
            }
            if (node.child2 != null && distanceToPivot - node.child2.radius <= pq.peek().distance) {
                kNearestNeighbors(node.child2, target, k, pq);
            }
        }
    }
}

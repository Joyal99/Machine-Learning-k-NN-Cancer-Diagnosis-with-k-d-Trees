package ass3;
import java.util.*;

public class BallTree {

    public static class Node {
        double[] point;
        Node child1, child2;
        double radius;

        Node(double[] point) {
            this.point = point;
        }

        Node(double[] point, Node child1, Node child2, double radius) {
            this.point = point;
            this.child1 = child1;
            this.child2 = child2;
            this.radius = radius;
        }
    }

    static class PointDistance {
        double[] point;
        double distance;

        PointDistance(double[] point, double distance) {
            this.point = point;
            this.distance = distance;
        }
    }

    public static Node constructBallTree(List<double[]> dataPoints) {
        if (dataPoints.isEmpty()) {
            return null; // Return null if dataPoints is empty
        }

        if (dataPoints.size() == 1) {
            return new Node(dataPoints.get(0));
        }

        int dimension = dataPoints.get(0).length;
        int greatestSpreadDimension = findGreatestSpreadDimension(dataPoints, dimension);
        dataPoints.sort(Comparator.comparingDouble(p -> p[greatestSpreadDimension]));
        int medianIndex = dataPoints.size() / 2;
        double[] pivot = dataPoints.get(medianIndex);

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

    private static double euclideanDistance(double[] p1, double[] p2) {
        double sum = 0;
        for (int i = 0; i < p1.length; i++) {
            double diff = p1[i] - p2[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

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

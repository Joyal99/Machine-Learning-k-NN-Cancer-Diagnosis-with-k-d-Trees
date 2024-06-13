package ass3;

import java.util.*;

public class KdTree {
    private int dimensions_;
    private Node root_ = null;

    public KdTree(int dimensions, Map<Integer, double[]> trainingRecords) {
        dimensions_ = dimensions;
        List<Node> nodes = new ArrayList<>();
        for (Map.Entry<Integer, double[]> entry : trainingRecords.entrySet()) {
            nodes.add(new Node(entry.getValue()));
        }
        root_ = makeTree(nodes, 0, nodes.size(), 0);
    }

    public List<Node> kNearestNeighbors(Node target, int k) {
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> -a.distance));
        nearest(root_, target, k, 0, pq);
        List<Node> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(pq.poll().node);
        }
        Collections.reverse(result);
        return result;
    }

    private void nearest(Node root, Node target, int k, int depth, PriorityQueue<NodeDistance> pq) {
        if (root == null)
            return;

        double distance = root.distance(target);
        pq.offer(new NodeDistance(root, distance));
        if (pq.size() > k) {
            pq.poll();
        }

        int axis = depth % dimensions_;
        Node nearChild = (target.coords_[axis] < root.coords_[axis]) ? root.left_ : root.right_;
        Node farChild = (nearChild == root.left_) ? root.right_ : root.left_;

        nearest(nearChild, target, k, depth + 1, pq);
        if (pq.size() < k || Math.abs(target.coords_[axis] - root.coords_[axis]) < pq.peek().distance) {
            nearest(farChild, target, k, depth + 1, pq);
        }
    }

    private Node makeTree(List<Node> nodes, int begin, int end, int depth) {
        if (end <= begin)
            return null;

        int axis = depth % dimensions_;
        int medianIndex = (begin + end) / 2;
        QuickSelect.select(nodes, begin, end - 1, medianIndex, new NodeComparator(axis));
        Node node = nodes.get(medianIndex);
        node.left_ = makeTree(nodes, begin, medianIndex, depth + 1);
        node.right_ = makeTree(nodes, medianIndex + 1, end, depth + 1);
        return node;
    }

    private static class NodeComparator implements Comparator<Node> {
        private int index_;

        private NodeComparator(int index) {
            index_ = index;
        }

        public int compare(Node n1, Node n2) {
            return Double.compare(n1.coords_[index_], n2.coords_[index_]);
        }
    }

    public static class Node {
        public double[] coords_;
        private Node left_ = null;
        private Node right_ = null;

        public Node(double[] coords) {
            coords_ = coords;
        }

        double distance(Node node) {
            double dist = 0;
            for (int i = 0; i < coords_.length; ++i) {
                double d = coords_[i] - node.coords_[i];
                dist += d * d;
            }
            return dist;
        }

        public String toString() {
            StringBuilder s = new StringBuilder("(");
            for (int i = 0; i < coords_.length; ++i) {
                if (i > 0)
                    s.append(", ");
                s.append(coords_[i]);
            }
            s.append(')');
            return s.toString();
        }
    }

    private static class NodeDistance {
        Node node;
        double distance;

        NodeDistance(Node node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }
}
//
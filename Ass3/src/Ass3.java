//COEN 352
//Assignment 3
//Joyal Biju Kulangara (40237314)
//Kevin Mandiouba (40243497)

import java.text.DecimalFormat;
import java.util.*;

public class Ass3 {

    public static void main(String[] args) {

        System.out.println("*************************************************************************************");
        System.out.println("\t\t\t\tAssignment 3: Breast Cancer Diagnosis Research");
        System.out.println("*************************************************************************************");


        Reader reader = new Reader();  // Creating a Reader object to read the data file
        Map<Integer, Patient> dataMap = reader.readFile();

        // Storing the IDs and shuffling them
        List<Integer> IDs = new ArrayList<>(dataMap.keySet());
        Collections.shuffle(IDs);

        // Creating Array of training records
        int[] training_count = {100, 200, 300, 400, 500, 568, 600};

        int choice = 1;

        // Loop to alternate between KdTree and BallTree algorithms
        while (choice <= 2) {

            switch (choice) {
                case 1: // KdTree Output
                    System.out.println("-------------------------------------------------------------------------------------");
                    System.out.println("\t\t\t\t\t\t\tKdTree Algorithm");
                    System.out.println("-------------------------------------------------------------------------------------");
                    break;
                case 2: // BallTree Output
                    System.out.println();
                    System.out.println("-------------------------------------------------------------------------------------");
                    System.out.println("\t\t\t\t\t\t\tBallTree Algorithm");
                    System.out.println("-------------------------------------------------------------------------------------");
                    break;
            }

            // Loop through different training records
            for (int size_N : training_count) {

                int test_count = size_N / 4;

                if (IDs.size() < size_N + test_count && size_N < IDs.size()) {
                    test_count = IDs.size() - size_N;
                }
                else if (size_N >= 569) {
                    System.out.println("\nNot enough data for a training sample of N = " + size_N + " to split into training and testing sets.");
                    continue;
                }

                System.out.println("\nFor a training sample of N = " + size_N + " and a testing sample of T = " + test_count + " here are the results:");

                // Splitting the data into training and testing sets
                Map<Integer, double[]> trainingRecords = new LinkedHashMap<>();
                Map<Integer, double[]> testingRecords = new LinkedHashMap<>();

                for (int i = 0; i < size_N; i++) {
                    Integer key = IDs.get(i);
                    Patient record = dataMap.get(key);
                    trainingRecords.put(key, record.getAttributes());
                }

                for (int i = size_N; i < size_N + test_count; i++) {
                    Integer key = IDs.get(i);
                    Patient record = dataMap.get(key);
                    testingRecords.put(key, record.getAttributes());
                }

                // Building the k-d tree with training records
                KdTree kdTree = new KdTree(10, trainingRecords);

                // Building the ball tree with training records
                List<double[]> trainingPoints = new ArrayList<>(trainingRecords.values());
                BallTree.Node rootBallTree = BallTree.constructBallTree(trainingPoints);

                // K-NN analysis on testing records
                int[] kValues = {1, 5, 7};

                switch (choice) {
                    case 1: // KdTree
                        for (int k : kValues) {
                            int correctPredictionsKdTree = 0;
                            long runtimeKdTree = 0;

                            for (Map.Entry<Integer, double[]> entry : testingRecords.entrySet()) {
                                double[] attributes = entry.getValue();

                                KdTree.Node targetNodeKd = new KdTree.Node(attributes);
                                List<KdTree.Node> neighborsKdTree = kdTree.kNearestNeighbors(targetNodeKd, k);

                                Stopwatch timerKdTree = new Stopwatch();
                                char predictedDiagnosisKdTree = majorityVote(neighborsKdTree, dataMap);
                                runtimeKdTree += timerKdTree.elapsedTimeInMicroseconds();

                                Patient testPatient = dataMap.get(entry.getKey());

                                if (predictedDiagnosisKdTree == testPatient.getDiagnosis()) {
                                    correctPredictionsKdTree++;
                                }
                            }

                            double accuracyKdTree = (double) correctPredictionsKdTree / testingRecords.size();
                            DecimalFormat df = new DecimalFormat("#.#");
                            double accuracyPercentageKdTree = Double.parseDouble(df.format(accuracyKdTree * 100));

                            System.out.println("- Accuracy for k = " + k + " is " + accuracyPercentageKdTree + "% and Running Time is " + runtimeKdTree + " microseconds");
                        }
                        break;

                    case 2: // BallTree
                        for (int k : kValues) {
                            int correctPredictionsBallTree = 0;
                            long runtimeBallTree = 0;

                            for (Map.Entry<Integer, double[]> entry : testingRecords.entrySet()) {
                                double[] attributes = entry.getValue();

                                Stopwatch timerBallTree = new Stopwatch();
                                List<double[]> neighborsBallTree = BallTree.kNearestNeighbors(rootBallTree, attributes, k);
                                char predictedDiagnosisBallTree = majorityVoteBallTree(neighborsBallTree, dataMap);
                                runtimeBallTree += timerBallTree.elapsedTimeInMicroseconds();

                                Patient testPatient = dataMap.get(entry.getKey());

                                if (predictedDiagnosisBallTree == testPatient.getDiagnosis()) {
                                    correctPredictionsBallTree++;
                                }
                            }

                            double accuracyBallTree = (double) correctPredictionsBallTree / testingRecords.size();
                            DecimalFormat df = new DecimalFormat("#.#");
                            double accuracyPercentageBallTree = Double.parseDouble(df.format(accuracyBallTree * 100));

                            System.out.println("- Accuracy for k = " + k + " is " + accuracyPercentageBallTree + "% and Running Time is " + runtimeBallTree + " microseconds");
                        }
                        break;
                }
            }
            choice++;
        }
    }

    // Class to measure running time
    public static class Stopwatch {
        private final long start;

        public Stopwatch() {
            start = System.nanoTime();
        }

        public long elapsedTimeInMicroseconds() {
            long now = System.nanoTime();
            return (now - start) / 1000;
        }
    }

    // Method to find the majority vote diagnosis for KdTree
    private static char majorityVote(List<KdTree.Node> neighbors, Map<Integer, Patient> dataMap) {
        Map<Character, Integer> voteCount = new HashMap<>();
        for (KdTree.Node neighbor : neighbors) {
            for (Map.Entry<Integer, Patient> entry : dataMap.entrySet()) {
                if (Arrays.equals(neighbor.coords_, entry.getValue().getAttributes())) {
                    char diagnosis = entry.getValue().getDiagnosis();
                    voteCount.put(diagnosis, voteCount.getOrDefault(diagnosis, 0) + 1);
                    break;
                }
            }
        }
        return Collections.max(voteCount.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    // Method to find the majority vote diagnosis for BallTree
    private static char majorityVoteBallTree(List<double[]> neighbors, Map<Integer, Patient> dataMap) {
        Map<Character, Integer> voteCount = new HashMap<>();
        for (double[] neighbor : neighbors) {
            for (Map.Entry<Integer, Patient> entry : dataMap.entrySet()) {
                if (Arrays.equals(neighbor, entry.getValue().getAttributes())) {
                    char diagnosis = entry.getValue().getDiagnosis();
                    voteCount.put(diagnosis, voteCount.getOrDefault(diagnosis, 0) + 1);
                    break;
                }
            }
        }
        return Collections.max(voteCount.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}


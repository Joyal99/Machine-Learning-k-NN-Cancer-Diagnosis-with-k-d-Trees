import ass3.Patient;
import ass3.Reader;
import ass3.KdTree;
import java.util.*;

public class Ass3 {

    public static void main(String[] args) {

        System.out.println("**********************************************************");
        System.out.println("\tAssignment 3: Breast Cancer Diagnosis Research");
        System.out.println("**********************************************************");

        // Creating a HashMap of patients (Keeps the order from the Excel file)
        Reader reader = new Reader();
        Map<Integer, Patient> dataMap = reader.readFile();

        // Storing the IDs and shuffling them
        List<Integer> IDs = new ArrayList<>(dataMap.keySet());
        Collections.shuffle(IDs);

        // Creating Training and Testing records
        Map<Integer, double[]> trainingRecords = new LinkedHashMap<>();
        Map<Integer, double[]> testingRecords = new LinkedHashMap<>();

        // Making sure we have enough elements for training and testing
        int [] training_count = {100, 200, 300, 400, 500, 568, 569};

        for(int size_N: training_count) {

            int test_count= size_N / 4;

            if (IDs.size() < size_N + test_count && size_N < IDs.size()){
                test_count = IDs.size() - size_N;
            }
            else if (size_N >= 569) {
                System.out.println("Not enough data for a training sample of N = " + size_N + " to split into training and testing sets.\n");
                continue;
            }

            System.out.println("For a training sample of N = " + size_N + " here are the results:");

            // Splitting the data into training and testing sets
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

            //Building the k-d tree with training records
            KdTree kdTree = new KdTree(10, trainingRecords);

            //K-NN analysis on testing records
            int[] kValues = {1, 5, 7};
            for (int k : kValues) {
                int correctPredictions = 0;
                long runtime = 0;

                for (Map.Entry<Integer, double[]> entry : testingRecords.entrySet()) {
                    double[] attributes = entry.getValue();

                    KdTree.Node targetNode = new KdTree.Node(attributes);
                    List<KdTree.Node> neighbors = kdTree.kNearestNeighbors(targetNode, k);

                    Patient testPatient = dataMap.get(entry.getKey());

                    Stopwatch timer = new Stopwatch();

                    char predictedDiagnosis = majorityVote(neighbors, dataMap);

                    if (predictedDiagnosis == testPatient.getDiagnosis()) {
                        correctPredictions++;
                    }

                    runtime += timer.elapsedTimeInMicroseconds();
                }

                double accuracy = (double)correctPredictions / testingRecords.size();
                double accuracy_percentage = accuracy * 100;

                System.out.println("- Accuracy for k = " + k + " is " + accuracy_percentage + "% and Running Time is " + runtime + " microseconds");
            }
            System.out.println();
        }

//        // Storing the training and testing sets IDs
//        List<Integer> trainingRecords_IDs = new ArrayList<>(trainingRecords.keySet());
//        List<Integer> testingRecords_IDs = new ArrayList<>(testingRecords.keySet());
    }
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

}
//
import ass3.Patient;
import ass3.Reader;
import java.util.*;

public class Ass3 {

    public static void main(String[] args) {
        Reader reader = new Reader();
        HashMap<Integer, Patient> dataMap = reader.readFile();

        // Check if the data was read correctly
        if (dataMap == null || dataMap.isEmpty()) {
            System.out.println("Data map is empty or null.");
            return;
        }

        List<Integer> IDs = new ArrayList<>(dataMap.keySet());
        Collections.shuffle(IDs);

        HashMap<Integer, Patient> shuffledMap = new HashMap<>();
        for (Integer key : IDs) {
            shuffledMap.put(key, dataMap.get(key));
        }

        HashMap<Integer, double[]> trainingRecords = new HashMap<>();
        HashMap<Integer, double[]> testingRecords = new HashMap<>();

        // Ensure we have enough elements for training and testing
        int training_count = 12;
        int test_count = training_count / 4;

        if (IDs.size() < training_count + test_count) {
            System.out.println("Not enough data to split into training and testing sets.");
            return;
        }

        // Split the data into training and testing sets
        for (int i = 0; i < training_count; i++) {
            Integer key = IDs.get(i);
            Patient record = shuffledMap.get(key);
            trainingRecords.put(key, record.getAttributes());
        }

        for (int i = IDs.size() - test_count; i < IDs.size(); i++) {
            Integer key = IDs.get(i);
            Patient record = shuffledMap.get(key);
            testingRecords.put(key, record.getAttributes());
        }

        // Print shuffled map patients
        System.out.println("***************************");
        System.out.println("\tshuffledMap Patients");
        System.out.println("***************************");
        int i = 1;
        for (Integer key : IDs) {
            if (i <=15) {
                Patient record = shuffledMap.get(key);
                System.out.println("Patient #" + i + ": " + record.getId());
                i++;
            }
        }

        // Print training records
        System.out.println("***************************");
        System.out.println("\ttrainingRecords Patients");
        System.out.println("***************************");
        int j = 1;
        for (Integer key : trainingRecords.keySet()) {
            double[] record = trainingRecords.get(key);
            System.out.println("Patient #" + j + ": " + record[0]);
            j++;
        }

        // Print testing records
        System.out.println("***************************");
        System.out.println("\ttestingRecords Patients");
        System.out.println("***************************");
        int k = 1;
        for (Integer key : testingRecords.keySet()) {
            double[] record = testingRecords.get(key);
            System.out.println("Patient #" + k + ": " + record[0]);
            k++;
        }
    }
}

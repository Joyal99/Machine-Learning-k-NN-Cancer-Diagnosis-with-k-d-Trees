import ass3.Patient;
import ass3.Reader;
import java.util.HashMap;
import java.util.*;

public class Ass3{

    public static void main(String[] args){

        Reader reader = new Reader();
        HashMap<Integer, Patient> dataMap= reader.readFile();

        HashMap<Integer, double[]> trainingRecords = new HashMap<>();
        HashMap<Integer, double[]> testingRecords = new HashMap<>();

        //Splitting training and testing sets
        List<Integer> IDs = new ArrayList<>(dataMap.keySet());
        Collections.shuffle(IDs);

        HashMap<Integer, Patient> shuffledMap = new HashMap<>();

        for (Integer key : IDs) {
            shuffledMap.put(key, dataMap.get(key));
        }

        int training_count = 12;
        int test_count = training_count / 4;

        int count = 0;

        for (Integer key: shuffledMap.keySet()) {

            Patient record = shuffledMap.get(key);

            if (count < training_count) {
                trainingRecords.put(key, record.getAttributes());
            }
            else if (count < training_count + test_count) {
                testingRecords.put(key, record.getAttributes());
            }

            count ++;
        }

        System.out.println("***************************");
        System.out.println("\tshuffledMap Patients");
        System.out.println("***************************");

        int i = 1;
        for (Integer key: shuffledMap.keySet()) {

            if (i <= 15) {
                Patient record = shuffledMap.get(key);
                System.out.println("Patient #" + i + ": " + record.getId());
            }

            i++;
        }

        System.out.println("***************************");
        System.out.println("\ttrainingRecords Patients");
        System.out.println("***************************");
        int j = 1;
        for (Integer key: trainingRecords.keySet()) {

            double[] record = trainingRecords.get(key);
            System.out.println("Patient #" + j + ": " + record[0]);
            j++;
        }

        System.out.println("***************************");
        System.out.println("\ttestingRecords Patients");
        System.out.println("***************************");
        int k = 1;
        for (Integer key: testingRecords.keySet()) {

            double[] record = testingRecords.get(key);
            System.out.println("Patient #" + k + ": " + record[0]);
            k++;
        }


//        Shuffle test
//
//        List<Integer> IDs = new ArrayList<>(dataMap.keySet());
//
//        for (Integer key: IDs) {
//            Patient record = dataMap.get(key);
//            System.out.println("ID: " + record.getId());
//        }

//        Accessibility test
//
//        int exampleID = 871001501;
//
//        if (dataMap.containsKey(exampleID)) {
//            Patient record = dataMap.get(exampleID);
//            System.out.println("Diagnosis: " + record.getDiagnosis());
//            System.out.println("Radius Mean: " + record.getRadius_mean());
//            System.out.println("Texture Mean: " + record.getTexture_mean());
//            System.out.println("Perimeter Mean: " + record.getPerimeter_mean());
//            System.out.println("Area Mean: " + record.getArea_mean());
//            System.out.println("Smoothness Mean: " + record.getSmoothness_mean());
//            System.out.println("Compactness Mean: " + record.getCompactness_mean());
//            System.out.println("Concavity Mean: " + record.getConcavity_mean());
//            System.out.println("Concave Points Mean: " + record.getConcave_points_mean());
//            System.out.println("Symmetry Mean: " + record.getSymmetry_mean());
//            System.out.println("Fractal Dimension Mean: " + record.getFractal_dimension_mean());
//        } else {
//            System.out.println("ID not found: " + exampleID);
//        }
    }
}

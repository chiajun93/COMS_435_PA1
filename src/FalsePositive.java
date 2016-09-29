import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class FalsePositive {
    private BloomFilterDet bloomFilterDet;
    private BloomFilterRan bloomFilterRan;
    private HashSet<String> dict;
    private int falseCountDet;
    private int falseCountRan;
    private int bitsPerElement;

    /**
     * Constructor for calculating the false positive in bloom filter det with a given number of tests
     *
     * @param setSize
     * @param bitsPerElement
     * @param numTests
     */
    public FalsePositive(int setSize, int bitsPerElement, int numTests) {
        bloomFilterDet = new BloomFilterDet(setSize, bitsPerElement);
        bloomFilterRan = new BloomFilterRan(setSize, bitsPerElement);
        dict = new HashSet<>();
        falseCountDet = 0;
        falseCountRan = 0;
        this.bitsPerElement = bitsPerElement;

        File file = new File("grams.txt");
        try{
            Scanner scan = new Scanner(file);
            for (int i = 0; i < numTests; i++) {
                String s = scan.nextLine();
                if (i < numTests / 2){
                    bloomFilterDet.add(s);
                    bloomFilterRan.add(s);
                }
                else
                    dict.add(s);
            }
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Returns the optimal values for the false positive rate
     *
     * @return false positive
     */
    public double getOptFalsePositive() {
        return Math.pow(0.618, bitsPerElement);
    }

    /**
     * Prints the false positive rates of BloomFilterDet and BloomFilterRan by dividing the occurrences of strings which do not not appear in dict in bloom filters
     *
     * @return false positive rate
     */
    public void getFalsePositive() {
        for (String s : dict) {
            if (bloomFilterDet.appears(s)) {
                falseCountDet++;
            }
            if (bloomFilterRan.appears(s)) {
                falseCountRan++;
            }
        }

        System.out.println("False count in BloomFilterDet: " + falseCountDet);
        System.out.println("False count in BloomFilterRan: " + falseCountRan);
        System.out.println("False positive of BloomFilterDet: " + (double) falseCountDet / dict.size());
        System.out.println("False positive of BloomFilterRan: " + (double) falseCountRan / dict.size());
        System.out.println("Dict size: " + dict.size());
    }

    public static void main(String[] args) {
        FalsePositive fp = new FalsePositive(10000, 4, 40000);
        fp.getFalsePositive();
        System.out.println("Optimal false positive of BloomFilterDet: " + fp.getOptFalsePositive());
    }
}

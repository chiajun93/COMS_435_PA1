import java.util.HashSet;
import java.util.Random;

public class FalsePositive {
    private BloomFilterDet bloomFilterDet;
    private BloomFilterRan bloomFilterRan;
    private HashSet<String> dict;
    private int falseCount;
    private int numTests;
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * Constructor for calculating the false positive in bloom filter det with a given number of tests
     *
     * @param bloomFilterDet
     * @param numTests
     */
    public FalsePositive(BloomFilterDet bloomFilterDet, int numTests) {
        this.bloomFilterDet = bloomFilterDet;
        dict = new HashSet<>();
        falseCount = 0;
        this.numTests = numTests;

        for (int i = 0; i < bloomFilterDet.filterSize(); i++) {
            String s = genStrings();
            if (i <= numTests)
                bloomFilterDet.add(s);
            else
                dict.add(s);
        }
    }

    /**
     * Constructor for calculating the false positive in bloom filter random with a given number of tests
     *
     * @param bloomFilterRan
     * @param numTests
     */
    public FalsePositive(BloomFilterRan bloomFilterRan, int numTests) {
        this.bloomFilterRan = bloomFilterRan;
        dict = new HashSet<>();
        falseCount = 0;
        this.numTests = numTests;

        for (int i = 0; i < bloomFilterRan.filterSize(); i++) {
            String s = genStrings();
            if (i <= numTests)
                bloomFilterRan.add(s);
            else
                dict.add(s);
        }
    }

    /**
     * Randomly generate a string with the fixed length
     *
     * @return generated string
     */
    private String genStrings() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int ith = rand.nextInt(LETTERS.length());
            sb.append(LETTERS.charAt(ith));
        }

        return sb.toString();
    }

    /**
     * Return the false positive rate by dividing the occurrences of strings which do not not appear in dict in bloom filter det
     *
     * @return false positive rate
     */
    public double getFalsePositive() {
        StringBuilder sb = new StringBuilder();
        for (String s : dict) {
            if (bloomFilterDet.appears(s)) {
                sb.append(s);
                sb.append(", ");
                falseCount++;
            }
        }
        System.out.println(sb.toString());
        System.out.println("False count: " + falseCount);
        System.out.println("Dict size: " + dict.size());

        return (double) falseCount / dict.size();
    }


    public static void main(String[] args) {
        BloomFilterDet det = new BloomFilterDet(30000, 4);
        BloomFilterRan ran = new BloomFilterRan(3000, 4);

        FalsePositive fp = new FalsePositive(det, 25000);
//        det.print();
        System.out.println("False positive: " + fp.getFalsePositive());
        System.out.println("Optimal False positive: " + det.getOptFalsePositive());
    }

}

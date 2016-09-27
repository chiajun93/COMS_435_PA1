import java.util.HashSet;
import java.util.Random;

/**
 * Created by chiajun on 9/25/16.
 */
public class FalsePositive {
    private BloomFilterDet bloomFilterDet;
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

        for (int i = 0; i < numTests; i++) {
            String s = genStrings();
            bloomFilterDet.add(s);
            dict.add(s);
        }
    }

    /**
     * Randomly generate a string with the fixed length
     *
     * @return
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
        int falseData = 0;

        for (int i = 0; i < numTests; i++) {
            String s = genStrings();

            if (!dict.contains(s)) {
                falseData++;

                if (bloomFilterDet.appears(s))
                    falseCount++;
            }
        }

        return (double) falseCount / falseData;
    }
    
    public int getFalseCount() {
        return falseCount;
    }

    public static void main(String[] args) {
        FalsePositive fp = new FalsePositive(new BloomFilterDet(3000, 4), 3000);
        System.out.println("False count: " + fp.getFalseCount() + "\nFalse positive: " + fp.getFalsePositive());
    }

}

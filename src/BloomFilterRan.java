import javafx.util.Pair;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by paulgerlich on 9/24/16.
 */
public class BloomFilterRan {

    // Arraylist containing K hash values
    private ArrayList<Pair<Pair<Integer, Integer>, Integer>> randomHashValues;

    // Bloom filter implementation
    private BitSet filterTable;

    // Misc counts
    private int totalSetSize;
    private int numHashFunctions;
    private int largestPrimeNumber; // The actual size of the bloom filter
    private int numElements;

    /**
     * Create a bloom filter for the given setSize and bitsPerElement hash functions
     * @param setSize size of the input set
     * @param bitsPerElement number of hash functions to use
     */
    BloomFilterRan(int setSize, int bitsPerElement){
        totalSetSize = setSize;
        numHashFunctions = bitsPerElement;
        numElements = 0;

        randomHashValues = new ArrayList<>();
        filterTable = new BitSet();

        generateHashValues();
    }

    /**
     * Add a string to the bloom filter
     * @param string
     */
    private void add(String string){
        for ( Pair<Pair<Integer, Integer>, Integer> abpPair : randomHashValues ) {
            int a = abpPair.getKey().getKey();
            int b = abpPair.getKey().getValue();
            int p = abpPair.getValue();

            int hashIndex = ((a * string.hashCode()) + b) % p;

            // Account for overflow
            if ( hashIndex < 0 ) hashIndex = Math.abs(Integer.MIN_VALUE) - Math.abs(hashIndex);

            filterTable.set(hashIndex);
        }

        numElements++;
    }

    /**
     * Whether or not a string appears in the Bloom Filter
     * @param string string in question
     * @return If it appears in our bloom filter
     */
    private boolean appears(String string){
        // Compute each hash, check if bit is set
        for ( Pair<Pair<Integer, Integer>, Integer> abpPair : randomHashValues ) {
            int a = abpPair.getKey().getKey();
            int b = abpPair.getKey().getValue();
            int p = abpPair.getValue();

            int hashIndex = ((a * string.hashCode()) + b) % p;

            // Account for overflow
            if ( hashIndex < 0 ) hashIndex = Math.abs(Integer.MIN_VALUE) - Math.abs(hashIndex);

            // If the bit is ever not set, then we return false
            if ( !filterTable.get(hashIndex) ) return false;
        }

        return true;
    }


    /**
     * The total size of the filter, in bits
     * @return
     */
    private int filterSize(){
        return largestPrimeNumber;
    }

    /**
     * The total number of elements stored in the filter
     * @return
     */
    private int dataSize(){
        return numElements;
    }

    /**
     * The number of hash functions in use
     * @return size of hash values array
     */
    private int numHashes(){
        return numHashFunctions;
    }

    /**
     * Generate the required number of hash functions for the set
     */
    private void generateHashValues(){
        // Already generated
        if ( randomHashValues.size() > 0 ) return;

        // Find next prime, use it to generate hash values
        int currentNumber = totalSetSize + 1;


        while ( randomHashValues.size() < numHashFunctions ) {

            // For each prime number, generate a new hash value set
            if ( isPrimeNumber(currentNumber) ) {
                int a = (int) (Math.random() * (currentNumber - 1));
                int b = (int) (Math.random() * (currentNumber - 1));
                Pair<Integer, Integer> abPair = new Pair<>(a, b);

                Pair<Pair<Integer, Integer>, Integer> abpPair = new Pair<>(abPair, currentNumber);

                randomHashValues.add(abpPair);
                largestPrimeNumber = currentNumber;
            }

            currentNumber++;
        }
    }

    /**
     * Determine whether or not a number is prime
     * @param n number
     * @return if n is prime
     */
    private boolean isPrimeNumber(int n){
        double num = (double) n;

        for(int i = 2; i < Math.sqrt(num); i++) {
            double result = num / i;

            if ( result == Math.floor(result) ) {
                return false;
            }
        }

        return true;
    }

    /**
     * Executor
     * @param args
     */
    public static void main(String[] args){
        BloomFilterRan bloomFilterRan = new BloomFilterRan(1000000000, 1);

        for(int i = 0; i < 5000; i++){
            bloomFilterRan.add("test" + i);
        }

        System.out.println(bloomFilterRan.appears("test57"));
        System.out.println(bloomFilterRan.appears("nope"));
        System.out.println(bloomFilterRan.appears("best57"));
        System.out.println(bloomFilterRan.appears("nada"));
        System.out.println(bloomFilterRan.appears("work"));
        System.out.println(bloomFilterRan.appears("twerk"));
        System.out.println(bloomFilterRan.appears("merk"));

        System.out.println(bloomFilterRan.dataSize());
        System.out.println(bloomFilterRan.filterSize());
        System.out.println(bloomFilterRan.numHashes());
    }


}

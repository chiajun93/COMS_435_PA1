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
    private int filterSize;
    private int numHashFunctions;
    private int numElements;

    /**
     * Create a bloom filter for the given setSize and bitsPerElement hash functions
     * @param setSize size of the input set
     * @param bitsPerElement number of hash functions to use
     */
    BloomFilterRan(int setSize, int bitsPerElement){
        filterSize = setSize * bitsPerElement;
        numHashFunctions = (int) Math.ceil(Math.log(2 * (filterSize/setSize)));
        numElements = 0;

        randomHashValues = new ArrayList<>();
        filterTable = new BitSet();

        generateHashValues();
    }

    /**
     * Add a string to the bloom filter
     * @param string
     */
    public void add(String string){
        string = string.toLowerCase();

        for ( Pair<Pair<Integer, Integer>, Integer> abpPair : randomHashValues ) {
            int a = abpPair.getKey().getKey();
            int b = abpPair.getKey().getValue();
            int p = abpPair.getValue();

            int hashIndex = ((a * string.hashCode()) + b);

            // Account for overflow
            if ( hashIndex < 0 ) hashIndex = Math.abs(Integer.MIN_VALUE) - Math.abs(hashIndex);

            hashIndex = hashIndex % p;

            filterTable.set(hashIndex);
        }

        numElements++;
    }

    /**
     * Whether or not a string appears in the Bloom Filter
     * @param string string in question
     * @return If it appears in our bloom filter
     */
    public boolean appears(String string){
        if ( string == null ) return false;

        string = string.toLowerCase();

        // Compute each hash, check if bit is set
        for ( Pair<Pair<Integer, Integer>, Integer> abpPair : randomHashValues ) {
            int a = abpPair.getKey().getKey();
            int b = abpPair.getKey().getValue();
            int p = abpPair.getValue();

            int hashIndex = ((a * string.hashCode()) + b);

            // Account for overflow
            if ( hashIndex < 0 ) hashIndex = Math.abs(Integer.MIN_VALUE) - Math.abs(hashIndex);

            hashIndex = hashIndex % p;

            // If the bit is ever not set, then we return false
            if ( !filterTable.get(hashIndex) ) return false;
        }


        return true;
    }

    /**
     *  Used to count the number of zeros that appear in the filterTable
     */
    public void countZeros(){
        System.out.println("Size: " + filterTable.size());
        int zeroCount = 0;

        for(int i = 0; i < filterTable.size(); i++ ){
            if ( !filterTable.get(i) ) zeroCount++;
        }

        System.out.println("Zero Count: " + zeroCount);
    }


    /**
     * The total size of the filter, in bits
     * @return
     */
    public int filterSize(){
        return filterSize;
    }

    /**
     * The total number of elements stored in the filter
     * @return
     */
    public int dataSize(){
        return numElements;
    }

    /**
     * The number of hash functions in use
     * @return size of hash values array
     */
    public int numHashes(){
        return numHashFunctions;
    }

    /**
     * Generate the required number of hash functions for the set
     */
    private void generateHashValues(){
        // Already generated
        if ( randomHashValues.size() > 0 ) return;

        // Find next prime, use it to generate hash values
        int currentNumber = filterSize + 1;

        // Skip to next prime
        while ( !isPrimeNumber(currentNumber) ) currentNumber++;

        // Generate K hash values
        for(int i = 0; i < numHashFunctions; i++ ) {
            int a = (int) (Math.random() * (currentNumber - 1));
            int b = (int) (Math.random() * (currentNumber - 1));

            Pair<Integer, Integer> abPair = new Pair<>(a, b);
            Pair<Pair<Integer, Integer>, Integer> abpPair = new Pair<>(abPair, currentNumber);

            randomHashValues.add(abpPair);
        }

        filterSize = currentNumber; // Increase filter size to the size of the next prime number
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

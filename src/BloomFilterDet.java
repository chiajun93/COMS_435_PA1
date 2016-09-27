import java.nio.ByteBuffer;
import java.util.BitSet;

public class BloomFilterDet {
    private int bitsPerElement;
    private int numHashes;
    private int filterSize;
    private int nextPrime;
    private int numOfData;
    private BitSet filter;
    private final long FNV_64INIT = Long.parseUnsignedLong("14695981039346656037");
    private final long FNV64PRIME = Long.parseUnsignedLong("109951168211");


    /**
     * Create a bloom filter that use deterministic functions
     *
     * @param setSize
     * @param bitsPerElement
     */
    public BloomFilterDet(int setSize, int bitsPerElement) {
        this.bitsPerElement = bitsPerElement;
        filterSize = setSize * bitsPerElement;
        filter = new BitSet(filterSize);
        setNextPrime(filterSize);
        this.numHashes = (int) Math.ceil(Math.log(2) * bitsPerElement);
    }

    /**
     * Adds the string s to the filter.
     *
     * @param s
     */
    public void add(String s) {
        s = s.toLowerCase();
        long hashVal = fnv64(s);
        int a = (int)hashVal;
        int b = (int)(hashVal >> 32);

        for(int i = 0; i < numHashes; i++){
            int idx = (a + b * i) % nextPrime;
            if(idx < 0)
                idx = Math.abs(Integer.MIN_VALUE) - Math.abs(idx);

            filter.set(idx);
        }

        numOfData++;
    }

    /**
     * Returns true if s appears in the filter; otherwise returns false.
     *
     * @param s
     * @return
     */
    public boolean appears(String s) {
        s = s.toLowerCase();
        long hashVal = fnv64(s);
        int a = (int)hashVal;
        int b = (int)(hashVal >> 32);

        for(int i = 0; i < numHashes; i++){
            int idx = (a + b * i) % nextPrime;
            if(idx < 0)
                idx = Math.abs(Integer.MIN_VALUE) - Math.abs(idx);

            if(!filter.get(idx))
                return false;
        }

        return true;
    }

    /**
     * Returns the size of the filter (the size of the table).
     *
     * @return
     */
    public int filterSize() {
        return filterSize;
    }


    /**
     * Returns the number of elements added to the filter.
     *
     * @return
     */
    public int dataSize() {
        return numOfData;
    }

    /**
     * Returns the number of hash function used.
     *
     * @return
     */
    public int numHashes() {
        return numHashes;
    }

    /**
     * Returns the hash value of a string that is calculated by FNV-64 hash functions
     *
     * @param s
     * @return
     */
    private long fnv64(String s) {
        long h = FNV_64INIT;

        for (int i = 0; i < s.length(); i++) {
            h = h ^ s.charAt(i);
            h = h * FNV64PRIME;
        }

        return h;
    }

    private byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    private long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
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
     * Set the next prime number based on the filter size
     * @param n
     */
    private void setNextPrime(int n){
        if(isPrimeNumber(filterSize))
            nextPrime = filterSize;

        while(!isPrimeNumber(n))
            n++;

        nextPrime = n;
    }

    public void print() {
        System.out.println(filter);
    }

    public static void main(String[] args) {
        BloomFilterDet det = new BloomFilterDet(5000, 4);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            det.add("test" + i);
        }

        System.out.println(det.appears("test1"));
        System.out.println(det.appears("test2"));
        System.out.println(det.appears("test"));

        long end = System.currentTimeMillis();
        System.out.println("Runtime: " + (end - start));

        det.print();
    }
}

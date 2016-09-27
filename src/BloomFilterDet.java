import java.math.BigInteger;
import java.util.BitSet;

public class BloomFilterDet {
    private int bitsPerElement;
    private int setSize;
    private int numHashes;
    private int filterSize;
    private int numOfData;
    private BitSet filter;
    private final BigInteger FNV_64INIT = new BigInteger("14695981039346656037");
    private final BigInteger FNV64PRIME = new BigInteger("109951168211");
    private final BigInteger twoPow64 = new BigInteger("2").pow(64);


    /**
     * Create a bloom filter that use deterministic functions
     *
     * @param setSize
     * @param bitsPerElement
     */
    public BloomFilterDet(int setSize, int bitsPerElement) {
        this.bitsPerElement = bitsPerElement;
        this.setSize = setSize;
        filterSize = setSize * bitsPerElement;
        filter = new BitSet(filterSize);
        this.numHashes = (int) Math.ceil(Math.log(2) * bitsPerElement);
    }

    /**
     * Adds the string s to the filter.
     *
     * @param s
     */
    public void add(String s) {
        s = s.toLowerCase();
        BigInteger hashVal = fnv64(s);
        BigInteger a = hashVal;
        BigInteger b = hashVal;

        for (int i = 0; i < numHashes; i++) {
            for(int j = 0; j < b.bitLength()/2; j++){
                b = b.clearBit(j);
            }
            for(int j = a.bitLength()/2; j < a.bitLength(); j++){
                a = a.clearBit(j);
            }

            BigInteger idx = a.add(b).multiply(new BigInteger(String.valueOf(i)));
            int pos = idx.mod(new BigInteger(String.valueOf(filterSize))).intValue();

            if (pos < 0) {
                pos = Math.abs(Integer.MIN_VALUE) - Math.abs(pos);
            }
            filter.set(pos);
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
        BigInteger hashVal = fnv64(s);
        BigInteger a = hashVal;
        BigInteger b = hashVal;

        for (int i = 0; i < numHashes; i++) {
            for(int j = 0; j < b.bitLength()/2; j++){
                b = b.clearBit(j);
            }
            for(int j = a.bitLength()/2; j < a.bitLength(); j++){
                a = a.clearBit(j);
            }

            BigInteger idx = a.add(b).multiply(new BigInteger(String.valueOf(i)));
            int pos = idx.mod(new BigInteger(String.valueOf(filterSize))).intValue();

            if (pos < 0) {
                pos = Math.abs(Integer.MIN_VALUE) - Math.abs(pos);
            }

            if (!filter.get(pos))
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
    private BigInteger fnv64(String s) {
        BigInteger h = FNV_64INIT;

        for (int i = 0; i < s.length(); i++) {
            h = h.xor(new BigInteger(Integer.toBinaryString(s.charAt(i))));
            h = h.multiply(FNV64PRIME).mod(twoPow64);
        }

        return h;
    }

    /**
     * Returns the optimal values for the false positive rate
     *
     * @return false positive
     */
    public double getOptFalsePositive() {
        return Math.pow(0.618, bitsPerElement);
    }

    public void print() {
        System.out.println(filter);
    }

    public static void main(String[] args) {
        BloomFilterDet det = new BloomFilterDet(500, 4);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            det.add("test" + i);
        }

        System.out.println(det.appears("test1"));
        System.out.println(det.appears("test2"));
        System.out.println(det.appears("testt"));

        long end = System.currentTimeMillis();
        System.out.println("Runtime: " + (end - start));

        det.print();
    }
}

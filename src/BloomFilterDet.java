import java.math.BigInteger;
import java.util.BitSet;

public class BloomFilterDet {
    private int bitsPerElement;
    private int setSize;
    private int numHashes;
    private int filterSize;
    private BitSet filter;
    private BigInteger FNV_64INIT = new BigInteger("14695981039346656037");
    private BigInteger FNV64PRIME = new BigInteger("109951168211");
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
        this.filterSize = setSize * bitsPerElement;

        filter = new BitSet(setSize);
        this.numHashes = (int) Math.ceil(Math.log(2) * bitsPerElement);
    }

    /**
     * Adds the string s to the filter.
     *
     * @param s
     */
    public void add(String s) {
        s = s.toLowerCase();
//        System.out.println(fnv64(s) % filterSize);
        filterSize++;
        int idx = (int)(fnv64(s) % filterSize);
        if(idx < 0){
            idx = Math.abs(Integer.MIN_VALUE) - Math.abs(idx);
        }
        filter.set(idx);

    }

    /**
     * Returns true if s appears in the filter; otherwise returns false.
     *
     * @param s
     * @return
     */
    public boolean appears(String s) {
        s = s.toLowerCase();
        int idx = (int)(fnv64(s) % filterSize);
        if(idx < 0){
            idx = Math.abs(Integer.MIN_VALUE) - Math.abs(idx);
        }
        if(filter.get(idx)) {
            return true;
        }

        return false;
    }

    /**
     * Returns the size of the filter (the size of the table).
     *
     * @return
     */
    public int filterSize() {
        return setSize * bitsPerElement;
    }


    /**
     * Returns the number of elements added to the filter.
     *
     * @return
     */
    public int dataSize() {
        return filter.cardinality();
    }

    /**
     * Returns the number of hash function used.
     *
     * @return
     */
    public int numHashes() {
        return numHashes;
    }

    private long fnv64(String s) {
        BigInteger h = FNV_64INIT;

        for (int i = 0; i < s.length(); i++) {
            h = h.xor(new BigInteger(Integer.toBinaryString(s.charAt(i))));
            h = h.multiply(FNV64PRIME).mod(twoPow64);
        }

        return h.longValue();
    }

    public void print(){
        System.out.println(filter);
    }

    public static void main(String[] args) {
        BloomFilterDet det = new BloomFilterDet(5000, 1);
        det.add("Test");
        System.out.println(det.appears("test"));
        det.print();
    }
}

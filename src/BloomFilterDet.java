import java.math.BigInteger;
import java.util.BitSet;

public class BloomFilterDet {
    private int bitsPerElement;
    private int setSize;
    private int numHashes;
    private int filterSize;
    private int numOfData;
    private BitSet filter;
    private static BigInteger FNV_64INIT = new BigInteger("14695981039346656037");
    private static BigInteger FNV64PRIME = new BigInteger("109951168211");
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
        long hashVal = fnv64(s);

        for(int i = 0; i < numHashes; i++){
            int a = (int)hashVal;
            int b = (int)(hashVal >> 32);
            int idx = a + b * i;
            if(idx < 0){
                idx = Math.abs(Integer.MIN_VALUE) - Math.abs(idx);
            }
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

        for(int i = 0; i < numHashes; i++){
            int a = (int)hashVal;
            int b = (int)(hashVal >> 32);
            int idx = a + b * i;
            if(idx < 0){
                idx = Math.abs(Integer.MIN_VALUE) - Math.abs(idx);
            }
            if(!filter.get(idx)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the size of the filter (the size of the table).
     *
     * @return
     */
    public int filterSize() {
        return filter.cardinality();
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

    private long fnv64(String s) {
        BigInteger h = FNV_64INIT;

        for (int i = 0; i < s.length(); i++) {
            h = h.xor(new BigInteger(Integer.toBinaryString(s.charAt(i))));
            h = h.multiply(FNV64PRIME).mod(twoPow64);
        }

        return h.longValue();
    }

//    private long fnv64NoBig(String s) {
//        long h = 0xcbf29ce484222325L;
//
//        for (int i = 0; i < s.length(); i++) {
//            h ^= s.charAt(i);
//            h = (h * 0x100000001b3L) ;
//        }
//
//        return h;
//    }

    public void print(){
        System.out.println(filter);
    }

    public static void main(String[] args) {
        BloomFilterDet det = new BloomFilterDet(500, 4);
        long start = System.currentTimeMillis();
        for(int i = 0; i < 500; i++){
            det.add("test" + i);
        }

        System.out.println(det.appears("test1"));

        long end = System.currentTimeMillis();
        System.out.println("Runtime: " + (end - start));

        det.print();
    }
}

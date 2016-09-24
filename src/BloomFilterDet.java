import java.util.BitSet;

public class BloomFilterDet {
    private int bitsPerElement;
    private BitSet filter;
    //private static final long FNV64PRIME ;
    //private static final int

    public BloomFilterDet(int setSize, int bitsPerElement){
        this.bitsPerElement = bitsPerElement;
        filter = new BitSet(setSize);
    }

    public void add(String s){

    }

    public boolean appears(String s){
        return false;
    }

    public int filterSize(){
        return filter.size();
    }

    public int dataSize(){
        return filter.cardinality();
    }

    public int numHashes(){
        return 0;
    }

    public static void main(String[] args){
        BloomFilterDet det = new BloomFilterDet(16, 2);
        System.out.println(det.dataSize());
    }
}

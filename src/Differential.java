import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Implementation of both a niave and bloom differential.
 */
public class Differential {

    // File paths
    private String databaseFilePath;
    private String differentialFilePath;
    private String differentialType;

    // Filter type + bloomFilter size constants
    private static final String NIAVE = "niave";
    private static final String BLOOM = "bloom";
    private static final int NUM_HASH_FUNCTIONS = 10;

    // Bloom filter, if applicable
    private BloomFilterRan bloomFilter = null;

    /**
     *  Use a bloom filter for differential references
     */
    Differential(String databaseFilePath, String differentialFilePath, String type){
        this.databaseFilePath = databaseFilePath;
        this.differentialFilePath = differentialFilePath;
        this.differentialType = type;
    }

    /**
     * Parse the different file into a bloom filter
     * @return
     */
    public BloomFilterRan createFiler(){
        if ( this.differentialType == NIAVE) return null;

        try {
            FileReader fileReader = new FileReader(differentialFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            ArrayList<String> keys = new ArrayList<>();
            String currentLine;

            // Read each key into our arraylist.. TODO: How can we know the bloomFilter setSize without doing this?
            while ( (currentLine = bufferedReader.readLine()) != null ) keys.add(getKeyFromLine(currentLine));

            // Create our bloomfilter, store our keys
            bloomFilter = new BloomFilterRan(keys.size(), NUM_HASH_FUNCTIONS);
            for ( String key : keys ) bloomFilter.add(key);
        } catch ( IOException e ) {
            System.out.println(e.getLocalizedMessage());
            System.out.println("Failed to read differential file");
        }

        return bloomFilter;
    }

    /**
     * Retrieves a record based on differential mode
     * Generally: Hits differential file if: In Niave mode OR it's in the bloom fiter
     * Note: Also hits the differential file if our bloom filter is null
     * @param key The key to the desired record
     * @return The record if it can be found, else an empty string
     */
    public String retrieveRecord(String key){
        String record;

        // Hit the differential file if: We're in niave mode OR the bloom filter contains the key OR the bloom filter doesn't exist
        if ( (differentialType.equals(BLOOM) && (bloomFilter == null || bloomFilter.appears(key))) ||
                differentialType.equals(NIAVE)) {
            record = getRecordFromFile(key, differentialFilePath);

            if ( !record.equals("") ) return record;
        }

        return getRecordFromFile(key, databaseFilePath);
    }

    /**
     * Given a file path, return the record associated to the key ( if it can be found in the file )
     * @param key The key to lookup
     * @param filePath The file to check
     * @return The record, or an empty string if it does not exist
     */
    private String getRecordFromFile(String key, String filePath) {
        String result = "";

        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String currentLine;

            while ( (currentLine = bufferedReader.readLine()) != null ) {
                String keyForLine = getKeyFromLine(currentLine);

                if ( keyForLine.equals(key) ) return currentLine;
            }

        } catch ( IOException e ) {
            System.out.println(e.getLocalizedMessage());
            System.out.println("Failed to read file: " + filePath);
        }

        return result;
    }


    /**
     * Get the key from a line of text (read from the differential file)
     * @param line
     * @return
     */
    private static String getKeyFromLine(String line){
        String[] words = line.split(" ");
        String key = "";

        for (String word : words ) {
            try {
                // Check if we are still reading words, if not break (we've already read the key)
                Integer.parseInt(word);
                break;
            } catch ( NumberFormatException e) {
                // In this case, we are still reading words
                key += word + " ";
            }
        }

        return key.substring(0, key.length() - 1); // Remove last space
    }

    /**
     * Executor
     * @param args
     */
    public static void main(String[] args){
        getKeyFromLine("The archbishop is a 123");
    }

}

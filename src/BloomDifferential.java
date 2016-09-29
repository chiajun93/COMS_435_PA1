import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BloomDifferential extends Differential {

    // Bloom filter, if applicable
    public BloomFilterRan bloomFilter = null;


    /**
     *  Use a bloom filter for differential references
     */
    BloomDifferential(String databaseFilePath, String differentialFilePath){
        super(databaseFilePath, differentialFilePath);
    }

    /**
     * Parse the different file into a bloom filter
     * @return
     */
    public BloomFilterRan createFilter(){

        try {
            FileReader fileReader = new FileReader(differentialFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            ArrayList<String> keys = new ArrayList<>();
            String currentLine;

            // Read each key into our arraylist.. TODO: How can we know the bloomFilter setSize without doing this?
            while ( (currentLine = bufferedReader.readLine()) != null ) {
                keys.add(getKeyFromLine(currentLine));
                differentialRuntimeStats.linesRead++;
            }

            // Create our bloomfilter, store our keys
            bloomFilter = new BloomFilterRan(keys.size(), 8);

            for ( String key : keys ) bloomFilter.add(key);
        } catch ( IOException e ) {
            System.out.println(e.getLocalizedMessage());
            System.out.println("Failed to read differential file");
        }

        if ( DEBUG ) System.out.println("Bloom Filter Created.");

        return bloomFilter;
    }

    /**
     * Retrieves a record based on differential mode
     * Generally: Hits differential file if: In Naive mode OR it's in the bloom fiter
     * Note: Also hits the differential file if our bloom filter is null
     * @param key The key to the desired record
     * @return The record if it can be found, else an empty string
     */
    public String retrieveRecord(String key){
        String record;

        // Hit the differential file if: We're in naive mode OR the bloom filter contains the key OR the bloom filter doesn't exist
        if ( bloomFilter == null || bloomFilter.appears(key) ) {

            if ( DEBUG && bloomFilter.appears(key)) System.out.println(key + " was found in bloom filter.");
            if ( DEBUG ) System.out.println("Querying differential file.");


            record = getRecordFromFile(key, differentialFilePath);

            if ( !record.equals("") ) {
                if ( DEBUG ) System.out.println("Found in differential file.");
                return record;
            }
        }

        if ( DEBUG ) {
            if ( !bloomFilter.appears(key) ) {
                System.out.println(key + " was not found in the bloom filter");
            } else {
                System.out.println("Not found in differential file.");
            }

            System.out.println("Querying database file.");
        }

        record = getRecordFromFile(key, databaseFilePath);

        if ( DEBUG ) {
            if ( record.length() > 0 )
                System.out.println("Found in database.");
            else
                System.out.println("Not Found in database.");
        }

        return record;
    }

}

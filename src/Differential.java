import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Implementation of both a niave and bloom differential.
 */
public class Differential {

    // File paths
    private String databaseFilePath;
    private String differentialFilePath;
    private String differentialType;

    // Filter type
    public static final String NIAVE = "niave";
    public static final String BLOOM = "bloom";


    /**
     *     Use a bloom filter for differential references
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
    private BloomFilterRan createFiler(){
        if ( this.differentialType == NIAVE) return null;

        try {
            FileReader fileReader = new FileReader(differentialFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String currentLine;

            while ( (currentLine = bufferedReader.readLine()) != null ) {

            }

        } catch ( IOException e ) {
            System.out.println(e.getLocalizedMessage());
            System.out.println("Failed to read differential file");
        }

        return null;
    }

    private String retrieveRecord(String key){
        String record;

        // Bloom queries filter first, hits differential if we get a positive result
        if ( differentialType == BLOOM ) {
            // TODO: Check bloom filter
            // TODO: If in bloom filter, hit differential
        }

        // Niave hits differential regardless
        if ( differentialType == NIAVE ) {
            record = getRecordFromFile(key, differentialFilePath);

            if ( !record.equals("") ) return record;
        }

        return getRecordFromFile(key, databaseFilePath);
    }

    /**
     * Given a file path, return the key ( if it exists )
     * @param key The key to lookup
     * @param filePath The file to check
     * @return The record, or an empty string if it does not exist
     */
    private String getRecordFromFile(String key, String filePath) {
        return "";
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

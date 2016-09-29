public class NaiveDifferential extends Differential {

    /**
     *  Use a bloom filter for differential references
     */
    NaiveDifferential(String databaseFilePath, String differentialFilePath){
        super(databaseFilePath, differentialFilePath);
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

        if ( DEBUG ) System.out.println("Querying differential file.");

        record = getRecordFromFile(key, differentialFilePath);

        if ( !record.equals("") ) {
            if ( DEBUG ) System.out.println("Found in differential file.");
            return record;
        }


        if ( DEBUG ) {
            System.out.println("Not found in differential file.");
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

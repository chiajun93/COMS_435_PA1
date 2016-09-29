import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

class Differential {

    static boolean DEBUG = false;

    public DifferentialRuntimeStats differentialRuntimeStats;

    // File paths
    String databaseFilePath;
    String differentialFilePath;

    Differential(String databaseFilePath, String differentialFilePath){
        this.databaseFilePath = databaseFilePath;
        this.differentialFilePath = differentialFilePath;

        differentialRuntimeStats = new DifferentialRuntimeStats();
        startTimer();
    }

    /**
     * Start the timer for empirical performance comparison
     */
    private void startTimer(){
        differentialRuntimeStats.startTime = new Date();
    }

    /**
     * Stop the empirical performance comparison timer
     */
    void endTimer(){
        differentialRuntimeStats.endTime = new Date();
        differentialRuntimeStats.runTime = differentialRuntimeStats.endTime.getTime() - differentialRuntimeStats.startTime.getTime();
    }

    /**
     * Given a file path, return the record associated to the key ( if it can be found in the file )
     * @param key The key to lookup
     * @param filePath The file to check
     * @return The record, or an empty string if it does not exist
     */
    String getRecordFromFile(String key, String filePath) {
        String result = "";
        int lineCount = 0;

        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String currentLine;

            while ( (currentLine = bufferedReader.readLine()) != null ) {
                String keyForLine = getKeyFromLine(currentLine);

                if ( keyForLine.equals(key) ) return currentLine;

                if ( lineCount % 1000000 == 0 && DEBUG ) System.out.println("Read line: " + ((lineCount / 1000000) + 1) + " million");

                lineCount++;
            }

        } catch ( IOException e ) {
            System.out.println(e.getLocalizedMessage());
            System.out.println("Failed to read file: " + filePath);
        }

        differentialRuntimeStats.linesRead += lineCount;

        return result;
    }


    /**
     * Get the key from a line of text (read from the differential file)
     * @param line the line in question
     * @return the key for the line
     */
    static String getKeyFromLine(String line){
        String[] words = line.split(" ");
        String firstInt = "";

        for (String word : words ) {
            try {
                // Check if we are still reading words, if not break and return the substring of 0 -> currentIndex
                firstInt = word;
                Integer.parseInt(word);
                break;
            } catch ( NumberFormatException e) {}
        }

        return line.substring(0, line.indexOf(firstInt)).trim(); // Remove last space
    }

    public class DifferentialRuntimeStats {
        int linesRead;
        Date startTime;
        Date endTime;
        long runTime;

        DifferentialRuntimeStats(){
            linesRead = 0;
            runTime = 0;
        }
    }
}
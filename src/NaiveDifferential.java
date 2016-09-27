import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by chiajun on 9/26/16.
 */
public class NaiveDifferential extends Differential {
    private HashSet<String> diffData;
    private HashSet<String> database;

    /**
     * Constructor for a naive differential application
     */
    public NaiveDifferential() {
        createData("DiffFile.txt");
        createData("database.txt");
    }

    private void createData(String filepath) {
        File inFile = new File(filepath);

        try {
            Scanner scan = new Scanner(inFile);
            String line;
            String keys;
            if(filepath.contains("DiffFile.txt")){
                diffData = new HashSet<>();

                while (scan.hasNextLine()) {
                    line = scan.nextLine();
                    keys = getKeyFromLine(line);
                    diffData.add(keys);
                }
            }
            else {
                database = new HashSet<>();

                while (scan.hasNextLine()) {
                    line = scan.nextLine();
                    keys = getKeyFromLine(line);
                    database.add(keys);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String retrieveRecord(String key) {
        String record = "";
        if (!diffData.contains(key)) {
            System.out.println("Records in not found in differential file. Looking at database now...");

            if(database.contains(key))
                System.out.println("Found the key in database.");
            else
                System.out.println("Key does not exist in both database and differential file.");
        }

        return record;
    }

    public HashSet<String> getDiffData() {
        return diffData;
    }

    public static void main(String[] args) {
        NaiveDifferential naiveDifferential = new NaiveDifferential();

        String record = naiveDifferential.retrieveRecord("ARE A FEW THINGS");
        System.out.println(record);

    }
}

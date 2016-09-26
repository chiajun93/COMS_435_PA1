import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by chiajun on 9/26/16.
 */
public class NaiveDifferential extends Differential {
    private HashSet<String> dict;

    /**
     * Constructor for a naive differential application
     */
    public NaiveDifferential() {
        createDiffData();
    }

    private void createDiffData() {
        File diffFile = new File("DiffFile.txt");

        try {
            Scanner scan = new Scanner(diffFile);
            dict = new HashSet<>();

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String keys = getKeyFromLine(line);
                dict.add(keys);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path.");
        }
    }

    @Override
    protected String retrieveRecord(String key) {
        String record = "";
        if (!dict.contains(key)) {
            System.out.println("Records in not found in differential file. Looking at database now...");

        }

        return record;
    }

    public HashSet<String> getDict() {
        return dict;
    }

    public static void main(String[] args) {
        NaiveDifferential naiveDifferential = new NaiveDifferential();

        String record = naiveDifferential.retrieveRecord("ARE A FEW THINGS");
        System.out.println(record);

    }
}

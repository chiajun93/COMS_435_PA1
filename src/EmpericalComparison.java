public class EmpericalComparison {

    BloomDifferential _bloomDifferential;
    NaiveDifferential _naiveDifferential;

    EmpericalComparison(BloomDifferential bloomDifferential, NaiveDifferential naiveDifferential) {
        _bloomDifferential = bloomDifferential;
        _naiveDifferential = naiveDifferential;
    }


    /**
     * Run the same lookups on both differentials comparing the runtime
     */
    public void compare(){
        System.out.println("Comparing BLOOM && NAIVE Differentials");

        String keyOneInDifFile = "ARTICLE_DET 1_NUM Section_NOUN 1_NUM";
        String keyTwoInDifFile = "are_VERB entered_VERB on_ADP cards_NOUN";
        String keyThreeInDifFile = "are the external appearances";
        String keyFourInDifFile = "are_VERB written_VERB up_PRT ,_.";
        String keyInDatabase = "ARCHBISHOP_NOUN LEIGHTON_NOUN ._. _END_";
        String keyInNeither = "Marty McFly";

        // Bloom differential
        System.out.println("Bloom Differential:");
        _bloomDifferential.createFilter();
        System.out.println(_bloomDifferential.retrieveRecord(keyOneInDifFile));
        System.out.println(_bloomDifferential.retrieveRecord(keyTwoInDifFile));
        System.out.println(_bloomDifferential.retrieveRecord(keyThreeInDifFile));
        System.out.println(_bloomDifferential.retrieveRecord(keyFourInDifFile));
        System.out.println(_bloomDifferential.retrieveRecord(keyInDatabase));
        System.out.println(_bloomDifferential.retrieveRecord(keyInNeither));
        _bloomDifferential.endTimer();

        // Naive differential
        System.out.println("Niave Differential:");
        System.out.println(_naiveDifferential.retrieveRecord(keyOneInDifFile));
        System.out.println(_naiveDifferential.retrieveRecord(keyTwoInDifFile));
        System.out.println(_naiveDifferential.retrieveRecord(keyThreeInDifFile));
        System.out.println(_naiveDifferential.retrieveRecord(keyFourInDifFile));
        System.out.println(_naiveDifferential.retrieveRecord(keyInDatabase));
        System.out.println(_naiveDifferential.retrieveRecord(keyInNeither));
        _naiveDifferential.endTimer();

        System.out.println("BloomDifferential:");
        System.out.println("Lines Read: " + _bloomDifferential.differentialRuntimeStats.linesRead);
        System.out.println("Run time: " + (_bloomDifferential.differentialRuntimeStats.runTime / 1000) + " seconds");
        System.out.println("Memory used: " + (((double) _bloomDifferential.bloomFilter.filterSize()) / 8000000) + " mb");

        System.out.println("");

        System.out.println("NaiveDifferential:");
        System.out.println("Lines Read: " + _naiveDifferential.differentialRuntimeStats.linesRead);
        System.out.println("Run time: " + (_naiveDifferential.differentialRuntimeStats.runTime / 1000) + " seconds");
        System.out.println("Memory Used: Constant (negligible)");

        System.out.println("");

        String fewestLinesReadDifferential = _bloomDifferential.differentialRuntimeStats.linesRead < _naiveDifferential.differentialRuntimeStats.linesRead ? "Bloom" : "Naive";
        String fastestDifferential = _bloomDifferential.differentialRuntimeStats.runTime < _naiveDifferential.differentialRuntimeStats.runTime ? "Bloom" : "Naive";

        System.out.println("Fewest Lines Read: " + fewestLinesReadDifferential + "Differential");
        System.out.println("Fastest run time: " + fastestDifferential + "Differential");
        System.out.println("Least Memory: Naive");
    }

    public static void main(String[] args){
        BloomDifferential bloomDifferential = new BloomDifferential("database.txt", "DiffFile.txt");
        NaiveDifferential naiveDifferential = new NaiveDifferential("database.txt", "DiffFile.txt");
        EmpericalComparison empericalComparison = new EmpericalComparison(bloomDifferential, naiveDifferential);
        empericalComparison.compare();
    }

}

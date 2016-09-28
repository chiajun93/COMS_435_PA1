/**
 * Empirical comparison class for comparing bloom and niava differential implementations
 */
public class EmpericalComparison {

    Differential bloomDifferential;
    Differential niaveDifferential;

    EmpericalComparison(Differential bloomDifferential, Differential niaveDifferential) {
        if ( bloomDifferential.differentialType == Differential.BLOOM )
            this.bloomDifferential = bloomDifferential;
         else
            throw new IllegalArgumentException("Invalid differential Type: Should be BLOOM");

        if ( niaveDifferential.differentialType == Differential.NAIVE )
            this.niaveDifferential = niaveDifferential;
        else
            throw new IllegalArgumentException("Invalid differential Type: Should be NAIVE");
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
        bloomDifferential.createFilter();
        System.out.println(bloomDifferential.retrieveRecord(keyOneInDifFile));
        System.out.println(bloomDifferential.retrieveRecord(keyTwoInDifFile));
        System.out.println(bloomDifferential.retrieveRecord(keyThreeInDifFile));
        System.out.println(bloomDifferential.retrieveRecord(keyFourInDifFile));
        System.out.println(bloomDifferential.retrieveRecord(keyInDatabase));
        System.out.println(bloomDifferential.retrieveRecord(keyInNeither));
        bloomDifferential.endTimer();

        // Naive differential
        System.out.println("Niave Differential:");
        System.out.println(niaveDifferential.retrieveRecord(keyOneInDifFile));
        System.out.println(niaveDifferential.retrieveRecord(keyTwoInDifFile));
        System.out.println(niaveDifferential.retrieveRecord(keyThreeInDifFile));
        System.out.println(niaveDifferential.retrieveRecord(keyFourInDifFile));
        System.out.println(niaveDifferential.retrieveRecord(keyInDatabase));
        System.out.println(niaveDifferential.retrieveRecord(keyInNeither));
        niaveDifferential.endTimer();

        System.out.println("BloomDifferential:");
        System.out.println("Lines Read: " + bloomDifferential.differentialRuntimeStats.linesRead);
        System.out.println("Run time: " + (bloomDifferential.differentialRuntimeStats.runTime / 1000) + " seconds");
        System.out.println("Memory used: " + (((double)bloomDifferential.bloomFilter.filterSize()) / 8000000) + " mb");

        System.out.println("");

        System.out.println("NiaveDifferential:");
        System.out.println("Lines Read: " + niaveDifferential.differentialRuntimeStats.linesRead);
        System.out.println("Run time: " + (niaveDifferential.differentialRuntimeStats.runTime / 1000) + " seconds");
        System.out.println("Memory Used: Constant (negligible)");

        System.out.println("");

        String fewestLinesReadDifferential = bloomDifferential.differentialRuntimeStats.linesRead < niaveDifferential.differentialRuntimeStats.linesRead ? "Bloom" : "Naive";
        String fastestDifferential = bloomDifferential.differentialRuntimeStats.runTime < niaveDifferential.differentialRuntimeStats.runTime ? "Bloom" : "Naive";

        System.out.println("Fewest Lines Read: " + fewestLinesReadDifferential + "Differential");
        System.out.println("Fastest run time: " + fastestDifferential + "Differential");
        System.out.println("Least Memory: Naive");
    }

    public static void main(String[] args){
        Differential bloomDifferential = new Differential("database.txt", "DiffFile.txt", Differential.BLOOM);
        Differential naiveDifferential = new Differential("database.txt", "DiffFile.txt", Differential.NAIVE);
        EmpericalComparison empericalComparison = new EmpericalComparison(bloomDifferential, naiveDifferential);
        empericalComparison.compare();
    }

}

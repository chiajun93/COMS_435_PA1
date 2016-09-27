/**
 * Created by paulgerlich on 9/26/16.
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


    public void compare(){
        System.out.println("Comparing BLOOM && NAIVE Differentials");

        // TODO: Run on some stuff

        System.out.println("Line Reads");
        System.out.println("Time Reads");
        System.out.println("Memory");
    }

    public static void main(String[] args){
        Differential bloomDifferential = new Differential("database.txt", "DiffFile.txt", Differential.BLOOM);
        Differential naiveDifferential = new Differential("database.txt", "DiffFile.txt", Differential.NAIVE);
        EmpericalComparison empericalComparison = new EmpericalComparison(bloomDifferential, naiveDifferential);
        empericalComparison.compare();
    }

}

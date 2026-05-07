import java.util.Random;
import de.svenjacobs.loremipsum.LoremIpsum;

public class Enigmes {
    private String textE;
    private int tempResolution; // temps de résolution
    private static Random rand = new Random(); 
    private LoremIpsum loremIpsum = new LoremIpsum();

    public Enigmes() {
        this.textE = genereLorem();
        this.tempResolution = rand.nextInt(20) + 1; 
    }

    public String genereLorem(){
        LoremIpsum loremIpsum = new LoremIpsum();
        String monTexte = loremIpsum.getWords(15);
        return monTexte;
    }

    public int getTempRes(){
        return this.tempResolution;
    }

    public String getTextE() {
        return this.textE;
    }

    // MIN = 1 MAX + 20 

}

import java.util.Random;
import de.svenjacobs.loremipsum.LoremIpsum;

public class Enigme {
    private String textE;
    private int tempResolution; 
    private static Random rand = new Random(); 
    private LoremIpsum loremIpsum = new LoremIpsum();

    // méthode d'initialisation poru que les enigmes ont un temps et une generation du lorem ipsum de maniere aléatoire.
    public Enigme() {
        this.textE = genereLorem();
        this.tempResolution = rand.nextInt(1000) + 1; 
    }
    
    public Enigme(String text, int temps) {
        this.textE = text;
        this.tempResolution = temps;
    }

    public String genereLorem(){
        LoremIpsum loremIpsum = new LoremIpsum();
        String monTexte = loremIpsum.getWords(rand.nextInt(50) + 1); // +1 dans le cas ou le random renvoie 0 
        return monTexte;
    }

    public int getTempRes(){
        return this.tempResolution;
    }

    public String getTextE() {
        return this.textE;
    }

}

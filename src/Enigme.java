import java.util.Random;
import de.svenjacobs.loremipsum.LoremIpsum;

public class Enigme {
    private String textE;
    private int tempResolution; // temps de résolution de l'énigmes
    private static Random rand = new Random(); 
    private LoremIpsum loremIpsum = new LoremIpsum();

    // méthode d'initialisation poru que les enigmes ont un temps et une generation du lorem ipsum de maniere aléatoire.
    public Enigme() {
        this.textE = genereLorem();
        this.tempResolution = rand.nextInt(20) + 1; 
    }

    public String genereLorem(){
        LoremIpsum loremIpsum = new LoremIpsum();
        String monTexte = loremIpsum.getWords(rand.nextInt(50) + 1); // +1 dans le cas ou le random renvoie 0 
        return monTexte;
    }

<<<<<<< HEAD:src/Enigmes.java
    // MIN = 1 MAX + 20 
=======
    public int getTempRes(){
        return this.tempResolution;
    }

    public String getTextE() {
        return this.textE;
    }
>>>>>>> 746b5e2 (Classe Enigems généré automatiquement a l'appel de Enigme()):src/Enigme.java

}
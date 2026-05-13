import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Item {
    private String nom;
    private int posO; // numéro de page où se trouve l'objet
    private static List<String> s = Arrays.asList("clé", "épée", "bouclier", "chalice", "lapin");

    public Item(int page) {
        Random r = new Random();
        this.posO = page;
        this.nom = s.get(r.nextInt(s.size()));
    }

    public String getNom() {
        return nom;
    }

    // Retourne le numéro de page (int) et non un objet Page
    public int getPosO() {
        return posO;
    }
}
import java.util.List;

public class Sauvegarde {
    public int pageCouranteId;
    public List<Item> inventaire;
    public int nbPages;
    public List<PageData> pages;
    public List<ArcData> arcs;

    // Représente l'état brut d'une page
    public static class PageData {
        public int id;
        public String type; 
        public String textEnigme;
        public int tempResolution;
        public Item obj;
    }

    // Représente une connexion entre deux pages
    public static class ArcData {
        public int sourceId;
        public int targetId;
        public double poids;
    }
}
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Livre{
    private int nbpage;
    private static Map<Integer,List<Map<String,String>>> enigmes = new HashMap<>();
    private Debut pDeb;
    private Fin pFin;
    private List<Page> pages;


    public Livre(int nbpage, Debut pDeb, Fin pFin) {
        this.nbpage = nbpage;
        this.pDeb = pDeb;
        this.pFin = pFin;
        this.pages = new ArrayList<>();
    }

    private void fill(){
        List<Map<String,String>> facile = new ArrayList<>();
        List<Map<String,String>> moyen = new ArrayList<>();
        List<Map<String,String>> dif = new ArrayList<>();
        enigmes.put(3, facile);
        enigmes.put(6, moyen);
        enigmes.put(9, dif);
    }
    
    
    
    
}
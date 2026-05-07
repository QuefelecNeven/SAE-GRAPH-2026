import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;



public class Livre{
    private int nbpage;
    private static List<Enigmes> enigmes = new ArrayList<>();
    private Debut pDeb;
    private Fin pFin;
    private List<Page> pages;


    public Livre(int nbpage, Debut pDeb, Fin pFin) {
        this.nbpage = nbpage;
        this.pDeb = new Debut();
        this.pFin = new Fin(nbpage);
        this.pages = new ArrayList<>();
        fill();
    }

    private Page createPage(int i){
        Random r = new Random();
        return new Page(enigmes.get(r.nextInt(enigmes.size())),i);
    }

    private void fill(){
        this.pages.add(pDeb);
        for(int i = 1; i < nbpage-1; i++)
            this.pages.add(createPage(i));
        this.pages.add(pFin);
    }
    
    
    
    
}
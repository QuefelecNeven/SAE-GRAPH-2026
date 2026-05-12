
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import java.util.Set;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import java.util.Collections;


public class Livre{
    private int nbpage;
    private static List<Enigme> enigmes = new ArrayList<>();
    private Debut pDeb;
    private Fin pFin;
    private List<Page> pages;
    private Graph<Page, DefaultWeightedEdge> graphe;
    private Set<Item> items;


    public Livre(int nbpage, Debut pDeb, Fin pFin,List<Integer> l) {
        this.nbpage = nbpage;
        this.pDeb = new Debut();
        this.pFin = new Fin(nbpage);
        this.pages = new ArrayList<>();
        items = poserObj(l);
        fill(); 
        this.graphe = initialiserGrapheAleatoire();
    }

    private Set<Item> poserObj(List<Integer> l){
        Set<Item> it = new HashSet<>();
        for (int i = 0; i<l.size();i++){
            it.add(new Item(i));
        }
    }

    private Graph<Page, DefaultWeightedEdge> initialiserGrapheSimple() {
        Graph<Page, DefaultWeightedEdge> g = 
            new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        
        Random random = new Random();
        List<Page> pageAvecObjet = new ArrayList<>();
        g.addVertex(pDeb);
        g.addVertex(pFin);
        for (Page page : pages) {
            g.addVertex(page);
            if (page.contientObjet()) {
                pageAvecObjet.add(page);
            }
        }
        Page anciennePage = pDeb;
        for (Page pageObjet : pageAvecObjet) {
            DefaultWeightedEdge arc = g.addEdge(anciennePage, pageObjet);
            g.setEdgeWeight(arc, pageObjet.getTempsResolution());
            anciennePage = pageObjet;
        }
        DefaultWeightedEdge arcFinal = g.addEdge(anciennePage, pFin);
        g.setEdgeWeight(arcFinal, pFin.getTempsResolution());
        List<Page> toutesPages = new ArrayList<>(pages);
        toutesPages.add(pDeb);
        toutesPages.add(pFin);

        for (Page page : toutesPages) {
            int nbArcs = 2 + random.nextInt(2);
            List<Page> cibles = new ArrayList<>(toutesPages);
            cibles.remove(page);
            Collections.shuffle(cibles, random);

            int ajouts = 0;
            for (Page cible : cibles) {
                if (ajouts >= nbArcs) break;
                if (g.getEdge(page, cible) == null) {
                    DefaultWeightedEdge arc = g.addEdge(page, cible);
                    g.setEdgeWeight(arc, cible.getTempsResolution());
                    ajouts++;
                }
            }
        }

        return g;
    }

    private Graph<Page, DefaultWeightedEdge> initialiserGrapheAleatoire() {
        Graph<Page, DefaultWeightedEdge> g = 
            new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        Random random = new Random();
        List<Page> toutesPages = new ArrayList<>(pages);
        toutesPages.add(0, pDeb);
        toutesPages.add(pFin);
        for (Page page : toutesPages) {
            g.addVertex(page);
        }

        for (Page page : toutesPages) {
            int nbArcs = 2 + random.nextInt(2);
            List<Page> cibles = new ArrayList<>(toutesPages);
            cibles.remove(page);
            Collections.shuffle(cibles, random);

            int ajouts = 0;
            for (Page cible : cibles) {
                if (ajouts >= nbArcs) break;
                if (g.getEdge(page, cible) == null) {
                    DefaultWeightedEdge arc = g.addEdge(page, cible);
                    g.setEdgeWeight(arc, cible.getTempsResolution());
                    ajouts++;
                }
            }
        }

        for (Page page : toutesPages) {
            if (!estAtteignable(g, pDeb, page)) {
                Page source = trouverPageAtteignable(g, pDeb, toutesPages, random);
                DefaultWeightedEdge arc = g.addEdge(source, page);
                g.setEdgeWeight(arc, page.getTempsResolution());
            }
        }

        for (Page page : toutesPages) {
            if (!estAtteignable(g, page, pageSortie)) {
                DefaultWeightedEdge arc = g.addEdge(page, pageSortie);
                g.setEdgeWeight(arc, pageSortie.getTempsResolution());
            }
        }

        return g;
    }
  
    private void initEnigmes(){
        if(enigmes.isEmpty()){
            for(int i = 0; i < 10; i++){
                enigmes.add(new Enigme());
            }
        }
    }

    private Page createPage(int i){
        Random r = new Random();
        return new Page(enigmes.get(r.nextInt(enigmes.size())),i);
    }

    private void fill(){
        initEnigmes();
        this.pages.add(pDeb);
        for(int i = 1; i < nbpage-1; i++)
            this.pages.add(createPage(i));
        this.pages.add(pFin);
    }


}

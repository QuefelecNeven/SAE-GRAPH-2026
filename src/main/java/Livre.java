import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Livre {
    private int nbpage;
    private static List<Enigme> enigmes = new ArrayList<>();
    private Debut pDeb;
    private Fin pFin;
    private List<Page> pages;
    private Graph<Page, DefaultWeightedEdge> graphe;
    private Set<Item> items;

<<<<<<< HEAD
<<<<<<< HEAD
    public Livre(int nbpage, List<Integer> l) {
        this.nbpage = nbpage;
        this.pDeb = new Debut(0);
        this.pFin = new Fin(nbpage);
        this.pages = new ArrayList<>();
        this.items = poserObj(l);
        remplirLesPages();
        this.graphe = initialiserGrapheAleatoire(); 
    }

    public void utiliserGrapheSimple() {
        this.graphe = initialiserGrapheSimple();
    }

    public void utiliserGrapheAleatoire() {
=======
=======
>>>>>>> d2714cb (Fin)

    public Livre(int nbpage, int pDeb, int pFin,List<Integer> l) {
=======
    public Livre(int nbpage, List<Integer> l) {
>>>>>>> 556f834 (Fin)
        this.nbpage = nbpage;
        this.pDeb = new Debut(0);
        this.pFin = new Fin(nbpage);
        this.pages = new ArrayList<>();
<<<<<<< HEAD
        fill(); 
        items = poserObj(l);
<<<<<<< HEAD
>>>>>>> 56cd319 (poser objet fait et fill modifié)
        this.graphe = initialiserGrapheAleatoire();
    }

=======
=======
        this.items = poserObj(l);
        fill();
        this.graphe = initialiserGrapheAleatoire(); // par défaut
    }

    public void utiliserGrapheSimple() {
        this.graphe = initialiserGrapheSimple();
    }

    public void utiliserGrapheAleatoire() {
>>>>>>> 556f834 (Fin)
        this.graphe = initialiserGrapheAleatoire();
    }

    // ------------------------------------------------------------------
    // Accesseurs
    // ------------------------------------------------------------------
>>>>>>> d2714cb (Fin)

    public Page getDebut() {
        return pDeb;
    }

    public Page getFin() {
        return pFin;
    }

    public List<Page> getPagesSuivantes(Page p) {
        List<Page> suivantes = new ArrayList<>();
        for (DefaultWeightedEdge arc : graphe.outgoingEdgesOf(p)) {
            suivantes.add(graphe.getEdgeTarget(arc));
        }
<<<<<<< HEAD
        for(Item obj : items){
            this.pages.get(obj.getPosO()).addobj(obj);
        }
        return it;
=======
        return suivantes;
    }

    public Graph<Page, DefaultWeightedEdge> getGraphe() {
        return graphe;
    }

    // ------------------------------------------------------------------
    // Construction des items
    // ------------------------------------------------------------------

    private Set<Item> poserObj(List<Integer> l) {
        Set<Item> it = new HashSet<>();
        for (int i = 0; i < l.size(); i++) {
            it.add(new Item(l.get(i)));
        }
        return it; // return manquant dans l'original
>>>>>>> 556f834 (Fin)
    }

    // ------------------------------------------------------------------
    // Graphe simple (chemin garanti passant par les pages avec objets)
    // ------------------------------------------------------------------

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

<<<<<<< HEAD
=======
        // Chemin principal : pDeb → pages avec objets → pFin
>>>>>>> d2714cb (Fin)
        Page anciennePage = pDeb;
        for (Page pageObjet : pageAvecObjet) {
            DefaultWeightedEdge arc = g.addEdge(anciennePage, pageObjet);
            g.setEdgeWeight(arc, pageObjet.getTempsResolution());
            anciennePage = pageObjet;
        }
        DefaultWeightedEdge arcFinal = g.addEdge(anciennePage, pFin);
        g.setEdgeWeight(arcFinal, pFin.getTempsResolution());

<<<<<<< HEAD
=======
        // Arcs aléatoires supplémentaires (2 à 3 par sommet)
>>>>>>> d2714cb (Fin)
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

    // ------------------------------------------------------------------
    // Graphe aléatoire (avec garantie d'atteignabilité)
    // ------------------------------------------------------------------

    private Graph<Page, DefaultWeightedEdge> initialiserGrapheAleatoire() {
        Graph<Page, DefaultWeightedEdge> g =
                new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        //ajoute les pages dans le graph
        Random random = new Random();
        List<Page> toutesPages = new ArrayList<>(pages);
        toutesPages.add(0, pDeb);
        toutesPages.add(pFin);

        for (Page page : toutesPages) {
            g.addVertex(page);
        }

<<<<<<< HEAD
        //ajoute les liaison random
=======
        // Arcs aléatoires (2 à 3 par sommet)
>>>>>>> d2714cb (Fin)
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
<<<<<<< HEAD
        //verif toujours connexe
=======

        // Garantir que chaque page est atteignable depuis pDeb
>>>>>>> d2714cb (Fin)
        for (Page page : toutesPages) {
            if (!estAtteignable(g, pDeb, page)) {
                Page source = trouverPageAtteignable(g, pDeb, toutesPages, random);
                DefaultWeightedEdge arc = g.addEdge(source, page);
                g.setEdgeWeight(arc, page.getTempsResolution());
            }
        }

<<<<<<< HEAD
        //verif que la fin est atteignable
=======
        // Garantir que pFin est atteignable depuis chaque page
>>>>>>> d2714cb (Fin)
        for (Page page : toutesPages) {
            if (!estAtteignable(g, page, pFin)) {
                DefaultWeightedEdge arc = g.addEdge(page, pFin);
                g.setEdgeWeight(arc, pFin.getTempsResolution());
            }
        }

        return g;
    }
<<<<<<< HEAD
    // permet de savoir si il existe un chemin depuis le sommet source au sommets cible donné en parametre sur un graph donné lui aussi
=======

    // ------------------------------------------------------------------
    // Utilitaires graphe
    // ------------------------------------------------------------------

>>>>>>> d2714cb (Fin)
    private boolean estAtteignable(Graph<Page, DefaultWeightedEdge> g, Page source, Page cible) {
        if (source.equals(cible)) return true;
        Set<Page> visites = new HashSet<>();
        List<Page> file = new ArrayList<>();
        file.add(source);
        while (!file.isEmpty()) {
            Page courant = file.remove(0);
            if (courant.equals(cible)) return true;
            if (visites.contains(courant)) continue;
            visites.add(courant);
            for (DefaultWeightedEdge arc : g.outgoingEdgesOf(courant)) {
                file.add(g.getEdgeTarget(arc));
            }
        }
        return false;
    }
    // verifie pour toutes les pages si il existe un chemin entre la page de debut et la pget donnée en parametre
    private Page trouverPageAtteignable(Graph<Page, DefaultWeightedEdge> g, Page pDeb,
                                        List<Page> toutesPages, Random random) {
        List<Page> atteignables = new ArrayList<>();
        for (Page p : toutesPages) {
            if (estAtteignable(g, pDeb, p)) {
                atteignables.add(p);
            }
        }
        return atteignables.get(random.nextInt(atteignables.size()));
    }

<<<<<<< HEAD
=======
    // ------------------------------------------------------------------
    // Initialisation des pages
    // ------------------------------------------------------------------
>>>>>>> d2714cb (Fin)

    private void initEnigmes() {
        if (enigmes.isEmpty()) {
            for (int i = 0; i < 10; i++) {
                enigmes.add(new Enigme());
            }
        }
    }

    private Page createPage(int i) {
        Random r = new Random();
        return new Page(enigmes.get(r.nextInt(enigmes.size())), i);
    }

    private void remplirLesPages() {
        initEnigmes();
<<<<<<< HEAD
        for(int i = 1; i < nbpage-1; i++)
            this.pages.add(createPage(i));
<<<<<<< HEAD
<<<<<<< HEAD
        }
        this.pages.add(pFin);

=======
=======
        this.pages.add(pDeb);
        for (int i = 1; i < nbpage - 1; i++) {
            this.pages.add(createPage(i));
        }
        this.pages.add(pFin);

        // Assigner chaque item à la page correspondante
>>>>>>> d2714cb (Fin)
        for (Item item : items) {
            for (Page page : pages) {
                if (page.getNumP() == item.getPosO()) {
                    page.setObj(item);
                    break;
                }
            }
        }
<<<<<<< HEAD
=======
>>>>>>> 56cd319 (poser objet fait et fill modifié)
=======
>>>>>>> 556f834 (Fin)
>>>>>>> d2714cb (Fin)
    }
    
    
     //Reconstruit intégralement le graphe à partir du fichier de sauvegarde
     
    public void chargerDepuisSauvegarde(Sauvegarde save) {
        this.nbpage = save.nbPages;
        this.pages = new ArrayList<>();
        this.items = new HashSet<>();
        
        this.graphe = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        for (Sauvegarde.PageData pd : save.pages) {
            Page p;
            if ("Debut".equals(pd.type)) {
                this.pDeb = new Debut(pd.id);
                p = this.pDeb;
            } else if ("Fin".equals(pd.type)) {
                this.pFin = new Fin(pd.id);
                p = this.pFin;
            } else {
                p = new Page(pd.id);
                if (pd.textEnigme != null) {
                    p.setEnigme(new Enigme(pd.textEnigme, pd.tempResolution)); 
                }
            }
            if (pd.obj != null) {
                p.setObj(pd.obj);
                this.items.add(pd.obj);
            }
            this.pages.add(p);
            this.graphe.addVertex(p);
        }

        for (Sauvegarde.ArcData ad : save.arcs) {
            Page source = getPageById(ad.sourceId);
            Page cible = getPageById(ad.targetId);
            if (source != null && cible != null) {
                DefaultWeightedEdge arc = this.graphe.addEdge(source, cible);
                this.graphe.setEdgeWeight(arc, ad.poids);
            }
        }
    }

}
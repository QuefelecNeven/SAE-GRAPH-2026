import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
<<<<<<< HEAD
<<<<<<< HEAD
import java.util.LinkedList;
=======
>>>>>>> 104af6c (Algorithme Djikstra implémenter a ajouter dans le menu)
=======
import java.util.LinkedList;
>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
<<<<<<< HEAD
<<<<<<< HEAD
import java.util.Queue;
=======
>>>>>>> 104af6c (Algorithme Djikstra implémenter a ajouter dans le menu)
=======
import java.util.Queue;
>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

// Algorithme implémenter a l'aide de ce site internet https://www.baeldung.com/java-dijkstra
public class Algorithme {
    private Livre livre;
    private Graph<Page, DefaultWeightedEdge> graphe;
    private double meilleurTemps = -1.0;

    public Algorithme(Livre livre) {
        this.livre = livre;
        this.graphe = livre.getGraphe();
    }


    private class Etat {
        Page page;
<<<<<<< HEAD
<<<<<<< HEAD
        Set<Integer> objetsRecuperes;
=======
        Set<Integer> objetsRecuperes; 
>>>>>>> 104af6c (Algorithme Djikstra implémenter a ajouter dans le menu)
=======
        Set<Integer> objetsRecuperes;
>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)

        Etat(Page page, Set<Integer> objetsRecuperes) {
            this.page = page;
            this.objetsRecuperes = new HashSet<>(objetsRecuperes);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Etat etat = (Etat) o;
            return page.equals(etat.page) && objetsRecuperes.equals(etat.objetsRecuperes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(page, objetsRecuperes);
        }
    }


    private class NoeudDijkstra implements Comparable<NoeudDijkstra> {
        Etat etat;
        double tempsCumule;
        List<Page> chemin;

        NoeudDijkstra(Etat etat, double tempsCumule, List<Page> chemin) {
            this.etat = etat;
            this.tempsCumule = tempsCumule;
            this.chemin = new ArrayList<>(chemin);
        }

        @Override
        public int compareTo(NoeudDijkstra autre) {
            return Double.compare(this.tempsCumule, autre.tempsCumule);
        }
    }

    public List<Page> executer() {
        Page pDeb = livre.getDebut();
<<<<<<< HEAD
<<<<<<< HEAD

=======
        
>>>>>>> 104af6c (Algorithme Djikstra implémenter a ajouter dans le menu)
=======

>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
        int totalObjets = 0;
        for (Page p : graphe.vertexSet()) {
            if (p.contientObjet()) {
                totalObjets++;
            }
        }

        PriorityQueue<NoeudDijkstra> fileAttente = new PriorityQueue<>();
        Map<Etat, Double> tempsMinimum = new HashMap<>();

        Set<Integer> objetsDepart = new HashSet<>();
        if (pDeb.contientObjet()) objetsDepart.add(pDeb.getNumP());
<<<<<<< HEAD
<<<<<<< HEAD

        Etat etatInitial = new Etat(pDeb, objetsDepart);
        List<Page> cheminInitial = new ArrayList<>();
        cheminInitial.add(pDeb);

=======
        
        Etat etatInitial = new Etat(pDeb, objetsDepart);
        List<Page> cheminInitial = new ArrayList<>();
        cheminInitial.add(pDeb);
        
>>>>>>> 104af6c (Algorithme Djikstra implémenter a ajouter dans le menu)
=======

        Etat etatInitial = new Etat(pDeb, objetsDepart);
        List<Page> cheminInitial = new ArrayList<>();
        cheminInitial.add(pDeb);

>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
        fileAttente.add(new NoeudDijkstra(etatInitial, 0.0, cheminInitial));
        tempsMinimum.put(etatInitial, 0.0);

        while (!fileAttente.isEmpty()) {
<<<<<<< HEAD
<<<<<<< HEAD
            NoeudDijkstra courant = fileAttente.poll();

=======
            NoeudDijkstra courant = fileAttente.poll(); 
            
            
>>>>>>> 104af6c (Algorithme Djikstra implémenter a ajouter dans le menu)
=======
            NoeudDijkstra courant = fileAttente.poll();

>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
            if (courant.tempsCumule > tempsMinimum.getOrDefault(courant.etat, Double.MAX_VALUE)) {
                continue;
            }

<<<<<<< HEAD
<<<<<<< HEAD
            if (courant.etat.page instanceof Fin && courant.etat.objetsRecuperes.size() == totalObjets) {
                this.meilleurTemps = courant.tempsCumule;
                return courant.chemin;
            }

            for (DefaultWeightedEdge arc : graphe.outgoingEdgesOf(courant.etat.page)) {
                Page voisin = graphe.getEdgeTarget(arc);
                double tempsDeLenigme = graphe.getEdgeWeight(arc);

=======

=======
>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
            if (courant.etat.page instanceof Fin && courant.etat.objetsRecuperes.size() == totalObjets) {
                this.meilleurTemps = courant.tempsCumule;
                return courant.chemin;
            }

            for (DefaultWeightedEdge arc : graphe.outgoingEdgesOf(courant.etat.page)) {
                Page voisin = graphe.getEdgeTarget(arc);
                double tempsDeLenigme = graphe.getEdgeWeight(arc);
<<<<<<< HEAD
                
>>>>>>> 104af6c (Algorithme Djikstra implémenter a ajouter dans le menu)
=======

>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
                Set<Integer> nouveauxObjets = new HashSet<>(courant.etat.objetsRecuperes);
                if (voisin.contientObjet()) {
                    nouveauxObjets.add(voisin.getNumP());
                }
<<<<<<< HEAD
<<<<<<< HEAD

=======
                
>>>>>>> 104af6c (Algorithme Djikstra implémenter a ajouter dans le menu)
=======

>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
                Etat nouvelEtat = new Etat(voisin, nouveauxObjets);
                double nouveauTemps = courant.tempsCumule + tempsDeLenigme;

                if (nouveauTemps < tempsMinimum.getOrDefault(nouvelEtat, Double.MAX_VALUE)) {
                    tempsMinimum.put(nouvelEtat, nouveauTemps);
<<<<<<< HEAD
<<<<<<< HEAD

                    List<Page> nouveauChemin = new ArrayList<>(courant.chemin);
                    nouveauChemin.add(voisin);

=======
                    
                    List<Page> nouveauChemin = new ArrayList<>(courant.chemin);
                    nouveauChemin.add(voisin);
                    
>>>>>>> 104af6c (Algorithme Djikstra implémenter a ajouter dans le menu)
=======

                    List<Page> nouveauChemin = new ArrayList<>(courant.chemin);
                    nouveauChemin.add(voisin);

>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
                    fileAttente.add(new NoeudDijkstra(nouvelEtat, nouveauTemps, nouveauChemin));
                }
            }
        }
<<<<<<< HEAD
<<<<<<< HEAD

        return new ArrayList<>();
=======
        
        return new ArrayList<>(); 
>>>>>>> 104af6c (Algorithme Djikstra implémenter a ajouter dans le menu)
=======

        return new ArrayList<>();
>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
    }

    public double getMeilleurTemps() {
        return meilleurTemps;
    }
<<<<<<< HEAD
<<<<<<< HEAD


    private int nbPagesVisitees = 0;
    private double tempsBFS = -1.0;
    private int nbPagesVisiteesDijkstra = 0;
=======


    private int nbPagesVisitees = 0;
    private double tempsBFS = -1.0;
>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)

    public List<Page> executerBFS() {
        Page pDeb = livre.getDebut();

<<<<<<< HEAD
<<<<<<< HEAD
=======
        // Compter le total d'objets à ramasser
>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
=======
>>>>>>> ec84cfc (Ajout temps exec)
        int totalObjets = 0;
        for (Page p : graphe.vertexSet()) {
            if (p.contientObjet()) totalObjets++;
        }

<<<<<<< HEAD
<<<<<<< HEAD

=======
        // Chaque entrée de la file contient : [chemin, objetsRecuperes]
        // On réutilise NoeudDijkstra mais avec tempsCumule = nb de pages (non utilisé ici)
        // On crée une file de paires chemin + objets trouvés
>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
=======

>>>>>>> ec84cfc (Ajout temps exec)
        Queue<NoeudBFS> file = new LinkedList<>();
        Set<Etat> vus = new HashSet<>();

        Set<Integer> objetsDepart = new HashSet<>();
        if (pDeb.contientObjet()) objetsDepart.add(pDeb.getNumP());

        Etat etatInitial = new Etat(pDeb, objetsDepart);
        List<Page> cheminInitial = new ArrayList<>();
        cheminInitial.add(pDeb);

        file.add(new NoeudBFS(etatInitial, cheminInitial));
        vus.add(etatInitial);

        while (!file.isEmpty()) {
<<<<<<< HEAD
        nbPagesVisiteesDijkstra = 0;
=======
>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
            NoeudBFS courant = file.poll();
            Page pageCourante = courant.etat.page;
            nbPagesVisitees++;

<<<<<<< HEAD
<<<<<<< HEAD
            if (pageCourante instanceof Fin && courant.etat.objetsRecuperes.size() == totalObjets) {

=======
            // Condition d'arrivée : on est à la Fin ET tous les objets sont ramassés
            if (pageCourante instanceof Fin && courant.etat.objetsRecuperes.size() == totalObjets) {
                // Calculer le temps total du chemin trouvé
>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
=======
            if (pageCourante instanceof Fin && courant.etat.objetsRecuperes.size() == totalObjets) {

>>>>>>> ec84cfc (Ajout temps exec)
                tempsBFS = 0.0;
                List<Page> c = courant.chemin;
                for (int i = 0; i < c.size() - 1; i++) {
                    DefaultWeightedEdge arc = graphe.getEdge(c.get(i), c.get(i + 1));
                    if (arc != null) tempsBFS += graphe.getEdgeWeight(arc);
                }
                return courant.chemin;
            }

<<<<<<< HEAD
<<<<<<< HEAD
            for (DefaultWeightedEdge arc : graphe.outgoingEdgesOf(pageCourante)) {
                Page voisin = graphe.getEdgeTarget(arc);

=======
            // Explorer les voisins
            for (DefaultWeightedEdge arc : graphe.outgoingEdgesOf(pageCourante)) {
                Page voisin = graphe.getEdgeTarget(arc);

                // Copier les objets courants et ramasser celui du voisin si besoin
>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
=======
            for (DefaultWeightedEdge arc : graphe.outgoingEdgesOf(pageCourante)) {
                Page voisin = graphe.getEdgeTarget(arc);

>>>>>>> ec84cfc (Ajout temps exec)
                Set<Integer> nouveauxObjets = new HashSet<>(courant.etat.objetsRecuperes);
                if (voisin.contientObjet()) {
                    nouveauxObjets.add(voisin.getNumP());
                }

                Etat nouvelEtat = new Etat(voisin, nouveauxObjets);

<<<<<<< HEAD
<<<<<<< HEAD
=======
                // Ne pas revisiter le même état (même page + mêmes objets)
>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
=======
>>>>>>> ec84cfc (Ajout temps exec)
                if (!vus.contains(nouvelEtat)) {
                    vus.add(nouvelEtat);
                    List<Page> nouveauChemin = new ArrayList<>(courant.chemin);
                    nouveauChemin.add(voisin);
                    file.add(new NoeudBFS(nouvelEtat, nouveauChemin));
                }
            }
        }

<<<<<<< HEAD
<<<<<<< HEAD
            nbPagesVisiteesDijkstra++;
        return new ArrayList<>();
    }

=======
        // Aucun chemin trouvé avec tous les objets
        return new ArrayList<>();
    }

    // Classe interne simple pour le BFS
>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
=======
        return new ArrayList<>();
    }

>>>>>>> ec84cfc (Ajout temps exec)
    private class NoeudBFS {
        Etat etat;
        List<Page> chemin;

        NoeudBFS(Etat etat, List<Page> chemin) {
            this.etat = etat;
            this.chemin = new ArrayList<>(chemin);
        }
    }

    public int getNbPagesVisitees() {
        return nbPagesVisitees;
    }

    public double getTempsBFS() {
        return tempsBFS;
    }
<<<<<<< HEAD

    public int getNbPagesVisiteesDijkstra() {
        return nbPagesVisiteesDijkstra;
    }
=======
>>>>>>> 104af6c (Algorithme Djikstra implémenter a ajouter dans le menu)
=======
>>>>>>> dda5d0e (tous est bon et ca J'AIME BIEN)
}
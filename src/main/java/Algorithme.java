import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
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
        Set<Integer> objetsRecuperes;

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

        Etat etatInitial = new Etat(pDeb, objetsDepart);
        List<Page> cheminInitial = new ArrayList<>();
        cheminInitial.add(pDeb);

        fileAttente.add(new NoeudDijkstra(etatInitial, 0.0, cheminInitial));
        tempsMinimum.put(etatInitial, 0.0);

        while (!fileAttente.isEmpty()) {
            NoeudDijkstra courant = fileAttente.poll();

            if (courant.tempsCumule > tempsMinimum.getOrDefault(courant.etat, Double.MAX_VALUE)) {
                continue;
            }

            if (courant.etat.page instanceof Fin && courant.etat.objetsRecuperes.size() == totalObjets) {
                this.meilleurTemps = courant.tempsCumule;
                return courant.chemin;
            }

            for (DefaultWeightedEdge arc : graphe.outgoingEdgesOf(courant.etat.page)) {
                Page voisin = graphe.getEdgeTarget(arc);
                double tempsDeLenigme = graphe.getEdgeWeight(arc);

                Set<Integer> nouveauxObjets = new HashSet<>(courant.etat.objetsRecuperes);
                if (voisin.contientObjet()) {
                    nouveauxObjets.add(voisin.getNumP());
                }

                Etat nouvelEtat = new Etat(voisin, nouveauxObjets);
                double nouveauTemps = courant.tempsCumule + tempsDeLenigme;

                if (nouveauTemps < tempsMinimum.getOrDefault(nouvelEtat, Double.MAX_VALUE)) {
                    tempsMinimum.put(nouvelEtat, nouveauTemps);

                    List<Page> nouveauChemin = new ArrayList<>(courant.chemin);
                    nouveauChemin.add(voisin);

                    fileAttente.add(new NoeudDijkstra(nouvelEtat, nouveauTemps, nouveauChemin));
                }
            }
        }

        return new ArrayList<>();
    }

    public double getMeilleurTemps() {
        return meilleurTemps;
    }


    private int nbPagesVisitees = 0;
    private double tempsBFS = -1.0;
    private int nbPagesVisiteesDijkstra = 0;

    public List<Page> executerBFS() {
        Page pDeb = livre.getDebut();

        int totalObjets = 0;
        for (Page p : graphe.vertexSet()) {
            if (p.contientObjet()) totalObjets++;
        }


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
        nbPagesVisiteesDijkstra = 0;
            NoeudBFS courant = file.poll();
            Page pageCourante = courant.etat.page;
            nbPagesVisitees++;

            if (pageCourante instanceof Fin && courant.etat.objetsRecuperes.size() == totalObjets) {

                tempsBFS = 0.0;
                List<Page> c = courant.chemin;
                for (int i = 0; i < c.size() - 1; i++) {
                    DefaultWeightedEdge arc = graphe.getEdge(c.get(i), c.get(i + 1));
                    if (arc != null) tempsBFS += graphe.getEdgeWeight(arc);
                }
                return courant.chemin;
            }

            for (DefaultWeightedEdge arc : graphe.outgoingEdgesOf(pageCourante)) {
                Page voisin = graphe.getEdgeTarget(arc);

                Set<Integer> nouveauxObjets = new HashSet<>(courant.etat.objetsRecuperes);
                if (voisin.contientObjet()) {
                    nouveauxObjets.add(voisin.getNumP());
                }

                Etat nouvelEtat = new Etat(voisin, nouveauxObjets);

                if (!vus.contains(nouvelEtat)) {
                    vus.add(nouvelEtat);
                    List<Page> nouveauChemin = new ArrayList<>(courant.chemin);
                    nouveauChemin.add(voisin);
                    file.add(new NoeudBFS(nouvelEtat, nouveauChemin));
                }
            }
        }

            nbPagesVisiteesDijkstra++;
        return new ArrayList<>();
    }

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

    public int getNbPagesVisiteesDijkstra() {
        return nbPagesVisiteesDijkstra;
    }
}
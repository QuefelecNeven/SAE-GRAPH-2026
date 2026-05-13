import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

public class Menu {
    private Livre livre;
    private Page pageCourante;
    private List<Item> inventaire;
    private Scanner scanner;

    public Menu(Livre livre) {
        this.livre = livre;
        this.pageCourante = livre.getDebut();
        this.inventaire = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    // ------------------------------------------------------------------
    // Menu principal (boucle jusqu'à Quitter)
    // ------------------------------------------------------------------

    public void afficherMenuPrincipal() {
        choisirTypeGraphe();
        int choix = -1;
        while (choix != 5) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("  1. Jouer (Mode Humain)");
            System.out.println("  2. Jouer (Mode Algorithme)");
            System.out.println("  3. Exporter le graphe en .dot / PDF");
            System.out.println("  4. Changer le type de graphe");
            System.out.println("  5. Quitter");
            System.out.print("Votre choix : ");

            choix = -1;
            while (choix < 1 || choix > 5) {
                if (scanner.hasNextInt()) {
                    choix = scanner.nextInt();
                } else {
                    scanner.next();
                }
            }

            switch (choix) {
                case 1 -> jouerHumain();
                case 2 -> jouerIA();
                case 3 -> exporterGraphe();
                case 4 -> choisirTypeGraphe();
                case 5 -> System.out.println("Au revoir !");
            }
        }
    }

    private void choisirTypeGraphe() {
        System.out.println("\n=== TYPE DE GRAPHE ===");
        System.out.println("  1. Graphe Simple (chemin garanti passant par les objets)");
        System.out.println("  2. Graphe Aléatoire (avec garantie d'atteignabilité)");
        System.out.print("Votre choix : ");

        int choix = -1;
        while (choix < 1 || choix > 2) {
            if (scanner.hasNextInt()) {
                choix = scanner.nextInt();
            } else {
                scanner.next();
            }
        }

        if (choix == 1) {
            livre.utiliserGrapheSimple();
            System.out.println("Graphe Simple sélectionné.");
        } else {
            livre.utiliserGrapheAleatoire();
            System.out.println("Graphe Aléatoire sélectionné.");
        }
    }

    // ------------------------------------------------------------------
    // Mode humain
    // ------------------------------------------------------------------

    public void jouerHumain() {
        System.out.println("\n=== DÉBUT DE L'AVENTURE (Mode Humain) ===");
        while (!(pageCourante instanceof Fin)) {
            afficherEtat();

            List<Page> choix = livre.getPagesSuivantes(pageCourante);

            if (choix.isEmpty()) {
                System.out.println("Aucune destination disponible. Fin forcée.");
                break;
            }

            System.out.println("\nDestinations possibles :");
            for (int i = 0; i < choix.size(); i++) {
                System.out.println("  " + (i + 1) + ". Page " + choix.get(i).getNumP());
            }

            int index = lireChoix(choix.size());
            pageCourante = choix.get(index);
        }
        terminerPartie();
    }

    /** Lit et valide le choix du joueur (retourne un index 0-based). */
    private int lireChoix(int max) {
        int index = -1;
        while (index < 0 || index >= max) {
            System.out.print("Votre choix (1-" + max + ") : ");
            if (scanner.hasNextInt()) {
                index = scanner.nextInt() - 1;
                if (index < 0 || index >= max) {
                    System.out.println("Choix invalide, réessayez.");
                }
            } else {
                System.out.println("Entrée non reconnue, veuillez saisir un nombre.");
                scanner.next();
            }
        }
        return index;
    }

    // ------------------------------------------------------------------
    // Mode IA
    // ------------------------------------------------------------------

    public void jouerIA() {
        System.out.println("\n=== DÉBUT DE L'AVENTURE (Mode Algorithme) ===");
        System.out.println("L'algorithme n'est pas encore implémenté.");
    }

    // ------------------------------------------------------------------
    // Export DOT
    // ------------------------------------------------------------------

    public void exporterGraphe() {
        String nomFichier = "graph.dot";
        DOTExporter<Page, DefaultWeightedEdge> exporter = new DOTExporter<>(
            page -> "page_" + page.getNumP()
        );

        // Label du sommet : numéro de page + indication s'il contient un objet
        exporter.setVertexAttributeProvider(page -> {
            Map<String, Attribute> attrs = new LinkedHashMap<>();
            String label = "Page " + page.getNumP();
            if (page.contientObjet()) {
                label += "\n[Objet : " + page.getObj().getNom() + "]";
            }
            attrs.put("label", DefaultAttribute.createAttribute(label));
            return attrs;
        });

        // Label de l'arc : poids (temps de résolution)
        exporter.setEdgeAttributeProvider(arc -> {
            Map<String, Attribute> attrs = new LinkedHashMap<>();
            double poids = livre.getGraphe().getEdgeWeight(arc);
            attrs.put("label", DefaultAttribute.createAttribute(String.valueOf((int) poids)));
            return attrs;
        });

        try (FileWriter writer = new FileWriter(nomFichier)) {
            exporter.exportGraph(livre.getGraphe(), writer);
            System.out.println("Graphe exporté dans : " + nomFichier);
        } catch (IOException e) {
            System.out.println("Erreur lors de l'export : " + e.getMessage());
            return;
        }

        // Génération automatique du PDF via la commande dot
        try {
            Process process = new ProcessBuilder("dot", "-T", "pdf", nomFichier, "-o", "graph.pdf")
                .inheritIO()
                .start();
            int code = process.waitFor();
            if (code == 0) {
                System.out.println("PDF généré : graph.pdf");
            } else {
                System.out.println("Erreur lors de la génération du PDF (code " + code + ").");
                System.out.println("Vérifiez que graphviz est installé : sudo apt install graphviz");
            }
        } catch (IOException e) {
            System.out.println("Impossible de lancer 'dot' : " + e.getMessage());
            System.out.println("Installez graphviz : sudo apt install graphviz");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Génération interrompue.");
        }
        // On ne quitte pas : retour automatique au menu principal
    }

    // ------------------------------------------------------------------
    // Affichage de l'état courant
    // ------------------------------------------------------------------

    private void afficherEtat() {
        System.out.println("\n--- Page " + pageCourante.getNumP() + " ---");

        if (pageCourante.getEnigme() != null) {
            System.out.println("Énigme : " + pageCourante.getEnigme().getTextE());
            System.out.println("Temps de résolution estimé : " + pageCourante.getTempsResolution() + "s");
        } else {
            System.out.println("(Pas d'énigme sur cette page)");
        }

        if (pageCourante.contientObjet()) {
            Item obj = pageCourante.getObj();
            System.out.println("Vous avez récupéré un objet : " + obj.getNom());
            inventaire.add(obj);
        }
    }

    // ------------------------------------------------------------------
    // Fin de partie
    // ------------------------------------------------------------------

    private void terminerPartie() {
        System.out.println("\n=== FIN DE L'AVENTURE ===");
        System.out.println("Vous avez atteint la page finale : " + pageCourante.getNumP());
        System.out.println("Objets récoltés (" + inventaire.size() + ") :");
        if (inventaire.isEmpty()) {
            System.out.println("  (aucun)");
        } else {
            for (Item item : inventaire) {
                System.out.println("  - " + item.getNom());
            }
        }
    }
}
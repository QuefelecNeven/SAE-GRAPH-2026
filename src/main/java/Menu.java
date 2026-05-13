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
        int choix = -1;
        while (choix != 4) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("  1. Jouer (Mode Humain)");
            System.out.println("  2. Jouer (Mode Algorithme)");
            System.out.println("  3. Exporter le graphe en .dot");
            System.out.println("  4. Quitter");
            System.out.print("Votre choix : ");

            choix = -1;
            while (choix < 1 || choix > 4) {
                if (scanner.hasNextInt()) {
                    choix = scanner.nextInt();
                } else {
                    scanner.next();
                }
            }

            switch (choix) {
                case 1 -> jouerHumain();
                case 2 -> jouerIA();
                case 3 -> exporterGraphe(); // retour au menu après export
                case 4 -> System.out.println("Au revoir !");
            }
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
            System.out.println("Pour générer le PDF, lancez :");
            System.out.println("  dot -T pdf " + nomFichier + " -o graph.pdf");
        } catch (IOException e) {
            System.out.println("Erreur lors de l'export : " + e.getMessage());
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
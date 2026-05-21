import java.io.File;
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
        System.out.println("\n=== INITIALISATION DE L'AVENTURE ===");
        System.out.println("  1. Créer une nouvelle partie (Nouveau graphe)");
        System.out.println("  2. Charger un fichier de sauvegarde");
        System.out.print("Votre choix : ");
        
        int choixInit = -1;
        while (choixInit < 1 || choixInit > 2) {
            String entree = scanner.next();
            try {
                choixInit = Integer.parseInt(entree);
                if (choixInit < 1 || choixInit > 2) {
                    System.out.print("Choix invalide, saisissez 1 ou 2 : ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Veuillez entrer un nombre valide (1 ou 2) : ");
            }
        }

        if (choixInit == 1) {
            choisirTypeGraphe();
        } else {
            chargerPartieDossier();
        }

        int choix = -1;
        while (choix != 7) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("  1. Jouer (Mode Humain)");
            System.out.println("  2. Jouer (Mode Algorithme)");
            System.out.println("  3. Exporter le graphe en .dot / PDF");
            System.out.println("  4. Changer le type de graphe");
            System.out.println("  5. Sauvegarder la partie");
            System.out.println("  6. Charger une sauvegarde");
            System.out.println("  7. Quitter");
            System.out.print("Votre choix : ");

            choix = -1;
            while (choix < 1 || choix > 7) {
                String entree = scanner.next();
                try {
                    choix = Integer.parseInt(entree);
                } catch (NumberFormatException e) {
                    // Ignorer les entrées incorrectes et reboucler
                }
            }

            switch (choix) {
                case 1 -> jouerHumain();
                case 2 -> jouerIA();
                case 3 -> exporterGraphe();
                case 4 -> choisirTypeGraphe();
                case 5 -> sauvegarderPartieDossier();
                case 6 -> chargerPartieDossier();
                case 7 -> System.out.println("Au revoir !");
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
            String entree = scanner.next();
            try {
                choix = Integer.parseInt(entree);
            } catch (NumberFormatException e) {
                // Réessayer silencieusement
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

            int index = lireChoixOuSauvegarde(choix.size());
            if (index == -2) {
                // L'utilisateur a sauvegardé sa partie à la volée, on rafraîchit la page actuelle
                continue;
            }
            pageCourante = choix.get(index);
        }
        terminerPartie();
    }

    /** Lit le choix de destination ou intercepte la commande de sauvegarde instantanée. */
    private int lireChoixOuSauvegarde(int max) {
        while (true) {
            System.out.print("Votre choix (1-" + max + ") ou écrivez 'save' pour sauvegarder : ");
            String entree = scanner.next();
            
            if (entree.equalsIgnoreCase("save")) {
                sauvegarderPartieDossier();
                return -2; // Code de retour indiquant qu'aucune action de déplacement n'a été faite
            }
            
            try {
                int index = Integer.parseInt(entree) - 1;
                if (index >= 0 && index < max) {
                    return index;
                } else {
                    System.out.println("Choix invalide, réessayez.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée non reconnue. Saisissez un nombre ou tapez 'save'.");
            }
        }
    }

    // ------------------------------------------------------------------
    // Gestion des fichiers dans le dossier "save"
    // ------------------------------------------------------------------

    private void sauvegarderPartieDossier() {
        System.out.print("Entrez le nom personnalisé pour votre sauvegarde (sans extension) : ");
        String nomFichier = scanner.next();
        
        File dossier = new File("save");
        if (!dossier.exists()) {
            dossier.mkdir();
        }
        
        String chemin = "save/" + nomFichier + ".json";
        GestionSauvegarde.sauvegarder(chemin, livre, pageCourante, inventaire);
        System.out.println("✅ Partie enregistrée avec succès sous : " + chemin);
    }

    private void chargerPartieDossier() {
        System.out.print("Entrez le nom du fichier à charger depuis le dossier 'save' (sans extension) : ");
        String nomFichier = scanner.next();
        String chemin = "save/" + nomFichier + ".json";
        
        Sauvegarde save = GestionSauvegarde.charger(chemin);
        if (save != null) {
            this.livre.chargerDepuisSauvegarde(save);
            this.pageCourante = this.livre.getPageById(save.pageCouranteId);
            this.inventaire = save.inventaire;
            System.out.println("✅ Sauvegarde restaurée ! Vous reprenez à la page : " + pageCourante.getNumP());
        } else {
            System.out.println("❌ Impossible de trouver ou charger le fichier '" + chemin + "'.");
            if (this.pageCourante == null) {
                System.out.println("Initialisation forcée d'une nouvelle configuration...");
                choisirTypeGraphe();
                this.pageCourante = livre.getDebut();
            }
        }
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

        exporter.setVertexAttributeProvider(page -> {
            Map<String, Attribute> attrs = new LinkedHashMap<>();
            String label;
            if (page instanceof Debut) {
                label = "Début (Page " + page.getNumP() + ")";
            } else if (page instanceof Fin) {
                label = "Fin (Page " + page.getNumP() + ")";
            } else {
                label = "Page " + page.getNumP();
            }
            if (page.contientObjet()) {
                label += "\n[Objet : " + page.getObj().getNom() + "]";
            }
            attrs.put("label", DefaultAttribute.createAttribute(label));
            return attrs;
        });

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

        try {
            Process process = new ProcessBuilder("dot", "-T", "pdf", nomFichier, "-o", "graph.pdf")
                .inheritIO()
                .start();
            int code = process.waitFor();
            if (code == 0) {
                System.out.println("PDF généré : graph.pdf");
            } else {
                System.out.println("Erreur lors de la génération du PDF (code " + code + ").");
            }
        } catch (IOException e) {
            System.out.println("Impossible de lancer 'dot' : " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Génération interrompue.");
        }
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
            pageCourante.setObj(null); // Consomme l'objet pour éviter la duplication post-sauvegarde
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
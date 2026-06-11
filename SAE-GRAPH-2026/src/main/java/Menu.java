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

    public Menu() {
        this.inventaire = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void afficherMenuPrincipal() {
        System.out.println("\n INITIALISATION DE L'AVENTURE ");
        System.out.println("  1. Créer une nouvelle partie (Nouveau graphe)");
        System.out.println("  2. Charger un fichier de sauvegarde");
        System.out.print("Votre choix : ");

        int choixInit = -1;
        while (choixInit < 1 || choixInit > 2) {
            String entree = scanner.next();
            try {
                choixInit = Integer.parseInt(entree);
                if (choixInit < 1 || choixInit > 2) {
                    System.out.print("Choix invalide, saisissez une bonne réponse : ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Veuillez entrer un nombre valide (1 ou 2) : ");
            }
        }

        if (choixInit == 1) {
            creerNouvellePartie();
            choisirTypeGraphe();
        } else {
            chargerPartieDossier();
        }

        int choix = -1;
        while (choix != 7) {
            System.out.println("\n MENU PRINCIPAL ");
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

    private void creerNouvellePartie() {
        System.out.println("\n CONFIGURATION DU LIVRE ");
        System.out.print("Nombre de pages du livre (minimum 5) : ");
        int nbPages = 0;
        while (nbPages < 5) {
            String entree = scanner.next();
            try {
                nbPages = Integer.parseInt(entree);
                if (nbPages < 5) {
                    System.out.print("Minimum 5 pages, réessayez : ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Nombre invalide, réessayez : ");
            }
        }

        System.out.println("Combien d'objets voulez-vous placer dans le livre ?");
        int nbObjets = -1;
        while (nbObjets <= 0) {
            String entree = scanner.next();
            try {
                nbObjets = Integer.parseInt(entree);
                if (nbObjets < 0) {
                    System.out.print("Valeur invalide, réessayez : ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Nombre invalide, réessayez : ");
            }
        }

        List<Integer> pagesObjets = new ArrayList<>();
        if (nbObjets > 0) {
            System.out.println("Entrez les numéros de pages où placer les objets.");
            System.out.println("(Valeurs acceptées : entre 1 et " + (nbPages - 1) + ")");
            for (int i = 1; i <= nbObjets; i++) {
                int page = -1;
                while (page < 1 || page >= nbPages || pagesObjets.contains(page)) {
                    System.out.print("  Objet " + i + " - Page : ");
                    String entree = scanner.next();
                    try {
                        page = Integer.parseInt(entree);
                        if (page < 1 || page >= nbPages) {
                            System.out.println("  La page doit être entre 1 et " + (nbPages - 1) + ".");
                            page = -1;
                        } else if (pagesObjets.contains(page)) {
                            System.out.println("  Cette page contient déjà un objet, choisissez-en une autre.");
                            page = -1;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("  Nombre invalide.");
                    }
                }
                pagesObjets.add(page);
            }
        }

        long debut = System.nanoTime();
        this.livre = new Livre(nbPages, pagesObjets);
        long fin = System.nanoTime();
        double dureeMs = (fin - debut) / 1_000_000.0;

        this.pageCourante = livre.getDebut();
        this.inventaire = new ArrayList<>();

        System.out.println("Livre créé avec " + nbPages + " pages et " + pagesObjets.size() + " objet(s) : " + pagesObjets);
        System.out.println("Durée de création du graphe : " + dureeMs + " ms");
    }

    private void choisirTypeGraphe() {
        System.out.println("\n TYPE DE GRAPHE");
        System.out.println("  1. Graphe Simple (chemin garanti passant par les objets)");
        System.out.println("  2. Graphe Aléatoire (avec garantie d'atteignabilité)");
        System.out.print("Votre choix : ");

        int choix = -1;
        while (choix < 1 || choix > 2) {
            String entree = scanner.next();
            try {
                choix = Integer.parseInt(entree);
            } catch (NumberFormatException e) {
            }
        }

        if (choix == 1) {
            long debut = System.nanoTime();
            livre.utiliserGrapheSimple();
            long fin = System.nanoTime();
            System.out.println("Graphe Simple sélectionné.");
            System.out.println("Durée de création du graphe : " + (fin - debut) / 1_000_000.0 + " ms");
        } else {
            long debut = System.nanoTime();
            livre.utiliserGrapheAleatoire();
            long fin = System.nanoTime();
            System.out.println("Graphe Aléatoire sélectionné.");
            System.out.println("Durée de création du graphe : " + (fin - debut) / 1_000_000.0 + " ms");
        }
    }

    public void jouerHumain() {
        System.out.println("\n DÉBUT DE L'AVENTURE (Mode Humain)");
        while (!(pageCourante instanceof Fin)) {
            afficherEtat();

            List<Page> choix = livre.getPagesSuivantes(pageCourante);

            if (choix.isEmpty()) {
                System.out.println("Aucune destination disponible. Fin forcée.");
                break;
            }

            System.out.println("\nDestinations possibles :");
            for (int i = 0; i < choix.size(); i++) {
                Page p = choix.get(i);
                String label = "Page " + p.getNumP();
                if (p instanceof Fin)   label = "Fin (Page " + p.getNumP() + ")";
                if (p instanceof Debut) label = "Début (Page " + p.getNumP() + ")";
                System.out.println("  " + (i + 1) + ". " + label);
            }

            int index = lireChoixOuSauvegarde(choix.size());
            if (index == -2) {
                continue;
            }
            pageCourante = choix.get(index);
        }
        terminerPartie();
    }

    private int lireChoixOuSauvegarde(int max) {
        while (true) {
            System.out.print("Votre choix (1-" + max + ") ou écrivez 'save' pour sauvegarder : ");
            String entree = scanner.next();

            if (entree.equalsIgnoreCase("save")) {
                sauvegarderPartieDossier();
                return -2;
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

    private void sauvegarderPartieDossier() {
        System.out.print("Entrez le nom personnalisé pour votre sauvegarde : ");
        String nomFichier = scanner.next();

        File dossier = new File("save");
        if (!dossier.exists()) {
            dossier.mkdir();
        }

        String chemin = "save/" + nomFichier + ".json";
        GestionSauvegarde.sauvegarder(chemin, livre, pageCourante, inventaire);
        System.out.println("Partie enregistrée avec succès sous : " + chemin);
    }

    private void chargerPartieDossier() {
        System.out.print("Entrez le nom du fichier à charger depuis le dossier 'save' (sans extension) : ");
        String nomFichier = scanner.next();
        String chemin = "save/" + nomFichier + ".json";

        Sauvegarde save = GestionSauvegarde.charger(chemin);
        if (save != null) {
            if (this.livre == null) {
                this.livre = new Livre(save.nbPages, new ArrayList<>());
            }
            this.livre.chargerDepuisSauvegarde(save);
            this.pageCourante = this.livre.getPageById(save.pageCouranteId);
            this.inventaire = save.inventaire != null ? save.inventaire : new ArrayList<>();
            System.out.println("Sauvegarde restaurée ! Vous reprenez à la page : " + pageCourante.getNumP());
        } else {
            System.out.println("Impossible de trouver ou charger le fichier '" + chemin + "'.");
            if (this.livre == null) {
                System.out.println("Initialisation forcée d'une nouvelle configuration...");
                creerNouvellePartie();
                choisirTypeGraphe();
            }
        }
    }

    public void jouerIA() {
        System.out.println("\n MODE ALGORITHME");
        System.out.println("  1. Dijkstra (chemin le plus rapide en temps, collecte tous les objets)");
        System.out.println("  2. BFS (chemin le plus court en nombre de pages jusqu'à la Fin)");
        System.out.print("Votre choix : ");

        int choix = -1;
        while (choix < 1 || choix > 2) {
            String entree = scanner.next();
            try {
                choix = Integer.parseInt(entree);
            } catch (NumberFormatException e) {
            }
        }

        Algorithme algo = new Algorithme(livre);

        if (choix == 1) {
            System.out.println("\n>> Lancement de Dijkstra...");
            long debut = System.nanoTime();
            List<Page> chemin = algo.executer();
            long fin = System.nanoTime();
            double dureeMs = (fin - debut) / 1_000_000.0;

            if (chemin.isEmpty()) {
                System.out.println("Aucun chemin trouvé collectant tous les objets jusqu'à la Fin.");
            } else {
                System.out.println("Chemin trouvé (" + chemin.size() + " pages) :");
                for (Page p : chemin) {
                    System.out.print("Page " + p.getNumP() + " ");
                }
                System.out.println();
                System.out.println("Temps total du chemin  : " + algo.getMeilleurTemps() + "s");
                System.out.println("Durée d'exécution      : " + dureeMs + " ms");
                System.out.println("Nombre de pages visitées pendant la recherche : " + algo.getNbPagesVisiteesDijkstra());
            }
        } else {
            System.out.println("\n>> Lancement du BFS...");
            long debut = System.nanoTime();
            List<Page> chemin = algo.executerBFS();
            long fin = System.nanoTime();
            double dureeMs = (fin - debut) / 1_000_000.0;

            if (chemin.isEmpty()) {
                System.out.println("Aucun chemin trouvé jusqu'à la page Fin.");
            } else {
                System.out.println("Chemin trouvé (" + chemin.size() + " pages) :");
                for (Page p : chemin) {
                    System.out.print("Page " + p.getNumP() + " ");
                }
                System.out.println();
                System.out.println("Temps total du chemin  : " + algo.getTempsBFS() + "s");
                System.out.println("Nombre de pages visitées pendant la recherche : " + algo.getNbPagesVisitees());
                System.out.println("Durée d'exécution      : " + dureeMs + " ms");
            }
        }
    }

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
                System.out.println("Vérifiez que graphviz est installé : sudo apt install graphviz");
            }
        } catch (IOException e) {
            System.out.println("Impossible de lancer 'dot' : " + e.getMessage());
            System.out.println("Installez graphviz : sudo apt install graphviz");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Génération interrompue.");
        }
    }

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
            pageCourante.setObj(null);
        }
    }

    private void terminerPartie() {
        System.out.println("\n FIN DE L'AVENTURE");
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
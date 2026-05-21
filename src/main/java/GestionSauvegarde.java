import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File; // <-- N'oublie pas cet import
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestionSauvegarde {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void sauvegarder(String fichier, Livre livre, Page pageCourante, List<Item> inventaire) {
        Sauvegarde save = new Sauvegarde();
        save.pageCouranteId = pageCourante.getNumP();
        save.inventaire = inventaire;
        save.nbPages = livre.getFin().getNumP(); 
        save.pages = new ArrayList<>();
        save.arcs = new ArrayList<>();

        // 1. Récupération de tous les sommets du graphe
        for (Page p : livre.getGraphe().vertexSet()) {
            Sauvegarde.PageData pd = new Sauvegarde.PageData();
            pd.id = p.getNumP();
            
            if (p instanceof Debut) pd.type = "Debut";
            else if (p instanceof Fin) pd.type = "Fin";
            else pd.type = "Normale";

            if (p.getEnigme() != null) {
                pd.textEnigme = p.getEnigme().getTextE();
                pd.tempResolution = p.getEnigme().getTempRes();
            }
            if (p.contientObjet()) {
                pd.obj = p.getObj();
            }
            save.pages.add(pd);
        }

        // 2. Récupération de tous les arcs et de leurs poids
        for (var arc : livre.getGraphe().edgeSet()) {
            Sauvegarde.ArcData ad = new Sauvegarde.ArcData();
            ad.sourceId = livre.getGraphe().getEdgeSource(arc).getNumP();
            ad.targetId = livre.getGraphe().getEdgeTarget(arc).getNumP();
            ad.poids = livre.getGraphe().getEdgeWeight(arc);
            save.arcs.add(ad);
        }

        // 🔥 SÉCURITÉ EN PLUS : Création automatique du dossier "save" s'il manque
        File file = new File(fichier);
        File dossierParent = file.getParentFile();
        if (dossierParent != null && !dossierParent.exists()) {
            dossierParent.mkdirs(); // Crée le dossier 'save/' automatiquement à la racine du projet
        }

        // Écriture du fichier JSON
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(save, writer);
        } catch (IOException e) {
            System.out.println("Erreur de sauvegarde : " + e.getMessage());
        }
    }

    public static Sauvegarde charger(String fichier) {
        try (FileReader reader = new FileReader(fichier)) {
            return GSON.fromJson(reader, Sauvegarde.class);
        } catch (IOException e) {
            System.out.println("Fichier introuvable : " + e.getMessage());
            return null;
        }
    }
}
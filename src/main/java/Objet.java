public class Objet {
    private String nom;
    private Page posO;

    public Objet(String nom, Page page) {
        this.nom = nom;
        this.posO = page;
    }

    public String getNom() {
        return nom;
    }

    public Page getPosO() {
        return posO;
    }
}
public class Page {
    protected int numP;
    private Enigmes enigme;
    private Objet obj;

    protected Page(){
    }

    public Page(Enigmes enigme, int num){
        this.enigme = enigme;
        this.numP = num;
    }

    public Page(Enigmes enigme, int num,Objet obj){
        this.obj = obj;
        this.enigme = enigme;
        this.numP = num;
    }

    public Enigmes getEnigme() {
        return enigme;
    }

    public int getNumP() {
        return numP;
    }

    public Objet getObj() {
        return obj;
    }

}

package fr.iut.sae;

public class Page {
    protected int numP;
    private Enigme enigme;
    private Objet obj;

    protected Page(){
    }

    public Page(Enigme enigme, int num){
        this.enigme = enigme;
        this.numP = num;
    }

    public Page(Enigme enigme, int num,Objet obj){
        this.obj = obj;
        this.enigme = enigme;
        this.numP = num;
    }

    public Page(int num){
        this.numP = num;
    }

    public Enigme getEnigme() {
        return enigme;
    }

    public int getNumP() {
        return numP;
    }

    public Objet getObj() {
        return obj;
    }

}

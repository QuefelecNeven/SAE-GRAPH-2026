public class Page {
    protected int numP;
    private Enigme enigme;
    private Item obj;

    protected Page(){
    }

    public Page(Enigme enigme, int num){
        this.enigme = new Enigme();
        this.numP = num;
    }

    public Page(Enigme enigme, int num, Item obj){
        this.obj = obj;
        this.enigme = new Enigme();
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

    public Item getObj() {
        return obj;
    }

    public void setObj(Item obj) {
        this.obj = obj;
<<<<<<< HEAD
    }

    public boolean contientObjet(){
        return obj != null;
=======
>>>>>>> d2714cb (Fin)
    }

    public boolean contientObjet(){
        return obj != null;
    }

    public int getTempsResolution(){
        if(this.enigme == null) return 0;
        return this.enigme.getTempRes();
    }
<<<<<<< HEAD
    
    public void setEnigme(Enigme enigme) {
        this.enigme = enigme;
    }
=======
>>>>>>> d2714cb (Fin)
}
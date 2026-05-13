public class Executable {
    public static void main(String[] args) {
<<<<<<< HEAD
        Menu menu = new Menu();
=======
        List<Integer> pagesAvecObjets = Arrays.asList(2, 5, 8);
        Livre livre = new Livre(10, pagesAvecObjets);
        Menu menu = new Menu(livre);
>>>>>>> d2714cb (Fin)
        menu.afficherMenuPrincipal();
    }
}
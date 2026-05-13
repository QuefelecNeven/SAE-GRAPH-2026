import java.util.Arrays;
import java.util.List;

public class Executable {
    public static void main(String[] args) {
        List<Integer> pagesAvecObjets = Arrays.asList(2, 5, 8);
        Livre livre = new Livre(10, pagesAvecObjets);
        Menu menu = new Menu(livre);
        menu.afficherMenuPrincipal();
    }
}
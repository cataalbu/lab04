import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        FA fa = null;
        try {
            fa = new FA();
            fa.displayMenu();

        } catch (NonDeterministicAutomatonException e) {
            System.out.println(e.getMessage());
        }
    }
}

//delta={(r 0 s),(r 0 v),(r 1 v),(s 0 r),(s 1 v),(v 0 s),(v 1 r)}
//delta={(r 0 s),(r 1 v),(s 0 r),(s 1 v),(v 0 s),(v 1 r)}

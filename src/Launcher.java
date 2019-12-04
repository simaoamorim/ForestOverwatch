import javax.swing.*;
import java.io.IOException;

public class Launcher implements Runnable {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Launcher());
    }

    @Override
    public void run() {
        try {
            new GUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

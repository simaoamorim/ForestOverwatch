package Application;

import javax.swing.*;

public class Launcher implements Runnable {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Launcher());
    }

    @Override
    public void run() {
        new GUI();
    }
}

package ForestOverWatch;

import javax.swing.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Launcher implements Runnable {
    private Logger logger = Logger.getLogger(getClass().getPackageName());
    private ConsoleHandler consoleHandler = new ConsoleHandler();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Launcher());
    }

    @Override
    public void run() {
        consoleHandler.setLevel(Level.FINE);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.FINE);
        logger.addHandler(consoleHandler);
        new Settings(logger);
        logger.fine("Settings window created");
    }
}

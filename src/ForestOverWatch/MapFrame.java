package ForestOverWatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import java.util.logging.Logger;

public class MapFrame extends JFrame {
    private static final Dimension frameSize = new Dimension(700,600);
    private Integer XCount;
    private Integer YCount;
    private Integer CellSize;
    private MapGrid mapGrid;
    private Properties localProperties;
    private Settings settingsWindow;
    private Logger logger;


    public MapFrame(Properties properties, Settings settings, Logger logger) {
        localProperties = properties;
        settingsWindow = settings;
        XCount = Integer.parseInt(localProperties.getProperty("XCount"));
        YCount = Integer.parseInt(localProperties.getProperty("YCount"));
        CellSize = Integer.parseInt(localProperties.getProperty("cellSize"));
        this.logger = logger;
        mapGrid = new MapGrid(XCount, YCount, CellSize, this.logger);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                settings.mapFrameClosed();
            }
        });
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocation(settings.getX()+settings.getWidth()+50, settings.getY()+50);
        setTitle("Forest Over-Watch - Map");
        setPreferredSize(frameSize);
        setMinimumSize(frameSize);
        setVisible(true);
    }
}

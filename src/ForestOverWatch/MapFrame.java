package ForestOverWatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import java.util.logging.Logger;

public class MapFrame extends BaseGridFrame {
    private Integer XCount;
    private Integer YCount;
    private Integer CellSize;
    private MapGrid mapGrid;
    private Properties localProperties;
    private Settings settingsWindow;
    private Logger logger;
    private TerrainPoint[][] terrainPoints;


    public MapFrame(Properties properties, Settings settings, Logger logger, TerrainPoint[][] terrainPoints) {
        super();
        localProperties = properties;
        settingsWindow = settings;
        XCount = Integer.parseInt(localProperties.getProperty("XCount"));
        YCount = Integer.parseInt(localProperties.getProperty("YCount"));
        CellSize = Integer.parseInt(localProperties.getProperty("cellSize"));
        this.logger = logger;
        this.terrainPoints = terrainPoints;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                settings.mapFrameClosed();
            }
        });
        setLocation(settings.getX()+settings.getWidth()+50, settings.getY()+50);
        setTitle("Forest Over-Watch - Map");
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        mapGrid = new MapGrid(Integer.parseInt(localProperties.getProperty("XCount")),
                Integer.parseInt(localProperties.getProperty("YCount")),
                Integer.parseInt(localProperties.getProperty("cellSize")),
                logger, localProperties, terrainPoints);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(mapGrid);
        JScrollPane mainPane = new JScrollPane(panel);
        add(mainPane);
    }

    void timerHandler() {
        mapGrid.iteration();
    }
}

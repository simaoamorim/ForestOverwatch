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
    private Integer cellSize;
    private MapGrid mapGrid;
    private Properties localProperties;
    private Settings settingsWindow;
    private Logger logger;
    private TerrainPoint[][] terrainPoints;
    private Integer[] droneCount = new Integer[3];


    public MapFrame(Properties properties, Settings settings, Logger logger, TerrainPoint[][] terrainPoints) {
        super();
        localProperties = properties;
        settingsWindow = settings;
        XCount = Integer.parseInt(localProperties.getProperty("XCount"));
        YCount = Integer.parseInt(localProperties.getProperty("YCount"));
        cellSize = Integer.parseInt(localProperties.getProperty("cellSize"));
        droneCount[0] = Integer.parseInt(localProperties.getProperty("DroneCount1"));
        droneCount[1] = Integer.parseInt(localProperties.getProperty("DroneCount2"));
        droneCount[2] = Integer.parseInt(localProperties.getProperty("DroneCount3"));
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

    void initUI() {
        setLayout(new BorderLayout());
        mapGrid = new MapGrid(logger, localProperties, terrainPoints);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(mapGrid);
        JScrollPane mainPane = new JScrollPane(panel);
        add(mainPane, BorderLayout.CENTER);
    }

    void timerHandler() {
        mapGrid.iteration();
    }

    void setCellSize(int cellSize) {
        mapGrid.setCellSize(cellSize);
    }

    @Override
    public void repaint() {
        super.repaint();
        mapGrid.repaint();
    }

    @Override
    public void revalidate() {
        super.revalidate();
        mapGrid.revalidate();
    }
}

package ForestOverWatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class TerrainFrame extends BaseGridFrame {
    private TerrainGrid terrainGrid;
    private Timer iterationTimer = new Timer(timeStep, this::timerHandler);
    private static final int timeStep = 12; // Time in ms (1000/80 = 12.5)
    public static Integer[] Sizes = TerrainGrid.Sizes;
    private Properties localProperties;
    private Logger logger;
    private Integer iterationCounter;

    public TerrainFrame(Properties localProperties, Settings settings, Logger logger) {
        super();
        this.localProperties = localProperties;
        this.logger = logger;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                settings.terrainFrameClosed();
            }
        });
        setTitle("Forest Over-Watch - Terrain");
        setLocation(settings.getX()+settings.getWidth(), settings.getY());
        initUI();
        pack();
        iterationTimer.stop();
        iterationCounter = 0;
    }

    private void initUI() {
        setLayout(new BorderLayout());
        terrainGrid = new TerrainGrid(Integer.parseInt(localProperties.getProperty("XCount")),
                Integer.parseInt(localProperties.getProperty("YCount")),
                Integer.parseInt(localProperties.getProperty("cellSize")),
                logger);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(terrainGrid);
        JScrollPane mainPane = new JScrollPane(panel);
        add(mainPane);
    }

    private void timerHandler(ActionEvent e) {
        terrainGrid.iteration();
        iterationCounter++;
        setTitle("Forest Over-Watch - Terrain (Iteration "+iterationCounter.toString()+")");
    }

    public boolean isRunning() { return iterationTimer.isRunning(); }

    public void startIteration() { iterationTimer.start(); }

    public void stopIteration() { iterationTimer.stop(); }

    public void setCellSize(int size) { terrainGrid.setCellSize(size); }

    void randomizeFire() {
        terrainGrid.randomizeFire();
        repaint();
        revalidate();
    }

    void randomizeTerrain() {
        terrainGrid.randomizeTerrain();
        repaint();
        revalidate();
    }

    void saveTerrain(String path) throws IOException {
        terrainGrid.saveTerrain(path);
    }

    void loadTerrain(String path) throws IOException {
        terrainGrid.loadTerrain(path);
    }

}

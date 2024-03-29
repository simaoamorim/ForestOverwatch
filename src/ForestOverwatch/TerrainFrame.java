package ForestOverwatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class TerrainFrame extends BaseGridFrame {
    private TerrainGrid terrainGrid;
    public static final Integer[] Sizes = TerrainGrid.Sizes;
    private final Properties localProperties;
    private final Logger logger;
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

    void timerHandler() {
        terrainGrid.iteration();
        iterationCounter++;
        int minutes = iterationCounter / 4800; // = (iterationCounter / 80) / 60
        int seconds = (iterationCounter / 80) % 60;
        setTitle(String.format("Forest Over-Watch - Terrain (Iteration %d, %dm%ds)", iterationCounter, minutes, seconds));
    }

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

    TerrainPoint[][] getTerrainPoints() {
        return terrainGrid.terrainPoints;
    }

    @Override
    public void repaint() {
        super.repaint();
        terrainGrid.repaint();
    }

    @Override
    public void revalidate() {
        super.revalidate();
        terrainGrid.revalidate();
    }

}

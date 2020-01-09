package ForestOverWatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;

public class TerrainFrame extends JFrame {
    private static final Dimension frameSize = new Dimension(700,600);
    private static final Dimension maxFrameSize = Toolkit.getDefaultToolkit().getScreenSize();
    private TerrainGrid terrainGrid;
    public Timer iterationTimer = new Timer(timeStep, this::timerHandler);
    private static final int timeStep = 12; // Time in ms (1000/80 = 12.5)
    public static Integer[] Sizes = TerrainGrid.Sizes;
    private Properties localProperties;
    private Settings settings;
    private Logger logger;
    private Integer iterationCounter;

    public TerrainFrame(Properties localProperties, Settings settings, Logger logger) {
        this.localProperties = localProperties;
        this.settings = settings;
        this.logger = logger;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                settings.terrainFrameClosed();
            }
        });
        setTitle("Forest Over-Watch - Terrain");
        setPreferredSize(frameSize);
        setMinimumSize(frameSize);
        setMaximumSize(maxFrameSize);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocation(settings.getX()+settings.getWidth(), settings.getY());
        initUI();
        pack();
        setVisible(true);
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

    public void reset() { terrainGrid.reset(); }

    public void setXCount(int count) { terrainGrid.setXCount(count); }

    public void setYCount(int count) { terrainGrid.setYCount(count); }

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

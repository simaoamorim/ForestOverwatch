package Terrain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TerrainFrame extends JFrame {
    private static final Dimension frameSize = new Dimension(700,600);
    private static final Dimension maxFrameSize = Toolkit.getDefaultToolkit().getScreenSize();
    private TerrainGrid terrainGrid;
    public Timer iterationTimer = new Timer(timeStep, this::timerHandler);
    private static final int timeStep = 12; // Time in ms (1000/80 = 12.5)
    public static Integer[] Sizes = TerrainGrid.Sizes;

    public TerrainFrame() {
        setTitle("Forest Over-Watch - Terrain");
        setPreferredSize(frameSize);
        setMinimumSize(frameSize);
        setMaximumSize(maxFrameSize);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        initUI();
        pack();
        setVisible(true);
        iterationTimer.stop();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        terrainGrid = new TerrainGrid();
        terrainGrid.initialize();
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(terrainGrid);
        JScrollPane mainPane = new JScrollPane(panel);
        add(mainPane);
    }

    private void timerHandler(ActionEvent e) {
        terrainGrid.iteration();
    }

    public boolean isRunning() { return iterationTimer.isRunning(); }

    public void startIteration() { iterationTimer.start(); }

    public void stopIteration() { iterationTimer.stop(); }

    public void setCellSize(int size) { terrainGrid.setCellSize(size); }

    public void reset() { terrainGrid.reset(); }

    public void setXCount(int count) { terrainGrid.setXCount(count); }

    public void setYCount(int count) { terrainGrid.setYCount(count); }

}

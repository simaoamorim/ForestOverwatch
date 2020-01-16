package forestOverWatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

public class MapFrame extends BaseGridFrame {
    private MapGrid mapGrid;
    private final Properties localProperties;
    private final TerrainPoint[][] terrainPoints;


    public MapFrame(Properties properties, Settings settings, TerrainPoint[][] terrainPoints) {
        super();
        localProperties = properties;
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
        mapGrid = new MapGrid(localProperties, terrainPoints);
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

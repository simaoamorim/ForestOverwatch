package ForestOverWatch;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

public class MapGrid extends JComponent {
    private int cellSize;
    private static final int margin = 1;
    private int XCount;
    private int YCount;
    private MapPoint[][] mapPoints;
    private Logger logger;
    private Drone drones[];
    private Properties localProperties;
    private TerrainPoint[][] terrainPoints;

    MapGrid(int XCount, int YCount, int cellSIze, Logger logger, Properties properties, TerrainPoint[][] terrainPoints) {
        this.XCount = XCount;
        this.YCount = YCount;
        this.cellSize = cellSIze;
        this.logger = logger;
        localProperties = properties;
        this.terrainPoints = terrainPoints;
        this.setPreferredSize(
                new Dimension(
                        (XCount *cellSize)+(2*margin),
                        (YCount *cellSize)+(2*margin)
                )
        );
        initialize();
        drones = new Drone[3];
        drones[0] = new Drone(mapPoints, localProperties, 1, terrainPoints);
        drones[1] = new Drone(mapPoints, localProperties, 2, terrainPoints);
        drones[2] = new Drone(mapPoints, localProperties, 3, terrainPoints);
        drones[0].randomPlacement();
        drones[1].randomPlacement();
        drones[2].randomPlacement();
    }

    void initialize() {
        mapPoints = new MapPoint[XCount][YCount];
        for (int x = 0; x < XCount; x++)
            for (int y = 0; y < YCount; y++)
                mapPoints[x][y] = new MapPoint(x,y);
        setNeighbourhood();
    }

    private void setNeighbourhood() {
        //      Add neighbors, ignoring borders (Moore)
        for (int x = 0; x < XCount; x++) {
            for (int y = 0; y < YCount; y++) {
                if (y > 0)
                    mapPoints[x][y].addNeighbour(mapPoints[x][y - 1]); //Von Neumann neighborhood
                if (y < YCount - 1)
                    mapPoints[x][y].addNeighbour(mapPoints[x][y + 1]); //Von Neumann neighborhood
                if (x > 0) {
                    mapPoints[x][y].addNeighbour(mapPoints[x - 1][y]); //Von Neumann neighborhood
                    if (y > 0)
                        mapPoints[x][y].addNeighbour(mapPoints[x - 1][y - 1]);
                    if (y < YCount - 1)
                        mapPoints[x][y].addNeighbour(mapPoints[x - 1][y + 1]);
                }
                if (x < XCount - 1) {
                    mapPoints[x][y].addNeighbour(mapPoints[x + 1][y]); //Von Neumann neighborhood
                    if (y > 0)
                        mapPoints[x][y].addNeighbour(mapPoints[x + 1][y - 1]);
                    if (y < YCount - 1)
                        mapPoints[x][y].addNeighbour(mapPoints[x + 1][y + 1]);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapPoints != null) {
            for (int x = 0; x < XCount; x++) {
                for (int y = 0; y < YCount; y++) {
                    int finalX = x;
                    int finalY = y;
                    if (Arrays.stream(drones).anyMatch(drones -> drones.getActualPosition().equals(mapPoints[finalX][finalY]))) {
                        g.setColor(new Color(0xB641B2));
                    } else {
                        if (mapPoints[x][y].isScanned()) {
                            switch (mapPoints[x][y].getType()) {
                                case GROUND:
                                    g.setColor(new Color(0x7B4716));
                                    break;
                                case TREE:
                                    g.setColor(new Color(0x00AA00));
                                    break;
                                case WATER:
                                    g.setColor(new Color(0x0066FF));
                                    break;
                                case FIRE:
                                    g.setColor(new Color(0xFF0000));
                                    break;
                                default:
                                    g.setColor(new Color(0x555555));
                            }
                        } else {
                            g.setColor(new Color(0x555555));
                        }
                        g.fillRect(margin + (x * cellSize), margin + (y * cellSize), cellSize, cellSize);
                    }
                }
            }
        }
        int _width = (XCount * cellSize);
        int _height = (YCount * cellSize);
        this.setPreferredSize(new Dimension( (_width+(2*margin)), (_height+(2*margin))));
    }

    void iteration() {
        for (Drone drone : drones) {
            drone.scan();
            drone.move();
        }
        repaint();
        revalidate();
    }
}

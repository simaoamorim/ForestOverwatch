package forestOverWatch;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class MapGrid extends JComponent {
    private int cellSize;
    private static final int margin = 1;
    private final int XCount;
    private final int YCount;
    private MapPoint[][] mapPoints;
    private Drone[] drones;
    private final Properties localProperties;
    private final TerrainPoint[][] terrainPoints;
    private final int totalDroneCount;
    private boolean ready = false;

    MapGrid(Properties properties, TerrainPoint[][] terrainPoints) {
        localProperties = properties;
        this.terrainPoints = terrainPoints;
        XCount = Integer.parseInt(localProperties.getProperty("XCount"));
        YCount = Integer.parseInt(localProperties.getProperty("YCount"));
        cellSize = Integer.parseInt(localProperties.getProperty("cellSize"));
        totalDroneCount = Integer.parseInt(localProperties.getProperty("DroneCount1")) +
                Integer.parseInt(localProperties.getProperty("DroneCount2")) +
                Integer.parseInt(localProperties.getProperty("DroneCount3"));
        this.setPreferredSize(
                new Dimension(
                        (XCount *cellSize)+(2*margin),
                        (YCount *cellSize)+(2*margin)
                )
        );
        initialize();
        new MapSettings(localProperties, this);
    }

    void initialize() {
        mapPoints = new MapPoint[XCount][YCount];
        for (int x = 0; x < XCount; x++)
            for (int y = 0; y < YCount; y++)
                mapPoints[x][y] = new MapPoint(x,y);
        setNeighbourhood();
        drones = new Drone[totalDroneCount];
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

    void calcStaticField(){
        ready = true;
        ArrayList<MapPoint> toCheck = new ArrayList<>();
        for (Drone drone : drones) {
            toCheck.addAll(drone.getActualPosition().getNeighboursMap());
            drone.getActualPosition().setScanned(true);
        }
        while(!toCheck.isEmpty()){
            if(!toCheck.get(0).isScanned()){
                toCheck.get(0).calculateField();
                for(int x = 0; x < toCheck.get(0).getNeighboursMap().size(); x++){
                    if(!toCheck.get(0).getNeighbourByIndexMap(x).isScanned())
                        toCheck.add(toCheck.get(0).getNeighbourByIndexMap(x));
                }
            }
            toCheck.remove(toCheck.get(0));
        }
        for (int x = 0; x < XCount; x++)
            for (int y = 0; y < YCount; y++)
                mapPoints[x][y].setScanned(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapPoints != null) {
            for (int x = 0; x < XCount; x++) {
                for (int y = 0; y < YCount; y++) {
                    if (ready && pointHasDrone(mapPoints[x][y])) {
                        g.setColor(new Color(0xE6E600));
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
                    }
                    g.fillRect(margin + (x * cellSize), margin + (y * cellSize), cellSize, cellSize);
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
        for(int x=0; x<XCount; x++) {
            for (int y=0; y<YCount; y++) {
                mapPoints[x][y].calcStaticField();
            }

        }
        repaint();
        revalidate();
    }

    void addDrone(int index, int type) {
        drones[index] = new Drone(mapPoints, this, localProperties, type, terrainPoints);
        drones[index].randomPlacement();
    }

    void addDrone(int index, int type, int X, int Y) {
        drones[index] = new Drone(mapPoints, this, localProperties, type, terrainPoints);
        drones[index].placeAt(X, Y);
    }

    void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    boolean pointHasDrone(MapPoint point) {
        return Arrays.stream(drones).anyMatch(drones -> drones.getActualPosition().equals(point));
    }

}

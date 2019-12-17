package ForestOverWatch;

import javax.swing.*;
import java.awt.*;

class TerrainGrid extends JComponent {
    private int cellSize = 10;
    private static final int margin = 1;
    public static Integer[] Sizes = {50,100,200,500,1000};
    private int XCount;
    private int YCount;
    private boolean firstIteration = true;
    private TerrainPoint[][] terrainPoints;

    TerrainGrid(int XCount, int YCount) {
        this.XCount = XCount;
        this.YCount = YCount;
        setFocusable(true);
        this.setPreferredSize(
                new Dimension(
                        (XCount *cellSize)+(2*margin),
                        (YCount *cellSize)+(2*margin)
                )
        );
    }

    void initialize() {
        terrainPoints = new TerrainPoint[XCount][YCount];
        for (int x = 0; x < XCount; x++)
            for (int y = 0; y < YCount; y++)
                terrainPoints[x][y] = new TerrainPoint();

//      Add neighbors, ignoring borders (Moore)
        for (int x = 0; x < XCount; x++) {
            for (int y = 0; y < YCount; y++) {
                if (y > 0)
                    terrainPoints[x][y].addNeighbour(terrainPoints[x][y - 1]);
                if (y < YCount - 1)
                    terrainPoints[x][y].addNeighbour(terrainPoints[x][y + 1]);
                if (x > 0) {
                    terrainPoints[x][y].addNeighbour(terrainPoints[x - 1][y]);
                    if (y > 0)
                        terrainPoints[x][y].addNeighbour(terrainPoints[x - 1][y - 1]);
                    if (y < YCount - 1)
                        terrainPoints[x][y].addNeighbour(terrainPoints[x - 1][y + 1]);
                }
                if (x < XCount - 1) {
                    terrainPoints[x][y].addNeighbour(terrainPoints[x + 1][y]);
                    if (y > 0)
                        terrainPoints[x][y].addNeighbour(terrainPoints[x + 1][y - 1]);
                    if (y < YCount - 1)
                        terrainPoints[x][y].addNeighbour(terrainPoints[x + 1][y + 1]);
                }
            }
        }
        randomizeTerrain();
    }

    private void randomizeTerrain() {
        for (int x = 0; x < XCount; x++) {
            for (int y = 0; y < YCount; y++) {
                terrainPoints[x][y].setType(TerrainPoint.Types.getRandom());
            }
        }
        System.out.printf("water %d tree %d ground %d\n", countTerrain(TerrainPoint.Types.WATER), countTerrain(TerrainPoint.Types.TREE), countTerrain(TerrainPoint.Types.GROUND));
        softTerrain();
    }

    private void softTerrain(){
        for (int x = 0; x < XCount; x++) {
            for (int y = 0; y < YCount; y++) {
                int water = 0, tree = 0, ground = 0;
                for(int z = 0; z < terrainPoints[x][y].neighbours.size(); ++z) {
                    switch (terrainPoints[x][y].neighbours.get(z).getType()) {
                        case GROUND: ground++; break;
                        case TREE: tree++; break;
                        case WATER: water++; break;
                    }
                }
                if((water > Math.max(tree,ground))){
                    terrainPoints[x][y].setType(TerrainPoint.Types.WATER);
                } else if((tree > Math.max(water,ground))){
                    terrainPoints[x][y].setType(TerrainPoint.Types.TREE);
                } else if((ground > Math.max(water,tree)) && (countTerrain(TerrainPoint.Types.GROUND) < XCount*YCount/2)){
                    terrainPoints[x][y].setType(TerrainPoint.Types.GROUND);
                }
            }
        }

        for (int x = 0; x < XCount; x++) {
            for (int y = 0; y < YCount; y++) {
                int water = 0, tree = 0, ground = 0;
                for(int z = 0; z < terrainPoints[x][y].neighbours.size(); ++z) {
                    switch (terrainPoints[x][y].neighbours.get(z).getType()) {
                        case GROUND: ground++; break;
                        case TREE: tree++; break;
                        case WATER: water++; break;
                    }
                }
                if((water < 3) && (terrainPoints[x][y].getType() == TerrainPoint.Types.WATER)){
                    if(ground >= tree){
                        terrainPoints[x][y].setType(TerrainPoint.Types.GROUND);
                    } else{
                        terrainPoints[x][y].setType(TerrainPoint.Types.TREE);
                    }
                } else if((tree < 3) && (terrainPoints[x][y].getType() == TerrainPoint.Types.TREE)){
                    if(ground >= water){
                        terrainPoints[x][y].setType(TerrainPoint.Types.GROUND);
                    } else{
                        terrainPoints[x][y].setType(TerrainPoint.Types.WATER);
                    }
                } else if((ground < 3) && (terrainPoints[x][y].getType() == TerrainPoint.Types.GROUND)){
                    if(tree >= water){
                        terrainPoints[x][y].setType(TerrainPoint.Types.TREE);
                    } else{
                        terrainPoints[x][y].setType(TerrainPoint.Types.WATER);
                    }
                }
            }
        }
        for (int x = 0; x < XCount; x++) {
            for (int y = 0; y < YCount; y++) {
                int water = 0, tree = 0, ground = 0;
                for(int z = 0; z < terrainPoints[x][y].neighbours.size(); ++z) {
                    switch (terrainPoints[x][y].neighbours.get(z).getType()) {
                        case GROUND: ground++; break;
                        case TREE: tree++; break;
                        case WATER: water++; break;
                    }
                }
                if((water == 0) && (terrainPoints[x][y].getType() == TerrainPoint.Types.WATER)){
                    if(ground >= tree){
                        terrainPoints[x][y].setType(TerrainPoint.Types.GROUND);
                    } else{
                        terrainPoints[x][y].setType(TerrainPoint.Types.TREE);
                    }
                } else if((tree == 0) && (terrainPoints[x][y].getType() == TerrainPoint.Types.TREE)){
                    if(ground >= water){
                        terrainPoints[x][y].setType(TerrainPoint.Types.GROUND);
                    } else{
                        terrainPoints[x][y].setType(TerrainPoint.Types.WATER);
                    }
                } else if((ground == 0) && (terrainPoints[x][y].getType() == TerrainPoint.Types.GROUND)){
                    if(tree >= water){
                        terrainPoints[x][y].setType(TerrainPoint.Types.TREE);
                    } else{
                        terrainPoints[x][y].setType(TerrainPoint.Types.WATER);
                    }
                }
            }
        }

        System.out.printf("water %d tree %d ground %d\n", countTerrain(TerrainPoint.Types.WATER), countTerrain(TerrainPoint.Types.TREE), countTerrain(TerrainPoint.Types.GROUND));
    }

    protected int countTerrain(TerrainPoint.Types terrain){
        int terrain_count = 0;
        for (int x = 0; x < XCount; x++) {
            for (int y = 0; y < YCount; y++) {
                if(terrainPoints[x][y].getType() == terrain)
                    terrain_count++;
            }
        }
        return terrain_count;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println(String.format("Cell size: %dx%d", cellSize, cellSize));
        System.out.printf("Cell Count: %dx%d\n", XCount, YCount);
        for (int x = 0; x < XCount; x++) {
            for (int y = 0; y < YCount; y++) {
                switch (terrainPoints[x][y].getType()) {
                    case GROUND: g.setColor(new Color(0xAA4400)); break;
                    case TREE: g.setColor(new Color( 0x00AA00)); break;
                    case WATER: g.setColor(new Color(0x0066FF)); break;
                    default: g.setColor(getBackground());
                }
                g.fillRect(margin+(x*cellSize), margin+(y*cellSize), cellSize, cellSize);
            }
        }
        int _width = (XCount * cellSize);
        int _height = (YCount * cellSize);
        this.setPreferredSize(new Dimension( (_width+(2*margin)), (_height+(2*margin))));
        g.setColor(Color.BLACK);
        g.drawRect( margin, margin, _width, _height);
        for (int x = margin; x <= _width; x += cellSize) {
            g.drawLine(x, margin, x, (_height+margin));
        }
        for (int y = margin; y <= _height; y += cellSize) {
            g.drawLine(margin, y, (_width+margin), y);
        }
    }

    void iteration() {
        if (firstIteration) {
            firstIteration = false;
        }
        repaint();
        requestFocus();
    }

    void setCellSize(int size) {
        cellSize = size;
        this.setPreferredSize(
                new Dimension(
                        (XCount *cellSize)+(2*margin),
                        (YCount *cellSize)+(2*margin)
                )
        );
    }

    void setXCount(int count) {
        XCount = count;
        repaint();
    }

    void setYCount(int count) {
        YCount = count;
        repaint();
    }

    void reset() {
        repaint();
        firstIteration = true;
    }
}
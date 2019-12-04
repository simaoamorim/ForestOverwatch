import javax.swing.*;
import java.awt.*;

class TerrainGrid extends JComponent {
    private int cellSize = 10;
    private static final int margin = 1;
    static Integer[] Sizes = {50,100,200,500,1000};
    private int XCount = 50;
    private int YCount = 50;
    private boolean firstIteration = true;
    private TerrainPoint[][] terrainPoints;

    TerrainGrid() {
        setFocusable(true);
        this.setPreferredSize(
                new Dimension(
                        (XCount *cellSize)+(2*margin),
                        (YCount *cellSize)+(2*margin)
                )
        );
    }

    public void initialize() {
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
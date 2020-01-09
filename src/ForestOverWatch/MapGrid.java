package ForestOverWatch;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class MapGrid extends JComponent {
    private int cellSize;
    private static final int margin = 1;
    public static Integer[] Sizes = {50,100,200,500,1000};
    private int XCount;
    private int YCount;
    private TerrainPoint[][] terrainPoints;
    private Logger logger;

    MapGrid(int XCount, int YCount, int cellSIze, Logger logger) {
        this.XCount = XCount;
        this.YCount = YCount;
        this.cellSize = cellSIze;
        this.logger = logger;
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
    }

}

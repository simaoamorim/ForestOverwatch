package ForestOverWatch;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class MapGrid extends JComponent {
    private int cellSize;
    private static final int margin = 1;
    private int XCount;
    private int YCount;
    private MapPoint[][] mapPoints;
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
        mapPoints = new MapPoint[XCount][YCount];
        for (int x = 0; x < XCount; x++)
            for (int y = 0; y < YCount; y++)
                mapPoints[x][y] = new MapPoint(x,y);
    }

}

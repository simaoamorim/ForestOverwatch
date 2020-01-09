package ForestOverWatch;

import java.util.logging.Logger;

public class MapGrid {
    private int cellSize = 10;
    private static final int margin = 1;
    public static Integer[] Sizes = {50,100,200,500,1000};
    private int XCount;
    private int YCount;
    private TerrainPoint[][] terrainPoints;
    private Logger logger;

    MapGrid(int XCount, int YCount, Logger logger) {
        this.XCount = XCount;
        this.YCount = YCount;
        this.logger = logger;

    }

}

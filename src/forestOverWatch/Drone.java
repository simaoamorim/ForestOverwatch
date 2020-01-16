package forestOverWatch;

import java.util.Properties;
import java.util.Random;

public class Drone {
    private final int type;
    private int stepsToWait;
    private int spentIterations = 0;
    private int moveScanDepth;
    private MapPoint actualPosition;
    private final MapPoint[][] mapPoints;
    enum Orientation {Horizontal, Vertical}
    private Orientation orientation;
    private final Integer XCount;
    private final Integer YCount;
    private final TerrainPoint[][] terrainPoints;
    private final MapGrid mapGrid;

    Drone(MapPoint[][] mapPoints, MapGrid mapGrid, Properties properties, Integer type, TerrainPoint[][] terrainPoints) {
        this.mapPoints = mapPoints;
        this.mapGrid = mapGrid;
        XCount = Integer.parseInt(properties.getProperty("XCount"));
        YCount = Integer.parseInt(properties.getProperty("YCount"));
        this.terrainPoints = terrainPoints;
        this.type = type;
        switch (this.type) {
            case 1: stepsToWait = 1; moveScanDepth=0; break;
            case 2: stepsToWait = 2; moveScanDepth=1; break;
            case 3: stepsToWait = 8; moveScanDepth=2; break;
        }

    }

    void scan(){
        int x_aux = actualPosition.getXCoordinate();
        int y_aux = actualPosition.getYCoordinate();

        switch (this.type){
            case 1: scanSquare(actualPosition); break;
            case 2: {
                if (orientation == Orientation.Vertical) {
                    for (int x = -1; x < 2; x++) {
                        if (((x_aux+x) != -1) && ((x_aux+x) != (XCount)))
                            scanSquare(mapPoints[x_aux + x][y_aux]);
                    }
                } else {
                    for (int y = -1; y < 2; y++) {
                        if (((y_aux+y) != -1) && ((y_aux+y) != (YCount)))
                            scanSquare(mapPoints[x_aux][y_aux + y]);
                    }
                }
                break;
            }
            case 3: {
                for (int x = -1; x < 2; x++) {
                    for (int y = -1; y < 2; y++) {
                        if (((x_aux+x) != -1) && ((x_aux+x) != (XCount)) &&
                                ((y_aux+y) != -1) && ((y_aux+y) != (YCount))) {
                            if ((x == 0) || (y == 0)) {
                                scanSquare(mapPoints[x_aux + x][y_aux + y]);
                            }
                        }
                    }
                }
                break;
            }
        }
    }

    void scanSquare(MapPoint scannedSquare){
        int x = scannedSquare.getXCoordinate();
        int y = scannedSquare.getYCoordinate();
        scannedSquare.setStaticField(0);
        scannedSquare.setType(terrainPoints[x][y].getType());
        scannedSquare.setScanned(true); //Use this to show the points in the MapGrid
    }

    void move(){
        if (spentIterations == stepsToWait) {
            spentIterations = 0;
            float staticField_aux = actualPosition.getNeighbourByIndexMap(0).getStaticField();
            MapPoint aux = actualPosition.getNeighbourByIndexMap(0);
            int x_aux = actualPosition.getXCoordinate();
            int y_aux = actualPosition.getYCoordinate();

            for(int x = -1; x < 2; x++){
                for( int y = -1; y < 2; y++){
                    if ((x == 0) ^ (y == 0)) {
                        int new_x = x + x_aux;
                        int new_y = y + y_aux;
                        if (new_x != -1 && new_x != XCount && new_y != -1 && new_y != YCount){
                            if (! mapGrid.pointHasDrone(mapPoints[new_x][new_y])) {
                                float newval = mapPoints[new_x][new_y].getStaticField(moveScanDepth);
                                if(newval > staticField_aux){
                                    staticField_aux = newval;
                                    aux = mapPoints[new_x][new_y];
                                }
                            }
                        }
                    }
                }
            }
            calculateOrientation(actualPosition, aux);
            actualPosition = aux;
        } else {
            spentIterations++;
        }
    }

    void calculateOrientation(MapPoint actual, MapPoint next){
        if((actual.getXCoordinate() - next.getXCoordinate()) == 0){
           orientation = Orientation.Vertical;
        } else {
            orientation = Orientation.Horizontal;
        }
    }

    public MapPoint getActualPosition() {
        return actualPosition;
    }

    void randomPlacement() {
        switch (new Random().nextInt(2)) {
            case 0: actualPosition = mapPoints[0][new Random().nextInt(YCount)]; break;
            case 1: actualPosition = mapPoints[XCount-1][new Random().nextInt(YCount)]; break;
            default: break;
        }
    }

    void placeAt(int X, int Y) {
        actualPosition = mapPoints[X][Y];
    }
}

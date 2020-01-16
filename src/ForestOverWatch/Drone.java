package ForestOverWatch;

import java.util.Properties;

public class Drone {
    private static Integer []types ={1,2,3};
    private int type;
    private int velocity;
    private MapPoint actualPosition;
    private MapPoint[][] mapPoints;
    enum Orientation {Horizontal, Vertical}
    private Orientation orientation;
    private Integer XCount;
    private Integer YCount;
    private Properties localProperties;

    Drone(MapPoint[][] mapPoints, Properties properties, Integer type) {
        this.mapPoints = mapPoints;
        localProperties = properties;
        XCount = Integer.parseInt(localProperties.getProperty("XCount"));
        YCount = Integer.parseInt(localProperties.getProperty("YCount"));
        this.type = type;
        switch (this.type) {
            case 1: velocity = 80; break;
            case 2: velocity = 40; break;
            case 3: velocity = 10; break;
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
        scannedSquare.setStaticField(0);
        scannedSquare.setScanned(true); //Use this to show the points in the MapGrid
    }

    void move(){
        float staticField_aux = 0.0f;
        MapPoint aux = actualPosition;
        int x_aux = actualPosition.getXCoordinate();
        int y_aux = actualPosition.getYCoordinate();

        for(int x = -1; x < 2; x++){
            for( int y = -1; y < 2; y++){
                if ((x == 0) ^ (y == 0)) {
                    if (((x_aux+x) != -1) && ((x_aux+x) != (XCount)) &&
                            ((y_aux+y) != -1) && ((y_aux+y) != (YCount))){
                        if(mapPoints[x+x_aux][y+y_aux].getStaticField() >= staticField_aux){
                            staticField_aux = mapPoints[x+x_aux][y+y_aux].getStaticField();
                            aux = mapPoints[x+x_aux][y+y_aux];
                        }
                    }
                }
            }
        }
        calculateOrientation(actualPosition, aux);
        actualPosition = aux;
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
}

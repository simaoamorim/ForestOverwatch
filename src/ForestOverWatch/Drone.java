package ForestOverWatch;

import java.awt.*;
import java.util.ArrayList;

public class Drone {
    public static Integer []types ={1,2,3};
    public int type;
    public int velocity;
    MapPoint actualPosition;
    MapPoint[][] terrainPoint;
    TerrainGrid terrainGrid;
    enum Orientation { Horizontal, Vertical}
    private Orientation orientation;


    Drone(MapPoint[][] terrain, TerrainGrid terrainGrid) {
        terrainPoint = terrain;
        this.terrainGrid = terrainGrid;
    }

    void setType(int type) {
        this.type = type;
        switch (this.type) {
            case 1: velocity = 80; break;
            case 2: velocity = 40; break;
            case 3: velocity = 10; break;
        }
    }

    void scan(){
        int x_aux = actualPosition.coordinates.x;
        int y_aux = actualPosition.coordinates.y;

        switch (this.type){
            case 1: scanSquare(actualPosition); break;
            case 2: {
                if (orientation == Orientation.Vertical) {
                    for (int x = -1; x < 2; x++) {
                        scanSquare(terrainPoint[x_aux + x][y_aux]);
                    }
                } else {
                    for (int y = -1; y < 2; y++) {
                        scanSquare(terrainPoint[x_aux][y_aux + y]);
                    }
                }

                break;
            }
            case 3: {
                for (int x = -1; x < 2; x++) {
                    for (int y = -1; y < 2; y++) {
                        if ((x == 0) || (y == 0)) {
                            scanSquare(terrainPoint[x_aux + x][y_aux + y]);
                        }
                    }
                }

                break;
            }
        }
    }

    void scanSquare(MapPoint scannedSquare){
        scannedSquare.staticField=0;
        scannedSquare.scanned = true; //Use this to show the points in the MapGrid
    }

    void move(){
        float staticField_aux = 0;
        MapPoint aux = actualPosition;
        int x_aux = actualPosition.coordinates.x;
        int y_aux = actualPosition.coordinates.y;

        for(int x = -1; x < 2; x++){
            for( int y = -1; y < 2; y++){
                if ((x == 0) ^ (y == 0)) {
                    if (((x_aux+x) != -1) && ((x_aux+x) != (terrainGrid.XCount+1)) && ((y_aux+y) != -1) && ((y_aux+y) != (terrainGrid.YCount+1))){ //Check if this works
                        if(terrainPoint[x+x_aux][y+y_aux].staticField >= staticField_aux){
                            staticField_aux = terrainPoint[x+x_aux][y+y_aux].staticField;
                            aux = terrainPoint[x+x_aux][y+y_aux];
                        }
                    }
                }
            }
        }
        calculateOrientation(actualPosition,aux);
        actualPosition = aux;
    }

    void calculateOrientation(MapPoint actual, MapPoint next){
        if((actual.coordinates.x - next.coordinates.x) == 0){
           orientation = Orientation.Vertical;
        } else {
            orientation = Orientation.Horizontal;
        }
    }

}

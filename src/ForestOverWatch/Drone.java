package ForestOverWatch;

import java.awt.*;
import java.util.ArrayList;

public class Drone {
    public static Integer []types ={1,2,3};
    public int type;
    public int velocity;
    MapPoint actualPosition;
    MapPoint[][] terrainPoint;
    enum Orientation { Horizontal, Vertical}
    private Orientation orientation;


    Drone(MapPoint[][] terrain) {
        terrainPoint = terrain;
    }

    void setType(int type) {
        this.type = type;
        switch (this.type) {
            case 1: velocity = 80; break;
            case 2: velocity = 40; break;
            case 3: velocity = 10; break;
        }
    }

    void scanSquare(MapPoint scannedSquare){
        scannedSquare.staticField=0;
    }

    void move(){
        float staticField_aux = 0;
        MapPoint aux = actualPosition;
        int x_aux = actualPosition.coordinates.x;
        int y_aux = actualPosition.coordinates.y;

        for(int x = -1; x < 2; x++){
            for( int y = -1; y < 2; y++){
                if ((x == 0) ^ (y == 0)) {
                    if(terrainPoint[x+x_aux][y+y_aux].staticField >= staticField_aux){
                        staticField_aux = terrainPoint[x+x_aux][y+y_aux].staticField;
                        aux = terrainPoint[x+x_aux][y+y_aux];
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

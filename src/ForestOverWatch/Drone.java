package ForestOverWatch;

import java.util.ArrayList;

public class Drone {
    public static Integer []types ={1,2,3};
    public int type;
    public int velocity;
    TerrainPoint actualPosition;
    TerrainPoint[][] terrainPoint;

    Drone(TerrainPoint[][] terrain) {
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


    void scanSquare(TerrainPoint scannedSquare){
//        scannedSquare.staticField=0;
    }

    void move(){/*
        float staticField_aux = 0;
        int index_aux = 0;
        for(int i = 0;i < neighbours.size(); i++){
            if(actualPosition.neighbours.get(i).staticField >= staticField_aux){
                staticField_aux = actualPosition.neighbours.get(i).staticField;
                index_aux = i;
            }
        }
        actualPosition = actualPosition.neighbours.get(index_aux);*/
    }

}

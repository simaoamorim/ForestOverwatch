package ForestOverwatch;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

class TerrainPoint implements Serializable {
    private transient ArrayList<TerrainPoint> neighbours;
    private Types type;
    private double FirePercentage = 0;
    private final Point coordinates;
    private boolean fireLocked = false;
    public enum Types {WATER,TREE,GROUND,FIRE;
        public static Types getRandom() {
            int choice = new Random().nextInt(4);
            if (choice == 3) choice = 2;
            return Types.values()[choice];
        }
    }

    public TerrainPoint(Integer x, Integer y) {
        neighbours = new ArrayList<>();
        coordinates = new Point(x, y);
    }

    public void addNeighbour(TerrainPoint neighbour) {
        if (neighbours == null)
            neighbours = new ArrayList<>();
        neighbours.add(neighbour);
    }

    public void setType(Types type) {
        this.type = type;
    }

    public void setTypeFrom(TerrainPoint extPoint) {
        this.type = extPoint.getType();
    }

    void fireSpreading(){
        for(int i = 0; i < neighbours.size(); i++){
            if(neighbours.get(i).getType() != Types.WATER && neighbours.get(i).getType() != Types.FIRE) {
                if((i == 0) || (i == 1) || (i == 2) || (i == 5)) {//Von Neumann neighborhood
                        neighbours.get(i).FirePercentage = neighbours.get(i).FirePercentage + 2;
                }
                else {
                    neighbours.get(i).FirePercentage = neighbours.get(i).FirePercentage + 1.43;
                }
                neighbours.get(i).fireCheck();
            }
        }
    }

    private void fireCheck(){
        if(FirePercentage >= 100)
            setType(Types.FIRE);
    }

    public Types getType() {
        return type;
    }

    public boolean fireSpreadingCondition() {
        if (type != Types.FIRE)
            return false;
        for (TerrainPoint neighbour: neighbours) {
            if (neighbour.getType() != Types.WATER && neighbour.getType() != Types.FIRE)
                return true;
        }
        fireLocked = true;
        return false;
    }

    public ArrayList<TerrainPoint> getNeighbours() {
        return neighbours;
    }

    public Integer getXCoordinate() {
        return coordinates.x;
    }

    public Integer getYCoordinate() {
        return coordinates.y;
    }

    public boolean isFireLocked() {
        return fireLocked;
    }

    public TerrainPoint getNeighbourByIndex(Integer index) {
        return neighbours.get(index);
    }
}

package ForestOverWatch;

import java.util.ArrayList;
import java.util.Random;

class TerrainPoint {
    ArrayList<TerrainPoint> neighbours;
    private Types type;
    private double FirePercentage = 0;
    enum Types {WATER,TREE,GROUND,FIRE;
        public static Types getRandom() {
            int choice = new Random().nextInt(4);
            if (choice == 3) choice = 2;
            return Types.values()[choice];
        }
    }

    TerrainPoint() {
        neighbours = new ArrayList<>();
    }

    protected void addNeighbour(TerrainPoint neighbour) {
        neighbours.add(neighbour);
    }

    void setType(Types type) {
        this.type = type;
    }

    void fireSpreading(){
        for(int i = 0; i < neighbours.size(); i++){
            if(neighbours.get(i).getType() != Types.WATER){
                if((i == 0) || (i == 1) || (i == 2) || (i == 5)) {//Von Neumann neighborhood
                        neighbours.get(i).FirePercentage = neighbours.get(i).FirePercentage + 2;
                        if (neighbours.get(i).FirePercentage >= 100) neighbours.get(i).setType(Types.FIRE);
                    }
                else {
                    neighbours.get(i).FirePercentage = neighbours.get(i).FirePercentage + 1.43;
                    if (neighbours.get(i).FirePercentage >= 100)  neighbours.get(i).setType(Types.FIRE);
                }
            }
        }
    }

    void fireCheck(){
        if(FirePercentage >= 100)
            setType(Types.FIRE);
    }

    Types getType() {
        return type;
    }

}

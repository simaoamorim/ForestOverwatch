package ForestOverWatch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

class TerrainPoint implements Serializable {
    transient ArrayList<TerrainPoint> neighbours;
    private Types type;
    private double FirePercentage = 0;
    public float staticField = 0;

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
        if (neighbours == null)
            neighbours = new ArrayList<>();
        neighbours.add(neighbour);
    }

    void calcStaticField(){
        if((getType() == Types.TREE) || (getType() == Types.GROUND)){
            staticField += 1.55;
        } else if(this.getType() == Types.FIRE){
            for (int i = 0; i < neighbours.size() ; i++){
                if((neighbours.get(i).getType() == Types.TREE) || (neighbours.get(i).getType() == Types.GROUND))
                    neighbours.get(i).staticField += 1.55;
            }
        }
    }

    void setType(Types type) {
        this.type = type;
    }

    void fireSpreading(){
        for(int i = 0; i < neighbours.size(); i++){
            if(neighbours.get(i).getType() != Types.WATER){
                if((i == 0) || (i == 1) || (i == 2) || (i == 5)) {//Von Neumann neighborhood
                        neighbours.get(i).FirePercentage = neighbours.get(i).FirePercentage + 2;
                }
                else {
                    neighbours.get(i).FirePercentage = neighbours.get(i).FirePercentage + 1.43;
                }
                fireCheck();
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

package Terrain;

import java.util.ArrayList;
import java.util.Random;

class TerrainPoint {
    private ArrayList<TerrainPoint> neighbours;
    private Types type;
    enum Types {WATER,GROUND,TREE;
        public static Types getRandom() {
            Types[] choose = {WATER,GROUND,GROUND,TREE};
            int choice = new Random().nextInt(4);
            return choose[choice];
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

    Types getType() {
        return type;
    }

}

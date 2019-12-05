package ForestOverWatch;

import java.util.ArrayList;
import java.util.Random;

class TerrainPoint {
    ArrayList<TerrainPoint> neighbours;
    private Types type;
    enum Types {WATER,TREE,GROUND;
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

    Types getType() {
        return type;
    }

}

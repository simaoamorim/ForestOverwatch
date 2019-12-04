import java.util.ArrayList;
import java.util.Random;

public class TerrainPoint {
    private ArrayList<TerrainPoint> neighbours;
    private Types type;
    public enum Types {WATER,GROUND,TREE;
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

    public void setType(Types type) {
        this.type = type;
    }

    public Types getType() {
        return type;
    }

}

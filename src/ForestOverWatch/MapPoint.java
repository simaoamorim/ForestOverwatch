package ForestOverWatch;

import java.util.ArrayList;

public class MapPoint extends TerrainPoint {
    private transient ArrayList<MapPoint> neighbours;
    private float staticField = 0;
    private boolean scanned;

    MapPoint(Integer x, Integer y) {
        super(x, y);
    }

    void calcStaticField(){
        if((getType() == Types.TREE) || (getType() == Types.GROUND)){
            staticField += 1.55;
        } else if(this.getType() == Types.FIRE){
            for (MapPoint neighbour : neighbours) {
                if ((neighbour.getType() != Types.WATER) || !(scanned)) //Verify if it works better
                    neighbour.staticField += 1.55;
            }
        }
    }

    public void addNeighbour(MapPoint neighbour) {
        if (neighbours == null)
            neighbours = new ArrayList<>();
        neighbours.add(neighbour);
    }

    public void setScanned(boolean scanned) {
        this.scanned = scanned;
    }

    public float getStaticField() {
        return staticField;
    }

    public void setStaticField(float staticField) {
        this.staticField = staticField;
    }
}

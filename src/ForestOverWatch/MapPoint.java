package ForestOverWatch;

import java.util.ArrayList;

public class MapPoint extends TerrainPoint {
    private transient ArrayList<MapPoint> neighbours;
    private float staticField = 100000;
    private boolean scanned = false;

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

    public MapPoint getNeighbourByIndexMap(Integer index) {
        return neighbours.get(index);
    }

    public ArrayList<MapPoint> getNeighboursMap() {
        return neighbours;
    }


    public float getStaticField() {
        return staticField;
    }

    public void setStaticField(float staticField) {
        this.staticField = staticField;
    }

    boolean isScanned() {return scanned;}

    void calculateField() {
        setScanned(true);
        for(int x = 0; x < getNeighboursMap().size(); x++) {
            if (getStaticField() >= (getNeighbourByIndexMap(x).getStaticField())) {
                setStaticField(getNeighbourByIndexMap(x).getStaticField()-1);
            }
        }
    }
}

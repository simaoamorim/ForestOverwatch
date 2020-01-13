package ForestOverWatch;

import java.util.ArrayList;

public class MapPoint extends TerrainPoint {
    transient ArrayList<MapPoint> neighbours;
    public float staticField = 0;

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
}

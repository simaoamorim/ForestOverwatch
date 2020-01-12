package ForestOverWatch;

import java.util.ArrayList;

public class MapPoint extends TerrainPoint {
    transient ArrayList<MapPoint> neighbours;
    public float staticField = 0;

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
}

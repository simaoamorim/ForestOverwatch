package forestOverWatch;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Random;
import java.util.logging.Logger;

class TerrainGrid extends JComponent {
    private int cellSize;
    private static final int margin = 1;
    public static final Integer[] Sizes = {50,100,200,500,1000};
    final int XCount;
    final int YCount;
    private boolean firstIteration = true;
    public TerrainPoint[][] terrainPoints;
    private final Logger logger;

    TerrainGrid(int XCount, int YCount, int cellSize, Logger logger) {
        this.XCount = XCount;
        this.YCount = YCount;
        this.cellSize = cellSize;
        this.logger = logger;
        setFocusable(true);
        this.setPreferredSize(
                new Dimension(
                        (XCount *cellSize)+(2*margin),
                        (YCount *cellSize)+(2*margin)
                )
        );
    }

    void initialize() {
        terrainPoints = new TerrainPoint[XCount][YCount];
        for (int x = 0; x < XCount; x++)
            for (int y = 0; y < YCount; y++)
                terrainPoints[x][y] = new TerrainPoint(x, y);
        setNeighbourhood();
    }

    void setNeighbourhood() {
        //      Add neighbors, ignoring borders (Moore)
        for (int x = 0; x < XCount; x++) {
            for (int y = 0; y < YCount; y++) {
                if (y > 0)
                    terrainPoints[x][y].addNeighbour(terrainPoints[x][y - 1]); //Von Neumann neighborhood
                if (y < YCount - 1)
                    terrainPoints[x][y].addNeighbour(terrainPoints[x][y + 1]); //Von Neumann neighborhood
                if (x > 0) {
                    terrainPoints[x][y].addNeighbour(terrainPoints[x - 1][y]); //Von Neumann neighborhood
                    if (y > 0)
                        terrainPoints[x][y].addNeighbour(terrainPoints[x - 1][y - 1]);
                    if (y < YCount - 1)
                        terrainPoints[x][y].addNeighbour(terrainPoints[x - 1][y + 1]);
                }
                if (x < XCount - 1) {
                    terrainPoints[x][y].addNeighbour(terrainPoints[x + 1][y]); //Von Neumann neighborhood
                    if (y > 0)
                        terrainPoints[x][y].addNeighbour(terrainPoints[x + 1][y - 1]);
                    if (y < YCount - 1)
                        terrainPoints[x][y].addNeighbour(terrainPoints[x + 1][y + 1]);
                }
            }
        }
    }

    void randomizeTerrain() {
        initialize();
        for (int x = 0; x < XCount; x++) {
            for (int y = 0; y < YCount; y++) {
                terrainPoints[x][y].setType(TerrainPoint.Types.getRandom());
            }
        }
        softTerrain();

        int auxX = XCount/50;
        int auxY = YCount/50;
        for(int x = 0; x < auxX; x++){
            for(int y=0; y < auxY; y++){
                if(((x+y)%3) == 0){
                    for (int z = 0; z < 50; z++) {
                        for (int w = 0; w < 50; w++){
                            terrainPoints[z + x*50][w + y*50].setTypeFrom(terrainPoints[z][w]);
                        }
                    }
                } else if(((x+y)%4) == 0 && ((x+y)%2!=0)){
                    for (int z = 0; z < 50; z++) {
                        for (int w = 49; w >= 0; w--){
                            terrainPoints[z + x*50][w + y*50].setTypeFrom(terrainPoints[z][49-w]);
                        }
                    }
                } else if(((x+y)%2)==0){
                    for (int z = 49; z >= 0; z--) {
                        for (int w = 0; w < 50; w++){
                            terrainPoints[z + x*50][w + y*50].setTypeFrom(terrainPoints[49-z][w]);
                        }
                    }
                } else {
                    for (int z = 49; z >= 0; z--) {
                        for (int w = 49; w >= 0; w--){
                            terrainPoints[z + x*50][w + y*50].setTypeFrom(terrainPoints[49-z][49-w]);
                        }
                    }
                }
            }
        }
        for(int x = 0; x < XCount; x++){
            for(int w = 0; w < auxX; w++) {
                if((x == 49*w) || (x== 50*w)){
                    for(int y = 0; y < YCount; y++) {
                        int water = 0, tree = 0, ground = 0;
                        for(int z = 0; z < terrainPoints[x][y].getNeighbours().size(); ++z) {
                            switch (terrainPoints[x][y].getNeighbourByIndex(z).getType()) {
                                case GROUND: ground++; break;
                                case TREE: tree++; break;
                                case WATER: water++; break;
                            }
                        }
                        if((water < 3) && (terrainPoints[x][y].getType() == TerrainPoint.Types.WATER)){
                            if(ground >= tree){
                                terrainPoints[x][y].setType(TerrainPoint.Types.GROUND);
                            } else{
                                terrainPoints[x][y].setType(TerrainPoint.Types.TREE);
                            }
                        } else if((tree < 3) && (terrainPoints[x][y].getType() == TerrainPoint.Types.TREE)){
                            if(ground >= water){
                                terrainPoints[x][y].setType(TerrainPoint.Types.GROUND);
                            } else{
                                terrainPoints[x][y].setType(TerrainPoint.Types.WATER);
                            }
                        } else if((ground < 3) && (terrainPoints[x][y].getType() == TerrainPoint.Types.GROUND)){
                            if(tree >= water){
                                terrainPoints[x][y].setType(TerrainPoint.Types.TREE);
                            } else{
                                terrainPoints[x][y].setType(TerrainPoint.Types.WATER);
                            }
                        }
                    }
                }
            }
        }
        for(int y = 0; y < YCount; y++){
            for(int w = 0; w < auxY; w++) {
                if((y == 49*w) || (y== 50*w)){
                    for(int x = 0; x < XCount; x++) {
                        int water = 0, tree = 0, ground = 0;
                        for(int z = 0; z < terrainPoints[x][y].getNeighbours().size(); ++z) {
                            switch (terrainPoints[x][y].getNeighbourByIndex(z).getType()) {
                                case GROUND: ground++; break;
                                case TREE: tree++; break;
                                case WATER: water++; break;
                            }
                        }
                        if((water < 3) && (terrainPoints[x][y].getType() == TerrainPoint.Types.WATER)){
                            if(ground >= tree){
                                terrainPoints[x][y].setType(TerrainPoint.Types.GROUND);
                            } else{
                                terrainPoints[x][y].setType(TerrainPoint.Types.TREE);
                            }
                        } else if((tree < 3) && (terrainPoints[x][y].getType() == TerrainPoint.Types.TREE)){
                            if(ground >= water){
                                terrainPoints[x][y].setType(TerrainPoint.Types.GROUND);
                            } else{
                                terrainPoints[x][y].setType(TerrainPoint.Types.WATER);
                            }
                        } else if((ground < 3) && (terrainPoints[x][y].getType() == TerrainPoint.Types.GROUND)){
                            if(tree >= water){
                                terrainPoints[x][y].setType(TerrainPoint.Types.TREE);
                            } else{
                                terrainPoints[x][y].setType(TerrainPoint.Types.WATER);
                            }
                        }
                    }
                }
            }
        }
        logger.info(String.format("water %d tree %d ground %d\n",
                countTerrain(TerrainPoint.Types.WATER),
                countTerrain(TerrainPoint.Types.TREE),
                countTerrain(TerrainPoint.Types.GROUND)));
        repaint();
    }

    void randomizeFire() {
        int count = new Random().nextInt(4)+1;
        for (int i = 0; i < count; i++) {
            int x = new Random().nextInt(XCount);
            int y = new Random().nextInt(YCount);
            TerrainPoint.Types type = terrainPoints[x][y].getType();

            if (type == TerrainPoint.Types.WATER)
                i--;
            else if (type == TerrainPoint.Types.GROUND || type == TerrainPoint.Types.TREE)
                terrainPoints[x][y].setType(TerrainPoint.Types.FIRE);
        }
    }

    private void softTerrain(){
        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                int water = 0, tree = 0, ground = 0;
                for(int z = 0; z < terrainPoints[x][y].getNeighbours().size(); ++z) {
                    switch (terrainPoints[x][y].getNeighbourByIndex(z).getType()) {
                        case GROUND: ground++; break;
                        case TREE: tree++; break;
                        case WATER: water++; break;
                    }
                }
                if((water > Math.max(tree,ground))){
                    terrainPoints[x][y].setType(TerrainPoint.Types.WATER);
                } else if((tree > Math.max(water,ground))){
                    terrainPoints[x][y].setType(TerrainPoint.Types.TREE);
                } else if((ground > Math.max(water,tree)) && (countTerrain(TerrainPoint.Types.GROUND) < 125)){
                    terrainPoints[x][y].setType(TerrainPoint.Types.GROUND);
                }
            }
        }

        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                int water = 0, tree = 0, ground = 0;
                for(int z = 0; z < terrainPoints[x][y].getNeighbours().size(); ++z) {
                    switch (terrainPoints[x][y].getNeighbourByIndex(z).getType()) {
                        case GROUND: ground++; break;
                        case TREE: tree++; break;
                        case WATER: water++; break;
                    }
                }
                if((water < 3) && (terrainPoints[x][y].getType() == TerrainPoint.Types.WATER)){
                    if(ground >= tree){
                        terrainPoints[x][y].setType(TerrainPoint.Types.GROUND);
                    } else{
                        terrainPoints[x][y].setType(TerrainPoint.Types.TREE);
                    }
                } else if((tree < 3) && (terrainPoints[x][y].getType() == TerrainPoint.Types.TREE)){
                    if(ground >= water){
                        terrainPoints[x][y].setType(TerrainPoint.Types.GROUND);
                    } else{
                        terrainPoints[x][y].setType(TerrainPoint.Types.WATER);
                    }
                } else if((ground < 3) && (terrainPoints[x][y].getType() == TerrainPoint.Types.GROUND)){
                    if(tree >= water){
                        terrainPoints[x][y].setType(TerrainPoint.Types.TREE);
                    } else{
                        terrainPoints[x][y].setType(TerrainPoint.Types.WATER);
                    }
                }
            }
        }
        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                int water = 0, tree = 0, ground = 0;
                for(int z = 0; z < terrainPoints[x][y].getNeighbours().size(); ++z) {
                    switch (terrainPoints[x][y].getNeighbourByIndex(z).getType()) {
                        case GROUND: ground++; break;
                        case TREE: tree++; break;
                        case WATER: water++; break;
                    }
                }
                if((water == 0) && (terrainPoints[x][y].getType() == TerrainPoint.Types.WATER)){
                    if(ground >= tree){
                        terrainPoints[x][y].setType(TerrainPoint.Types.GROUND);
                    } else{
                        terrainPoints[x][y].setType(TerrainPoint.Types.TREE);
                    }
                } else if((tree == 0) && (terrainPoints[x][y].getType() == TerrainPoint.Types.TREE)){
                    if(ground >= water){
                        terrainPoints[x][y].setType(TerrainPoint.Types.GROUND);
                    } else{
                        terrainPoints[x][y].setType(TerrainPoint.Types.WATER);
                    }
                } else if((ground == 0) && (terrainPoints[x][y].getType() == TerrainPoint.Types.GROUND)){
                    if(tree >= water){
                        terrainPoints[x][y].setType(TerrainPoint.Types.TREE);
                    } else{
                        terrainPoints[x][y].setType(TerrainPoint.Types.WATER);
                    }
                }
            }
        }

    }

    private int countTerrain(TerrainPoint.Types terrain){
        int terrain_count = 0;
        for (int x = 0; x < XCount; x++) {
            for (int y = 0; y < YCount; y++) {
                if(terrainPoints[x][y].getType() == terrain)
                    terrain_count++;
            }
        }
        return terrain_count;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        logger.info(String.format("Cell size: %dx%d", cellSize, cellSize));
        logger.info(String.format("Cell Count: %dx%d\n", XCount, YCount));
        if (terrainPoints != null) {
            for (int x = 0; x < XCount; x++) {
                for (int y = 0; y < YCount; y++) {
                    switch (terrainPoints[x][y].getType()) {
                        case GROUND: g.setColor(new Color(0x7B4716)); break;
                        case TREE: g.setColor(new Color( 0x00AA00)); break;
                        case WATER: g.setColor(new Color(0x0066FF)); break;
                        case FIRE: g.setColor(new Color(0xFF0000)); break;
                        default: g.setColor(getBackground());
                    }
                    g.fillRect(margin+(x*cellSize), margin+(y*cellSize), cellSize, cellSize);
                }
            }
        }
        int _width = (XCount * cellSize);
        int _height = (YCount * cellSize);
        this.setPreferredSize(new Dimension( (_width+(2*margin)), (_height+(2*margin))));
    }

    void iteration() {
        if (firstIteration) {
            firstIteration = false;
        }
        for (int x = 0; x < XCount; x++) {
            for (int y = 0; y < YCount; y++) {
                if (!terrainPoints[x][y].isFireLocked() && terrainPoints[x][y].fireSpreadingCondition())
                    terrainPoints[x][y].fireSpreading();
            }
        }
        repaint();
        revalidate();
    }

    void setCellSize(int size) {
        cellSize = size;
        this.setPreferredSize(
                new Dimension(
                        (XCount *cellSize)+(2*margin),
                        (YCount *cellSize)+(2*margin)
                )
        );
    }

    void saveTerrain(String path) throws IOException {
        FileOutputStream f = new FileOutputStream(path);
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(terrainPoints);
        o.close();
        f.close();
        logger.info("terrainPoints saved to file \""+path+"\"");
    }

    void loadTerrain(String path) throws IOException {
        FileInputStream f = new FileInputStream(path);
        ObjectInputStream o = new ObjectInputStream(f);
        try {
            terrainPoints = (TerrainPoint[][]) o.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        o.close();
        f.close();
        if (terrainPoints.length != XCount || terrainPoints[0].length != YCount) {
            logger.warning("Loaded file does not match the configuration, aborting!");
            terrainPoints = null;
            return;
        }
        setNeighbourhood();
        repaint();
        logger.info("terrainPoint successfully loaded from file \""+path+"\"");
    }
}
package ForestOverWatch;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import java.util.logging.Logger;

public class MapFrame extends BaseGridFrame {
    private Integer XCount;
    private Integer YCount;
    private Integer CellSize;
    private MapGrid mapGrid;
    private Properties localProperties;
    private Settings settingsWindow;
    private Logger logger;


    public MapFrame(Properties properties, Settings settings, Logger logger) {
        super();
        localProperties = properties;
        settingsWindow = settings;
        XCount = Integer.parseInt(localProperties.getProperty("XCount"));
        YCount = Integer.parseInt(localProperties.getProperty("YCount"));
        CellSize = Integer.parseInt(localProperties.getProperty("cellSize"));
        this.logger = logger;
        mapGrid = new MapGrid(XCount, YCount, CellSize, this.logger, localProperties);
        mapGrid.initialize();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                settings.mapFrameClosed();
            }
        });
        setLocation(settings.getX()+settings.getWidth()+50, settings.getY()+50);
        setTitle("Forest Over-Watch - Map");
    }

    void timerHandler() {

    }
}

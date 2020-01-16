package ForestOverWatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Properties;

public class MapSettings extends JFrame {
    private Properties localProperties;
    private int XCount;
    private int YCount;
    private MapGrid mapGrid;
    private Integer[] DroneCount = new Integer[3];
    private JSpinner[][] droneXPosition = new JSpinner[3][];
    private JSpinner[][] droneYPosition = new JSpinner[3][];
    private JCheckBox randomizeAll = new JCheckBox("Randomize placements");

    MapSettings(Properties properties, MapGrid mapGrid) {
        localProperties = properties;
        this.mapGrid = mapGrid;
        XCount = Integer.parseInt(localProperties.getProperty("XCount"));
        YCount = Integer.parseInt(localProperties.getProperty("YCount"));
        DroneCount[0] = Integer.parseInt(localProperties.getProperty("DroneCount1"));
        DroneCount[1] = Integer.parseInt(localProperties.getProperty("DroneCount2"));
        DroneCount[2] = Integer.parseInt(localProperties.getProperty("DroneCount3"));
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel main = new JPanel(new GridLayout(0,2));
        JPanel[] childs = new JPanel[3];
        for (int i=0; i<3; i++) {
            childs[i] = new JPanel(new GridLayout(0,2));
            droneXPosition[i] = new JSpinner[DroneCount[i]];
            droneYPosition[i] = new JSpinner[DroneCount[i]];
            for (int j=0; j<DroneCount[i]; j++) {
                droneXPosition[i][j] = new JSpinner(new SpinnerNumberModel(0,0,XCount,1));
                droneYPosition[i][j] = new JSpinner(new SpinnerNumberModel(0,0,YCount,1));
                childs[i].add(new JLabel("X:"));
                childs[i].add(droneXPosition[i][j]);
                childs[i].add(new JLabel("Y:"));
                childs[i].add(droneYPosition[i][j]);
            }
            main.add(new JLabel(String.format("Drone type %d", i)));
            main.add(childs[i]);
        }
        JButton finish = new JButton("Apply");
        finish.setEnabled(true);
        finish.addActionListener(this::finish);
        main.add(randomizeAll);
        main.add(finish);
        JScrollPane scroller = new JScrollPane(main);
        add(scroller);
        setMinimumSize(new Dimension(300,400));
        setVisible(true);
    }

    private void finish(ActionEvent e) {
        placeDrones();
        setVisible(false);
        mapGrid.calcStaticField();
        mapGrid.repaint();
        mapGrid.revalidate();
    }

    private void placeDrones() {
        int droneCount = 0;
        for (int i=0; i<3; i++) {
            for (int j=0; j<DroneCount[i]; j++) {
                if (randomizeAll.isSelected())
                    mapGrid.addDrone(droneCount, i+1);
                else
                    mapGrid.addDrone(droneCount, i+1, (int) droneXPosition[i][j].getValue(), (int) droneYPosition[i][j].getValue());
                droneCount++;
            }
        }
    }
}

package ForestOverwatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Properties;

public class MapSettings extends JFrame {
    private final int XCount;
    private final int YCount;
    private final MapGrid mapGrid;
    private final Integer[] DroneCount = new Integer[3];
    private final JSpinner[][] droneXPosition = new JSpinner[3][];
    private final JSpinner[][] droneYPosition = new JSpinner[3][];
    private final JCheckBox randomizeAll = new JCheckBox("Randomize placements");

    MapSettings(Properties properties, MapGrid mapGrid) {
        this.mapGrid = mapGrid;
        XCount = Integer.parseInt(properties.getProperty("XCount"));
        YCount = Integer.parseInt(properties.getProperty("YCount"));
        DroneCount[0] = Integer.parseInt(properties.getProperty("DroneCount1"));
        DroneCount[1] = Integer.parseInt(properties.getProperty("DroneCount2"));
        DroneCount[2] = Integer.parseInt(properties.getProperty("DroneCount3"));
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel main = new JPanel(new GridLayout(0,2));
        JPanel[] children = new JPanel[3];
        for (int i=0; i<3; i++) {
            children[i] = new JPanel(new GridLayout(0,2));
            droneXPosition[i] = new JSpinner[DroneCount[i]];
            droneYPosition[i] = new JSpinner[DroneCount[i]];
            for (int j=0; j<DroneCount[i]; j++) {
                droneXPosition[i][j] = new JSpinner(new SpinnerNumberModel(0,0,XCount,1));
                droneYPosition[i][j] = new JSpinner(new SpinnerNumberModel(0,0,YCount,1));
                children[i].add(new JLabel("X:"));
                children[i].add(droneXPosition[i][j]);
                children[i].add(new JLabel("Y:"));
                children[i].add(droneYPosition[i][j]);
            }
            main.add(new JLabel(String.format("Drone type %d", i)));
            main.add(children[i]);
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

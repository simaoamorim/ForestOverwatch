package Terrain;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TerrainFrame extends JFrame {
    private static final Dimension frameSize = new Dimension(700,600);
    private static final Dimension maxFrameSize = Toolkit.getDefaultToolkit().getScreenSize();
    private TerrainGrid terrainGrid;
    private JSlider slider;
    private JPanel panel2;
    private JComboBox<Integer> xCountChoice;
    private JComboBox<Integer> yCountChoice;
    private JButton startButton;
    private JButton resetButton;
    private Timer iterationTimer = new Timer(timeStep, this::timerHandler);
    private static final int timeStep = 12; // Time in ms (1000/80 = 12.5)

    public TerrainFrame() {
        setTitle("Forest Over-Watch - Terrain");
        setPreferredSize(frameSize);
        setMinimumSize(frameSize);
        setMaximumSize(maxFrameSize);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
        iterationTimer.stop();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        terrainGrid = new TerrainGrid();
        terrainGrid.initialize();
        panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel2.add(terrainGrid);
        JScrollPane mainPanel = new JScrollPane(panel2);
        add(mainPanel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        xCountChoice = new JComboBox<>(TerrainGrid.Sizes);
        xCountChoice.setFocusable(false);
        xCountChoice.setActionCommand("setXCount");
        xCountChoice.addActionListener(this::actionPerformed);
        yCountChoice = new JComboBox<>(TerrainGrid.Sizes);
        yCountChoice.setFocusable(false);
        yCountChoice.setActionCommand("setYCount");
        yCountChoice.addActionListener(this::actionPerformed);
        resetButton = new JButton("Reset");
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(this::actionPerformed);
        resetButton.setFocusable(false);
        startButton = new JButton("Start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this::actionPerformed);
        startButton.setFocusable(false);
        slider = new JSlider(JSlider.HORIZONTAL);
        slider.setMinimum(4);
        slider.setMaximum(25);
        slider.setValue(10);
        slider.addChangeListener(this::zoomChosen);
        slider.setFocusable(false);
        resetButton.setSize(100,60);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(new JLabel("Width:"));
        buttonsPanel.add(xCountChoice);
        buttonsPanel.add(new JLabel("Height:"));
        buttonsPanel.add(yCountChoice);
        buttonsPanel.add(startButton);
        buttonsPanel.add(resetButton);
        buttonsPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        buttonsPanel.add(new JLabel("Zoom:"));
        buttonsPanel.add(slider);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void zoomChosen(ChangeEvent e) {
        int reqSize = slider.getValue();
        System.out.println(String.format("Setting zoom to %d", reqSize));
        terrainGrid.setCellSize(reqSize);
        terrainGrid.repaint();
        terrainGrid.revalidate();
        panel2.setPreferredSize(
                new Dimension(
                        (int) terrainGrid.getPreferredSize().getWidth()+5,
                        (int) terrainGrid.getPreferredSize().getHeight()+5
                )
        );
    }

    private void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "reset" : {
                xCountChoice.setSelectedItem(50.0);
                yCountChoice.setSelectedItem(50.0);
                slider.setValue(10);
                pack(); // Resize the window to fit contents
                setLocationRelativeTo(null); // Move window to the center of the screen
                terrainGrid.reset();
                break;
            }
            case "start": {
                if (! iterationTimer.isRunning()) {
                    iterationTimer.start();
                    startButton.setText("Pause");
                    resetButton.setEnabled(false);
                }
                else {
                    iterationTimer.stop();
                    startButton.setText("Start");
                    resetButton.setEnabled(true);
                }
                break;
            }
            case "setXCount": {
                System.out.println("Changed X count");
                terrainGrid.setXCount((int) xCountChoice.getSelectedItem());
                terrainGrid.repaint();
                break;
            }
            case "setYCount": {
                System.out.println("Changed Y count");
                terrainGrid.setYCount((int) yCountChoice.getSelectedItem());
                terrainGrid.repaint();
                break;
            }
            default: {
                break;
            }
        }
    }

    private void timerHandler(ActionEvent e) {
        terrainGrid.iteration();
    }

}
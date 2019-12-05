import Terrain.TerrainFrame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Settings extends JFrame {private JComboBox<Integer> xCountChoice;
    private JComboBox<Integer> yCountChoice;
    private JButton startButton;
    private JButton resetButton;
    private JSlider slider;
    private TerrainFrame terrainFrame;

    Settings() {
        terrainFrame = new TerrainFrame();
        initUI();
        pack();
        setLocation(10,10);
        terrainFrame.setLocation(getX()+getWidth(), getY());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initUI() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonsPanel = new JPanel(new SpringLayout());
        xCountChoice = new JComboBox<>(TerrainFrame.Sizes);
        xCountChoice.setFocusable(false);
        xCountChoice.setActionCommand("setXCount");
        xCountChoice.addActionListener(this::actionPerformed);
        yCountChoice = new JComboBox<>(TerrainFrame.Sizes);
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
        buttonsPanel.setLayout(new GridLayout(0,2));
        buttonsPanel.add(new JLabel("Width:"));
        buttonsPanel.add(xCountChoice);
        buttonsPanel.add(new JLabel("Height:"));
        buttonsPanel.add(yCountChoice);
        buttonsPanel.add(startButton);
        buttonsPanel.add(resetButton);
        buttonsPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        buttonsPanel.add(new JLabel("Zoom:"));
        buttonsPanel.add(slider);
        add(buttonsPanel);
    }

    private void zoomChosen(ChangeEvent e) {
        int reqSize = slider.getValue();
        System.out.println(String.format("Setting zoom to %d", reqSize));
        terrainFrame.setCellSize(reqSize);
        terrainFrame.repaint();
        terrainFrame.revalidate();
        pack();
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
                terrainFrame.reset();
                break;
            }
            case "start": {
                if (! terrainFrame.isRunning()) {
                    terrainFrame.startIteration();
                    startButton.setText("Pause");
                    resetButton.setEnabled(false);
                }
                else {
                    terrainFrame.stopIteration();
                    startButton.setText("Start");
                    resetButton.setEnabled(true);
                }
                break;
            }
            case "setXCount": {
                System.out.println("Changed X count");
                terrainFrame.setXCount((int) xCountChoice.getSelectedItem());
                terrainFrame.repaint();
                break;
            }
            case "setYCount": {
                System.out.println("Changed Y count");
                terrainFrame.setYCount((int) yCountChoice.getSelectedItem());
                terrainFrame.repaint();
                break;
            }
            default: {
                break;
            }
        }
    }

}

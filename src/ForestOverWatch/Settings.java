package ForestOverWatch;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Settings extends JFrame {private JComboBox<Integer> xCountChoice;
    private JComboBox<Integer> yCountChoice;
    private JButton newWindowButton;
    private JButton resetButton;
    private JButton startButton;
    private JButton randomizeFireButton;
    private JButton randomizeTerrainButton;
    private JButton saveTerrain;
    private JButton loadTerrain;
    private JFileChooser fileChooser = new JFileChooser(".");
    private JSlider slider;
    private TerrainFrame terrainFrame;
    private Properties localProperties = new Properties();
    private Logger logger;

    Settings(Logger logger) {
        this.logger = logger;
        loadProperties();
        logger.config(localProperties.toString());
        setTitle("Forest Over-Watch - Settings");
        initUI();
        pack();
        setLocation(10,10);
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
        xCountChoice.setSelectedItem(Integer.parseInt(localProperties.getProperty("XCount")));
        yCountChoice = new JComboBox<>(TerrainFrame.Sizes);
        yCountChoice.setFocusable(false);
        yCountChoice.setActionCommand("setYCount");
        yCountChoice.addActionListener(this::actionPerformed);
        yCountChoice.setSelectedItem(Integer.parseInt(localProperties.getProperty("YCount")));
        resetButton = new JButton("Reset");
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(this::actionPerformed);
        resetButton.setFocusable(false);
        newWindowButton = new JButton("Create Window");
        newWindowButton.setActionCommand("createWindow");
        newWindowButton.addActionListener(this::actionPerformed);
        newWindowButton.setFocusable(false);
        startButton = new JButton("Start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this::actionPerformed);
        startButton.setFocusable(false);
        startButton.setEnabled(false);
        randomizeFireButton = new JButton("Randomize fire");
        randomizeFireButton.setActionCommand("randomizeFire");
        randomizeFireButton.addActionListener(this::actionPerformed);
        randomizeFireButton.setFocusable(false);
        randomizeFireButton.setEnabled(false);
        randomizeTerrainButton = new JButton("Randomize Terrain");
        randomizeTerrainButton.setActionCommand("randomizeTerrain");
        randomizeTerrainButton.addActionListener(this::actionPerformed);
        randomizeTerrainButton.setFocusable(false);
        randomizeTerrainButton.setEnabled(false);
        saveTerrain = new JButton("Save Terrain Layout");
        saveTerrain.setFocusable(false);
        saveTerrain.setActionCommand("saveTerrain");
        saveTerrain.addActionListener(this::actionPerformed);
        saveTerrain.setEnabled(false);
        loadTerrain = new JButton("Load terrain from file");
        loadTerrain.setFocusable(false);
        loadTerrain.setActionCommand("loadTerrain");
        loadTerrain.addActionListener(this::actionPerformed);
        loadTerrain.setEnabled(true);
        slider = new JSlider(JSlider.HORIZONTAL);
        slider.setMinimum(1);
        slider.setMaximum(25);
        slider.setValue(Integer.parseInt(localProperties.getProperty("cellSize")));
        slider.addChangeListener(this::zoomChosen);
        slider.setFocusable(false);
        resetButton.setSize(100,60);
        buttonsPanel.setLayout(new GridLayout(0,2));
        buttonsPanel.add(new JLabel("Width:", JLabel.RIGHT));
        buttonsPanel.add(xCountChoice);
//        buttonsPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        buttonsPanel.add(new JLabel("Height:", JLabel.RIGHT));
        buttonsPanel.add(yCountChoice);
//        buttonsPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        buttonsPanel.add(new JLabel("Zoom:", JLabel.RIGHT));
        buttonsPanel.add(slider);
//        buttonsPanel.add(new JSeparator(JSeparator.HORIZONTAL));
//        buttonsPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        buttonsPanel.add(newWindowButton);
        buttonsPanel.add(resetButton);
        buttonsPanel.add(startButton);
        buttonsPanel.add(randomizeFireButton);
        buttonsPanel.add(randomizeTerrainButton);
        buttonsPanel.add(saveTerrain);
        buttonsPanel.add(loadTerrain);
//        buttonsPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        add(buttonsPanel);
    }

    private void zoomChosen(ChangeEvent e) {
        int reqSize = slider.getValue();
        localProperties.setProperty("cellSize", String.valueOf(reqSize));
        System.out.println(String.format("Setting zoom to %d", reqSize));
        if (terrainFrame != null) {
            terrainFrame.setCellSize(reqSize);
            terrainFrame.repaint();
            terrainFrame.revalidate();
        }
        pack();
    }

    private void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "reset" : {
                logger.fine("Application reset");
                if (terrainFrame != null)
                    terrainFrame.dispose();
                xCountChoice.setSelectedItem(Integer.parseInt(localProperties.getProperty("XCount")));
                yCountChoice.setSelectedItem(Integer.parseInt(localProperties.getProperty("YCount")));
                slider.setValue(Integer.parseInt(localProperties.getProperty("cellSize")));
                pack(); // Resize the window to fit contents
                break;
            }
            case "createWindow": {
                if (terrainFrame != null) terrainFrame.dispose();
                terrainFrame = new TerrainFrame(localProperties, this, logger);
                startButton.setEnabled(true);
                randomizeFireButton.setEnabled(true);
                randomizeTerrainButton.setEnabled(true);
                saveTerrain.setEnabled(true);
                break;
            }
            case "setXCount": {
                localProperties.setProperty("XCount", Objects.requireNonNull(xCountChoice.getSelectedItem()).toString());
                logger.config("XCount changed to "+localProperties.getProperty("XCount"));
                break;
            }
            case "setYCount": {
                localProperties.setProperty("YCount", Objects.requireNonNull(yCountChoice.getSelectedItem()).toString());
                logger.config("YCount changed to "+localProperties.getProperty("YCount"));
                break;
            }
            case "start": {
                if (! terrainFrame.isRunning()) {
                    terrainFrame.startIteration();
                    startButton.setText("Stop");
                } else {
                    terrainFrame.stopIteration();
                    startButton.setText("Start");
                }
                break;
            }
            case "randomizeFire": {
                terrainFrame.randomizeFire();
                break;
            }
            case "randomizeTerrain": {
                terrainFrame.randomizeTerrain();
                break;
            }
            case "saveTerrain": {
                try {
                    fileChooser.showSaveDialog(this);
                    terrainFrame.saveTerrain(fileChooser.getSelectedFile().getAbsolutePath());
                } catch (IOException e2) {
                    logger.log(Level.SEVERE, e2.getMessage(), e2);
                }
                break;
            }
            case "loadTerrain": {
                try {
                    fileChooser.showOpenDialog(this);
                    terrainFrame.loadTerrain(fileChooser.getSelectedFile().getAbsolutePath());
                } catch (IOException e2) {
                    logger.log(Level.SEVERE, e2.getMessage(), e2);
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    private void loadProperties() {
        try {
            InputStream defaultPreferencesFile = getClass().getResourceAsStream("default.cfg");
            localProperties.load(defaultPreferencesFile);
            defaultPreferencesFile.close();
        } catch (IOException e) {
            logger.severe("No default config file found: "+e.getMessage());
        }

        try {
            FileInputStream userPreferencesFile = new FileInputStream("config.cfg");
            localProperties.load(userPreferencesFile);
            userPreferencesFile.close();
        } catch (IOException e) {
            logger.warning("No user config file found: "+e.getMessage());
        }
    }

    void terrainFrameClosed() {
        startButton.setText("Start");
        startButton.setEnabled(false);
        randomizeFireButton.setEnabled(false);
    }

}

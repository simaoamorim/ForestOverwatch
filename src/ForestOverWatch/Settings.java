package ForestOverWatch;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public class Settings extends JFrame {private JComboBox<Integer> xCountChoice;
    private JComboBox<Integer> yCountChoice;
    private JButton newWindowButton;
    private JButton resetButton;
    private JButton startButton;
    private JButton randomizeButton;
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
        yCountChoice = new JComboBox<>(TerrainFrame.Sizes);
        yCountChoice.setFocusable(false);
        yCountChoice.setActionCommand("setYCount");
        yCountChoice.addActionListener(this::actionPerformed);
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
        randomizeButton = new JButton("Randomize fire");
        randomizeButton.setActionCommand("randomizeFire");
        randomizeButton.addActionListener(this::actionPerformed);
        randomizeButton.setFocusable(false);
        randomizeButton.setEnabled(false);
        slider = new JSlider(JSlider.HORIZONTAL);
        slider.setMinimum(1);
        slider.setMaximum(25);
        slider.setValue(10);
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
        buttonsPanel.add(randomizeButton);
//        buttonsPanel.add(new JSeparator(JSeparator.HORIZONTAL));
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
                logger.fine("Application reset");
                if (terrainFrame != null)
                    terrainFrame.dispose();
                xCountChoice.setSelectedItem(50);
                yCountChoice.setSelectedItem(50);
                slider.setValue(10);
                pack(); // Resize the window to fit contents
                break;
            }
            case "createWindow": {
                if (terrainFrame != null) terrainFrame.dispose();
                terrainFrame = new TerrainFrame(localProperties, this);
                startButton.setEnabled(true);
                randomizeButton.setEnabled(true);
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

}

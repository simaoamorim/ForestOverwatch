import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

class GUI extends JFrame {
    private Grid grid;
    private JSlider slider;
    private JPanel panel2;
    private static final Dimension frameSize = new Dimension(700,600);
    private static final Dimension maxFrameSize = Toolkit.getDefaultToolkit().getScreenSize();
    private JComboBox<Double> xCountChoice;
    private JComboBox<Double> yCountChoice;
    private JButton startButton;
    private JButton resetButton;
    private Timer iterationTimer;
    private static final int timeStep = 100; // Time in ms

    GUI() {
        setPreferredSize(frameSize);
        setMinimumSize(frameSize);
        setMaximumSize(maxFrameSize);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
        pack();
//        setMinimumSize(getSize());
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
        iterationTimer = new Timer(timeStep,this::timerHandler);
        iterationTimer.stop();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        grid = new Grid();
        panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel2.add(grid);
        JScrollPane mainPanel = new JScrollPane(panel2);
        add(mainPanel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        xCountChoice = new JComboBox<>(Grid.Sizes);
        xCountChoice.setFocusable(false);
        xCountChoice.setActionCommand("setXCount");
        xCountChoice.addActionListener(this::actionPerformed);
        yCountChoice = new JComboBox<>(Grid.Sizes);
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
        grid.setCellSize(reqSize);
        grid.repaint();
        grid.revalidate();
        panel2.setPreferredSize(
                new Dimension(
                        (int)grid.getPreferredSize().getWidth()+5,
                        (int)grid.getPreferredSize().getHeight()+5
                )
        );
    }

    private void timerHandler(ActionEvent e) {
        grid.iteration();
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
                grid.reset();
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
                grid.setXCount((double) xCountChoice.getSelectedItem());
//                grid.revalidate();
                grid.repaint();
//                this.revalidate();
//                this.repaint();
//                this.pack();
                break;
            }
            case "setYCount": {
                System.out.println("Changed Y count");
                grid.setYCount((double) yCountChoice.getSelectedItem());
//                grid.revalidate();
                grid.repaint();
//                this.revalidate();
//                this.repaint();
//                this.pack();
                break;
            }
            default: {
                break;
            }
        }
    }
}

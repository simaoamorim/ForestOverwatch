package ForestOverwatch;

import javax.swing.*;
import java.awt.*;

public class BaseGridFrame extends JFrame {
    private static final Dimension frameSize = new Dimension(700,600);
    private static final Dimension maxFrameSize = Toolkit.getDefaultToolkit().getScreenSize();

    public BaseGridFrame() {
        setPreferredSize(frameSize);
        setMinimumSize(frameSize);
        setMaximumSize(maxFrameSize);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

}

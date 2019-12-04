import javax.swing.*;
import java.awt.*;

class TerrainGrid extends JComponent {
    private int cellSize = 10;
    private static final int margin = 1;
    static Integer[] Sizes = {50,100,200,500,1000};
    private int XCount = 50;
    private int YCount = 50;
    private boolean firstIteration = true;

    TerrainGrid() {
        setFocusable(true);
        this.setPreferredSize(
                new Dimension(
                        (XCount *cellSize)+(2*margin),
                        (YCount *cellSize)+(2*margin)
                )
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println(String.format("Cell size: %dx%d", cellSize, cellSize));
        System.out.printf("Cell Count: %dx%d\n", XCount, YCount);
        // TODO: Paint cells with appropriate colors
        int _width = (XCount * cellSize);
        int _height = (YCount * cellSize);
        this.setPreferredSize(new Dimension( (_width+(2*margin)), (_height+(2*margin))));
        g.drawRect( margin, margin, _width, _height);
        for (int x = margin; x <= _width; x += cellSize) {
            g.drawLine(x, margin, x, (_height+margin));
        }
        for (int y = margin; y <= _height; y += cellSize) {
            g.drawLine(margin, y, (_width+margin), y);
        }
    }

    void iteration() {
        if (firstIteration) {
            firstIteration = false;
        }
        repaint();
        requestFocus();
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

    void setXCount(int count) {
        XCount = count;
        repaint();
    }

    void setYCount(int count) {
        YCount = count;
        repaint();
    }

    void reset() {
        repaint();
        firstIteration = true;
    }
}
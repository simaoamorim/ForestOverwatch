import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class TerrainGrid extends JComponent {
    private int cellSize = 10;
    private static final double margin = 1.0;
    static Double []Sizes = {50.0,100.0,200.0,500.0,1000.0};
    private double XCount = 50.0;
    private double YCount = 50.0;
    private boolean firstIteration = true;

    TerrainGrid() {
        setFocusable(true);
        this.setPreferredSize(
                new Dimension(
                        (int)((XCount *cellSize)+(2*margin)),
                        (int)((YCount *cellSize)+(2*margin))
                )
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println(String.format("Cell size: %dx%d", cellSize, cellSize));
        System.out.printf("Cell Count: %dx%d\n", (int)XCount, (int)YCount);
//        for (Point fillCell : snake.getBody()) {
//            int cellX = (int)(margin + (fillCell.x * cellWidth));
//            int cellY = (int)(margin + (fillCell.y * cellHeight));
//            g.setColor(Color.BLACK);
//            g.fillRect(cellX, cellY, (int)cellWidth, (int)cellHeight);
//        }
        double _width = (XCount * cellSize);
        double _height = (YCount * cellSize);
        this.setPreferredSize(new Dimension((int) (_width+(2*margin)), (int)(_height+(2*margin))));
        g.drawRect( (int) margin, (int) margin, (int) _width, (int)_height);
        for (double x = margin; x <= _width; x += cellSize) {
            g.drawLine((int) x, (int) margin, (int) x, (int) (_height+margin));
        }
        for (double y = margin; y <= _height; y += cellSize) {
            g.drawLine((int) margin, (int) y, (int) (_width+margin), (int) y);
        }
    }

    void iteration() {
        if (firstIteration) {
            firstIteration = false;
//            System.out.printf("First direction: %s\n", nextDirection.toString());
        }
//        snake.move(nextDirection);
        repaint();
        requestFocus();
    }

    void setCellSize(int size) {
        cellSize = size;
        this.setPreferredSize(
                new Dimension(
                        (int)((XCount *cellSize)+(2*margin)),
                        (int)((YCount *cellSize)+(2*margin))
                )
        );
    }

    void setXCount(double count) {
        XCount = count;
        repaint();
    }

    void setYCount(double count) {
        YCount = count;
        repaint();
    }

    void reset() {
        repaint();
        firstIteration = true;
    }

    // KeyListener methods

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        /*int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_LEFT: {// Left arrow
                System.out.println("Left arrow clicked");
                nextDirection = Snake.Direction.WEST;
                break;
            }
            case KeyEvent.VK_UP: {// Up arrow
                System.out.println("Up arrow clicked");
                nextDirection = Snake.Direction.NORTH;
                break;
            }
            case KeyEvent.VK_RIGHT: {// Right arrow
                System.out.println("Right arrow clicked");
                nextDirection = Snake.Direction.EAST;
                break;
            }
            case KeyEvent.VK_DOWN: {// Down arrow
                System.out.println("Down arrow clicked");
                nextDirection = Snake.Direction.SOUTH;
                break;
            }
            case KeyEvent.VK_ESCAPE: {
                gui.startButton.doClick();
                break;
            }
            case KeyEvent.VK_R: {
                if (gui.resetButton.isEnabled()) {
                    gui.resetButton.doClick();
                }
                break;
            }
            default : break;
        }*/
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
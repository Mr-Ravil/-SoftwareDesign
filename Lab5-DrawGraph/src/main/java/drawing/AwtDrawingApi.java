package drawing;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class AwtDrawingApi implements DrawingApi {
    private final long drawingAreaWidth;
    private final long drawingAreaHeight;
    private final List<Ellipse2D> ellipses;
    private final List<Line2D> lines;

    public AwtDrawingApi(long drawingAreaWidth, long drawingAreaHeight) {
        this.drawingAreaWidth = drawingAreaWidth;
        this.drawingAreaHeight = drawingAreaHeight;
        ellipses = new ArrayList<>();
        lines = new ArrayList<>();
    }

    public long getDrawingAreaWidth() {
        return drawingAreaWidth;
    }

    public long getDrawingAreaHeight() {
        return drawingAreaHeight;
    }

    public void drawCircle(Point2D centre, double radius) {
        ellipses.add(new Ellipse2D.Double(centre.getX() - radius, centre.getY() - radius,
                2 * radius, 2 * radius));
    }

    public void drawLine(Point2D from, Point2D to) {
        lines.add(new Line2D.Double(from.getX(), from.getY(), to.getX(), to.getY()));
    }

    public void show() {
        Frame frame = new AwtDrawingFrame();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        frame.setSize((int) drawingAreaWidth, (int) drawingAreaHeight);
        frame.setVisible(true);
    }

    private class AwtDrawingFrame extends Frame {

        @Override
        public void paint(Graphics g) {
            Graphics2D ga = (Graphics2D) g;
            ellipses.forEach(ga::fill);
            lines.forEach(ga::draw);
        }
    }


}

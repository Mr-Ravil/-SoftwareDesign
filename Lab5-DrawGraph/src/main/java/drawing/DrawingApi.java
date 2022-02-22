package drawing;

public interface DrawingApi {
    long getDrawingAreaWidth();
    long getDrawingAreaHeight();
    void drawCircle(Point2D centre, double radius);
    void drawLine(Point2D from, Point2D to);
    void show();
}
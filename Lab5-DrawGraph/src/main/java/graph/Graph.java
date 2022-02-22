package graph;

import drawing.DrawingApi;
import drawing.Point2D;

public abstract class Graph {
    protected int size;
    private final double VERTEXES_RADIUS_RATIO = 0.05;
    private final double CIRCLE_RADIUS_RATIO = 0.7;

    /**
     * Bridge to drawing api
     */
    protected DrawingApi drawingApi;

    public Graph(DrawingApi drawingApi) {
        this.drawingApi = drawingApi;
    }

    public void drawGraph() {
        drawVertexes();
        drawEdges();
        drawingApi.show();
    }

    private void drawVertexes() {
        double vertexesRadius = Math.min(drawingApi.getDrawingAreaHeight(), drawingApi.getDrawingAreaWidth()) *
                VERTEXES_RADIUS_RATIO;
        for (int i = 0; i < size; i++) {
            drawingApi.drawCircle(getPoint(i), vertexesRadius);
        }
    }

    protected abstract void drawEdges();

    protected Point2D getPoint(int vertex) {
        Point2D centre = new Point2D((double) drawingApi.getDrawingAreaWidth() / 2,
                (double) drawingApi.getDrawingAreaHeight() / 2);

        double circleRadius = Math.min(centre.getX(), centre.getY()) * CIRCLE_RADIUS_RATIO;
        double theta = (2 * Math.PI) / size;
        double x = circleRadius * Math.cos(theta * vertex) + centre.getX();
        double y = circleRadius * Math.sin(theta * vertex) + centre.getY();

        return new Point2D(x, y);
    }
}
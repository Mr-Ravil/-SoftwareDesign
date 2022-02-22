package graph;

import drawing.DrawingApi;

import java.util.List;

public class ListGraph extends Graph {
    protected final List<Edge> edges;

    public ListGraph(DrawingApi drawingApi, int size, List<Edge> edges) {
        super(drawingApi);
        this.size = size;
        this.edges = edges;
    }

    protected void drawEdges() {
        for (Edge edge : edges) {
            drawingApi.drawLine(getPoint(edge.getFrom()), getPoint(edge.getTo()));
        }
    }
}

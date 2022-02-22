package graph;

import drawing.DrawingApi;

import java.util.List;

public class ListGraph extends Graph {
    protected final List<Edge> edges;

    public ListGraph(DrawingApi drawingApi, List<Edge> edges) {
        super(drawingApi);
        this.edges = edges;
        this.size=edges.size();
    }

    protected void drawEdges() {
        for (Edge edge : edges) {
            drawingApi.drawLine(getPoint(edge.getFrom()), getPoint(edge.getTo()));
        }
    }
}

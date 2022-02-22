package graph;

import drawing.DrawingApi;

public class MatrixGraph extends Graph {
    private final Boolean[][] matrix;

    public MatrixGraph(DrawingApi drawingApi, Boolean[][] matrix) {
        super(drawingApi);
        this.matrix = matrix;
        this.size = matrix.length;
    }

    protected void drawEdges() {
        for (int from = 0; from < size; from++) {
            for (int to = 0; to < from; to++) {
                if (matrix[from][to]) {
                    drawingApi.drawLine(getPoint(from), getPoint(to));
                }
            }
        }
    }
}

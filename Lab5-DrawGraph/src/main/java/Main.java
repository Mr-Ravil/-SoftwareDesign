import drawing.AwtDrawingApi;
import drawing.DrawingApi;
import drawing.JavaFxDrawingApi;
import graph.Edge;
import graph.Graph;
import graph.ListGraph;
import graph.MatrixGraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    private static final long DRAWING_AREA_WIDTH = 500;
    private static final long DRAWING_AREA_HEIGHT = 500;

    private static List<Edge> readEdges(BufferedReader bufferedReader) {
        return bufferedReader.lines().map(String::trim).filter(line -> !line.isEmpty())
                .map(line -> Arrays.stream(line.split("\\s+"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()))
                .map(list -> new Edge(list.get(0), list.get(1)))
                .collect(Collectors.toList());
    }

    private static Boolean[][] readMatrix(BufferedReader bufferedReader) {
        return bufferedReader.lines().map(String::trim).filter(line -> !line.isEmpty())
                .map(line -> Arrays.stream(line.split("\\s+"))
                .map(a -> Objects.equals(a, "1")).toArray(Boolean[]::new)).toArray(Boolean[][]::new);
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            throw new IllegalArgumentException("Usage : awt|javafx list|matrix <fileName>");
        }
        String drawingApiType = args[0];
        String graphType = args[1];
        String fileName = args[2];

        DrawingApi drawingApi;
        switch (drawingApiType) {
            case "awt":
                drawingApi = new AwtDrawingApi(DRAWING_AREA_WIDTH, DRAWING_AREA_HEIGHT);
                break;
            case "javafx":
                drawingApi = new JavaFxDrawingApi(DRAWING_AREA_WIDTH, DRAWING_AREA_HEIGHT);
                break;
            default:
                throw new IllegalArgumentException("Unknown drawing api:" + drawingApiType);
        }


        BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(fileName));

        Graph graph;
        switch (graphType) {
            case "list":
                graph = new ListGraph(drawingApi, readEdges(bufferedReader));
                break;
            case "matrix":
                graph = new MatrixGraph(drawingApi, readMatrix(bufferedReader));
                break;
            default:
                throw new IllegalArgumentException("Unknown graph type:" + graphType);
        }

        graph.drawGraph();
    }
}

package drawing;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class JavaFxDrawingApi implements DrawingApi {
    private static long drawingAreaWidth;
    private static long drawingAreaHeight;
    private static List<Circle> circles = new ArrayList<>();
    private static List<Line> lines = new ArrayList<>();

    public JavaFxDrawingApi(long drawingAreaWidth, long drawingAreaHeight) {
        JavaFxDrawingApi.drawingAreaWidth = drawingAreaWidth;
        JavaFxDrawingApi.drawingAreaHeight = drawingAreaHeight;
        circles = new ArrayList<>();
        lines = new ArrayList<>();
    }

    @Override
    public long getDrawingAreaWidth() {
        return drawingAreaWidth;
    }

    @Override
    public long getDrawingAreaHeight() {
        return drawingAreaHeight;
    }

    @Override
    public void drawCircle(Point2D centre, double radius) {
        circles.add(new Circle(centre.getX() - radius, centre.getY() - radius, 2 * radius));
    }

    @Override
    public void drawLine(Point2D from, Point2D to) {
        lines.add(new Line(from.getX(), from.getY(), to.getX(), to.getY()));
    }

    public void show() {
        FxDrawer.launch(FxDrawer.class);
    }

    public static class FxDrawer extends Application {

        @Override
        public void start(Stage primaryStage) {
            Group root = new Group();
            Canvas canvas = new Canvas(drawingAreaWidth, drawingAreaHeight);
            GraphicsContext gc = canvas.getGraphicsContext2D();

            circles.forEach(circle -> gc.fillOval(circle.getCenterX(), circle.getCenterY(),
                    circle.getRadius(), circle.getRadius()));
            lines.forEach(line -> gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));

            root.getChildren().add(canvas);
            primaryStage.setScene(new Scene(root, Color.WHITE));
            primaryStage.show();
        }
    }
}

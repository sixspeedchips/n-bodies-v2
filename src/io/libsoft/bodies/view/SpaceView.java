package io.libsoft.bodies.view;

import io.libsoft.bodies.model.Body;
import io.libsoft.bodies.model.Space;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

public class SpaceView extends Canvas {

  private static final double DISPLAY_SIZE = 1;
  private final int ARR_SIZE = 4;
  private Space space;
  private boolean bound;
  private GraphicsContext context;

  @Override
  public boolean isResizable() {
    if (!bound) {
      widthProperty().bind(((Pane) getParent()).widthProperty());
      heightProperty().bind(((Pane) getParent()).heightProperty());
      bound = true;
    }
    return true;
  }

  @Override
  public void resize(double width, double height) {
    super.resize(width, height);
    if (space != null) {
      draw(0);
    }
  }

  public void setSpace(Space space) {
    this.space = space;
  }

  public void clear() {
    context.clearRect(0, 0, getWidth(), getHeight());
    draw(0);
  }

  public void draw(int index) {
    if (space != null) {
      context = getGraphicsContext2D();
//      context.clearRect(0, 0, getWidth(), getHeight());
      for (Body body : space.getStates().get(index).getBodies()) {
        context.setFill(Color.hsb(body.getVelocity().getMagnitude()*255, .7, 1));
//        context.setFill(Color.WHITE);
        if (body.inCollision()){
          context.setFill(Color.RED);
        }
        double size = DISPLAY_SIZE * body.getRadius();
        context.fillOval(body.getX() - size / 2, body.getY() - size / 2, size, size);
      }
    }
  }

  private void drawVectors(Body body, double size) {
    drawVector(body.getX() + size / 2, body.getY() + size / 2,
        (body.getX() + 400 * body.getVelocity().getX() + size / 2), body.getY() + size / 2);
    drawVector(body.getX() + size / 2, body.getY() + size / 2, body.getX() + size / 2,
        (body.getY() + 400 * body.getVelocity().getY() + size / 2));
  }

  private void drawVector(double x1, double y1, double x2, double y2) {
    context.setFill(Color.GREEN);

    double dx = x2 - x1, dy = y2 - y1;
    double angle = Math.atan2(dy, dx);
    int len = (int) Math.sqrt(dx * dx + dy * dy);

    Transform transform = Transform.translate(x1, y1);
    transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
    context.setTransform(new Affine(transform));
    context.strokeLine(0, 0, len, 0);

    context.fillPolygon(new double[]{len, len - ARR_SIZE, len - ARR_SIZE, len},
        new double[]{0, -ARR_SIZE, ARR_SIZE, 0},
        4);
    context.setTransform(new Affine(Transform.translate(0, 0)));

  }


}

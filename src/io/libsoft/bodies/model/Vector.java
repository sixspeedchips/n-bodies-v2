package io.libsoft.bodies.model;

import java.util.List;
import java.util.Random;

public final class Vector {

  private static Random random = new Random();
  private double x;
  private double y;


  private Vector(double x, double y) {
    this.x = x;
    this.y = y;
  }

  private Vector(Builder builder) {
    setX(builder.x);
    setY(builder.y);
  }

  public static Vector set(double x, double y) {
    return new Vector(x, y);
  }

  public static Vector random(double lowerBound, double upperBound) {
    return set(random.nextDouble() * (upperBound - lowerBound) + lowerBound,
        random.nextDouble() * (upperBound - lowerBound) + lowerBound);
  }

  public static Vector rotateInner(double x, double y, double height, double width, double scale) {
    x = -x + width / 2;
    y = y - height / 2;
    double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    return Vector.set(-(y) * scale / r, -(x) * scale / r);
  }

  public static Vector rotateOuter(double x, double y, double height, double width, double scale) {
    x = -x + width / 2;
    y = y - height / 2;
    double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    return Vector.set(-y * scale, -x * scale);
  }

  public static Vector random(double range) {
    return set(random.nextDouble() * range - range / 2,
        random.nextDouble() * range - range / 2);
  }

  public static Vector EMPTY() {
    return set(0, 0);
  }

  public static Vector gravity(Body current, List<Body> bodies) {
    Vector v = Vector.EMPTY();
    for (Body other : bodies) {
      if (!current.collided(other)) {
        v.add(Fg(current, other));
      }
    }
    return v;
  }

  private static Vector Fg(Body body1, Body body2) {
    Vector vector = Vector.EMPTY();
    double mass = body2.getMass();
    double xDisp = body1.xDisplacement(body2);
    double yDisp = body1.yDisplacement(body2);
    double r = body1.distance(body2);
    double xHat = -(Space.GRAVITY * mass * xDisp) / (r * r);
    double yHat = -(Space.GRAVITY * mass * yDisp) / (r * r);
    vector.setX(xHat);
    vector.setY(yHat);
    return vector;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public void add(Vector v) {
    setX(getX() + v.getX());
    setY(getY() + v.getY());
  }

  public Vector addCopy(Vector v) {
    return Vector.set(getX() + v.getX(), getY() + v.getY());
  }

  public Vector copy() {
    return set(getX(), getY());
  }

  public double getMagnitude() {
    return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
  }


  public static final class Builder {

    private double x;
    private double y;

    public Builder() {
    }

    public Builder(Vector copy) {
      this.x = copy.getX();
      this.y = copy.getY();
    }

    public Builder x(double val) {
      x = val;
      return this;
    }

    public Builder y(double val) {
      y = val;
      return this;
    }

    public Vector build() {
      return new Vector(this);
    }
  }
}

package io.libsoft.bodies.model;

import java.util.List;
import java.util.Objects;

public class Body {

  private double x;
  private double y;
  private Vector vector;
  private double mass;
  private double density;
  private double radius;
  private boolean inCollision;

  private Body(double x, double y, double mass, double density, Vector vector) {
    this.x = x;
    this.y = y;
    this.density = density;
    this.vector = vector;
    this.mass = mass;
    radius = Math.sqrt(mass / (density * Math.PI));


  }

  private Body(Builder builder) {
    x = builder.x;
    y = builder.y;
    vector = builder.velocity;
    mass = builder.mass;
    density = builder.density;
    radius = Math.sqrt(builder.mass / (builder.density * Math.PI));
  }

  public static Body get(double x, double y, double mass, double density, Vector vector) {
    return new Body(x, y, mass, density, vector);
  }

  public Body apply(Vector v) {
    v.setX(v.getX() * .2);
    v.setY(v.getY() * .2);

    double nextX = x + vector.getX() + v.getX();
    double nextY = y + vector.getY() + v.getY();
    return Body.get(nextX, nextY, mass, density, vector.addCopy(v));
  }

  public Vector getVelocity() {
    return vector;
  }

  public double xDisplacement(Body o1) {
    return getX() - o1.getX();
  }

  public double yDisplacement(Body o1) {
    return getY() - o1.getY();
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getMass() {
    return mass;
  }

  public Body copy() {
    return new Body(getX(), getY(), getMass(), getDensity(), getVelocity().copy());
  }


  public void inelasticCollision(Body other) {
    double coefficient = Space.COEFFICIENT;
    Vector v = Vector.EMPTY();
    Vector vOther = Vector.EMPTY();

    double vXNext =
        (coefficient * other.mass * (other.vector.getX() - vector.getX()) + (mass * vector.getX())
            + (other.mass * other.vector.getX())) / (mass + other.mass);
    double vYNext =
        (coefficient * other.mass * (other.vector.getY() - vector.getY()) + (mass * vector.getY())
            + (other.mass * other.vector.getY())) / (mass + other.mass);

    double vXNextOther =
        (coefficient * mass * (vector.getX() - other.vector.getX()) + (other.mass * other.vector
            .getX())
            + (mass * vector.getX())) / (mass + other.mass);
    double vYNextOther =
        (coefficient * mass * (vector.getY() - other.vector.getY()) + (other.mass * other.vector
            .getY())
            + (mass * vector.getY())) / (mass + other.mass);

    v.setX(vXNext);
    v.setY(vYNext);
    vOther.setX(vXNextOther);
    vOther.setY(vYNextOther);
    vector = v;
    other.vector = vOther;
  }

  public double getDensity() {
    return density;
  }

  public double getRadius() {
    return radius;
  }


  public Body collisions(List<Body> others) {
    for (Body other : others) {
      if (collided(other)) {
        inelasticCollision(other);
      }
    }
    return this;
  }

  public boolean collided(Body other) {
    return (radius + other.getRadius()) * (.8) > distance(other);
  }

  public double distance(Body other) {
    return Math.sqrt(Math.pow(xDisplacement(other), 2) + Math.pow(yDisplacement(other), 2));
  }

  public boolean inCollision() {
    return inCollision;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Body body = (Body) o;
    return Double.compare(body.x, x) == 0 &&
        Double.compare(body.y, y) == 0 &&
        Double.compare(body.mass, mass) == 0 &&
        Double.compare(body.density, density) == 0 &&
        vector.equals(body.vector);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, vector, mass, density);
  }

  @Override
  public String toString() {
    return "Body{" +
        "x=" + x +
        ", y=" + y +
        ", curr=" + vector +
        ", mass=" + mass +
        '}';
  }

  public static final class Builder {

    private double x = 0.0;

    private double y = 0.0;
    private Vector velocity = Vector.EMPTY();
    private double mass = 1.0;
    private double density = 1.0;

    public Builder x(double val) {
      x = val;
      return this;
    }

    public Builder y(double val) {
      y = val;
      return this;
    }

    public Builder velocity(Vector val) {
      velocity = val;
      return this;
    }

    public Builder mass(double val) {
      mass = val;
      return this;
    }

    public Builder density(double val) {
      density = val;
      return this;
    }


    public Body build() {
      return new Body(this);
    }

  }
}

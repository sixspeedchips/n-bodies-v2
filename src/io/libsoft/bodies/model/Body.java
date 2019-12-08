package io.libsoft.bodies.model;

import java.util.LinkedList;
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

  public double getDensity() {
    return density;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }
    Body other = (Body) obj;
    return x == other.getX() && y == other.getY();
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

  public double getRadius() {
    return radius;
  }


  public Body collisions(List<Body> others) {
//    List<Body> removal = new LinkedList<>();
    for (Body other : others) {
      if (collided(other)) {
        inCollision = true;
        other.inCollision = true;
//        removal.add(other);
//        body = Body.get((body.getX() + other.getX())/2,
//            (body.getY()+other.getY())/2,
//            body.mass + other.getMass(),
//            body.getDensity(),
//            body.vector.addCopy(other.vector));
      }
    }
//    others.removeAll(removal);
    return this;
  }

  private boolean collided(Body other) {
    return (radius + other.getRadius()) > distance(other);
  }

  public double distance(Body other){
    return Math.sqrt(Math.pow(xDisplacement(other), 2) + Math.pow(yDisplacement(other), 2));
  }

  public boolean inCollision() {
    return inCollision;
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

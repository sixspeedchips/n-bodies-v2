package io.libsoft.bodies.model;

import io.libsoft.bodies.model.Space.State;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Space implements Iterator<State> {


  public static double GRAVITY = .00001;
  public static double COEFFICIENT = .3;
  private List<State> states;
  private State currentState;
  private int numBodies;
  private double height;
  private double width;
  private double bounds;

  public Space(int numBodies, double height, double width) {
    this.numBodies = numBodies;
    this.height = height;
    this.width = width;
    init();
  }

  public static void setGRAVITY(double GRAVITY) {
    Space.GRAVITY = GRAVITY;
  }

  public List<State> getStates() {
    return states;
  }

  public State getCurrentState() {
    return currentState;
  }

  @Override
  public boolean hasNext() {
    return true;
  }

  @Override
  public State next() {
    currentState = currentState.step();
//    states.add(currentState);
    return currentState;
  }

  public void init() {
    currentState = State.nBody(numBodies, height, width);
    states = new LinkedList<>();
    states.add(currentState);
  }

  public static class State {

    private List<Body> bodies;
    private static Random rng;

    private State(List<Body> bodies) {
      this.bodies = bodies;
    }


    public static State nBody(int n, double height, double width) {
      List<Body> bodies = new LinkedList<>();
      rng = new Random();
      bodies.add(new Body.Builder()
          .x(width / 2)
          .y(height / 2)
//          .velocity(Vector.set(.002,0))
          .mass(100000).density(100).build());
      double x, y;
      for (int i = 0; i < n; i++) {
        x = rng.nextDouble() * width;
        y = rng.nextDouble() * height;

        bodies.add(
            new Body.Builder()
                .x(x)
                .y(y)
                .velocity(Vector.rotateInner(x, y, height, width, 5e-1))
                .mass(rng.nextDouble() * 20)
                .density(.5)
                .build());
      }
      return new State(bodies);
    }


    private State step() {

      List<Body> nextBodies = new LinkedList<>();
      List<Body> others = new LinkedList<>(bodies);
      Body curr;
      for (int i = 0; i < bodies.size(); i++) {
        curr = others.remove(i);
        nextBodies.add(curr.apply(Vector.gravity(curr, others)));
        others.add(i, curr);
      }
      //resolve collisions
      others = new LinkedList<>(nextBodies);
      while (others.size() > 0) {

        curr = others.remove(0);
        curr.collisions(others);
      }

      return new State(nextBodies);
    }

    public List<Body> getBodies() {
      return bodies;
    }

    @Override
    public String toString() {
      return "State{" +
          "bodies=" + bodies +
          '}';
    }


  }


}

package io.libsoft.bodies.model;

import io.libsoft.bodies.model.Space.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Space implements Iterator<State> {


  public static double GRAVITY = .001;
  private List<State> states;
  private State currentState;
  private int numBodies;
  private double bounds;

  public Space(int numBodies, double bounds) {
    this.numBodies = numBodies;
    this.bounds = bounds;
    init();
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
    states.add(currentState);
    return currentState;
  }

  public void init() {
    this.currentState = State.nBody(numBodies,bounds);
    states = new LinkedList<>(Collections.singletonList(currentState));
  }

  public static void setGRAVITY(double GRAVITY) {
    Space.GRAVITY = GRAVITY;
  }

  public static class State {

    private List<Body> bodies;

    private State(List<Body> bodies) {
      this.bodies = bodies;
    }


    public static State nBody(int n, double bounds) {
      List<Body> bodies = new LinkedList<>();
      Random rng = new Random();
      bodies.add(new Body.Builder().x(rng.nextDouble()*bounds)
          .y(rng.nextDouble()*bounds)
          .velocity(Vector.set(.005,0))
          .mass(200).density(1).build());
      for (int i = 0; i < n; i++) {
        bodies.add(
            new Body.Builder()
                .x(rng.nextDouble()*bounds)
                .y(rng.nextDouble()*bounds)
//                .x(300+rng.nextDouble())
//                .y(300+rng.nextDouble())
                .velocity(Vector.RANDOM(1e-2))
                .mass(rng.nextDouble())
                .density(1e-1)
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
        nextBodies.add(curr.apply(Vector.GRAVITY(curr, others)));
        others.add(i, curr);
      }
      //resolve collisions
      others = new LinkedList<>(nextBodies);
//      HashSet<Body> resolved = new HashSet<>();
      Body res;
       while (others.size()>0){
        curr = others.remove(0);
        res = curr.collisions(others);
//        resolved.add(res);
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

package io.libsoft.bodies.controller;

import io.libsoft.bodies.model.Space;
import io.libsoft.bodies.view.SpaceView;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleButton;

public class SpaceController {

  private static final int MS_SLEEP = 10;
  private static int ITERATIONS_PER_SLEEP = 50;
  @FXML public Spinner<Double> spinner;
  @FXML private Slider slider;
  @FXML private Button reset;
  @FXML private ToggleButton toggleRun;
  @FXML private SpaceView spaceView;
  @FXML private ResourceBundle resources;
  private Space space;
  private Updater updater;
  private boolean running;
  private int index;

  public static void setIterationsPerSleep(int iterationsPerSleep) {
    ITERATIONS_PER_SLEEP = iterationsPerSleep;
  }

  @FXML
  private void initialize() {
    space = new Space(200, 1000, 1200);
    spaceView.setSpace(space);
    updater = new Updater();
    reset(null);
    slider.valueProperty().addListener((observable, oldValue, newValue) -> {
//      updateView(newValue.intValue());
      setIterationsPerSleep(newValue.intValue());
    });
    slider.setMin(1);
    slider.setValue(10);
    slider.setMax(100);
    setIterationsPerSleep(((int) slider.getValue()));

    initSpinner();

  }
  private void initSpinner(){
      spinner.setValueFactory(new SpinnerValueFactory<Double>() {
        double step = 1/2d;
        @Override
        public void decrement(int steps) {
          spinner.getValueFactory().setValue((spinner.getValue()*step));
          Space.setGRAVITY(spinner.getValue());
        }

        @Override
        public void increment(int steps) {
          spinner.getValueFactory().setValue((spinner.getValue()/step));
          Space.setGRAVITY(spinner.getValue());
        }
      });
      spinner.getValueFactory().setValue(.01);
  }

  public void stop() {
    running = false;
    updater.stop();
//    slider.setMax(index);
//    slider.setValue(index);
  }

  @FXML
  private void reset(ActionEvent actionEvent) {
    space.init();
    index = 0;
    updateView(index);
    spaceView.clear();
  }

  @FXML
  private void toggleRun(ActionEvent actionEvent) {
    if (toggleRun.isSelected()) {
      start();
    } else {
      stop();
      updateControls();
    }
  }



  private void start() {
    running = true;
    updateControls();
    new Runner().start();
    updater.start();
  }

  private void updateView(int index) {
    spaceView.draw(index);
  }

  private void updateControls() {
    if (running) {
      reset.setDisable(true);
    } else {
      reset.setDisable(false);
      if (toggleRun.isSelected()) {
        toggleRun.setSelected(false);
      }
      updateView(index);
    }
  }


  class Runner extends Thread {


    @Override
    public void run() {
      while (running) {
        for (int i = 0; i < ITERATIONS_PER_SLEEP; i++) {
          space.next();
          index++;
        }
        try {
          Thread.sleep(MS_SLEEP);
        } catch (InterruptedException ignored) {
        }
      }


    }
  }

  private class Updater extends AnimationTimer {
    @Override
    public void handle(long now) {
      updateView(index);
    }

  }
}

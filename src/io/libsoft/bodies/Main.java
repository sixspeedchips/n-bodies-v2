package io.libsoft.bodies;

import io.libsoft.bodies.controller.SpaceController;
import io.libsoft.bodies.model.Vector;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  private static final String LAYOUT_RESOURCE = "space.fxml";
  private static final String RESOURCE_BUNDLE = "strings";

  private SpaceController controller;


  public static void main(String[] args) {

    launch(args);
  }

  /**
   * Loads FXML layout, resource bundle, and icon used by application.
   *
   * @param stage primary application window.
   * @throws Exception if a required resource cannot be loaded.
   */
  @Override
  public void start(Stage stage) throws Exception {
    ClassLoader classLoader = getClass().getClassLoader();
    ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);
    FXMLLoader fxmlLoader = new FXMLLoader(classLoader.getResource(LAYOUT_RESOURCE), bundle);
    Parent root = fxmlLoader.load();
    controller = fxmlLoader.getController();
    Scene scene = new Scene(root);
    stage.setResizable(true);
    stage.setScene(scene);
    stage.sizeToScene();
    stage.show();
    setStageSize(stage, root);
    
  }


  @Override
  public void stop() throws Exception {
    controller.stop();
    super.stop();
  }

  private void setStageSize(Stage stage, Parent root) {
    Bounds bounds = root.getLayoutBounds();
    stage.setMinWidth(root.minWidth(-1) + stage.getWidth() - bounds.getWidth());
    stage.setMinHeight(root.minHeight(-1) + stage.getHeight() - bounds.getHeight());
  }

}
package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GUITest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            AnchorPane root = FXMLLoader.load(GUITest.class.getResource("/fxml/MainWindow.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("GUI Test");
            stage.setMinWidth(root.getPrefWidth());
            stage.setMinHeight(root.getPrefHeight());
            stage.show();
        } catch (Exception exception) {
            System.out.println(exception);
            exception.printStackTrace();
        }
    }
}

package ChangePassword;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ChangePasswordMain extends Application {


    public void display() throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ChangePasswordMain.class.getResource("/ChangePassword/ChangePasswordUI.fxml"));
        root = loader.load();
        root.requestLayout();
        Stage window = new Stage();
        Scene scene = new Scene(root, Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        window.setResizable(false);
        window.initStyle(StageStyle.UNDECORATED);
        scene.getStylesheets().add("/ChangePassword/ChangePasswordStyle.css");
        window.setScene(scene);
        Platform.runLater(window::showAndWait);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}

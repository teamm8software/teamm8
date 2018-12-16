package CreateTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CreateTask extends Application {

    public void display() throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/CreateTask/CreateTaskUI.fxml"));
        root = loader.load();
        root.requestLayout();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/CreateTask/CreateTaskStyle.css");
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(scene);
        Platform.runLater(window::showAndWait);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}

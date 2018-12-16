package CreateProject;

import dashboard.DashboardMain;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CProject extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception {

    }
    public void display ()throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/CreateProject/createproject.fxml"));
        root=loader.load();
        root.requestLayout();
        Stage window = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/CreateProject/cproject.css");
        window.setTitle("Create Project");
        window.initStyle(StageStyle.UNDECORATED);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setScene(scene);
        window.setX((DashboardMain.WX -window.getWidth())/2);
        window.setY((DashboardMain.WY-window.getHeight())/2);
        Platform.runLater(window::showAndWait);
    }
}

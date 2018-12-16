package CreateUserPane;


import dashboard.DashboardMain;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;


public class CreateUserPane extends Application {

    public void display ()throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/CreateUserPane/creatinguser.fxml"));
        root=loader.load();
        root.requestLayout();
        Stage window = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/CreateUserPane/userpane.css");
        window.setTitle("TeamM8 - Add User");
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(scene);
        window.setX((DashboardMain.WX -window.getWidth())/2);
        window.setY((DashboardMain.WY-window.getHeight())/2);
        Platform.runLater(window::showAndWait);
    }





    @Override
    public void start(Stage stage) throws Exception {

    }
}

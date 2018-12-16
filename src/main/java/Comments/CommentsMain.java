package Comments;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CommentsMain extends Application {

    public void display(String taskname, String username, String duedate) throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Comments/CommentsUI.fxml"));
        root = loader.load();
        root.requestLayout();
        Stage window = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/Comments/CommentsStyle.css");
        window.initStyle(StageStyle.UNDECORATED);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setScene(scene);
        CommentsController commentsController = loader.getController();
        commentsController.set_task_name_text(taskname);
        commentsController.set_user_name_text(username);
        commentsController.set_due_date_text(duedate);
        Platform.runLater(window::showAndWait);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}

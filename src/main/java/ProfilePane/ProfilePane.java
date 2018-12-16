package ProfilePane;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;

public class ProfilePane extends Application {
    @FXML private static AnchorPane rootAnchor;
    @FXML private static MaterialIconView icon_exit;
    public static ImagePattern ppfill;
public static double WHeight;
    public void display()  throws IOException{
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int w = screenSize.getSize().width;
        int h = screenSize.getSize().height;
        int x = (screenSize.width-w)/2;
        int y = (screenSize.height-h)/2;

        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ProfilePane/profileUI.fxml"));
        root=loader.load();
        root.requestLayout();
        Stage window = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/ProfilePane/profile.css");
        window.setTitle("TeamM8");
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(scene);
        window.setX(400);
        window.setY(250);
        window.initModality(Modality.APPLICATION_MODAL);


        Platform.runLater(window::showAndWait);
    }
public static double getHeight(){
        return rootAnchor.getHeight();
}


    @Override
    public void start(Stage stage) throws Exception {

    }
    public static void closeButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage)icon_exit.getScene().getWindow();
        // do what you have to do
        stage.close();
        System.out.println("Stage Closed.");
    }

}

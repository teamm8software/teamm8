package dashboard;


import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.bson.Document;
import packMongo.mongoCON;

import java.io.IOException;

import static com.mongodb.client.model.Filters.eq;

public class DashboardMain extends Application {
public static double WY,WX;
    public void display(String user_name, mongoCON connection, MongoDatabase database) throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DashboardMain.class.getResource("/dashboard/dashboardUI.fxml"));
        root = loader.load();
        DashboardController dashboardController = loader.getController();
        dashboardController.setConnection(connection);
        dashboardController.setDatabase(database);
        dashboardController.setUser_name(user_name);
        dashboardController.set_lbl_role_text(getRole(database,user_name));
        dashboardController.set_lbl_username_text(getUserName(database, user_name));
        root.requestLayout();
        Stage window = new Stage();
        window.setTitle("TeamM8 - " + user_name);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
        window.setFullScreen(false);
        window.setResizable(false);
        window.initStyle(StageStyle.UNDECORATED);
        scene.getStylesheets().add("/dashboard/dashboardStyle.css");

        window.setScene(scene);
        WY=window.getScene().getHeight();
        WX=window.getScene().getWidth();
        Platform.runLater(window::showAndWait);
    }

    @Override
    public void start(Stage stage) throws Exception {

    }

    public String getUserName(MongoDatabase database, String username){
        Document userDoc = database.getCollection("teammates").find(eq("username",username)).first();
        if (userDoc!=null){
            return userDoc.get("name").toString();
        }
        else{
            return null;
        }
    }

    public String getRole(MongoDatabase database, String username){
        Document userDoc = database.getCollection("teammates").find(eq("username",username)).first();
        if(userDoc!=null){
            Document roleDoc = database.getCollection("roles").find(eq("role_id",userDoc.get("assignment"))).first();
            return (String) roleDoc.get("role_name");
        }
        else{
            return null;
        }
    }
}

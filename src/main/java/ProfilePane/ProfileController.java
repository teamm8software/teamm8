package ProfilePane;

import com.mongodb.client.MongoDatabase;
import dashboard.DashboardController;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import login.loginController;
import org.bson.Document;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Filters.eq;

public class ProfileController implements Initializable {
    private Scene dashboardScene;
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    @FXML private AnchorPane rootAnchor;
    @FXML private GridPane gridPane;
    @FXML private Label lbl_name,lbl_surname,lbl_id,lbl_password,lbl_role,lbl_salary,lbl_username,lbl_birthdate;
    @FXML private  MaterialIconView icon_exit,empty;
    @FXML private Circle circle_pp;
    double xOffset,yOffset;
    MongoDatabase database = loginController.database;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootAnchor.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        rootAnchor.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage)rootAnchor.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX()-xOffset);
            stage.setY(mouseEvent.getScreenY()-yOffset);
        });
        icon_exit.setOnMouseClicked(mouseEvent ->{
            closeButtonAction();
            dashboardScene = DashboardController.dashboardScene;
            dashboardScene.getRoot().setEffect(null);
        } );
        Document user =database.getCollection("teammates").find(eq("username", DashboardController.USERNAME)).first();
        long id_number= user.getLong("id_number");;
        int salary;
        String name= user.getString("name"),surname = user.getString("surname"),password,birth,role;

        salary = user.getInteger("salary");
        name =user.getString("name");
         surname = user.getString("surname");
         password = user.getString("password");
        lbl_id .setText(String.valueOf(id_number) );
        lbl_name.setText(name);
        lbl_surname.setText(surname);
        lbl_salary.setText(String.valueOf(salary));
        lbl_password.setText(password);
         birth = dateFormat.format(user.get("birth"));
        lbl_birthdate.setText(birth);
        circle_pp.setFill(ProfilePane.ppfill);
         role="null";
        if(user.getInteger("assignment").equals(0)){role="Supervisor";}
        if(user.getInteger("assignment").equals(1)){role="Project Manager";}
        if(user.getInteger("assignment").equals(2)){role="Team Member";}
        lbl_role.setText(role);
        lbl_username.setText(DashboardController.USERNAME);
    }



    private   void closeButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage)icon_exit.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}

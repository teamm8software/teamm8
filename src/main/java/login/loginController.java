package login;

import ChangePassword.ChangePasswordMain;
import com.jfoenix.controls.*;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dashboard.DashboardMain;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bson.Document;
import packMongo.mongoCON;
import packMongo.teammate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Filters.eq;

public class loginController implements Initializable {

    public static mongoCON connection;
    public static   MongoDatabase database;
    public static String username;
    public static Stage loginstage;
    double xOffset, yOffset;
    @FXML
    private JFXPasswordField txt_pass;
    @FXML
    private JFXTextField txt_username;
    @FXML
    private JFXButton btn_login;
    @FXML
    private JFXButton btn_exit;
    @FXML
    private MaterialIconView icon_exit;
    @FXML
    private StackPane rootStackPane;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private MaterialIconView eyeVis;
    @FXML
    private JFXTextField txt_show_pass;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        txt_show_pass.setVisible(false);
        this.connection = new mongoCON();
        this.database = this.connection.connect(
                "mongodb+srv://proj_manager:creaturerigger@inteamate-ad9cd.gcp.mongodb.net/test?retryWrites=true",
                "users");
        btn_exit.setOnAction(actionEvent -> {
            closeButtonAction();
        });
        btn_login.setOnAction(actionEvent ->{username=txt_username.getText();loginAction(txt_username.getText(),txt_pass.getText());} );
        icon_exit.setOnMouseClicked(mouseEvent -> javafx.application.Platform.exit());
        eyeVis.setOnMouseClicked(mouseEvent -> {
            boolean is_pass_visible = txt_show_pass.isVisible();
            if(is_pass_visible){
                hidePass();
                eyeVis.setGlyphName("VISIBILITY_OFF");
            }
            else{
                showPass();
                eyeVis.setGlyphName("VISIBILITY");
            }
        });
        rootAnchorPane.setOnMousePressed(mouseEvent -> {
             xOffset = mouseEvent.getSceneX();
             yOffset = mouseEvent.getSceneY();
        });

        rootAnchorPane.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage)rootAnchorPane.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX()-xOffset);
            stage.setY(mouseEvent.getScreenY()-yOffset);
        });
        txt_username.clear();
        txt_show_pass.clear();
        txt_pass.clear();
        txt_username.setText("volkan.bakir");
        txt_pass.setText("asdf");
    }

    public void showAlert(String alertTitle, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(alertTitle);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void showPass(){
        String password = txt_pass.getText();
        txt_show_pass.setText(password);
        txt_show_pass.setVisible(true);
        txt_pass.setVisible(false);
    }

    public void hidePass(){
        txt_show_pass.setVisible(false);
        txt_pass.setVisible(true);
    }

    private Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };

    private void closeButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage) btn_exit.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    private boolean loginAction(String username, String password){
        if(!username.equals("") && !password.equals("")){
            /*mongoCON connection = new mongoCON();
            MongoDatabase mongoDatabase = connection.connect(
                    "mongodb+srv://proj_manager:creaturerigger@inteamate-ad9cd.gcp.mongodb.net/test?retryWrites=true",
                    "users");*/
            MongoCollection<Document> collection = this.database.getCollection("teammates");
            FindIterable usernameResults = collection.find(eq("username", username));
            Document userDocument = (Document) usernameResults.first();

            if(userDocument!=null) {
                String pass = (String)userDocument.get("password");
                System.out.println(pass);
            }
            FindIterable passResults = collection.find(eq("password", password));
            /*if(userDocument.isEmpty()){
                System.out.println("username has been found");
            }*/
            assert userDocument != null;
            if(userDocument.getString("username").equals(username)){
                if(userDocument.getString("password").equals(password)){
                    Boolean isRegistered = userDocument.getBoolean("registered");
                    if(isRegistered.equals(false)){
                        System.out.println("You haven't registered your account");
                        ChangePasswordMain changePasswordMain = new ChangePasswordMain();
                        loginstage = (Stage)icon_exit.getScene().getWindow();
                        txt_pass.clear();
                        txt_show_pass.clear();
                        txt_username.clear();
                        loginstage.hide();
                        try {
                            changePasswordMain.display();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        //loadDialog("Successfully logged in!");
                        System.out.println("Logged in!");
                        DashboardMain dashboardMain = new DashboardMain();
                        Stage stage = (Stage) btn_login.getScene().getWindow();
                        stage.close();
                        try {
                            dashboardMain.display(username, this.connection, this.database);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    loadDialog("Please check your password and try again.", "Wrong password.");
                    System.out.println("Wrong password!");
                }
            }
            else{
                loadDialog("User does not exist contact to system administrator.", "No user found!");
                System.out.println("User does not exist contact to system administrator.");
            }
            //results.forEach(printBlock);
        /*for (Document doc:
             collection.find()) {
            String name = (String) doc.get("username");
            String pass = (String) doc.get("password");
            System.out.println(name + ":::::" + pass);
        }*/
        }
        else{
            loadDialog("User name and password fields has to be filled before clicking on Login button!", "Required Fields!");
            System.out.println("Please be sure that you entered your username and password!");
        }
        return false;
    }

    public teammate getTeammate(MongoDatabase database, Document user){
        return new teammate(user, database);
    }


    public void loadDialog(String content, String heading){
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setBody(new Text(content));
        jfxDialogLayout.setHeading(new Text(heading));
        JFXDialog jfxDialog = new JFXDialog();
        jfxDialog.setContent(jfxDialogLayout);
        jfxDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        jfxDialog.setDialogContainer(rootStackPane);
        JFXButton confirmButton = new JFXButton();
        confirmButton.setText("Okay");
        confirmButton.setOnAction(actionEvent -> jfxDialog.close());
        jfxDialogLayout.setActions(confirmButton);
        jfxDialog.setOverlayClose(true);
        jfxDialog.show();
    }
}

package ChangePassword;

import com.jfoenix.controls.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.loginController;
import org.bson.Document;
import packMongo.mongoCON;

import java.net.URL;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Filters.eq;

public class ChangePasswordController implements Initializable {

    private mongoCON connection = loginController.connection;

    private MongoDatabase database = loginController.database;

    private String user_name = loginController.username;


    @FXML
    private StackPane rootStackPane;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private MaterialIconView icon_exit;

    @FXML
    private JFXPasswordField txt_hide_pass;

    @FXML
    private JFXTextField txt_show_pass;

    @FXML
    private MaterialIconView eyeVis_newpass;

    @FXML
    private JFXPasswordField txt_c_hide_pass;

    @FXML
    private JFXTextField txt_c_show_pass;

    @FXML
    private MaterialIconView eyeVis_conpass;

    @FXML
    private JFXButton btn_change;

    private double xOffset, yOffset;
    private Stage loginStage = loginController.loginstage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        rootAnchorPane.setOnMouseDragged(this::DragWindow);
        rootAnchorPane.setOnMousePressed(this::GetWindowLocation);
        txt_c_show_pass.setVisible(false);
        txt_show_pass.setVisible(false);

        eyeVis_newpass.setOnMouseClicked(mouseEvent -> {
            TogglePasswordVisibility(mouseEvent, txt_show_pass, txt_hide_pass, eyeVis_newpass);
        });

        eyeVis_conpass.setOnMouseClicked(mouseEvent -> {
            TogglePasswordVisibility(mouseEvent, txt_c_show_pass, txt_c_hide_pass, eyeVis_conpass);
        });

        icon_exit.setOnMouseClicked(mouseEvent -> {
            Stage window = (Stage) icon_exit.getScene().getWindow();
            window.close();
        });

        btn_change.setOnMouseClicked(mouseEvent -> {
            if(checkFields(txt_hide_pass, txt_c_hide_pass)){
                if(!user_name.equals(null)){
                    WritePassword2DB(user_name, txt_hide_pass);
                    Stage pasStage = (Stage)btn_change.getScene().getWindow();
                    loadDialog("Login with your new password please!", "New login required!", loginStage, pasStage);
                }
            }
        });

        txt_hide_pass.textProperty().addListener((observable, oldValue, newValue) -> {
            txt_show_pass.setText(newValue);
        });

        txt_show_pass.textProperty().addListener((observable, oldValue, newValue) -> {
            txt_hide_pass.setText(newValue);
        });

        txt_c_hide_pass.textProperty().addListener((observable, oldValue, newValue) -> {
            txt_c_show_pass.setText(newValue);
        });

        txt_c_show_pass.textProperty().addListener((observable, oldValue, newValue) -> {
            txt_c_hide_pass.setText(newValue);
        });

    }

    private void WritePassword2DB(String user_name, JFXPasswordField pass_field) {
        System.out.println("değişiyor");
        MongoCollection collection = database.getCollection("teammates");
        collection.updateOne(eq("username", user_name), new Document("$set", new Document("password", pass_field.getText())));
        collection.updateOne(eq("username", user_name), new Document("$set", new Document("registered", true)));
    }

    private boolean checkFields(JFXPasswordField pass1, JFXPasswordField pass2) {
        if (pass1.getText().equals("") || pass2.getText().equals("")) {
            loadDialog("You have to fill both password fields!", "Warning!");
            return false;
        } else {
            if (!pass1.getText().equals(pass2.getText())) {
                loadDialog("Passwords don't match!", "Warning!");
                return false;
            } else {
                return true;
            }
        }
    }

    public void loadDialog(String content, String heading) {
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

    public void loadDialog(String content, String heading, Stage logStage, Stage pasStage) {
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setBody(new Text(content));
        jfxDialogLayout.setHeading(new Text(heading));
        JFXDialog jfxDialog = new JFXDialog();
        jfxDialog.setContent(jfxDialogLayout);
        jfxDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        jfxDialog.setDialogContainer(rootStackPane);
        JFXButton confirmButton = new JFXButton();
        confirmButton.setText("Okay");
        confirmButton.setOnAction(actionEvent -> {
            jfxDialog.close();
            logStage.show();
            pasStage.close();
        });
        jfxDialogLayout.setActions(confirmButton);
        jfxDialog.setOverlayClose(true);
        jfxDialog.show();
    }

    private void showPass(JFXTextField txt_field, JFXPasswordField pass_field) {
        txt_field.setVisible(true);
        pass_field.setVisible(false);
    }

    private void hidePass(JFXTextField txt_field, JFXPasswordField pass_field) {
        txt_field.setVisible(false);
        pass_field.setVisible(true);
    }

    private void DragWindow(MouseEvent mouseEvent) {
        Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    private void GetWindowLocation(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    private void TogglePasswordVisibility(MouseEvent mouseEvent,
                                          JFXTextField txt_field, JFXPasswordField pass_field, MaterialIconView icon) {
        boolean is_pass_visible = txt_field.isVisible();
        if (is_pass_visible) {
            hidePass(txt_field, pass_field);
            icon.setGlyphName("VISIBILITY_OFF");
        } else {
            showPass(txt_field, pass_field);
            icon.setGlyphName("VISIBILITY");
        }
    }
}

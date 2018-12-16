package CreateTask;

import com.jfoenix.controls.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dashboard.DashboardController;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.loginController;
import org.bson.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Filters.eq;

public class CreateTaskController implements Initializable {

    private double offsetX, offsetY;

    private Scene dashboardScene;

    @FXML
    private StackPane rootStackPane;

    @FXML
    private JFXTextField txt_taskname;

    @FXML
    private JFXComboBox<String> cmb_user;

    @FXML
    private JFXComboBox<String> cmb_project;

    @FXML
    private DatePicker date_start;

    @FXML
    private DatePicker date_end;

    @FXML
    private JFXTextArea txt_description;

    @FXML
    private JFXButton btn_assign;

    @FXML
    private MaterialIconView icon_exit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        rootStackPane.setOnMousePressed(mouseEvent->{
            offsetX = mouseEvent.getX();
            offsetY = mouseEvent.getY();
        });

        rootStackPane.setOnMouseDragged(mouseEvent->{
            Stage stage = (Stage)rootStackPane.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX()-offsetX);
            stage.setY(mouseEvent.getScreenY()-offsetY);
        });

        icon_exit.setOnMouseClicked(mouseEvent -> {
            Stage thisWindow = (Stage) icon_exit.getScene().getWindow();
            thisWindow.close();
            dashboardScene = DashboardController.dashboardScene;
            dashboardScene.getRoot().setEffect(null);
        });

        loadUsers();
        cmb_project.setDisable(true);
        cmb_user.setOnAction(event -> {
            String username = cmb_user.getValue();
            cmb_project.setDisable(false);
            loadProjects(username);
        });

        btn_assign.setOnAction(event -> {
            Boolean isTaskAdded = createNewTask();
            if (isTaskAdded.equals(true)){
                Stage window = (Stage) btn_assign.getScene().getWindow();
                window.close();
            }
        });

    }

    private boolean createNewTask() {
        if (!txt_taskname.getText().equals("")) {
            if (cmb_user.getValue() != null) {
                if (cmb_project.getValue() != null) {
                    if (date_start.getValue() != null) {
                        if (date_end.getValue() != null) {
                            if (!txt_description.getText().equals("")) {
                                WriteTask2MongoDB();
                                return true;
                            } else {
                                loadDialog("You must enter a description.", "No description entered!");
                                return false;
                            }
                        } else {
                            loadDialog("End date can not be null", "Set end date!");
                            return false;
                        }
                    } else {
                        loadDialog("Start date can not be null", "Set start date!");
                        return false;
                    }

                } else {
                    if (cmb_project.isDisabled()) {
                        loadDialog("The user you have selected is not a member \n of a project. Please" +
                                " contact to your admin \n to assign a project to the user.", "No project assigned!");
                        return false;
                    } else {
                        loadDialog("You have to select a project before you assign a task.", "No project selected");
                        return false;
                    }
                }
            } else {
                loadDialog("You haven't selected a user.", "No user selected!");
                return false;
            }
        } else {
            loadDialog("You left task name field empty. You have to+\n" +
                    " enter a task name before you assign the task!", "Empty field!");
            return false;
        }
    }

    private void WriteTask2MongoDB() {
        MongoDatabase database = loginController.database;
        MongoCollection collection = database.getCollection("tasks");
        Document task = new Document("summary", txt_taskname.getText())
                .append("username", cmb_user.getValue())
                .append("project_name", cmb_project.getValue())
                .append("start_date", date_start.getValue())
                .append("end_date", date_end.getValue())
                .append("description", txt_description.getText())
                .append("done", 0);
        collection.insertOne(task);
        loadDialog("New task has been added","Completed");

    }

    private void loadUsers() {
        cmb_user.getItems().clear();
        MongoDatabase database = loginController.database;
        FindIterable<Document> collection = database.getCollection("teammates").find();
        for (Document user : collection) {
            String username = user.getString("username");
            cmb_user.getItems().add(username);
        }
    }

    private void loadProjects(String username) {
        cmb_project.getItems().clear();
        MongoDatabase database = loginController.database;
        Document user = database.getCollection("teammates").find(eq("username", username)).first();
        if (user != null) {

            List<String> projects = (List<String>) user.get("projects");

            if (projects != null) {
                for (String project : projects) {
                    cmb_project.getItems().add(project);
                }
            } else {
                cmb_project.getItems().clear();
                cmb_project.setDisable(true);
            }

        }
    }

    private void loadDialog(String content, String heading) {
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setBody(new Text(content));
        jfxDialogLayout.setHeading(new Text(heading));
        JFXDialog jfxDialog = new JFXDialog();
        jfxDialog.setContent(jfxDialogLayout);
        jfxDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        jfxDialog.setDialogContainer(rootStackPane);
        jfxDialogLayout.setStyle("-fx-background-color:#ffffff");
        JFXButton confirmButton = new JFXButton();
        confirmButton.setText("Okay");
        confirmButton.setOnAction(actionEvent -> jfxDialog.close());
        jfxDialogLayout.setActions(confirmButton);
        jfxDialog.setOverlayClose(true);
        jfxDialog.show();
    }
}

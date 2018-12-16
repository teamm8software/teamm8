package CreateProject;

import com.jfoenix.controls.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dashboard.DashboardController;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.loginController;
import org.bson.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CProjectController implements Initializable {
    private Scene dashboardScene;
    MongoDatabase database = loginController.database;
    @FXML JFXTextField txt_name;
    @FXML JFXTextArea txt_description;
    @FXML JFXComboBox com_member,com_manager;
    @FXML JFXListView list_member;
    @FXML JFXButton btn_send,btn_add;
    @FXML DatePicker date_end,date_start;
    @FXML AnchorPane rootAnchor;
    @FXML StackPane rootStackPane;
    @FXML
    MaterialIconView icon_exit;

    double xOffset, yOffset;
    @Override

    public void initialize(URL location, ResourceBundle resources) {
        rootAnchor.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        rootAnchor.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage)rootAnchor.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX()-xOffset);
            stage.setY(mouseEvent.getScreenY()-yOffset);
        });
SetMembers();
btn_add.setOnAction(event -> {
    if (!list_member.getItems().contains(com_member.getValue())){
            list_member.getItems().add(com_member.getValue().toString());
        }
});

com_manager.setOnAction(event -> {
    if(list_member.getItems().contains(com_manager.getValue())){
        com_manager.setValue("");
    }else{
    }


});
icon_exit.setOnMouseClicked(event -> {
    closeButtonAction();
    dashboardScene = DashboardController.dashboardScene;
    dashboardScene.getRoot().setEffect(null);
});
btn_send.setOnAction(event -> {
    if(!txt_description.getText().equals("")){
        if (!txt_name.getText().equals("")){
            if(com_manager.getValue()!=null){
                if(list_member.getItems().size()>1){
                    if(date_start.getValue()!=null){
                        if (date_end.getValue()!=null){
                            Create();
                        }else{
                            loadDialog("Please enter an end date to this project.","Missing End Date.");
                        }


                    }else{
                        loadDialog("Please enter an end date to this project.","Missing Stard Date.");
                    }


                } else{
                    loadDialog("Please add few members to this project.","Missing Members");
                }

            }else{
                loadDialog("Please choose a project manager to this project.","Missing Project Manager");
            }

        }else{
            loadDialog("Please enter a name to this project.","Missing Project Name");
        }


    }else{
        loadDialog("Please describe this project.","Missing Project Description");
    }



});
    }
    private void closeButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage) btn_send.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    private void Create(){
        MongoCollection col = database.getCollection("projects");
        Document doc = new Document();
        String members = String.join(",", list_member.getItems());
        doc.append("project_name",txt_name.getText())
                .append("start_date",date_start.getValue())
                .append("end_date",date_end.getValue())
                .append("description",txt_description.getText())
                .append("teammates",com_manager.getValue() + "," + members)
                .append("done",false);
        col.insertOne(doc);
        loadDialog("Project assigned to : " + members + " .",txt_name.getText() + " created." );
    }

    private void SetMembers(){
        FindIterable<Document> users = database.getCollection("teammates").find();
        List<String> list = new ArrayList<>();
        ObservableList<String> com = FXCollections.observableList(list);
        for (Document user : users) {
            com_member.getItems().add(user.getString("username"));
            com_manager.getItems().add(user.getString("username"));
        }

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

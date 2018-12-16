package Comments;

import CommentCard.CommentCard;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dashboard.DashboardController;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.loginController;
import org.bson.Document;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Filters.eq;

public class CommentsController implements Initializable {

    private double offsetX, offsetY;

    private Scene dashboardScene;

    private MongoDatabase database = loginController.database;

    @FXML
    private GridPane grid_pane;

    @FXML
    private JFXButton btn_mark_complete;

    @FXML
    private Label task_name;

    @FXML
    private Label user_name;

    @FXML
    private Label due_date;

    @FXML
    private JFXButton btn_comment;

    @FXML
    private JFXButton btn_attach_file;

    @FXML
    private MaterialIconView icon_exit;
    @FXML
    private JFXTextArea txt_comment;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private StackPane commentStackPane;


    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy - hh:mm");
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_comment.setOnAction(event -> send_comment());

        rootAnchorPane.setOnMousePressed(mouseEvent -> {
            offsetX = mouseEvent.getSceneX();
            offsetY = mouseEvent.getSceneY();
        });

        rootAnchorPane.setOnMouseDragged(mouseEvent->{
            Stage window = (Stage)rootAnchorPane.getScene().getWindow();
            window.setX(mouseEvent.getScreenX()-offsetX);
            window.setY(mouseEvent.getScreenY()-offsetY);
        });
        icon_exit.setOnMouseClicked(mouseEvent->{
            Stage window = (Stage)icon_exit.getScene().getWindow();
            window.close();
            dashboardScene = DashboardController.dashboardScene;
            dashboardScene.getRoot().setEffect(null);
        });


        load_comments();


    }
    public void send_comment(){
        if(!txt_comment.getText().equals("")){
            if(txt_comment.getText().length()<250){
                MongoCollection collection = database.getCollection("comments");
                Document comment = new Document();
                Date date = new Date();
                comment.append("comment_date",date)
                        .append("content",txt_comment.getText())
                        .append("task_name",DashboardController.taskName)
                        .append("username",loginController.username);
                collection.insertOne(comment);
                txt_comment.setText("");
                load_comments();
            }else {
                loadDialog("Your post mustn't contain more than 250 characters. Please split your post into separate comments.",
                        "Character overflow!");
            }

        } else {
            loadDialog("You have to write something to post a comment", "Empty comment!");
        }


    }


    public void load_comments(){
        MongoCollection collection = database.getCollection("comments");
        FindIterable<Document> taskList = collection.find(eq("task_name", DashboardController.taskName));
        System.out.println(DashboardController.taskName);
        int row=0;
        for(Document taskDoc:taskList){
            CommentCard commentCard = new CommentCard();
            Date comment_date = taskDoc.getDate("comment_date");
            commentCard.set_comment_content(taskDoc.getString("content"));
            commentCard.set_user_name_text(taskDoc.getString("username"));
            commentCard.set_comment_date(dateFormat.format(comment_date));
            grid_pane.add(commentCard, 0,row);
            row++;
        }
    }

    public void set_task_name_text(String task_name_text){
        this.task_name.setText(task_name_text);
    }

    public void set_user_name_text(String user_name_text){
        this.user_name.setText(user_name_text);
    }

    public void set_due_date_text(String due_date_text){
        this.due_date.setText(due_date_text);
    }

    public void loadDialog(String content, String heading){
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setBody(new Text(content));
        jfxDialogLayout.setHeading(new Text(heading));
        JFXDialog jfxDialog = new JFXDialog();
        jfxDialog.setContent(jfxDialogLayout);
        jfxDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        jfxDialog.setDialogContainer(commentStackPane);
        JFXButton confirmButton = new JFXButton();
        confirmButton.setText("Okay");
        confirmButton.setOnAction(actionEvent -> jfxDialog.close());
        jfxDialogLayout.setActions(confirmButton);
        jfxDialog.setOverlayClose(true);
        jfxDialog.show();
    }

}

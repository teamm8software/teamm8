package CommentCard;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Date;

public class CommentCard extends AnchorPane {
    @FXML
    private Label comment_date;

    @FXML
    private Label user_name;

    @FXML
    private Label comment_content;

    @FXML
    private ImageView comment_image;

    public CommentCard(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CommentCard/CommentCardUI.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try{
            loader.load();
        }
        catch(IOException exception){
            throw new RuntimeException(exception);
        }
        comment_content.setWrapText(true);
    }

    public void set_user_name_text(String user_name_text){
        this.user_name.setText(user_name_text);
    }

    public void set_comment_date(String comment_date){this.comment_date.setText(comment_date); }

    public void set_comment_content(String comment_content){
        this.comment_content.setText(comment_content);
    }

    public void set_comment_image(Image img){
        this.comment_image.setImage(img);
    }
}

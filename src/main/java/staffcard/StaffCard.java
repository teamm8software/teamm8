package staffcard;

import DropBoxClass.DropBoxClass;
import ProfilePane.ProfilePane;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.SearchMatch;
import com.dropbox.core.v2.files.SearchResult;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class StaffCard extends AnchorPane {
    @FXML private AnchorPane staff_view;
    @FXML
    public Label lbl_username,lbl_birth,lbl_salary,lbl_password,lbl_role,lbl_id,lbl_namesurname,lbl_count;
    String username;

    @FXML private Circle circle_staff_pic;
    @FXML private Line line123;
    @FXML private MaterialIconView addicon,empty;
    private Image userImage;

    public static boolean haspp;

    public StaffCard() {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("staffcard/staffview.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try{
            loader.load();
        }
        catch(IOException exception){
            throw new RuntimeException(exception);
        }
        circle_staff_pic.setFill(ProfilePane.ppfill);
        staff_view.getStylesheets().add("/staffcard/staffStyle.css");

    }
    public void HideAll(){
       lbl_role.setVisible(false);
        lbl_username.setVisible(false);
        circle_staff_pic.setVisible(false);
        lbl_count.setVisible(true);
        addicon.setVisible(true);
        empty.setVisible(false);
    }
    public void ShowAll(){
        addicon.setVisible(false);
        lbl_count.setVisible(false);
    }

    public void setUserPic(String username) throws DbxException {
        DropBoxClass dropBoxClass = new DropBoxClass();
        DbxClientV2 client = dropBoxClass.clientV2();

        try(OutputStream os = new ByteArrayOutputStream()) {
            SearchResult searchResult = client.files().searchBuilder("/userImages", username).start();
            List<SearchMatch> list = searchResult.getMatches();
            if(!list.isEmpty()){
                for(SearchMatch match:list){
                    FileMetadata fileMetadata = (FileMetadata) match.getMetadata();
                    client.files().downloadBuilder(fileMetadata.getPathLower())
                            .download(os);
                    byte[] data = ((ByteArrayOutputStream) os).toByteArray();
                    ByteArrayInputStream input = new ByteArrayInputStream(data);
                    //BufferedImage bufferedImage = ImageIO.read(input);
                    userImage = new Image(input);
                    circle_staff_pic.setFill(new ImagePattern(userImage));
                    empty.setVisible(false);

                }
            }
            else
            {

                empty.setVisible(true);
                circle_staff_pic.setVisible(false);
                System.out.println(username + " -> User not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ImagePattern getFill(){
        return (ImagePattern) circle_staff_pic.getFill();
    }


public void setLbl_username(String username){lbl_username.setText(username);}
    public void setLbl_role(String role){
        lbl_role.setText(role);
    }
    public void setLbl_count(String something){lbl_count.setText(something);}
   public void setLbl_namesurname(String namesurname){lbl_namesurname.setText(namesurname);}
}


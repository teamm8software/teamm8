package cardview;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CardView extends AnchorPane {

    @FXML
    private AnchorPane card_view;

    @FXML
    private Label lbl_task_name;

    @FXML
    private Label lbl_description;

    @FXML
    private Label lbl_start_date;

    @FXML
    private Label lbl_end_date;

    @FXML
    private ProgressBar pBar;

    @FXML
    private MaterialIconView setDone;

    public CardView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cardview/cardview.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try{
            loader.load();
        }
        catch(IOException exception){
            throw new RuntimeException(exception);
        }
        card_view.getStylesheets().add("/cardview/cardStyle.css");
        lbl_description.setWrapText(true);
    }

    public void set_lbl_task_name_text(String task_name_text){
        lbl_task_name.setText(task_name_text);
    }

    public void set_lbl_description(String description){
        lbl_description.setText(description);
    }

    public void set_lbl_start_date(String start_date){
        lbl_start_date.setText(start_date);
    }

    public void set_lbl_end_date(String end_date){
        lbl_end_date.setText(end_date);
    }

    public void set_pBar(Double val){pBar.setProgress(val);}


    public String get_task_name(){
        return lbl_task_name.getText();
    }

    public MaterialIconView getTick(){
        return setDone;
    }

}

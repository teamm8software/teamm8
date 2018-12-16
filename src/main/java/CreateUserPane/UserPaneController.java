package CreateUserPane;

import com.jfoenix.controls.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dashboard.DashboardController;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.loginController;
import org.bson.Document;
import packMongo.mongoCON;
import packMongo.teammate;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

public class UserPaneController implements Initializable {
    private Scene dashboardScene;
    private mongoCON connection = loginController.connection;
    private MongoDatabase database = loginController.database;
    @FXML private StackPane rootStackPane;
    @FXML private AnchorPane rootAnchor;
    @FXML private Label lbl_Pass, lbl_UserName;
    @FXML private JFXTextField tx_IDNumber, tx_Name, tx_Surname, tx_Salary;
    @FXML private JFXComboBox com_Role;
    @FXML private JFXButton btn_Set;
    @FXML private MaterialIconView icon_exit;
    @FXML private DatePicker datePicker;

    double xOffset, yOffset;


    private int role,salary;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootAnchor.setOnMouseClicked(mouseEvent -> {



        });


        icon_exit.setOnMouseClicked(mouseEvent -> {
            closeButtonAction();
            dashboardScene = DashboardController.dashboardScene;
            dashboardScene.getRoot().setEffect(null);
        } );
        rootAnchor.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        rootAnchor.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage)rootAnchor.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX()-xOffset);
            stage.setY(mouseEvent.getScreenY()-yOffset);
        });
        setTextFieldFormat(tx_IDNumber);
        tx_Name.textProperty().addListener((observable, oldValue, newValue) -> { if (!newValue.matches("\\sa-zA-Z*")) { tx_Name.setText(newValue.replaceAll("[^\\sa-zA-Z]", "")); } });
        tx_Surname.textProperty().addListener((observable, oldValue, newValue) -> { if (!newValue.matches("\\sa-zA-Z*")) { tx_Surname.setText(newValue.replaceAll("[^\\sa-zA-Z]", "")); } });
        tx_IDNumber.textProperty().addListener((observable,oldValue,newValue) -> { if (tx_IDNumber.getText().length() > 11) { String s = tx_IDNumber.getText().substring(0, 11);tx_IDNumber.setText(s); }});
        tx_Salary.textProperty().addListener((observable,oldValue,newValue) -> { if (tx_Salary.getText().length() > 6) { String s = tx_Salary.getText().substring(0, 6);tx_Salary.setText(s); }});
        btn_Set.setOnAction(actionEvent ->{

            if(!tx_IDNumber.getText().equals("")){
                if(!tx_Name.getText().equals("")) {
                    if(!tx_Surname.getText().equals("")){
                        if(!tx_Salary.getText().equals("")){
                            if(com_Role.getValue()!=null) {
                                if(tx_Name.getText().length()>2){
                                    if(tx_Surname.getText().length()>1){
                                        if(tx_IDNumber.getText().length()==11){
                                            if(datePicker.getValue()!=null){
                                                SetUser();
                                            }else {loadDialog("Please enter a birth date.","Birth date can not be empty.");}

                                        }else {loadDialog(tx_IDNumber.getText()+ " can not be an id.","Make Sure that you enter a valid ID Number!");}

                                    }else {loadDialog(tx_Surname.getText()+ " can not be surname.","Make Sure that you enter a surname!");}

                                }else {loadDialog(tx_Name.getText()+ " can not be name.","Make Sure that you enter a Name!");}

                            }else{loadDialog("Please assign a role to user.","Role can not be empty.");}

                        }else{loadDialog("Please enter a salary value.","Salary can not be empty.");}


                    }else{loadDialog("Please enter a surname.","Surame can not be empty.");}
                }else{loadDialog("Please enter a name.","Name can not be empty.");}
            }else{loadDialog("Please enter an ID Number.","ID can not be empty.");}


        });
        setTextFieldFormat(tx_Salary);
        SetItems(com_Role);
        if(tx_Surname.getText()=="" || tx_Surname.getText()=="surname"){
            tx_Name.textProperty().addListener((observable, oldValue, newValue) -> { lbl_UserName.setText((newValue) );lbl_UserName.setText(lbl_UserName.getText().replaceAll("\\s+","")) ;});
        }else{

            tx_Name.textProperty().addListener((observable, oldValue, newValue) -> { lbl_UserName.setText((newValue+ "." + tx_Surname.getText()) );
                lbl_UserName.setText(lbl_UserName.getText().replaceAll("\\s+","")) ;});
            tx_Surname.textProperty().addListener((observable, oldValue, newValue) -> { lbl_UserName.setText(( tx_Name.getText()+ "." +newValue) );
                lbl_UserName.setText(lbl_UserName.getText().replaceAll("\\s+","")) ;});


        }
        com_Role.setOnAction(actionEvent -> {
            role=com_Role.getSelectionModel().getSelectedIndex();
        });

        lbl_Pass.setText(password());

    }


    private void closeButtonAction(){
        // get a handle to the stage
        Stage stage = (Stage) btn_Set.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    public void SetUser(){
        Date birthdate; // example date add
        int ifexist;
        String user_name=lbl_UserName.getText();
        Long id = Long.valueOf(tx_IDNumber.getText());
        int Salary = Integer.valueOf(tx_Salary.getText());
        String password=password();
        String Name=tx_Name.getText().toUpperCase();
        String Surname=tx_Surname.getText().toUpperCase();
        int Rol=role;
        LocalDate ld = datePicker.getValue();
        Calendar c =  Calendar.getInstance();
        c.set(ld.getYear(), ld.getMonthValue()-1, ld.getDayOfMonth());
        birthdate = c.getTime();
        teammate user = new teammate();

        user.setPassword(password);
        user.setName(Name);
        user.setSurname(Surname);
        user.setSalary(Salary);
        user.setAssignment(Rol);
        user.setBirthdate(birthdate);
        MongoCollection collection = database.getCollection("teammates");
        Document userr = database.getCollection("teammates").find(eq("username", user_name)).first();
        Document ids = database.getCollection("teammates").find(eq("id_number",id )).first();
        Document doc = new Document();
        String datee = "28-03-2016";

        //datePicker.getValue().toString() - get date selected
        if(ids==null){
        doc.append("name",user.getName())
                .append("surname",user.getSurname())
                .append("password",user.getPassword())
                .append("assignment",user.getAssignment())
                .append("salary",(int) user.getSalary())
                .append("birth",user.getBirthdate());
            user.setId_number(id);
            doc.append("id_number",id);
            if(userr==null){
                ifexist=0;
                user.setUsername(user_name);
                doc.append("username",user.getUsername());

            }else{
                ifexist=1;
                Random rndm = new Random();
                int sayi = rndm.nextInt(150);
                user.setUsername(user_name + "." + sayi);
                doc.append("username",user.getUsername() + "." + sayi);

            }
            collection.insertOne(doc);
            tx_Salary.setText("");
            tx_IDNumber.setText("");
            tx_Surname.setText("");
            tx_Name.setText("");
            lbl_Pass.setText(password);
            com_Role.setValue(null);
            lbl_UserName.setText("user.name");
            if(ifexist==0){
                loadDialog("New user added successfully.","User added.");
            }else{
                loadDialog(("This username ("+user.getName()+"." +user.getSurname() + ") is already exist. New username is : " + user.getUsername() ),"New User Added." );

            }


        }else{
            loadDialog("This ID already exist.","Check your informations.");
        }



    }
    private void setTextFieldFormat(JFXTextField txt_fld){
        Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d*");
        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null; });
        txt_fld.setTextFormatter(formatter); }

    public String password() {
        StringBuilder sb = new StringBuilder();
        int n = 8; // how many characters in password
        String set = "abcdefghijklmnoprstuvyzxqw";
        ; // characters to choose from
        String set2 = "0123456789";
        Random rndm = new Random();

        for (int i = 0; i < n / 2; i++) {
            int k = rndm.nextInt(set.length() - 1);
            int j = rndm.nextInt(set2.length() - 1);
            sb.append(set.charAt(k));
            sb.append(set2.charAt(j));
        }
        String pass = sb.toString();
        return pass;
    }
    public void showAlert(String alertTitle, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(alertTitle);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    private void SetItems(JFXComboBox combo){
        List<String> list = new ArrayList<>();
        ObservableList<String> com = FXCollections.observableList(list);
        list.add("Supervisor");
        list.add("Project Manager");
        list.add("Team Member");
        combo.setItems(com);

    }

    private void loadDialog(String content, String heading){
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setBody(new Text(content));
        jfxDialogLayout.setHeading(new Text(heading));
        JFXDialog jfxDialog = new JFXDialog();
        jfxDialog.setContent(jfxDialogLayout);
        jfxDialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        jfxDialog.setDialogContainer(rootStackPane);
        jfxDialogLayout.setStyle("-fx-background-color:#ff4d4d");
        JFXButton confirmButton = new JFXButton();
        confirmButton.setText("Okay");
        confirmButton.setOnAction(actionEvent -> jfxDialog.close());
        jfxDialogLayout.setActions(confirmButton);
        jfxDialog.setOverlayClose(true);
        jfxDialog.show();
    }
}

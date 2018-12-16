package dashboard;

import Comments.CommentsMain;
import CreateUserPane.CreateUserPane;
import CreateProject.CProject;
import CreateTask.CreateTask;
import DropBoxClass.DropBoxClass;
import ProfilePane.ProfilePane;
import cardview.CardView;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.SearchMatch;
import com.dropbox.core.v2.files.SearchResult;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import login.loginController;
import org.bson.Document;
import packMongo.mongoCON;
import staffcard.StaffCard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.eq;

public class DashboardController implements Initializable {
    public static double mouseX, mouseY;

    public static Scene dashboardScene;

    private mongoCON connection = loginController.connection;

    private MongoDatabase database = loginController.database;

    private String user_name = loginController.username;

    public static String taskName;

    @FXML
    private Pane pane_tasks, pane_team, pane_projects, pane_teams, pane_staff, pane_settings;
    @FXML
    private MaterialIconView btn_minimize, btn_exit;
    @FXML
    private VBox vbox_menu;
    @FXML
    private AnchorPane base_pane;
    @FXML
    private GridPane gridPane;
    @FXML
    private VBox content_container;
    @FXML
    private Label lbl_user_name, lbl_role, lbl_pagename, lbl_loading;
    @FXML
    private ScrollPane scroll_pane;
    @FXML
    private ImageView image_gif;
    @FXML
    private Circle circle_profile_img;
    @FXML
    private ContextMenu menu;
    MenuItem create_project = new MenuItem("New Project");
    MenuItem delete_project = new MenuItem("Delete Project");
    MenuItem create_task = new MenuItem("New Task");
    MenuItem delete_task = new MenuItem("Delete Task");
    MenuItem delete_user = new MenuItem("Delete User");
    MenuItem new_user = new MenuItem("New User");

    private StaffCard adduser = new StaffCard();

    public static String USERNAME;

    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private Drive drive;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menu.getItems().add(create_task);
        menu.getItems().add(create_project);
        menu.getItems().add(new_user);
        new_user.setOnAction(event -> {
            CreateUserPane crt = new CreateUserPane();
            try {
                crt.display();
                SetBlurred(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        create_project.setOnAction(event -> {
            CProject crt = new CProject();
            try {
                crt.display();
                SetBlurred(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        create_task.setOnAction(event -> {
            CreateTask newTask = new CreateTask();
            SetBlurred(true);
            try {
                newTask.display();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        ShowLoading(false);
        ShowTasks();
        SetPageName("TASKS");
        Document user = database.getCollection("teammates").find(eq("username", user_name)).first();
        if (user.getInteger("assignment") > 0) {
            pane_staff.setVisible(false);

        } else pane_staff.setVisible(true);
        if (user.getInteger("assignment") < 2) {
            pane_teams.setVisible(true);
        } else pane_teams.setVisible(false);

        scroll_pane.setOnMouseClicked(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
            menu.getItems().remove(delete_project);
            menu.getItems().remove(delete_task);
        });

        adduser.setOnMouseClicked(mouseEvent -> {
            CreateUserPane crt = new CreateUserPane();
            try {
                crt.display();
                SetBlurred(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        btn_minimize.setOnMouseClicked(mouseEvent -> {
            Stage stage = (Stage) btn_minimize.getScene().getWindow();
            stage.setIconified(true);
        });
        btn_exit.setOnMouseClicked(mouseEvent -> {

            Platform.exit();
        });

        try {
            loadProfilePicture(circle_profile_img, user_name);
        } catch (DbxException e) {
            e.printStackTrace();
        }

        pane_tasks.setOnMouseClicked(mouseEvent -> ShowTasks());
        pane_tasks.setOnMousePressed(mouseEvent -> {
            ShowLoading(true);
        });
        circle_profile_img.setOnMouseClicked(event -> {
            USERNAME = user_name;
            ProfilePane crt = new ProfilePane();
            try {
                crt.ppfill = (ImagePattern) circle_profile_img.getFill();
                crt.display();
                SetBlurred(true);
            } catch (IOException e) {
                e.printStackTrace();
            }


        });


        pane_staff.setOnMousePressed(mouseEvent -> {
            ShowLoading(true);
        });

        pane_staff.setOnMouseClicked(mouseEvent -> {
            try {
                getStaff();
            } catch (DbxException e) {
                e.printStackTrace();
            }
        });
        pane_projects.setOnMouseClicked(mouseEvent -> {
            ShowProjects();
        });
    }


    public void SetPageName(String name) {
        lbl_pagename.setText(name);

               /*

                if (lbl_pagename.getText() == "PROJECTS") {
                    menu.getItems().remove(create_task);
                    menu.getItems().add(create_project);
                    menu.getItems().remove(delete_task);
                    menu.getItems().remove(new_user);
                    menu.getItems().remove(delete_user);
                }
                if(lbl_pagename.getText() == "TASKS"){
                    menu.getItems().remove(create_project);
                    menu.getItems().add(create_task);
                    menu.getItems().remove(delete_project);
                    menu.getItems().remove(new_user);
                    menu.getItems().remove(delete_user);
            }
                if(lbl_pagename.getText()=="STAFF LIST"){
                    menu.getItems().add(new_user);
                    menu.getItems().remove(create_project);
                    menu.getItems().remove(delete_project);
                    menu.getItems().remove(delete_task);
                    menu.getItems().remove(create_task);

                }
*/


    }


    private void loadProfilePicture(Circle image_container, String username) throws DbxException {
        DropBoxClass dropBoxClass = new DropBoxClass();
        DbxClientV2 client = dropBoxClass.clientV2();

        try (OutputStream os = new ByteArrayOutputStream()) {
            SearchResult searchResult = client.files().searchBuilder("/userImages", username + ".jpg").start();
            List<SearchMatch> list = searchResult.getMatches();
            for (SearchMatch match : list) {
                FileMetadata fileMetadata = (FileMetadata) match.getMetadata();
                FileMetadata downloadBuilder = client.files().downloadBuilder(fileMetadata.getPathLower())
                        .download(os);
                byte[] data = ((ByteArrayOutputStream) os).toByteArray();
                ByteArrayInputStream input = new ByteArrayInputStream(data);
                //BufferedImage bufferedImage = ImageIO.read(input);
                Image userImage = new Image(input);
                image_container.setFill(new ImagePattern(userImage));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String GetPageName() {
        return lbl_pagename.getText();
    }

    public long totalTime(Date first, Date second) {
        Date firstDate = first;
        Date secondDate = second;
        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long total = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return total;
    }

    private void ShowLoading(boolean a) {
        image_gif.setVisible(a);
        lbl_loading.setVisible(a);
    }

    private void ShowTasks() {
        SetPageName("TASKS");
        int column = 0, row = 0;
        gridPane.getChildren().clear();
        FindIterable<Document> tasks = database.getCollection("tasks").find(eq("username", user_name));
        for (Document task : tasks) {
            if (column == 4) {
                column = 0;
                row++;
            }
            Date firstDate = (Date) task.get("start_date");
            Date secondDate = (Date) task.get("end_date");
            Date today = new Date();
            long total = totalTime(firstDate, secondDate);
            long remaining = totalTime(today, secondDate);
            Double deger = (Double.valueOf(remaining) / Double.valueOf(total));
            String startdate = dateFormat.format(firstDate);
            String enddate = dateFormat.format(secondDate);
            CardView card = new CardView();
            card.set_lbl_task_name_text((String) task.get("summary"));
            card.set_lbl_description((String) task.get("description"));
            card.set_lbl_start_date(startdate);
            card.set_lbl_end_date(enddate);
            System.out.println(total < remaining);
            if (total < remaining) {
                card.set_pBar(0.0);
            } else {
                card.set_pBar(1.0 - deger);
            }
            MaterialIconView setDone;
            setDone = card.getTick();
            MaterialIconView finalSetDone = setDone;
            setDone.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2) {
                    finalSetDone.setFill(Color.GREEN);
                }
            });
            card.setOnMouseClicked(mouseEvent -> {
                menu.getItems().add(delete_task);
                try {
                    CommentsMain commentsMain = new CommentsMain();
                    taskName = task.getString("summary");
                    commentsMain.display(taskName, user_name, enddate);
                    SetBlurred(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            gridPane.add(card, column, row);

            column++;
        }
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        ShowLoading(false);
    }

    public void SetBlurred(boolean value) {
        dashboardScene = gridPane.getScene();
        if (value) {
            GaussianBlur gaussianBlur = new GaussianBlur();
            gaussianBlur.setRadius(15);
            dashboardScene.getRoot().setEffect(gaussianBlur);
        } else dashboardScene.getRoot().setEffect(null);
    }

    private void ShowProjects() {
        SetPageName("PROJECTS");
        int column = 0, row = 0;
        gridPane.getChildren().clear();
        FindIterable<Document> projects = database.getCollection("projects").find();
        System.out.println(user_name);
        for (Document project : projects) {

            System.out.println(project.getString("teammates"));
            if (project.getString("teammates").contains(user_name)) {
                System.out.println("there is a project contains your username::::" + project.getString("project_name"));
                if (column == 4) {
                    column = 0;
                    row++;
                }
                Date firstDate = (Date) project.get("start_date");
                Date secondDate = (Date) project.get("end_date");
                Date today = new Date();
                long total = totalTime(firstDate, secondDate);
                long remaining = totalTime(today, secondDate);
                Double deger = (Double.valueOf(remaining) / Double.valueOf(total));
                String startdate = dateFormat.format(firstDate);
                String enddate = dateFormat.format(secondDate);
                CardView card = new CardView();
                card.set_lbl_task_name_text((String) project.get("project_name"));
                card.set_lbl_description((String) project.get("description"));
                card.set_lbl_start_date(startdate);
                card.set_lbl_end_date(enddate);
                if (remaining > total) {
                    card.set_pBar(0.0);
                } else {
                    card.set_pBar(1 - deger);
                }
                gridPane.add(card, column, row); // add card.
                column++;
            }
        }
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        ShowLoading(false);
    }

    public void DeleteUser(String fieldName, Object value) {
        MongoCollection<Document> col = database.getCollection("teammates");
        FindIterable<Document> user = col.find(eq(fieldName, value));
        System.out.println(user.first());
        col.deleteOne(user.first());
    }


    private void getStaff() throws DbxException {
        gridPane.getChildren().clear();
        adduser.HideAll();
        adduser.setLbl_namesurname("Add User");
        adduser.setPrefWidth(scroll_pane.getWidth() * 0.9);
        adduser.setStyle("#lbl_namesurname{-fx-font-family:HelveticaNeueLT Pro 65 Md}");
        gridPane.add(adduser, 0, 0);

        SetPageName("STAFF LIST");
        FindIterable<Document> teammates = database.getCollection("teammates").find();
        MongoCollection<Document> roles = database.getCollection("roles");

        int column = 0, row = 1;


        for (Document teammate : teammates) {
            Document roleDoc = roles.find(eq("role_id", teammate.getInteger("assignment"))).first();
            String name = teammate.getString("name");
            String surname = teammate.getString("surname");
            String role;
            if (roleDoc != null) {
                role = roleDoc.getString("role_name");
            } else {
                role = "";
            }
            String username = teammate.getString("username");
            StaffCard staffCard = new StaffCard();
            staffCard.setLbl_role(role);
            staffCard.setLbl_username(username);
            staffCard.setLbl_namesurname(name + " " + surname);
            staffCard.setUserPic(username);
            staffCard.ShowAll();
            staffCard.setOnMouseClicked(event -> {
                menu.getItems().add(delete_user);
                USERNAME = staffCard.lbl_username.getText();
                ProfilePane crt = new ProfilePane();
                try {
                    crt.ppfill = staffCard.getFill();
                    crt.display();
                    SetBlurred(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            gridPane.add(staffCard, column, row);
            column++;
            if (column == 1) {
                row++;
                column = 0;
            }
            staffCard.setPrefWidth(scroll_pane.getWidth() * 0.9);

        }
        long count = database.getCollection("teammates").countDocuments();
        adduser.setLbl_count(count + " users");
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        ShowLoading(false);
    }


    public void showAlert(String alertTitle, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(alertTitle);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public mongoCON getConnection() {
        return connection;
    }

    public void setConnection(mongoCON connection) {
        this.connection = connection;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    public void setDatabase(MongoDatabase _database) {
        this.database = _database;
    }

    public String get_lbl_user_name() {
        return this.lbl_user_name.getText();
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void set_lbl_username_text(String user_name) {
        this.lbl_user_name.setText(user_name);
    }

    public void set_lbl_role_text(String role_name) {
        this.lbl_role.setText(role_name);
    }
}

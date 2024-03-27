package cgroup2.cadmycode.gui.user;

import cgroup2.cadmycode.content.ContentStatus;
import cgroup2.cadmycode.content.Course;
import cgroup2.cadmycode.content.Webcast;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.DashboardScene;
import cgroup2.cadmycode.gui.GuiMain;
import cgroup2.cadmycode.gui.SceneType;
import cgroup2.cadmycode.gui.SceneWrapper;
import cgroup2.cadmycode.gui.course.CourseScene;
import cgroup2.cadmycode.gui.course.CourseStatisticsView;
import cgroup2.cadmycode.gui.webcast.WebcastCreationForm;
import cgroup2.cadmycode.gui.webcast.WebcastDeletionPopup;
import cgroup2.cadmycode.gui.webcast.WebcastEditForm;
import cgroup2.cadmycode.user.Sex;
import cgroup2.cadmycode.user.User;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.time.LocalDate;

import javafx.scene.control.Button;

/**
 * shows a list of {@link User} and buttons for creating, deleting and updating selected users
 */
public class UserScene extends SceneWrapper {
    private Button home = new Button("Home");
    private Button create = new Button("Register");
    private Button edit = new Button("Edit user");
    private Button delete = new Button("Delete user");
    private Button refresh = new Button("Refresh");
    private Button stats = new Button("Statistics");
    private TableView<User> userTable = new TableView<>();

    private TableColumn<User, Integer> attributeUserId = new TableColumn<>("UserId");
    private TableColumn<User, String> attributeEmail = new TableColumn<>("Email");
    private TableColumn<User, String> attributeName = new TableColumn<>("Name");
    private TableColumn<User, String> attributeAddress = new TableColumn<>("Address");
    private TableColumn<User, String> attributeCountry = new TableColumn<>("Country");
    private TableColumn<User, String> attributeHouseNumber = new TableColumn<>("HouseNumber");
    private TableColumn<User, LocalDate> attributeDateOfBirth = new TableColumn<>("Date of birth");
    private TableColumn<User, Sex> attributeSex = new TableColumn<>("Sex");

    /**
     * creates an instance of a {@link UserScene} scene
     * @param stage the stage on which the {@link UserScene} is to be drawn
     */
    public  UserScene(Stage stage){
        super(stage);
        attributeUserId.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        attributeEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        attributeName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        attributeAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        attributeCountry.setCellValueFactory(new PropertyValueFactory<>("Country"));
        attributeHouseNumber.setCellValueFactory(new PropertyValueFactory<>("HouseNumber"));
        attributeDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("DateOfBirth"));
        attributeSex.setCellValueFactory(new PropertyValueFactory<>("Sex"));

        userTable.getColumns().addAll(
            attributeUserId,
            attributeEmail,
            attributeName,
            attributeAddress,
            attributeHouseNumber,
            attributeCountry,
            attributeDateOfBirth,
            attributeSex
        );
        VBox v1 = new VBox(create, edit, delete, stats);
        v1.setSpacing(10.0);

        VBox v2 = new VBox(home, refresh);
        v2.setSpacing(40);

        HBox hBox = new HBox(v2,userTable, v1);
        hBox.setPadding(new Insets(10.0));
        hBox.setSpacing(10.0);

        create.setOnMouseClicked(this::onCreateButtonPressed);
        refresh.setOnMouseClicked(this::loadData);
        edit.setOnMouseClicked(this::onEditButtonPressed);
        delete.setOnMouseClicked(this::onDeleteButtonPressed);
        home.setOnMouseClicked(this::onHomeButtonPressed);
        stats.setOnMouseClicked(this::onStatsButtonPressed);

        loadData(new Event(EventType.ROOT));

        this.scene = new Scene(hBox);
    }

    /**
     * loads the certificates from the database to the list of certificates in the {@link UserScene}
     * @param e the event that triggers retrieving data en loading it into the gui
     */
    public void loadData(Event e) {
        userTable.setItems(FXCollections.observableArrayList(
                Database.getUsers()
        ));
    }

    /**
     * creates a popup for creating new Users
     * @param e represents the button that when clicked will trigger showing the register popup
     */
    private void onCreateButtonPressed(Event e) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        // shows the webcast form
        new UserCreationForm(dialog).show();

        dialog.show();
    }

    /**
     * creates a popup for editing selected users
     * @param e represents the button that when clicked will trigger showing the edit popup
     */
    private void onEditButtonPressed(Event e) {
        if (userTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new UserEditForm(dialog, userTable.getSelectionModel().getSelectedItem()).show(); //UserEditForm is leeg

        dialog.show();
    }

    /**
     * creates a popup for deleting selected users
     * @param e represents the button that when clicked will trigger showing the deletion popup
     */
    private void onDeleteButtonPressed(Event e) {
        if (userTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        // shows the deletion popup
        new UserDeletionPopup(dialog, userTable.getSelectionModel().getSelectedItem()).show();

        dialog.show();
    }

    /**
     * when triggered will return to the {@link DashboardScene}
     * @param e represents the button that when clicked will trigger returning to the {@link DashboardScene}
     */
    private void onHomeButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.DASHBOARD);
    }

    /**
     * when triggered will open the {@link UserStatisticsView}
     * @param e represents the button that when clicked will trigger opening the {@link UserStatisticsView}
     */
    private void onStatsButtonPressed(Event e) {
        if (userTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        // shows the deletion popup
        new UserStatisticsView(dialog, userTable.getSelectionModel().getSelectedItem()).show();

        dialog.show();
    }
}

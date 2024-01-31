package cgroup2.cadmycode.gui.user;

import cgroup2.cadmycode.content.ContentStatus;
import cgroup2.cadmycode.content.Webcast;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.GuiMain;
import cgroup2.cadmycode.gui.SceneManager;
import cgroup2.cadmycode.gui.SceneWrapper;
import cgroup2.cadmycode.gui.webcast.WebcastScene;
import cgroup2.cadmycode.user.Sex;
import cgroup2.cadmycode.user.User;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;

public class UserCreationForm extends SceneWrapper {


    private TextField nameArea = new TextField();
    private TextField emailArea = new TextField();
    private TextField addressArea = new TextField();
    private TextField countryArea = new TextField();
    private TextField cityArea = new TextField();
    private DatePicker dateOfBirthArea = new DatePicker();
    private ComboBox<cgroup2.cadmycode.user.Sex> sexBox = new ComboBox<>(FXCollections.observableArrayList(
            Sex.MALE,
            Sex.FEMALE,
            Sex.UNKNOWN
    ));

    private Button submit = new Button("Submit");
    public UserCreationForm(Stage stage){
        super(stage);
        Label name = new Label("Name");
        Label email = new Label("Email");
        Label address = new Label("Address");
        Label country = new Label("Country");
        Label city = new Label("City");
        Label dateOfBirth = new Label("Date of birth");
        Label sex = new Label("Sex");

        HBox h1 = new HBox(name, nameArea);
        HBox h2 = new HBox(email, emailArea);
        HBox h3 = new HBox(address, addressArea);
        HBox h4 = new HBox(country, countryArea);
        HBox h5 = new HBox(city, cityArea);
        HBox h6 = new HBox(dateOfBirth, dateOfBirthArea);
        HBox h7 = new HBox(sex, sexBox);
        HBox h8 = new HBox(new Label(), submit);

        VBox v = new VBox(h1, h2, h3, h4, h5, h6, h7, h8);

        v.setSpacing(10.0);
        v.setPadding(new Insets(10.0));
        v.setAlignment(Pos.CENTER_LEFT);

        submit.setOnMouseClicked(this::onSubmit);

        this.scene = new Scene(v);
    }
    private void onSubmit(Event event) {
        // TODO: validation of fields

        try {
            Database.create(
                    new User(
                            nameArea.getText(),
                            emailArea.getText(),
                            addressArea.getText(),
                            countryArea.getText(),
                            cityArea.getText(),
                            dateOfBirthArea.getValue(),
                            sexBox.getValue()
                    )
            );
        } catch (Exception e) {
            SceneManager.showErrorDialog(e.getMessage());
            return;
        }

        stage.close();
        // refresh table
        ((UserScene) GuiMain.SCENE_MANAGER.getCurrentScene()).loadData(new Event(EventType.ROOT));
    }
}

package cgroup2.cadmycode.gui.webcast;

import cgroup2.cadmycode.content.ContentStatus;
import cgroup2.cadmycode.content.Webcast;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.GuiMain;
import cgroup2.cadmycode.gui.SceneManager;
import cgroup2.cadmycode.gui.SceneWrapper;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WebcastCreationForm extends SceneWrapper {

  private TextField titleField = new TextField();
  private TextArea descriptionArea = new TextArea();
  private DatePicker publicationDate = new DatePicker();
  private ComboBox<ContentStatus> status = new ComboBox<>(
    FXCollections.observableArrayList(
      ContentStatus.ACTIVE,
      ContentStatus.CONCEPT,
      ContentStatus.ARCHIVE
    )
  );
  // version is 0 for webcast
  private TextField length = new TextField(); // can only contain a number
  private TextField location = new TextField(); // a URL, starting with https://*.com
  private TextField organisation = new TextField();
  private TextField speaker = new TextField();

  private Button submit = new Button("Submit");

  public WebcastCreationForm(Stage stage) {
    super(stage);
    Label titleLabel = new Label("Title");
    Label descriptionLabel = new Label("Description");
    Label pubDateLabel = new Label("Date of publication");
    Label statusLabel = new Label("Status");
    Label lengthLabel = new Label("Length");
    Label locationLabel = new Label("Web address");
    Label orgLabel = new Label("Organisation");
    Label speakerLabel = new Label("Speaker");

    HBox h1 = new HBox(titleLabel, titleField);
    HBox h2 = new HBox(descriptionLabel, descriptionArea);
    HBox h3 = new HBox(pubDateLabel, publicationDate);
    HBox h4 = new HBox(statusLabel, status);
    HBox h5 = new HBox(lengthLabel, length);
    HBox h6 = new HBox(locationLabel, location);
    HBox h7 = new HBox(orgLabel, organisation);
    HBox h8 = new HBox(speakerLabel, speaker);
    HBox h9 = new HBox(new Label(), submit);

    VBox v = new VBox(h1, h2, h3, h4, h5, h6, h7, h8, h9);

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
        new Webcast(
          titleField.getText(),
          descriptionArea.getText().replace("\n", " "),
          publicationDate.getValue(),
          status.getValue(),
          Integer.parseInt(length.getText()),
          new URL(location.getText()).toString(),
          speaker.getText(),
          organisation.getText()
        )
      );
    } catch (MalformedURLException | NumberFormatException e) {
      SceneManager.showErrorDialog(e.getMessage());
      return;
    }

    stage.close();
    // refresh table
    ((WebcastScene) GuiMain.SCENE_MANAGER.getCurrentScene()).loadData(
        new Event(EventType.ROOT)
      );
  }
}

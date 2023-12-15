package cgroup2.cadmycode.gui;

import cgroup2.cadmycode.content.Webcast;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WebcastScene extends SceneWrapper {

    Button home = new Button("home");
    Button create = new Button("+");
    Button edit = new Button("edit");
    Button delete = new Button("delete");
    TableView<Webcast> webcastTable = new TableView<>();
    TableColumn atribuutContentItemId = new TableColumn<Webcast, Integer>("contentItemId");
    TableColumn atribuutLenth = new TableColumn<Webcast, Integer>("length");
    TableColumn atribuutURL = new TableColumn<Webcast, String>("URL");
    TableColumn atribuutSpeaker = new TableColumn<Webcast, String>("speaker");
    TableColumn atribuutOrganisation = new TableColumn<Webcast, String>("organisation");

    public WebcastScene(Stage stage) {
        super(stage);

        atribuutContentItemId.setCellFactory(new PropertyValueFactory<Webcast, Integer>("contentItemId"));
        atribuutLenth.setCellFactory(new PropertyValueFactory<Webcast, Integer>("length"));
        atribuutURL.setCellFactory(new PropertyValueFactory<Webcast, String>("URL"));
        atribuutSpeaker.setCellFactory(new PropertyValueFactory<Webcast, String>("speaker"));
        atribuutOrganisation.setCellFactory(new PropertyValueFactory<Webcast, String>("organisation"));
        webcastTable.getColumns().addAll(atribuutContentItemId, atribuutLenth, atribuutURL, atribuutSpeaker, atribuutOrganisation);

        VBox vBox = new VBox(create, edit, delete);

        HBox hBox = new HBox(home,webcastTable, vBox);
        hBox.setPadding(new Insets(10.0));

        stage.setScene(new Scene(hBox));
    }

}

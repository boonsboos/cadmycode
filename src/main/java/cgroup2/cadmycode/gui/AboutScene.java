package cgroup2.cadmycode.gui;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AboutScene extends SceneWrapper {

    private final Button home = new Button("Home");

    public AboutScene(Stage stage) {
        super(stage);

        VBox v = new VBox(
                new Label("Jonas Dingemans - 2216880 | boonsboos"),
                new Label("Felix Baeten - 2216594 | FB99FIRE"),
                new Label("Iris Peters - 2218597 | iris2344"),
                new Label("Niels Plug - 2206017 | Mingull")
        );
        v.setPadding(new Insets(10));

        TitledPane tp = new TitledPane("Credits", v);

        VBox v2 = new VBox(new Hyperlink("https://github.com/boonsboos/cadmycode"), tp);
        v2.setSpacing(10);

        HBox h = new HBox(home, v2);
        h.setPadding(new Insets(10));
        h.setSpacing(10);

        home.setOnMouseClicked(this::onHomeButtonPressed);

        this.scene = new Scene(h);
    }

    private void onHomeButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.DASHBOARD);
    }
}

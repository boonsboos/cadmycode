package cgroup2.cadmycode.gui.course;

import java.util.ArrayList;

import cgroup2.cadmycode.content.Course;
import cgroup2.cadmycode.content.CourseLevel;
import cgroup2.cadmycode.content.Module;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.GuiMain;
import cgroup2.cadmycode.gui.SceneManager;
import cgroup2.cadmycode.gui.SceneWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class CourseEditForm extends SceneWrapper {
    private TextField courseName = new TextField();
    private TextField subject = new TextField();
    private TextArea introductionText = new TextArea();
    private TextField courseID = new TextField();
    private ComboBox<CourseLevel> levelBox = new ComboBox<>(FXCollections.observableArrayList(
            CourseLevel.BEGINNER,
            CourseLevel.INTERMEDIATE,
            CourseLevel.EXPERT
    ));
    private TextField certificateID = new TextField();
    private ComboBox<Module> modulesBox = new ComboBox<>();
    private ListView<Module> moduleList = new ListView<>();
    private ArrayList<Module> modules = new ArrayList<>(Database.getModules());
    private Button removeModuleButton = new Button("Remove module(s)");

    Button submit = new Button("Submit");

    public CourseEditForm(Stage stage, Course selected) {
        super(stage);
        stage.setTitle("edit course");

        courseName.setText(selected.getCourseName());
        subject.setText(selected.getSubject());
        introductionText.setText(selected.getIntroductionText());
        courseID.setText(String.valueOf(selected.getCourseID()));
        levelBox.setValue(selected.getLevel());
        certificateID.setText(String.valueOf(selected.getCertificateID()));
        modulesBox.setItems(FXCollections.observableArrayList(modules));
        moduleList.setItems(FXCollections.observableArrayList(modules.stream().filter(m -> m.getCourseID() == selected.getCourseID()).toArray(Module[]::new)));

        Label courseNameLabel = new Label("courseName");
        Label subjectLabel = new Label("subject");
        Label introductionTextLabel = new Label("introduction");
        Label courseIDLabel = new Label("courseID");
        Label levelBoxLabel = new Label("level");
        Label certificateIDLabel = new Label("certificateID");
        Label allModulesBoxLabel = new Label("select modules");
        Label moduleListLabel = new Label("modules"); 


        modulesBox.setConverter(new StringConverter<Module>() {
            @Override
            public String toString(Module module) {
                if(module == null) return null;
                return module.getTitle();
            }

            @Override
            public Module fromString(String string) {
                return modulesBox.getItems()
                                    .stream()
                                    .filter(m -> m.getTitle().equals(string))
                                    .findFirst().orElse(null);
            }
        });

        modulesBox.valueProperty().addListener((obs, oldModule, newModule) -> {
            if(newModule != null)
                if(!moduleList.getItems().contains(newModule)){
                    moduleList.getItems().add(newModule);
                }
        });

        moduleList.setPrefSize(300, 350);

        moduleList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        moduleList.setCellFactory(new Callback<ListView<Module>, ListCell<Module>>() {
            @Override
            public ListCell<Module> call(ListView<Module> param) {
                return new ListCell<Module>() {
                    @Override
                    protected void updateItem(Module item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null) {
                            setText(item.getTitle());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });

        removeModuleButton.setOnAction(e -> {
           ObservableList<Module> selectedModules = moduleList.getSelectionModel().getSelectedItems();
            if(selectedModules.size() != 0) {
                moduleList.getItems().removeAll(selectedModules);
            }
        });

        HBox h1 = new HBox(courseNameLabel, courseName);
        HBox h2 = new HBox(subjectLabel, subject);
        HBox h3 = new HBox(introductionTextLabel, introductionText);
        HBox h4 = new HBox(courseIDLabel, courseID);
        HBox h5 = new HBox(levelBoxLabel, levelBox);
        HBox h6 = new HBox(certificateIDLabel, certificateID);
        HBox h7 = new HBox(allModulesBoxLabel, modulesBox);
        HBox h8 = new HBox(moduleListLabel, moduleList, removeModuleButton);
        HBox h9 = new HBox(submit);

        VBox v = new VBox(h1, h2, h3, h4, h5, h6, h7, h8, h9);

        for (var child : v.getChildren()) {
            ((HBox) child).setSpacing(10);
        }

        v.setSpacing(10.0);
        v.setPadding(new Insets(10.0));
        v.setAlignment(Pos.CENTER_LEFT);

        submit.setOnMouseClicked(this::onSubmit);

        this.scene = new Scene(v);
    }
    private void onSubmit(Event event) {
        try {
            // loop through all modules and update to this course with the courseID if it is not already or remove if it is not in the list
            for (Module module : modules) {
                if(moduleList.getItems().contains(module)) {
                    if(module.getCourseID() != Integer.parseInt(courseID.getText())) {
                        module.setCourseID(Integer.parseInt(courseID.getText()));
                        Database.update(module);
                    }
                } else {
                    if(module.getCourseID() == Integer.parseInt(courseID.getText())) {
                        module.setCourseID(0);
                        Database.update(module);
                    }
                }
            }
            Database.update(
                new Course(
                    courseName.getText(),
                    subject.getText(),
                    introductionText.getText().replace("\n", " "),
                    Integer.parseInt(courseID.getText()),
                    levelBox.getValue(),
                    Integer.parseInt(certificateID.getText())
                )
            );
        } catch (NumberFormatException e) {
            SceneManager.showErrorDialog(e.getMessage());
            return;
        }
        stage.close();
        // refresh table
        ((CourseScene) GuiMain.SCENE_MANAGER.getCurrentScene()).loadData(new Event(EventType.ROOT));
    }
}

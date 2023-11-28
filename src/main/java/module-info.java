module cgroup2.cadmycode {
    requires javafx.controls;
    requires javafx.fxml;


    opens cgroup2.cadmycode to javafx.fxml;
    exports cgroup2.cadmycode;
    exports cgroup2.cadmycode.gui;
    opens cgroup2.cadmycode.gui to javafx.fxml;
}
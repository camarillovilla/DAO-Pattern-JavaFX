module system.coursesdemo_template {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires java.sql;

    opens system.controllers to javafx.fxml;
    exports system.controllers;
    exports system.objects;
}

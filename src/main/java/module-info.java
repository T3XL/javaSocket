module com.example.socketlab {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires emoji.java;

    opens com.example.socketlab to javafx.fxml;
    exports com.example.socketlab;
    exports com.example.thread;
    opens com.example.thread to javafx.fxml;
    exports com.example.controller;
    opens com.example.controller to javafx.fxml;
}
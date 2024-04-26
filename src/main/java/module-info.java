module com.example.privateclinic {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.jfoenix;

    opens com.example.privateclinic to javafx.fxml;
    exports com.example.privateclinic;
    opens com.example.privateclinic.Controllers to javafx.fxml;
    exports com.example.privateclinic.Models;
    exports com.example.privateclinic.Views;
}
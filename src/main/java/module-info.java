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
    requires java.sql;
    requires java.desktop;
    requires java.mail;
    requires itextpdf;
    requires kernel;
    requires layout;
    requires io;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.example.privateclinic to javafx.fxml;
    exports com.example.privateclinic;
    opens com.example.privateclinic.Controllers to javafx.fxml;
    exports com.example.privateclinic.Models;
    exports com.example.privateclinic.Views;
    opens com.example.privateclinic.Models to javafx.fxml;
    exports com.example.privateclinic.Models.Report;
    opens com.example.privateclinic.Models.Report to javafx.fxml;
}
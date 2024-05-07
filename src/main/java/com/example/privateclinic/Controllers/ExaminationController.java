package com.example.privateclinic.Controllers;

import com.example.privateclinic.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ExaminationController implements Initializable {
    public Pane btnClose;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void close(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }
}

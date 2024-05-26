package com.example.privateclinic.Controllers;

import com.example.privateclinic.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEmployeeController implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    void close(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(s);
    }
}

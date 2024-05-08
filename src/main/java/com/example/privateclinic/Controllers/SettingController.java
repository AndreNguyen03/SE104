package com.example.privateclinic.Controllers;

import com.example.privateclinic.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    public ComboBox cbMax;
    public ComboBox cbMoney;
    public Button btnSave;
    public Button btnClose;
    public Button btnCancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    btnClose.setOnAction(event -> closeStage());
    }

    private void closeStage() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }
}

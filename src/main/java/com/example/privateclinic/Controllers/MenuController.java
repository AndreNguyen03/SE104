package com.example.privateclinic.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Optional;

public class MenuController {

    @FXML
    private Button btnAccountant;

    @FXML
    private Button btnCategory;

    @FXML
    private Button btnExamination;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnReport;

    @FXML
    private Button btnSetting;

    @FXML
    private Text titleTextField;

    @FXML
    void btnDanhMucClicked(ActionEvent event) {

    }

    @FXML
    void btnReceptionClicked(ActionEvent event) {

    }

    @FXML
    void closeMenu(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("DangXuat");
        confirmationAlert.setContentText("Ban muon dang xuat?");

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = ButtonType.CANCEL;

        confirmationAlert.getButtonTypes().setAll(okButton, cancelButton);

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == okButton) {
            s.close();
        }
    }

    @FXML
    void minimizeMenu(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

}

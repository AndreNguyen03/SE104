package com.example.privateclinic.Controllers;

import com.example.privateclinic.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    public ComboBox<Integer> cbMax;
    public ComboBox<Integer> cbMoney;
    public Button btnSave;
    public Button btnClose;
    public Button btnCancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbMax.getItems().addAll(8, 16, 32, 64);
        cbMoney.getItems().addAll(30000, 40000, 50000, 60000);

        cbMax.setEditable(true);
        cbMoney.setEditable(true);

        cbMax.getEditor().setOnAction(event -> validateAndAddToComboBox(cbMax, "Vui lòng nhập một số nguyên."));

        cbMoney.getEditor().setOnAction(event -> validateAndAddToComboBox(cbMoney, "Vui lòng nhập một số nguyên."));

        btnSave.setOnAction(event -> saveSettings());
        btnCancel.setOnAction(event -> resetSettings());
        btnClose.setOnAction(event -> closeStage());
    }

    private void validateAndAddToComboBox(ComboBox<Integer> comboBox, String errorMessage) {
        try {
            Integer value = Integer.parseInt(comboBox.getEditor().getText());
            if (!comboBox.getItems().contains(value)) {
                comboBox.getItems().add(value);
                comboBox.getSelectionModel().select(value);
            }
        } catch (NumberFormatException e) {
            showErrorMessage(errorMessage);
        }
    }

    private void saveSettings() {
        if (!validateInput()) {
            return;
        }

        System.out.println("Settings saved: Max Patients - " + cbMax.getValue() + ", Consultation Fee - " + cbMoney.getValue());


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Lưu Thiết Lập");
        alert.setHeaderText(null);
        alert.setContentText("Thiết lập đã được lưu thành công!");
        alert.showAndWait();
    }

    private boolean validateInput() {
        try {
            Integer.parseInt(cbMax.getEditor().getText());
            Integer.parseInt(cbMoney.getEditor().getText());
            return true;
        } catch (NumberFormatException e) {
            showErrorMessage("Dữ liệu đã nhập không hợp lệ. Vui lòng nhập số nguyên.");
            return false;
        }
    }

    private void resetSettings() {
        cbMax.getItems().clear();
        cbMoney.getItems().clear();

        cbMax.getItems().addAll(8, 16, 32, 64);
        cbMoney.getItems().addAll(30000, 40000, 50000, 60000);
    }

    private void closeStage() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

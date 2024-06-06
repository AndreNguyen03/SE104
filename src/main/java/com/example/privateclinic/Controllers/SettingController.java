package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.RegulationDAO;
import com.example.privateclinic.Models.Regulation;
import com.example.privateclinic.Models.Model;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton closeButton;

    @FXML
    private TextField tf_MaxPatient;

    @FXML
    private TextField tf_MaxMoney;

    private final RegulationDAO regulationDAO = new RegulationDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSettings();

        saveButton.setOnAction(event -> saveSettings());
        cancelButton.setOnAction(event -> resetSettings());
        closeButton.setOnAction(event -> closeStage());
    }

    private void loadSettings() {

        int maxPatientRegulationValue = regulationDAO.getRegulationValueById(1);
        int maxMoneyRegulationValue = regulationDAO.getRegulationValueById(2);

        tf_MaxPatient.setText(String.valueOf(maxPatientRegulationValue));
        tf_MaxMoney.setText(String.valueOf(maxMoneyRegulationValue));
    }

    private void saveSettings() {
        if (validateInput()) {
            int maxPatient = Integer.parseInt(tf_MaxPatient.getText());
            int maxMoney = Integer.parseInt(tf_MaxMoney.getText());

            // Giả sử mã quy định cho MaxPatient là 1
            int maxPatientRegulationId = 1;
            int maxMoneyRegulationId = 2;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận chỉnh sửa");
            alert.setHeaderText("Bạn có chắc chắn muốn chỉnh sửa?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                regulationDAO.updateRegulationValue(maxPatientRegulationId, maxPatient);
                regulationDAO.updateRegulationValue(maxMoneyRegulationId, maxMoney);
                loadSettings();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Thành công");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Thiết lập đã được lưu thành công.");
                successAlert.showAndWait();
            }
        }
    }

    private boolean validateInput() {
        try {
            Integer.parseInt(tf_MaxPatient.getText());
            Integer.parseInt(tf_MaxMoney.getText());
            return true;
        } catch (NumberFormatException e) {
            showErrorMessage("Dữ liệu đã nhập không hợp lệ!");
            return false;
        }
    }

    private void resetSettings() {
        loadSettings();
    }

    private void closeStage() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

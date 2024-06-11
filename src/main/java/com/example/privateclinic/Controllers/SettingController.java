package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.HistoryDAO;
import com.example.privateclinic.DataAccessObject.RegulationDAO;
import com.example.privateclinic.Models.History;
import com.example.privateclinic.Models.Regulation;
import com.example.privateclinic.Models.Model;
import com.example.privateclinic.Models.User;
import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
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
    public TitledPane tltSetting;
    private final RegulationDAO regulationDAO = new RegulationDAO();
    private double xOffset = 0;
    private double yOffset =0;
    private HistoryDAO historyDAO= new HistoryDAO();
    User user;

    public void initData(User user) {
        this.user = user;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSettings();

        saveButton.setOnAction(event -> saveSettings());
        cancelButton.setOnAction(event -> resetSettings());
        closeButton.setOnAction(event -> closeStage());
        tf_MaxPatient.textProperty().addListener((observableValue, oldvalue,newvalue) -> {
            if (!newvalue.matches("\\d*")) {
                tf_MaxPatient.setText(newvalue.replaceAll("[^\\d]", ""));
                showErrorMessage("Chỉ nhập số");
            }
        });
        tf_MaxPatient.textProperty().addListener((observableValue, oldvalue,newvalue) -> {
            if (!newvalue.matches("\\d*")) {
                tf_MaxPatient.setText(newvalue.replaceAll("[^\\d]", ""));
                showErrorMessage("Chỉ nhập số");
            }
        });
        tltSetting.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getScreenY();
        });
        tltSetting.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX()-xOffset);
            stage.setY(mouseEvent.getScreenY()-yOffset);
        });
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

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận chỉnh sửa");
            alert.setHeaderText("Bạn có chắc chắn muốn chỉnh sửa?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                regulationDAO.updateRegulationValue(1, maxPatient);
                regulationDAO.updateRegulationValue(2, maxMoney);
                loadSettings();
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Thành công");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Thiết lập đã được lưu thành công.");
                successAlert.showAndWait();
                History history = new History(user.getEmployee_id(), STR." Thiết lập : Đã thay đổi bệnh nhân tối đa thành \{maxPatient} \n tiền khám thành \{maxPatient}");
                historyDAO.addHistory(history);
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

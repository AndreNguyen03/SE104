package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.DiseaseDAO;
import com.example.privateclinic.Models.Disease;
import com.example.privateclinic.Models.Patient;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Date;
import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Category_DiseaseController implements Initializable {

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton editButton;

    @FXML
    private JFXButton deleteButton;

    @FXML
    private TextField diseaseICDTextField;

    @FXML
    private TextField diseaseNameTextField;

    @FXML
    private TextField tf_diseaseByICD;

    @FXML
    private TextField tf_diseaseByName;

    @FXML
    private TableView<Disease> diseaseTableView;

    @FXML
    private TableColumn<Disease, Integer> idColumn;

    @FXML
    private TableColumn<Disease, String> icdColumn;

    @FXML
    private TableColumn<Disease, String> nameColumn;

    @FXML
    private Text diseaseCount;

    private DiseaseDAO diseaseDAO = new DiseaseDAO();

    private final ObservableList<Disease> diseaseData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        diseaseDAO = new DiseaseDAO();

        idColumn.setStyle("-fx-text-fill: white;");
        icdColumn.setStyle("-fx-text-fill: white;");
        nameColumn.setStyle("-fx-text-fill: white;");

        // Set up columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("maBenh"));
        icdColumn.setCellValueFactory(new PropertyValueFactory<>("maICD"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenBenh"));

        // Load data into table
        loadDiseaseData();
        searchDiseaseByICDAndByName();
        DiseaseSelection();
        getDiseaseCount();

        addButton.setOnAction(this::handleAddDisease);
        editButton.setOnAction(this::handleEditDisease);
        deleteButton.setOnAction(this::handleDeleteDisease);

    }

    private void loadDiseaseData() {
        List<Disease> diseases = diseaseDAO.getAllDiseases();
        diseaseData.addAll(diseases);
        diseaseTableView.setItems(diseaseData);
    }

    @FXML
    void handleAddDisease(ActionEvent event) {

        String diseaseICD = diseaseICDTextField.getText();
        String diseaseName = diseaseNameTextField.getText();

        if (diseaseICD.isEmpty() || diseaseName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo!");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đầy đủ mã ICD và tên bệnh!");
            alert.showAndWait();
            return;
        }

        if (diseaseDAO.isDiseaseNameExists(diseaseName, -1)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo!");
            alert.setHeaderText(null);
            alert.setContentText("Tên bệnh đã tồn tại!");
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận thêm");
            alert.setHeaderText("Bạn có chắc chắn muốn thêm?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                diseaseDAO.addDisease(new Disease(diseaseDAO.getNextDiseaseId(), diseaseName, diseaseICD));
                diseaseData.clear();
                clearDiseaseFields();
                loadDiseaseData();
                getDiseaseCount();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Thành công");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Đã thêm bệnh thành công.");
                successAlert.showAndWait();
            }
        }
    }

    @FXML
        void handleEditDisease (ActionEvent event) {
        Disease selectedDisease = diseaseTableView.getSelectionModel().getSelectedItem();
        String diseaseICD = diseaseICDTextField.getText();
        String diseaseName = diseaseNameTextField.getText();

        if (diseaseICD.isEmpty() || diseaseName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo!");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đầy đủ mã ICD và tên bệnh!");
            alert.showAndWait();
            return;
        }

        if (diseaseDAO.isDiseaseNameExists(diseaseName, selectedDisease.getMaBenh())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo!");
            alert.setHeaderText(null);
            alert.setContentText("Tên bệnh đã tồn tại!");
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận chỉnh sửa");
            alert.setHeaderText("Bạn có chắc chắn muốn chỉnh sửa?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                selectedDisease.setMaICD(diseaseICD);
                selectedDisease.setTenBenh(diseaseName);

                diseaseDAO.updateDisease(selectedDisease);
                diseaseData.clear();
                clearDiseaseFields();
                loadDiseaseData();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Thành công");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Đã cập nhật bệnh thành công.");
                successAlert.showAndWait();
            }
        }
    }

    @FXML
    void handleDeleteDisease(ActionEvent event) {
        Disease selectedDisease = diseaseTableView.getSelectionModel().getSelectedItem();
        if (selectedDisease != null) {
            Alert confirmDeleteAlert = new Alert(Alert.AlertType.WARNING);
            confirmDeleteAlert.setTitle("Xác nhận xóa");
            confirmDeleteAlert.setHeaderText("Bạn có chắc chắn muốn xóa bệnh này?");
            confirmDeleteAlert.setContentText("Bệnh này sẽ bị xóa khỏi cơ sở dữ liệu");

            Optional<ButtonType> result = confirmDeleteAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int diseaseID = selectedDisease.getMaBenh();
                diseaseDAO.deleteDisease(diseaseID);
                diseaseData.clear(); // Xóa dữ liệu hiện tại trong bảng
                diseaseDAO.updateDiseaseIds(); // Cập nhật lại các ID sau khi xóa
                loadDiseaseData(); // Tải lại dữ liệu từ cơ sở dữ liệu
                getDiseaseCount();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Thành công");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Đã xóa bệnh thành công.");
                successAlert.showAndWait();
            }
        } else {
            // Hiển thị thông báo lỗi nếu không có hàng nào được chọn
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một bệnh để xóa.");
            alert.showAndWait();
        }
    }

    private void DiseaseSelection() {
        diseaseTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Lấy thông tin của thuốc từ hàng được chọn
                Disease selectedMedicine = diseaseTableView.getSelectionModel().getSelectedItem();

                // Đặt thông tin của thuốc vào các trường TextField và ComboBox
                diseaseICDTextField.setText(selectedMedicine.getMaICD());
                diseaseNameTextField.setText(selectedMedicine.getTenBenh());

            } else {
                // Nếu không có hàng nào được chọn, xóa nội dung của các trường
                diseaseICDTextField.clear();
                diseaseNameTextField.clear();
            }
        });
    }

    private void clearDiseaseFields() {
        diseaseICDTextField.clear();
        diseaseNameTextField.clear();
    }

    private void getDiseaseCount() {
        int maxMabenh = diseaseDAO.getMaxDiseaseId();
        diseaseCount.setText(String.valueOf(maxMabenh));
    }

    private void searchDiseaseByICDAndByName() {
        tf_diseaseByICD.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                diseaseTableView.setItems(diseaseData);
            } else {
                ObservableList<Disease> filteredList = FXCollections.observableArrayList();
                String strippedValue = removeAccentsAndToLower(newValue);
                for (Disease disease : diseaseData) {
                    String strippedICD = removeAccentsAndToLower(disease.getMaICD());
                    if (strippedICD.contains(strippedValue)) {
                        filteredList.add(disease);
                    }
                }
                diseaseTableView.setItems(filteredList);
            }
        });

        tf_diseaseByName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                diseaseTableView.setItems(diseaseData);
            } else {
                ObservableList<Disease> filteredList = FXCollections.observableArrayList();
                String strippedValue = removeAccentsAndToLower(newValue);
                for (Disease disease : diseaseData) {
                    String strippedName = removeAccentsAndToLower(disease.getTenBenh());
                    if (strippedName.contains(strippedValue)) {
                        filteredList.add(disease);
                    }
                }
                diseaseTableView.setItems(filteredList);
            }
        });
    }


    private String removeAccentsAndToLower(String input) {
        if (input == null) {
            return null;
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase();
    }

}
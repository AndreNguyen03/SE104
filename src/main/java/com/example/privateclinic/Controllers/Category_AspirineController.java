package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.MedicineDAO;
import com.example.privateclinic.Models.Medicine;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Category_AspirineController implements Initializable {

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton editButton;

    @FXML
    private JFXButton deleteButton;

    @FXML
    private TextField medicineNameTextField;

    @FXML
    private ComboBox<String> unitComboBox;

    @FXML
    private ComboBox<String> formComboBox;

    @FXML
    private ComboBox<String> useComboBox;

    @FXML
    private TableView<Medicine> medicineTableView;

    @FXML
    private TableColumn<Medicine, Integer> idColumn;

    @FXML
    private TableColumn<Medicine, String> nameColumn;

    @FXML
    private TableColumn<Medicine, String> unitColumn;

    @FXML
    private TableColumn<Medicine, String> formColumn;

    @FXML
    private TableColumn<Medicine, String> useColumn;

    @FXML
    private TableColumn<Medicine, String> quantityColumn;

    @FXML
    private TableColumn<Medicine, Double> priceColumn;

    private MedicineDAO medicineDAO = new MedicineDAO();

    private final ObservableList<Medicine> medicineData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        unitComboBox.getItems().addAll("Viên", "Chai");
        formComboBox.getItems().addAll("Viên", "Lỏng", "Gel");
        useComboBox.getItems().addAll("Uống", "Bôi ngoài", "Xông", "Nhai");

        medicineDAO = new MedicineDAO();

        // Set up columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("tenDonViTinh"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("giaBan"));
        formColumn.setCellValueFactory(new PropertyValueFactory<>("tenDangThuoc"));
        useColumn.setCellValueFactory(new PropertyValueFactory<>("tenCachDung"));

        // Load data into table
        loadMedicineData();

        addButton.setOnAction(this::handleAddMedicine);
        editButton.setOnAction(this::handleEditMedicine);
        deleteButton.setOnAction(this::handleDeleteMedicine);
    }

    private void loadMedicineData() {
        List<Medicine> medicines = medicineDAO.getAllMedicines();
        medicineData.addAll(medicines);
        medicineTableView.setItems(medicineData);
    }

    @FXML
    void handleAddMedicine(ActionEvent event) {

        String medicineName = medicineNameTextField.getText();
        String medicineUnit = unitComboBox.getValue();
        int medicineUnitValue;
        String medicineForm = formComboBox.getValue();
        int medicineFormValue;
        String medicineUse = useComboBox.getValue();
        int medicineUseValue;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thêm");
        alert.setHeaderText("Bạn có chắc chắn muốn thêm?");

        Optional<ButtonType> result = alert.showAndWait();

        switch (medicineUnit) {
            case "Viên":
                medicineUnitValue = 1;
                break;
            case "Chai":
                medicineUnitValue = 2;
                break;
            default:
                medicineUnitValue = -1;
                break;
        }

        switch (medicineForm) {
            case "Viên":
                medicineFormValue = 1;
                break;
            case "Lỏng":
                medicineFormValue = 2;
                break;
            case "Gel":
                medicineFormValue = 3;
                break;
            default:
                medicineFormValue = -1;
                break;
        }

        switch (medicineUse) {
            case "Uống":
                medicineUseValue = 1;
                break;
            case "Bôi ngoài":
                medicineUseValue = 2;
                break;
            case "Xông":
                medicineUseValue = 3;
                break;
            case "Nhai":
                medicineUseValue = 4;
                break;
            default:
                medicineUseValue = -1;
                break;
        }
        if (result.isPresent() && result.get() == ButtonType.OK) {
            medicineDAO.addMedicine(new Medicine(medicineDAO.getNextMedicineId(), medicineName, medicineUnitValue, 50, 30000, medicineFormValue, medicineUseValue));
            medicineData.clear();
            loadMedicineData();
        }

    }

    @FXML
    void handleEditMedicine(ActionEvent event) {
        Medicine selectedMedicine = medicineTableView.getSelectionModel().getSelectedItem();
        String medicineName = medicineNameTextField.getText();
        String medicineUnit = unitComboBox.getValue();
        int medicineUnitValue;
        String medicineForm = formComboBox.getValue();
        int medicineFormValue;
        String medicineUse = useComboBox.getValue();
        int medicineUseValue;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận chỉnh sửa");
        alert.setHeaderText("Bạn có chắc chắn muốn chỉnh sửa?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            switch (medicineUnit) {
                case "Viên":
                    medicineUnitValue = 1;
                    break;
                case "Chai":
                    medicineUnitValue = 2;
                    break;
                default:
                    medicineUnitValue = -1;
                    break;
            }

            switch (medicineForm) {
                case "Viên":
                    medicineFormValue = 1;
                    break;
                case "Lỏng":
                    medicineFormValue = 2;
                    break;
                case "Gel":
                    medicineFormValue = 3;
                    break;
                default:
                    medicineFormValue = -1;
                    break;
            }

            switch (medicineUse) {
                case "Uống":
                    medicineUseValue = 1;
                    break;
                case "Bôi ngoài":
                    medicineUseValue = 2;
                    break;
                case "Xông":
                    medicineUseValue = 3;
                    break;
                case "Nhai":
                    medicineUseValue = 4;
                    break;
                default:
                    medicineUseValue = -1;
                    break;
            }

            // Cập nhật thông tin của thuốc được chọn
            selectedMedicine.setTenThuoc(medicineName);
            selectedMedicine.setMaDonViTinh(medicineUnitValue);
            selectedMedicine.setMaDangThuoc(medicineFormValue);
            selectedMedicine.setMaCachDung(medicineUseValue);

            // Cập nhật thuốc trong cơ sở dữ liệu
            medicineDAO.updateMedicine(selectedMedicine);

            // Xóa dữ liệu hiện tại trong bảng và tải lại dữ liệu mới
            medicineData.clear();
            loadMedicineData();
        }
    }

    @FXML
    void handleDeleteMedicine(ActionEvent event) {
        Medicine selectedMedicine = medicineTableView.getSelectionModel().getSelectedItem();
        if (selectedMedicine != null) {
            Alert confirmDeleteAlert = new Alert(Alert.AlertType.WARNING);
            confirmDeleteAlert.setTitle("Xác nhận xóa");
            confirmDeleteAlert.setHeaderText("Bạn có chắc chắn muốn xóa thuốc này?");
            confirmDeleteAlert.setContentText("Thuốc này sẽ bị xóa khỏi cơ sở dữ liệu");

            Optional<ButtonType> result = confirmDeleteAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int medicineID = selectedMedicine.getMaThuoc();
                medicineDAO.deleteMedicine(medicineID);
                medicineData.clear(); // Xóa dữ liệu hiện tại trong bảng
                medicineDAO.updateMedicineIds(); // Cập nhật lại các ID sau khi xóa
                loadMedicineData(); // Tải lại dữ liệu từ cơ sở dữ liệu
            }
        } else {
            // Hiển thị thông báo lỗi nếu không có hàng nào được chọn
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một thuốc để xóa.");
            alert.showAndWait();
        }
    }

}
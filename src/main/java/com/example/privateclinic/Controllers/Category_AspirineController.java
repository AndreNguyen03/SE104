package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.MedicineDAO;
import com.example.privateclinic.Models.Medicine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Category_AspirineController implements Initializable {

    @FXML
    private TableView<Medicine> medicineTableView;

    @FXML
    private TableColumn<Medicine, Integer> idColumn;

    @FXML
    private TableColumn<Medicine, String> nameColumn;

    @FXML
    private TableColumn<Medicine, Integer> unitColumn;

    @FXML
    private TableColumn<Medicine, Integer> formColumn;

    @FXML
    private TableColumn<Medicine, Integer> useColumn;

    @FXML
    private TableColumn<Medicine, Integer> quantityColumn;

    @FXML
    private TableColumn<Medicine, Double> priceColumn;

    private MedicineDAO medicineDAO;

    private final ObservableList<Medicine> medicineData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
    }

    private void loadMedicineData() {
        List<Medicine> medicines = medicineDAO.getAllMedicines();
        medicineData.addAll(medicines);
        medicineTableView.setItems(medicineData);
    }
}

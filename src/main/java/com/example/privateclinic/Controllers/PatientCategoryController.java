package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.PatientDAO;
import com.example.privateclinic.Models.Patient;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PatientCategoryController implements Initializable {

    PatientDAO patientDAO = new PatientDAO();

    @FXML
    private JFXButton btnAddFromDB;

    @FXML
    private TableColumn<Patient, String> tcPatientAddress;

    @FXML
    private TableColumn<Patient, Date> tcPatientBirth;

    @FXML
    private TableColumn<Patient, String> tcPatientGender;

    @FXML
    private TableColumn<Patient, Integer> tcPatientId;

    @FXML
    private TableColumn<Patient, String> tcPatientName;

    @FXML
    private TableColumn<Patient, String> tcPatientPhoneNumber;

    @FXML
    private TextField tfPatientByPhoneNumber;

    @FXML
    private TableView<Patient> tvPatient;

    ReceptionController receptionController;

    public void setController(ReceptionController controller) {
        this.receptionController = controller;
    }

    Patient selected;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @FXML
    void btnAdd(MouseEvent event) {
        LocalDateTime dateTime = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        selected.setArrivalDate(dateTime);

        boolean isExist = false;
        for (Patient item : receptionController.getPatients()) {
            if (item.getPatientId() == selected.getPatientId()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Đã có trong danh sách khám bệnh", ButtonType.OK);
                alert.showAndWait();
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            int index = receptionController.getPatients().size() + 1;
            patientDAO.admitPatient(selected.getPatientId(), index);
            patientDAO.getReceptionId(selected.getPatientId());
            receptionController.getPatients().add(selected);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Đã thêm vào danh sách khám bệnh", ButtonType.OK);
            alert.showAndWait();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Patient> patientList = FXCollections.observableList(patientDAO.getAllPatients());
        bindPatientData(tcPatientId, tcPatientName, tcPatientGender, tcPatientBirth, tcPatientPhoneNumber, tcPatientAddress);
        tvPatient.setItems(patientList);
        tvPatient.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                selected = tvPatient.getSelectionModel().getSelectedItem();

                if (selected == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Không tìm thấy bệnh nhân", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });
        searchPatientByPhoneNumber();
    }

    static void bindPatientData(TableColumn<Patient, Integer> tcPatientId, TableColumn<Patient, String> tcPatientName, TableColumn<Patient, String> tcPatientGender, TableColumn<Patient, Date> tcPatientBirth, TableColumn<Patient, String> tcPatientPhoneNumber, TableColumn<Patient, String> tcPatientAddress) {
        tcPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        tcPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        tcPatientGender.setCellValueFactory(new PropertyValueFactory<>("patientGender"));
        tcPatientBirth.setCellValueFactory(new PropertyValueFactory<>("patientBirth"));
        tcPatientPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("patientPhoneNumber"));
        tcPatientAddress.setCellValueFactory(new PropertyValueFactory<>("patientAddress"));

    }

    private void searchPatientByPhoneNumber() {
        ObservableList<Patient> patientList = FXCollections.observableArrayList(patientDAO.getAllPatients());
        tfPatientByPhoneNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                tvPatient.setItems(patientList);
            } else {

                ObservableList<Patient> filteredList = FXCollections.observableArrayList();
                for (Patient patient : patientList) {
                    if (patient.getPatientPhoneNumber().startsWith(newValue)) {
                        filteredList.add(patient);
                    }
                }
                tvPatient.setItems(filteredList);
            }
        });
    }
}

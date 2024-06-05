package com.example.privateclinic.Controllers;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.example.privateclinic.DataAccessObject.PatientDAO;
import com.example.privateclinic.Models.Model;
import com.example.privateclinic.Models.Patient;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;

public class ReceptionController implements Initializable {
    @FXML
    private Pane btnClose;

    @FXML
    private JFXButton btnAddFromList;

    @FXML
    private JFXButton btnAddNewPatient;


    @FXML
    private JFXButton btnDeleteFromList;

    @FXML
    private JFXButton btnPrintWaitingSlip;

    @FXML
    private ComboBox<String> cbPatientGender;

    @FXML
    private TextField tfPatientBirthDay;

    @FXML
    private DatePicker dpDate;

    @FXML
    private TableColumn<?, ?> tcDoctor;

    @FXML
    private TableColumn<Integer, Integer> tcNumber;

    @FXML
    private TableColumn<Integer, Integer> tcNumberDetails;

    @FXML
    private TableColumn<Patient, String> tcPatientAddressDetail;

    @FXML
    private TableColumn<Patient, Date> tcPatientBirthDetail;

    @FXML
    private TableColumn<Patient, String> tcPatientGenderDetail;

    @FXML
    private TableColumn<Patient, Integer> tcPatientId;

    @FXML
    private TableColumn<Patient, Integer> tcPatientIdDetail;

    @FXML
    private TableColumn<Patient, String> tcPatientName;

    @FXML
    private TableColumn<Patient, String> tcPatientNameDetail;

    @FXML
    private TableColumn<Patient, String> tcPatientPhoneNumberDetail;

    @FXML
    private TextField tfPatientById;

    @FXML
    private TextField tfPatientByName;

    @FXML
    private TextField tfPatientId;

    @FXML
    private TextField tfPatientName;

    @FXML
    private TextField tfPatientPhoneNumber;

    @FXML
    private TableView<Patient> tvPatient;

    @FXML
    private TableView<Patient> tvPatientDetails;
    @FXML
    private TextArea taPatientAddress;

    private PatientDAO patientDAO = new PatientDAO();

    private ObservableList<Patient> patientsDetails;
    private ObservableList<Patient> patients;

    final private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDatePickerToday();
        validatePhoneNumber();
        searchPatientByIdAndByName();
        setPatientList(Date.valueOf(dpDate.getValue()));
        setPatientDetailList(Date.valueOf(dpDate.getValue()));
        setTableViewByDate();
        setOnOffAddDeleteBtn();
    }

    private void setOnOffAddDeleteBtn() {
        tvPatient.setOnMouseClicked((MouseEvent event) -> {
            if (tvPatient.getSelectionModel().getSelectedItem() != null) {
                btnDeleteFromList.setDisable(true);
                // Nếu có dòng được chọn, bật nút Thêm
                btnAddFromList.setDisable(false);
            } else {
                // Ngược lại, vô hiệu hóa nút Thêm
                btnAddFromList.setDisable(true);
            }
        });

        tvPatientDetails.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnDeleteFromList.setDisable(false);
                btnAddFromList.setDisable(true);
            } else {
                btnDeleteFromList.setDisable(true);
            }
        });
    }

    private void setTableViewByDate() {
        dpDate.setOnAction(event -> {
            // Khởi tạo task để thực hiện các công việc nền
            Task<Void> backgroundTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    // Thực hiện công việc nền ở đây
                    Date selectedDate = Date.valueOf(dpDate.getValue());
                    setPatientList(selectedDate);
                    setPatientDetailList(selectedDate);
                    searchPatientByIdAndByName();
                    return null;
                }
            };

            // Khởi động task trong một luồng mới
            new Thread(backgroundTask).start();
        });
    }


    private void setDatePickerToday() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Đặt ngày hiện tại cho DatePicker
        dpDate.setValue(LocalDate.now());

        // Định dạng cách hiển thị của DatePicker
        dpDate.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, dateFormatter);
                    } catch (DateTimeParseException e) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        });
    }

    private void searchPatientByIdAndByName() {
        ObservableList<Patient> patientList = FXCollections.observableArrayList(patientDAO.getPatientsFromReceptionByDate(Date.valueOf(dpDate.getValue())));
        tfPatientById.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                tvPatient.setItems(patientList);
            } else {
                ObservableList<Patient> filteredList = FXCollections.observableArrayList();
                for (Patient patient : patientList) {
                    if (patient.getPatientId()==Integer.parseInt(newValue)) {
                        filteredList.add(patient);
                    }
                }
                tvPatient.setItems(filteredList);
            }
        });

        tfPatientByName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                tvPatient.setItems(patientList);
            } else {
                ObservableList<Patient> filteredList = FXCollections.observableArrayList();
                for (Patient patient : patientList) {
                    if (patient.getPatientName().contains(newValue)) {
                        filteredList.add(patient);
                    }
                }
                tvPatient.setItems(filteredList);
            }
        });
    }

    private void validatePhoneNumber() {
        tfPatientPhoneNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            // Loại bỏ bất kỳ ký tự không phải là số
            String formattedPhoneNumber = newValue.replaceAll("[^\\d]", "");

            // Kiểm tra điều kiện:
            // 1. Bắt đầu bằng 0.
            // 2. Chiều dài tối đa là 10 ký tự.
            if (formattedPhoneNumber.length() == 0 || formattedPhoneNumber.startsWith("0")) {
                if (formattedPhoneNumber.length() <= 10) {
                    tfPatientPhoneNumber.setText(formattedPhoneNumber);
                } else {
                    // Nếu chiều dài vượt quá 10 ký tự, cắt chuỗi thành 10 ký tự đầu tiên
                    tfPatientPhoneNumber.setText(formattedPhoneNumber.substring(0, 10));
                }
            } else {
                // Nếu không bắt đầu bằng 0, giữ nguyên giá trị cũ
                tfPatientPhoneNumber.setText(oldValue);
            }
        });
    }


    public void close(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    @FXML
    void addPatientToDatabase(ActionEvent event) throws ParseException {
        // Định dạng của chuỗi ngày
        if(checkFillData()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Phân tích chuỗi thành LocalDate
            LocalDate localDate = LocalDate.parse(tfPatientBirthDay.getText(), formatter);
            // Chuyển đổi LocalDate thành java.sql.Date
            Date patientBirthDay = Date.valueOf(localDate);
            String patientName = tfPatientName.getText();
            String patientGender = cbPatientGender.getValue().toString();
            String patientPhoneNumber = tfPatientPhoneNumber.getText();
            String patientAddress = taPatientAddress.getText();
            Date patientArrival = Date.valueOf(LocalDate.now());
            patientDAO.addPatient(new Patient(patientDAO.getNextPatientId(), patientName, patientGender
                    , patientPhoneNumber, patientBirthDay, patientAddress,patientArrival));

            clearDataField();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thêm thành công!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Chưa nhập đủ thông tin");
            alert.showAndWait();
        }
    }

    private boolean checkFillData() {
        if (tfPatientName.getText().isEmpty()  || tfPatientPhoneNumber.getText().isEmpty()
                || tfPatientBirthDay.getText().isEmpty()|| taPatientAddress.getText().isEmpty()
                || cbPatientGender.getValue().isBlank()) {
            return false;
        }
        return true;
    }

    private void setPatientList(Date date) {
        tcNumber.setCellValueFactory(cellData -> new SimpleIntegerProperty(tvPatient.getItems().indexOf(cellData.getValue()) + 1).asObject());
        tcPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        tcPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        patients = patientDAO.getPatientsFromReceptionByDate(date);
        tvPatient.getItems().clear();
        tvPatient.setItems(patients);
    }

    private void setPatientDetailList(Date date) {
        tcNumberDetails.setCellValueFactory(cellData -> new SimpleIntegerProperty(tvPatientDetails.getItems().indexOf(cellData.getValue()) + 1).asObject());
        tcPatientIdDetail.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        tcPatientNameDetail.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        tcPatientGenderDetail.setCellValueFactory(new PropertyValueFactory<>("patientGender"));
        tcPatientBirthDetail.setCellValueFactory(new PropertyValueFactory<>("patientBirth"));
        tcPatientPhoneNumberDetail.setCellValueFactory(new PropertyValueFactory<>("patientPhoneNumber"));
        tcPatientAddressDetail.setCellValueFactory(new PropertyValueFactory<>("patientAddress"));
        patientsDetails = patientDAO.getPatientsFromReceptionByDate(date);
        tvPatientDetails.getItems().clear();
        tvPatientDetails.setItems(patients);
    }

    private void clearDataField() {
        tfPatientId.clear();
        tfPatientBirthDay.clear();
        tfPatientName.clear();
        tfPatientPhoneNumber.clear();
        taPatientAddress.clear();
        cbPatientGender.setValue("");
        cbPatientGender.setPromptText("Chọn giới tính");
    }


    @FXML
    void addPatientToDetails(MouseEvent event) {
        Patient patientToAdd = tvPatient.getSelectionModel().getSelectedItem();
        if(checkExist(patientToAdd)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Bệnh nhân đã tồn tại");
            alert.setContentText("Bệnh nhân với ID " + patientToAdd.getPatientId() + " đã tồn tại trong danh sách.");
            alert.showAndWait();
        }
        else if(patientToAdd!= null && !checkExist(patientToAdd)) {
            patientsDetails.add(patientToAdd);
        }
        tvPatientDetails.setItems(patientsDetails);
    }

    private boolean checkExist(Patient patientToAdd) {
        for (Patient p : tvPatientDetails.getItems()) {
            if (patientToAdd.getPatientId()==(p.getPatientId())) {
                return true;
            }
        }
        return false;
    }

    @FXML
    void detelePatientFromDetails(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xóa bệnh nhân");
        alert.setContentText("Bạn chắc chắn muốn xóa ?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            Patient patientToDelete = tvPatientDetails.getSelectionModel().getSelectedItem();
            patientsDetails.remove(patientToDelete);
            tvPatientDetails.setItems(patientsDetails);
        } else {
            // Không thực hiện việc xóa dòng
        }
    }

    @FXML
    void printWaitingSlip(MouseEvent event) {
        Patient patientToPrint = tvPatientDetails.getSelectionModel().getSelectedItem();
        System.out.println(patientToPrint);
        int patientNumber =  tvPatientDetails.getSelectionModel().getSelectedIndex()+ 1;
        String path = STR."STT\{patientNumber}.pdf";
        if(patientToPrint != null)
        {
            try {
                PdfWriter writer = new PdfWriter(path);
                PdfDocument pdf = new PdfDocument(writer);
                pdf.setDefaultPageSize(PageSize.A6);
                Document document = new Document(pdf);

                // Add content to the PDF
                PdfFont font = PdfFontFactory.createFont("NotoSans-Regular.ttf", PdfEncodings.IDENTITY_H);
                PdfFont italicFont = PdfFontFactory.createFont("NotoSans-Regular.ttf", PdfEncodings.IDENTITY_H);

                document.add(new Paragraph("PHÒNG MẠCH TƯ")
                        .setFont(font)
                        .setFontSize(18)
                        .setTextAlignment(TextAlignment.CENTER));

                document.add(new Paragraph("GREEN CLINIC")
                        .setFont(font)
                        .setFontSize(18)
                        .setTextAlignment(TextAlignment.CENTER));

                document.add(new Paragraph("Ngày khám : "+ dpDate.getValue().format(dateTimeFormatter))
                        .setFont(italicFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER));

                document.add(new Paragraph(STR."Mã BN: \{patientToPrint.getPatientId()} ,Họ tên : \{patientToPrint.getPatientName()}")
                        .setFont(font)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER));

                // Add TableView data to PDF
                document.add(new Paragraph("STT: " + patientNumber)
                        .setFont(font)
                        .setFontSize(40)
                        .setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("Cảm ơn quý khách đã tín nhiệm")
                        .setFont(font)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER));
                document.close();
                File file = new File(path);
                Desktop.getDesktop().open(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

package com.example.privateclinic.Controllers;

import com.example.privateclinic.Models.Medicine;
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
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    private TableColumn<Patient, String> tcDoctor;

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

    public TableView<Patient> getTvPatientDetails() {
        return tvPatientDetails;
    }

    public void setTvPatientDetails(TableView<Patient> tvPatientDetails) {
        this.tvPatientDetails = tvPatientDetails;
    }

    @FXML
    private TextArea taPatientAddress;

    private final PatientDAO patientDAO = new PatientDAO();

    private ObservableList<Patient> patientsDetails;
    private ObservableList<Patient> patients;

    public ObservableList<Patient> getPatientsDetails() {
        return patientsDetails;
    }

    public void setPatientsDetails(ObservableList<Patient> patientsDetails) {
        this.patientsDetails = patientsDetails;
    }

    public ObservableList<Patient> getPatients() {
        return patients;
    }

    final private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    Stage stagePatientData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDatePickerToday();
        validatePhoneNumber();
        searchPatientByIdAndByName();
        setPatientList(Date.valueOf(dpDate.getValue()));
        setPatientDetailList(Date.valueOf(dpDate.getValue()));
        setTableViewByDate();
        setOnOffAddDeleteBtn();
        setTableRowFactory(tvPatientDetails,patientDAO);
    }

    public void setTableRowFactory(TableView<Patient> tvPatientDetails, PatientDAO patientDAO) {
        tvPatientDetails.setRowFactory(tv -> {
            TableRow<Patient> row = new TableRow<Patient>() {
                @Override
                protected void updateItem(Patient patient, boolean empty) {
                    super.updateItem(patient, empty);
                    if (patient == null || empty) {
                        setStyle("");
                    } else {
                        // Kiểm tra nếu bác sĩ đã được tải trước đó
                        if (patient.getDoctor() != null) {
                            setStyle("-fx-background-color: yellow;");
                        } else {
                            // Chạy việc lấy dữ liệu từ cơ sở dữ liệu trong một luồng riêng
                            new Thread(() -> {
                                String doctor = patientDAO.getDoctor(patient.getReceptionId());
                                // Cập nhật giao diện người dùng trong Platform.runLater
                                Platform.runLater(() -> {
                                    if (doctor != null) {
                                        patient.setDoctor(doctor);
                                        setStyle("-fx-background-color: yellow;");
                                        // Thông báo thay đổi cho TableView
                                        tvPatientDetails.refresh();
                                    } else {
                                        setStyle(""); // Reset style if no doctor is found
                                    }
                                });
                            }).start();
                        }
                    }
                }
            };

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Patient clickedPatient = row.getItem();
                    // Xử lý khi click vào dòng (nếu cần)
                }
            });

            return row;
        });
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
        ObservableList<Patient> patientList = FXCollections.observableArrayList(patientDAO.getPatientsByDateReception(Date.valueOf(dpDate.getValue())));
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
    void addPatientFromDB(MouseEvent event) {
        if(stagePatientData==null)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/PatientCategory.fxml"));
            stagePatientData=createStage(loader);
            PatientCategoryController patientCategoryController = loader.getController();
            patientCategoryController.setController(this);
        }
        else
        {
            stagePatientData.toFront();
        }
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
            String patientGender = cbPatientGender.getValue();
            String patientPhoneNumber = tfPatientPhoneNumber.getText();
            String patientAddress = taPatientAddress.getText();
            Patient patient = new Patient(patientDAO.getNextPatientId(), patientName, patientGender
                    , patientPhoneNumber, patientBirthDay, patientAddress);
            patientDAO.addPatient(patient);
            int index = patients.size()+1;
            patientDAO.admitPatient(patient.getPatientId(),index);
            patient.setReceptionId(patientDAO.getReceptionId(patient.getPatientId()));
            patients.add(patient);
            clearDataField();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thêm thành công!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Chưa nhập đủ thông tin");
            alert.showAndWait();
        }
    }

    private boolean checkFillData() {
        return !tfPatientName.getText().isEmpty() && !tfPatientPhoneNumber.getText().isEmpty()
                && !tfPatientBirthDay.getText().isEmpty() && !taPatientAddress.getText().isEmpty()
                && !cbPatientGender.getValue().isBlank();
    }

    private void setPatientList(Date date) {
        tcNumber.setCellValueFactory(cellData -> new SimpleIntegerProperty(tvPatient.getItems().indexOf(cellData.getValue()) + 1).asObject());
        tcPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        tcPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        patients = patientDAO.getPatientsByDateReception(date);
        tvPatient.getItems().clear();
        tvPatient.setItems(patients);
    }

    private void setPatientDetailList(Date date) {
        tcNumberDetails.setCellValueFactory(cellData -> new SimpleIntegerProperty(tvPatientDetails.getItems().indexOf(cellData.getValue()) + 1).asObject());
        PatientCategoryController.bindPatientData(tcPatientIdDetail, tcPatientNameDetail, tcPatientGenderDetail, tcPatientBirthDetail, tcPatientPhoneNumberDetail, tcPatientAddressDetail);
        tcDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        patientsDetails = patientDAO.getPatientsByDateReception(date);
        tvPatientDetails.getItems().clear();
        tvPatientDetails.setItems(patientsDetails);
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
        int index = tvPatient.getItems().indexOf(patientToAdd);
        if(checkExist(patientToAdd)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Bệnh nhân đã tồn tại");
            alert.setContentText(STR."Bệnh nhân với ID \{patientToAdd.getPatientId()} đã tồn tại trong danh sách.");
            alert.showAndWait();
        }
        else if(patientToAdd!= null && !checkExist(patientToAdd)) {
            patientsDetails.add(patientToAdd);
            patientDAO.admitPatient(patientToAdd.getPatientId(), index);
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

    public void addPatientToList(Patient selected) {
        patients.add(selected);
        tvPatient.refresh();
    }

    private Stage createStage(FXMLLoader loader ) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        return stage;
    }
}

package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.HistoryDAO;
import com.example.privateclinic.Models.*;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
import java.util.*;
import java.util.List;

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
    private TextField tfPatientByPhone;


    @FXML
    private TextArea taPatientAddress;

    private final PatientDAO patientDAO = new PatientDAO();

    private ObservableList<Patient> patientsDetails;
    private ObservableList<Patient> patients;
    public AnchorPane lbl_header,lbl_header2;
    private double xOffset = 0;
    private double yOffset =0;
    Patient selectedPatient;
    public int MAX_PATIENT = patientDAO.getMaxPatient();
    private final HistoryDAO historyDAO = new HistoryDAO();

    final private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @FXML
    private TableColumn<Patient,String> tcPatientPhoneNumber;

    User user;
    public void initUser(User user) {
        this.user = user;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDatePickerToday();
        validatePhoneNumber();
        searchPatientByPhoneAndByName();
        setPatientList();
        setPatientDetailList(Date.valueOf(dpDate.getValue()));
        setTableViewByDate();
        setOnOffAddDeleteBtn();
        lbl_header.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        lbl_header.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage) lbl_header.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX()-xOffset);
            stage.setY(mouseEvent.getScreenY()-yOffset);
        });
        lbl_header2.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        lbl_header2.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage) lbl_header.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX()-xOffset);
            stage.setY(mouseEvent.getScreenY()-yOffset);
        });
        setTableRowFactory(tvPatientDetails,patientDAO);
        setPatientsDetailsRowClicked();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                patientDAO.checkForNewRecords(tvPatientDetails,dpDate.getValue());
                System.out.println("check");
            }
        };
        // Đặt lịch để kiểm tra mỗi 30 giây
        timer.schedule(task, 0, 30000);


        tfPatientBirthDay.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // Mất tiêu điểm (focus)
                String input = tfPatientBirthDay.getText();
                String formattedDate = formatDateString(input);
                if (formattedDate != null) {
                    tfPatientBirthDay.setText(formattedDate);
                    if (!isValidDate(formattedDate)) {
                        showAlert("Invalid Date", "Ngày sinh không hợp lệ: " + formattedDate);
                        tfPatientBirthDay.setText("");
                    }
                } else {
                    showAlert("Invalid Input", "Vui lòng nhập ngày hợp lệ ở định dạng ddMMyyyy.!");
                    tfPatientBirthDay.setText("");
                }
            }
        });
    }


    private void setPatientList() {
        tcPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        tcPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        tcPatientPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("patientPhoneNumber"));
        ObservableList<Patient> patients = FXCollections.observableList(patientDAO.getAllPatients());
        tvPatient.setItems(patients);
    }

    private void setPatientsDetailsRowClicked() {
        tvPatientDetails.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Lấy phần tử được chọn
                selectedPatient = newSelection;
                // Thực hiện các thao tác với phần tử được chọn ở đây
                // Ví dụ: Hiển thị thông tin của phần tử được chọn, xử lý các thao tác khác...
            }
        });
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
                                String doctor = patient.getDoctor();
                                // Cập nhật giao diện người dùng trong Platform.runLater
                                Platform.runLater(() -> {
                                    if (doctor != "") {
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

                    Date selectedDate = Date.valueOf(dpDate.getValue());
                    setPatientDetailList(selectedDate);
                    searchPatientByPhoneAndByName();

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

    private void searchPatientByPhoneAndByName() {
        tfPatientByPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                tvPatient.setItems(patients);
            } else {

                ObservableList<Patient> filteredList = FXCollections.observableArrayList();
                for (Patient patient : patients) {
                    if (patient.getPatientPhoneNumber().startsWith(newValue)) {
                        filteredList.add(patient);
                    }
                }
                tvPatient.setItems(filteredList);
            }
        });

        tfPatientByName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                tvPatient.setItems(patients);
            } else {
                ObservableList<Patient> filteredList = FXCollections.observableArrayList();
                for (Patient patient : patients) {
                    if (patient.getPatientName().contains(newValue)) {
                        filteredList.add(patient);
                    }
                }
                tvPatient.setItems(filteredList);
            }
        });
    }

    private void validatePhoneNumber() {
        validatePhoneNumber(tfPatientByPhone);
        validatePhoneNumber(tfPatientPhoneNumber);
    }

    private void validatePhoneNumber(TextField tfPatientPhoneNumber) {
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
        if (tvPatientDetails.getItems().size() >= MAX_PATIENT) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION, STR."Đã tiếp nhận đủ \{MAX_PATIENT}  người");
            alert1.showAndWait();
        } else {
            if (checkFillData()) {
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
                int index = patientsDetails.size() + 1;
                patientDAO.admitPatient(patient.getPatientId(), index);
                patient.setReceptionId(patientDAO.getReceptionId(patient.getPatientId()));
                patients.add(patient);
                patientsDetails.add(patient);
                tvPatientDetails.refresh();
                tvPatient.refresh();
                clearDataField();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thêm thành công!");
                alert.showAndWait();
                History history = new History(user.getEmployee_id(), STR."Tiếp nhận ID: \{patient.getReceptionId()} - \{patient.getPatientName()}");
                historyDAO.addHistory(history);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Chưa nhập đủ thông tin");
                alert.showAndWait();
            }
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



    private void setPatientDetailList(Date date) {
        tcNumberDetails.setCellValueFactory(cellData -> new SimpleIntegerProperty(tvPatientDetails.getItems().indexOf(cellData.getValue()) + 1).asObject());
        tcPatientIdDetail.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        tcPatientNameDetail.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        tcPatientGenderDetail.setCellValueFactory(new PropertyValueFactory<>("patientGender"));
        tcPatientBirthDetail.setCellValueFactory(new PropertyValueFactory<>("patientBirth"));
        tcPatientPhoneNumberDetail.setCellValueFactory(new PropertyValueFactory<>("patientPhoneNumber"));
        tcPatientAddressDetail.setCellValueFactory(new PropertyValueFactory<>("patientAddress"));
        tcDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        patientsDetails = patientDAO.getPatientsByDateReception(date);
        if (patientsDetails != null && !patientsDetails.isEmpty()) {
            Platform.runLater(() -> {
                tvPatientDetails.getItems().clear();
                tvPatientDetails.setItems(patientsDetails);
            });
        } else {
            // Nếu danh sách dữ liệu rỗng, không cần cập nhật TableView
            tvPatientDetails.getItems().clear();
        }
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
        if (tvPatientDetails.getItems().size() >= MAX_PATIENT) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION, STR."Đã tiếp nhận đủ \{MAX_PATIENT}  người");
            alert1.showAndWait();
        } else {
            Patient patientToAdd = tvPatient.getSelectionModel().getSelectedItem();
            int index = tvPatient.getItems().indexOf(patientToAdd);
            if (checkExist(patientToAdd)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Bệnh nhân đã tồn tại");
                alert.setContentText(STR."Bệnh nhân với ID \{patientToAdd.getPatientId()} đã tồn tại trong danh sách.");
                alert.showAndWait();
            } else if (patientToAdd != null && !checkExist(patientToAdd)) {
                patientsDetails.add(patientToAdd);
                patientDAO.admitPatient(patientToAdd.getPatientId(), index);
                patientToAdd.setReceptionId(patientDAO.getReceptionId(patientToAdd.getPatientId()));
                History history = new History(user.getEmployee_id(), STR."Tiếp nhận ID: \{patientToAdd.getReceptionId()} - \{patientToAdd.getPatientName()}");
                historyDAO.addHistory(history);
            }
            tvPatientDetails.setItems(patientsDetails);
        }
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Xóa bệnh nhân",ButtonType.OK,ButtonType.CANCEL);
        alert.setContentText("Bạn chắc chắn muốn xóa ?");
        Optional<ButtonType> result = alert.showAndWait();
        if ( result.get() == ButtonType.OK) {
            tvPatientDetails.getItems().remove(selectedPatient);
        }
        patientDAO.deletePatientFromAdmit(selectedPatient.getPatientId());

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

    public void minimizeReception(MouseEvent mouseEvent) {
        Model.getInstance().getViewFactory().minimizeStage((Stage) btnClose.getScene().getWindow());
    }

    private String formatDateString(String input) {
        if (input.matches("\\d{8}")) {
            String day = input.substring(0, 2);
            String month = input.substring(2, 4);
            String year = input.substring(4, 8);
            String formattedDate = day + "/" + month + "/" + year;
            return formattedDate;
        } else if (input.matches("\\d{6}")) {
            String day = input.substring(0, 2);
            String month = input.substring(2, 4);
            String year = "20" + input.substring(4, 6);
            String formattedDate = day + "/" + month + "/" + year;
            return formattedDate;
        }
        return null;
    }

    private boolean isValidDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.ExaminationHistoryDAO;
import com.example.privateclinic.Models.*;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.example.privateclinic.Controllers.ExaminationController.removeAccentsAndSpaces;

public class PatientController implements Initializable {
    public ComboBox<Integer> cbYear;
    public ComboBox<Integer> cbMonth;
    public ComboBox<Integer> cbDay;
    public TableView<ExaminationHistory> patientExaminationTable;
    public TableColumn<ExaminationHistory,Integer> patientSttColumn;
    public TableColumn<ExaminationHistory,String> symptomsColumn;
    public TableColumn<ExaminationHistory,String> diseaseMainColumn;

    public TableColumn<ExaminationHistory,Integer> patientIdColumn;
    public TableColumn<ExaminationHistory,String> patientNameColumn;
    public TableColumn<ExaminationHistory,String>  patientPhoneColumn;
    public TableColumn<ExaminationHistory,String>  patientExamDayColumn;
    public TableColumn<ExaminationHistory,String>  patientDoctorColumn;
    public TableColumn<ExaminationHistory,Void> actionColumn;
    public TextField tfPatientByIdOrName;
    public Label lbl_noExaminationResult;
    public Text lblDay;
    public HBox hboxDateToDate,hboxDate;
    public DatePicker dateTimePickerPayslipStart;
    public DatePicker dateTimePickerPayslipEnd;
    public MenuButton menuItemFilter;
    public Pane paneProgress;
    public TableColumn<ExaminationHistory,Void> printColumn;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    ExaminationHistoryDAO examinationHistoryDAO = new ExaminationHistoryDAO();
    ObservableList<ExaminationHistory> examinationHistories = FXCollections.observableArrayList();
    private String _message;

    public void handleMenuDayMonthYear(ActionEvent event) {
        hboxDate.setVisible(true);
        hboxDateToDate.setVisible(false);
        cbDay.setVisible(true);
        cbDay.setValue(LocalDateTime.now().getDayOfMonth());
        menuItemFilter.setText("Ngày/tháng/năm");
        LoadAllData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
    }

    public void handleMenuMonthYear(ActionEvent event) {
        hboxDate.setVisible(true);
        hboxDateToDate.setVisible(false);
        cbDay.setVisible(false);
        cbDay.setValue(0);
        lblDay.setVisible(false);
        menuItemFilter.setText("Tháng/năm");
        LoadAllData(0,cbMonth.getValue(),cbYear.getValue());
        SearchPatient(tfPatientByIdOrName.getText().trim());
    }

    public void handlePickDay(ActionEvent event) {
        LoadAllData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
        SearchPatient(tfPatientByIdOrName.getText().trim());
    }

    public void handlePickMonth(ActionEvent event) {
        if(cbDay.isVisible()) LoadAllData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
        else LoadAllData(0,cbMonth.getValue(),cbYear.getValue());
        SearchPatient(tfPatientByIdOrName.getText().trim());
    }

    public void handlePickYear(ActionEvent event) {
        if(cbDay.isVisible()) LoadAllData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
        else LoadAllData(0,cbMonth.getValue(),cbYear.getValue());
        SearchPatient(tfPatientByIdOrName.getText().trim());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SetUp();
        SetUpTable();
        LoadAllData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
    }

    private void LoadAllData(int day,int month,int year) {
        paneProgress.setVisible(true);
        new Thread(()->{
            try {
                examinationHistories=examinationHistoryDAO.getAllHistoryExamination(day,month,year);
                patientExaminationTable.setItems(examinationHistories);
                Platform.runLater(() -> {
                    if (!examinationHistories.isEmpty()) {
                        lbl_noExaminationResult.setVisible(false);
                    } else {
                        lbl_noExaminationResult.setVisible(true);
                        if(cbDay.isVisible()) lbl_noExaminationResult.setText("Không có kết quả vào ngày "+cbDay.getValue()+" tháng "+cbMonth.getValue()+" năm "+cbYear.getValue()+" ");
                        else lbl_noExaminationResult.setText("Không có kết quả vào tháng "+cbMonth.getValue()+" năm "+cbYear.getValue()+" ");
                    }
                });
            } finally {
                Platform.runLater(() -> {
                    paneProgress.setVisible(false);
                });
            }
        }).start();
    }

    private void SetUpTable() {
        patientSttColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(patientExaminationTable.getItems().indexOf(cellData.getValue()) + 1).asObject());
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        symptomsColumn.setCellValueFactory(new PropertyValueFactory<>("trieuChung"));
        diseaseMainColumn.setCellValueFactory(new PropertyValueFactory<>("tenBenh"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        patientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("patientPhoneNumber"));
        patientExamDayColumn.setCellValueFactory(new PropertyValueFactory<>("ngay"));
        patientDoctorColumn.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
        actionColumn.setCellFactory(new Callback<TableColumn<ExaminationHistory, Void>, TableCell<ExaminationHistory, Void>>() {
            @Override
            public TableCell<ExaminationHistory, Void> call(final TableColumn<ExaminationHistory, Void> param) {
                final TableCell<ExaminationHistory, Void> cell = new TableCell<ExaminationHistory, Void>() {

                    private final Button btn = new Button("Xem lịch sử");
                    {
                        btn.setOnAction(event -> {
                            ExaminationHistory data = getTableView().getItems().get(getIndex());
                            Model.getInstance().getViewFactory().showHistoryExamination(data.getPatient(),null,false,data);
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        });
        printColumn.setCellFactory(new Callback<TableColumn<ExaminationHistory, Void>, TableCell<ExaminationHistory, Void>>() {
            @Override
            public TableCell<ExaminationHistory, Void> call(final TableColumn<ExaminationHistory, Void> param) {
                final TableCell<ExaminationHistory, Void> cell = new TableCell<ExaminationHistory, Void>() {

                    private final MenuButton menuButton = new MenuButton("In");
                    private final MenuItem printToa = new MenuItem("In toa");
                    private final MenuItem printBangKe = new MenuItem("In bảng kê");

                    {

                        printToa.setOnAction(event -> {
                            try {
                                if(ShowYesNoAlert("in toa thuốc")==JOptionPane.YES_OPTION) {
                                    ExaminationHistory data = getTableView().getItems().get(getIndex());
                                    printToaThuoc(data);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        printBangKe.setOnAction(event -> {
                            ExaminationHistory data = getTableView().getItems().get(getIndex());
                            LocalDate datetime= LocalDate.parse(data.getExamination().getNgay().split(" ")[0],dateFormatter);
                            if(LocalDate.now().getDayOfMonth() - datetime.getDayOfMonth() > 7) {
                                showAlert("Warning","Quá 7 ngày kể từ ngày khám không thể in bảng kê");
                                printBangKe.setDisable(true);
                                return;
                            }
                            try {
                                if(ShowYesNoAlert("in bảng kê")==JOptionPane.YES_OPTION) {
                                    printBangke(data);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        menuButton.getItems().addAll(printToa, printBangKe);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(menuButton);
                        }
                    }
                };
                return cell;
            }
        });
    }

    private void SetUp() {
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();
        int currentDay = LocalDate.now().getDayOfMonth();
        ObservableList<Integer> years = FXCollections.observableArrayList();
        ObservableList<Integer> months = FXCollections.observableArrayList();
        ObservableList<Integer> days = FXCollections.observableArrayList();
        for (int i = 0; i < currentYear -1999; i++) {
            years.add(currentYear - i);
        }
        for (int i = 1; i <=12;i++)
        {
            months.add(i);
        }
        for(int i =1 ; i<31;i++) {
            days.add(i);
        }
        cbYear.setValue(currentYear);
        cbMonth.setValue(currentMonth);
        cbDay.setValue(currentDay);
        cbYear.setItems(years);
        cbMonth.setItems(months);
        cbDay.setItems(days);
        tfPatientByIdOrName.textProperty().addListener((observableValue, oldValue,newValue) -> {
            String searchString = newValue.trim();
            SearchPatient(searchString);
        } );
        dateTimePickerPayslipStart.setConverter(new StringConverter<LocalDate>() {
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
        dateTimePickerPayslipEnd.setConverter(new StringConverter<LocalDate>() {
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
        dateTimePickerPayslipStart.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue) {
                if(dateTimePickerPayslipStart.getValue()==null ||dateTimePickerPayslipEnd.getValue()==null) {
                    patientExaminationTable.getItems().clear();
                    return;
                }
                if(checkPropertie()) {
                    LoadAllDataDateToDate(Date.valueOf(dateTimePickerPayslipStart.getValue()), Date.valueOf(dateTimePickerPayslipEnd.getValue()));
                    tfPatientByIdOrName.clear();
                } else {
                    showAlert("Warning",_message);
                    patientExaminationTable.getItems().clear();
                    dateTimePickerPayslipStart.setValue(oldValue);
                }
            }
        });
        dateTimePickerPayslipEnd.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue) {
                if(dateTimePickerPayslipStart.getValue()==null ||dateTimePickerPayslipEnd.getValue()==null)  {
                    patientExaminationTable.getItems().clear();
                    return;
                }
                if(checkPropertie()) {
                    LoadAllDataDateToDate(Date.valueOf(dateTimePickerPayslipStart.getValue()), Date.valueOf(dateTimePickerPayslipEnd.getValue()));
                    tfPatientByIdOrName.clear();
                } else {
                    showAlert("Warning",_message);
                    patientExaminationTable.getItems().clear();
                    dateTimePickerPayslipEnd.setValue(oldValue);
                }
            }
        });
    }

    private void SearchPatient(String searchString) {
        if(searchString.isEmpty()) {
            patientExaminationTable.setItems(examinationHistories);
        } else {
            String lowerCase = normalizeString(searchString.toLowerCase());
            ObservableList<ExaminationHistory> listResult = FXCollections.observableArrayList(
                    examinationHistories.stream()
                            .filter(examinationHistory ->
                                    normalizeString(String.valueOf(examinationHistory.getPatientId()).toLowerCase()).startsWith(lowerCase) ||
                                            normalizeString(examinationHistory.getPatientName().toLowerCase()).contains(lowerCase))
                            .collect(Collectors.toList())
            );
            patientExaminationTable.setItems(listResult);
            if (!listResult.isEmpty()) {
                lbl_noExaminationResult.setVisible(false);
            } else {
                lbl_noExaminationResult.setVisible(true);
            }
        }
    }

    private void LoadAllDataDateToDate(Date dateFrom, Date dateTo) {
        paneProgress.setVisible(true);
        new Thread(()->{
            try {
                Thread.sleep(1000);
                    examinationHistories = examinationHistoryDAO.getAllPayslipDayToDay(dateFrom,dateTo);
                    patientExaminationTable.setItems(examinationHistories);
                Platform.runLater(() -> {
                    if (!examinationHistories.isEmpty()) {
                        lbl_noExaminationResult.setVisible(false);
                    } else {
                        lbl_noExaminationResult.setVisible(true);
                        lbl_noExaminationResult.setText("Không có kết quả vào từ "+dateTimePickerPayslipStart.getValue()+" đến "+dateTimePickerPayslipEnd.getValue()+" ");
                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                Platform.runLater(() -> {
                    paneProgress.setVisible(false);
                });
            }
        }).start();
    }

    private boolean checkPropertie() {
        if(dateTimePickerPayslipStart.getValue().isAfter(dateTimePickerPayslipEnd.getValue()))
        {
            _message= " Ngày bắt đầu không quá ngày kết thúc!";
            return false;
        }
        return true;
    }
    public void handleDoubleClick(MouseEvent mouseEvent) {
        ExaminationHistory examinationHistoryClicked = patientExaminationTable.getSelectionModel().getSelectedItem();
        if(mouseEvent.getClickCount()==2) {
            Model.getInstance().getViewFactory().showHistoryExamination(examinationHistoryClicked.getPatient(),null,false,examinationHistoryClicked);
        }
    }
    private static String normalizeString(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public void handleDateToDate(ActionEvent event) {
        hboxDateToDate.setVisible(true);
        hboxDate.setVisible(false);
        menuItemFilter.setText("Từ ngày đến ngày");
    }
    private void showAlert(String tilte,String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tilte);
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }

    public void handleAll(ActionEvent event) {
        hboxDate.setVisible(false);
        hboxDateToDate.setVisible(false);
        paneProgress.setVisible(true);
        menuItemFilter.setText("Tất cả");
        LoadAllData(0,0,0);
        tfPatientByIdOrName.clear();
    }
    private void printToaThuoc(ExaminationHistory examinationHistory) throws IOException {
        Document document = new Document();
        Patient patient = examinationHistory.getCustomer();
        ObservableList<Receipt> detailReceipt = examinationHistory.getPrescribe();
        Examination examination = examinationHistory.getExamination();
        if(detailReceipt.isEmpty()) {
            showAlert("Warning","Bệnh nhân " + patient.getPatientName() +" với mã TN: " +examination.getMatn()+" không mua thuốc tại phòng khám");
            return;
        }
        String path = STR."\{removeAccentsAndSpaces(patient.getPatientName())}_toa.pdf";
        try {
            String maTenBenhPhu="";
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            String fontPath = "notosans-regular.ttf";
            BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font boldFont = new Font(baseFont, 13, Font.BOLD);
            Font titleBoldFont = new Font(baseFont, 17, Font.BOLD);
            Font regularFont = new Font(baseFont, 14);
            Font footerbold = new Font(baseFont, 13,Font.BOLD);

            document.add(new Paragraph("PHÒNG MẠCH TƯ", boldFont));
            document.add(new Paragraph("UIT CLINIC", boldFont));
            document.add(new Paragraph("Email: greenuitk17@gmail.com", footerbold));
            document.add(new Paragraph("Số điện thoại: 1900 1555", footerbold));
            document.add(new Paragraph("Địa chỉ:  136, Linh Trung, Thủ Đức, TP Thủ Đức", regularFont));
            document.add(new Paragraph("                                                ĐƠN THUỐC", titleBoldFont));
            document.add(new Paragraph(STR."Mã BN:  \{patient.getPatientId()} - Họ tên:  \{patient.getPatientName()} - Ngày sinh:  \{ patient.getPatientBirth()} - Giới tính:  \{(patient.getPatientGender())}",regularFont));
            document.add(new Paragraph(STR."Triệu chứng:  \{examination.getTrieuChung()}", regularFont));
            if(examination.getSubDisease().getMaBenh()>0) maTenBenhPhu = STR.";  Bệnh phụ:\{examination.getSubDisease().getMaBenh()} -\{examination.getSubDisease().getTenBenh()} (ICD:\{examination.getSubDisease().getMaICD()})";
            document.add(new Paragraph(STR."Chẩn đoán: Bệnh chính: \{examination.getMainDisease().getMaBenh()} - \{examination.getMainDisease().getTenBenh()} (ICD:\{examination.getMainDisease().getMaICD()})"+maTenBenhPhu, regularFont));
            document.add(new Paragraph("\n                                              THUỐC ĐIỀU TRỊ", titleBoldFont));
            int index=0,maxDay=0;
            for(Receipt prescribe:detailReceipt){
                index++;
                document.add(new Paragraph(prescribe.getSothuTu() + ") " + STR."\{prescribe.getTenThuoc()}                                                                                         SL: " +prescribe.getSoLuong() , boldFont));
                String sang="",trua="",chieu="",toi="";
                if(prescribe.getSang()>0) sang =STR."Sáng: \{prescribe.getSang()} viên";
                if(prescribe.getTrua()>0) trua =STR."Trưa: \{prescribe.getTrua()} viên";
                if(prescribe.getChieu()>0) chieu =STR."Chiều: \{prescribe.getChieu()} viên";
                if(prescribe.getToi()>0) toi =STR."Tối: \{prescribe.getToi()} viên";
                document.add(new Paragraph("        Uống:    "+sang+"          "+trua+"          "+chieu+"             "+toi+"" , regularFont));
                if(prescribe.getNote()!=null)
                    document.add(new Paragraph("     Nhắc:"+prescribe.getNote() , regularFont));
                if(prescribe.getNgay()>maxDay) maxDay = prescribe.getNgay();
            }
            LocalDate date = LocalDate.now();
            document.add(new Paragraph(STR."\nLời dặn: \{examination.getLuuy()}  ", boldFont));
            document.add(new Paragraph(       "                                                                                            Ngày "+date.getDayOfMonth()+ " tháng " + date.getMonthValue() + " năm " + date.getYear(), boldFont));
            document.add(new Paragraph(STR."Cộng khoản:     " + index +"                                                                        Bác sĩ/Y sĩ khám bệnh", boldFont));
            document.add(new Paragraph(STR."Toa uống:       " + maxDay +" ngày" +"                                                              (Ký, ghi rõ họ tên)" , regularFont));
            document.add(new Paragraph(STR."\n\nKhám lại mang theo đơn này." , footerbold));
            document.add(new Paragraph(STR."Ngày giờ in: " +LocalDateTime.now().format(formatter) +"                                                        BS." +examination.getTenNhanVien(), footerbold));

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            File file = new File(path);
            Desktop.getDesktop().open(file);
            document.close();
        }
    }
    private void printBangke(ExaminationHistory examinationHistory) throws IOException {
        Document document = new Document();
        Patient patient = examinationHistory.getCustomer();
        ObservableList<Receipt> detailReceipt = examinationHistory.getPrescribe();
        Examination examination = examinationHistory.getExamination();
        int examfree = examination.getTienkham();
        int prescibefree = examination.getTienthuoc();
        String path = STR."\{removeAccentsAndSpaces(patient.getPatientName())}_bangke.pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            String fontPath = "notosans-regular.ttf";
            BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font boldFont = new Font(baseFont, 13, Font.BOLD);
            Font titleBoldFont = new Font(baseFont, 17, Font.BOLD);
            Font regularFont = new Font(baseFont, 14);
            Font smallFont = new Font(baseFont, 12);
            Font footerbold = new Font(baseFont, 13, Font.BOLD);

            document.add(new Paragraph("PHÒNG MẠCH TƯ", boldFont));
            document.add(new Paragraph("UIT CLINIC", boldFont));
            document.add(new Paragraph("Email: greenuitk17@gmail.com", footerbold));
            document.add(new Paragraph("Số điện thoại: 1900 1555", footerbold));
            document.add(new Paragraph("Địa chỉ:  136, Linh Trung, Thủ Đức, TP Thủ Đức", regularFont));
            document.add(new Paragraph("                                    BẢNG KÊ CHI PHÍ KHÁM BỆNH", titleBoldFont));
            document.add(new Paragraph("I. Phần hành chính:", boldFont));
            document.add(new Paragraph(STR."Mã BN:  \{patient.getPatientId()} - Họ tên:  \{patient.getPatientName()} - Ngày sinh:  \{patient.getPatientBirth()} - Giới tính:  \{patient.getPatientGender()}", regularFont));
            document.add(new Paragraph(STR."Triệu chứng:  \{examination.getTrieuChung()}", regularFont));
            String maTenBenhPhu = "";
            if (examination.getSubDisease().getMaBenh()>0)
                maTenBenhPhu = STR.";   Bệnh phụ: (\{examination.getSubDisease().getMaBenh()}) - \{examination.getSubDisease().getTenBenh()} (ICD:\{examination.getSubDisease().getMaICD()}) ";
            document.add(new Paragraph(STR."Chẩn đoán:   Bệnh chính: \{examination.getMainDisease().getMaBenh()} - \{examination.getMainDisease().getTenBenh()} (ICD:\{examination.getMainDisease().getMaICD()})" + maTenBenhPhu, regularFont));
            document.add(new Paragraph("\nII. Phần chi phí khám bệnh: ", boldFont));
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] columnWidths = {1.5f, 0.5f, 0.5f, 0.5f, 0.5f};
            float[] rowWidths = {0.2f, 0.4f, 0.2f, 0.2f, 0.2f};
            table.setWidths(columnWidths);

            PdfPCell cell = new PdfPCell(new Paragraph("Nội dung", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("ĐVT", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Số lượng", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Đơn giá", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Thành tiền", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            table.addCell(new Paragraph("1. Khám bệnh:", boldFont));
            table.addCell(new Paragraph("", regularFont));
            table.addCell(new Paragraph("", regularFont));
            table.addCell(new Paragraph("", regularFont));
            table.addCell(new Paragraph(String.valueOf(examfree), boldFont));

            table.addCell(new Paragraph("   1. Công khám", regularFont));
            table.addCell(new Paragraph("Lần", regularFont));
            table.addCell(new Paragraph("1", regularFont));
            table.addCell(new Paragraph("35000", regularFont));
            table.addCell(new Paragraph(String.valueOf((int)examfree), regularFont));

            table.addCell(new Paragraph("2. Thuốc", boldFont));
            table.addCell(new Paragraph("", boldFont));
            table.addCell(new Paragraph("", boldFont));
            table.addCell(new Paragraph("", boldFont));
            table.addCell(new Paragraph(String.valueOf(prescibefree), boldFont));
            if(prescibefree!=0) {
                for (Receipt prescribe : detailReceipt) {
                    table.addCell(new Paragraph("   "+prescribe.getSothuTu()+STR.". \{prescribe.getTenThuoc()}", regularFont));
                    table.addCell(new Paragraph(String.valueOf(prescribe.getTenDonViTinh()), regularFont));
                    table.addCell(new Paragraph(String.valueOf(prescribe.getSoLuong()), regularFont));
                    table.addCell(new Paragraph(String.valueOf((int)prescribe.getDonGia()), regularFont));
                    table.addCell(new Paragraph(String.valueOf((int)prescribe.getThanhTien()), regularFont));
                }
            } else {
                table.addCell(new Paragraph("Bệnh nhân không không mua thuốc từ phòng khám", smallFont));
                table.addCell(new Paragraph("", regularFont));
                table.addCell(new Paragraph("", regularFont));
                table.addCell(new Paragraph("", regularFont));
                table.addCell(new Paragraph("", regularFont));
            }
            table.addCell(new Paragraph("Cộng", boldFont));
            table.addCell(new Paragraph("", boldFont));
            table.addCell(new Paragraph("", boldFont));
            table.addCell(new Paragraph("", boldFont));
            int total =(int)(examfree+prescibefree);
            table.addCell(new Paragraph(String.valueOf(total), boldFont));
            for (int i = 0; i < rowWidths.length; i++) {
                table.getRow(i).setMaxHeights(rowWidths[i] * table.getTotalHeight());
            }
            document.add(table);
            document.add(new Paragraph("\nTổng chi phí lần khám bệnh (làm tròn đến đơn vị đồng):            "+total+" đ",boldFont));
            LocalDate date = LocalDate.now();
            document.add(new Paragraph(STR."\n                                                                                       Ngày "+date.getDayOfMonth()+ " tháng " + date.getMonthValue() + " năm " + date.getYear(), regularFont));
            document.add(new Paragraph(STR."                           NGƯỜI LẬP BẢNG KÊ                                    NGƯỜI THU TIỀN", boldFont));
            document.add(new Paragraph(STR."                            (Ký, ghi rõ họ tên)                                 (Ký, ghi rõ họ tên) ", regularFont));



        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            File file = new File(path);
            Desktop.getDesktop().open(file);
            document.close();
        }
    }
    private int ShowYesNoAlert(String string) {
        return JOptionPane.showConfirmDialog(null, "Có phải bạn muốn " + string + "?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
    }
}

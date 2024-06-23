package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.*;
import com.example.privateclinic.Models.*;
import com.jfoenix.controls.JFXRadioButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Comparator;
import java.util.ResourceBundle;

public class ExaminationHistoryController implements Initializable {

    public Pane btnClose;
    public TableView<Examination> tbl_examHistory;
    public TableColumn<Examination,String> col_ngayvaoHistory;
    public Button btnReuse,btnPrint;
    public ComboBox<Integer> cbYear;
    public TableView<Receipt> tbl_kethuocHistory;
    public TableColumn<Receipt,Integer> col_stt;
    public TableColumn<Receipt,String> col_tenThuoc;
    public TableColumn<Receipt,String> col_donVi;
    public TableColumn<Receipt,String> col_dangThuoc;
    public TableColumn<Receipt,String> col_cachDung;
    public TableColumn<Receipt,String> col_Ngay;
    public TableColumn<Receipt,String> col_Sang;
    public TableColumn<Receipt,String> col_Trua;
    public TableColumn<Receipt,String> col_Chieu;
    public TableColumn<Receipt,String> col_Toi;
    public TableColumn<Receipt,String> col_soLuong;
    public TableColumn<Receipt,String> col_donGia;
    public TableColumn<Receipt,String> col_thanhTien;
    public TableColumn<Receipt,String> col_note;
    public TextField tf_maBenhNhanHistory;
    public TextField tf_tenBenhNhanHistory;
    public JFXRadioButton rad_menHistory;
    public JFXRadioButton rad_womenHistory;
    public TextField tf_ngaysinhHistory;
    public TextField tf_trieuChungHistory;
    public TextField tf_maBenhChinhHistory;
    public TextField tf_tenBenhChinhHistory;
    public TextArea ta_luuYHistory;
    public TextField tf_maBenhPhuHistory;
    public TextField tf_tenBenhPhuHistory;
    public TextField tf_maTiepNhan;
    public TextField tf_phoneNumber;
    public TextField tf_tenbs;
    public Text txtReceiptId;
    public TextField tf_soThuTu;
    public Label lbl_noPrescribe;
    public VBox labelButton;
    public MenuButton menuItemFilter;
    public Button btnOtherHistory;
    private Patient patient;
    public Text txtPrescribeFee;
    public Text txtExamFee;
    public Text txtTotalFee;
    public TextField tf_ngayKham;
    int currentYear;
     PatientDAO patientDAO;
     ExaminationHistoryDAO examinationHistoryDAO;
     ObservableList<ExaminationHistory> listExaminationsHistory_detail;
     ObservableList<Examination> listExaminationsHistory;
     ExaminationHistory examHistoryBeforeClicked, examinationHistory_init;
    ExaminationController examinationController ;
    public Pane paneHeader;
    private double xOffset = 0;
    private double yOffset =0;
    boolean firstAccess =true;
    int index ;
    public void initData(Patient _patient, ExaminationController _examinationController,boolean fromWaitingListOrBoolSub,ExaminationHistory examinationHistory_init)
    {
        patient = _patient;
        tf_maBenhNhanHistory.setText(String.valueOf(patient.getPatientId()));
        tf_tenBenhNhanHistory.setText(patient.getPatientName());
        tf_ngaysinhHistory.setText(String.valueOf(patient.getPatientBirth()));
        tf_phoneNumber.setText(patient.getPatientPhoneNumber());
        tf_maTiepNhan.setText(String.valueOf(patient.getReceptionId()));
        tf_soThuTu.setText(String.valueOf(patient.getNumber()));
        if(patient.getPatientGender().equals("Nam"))
            rad_menHistory.setSelected(true);
        else rad_womenHistory.setSelected(true);

        if(_examinationController!=null ){
            index=1;
            ClearAll();
            menuItemFilter.setVisible(true);
            cbYear.setVisible(true);
            btnOtherHistory.setVisible(false);
            labelButton.setVisible(true);
            if(fromWaitingListOrBoolSub) btnReuse.setVisible(true); else btnReuse.setVisible(false);
            examinationController = _examinationController;
            LoadHistory_Date(cbYear.getValue());
        } else
        {
            index = 2;
            ClearAll();
            menuItemFilter.setVisible(false);
            cbYear.setVisible(false);
            btnOtherHistory.setVisible(true);
            ObservableList<Examination> list = FXCollections.observableArrayList();
            list.add(examinationHistory_init.getExamination());
            tbl_examHistory.setItems(list);
            FillData(examinationHistory_init);
            labelButton.setVisible(false);
            this.examinationHistory_init=examinationHistory_init;
        }
    }
    public void close(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //inititalation
        patientDAO = new PatientDAO();
        examinationHistoryDAO = new ExaminationHistoryDAO();
        listExaminationsHistory = FXCollections.observableArrayList();
        currentYear= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        cbYear.setValue(currentYear);
        SetUp();
        setUpTableView();
        setAction();
    }

    private void SetUp() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int i = 0; i < currentYear -1950; i++) {
            years.add(1951 + i);
        }
        years.sort(Comparator.reverseOrder());
        cbYear.setItems(years);
        paneHeader.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        paneHeader.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage) paneHeader.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX()-xOffset);
            stage.setY(mouseEvent.getScreenY()-yOffset);
        });
    }

    private void setAction() {
        cbYear.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer oldValue, Integer newValue) {
                if(!LoadHistory_Date(newValue)) {
                    cbYear.setValue(oldValue);
                }
            }
        });
        tbl_examHistory.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(tbl_examHistory.getSelectionModel().getSelectedItem()==null) return;
                if(mouseEvent.getClickCount()==2) {
                    examHistoryBeforeClicked = FindExamHistoryByExamination(tbl_examHistory.getSelectionModel().getSelectedItem());
                    if (examHistoryBeforeClicked!=null) FillData(examHistoryBeforeClicked);
                }
            }
        });
        btnReuse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(examHistoryBeforeClicked!=null) {
                    examinationController.initFromHistory(examHistoryBeforeClicked,false);
                    Model.getInstance().getViewFactory().closeStage((Stage) btnClose.getScene().getWindow());
                }
            }
        });
        btnPrint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (examHistoryBeforeClicked != null) {
                    examinationController.initFromHistory(examHistoryBeforeClicked, true);
                    Model.getInstance().getViewFactory().closeStage((Stage) btnClose.getScene().getWindow());
                }
            }
        });
    }

    private void FillData(ExaminationHistory examHistoryBeforeClicked) {
        Examination examination = examHistoryBeforeClicked.getExamination();
        ObservableList<Receipt> detailReceipts = examHistoryBeforeClicked.getPrescribe();
        tf_trieuChungHistory.setText(examination.getTrieuChung());
        tf_maBenhChinhHistory.setText(String.valueOf(examination.getMainDisease().getMaBenh()));
        tf_tenBenhChinhHistory.setText(examination.getMainDisease().getTenBenh());
        tf_maBenhPhuHistory.setText(String.valueOf(examination.getSubDisease().getMaBenh()));
        tf_tenBenhPhuHistory.setText(examination.getSubDisease().getTenBenh());
        tf_tenbs.setText("BS."+examination.getTenNhanVien());
        tf_ngayKham.setText(examination.getNgay());
        ta_luuYHistory.setText(examination.getLuuy());
        txtReceiptId.setText(String.valueOf(examination.getMahd()));
        tbl_kethuocHistory.setItems(detailReceipts);
        if(detailReceipts.isEmpty())lbl_noPrescribe.setVisible(true);
        else lbl_noPrescribe.setVisible(false);
        txtExamFee.setText(String.valueOf(examination.getTienkham()));
        txtPrescribeFee.setText(String.valueOf(examination.getTienthuoc()));
        txtTotalFee.setText(String.valueOf(examination.getTienkham()+examination.getTienthuoc()));
    }
    public void ClearAll(){
        tf_trieuChungHistory.clear();
        tf_maBenhChinhHistory.clear();
        tf_tenBenhChinhHistory.clear();
        tf_maBenhPhuHistory.clear();
        tf_tenBenhPhuHistory.clear();
        tf_tenbs.clear();
        tf_ngayKham.clear();
        ta_luuYHistory.clear();
        txtReceiptId.setText("null");
        tbl_kethuocHistory.getItems().clear();
        lbl_noPrescribe.setVisible(true);
        lbl_noPrescribe.setText("Bạn chưa chọn ngày");
        txtExamFee.setText("0");
        txtPrescribeFee.setText("0");
        txtTotalFee.setText("0");
    }
    private void setUpTableView() {
        col_ngayvaoHistory.setCellValueFactory(new PropertyValueFactory<>("ngay"));

        col_stt.setCellValueFactory(new PropertyValueFactory<>("sothuTu"));
        col_tenThuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
        col_donVi.setCellValueFactory(new PropertyValueFactory<>("tenDonViTinh"));
        col_dangThuoc.setCellValueFactory(new PropertyValueFactory<>("tenDangThuoc"));
        col_cachDung.setCellValueFactory(new PropertyValueFactory<>("tenCachDung"));
        col_Ngay.setCellValueFactory(new PropertyValueFactory<>("ngay"));
        col_Sang.setCellValueFactory(new PropertyValueFactory<>("sang"));
        col_Trua.setCellValueFactory(new PropertyValueFactory<>("trua"));
        col_Chieu.setCellValueFactory(new PropertyValueFactory<>("chieu"));
        col_Toi.setCellValueFactory(new PropertyValueFactory<>("toi"));
        col_soLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        col_donGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        col_thanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        col_note.setCellValueFactory(new PropertyValueFactory<>("note"));
    }

    private boolean LoadHistory_Date(int year) {
        listExaminationsHistory.clear();
        listExaminationsHistory_detail=examinationHistoryDAO.getHistoryExaminationByYear(patient.getPatientId(),year);
        for(ExaminationHistory examinationHistory : listExaminationsHistory_detail) {
            listExaminationsHistory.add(examinationHistory.getExamination());
        }
        tbl_examHistory.setItems(listExaminationsHistory);
        if(!tbl_examHistory.getItems().isEmpty()) {
        } else {
            if (firstAccess) {
                showAlert("Warning",patient.getPatientName() +" không có trong dữ liệu đã khám!");
                Model.getInstance().getViewFactory().closeStage((Stage) btnReuse.getScene().getWindow());
            } else {
                showAlert("Warning", String.format("Năm %d về trước không ghi nhận bệnh nhân %s đến khám", cbYear.getValue(), patient.getPatientName()));
                return false;
            }

        }
        firstAccess=false;
        return true;
    }
    private ExaminationHistory FindExamHistoryByExamination(Examination examination)
    {
        for(ExaminationHistory examinationHistory : listExaminationsHistory_detail) {
            if(examinationHistory.getExamination().getMatn()==examination.getMatn()
                    &&examinationHistory.getExamination().getNgay().equals(examination.getNgay())) return examinationHistory;
        }
        return null;
    }
    private void showAlert(String tilte,String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tilte);
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }

    public void handleYear(ActionEvent event) {
        menuItemFilter.setText("Lọc theo năm");
        cbYear.setVisible(true);
        cbYear.setValue(LocalDate.now().getYear());
        LoadHistory_Date(cbYear.getValue());
    }

    public void handleAll(ActionEvent event) {
        menuItemFilter.setText("Tất cả");
        cbYear.setVisible(false);
        LoadHistory_Date(0);
    }

    public void handleOtherHistoryButton(ActionEvent event) {
        ClearAll();
        if(!btnOtherHistory.getText().equals("Quay lại"))
        {
            menuItemFilter.setVisible(true);
            menuItemFilter.setText("Tất cả");
            LoadHistory_Date(0);
            btnOtherHistory.setText("Quay lại");
        } else {
            ObservableList<Examination> list = FXCollections.observableArrayList();
            list.add(examinationHistory_init.getExamination());
            tbl_examHistory.setItems(list);
            cbYear.setVisible(false);
            menuItemFilter.setVisible(false);
            labelButton.setVisible(false);
            FillData(examinationHistory_init);
            btnOtherHistory.setText("Xem thêm lịch sử khác");
        }

    }
}

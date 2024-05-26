package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.CustomerDAO;
import com.example.privateclinic.DataAccessObject.ExaminationDAO;
import com.example.privateclinic.DataAccessObject.ExaminationHistoryDAO;
import com.example.privateclinic.DataAccessObject.PrescribeDAO;
import com.example.privateclinic.Models.*;
import com.jfoenix.controls.JFXRadioButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ResourceBundle;

public class ExaminationHistoryController implements Initializable {

    public Pane btnClose;
    public Spinner<Integer> spinnerYear;
    public TableView<Examination> tbl_examHistory;
    public TableColumn<Examination,String> col_ngayvaoHistory;
    public Button btnReuse;
    public TableView<Prescribe> tbl_kethuocHistory;
    public TableColumn<Prescribe,Integer> col_stt;
    public TableColumn<Prescribe,String> col_tenThuoc;
    public TableColumn<Prescribe,String> col_donVi;
    public TableColumn<Prescribe,String> col_dangThuoc;
    public TableColumn<Prescribe,String> col_cachDung;
    public TableColumn<Prescribe,String> col_Ngay;
    public TableColumn<Prescribe,String> col_Sang;
    public TableColumn<Prescribe,String> col_Trua;
    public TableColumn<Prescribe,String> col_Chieu;
    public TableColumn<Prescribe,String> col_Toi;
    public TableColumn<Prescribe,String> col_soLuong;
    public TableColumn<Prescribe,String> col_donGia;
    public TableColumn<Prescribe,String> col_thanhTien;
    public TextField tf_maBenhNhanHistory;
    public TextField tf_tenBenhNhanHistory;
    public JFXRadioButton rad_menHistory;
    public JFXRadioButton rad_womenHistory;
    public TextField tf_ngaysinhHistory;
    public TextField tf_trieuChungHistory;
    public TextField tf_maBenhChinhHistory;
    public TextField tf_tenBenhChinhHistory;
    public TextField tf_luuYHistory;
    public TextField tf_maBenhPhuHistory;
    public TextField tf_tenBenhPhuHistory;
    private Customer customer;
    int currentYear;
     CustomerDAO customerDAO;
     PrescribeDAO prescribeDAO;
     ExaminationHistoryDAO examinationHistoryDAO;
     ObservableList<ExaminationHistory> listExaminationsHistory_detail;
     ObservableList<Examination> listExaminationsHistory;
     ExaminationHistory examHistoryBeforeClicked;
    ExaminationController examinationController ;
    public void initData(Customer _customer, ExaminationController _examinationController)
    {
        this.customer = _customer;
        tf_maBenhNhanHistory.setText(String.valueOf(customer.getMaBN()));
        tf_tenBenhNhanHistory.setText(customer.getHoTen());
        tf_ngaysinhHistory.setText(String.valueOf(customer.getNgaySinh()));
        if(customer.getGioiTinh().equals("Nam"))
            rad_menHistory.setSelected(true);
        else rad_womenHistory.setSelected(true);
        examinationController = _examinationController;
        LoadHistory_Date(spinnerYear.getValue());

    }
    public void close(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //inititalation
        customerDAO = new CustomerDAO();
        examinationHistoryDAO = new ExaminationHistoryDAO();
        listExaminationsHistory = FXCollections.observableArrayList();
        currentYear= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, currentYear, currentYear);
        spinnerYear.setValueFactory(valueFactory);
        setUpTableView();
        setAction();

    }

    private void setAction() {
        spinnerYear.valueProperty().addListener((obs, oldValue, newValue) -> {
            LoadHistory_Date(newValue);
        });
        tbl_examHistory.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount()==2) {
                    examHistoryBeforeClicked = FindExamHistoryByExamination(tbl_examHistory.getSelectionModel().getSelectedItem());
                    if (examHistoryBeforeClicked!=null) FillDataAfter2Clicked();
                }
            }
        });
        btnReuse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                examinationController.initFromHistory(examHistoryBeforeClicked);
                Model.getInstance().getViewFactory().closeStage((Stage) btnClose.getScene().getWindow());
            }
        });
    }

    private void FillDataAfter2Clicked() {
        Examination examination = examHistoryBeforeClicked.getExamination();
        ObservableList<Prescribe> prescribes = examHistoryBeforeClicked.getPrescribe();
        tf_trieuChungHistory.setText(examination.getTrieuChung());
        tf_maBenhChinhHistory.setText(String.valueOf(examination.getMaBenhChinh()));
        tf_tenBenhChinhHistory.setText(examination.getTrieuChung());
        tf_maBenhPhuHistory.setText(examination.getTrieuChung());
        tf_tenBenhPhuHistory.setText(examination.getTrieuChung());
        tf_luuYHistory.setText(examination.getLuuy());
        tbl_kethuocHistory.setItems(prescribes);
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
    }

    private void LoadHistory_Date(int year) {
        listExaminationsHistory_detail=examinationHistoryDAO.getPatientsByDate(customer.getMaBN(), year);
        for(ExaminationHistory examinationHistory : listExaminationsHistory_detail) {
            listExaminationsHistory.add(examinationHistory.getExamination());
        }
        tbl_examHistory.setItems(listExaminationsHistory);
        if(!tbl_examHistory.getItems().isEmpty()) {
        } else {
            showAlert("Warning",customer.getHoTen() +" không có trong dữ liệu đã khám!");
            Model.getInstance().getViewFactory().closeStage((Stage) btnReuse.getScene().getWindow());
        }
    }
    private ExaminationHistory FindExamHistoryByExamination(Examination examination)
    {
        for(ExaminationHistory examinationHistory : listExaminationsHistory_detail) {
            if(examinationHistory.getExamination().getMabn()==examination.getMabn()
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
}

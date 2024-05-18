package com.example.privateclinic.Controllers;

import com.example.privateclinic.Models.Customer;
import com.example.privateclinic.Models.Model;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ExaminationHistoryController {

    public Pane btnClose;
    public Spinner spinnerYear;
    public TableView tbl_customerHIstory;
    public TableColumn col_ngayvaoHIstory;
    public Button btnReuse;
    public TableView tbl_kethuocHistory;
    public TableColumn<Customer,Integer> col_stt;
    public TableColumn<Customer,String> col_tenThuoc;
    public TableColumn<Customer,String> col_donVi;
    public TableColumn<Customer,String> col_dangThuoc;
    public TableColumn<Customer,String> col_cachDung;
    public TableColumn<Customer,String> col_Ngay;
    public TableColumn<Customer,String> col_Sang;
    public TableColumn<Customer,String> col_True;
    public TableColumn<Customer,String> col_Chieu;
    public TableColumn<Customer,String> col_Toi;
    public TableColumn<Customer,String> col_soLuong;
    public TableColumn<Customer,String> col_donGia;
    public TableColumn<Customer,String> col_thanhTien;
    private String customer_id;

    public void initatata(String id)
    {
        this.customer_id=id;
    }
    public void close(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

}

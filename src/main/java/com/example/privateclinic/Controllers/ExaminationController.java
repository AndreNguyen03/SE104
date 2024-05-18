package com.example.privateclinic.Controllers;

import com.example.privateclinic.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ExaminationController implements Initializable {
    public Pane btnClose;
    public ComboBox cb_ngay;
    public ComboBox cb_sang;
    public ComboBox cb_trua;
    public ComboBox cb_chieu;
    public ComboBox cb_toi;
    public TextField tf_soLuong;
    public ComboBox cb_dvt;
    public ComboBox cb_dangThuoc;
    public ComboBox cb_cachDung;
    Customer customer;
    @FXML
    RadioButton rad_men,rad_women;
    @FXML
    ProgressBar progressCustomerLoad,progressMedicineLoad;
    @FXML
    TextField tf_mabn,tf_tenbn,tf_ngaysinh;
    @FXML
    public TableView tbl_customer;
    @FXML
    public TableColumn <Customer,String> col_mabn;
    @FXML
    public TableColumn <Customer,String> col_tenbn;
    @FXML
    public TableColumn <Customer,String> col_sdt;

    ObservableList<Customer>  list ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Initialization
        customer = new Customer();
        progressCustomerLoad.setVisible(false);
        progressMedicineLoad.setVisible(false);
        //set up
        fillDataCustomers_waiting(); // tải danh sách customers
        uploadDonViTinh();
        uploadDangThuoc();
        uploadCachDung();
        uploadComboBox();
        tbl_customer.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount()==2)
            {
                try
                {
                    fillDataCustomer_exam();

                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void uploadComboBox() {
        List<Integer> listOptions = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        cb_ngay.getItems().addAll(listOptions);
        cb_trua.getItems().addAll(listOptions);
        cb_chieu.getItems().addAll(listOptions);
        cb_toi.getItems().addAll(listOptions);
        cb_sang.getItems().addAll(listOptions);
    }

    private void uploadCachDung() {
        UseWay useWay = new UseWay();
        ResultSet resultSet = useWay.LoadList();
        try
        {
            while (resultSet.next())
            {
                cb_cachDung.getItems().add(resultSet.getString("tencd"));
            }
        }
        catch (SQLException e )
        {
            e.printStackTrace();
            showAlert("Error","Error");
        }
    }

    private void uploadDangThuoc() {
        MedicineType medicineType = new MedicineType();
        ResultSet resultSet = medicineType.LoadList();
        try
        {
            while (resultSet.next())
            {
                cb_dangThuoc.getItems().add(resultSet.getString("tendt"));
            }
        }
        catch (SQLException e )
        {
            e.printStackTrace();
            showAlert("Error","Error");
        }
    }

    private void uploadDonViTinh() {
        Unit unit = new Unit();
        ResultSet resultSet = unit.LoadList();
        try
        {
            while (resultSet.next())
            {
                cb_dvt.getItems().add(resultSet.getString("tendvt"));
            }
        }
        catch (SQLException e )
        {
            e.printStackTrace();
            showAlert("Error","Error");
        }
    }

    private void fillDataCustomers_waiting() {
        col_mabn.setCellValueFactory(new PropertyValueFactory<>("maBN"));
        col_tenbn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        col_sdt.setCellValueFactory(new PropertyValueFactory<>("SDT"));
        list = FXCollections.observableArrayList();

        ResultSet resultSet = customer.LoadListCustomers();
        try {
            if(resultSet!=null)
            {
                while(resultSet.next())
                {
                    list.add(new Customer(resultSet.getString("mabn"),resultSet.getString("hoten"),resultSet.getString("sdt")));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        tbl_customer.setItems(list);

    }

    public void fillDataCustomer_exam() throws  SQLException
    {
        progressCustomerLoad.setVisible(true);
        new Thread(() ->
        {
          try
          {
              ObservableList<Customer> selectedCustomer  = tbl_customer.getSelectionModel().getSelectedItems();
              if(selectedCustomer.stream().count()!=1)
              {
                  progressCustomerLoad.setVisible(false);
                  return;
              }
              String id = selectedCustomer.get(0).getMaBN();
              ResultSet resultSet = customer.LoadCustomer(id);
              if(resultSet.next()){
                  tf_mabn.setText(resultSet.getString("mabn"));
                  tf_tenbn.setText(resultSet.getString("hoten"));
                  tf_ngaysinh.setText(resultSet.getString("ngaysinh"));
                  if(resultSet.getString("gioitinh").equals("Nam"))
                  {
                      rad_men.setSelected(true);
                  }
                  else rad_women.setSelected(true);
                  progressCustomerLoad.setVisible(false);
              }
              else {
                  showAlert("Error","Error");
                  progressCustomerLoad.setVisible(false);
              }
          }
          catch (SQLException e){
              progressCustomerLoad.setVisible(false);
              e.printStackTrace();
          }
        }).start();

        //ResultSet resultSet  = customer.LoadCustomer(id); // nếu không xài nữa thì xoá
        /*if(resultSet!=null)
        {
            tf_mabn.setText(resultSet.getString("mabn"));
            tf_tenbn.setText(resultSet.getString("tenbn"));
            tf_ngaysinh.setText(resultSet.getString("ngaysinh"));
        }
        else
        {
            showAlert("Error","Database no reply");
        }*/
    }
    public void close(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }
    private void showAlert(String tilte,String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tilte);
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }
}

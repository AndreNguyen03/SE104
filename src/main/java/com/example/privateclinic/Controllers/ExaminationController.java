package com.example.privateclinic.Controllers;

import com.example.privateclinic.Models.Customer;
import com.example.privateclinic.Models.Model;
import com.example.privateclinic.Models.User;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ExaminationController implements Initializable {
    public Pane btnClose;
    Customer customer;
    @FXML
    RadioButton rad_men,rad_women;
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
        customer = new Customer();
        fillDataCustomers_waiting();
        tbl_customer.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount()==2)
            {
                try
                {
                    FillDataCustomer_exam();

                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        });
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

    public void FillDataCustomer_exam() throws  SQLException
    {
        ObservableList<Customer> selectedCustomer  = tbl_customer.getSelectionModel().getSelectedItems();
        if(selectedCustomer.stream().count()!=1)
        {
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
            }
        else {
            showAlert("Error","Error");
        }
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

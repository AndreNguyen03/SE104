package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.CustomerDAO;
import com.example.privateclinic.DataAccessObject.MedicineTypeDAO;
import com.example.privateclinic.DataAccessObject.UnitDAO;
import com.example.privateclinic.DataAccessObject.UseWayDAO;
import com.example.privateclinic.Models.*;
import eu.hansolo.tilesfx.addons.Switch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ExaminationController implements Initializable {
    public Pane btnClose;
    public DatePicker dp_date;
    LocalDate dateCurrent ;
    public Label  lbl_soLuong;
    public ComboBox<Integer> cb_ngay;
    public ComboBox<Integer> cb_sang;
    public ComboBox<Integer> cb_trua;
    public ComboBox<Integer> cb_chieu;
    public ComboBox<Integer> cb_toi;
    public ComboBox<String> cb_dvt;
    public ComboBox<String> cb_dangThuoc;
    public ComboBox<String> cb_cachDung;
    Customer customer;
    @FXML
    RadioButton rad_men,rad_women;
    @FXML
    TextField tf_mabn,tf_tenbn,tf_ngaysinh;
    @FXML
    public TableView<Customer> tbl_customer;
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
        dateCurrent = LocalDate.now();
        dp_date.setValue(dateCurrent);
        lbl_soLuong.setText("0");
        //set action
        dp_date.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fillDataCustomers_waiting(Date.valueOf(dp_date.getValue()));
                lbl_soLuong.setText("0");
            }
        });
        addComboboxListener(cb_ngay);//khi pick value cho combo thi tf_soluong thay đổi giá trij
        addComboboxListener(cb_sang);
        addComboboxListener(cb_trua);
        addComboboxListener(cb_chieu);
        addComboboxListener(cb_toi);
        //set up
        fillDataCustomers_waiting(Date.valueOf(dateCurrent)); // tải danh sách customers
        uploadDonViTinh();
        uploadDangThuoc();
        uploadCachDung();
        uploadComboBox();
        tbl_customer.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount()==2)
            {
                fillDataCustomer_exam();
            }
        });
        UpTotal();
    }


    private void addComboboxListener(ComboBox<Integer> comboBox) {
        comboBox.setOnAction(event ->{
            UpTotal();
        });
    }


    private void UpTotal() {

    }

    private void uploadComboBox() {
        List<Integer> listOptions = Arrays.asList(0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        cb_ngay.getItems().addAll(listOptions);
        cb_trua.getItems().addAll(listOptions);
        cb_chieu.getItems().addAll(listOptions);
        cb_toi.getItems().addAll(listOptions);
        cb_sang.getItems().addAll(listOptions);
    }

    private void uploadCachDung() {
        UseWayDAO useWay = new UseWayDAO();
        ResultSet resultSet = useWay.LoadListUseWays();
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
        MedicineTypeDAO medicineType = new MedicineTypeDAO();
        ResultSet resultSet = medicineType.LoadListMedicines();
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
        UnitDAO unit = new UnitDAO();
        ResultSet resultSet = unit.LoadListUnits();
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

    private void fillDataCustomers_waiting(Date date) {
        col_mabn.setCellValueFactory(new PropertyValueFactory<>("maBN"));
        col_tenbn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        col_sdt.setCellValueFactory(new PropertyValueFactory<>("SDT"));
        CustomerDAO customerDAO = new CustomerDAO();
        ObservableList<Customer> customers = customerDAO.getPatientsByDate(date);
        //Kiem tra danh sách cho co null khong
        if(customers.isEmpty()) showAlert("Warning","List is empty");
        tbl_customer.setItems(customers);
    }

    public void fillDataCustomer_exam()
    {
        Customer customer = tbl_customer.getSelectionModel().getSelectedItem();
        tf_mabn.setText(customer.getMaBN());
        tf_ngaysinh.setText(customer.getNgaySinh());
        tf_tenbn.setText(customer.getHoTen());
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

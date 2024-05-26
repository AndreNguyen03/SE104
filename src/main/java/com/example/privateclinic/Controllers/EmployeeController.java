package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.UserDAO;
import com.example.privateclinic.Models.Employee;
import com.example.privateclinic.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmployeeController implements Initializable {
    public Button btnDeleteEmployee;
    public Button btnAddEmployee;
    public Button btnEditEmployee;
    @FXML
    private TextField tfEmployee;
    @FXML
    private TextField addName;
    @FXML
    private TextField addcitizenId;
    @FXML
    private TextField addAddress;
    @FXML
    private TextField addPhoneNum;
    @FXML
    private TextField addEmail;
    @FXML
    private TextField addPosition;
    @FXML
    private TableView<User> employeeTableView;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> citizenIdColumn;
    @FXML
    private TableColumn<User, String> addressColumn;
    @FXML
    private TableColumn<User, String> phoneNumColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> positionColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;

    private UserDAO employeeDAO = new UserDAO();
    private ObservableList<User> users;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTableColumns();
        loadEmployeeData();
        setOnOffAddDeleteBtn();
        btnDeleteEmployee.setOnAction(event -> handleDeleteAction());
        btnAddEmployee.setOnAction(event -> handleAddAction());
        btnEditEmployee.setOnAction(event -> handleEditAction()); // Add this line to handle edit action
        tfEmployee.setOnKeyPressed(event -> handleSearchKeyPressed(event));
        addSelectionListener(); // Add this line to handle selection changes
    }

    private void setOnOffAddDeleteBtn() {
        btnDeleteEmployee.setDisable(true); // Bắt đầu bằng việc vô hiệu hóa nút Delete
        employeeTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnDeleteEmployee.setDisable(newValue == null); // Nếu không có mục nào được chọn, vô hiệu hóa nút Delete
        });
    }

    private void configureTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("Employee_id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("EmployName"));
        citizenIdColumn.setCellValueFactory(new PropertyValueFactory<>("Citizen_id"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        phoneNumColumn.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("Position"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("Username"));
    }

    private void loadEmployeeData() {
        users = FXCollections.observableArrayList(employeeDAO.getAllEmployees());
        employeeTableView.setItems(users);
    }

    @FXML
    private void handleSearchAction() {
        String searchText = tfEmployee.getText().trim().toLowerCase();
        if (!searchText.isEmpty()) {
            ObservableList<User> filteredEmployees = FXCollections.observableArrayList(
                    users.stream()
                            .filter(emp -> String.valueOf(emp.getCitizen_id()).toLowerCase().contains(searchText)
                                    || emp.getEmployName().toLowerCase().contains(searchText))
                            .collect(Collectors.toList())
            );
            employeeTableView.setItems(filteredEmployees);
        } else {
            employeeTableView.setItems(users);
        }
    }

    @FXML
    private void handleDeleteAction() {
        User selectedUser = employeeTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            employeeDAO.deleteEmployee(selectedUser.getEmployee_id());
            users.remove(selectedUser);
        }
    }

    @FXML
    private void handleSearchKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleSearchAction();
        }
    }

    @FXML
    private void handleAddAction() {
        String name = addName.getText();
        String citizenId = addcitizenId.getText();
        String address = addAddress.getText();
        String phoneNum = addPhoneNum.getText();
        String email = addEmail.getText();
        String position = addPosition.getText();
        String username = addEmail.getText();

        if (!name.isEmpty() && !citizenId.isEmpty() && !address.isEmpty() && !phoneNum.isEmpty() && !email.isEmpty() && !position.isEmpty()) {
            User user = new User(username,name,citizenId,email,phoneNum,address,position);
            employeeDAO.addEmployee(user);
            users.add(user);
            clearAddEmployeeFields();
        }
    }

    @FXML
    private void handleEditAction() {
        User selectedUser = employeeTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String name = addName.getText();
            String citizenId = addcitizenId.getText();
            String address = addAddress.getText();
            String phoneNum = addPhoneNum.getText();
            String email = addEmail.getText();
            String position = addPosition.getText();
            String username = addEmail.getText();

            if (!name.isEmpty() && !citizenId.isEmpty() && !address.isEmpty() && !phoneNum.isEmpty() && !email.isEmpty() && !position.isEmpty()) {
                selectedUser.setEmployName(name);
                selectedUser.setCitizen_id(citizenId);
                selectedUser.setAddress(address);
                selectedUser.setPhoneNumber(phoneNum);
                selectedUser.setEmail(email);
                selectedUser.setPosition(position);
                selectedUser.setUsername(username);

                employeeDAO.updateEmployee(selectedUser);
                employeeTableView.refresh();
                clearAddEmployeeFields();
            }
        }
    }

    private void addSelectionListener() {
        employeeTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean employeeSelected = newValue != null;
            btnAddEmployee.setDisable(employeeSelected); // Vô hiệu hóa nút Add khi chọn nhân viên
            if (employeeSelected) {
                // Hiển thị thông tin nhân viên lên các TextField
                addName.setText(newValue.getEmployName());
                addcitizenId.setText(newValue.getCitizen_id());
                addAddress.setText(newValue.getAddress());
                addPhoneNum.setText(newValue.getPhoneNumber());
                addEmail.setText(newValue.getEmail());
                addPosition.setText(newValue.getPosition());
            } else {
                clearAddEmployeeFields();
            }
        });

    }

    private void clearAddEmployeeFields() {
        addName.clear();
        addcitizenId.clear();
        addAddress.clear();
        addPhoneNum.clear();
        addEmail.clear();
        addPosition.clear();
    }
}

package com.example.privateclinic.Controllers;

import com.example.privateclinic.Models.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button btnChange;

    @FXML
    private Text loginMessageLabel;
    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnLogin;

    @FXML
    private Pane changePane;

    @FXML
    private Pane forgetPane;

    @FXML
    private Pane loginPane;

    @FXML
    private TextField tf;

    @FXML
    private TextField textFieldOTP;

    @FXML
    private TextField tfUsername_Login;
    @FXML
    private TextField tfPassword_Login;
    @FXML
    private TextField textFieldUsernameCP;

    @FXML
    private Text textWelcome;

    @FXML
    private TextField tfPassword1CP;

    @FXML
    private TextField tfPassword2CP;

    @FXML
    void backToLogin(MouseEvent event) {
        forgetPane.toBack();
        changePane.toBack();
    }

    @FXML
    void forgotPasswordOnclick(MouseEvent event) {
        loginPane.toBack();
        changePane.toBack();
    }

    @FXML
    void sendOTP(MouseEvent event) {

    }

    @FXML
    void btnChangePassword(MouseEvent event) {
        loginPane.toBack();
        forgetPane.toBack();
    }
    @FXML
    void close(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.close();
    }

    @FXML
    void minimize(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnAction(event -> loginButtonOnAction());
        forgetPane.toBack();
        changePane.toBack();
    }

    public void loginButtonOnAction()
    {
        if((!tfUsername_Login.getText().isBlank()) && !tfPassword_Login.getText().isBlank())
        {
            ValidateLogin();
        }else {
            loginMessageLabel.setVisible(true);
            loginMessageLabel.setText("Please enter username and password");
        }
    }
    public void ValidateLogin()
    {
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connectionDB= connectionNow.getConnection();

        String verifyLogin = "SELECT COUNT(1) FROM \"NHANVIEN\" WHERE \"TK\" ='"+tfUsername_Login.getText().toString()+"' AND \"MK\" ='"+tfPassword_Login.getText().toString()+"'";
        try {
            Statement statement = connectionDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);
            while(queryResult.next())
            {
                if(queryResult.getInt(1)==1){
                    //loginMessageLabel.setText("Congratulations!");
                    Model.getInstance().getViewFactory().showMenuWindow();
                }
                else {
                    loginMessageLabel.setVisible(true);
                    loginMessageLabel.setText("Invalid login. PLease try login again.");
                    tfUsername_Login.setText("");
                    tfPassword_Login.setText("");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }
}

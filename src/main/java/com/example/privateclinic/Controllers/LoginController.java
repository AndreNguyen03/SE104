package com.example.privateclinic.Controllers;

import com.example.privateclinic.Models.Model;
import com.example.privateclinic.Models.User;
import com.jfoenix.controls.JFXRadioButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    User user;
    String sentEmail = null;
    private String storedOTP;
    private int index;
    private int time_remaining = 20;
    private Timeline timeline;
    @FXML
    private Button btnChange;

    @FXML
    private Button btnConfirm;

    @FXML
    public Text lbl_send_otp;
    @FXML
    public Text lbl_remaining_send;
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
    private TextField tfUsername_Login, tf_username_forgot;
    @FXML
    private PasswordField pfPassword_Login, tfPassword2_change, tfPassword1_change;
    @FXML
    private TextField tfShowPasswordCP2, tfShowPasswordCP1, tfShowPasswordLogin;

    @FXML
    private Text textWelcome;

    @FXML
    private JFXRadioButton radioHideShow, radioHideShowChange;
    @FXML
    private Pane paneProgress;

    @FXML
    void backToLogin(MouseEvent event) {
        index = 0;
        ResetTextField();
        loginPane.toFront();
    }

    private void ResetTextField() {
        tfUsername_Login.setText("");
        pfPassword_Login.setText("");
        tfPassword1_change.setText("");
        tfPassword2_change.setText("");
        tf_username_forgot.setText("");
        textFieldOTP.setText("");
    }

    @FXML
    void forgotPasswordOnclick(MouseEvent event) {
        index = 1;
        forgetPane.toFront();
    }

    @FXML
    void sendOTP(MouseEvent event) {
        if (tf_username_forgot.getText().isBlank()) {
            showAlert("You must fill the username!");
            return;
        }
        paneProgress.toFront();
        paneProgress.setVisible(true);
        new Thread(() -> {
            String username_result = null;
            username_result = user.getUsername(tf_username_forgot.getText().toString());
            if (username_result == null) {
                showAlert("Invalid username: " + tf_username_forgot.getText().toString());
                return;
            }
            storedOTP = generateOTP();
            String fromEmail = "kiseryouta2003@gmail.com";
            String password = "qcqa slmu vkbr edha";
            String subject = "OTP code";
            String body = "Your OTP code is " + storedOTP;

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail, "Green Clinic"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail(tf_username_forgot.getText())));
                message.setSubject(subject);
                message.setText(body);

                Transport.send(message);
                sentEmail = storedOTP;
                Platform.runLater(() -> {
                    if (sentEmail != null) {
                        paneProgress.setVisible(false);
                        showAlert("OTP is now sent to mail!");
                        countDown(); // Start countdown here
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                sentEmail = null;
                showAlert("Failed to send OTP: " + e.getMessage());
            }
        }).start();


    }

    private void countDown() {
        lbl_send_otp.setDisable(true);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                time_remaining--;
                lbl_send_otp.setText("Gửi lại " + time_remaining + "s");
                if (time_remaining <= 0) {
                    timeline.stop();
                    lbl_send_otp.setText("Gửi OTP");
                    lbl_send_otp.setDisable(false);
                    sentEmail = null; // đặt sent email về null để huỷ otp
                    time_remaining = 50; // khởi động lại timer
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public String generateOTP() {
        Random random = new Random();
        int otp = random.nextInt(100000, 999999);
        return String.valueOf(otp);
    }

    @FXML
    void btnContinue_clicked(MouseEvent event) {

        if (CheckForFill()) {
            if (!textFieldOTP.getText().toString().equals(sentEmail)) {
                showAlert("OTP is wrong!");
                return;
            }
            index = 2;
            changePane.toFront();
            pfPassword_Login.setText("");
            sentEmail = null;
        }
    }

    private boolean CheckForFill() {
        if (index == 0) // đang ở màn login
        {
            if ((tfUsername_Login.getText().isBlank()) && pfPassword_Login.getText().isBlank()) {
                showAlert("Please enter username and password");
                tfUsername_Login.setText("");
                pfPassword_Login.setText("");
                return false;
            }
        } else if (index == 1) // đang ở màn forgotpassword
        {
            if (tf_username_forgot.getText().isBlank()) {
                showAlert("You must fill the username!");
                return false;
            } else if (textFieldOTP.getText().isBlank()) {
                showAlert("You must fill the OTP!");
                return false;
            }
        } else // đang ở màn change
        {
            if (tfPassword1_change.getText().isBlank()) {
                showAlert("You must fill new password");
                return false;
            }
            if (tfPassword2_change.getText().isBlank()) {
                showAlert("You must fill confirm new password");
                return false;

            }
            if (!tfPassword2_change.getText().equals(tfPassword1_change.getText())) {
                showAlert("Wrong password re-entered, please check again");
                return false;
            }

        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnAction(event -> loginButtonOnAction());
        radioHideShow.setOnAction(event -> showPassword());
        radioHideShowChange.setOnAction(event -> showPassword());
        loginPane.toFront();
        ;
        user = new User();
        index = 0;
    }

    private void showPassword() {
        if (index == 0 && radioHideShow.isSelected()) {
            tfShowPasswordLogin.setVisible(true);
            pfPassword_Login.setVisible(false);
            tfShowPasswordLogin.setText(pfPassword_Login.getText());
        } else if (index == 0 && !radioHideShow.isSelected()) {
            pfPassword_Login.setVisible(true);
            tfShowPasswordLogin.setVisible(false);
        } else if (index == 2 && radioHideShowChange.isSelected()) {
            tfShowPasswordCP1.setVisible(true);
            tfPassword1_change.setVisible(false);
            tfShowPasswordCP1.setText(tfPassword1_change.getText());
            tfShowPasswordCP2.setVisible(true);
            tfPassword2_change.setVisible(false);
            tfShowPasswordCP2.setText(tfPassword2_change.getText());
        } else if (index == 2 && !radioHideShowChange.isSelected()) {
            tfShowPasswordCP1.setVisible(false);
            tfPassword1_change.setVisible(true);
            tfShowPasswordCP2.setVisible(false);
            tfPassword2_change.setVisible(true);
        }
    }


    public void loginButtonOnAction() {
        if (CheckForFill()) Login(tfUsername_Login.getText().toString(), pfPassword_Login.getText().toString());
    }

    private void Login(String username, String password) {
        int valid = user.CheckValidate(username, password);
        if (valid == 1) {
            showAlert("Welcome! Please change your password");
            loginPane.toBack();
            forgetPane.toBack();
        } else if (valid == 2) {
            showAlert("Login successfully!");
            pfPassword_Login.setText("");
            Stage stage = (Stage) btnLogin.getScene().getWindow(); //get login-screen
            Model.getInstance().getViewFactory().closeStage(stage);//close login-screen
            Model.getInstance().getViewFactory().showMenuWindow(user);//mở menu-screen
        } else
            showAlert("Fail to login! Check your Username and Password again");
    }

    @FXML
    public void btnConfirm_clicked(MouseEvent mouseEvent) throws SQLException {
        if (CheckForFill() && UpdatePassword(index)) {
            showAlert("Password is now changed!");
            index = 0;
            ResetTextField();
            changePane.toBack();
            forgetPane.toBack();
        } else showAlert("Error!");
    }

    private boolean UpdatePassword(int index) throws SQLException {
        if (index == 0)// yêu cầu đổi mật khẩu mặc định
        {
            return user.UpdatePassword(tf_username_forgot.getText().toString(), tfPassword2_change.getText().toString(), index);
        }
        return user.UpdatePassword(tf_username_forgot.getText().toString(), tfPassword2_change.getText().toString(), index);
    }

    @FXML
    void passwordFieldKeyTyped(KeyEvent event) {
        checkValidate20characters(event);
    }

    private void checkValidate20characters(KeyEvent event) {
        if (pfPassword_Login.getText().length() >= 20) {
            // Hiển thị thông báo khi độ dài vượt quá 20 ký tự
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Password must be 20 characters or less.");
            alert.showAndWait();
            event.consume(); // Hủy sự kiện

            // Xoá ký tự vừa nhập dư
            int caretPos = pfPassword_Login.getCaretPosition();
            pfPassword_Login.deleteText(caretPos - 1, caretPos);
        }
    }

    private void showAlert(String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }

    @FXML
    void close(MouseEvent event) {
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.close();
    }

    public void showLogin() {
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    @FXML
    void minimize(MouseEvent event) {
        Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }
}

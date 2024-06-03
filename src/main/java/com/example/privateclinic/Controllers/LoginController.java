package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.UserDAO;
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
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    public PasswordField pfPassword1_change;
    public PasswordField pfPassword2_change;
    UserDAO userDAO ;
    String sentEmail = null;
    private String storedOTP;
    private int index;
    private int time_remaining = 50;
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
    private PasswordField pfPassword_Login;
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
        pfPassword1_change.setText("");
        pfPassword2_change.setText("");
        tf_username_forgot.setText("");
        textFieldOTP.setText("");
        tfShowPasswordCP1.setText("");
        tfShowPasswordCP2.setText("");
    }

    @FXML
    void forgotPasswordOnclick(MouseEvent event) {
        index = 1;
        forgetPane.toFront();
    }

    @FXML
    void sendOTP(MouseEvent event) {
        if (tf_username_forgot.getText().isBlank()) {
            showAlert("Warning","You must fill the username!");
            return;
        }
        paneProgress.toFront();
        paneProgress.setVisible(true);
        new Thread(() -> {
            String username_result = null;
            username_result = userDAO.getUsername(tf_username_forgot.getText());
            if (username_result == null) {
                Platform.runLater(() -> {
                    paneProgress.setVisible(false);
                    showAlert("Warning","Invalid username: " + tf_username_forgot.getText());
                });
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
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userDAO.getEmail(tf_username_forgot.getText())));
                message.setSubject(subject);
                message.setText(body);

                Transport.send(message);
                sentEmail = storedOTP;
                Platform.runLater(() -> {
                    if (sentEmail != null) {
                        paneProgress.setVisible(false);
                        showAlert("Warning","OTP is now sent to mail!");
                        countDown(); // Start countdown here
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                sentEmail = null;
                showAlert("Warning","Failed to send OTP: " + e.getMessage());
            }
        }).start();


    }

    private void countDown() {
        lbl_send_otp.setDisable(true);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                time_remaining--;
                lbl_send_otp.setText("Gửi lại sau " + time_remaining + " s");
               // lbl_send_otp.setX();
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
            if (!textFieldOTP.getText().equals(sentEmail)) {
                showAlert("Warning","OTP is wrong!");
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
                showAlert("Warning","Please enter username and password");
                tfUsername_Login.setText("");
                pfPassword_Login.setText("");
                return false;
            }
        } else if (index == 1) // đang ở màn forgotpassword
        {
            if (tf_username_forgot.getText().isBlank()) {
                showAlert("Warning","You must fill the username!");
                return false;
            } else if (textFieldOTP.getText().isBlank()) {
                showAlert("Warning","You must fill the OTP!");
                return false;
            }
        } else // đang ở màn change
        {
            if (pfPassword1_change.getText().isBlank()) {
                showAlert("Warning","You must fill new password");
                return false;
            }
            if (pfPassword2_change.getText().isBlank()) {
                showAlert("Warning","You must fill confirm new password");
                return false;

            }
            if (!pfPassword2_change.getText().equals(pfPassword1_change.getText())) {
                showAlert("Warning",
                        "Wrong password re-entered, please check again");
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
        userDAO =  new UserDAO();
        index = 0;
    }

    private void showPassword() {
        if (index == 0 && radioHideShow.isSelected()) {

            //show password va an password field
            tfShowPasswordLogin.setVisible(true);
            pfPassword_Login.setVisible(false);
            tfShowPasswordLogin.setText(pfPassword_Login.getText());

        } else if (index == 0 && !radioHideShow.isSelected()) {

            // set lai matkhau da hien vao passwordfield
            pfPassword_Login.setText(tfShowPasswordLogin.getText());

            //an textfield va show lai passwordfield
            pfPassword_Login.setVisible(true);
            tfShowPasswordLogin.setVisible(false);

        } else if (index == 2 && radioHideShowChange.isSelected()) {

            // show password va an passwordfield
            tfShowPasswordCP1.setVisible(true);
            pfPassword1_change.setVisible(false);
            tfShowPasswordCP1.setText(pfPassword1_change.getText());
            tfShowPasswordCP2.setVisible(true);
            pfPassword2_change.setVisible(false);
            tfShowPasswordCP2.setText(pfPassword2_change.getText());

        } else if (index == 2 && !radioHideShowChange.isSelected()) {

            //set lai mat khau da hien vao passwordfield
            pfPassword1_change.setText(tfShowPasswordCP1.getText());
            pfPassword2_change.setText(tfShowPasswordCP2.getText());

            //an textfield va show lai passwordfield
            tfShowPasswordCP1.setVisible(false);
            pfPassword1_change.setVisible(true);
            tfShowPasswordCP2.setVisible(false);
            pfPassword2_change.setVisible(true);
        }
    }


    public void loginButtonOnAction() {
        paneProgress.setVisible(true);
        paneProgress.toFront();
        new Thread(() -> {
            try {
                if (CheckForFill()) {
                    Login(tfUsername_Login.getText().trim(), pfPassword_Login.getText().trim());
                }
            }
            catch (SQLException|IOException e)
            {
                e.printStackTrace();
                Platform.runLater(() -> {
                    paneProgress.setVisible(false);
                    showAlert("Error Connection","Can't connect server!");
                });
            }
        }).start();
    }

    private void Login(String username, String password) throws SQLException,IOException {

            int valid = userDAO.CheckValidate(username, password);
            Platform.runLater(()-> {
                paneProgress.setVisible(false);
                if (valid == 1) {
                    showAlert("Notification","Welcome! Please change your password");
                    loginPane.toBack();
                    forgetPane.toBack();
                } else if (valid == 2) {
                    showAlert("Notification","Login successfully!");
                    pfPassword_Login.setText("");
                    Stage stage = (Stage) btnLogin.getScene().getWindow(); //get login-screen
                    Model.getInstance().getViewFactory().closeStage(stage);//close login-screen
                    Model.getInstance().getViewFactory().showMenuWindow(userDAO.getEmployee());//mở menu-screen
                } else
                    showAlert("Warning","Fail to login! Check your Username and Password again");
            });
    }

    @FXML
    public void btnConfirm_clicked(MouseEvent mouseEvent) {
        if(isLess6characters(pfPassword1_change)) return;
        paneProgress.setVisible(true);
        paneProgress.toFront();
        new Thread(() -> {
            try {
                if (CheckForFill()) {
                    boolean updateSuccess = UpdatePassword(index);
                    Platform.runLater(() -> {
                        if (updateSuccess) {
                            showAlert("Notification", "Password is now changed!");
                            index = 0;
                            ResetTextField();
                            changePane.toBack();
                            forgetPane.toBack();
                        } else {
                            showAlert("Warning", "Failed to update password. Please try again.");
                        }
                        paneProgress.setVisible(false);
                    });
                } else {
                    Platform.runLater(() -> {
                        showAlert("Warning", "Error! Please check the input fields.");
                        paneProgress.setVisible(false);
                    });
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    showAlert("Error Connection", "Can't connect to the server!");
                    paneProgress.setVisible(false);
                });
            }
        }).start();

    }

    private boolean UpdatePassword(int index) throws SQLException,IOException {
        if(index ==0 )
            return userDAO.UpdatePassword(tfUsername_Login.getText().toString(), pfPassword2_change.getText().toString(), index);
        return  userDAO.UpdatePassword(tf_username_forgot.getText().toString(), pfPassword2_change.getText().toString(), index);
    }

    @FXML
    void passwordFieldKeyTyped(KeyEvent event) {
        checkValidate20characters(event);
    }

    private void checkValidate20characters(KeyEvent event) {///
        if (pfPassword_Login.getText().length() >= 18) {
            // Hiển thị thông báo khi độ dài vượt quá 18 ký tự
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Password must be 18 characters or less.");
            alert.showAndWait();
            event.consume(); // Hủy sự kiện

            // Xoá ký tự vừa nhập dư
            int caretPos = pfPassword_Login.getCaretPosition();
            pfPassword_Login.deleteText(caretPos - 1, caretPos);
        }
    }
    private boolean isLess6characters(TextField tf) {
        if (tf.getText().length() <6) {
            // Hiển thị thông báo khi độ dài vượt quá 15 ký tự
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Password must be 6 character or more!");
            alert.showAndWait();
            return true;
        }
        return false;
    }
    private void showAlert(String tilte,String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tilte);
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

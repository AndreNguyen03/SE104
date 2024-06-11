package com.example.privateclinic.Controllers;

import com.example.privateclinic.Models.Model;
import com.example.privateclinic.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    private Button btnEmployee;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnReport;
    @FXML
    private Button btnSetting;
    @FXML
    private Button btnExamination;
    @FXML
    private MenuItem btnDisease;
    @FXML
    private MenuItem btnAspirin;
    @FXML
    private MenuButton btnCategory;
    @FXML
    private Text titleTextField;
    @FXML
    private ImageView imageViewHome;
    private User user;

    public Button btnReception;


    @FXML
    private Pane mainPane;

    public MenuController()
    {
        this.user = new User();
    }

    public void handleLogo(MouseEvent mouseEvent) throws IOException {
        mainPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Home.fxml"));
        Parent reportSceneRoot = loader.load();
        mainPane.getChildren().add(reportSceneRoot);
        titleTextField.setText("Trang chủ");
    }
    @FXML
    void btnEmployeeClicked(ActionEvent event) throws IOException {
        mainPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Employee.fxml"));
        Parent reportSceneRoot = loader.load();
        mainPane.getChildren().add(reportSceneRoot);
        EmployeeController employeeController = loader.getController();
        employeeController.initData(user);
        titleTextField.setText("Nhân viên");
    }


    @FXML
    void btnAspirineClicked(ActionEvent event) throws IOException {
        Model.getInstance().getViewFactory().showCategoryAspirine(user);
    }

    @FXML
    void btnDiseaseClicked(ActionEvent event) throws IOException {
        Model.getInstance().getViewFactory().showCategoryDisease(user);
    }
    @FXML
    void btnExaminationClicked(ActionEvent event) throws IOException {
        Model.getInstance().getViewFactory().showExaminationWindow(user);
    }

    @FXML
    void btnLogoutClicked(ActionEvent event) {

    }

    @FXML
    void btnReceptionClicked(ActionEvent event) throws IOException {
        Model.getInstance().getViewFactory().showReceptionWindow(user);
    }

    @FXML
    void btnReportClicked(ActionEvent event) throws IOException {
        mainPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Report.fxml"));
        Parent reportSceneRoot = loader.load();
        mainPane.getChildren().add(reportSceneRoot);
    }

    @FXML
    void btnSettingClicked(ActionEvent event) {
        Model.getInstance().getViewFactory().showSettingWindow(user);

    }
    @FXML
    void closeMenu(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("DangXuat");
        confirmationAlert.setContentText("Ban muon dang xuat?");

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = ButtonType.CANCEL;

        confirmationAlert.getButtonTypes().setAll(okButton, cancelButton);

        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == okButton) {
            Model.getInstance().getViewFactory().showLoginWindow();
            Model.getInstance().getViewFactory().closeStage(s);
        }
    }
    public void initData(User _user)
    {
        this.user=_user;
        btnReport.setDisable(true);
        btnEmployee.setDisable(true);
        btnSetting.setDisable(true);
        btnExamination.setDisable(true);
        btnReception.setDisable(true);
        btnCategory.setDisable(true);
        switch (_user.getEmployeePosition().trim()) {
            case "Bác sĩ":
                btnExamination.setDisable(false);
                btnCategory.setDisable(false);
                break;
            case "Chủ":
                btnReport.setDisable(false);
                btnEmployee.setDisable(false);
                btnSetting.setDisable(false);
                btnExamination.setDisable(false);
                btnReception.setDisable(false);
                btnCategory.setDisable(false);
                break;
            case "Tiếp tân":
                btnReception.setDisable(false);
                btnSetting.setDisable(false);
                break;
        }
    }
    public void ProfileEmploy_Clicked(MouseEvent mouseEvent) {
        String id = String.valueOf(this.user.getEmployee_id());
        String name = this.user.getEmployName();
        String username = this.user.getUsername();
        String pos = this.user.getPosition();
        Model.getInstance().getViewFactory().showProfileWindow(id,name,username,pos);
    }
    @FXML
    void minimizeMenu(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleTextField.setFocusTraversable(true);
    }

}

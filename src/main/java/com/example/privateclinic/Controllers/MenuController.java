package com.example.privateclinic.Controllers;

import com.almasb.fxgl.scene.Scene;
import com.example.privateclinic.Models.Model;
import com.example.privateclinic.Models.User;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.control.PropertySheet;

import java.io.IOException;
import java.util.Optional;

public class MenuController {
    private User user;

    @FXML
    private MenuItem btnAspirine;

    @FXML
    private MenuItem btnDisease;

    @FXML
    private Button btnAccountant;

    @FXML
    private Button btnCategory;

    @FXML
    private Button btnExamination;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnReport;

    @FXML
    private Button btnSetting;

    @FXML
    private Text titleTextField;

    @FXML
    private Pane mainPane;

    public MenuController()
    {
        this.user = new User();
    }

    @FXML
    void btnEmployeeClicked(ActionEvent event) throws IOException {
        mainPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Employee.fxml"));
        Parent reportSceneRoot = loader.load();
        mainPane.getChildren().add(reportSceneRoot);
    }


    @FXML
    void btnAspirineClicked(ActionEvent event) throws IOException {
        mainPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Category_Aspirine.fxml"));
        Parent reportSceneRoot = loader.load();
        mainPane.getChildren().add(reportSceneRoot);
    }

    @FXML
    void btnDiseaseClicked(ActionEvent event) throws IOException {
        mainPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Category_Disease.fxml"));
        Parent reportSceneRoot = loader.load();
        mainPane.getChildren().add(reportSceneRoot);
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
        Model.getInstance().getViewFactory().showReceptionWindow();
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
        Model.getInstance().getViewFactory().showSettingWindow();
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
            Model.getInstance().getViewFactory().closeStage(s);
        }
    }
    public void initData(User _user)
    {
        this.user=_user;
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


    public void minimizeStage(Stage stage)
    {
        stage.setIconified(true);
    }

}

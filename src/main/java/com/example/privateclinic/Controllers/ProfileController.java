package com.example.privateclinic.Controllers;

import com.example.privateclinic.Models.Model;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ProfileController {


    @FXML
    public Label employee_id_lbl;
    @FXML
    public Label employname_lbl;
    @FXML
    public Label username_lbl;
    @FXML
    public Label position_lbl;
    private Stage stage;

    public ProfileController()
    {
    }
    public void initData(int _id,String _name,String _username,String _pos)
    {
        employee_id_lbl.setText(String.valueOf(_id));
        employname_lbl.setText(_name);
        username_lbl.setText(_username);
        position_lbl.setText(_pos);
    }

    public void  closeProfileView(MouseEvent mouseEvent) {
        stage = (Stage) employee_id_lbl.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    public void minimizeProfile(MouseEvent mouseEvent) {
        Model.getInstance().getViewFactory().minimizeStage(stage);
    }
}

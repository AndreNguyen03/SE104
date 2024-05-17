package com.example.privateclinic;

import com.example.privateclinic.Controllers.ExaminationController;
import com.example.privateclinic.Controllers.LoginController;
import com.example.privateclinic.Models.Model;


import javafx.application.Application;


import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        /*LoginController loginController = new LoginController();
        loginController.showLogin();*/
        Model.getInstance().getViewFactory().showExaminationWindow();
    }
}

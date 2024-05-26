package com.example.privateclinic;

import com.example.privateclinic.Controllers.LoginController;
import com.example.privateclinic.Models.Model;


import com.example.privateclinic.Models.User;
import javafx.application.Application;


import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        /*LoginController loginController = new LoginController();
        loginController.showLogin();*/
        User user = new User();
        Model.getInstance().getViewFactory().showMenuWindow(user);
    }
}

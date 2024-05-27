package com.example.privateclinic;

import com.example.privateclinic.Controllers.LoginController;
import com.example.privateclinic.Models.Model;


import com.example.privateclinic.Models.User;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Application;


import javafx.print.PrinterJob;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        LoginController loginController = new LoginController();
        loginController.showLogin();
       /* User user = new User();
        Model.getInstance().getViewFactory().showExaminationWindow(user);*/
    }
}

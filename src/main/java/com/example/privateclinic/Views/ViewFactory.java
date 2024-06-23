package com.example.privateclinic.Views;

import com.example.privateclinic.Controllers.*;
import com.example.privateclinic.Models.ExaminationHistory;
import com.example.privateclinic.Models.Patient;
import com.example.privateclinic.Models.User;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;

public class ViewFactory {
    Stage stageExamination = null;
    Stage stageReception =null;
    Stage stageSetting = null;
    Stage stageMenu = null;
    Stage stageProfile= null;
    Stage stageExaminationHistory = null;
    Stage stageCategoryAspirine= null;
    Stage stageCategoryDisease = null;
    Stage stageReportHistory = null;
    public void showLoginWindow() {
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Login.fxml"));
        createStage(loader);

    }

    public void showMenuWindow(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Menu.fxml"));
        Pane pane = loader.load();
        VBox root = new VBox(pane);
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        double width = resolution.getWidth();
        double height = resolution.getHeight();
        double w = width/1600;  // your window width
        double h = height/1000;  // your window height
        Scale scale = new Scale(w, h, 0, 0);
        root.getTransforms().add(scale);
        Scene scene = new Scene(root);
        stageMenu = new Stage(StageStyle.UNDECORATED);
        stageMenu.setScene(scene);
        stageMenu.setMaximized(true);
        MenuController menuController = loader.getController();
        menuController.initData(user);
        stageMenu.show();

    }
    public void showProfileWindow(String id,String name,String username,String pos)
    {
        if(stageProfile==null)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Profile.fxml"));
            stageProfile=createStage(loader);
            ProfileController profileController = loader.getController();
            profileController.initData(id,name,username,pos);
        }
        else
        {
            stageProfile.toFront();
        }

    }
    public void showReceptionWindow(User user) {
        if(stageReception==null)  //xử lí mở 2 lần scene
        {
            FXMLLoader receptionLoader =  new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Reception.fxml"));
            stageReception=createStage(receptionLoader);
            ReceptionController receptionController = receptionLoader.getController();
            receptionController.initUser(user);

        }
        else
        {
            stageReception.toFront(); // nếu đã mở scene thì bring to front
        }
    }
    public void showExaminationWindow(User user)
    {
        if(stageExamination==null) //xử lí mở 2 lần scene
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Examination.fxml"));
            stageExamination=createStage(loader);
            ExaminationController examinationController = loader.getController();
            examinationController.initData(user);
        }
        else
        {
            stageExamination.toFront(); // nếu đã mở scene thì bring to front
        }
    }
    public void showSettingWindow(User user)
    {
        if(stageSetting==null)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Setting.fxml"));
            stageSetting=createStage(loader);
            SettingController settingController = loader.getController();
            settingController.initData(user);
        }
        else
        {
            stageSetting.toFront();
        }
    }
    public void showHistoryExamination(Patient patient, ExaminationController _examinationController, boolean fromWaitingList, ExaminationHistory examinationHistory)
    {
        if(stageExaminationHistory==null) //xử lí mở 2 lần scene
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/ExaminationHistory.fxml"));
            stageExaminationHistory=createStage(loader);
            ExaminationHistoryController examHistoryController = loader.getController();
            examHistoryController.initData(patient,_examinationController,fromWaitingList,examinationHistory);
        }
        else {
            closeStage(stageExaminationHistory);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/ExaminationHistory.fxml"));
            stageExaminationHistory=createStage(loader);
            ExaminationHistoryController examHistoryController = loader.getController();
            examHistoryController.initData(patient,_examinationController,fromWaitingList,examinationHistory);
            stageExaminationHistory.toFront(); // nếu đã mở scene thì bring to front
        }
    }
    public void showCategoryDisease(User user) {
        if(stageCategoryDisease==null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Category_Disease.fxml"));
            stageCategoryDisease=createStage(loader);
            Category_DiseaseController categoryDiseaseController = loader.getController();
            categoryDiseaseController.InitData(user);
        } else {
            stageCategoryDisease.toFront();
        }
    }
    public void showCategoryAspirine(User user) {
        if(stageCategoryAspirine==null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Category_Aspirine.fxml"));
            stageCategoryAspirine=createStage(loader);
            Category_AspirineController categoryAspirineController = loader.getController();
            categoryAspirineController.init(user);

        } else {
            stageCategoryAspirine.toFront();
        }
    }
    public void showReportHistory() {
        if(stageReportHistory==null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/ReportHistory.fxml"));
            stageReportHistory=createStage(loader);
        } else {
            stageReportHistory.toFront();
        }
    }
    private Stage createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        return stage;
    }
    public void closeStage(Stage stage)
    {
        stage.close();
        if(stageMenu!=null &&!stageMenu.isShowing()) // nếu menu đóng thì đóng hết những window đang mở
        {
            if(stageExamination!=null) stageExamination.close();
            if(stageReception!=null) stageReception.close();
            if(stageSetting!=null) stageSetting.close();
            if(stageProfile!=null) stageProfile.close();
            if(stageExaminationHistory!=null) stageExaminationHistory.close();
            if(stageCategoryAspirine!=null) stageCategoryAspirine.close();
            if(stageCategoryDisease!=null) stageCategoryDisease.close();
            if(stageReportHistory!=null) stageReportHistory.close();
        }
        //khi 1 stage nào đó đóng thì cập nhật tình hình các stage khác
        if(stageExamination!=null && !stageExamination.isShowing()) stageExamination=null;  // nếu khác null nhưng ko còn show thì cập nhật về null
        if(stageReception!=null && !stageReception.isShowing()) stageReception=null;
        if(stageSetting!=null && !stageSetting.isShowing()) stageSetting=null;
        if(stageProfile!=null &&!stageProfile.isShowing()) stageProfile=null;
        if(stageExaminationHistory!=null && !stageExaminationHistory.isShowing()) stageExaminationHistory=null;
        if(stageCategoryAspirine!=null && !stageCategoryAspirine.isShowing()) stageCategoryAspirine=null;
        if(stageCategoryDisease!=null && !stageCategoryDisease.isShowing()) stageCategoryDisease=null;
        if(stageReportHistory!=null && !stageReportHistory.isShowing()) stageReportHistory=null;

    }
    public void minimizeStage(Stage stage)
    {
        stage.setIconified(true);
    }

}

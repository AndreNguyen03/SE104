package com.example.privateclinic.Views;

import com.example.privateclinic.Controllers.ExaminationHistoryController;
import com.example.privateclinic.Controllers.MenuController;
import com.example.privateclinic.Controllers.ProfileController;
import com.example.privateclinic.Controllers.SettingController;
import com.example.privateclinic.Models.User;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewFactory {
    Stage stageExamination = null;
    Stage stageReception =null;
    Stage stageSetting = null;
    Stage stageMenu = null;
    Stage stageProfile= null;
    Stage stageExaminationHistory = null;
    public void showLoginWindow() {
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Login.fxml"));
        createStage(loader);
    }

    public void showMenuWindow(User user) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Menu.fxml"));
        createStage(loader);
        MenuController menuController = loader.getController();
        menuController.initData(user);
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
            stageProfile.setAlwaysOnTop(true);
        }

    }
    public void showReceptionWindow() {
        if(stageReception==null)  //xử lí mở 2 lần scene
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Reception.fxml"));
            stageReception=createStage(loader);
        }
        else
        {
            stageReception.setAlwaysOnTop(true); // nếu đã mở scene thì bring to front
        }
    }
    public void showExaminationWindow()
    {
        if(stageExamination==null) //xử lí mở 2 lần scene
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Examination.fxml"));
            stageExamination=createStage(loader);
        }
        else
        {
            stageExamination.setAlwaysOnTop(true); // nếu đã mở scene thì bring to front
        }
    }
    public void showSettingWindow()
    {
        if(stageSetting==null)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Setting.fxml"));
            stageSetting=createStage(loader);
        }
        else
        {
            stageSetting.setAlwaysOnTop(true);
        }
    }
    public void showHistoryExamination(String id,String year)
    {
        if(stageExaminationHistory==null) //xử lí mở 2 lần scene
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/HistoryExamination"));
            stageExaminationHistory=createStage(loader);
            ExaminationHistoryController examHistoryController = loader.getController();
            examHistoryController.initatata(id);
        }
        else
        {
            stageExaminationHistory.setAlwaysOnTop(true); // nếu đã mở scene thì bring to front
        }
    }

    private Stage createStage(FXMLLoader loader ) {
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
        }
        //khi 1 stage nào đó đóng thì cập nhật tình hình các stage khác
        if(stageExamination!=null && !stageExamination.isShowing()) stageExamination=null;  // nếu khác null nhưng ko còn show thì cập nhật về null
        if(stageReception!=null && !stageReception.isShowing()) stageReception=null;
        if(stageSetting!=null && !stageSetting.isShowing()) stageSetting=null;
        if(stageProfile!=null &&!stageProfile.isShowing()) stageProfile=null;
        if(stageExaminationHistory!=null &&!stageExaminationHistory.isShowing()) stageExaminationHistory=null;
    }
    public void minimizeStage(Stage stage)
    {
        stage.setIconified(true);
    }

}

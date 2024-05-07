package com.example.privateclinic.Views;

import com.example.privateclinic.Controllers.MenuController;
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
    public void showLoginWindow() {
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Login.fxml"));
        createStage(loader);
    }

    public void showMenuWindow() {
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/privateclinic/Fxml/Menu.fxml"));
        createStage(loader);
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
        stage.close();//khi 1 stage nào đó đóng thì cập nhật tình hình các stage khác
        if(stageExamination!=null && !stageExamination.isShowing()) stageExamination=null;  // nếu khác null nhưng ko còn show thì cập nhật về null
        if(stageExamination!=null && !stageReception.isShowing()) stageReception=null;
    }
    public void hideStage(Stage stage)
    {
        stage.hide();
    }
    public void showStage(Stage stage)
    {
        stage.show();
    }
}

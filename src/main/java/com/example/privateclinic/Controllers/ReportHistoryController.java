package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.HistoryDAO;
import com.example.privateclinic.Models.History;
import com.example.privateclinic.Models.Model;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReportHistoryController implements Initializable {
    public ComboBox<Integer> cbYear;
    public ComboBox<Integer> cbMonth;
    public ComboBox<Integer> cbDay;
    public TableView<History> reportHistoryTableView;
    public TableColumn<History,Integer> idColumn;
    public TableColumn<History,String> employeeColumn;
    public TableColumn<History,String> dateColumn;
    public TableColumn<History,String> contentColumn;
    public Pane lbl_header;
    public Text diseaseCount;
    public Text lblDay;
    public Text lblToday;
    public Text lblRefresh;
    private double xOffset = 0;
    private double yOffset =0;
    HistoryDAO historyDAO = new HistoryDAO();
    ObservableList<History> histories = FXCollections.observableArrayList();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    Timeline timeline;
    int time_remaining =61;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SetUp();
        SetUpTable();
        lblToday.setText(LocalDateTime.now().format(formatter));
        LoadData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
    }

    private void LoadData(Integer day, Integer month, Integer year) {
        histories= historyDAO.getAllHistory(day,month,year);
        reportHistoryTableView.setItems(histories);
        diseaseCount.setText(String.valueOf(histories.size()));
    }

    private void SetUp() {
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();
        int currentDay = LocalDate.now().getDayOfMonth();
        ObservableList<Integer> years = FXCollections.observableArrayList();
        ObservableList<Integer> months = FXCollections.observableArrayList();
        ObservableList<Integer> days = FXCollections.observableArrayList();
        for (int i = 0; i < currentYear -1999; i++) {
            years.add(currentYear - i);
        }
        for (int i = 1; i <=12;i++)
        {
            months.add(i);
        }
        for(int i =1 ; i<31;i++) {
            days.add(i);
        }
        cbYear.setValue(currentYear);
        cbMonth.setValue(currentMonth);
        cbDay.setValue(currentDay);
        cbYear.setItems(years);
        cbMonth.setItems(months);
        cbDay.setItems(days);

        lbl_header.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        lbl_header.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage) lbl_header.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX()-xOffset);
            stage.setY(mouseEvent.getScreenY()-yOffset);
        });
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                time_remaining--;
                lblRefresh.setText("Làm mới sau " + (int)(time_remaining - 1)+ " s");
                // lbl_send_otp.setX();
                if (time_remaining == 1) {
                    lblRefresh.setText("Đang làm mới.....");
                    System.out.println("Lam moi");
                    if(!cbDay.isVisible()) LoadData(0,cbMonth.getValue(),cbYear.getValue());
                    else LoadData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
                }
                if(time_remaining==0) {
                    time_remaining=61;
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void SetUpTable() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(reportHistoryTableView.getItems().indexOf(cellData.getValue()) + 1).asObject());
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("ngay"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("noiDung"));
    }

    public void handleMenuDayMonthYear(ActionEvent event) {
        cbDay.setVisible(true);
        lblDay.setVisible(true);
        cbDay.setValue(LocalDateTime.now().getDayOfMonth());
        LoadData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
    }

    public void handleMenuMonthYear(ActionEvent event) {
        cbDay.setVisible(false);
        lblDay.setVisible(false);
        cbDay.setValue(LocalDateTime.now().getDayOfMonth());
        LoadData(0,cbMonth.getValue(),cbYear.getValue());
    }

    public void handlePickYear(ActionEvent event) {
        if(!cbDay.isVisible()) LoadData(0,cbMonth.getValue(),cbYear.getValue());
        else LoadData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
    }

    public void handlePickMonth(ActionEvent event) {
        if(!cbDay.isVisible()) LoadData(0,cbMonth.getValue(),cbYear.getValue());
        else LoadData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
    }

    public void handlePickDay(ActionEvent event) {
        LoadData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
    }

    public void close(MouseEvent mouseEvent) {
        Model.getInstance().getViewFactory().closeStage((Stage) cbDay.getScene().getWindow());
    }

    public void minimizeCategory(MouseEvent mouseEvent) {
        
        Model.getInstance().getViewFactory().minimizeStage((Stage) cbDay.getScene().getWindow());
    }

    public void handlebtnLamMoi(ActionEvent event) {
        time_remaining = 61;
        if(!cbDay.isVisible()) LoadData(0,cbMonth.getValue(),cbYear.getValue());
        else LoadData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
    }
}

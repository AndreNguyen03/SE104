package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.ReportDAO;
import com.example.privateclinic.Models.DrugUsageReport;
import com.example.privateclinic.Models.MonthlyReport;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.apache.poi.ss.formula.functions.T;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportController {

    @FXML
    private TableView<MonthlyReport> monthlyReportTable;

    @FXML
    private TableView<DrugUsageReport> drugUsageReportTable;

    public TableColumn<MonthlyReport, Integer> sttColumn;

    @FXML
    private TableColumn<MonthlyReport, String> dateColumn;

    @FXML
    private TableColumn<MonthlyReport, Integer> patientCountColumn;

    @FXML
    private TableColumn<MonthlyReport, Integer> revenueColumn;
    public TableColumn<MonthlyReport, Double> averageColumn;
    @FXML
    private TableColumn<MonthlyReport, Double> rateColumn;

    @FXML
    private TableColumn<DrugUsageReport, String> drugNameColumn;
    @FXML
    private TableColumn<DrugUsageReport, Integer> drugSttColumn;

    @FXML
    private TableColumn<DrugUsageReport, String> unitColumn;

    @FXML
    private TableColumn<DrugUsageReport, Integer> quantityColumn;

    @FXML
    private TableColumn<DrugUsageReport, Integer> usageCountColumn;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Pane panelNoData;
    @FXML
    ComboBox<Integer> cbYear;
    @FXML
    ComboBox<Integer> cbMonth;
    @FXML
    ComboBox<Integer> cbDay;
    public Pane paneProgress;
    public Text valueRevenueMonth,valueRevenueToday;
    public Text numberProductOfMonth,numberProductOfDay;
    public Text numberExamOfMonth,numberExamOfToday;
    ReportDAO reportDAO = new ReportDAO();

    @FXML
    public void initialize() {
        SetUp();
        SetUpTable();
        LoadProductBarChart();
        LoadData(cbMonth.getValue(),cbMonth.getValue(),cbYear.getValue());
        initializeChart();
    }

    private void LoadData(int day,int month,int year) {
        paneProgress.setVisible(true);
        new Thread(()->{
            try {
                Thread.sleep(1000);
                Platform.runLater(() -> {
                    ObservableList<MonthlyReport> observableReports = FXCollections.observableArrayList(reportDAO.getMonthlyReports(month,year));
                    monthlyReportTable.setItems(observableReports);
                    ObservableList<DrugUsageReport> observableDrugUsageReport= FXCollections.observableArrayList(reportDAO.getDrugUsageReports(month,year));
                    drugUsageReportTable.setItems(observableDrugUsageReport);
                    LoadChar();
                    LoadThreePanel(day,month,year);
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                Platform.runLater(() -> {
                    paneProgress.setVisible(false);
                });
            }
        }).start();
    }

    private void LoadThreePanel(int day,int month,int year)  {
        try {
            numberProductOfMonth.setText(reportDAO.GetNumberOfProductMonth(month,year));
            numberProductOfDay.setText(reportDAO.GetNumberOfProductToday(day,month,year));
            valueRevenueMonth.setText((reportDAO.GetRevenueFromExamMonth(month,year)));
            valueRevenueToday.setText((reportDAO.GetRevenueFromExamToday(day,month,year)));
            numberExamOfMonth.setText(reportDAO.GetNumberExamOfMonth(month,year));
            numberExamOfToday.setText(reportDAO.GetNumberExamOfToday(day,month,year));
            ConvertValueNullToZero(numberProductOfMonth,numberProductOfDay,valueRevenueMonth,valueRevenueToday,numberExamOfMonth,numberExamOfToday);
        } catch (SQLException e){
        }

    }

    private void ConvertValueNullToZero(Text...tfs) {
        for(Text tf: tfs) {
            if(tf.getText().equals("null")) {
                tf.setText("0");
            }
        }
    }

    private void LoadChar() {
        Node node =  borderPane.getCenter();
        if(node.getId().equals("productBart")) {
            LoadProductBarChart();
        } else if(node.getId().equals("productPie")) {
            LoadProductPieChart();
        } else if (node.getId().equals("revenueBar")) {
                LoadRevenueBarChar();
        }
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
    }

    private void SetUpTable() {
        sttColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(monthlyReportTable.getItems().indexOf(cellData.getValue()) + 1).asObject());
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        patientCountColumn.setCellValueFactory(new PropertyValueFactory<>("patientCount"));
        revenueColumn.setCellValueFactory(new PropertyValueFactory<>("revenue"));
        averageColumn.setCellValueFactory(new PropertyValueFactory<>("average"));
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));

        drugSttColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(drugUsageReportTable.getItems().indexOf(cellData.getValue()) + 1).asObject());
        drugNameColumn.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        usageCountColumn.setCellValueFactory(new PropertyValueFactory<>("usageCount"));
    }
    private void initializeChart() {

    }
    private void LoadProductPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int count=0;

        for(DrugUsageReport drug : drugUsageReportTable.getItems()) {
            if(count++==20) break;
            pieChartData.add(new PieChart.Data(drug.getDrugName(), drug.getQuantity()));
        }
        //Create PieChsrt object
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Biểu đồ cơ cấu các sản phẩm bán chạy trong tháng "+cbMonth.getValue()+" năm" + cbYear.getValue());
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(50);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(180);

        pieChart.setId("productPie");
        borderPane.setCenter(pieChart);
        if(drugUsageReportTable.getItems().isEmpty()) {
            panelNoData.setVisible(true);
        } else panelNoData.setVisible(false);
    }
    private void LoadProductBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Sản phẩm");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Số lượng đã bán");

        BarChart barChart = new BarChart(xAxis,yAxis);

        XYChart.Series data = new XYChart.Series();
        data.setName("Những sản phẩm hàng hàng đầu tháng "+cbMonth.getValue()+" năm "+cbYear.getValue()+" ");

        //provide data
        /*int count=0;*/
        for(DrugUsageReport bill : drugUsageReportTable.getItems()) {
            data.getData().add(new XYChart.Data(bill.getDrugName(),bill.getQuantity()));
            /*if(++count==10) break;*/
        }
        barChart.getData().add(data);

        //add to border pane
        barChart.setId("productBart");
        borderPane.setCenter(barChart);
        if(drugUsageReportTable.getItems().isEmpty()) {
            panelNoData.setVisible(true);
        } else panelNoData.setVisible(false);
    }

    private void LoadRevenueBarChar() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Tháng");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Thu nhập (VNĐ)");

        BarChart barChart = new BarChart(xAxis,yAxis);

        XYChart.Series data = new XYChart.Series();
        data.setName("Biểu đồ thống kê thu nhập trong năm "+cbYear.getValue()+" (đơn vị tháng)");
        //provide data
        boolean haveData = false;
        for(int i =1 ;i <=12;i++) {
            try {
                int dt = reportDAO.getToTalMonth(i,cbYear.getValue());
                if(dt>0) haveData = true;
                data.getData().add(new XYChart.Data("Tháng "+i,dt));
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        barChart.getData().add(data);
        //add to border pane
        barChart.setId("revenueBar");
        borderPane.setCenter(barChart);
        if(!haveData) {
            panelNoData.setVisible(true);
        } else panelNoData.setVisible(false);
    }
    public void handlePickMonth(ActionEvent event) {
        LoadData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
        LoadProductPieChart();
        LoadProductBarChart();
    }
    public void handlePickYear(ActionEvent event) {
        LoadData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
        if (borderPane.getCenter().getId().equals("revenueBar"))
            LoadRevenueBarChar();
        LoadProductPieChart();
        LoadProductBarChart();
    }
    public void handleRevenueBarChar(ActionEvent event) {
        LoadRevenueBarChar();
    }

    public void handleProductPieChar(ActionEvent event) {
        LoadProductPieChart();
    }

    public void handleProductBarChart(ActionEvent event) {
        LoadProductBarChart();
    }

    public void handlePickDay(ActionEvent event) {
        paneProgress.setVisible(true);
        new Thread(()->{
            try {
                Thread.sleep(1000);
                Platform.runLater(() -> {
                LoadThreePanel(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
            });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                Platform.runLater(() -> {
                    paneProgress.setVisible(false);
                });
            }
        }).start();
    }
}

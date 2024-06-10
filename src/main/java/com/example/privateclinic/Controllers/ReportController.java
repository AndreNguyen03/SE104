package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.ReportDAO;
import com.example.privateclinic.Models.DrugUsageReport;
import com.example.privateclinic.Models.MonthlyReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ReportController {

    @FXML
    private TableView<MonthlyReport> monthlyReportTable;

    @FXML
    private TableView<DrugUsageReport> drugUsageReportTable;

    @FXML
    private BarChart<String, Number> customerChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private TableColumn<MonthlyReport, Integer> sttColumn;

    @FXML
    private TableColumn<MonthlyReport, Integer> dateColumn;

    @FXML
    private TableColumn<MonthlyReport, Integer> patientCountColumn;

    @FXML
    private TableColumn<MonthlyReport, Double> revenueColumn;

    @FXML
    private TableColumn<MonthlyReport, Double> rateColumn;

    @FXML
    private TableColumn<DrugUsageReport, String> drugNameColumn;

    @FXML
    private TableColumn<DrugUsageReport, String> unitColumn;

    @FXML
    private TableColumn<DrugUsageReport, Integer> quantityColumn;

    @FXML
    private TableColumn<DrugUsageReport, Integer> usageCountColumn;

    ReportDAO reportDAO = new ReportDAO();

    @FXML
    public void initialize() {
        SetUpTable();
        loadMonthlyReportData();
        loadDrugUsageReportData();
        initializeChart();
    }

    private void SetUpTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        patientCountColumn.setCellValueFactory(new PropertyValueFactory<>("patientCount"));
        revenueColumn.setCellValueFactory(new PropertyValueFactory<>("revenue"));
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
        drugNameColumn.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        usageCountColumn.setCellValueFactory(new PropertyValueFactory<>("usageCount"));
    }

    private void loadMonthlyReportData() {
        List<MonthlyReport> reports = reportDAO.getMonthlyReports();
        ObservableList<MonthlyReport> observableReports = FXCollections.observableArrayList(reports);
        monthlyReportTable.setItems(observableReports);
    }

    private void loadDrugUsageReportData() {
        List<DrugUsageReport> reports = reportDAO.getDrugUsageReports();
        ObservableList<DrugUsageReport> observableReports = FXCollections.observableArrayList(reports);
        drugUsageReportTable.setItems(observableReports);
    }

    private void initializeChart() {
        xAxis.setLabel("Tháng");
        yAxis.setLabel("Doanh thu");

        List<MonthlyReport> reports = reportDAO.getMonthlyReports();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu theo tháng");

        for (MonthlyReport report : reports) {
            series.getData().add(new XYChart.Data<>(String.valueOf(report.getMonth()), report.getRevenue()));
        }

        customerChart.getData().add(series);
    }
}

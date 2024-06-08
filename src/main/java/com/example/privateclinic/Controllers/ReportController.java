package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.ReportDAO;
import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.DrugUsageReport;
import com.example.privateclinic.Models.MonthlyReport;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReportController {

    @FXML
    private TableView<MonthlyReport> monthlyReportTable;

    @FXML
    private TableView<DrugUsageReport> drugUsageReportTable;

    @FXML
    private StackedBarChart<String, Number> customerChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private TableColumn<MonthlyReport, Integer> sttColumn;

    @FXML
    private TableColumn<MonthlyReport, LocalDate> dateColumn;

    @FXML
    private TableColumn<MonthlyReport, Integer> patientCountColumn;

    @FXML
    private TableColumn<MonthlyReport, Double> revenueColumn;

    @FXML
    private TableColumn<MonthlyReport, Double> rateColumn;

    @FXML
    private TableColumn<DrugUsageReport, Integer> drugSttColumn;

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
        sttColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        patientCountColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        revenueColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        rateColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        drugSttColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        drugNameColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        usageCountColumn.setCellValueFactory(new PropertyValueFactory<>(""));
    }

    private void loadMonthlyReportData() {
        ReportDAO dao = new ReportDAO();
        List<MonthlyReport> reports = dao.getMonthlyReports();
        monthlyReportTable.getItems().setAll(reports);
    }

    private void loadDrugUsageReportData() {
            List<DrugUsageReport> reports = reportDAO.getDrugUsageReports();
            drugUsageReportTable.getItems().setAll(reports);
    }

    private void initializeChart() {
        // Gán dữ liệu cho biểu đồ customerChart ở đây
    }
}

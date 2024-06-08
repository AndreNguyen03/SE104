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

    @FXML
    public void initialize() {
        loadMonthlyReportData();
        loadDrugUsageReportData();
        initializeChart();
    }

    private void loadMonthlyReportData() {
        try (Connection conn = ConnectDB.getInstance().databaseLink) {
            ReportDAO dao = new ReportDAO();
            List<MonthlyReport> reports = dao.getMonthlyReports(conn);
            monthlyReportTable.getItems().setAll(reports);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDrugUsageReportData() {
        try (Connection conn = ConnectDB.getInstance().databaseLink) {
            ReportDAO dao = new ReportDAO();
            List<DrugUsageReport> reports = dao.getDrugUsageReports(conn);
            drugUsageReportTable.getItems().setAll(reports);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeChart() {
        // Gán dữ liệu cho biểu đồ customerChart ở đây
    }
}

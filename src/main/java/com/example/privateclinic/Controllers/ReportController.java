package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.ReportDAO;
import com.example.privateclinic.Models.Report.DoctorReport;
import com.example.privateclinic.Models.Report.DrugUsageReport;
import com.example.privateclinic.Models.Model;
import com.example.privateclinic.Models.Report.MonthlyReport;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;

public class ReportController {

    public TableView<DoctorReport> doctorUsageReportTable;
    public TableColumn<DoctorReport,Integer> doctorSttColumn;
    public TableColumn<DoctorReport,String>  doctorNameColumn;
    public TableColumn<DoctorReport,String>  doctorPositionColumn;
    public TableColumn<DoctorReport,Integer>  doctorQuantityColumn;
    public TableColumn<DoctorReport,Integer>  doctorTotalColumn;
    public TableColumn<DoctorReport,Integer>  doctorIDColumn;
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
    ObservableList<DrugUsageReport> drugMonthUsageReports,drugYearUsageReports;
    @FXML
    public void initialize() {
        SetUp();
        SetUpTable();
        LoadData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
        LoadProductMonthBarChart();
    }

    private void LoadData(int day,int month,int year) {
        paneProgress.setVisible(true);
        new Thread(()->{
            try {
                LoadThreePanel(day,month,year);
                ObservableList<MonthlyReport> observableReports = FXCollections.observableArrayList(reportDAO.getMonthlyReports(month,year));
                drugMonthUsageReports= FXCollections.observableArrayList(reportDAO.getDrugUsageReports(month,year));
                drugYearUsageReports = FXCollections.observableArrayList(reportDAO.getDrugYearUsageReports(year));
                ObservableList<DoctorReport> observableDoctorMonthlyReport = FXCollections.observableArrayList(reportDAO.getDoctorReport(month,year));
                Platform.runLater(() -> {
                    doctorUsageReportTable.setItems(observableDoctorMonthlyReport);
                    monthlyReportTable.setItems(observableReports);
                    drugUsageReportTable.setItems(drugMonthUsageReports);
                        });
                LoadChar();
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
            valueRevenueMonth.setText((reportDAO.GetRevenueFromExamMonth(month,year))+" VNĐ");
            valueRevenueToday.setText((reportDAO.GetRevenueFromExamToday(day,month,year))+" VNĐ");
            numberExamOfMonth.setText(reportDAO.GetNumberExamOfMonth(month,year) + " ca");
            numberExamOfToday.setText(reportDAO.GetNumberExamOfToday(day,month,year)+" ca");
            ConvertValueNullToZero(numberProductOfMonth,numberProductOfDay,valueRevenueMonth,valueRevenueToday,numberExamOfMonth,numberExamOfToday);
        } catch (SQLException e){
            e.printStackTrace();
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
        if(node==null) return;
        if(node.getId().equals("productMonthBar")) {
            LoadProductMonthPieChart();
        } else if(node.getId().equals("productMonthPie")) {
            LoadProductMonthPieChart();
        } else if (node.getId().equals("revenueBar")) {
                LoadRevenueBarChar();
        } else if(node.getId().equals("productYearBar")) {
            LoadProductYearBarChart();
        } else if(node.getId().equals("productYearPie")) {
            LoadProductYearPieChar();
        } else if(node.getId().equals("patientYearBar")) {
            LoadPatientsCountEachMonthBarChar();
        } else if(node.getId().equals("patientMonthBar")) {
            LoadPatientsCountEachDayBarChar();
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

        doctorSttColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(doctorUsageReportTable.getItems().indexOf(cellData.getValue()) + 1).asObject());
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("employName"));
        doctorPositionColumn.setCellValueFactory(new PropertyValueFactory<>("employPosition"));
        doctorQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("turn"));
        doctorTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        doctorIDColumn.setCellValueFactory(new PropertyValueFactory<>("employId"));
    }
    private void LoadProductMonthPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int count=0;

        for(DrugUsageReport drug : drugMonthUsageReports) {
            if(count++==20) break;
            pieChartData.add(new PieChart.Data(drug.getDrugName(), drug.getQuantity()));
        }
        //Create PieChsrt object
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Biểu đồ cơ cấu các sản phẩm bán chạy trong tháng "+cbMonth.getValue()+" năm " + cbYear.getValue());
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(50);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(180);
        Platform.runLater(() -> {
            pieChart.setId("productMonthPie");
            borderPane.setCenter(pieChart);
            if(drugUsageReportTable.getItems().isEmpty()) {
                panelNoData.setVisible(true);
            } else panelNoData.setVisible(false);
        });
    }
    private void LoadProductMonthBarChart() {
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
        barChart.setId("productMonthBar");
        borderPane.setCenter(barChart);
        if(drugUsageReportTable.getItems().isEmpty()) {
            panelNoData.setVisible(true);
        } else panelNoData.setVisible(false);
    }

    private void LoadRevenueBarChar() {
        paneProgress.setVisible(true);
        new Thread(()->{
            try {
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
                if(!haveData) {
                    panelNoData.setVisible(true);
                } else panelNoData.setVisible(false);
                Platform.runLater(() -> {
                    barChart.getData().add(data);
                    //add to border pane
                    barChart.setId("revenueBar");
                    borderPane.setCenter(barChart);
                });
            } finally {
                Platform.runLater(() -> {
                    paneProgress.setVisible(false);
                });
            }
        }).start();
    }

    private void LoadProductYearBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Sản phẩm");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Số lượng đã bán");

        BarChart barChart = new BarChart(xAxis,yAxis);

        XYChart.Series data = new XYChart.Series();
        data.setName("Những sản phẩm hàng hàng đầu năm "+cbYear.getValue()+" ");

        //provide data
        /*int count=0;*/
        for(DrugUsageReport bill : drugYearUsageReports) {
            data.getData().add(new XYChart.Data(bill.getDrugName(),bill.getQuantity()));
            /*if(++count==10) break;*/
        }
        barChart.getData().add(data);

        //add to border pane
        barChart.setId("productYearBar");
        borderPane.setCenter(barChart);
        if(drugUsageReportTable.getItems().isEmpty()) {
            panelNoData.setVisible(true);
        } else panelNoData.setVisible(false);
    }
    private void LoadPatientsCountEachMonthBarChar() {
        paneProgress.setVisible(true);
        new Thread(()->{
            try {
                CategoryAxis xAxis = new CategoryAxis();
                xAxis.setLabel("Tháng");

                NumberAxis yAxis = new NumberAxis();
                yAxis.setLabel("Người");

                BarChart barChart = new BarChart(xAxis, yAxis);

                XYChart.Series data = new XYChart.Series();
                data.setName("Theo dõi số lượng bệnh nhân qua từng tháng (năm " + cbYear.getValue() + ")");

                //provide data
                /*int count=0;*/
                boolean haveData = false;
                for (int i = 1; i <= 12; i++) {
                    int dt = reportDAO.getPatientReport(i, cbYear.getValue());
                    if (dt > 0) haveData = true;
                    data.getData().add(new XYChart.Data("Tháng " + i, dt));
                }
                if (!haveData) {
                    panelNoData.setVisible(true);
                } else panelNoData.setVisible(false);
                Platform.runLater(() -> {
                    barChart.getData().add(data);
                    //add to border pane
                    barChart.setId("patientYearBar");
                    borderPane.setCenter(barChart);
                });
            } finally {
                Platform.runLater(() -> {
                    paneProgress.setVisible(false);
                });
            }
        }).start();
    }
    private void LoadPatientsCountEachDayBarChar() {
        paneProgress.setVisible(true);
        new Thread(()->{
            try {
                Thread.sleep(800);
                    CategoryAxis xAxis = new CategoryAxis();
                    xAxis.setLabel("Ngày");

                    NumberAxis yAxis = new NumberAxis();
                    yAxis.setLabel("Người");

                    BarChart barChart = new BarChart(xAxis,yAxis);

                    XYChart.Series data = new XYChart.Series();
                    data.setName("Theo dõi số lượng bệnh nhân qua từng ngày (tháng "+cbMonth.getValue()+"- năm "+cbYear.getValue()+")");

                    YearMonth yearMonth = YearMonth.of(cbYear.getValue(),cbMonth.getValue());
                    int daysInMonth = yearMonth.lengthOfMonth();
                    boolean haveData = false;
                    for(int i =1 ;i <=daysInMonth;i++) {
                        int dt = reportDAO.getPatientReport(i,cbMonth.getValue(),cbYear.getValue());
                        if(dt>0&&!haveData) haveData = true;
                        data.getData().add(new XYChart.Data("Ngày "+i,dt));
                    }
                if(!haveData) {
                    panelNoData.setVisible(true);
                } else panelNoData.setVisible(false);
                Platform.runLater(() -> {
                    //add to border pane
                    barChart.setId("patientMonthBar");
                    borderPane.setCenter(barChart);
                    barChart.getData().add(data);
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
    private void LoadDoctorMonthPieChar() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int count=0;

        for(DoctorReport doctor : doctorUsageReportTable.getItems()) {
            if(count++==20) break;
            pieChartData.add(new PieChart.Data(doctor.getEmployId()+"-"+doctor.getEmployName(), doctor.getTotal()));
        }
        //Create PieChsrt object
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Biểu đồ đóng góp doanh thu (nhân viên) tháng " + cbMonth.getValue());
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(50);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(180);

        pieChart.setId("productYearPie");
        borderPane.setCenter(pieChart);
        if(drugUsageReportTable.getItems().isEmpty()) {
            panelNoData.setVisible(true);
        } else panelNoData.setVisible(false);

    }
    private void LoadProductYearPieChar() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int count=0;

        for(DrugUsageReport drug : drugYearUsageReports) {
            if(count++==20) break;
            pieChartData.add(new PieChart.Data(drug.getDrugName(), drug.getQuantity()));
        }
        //Create PieChsrt object
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Biểu đồ cơ cấu các sản phẩm bán chạy trong năm " + cbYear.getValue());
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(50);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(180);

        pieChart.setId("productYearPie");
        borderPane.setCenter(pieChart);
        if(drugUsageReportTable.getItems().isEmpty()) {
            panelNoData.setVisible(true);
        } else panelNoData.setVisible(false);
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
    public void handlePickMonth(ActionEvent event) {
        LoadData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
    }
    public void handlePickYear(ActionEvent event) {
        LoadData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
    }
    public void handleRevenueBarChar(ActionEvent event) {
        LoadRevenueBarChar();
    }

    public void handleProductMonthPieChar(ActionEvent event) {
        LoadProductMonthPieChart();
    }
    public void handleProductYearPieChar(ActionEvent event) {
        LoadProductYearPieChar();
    }
    public void handleDoctorMonthPieChar(ActionEvent event) {
        LoadDoctorMonthPieChar();
    }
    public void handleProductMonthBarChart(ActionEvent event) {
        LoadProductMonthBarChart();
    }
    public void handleProductYearBarChart(ActionEvent event) {
        LoadProductYearBarChart();
    }
    public void handleShowHistory(ActionEvent event) {
        Model.getInstance().getViewFactory().showReportHistory();
    }

    public void handlePatientsCountEachMonth(ActionEvent event) {
        LoadPatientsCountEachMonthBarChar();
    }
    public void handlePatientsCountEachDay(ActionEvent event) {
        LoadPatientsCountEachDayBarChar();
    }

    public void handleReFresh(ActionEvent event) {
        LoadData(cbDay.getValue(),cbMonth.getValue(),cbYear.getValue());
    }
}

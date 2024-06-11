package com.example.privateclinic.Controllers;

import com.example.privateclinic.DataAccessObject.DiseaseDAO;
import com.example.privateclinic.Models.Disease;
import com.example.privateclinic.Models.Model;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

import java.io.File;
import java.net.URL;
import java.text.Normalizer;
import java.util.List;

public class Category_DiseaseController implements Initializable {

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton editButton;

    @FXML
    private JFXButton deleteButton;

    @FXML
    private JFXButton addExcelFileBtn, sampleExcelFileBtn, saveExcelFileBtn, cancelExcelFileBtn;

    @FXML
    private TextField diseaseICDTextField;

    @FXML
    private TextField diseaseNameTextField;

    @FXML
    private TextField tf_diseaseByICD;

    @FXML
    private TextField tf_diseaseByName;

    @FXML
    private TableView<Disease> diseaseTableView;

    @FXML
    private TableColumn<Disease, Integer> idColumn;

    @FXML
    private TableColumn<Disease, String> icdColumn;

    @FXML
    private TableColumn<Disease, String> nameColumn;

    @FXML
    private Text diseaseCount;
    @FXML
    private Pane lbl_header;
    private DiseaseDAO diseaseDAO = new DiseaseDAO();

    private final ObservableList<Disease> diseaseData = FXCollections.observableArrayList();
    private double xOffset;
    private  double yOffset;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        diseaseDAO = new DiseaseDAO();
        // Set up columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("maBenh"));
        icdColumn.setCellValueFactory(new PropertyValueFactory<>("maICD"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenBenh"));
        // Load data into table
        loadDiseaseData();
        searchDiseaseByICDAndByName();
        DiseaseSelection();
        getDiseaseCount();

        addButton.setOnAction(this::handleAddDisease);
        editButton.setOnAction(this::handleEditDisease);
        deleteButton.setOnAction(this::handleDeleteDisease);
        addExcelFileBtn.setOnAction(this::handleAddExcelFile);
        sampleExcelFileBtn.setOnAction(this::handleDownloadSampleExcelFile);
        saveExcelFileBtn.setOnAction(this::handleSaveExcelFile);
        cancelExcelFileBtn.setOnAction(this::handleCancelExcelFile);

        lbl_header.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        lbl_header.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX()-xOffset);
            stage.setY(mouseEvent.getScreenY()-yOffset);
        });
    }

    private void loadDiseaseData() {
        List<Disease> diseases = diseaseDAO.getAllDiseases();
        diseaseData.addAll(diseases);
        diseaseTableView.setItems(diseaseData);
    }
    @FXML
    void handleAddDisease(ActionEvent event) {

        String diseaseICD = diseaseICDTextField.getText();
        String diseaseName = diseaseNameTextField.getText();

        if (diseaseICD.isEmpty() || diseaseName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo!");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đầy đủ mã ICD và tên bệnh!");
            alert.showAndWait();
            return;
        }

        if (diseaseDAO.isDiseaseNameExists(diseaseName, -1) || diseaseDAO.isDiseaseICDExists(diseaseICD, -1)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo!");
            alert.setHeaderText(null);
            alert.setContentText("Tên bệnh hoặc mã ICD đã tồn tại!");
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận thêm");
            alert.setHeaderText("Bạn có chắc chắn muốn thêm?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                diseaseDAO.addDisease(new Disease(diseaseDAO.getNextDiseaseId(), diseaseName, diseaseICD));
                diseaseData.clear();
                clearDiseaseFields();
                loadDiseaseData();
                getDiseaseCount();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Thành công");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Đã thêm bệnh thành công.");
                successAlert.showAndWait();
            }
        }
    }

    @FXML
    void handleEditDisease(ActionEvent event) {
        Disease selectedDisease = diseaseTableView.getSelectionModel().getSelectedItem();
        String diseaseICD = diseaseICDTextField.getText();
        String diseaseName = diseaseNameTextField.getText();

        if (diseaseICD.isEmpty() || diseaseName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo!");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đầy đủ mã ICD và tên bệnh!");
            alert.showAndWait();
            return;
        }

        if (diseaseDAO.isDiseaseNameExists(diseaseName, selectedDisease.getMaBenh()) ||
                diseaseDAO.isDiseaseICDExists(diseaseICD, selectedDisease.getMaBenh())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo!");
            alert.setHeaderText(null);
            alert.setContentText("Tên bệnh hoặc mã ICD đã tồn tại!");
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận chỉnh sửa");
            alert.setHeaderText("Bạn có chắc chắn muốn chỉnh sửa?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                selectedDisease.setMaICD(diseaseICD);
                selectedDisease.setTenBenh(diseaseName);

                diseaseDAO.updateDisease(selectedDisease);
                diseaseData.clear();
                clearDiseaseFields();
                loadDiseaseData();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Thành công");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Đã cập nhật bệnh thành công.");
                successAlert.showAndWait();
            }
        }
    }

    @FXML
    void handleDeleteDisease(ActionEvent event) {
        Disease selectedDisease = diseaseTableView.getSelectionModel().getSelectedItem();
        if (selectedDisease != null) {
            Alert confirmDeleteAlert = new Alert(Alert.AlertType.WARNING);
            confirmDeleteAlert.setTitle("Xác nhận xóa");
            confirmDeleteAlert.setHeaderText("Bạn có chắc chắn muốn xóa bệnh này?");
            confirmDeleteAlert.setContentText("Bệnh này sẽ bị xóa khỏi cơ sở dữ liệu");

            Optional<ButtonType> result = confirmDeleteAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int diseaseID = selectedDisease.getMaBenh();
                diseaseDAO.deleteDisease(diseaseID);
                diseaseData.clear(); // Xóa dữ liệu hiện tại trong bảng
                diseaseDAO.updateDiseaseIds(); // Cập nhật lại các ID sau khi xóa
                loadDiseaseData(); // Tải lại dữ liệu từ cơ sở dữ liệu
                getDiseaseCount();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Thành công");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Đã xóa bệnh thành công.");
                successAlert.showAndWait();
            }
        } else {
            // Hiển thị thông báo lỗi nếu không có hàng nào được chọn
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một bệnh để xóa.");
            alert.showAndWait();
        }
    }

    private void DiseaseSelection() {
        diseaseTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Lấy thông tin của thuốc từ hàng được chọn
                Disease selectedMedicine = diseaseTableView.getSelectionModel().getSelectedItem();

                // Đặt thông tin của thuốc vào các trường TextField và ComboBox
                diseaseICDTextField.setText(selectedMedicine.getMaICD());
                diseaseNameTextField.setText(selectedMedicine.getTenBenh());

            } else {
                // Nếu không có hàng nào được chọn, xóa nội dung của các trường
                diseaseICDTextField.clear();
                diseaseNameTextField.clear();
            }
        });
    }

    private void clearDiseaseFields() {
        diseaseICDTextField.clear();
        diseaseNameTextField.clear();
    }

    private void getDiseaseCount() {
        int maxMabenh = diseaseDAO.getMaxDiseaseId();
        diseaseCount.setText(String.valueOf(maxMabenh));
    }

    private void disableButtons() {
        addButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private void enableButtons() {
        addButton.setDisable(false);
        editButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    private void searchDiseaseByICDAndByName() {
        tf_diseaseByICD.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                diseaseTableView.setItems(diseaseData);
            } else {
                ObservableList<Disease> filteredList = FXCollections.observableArrayList();
                String strippedValue = removeAccentsAndToLower(newValue);
                for (Disease disease : diseaseData) {
                    String strippedICD = removeAccentsAndToLower(disease.getMaICD());
                    if (strippedICD.contains(strippedValue)) {
                        filteredList.add(disease);
                    }
                }
                diseaseTableView.setItems(filteredList);
            }
        });

        tf_diseaseByName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                diseaseTableView.setItems(diseaseData);
            } else {
                ObservableList<Disease> filteredList = FXCollections.observableArrayList();
                String strippedValue = removeAccentsAndToLower(newValue);
                for (Disease disease : diseaseData) {
                    String strippedName = removeAccentsAndToLower(disease.getTenBenh());
                    if (strippedName.contains(strippedValue)) {
                        filteredList.add(disease);
                    }
                }
                diseaseTableView.setItems(filteredList);
            }
        });
    }


    private String removeAccentsAndToLower(String input) {
        if (input == null) {
            return null;
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase();
    }

    @FXML
    void handleAddExcelFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn tệp Excel");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel files", "*.xlsx", "*.xls")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                ObservableList<Disease> excelData = readDataFromExcel(selectedFile);
                if (excelData != null) {
                    diseaseTableView.setItems(excelData);
                    saveExcelFileBtn.setDisable(false);
                    cancelExcelFileBtn.setDisable(false);
                    disableButtons();
                    showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Nhập dữ liệu từ Excel thành công.");

                } else {
                    showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể đọc dữ liệu từ Excel.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi khi đọc tệp Excel.");
            }
        }
    }

    private ObservableList<Disease> readDataFromExcel(File file) throws IOException {
        ObservableList<Disease> excelData = FXCollections.observableArrayList();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            int count = 0;
            DataFormatter dataFormatter = new DataFormatter();
            while (rowIterator.hasNext()) {
                count++;
                Row row = rowIterator.next();
                String icd = dataFormatter.formatCellValue(row.getCell(0));
                String name = dataFormatter.formatCellValue(row.getCell(1));

                if (!isString(icd) || !isString(name)) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "File Excel không đúng mẫu.");
                    return null;
                }
                if (icd.isEmpty() || name.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Cảnh báo", "File Excel không đúng mẫu.");
                    return null;
                }
                excelData.add(new Disease(count, name, icd));
                int maxMabenh = count;
                diseaseCount.setText(String.valueOf(maxMabenh));
            }
        }
        return excelData;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isString(String value) {
        try {
            Double.parseDouble(value);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    @FXML
    void handleSaveExcelFile(ActionEvent event) {
        // Kiểm tra nếu TableView không có dữ liệu
        if (diseaseTableView.getItems().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không có dữ liệu để lưu.");
            return;
        }

        // Lấy dữ liệu từ TableView
        ObservableList<Disease> diseases = diseaseTableView.getItems();

        // Tạo danh sách các bệnh mới
        List<Disease> newDiseases = new ArrayList<>();
        int nextId = diseaseDAO.getNextDiseaseId(); // Lấy id tiếp theo từ cơ sở dữ liệu
        for (Disease disease : diseases) {
            // Kiểm tra xem tên bệnh hoặc icd đã tồn tại trong cơ sở dữ liệu hay không
            if (!diseaseDAO.isDiseaseNameExists(disease.getTenBenh(), 0) &&
                    !diseaseDAO.isDiseaseICDExists(disease.getMaICD(), 0)) {
                // Gán id mới cho bệnh và thêm vào danh sách bệnh mới
                disease.setMaBenh(nextId++);
                newDiseases.add(disease);
            }
        }

        diseaseDAO.addDiseases(newDiseases);
        diseaseData.clear();
        loadDiseaseData();
        getDiseaseCount();
        saveExcelFileBtn.setDisable(true);
        cancelExcelFileBtn.setDisable(true);
        enableButtons();
        showAlert(Alert.AlertType.INFORMATION, "Thông báo!", "Đã lưu dữ liệu thành công.");
    }

    @FXML
    void handleDownloadSampleExcelFile(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn nơi lưu");

            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files", "*.xlsx", "*.xls"));

            String defaultFileName = "sampleExcelFile.xlsx";
            String userDownloadsDirectory = System.getProperty("user.home") + File.separator + "Downloads";

            File defaultDirectory = new File(userDownloadsDirectory);
            if (!defaultDirectory.exists()) {
                defaultDirectory.mkdirs();
            }

            fileChooser.setInitialDirectory(defaultDirectory);

            // Kiểm tra xem file có tồn tại không, nếu có thêm số thứ tự vào tên file
            File selectedFile = new File(defaultDirectory, defaultFileName);
            int index = 1;
            while (selectedFile.exists()) {
                defaultFileName = "sampleExcelFile(" + index + ").xlsx";
                selectedFile = new File(defaultDirectory, defaultFileName);
                index++;
            }

            fileChooser.setInitialFileName(defaultFileName);
            selectedFile = fileChooser.showSaveDialog(null);

            if (selectedFile != null) {
                InputStream inputStream = getClass().getResourceAsStream("/com/example/privateclinic/Excel/samplefile.xlsx");
                Files.copy(inputStream, selectedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Desktop.getDesktop().open(selectedFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCancelExcelFile(ActionEvent event) {
        diseaseData.clear();
        clearDiseaseFields();
        loadDiseaseData();
        getDiseaseCount();
        saveExcelFileBtn.setDisable(true);
        cancelExcelFileBtn.setDisable(true);
        enableButtons();
    }

    @FXML
    void close(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(s);
    }

    public void minimizeCategory(MouseEvent mouseEvent) {
        Model.getInstance().getViewFactory().minimizeStage((Stage) addExcelFileBtn.getScene().getWindow());
    }
}


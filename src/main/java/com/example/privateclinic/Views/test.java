package com.example.privateclinic.Views;
import javafx.print.PrinterJob;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class test {
    public static void main(String[] args) {
        String content = "Chào bạn! Xin chào thế giới!"; // Văn bản tiếng Việt

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
            Font font = new Font("Arial Unicode MS", 12); // Sử dụng font hỗ trợ Unicode
            Text text = new Text(content);
            text.setFont(font);

            boolean success = job.printPage(text);
            if (success) {
                job.endJob();
            }
        }
    }
}

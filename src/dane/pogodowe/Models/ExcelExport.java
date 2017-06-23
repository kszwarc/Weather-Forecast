/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dane.pogodowe.Models;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 *
 * @author pc
 */
public class ExcelExport 
{
    private WritableCellFormat font;
    private final String[] headers = {"Dzień", "Temperatura [C]", "Temperatura minimalna [C]", "Temperatura maksymalna [C]", "Wilgotność [%]", "Ciśnienie [hPa]", "Prędkość wiatru [m/s]", "Kierunek wiatru [stopnie]", "Opady deszczu [mm/m^2]"};

    public File getFileLocation(Stage actualStage)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Eksport danych pogodowych");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Microsoft Excel", "*.xls"));
        fileChooser.setInitialFileName("Dane pogodowe");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        return fileChooser.showSaveDialog(actualStage);
    }
    
    public void write(File file, String operation, WeatherData[] actualDayData, WeatherData[] nextDaysData) throws IOException, WriteException 
    {
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("pl", "PL"));
        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        createLabel();
        workbook.createSheet("Dzisiaj", 0);
        WritableSheet excelSheet = workbook.getSheet(0);
        addData(excelSheet, actualDayData, this.headers);
        if (operation.length()>31)
            operation = operation.substring(0, 31);
        workbook.createSheet(operation, 1);
        excelSheet = workbook.getSheet(1);
        addData(excelSheet, nextDaysData, this.headers);
        saveWorkbook(workbook);
    }
    
    public void write(File file, String operation, WeatherData[] nextDaysData) throws IOException, WriteException 
    {
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("pl", "PL"));
        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        createLabel();
        if (operation.length()>31)
            operation = operation.substring(0, 31);
        workbook.createSheet(operation, 0);
        WritableSheet excelSheet = workbook.getSheet(0);
        addData(excelSheet, nextDaysData, this.headers);
        saveWorkbook(workbook);
    }
    
    private void saveWorkbook(WritableWorkbook workbook) throws IOException, WriteException 
    {
        workbook.write();
        workbook.close();
        Messages.showSuccess("Dane zostały pomyślnie wyeksportowane.");
    }
    
    private void addData(WritableSheet sheet, WeatherData[] data, String[] headers) throws WriteException, RowsExceededException 
    {
        for (int i = 0; i < headers.length; i++)
            addLabel(sheet, i, 0, headers[i]);
        for (int i=0; i<data.length; i++)
        {
            addLabel(sheet, 0, i+1, data[i].getDateTimeText());
            addNumber(sheet, 1, i+1, data[i].getTemperature());
            addNumber(sheet, 2, i+1, data[i].getTemperatureMin());
            addNumber(sheet, 3, i+1, data[i].getTemperatureMax());
            addNumber(sheet, 4, i+1, data[i].getHumidity());
            addNumber(sheet, 5, i+1, data[i].getPressure());
            addNumber(sheet, 6, i+1, data[i].getSpeed());
            addNumber(sheet, 7, i+1, data[i].getDeg());
            addNumber(sheet, 8, i+1, data[i].getFall());
        }
    }
    
    private void addNumber(WritableSheet sheet, int column, int row, double doubleNumber) throws WriteException, RowsExceededException 
    {
        Number number;
        number = new Number(column, row, doubleNumber, this.font);
        sheet.addCell(number);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s) throws WriteException, RowsExceededException 
    {
        Label label;
        label = new Label(column, row, s, this.font);
        sheet.addCell(label);
    }
    
    private void createLabel() throws WriteException 
    {
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        this.font = new WritableCellFormat(times10pt);
        this.font.setWrap(true);
        CellView cellView = new CellView();
        cellView.setFormat(this.font);
        cellView.setAutosize(true);
    }
}

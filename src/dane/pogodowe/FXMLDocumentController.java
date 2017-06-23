package dane.pogodowe;
import dane.pogodowe.Models.ChartOperations;
import dane.pogodowe.Models.WeatherData;
import dane.pogodowe.Models.OpenWeatherMap;
import dane.pogodowe.Models.CityFromFile;
import dane.pogodowe.Models.Database;
import dane.pogodowe.Models.ExcelExport;
import dane.pogodowe.Models.Messages;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author pc
 */
public class FXMLDocumentController implements Initializable 
{
    private final CityFromFile cityFromFile = new CityFromFile();
    private final OpenWeatherMap openWeatherMap = new OpenWeatherMap();
    private final Database database = new Database();
    private Stage archiveStage = null;
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private VBox VBoxOption;
    @FXML
    private HBox HBoxCity;
    @FXML
    private TextField CityText;
    @FXML
    private Button LookEventButton;
    @FXML
    private ProgressIndicator ProgressIndicator;
    @FXML
    private NumberTextField NumberOfDays;
    @FXML
    private ChoiceBox DataTypeCombo;
    @FXML
    private CheckBox IsPolandCheckbox;
    @FXML
    private Label NumberOfDaysLabel, FoundCity;
    @FXML
    private LineChart TodayTemperatureChart, SecondTemperatureChart, TodayHumidityChart, SecondHumidityChart;
    @FXML
    private AreaChart TodayPressureChart, SecondPressureChart, TodayWindSpeedChart, SecondWindSpeedChart, TodayWindDegreesChart, SecondWindDegreesChart, TodayFallChart, SecondFallChart;
    
    @FXML
    private void validateNumberOfDays()
    {
        String text = NumberOfDays.getText();
        if (!text.isEmpty() && (Integer.parseInt(text)>16 || Integer.parseInt(text)<=0))
            NumberOfDays.setText("16");
        if (text.length()>0)
            lookEvent();
    }  
    
    @FXML
    private void showArchive()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DatabaseManagement.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            if (this.archiveStage!=null)
                this.archiveStage.close();
            this.archiveStage = new Stage();
            this.archiveStage.setTitle("Dane pogodowe");
            this.archiveStage.getIcons().add(new Image(DanePogodowe.class.getResourceAsStream("logo.png")));
            this.archiveStage.setScene(new Scene(root1));  
            this.archiveStage.show();
        }
        catch (Exception ex)
        {
            Messages.showError(ex.getMessage());
        }
    }
    
    @FXML
    private void lookEvent()
    {
        String city = CityText.getText();
        if (isCityValid(city))
        {
            try
            {
                LineChart[] lineToday = {TodayTemperatureChart, TodayHumidityChart};
                AreaChart[] areaToday = {TodayPressureChart, TodayWindSpeedChart, TodayWindDegreesChart, TodayFallChart};
                LineChart[] lineNextDays = {SecondTemperatureChart, SecondHumidityChart};
                AreaChart[] areaNextDays = {SecondPressureChart, SecondWindSpeedChart, SecondWindDegreesChart, SecondFallChart};
                String option = DataTypeCombo.getValue().toString();
                this.cityFromFile.writeCityToFile(city);
                showHideProgress(false);
                Service<Void> service = getDataFromAPIAndPutToCharts(lineToday, areaToday, lineNextDays, areaNextDays, option, this.database, city, IsPolandCheckbox.isSelected(), this.openWeatherMap);
                service.start();
            }
            catch (Exception ex){}
        }
    }
    
    @FXML
    private void export()
    {
        String city = CityText.getText();
        if (isCityValid(city))
        {
            try
            {
                ExcelExport excel = new ExcelExport();
                Stage actualStage = (Stage)FoundCity.getScene().getWindow();
                File file = excel.getFileLocation(actualStage);
                if (file != null) 
                {
                    String operation = DataTypeCombo.getValue().toString();
                    showHideProgress(false);
                    Service<Void> service = getDataFromAPIAndExportThem(this.openWeatherMap, city, operation, excel, file);
                    service.start();
                }
            }
            catch (Exception ex){}
        }
    }
    
    private void showHideProgress(Boolean showConfig)
    {
        this.LookEventButton.visibleProperty().set(showConfig);
        this.HBoxCity.visibleProperty().set(showConfig);
        this.VBoxOption.visibleProperty().set(showConfig);
        this.ProgressIndicator.visibleProperty().set(!showConfig);
    }
    
    private Boolean isCityValid(String city)
    {
        if (city.length()==0)
        {
            Messages.showError("Nie wypełniłeś pola z nazwą miasta.");
            return false;
        }
        else
            return true;
    }
    
    private int getNumberOfDays()
    {
        try
        {
            int num = Integer.parseInt(NumberOfDays.getText());
            if (num<=0 || num>16)
            {
                NumberOfDays.setText("16");
                return 16;
            }
            else
                return num;
        }
        catch (NumberFormatException ex) 
        {
            NumberOfDays.setText("1");
            return 1;
        }
    }
  
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        CityText.setText(this.cityFromFile.readCityFromFileOrIP());
        if (CityText.getText().length()>0)
            lookEvent();
        
        DataTypeCombo.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() 
        {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number newNumber) 
            {
                Boolean visible;
                if (DataTypeCombo.getItems().get((Integer) newNumber).equals("1-16 dniowa z dokładnością dzienną"))
                  visible = true;
                else
                  visible = false;
                NumberOfDays.visibleProperty().set(visible);
                NumberOfDaysLabel.visibleProperty().set(visible);
            }
        });
    }
    
    private Service<Void> getDataFromAPIAndPutToCharts(LineChart[] lineToday, AreaChart[] areaToday, LineChart[] lineNextDays, AreaChart[] areaNextDays, String option, Database database, String city, Boolean isCityInPoland, OpenWeatherMap openWeatherMap)
    {
        Service<Void> service = new Service<Void>() 
        {
            @Override
            protected Task<Void> createTask() 
            {
                return new Task<Void>() 
                {           
                    @Override
                    protected Void call() throws Exception 
                    {
                        try
                        {
                            final CountDownLatch latch = new CountDownLatch(1);
                            WeatherData[] data = openWeatherMap.getDataFromAPI(city, isCityInPoland, "Dzisiaj", 1);
                            WeatherData[] dataForNextDays = openWeatherMap.getDataFromAPI(city, isCityInPoland,  option, getNumberOfDays());
                           
                            try
                            {
                                database.insertData(dataForNextDays, option);
                            } 
                            catch(Exception ex) {}
                            
                            Platform.runLater(new Runnable() 
                            {                          
                                @Override
                                public void run() 
                                {
                                    try
                                    {
                                        ChartOperations charts = new ChartOperations();
                                        charts.addDataToCharts(data, lineToday, areaToday);
                                        FoundCity.setText("Dane dla miasta: "+data[0].getCity());
                                        charts.addDataToCharts(dataForNextDays, lineNextDays, areaNextDays);
                                        showHideProgress(true);
                                    }
                                    catch (Exception ex)
                                    {
                                        showErrorFromThreadAndUnhideGUI(ex.getMessage());
                                    }
                                    finally
                                    {
                                        latch.countDown();
                                    }
                                }
                            });
                            latch.await();  
                        }
                        catch (Exception ex)
                        {
                            showErrorFromThreadAndUnhideGUI(ex.getMessage());
                        }
                        return null;
                    }
                };
            }
        };
        return service;
    }
    
    private Service<Void> getDataFromAPIAndExportThem(OpenWeatherMap openWeatherMap, String city, String operation, ExcelExport excel, File file)
    {
        Service<Void> service = new Service<Void>() 
        {
            @Override
            protected Task<Void> createTask() 
            {
                return new Task<Void>() 
                {           
                    @Override
                    protected Void call() throws Exception 
                    {        
                        try
                        {
                            final CountDownLatch latch = new CountDownLatch(1);
                            WeatherData[] actualDayData = openWeatherMap.getDataFromAPI(city, IsPolandCheckbox.isSelected(),  "Dzisiaj", 1);
                            WeatherData[] nextDaysData = openWeatherMap.getDataFromAPI(city, IsPolandCheckbox.isSelected(), operation , getNumberOfDays());
                            Platform.runLater(new Runnable() 
                            {                          
                                @Override
                                public void run() 
                                {
                                    try
                                    {
                                        excel.write(file, operation, actualDayData, nextDaysData);
                                        showHideProgress(true);
                                    }
                                    catch (Exception ex)
                                    {
                                        showErrorFromThreadAndUnhideGUI(ex.getMessage());
                                    }
                                    finally
                                    {
                                        latch.countDown();
                                    }
                                }
                            });
                            latch.await();   
                        }
                        catch (Exception ex)
                        {
                            showErrorFromThreadAndUnhideGUI(ex.getMessage());
                        }
                        return null; 
                    }
                };
            }
        };
        return service;
    }
    
    private void showErrorFromThreadAndUnhideGUI(String message)
    {
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(new Runnable() 
        {                          
            @Override
            public void run() 
            {
                showHideProgress(true);
                Messages.showError(message);                                
            }
        });
        try
        {
            latch.await();   
        }
        catch (Exception ex){}
    }
}

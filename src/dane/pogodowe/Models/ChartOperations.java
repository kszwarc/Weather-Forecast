/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dane.pogodowe.Models;

import java.util.ArrayList;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 *
 * @author pc
 */
public class ChartOperations 
{
    public void addHumidity(WeatherData[] data, LineChart chart)
    {
        PrepareSeries prepareSeries = new PrepareSeries();
        XYChart.Series<String, Double> serie = prepareSeries.getHumiditySerie(data);
        addOneSerie(chart, serie);
    }
    
    public void addTemperature(WeatherData[] data, LineChart chart)
    {
        PrepareSeries prepareSeries = new PrepareSeries();
        ArrayList<XYChart.Series<String, Double>> series = prepareSeries.getTemperatureSeries(data);
        addMultipleSeries(chart, series);
    }
    
    public void addPresure(WeatherData[] data, AreaChart chart)
    {
        PrepareSeries prepareSeries = new PrepareSeries();
        XYChart.Series<String, Double> serie = prepareSeries.getPressureSerie(data);
        addOneSerie(chart, serie);
    }
    
    public void addWindSpeed(WeatherData[] data, AreaChart chart)
    {
        PrepareSeries prepareSeries = new PrepareSeries();
        XYChart.Series<String, Double> serie = prepareSeries.getWindSpeedSerie(data);
        addOneSerie(chart, serie);
    }
    
    public void addWindDegrees(WeatherData[] data, AreaChart chart)
    {
        PrepareSeries prepareSeries = new PrepareSeries();
        XYChart.Series<String, Double> serie = prepareSeries.getWindDegreesSerie(data);
        addOneSerie(chart, serie);
    }
    
    public void addFall(WeatherData[] data, AreaChart chart)
    {
        PrepareSeries prepareSeries = new PrepareSeries();
        XYChart.Series<String, Double> serie = prepareSeries.getFallSerie(data);
        addOneSerie(chart, serie);
    }
    
    public void addDataToCharts(WeatherData[] data, LineChart[] lineCharts, AreaChart[] areaCharts)
    {
        if (data!=null) 
        {
            this.addTemperature(data, lineCharts[0]);
            this.addHumidity(data, lineCharts[1]);
            this.addPresure(data, areaCharts[0]);
            this.addWindSpeed(data, areaCharts[1]);
            this.addWindDegrees(data, areaCharts[2]);
            this.addFall(data, areaCharts[3]);
        }
    }
    
    private void addOneSerie(LineChart chart, XYChart.Series<String, Double> serie)
    {
        chart.getData().clear();
        chart.getData().addAll(serie);
    }
    
    private void addMultipleSeries(LineChart chart, ArrayList<XYChart.Series<String, Double>> series)
    {
        chart.getData().clear();
        int seriesSize = series.size(); 
        for (int i=0; i<seriesSize; i++)
            chart.getData().addAll(series.get(i));
    }
    
    private void addOneSerie(AreaChart chart, XYChart.Series<String, Double> serie)
    {
        chart.getData().clear();
        chart.getData().addAll(serie);
    }
    
    private void addMultipleSeries(AreaChart chart, ArrayList<XYChart.Series<String, Double>> series)
    {
        chart.getData().clear();
        int seriesSize = series.size(); 
        for (int i=0; i<seriesSize; i++)
            chart.getData().addAll(series.get(i));
    }
}

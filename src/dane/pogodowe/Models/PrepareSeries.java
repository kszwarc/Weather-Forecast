/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dane.pogodowe.Models;

import java.util.ArrayList;
import javafx.scene.chart.XYChart;

/**
 *
 * @author pc
 */
public class PrepareSeries 
{
    public ArrayList<XYChart.Series<String, Double>> getTemperatureSeries(WeatherData[] data)
    {
        XYChart.Series<String, Double> serieTemperature = new XYChart.Series<String, Double>();
        XYChart.Series<String, Double> serieTemperatureMin = new XYChart.Series<String, Double>();
        XYChart.Series<String, Double> serieTemperatureMax = new XYChart.Series<String, Double>();
            
        serieTemperature.setName("Średnia/aktualna temperatura");
        for (int i=0; i<data.length; i++)
            serieTemperature.getData().add(new XYChart.Data(data[i].getDateTimeText(), data[i].getTemperature()));
        serieTemperatureMin.setName("Minimalna temperatura");
        for (int i=0; i<data.length; i++)
            serieTemperatureMin.getData().add(new XYChart.Data(data[i].getDateTimeText(), data[i].getTemperatureMin()));
        serieTemperatureMax.setName("Maksymalna temperatura");
        for (int i=0; i<data.length; i++)
            serieTemperatureMax.getData().add(new XYChart.Data(data[i].getDateTimeText(), data[i].getTemperatureMax()));
        
        ArrayList<XYChart.Series<String, Double>> series = new ArrayList<>();
        series.add(serieTemperature);
        series.add(serieTemperatureMin);
        series.add(serieTemperatureMax);
        return series;
    }
    
    public XYChart.Series<String, Double> getHumiditySerie(WeatherData[] data)
    {
        XYChart.Series<String, Double> serieHumidity = new XYChart.Series<String, Double>();
            
        serieHumidity.setName("Wilgotność");
        for (int i=0; i<data.length; i++)
            serieHumidity.getData().add(new XYChart.Data(data[i].getDateTimeText(), data[i].getHumidity()));

        return serieHumidity;
    }
    
    public XYChart.Series<String, Double> getPressureSerie(WeatherData[] data)
    {
        XYChart.Series<String, Double> seriePressure = new XYChart.Series<String, Double>();
            
        seriePressure.setName("Ciśnienie");
        for (int i=0; i<data.length; i++)
            seriePressure.getData().add(new XYChart.Data(data[i].getDateTimeText(), data[i].getPressure()));
        
        return seriePressure;
    }
    
    public XYChart.Series<String, Double> getWindSpeedSerie(WeatherData[] data)
    {
        XYChart.Series<String, Double> serieSpeed = new XYChart.Series<String, Double>();
           
        serieSpeed.setName("Prędkość wiatru");
        for (int i=0; i<data.length; i++)
            serieSpeed.getData().add(new XYChart.Data(data[i].getDateTimeText(), data[i].getSpeed()));
        
        return serieSpeed;
    }
    
    public XYChart.Series<String, Double> getWindDegreesSerie(WeatherData[] data)
    {
        XYChart.Series<String, Double> serieDeg = new XYChart.Series<String, Double>();
           
        serieDeg.setName("Kierunek wiatru");
        for (int i=0; i<data.length; i++)
            serieDeg.getData().add(new XYChart.Data(data[i].getDateTimeText(), data[i].getDeg()));
        
        return serieDeg;
    }
    
    public XYChart.Series<String, Double> getFallSerie(WeatherData[] data)
    {
        XYChart.Series<String, Double> serieFall = new XYChart.Series<String, Double>();
           
        serieFall.setName("Opady");
        for (int i=0; i<data.length; i++)
            serieFall.getData().add(new XYChart.Data(data[i].getDateTimeText(), data[i].getFall()));
        
        return serieFall;
    }
}

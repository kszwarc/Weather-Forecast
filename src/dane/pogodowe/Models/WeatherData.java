/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dane.pogodowe.Models;


public class WeatherData 
{
    private final String city;
    private final String dateTimeText;
    private final double temperature;
    private final double temperatureMin;
    private final double temperatureMax;
    private final double humidity;
    private final double pressure;
    private final double speed;
    private final double deg;
    private final double fall;
    
    public WeatherData(String city, String dateTimeText, double temperature, double temperatureMin, double temperatureMax, double humidity, double pressure, double speed, double deg, double fall)
    {
        this.city=city;
        this.dateTimeText=dateTimeText;
        this.temperature=temperature;
        this.temperatureMin=temperatureMin;
        this.temperatureMax=temperatureMax;
        this.humidity=humidity;
        this.pressure=pressure;
        this.speed=speed;
        this.deg=deg;
        this.fall=fall;
    }
    
    public String getDateTimeText()
    {
        return this.dateTimeText;
    }
    
    public String getCity()
    {
        return this.city;
    }
    
    public double getTemperature()
    {
        return this.temperature;
    }
    
    public double getTemperatureMin()
    {
        return this.temperatureMin;
    }
    
    public double getTemperatureMax()
    {
        return this.temperatureMax;
    }
    
    public double getHumidity()
    {
        return this.humidity;
    }
    
    public double getPressure()
    {
        return this.pressure;
    }
    
    public double getSpeed()
    {
        return this.speed;
    }
    
    public double getDeg()
    {
        return this.deg;
    }
    
    public double getFall()
    {
        return this.fall;
    }
}

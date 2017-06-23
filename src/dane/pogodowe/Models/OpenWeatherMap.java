    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dane.pogodowe.Models;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 *
 * @author pc
 */
public class OpenWeatherMap 
{
    public WeatherData[] getDataFromAPI(String city, Boolean isPoland, String option, int numberOfDays) throws NotFoundCityError, Exception, InternetConnectionError
    {
        try
        {
            String APIKey = "64ef15267af680e8658a3fa3ffbf50f3";
            String countryCode = "";
            if (isPoland==true)
                countryCode = ",pl";
            if (option.equals("Dzisiaj"))
            {
                URL url = new URL(("http://api.openweathermap.org/data/2.5/weather?q="+URLEncoder.encode(city, "UTF-8")+countryCode+"&units=metric&appid="+APIKey));
                JSONObject obj = readFromURL(url);
                if (obj==null)
                    throw new InternetConnectionError();
                else if (obj.getInt("cod")==200)
                    return getDataForToday(obj);
                else
                    throw new NotFoundCityError();
            }
            else if (option.equals("5 dniowa z dokładnością 3 godzinną"))
            {
                URL url = new URL(("http://api.openweathermap.org/data/2.5/forecast?q="+URLEncoder.encode(city, "UTF-8")+countryCode+"&units=metric&appid="+APIKey));
                JSONObject obj = readFromURL(url);
                if (obj==null)
                    throw new InternetConnectionError();
                else if (obj.getInt("cod")==200)
                    return getDataForFiveDays(obj);
                else
                    throw new NotFoundCityError();
            }
            else if (option.equals("1-16 dniowa z dokładnością dzienną"))
            {
                URL url = new URL(("http://api.openweathermap.org/data/2.5/forecast/daily?q="+URLEncoder.encode(city, "UTF-8")+countryCode+"&units=metric&appid="+APIKey+"&cnt="+numberOfDays));
                JSONObject obj = readFromURL(url);
                if (obj==null)
                    throw new InternetConnectionError();
                else if (obj.getInt("cod")==200)
                    return getDataForNextDays(obj);
                else
                    throw new NotFoundCityError();
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
        return null;
    }
    
    private WeatherData[] getDataForToday(JSONObject obj)
    {
        double rain;
        JSONObject mainData = getJSONObject(obj, "main");
        JSONObject wind = getJSONObject(obj, "wind");
        if (obj.isNull("rain"))
            rain = 0.0;
        else if (!obj.getJSONObject("rain").isNull("1h"))
            rain = obj.getJSONObject("rain").getDouble("1h");
        else if (!obj.getJSONObject("rain").isNull("3h"))
            rain = obj.getJSONObject("rain").getDouble("3h");
        else
            rain = 0.0;
        
        WeatherData weatherData = new WeatherData(getStringFromAPIObject(obj, "name"), "1", getDoubleFromAPIObject(mainData, "temp"), getDoubleFromAPIObject(mainData, "temp_min"), getDoubleFromAPIObject(mainData, "temp_max"), getDoubleFromAPIObject(mainData, "humidity"), getDoubleFromAPIObject(mainData, "pressure"), getDoubleFromAPIObject(wind, "speed"), getDoubleFromAPIObject(wind, "deg"), rain);
        WeatherData[] arrayOfWeatherData = {weatherData};
        return arrayOfWeatherData;
    }
    
    private WeatherData[] getDataForFiveDays(JSONObject obj)
    {
        JSONArray array = obj.getJSONArray("list");
        int arrayLength = array.length();
        WeatherData[] arrayOfWeatherData = new WeatherData[arrayLength];
        for (int i=0; i<arrayLength; i++)
        {
            obj = array.getJSONObject(i);
            double rain;
            JSONObject mainData = getJSONObject(obj, "main");
            JSONObject wind = getJSONObject(obj, "wind");
            if (obj.isNull("rain") || obj.getJSONObject("rain").isNull("3h"))
                rain = 0.0;
            else
                rain = obj.getJSONObject("rain").getDouble("3h");
            
            WeatherData weatherData = new WeatherData("", getStringFromAPIObject(obj, "dt_txt"), getDoubleFromAPIObject(mainData, "temp"), getDoubleFromAPIObject(mainData, "temp_min"), getDoubleFromAPIObject(mainData, "temp_max"), getDoubleFromAPIObject(mainData, "humidity"), getDoubleFromAPIObject(mainData, "pressure"), getDoubleFromAPIObject(wind, "speed"), getDoubleFromAPIObject(wind, "deg"), rain);
            arrayOfWeatherData[i] = weatherData;
        }
        return arrayOfWeatherData;
    }
    
    private WeatherData[] getDataForNextDays(JSONObject obj)
    {
        JSONArray array = obj.getJSONArray("list");
        int arrayLength = array.length();
        WeatherData[] arrayOfWeatherData = new WeatherData[arrayLength];
        for (int i=0; i<arrayLength; i++)
        {
            obj = array.getJSONObject(i);
            JSONObject temp = getJSONObject(obj, "temp");
            WeatherData weatherData = new WeatherData("", "+"+(i+1), getDoubleFromAPIObject(temp, "day"), getDoubleFromAPIObject(temp, "min"), getDoubleFromAPIObject(temp, "max"), getDoubleFromAPIObject(obj, "humidity"), getDoubleFromAPIObject(obj, "pressure"), getDoubleFromAPIObject(obj, "speed"), getDoubleFromAPIObject(obj, "deg"), getDoubleFromAPIObject(obj, "rain"));
            arrayOfWeatherData[i] = weatherData;
        }
        return arrayOfWeatherData;
    }
    
    private double getDoubleFromAPIObject(JSONObject obj, String name)
    {
        if (obj.isNull(name))
            return 0;
        else
            return obj.getDouble(name);
    }
    
    private String getStringFromAPIObject(JSONObject obj, String name)
    {
        if (obj.isNull(name))
            return "";
        else
            return obj.getString(name);
    }
    
    private JSONObject getJSONObject(JSONObject obj, String name)
    {
        if (!obj.isNull(name))
            return obj.getJSONObject(name);
        else
            return new JSONObject();
    }
    
    private JSONObject readFromURL(URL url)
    {
        try
        {
            Scanner scan = new Scanner(url.openStream(), "UTF-8");
            String data = new String();
            while (scan.hasNext())
                data += scan.nextLine();
            scan.close();
            return new JSONObject(data);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}

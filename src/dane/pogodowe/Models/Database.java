/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dane.pogodowe.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
/**
 *
 * @author pc
 */
public class Database
{
    private static Connection conn = null;
    
    public Database()
    {
        try
        {
            if (this.conn==null)
            {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                this.conn = DriverManager.getConnection("jdbc:derby:baza;create=true;user=admin1;password=admin1"); 
            } 
        }
        catch (Exception ex)
        {
            Messages.showError("Błąd bazy danych. Szczegóły: "+ex.getMessage());
        }
    }
    
    public void shutdown()
    {
        try
        {
            if (this.conn != null)
                this.conn.close();
        }
        catch (Exception ex){}
    }
    
    public void insertData(WeatherData[] data, String option) throws DatabaseError
    {
        int optionId = getOptionId(option);
        int dataId = insertWeatherData(optionId, data.length);
        for (int i=0; i<data.length; i++)
            insertWeatherDataDetails(data[i], dataId);
    }
    
    public SavedDataModel[] getSavedDataInfo()
    {
        ArrayList<SavedDataModel> data = new ArrayList<SavedDataModel>();
        try
        {
            String query = "SELECT DATA.id, added, options FROM DATA JOIN WEATHER_OPTIONS ON DATA.OPTION_ID=WEATHER_OPTIONS.ID";
            Statement stmt = this.conn.createStatement();
            ResultSet results = stmt.executeQuery(query);
            ResultSetMetaData rsmd = results.getMetaData();
            while(results.next())
            {
                SavedDataModel row = new SavedDataModel(results.getInt(1), results.getString(3), results.getDate(2));
                data.add(row);
            }
            results.close();
            stmt.close();
        }
        catch (Exception ex)
        {
            Messages.showError("Błąd bazy danych. Szczegóły: "+ex.getMessage());
        }
        
        SavedDataModel[] array = new SavedDataModel[data.size()];
        array = data.toArray(array);
        
        return array;
    }
    
    public WeatherData[] getSavedData(int id)
    {
        ArrayList<WeatherData> data = new ArrayList<WeatherData>();
        try
        {
            String query = "SELECT TEMPERATURE, TEMPERATUREMIN, TEMPERATUREMAX, HUMIDITY, PRESSURE, SPEED, DIRECTION, RAIN, DATE FROM DATA_DETAILS WHERE DATA_ID=?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet results = stmt.executeQuery();
            ResultSetMetaData rsmd = results.getMetaData();
            while(results.next())
            {
                WeatherData row = new WeatherData("", results.getString(9), results.getDouble(1), results.getDouble(2), results.getDouble(3), results.getDouble(4), results.getDouble(5), results.getDouble(6), results.getDouble(7), results.getDouble(8));
                data.add(row);
            }
            results.close();
            stmt.close();
        }
        catch (Exception ex)
        {
            Messages.showError("Błąd bazy danych. Szczegóły: "+ex.getMessage());
        }
        
        WeatherData[] array = new WeatherData[data.size()];
        array = data.toArray(array);
        
        return array;
    }
    
    private int insertWeatherData(int optionId, int numberOfDays) throws DatabaseError
    {
        int id = 1;
        try
        {
            String query = "INSERT INTO DATA (\"ADDED\", \"OPTION_ID\", \"NUMBEROFDAYS\") VALUES (?,?,?)";
            PreparedStatement stmt = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setDate(1, new java.sql.Date(new Date().getTime()));
            stmt.setInt(2, optionId);
            stmt.setInt(3, numberOfDays);
            stmt.execute();
            ResultSet generatedKeys = stmt.getGeneratedKeys();

            if (generatedKeys.next())
                id = generatedKeys.getInt(1);
            stmt.close();
        }
        catch (Exception ex)
        {
            throw new DatabaseError();
        }
        return id;
    }
    
    private void insertWeatherDataDetails(WeatherData data, int dataId) throws DatabaseError
    {
        try
        {
            String query = "INSERT INTO DATA_DETAILS (\"DATA_ID\", \"DATE\", \"RAIN\", \"DIRECTION\", \"SPEED\", \"PRESSURE\", \"HUMIDITY\", \"TEMPERATURE\", \"TEMPERATUREMIN\", \"TEMPERATUREMAX\") VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, dataId);
            stmt.setString(2, data.getDateTimeText());
            stmt.setDouble(3, data.getFall());
            stmt.setDouble(4, data.getDeg());
            stmt.setDouble(5, data.getSpeed());
            stmt.setDouble(6, data.getPressure());
            stmt.setDouble(7, data.getHumidity());
            stmt.setDouble(8, data.getTemperature());
            stmt.setDouble(9, data.getTemperatureMin());
            stmt.setDouble(10, data.getTemperatureMax());
            stmt.execute();
            stmt.close();
        }
        catch (Exception ex)
        {
            throw new DatabaseError();
        }
    }
    
    private int getOptionId(String option) throws DatabaseError
    {
        int id = 0;
        try
        {
            String query = "SELECT id FROM WEATHER_OPTIONS WHERE OPTIONS=?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, option);
            ResultSet results = stmt.executeQuery();
            ResultSetMetaData rsmd = results.getMetaData();
            while(results.next())
            {
                id = results.getInt(1);
            }
            results.close();
            stmt.close();
        }
        catch (Exception ex)
        {
           throw new DatabaseError();
        }
        return id;
    }
}

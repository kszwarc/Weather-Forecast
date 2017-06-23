package dane.pogodowe.Models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class CityFromFile 
{
    public void writeCityToFile(String city)
    {
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("data")), "UTF8"));
            writer.write(city);
        }
        catch (Exception ex){}
        finally
        {
            try
            {
                if (writer!=null)
                   writer.close();
            }
            catch (Exception ex){}
        }
    }
    
    public String readCityFromFileOrIP()
    {
        String city = "";
        File file = new File("data");
        if (file.isFile() && file.canRead()) 
        {
            BufferedReader reader = null;
            try 
            {
                FileInputStream input = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(input, "UTF8"));
                city = reader.readLine();
            } 
            catch (Exception ex) {}
            finally
            {
                try
                {
                    if (reader!=null)
                        reader.close();
                }
                catch (Exception ex) {}
            }
        }
        else
            city = getCityFromIP();
        return city;
    }
        
    private String getCityFromIP()
    {
        try
        {
            URL url = new URL("http://ip-api.com/json");
            Scanner scan = new Scanner(url.openStream());
            String data = new String();
            while (scan.hasNext())
                data += scan.nextLine();
            scan.close();
            JSONObject obj = new JSONObject(data);
            return obj.getString("city");
        }
        catch (Exception ex)
        {
            return "";
        }
    }
    
}

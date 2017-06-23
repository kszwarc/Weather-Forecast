/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dane.pogodowe.Models;

/**
 *
 * @author pc
 */
public class InternetConnectionError extends Exception 
{
    @Override
    public String getMessage()
    {
        return "Nie udało się połączyć z API. Sprawdź połączenie internetowe.";
    }
}

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
public class DatabaseError extends Exception
{
    @Override
    public String getMessage()
    {
        return "Nie udało się zaimportować danych do bazy.";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dane.pogodowe.Models;

import java.sql.Date;

/**
 *
 * @author pc
 */
public class SavedDataModel 
{
    private final int id;
    private final String option;
    private final Date dateAdded;
        
        
    public SavedDataModel(int id, String option, Date dateAdded)
    {
        this.id = id;
        this.option = option;
        this.dateAdded = dateAdded;
    }
    
    public String getOption()
    {
        return this.option;
    }
    
    public Date getDateAdded()
    {
        return this.dateAdded;
    }
    
    public int getId()
    {
        return this.id;
    }
}

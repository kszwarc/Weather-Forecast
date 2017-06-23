/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dane.pogodowe.Models;

import javafx.scene.control.Alert;

/**
 *
 * @author pc
 */
public class Messages 
{
    public static void showError(String errorDetails)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Dane pogodowe");
        alert.setHeaderText("Błąd");
        alert.setContentText("Szczegóły błędu: "+errorDetails);
        alert.showAndWait();
    }
    
    public static void showSuccess(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dane pogodowe");
        alert.setHeaderText("Sukces");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

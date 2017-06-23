/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dane.pogodowe;

import dane.pogodowe.Models.Database;
import dane.pogodowe.Models.ExcelExport;
import dane.pogodowe.Models.SavedDataModel;
import dane.pogodowe.Models.WeatherData;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author pc
 */
public class DatabaseController implements Initializable
{
    @FXML
    private TableView<SavedDataModel> tableView;
    @FXML
    private TableColumn idColumn, dateColumn, optionColumn, exportColumn;
    
    private final Database database = new Database();
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        TableColumn[] columns = {idColumn, dateColumn, optionColumn, exportColumn};
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        for (int i=0; i<columns.length; i++)
            columns[i].setStyle( "-fx-alignment: CENTER;");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        optionColumn.setCellValueFactory(new PropertyValueFactory<>("option"));
        exportColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        Callback<TableColumn<SavedDataModel, String>, TableCell<SavedDataModel, String>> cellFactory = //
                new Callback<TableColumn<SavedDataModel, String>, TableCell<SavedDataModel, String>>()
                {
                    @Override
                    public TableCell call( final TableColumn<SavedDataModel, String> param )
                    {
                        final TableCell<SavedDataModel, String> cell = new TableCell<SavedDataModel, String>()
                        {
                            final Button btn = new Button( "Eksportuj" );
                            @Override
                            public void updateItem( String item, boolean empty )
                            {
                                super.updateItem( item, empty );
                                if ( empty )
                                {
                                    setGraphic( null );
                                    setText( null );
                                }
                                else
                                {
                                    btn.setOnAction( ( ActionEvent event ) ->
                                    {
                                        SavedDataModel model = getTableView().getItems().get( getIndex() );
                                        saveData(model.getId(), model.getOption());        
                                    } );
                                    setGraphic( btn );
                                    setText( null );
                                }
                            }

                            private void saveData(int id, String operation) 
                            {
                                try
                                {
                                    ExcelExport excel = new ExcelExport();
                                    Stage actualStage = (Stage)tableView.getScene().getWindow();
                                    File file = excel.getFileLocation(actualStage);
                                    if (file != null) 
                                    {
                                        Database database = new Database();
                                        WeatherData[] data = database.getSavedData(id);
                                        excel.write(file, operation, data);
                                    }
                                }
                                catch (Exception ex){}
                            }
                        };
                        return cell;
                    }
                };

        exportColumn.setCellFactory( cellFactory );
        SavedDataModel[] savedData = this.database.getSavedDataInfo();
        
        for (int i=0; i<savedData.length; i++)
            this.tableView.getItems().add(savedData[i]);
    }
}

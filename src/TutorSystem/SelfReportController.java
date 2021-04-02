/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorSystem;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hassan
 */
public class SelfReportController {
    @FXML private ToggleGroup confidenceGroup;
    @FXML private ToggleGroup effortGroup;
    @FXML private Button submitButton;
    
    private static User currentUser;
    private final static int subtractVal = -3;
  
    public SelfReportController(){}
     
    public void setCurrentuser(User user){
        currentUser = user;
    }
    
    private int getConfidenceChoice(){
        RadioButton choice = (RadioButton) confidenceGroup.getSelectedToggle();
        if (choice == null){ 
            return Math.abs(subtractVal);
        }
        return Integer.parseInt(choice.getText());
    }
    
    private int getEffortChoice(){
        RadioButton choice = (RadioButton) confidenceGroup.getSelectedToggle();
        if (choice == null){ 
            return Math.abs(subtractVal);
        }
        return Integer.parseInt(choice.getText());
    }
    
    private void updateUserConfidence() throws IOException, CsvException{
        int confNum = getConfidenceChoice();
        int updateVal = confNum + subtractVal;
        currentUser.addConfidence(updateVal);
    }
    
    private void updateUserEffort() throws IOException, CsvException{
        int effortNum = getEffortChoice();
        int updateVal = effortNum + subtractVal;
        currentUser.addEffort(updateVal);
    }
    
    @FXML
    public void closeSelfReportWindow(ActionEvent e) throws IOException, CsvException {
        updateUserConfidence();
        updateUserEffort();
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    } 
}

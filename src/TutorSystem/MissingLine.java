/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorSystem;

import com.opencsv.exceptions.CsvException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 *
 * @author Hassan
 */
public class MissingLine extends Question{
    
    @FXML private TextField inputText;
    
    @Override
    public void checkAnswer(){
        String ans = inputText.getText();
        if (ans.equals(correctAnswer)){
            try {
                //            System.out.println("Bogchamp");
                answerCorrect();
            } catch (FileNotFoundException ex) {
                System.out.println("error in missing line");
            } catch (IOException | CsvException ex) {
                Logger.getLogger(MissingLine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            incorrectAnswer();
        
        }
        
    }
    
}

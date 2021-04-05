/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorSystem;

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
//            System.out.println("Bogchamp");
            answerCorrect();
        }
        else {
        
        }
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorSystem;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

/**
 *
 * @author Hassan
 */
public class MultipleChoice extends Question{
//    @FXML private Label question;
//    @FXML private Label questionCode;
    
    @FXML private RadioButton option1;
    @FXML private RadioButton option2;
    @FXML private RadioButton option3;
    @FXML private RadioButton option4;
    @FXML private ToggleGroup toggleGroup;

//    @FXML private Button checkAnswerBtn;
    
//    private String correctAnswer;
    
    
    public MultipleChoice(){

    }
    
    public void getQuestion() throws CsvValidationException{
        try {
            URL url = getClass().getResource("questions.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] row;
            reader.readNext(); // Skips heading rows
            while ((row = reader.readNext()) != null){
                System.out.println(row);
//                if (row[0].equals(username) && row[1].equals(password)){
//                    System.out.println("Pog?");
//                }
            }
                    
        }catch(IOException | NullPointerException | URISyntaxException e){
            System.out.println("Something went wrong");
            e.printStackTrace();            
        }
    }
    
    @FXML
    public void checkAnswer(){
        RadioButton selectedAnswer = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedAnswer == null){ 
            return;
        }
        String answer = selectedAnswer.getText();
        if (answer.equals(correctAnswer)){
            selectedAnswer.setTextFill(Color.rgb(104, 209, 6));
            try {
                //            question.setText("WOWO");
                answerCorrect();
            } catch (FileNotFoundException ex) {
                System.out.println("error in mcq");
            } catch (IOException | CsvException ex) {
                Logger.getLogger(MultipleChoice.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            selectedAnswer.setDisable(true);
            selectedAnswer.setTextFill(Color.RED);
            incorrectAnswer();

        }
    }
    
    public void setOption1Text(String text){
        option1.setText(text);
    }
    
    public void setOption2Text(String text){
        option2.setText(text);
    }
    
    public void setOption3Text(String text){
        option3.setText(text);
    }
    
    public void setOption4Text(String text){
        option4.setText(text);
    }
    
    
    
}

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package TutorSystem;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
//
///**
// *
// * @author Hassan
// */
public abstract class Question {
    @FXML protected Label question;
    @FXML protected Label questionCode;
//    @FXML protected VBox container;
    
    @FXML protected Button checkAnswerBtn;    
    protected String correctAnswer;
    protected QuestionPage questionPage;
    protected String hint1;
    protected String hint2;
    protected String hint3;
    
//    public Question(QuestionPage qp){
//        questionPage = qp;        
//    }
    
    public abstract void checkAnswer();
    
    public void setQuestion(String questionText){
        question.setText(questionText);
    }
    
    public void setCode(String codeText){
        questionCode.setText(codeText);                
    }
    
    public void setCorrectAnswer(String ans){
        correctAnswer = ans;
    }
    
    public void setQuestionPage(QuestionPage qp){
        questionPage = qp;
    }
    
    public void setHints(String hint1, String hint2, String hint3){
        this.hint1 = hint1;
        this.hint2 = hint2;
        this.hint3 = hint3;
    }
    
    public void answerCorrect(){
        questionPage.answeredCorrectly();
    }

}

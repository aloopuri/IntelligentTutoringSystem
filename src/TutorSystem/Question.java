///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
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
import java.util.Timer;
import java.util.TimerTask;
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
    @FXML protected Button checkAnswerBtn; 
    
    protected String difficulty;
    protected QuestionPage questionPage;
    protected String correctAnswer;
    protected String hint1;
    protected String hint2;
    protected String hint3;    
    protected int curHintNum;
    protected int numOfSubmitAns;
    private String hintsMessage;
    private boolean didAction;
    
    private boolean answered;
//    private boolean isHintAbuse;
    
    public Question(){
        hintsMessage = "";
        curHintNum = 1;    
        numOfSubmitAns = 0;
        didAction = false;
        answered = false;
//        checkPeriodically();
    }
    
    public abstract void checkAnswer();
    
    public void setDifficulty(String difficulty){
        this.difficulty = difficulty;
    }
    
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
    
//    private void checkPeriodically(){
//        Timer timer = new Timer();    // 1000 = 1 second
//        timer.schedule(
//            new TimerTask() {
//                int tick = 0;
//                    
//            @Override
//            public void run() {
//                System.out.println("pong");
//                if (didAction){
//                    System.out.println("ENDEDDD");
//                    timer.cancel();
//                }
//                
//            }
//        }, 0, 2000);
//    }
//    
//    public void didAction(){
//        didAction = true;
//    }
//    
    public String getDifficulty(){
        return difficulty;
    }
    
    public void setHints(String hint1, String hint2, String hint3){
        this.hint1 = hint1;
        this.hint2 = hint2;
        this.hint3 = hint3;
    }
    
    public String getCurrentHintsGiven(){
        return hintsMessage;
    }
    
    public String getHintMessage(){
        hintsMessage = hintsMessage + getNextHint() + "\n";
        return hintsMessage;
    }
    
    public String getNextHint(){
        String hint = getHint(curHintNum);
        if (!isValidHint(hint)){
//            String oldHint = getHint(curHintNum -1);
            return "";
        }
        if (curHintNum <= 3){
            curHintNum++;
        }
        return "Hint" + (curHintNum-1) + ":\n" + hint;
    }
    
    private boolean isValidHint(String hint){
        if (hint.isBlank()){ return false;}
        return true;
    }
    
    private String getHint(int hintNum){
        switch (hintNum) {
            case 1:
                return hint1;
            case 2:
                return hint2;
            case 3:
                return hint3;
            default:
                break;
        }
        return "";
    }
    
    public boolean isAllHintsUsed(){
        if (getHint(curHintNum).equals("")){
            return true;
        }
        return false;
    }
    
    public void answerCorrect() throws FileNotFoundException, IOException, CsvException{
        checkAnswerBtn.setDisable(true);
        answered = true;
        questionPage.answeredCorrectly();
    }
    
    public void incorrectAnswer(){
        questionPage.incorrectAnswer();
    } 
    
    public boolean questionAnswered(){
        return answered;
    }
   
    

}

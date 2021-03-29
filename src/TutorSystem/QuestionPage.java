/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorSystem;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Hassan
 */
public class QuestionPage {
    
    enum QuestionType{
        MCQ, MISSING_LINE, CODING;
    }
    
    private final static int DIFFICULTY_INDEX = 0;
    private final static int QUESTION_TYPE_INDEX = 1;
    private final static int QUESTION_INDEX = 2;
    private final static int CODE_INDEX = 3;
    private final static int CORRECT_ANS_INDEX = 4;
    private final static int INCORRECT_ANS_INDEX = 5;
    private final static int HINT1_INDEX = 6;
    private final static int HINT2_INDEX = 7;
    private final static int HINT3_INDEX = 8;
    
    
    
    @FXML private Label header;
    
    @FXML private BorderPane borderPane;   
    @FXML private VBox agentInterface;
    @FXML private ImageView agentImg;
    @FXML private TextArea agentTextArea;
    
    @FXML private Button hideAgentBtn;    
    @FXML private Button returnHomeBtn;
    @FXML private Button syntaxBtn;
    @FXML private Button hintBtn;
    @FXML private Button skipBtn;
    
    private Question question;

    
    public QuestionPage(){
//        question = new MultipleChoice();
    }
    
    @FXML 
    public void loadQuestion() throws IOException, CsvValidationException{
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("MultipleChoice.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("MissingLine.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CodingQuestion.fxml"));
        Parent pane = loader.load();
        Question controller = loader.getController();
        createQuestion(controller);
         
       
//        Parent mcq = FXMLLoader.load(getClass().getResource("MultipleChoice.fxml"));
        borderPane.setCenter(pane);        

    }
    
    public void createQuestion(Question controller) throws CsvValidationException{
        try {
            URL url = getClass().getResource("questions.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] row;
            reader.readNext(); // Skips heading rows
            while ((row = reader.readNext()) != null){
//                switch (row[QUESTION_TYPE_INDEX]){
//                    case "MCQ":
//                        MultipleChoice c = (MultipleChoice) controller;
//                        setupMCQ(row, c);
//                        break;
//                    case "MISSING_LINE":
//                        MissingLine m = (MissingLine) controller;
//                        setupMissingLineQuestion(row, m);
//                        System.out.println("jsdslkjdjdsjdkldkjkjsLKJSNDKSLN");
//                        break;                
//                }
                if (row[QUESTION_TYPE_INDEX].equals("MCQ")){
                    continue;
//                    MultipleChoice mc = (MultipleChoice) controller;
//                    setupMCQ(row, mc);
                }
                else if (row[QUESTION_TYPE_INDEX].equals("MISSING_LINE")){
                    continue;
//                    MissingLine ml = (MissingLine) controller;
//                    setupMissingLineQuestion(row, ml);
                }
                else if (row[QUESTION_TYPE_INDEX].equals("CODING")){
                    CodingQuestion cq = (CodingQuestion) controller;
                    setupCodingQuestion(row, cq);
                }
//                controller.setQuestion(row[QUESTION_INDEX]);
//                controller.setCode(row[CODE_INDEX]);
//                controller.setCorrectAnswer(row[CORRECT_ANS_INDEX]);
//                String s = row[INCORRECT_ANS_INDEX];
//                String [] temp = s.split("\\|");                
//                List<String> answers = new ArrayList(Arrays.asList(temp));
//                answers.add(row[CORRECT_ANS_INDEX]);
////                for(String p: answers){
////                    System.out.println(p);  
////                }
//                Collections.shuffle(answers);
//                controller.setOption1Text(answers.get(0));
//                controller.setOption2Text(answers.get(1));
//                controller.setOption3Text(answers.get(2));
//                controller.setOption4Text(answers.get(3));
                 
//                System.out.println("TutorSystem.QuestionPage.getQuestion()");
//                for(String p: answers){
//                    System.out.println(p);  
//                }
                
//                System.out.println(Arrays.toString(row));
            }
                    
        }catch(IOException | NullPointerException | URISyntaxException e){
            e.printStackTrace();
            System.out.println("oh no");
        }
    }
    
    private void setupMCQ(String [] data, MultipleChoice controller){
//        controller.setQuestion(data[QUESTION_INDEX]);
//        controller.setCode(data[CODE_INDEX]);
//        controller.setCorrectAnswer(data[CORRECT_ANS_INDEX]);
        setTitle_Code_Answer(data, controller);
        String s = data[INCORRECT_ANS_INDEX];
        String [] temp = s.split("\\|");                
        List<String> answers = new ArrayList(Arrays.asList(temp));
        answers.add(data[CORRECT_ANS_INDEX]);
    //                for(String p: answers){
    //                    System.out.println(p);  
    //                }
        Collections.shuffle(answers);
        controller.setOption1Text(answers.get(0));
        controller.setOption2Text(answers.get(1));
        controller.setOption3Text(answers.get(2));
        controller.setOption4Text(answers.get(3));
    }
    
    private void setupMissingLineQuestion(String [] data, MissingLine controller){
        System.out.println("XDDDDDDDDDDDDDDDDDDDDDD");
        setTitle_Code_Answer(data, controller);
                    
    }
    
    private void setupCodingQuestion(String [] data, CodingQuestion controller){
        controller.setQuestion(data[QUESTION_INDEX]);      
//        setTitle_Code_Answer(data, controller);
        System.out.println(data[CORRECT_ANS_INDEX]);
        controller.getAnswer(data[CORRECT_ANS_INDEX]);
        System.out.println("CODE DATA: "+data[CODE_INDEX]);
        controller.setVariables(data[CODE_INDEX]);
        controller.setCorrectAnswer(data[CORRECT_ANS_INDEX]);        

    }
    
    private void setTitle_Code_Answer(String [] data, Question controller){
        controller.setQuestion(data[QUESTION_INDEX]);
        controller.setCode(data[CODE_INDEX]);
        controller.setCorrectAnswer(data[CORRECT_ANS_INDEX]);        
    }
    
    public void makeQuestion(){
    
    }
    
    public void checkAnswer(){
        
    }
    
    public void openHomePage(ActionEvent e) throws IOException{
        Parent homepageParent = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Scene homepageScene = new Scene(homepageParent);
        
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(homepageScene);
        window.show();
    }
        
    @FXML
    public void toggleAgentSidebar(ActionEvent e){
//        agentInterface.isv
        if (agentInterface.isVisible()){
            agentInterface.setVisible(false);
            agentInterface.setManaged(false);
            hideAgentBtn.setText("Show Agent");
            return;
        }
        agentInterface.setVisible(true);
        agentInterface.setManaged(true);
        hideAgentBtn.setText("Hide Agent");
        
    }
}

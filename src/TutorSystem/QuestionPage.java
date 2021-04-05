/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorSystem;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Hassan
 */
public class QuestionPage {
    
//    enum QuestionType{
//        MCQ, MISSING_LINE, CODING;
//    }
    
    private final static int DIFFICULTY_INDEX = 0;
    private final static int QUESTION_TYPE_INDEX = 1;
    private final static int QUESTION_TITLE_INDEX = 2;
    private final static int CODE_INDEX = 3;
    private final static int CORRECT_ANS_INDEX = 4;
    private final static int INCORRECT_ANS_INDEX = 5;
    private final static int HINT1_INDEX = 6;
    private final static int HINT2_INDEX = 7;
    private final static int HINT3_INDEX = 8;
    
    
    
    @FXML private Label header;
    
    @FXML private StackPane stackPane;
    @FXML private BorderPane borderPane;   
    @FXML private VBox agentRegion;
    @FXML private ImageView agentImg;
    @FXML private ImageView clapGif;
    @FXML private TextArea agentTextArea;
    
    @FXML private Button hideAgentBtn;    
    @FXML private Button returnHomeBtn;
    @FXML private Button syntaxBtn;
    @FXML private Button hintBtn;
    @FXML private Button skipBtn;
    
    private List<String []> allQuestions;
    private static User currentUser;
    private static Question currentQuestion; // or make it String []
    private Agent agent;
    
    private List<Timer> timers;
    
    private boolean pressed = false;
    private boolean showingHint;
    private boolean hintUsed;
    
    private long startTime;
    
//    private Question question;

    
    public QuestionPage(){
//        question = new MultipleChoice();
        allQuestions = new ArrayList();
        timers = new ArrayList();
        try {
            getQuestionData();
        } catch (CsvValidationException ex) {
            System.out.println("Couldn't retrieve files");
        }
        agent = new Agent(this, currentUser);
//        backgroundTimer(5000);
//        checkPeriodically();
        showingHint = false;
        hintUsed = false;
//        clapGif.setVisible(false);
//        clapGif.setManaged(false);
    }
    
    /**
     * Runs code inside run every 'time' milliseconds e.g. 1000 = 1 second
     * @param time 
     */
    private void backgroundTimer(int time){
        
        Timer timer = new Timer();    // 1000 = 1 second
        timer.schedule(
            new TimerTask() {
                int tick = 0;
                    
            @Override
            public void run() {
                System.out.println("ping");
                if (tick  == 1){
                    System.out.println("cancelled");
                    
                    timer.cancel();
                }  
                tick++;
            }
        }, 0, time);
        
    }
    
    private void checkPeriodically(){
        Timer timer = new Timer();    // 1000 = 1 second
        timer.schedule(
            new TimerTask() {
                int tick = 0;
                    
            @Override
            public void run() {
                System.out.println("pong");
                if (pressed){
                    System.out.println("ENDEDDD");
                    timer.cancel();
                }
                
            }
        }, 0, 2000);
    }
    
    private boolean isHintAbuse(){
        long curTime = System.currentTimeMillis();
        
        if (curTime - startTime  <= 6000) {
            startTime = curTime;    // Stops them from waiting only 6 seconds
            return true;
        }
        return false;
    }
    
    private void checkHintAvoidance(){
        String diff = currentQuestion.getDifficulty();
        switch (diff) {
            case "EASY":
                System.out.println("QUESTION AMARK?");
                hintAvoidanceTimer(5000);
                break;
            case "MEDIUM":
                hintAvoidanceTimer(40000);
                break;
            case "HARD":
                hintAvoidanceTimer(55000);
                break;
            default:
                break;
        }
    }
    
    private void hintAvoidanceTimer(int time){
        Timer timer = new Timer();    // 1000 = 1 second
        timers.add(timer);
        timer.schedule(
            new TimerTask() {
                int tick = 0;
                
            @Override
            public void run() {
                
                if (tick  == 1){
                    System.out.println("runned");
                    dealWithHintAvoid(agent.giveHelp());                    
                    timer.cancel();
                    timers.remove(timer);
                }  
                tick++;
            }
        }, 0, time);
    }
    
    private void dealWithHintAvoid(boolean help){
        if (help){
            String msg = "You appear to be struggling. Remember, don't be "
                + "afraid to ask for help if you need it.";
            showMessageWithHint(msg);
//            agentTextArea.setText("You appear to be struggling, Don't be afraid"
//                + " to ask for help if you need it\n" + currentQuestion.getHintMessage());
            showingHint = true;
        }
        else {
            suggestHint();
        }
    }
    
    public void stopAllTimers(){
        System.out.println(timers.size() + " SIZEEEEEEEEEEEEEEEEEEEEEEE");
        for (Timer timer : timers){
            timer.cancel();            
        }
        timers.clear();
        System.out.println("BURRRRRRRRRRRRRRRRRRRRRRRRRRRRh");
        
    }
    
    @FXML
    public void btnPressed(){        
        pressed = true;
        hideSuggestHint();
    }
    
    public void setUser(User user){
        currentUser = user;
        agent.setAgent(user);
        
    }
    
    private void suggestHint(){
        stackPane.getChildren().get(1).setVisible(true);
    }
    
    public void hideSuggestHint(){
        System.out.println(stackPane.getChildren().size());
        stackPane.getChildren().get(1).setVisible(false);
        showAgentMessage("Don't be afraid to ask for hints if you're struggling");
//        agentTextArea.setText("Don't be afraid to ask for hints if you're struggling");
    }
    
    @FXML
    public void giveHint(){
        if (currentQuestion == null){ return;}
        if (stackPane.getChildren().get(1).isVisible()){ 
            stackPane.getChildren().get(1).setVisible(false);
        }
        if (isHintAbuse()){
            agentTextArea.setText("Instantly using hints won't help inprove your java skills. "
                    + "You should try attempting the question first.");
            return;
        }
        String hint = currentQuestion.getCurrentHintsGiven();
        if (hint.equals("") && !showingHint){
            currentQuestion.getCurrentHintsGiven();
        }
        currentQuestion.didAction();       
        
        hint = currentQuestion.getHintMessage();
        if (hint.equals("")) { return;}
//        String oldText = agentTextArea.getText() + "\n";
        agentTextArea.setText(hint);
        showingHint = true;
        hintUsed = true;
    }
    
    public void showAgentMessage(String text){
        agentTextArea.setText(text);
        showingHint = false;
    }
    
    public void showMessageWithHint(String message){
        agentTextArea.setText(message + "\n" + currentQuestion.getHintMessage());
        showingHint = true;
        hintUsed = true;
        
    }
    
    private void resetAllVariables(){
        stopAllTimers();
        hintUsed = false;
        showingHint = false;
    }
    
    private void getQuestionData() throws CsvValidationException{
        try {
            URL url = getClass().getResource("questions.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] row;
            reader.readNext(); // Skips heading rows
            while ((row = reader.readNext()) != null){
                allQuestions.add(row);
            }
            
        } catch (IOException | URISyntaxException ex) {
//            Logger.getLogger(QuestionPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void getRandomQuestion() throws IOException, CsvValidationException{
        resetAllVariables();
        Random rand = new Random();
        int index = rand.nextInt(allQuestions.size());
//        for (String []x : allQuestions){
//            System.out.println(x[DIFFICULTY_INDEX]);
//        }
//        System.out.println("SIZEEEEEEEEEEEE: "+ allQuestions.size());
//        System.out.println("INDEXXXXXXXXX: "+index);
        String [] questionParam = allQuestions.get(index);
//        System.out.println(allQuestions.size());
//        System.out.println(data[QUESTION_TYPE_INDEX]);
//        String type = getQuestionClassType(data[QUESTION_TYPE_INDEX]);
//        System.out.println(type);
        loadQuestion(questionParam);
        startTime = System.currentTimeMillis();
        checkHintAvoidance();
        agentTextArea.setText("Try this question");
    }
    
    private String getQuestionClassType(String type){
        switch (type) {
            case "MCQ":
                return "MultipleChoice";
            case "MISSING_LINE":
                return "MissingLine";
            case "CODING":
                return "CodingQuestion";
            default:
                break;
        }
        return null;
    }
    
    public void loadQuestion(String [] questionParam) throws IOException, CsvValidationException{
        
        String type = getQuestionClassType(questionParam[QUESTION_TYPE_INDEX]);
        System.out.println("TYPE: "+ type +", "+questionParam[QUESTION_TYPE_INDEX]);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(type + ".fxml"));
        Parent pane = loader.load();
        Question controller = loader.getController();
        createQuestion(controller, questionParam);
        currentQuestion = controller;
        borderPane.setCenter(pane);
    }
    
    @FXML 
    public void loadQuestion() throws IOException, CsvValidationException{
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("MultipleChoice.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("MissingLine.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CodingQuestion.fxml"));
        Parent pane = loader.load();
        Question controller = loader.getController();
//        createQuestion(controller);
         
       
//        Parent mcq = FXMLLoader.load(getClass().getResource("MultipleChoice.fxml"));
        borderPane.setCenter(pane);        

    }
    
    public void createQuestion(Question controller, String [] data){
        switch (data[QUESTION_TYPE_INDEX]) {
            case "MCQ":
                System.out.println("1:    HERRRRRRRREEEEEEEEEE");
                MultipleChoice mc = (MultipleChoice) controller;
                setupMCQ(data, mc);
                break;
            case "MISSING_LINE":
                System.out.println("2: LINEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                MissingLine ml = (MissingLine) controller;
                setupMissingLineQuestion(data, ml);
                break;
            case "CODING":
                System.out.println("3: CODIIIIIIIIIIIIIING");
                CodingQuestion cq = (CodingQuestion) controller;
                setupCodingQuestion(data, cq);
                break;
            default:
                break;
        }
    }
    
    public void answeredCorrectly() throws FileNotFoundException{
        stopAllTimers();
        agent.answeredCorrectly(hintUsed);
//        agentTextArea.setText("Great Job!");
    }
    
    public void changeAgentImage() throws FileNotFoundException{
        Image img = new Image(getClass().getResource("images/coolAgent.png").toString());
        
//        agentImg.setImage(new Image(new FileInputStream("images/coolAgent.png")));
        agentImg.setImage(img);
        toggleClap();
    }
    
    private void setupMCQ(String [] data, MultipleChoice controller){
        setQuestionParams(data, controller);
        String s = data[INCORRECT_ANS_INDEX];
        String [] temp = s.split("\\|");                
        List<String> answers = new ArrayList(Arrays.asList(temp));
        answers.add(data[CORRECT_ANS_INDEX]);
        controller.setHints(data[HINT1_INDEX], data[HINT2_INDEX], data[HINT3_INDEX]);
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
        setQuestionParams(data, controller);
                    
    }
    
    private void setupCodingQuestion(String [] data, CodingQuestion controller){
        controller.setDifficulty(data[DIFFICULTY_INDEX]);
        controller.setQuestion(data[QUESTION_TITLE_INDEX]);      
//        setTitle_Code_Answer(data, controller);
        System.out.println(data[CORRECT_ANS_INDEX]);
        controller.getAnswer(data[CORRECT_ANS_INDEX]);
        System.out.println("CODE DATA: "+data[CODE_INDEX]);
        controller.setVariables(data[CODE_INDEX]);
        controller.setCorrectAnswer(data[CORRECT_ANS_INDEX]);  
        controller.setHints(data[HINT1_INDEX], data[HINT2_INDEX], data[HINT3_INDEX]);
        controller.setQuestionPage(this);

    }
    
    private void setQuestionParams(String [] data, Question controller){
        controller.setDifficulty(data[DIFFICULTY_INDEX]);
        controller.setQuestion(data[QUESTION_TITLE_INDEX]);
        controller.setCode(data[CODE_INDEX]);
        controller.setCorrectAnswer(data[CORRECT_ANS_INDEX]);
        controller.setHints(data[HINT1_INDEX], data[HINT2_INDEX], data[HINT3_INDEX]);
        controller.setQuestionPage(this);
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
        if (agentRegion.isVisible()){
            agentRegion.setVisible(false);
            agentRegion.setManaged(false);
            hideAgentBtn.setText("Show Agent");
            return;
        }
        agentRegion.setVisible(true);
        agentRegion.setManaged(true);
        hideAgentBtn.setText("Hide Agent");
        
    }
    
    public void toggleClap(){
        if (clapGif.isVisible()){
            clapGif.setVisible(false);
            clapGif.setManaged(false);
            return;
        }
        clapGif.setVisible(true);
        clapGif.setManaged(true);
    }
     /*
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
                // star if (row[QUESTION_TYPE_INDEX].equals("MCQ")){
                    MultipleChoice mc = (MultipleChoice) controller;
                    setupMCQ(row, mc);
                }
                else if (row[QUESTION_TYPE_INDEX].equals("MISSING_LINE")){
                    MissingLine ml = (MissingLine) controller;
                    setupMissingLineQuestion(row, ml);
                }
                        else // star/ if (row[QUESTION_TYPE_INDEX].equals("CODING")){
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
    } */
}

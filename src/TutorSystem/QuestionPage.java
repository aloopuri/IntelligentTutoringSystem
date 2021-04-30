/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorSystem;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Hassan
 */
public class QuestionPage {
    
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
    private static Question currentQuestion;
    private Agent agent;
    private boolean pressed = false;
    private boolean showingHint;
    private boolean hintUsed;
    private boolean stopMakeTimers;
    private List<Timer> timers;
    private long startTime;
    private long hintAbuseTimer;
    private String questionDiff;
    
    public QuestionPage(){
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
        startTime = System.currentTimeMillis();
        showingHint = false;
        hintUsed = false;
        stopMakeTimers = false;
        questionDiff = "EASY";
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
        if (currentQuestion.isAllHintsUsed()) {
            agentTextArea.setText("No more hints left. \n" + currentQuestion.getHintMessage());
        }
        long curTime = System.currentTimeMillis();
        
        if (curTime - hintAbuseTimer  <= 6000) {
            hintAbuseTimer = curTime;    // Stops them from waiting only 6 seconds
            return true;
        }
        return false;
    }
    
    private void checkHintAvoidance(){
        if (stopMakeTimers) { return;}
        String diff = currentQuestion.getDifficulty();
        switch (diff) {
            case "EASY":
                System.out.println("QUESTION AMARK?");
                hintAvoidanceTimer(35000);
                break;
            case "MEDIUM":
                hintAvoidanceTimer(45000);
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
                    agent.addConfidence(-1);
                    agent.addEffort(1);
                    timer.cancel();
                    timers.remove(timer);
                }  
                tick++;
            }
        }, 0, time);
    }
    
    private void dealWithHintAvoid(boolean help){
        if (currentQuestion.isAllHintsUsed()) { 
            String msg = "Remember, use the hints to your advantage";
//            System.out.println(msg);
            stopMakeTimers = true;
            showMessageWithHint(msg);
            
            return;
        }
        if (help){
            String msg = "You appear to be struggling. Remember, don't be "
                + "afraid to ask for help if you need it.";
            showMessageWithHint(msg);

            showingHint = true;
        }
        else {
            suggestHint();
        }
    }
    
    public void stopAllTimers(){
        for (Timer timer : timers){
            timer.cancel();            
        }
        timers.clear();
        
    }
    
    @FXML
    public void showSyntax(){  
        stackPane.getChildren().get(3).setVisible(true);
        borderPane.setDisable(true);
        agent.addIndependence(-1);
    }
    
    public void setUser(User user){
        currentUser = user;
        agent.setAgent(user);
        
    }
    
    private void suggestHint(){
        agent.addIndependence(-1);
        stackPane.getChildren().get(1).setVisible(true);
        borderPane.setDisable(true);
    }
    
    public void hideSuggestHint(){
        stackPane.getChildren().get(1).setVisible(false);
        showAgentMessage("Don't be afraid to ask for hints if you're struggling");
        borderPane.setDisable(false);
        checkHintAvoidance();
    }
    
    @FXML
    public void giveHint(){
        if (currentQuestion == null){ return;}
        if (stackPane.getChildren().get(1).isVisible()){ // checks if the help box is showing
//            stackPane.getChildren().get(1).setVisible(false);
//            borderPane.setDisable(false);
            hideAllPopupMessages();
        }
        if (currentQuestion.isAllHintsUsed()){
            agentTextArea.setText("No more hints left. You can solve this!\n" 
                    + currentQuestion.getCurrentHintsGiven());
            return;
        }
        
        if (isHintAbuse() && !currentQuestion.isAllHintsUsed()){
            String txt = "So let's try solving this problem now";
            agentTextArea.setText("Instantly using hints won't help inprove"
                + " your java skills. You should try solving the question"
                + " and only ask for help when stuck");
            String dir = getClass().getResource("images/helpAbuse.png").toString();
            showingHint = false;
            agent.addEffort(-1);
            timedAgentImage(dir, 5000, txt);
            stopAllTimers();
            checkHintAvoidance();  
            hintAbuseTimer = System.currentTimeMillis();
            return;
        }
        
//        if (currentUser.getIndependence()>11){
//            // give hint if really need it
//        }
        
        String hint = currentQuestion.getCurrentHintsGiven();
        if (hint.equals("") && !showingHint){
            currentQuestion.getCurrentHintsGiven();
        }
//        currentQuestion.didAction();       
        
        hint = currentQuestion.getHintMessage();
        if (hint.equals("")) { return;}
//        String oldText = agentTextArea.getText() + "\n";
        agentTextArea.setText(hint);
        stopAllTimers();
        checkHintAvoidance();
        showingHint = true;
        hintUsed = true;
        hintAbuseTimer = System.currentTimeMillis();
        agent.addIndependence(-1);
    }
    
    public void showAgentMessage(String text){
        agentTextArea.setText(text);
        showingHint = false;
    }
    
    /**
     * Displays a message in the Agent text area followed by 
     * hints
     * @param message 
     */
    public void showMessageWithHint(String message){
        agentTextArea.setText(message + "\n" + currentQuestion.getHintMessage());
        String dir = getClass().getResource("images/think.png").toString();
        timedAgentImage(dir, 5000, agentTextArea.getText());
//        changeAgentImage("think");
        checkHintAvoidance();
        showingHint = true;
        hintUsed = true;        
    }
    
    private void resetAllVariables(){
        stopAllTimers();
        agent.resetVars();
        setDefaultAgent();
        hintUsed = false;
        showingHint = false;
        stopMakeTimers = false;
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
    
    /**
     * Gets a new question of a certain difficulty based on the 
     * user's confidence level
     * @throws IOException
     * @throws CsvValidationException 
     */
    @FXML
    public void getNewQuestion() throws IOException, CsvValidationException{
        Pane center = (Pane) borderPane.getCenter();
        if (center instanceof VBox){
            // Initially choose a question difficulty based on avg of 
            // user variables
            int avg = (currentUser.getConfidence() +currentUser.getEffort()
                    + currentUser.getIndependence())/3;
            if (avg < 7){ questionDiff = "EASY";}
            else if (avg >=7 && avg <=14) { questionDiff = "MEDIUM";}
            else if (avg > 14){ questionDiff = "HARD";}
        }
        else if (currentQuestion.questionAnswered()){}
        else if (System.currentTimeMillis() -  startTime > 15000) {
            System.out.println(System.currentTimeMillis() -  startTime);
            agent.givenUp();
            showGiveUpPane();
            return;
        }
//        String diff = currentQuestion.getDifficulty();
        if (agent.getStreak() >= 2){ //increase difficulty  
            agent.resetStreak();
            switch (questionDiff) {
                case "EASY":
                    questionDiff = "MEDIUM";
                    break;
                case "MEDIUM":
                    questionDiff = "HARD";
                    break;
//                case "HARD":
//                    break;
                default:
                    break;
            }
        }
        else if (agent.getStreak() <= -2){ //decrease difficulty
            agent.resetStreak();
            switch (questionDiff) {
//                case "EASY":
//                    break;
                case "MEDIUM":
                    questionDiff = "EASY";
                    break;
                case "HARD":
                    questionDiff = "MEDIUM";
                    break;
                default:
                    break;
            }
        }
        System.out.println("STREAKKKKKKKKKKkk, "+ agent.getStreak());
        resetAllVariables();
//        int conf = currentUser.getConfidence();
//        String difficulty = "";
//        if (conf < 7){
//            difficulty = "EASY";
//        }
//        else if (conf >=7 && conf <=14) {
//            difficulty = "MEDIUM";           
//        }
//        else if (conf > 14){
//            difficulty = "HARD";
//        }
        List<String []> questions = new ArrayList<>();
        for (String [] question : allQuestions){
            if (question[DIFFICULTY_INDEX].equals(questionDiff)){
                questions.add(question);
            }
        }
        Random rand = new Random();
        int index = rand.nextInt(questions.size());
        String [] questionParam = questions.get(index);
        loadQuestion(questionParam);
        startTime = System.currentTimeMillis();
        hintAbuseTimer = startTime;
        checkHintAvoidance();
        agentTextArea.setText("Try this question"); 
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
        hintAbuseTimer = startTime;
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
//        System.out.println("TYPE: "+ type +", "+questionParam[QUESTION_TYPE_INDEX]);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(type + ".fxml"));
        Parent pane = loader.load();
        Question controller = loader.getController();
        createQuestion(controller, questionParam);
        currentQuestion = controller;
        borderPane.setCenter(pane);
    }
    
//    @FXML 
//    public void loadQuestion() throws IOException, CsvValidationException{
////        FXMLLoader loader = new FXMLLoader(getClass().getResource("MultipleChoice.fxml"));
////        FXMLLoader loader = new FXMLLoader(getClass().getResource("MissingLine.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("CodingQuestion.fxml"));
//        Parent pane = loader.load();
//        Question controller = loader.getController();
////        createQuestion(controller);       
////        Parent mcq = FXMLLoader.load(getClass().getResource("MultipleChoice.fxml"));
//        borderPane.setCenter(pane);        
//
//    }
    
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
    
    public void answeredCorrectly() throws FileNotFoundException, IOException, CsvException{
        stopAllTimers();
        String difficulty = currentQuestion.getDifficulty();
        agent.answeredCorrectly(hintUsed, difficulty);
        agent.updateUserVariables();
    }
    
    public void incorrectAnswer(){
        stopAllTimers();
        checkHintAvoidance();
        agent.increaseAttempts();
        agent.incorrect(currentQuestion.getDifficulty());
    }    
    
    /**
     * Changes the agent image
     * @param image "default", "coolAgent", "helpAbuse"
     * @throws FileNotFoundException 
     */
    public void changeAgentImage(String image) throws FileNotFoundException{
//        Image img = new Image(getClass().getResource("images/coolAgent.png").toString()); 
        Image img = new Image(getClass().getResource("images/"+image+".png").toString()); 
        agentImg.setImage(img);
    }
    
    private void timedAgentImage(String directory, int time, String afterMsg){
        Image img = new Image(directory);
        agentImg.setImage(img);
        Timer timer = new Timer();    // 1000 = 1 second
        timer.schedule(
            new TimerTask() {
                int tick = 0;
                    
            @Override
            public void run() {
                System.out.println("starto");
//                    try {
//                        changeAgentImage("helpAbuse");
//                    } catch (FileNotFoundException ex) {
//                        Logger.getLogger(QuestionPage.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                if (tick  == 1){
                    System.out.println("cancelled");
                    try {
                        changeAgentImage("default");
                        agentTextArea.setText(afterMsg);
                        stopAllTimers();
                        checkHintAvoidance();
                    } catch (FileNotFoundException ex) {
                        
                    }
                    
                    timer.cancel();
                }  
                tick++;
            }
        }, 0, time);
        
    }
    
    private void setDefaultAgent(){
        Image img = new Image(getClass().getResource("images/default.png").toString());        
        agentImg.setImage(img);
        if (clapGif.isVisible()) {
            toggleClap();
        }
    }
    
    private void setupMCQ(String [] data, MultipleChoice controller){
        setQuestionParams(data, controller);
        String s = data[INCORRECT_ANS_INDEX];
        String [] temp = s.split("\\|");                
        List<String> answers = new ArrayList(Arrays.asList(temp));
        answers.add(data[CORRECT_ANS_INDEX]);
        controller.setHints(data[HINT1_INDEX], data[HINT2_INDEX], data[HINT3_INDEX]);
        Collections.shuffle(answers);
        controller.setOption1Text(answers.get(0));
        controller.setOption2Text(answers.get(1));
        controller.setOption3Text(answers.get(2));
        controller.setOption4Text(answers.get(3));
    }
    
    private void setupMissingLineQuestion(String [] data, MissingLine controller){
        setQuestionParams(data, controller);
                    
    }
    
    private void setupCodingQuestion(String [] data, CodingQuestion controller){
        controller.setDifficulty(data[DIFFICULTY_INDEX]);
        controller.setQuestion(data[QUESTION_TITLE_INDEX]);      
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
        stopAllTimers();
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
    
    public void notGivenUp(){
        hideAllPopupMessages();
        agent.notGivenUp();
    }
    
    public void hideAllPopupMessages(){
        for (Node n : stackPane.getChildren()){
            if (n.equals(borderPane)){ continue;}
            BorderPane p = (BorderPane) n;
            p.setVisible(false);
        }
//        for (int i = 1; i<=2; i++ ){
//        stackPane.getChildren().get(i).setVisible(false);        
//        } 
        borderPane.setDisable(false);           
    }
    
    public void showGiveUpPane(){
        stackPane.getChildren().get(2).setVisible(true);
        borderPane.setDisable(true);
    }
    
    public void givenUp() throws IOException, CsvException{
        agent.addConfidence(-2);
        agent.addEffort(-2);
        agent.addIndependence(-2);
        agent.updateUserVariables();
        startTime = System.currentTimeMillis(); 
        hideAllPopupMessages();
        getNewQuestion();
    }
    
    @FXML
    public void openSelfReport(ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SelfReport.fxml"));
        Parent root = loader.load();
        SelfReportController controller = (SelfReportController) loader.getController();
        controller.setCurrentuser(currentUser);;
        Stage stage = new Stage();    
        stage.setTitle("Self-Report");
        stage.setScene(new Scene(root, 600, 450));
        stage.show();
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

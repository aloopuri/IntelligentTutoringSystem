/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorSystem;

import bsh.EvalError;
import bsh.Interpreter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Hassan
 */
public class Homepage extends Application {
    
//    private ArrayList<Pane> panels;
    @FXML private Pane currentPanel;    
    @FXML private Label welcomeLabel;
    @FXML private Button statementsBtn;
    @FXML private Button cdtnStatementBtn;
    @FXML private Button loopsBtn;  
    @FXML private Button questionBtn;
    @FXML private VBox agentInterface;
//    private LearningPage learnPage;
    
    private static User currentUser;
    
    @Override
    public void start(Stage stage) throws Exception {
        URL url = getClass().getResource("LoginMenu.fxml");
        
        Parent root = FXMLLoader.load(url); 
        
//        test.test();
        
//        TextFlow t = new TextFlow();
//        learnPage = new LearningPage();
        
        Scene scene = new Scene(root, 1024, 786);
        stage.setScene(scene);
        stage.show();
    }
    
    public Homepage(){ 
        checkCSVExist();
    }

    
    public static void main(String[] args) {
        launch(args);

    }
    
    /*@FXML
    public void testInterpreter() throws URISyntaxException, FileNotFoundException, IOException, CsvValidationException{
        Interpreter i = new Interpreter();
        i.setStrictJava(true);
        
//        i.redirectOutputToFile("src/TutorSystem/file.txt");
//        i.run();
        try {
            URL url = getClass().getResource("questions.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] row;
            reader.readNext(); // Skips heading rows
            int j = 0;
            String s = "";
            while (((row = reader.readNext()) != null)&&(j<3)){
                j++;
                s = row[3];
            }
            i.eval(new FileReader(new File("src/TutorSystem/file.txt")) );
//            i.error(s);
//            i.eval(s);
            i.eval(" String x, y;"+ 
                    "x = \"abc\";"+ 
                    "y = \"10\";" + 
                    "System.out.println(x+y);"+
                    ");");
//            System.out.println(s);
//            System.out.println(StringEscapeUtils.escapeJava(s));
            i.eval(s);
            System.out.println("x: " + i.get("x"));
            System.out.println("y: " + i.get("y"));
//            System.out.println(i.getOut());
        } catch (EvalError e) {
            System.out.println("WOWOWOWOWOWO");
            System.out.println(e.getErrorText());
//            System.out.println(e.getErrorLineNumber());
//            Throwable t = e.getCause();
//            System.out.println("ERROR SAYS: " + t);
//            i.redirectOutputToFile("file.txt");
//        java bsh.Console;
        
        }
    }*/
    
//    @FXML
//    public void loadLearningPage(ActionEvent e){
//        if (e.getSource() == statementsBtn){
//            System.out.println("xdd");
////            currentPanel = learnPage.getPane();
//            
//        }        
//        else if (e.getSource() == cdtnStatementBtn){
//            System.out.println("cdtn");
//        }
//        else if (e.getSource() == loopsBtn){
//            System.out.println("loops");
//        }
//    }
    
    @FXML
    public void hideAgentSidebar(ActionEvent e){
        agentInterface.setVisible(false);
        agentInterface.setManaged(false);
    }
    
//    @FXML
//    public void openLearnPage(ActionEvent e) throws IOException{
//        Parent learnPageParent = FXMLLoader.load(getClass().getResource("LearnPage.fxml"));
//        Scene learnPageScene = new Scene(learnPageParent);
//        
//        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
//        window.setScene(learnPageScene);
//        window.show();
//    } 
    @FXML
    public void openLearnPage(ActionEvent e) throws IOException{
        Scene scene = loadPage("LearnPage");
        changePage(e, scene);
    } 
    
    @FXML
    public void openQuestionPage(ActionEvent e) throws IOException{
        Scene scene = loadPage("QuestionPage");
        changePage(e, scene);
    }
    
    @FXML
    public void logout(ActionEvent e) throws IOException{
        currentUser = null;
        Scene scene = loadPage("LoginMenu");
        changePage(e, scene);
    }
    
    public Scene loadPage(String pageName) throws IOException{
        Parent pageParent = FXMLLoader.load(getClass().getResource(pageName + ".fxml"));
        Scene pageScene = new Scene(pageParent);
        
//        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
//        window.setScene(pageScene);
//        window.show();
        return pageScene;
    }
    
    public void changePage(ActionEvent e, Scene pageScene){
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(pageScene);
        window.show();
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
    
    private void checkLogin(){
        return;
    }
    
    public void setCurrentUser(User user){
        currentUser = user;
    }
    
    public void setWelcomeLabel(String text){
        welcomeLabel.setText(text);
    }
    
    @FXML
    public void printUser(){
        System.out.println(currentUser.getUsername());
    }
    
    
    
    /*
     * This checks to see if there is a CSV which contains user data
    */
    private void checkCSVExist(){
        File f = new File("src/TutorSystem/file.txt");
        if (f.exists() && !f.isDirectory()){
//            System.out.println("DRU");
        }
        else{
//            System.out.println("NOT TRU");
        }
    }
    
    private void createCSVFile(){
        
    }
    
    @FXML
    private void readCSV(ActionEvent e) throws CsvValidationException, URISyntaxException{
        loadCSV();
        /*try {
            URL url = getClass().getResource("questions.csv");
//            FileReader filereader = new FileReader(getClass().getResource("questions.csv"));

//            CSVReader reader = new CSVReader(filereader);
            System.out.println("NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            CSVReader reader = new CSVReader(new FileReader(new File("src/TutorSystem/File.txt")));
            System.out.println("PASSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            String [] row;
//            reader.readNext(); // Skips heading rows
            while ((row = reader.readNext()) != null){
                for (String cell : row) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
//                System.out.println(Arrays.toString(row));
//                if (row[0].equals(username) && row[1].equals(password)){
//                    System.out.println("Pog?");
//                }
            }
                    
        }catch(IOException | NullPointerException err   ){
            System.out.println("Something went wrong");
            err.printStackTrace();            
        }*/
    }
    
    
    public void loadCSV() throws CsvValidationException, URISyntaxException{
        try {
            URL url = getClass().getResource("questions.csv");
            System.out.println("NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
//            URL url = getClass().getResource("questions.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));

            
//            CSVReader reader = new CSVReader(new FileReader(new File("src/TutorSystem/questions.csv")));
            System.out.println("PASSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            String [] row;
            while ((row = reader.readNext()) != null){
                System.out.println(row[0]);
                System.out.println(row[1]);
//                System.out.pri    ntln(Arrays.toString(row));
//                for (String cell : row) {
//                    System.out.print(cell + "\t");
//                }
//                System.out.println();

            }
                    
        }catch(IOException | NullPointerException err   ){
            System.out.println("Something went wrong");
            err.printStackTrace();            
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorSystem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author Hassan
 */
public class LearningPage {
    
    @FXML private Pane pane;
    @FXML private Label header;
    @FXML private Button homeBtn;
    @FXML private WebView webView;
    private WebEngine engine;
    
    // Basic lessons buttons
    @FXML private Button introL; 
    @FXML private Button basicSyntaxL;
    @FXML private Button identifiersL;
    @FXML private Button dataTypesL;
    @FXML private Button variablesL;
    @FXML private Button commentsL; 
    @FXML private Button operatorsL;
    
    // Flow control lesson buttons
    @FXML private Button decisionMakingL;
    @FXML private Button loopsL;
    @FXML private Button forLoopEgL;
    @FXML private Button forEachLoopL;
    @FXML private Button whileLoopEgL;
    @FXML private Button switchL;
    @FXML private Button continueL;
    @FXML private Button breakL;
    
    private Lesson lessons;    
        
    public LearningPage(){     
        lessons = new Lesson();
    }
    
    @FXML
    public void showLesson(ActionEvent e){
        engine = webView.getEngine();
        updateLabel(e);
        String url = getLesson(e);
        engine.load(url);
    }

    public String getLesson(ActionEvent e){
        if (e.getSource() == introL){
            return lessons.getIntro();
        }
        else if (e.getSource() == basicSyntaxL){
            return lessons.getBasicSyntax();
        }
        else if (e.getSource() == identifiersL){
            return lessons.getIdentifiers();
        }
        else if (e.getSource() == dataTypesL){
            return lessons.getDataTypes();
        }
        else if (e.getSource() == variablesL){
            return lessons.getVariables();
        }
        else if (e.getSource() == commentsL){
            return lessons.getComments();
        }
        else if (e.getSource() == operatorsL){
            return lessons.getOperators();
        }
        else if (e.getSource() == decisionMakingL){
            return lessons.getDecisionMaking();
        }
        else if (e.getSource() == loopsL){
            return lessons.getLoops();
        }
        else if (e.getSource() == forLoopEgL){
            return lessons.getForLoopEg();
        }
        else if (e.getSource() == forEachLoopL){
            return lessons.getForEachLoop();
        }
        else if (e.getSource() == whileLoopEgL){
            return lessons.getWhileLoopEg();
        }
        else if (e.getSource() == switchL){
            return lessons.getSwitchStatement();
        }
        else if (e.getSource() == continueL){
            return lessons.getContinueStatement();
        }
        else if (e.getSource() == breakL){
            return lessons.getBreakStatement();
        }
        
        return "https://www.geeksforgeeks.org/java/";        
    }
    
    public void updateLabel(ActionEvent e){
        Button sourceBtn = (Button) e.getSource();  
        header.setText(sourceBtn.getText());
    }
        
    @FXML
    public void openHomePage(ActionEvent e) throws IOException{
        Parent homepageParent = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Scene homepageScene = new Scene(homepageParent);
        
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(homepageScene);
        window.show();
    }    
    
}

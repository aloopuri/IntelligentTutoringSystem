/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorSystem;

import com.opencsv.exceptions.CsvException;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Math.round;
import javafx.scene.Node;

/**
 *
 * @author Hassan
 */
public class Agent {
//    private static Node parentNode;
    private final static int VAR_BOUNDARY = 11;
    private static QuestionPage questionPage;
    private static User currentUser;
    
    private int confidenceToAdd;
    private int effortToAdd;
    private int independenceToAdd;
    private int attempts;
    private int streak;
    
    
//    private int confidence;
//    private int effort;
//    private int independence;
    
    public Agent(QuestionPage qp, User user){
        questionPage = qp;
        currentUser= user;
        attempts = 0;    
        confidenceToAdd = 0;
        effortToAdd = 0;
        independenceToAdd = 0;
        streak = 0;
    }
    
    public void setAgent(User user){
        currentUser = user;
    }
    
    /**
     * Adds a value to the confidenceToAdd variable, which is limited to 
     * the range (-2,2)
     * @param num 
     */
    public void addConfidence(int num){
        if (confidenceToAdd>=3 || confidenceToAdd <= -3){ return;}
        confidenceToAdd += num;
    }
    
    public void addEffort(int num){
        if (effortToAdd >=3 || effortToAdd <= -3){ return;}
        effortToAdd += num;
    }
    
    public void addIndependence(int num){
        if (independenceToAdd >=3 || independenceToAdd <= -3){ return;}
        independenceToAdd += num;
    }
    
    public void addToAllVars(int num){
        addConfidence(num);
        addEffort(num);
        addIndependence(num);
    }    
    
    public void updateUserVariables() throws IOException, CsvException{
        currentUser.addConfidence(confidenceToAdd);
        currentUser.addEffort(effortToAdd);
        currentUser.addIndependence(independenceToAdd);
//        currentUser.updateConfidence(confidenceToAdd);
//        currentUser.updateEffort(effortToAdd);
//        currentUser.updateIndependence(independenceToAdd);
    }
    
    public void resetVars(){
        confidenceToAdd = 0;
        effortToAdd = 0;
        independenceToAdd = 0;
        attempts = 0;
    }
    
//    private void increaseStreak(){
//        streak++;
//    }
//    
//    private void decreaseStreak(){
//        streak--;
//    }
//    
    public void resetStreak(){
        streak = 0;
    }
    
    public int getStreak(){
        return streak;
    }
    
    public void increaseAttempts(){
        attempts++;
    }
    
    private void addToVarsBasedOnDifficulty(boolean hintUsed, String difficulty){
        switch (difficulty) {
            case "EASY":
                if (!hintUsed){
                    addToAllVars(1);
                }
                else {
                    addToAllVars(0);
                }
                
                break;
            case "MEDIUM":
                if (!hintUsed){
                    addToAllVars(2);
                }
                else {
                    addToAllVars(1);
                }
                break;
            case "HARD":
                if (!hintUsed){
                    addToAllVars(3);
                }
                else {
                    addToAllVars(2);
                }
                
                break;
            default:
                break;
        }
    }
    
    public void answeredCorrectly(boolean hintUsed, String difficulty) throws FileNotFoundException{
//        increaseStreak();
        if (streak <=0){ streak = 1;}
        else{ streak++;}
        String msg = "";
        if (!hintUsed){  // check if hints were used
            msg = "Excellent work! You did it without any help!";
            questionPage.showAgentMessage(msg);
            questionPage.changeAgentImage("coolAgent");
            questionPage.toggleClap();
            addToVarsBasedOnDifficulty(hintUsed, difficulty);
            System.out.println(msg);
            return;
        }
        int confidence = currentUser.getConfidence();
        int effort = currentUser.getEffort();
        int independence = currentUser.getIndependence();
        if (streak >=2) {
            if (confidence <= VAR_BOUNDARY && effort <= VAR_BOUNDARY){ // reduce independence
                msg = "Great job! You solved it. How about we try a harder"
                        + " problem next?";                          
            }
            else if (confidence > VAR_BOUNDARY && effort <= VAR_BOUNDARY){
                msg = "Nice Work! You're did it. How about we try a harder "
                        + "problem next?";
            }
            else if (confidence <= VAR_BOUNDARY && effort > VAR_BOUNDARY){
                msg = "Nice work! All that effort paid out in the end. "
                        + "Let's try another question";
            }
            else if (confidence > VAR_BOUNDARY && effort > VAR_BOUNDARY){
                msg = "Great job! You did it! You seem to know your Java well!.";
            }
        }
        else{
            msg = "Nice! Keep up the good work";
        }
        
//        if (confidence <= VAR_BOUNDARY && effort <= VAR_BOUNDARY){ // reduce independence
//            msg = "Great job! You solved it. How about we try a slightly harder"
//                    + " problem next?";              
//            // harder prob preferably, comment promotion 
//            //"Good job! You did that nicely. How about we try a slightly harder problem next?"            
//        }
//        else if (confidence > VAR_BOUNDARY && effort <= VAR_BOUNDARY){
//            msg = "Nice Work! You're did it. How about we try a harder "
//                    + "problem next?";
//            // suggest challenge, much harder
//            // "Nice work! You're doing pretty good. Let's try doing a harder problem next"
//        }
//        else if (confidence <= VAR_BOUNDARY && effort > VAR_BOUNDARY){
//            msg = "Nice work! All that effort paid out in the end. "
//                    + "Let's try another question";
//            // link effort to success, similar level
//            // "Nice work! All that effort paid out in the end. Let's try another question"
//        }
//        else if (confidence > VAR_BOUNDARY && effort > VAR_BOUNDARY){
//            msg = "Great job! You did it! You seem to know your Java well!.";
//            // performance feedback, harder
//            // "Great job! You solved that problem well"
//        }
        addToVarsBasedOnDifficulty(hintUsed, difficulty);
        questionPage.changeAgentImage("cheer");
        questionPage.showAgentMessage(msg);    
    }
    
    
    private void incorrectVarUpdate(String difficulty){
        switch (difficulty) {
            case "EASY":
                if (attempts>=1){
                    addToAllVars(-1);
                }                
                break;
            case "MEDIUM":
                if (attempts >= 2){
                    addToAllVars(-1);
                }
                break;
            case "HARD":
                if (attempts >=3){
                    addToAllVars(-1);
                }              
                break;
            default:
                break;
        }
    
    }
    
    public void incorrect(String difficulty){
        streak = 0;
        int confidence = currentUser.getConfidence();
        int effort = currentUser.getEffort();
        int independence = currentUser.getIndependence();
        String msg = "Incorrect.\n";
        if (confidence < VAR_BOUNDARY && effort < VAR_BOUNDARY){ // reduce independence
            // hint, insist same problem
            msg += "Don't worry about it. You can do it if you keep trying!"
                    + " I'll give some help";
            questionPage.showMessageWithHint(msg);
        }
        else if (confidence > VAR_BOUNDARY && effort < VAR_BOUNDARY){
            // same problem, 
            msg += "You can do it! Keep trying to solve it!";
            questionPage.showAgentMessage(msg);
            
//            questionPage.showMessageWithHint(msg);
        }
        else if (confidence < VAR_BOUNDARY && effort > VAR_BOUNDARY){
            // praise effort, dont give up
            msg += "You have been working hard and put a lot of effort. "
                + "You can do it! I'll give you a little help";
            questionPage.showMessageWithHint(msg);
        }
        else if (confidence > VAR_BOUNDARY && effort > VAR_BOUNDARY){ // promote indpendence
           msg += "You've been doing great so far and have shown a lot of "
                   + "effort. You can do this!";
           questionPage.showAgentMessage(msg);
//           questionPage.showMessageWithHint(mgs);
            // performance feedback, praise, keep tryin
        }
        incorrectVarUpdate(difficulty);
        
    }
    
    public void givenUp(){
//        decreaseStreak();
        if (streak >=1){ streak = -1;}
        else{ streak--;}
        int confidence = currentUser.getConfidence();
        int effort = currentUser.getEffort();
        int independence = currentUser.getIndependence();
        addConfidence(-1);
        String msg = "You really want to give up?";
        if (confidence < VAR_BOUNDARY && effort < VAR_BOUNDARY){ 
            // hint, insist same problem            
            msg = "You have overcome other questions like this before."
                + " I know you can do it! If you still believe you can't, then "
                + "believe in the me that believes in you. Never give up!";
        }
        else if (confidence > VAR_BOUNDARY && effort < VAR_BOUNDARY){
            // same problem, 
            msg = "It doesn't seem like you're putting much effort."
                    + " I know you got it in you, just try a bit more";
            
        }
        else if (confidence < VAR_BOUNDARY && effort > VAR_BOUNDARY){
            // praise effort, dont give up
            msg = "You've been putting in a lot of effort. It will pay off"
                + " in the end. Don't give up!";
        }
        else if (confidence > VAR_BOUNDARY && effort > VAR_BOUNDARY){ 
            msg = "I know you can do it! Just keep at it like you already are"
                + " and you'll be able to do this easy-peasy lemon "
                + "squeezy";
        }
        questionPage.showAgentMessage(msg);
    }
    
    public void notGivenUp(){
        int confidence = currentUser.getConfidence();
        int effort = currentUser.getEffort();
        int independence = currentUser.getIndependence();
        addEffort(1);
        
        String msg = "Yes keep going!\n";
        if (confidence < VAR_BOUNDARY && effort < VAR_BOUNDARY){ 
            // hint, insist same problem            
            msg += "Use the hint to solve the problem";
            questionPage.showMessageWithHint(msg);
        }
        else if (confidence > VAR_BOUNDARY && effort < VAR_BOUNDARY){
            // same problem, 
            msg += "Put the effort in and you can do this!. Ask for a hint"
                    + " if you need to.";  
            questionPage.showAgentMessage(msg);
//            questionPage.showMessageWithHint(msg);
        }
        else if (confidence < VAR_BOUNDARY && effort > VAR_BOUNDARY){
            // praise effort, dont give up
            msg += "You can do this!";
            questionPage.showMessageWithHint(msg);
        }
        else if (confidence > VAR_BOUNDARY && effort > VAR_BOUNDARY){ 
            msg += "Lets solve and finish this!";
            questionPage.showAgentMessage(msg);
        }
    }
    
    public boolean giveHelp(){
        int confidence = currentUser.getConfidence();
        int independence = currentUser.getIndependence();
        if (confidence >= VAR_BOUNDARY ){
            return false;
            // skip help and suggest keep tryin
        }
        return true;
    }
    
    public void hintUsed(){}
    
    public void helpRejected(){
        int conf = currentUser.getConfidence();
        if (giveHelp()) {
            
        }
    }
    
}

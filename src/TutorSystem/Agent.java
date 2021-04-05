/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorSystem;

import java.io.FileNotFoundException;
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
    
    private int attempts;
    
    
//    private int confidence;
//    private int effort;
//    private int independence;
    
    public Agent(QuestionPage qp, User user){
        questionPage = qp;
        currentUser= user;
        attempts = 0;    
    }
    
    public void setAgent(User user){
        currentUser = user;
    }
    
    public void answeredCorrectly(boolean hintUsed) throws FileNotFoundException{
        String msg = "";
        System.out.println("DISSSSSSSSSSSSSSSSSSSSSHEREEEEE");
        if (!hintUsed){  // check if hints were used
            msg = "Excellent work! You did it without any help!";
            questionPage.showAgentMessage(msg);
            questionPage.changeAgentImage();
            System.out.println(msg);
            return;
        }
        int confidence = currentUser.getConfidence();
        int effort = currentUser.getEffort();
        int independence = currentUser.getIndependence();
        if (confidence <= VAR_BOUNDARY && effort <= VAR_BOUNDARY){ // reduce independence
            msg = "Great job! You solved it. How about we try a slightly harder"
                    + " problem next?";            
            
            // harder prob preferably, comment promotion 
            //"Good job! You did that nicely. How about we try a slightly harder problem next?"            
        }
        else if (confidence > VAR_BOUNDARY && effort <= VAR_BOUNDARY){
            msg = "Nice Work! You're did it. How about we try a harder "
                    + "problem next?";
            
            // suggest challenge, much harder
            // "Nice work! You're doing pretty good. Let's try doing a harder problem next"
        }
        else if (confidence <= VAR_BOUNDARY && effort > VAR_BOUNDARY){
            msg = "Nice work! All that effort paid out in the end. "
                    + "Let's try another question";
            
            // link effort to success, similar level
            // "Nice work! All that effort paid out in the end. Let's try another question"
        }
        else if (confidence > VAR_BOUNDARY && effort > VAR_BOUNDARY){ // promote indpendence
            msg = "Great job! You did it! You seem to know your Java.";
            
            
            // performance feedback, harder
            // "Great job! You solved that problem well"
        }
        System.out.println(msg);
    
    }
    
    public void incorrect(){
        int confidence = currentUser.getConfidence();
        int effort = currentUser.getEffort();
        int independence = currentUser.getIndependence();
        if (confidence < VAR_BOUNDARY && effort < VAR_BOUNDARY){ // reduce independence
            // hint, insist same problem
            String msg = "Don't worry about it. You can do it if you keep trying!";
            questionPage.showMessageWithHint(msg);
        }
        else if (confidence > VAR_BOUNDARY && effort < VAR_BOUNDARY){
            // same problem, 
            String msg = "You can do it! Keep trying to solve it!";
            questionPage.showAgentMessage(msg);
            
//            questionPage.showMessageWithHint(msg);
        }
        else if (confidence < VAR_BOUNDARY && effort > VAR_BOUNDARY){
            // praise effort, dont give up
            String msg = "You have been working hard on learning Java, don't feel disheartened."
                    + " You can do it!";
            questionPage.showMessageWithHint(msg);
        }
        else if (confidence > VAR_BOUNDARY && effort > VAR_BOUNDARY){ // promote indpendence
           String mgs = "You're doing great. This is perhaps a little mistake";
           questionPage.showAgentMessage(mgs);
//           questionPage.showMessageWithHint(mgs);
            // performance feedback, praise, keep tryin
        }
        
    }
    
    public void givenUp(){
    
    }
    
    public boolean giveHelp(){
        int confidence = currentUser.getConfidence();
        int independence = currentUser.getIndependence();
        if (confidence > VAR_BOUNDARY ){
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
    
    private void updateUserVars(int num){}
    

    
    
    
    
    
    
}

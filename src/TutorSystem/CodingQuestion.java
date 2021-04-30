/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorSystem;

import bsh.EvalError;
import bsh.Interpreter;
import com.opencsv.exceptions.CsvException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 *
 * @author Hassan
 */
public class CodingQuestion extends Question{
    @FXML private TextArea codingArea;
    
    private List<String> variables;
    private Interpreter correctAnsInterpreter;
    private int chances;
    
    public CodingQuestion(){
        correctAnsInterpreter = new Interpreter();
        correctAnsInterpreter.setStrictJava(true);
        variables = new ArrayList();
        chances = 2;
//        i.set
        
    }

    @Override
    public void checkAnswer() {
        String userCode = codingArea.getText();
        Interpreter inputInterpreter = new Interpreter();
        inputInterpreter.setStrictJava(true);
        if (userCode.isBlank()){
            System.out.println("NOTHING ENTERED");
            return;
        }
        try {
            
            correctAnsInterpreter.eval(correctAnswer);
            inputInterpreter.eval(userCode);
            boolean correct = compareAnswer(correctAnsInterpreter, inputInterpreter);
            System.out.println(correct);
            if (correct){
                answerCorrect();
//                System.out.println("YOU GOT IT RIGHT BOGG");
            }
            else {
                chances--;
                if (chances == 0){
                    incorrectAnswer();
//                    System.out.println("YOU FAILED");
                    // do the hint stuff and then perhaps reset chances
                }
            }
                    
                    
                    
        } catch (EvalError ex) {
            System.out.println("Syntax Error");
//            Logger.getLogger(CodingQuestion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException n){
            System.out.println("Variable not found");
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException | CsvException ex) {
            Logger.getLogger(CodingQuestion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
    public void getAnswer(String code){
        try {
            Interpreter i = new Interpreter();
            i.setStrictJava(true);
//            System.out.println(StringEscapeUtils.escapeJava(code));
            String c = code.replace("\n", "");
//            System.out.println(StringEscapeUtils.escapeJava(c));
//            String mainLauncher = "\nString [] mainMethodArg = new String[0];\nmain(mainMethodArg)";
//            String codeRunner = code + mainLauncher;
            
            
            i.eval(code);
            System.out.println(i.get("z"));
        } catch (EvalError e) {
            System.out.println("Error in code");
        }
        
    }
    
    public void setVariables(String vars){
        System.out.println("HERE????????????: "+vars);
        String [] temp = vars.split("\\|"); 
        variables.addAll(Arrays.asList(temp));
        for(String s : variables){
            System.out.println(s);
        }
    }
    
    public boolean compareAnswer(Interpreter correctAns, Interpreter inputAns) throws EvalError, NullPointerException{
        System.out.println("SIZW: "+variables.size());
        String errVar = "?";
        try {
            for (String v : variables){
                errVar = v;
//                System.out.println(correctAns.get(v) +", "+ correctAns.get(v).getClass());
                inputAns.get(v).getClass(); // Used to check if any variable is missing from answer
            }
        }
        catch (NullPointerException n) {
            System.out.println("Variable \"" + errVar + "\" not found");
        }
//        for (String v : variables){
//            System.out.println(correctAns.get(v) +", "+ correctAns.get(v).getClass());
//            inputAns.get(v).getClass();
//            inputAns.get(v);
//        }
        
//        for (String v : variables){
//            inputAns.get(v);
////            System.out.println("VAR: " + v);
////            System.out.println(correctAns.get(v) +", "+ correctAns.get(v).getClass());
////            System.out.println(inputAns.get(v) +", "+ inputAns.get(v).getClass());
////            if (!correctAns.get(v).equals(inputAns.get(v))){
////                System.out.println("CORECT: "+correctAns.get(v));
////                System.out.println("INPUT: "+inputAns.get(v));
//////                return false;
////            }
//        }
        for (String v : variables){
            if (!correctAns.get(v).equals(inputAns.get(v))){
                System.out.println("CORECT: "+correctAns.get(v));
                System.out.println("INPUT: "+inputAns.get(v));
                return false;
            }
        }                
        return true;
    }
    
    
    
}

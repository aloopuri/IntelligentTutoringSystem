/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorSystem;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hassan
 */
public class User {
    private final static int USERNAME_INDEX = 0;
    private final static int PASSWORD_INDEX = 1;
    private final static int CONFIDENCE_INDEX = 2;
    private final static int EFFORT_INDEX = 3;
    private final static int INDEPENDENCE_INDEX = 4;
    
    private String username, password;   
    private int confidence;
    private int effort;
    private int independence;

    
    public User(String username, String password){
        this.username = username;
        this.password = password;
        try {
            loadMotivationVars();
        } catch (CsvValidationException ex) {
            confidence = -1;
            effort = -1;
            independence = -1;
        }
    }
    
    public String getUsername(){
        return username;
    }
    
    public int getConfidence(){
        return confidence;
    }
    
    public int getEffort(){
        return effort;
    }
    
    public int getIndependence(){
        return independence;
    }
    
    public void loadMotivationVars() throws CsvValidationException{
        try {
            URL url = getClass().getResource("users.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] row;
            reader.readNext(); // Skips heading rows
            while ((row = reader.readNext()) != null){
                if (row[USERNAME_INDEX].equals(username)){
                    System.out.println("DIS WORK?");
                    setMotivationVars(row);
                    return;
                }
            }
                    
        }catch(IOException | NullPointerException | URISyntaxException ex){
            System.out.println("Something went wrong");
        }
    }
    
    private void setMotivationVars(String[] user){
        confidence = Integer.parseInt(user[CONFIDENCE_INDEX]);
        effort = Integer.parseInt(user[EFFORT_INDEX]);
        independence = Integer.parseInt(user[INDEPENDENCE_INDEX]);
                
    }
    
    public void addConfidence(int num) throws IOException, CsvException{
        int newVal = confidence + num;
        int bound = withinBound(newVal);
        updateConfidence(bound);
    }
    
    public void addEffort(int num) throws IOException, CsvException{
        int newVal= effort + num;
        int bound = withinBound(newVal);
        updateEffort(bound);
    }
    
    private int withinBound(int num){
        int n = num;
        if ((n == 10) || (n == 1)){ return num;}
        else if (n > 10){n = 10;}
        else if (n < 1){n = 1;}
        return n;
    }
    
    private void updateConfidence(int newVal) throws IOException, CsvException{        
        updateInformation(CONFIDENCE_INDEX, newVal);    
        confidence = newVal;
    }
    
    private void updateEffort(int newVal) throws IOException, CsvException{
        updateInformation(EFFORT_INDEX, newVal);
        effort = newVal;
    }
    
    public void updateIndependence(int newVal) throws IOException, CsvException{
        updateInformation(INDEPENDENCE_INDEX, newVal);
        independence = newVal;
    }
    
    private void updateInformation(int updateVarInd, int newVal) throws IOException, CsvException{
        try {
            File file = new File("src/TutorSystem/users.csv");
            CSVReader reader = new CSVReader(new FileReader(file));
            List<String[]> allUsers = reader.readAll();
            int cnt = 0;
            for (String[] user : allUsers){
                if (user[USERNAME_INDEX].equals(username)){
                    System.out.println("HERE");
                    break;
                }
                cnt++;
            }
//            System.out.println("VAL: "+allUsers.get(cnt)[2]);
            allUsers.get(cnt)[updateVarInd] = Integer.toString(newVal);
            reader.close();
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            writer.writeAll(allUsers);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

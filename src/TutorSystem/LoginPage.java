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
import javafx.fxml.FXML;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;


/**
 *
 * @author Hassan
 */
public class LoginPage /*extends Application*/{
//   @FXML private BorderPane root;
    @FXML private Label header;
    @FXML private Label errorMessage;
    @FXML private TextField usernameBox;
    @FXML private PasswordField passwordBox;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    
    private final static int USERNAME_INDEX = 0;
    private final static int PASSWORD_INDEX = 1;
    
    private User currentUser;
    
    
    
    /*
    YOU DONT NEED START TO LOAD JUST LOOK AT MAPPANEL FOR HOW TO LOAD
    */
//   @Override
//   public void start(Stage stage) throws IOException {
//       
//       URL url = getClass().getResource("LoginMenu.fxml");
////       FXMLLoader loader = new FXMLLoader(getClass().getResource("/TutorSystem/LoginMenu.fxml"));
//       Parent root = FXMLLoader.load(url);
////       Group root = new Group(new Label("Hello JavaFX!"));
//       Scene scene = new Scene(root, 1024, 786);
//       stage.setScene(scene);
//       stage.show();
//   }
    public LoginPage(){
    
    }
    
//    public static void main(String[] args) {
//        Application.launch(args);
//
//    }
    
    
    @FXML
    public void checkLogin(ActionEvent e) throws CsvValidationException, CsvException{
        String username = usernameBox.getText();
        String password = passwordBox.getText();
        if (username.isBlank() || password.isBlank()){
            errorMessage.setText("One or more fields are empty");
            return;
        }
//        Pattern pat = Pattern.compile("[,/\\\"\']");
//        Matcher match = pat.matcher(password);
//        boolean b = match.find();
        if (isInvalidString(username) || isInvalidString(password)){
            errorMessage.setText("Invalid username or password");
            return;
        }

//        System.out.println(b);
        login(username, password, e);        
    }
    
    private boolean isInvalidString(String str){
        Pattern pat = Pattern.compile("[,/\\\"\']");
        Matcher match = pat.matcher(str);
        boolean b = match.find();
        return b;
    }
        
    public void login(String username, String password, ActionEvent e) throws CsvValidationException, CsvException{
        try {
            URL url = getClass().getResource("users.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] row;
            reader.readNext(); // Skips heading rows
            while ((row = reader.readNext()) != null){
                if (row[USERNAME_INDEX].equals(username) && row[PASSWORD_INDEX].equals(password)){
                    System.out.println("Pog?");
                    errorMessage.setText("LOGGED IN");
//                    updateUser(username);
                    currentUser = new User(username,password);
                    openHomepage(e);
                    return;
                }
            }
                    
        }catch(IOException | NullPointerException | URISyntaxException ex){
            System.out.println("Something went wrong");
            ex.printStackTrace();            
        }
        errorMessage.setText("Incorrect username or password");
    }
    
    private void openHomepage(ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent homepageParent = loader.load();
        Homepage controller = loader.getController();
        controller.setCurrentUser(currentUser);
        Scene homepageScene = new Scene(homepageParent);
        
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(homepageScene);
        window.show();
    
    }
    
    private void updateUser(String username) throws FileNotFoundException, IOException, CsvException{
//        URL url = getClass().getResource("users.csv");
//        System.out.println(url.toString());
        //            File file = new File(url.toURI()).getAbsoluteFile();
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
        System.out.println("VAL: "+allUsers.get(cnt)[2]);
        allUsers.get(cnt)[2] = "8";
        reader.close();
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        writer.writeAll(allUsers);
        writer.flush();
        writer.close();

    
    }
    
    @FXML public void registerNewUser(ActionEvent e){
        String username = usernameBox.getText();
        String password = passwordBox.getText();
        addNewUser(username, password);
    }
    
    public void addNewUser(String username, String password){
        if (checkIfExistUsername(username)){
            return;
        }      
        FileWriter writer = null;
        try {
            writer = new FileWriter("src/TutorSystem/users.csv", true);
            StringBuilder sb = new StringBuilder();
            sb.append(username);
            sb.append(",");
            sb.append(password);
            sb.append(",");
            sb.append("5");
            sb.append(",");
            sb.append("5");
            sb.append(",");
            sb.append("5");            
            sb.append("\n");
            writer.write(sb.toString());
            errorMessage.setText("Registered! Please presss login now.");
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean checkIfExistUsername(String username) {
        try {
            URL url = getClass().getResource("users.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] row;
            reader.readNext(); // Skips heading rows
            while ((row = reader.readNext()) != null){
                if (row[USERNAME_INDEX].equals(username)){
                    errorMessage.setText("This username is already taken");
                    return true;
                }
            }
        } catch (URISyntaxException | CsvValidationException | IOException ex) {
//            Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return false;
    }
}

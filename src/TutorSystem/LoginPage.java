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
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


/**
 *
 * @author Hassan
 */
public class LoginPage extends Application{
//   @FXML private BorderPane root;
    @FXML private Label header;
    @FXML private Label errorMessage;
    @FXML private TextField usernameBox;
    @FXML private TextField passwordBox;
    @FXML private Button loginButton;
    
    /*
    YOU DONT NEED START TO LOAD JUST LOOK AT MAPPANEL FOR HOW TO LOAD
    */
   @Override
   public void start(Stage stage) throws IOException {
       
       URL url = getClass().getResource("LoginMenu.fxml");
//       FXMLLoader loader = new FXMLLoader(getClass().getResource("/TutorSystem/LoginMenu.fxml"));
       Parent root = FXMLLoader.load(url);
//       Group root = new Group(new Label("Hello JavaFX!"));
       Scene scene = new Scene(root, 1024, 786);
       stage.setScene(scene);
       stage.show();
   }
    public LoginPage(){
    
    }
    
    public static void main(String[] args) {
        Application.launch(args);

    }
    
    @FXML
    public void dosomethin(ActionEvent e){
        System.out.println("test");
    }
    
    @FXML
    public void checkLogin(ActionEvent e) throws CsvValidationException{
        String username = usernameBox.getText();
        String password = passwordBox.getText();
        System.out.println(username);
        System.out.println(password);    
        login(username, password);
        
    }
    
    public void login(String username, String password) throws CsvValidationException{
        try {
            URL url = getClass().getResource("Users.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] row;
            reader.readNext(); // Skips heading rows
            while ((row = reader.readNext()) != null){
                if (row[0].equals(username) && row[1].equals(password)){
                    System.out.println("Pog?");
                }
            }
                    
        }catch(IOException | NullPointerException | URISyntaxException e){
            System.out.println("Something went wrong");
            e.printStackTrace();            
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorSystem;

/**
 *
 * @author Hassan
 */
public class User {
    private String username, password;   
    private int confidence;
    private int effort;
    private int independence;

    
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
    
    public String getUsername(){
        return username;
    }
    
    
}

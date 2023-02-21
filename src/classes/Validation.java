/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wongj
 */
public class Validation {
    
    public Validation() {};
    
    FileHandling fh = new FileHandling();
    
    public boolean passwordValid(String password) {   
        String passwordPattern =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    
    public boolean emailValid(String email) {
        String emailPattern = 
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    public boolean emailDuplication(String email, String fileName, String exception) {
        List<String> userProfile = fh.fileRead(fileName);
        
        for (int i = 1; i<userProfile.size(); i++) {
            String[] userInfo = userProfile.get(i).split(";");
            String userID = userInfo[0].toLowerCase();
            String eEmail = userInfo[1].toLowerCase();
            if (email.toLowerCase().equals(eEmail) && !userID.equals(exception.toLowerCase())) {
                return false;
            }
        } return true;
    }
}

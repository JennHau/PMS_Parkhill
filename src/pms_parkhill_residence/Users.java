/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wongj
 */
public class Users{
    private String userID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNo;
    private String identificationNo;
    private String gender;
    FileHandling fh = new FileHandling();
    
    public Users() {}
    
    //Constructor for users
    public Users(String userID, String email, String password, String firstName,
                 String lastName, String identificationNo, String gender,
                 String phoneNo) { 
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.identificationNo = identificationNo;
        this.gender = gender;
    }

    /**
     * @return the userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the phoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * @param phoneNo the phoneNo to set
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * @return the identificationNo
     */
    public String getIdentificationNo() {
        return identificationNo;
    }

    /**
     * @param identificationNo the identificationNo to set
     */
    public void setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String[] login(String email, String password) throws IOException {
        List<String> userProfile = fh.fileRead("userProfile.txt");
        for (String eachUser : userProfile){
            String[] userDet = eachUser.split(";");
            String userEmail = userDet[1];
            if (userEmail.equals(email)) {
                String userPass = userDet[2];
                if (userPass.equals(password)) {
                    return userDet;
                }
            }
        }
        return null;
    }
    
    public void setCurrentSession(String email) {
        try {
            List<String> userProfile = fh.fileRead("userProfile.txt");
            String[] userProfileArray = new String[userProfile.size()];
            userProfile.toArray(userProfileArray);
            
            List<String> newData = new ArrayList<>();
            newData.add("userID;email;password;firstName;lastName;"
                    + "identificationNo;gender;phoneNo;unitNo;");
            
            for (int i = 1; i<userProfile.size(); i++){
                String[] userInfo = userProfileArray[i].split(";");
                String email_temp = userInfo[1];
                if(email.equals(email_temp)){
                    newData.add(userProfileArray[i]);
                }
            } 
            fh.fileWrite("currentSession.txt", false, newData);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public boolean resetPasswordVerify(String email, String phoneNo, String identificationNo)
            throws IOException {
        List<String> userProfile = fh.fileRead("userProfile.txt");
        String[] userProfileArray = new String[userProfile.size()];
        userProfile.toArray(userProfileArray);
        
        for (int i = 1; i<userProfile.size(); i++){
            String[] userInfo = userProfileArray[i].split(";");
            String email_temp = userInfo[1];
            String iden_temp = userInfo[5];
            String phoneNo_temp = userInfo[7];
            if(email.equals(email_temp) && phoneNo.equals(phoneNo_temp)
                    && identificationNo.equals(iden_temp)) {
                setCurrentSession(email);
                return true;
            } 
        } return false;
    }
    
    public void resetPassword(String password, String email) throws IOException {
        List<String> userProfile = fh.fileRead("userProfile.txt");
        String[] userProfileArray = new String[userProfile.size()];
        userProfile.toArray(userProfileArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i<userProfile.size(); i++) {
            String[] userInfo2 = userProfileArray[i].split(";");
            String userID_temp = userInfo2[0];
            String email_temp = userInfo2[1].toLowerCase();
            String firstName_temp = userInfo2[3];
            String lastName_temp = userInfo2[4];
            String identificationNo_temp = userInfo2[5];
            String gender_temp = userInfo2[6];
            String phoneNo_temp = userInfo2[7];
            String unitNo_temp = userInfo2[8];
            if(!email_temp.equals(email.toLowerCase())) {
                newData.add(userProfileArray[i]);
            } else {
                    newData.add(userID_temp +";"+ email_temp +";"+ password +";"+
                            firstName_temp +";"+ lastName_temp +";"+
                            identificationNo_temp +";"+ gender_temp +";"+
                            phoneNo_temp +";"+ unitNo_temp +";");
            }
        } 
        fh.fileWrite("userProfile.txt", false, newData);
    }
    
    public void resetPasswordtoDefault(String password, String userID) {
        List<String> userProfile = fh.fileRead("userProfile.txt");
        String[] userProfileArray = new String[userProfile.size()];
        userProfile.toArray(userProfileArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i<userProfile.size(); i++) {
            String[] userInfo2 = userProfileArray[i].split(";");
            String userID_temp = userInfo2[0];
            String email_temp = userInfo2[1];
            String firstName_temp = userInfo2[3];
            String lastName_temp = userInfo2[4];
            String identificationNo_temp = userInfo2[5];
            String gender_temp = userInfo2[6];
            String phoneNo_temp = userInfo2[7];
            String unitNo_temp = userInfo2[8];
            if(!userID.equals(userID_temp)) {
                newData.add(userProfileArray[i]);
            } else {
                    newData.add(userID_temp +";"+ email_temp +";"+ password +";"+
                            firstName_temp +";"+ lastName_temp +";"+
                            identificationNo_temp +";"+ gender_temp +";"+
                            phoneNo_temp +";"+ unitNo_temp +";");
            }
        } 
        fh.fileWrite("userProfile.txt", false, newData);
    }
    
    public void modifySelfAccount() {
        String userID = this.getUserID().toLowerCase();
        String email = this.getEmail();
        String firstName = this.getFirstName();
        String lastName = this.getLastName();
        String password = this.getPassword();
        String identificationNo = this.getIdentificationNo();
        String gender = this.getGender();
        String phoneNo = this.getPhoneNo();
        
        List<String> userProfile = fh.fileRead("userProfile.txt");
        String[] userProfileArray = new String[userProfile.size()];
        userProfile.toArray(userProfileArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i<userProfile.size(); i++) {
            String[] userInfo = userProfileArray[i].split(";");
            String userID_temp = userInfo[0];
            
            if (userID_temp.equals(userID)) {
                newData.add(userID +";"+ email +";"+ password +";"+ firstName
                        +";"+ lastName +";"+ identificationNo +";"+ gender
                        +";"+ phoneNo +";"+ "-" +";");
            } else {
                newData.add(userProfileArray[i]);
            }
        } fh.fileWrite("userProfile.txt", false, newData);
    }
}
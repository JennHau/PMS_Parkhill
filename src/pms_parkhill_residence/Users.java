/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import buildingExecutive.BuildingExecutiveMainPage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wongj
 */
public class Users{
    private Users user;
    private String userID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNo;
    private String identificationNo;
    private String gender;
    private String unitNo;
    FileHandling fh = new FileHandling();
    
    public Users() {}
    
    //Constructor for non-residents
    public Users(String userId){
        userID = userId;
        List<String> userFile = fh.fileRead("userProfile.txt");
        for (String eachUser : userFile) {
            String[] userDetails = eachUser.split(";");
            String id = userDetails[0];
            if (userID.equals(id)) {
                email = userDetails[1];
                password = userDetails[2];
                firstName = userDetails[3];
                lastName = userDetails[4];
                identificationNo = userDetails[5];
                gender = userDetails[6];
                phoneNo = userDetails[7];
                unitNo = userDetails[8];
            }
        }
    }
    
    //Constructor for non-redidents
    public Users(String userID, String email, String password, String firstName,
                 String lastName, String identificationNo, String gender,
                 String phoneNo) { 
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.identificationNo = identificationNo;
        this.gender = gender;
        this.unitNo = "-";
    }

    //Constructor for redidents/tenants/vendors
    public Users(String userID, String email, String password, String firstName,
                 String lastName, String identificationNo, String gender, String phoneNo, 
                 String unitNo) { 
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.identificationNo = identificationNo;
        this.gender = gender;
        this.unitNo = unitNo;
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

    /**
     * @return the unitNo
     */
    public String getUnitNo() {
        return unitNo;
    }

    /**
     * @param unitNo the unitNo to set
     */
    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }
    
//    public boolean login(String email, String password) throws IOException {
//        List<String> userProfile = fh.fileRead("userProfile.txt");
//        String[] userProfileArray = new String[userProfile.size()];
//        userProfile.toArray(userProfileArray);
//        
//        // declare username_lst arrayList to store username in text file
//        ArrayList<String> email_lst = new ArrayList<String>();
//        // declare password_lst arrayList to store password in text file
//        ArrayList<String> password_lst = new ArrayList<String>();
//        // declare repeatedPosition arrayList to store the repeated password index in text file
//        ArrayList<Integer> repeatedPosition = new ArrayList<Integer>();
//        
//        // extract data from text file
//        for (int i = 1; i<userProfile.size(); i++){
//            String[] userInfo = userProfileArray[i].split(";");
//            String email_temp = userInfo[1];
//            String password_temp = userInfo[2];
//            email_lst.add(email_temp);
//            password_lst.add(password_temp);
//        } 
//
//        // scan for repeated password
//        for (int i = 0; i < password_lst.size(); i++) {
//            if (password_lst.get(i).equals(password)) {
//                // add index of repeated password to repeatedPosition arrayList
//                repeatedPosition.add(i);
//            } 
//        }
//        // if username from text field contains in arrayList 
//        if (email_lst.contains(email) && password_lst.contains(password) &&
//                // and password index in password_lst contains in repeatedPosition arrayList
//                repeatedPosition.contains(password_lst.indexOf(password))) {
//            setCurrentSession(email);
//            // if password from text field contains in arrayList
//            return true;
//        } else {
//            return false;
//        }
//    }
    
    public boolean login(String email, String password) throws IOException {
        List<String> userProfile = fh.fileRead("userProfile.txt");
        for (String eachUser : userProfile){
            String[] userDet = eachUser.split(";");
            String userEmail = userDet[1];
            if (userEmail.equals(email)) {
                String userPass = userDet[2];
                if (userPass.equals(password)) {
                    user = new Users(userDet[0]);
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public void userRole(String email) throws IOException {
        List<String> userProfile = fh.fileRead("userProfile.txt");
        String[] userProfileArray = new String[userProfile.size()];
        userProfile.toArray(userProfileArray);
        
        String userID = "";
        for (int i = 1; i<userProfile.size(); i++){
            String[] userInfo = userProfileArray[i].split(";");
            String userID_temp = userInfo[0];
            String email_temp = userInfo[1];
            if (email.equals(email_temp)) {
                userID = userID_temp;
            }
        } 
        if(userID.startsWith("bdm")) {
            
        } else if(userID.startsWith("ace")) {
            
        } else if(userID.startsWith("ade")) {
            
        } else if(userID.startsWith("rsd")) {
            new OwnerMainPage().setVisible(true);
        } else if(userID.startsWith("vdr")) {
            
        } else if(userID.startsWith("scg")) {
            
        } else if(userID.startsWith("vst")) {
            
        }
    }
    
    public void userPage() {
        String userCode = user.getUserID().substring(0, 3);
        switch (userCode) {
            case "bde" -> new BuildingExecutiveMainPage(this.user).setVisible(true);
            
        }
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
    
    public void resetPassword(String password) throws IOException {
        List<String> currentUser = fh.fileRead("currentSession.txt");
        String[] currentUserArray = new String[currentUser.size()];
        currentUser.toArray(currentUserArray);
        
        String[] userInfo = currentUserArray[1].split(";");
        String email = userInfo[1];
        
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
            if(!email_temp.equals(email)) {
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
    
    public void resetPassword(String password, String userID) {
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
    
    public void userRegistration() {
        String userID = this.getUserID().toLowerCase();
        String email = this.getEmail();
        String password = this.getPassword();
        String firstName = this.getFirstName();
        String lastName = this.getLastName();
        String identificationNo = this.getIdentificationNo();
        String gender = this.getGender();
        String phoneNo = this.getPhoneNo();
        String unitNo = this.getUnitNo();
        
        List<String> newData = new ArrayList<>();
        newData.add(userID +";"+ email +";"+password +";"+ firstName +";"+ lastName
                +";"+ identificationNo +";"+ gender +";"+ phoneNo +";"+ unitNo +";");
        
        fh.fileWrite("userProfile.txt", true, newData);
    }
    
    public void setAllUserData(String userID) {
        List<String> userProfile = fh.fileRead("userProfile.txt");
        String[] userProfileArray = new String[userProfile.size()];
        userProfile.toArray(userProfileArray);
        
        for (int i = 0; i<userProfile.size(); i++) {
            String[] userInfo2 = userProfileArray[i].split(";");
            String userID_temp = userInfo2[0];
            String email = userInfo2[1];
            String firstName = userInfo2[3];
            String lastName = userInfo2[4];
            String identificationNo = userInfo2[5];
            String gender = userInfo2[6];
            String phoneNo = userInfo2[7];
            String unitNo = userInfo2[8];
            
            if (userID_temp.equals(userID.toLowerCase())) {
                this.userID = userID;
                this.firstName = firstName;
                this.lastName = lastName;
                this.email = email;
                this.phoneNo = phoneNo;
                this.identificationNo = identificationNo;
                this.gender = gender;
                this.unitNo = unitNo;
            }
        }
    }
    
    public void modifyUserAccount() {
        String userID = this.getUserID().toLowerCase();
        String email = this.getEmail();
        String firstName = this.getFirstName();
        String lastName = this.getLastName();
        String identificationNo = this.getIdentificationNo();
        String gender = this.getGender();
        String phoneNo = this.getPhoneNo();
        String unitNo = this.getUnitNo();
        
        List<String> userProfile = fh.fileRead("userProfile.txt");
        String[] userProfileArray = new String[userProfile.size()];
        userProfile.toArray(userProfileArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i<userProfile.size(); i++) {
            String[] userInfo = userProfileArray[i].split(";");
            String userID_temp = userInfo[0];
            String password_temp = userInfo[2];
            
            if (userID_temp.equals(userID)) {
                newData.add(userID +";"+ email +";"+ password_temp +";"+ firstName
                        +";"+ lastName +";"+ identificationNo +";"+ gender
                        +";"+ phoneNo +";"+ unitNo +";");
            } else {
                newData.add(userProfileArray[i]);
            }
        } fh.fileWrite("userProfile.txt", false, newData);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author wongj
 */
public class Users {
    private String userID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNo;
    private String identificationNo;
    private String gender;
    private Date birthOfDate;
    private String role;
    private String unitNo;
    
    public Users() {}
    
    //Constructor for non-redidents
    public Users(String userID, String firstName, String lastName, String email,
                 String password, String phoneNo, String identificationNo,
                 String gender, Date birthOfDate, String role) { 
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.identificationNo = identificationNo;
        this.gender = gender;
        this.birthOfDate = birthOfDate;
        this.role = role;
    }
    
    //Constructor for redidents
    public Users(String userID, String firstName, String lastName, String email,
                 String password, String phoneNo, String identificationNo,
                 String gender, Date birthOfDate, String role, String unitNo) { 
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.identificationNo = identificationNo;
        this.gender = gender;
        this.birthOfDate = birthOfDate;
        this.unitNo = unitNo;
        System.out.println("helo");
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
     * @return the birthOfDate
     */
    public Date getBirthOfDate() {
        return birthOfDate;
    }

    /**
     * @param birthOfDate the birthOfDate to set
     */
    public void setBirthOfDate(Date birthOfDate) {
        this.birthOfDate = birthOfDate;
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
    
    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean login(String email, String password) throws IOException {
        FileHandling fh = new FileHandling();
        List<String> userProfile = fh.fileRead("userProfile.txt");
        String[] userProfileArray = new String[userProfile.size()];
        userProfile.toArray(userProfileArray);
        
        // declare username_lst arrayList to store username in text file
        ArrayList<String> email_lst = new ArrayList<String>();
        // declare password_lst arrayList to store password in text file
        ArrayList<String> password_lst = new ArrayList<String>();
        // declare repeatedPosition arrayList to store the repeated password index in text file
        ArrayList<Integer> repeatedPosition = new ArrayList<Integer>();
            
        // extract data from text file
        for (int i = 0; i<userProfile.size(); i++){
            String[] userInfo = userProfileArray[i].split(";");
            String email_temp = userInfo[1];
            String password_temp = userInfo[2];
            email_lst.add(email_temp);
            password_lst.add(password_temp);
        } 

        // scan for repeated password
        for (int i = 0; i < password_lst.size(); i++) {
            if (password_lst.get(i).equals(password)) {
                // add index of repeated password to repeatedPosition arrayList
                repeatedPosition.add(i);
            } 
        }
        // if username from text field contains in arrayList 
        if (email_lst.contains(email) && password_lst.contains(password) &&
                // and password index in password_lst contains in repeatedPosition arrayList
                repeatedPosition.contains(password_lst.indexOf(password))) {
            setCurrentSession(email);
            // if password from text field contains in arrayList
            return true;
        } else {
            return false;
        }
    }
    
    public void userRole(String email) throws IOException {
        FileReader fr = new FileReader("userProfile.txt");
        BufferedReader br = new BufferedReader(fr);
        br.readLine(); String userID = "";
        for (String line = br.readLine(); line != null; line = br.readLine()){
            String[] userInfo = line.split(";");
            String userID_temp = userInfo[0];
            String email_temp = userInfo[1];
            if (email.equals(email_temp)) {
                userID = userID_temp;
            }
        } br.close(); fr.close();
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
    
    public void setCurrentSession(String email) {
        try {
            FileWriter fWriter = new FileWriter("currentSession.txt");
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            FileReader fReader = new FileReader("userProfile.txt");
            BufferedReader bReader = new BufferedReader(fReader);
            
            fWriter.write("userID;email;password;firstName;lastName;"
                    + "identificationNo;gender;phoneNo;unitNo;" + "\n");
            
            for (String line = bReader.readLine(); line != null; line = bReader.readLine()){
                String[] userInfo = line.split(";");
                String email_temp = userInfo[1];
                if(email.equals(email_temp)){
                    bWriter.write(line + "\n");
                }
            } 
            fWriter.flush(); bWriter.flush(); bReader.close(); fReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public boolean resetPasswordVerify(String email, String phoneNo, String identificationNo)
            throws IOException {
        FileReader fr = new FileReader("userProfile.txt");
        BufferedReader br = new BufferedReader(fr);
        for (String line = br.readLine(); line != null; line = br.readLine()){
            String[] userInfo = line.split(";");
            String email_temp = userInfo[1];
            String iden_temp = userInfo[5];
            String phoneNo_temp = userInfo[7];
            if(email.equals(email_temp) && phoneNo.equals(phoneNo_temp)
                    && identificationNo.equals(iden_temp)) {
                setCurrentSession(email); br.close(); fr.close();
                return true;
            } 
        } return false;
    }
    
    public void resetPassword(String password) throws IOException {
        FileReader fr = new FileReader("currentSession.txt");
        BufferedReader br = new BufferedReader(fr);
        br.readLine(); String line = br.readLine();
        String[] userInfo = line.split(";");
        String email = userInfo[1];
        fr.close(); br.close();
        
        FileReader fr2 = new FileReader("userProfile.txt");
        BufferedReader br2 = new BufferedReader(fr2);
        
        FileWriter fw = new FileWriter("temp.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        
        for (String line2 = br2.readLine(); line2 != null; line2 = br2.readLine()) {
            String[] userInfo2 = line2.split(";");
            String userID_temp = userInfo2[0];
            String email_temp = userInfo2[1];
            String firstName_temp = userInfo2[3];
            String lastName_temp = userInfo2[4];
            String identificationNo_temp = userInfo2[5];
            String gender_temp = userInfo2[6];
            String phoneNo_temp = userInfo2[7];
            String unitNo_temp = userInfo2[8];
            if(!email_temp.equals(email)) {
                bw.write(line2 + "\n");
            } else {
                    bw.write(userID_temp +";"+ email_temp +";"+ password +";"+
                            firstName_temp +";"+ lastName_temp +";"+
                            identificationNo_temp +";"+ gender_temp +";"+
                            phoneNo_temp +";"+ unitNo_temp +";"+ "\n");
            }
        } 
        fw.flush(); bw.flush(); fw.close(); bw.close(); 
        br.close(); fr.close(); 
        br2.close(); fr2.close();
        
        File oldFile = new File("userProfile.txt");
        oldFile.delete();
        
        File tempFile = new File("temp.txt");
        File rename = new File("userProfile.txt");
        tempFile.renameTo(rename);
    }
}

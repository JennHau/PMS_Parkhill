/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.util.Date;

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
    private String unitNo;
    
    //Constructor for non-redidents
    public Users(){}
    
    // Constructor for admin
    public Users(String userID, String firstName, String lastName, String email,
                 String phoneNo, String identificationNo,
                 String gender, Date birthOfDate) { 
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.identificationNo = identificationNo;
        this.gender = gender;
        this.birthOfDate = birthOfDate;
    }
    
    //Constructor for residents
    public Users(String userID, String firstName, String lastName, String email,
                 String password, String phoneNo, String identificationNo,
                 String gender, Date birthOfDate, String unitNo) { 
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
}

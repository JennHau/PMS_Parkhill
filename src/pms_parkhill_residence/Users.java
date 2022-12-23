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
    public Users(String userID, String firstName, String lastName, String email,
                 String password, String phoneNo, String identificationNo,
                 String gender, Date birthOfDate) { 
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.identificationNo = identificationNo;
        this.gender = gender;
        this.birthOfDate = birthOfDate;
    }
    
    //Constructor for redidents
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
}

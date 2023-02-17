/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Winson
 */
public class Employee {
    private String empID;
    private String empEmail;
    private String empName;
    private String phoneNo;
    private String position;
    private String icNo;
    private String gender;
    
    public Employee(String[] empData) {
        this.empID = empData[0];
        this.empEmail = empData[1];
        this.empName = empData[2];
        this.phoneNo = empData[3];
        this.position = empData[4];
        this.icNo = empData[5];
        this.gender = empData[6];
    }
    
    private final String[] empCode = {"scg", "tcn", "cln"};
    
    public boolean isEmployee() {
        List<String> employeeCode = new ArrayList<>(Arrays.asList(empCode));

        return employeeCode.contains(this.empID.substring(0, 3));
    }

    /**
     * @return the empID
     */
    public String getEmpID() {
        return empID;
    }

    /**
     * @param empID the empID to set
     */
    public void setEmpID(String empID) {
        this.empID = empID;
    }

    /**
     * @return the empEmail
     */
    public String getEmpEmail() {
        return empEmail;
    }

    /**
     * @param empEmail the empEmail to set
     */
    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    /**
     * @return the empName
     */
    public String getEmpName() {
        return empName;
    }

    /**
     * @param empName the empName to set
     */
    public void setEmpName(String empName) {
        this.empName = empName;
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
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * @return the icNo
     */
    public String getIcNo() {
        return icNo;
    }

    /**
     * @param icNo the icNo to set
     */
    public void setIcNo(String icNo) {
        this.icNo = icNo;
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
    
    
}

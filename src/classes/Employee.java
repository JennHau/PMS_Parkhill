/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

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
    
    private ArrayList<AssignedJob> employeeJob = new ArrayList<>();
    private String positionCode;
    
    FileHandling FH = new FileHandling();
    TextFile TF = new TextFile();
    public Job JB = new Job();
    
    public Employee(String empId) {
        List<String> employeeList = FH.fileRead(TF.fullEmployeeList);
        for (String eachEmp : employeeList) {
            String[] empData = eachEmp.split(TF.sp);
            String eId = empData[0];
            if (eId.equals(empId)) {
                this.empID = eId;
                this.empEmail = empData[1];
                this.empName = empData[2];
                this.phoneNo = empData[3];
                this.position = empData[4];
                this.icNo = empData[5];
                this.gender = empData[6];
                
                setPositionCode();
                setEmployeeJobList();
            }
        }
    }
    
    public Employee(String[] empData) {
        this.empID = empData[0];
        this.empEmail = empData[1];
        this.empName = empData[2];
        this.phoneNo = empData[3];
        this.position = empData[4];
        this.icNo = empData[5];
        this.gender = empData[6];
        
        setPositionCode();
        setEmployeeJobList();
    }
    
    private void setEmployeeJobList() {
        List<String> allJob = FH.fileRead(TF.employeeJobFile);
        
        boolean firstLine = true;
        for (String jobLine : allJob) {
            if (!firstLine) {
                AssignedJob assJob = new AssignedJob(jobLine.split(TF.sp));

                String emplyID = assJob.getTaskEmpID();

                if (emplyID.equals(this.empID)) {
                    employeeJob.add(assJob);
                }
            }
            
            firstLine = false;
        }
    }
    
    public ArrayList<AssignedJob> getEmployeeJobList() {
        return employeeJob;
    }
    
    private final String[] empCode = {"scg", "tcn", "cln"};
    
    public boolean isEmployee() {
        List<String> employeeCode = new ArrayList<>(Arrays.asList(empCode));

        return employeeCode.contains(this.empID.substring(0, 3));
    }
    
    // Getter and Setter
    private void setPositionCode() {
        switch (this.position) {
            case "Security Guard" -> this.positionCode = empCode[0];
            case "Technician" -> this.positionCode = empCode[1];
            case "Cleaner" -> this.positionCode = empCode[2];
        }
    }
    
    public String getPositionCode(String code) {
        String roleCode;
        switch (code) {
            case "Security Guard" -> roleCode = empCode[0];
            case "Technician" -> roleCode = empCode[1];
            case "Cleaner" -> roleCode = empCode[2];
            default -> roleCode = null;
        }
        
        return roleCode;
    }
    
    public String getPositionCode() {
        return this.positionCode;
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

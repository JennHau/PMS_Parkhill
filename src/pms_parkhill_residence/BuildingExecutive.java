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
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 * @author Winson
 */
public class BuildingExecutive extends Users{
    // text file seperator
    String sp = "; ";
    
    // user profile text file link
    File usersProfile = new File("userProfile.txt");
    
    // inactivated account text file link
    File inactiveAcc = new File("inactiveAcc.txt");
    
    // employee job file link
    File employeeJobFile = new File("employeeJobFile.txt");
    
    // to get employee list only from the user profile text file
    public void udpateEmployeeJobList() throws IOException{
            FileReader frUserProfile = new FileReader(usersProfile);
            BufferedReader brUserProfile = new BufferedReader(frUserProfile);

            FileWriter fwJobFile = new FileWriter(employeeJobFile, true);
            BufferedWriter bwJobFile = new BufferedWriter(fwJobFile);
            
            FileReader frJobFile;
            BufferedReader brJobFile;

        // add new employee
            boolean notExist = false;

            for (String line = brUserProfile.readLine(); line != null; line = brUserProfile.readLine()) {
                String[] data = line.split(sp);
                String userId = data[0];
                String role = userId.substring(0, 3);
                if (role.equals("scg")){
                    frJobFile = new FileReader(employeeJobFile);
                    brJobFile = new BufferedReader(frJobFile);
                    
                    for (String jobLine = brJobFile.readLine(); jobLine != null; jobLine = brJobFile.readLine()){
                         String[] jobLineData = jobLine.split(sp);
                         String emplyId = jobLineData[0];
                         if (emplyId.equals(userId)){
                             notExist = false;
                             break;
                         }else {
                             notExist = true;
                         }
                    }
                    brJobFile.close(); frJobFile.close();

                    if (notExist) {
                        bwJobFile.write(data[0] + sp + data[1] + sp + data[3] + data[4] + sp + data[7] + sp + "0" + sp + "\n");
                    }
                }
            }
            bwJobFile.close(); fwJobFile.close();
            brUserProfile.close(); frUserProfile.close();
            
        // remove inactivated employee account
            frJobFile = new FileReader(employeeJobFile);
            brJobFile = new BufferedReader(frJobFile);
            
            fwJobFile = new FileWriter(employeeJobFile, true);
            bwJobFile = new BufferedWriter(fwJobFile);
        
            FileReader frInactiveAcc = new FileReader("inactiveAccount.txt");
            BufferedReader brInactiveAcc = new BufferedReader(frInactiveAcc);
            
            ArrayList<String> arrayInactiveAcc = new ArrayList<>();
            ArrayList<String> removeInactiveAcc = new ArrayList<>();

            for (String inactiveAcc = brInactiveAcc.readLine(); inactiveAcc != null; inactiveAcc = brInactiveAcc.readLine()) {
                String[] getInactiveAccId = inactiveAcc.split(sp);
                String inactiveAccId = getInactiveAccId[0];
                arrayInactiveAcc.add(inactiveAccId);
            } brInactiveAcc.close(); frInactiveAcc.close();
            
            for (String jobLine = brJobFile.readLine(); jobLine != null; jobLine = brJobFile.readLine()){
                String[] emplyInfo = jobLine.split(sp);
                String emplyId = emplyInfo[0];
                if (!arrayInactiveAcc.contains(emplyId)){
                    removeInactiveAcc.add(jobLine);
                }
            }
            brJobFile.close(); frJobFile.close();
            
            PrintWriter pw = new PrintWriter(employeeJobFile);
            pw.flush();
            
            bwJobFile.write("userID; email; fullName; phoneNo; jobStatus;\n");
            
            for (String employeeInfo : removeInactiveAcc) {
                bwJobFile.write(employeeInfo + "\n");
            } bwJobFile.close(); fwJobFile.close();
    }
    
    public ArrayList getAssignedEmployees() throws IOException {
        FileReader frJobFile = new FileReader(employeeJobFile);
        BufferedReader brJobFile = new BufferedReader(frJobFile);
        
        ArrayList<String> assignedEmployee = new ArrayList<>();
        ArrayList<String> unassignedEmployee = new ArrayList<>();
        ArrayList<ArrayList> assignedANDunassigned = new ArrayList<>();

        boolean firstLine = true;
        
        for (String jobLine = brJobFile.readLine(); jobLine != null; jobLine = brJobFile.readLine()) {
            if (!firstLine) {
                String[] employeeInfo = jobLine.split(sp);
                String emplyJobStatus = employeeInfo[4];
                String emplyId = employeeInfo[0];
                String role = emplyId.substring(0, 3);
                String employeeData = employeeInfo[0] + sp + employeeInfo[2] + sp + employeeInfo[3] + sp;
                String employeeRole = employeeRole(role);

                if (emplyJobStatus.equals("1")) {
                    assignedEmployee.add(employeeData + employeeRole + sp);
                }
                else {
                    unassignedEmployee.add(employeeData + employeeRole + sp);
                }
            }
            
            firstLine = false;
        }
        
        assignedANDunassigned.add(assignedEmployee);
        assignedANDunassigned.add(unassignedEmployee);
        
        return assignedANDunassigned;
    }
    
//    public ArrayList getUnassignedEmployee() {
//        
//    }
    
    public String employeeRole(String role) {
        String employeeRole = null;
        
        switch (role) {
            case "scg" -> employeeRole = "Security Guard";
            case "tnc" -> employeeRole = "Technician";
            case "cln" -> employeeRole = "Cleaner";
        }
        
        return employeeRole;
    }
    
    public void assignJob() {
        
    }
}

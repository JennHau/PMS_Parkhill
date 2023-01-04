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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;

/**
 *
 * @author Winson
 */
public class BuildingExecutive extends Users{
    Users users = new Users();
    
    // text file seperator
    String sp = "; ";
    
    final int assignedEmployee = 1;
    final int unassignedEmployee = 0;
    
    // user profile text file link
    File userProfile = new File("userProfile.txt");
    
    // inactivated account text file link
    File inactiveAcc = new File("inactiveAcc.txt");
    
    // employee name list link
    File arrayToAppend = new File("employeeList.txt");
    
    // employee job file link
    File employeeJobFile = new File("employeeJobFile.txt");
    
    // selected Employee record
    File recordSelectedEmployee = new File("recordSelectedEmployee.txt");
    
    public Users BuildingExecutive(String userID, String firstName, String lastName, 
                                   String email, String phoneNo, String identificationNo,
                                   String gender, Date dateOfBirth){
        
        return new Users(userID, firstName, lastName, email, phoneNo, identificationNo, gender, dateOfBirth);
    }
    
    // to get employee list only from the user profile text file
    public void updateEmployeeList() throws IOException{
            BufferedReader brUserProfile = fileReader(userProfile);

            BufferedWriter bwJobFile = fileWriter(arrayToAppend, false);

            ArrayList<String> newEmployeeList = new ArrayList<>();

        // add new employee
            String fileHeader = "userID; email; fullName; phoneNo; position;";
            newEmployeeList.add(fileHeader);
            
            for (String line = brUserProfile.readLine(); line != null; line = brUserProfile.readLine()) {
                String[] data = line.split(sp);
                String userId = data[0];
                String role = userId.substring(0, 3);
                if (isEmployee(role)){
                    String position = employeeRole(role);
                    newEmployeeList.add(data[0] + sp + data[1] + sp + data[3] + " " + data[4] + sp + data[7] + sp + position + sp);
                }
            }
            brUserProfile.close(); 
            
            for (String employeeInfo : newEmployeeList) {
                bwJobFile.write(employeeInfo + "\n");
            } bwJobFile.close();
    }
    
    private ArrayList getEmployeeJobList(LocalDate localDate, LocalTime localTime) throws IOException {
        BufferedReader readEmployeeList = fileReader(arrayToAppend);
        
        BufferedReader readJobFile = fileReader(employeeJobFile);
        
        ArrayList<String> workingList = new ArrayList<>();
        ArrayList<String> assignedEmployee = new ArrayList<>();
        ArrayList<String> unassignedEmployee = new ArrayList<>();
        ArrayList<ArrayList> assignedANDunassigned = new ArrayList<>();

        boolean firstLine;
        
        firstLine = true;
        for (String jobFileLine = readJobFile.readLine(); jobFileLine != null; jobFileLine = readJobFile.readLine()) {
            if (!firstLine) {
                String[] jobLineDetails = jobFileLine.split(sp);
                String workingEmplyId = jobLineDetails[0];
                String jobAssigned = jobLineDetails[2];
                String assignee = jobLineDetails[8];
                
                LocalDate workingDate = formatDate(jobLineDetails[3]);
                LocalTime workingTime = formatTime(jobLineDetails[4]);
                LocalTime workingEndTime = formatTime(jobLineDetails[6]);    
                
                if (localDate.equals(workingDate) && !(localTime.isBefore(workingTime) || localTime.isAfter(workingEndTime))) {
                    if (!workingList.contains(workingEmplyId)) {
                        workingList.add(workingEmplyId);
                        
                        String[] employeeDetails = getEmployeeDetails(workingEmplyId).split(sp);
                        String workingEmplyName = employeeDetails[2];
                        String workingEmplyPos = employeeDetails[4];
                        
                        assignedEmployee.add(jobLineDetails[0] + sp + workingEmplyName + sp + workingEmplyPos + sp + jobAssigned + sp + assignee + sp);
                    }
                }
            }
            
            firstLine = false;
        }
        readJobFile.close();
        
        firstLine = true;
        for (String employeeList = readEmployeeList.readLine(); employeeList != null; employeeList = readEmployeeList.readLine()) {
            if (!firstLine) {
                String[] employeeInfo = employeeList.split(sp);
                String emplyId = employeeInfo[0];
                String emplyName = employeeInfo[2];
                String emplyPos = employeeInfo[4];
                
                if (!workingList.contains(emplyId)) {
                    unassignedEmployee.add(emplyId + sp + emplyName + sp + emplyPos + sp);
                }
            }
            
            firstLine = false;
        }
        
        assignedANDunassigned.add(unassignedEmployee);
        assignedANDunassigned.add(assignedEmployee);
        
        return assignedANDunassigned;
    }
    
    public ArrayList getAssignedEmployeeList(LocalDate localDate, LocalTime localTime, String role) throws IOException {
        
        ArrayList<ArrayList> employeeJobList = getEmployeeJobList(localDate, localTime);
        ArrayList<String> assignedEmployee = employeeJobList.get(0);
        
        if (role != null) {
            return getSpecificRoleList(assignedEmployee, role);
        }
        
        return assignedEmployee;
    }
    
    public ArrayList getEmployeeList(LocalDate localDate, LocalTime localTime, String role, String searchText, int employeeStatus) throws IOException {
        ArrayList<ArrayList> employeeJobList = getEmployeeJobList(localDate, localTime);
        ArrayList<String> employeeList = employeeJobList.get(employeeStatus);
        
        if (role != null && !role.equals("All")) {
            employeeList = getSpecificRoleList(employeeList, role);
        }
        
        if (searchText != null && !searchText.equals("")) {
            employeeList = searchFunction(employeeList, searchText);
        }
        
        return employeeList;
    }
    
    private ArrayList getSpecificRoleList(ArrayList<String> employeeList, String role) {
        ArrayList<String> specificRoleList = new ArrayList<>();
        
        for (String employeeInfo : employeeList) {
            String[] employeeDetails = employeeInfo.split(sp);
            
            if (employeeDetails[2].equals(role)) {
                specificRoleList.add(employeeInfo);
            }
        }
        
        return specificRoleList;
    }
    
    private ArrayList searchFunction(ArrayList<String> employeeList, String searchText) {
        ArrayList<String> searchedList = new ArrayList<>();
        
        for (String employeeInfo : employeeList) {
            String[] employeeDetails = employeeInfo.split(sp);
            
            if (employeeDetails[0].contains(searchText)) {
                searchedList.add(employeeInfo);
            }
        }
        
        return searchedList;
    }
    
    public boolean isEmployee(String roleCode) {
        return roleCode.equals("scg") || roleCode.equals("tcn") || roleCode.equals("cln");
    }
    
    public String employeeRole(String role) {
        String employeeRole = null;
        
        switch (role) {
            case "scg" -> employeeRole = "Security Guard";
            case "tnc" -> employeeRole = "Technician";
            case "cln" -> employeeRole = "Cleaner";
        }
        
        return employeeRole;
    }
    
    public void recordSelectedEmployee(String employeeID) throws IOException {
        String fileHeader = "employeeID; \n";
        
        BufferedWriter writeRecord = fileWriter(recordSelectedEmployee, false);
        
        writeRecord.write(fileHeader + employeeID + sp);
    }
    
    public BufferedReader fileReader(File fileName) throws IOException {
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        
        return br;
    }
    
    public BufferedWriter fileWriter(File fileName, boolean append)throws IOException{
        FileWriter fw = new FileWriter(fileName, append);
        BufferedWriter bw = new BufferedWriter(fw);
        
        return bw;
    }
    
    public LocalDate formatDate(String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        
        return localDate;
    }
    
    public LocalTime formatTime(String time) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        LocalTime localTime = LocalTime.parse(time, timeFormatter);
        
        return localTime;
    }
    
    public LocalTime getTimeCategory(LocalTime time) {
        int timeMin = time.getMinute();
        if (timeMin < 30) {
            time = time.withMinute(00);
        }
        else {
            time = time.withMinute(30);
        }
        
        return time;
    }
    
    public String getEmployeeDetails(String employeeID) throws IOException {
        BufferedReader readEmployeeList = fileReader(arrayToAppend);
        
        boolean firstLine = true;
        for (String employeeList = readEmployeeList.readLine(); employeeList != null; employeeList = readEmployeeList.readLine()) {
            if (!firstLine) {
                String[] employeeInfo = employeeList.split(sp);
                String emplyId = employeeInfo[0];
                
                if (emplyId.equals(employeeID)) {
                    readEmployeeList.close();
                    return employeeList;
                }
            }
            
            firstLine = false;
        }
        readEmployeeList.close();
        
        return null;
    }
    
    public void fileCleaner(File fileName) throws IOException {
        PrintWriter pw = new PrintWriter(fileName);
        pw.flush();
    }
    
    // Change Page Method
    public void toDashboard(JFrame frame) {
        try {
            BuildingExecutiveMainPage page;
            page = new BuildingExecutiveMainPage();
            page.setVisible(true);
            frame.dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void toJobManagement(JFrame frame) {
        try {
            BuildingExecutiveJobManagement page;
            page = new BuildingExecutiveJobManagement(users.getUserID(), users.getFirstName(), users.getLastName(), 
                                                      users.getPhoneNo(), users.getEmail(), users.getIdentificationNo(), 
                                                      users.getGender(), users.getBirthOfDate());
            page.setVisible(true);
            frame.dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void toComplaints(JFrame frame) {
        try {
            BuildingExecutiveComplaints page;
            page = new BuildingExecutiveComplaints();
            page.setVisible(true);
            frame.dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void toPatrollingManagement(JFrame frame) {
        BuildingExecutivePatrollingManagement page;
        page = new BuildingExecutivePatrollingManagement();
        page.setVisible(true);
        frame.dispose();
    }
    
    public void toBEreports(JFrame frame) {
        BuildingExecutiveReports page;
        page = new BuildingExecutiveReports();
        page.setVisible(true);
        frame.dispose();
    }
}

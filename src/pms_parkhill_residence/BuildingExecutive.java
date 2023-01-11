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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Winson
 */
public class BuildingExecutive extends Users{
    // text file seperator
    String sp = "; ";
    
    // Employee position
    final String technician = "tcn";
    final String cleaner = "cln";
    final String securityGuard = "scg";
    
    // Repitition
    final int repititionON = 1;
    final int repititionOFF = 0;
    
    // Assign and Unassign Job to Employee
    final int assignedEmployee = 1;
    final int unassignedEmployee = 0;
    
    // user profile text file link
    File userProfile = new File("userProfile.txt");
    
    // inactivated account text file link
    File inactiveAcc = new File("inactiveAcc.txt");
    
    // employee name list link
    File fullEmployeeList = new File("employeeList.txt");
    
    // employee job file link
    File employeeJobFile = new File("employeeJobFile.txt");
    
    // selected Employee record
    File recordSelectedEmployee = new File("recordSelectedEmployee.txt");
    
    // job list link
    File jobListFile = new File("jobList.txt");
    
    // to get employee list only from the user profile text file
//    public void updateEmployeeList() throws IOException{
//            BufferedReader brUserProfile = fileReader(userProfile);
//
//            BufferedWriter bwJobFile = fileWriter(fullEmployeeList, false);
//
//            ArrayList<String> newEmployeeList = new ArrayList<>();
//
//        // add new employee
//            String fileHeader = "userID; email; fullName; phoneNo; position;";
//            newEmployeeList.add(fileHeader);
//            
//            for (String line = brUserProfile.readLine(); line != null; line = brUserProfile.readLine()) {
//                String[] data = line.split(sp);
//                String userId = data[0];
//                String role = userId.substring(0, 3);
//                if (isEmployee(role)){
//                    String position = showEmployeeFullRoleName(role);
//                    newEmployeeList.add(data[0] + sp + data[1] + sp + data[3] + " " + data[4] + sp + data[7] + sp + position + sp);
//                }
//            }
//            brUserProfile.close(); 
//            
//            for (String employeeInfo : newEmployeeList) {
//                bwJobFile.write(employeeInfo + "\n");
//            } bwJobFile.close();
//    }
    
    private ArrayList getEmployeeJobList(LocalDate localDate, LocalTime localTime) throws IOException {
        BufferedReader readEmployeeList = fileReader(fullEmployeeList);
        BufferedReader readJobFile = fileReader(employeeJobFile);
        
        ArrayList<String> workingList = new ArrayList<>();
        ArrayList<String> assEmply = new ArrayList<>();
        ArrayList<String> unassEmply = new ArrayList<>();
        ArrayList<ArrayList> assignedANDunassigned = new ArrayList<>();

        // Get the day of the date parsed
        String dayOfWeek = localDate.getDayOfWeek().toString().toLowerCase();
        
        boolean firstLine = true;
        for (String jobFileLine = readJobFile.readLine(); jobFileLine != null; jobFileLine = readJobFile.readLine()) {
            if (!firstLine) {
                String[] jobLineDetails = jobFileLine.split(sp);
                int repitition = Integer.valueOf(jobLineDetails[4]);
                String workingEmplyId = jobLineDetails[1];
                String jobAssigned = jobLineDetails[3];
                String assignee = jobLineDetails[11];
                
                String timeNeeded = jobLineDetails[5];
                LocalDate workingDate = null;
                LocalTime workingTime = formatTime(jobLineDetails[7]);
                String[] workingEndDateTime = jobLineDetails[8].split(" ");
                
                if (repitition == repititionON) {
                    boolean isOvernight = checkOvernight(formatTime(workingEndDateTime[1]), timeNeeded);
                    ArrayList<String> dayToRepeat = new ArrayList<>(Arrays.asList(jobLineDetails[9].split(",")));
                    if (isOvernight) {
                        dayOfWeek = localDate.getDayOfWeek().minus(1).toString().toLowerCase();
                        workingTime = LocalTime.parse("00:00:00");
                    }
                    
                    if (dayToRepeat.contains(dayOfWeek)) {
                        workingDate = localDate;
                    }
                }
                else {
                    workingDate = formatDate(jobLineDetails[6]);
                }
                
                if (workingDate != null) {
                    LocalDateTime selectedDateTime = LocalDateTime.of(localDate, localTime);
                    LocalDateTime startDateTime = LocalDateTime.of(workingDate, workingTime);
                    LocalDateTime endDateTime = LocalDateTime.of(formatDate(workingEndDateTime[0]), formatTime(workingEndDateTime[1]));

                    if ((selectedDateTime.equals(startDateTime) || selectedDateTime.isAfter(startDateTime)) 
                     && (selectedDateTime.equals(endDateTime) || selectedDateTime.isBefore(endDateTime))) {

                        if (!workingList.contains(workingEmplyId)) {
                            workingList.add(workingEmplyId);

                            String[] employeeDetails = getEmployeeDetails(workingEmplyId);
                            String workingEmplyName = employeeDetails[2];
                            String workingEmplyPos = employeeDetails[4];

                            assEmply.add(jobLineDetails[0] + sp + workingEmplyId + sp +
                                    workingEmplyName + sp + workingEmplyPos + sp +
                                    jobAssigned + sp + assignee + sp + "View" + sp);
                        }
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
                    unassEmply.add(emplyId + sp + emplyName + sp + emplyPos + sp + "Assign" + sp);
                }
            }
            
            firstLine = false;
        }
        readEmployeeList.close();
        
        assignedANDunassigned.add(unassEmply);
        assignedANDunassigned.add(assEmply);
        
        return assignedANDunassigned;
    }
    
    public ArrayList getSpecificStatusEmployeeList(LocalDate localDate, LocalTime localTime, String role, String searchText, int employeeStatus) throws IOException {
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
    
    public String showEmployeeFullRoleName(String role) {
        String employeeRole = null;
        
        switch (role) {
            case "scg" -> employeeRole = "Security Guard";
            case "tnc" -> employeeRole = "Technician";
            case "cln" -> employeeRole = "Cleaner";
        }
        
        return employeeRole;
    }
    
    public String getEmployeePositionCode(String employeeId, String roleName) throws IOException {
        String employeePos = null;
        if (employeeId != null) {
            String[] employeeDetails = getEmployeeDetails(employeeId);
            employeePos = employeeDetails[4];
        }
        
        if (roleName != null) {
            employeePos = roleName;
        }
        
        return switch (employeePos) {
            case "Technician" -> technician;
            case "Cleaner" -> cleaner;
            case "Security Guard" -> securityGuard;
            default -> null;
        };
    }
    
    public String validateTableSelectionAndGetValue(DefaultTableModel table, int selectedColumn, int selectedRow, int expectedColumn, int getValueColumn) {
        if (selectedColumn == expectedColumn) {
            String employeeData = (String) table.getValueAt(selectedRow, getValueColumn);
            return employeeData;
        }
        
        return null;
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
        
        LocalTime localTime = LocalTime.parse(LocalTime.parse(time).format(timeFormatter)).withSecond(1);
        
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
    
    public String[] getEmployeeDetails(String employeeID) throws IOException {
        BufferedReader readEmployeeList = fileReader(fullEmployeeList);
        
        boolean firstLine = true;
        for (String employeeList = readEmployeeList.readLine(); employeeList != null; employeeList = readEmployeeList.readLine()) {
            if (!firstLine) {
                String[] employeeInfo = employeeList.split(sp);
                String emplyId = employeeInfo[0];
                
                if (emplyId.equals(employeeID)) {
                    readEmployeeList.close();
                    return employeeInfo;
                }
            }
            
            firstLine = false;
        }
        readEmployeeList.close();
        
        return null;
    }
    
    public String[] getCurrentBE(String currentBEid) throws IOException {
        BufferedReader readUserProfile = fileReader(userProfile);
        
        boolean firstLine = true;
        for (String userLine = readUserProfile.readLine(); userLine != null; userLine = readUserProfile.readLine()) {
            if (!firstLine) {
                String[] userDetails = userLine.split(sp);
                String userID = userDetails[0];
                if (userID.equals(currentBEid)) {
                    readUserProfile.close();
                    return userDetails;
                }
            }
            
            firstLine = false;
        }
        readUserProfile.close();
        
        return null;
    }
    
    public ArrayList getAssignedJobForSpecificEmployee(String employeeId) throws IOException {
        BufferedReader readJobFile = fileReader(employeeJobFile);
        ArrayList<String> employeeJobList = new ArrayList<>();
        
        for (String jobLine = readJobFile.readLine(); jobLine != null; jobLine = readJobFile.readLine()) {
            String[] jobDetails = jobLine.split(sp);
            String emplyID = jobDetails[1];
            
            if (emplyID.equals(employeeId)) {
                employeeJobList.add(jobLine);
            }
        }
        readJobFile.close();
        
        return employeeJobList;
    }
    
    public String[] getSpecificJobDetails(String employeeId, String jobId) throws IOException {
        BufferedReader readJobFile = fileReader(employeeJobFile);
        
        for (String jobLine = readJobFile.readLine(); jobLine != null; jobLine = readJobFile.readLine()) {
            String[] jobDetails = jobLine.split(sp);
            String jobID = jobDetails[0];
            String emplyID = jobDetails[1];
            
            if (jobID.equals(jobId) && emplyID.equals(employeeId)) {
                return jobDetails;
            }
        }
        readJobFile.close();
        
        return null;
    }
    
    public ArrayList getAvailableJobs(String employeeId, String complaintId) throws IOException {
        BufferedReader readJobList = fileReader(jobListFile);
        ArrayList<String> jobLists = new ArrayList<>();
        
        boolean firstLine = true;
        for (String jobLine = readJobList.readLine(); jobLine != null; jobLine = readJobList.readLine()) {
            if (!firstLine) {
                String[] jobDetails = jobLine.split(sp);
                String roleCode = jobDetails[0];
                String role = employeeId.substring(0, 3);

                if (roleCode.equals(role)) {
                    jobLists.add(jobLine);
                }

                if ((roleCode.equals("cmp")) && (complaintId != null)) {
                    jobLists.add(jobLine);
                }
            }
            
            firstLine = false;
        }
        readJobList.close();
        
        return jobLists;
    }
    
    public String getNewId(File fileName, int idColumn) throws IOException {
        BufferedReader readFile = fileReader(fileName);
        
        String jobIdCode = "tsk";
        int largestId = 114560;        
        
        boolean firstLine = true;
        for (String fileLine = readFile.readLine(); fileLine != null; fileLine = readFile.readLine()){
            if (!firstLine) {
                String[] lineDetails = fileLine.split(sp);
                String id = lineDetails[idColumn];

                id = id.replace(jobIdCode, "");
                int intId = Integer.valueOf(id);

                if (intId > largestId) {
                    largestId = intId;
                }
            }
            
            firstLine = false;
        }readFile.close();
        
        largestId += 1;
        
        return jobIdCode + largestId;
    }
    
    public String getDateTimeNow() {
        LocalDate dateNow = formatDate(String.valueOf(LocalDate.now()));
        LocalTime timeNow = formatTime(String.valueOf(LocalTime.now()));
        
        return String.valueOf(dateNow + " " + timeNow);
    }
    
    public LocalDateTime combineStringDateTime(String date, String time) {
        LocalDate dateLocal = formatDate(date);
        LocalTime timeLocal = formatTime(time);
        
        LocalDateTime combinedLocalDT = LocalDateTime.of(dateLocal, timeLocal);
        
        return combinedLocalDT;
    }
    
    public boolean checkOvernight(LocalTime timeEndExpected, String expectedTimeNeeded) {
        String timePlace = expectedTimeNeeded.substring(expectedTimeNeeded.length()-1);
        int timeNeeded = Integer.valueOf(expectedTimeNeeded.substring(0, expectedTimeNeeded.length()-1));
            
        if (timePlace.equals("h")) {
            timeNeeded*=60;
        }
            
        LocalDate simplyDate = LocalDate.parse("2000-01-02");
        LocalDateTime dateTimeCombine = LocalDateTime.of(simplyDate, timeEndExpected);
        LocalDateTime startDateTime = dateTimeCombine.minusMinutes(timeNeeded);
        
        return startDateTime.toLocalDate().isBefore(simplyDate);
    }
    
    public void setTableRow(DefaultTableModel table, ArrayList arrayList) {
        table.setRowCount(0);
        
        for (int rowCount = 0; rowCount < arrayList.size(); rowCount++) {
            String[] rowDetails = String.valueOf(arrayList.get(rowCount)).split(sp);
            table.addRow(rowDetails);
        }
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
            page = new BuildingExecutiveJobManagement(this.getUserID());
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
    
    public void toEmployeeJobAssignation(String beID, String employeeID, String jobID) {
        EmployeeJobAssignation EJA;
        try {
            EJA = new EmployeeJobAssignation(beID, employeeID, jobID);
            EJA.setVisible(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void toJobModificationPage(String positionCode) throws IOException {
        JobModificationPage page = new JobModificationPage(positionCode);
        page.setVisible(true);
    }
}

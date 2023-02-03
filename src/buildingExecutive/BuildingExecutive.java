/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buildingExecutive;

import pms_parkhill_residence.CRUD;
import java.io.File;
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
import pms_parkhill_residence.Complaints;
import pms_parkhill_residence.FileHandling;
import pms_parkhill_residence.PMS_DateTimeFormatter;
import pms_parkhill_residence.TextFiles;
import pms_parkhill_residence.Users;

/**
 *
 * @author Winson
 */
public class BuildingExecutive extends Users{
    PMS_DateTimeFormatter DTF = new PMS_DateTimeFormatter();
    TextFiles TF = new TextFiles();
    FileHandling fileHandling = new FileHandling();
    Complaints CP = new Complaints();
    
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
    
    private ArrayList getEmployeeJobList(LocalDate localDate, LocalTime localTime) throws IOException {
        List<String> employeeList = fileHandling.fileRead(TF.fullEmployeeList);
        
        ArrayList<String> workingList = new ArrayList<>();
        ArrayList<String> assEmply = new ArrayList<>();
        ArrayList<String> unassEmply = new ArrayList<>();
        ArrayList<ArrayList> assignedANDunassigned = new ArrayList<>();

        // Get the day of the date parsed
        String dayOfWeek = localDate.getDayOfWeek().toString().toLowerCase();
        
        ArrayList<String> historyList = new ArrayList<>();
        ArrayList<String> updateJobList = new ArrayList<>();
        List<String> oldVer = fileHandling.fileRead(TF.employeeJobFile);
        boolean firstLine = true;
        for (String eachLine : oldVer) {
            if (firstLine) {
                updateJobList.add(eachLine);
            }
            else {
                String[] eachData = eachLine.split(TF.sp
                );
                String[] endDateTime = eachData[8].split(" ");
                if (!endDateTime[0].equals("null")) {
                    if (combineStringDateTime(endDateTime[0], endDateTime[1]).isBefore(LocalDateTime.now())){
                        String emplyId = eachData[1];
                        String emplyName = getEmployeeDetails(emplyId)[2];
                        historyList.add(eachData[0] + TF.sp
                                + eachData[1] + TF.sp
                                + emplyName + TF.sp
                                + eachData[2] + TF.sp
                                + eachData[3] + TF.sp
                                + eachData[4] + TF.sp
                                + eachData[5] + TF.sp
                                + eachData[6] + TF.sp
                                + eachData[7] + TF.sp
                                + eachData[8] + TF.sp
                                + eachData[9] + TF.sp
                                + eachData[10] + TF.sp
                                + eachData[11] + TF.sp
                                + eachData[12] + TF.sp
                                + eachData[13] + TF.sp);
                    }
                    else {
                        updateJobList.add(eachLine);
                    }
                }
                else {
                    updateJobList.add(eachLine);
                } 
            }
            
            firstLine = false;
        }
        
        fileHandling.fileWrite(TF.jobFileHistory, true, historyList);
        fileHandling.fileWrite(TF.employeeJobFile, false, updateJobList);
        
        List<String> jobFile = fileHandling.fileRead(TF.employeeJobFile);
        firstLine = true;
        for (String jobFileLine : jobFile) {
            if (!firstLine) {
                String[] jobLineDetails = jobFileLine.split(TF.sp);
                int repitition = Integer.valueOf(jobLineDetails[4]);
                String workingEmplyId = jobLineDetails[1];
                
                String timeNeeded = jobLineDetails[5];
                LocalDate workingDate = null;
                LocalTime workingTime = formatTime(jobLineDetails[7]);
                String[] workingEndDateTime = jobLineDetails[8].split(" ");
                
                LocalTime workingStartTime2 = null;
                LocalTime workingEndTime2 = null;
                
                ArrayList<String> dateData = compareJobDate(repitition, workingEndDateTime, jobLineDetails, timeNeeded, dayOfWeek, localDate, workingTime, workingDate);
                
                if (!dateData.isEmpty()){
                    workingDate = formatDate(dateData.get(0));
                    workingTime = formatTime(dateData.get(1));

                    workingStartTime2 = (!dateData.get(2).equals("null")) ? formatTime(dateData.get(2)) : null;
                    workingEndTime2 = (!dateData.get(3).equals("null")) ? formatTime(dateData.get(3)) : null;

                    workingEndDateTime = dateData.get(4).split(" ");
                }
                
                if (workingDate != null) {
                    boolean addToList = compareDateTime(localDate, localTime, workingDate, workingTime, workingEndDateTime, workingStartTime2, workingEndTime2);

                    if (addToList) {
                        if (!workingList.contains(workingEmplyId)) {
                            workingList.add(workingEmplyId);

                            String[] employeeDetails = getEmployeeDetails(workingEmplyId);
                            String workingEmplyName = employeeDetails[2];
                            String workingEmplyPos = employeeDetails[4];
                            String jobId = jobLineDetails[0];
                            String assignedJobCode = jobLineDetails[3];

                            String jobDetails = findJobDetailsUsingDescriptionOrId(assignedJobCode, null);
                            String jobDesc = jobDetails.split(TF.sp)[2];

                            assEmply.add(workingEmplyId + TF.sp
                                    + workingEmplyName + TF.sp
                                    + workingEmplyPos + TF.sp
                                    + jobId + TF.sp
                                    + jobDesc + TF.sp
                                    + "View" + TF.sp);
                        }
                    }
                }
            }
            
            firstLine = false;
        }
        
        firstLine = true;
        for (String eachEmployee : employeeList) {
            if (!firstLine) {
                String[] employeeInfo = eachEmployee.split(TF.sp
                );
                String emplyId = employeeInfo[0];
                String emplyName = employeeInfo[2];
                String emplyPos = employeeInfo[4];
                
                if (!workingList.contains(emplyId)) {
                    unassEmply.add(emplyId + TF.sp
                            + emplyName + TF.sp
                            + emplyPos + TF.sp
                            + "Assign" + TF.sp);
                }
            }
            
            firstLine = false;
        }
        
        assignedANDunassigned.add(unassEmply);
        assignedANDunassigned.add(assEmply);
        
        return assignedANDunassigned;
    }
    
    public ArrayList compareJobDate(int repitition, String[] workingEndDateTime, String[] jobDetails,String timeNeeded, String dayOfWeek, LocalDate localDate, LocalTime workingTime, LocalDate workingDate){
        ArrayList<String> dateData = new ArrayList<>();
        
        LocalTime workingStartTime2 = null;
        LocalTime workingEndTime2 = null;
        
        if (repitition == repititionON) {
            int overnightDay = checkOvernight(formatTime(workingEndDateTime[1]), timeNeeded);
            boolean isOvernight = (overnightDay != 0);
            ArrayList<String> dayToRepeat = new ArrayList<>(Arrays.asList(jobDetails[9].split(",")));

            boolean foundToday = false;
            if (dayToRepeat.contains(dayOfWeek)) {
                foundToday = true;
            }

            boolean foundYesterday = false;
            if (isOvernight) {
                if (dayToRepeat.contains(localDate.getDayOfWeek().plus(overnightDay).toString().toLowerCase())) {
                    foundYesterday = true;
                }
            }

            if (isOvernight && foundYesterday || 
                isOvernight && foundToday || 
                isOvernight && foundYesterday ||
                !isOvernight && foundToday && !foundYesterday) 
            {
                workingDate = localDate;
                workingEndDateTime[0] = localDate.toString();
            }

            if (isOvernight && foundToday && foundYesterday) {
                workingStartTime2 = workingTime;
                workingTime = LocalTime.parse("00:00:00");
                workingEndTime2 = LocalTime.parse("00:00:00");
            }
            else if (isOvernight && foundToday && !foundYesterday) {
                workingEndDateTime[1] = "00:00:00";
            }
            else if (isOvernight && !foundToday && foundYesterday) {
                workingTime = LocalTime.parse("00:00:00");
            }
        }
        else {
            workingDate = formatDate(jobDetails[6]);
        }
        
        if (workingDate != null ){
            dateData.add(workingDate.toString());
            dateData.add(workingTime.toString());
            dateData.add(String.valueOf(workingStartTime2));
            dateData.add(String.valueOf(workingEndTime2));
            dateData.add(workingEndDateTime[0] + " " + workingEndDateTime[1]);
        }
        
        return dateData;
    }
    
    public boolean compareDateTime(LocalDate localDate, LocalTime localTime, LocalDate workingDate, LocalTime workingTime, String[] workingEndDateTime, LocalTime workingStartTime2, LocalTime workingEndTime2) {
        LocalDateTime selectedDateTime = LocalDateTime.of(localDate, localTime);
        LocalDateTime startDateTime = LocalDateTime.of(workingDate, workingTime);
        LocalDateTime endDateTime = LocalDateTime.of(formatDate(workingEndDateTime[0]), formatTime(workingEndDateTime[1]));

        boolean addToList = false;
        if ((selectedDateTime.equals(startDateTime) || selectedDateTime.isAfter(startDateTime)) && 
            (selectedDateTime.equals(endDateTime) || selectedDateTime.isBefore(endDateTime))) {
            addToList = true;
        }
        else if (workingStartTime2 != null && workingEndTime2 != null) {
            LocalDateTime startDateTime2 = LocalDateTime.of(workingDate, workingStartTime2);
            LocalDateTime endDateTime2 = LocalDateTime.of(workingDate.plusDays(1), workingEndTime2);

            if ((selectedDateTime.equals(startDateTime2) || selectedDateTime.isAfter(startDateTime2)) &&
                (selectedDateTime.equals(endDateTime2) || selectedDateTime.isBefore(endDateTime2))) {
                addToList = true;
            }
        }
        
        return addToList;
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
            String[] employeeDetails = employeeInfo.split(TF.sp);
            
            if (employeeDetails[2].equals(role)) {
                specificRoleList.add(employeeInfo);
            }
        }
        
        return specificRoleList;
    }
    
    private ArrayList searchFunction(ArrayList<String> employeeList, String searchText) {
        ArrayList<String> searchedList = new ArrayList<>();
        
        for (String employeeInfo : employeeList) {
            String[] employeeDetails = employeeInfo.split(TF.sp);
            
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
            String data = (String) table.getValueAt(selectedRow, getValueColumn);
            return data;
        }
        
        return null;
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
        if (timeMin > 30) {
            time = time.withMinute(00);
            time = time.plusHours(1);
        }
        else {
            time = time.withMinute(30);
        }
        
        return time;
    }
    
    public String[] getEmployeeDetails(String employeeID) throws IOException {
        List<String> readEmployeeList = fileHandling.fileRead(TF.fullEmployeeList);
        
        boolean firstLine = true;
        for (String employeeList : readEmployeeList) {
            if (!firstLine) {
                String[] employeeInfo = employeeList.split(TF.sp);
                String emplyId = employeeInfo[0];
                
                if (emplyId.equals(employeeID)) {
                    return employeeInfo;
                }
            }
            
            firstLine = false;
        }
        
        return null;
    }
    
    public ArrayList getAssignedJobForSpecificEmployee(String employeeId) throws IOException {
        List<String> readJobFile = fileHandling.fileRead(TF.employeeJobFile);
        ArrayList<String> employeeJobList = new ArrayList<>();
        
        for (String jobLine : readJobFile) {
            String[] jobDetails = jobLine.split(TF.sp);
            String emplyID = jobDetails[1];
            
            if (emplyID.equals(employeeId)) {
                employeeJobList.add(jobLine);
            }
        }
        
        return employeeJobList;
    }
    
    public String[] getSpecificJobDetails(String employeeId, String jobId) throws IOException {
        List<String> readJobFile = fileHandling.fileRead(TF.employeeJobFile);
        
        for (String jobLine : readJobFile) {
            String[] jobDetails = jobLine.split(TF.sp);
            String jobID = jobDetails[0];
            String emplyID = jobDetails[1];
            
            if (jobID.equals(jobId) && emplyID.equals(employeeId)) {
                return jobDetails;
            }
        }
        
        return null;
    }
    
    public String findJobDetailsUsingDescriptionOrId(String jobCode, String jobDesc) {
        List<String> jobList = fileHandling.fileRead(TF.jobListFile);
        for (String eachJob : jobList) {
            String jobId = eachJob.split(TF.sp)[1];
            String description = eachJob.split(TF.sp)[2];
            
            if (jobCode != null) {
                if (jobId.equals(jobCode)) {
                    return eachJob;
                }
            }
            
            if (jobDesc != null) {
                if (jobDesc.equals(description)) {
                    return eachJob;
                }
            }
            
        }
        
        return null;
    }
    
    public ArrayList getAvailableJobs(String employeeId, String complaintId) throws IOException {
        List<String> readJobList = fileHandling.fileRead(TF.jobListFile);
        ArrayList<String> jobLists = new ArrayList<>();
        
        boolean firstLine = true;
        for (String jobLine : readJobList) {
            if (!firstLine) {
                String[] jobDetails = jobLine.split(TF.sp);
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
        
        return jobLists;
    }
    
    public String getNewTaskId(String fileName, int idColumn) throws IOException {
        List<String> readFile = fileHandling.fileRead(fileName);
        
        String jobIdCode = "tsk";
        int largestId = 114560;        
        
        boolean firstLine = true;
        for (String fileLine : readFile){
            if (!firstLine) {
                String[] lineDetails = fileLine.split(TF.sp);
                String id = lineDetails[idColumn];

                id = id.replace(jobIdCode, "");
                int intId = Integer.valueOf(id);

                if (intId > largestId) {
                    largestId = intId;
                }
            }
            
            firstLine = false;
        }
        
        largestId += 1;
        
        return jobIdCode + largestId;
    }
    
    public String getNewId(String fileName, int idColumn) {
        List<String> scheRec = fileHandling.fileRead(fileName);
        
        int largestId = 0;
        
        boolean firstLine = true;
        for (String eachRec : scheRec) {
            if (!firstLine) {
                String checkId = eachRec.split(TF.sp)[idColumn];
                
                for (int check = 0; check < checkId.length(); check++) {
                    if (Character.isDigit(checkId.charAt(check))) {
                        int id = Integer.valueOf(eachRec.split(TF.sp)[idColumn]);
                        largestId = (id > largestId) ? id : largestId;
                    }
                }
            }
            
            firstLine = false;
        }
        
        return String.valueOf(largestId + 1);
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
    
    public int checkOvernight(LocalTime timeEndExpected, String expectedTimeNeeded) {
        String timePlace = expectedTimeNeeded.substring(expectedTimeNeeded.length()-1);
        int timeNeeded = Integer.valueOf(expectedTimeNeeded.substring(0, expectedTimeNeeded.length()-1));
            
        if (timePlace.equals("h")) {
            timeNeeded*=60;
        }
            
        LocalDate simplyDate = LocalDate.parse("2000-01-02");
        LocalDateTime dateTimeCombine = LocalDateTime.of(simplyDate, timeEndExpected);
        LocalDateTime startDateTime = dateTimeCombine.minusMinutes(timeNeeded);
        
        return startDateTime.toLocalDate().compareTo(simplyDate);
    }
    
    public void setTableRow(DefaultTableModel table, ArrayList arrayList) {
        table.setRowCount(0);
        
        for (int rowCount = 0; rowCount < arrayList.size(); rowCount++) {
            String[] rowDetails = String.valueOf(arrayList.get(rowCount)).split(TF.sp
            );
            table.addRow(rowDetails);
        }
    }
    
    public void fileCleaner(File fileName) throws IOException {
        PrintWriter pw = new PrintWriter(fileName);
        pw.flush();
    }
    
    public void updateComplaintStatus(Complaints complaints) {
        String[] complaintDetails = complaints.toString().split(TF.sp);
        
        String complaint = "";
        for (String eachData : complaintDetails) {
            complaint += eachData + TF.sp;
        }
        
        CRUD crud = new CRUD();
        crud.update(TF.complaintFiles, complaintDetails[0], complaint, 0);
    }
    
    public ArrayList getAvailableBlock() {
        ArrayList<String> blockList = new ArrayList<>();
        List<String> getBlock = fileHandling.fileRead("propertyDetails.txt");
        
        boolean firstLine = true;
        for (String eachLine : getBlock) {
            if (!firstLine) {
                String unitCode = eachLine.split(TF.sp)[0];
                unitCode = unitCode.substring(0, 1);
                if (!blockList.contains(unitCode)) {
                    blockList.add(unitCode);
                }
            }
            
            firstLine = false;
        }
        
        return blockList;
    }
    
    public void tableSettingUpdate(String timeIntervalSet, String levelIntervalSet, boolean resetDefault) {
        ArrayList<String> blockList = getAvailableBlock();
        
        if (blockList.contains("S")) {
            blockList.remove("S");
        }
        
        ArrayList<String> newSched = new ArrayList<>();
        LocalTime tempTime = LocalTime.of(0, 0, 0);
        
        int timeSequence = (24%Integer.valueOf(timeIntervalSet)!=0) ? (24/Integer.valueOf(timeIntervalSet) + 1) : (24/Integer.valueOf(timeIntervalSet));
    
        ArrayList<String> levelSequence = getLevelSequence(Integer.valueOf(levelIntervalSet));
        
        List<String> toGetHeader = fileHandling.fileRead(TF.fixFile);
        newSched.add(toGetHeader.get(0));
        
        int newNo = 1;
        for (int time = 0; time < timeSequence; time++ ) {
            String thisTime = tempTime.toString();
            for (int block = 0; block < blockList.size(); block++) {
                String currentBlock = blockList.get(block);
                for (String eachSeq : levelSequence) {
                    String[] levelANDcheck = eachSeq.split(TF.sp);
                    newSched.add(newNo + TF.sp
                            + thisTime + TF.sp
                            + currentBlock + TF.sp
                            + levelANDcheck[0] + TF.sp
                            + levelANDcheck[1] + TF.sp
                            + formatTime(thisTime).plusHours(1).toString() + TF.sp
                            + " " + TF.sp
                            + " " + TF.sp
                            + " " + TF.sp
                            + " " + TF.sp
                            + " " + TF.sp
                            + " " + TF.sp);
                    newNo++;
                }
            }
            
            newSched.add(newNo++ + TF.sp
                    + thisTime + TF.sp
                    + "S" + TF.sp
                    + "Level 1-2" + TF.sp
                    + "Level 2" + TF.sp
                    + formatTime(thisTime).plusHours(1).toString() + TF.sp
                    + " " + TF.sp
                    + " " + TF.sp
                    + " " + TF.sp
                    + " " + TF.sp
                    + " " + TF.sp
                    + " " + TF.sp);
            
            tempTime = tempTime.plusHours(Integer.valueOf(timeIntervalSet));
        }
        
        fileHandling.fileWrite(TF.tempFile, false, newSched);
        
        if (resetDefault) {
            File rename = new File(TF.tempFile);
            if (new File(TF.fixFile).exists()) {
                new File(TF.fixFile).delete();
            }
            rename.renameTo(new File(TF.fixFile));
        }
    }
    
    private ArrayList getLevelSequence(int selectedLevel) {
        ArrayList<String> levelValue = new ArrayList<>();
        switch (selectedLevel) {
            case 5 -> {
                levelValue.add("Level 1-5;Level 5");
                levelValue.add("Level 6-10;Level 10");
                levelValue.add("Level 11-15;Level 15");
            }
            case 10 -> {
                levelValue.add("Level 1-10;Level 10");
                levelValue.add("Level 10-15;Level 15");
            }
            case 15 -> {
                levelValue.add("Level 1-15;Level 15");
            }
        }
        
        return levelValue;
    }
    
    // Change Page Method
    public void toDashboard(JFrame frame, Users user) {
            BuildingExecutiveMainPage page;
            page = new BuildingExecutiveMainPage(user);
            page.setVisible(true);
            frame.dispose();
    }
    
    public void toJobManagement(JFrame frame, Users user, Complaints complaint, boolean fromComplaintPage) {
        try {
            BuildingExecutiveJobManagement page;
            page = new BuildingExecutiveJobManagement(user, complaint, fromComplaintPage);
            page.setVisible(true);
            frame.dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void toComplaints(JFrame frame, Users users) {
        try {
            BuildingExecutiveComplaints page;
            page = new BuildingExecutiveComplaints(users);
            page.setVisible(true);
            frame.dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void toPatrollingManagement(JFrame frame, Users users) throws IOException {
        BuildingExecutivePatrollingManagement page;
        page = new BuildingExecutivePatrollingManagement(users);
        page.setVisible(true);
        frame.dispose();
    }
    
    public void toBEreports(JFrame frame) {
        BuildingExecutiveReports page;
        page = new BuildingExecutiveReports();
        page.setVisible(true);
        frame.dispose();
    }
    
    public void toEmployeeJobAssignation(Users user, String employeeID, String jobID, Complaints complaint, boolean fromComplaintPage) {
        EmployeeJobAssignation EJA;
        try {
            EJA = new EmployeeJobAssignation(user, employeeID, jobID, complaint, fromComplaintPage);
            EJA.setVisible(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void toJobModificationPage(Users users, String positionCode, String jobID, Complaints complaint, String employeeID) throws IOException {
        JobModificationPage page = new JobModificationPage(users, positionCode, jobID, complaint, employeeID);
        page.setVisible(true);
    }
    
    public void toComplaintDetailsPage(Users users, Complaints complaint) {
        ComplaintsDetails page = new ComplaintsDetails(users, complaint);
        page.setVisible(true);
    }
    
    public void toScheduleModification(Users users, String file, LocalDate inputDate) {
        PatrollingScheduleModification page = new PatrollingScheduleModification(users, file, inputDate);
        page.setVisible(true);
    }
}

enum cptStatus{
    Pending,
    Progressing,
    Complete,
}
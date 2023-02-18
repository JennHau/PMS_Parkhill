/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buildingExecutive;

import classes.AssignedJob;
import java.awt.Color;
import classes.CRUD;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import classes.Complaint;
import classes.Employee;
import classes.FileHandling;
import classes.PMS_DateTimeFormatter;
import classes.TextFile;
import classes.Users;

/**
 *
 * @author Winson
 */
public class BuildingExecutive extends Users{
    PMS_DateTimeFormatter DTF = new PMS_DateTimeFormatter();
    TextFile TF = new TextFile();
    FileHandling fh = new FileHandling();
    Complaint CP = new Complaint();
    CRUD crud = new CRUD();
    
    // Repitition
    final int repititionON = 1;
    final int repititionOFF = 0;
    
    // Assign and Unassign Job to Employee
    final int assignedEmployee = 1;
    final int unassignedEmployee = 0;
    
    public BuildingExecutive(String userID, String email, String password, String firstName,
                 String lastName, String identificationNo, String gender,
                 String phoneNo) {
        
        super(userID, email, password, firstName,
             lastName, identificationNo, gender,
             phoneNo);
    }
    
    public ArrayList<Employee> getEmployeeList() {
        ArrayList<Employee> empList = new ArrayList<>();
        
        List<String> employeeList = fh.fileRead(TF.fullEmployeeList);
        
        boolean firstLine = true;
        for (String eachEmp : employeeList) {
            if (!firstLine) {
                Employee emp = new Employee(eachEmp.split(TF.sp));
                empList.add(emp);
            }
            
            firstLine = false;
        }
        
        return empList;
    }
    
    public void updateJobList() throws IOException{
        // Remove past job and replace to another file
        ArrayList<String> historyList = new ArrayList<>();
        ArrayList<String> updateJobList = new ArrayList<>();
        List<String> oldVer = fh.fileRead(TF.employeeJobFile);
        
        boolean firstLine = true;
        for (String eachLine : oldVer) {
            if (firstLine) {
                updateJobList.add(eachLine);
            }
            else {
                String[] eachData = eachLine.split(TF.sp);
                String[] endDateTime = eachData[8].split(" ");
                if (!endDateTime[0].equals(TF.empty)) {
                    if (DTF.combineStringDateTime(endDateTime[0], endDateTime[1]).isBefore(LocalDateTime.now())){
                        String emplyId = eachData[1];
                        Employee employee = new Employee(emplyId);
                        historyList.add(eachData[0].toUpperCase() + TF.sp
                                + eachData[1] + TF.sp
                                + employee.getEmpName() + TF.sp
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
        
        fh.fileWrite(TF.jobFileHistory, true, historyList);
        fh.fileWrite(TF.employeeJobFile, false, updateJobList);
    }
    
    // To get all employee job list
    private ArrayList<ArrayList<String>> getEmployeeJobList(LocalDate localDate, LocalTime localTime) throws IOException {
        ArrayList<Employee> employeeList = getEmployeeList();
        
        ArrayList<String> workingList = new ArrayList<>();
        ArrayList<String> assEmply = new ArrayList<>();
        ArrayList<String> unassEmply = new ArrayList<>();
        
        ArrayList<ArrayList<String>> assignedANDunassigned = new ArrayList<>();

        // Get the day of the date parsed
        String dayOfWeek = localDate.getDayOfWeek().toString().toLowerCase();
        
        // update job file by removing completed job
        updateJobList();
        
        // Get all the ongoing/future job list from the job file
        List<String> jobFile = fh.fileRead(TF.employeeJobFile);
        boolean firstLine = true;
        for (String jobFileLine : jobFile) {
            if (!firstLine) {
                AssignedJob jobLineDetails = new AssignedJob(jobFileLine.split(TF.sp));
                
                int repitition = jobLineDetails.getRepetition();
                
                Employee workingEmp = new Employee(jobLineDetails.getTaskEmpID());
                
                String timeNeeded = jobLineDetails.getTimeNeeded();
                LocalDate workingDate = null;
                LocalTime workingTime = DTF.formatTime(jobLineDetails.getStartTime());
                String[] workingEndDateTime = jobLineDetails.getExpectedEndDateTime().split(" ");
                
                LocalTime workingStartTime2 = null;
                LocalTime workingEndTime2 = null;
                
                // check the job is repeated or overnight
                ArrayList<String> dateData = compareJobDate(repitition, workingEndDateTime, jobLineDetails, timeNeeded, dayOfWeek, localDate, workingTime, workingDate);
                
                // assign value to the working data and time according to the comparedJobDate method
                if (!dateData.isEmpty()){
                    workingDate = DTF.formatDate(dateData.get(0));
                    workingTime = DTF.formatTime(dateData.get(1));

                    workingStartTime2 = (!dateData.get(2).equals("null")) ? DTF.formatTime(dateData.get(2)) : null;
                    workingEndTime2 = (!dateData.get(3).equals("null")) ? DTF.formatTime(dateData.get(3)) : null;

                    workingEndDateTime = dateData.get(4).split(" ");
                }
                
                // If the working date is not null
                if (workingDate != null) {
                    // Compare the job with inputted date and time
                    boolean addToList = compareDateTime(localDate, localTime, workingDate, workingTime, workingEndDateTime, workingStartTime2, workingEndTime2);

                    // Add the job to assigned list
                    if (addToList) {
                        if (!workingList.contains(workingEmp.getEmpID())) {
                            workingList.add(workingEmp.getEmpID());

                            String jobId = jobLineDetails.getTaskID();
                            String jobDesc = jobLineDetails.getTask();

                            assEmply.add(workingEmp.getEmpID().toUpperCase() + TF.sp
                                    + workingEmp.getEmpName() + TF.sp
                                    + workingEmp.getPosition() + TF.sp
                                    + jobId.toUpperCase() + TF.sp
                                    + jobDesc + TF.sp
                                    + "MODIFY" + TF.sp);
                        }
                    }
                }
            }
            
            firstLine = false;
        }
        
        // Get all unassigned employee to the list
        for (Employee eachEmployee : employeeList) {
            String emplyId = eachEmployee.getEmpID();
            String emplyName = eachEmployee.getEmpName();
            String emplyPos = eachEmployee.getPosition();

            if (!workingList.contains(emplyId)) {
                unassEmply.add(emplyId.toUpperCase() + TF.sp
                        + emplyName + TF.sp
                        + emplyPos + TF.sp
                        + "ASSIGN" + TF.sp);
            }
        }
        
        assignedANDunassigned.add(unassEmply);
        assignedANDunassigned.add(assEmply);
        
        return assignedANDunassigned;
    }
    
    // To check whether the job is repeated or overnight
    public ArrayList compareJobDate(int repitition, String[] workingEndDateTime, AssignedJob assignedJob, String timeNeeded, String dayOfWeek, LocalDate localDate, LocalTime workingTime, LocalDate workingDate){
        ArrayList<String> dateData = new ArrayList<>();
        
        LocalTime workingStartTime2 = null;
        LocalTime workingEndTime2 = null;
        
        if (repitition == repititionON) {
            int overnightDay = checkOvernight(DTF.formatTime(workingEndDateTime[1]), timeNeeded);
            boolean isOvernight = (overnightDay != 0);
            ArrayList<String> dayToRepeat = new ArrayList<>(Arrays.asList(assignedJob.getDayToRepeat().split(",")));

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
            workingDate = DTF.formatDate(assignedJob.getStartDate());
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
    
    // Compare the job date time with the input date and time
    public boolean compareDateTime(LocalDate localDate, LocalTime localTime, LocalDate workingDate, LocalTime workingTime, String[] workingEndDateTime, LocalTime workingStartTime2, LocalTime workingEndTime2) {
        LocalDateTime selectedDateTime = LocalDateTime.of(localDate, localTime);
        LocalDateTime startDateTime = LocalDateTime.of(workingDate, workingTime);
        LocalDateTime endDateTime = LocalDateTime.of(DTF.formatDate(workingEndDateTime[0]), DTF.formatTime(workingEndDateTime[1]));

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
    
    // Get the assign or unassigned employee list
    public ArrayList getSpecificStatusEmployeeList(LocalDate localDate, LocalTime localTime, String role, String searchText, int employeeStatus) throws IOException {
        ArrayList<ArrayList<String>> employeeJobList = getEmployeeJobList(localDate, localTime);
        
        ArrayList<String> employeeList = employeeJobList.get(employeeStatus);
        
        if (role != null && !role.equals("All")) {
            employeeList = getSpecificRoleList(employeeList, role);
        }
        
        if (searchText != null && !searchText.equals("")) {
            employeeList = searchFunction(employeeList, searchText);
        }
        
        return employeeList;
    }
    
    // get specific role of employee list
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
    
    // search function
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
    
//    // get employee full role description
//    public String showEmployeeFullRoleName(String role) {
//        String employeeRole = null;
//        
//        switch (role) {
//            case "scg" -> employeeRole = "Security Guard";
//            case "tnc" -> employeeRole = "Technician";
//            case "cln" -> employeeRole = "Cleaner";
//        }
//        
//        return employeeRole;
//    }
    
    // validate table data selection
    public String validateTableSelectionAndGetValue(DefaultTableModel table, int selectedColumn, int selectedRow, int expectedColumn, int getValueColumn) {
        if (selectedColumn == expectedColumn) {
            String data = (String) table.getValueAt(selectedRow, getValueColumn);
            return data;
        }
        
        return null;
    }
    
    // get the specific job details for the specific employee
    public String[] getSpecificJobDetails(String employeeId, String jobId) {
        List<String> readJobFile = fh.fileRead(TF.employeeJobFile);
        
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
    
    // find the job details from the job file
    public String findJobDetailsUsingDescriptionOrId(String jobCode, String jobDesc) {
        List<String> jobList = fh.fileRead(TF.jobListFile);
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
    
    // get new job id
    public String getNewTaskId(String fileName, int idColumn) throws IOException {
        List<String> readFile = fh.fileRead(fileName);
        
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
    
    // get new id (only applicable without alphabet code)
    public String getNewId(String fileName, int idColumn) {
        List<String> scheRec = fh.fileRead(fileName);
        
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
    
    // check is the job overnight
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
    
    // set table row function
    public void setTableRow(DefaultTableModel table, ArrayList arrayList) {
        table.setRowCount(0);
        
        for (int rowCount = 0; rowCount < arrayList.size(); rowCount++) {
            String[] rowDetails = String.valueOf(arrayList.get(rowCount)).split(TF.sp
            );
            table.addRow(rowDetails);
        }
    }
    
    // clear file
    public void fileCleaner(File fileName) throws IOException {
        PrintWriter pw = new PrintWriter(fileName);
        pw.flush();
    }
    
    
    // get all available block from list
    public ArrayList getAvailableBlock() {
        ArrayList<String> blockList = new ArrayList<>();
        List<String> getBlock = fh.fileRead("propertyDetails.txt");
        
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
    
    // update the patrolling schedule (manage schedule function)
    public void tableSettingUpdate(String timeIntervalSet, String levelIntervalSet, int hourRequest, boolean resetDefault) {
        ArrayList<String> blockList = getAvailableBlock();
        
        if (blockList.contains("S")) {
            blockList.remove("S");
        }
        
        ArrayList<String> newSched = new ArrayList<>();
        LocalTime tempTime = LocalTime.of(0, 0, 0);
        
        int timeSequence = (24%Integer.valueOf(timeIntervalSet)!=0) ? (24/Integer.valueOf(timeIntervalSet) + 1) : (24/Integer.valueOf(timeIntervalSet));
    
        ArrayList<String> levelSequence = getLevelSequence(Integer.valueOf(levelIntervalSet));
        
        List<String> toGetHeader = fh.fileRead(TF.fixFile);
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
                            + DTF.formatTime(thisTime).plusHours(hourRequest).toString() + TF.sp
                            + "-" + TF.sp
                            + "-" + TF.sp
                            + "-" + TF.sp
                            + "-" + TF.sp
                            + "-" + TF.sp
                            + "-" + TF.sp);
                    newNo++;
                }
            }
            
            newSched.add(newNo++ + TF.sp
                    + thisTime + TF.sp
                    + "S" + TF.sp
                    + "Level 1-2" + TF.sp
                    + "Level 2" + TF.sp
                    + DTF.formatTime(thisTime).plusHours(hourRequest).toString() + TF.sp
                    + "-" + TF.sp
                    + "-" + TF.sp
                    + "-" + TF.sp
                    + "-" + TF.sp
                    + "-" + TF.sp
                    + "-" + TF.sp);
            
            tempTime = tempTime.plusHours(Integer.valueOf(timeIntervalSet));
        }
        
        fh.fileWrite(TF.tempFile, false, newSched);
        
        if (resetDefault) {
            File rename = new File(TF.tempFile);
            if (new File(TF.fixFile).exists()) {
                new File(TF.fixFile).delete();
            }
            rename.renameTo(new File(TF.fixFile));
        }
    }
    
    // get the level sequence
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
    
    // get all jobs
    public List getAllAssignedJob() {
        List<String> jobList = fh.fileRead(TF.employeeJobFile);
        List<String> completedJobList = fh.fileRead(TF.jobFileHistory);
        
        boolean firstLine = true;
        for (String completedJob : completedJobList) {
            if (!firstLine) {
                String[] jobDet = completedJob.split(TF.sp);
            
                String line = "";
                for (int item = 0; item < jobDet.length; item++) {
                    if (item != 2) {
                        line = line + jobDet[item] + TF.sp;
                    }
                }

                jobList.add(line);
            }
            
            firstLine = false;
        }
        
        return jobList;
    }
    
    // get all the data from the table
    public ArrayList getTableData(DefaultTableModel table) {
        int tableColumnLength = table.getColumnCount();
        int tableRowLength = table.getRowCount();
        
        ArrayList<String> tableData = new ArrayList<>();
//        
//        String header = "";
//        for (int colCount = 0; colCount < tableColumnLength; colCount++) {
//            String headerData = table.getColumnName(colCount);
//            header = header + headerData + TF.sp;
//        }
//        
//        tableData.add(header);
//        
        for (int rowCount = 0; rowCount < tableRowLength; rowCount++) {
            
            String rowData = "";
            for (int colCount = 0; colCount < tableColumnLength; colCount++) {
                String eachData = table.getValueAt(rowCount, colCount).toString();
                rowData = rowData + eachData + TF.sp;
            }
            
            tableData.add(rowData);
        }
        
        return tableData;
    }
    
    // get the complainer data
    public String[] findComplainerDetails(String complainerId) {
        List<String> userProfile = fh.fileRead(TF.userProfile);
        for (String eachUser : userProfile) {
            String[] userData = eachUser.split(TF.sp);
            if (userData[0].equals(complainerId)) {
                return userData;
            }
        }
        
        return null;
    }
    
    // set the table design
    public void setTableDesign(JTable jTable, JLabel jLabel, int[] columnLength, int[] ignoreColumn) {
        // design for the table header
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(13, 24, 42));
        headerRenderer.setHorizontalAlignment(jLabel.CENTER);
        headerRenderer.setForeground(new Color(255, 255, 255));
        for (int i = 0; i < jTable.getModel().getColumnCount(); i++) {
            jTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        ArrayList<Integer> ignoreColumnList = new ArrayList<>();
        for (int i : ignoreColumn) {
            ignoreColumnList.add(i);
        }
        
        // design for the table row
        DefaultTableCellRenderer rowRenderer = new DefaultTableCellRenderer();
        rowRenderer.setHorizontalAlignment(jLabel.CENTER);
        for (int i = 0; i < jTable.getModel().getColumnCount(); i++) {
            if (!ignoreColumnList.contains(i)) {
                jTable.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
            }
        }
        
        TableColumnModel columnModel = jTable.getColumnModel();
        // set first column width of the table to suitable value
        for (int count = 0; count < columnLength.length; count++) {
            columnModel.getColumn(count).setMaxWidth(columnLength[count]);
            columnModel.getColumn(count).setMinWidth(columnLength[count]);
            columnModel.getColumn(count).setPreferredWidth(columnLength[count]);
        }
    }
    
    // Change Page Method
    public void toDashboard(JFrame frame, BuildingExecutive BE) {
            BuildingExecutiveMainPage page;
            page = new BuildingExecutiveMainPage(BE);
            page.setVisible(true);
            frame.dispose();
    }
    public void toJobManagement(JFrame frame, BuildingExecutive BE, Complaint complaint, boolean fromComplaintPage) {
        try {
            BuildingExecutiveJobManagement page;
            page = new BuildingExecutiveJobManagement(BE, complaint, fromComplaintPage);
            page.setVisible(true);
            frame.dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void toComplaints(JFrame frame, BuildingExecutive BE) {
        try {
            BuildingExecutiveComplaints page;
            page = new BuildingExecutiveComplaints(BE);
            page.setVisible(true);
            frame.dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void toPatrollingManagement(JFrame frame, BuildingExecutive BE, LocalDate inputDate) throws IOException {
        BuildingExecutivePatrollingManagement page;
        page = new BuildingExecutivePatrollingManagement(BE, inputDate);
        page.setVisible(true);
        frame.dispose();
    }
    public void toPatrollingReports(JFrame frame, BuildingExecutive BE) {
        BuildingExecutivePatrollingReports page;
        page = new BuildingExecutivePatrollingReports(BE);
        page.setVisible(true);
        frame.dispose();
    }
    public void toComplaintsReports(BuildingExecutive BE) {
        BuildingExecutiveComplaintsReports page = new BuildingExecutiveComplaintsReports(BE);
        page.setVisible(true);
    }
    public void toJobReports(BuildingExecutive BE) {
        BuildingExecutiveJobReports page = new BuildingExecutiveJobReports(BE);
        page.setVisible(true);
    }
    public void toEmployeeJobAssignation(BuildingExecutive BE, String employeeID, String jobID, Complaint complaint, boolean fromComplaintPage) {
        EmployeeJobAssignation EJA;
        try {
            EJA = new EmployeeJobAssignation(BE, employeeID, jobID, complaint, fromComplaintPage);
            EJA.setVisible(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void toJobModificationPage(BuildingExecutive BE, AssignedJob assignedJob, Complaint complaint, Employee employee) {
        JobModificationPage page = new JobModificationPage(BE, assignedJob, complaint, employee);
        page.setVisible(true);
    }
    public void toComplaintDetailsPage(BuildingExecutive BE, Complaint complaint) {
        ComplaintsDetails page = new ComplaintsDetails(BE, complaint);
        page.setVisible(true);
    }
    public void toScheduleModification(BuildingExecutive BE, String file, LocalDate inputDate) {
        PatrollingScheduleModification page = new PatrollingScheduleModification(BE, file, inputDate);
        page.setVisible(true);
    }
    public void toRepetitiveJob(BuildingExecutive BE) {
        BuildingExecutiveRepetitiveJobReports page = new BuildingExecutiveRepetitiveJobReports(BE);
        page.setVisible(true);
    }
    public void toAllReportsPage(BuildingExecutive BE, String reportTitle, ArrayList tableData) {
        BuildingExecutivePatrollingReportPage page = new BuildingExecutivePatrollingReportPage(BE, reportTitle, tableData);
        page.setVisible(true);
    }
    public void toProfile(BuildingExecutive BE) {
        BuildingExecutiveViewProfile page = new BuildingExecutiveViewProfile(BE);
        page.setVisible(true);
    }
}
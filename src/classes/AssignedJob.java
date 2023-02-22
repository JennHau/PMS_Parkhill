/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Winson
 */
public class AssignedJob extends Job{
    private String taskID;
    private String taskEmpID;
    private String complaintID;
    private int repetition;
    private String expectedTimeRequired;
    private String startDate;
    private String startTime;
    private String expectedEndDateTime;
    private String dayToRepeat;
    private String remarks;
    private String updateBy;
    private String updatedTime;
    private String patrolCode;
    
    public final int deleteItem = 0;    
    public final int updateItem = 1;
    public final int addItem = 2;
    
    public AssignedJob(String taskID, String taskEmpID, String complaintID, String jobID, int repetition, String expectedTimeRequired,
                       String startDate, String startTime, String expectedEndDateTime, String dayToRepeat, String remarks,
                       String updateBy, String updatedTime, String patrolCode) 
    {
        super(jobID);
        
        this.taskID = taskID;
        this.taskEmpID = taskEmpID;
        this.complaintID = complaintID;
        this.repetition = repetition;
        this.expectedTimeRequired = expectedTimeRequired;
        this.startDate = startDate;
        this.startTime = startTime;
        this.expectedEndDateTime = expectedEndDateTime;
        this.dayToRepeat = dayToRepeat;
        this.remarks = remarks;
        this.updateBy = updateBy;
        this.updatedTime = updatedTime;
        this.patrolCode = patrolCode;
    }
    
    public AssignedJob(String inputDate, String patID) 
    {
        String patCode = inputDate + " " + patID;
        
        List<String> jobList = FH.fileRead(TF.employeeJobFile);
        for (String eachJob : jobList) {
            String[] jobData = eachJob.split(TF.sp);
            if (jobData[13].equals(patCode)) {
                this.taskID = jobData[0];
                this.taskEmpID = jobData[1];
                this.complaintID = jobData[2];
                this.repetition = Integer.parseInt(jobData[4]);
                this.expectedTimeRequired = jobData[5];
                this.startDate = jobData[6];
                this.startTime = jobData[7];
                this.expectedEndDateTime = jobData[8];
                this.dayToRepeat = jobData[9];
                this.remarks = jobData[10];
                this.updateBy = jobData[11];
                this.updatedTime = jobData[12];
                this.patrolCode = patCode;
                
                this.setJobID(jobData[3]);
            }
        }
    }
    
    public AssignedJob(String taskId) {
        List<String> jobList = FH.fileRead(TF.employeeJobFile);
        for (String eachJob : jobList) {
            String[] jobData = eachJob.split(TF.sp);
            if (jobData[0].equals(taskId)) {
                this.taskID = jobData[0];
                this.taskEmpID = jobData[1];
                this.complaintID = jobData[2];
                this.repetition = Integer.parseInt(jobData[4]);
                this.expectedTimeRequired = jobData[5];
                this.startDate = jobData[6];
                this.startTime = jobData[7];
                this.expectedEndDateTime = jobData[8];
                this.dayToRepeat = jobData[9];
                this.remarks = jobData[10];
                this.updateBy = jobData[11];
                this.updatedTime = jobData[12];
                this.patrolCode = jobData[13];
            }
        }
    }
    
    public AssignedJob(String[] jobData) {
        super(jobData[3]);
        
        this.taskID = jobData[0];
        this.taskEmpID = jobData[1];
        this.complaintID = jobData[2];
        this.repetition = Integer.parseInt(jobData[4]);
        this.expectedTimeRequired = jobData[5];
        this.startDate = jobData[6];
        this.startTime = jobData[7];
        this.expectedEndDateTime = jobData[8];
        this.dayToRepeat = jobData[9];
        this.remarks = jobData[10];
        this.updateBy = jobData[11];
        this.updatedTime = jobData[12];
        this.patrolCode = jobData[13];
    }
    
    // to get all assigned job from the job file
    public ArrayList<AssignedJob> getAllAssignedJob() {
        ArrayList<AssignedJob> assignedJobList = new ArrayList<>();
        
        boolean firstLine = true;
        List<String> jobFile = FH.fileRead(TF.employeeJobFile);
        for (String eachJob : jobFile) {
            if (!firstLine) {
                AssignedJob aJob = new AssignedJob(eachJob.split(TF.sp));
                assignedJobList.add(aJob);
            }
            
            firstLine = false;
        }
        
        return assignedJobList;
    }
    
    // get all assigned job for a specific employee
    public ArrayList<AssignedJob> getAssignedJobForSpecificEmployee(String employeeId) {
        ArrayList<AssignedJob> allJob = getAllAssignedJob();
        ArrayList<AssignedJob> employeeJobList = new ArrayList<>();
        
        for (AssignedJob jobLine : allJob) {
            String emplyID = jobLine.getTaskEmpID();
            
            if (emplyID.equals(employeeId)) {
                employeeJobList.add(jobLine);
            }
        }
        
        return employeeJobList;
    }
    
    // update job text file
    public void updateJobTextFile(int action) throws IOException {
        ArrayList<AssignedJob> assignedJob = getAllAssignedJob();
        List<String> newItemLists = new ArrayList<>();
        
        List<String> empJobFile = FH.fileRead(TF.employeeJobFile);
        String header = empJobFile.get(0);
        newItemLists.add(header);
        
        // make the string[] to an array list
        String specificItem = this.toString();
        
        for (AssignedJob eachJob : assignedJob) {
            String jobID = eachJob.getTaskID();

            if (jobID.equals(this.taskID)) {
                if (action == updateItem) {
                    newItemLists.add(specificItem);
                }
            }
            else {
                newItemLists.add(eachJob.toString());
            }
        }
        
        if (action == addItem) {
            newItemLists.add(specificItem);
        }
        
        FH.fileWrite(TF.employeeJobFile, false, newItemLists);
    }
    
    @Override
    public String toString() {
        String[] eachData = {taskID, taskEmpID, complaintID, this.getJobID(), String.valueOf(repetition), expectedTimeRequired, startDate, startTime, expectedEndDateTime, dayToRepeat, remarks, updateBy, updatedTime, patrolCode};
        
        String toStringData = "";
        for (String each : eachData) {
            toStringData += each + TF.sp;
        }
        
        return toStringData;
    }

    /**
     * @return the taskID
     */
    public String getTaskID() {
        return taskID;
    }

    /**
     * @param taskID the taskID to set
     */
    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    /**
     * @return the taskEmpID
     */
    public String getTaskEmpID() {
        return taskEmpID;
    }

    /**
     * @param taskEmpID the taskEmpID to set
     */
    public void setTaskEmpID(String taskEmpID) {
        this.taskEmpID = taskEmpID;
    }

    /**
     * @return the complaintID
     */
    public String getComplaintID() {
        return complaintID;
    }

    /**
     * @param complaintID the complaintID to set
     */
    public void setComplaintID(String complaintID) {
        this.complaintID = complaintID;
    }

    /**
     * @return the repetition
     */
    public int getRepetition() {
        return repetition;
    }

    /**
     * @param repetition the repetition to set
     */
    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    /**
     * @return the expectedTimeRequired
     */
    public String getExpectedTimeRequired() {
        return expectedTimeRequired;
    }

    /**
     * @param expectedTimeRequired the expectedTimeRequired to set
     */
    public void setExpectedTimeRequired(String expectedTimeRequired) {
        this.expectedTimeRequired = expectedTimeRequired;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the expectedEndDateTime
     */
    public String getExpectedEndDateTime() {
        return expectedEndDateTime;
    }

    /**
     * @param expectedEndDateTime the expectedEndDateTime to set
     */
    public void setExpectedEndDateTime(String expectedEndDateTime) {
        this.expectedEndDateTime = expectedEndDateTime;
    }

    /**
     * @return the dayToRepeat
     */
    public String getDayToRepeat() {
        return dayToRepeat;
    }

    /**
     * @param dayToRepeat the dayToRepeat to set
     */
    public void setDayToRepeat(String dayToRepeat) {
        this.dayToRepeat = dayToRepeat;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the updateBy
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * @param updateBy the updateBy to set
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * @return the updatedTime
     */
    public String getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime the updatedTime to set
     */
    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * @return the patrolCode
     */
    public String getPatrolCode() {
        return patrolCode;
    }

    /**
     * @param patrolCode the patrolCode to set
     */
    public void setPatrolCode(String patrolCode) {
        this.patrolCode = patrolCode;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Winson
 */
public class Job {
    private String roleJob;
    private String jobID;
    private String task;
    private String timeNeeded;
    private String jobStartTime;
    
    FileHandling FH = new FileHandling();
    TextFile TF = new TextFile();
    CRUD crud = new CRUD();
    
    public Job() {}
    
    public Job(String[] jobData) {
        this.roleJob = jobData[0];
        this.jobID = jobData[1];
        this.task = jobData[2];
        this.timeNeeded = jobData[3];
        this.jobStartTime = jobData[4];
    }
    
    public Job(String jobID) {
        List<String> jobList = FH.fileRead(TF.jobListFile);
        
        for (String eachJob : jobList) {
            String[] jobData = eachJob.split(TF.sp);
            String jId = jobData[1];
            if (jId.equals(jobID)) {
                this.roleJob = jobData[0];
                this.jobID = jId;
                this.task = jobData[2];
                this.timeNeeded = jobData[3];
                this.jobStartTime = jobData[4];
            }
        }
    }
    
    public ArrayList<Job> extractAllJob() {
        ArrayList<Job> allJobList = new ArrayList<>();
        
        List<String> jobList = FH.fileRead(TF.jobListFile);
        for (String eachJob : jobList) {
            Job job = new Job(eachJob.split(TF.sp)[1]);
            allJobList.add(job);
        }
        
        return allJobList;
    }
    
    public ArrayList<Job> extractJobForSpecificRole(String roleCode, String complaintId) {
        ArrayList<Job> allJobLists = extractAllJob();
        ArrayList<Job> jobLists = new ArrayList<>();
        
        boolean firstLine = true;
        for (Job jobLine : allJobLists) {
            if (!firstLine) {

                if (roleCode.equals(jobLine.getRoleJob())) {
                    jobLists.add(jobLine);
                }

                if (complaintId != null) {
                    jobLists.add(jobLine);
                }
            }
            
            firstLine = false;
        }
        
        return jobLists;
    }
    
    public void addJob() {
        List<String> newItem = new ArrayList<>();
        newItem.add(toString());
        
        crud.create(TF.jobListFile, newItem);
    }
    
    public void updateJob() {
        crud.update(TF.jobListFile, jobID, toString(), 1);
    }
    
    public void deleteJob() {
        crud.delete(TF.jobListFile, jobID, 1);
        crud.delete(TF.employeeJobFile, jobID, 3);
    }
    
    @Override
    public String toString() {
        String[] jobData = {roleJob, jobID, task, timeNeeded, jobStartTime};
        String jobLine = "";
        for (String eachData : jobData) {
            jobLine = jobLine + eachData + TF.sp;
        }
        
        return jobLine;
    }

    /**
     * @return the roleJob
     */
    public String getRoleJob() {
        return roleJob;
    }

    /**
     * @param roleJob the roleJob to set
     */
    public void setRoleJob(String roleJob) {
        this.roleJob = roleJob;
    }

    /**
     * @return the jobID
     */
    public String getJobID() {
        return jobID;
    }

    /**
     * @param jobID the jobID to set
     */
    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    /**
     * @return the task
     */
    public String getTask() {
        return task;
    }

    /**
     * @param task the task to set
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * @return the timeNeeded
     */
    public String getTimeNeeded() {
        return timeNeeded;
    }

    /**
     * @param timeNeeded the timeNeeded to set
     */
    public void setTimeNeeded(String timeNeeded) {
        this.timeNeeded = timeNeeded;
    }

    /**
     * @return the startTime
     */
    public String getJobStartTime() {
        return jobStartTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setJobStartTime(String startTime) {
        this.jobStartTime = startTime;
    }
}

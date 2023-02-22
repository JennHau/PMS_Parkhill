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

public class Complaint implements Status{    
    private String complaintID;
    private String complainerID;
    private String complaintDetails;
    private String complaintDate;
    private String complaintTime;
    private String complaintStatus;
    private String statusUpdatedBy;
    private String lastUpdateDateTime;
    
    FileHandling FH = new FileHandling();
    TextFile TF = new TextFile();
    CRUD crud = new CRUD();
    
    public Complaint() {}
    
    public Complaint(String complaintId) {
        List<String> complaint = FH.fileRead(TF.complaintFiles);
        for (String eachComp : complaint) {
            String[] compDet = eachComp.split(TF.sp);
            String compId = compDet[0];
            if (compId.equals(complaintId)) {
                this.complaintID = complaintId;
                this.complainerID = compDet[1];
                this.complaintDetails = compDet[2];
                this.complaintDate = compDet[3];
                this.complaintTime = compDet[4];
                this.complaintStatus = compDet[5];
                this.statusUpdatedBy = compDet[6];
                this.lastUpdateDateTime = compDet[7];
            }
        }
    }
    
    public Complaint(String[] compDet) {
        this.complaintID = compDet[0];
        this.complainerID = compDet[1];
        this.complaintDetails = compDet[2];
        this.complaintDate = compDet[3];
        this.complaintTime = compDet[4];
        this.complaintStatus = compDet[5];
        this.statusUpdatedBy = compDet[6];
        this.lastUpdateDateTime = compDet[7];
    }
    
    // update the complaint status method
    @Override
    public void updateStatus() {
        String complaint = toString();
        
        CRUD crud = new CRUD();
        crud.update(TF.complaintFiles, this.complaintID, complaint, 0);
    }
    
    // get all the complaints for a specific residence/tenants
    public List<ArrayList<Complaint>> getComplaints(String currentRTid) {
        List<ArrayList<Complaint>> complaintList = new ArrayList<>();
        ArrayList<Complaint> pendingComp = new ArrayList<>();
        ArrayList<Complaint> progressingComp = new ArrayList<>();
        ArrayList<Complaint> completedComp = new ArrayList<>();
        
        List<String> complaints = FH.fileRead(TF.complaintFiles);
        for (String eachComp : complaints) {
            String[] compDet = eachComp.split(TF.sp);
            
            Complaint complaint = new Complaint(compDet);
            
            if (complaint.getComplainerID().toLowerCase().equals(currentRTid) || currentRTid == null) {
                String status = complaint.getComplaintStatus();
                switch (status) {
                    case "Pending" -> pendingComp.add(complaint);
                    case "Progressing" -> progressingComp.add(complaint);
                    case "Completed" -> completedComp.add(complaint);
                }
            }
        }
        
        complaintList.add(pendingComp);
        complaintList.add(progressingComp);
        complaintList.add(completedComp);
        
        return complaintList;
    }
    
    // convert complaint to table form for residence and vendor
    public ArrayList tableFormForRTandVD(ArrayList<Complaint> complaintList) {
        ArrayList<String> tableForm = new ArrayList<>();
        
        for (Complaint comp : complaintList) {
            String action = "MODIFY";
            
            if (comp.getComplaintStatus().equals(Complaint.cptStatus.Completed.name())) {
                action = "VIEW";
            }
            
            String[] compData = {comp.getComplaintID().toUpperCase(), comp.getComplaintDetails(), comp.getComplaintDate(), comp.getComplaintTime(), comp.getComplaintStatus(), action};
            
            String compList = "";
            for (String eachData : compData) {
                compList = compList + eachData + TF.sp;
            }
            
            tableForm.add(compList);
        }
        
        return tableForm;
    }
    
    // conver complaint to table form for building executive
    public ArrayList tableFormForBE(ArrayList<Complaint> complaintList) {
        ArrayList<String> tableForm = new ArrayList<>();
        
        for (Complaint comp : complaintList) {
            String compLine = "";
            
            String action = "VIEW";
            
            if (comp.getComplaintStatus().equals(Complaint.cptStatus.Progressing.name())) {
                action = "MODIFY";
            }
            
            if (comp.getComplaintStatus().equals(Complaint.cptStatus.Pending.name())) {
                String[] compData = {comp.getComplaintID().toUpperCase(), comp.getComplainerID().toUpperCase(), comp.complaintDate, comp.complaintStatus, action};
                
                for (String eachData : compData) {
                    compLine = compLine + eachData + TF.sp;
                }
            } 
            else {
                String compData[] = {comp.getComplaintID().toUpperCase(), comp.getComplainerID().toUpperCase(), comp.getComplaintStatus(), comp.getStatusUpdatedBy().toUpperCase(), comp.getLastUpdateDateTime(), action};
                
                for (String eachData : compData) {
                    compLine = compLine + eachData + TF.sp;
                }
            }
            
            tableForm.add(compLine);
        }
        
        return tableForm;
    }
    
    // get new complaint ID
    public String getNewCompId() {
        List<String> compFile = FH.fileRead(TF.complaintFiles);
        
        String compCode = "cmp";
        int minimumId = 450000;
        
        boolean firstLine = true;
        for (String eachComp : compFile) {
            if (!firstLine) {
                String[] compDet = eachComp.split(TF.sp);
            
                int compId = Integer.valueOf(compDet[0].replace(compCode, ""));
                if (compId > minimumId) {
                    minimumId = compId;
                }
            }
            
            firstLine = false;
        }
        
        return compCode + (minimumId + 1);
    }
    
    // update the complaint details
    public void updateComplaint() {
        String compData = this.toString();
        crud.update(TF.complaintFiles, this.complaintID, compData, 0);
    }
    
    // store a newly created complaint
    public void storeNewComplaint() {
        List<String> newData = new ArrayList<>();
        
        String newComp = this.toString();
        
        newData.add(newComp);
        
        crud.create(TF.complaintFiles, newData);
    }
    
    // remove the complaint
    public void removeComplaint() {
        // Remove complaint from complaintFile
        crud.delete(TF.complaintFiles, complaintID, 0);
        
        // Remove complaint job in employee jobFile
        crud.delete(TF.employeeJobFile, complaintID, 2);
    }
    
    
    @Override
    public String toString() {
        String complaint = "";
        
        String[] complaintData = {complaintID, complainerID, complaintDetails, complaintDate, complaintTime, complaintStatus, statusUpdatedBy, lastUpdateDateTime};
        for (String eachData : complaintData) {
            complaint = complaint + eachData + TF.sp;
        }
        
        return complaint;
    }
    
    // types of complaint status
    public enum cptStatus{
        Pending,
        Progressing,
        Completed,
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
     * @return the complainerID
     */
    public String getComplainerID() {
        return complainerID;
    }

    /**
     * @param complainerID the complainerID to set
     */
    public void setComplainerID(String complainerID) {
        this.complainerID = complainerID;
    }

    /**
     * @return the complaintDetails
     */
    public String getComplaintDetails() {
        return complaintDetails;
    }

    /**
     * @param complaintDetails the complaintDetails to set
     */
    public void setComplaintDetails(String complaintDetails) {
        this.complaintDetails = complaintDetails;
    }

    /**
     * @return the complaintStatus
     */
    public String getComplaintStatus() {
        return complaintStatus;
    }

    /**
     * @param complaintStatus the complaintStatus to set
     */
    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    /**
     * @return the complaintDate
     */
    public String getComplaintDate() {
        return complaintDate;
    }

    /**
     * @param complaintDate the complaintDate to set
     */
    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    /**
     * @return the complaintTime
     */
    public String getComplaintTime() {
        return complaintTime;
    }

    /**
     * @param complaintTime the complaintTime to set
     */
    public void setComplaintTime(String complaintTime) {
        this.complaintTime = complaintTime;
    }

    /**
     * @return the statusUpdatedBy
     */
    public String getStatusUpdatedBy() {
        return statusUpdatedBy;
    }

    /**
     * @param statusUpdatedBy the statusUpdatedBy to set
     */
    public void setStatusUpdatedBy(String statusUpdatedBy) {
        this.statusUpdatedBy = statusUpdatedBy;
    }

    /**
     * @return the lastUpdateDateTime
     */
    public String getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    /**
     * @param lastUpdateDateTime the lastUpdateDateTime to set
     */
    public void setLastUpdateDateTime(String lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }
}


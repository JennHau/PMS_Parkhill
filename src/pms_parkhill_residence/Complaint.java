/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Winson
 */

enum cptStatus{
    Pending,
    Progressing,
    Complete
}

public class Complaint {
    
    private String complaintID;
    private String complainerID;
    private String complaintDetails;
    private String complaintDate;
    private String complaintTime;
    private String complaintStatus;
    private String statusUpdatedBy;
    private String lastUpdateDateTime;
    
    FileHandling FH = new FileHandling();
    TextFiles TF = new TextFiles();
    
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
    
    public ArrayList getComplaints(String currentRTid) {
        ArrayList<ArrayList> combinedComp = new ArrayList<>();
        ArrayList<String> pendingComp = new ArrayList<>();
        ArrayList<String> progressingComp = new ArrayList<>();
        ArrayList<String> completedComp = new ArrayList<>();
        
        List<String> complaints = FH.fileRead(TF.complaintFiles);
        for (String eachComp : complaints) {
            String[] compDet = eachComp.split(TF.sp);
            String complainerId = compDet[1];
            if (complainerId.equals(currentRTid)) {
                String status = compDet[5];
                String tableLine = compDet[0].toUpperCase() + TF.sp + compDet[2] + TF.sp + compDet[3] + TF.sp + compDet[4] + TF.sp + status + TF.sp;
                switch (status) {
                    case "Pending" -> pendingComp.add(tableLine);
                    case "Progressing" -> progressingComp.add(tableLine);
                    case "Completed" -> completedComp.add(tableLine);
                }
            }
        }
        
        for (String progress : progressingComp) {
            pendingComp.add(progress);
        }
        
        combinedComp.add(pendingComp);
        combinedComp.add(completedComp);
        
        return combinedComp;
    }
    
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
    
    @Override
    public String toString() {
        String complaint = "";
        
        String[] complaintData = {complaintID, complainerID, complaintDetails, complaintDate, complaintTime, complaintStatus, statusUpdatedBy, lastUpdateDateTime};
        for (String eachData : complaintData) {
            complaint = complaint + eachData + TF.sp;
        }
        
        return complaint;
    }
    
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


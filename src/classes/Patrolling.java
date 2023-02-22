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
public class Patrolling {
    private String patID;
    private String slot;
    private String block;
    private String level;
    private String checkpoints;
    private String checkBefore;
    private String patSecID;
    private String patSecName;
    private String patRemarks;
    private String status;
    private String checkedAt;
    private String updatedBy;
    private String lastUpdate;
    private String currentFile;
    
    FileHandling fh = new FileHandling();
    TextFile TF = new TextFile();
    CRUD crud = new CRUD();
    
    public Patrolling() {}
    
    public Patrolling(String patrollingId, String patSchedule) {
        List<String> patList = fh.fileRead(patSchedule);
        
        for (String eachPat : patList) {
            String[] patDet = eachPat.split(TF.sp);
            
            if (patDet[0].equals(patrollingId)) {
                this.patID = patDet[0];
                this.slot = patDet[1];
                this.block = patDet[2];
                this.level = patDet[3];
                this.checkpoints = patDet[4];
                this.checkBefore = patDet[5];
                this.patSecID = patDet[6];
                this.patSecName = patDet[7];
                this.patRemarks = patDet[8];
                this.status = patDet[9];
                this.checkedAt = patDet[10];
                this.updatedBy = patDet[11];
                this.lastUpdate = patDet[12];
            }
        }
    }
    
    public Patrolling(String[] patDet) {
        this.patID = patDet[0];
        this.slot = patDet[1];
        this.block = patDet[2];
        this.level = patDet[3];
        this.checkpoints = patDet[4];
        this.checkBefore = patDet[5];
        this.patSecID = patDet[6];
        this.patSecName = patDet[7];
        this.patRemarks = patDet[8];
        this.status = patDet[9];
        this.checkedAt = patDet[10];
        this.updatedBy = patDet[11];
        this.lastUpdate = patDet[12];
    }
    
    public ArrayList<Patrolling> extractAllPatrolling(String patrollingScheduleFile) {
        ArrayList<Patrolling> patList = new ArrayList<>();
        
        List<String> patrollingSchedule = fh.fileRead(patrollingScheduleFile);
        
        for (String eachPat : patrollingSchedule) {
            Patrolling pat = new Patrolling(eachPat.split(TF.sp));
            patList.add(pat);
        }
        
        return patList;
    }
    
    public void updatePatrolling(String patScheduleFile) {
        String newData = toString();
        
        crud.update(patScheduleFile, patID, newData, 0);
    }
    
    @Override
    public String toString() {
        String[] patData = {patID, slot, block, level, checkpoints, checkBefore, patSecID, patSecName, patRemarks, status, checkedAt, updatedBy, lastUpdate};
        String patRec = "";
        for (String eachData : patData) {
            patRec = patRec + eachData + TF.sp;
        }
        return patRec;
    }

    /**
     * @return the patID
     */
    public String getPatID() {
        return patID;
    }

    /**
     * @param patID the patID to set
     */
    public void setPatID(String patID) {
        this.patID = patID;
    }

    /**
     * @return the slot
     */
    public String getSlot() {
        return slot;
    }

    /**
     * @param slot the slot to set
     */
    public void setSlot(String slot) {
        this.slot = slot;
    }

    /**
     * @return the block
     */
    public String getBlock() {
        return block;
    }

    /**
     * @param block the block to set
     */
    public void setBlock(String block) {
        this.block = block;
    }

    /**
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * @return the checkpoints
     */
    public String getCheckpoints() {
        return checkpoints;
    }

    /**
     * @param checkpoints the checkpoints to set
     */
    public void setCheckpoints(String checkpoints) {
        this.checkpoints = checkpoints;
    }

    /**
     * @return the checkBefore
     */
    public String getCheckBefore() {
        return checkBefore;
    }

    /**
     * @param checkBefore the checkBefore to set
     */
    public void setCheckBefore(String checkBefore) {
        this.checkBefore = checkBefore;
    }

    /**
     * @return the patSecID
     */
    public String getPatSecID() {
        return patSecID;
    }

    /**
     * @param patSecID the patSecID to set
     */
    public void setPatSecID(String patSecID) {
        this.patSecID = patSecID;
    }

    /**
     * @return the patSecName
     */
    public String getPatSecName() {
        return patSecName;
    }

    /**
     * @param patSecName the patSecName to set
     */
    public void setPatSecName(String patSecName) {
        this.patSecName = patSecName;
    }

    /**
     * @return the patRemarks
     */
    public String getPatRemarks() {
        return patRemarks;
    }

    /**
     * @param patRemarks the patRemarks to set
     */
    public void setPatRemarks(String patRemarks) {
        this.patRemarks = patRemarks;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the checkedAt
     */
    public String getCheckedAt() {
        return checkedAt;
    }

    /**
     * @param checkedAt the checkedAt to set
     */
    public void setCheckedAt(String checkedAt) {
        this.checkedAt = checkedAt;
    }

    /**
     * @return the updatedBy
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @param updatedBy the updatedBy to set
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * @return the lastUpdate
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate the lastUpdate to set
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return the currentFile
     */
    public String getCurrentFile() {
        return currentFile;
    }

    /**
     * @param currentFile the currentFile to set
     */
    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }
    
    
}

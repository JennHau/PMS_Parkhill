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
public class VisitorPass {

    private String passID;
    private String visitorIC;
    private String visitorName;
    private String carPlate;
    private String visitorContact;
    private String date;
    private String time;
    private String checkInStatus;
    private String checkedInAt;
    private String checkedOutAt;
    private String residentId;
    private String lastUpdate;

    private final FileHandling FH = new FileHandling();
    private final TextFile TF = new TextFile();
    
    private final CRUD crud = new CRUD();

    public static String[] visitorPassStatus = {"Registered", "Checked-In", "Checked-Out"};

    public VisitorPass() {
    }

    public VisitorPass(String[] passData) {
        this.passID = passData[0];
        this.visitorIC = passData[1];
        this.visitorName = passData[2];
        this.carPlate = passData[3];
        this.visitorContact = passData[4];
        this.date = passData[5];
        this.time = passData[6];
        this.checkInStatus = passData[7];
        this.checkedInAt = passData[8];
        this.checkedOutAt = passData[9];
        this.residentId = passData[10];
        this.lastUpdate = passData[11];
    }

    public VisitorPass(String passId) {
        List<String> visitorFile = FH.fileRead(TF.visitorPass);

        for (String eachPass : visitorFile) {
            String[] passData = eachPass.split(TF.sp);
            String pId = passData[0];

            if (pId.equals(passId)) {
                this.passID = pId;
                this.visitorIC = passData[1];
                this.visitorName = passData[2];
                this.carPlate = passData[3];
                this.visitorContact = passData[4];
                this.date = passData[5];
                this.time = passData[6];
                this.checkInStatus = passData[7];
                this.checkedInAt = passData[8];
                this.checkedOutAt = passData[9];
                this.residentId = passData[10];
                this.lastUpdate = passData[11];
            }
        }
    }

    public ArrayList<VisitorPass> getCurrentUserRegisteredVisitor(String userID) {
        ArrayList<VisitorPass> registeredVisitor = new ArrayList<>();

        List<String> visitorFiles = FH.fileRead(TF.visitorPass);
        
        boolean firstLine = true;
        for (String eachVis : visitorFiles) {
            if (!firstLine) {
                String[] passData = eachVis.split(TF.sp);
            
                VisitorPass visitorPass = new VisitorPass(passData);

                if (visitorPass.getResidentId().equals(userID)) {
                    registeredVisitor.add(visitorPass);
                }
            }
            
            firstLine = false;
        }
        
        firstLine = true;
        for (String eachVis : visitorFiles) {
            if (!firstLine) {
                String[] passData = eachVis.split(TF.sp);
            
                VisitorPass visitorPass = new VisitorPass(passData);

                if (visitorPass.getResidentId().equals(userID)) {
                    registeredVisitor.add(visitorPass);
                }
            }
            
            firstLine = false;
        }

        return registeredVisitor;
    }

    public void updateVisitorPass() {
        List<String> updateList = new ArrayList<>();

        ArrayList<VisitorPass> visitorPass = getCurrentUserRegisteredVisitor(this.residentId);

        boolean found = false;
        for (VisitorPass eachVis : visitorPass) {
            if (eachVis.getPassID().equals(this.passID)) {
                found = true;
            }
        }

        String passLine = this.toString();

        if (found) {
            crud.update(TF.visitorPass, passID, passLine, 0);
        } else {
            updateList.add(passLine);
            crud.create(TF.visitorPass, updateList);
        }
    }

    public void removeVisitorPass() {
        crud.delete(TF.visitorPass, this.passID, 0);
    }

    public String generateNewPassId() {
        List<String> passFile = FH.fileRead(TF.visitorPass);

        String passCode = "vsp";
        int minimumId = 210000;

        boolean firstLine = true;
        for (String eachPass : passFile) {
            if (!firstLine) {
                String[] passDet = eachPass.split(TF.sp);

                int passId = Integer.valueOf(passDet[0].replace(passCode, ""));
                if (passId > minimumId) {
                    minimumId = passId;
                }
            }

            firstLine = false;
        }

        return passCode + (minimumId + 1);
    }

    @Override
    public String toString() {
        String[] visitorPassData = {passID, visitorIC, visitorName, carPlate, visitorContact, date, time, checkInStatus, checkedInAt, checkedOutAt, residentId, lastUpdate};

        String data = "";
        for (String eachData : visitorPassData) {
            data = data + eachData + TF.sp;
        }

        return data;
    }

    /**
     * @return the passID
     */
    public String getPassID() {
        return passID;
    }

    /**
     * @param passID the passID to set
     */
    public void setPassID(String passID) {
        this.passID = passID;
    }

    /**
     * @return the visitorIC
     */
    public String getVisitorIC() {
        return visitorIC;
    }

    /**
     * @param visitorIC the visitorIC to set
     */
    public void setVisitorIC(String visitorIC) {
        this.visitorIC = visitorIC;
    }

    /**
     * @return the visitorName
     */
    public String getVisitorName() {
        return visitorName;
    }

    /**
     * @param visitorName the visitorName to set
     */
    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    /**
     * @return the carPlate
     */
    public String getCarPlate() {
        return carPlate;
    }

    /**
     * @param carPlate the carPlate to set
     */
    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    /**
     * @return the visitorContact
     */
    public String getVisitorContact() {
        return visitorContact;
    }

    /**
     * @param visitorContact the visitorContact to set
     */
    public void setVisitorContact(String visitorContact) {
        this.visitorContact = visitorContact;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the checkInStatus
     */
    public String getCheckInStatus() {
        return checkInStatus;
    }

    /**
     * @param checkInStatus the checkInStatus to set
     */
    public void setCheckInStatus(String checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    /**
     * @return the checkedInAt
     */
    public String getCheckedInAt() {
        return checkedInAt;
    }

    /**
     * @param checkedInAt the checkedInAt to set
     */
    public void setCheckedInAt(String checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    /**
     * @return the checkedOutAt
     */
    public String getCheckedOutAt() {
        return checkedOutAt;
    }

    /**
     * @param checkedOutAt the checkedOutAt to set
     */
    public void setCheckedOutAt(String checkedOutAt) {
        this.checkedOutAt = checkedOutAt;
    }

    /**
     * @return the residentId
     */
    public String getResidentId() {
        return residentId;
    }

    /**
     * @param residentId the residentId to set
     */
    public void setResidentId(String residentId) {
        this.residentId = residentId;
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
}

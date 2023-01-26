/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package residentANDtenant;

import pms_parkhill_residence.CRUD;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import pms_parkhill_residence.FileHandling;
import pms_parkhill_residence.TextFiles;

/**
 *
 * @author Winson
 */
public class ResidentTenant {
    FileHandling fh = new FileHandling();
    TextFiles TF = new TextFiles();
    CRUD crud = new CRUD();
    
    public String[] visitorPassStatus = {"Registered", "Checked-In", "Checked-Out"};
    
    public void setTableRow(DefaultTableModel table, ArrayList arrayList) {
        table.setRowCount(0);
        
        for (int rowCount = 0; rowCount < arrayList.size(); rowCount++) {
            String[] rowDetails = String.valueOf(arrayList.get(rowCount)).split(TF.sp);
            table.addRow(rowDetails);
        }
    }
    
    public String getNewCompId() {
        List<String> compFile = fh.fileRead(TF.complaintFiles);
        
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
    
    public ArrayList getCurrentRTcomplaints(String currentRTid) {
        ArrayList<ArrayList> combinedComp = new ArrayList<>();
        ArrayList<String> pendingComp = new ArrayList<>();
        ArrayList<String> progressingComp = new ArrayList<>();
        ArrayList<String> completedComp = new ArrayList<>();
        
        List<String> complaints = fh.fileRead(TF.complaintFiles);
        for (String eachComp : complaints) {
            String[] compDet = eachComp.split(TF.sp);
            String complainerId = compDet[1];
            if (complainerId.equals(currentRTid)) {
                String status = compDet[5];
                String tableLine = compDet[0] + TF.sp + compDet[2] + TF.sp + compDet[3] + TF.sp + compDet[4] + TF.sp + compDet[5] + TF.sp;
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
    
    public ArrayList getCurrentRTvisitor(String currentRTid) {
        ArrayList<String> registeredVisitor = new ArrayList<>();
        
        List<String> visitorFiles = fh.fileRead(TF.visitorPass);
        for (String eachVis : visitorFiles) {
            String rtID = eachVis.split(TF.sp)[7];
            if (rtID.equals(currentRTid)) {
                registeredVisitor.add(eachVis);
            }
        }
        
        return registeredVisitor;
    }
    
    public String getVisitorDetails(String passId) {
        List<String> visitorFile = fh.fileRead(TF.visitorPass);
        for (String eachVis : visitorFile) {
            String pID = eachVis.split(TF.sp)[0];
            if (pID.equals(passId)) {
                return eachVis;
            }
        }
        
        return null;
    }
    
    public String getNewPassID() {
        List<String> passFile = fh.fileRead(TF.visitorPass);
        
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
}

enum cptStatus{
    Pending,
    Progressing,
    Complete,
}

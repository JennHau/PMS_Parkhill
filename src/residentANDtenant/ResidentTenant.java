/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package residentANDtenant;

import java.text.ParseException;
import pms_parkhill_residence.CRUD;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import pms_parkhill_residence.FileHandling;
import pms_parkhill_residence.PMS_DateTimeFormatter;
import pms_parkhill_residence.TextFiles;
import pms_parkhill_residence.Users;

/**
 *
 * @author Winson
 */
public class ResidentTenant {
    FileHandling fh = new FileHandling();
    TextFiles TF = new TextFiles();
    CRUD crud = new CRUD();
    PMS_DateTimeFormatter DTF = new PMS_DateTimeFormatter();
    
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
    
    public ArrayList getCurrentUnitInvoice(String unitNo) {
        ArrayList<ArrayList> combineList = new ArrayList<>();
        ArrayList<String> incompleteInvoice = new ArrayList<>();
        ArrayList<String> completeInvoice = new ArrayList<>();        
        
        List<String> invoiceFile = fh.fileRead(TF.invoiceFile);
        List<String> paymentFile = fh.fileRead(TF.paymentFile);
        for (String eachInv : invoiceFile) {
            String[] invDet = eachInv.split(TF.sp);
            String uNo = invDet[1];
            if (uNo.equals(unitNo)) {
                String deleteID = invDet[invDet.length-1];
                if (deleteID.equals(TF.empty)) {
                    String invNo = invDet[0];
                    String feeType = invDet[2];
                    String[] combine = {invNo, feeType};
                    String invoiceKey = concatenateKey(combine);

                    boolean unpaid = true;
                    for (String eachPay : paymentFile) {
                        String[] payDet = eachPay.split(TF.sp);
                        String[] payCom = {payDet[0], payDet[2]};
                        String paymentKey = concatenateKey(payCom);

                        if (paymentKey.equals(invoiceKey)) {
                            completeInvoice.add(eachPay);
                            unpaid = false;
                        }
                    }

                    if (unpaid) {
                        incompleteInvoice.add(eachInv);
                    }
                }
            }
        }
        
        combineList.add(incompleteInvoice);
        combineList.add(completeInvoice);
        
        return combineList;
    }
    
    public ArrayList getCurrentUnitPaymentHistory(String unitNo) {
        ArrayList<String> paymentHistory = new ArrayList<>();
        List<String> paymentFile = fh.fileRead(TF.paymentFile);
        
        for (String eachPay : paymentFile) {
            String[] payDet = eachPay.split(TF.sp);
            String uNo = payDet[1];
            if (uNo.equals(unitNo)) {
                String deleteID = payDet[payDet.length-1];
                if (deleteID.equals(TF.empty)) {
                    paymentHistory.add(eachPay);
                }
            }
        }
        
        return paymentHistory;
    }
    
    public ArrayList getCurrentUnitFacilityPayment(String unitNo) {
        ArrayList<String> facilityPay = new ArrayList<>();
        List<String> facilityBooking = fh.fileRead(TF.facilityBookingFile);
        List<String> facilityFile = fh.fileRead(TF.facilityFile);
        ArrayList<String> reqPayFac = new ArrayList<>();
        ArrayList<String> bookingList = new ArrayList<>();
        
        for (String eachFac : facilityFile) {
            boolean paymentReq = Boolean.parseBoolean(eachFac.split(TF.sp)[3]);
            if (paymentReq) {
                reqPayFac.add(eachFac.split(TF.sp)[0]);
            }
        }
        
        for (String eachBooking : facilityBooking) {
            String[] bookDet = eachBooking.split(TF.sp);
            String uNo = bookDet[3];
            
            if (uNo.equals(unitNo)) {
                String facilityId = bookDet[1];
                if (reqPayFac.contains(facilityId)) {
                    String bookId = bookDet[0];
                    if (!bookingList.contains(bookId)) {
                        String toAdd = "";
                        String bookType = bookDet[2];
                        String totalPrice = bookDet[8];
                        String paidDate = bookDet[9];
                        String[] data = {bookId, bookType, totalPrice, paidDate};
                        for (String eachData : data) {
                            toAdd = toAdd + eachData + TF.sp;
                        }
                        facilityPay.add(toAdd);
                        bookingList.add(bookId);
                    }
                }
            }
        }
        
        return facilityPay;
    }
    
    public String concatenateKey(String[] keyList) {
        String concatenatedKey = "";
        for (String eachKey : keyList) {
            concatenatedKey = concatenatedKey + eachKey + "-";
        }
        
        return concatenatedKey;
    }
    
    public String validateTableSelectionAndGetValue(DefaultTableModel table, int selectedColumn, int selectedRow, int expectedColumn, int getValueColumn) {
        if (selectedColumn == expectedColumn) {
            String data = (String) table.getValueAt(selectedRow, getValueColumn);
            return data;
        }
        
        return null;
    }
    
    public ArrayList getIssuedStatement(String unitNo) {
        ArrayList<String> statement = new ArrayList<>();
        
        List<String> issuedStatement = fh.fileRead(TF.statementFile);
        
        for (String eachIssued : issuedStatement) {
            String uNo = eachIssued.split(TF.sp)[1];
            
            if (uNo.equals(unitNo)) {
                String deleteID = eachIssued.split(TF.sp)[3];
                if (deleteID.equals(TF.empty)) {
                    statement.add(eachIssued.split(TF.sp)[0]);                    
                }
            }
        }
        
        return statement;
    }
    
    public String getSpecificUser(String userId) {
        List<String> userProfile = fh.fileRead(TF.userProfile);
        for (String eachUser : userProfile) {
            String uId = eachUser.split(TF.sp)[0];
            if (uId.equals(userId)) {
                return eachUser;
            }
        }
        
        return null;
    }
    
    // Page Navigator
    public void toPaymentGateway(Users user, String totalAmount, ArrayList itemId) {
        ResidentTenantPaymentCredential page = new ResidentTenantPaymentCredential(user, totalAmount, itemId);
        page.setVisible(true);
    }
    
    public void toPaymentManagement(Users user) {
        ResidentTenantPaymentManagement page = new ResidentTenantPaymentManagement(user);
        page.setVisible(true);
    }
    
    public void toPaymentHistory(Users user) {
        ResidentTenantPaymentHistory page = new ResidentTenantPaymentHistory(user);
        page.setVisible(true);
    }
    
    public void toInvoice(Users user) {
        ResidentTenantInvoice page = new ResidentTenantInvoice(user);
        page.setVisible(true);
    }
    
    public void toStatement(Users user) {
        ResidentTenantStatement page = new ResidentTenantStatement(user);
        page.setVisible(true);
    }
    
    public void toFacilityReceipt(Users user, String bookingId) {
        ResidentTenantFacilityBookingReceipt page = new ResidentTenantFacilityBookingReceipt(user, bookingId);
        page.setVisible(true);
    }
}

enum cptStatus{
    Pending,
    Progressing,
    Complete,
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vendor;

import java.text.ParseException;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import pms_parkhill_residence.CRUD;
import pms_parkhill_residence.Complaints;
import pms_parkhill_residence.FileHandling;
import pms_parkhill_residence.PMS_DateTimeFormatter;
import pms_parkhill_residence.TextFiles;
import pms_parkhill_residence.Users;
import residentANDtenant.ResidentTenantPaymentCredential;

/**
 *
 * @author Winson
 */
public class Vendor {
    FileHandling fh = new FileHandling();
    TextFiles TF = new TextFiles();
    CRUD crud = new CRUD();
    PMS_DateTimeFormatter DTF = new PMS_DateTimeFormatter();
    Complaints CP = new Complaints();
    
    // Main method
    public void setTableRow(DefaultTableModel table, ArrayList arrayList) {
        table.setRowCount(0);
        
        for (int rowCount = 0; rowCount < arrayList.size(); rowCount++) {
            String[] rowDetails = String.valueOf(arrayList.get(rowCount)).split(TF.sp);
            table.addRow(rowDetails);
        }
    }
    
    public String concatenateKey(String[] keyList) {
        String concatenatedKey = "";
        for (String eachKey : keyList) {
            concatenatedKey = concatenatedKey + eachKey + "-";
        }
        
        return concatenatedKey;
    }
    
    public ArrayList<ArrayList> getCurrentUnitInvoice(String unitNo) {
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
    
    public double getTotalPricePerInvoice(String invoiceId, ArrayList<String> dataList) {
        double totalAmount = 0;
        for (String eachInv : dataList) {
            String[] invDet = eachInv.split(TF.sp);
            String invNo = invDet[0];
            
            if (invNo.equals(invoiceId)) {
                double eachPrice = Double.parseDouble(invDet[7]);
                totalAmount += eachPrice;
            }
        }
        
        return totalAmount;
    }
    
    public String validateTableSelectionAndGetValue(DefaultTableModel table, int selectedColumn, int selectedRow, int expectedColumn, int getValueColumn) {
        if (selectedColumn == expectedColumn) {
            String data = (String) table.getValueAt(selectedRow, getValueColumn);
            return data;
        }
        
        return null;
    }
    
    public ArrayList getCurrentUnitIssuedReceipt(String unitNo) {
        ArrayList<String> issuedReceipt = new ArrayList<>();
        
        List<String> receiptList = fh.fileRead(TF.receiptFile);
        
        for (String eachReceipt : receiptList) {
            String[] receiptDet = eachReceipt.split(TF.sp);
            String uNo = receiptDet[1];
            
            if (uNo.equals(unitNo)) {
                issuedReceipt.add(eachReceipt);
            }
        }
        
        return issuedReceipt;
    }
    
    public ArrayList getCurrentUnitIssuedInvoice(String unitNo) {
        ArrayList<String> issuedInvoice = new ArrayList<>();
        
        List<String> invoiceList = fh.fileRead(TF.invoiceFile);
        for (String eachInv : invoiceList) {
            String uNo = eachInv.split(TF.sp)[1];
            if (uNo.equals(unitNo)) {
                issuedInvoice.add(eachInv);
            }
        }
        
        return issuedInvoice;
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
    
    public ArrayList getCurrentUnitMonthStatement(Users user, String monthNyear) throws ParseException{
        ArrayList<String> statementList = new ArrayList<>();
        ArrayList<String> monthStatement = new ArrayList<>();
        
        // Get all issued invoice
        ArrayList<String> invoiceList = getCurrentUnitIssuedInvoice(user.getUnitNo());
        
        // get all paid invoice
        ArrayList<String> completedInvoice = getCurrentUnitPaymentHistory(user.getUnitNo());
        
        // Data Structure = Date, Transaction, Details, Amount, Payments
        // to change the issued invoice list to same data structure
        for (String eachInv : invoiceList) {
            String[] invDet = eachInv.split(TF.sp);
            String issuedDate = DTF.changeFormatDate(invDet[9]);
            String id = invDet[0];
            String type = invDet[2];
            String amount = invDet[7];
            String[] data = {issuedDate, "Invoice", id.toUpperCase() + " " + type, amount, "-"};
            
            String line = "";
            for (String eachData : data) {
                line = line + eachData + TF.sp;
            }
            
            statementList.add(line);
        } 
        // change the paid invoice list to same data structure
        for (String eachInv : completedInvoice) {
            String[] invDet = eachInv.split(TF.sp);
            String date = DTF.changeFormatDate(invDet[10]);
            String id = invDet[0];
            String type = invDet[2];
            String amount = invDet[7];
            String[] data = {date, "Invoice Payment", id.toUpperCase() + " " + type + " - " + amount + " in excess payments.", "-", amount};
            
            String line = "";
            for (String eachData : data) {
                line = line + eachData + TF.sp;
            }
            statementList.add(line);
        }
        
        LocalDate firstDay = DTF.formatDate(DTF.changeFormatDate("01/" + monthNyear));
        LocalDate lastDay = firstDay.with(lastDayOfMonth());
        
        // Retrieve only the data that is between the selected month
        for (String eachState : statementList) {
            String[] stateDet = eachState.split(TF.sp);
            LocalDate paymentDate = DTF.formatDate(stateDet[0]);
            if ((paymentDate.isAfter(firstDay) || paymentDate.isEqual(firstDay)) && (paymentDate.isBefore(lastDay) || paymentDate.isEqual(lastDay))) {
                monthStatement.add(eachState);
            }
        }
        // change list to array
        String[] monStateList = monthStatement.toArray(String[]::new);
        
        // sorting date method for the array
        for (int count1 = 0; count1 < monStateList.length - 1; count1++) {
            for (int count2 = count1+1; count2 < monStateList.length; count2++) {
                String item1 = monStateList[count1];
                String item2 = monStateList[count2];
                
                LocalDate date1 = DTF.formatDate(item1.split(TF.sp)[0]);
                LocalDate date2 = DTF.formatDate(item2.split(TF.sp)[0]);
                
                if (date2.isBefore(date1)) {
                    String tempItem = item1;
                    monStateList[count1] = item2;
                    monStateList[count2] = tempItem;
                }
            }
        }
        ArrayList<String> dateList = new ArrayList<>();
        monthStatement = new ArrayList<>();
        
        // if have the same date as previous, make the particular row to have empty data for "Date" column
        for (String eachState : monStateList) {
            String[] stateDet = eachState.split(TF.sp);
            String date = stateDet[0];
            if (!dateList.contains(date)) {
                monthStatement.add(eachState);
                dateList.add(date);
            }
            else {
                String stateDate = "";
                String stateId = stateDet[1];
                String stateType = stateDet[2];
                String stateAmount = stateDet[3];
                String statePayment = stateDet[4];
                String[] data = {stateDate, stateId, stateType, stateAmount, statePayment};
                
                String stateItem = "";
                for (String eachData : data) {
                    stateItem = stateItem + eachData + TF.sp;
                }
                monthStatement.add(stateItem);
            }
        }
        return monthStatement;
    }
    
    // Page navigator
    public void toResidentTenantDashboard(Users user) {
        VendorDashboard page = new VendorDashboard(user);
        page.setVisible(true);
    }
    
    public void toPaymentManagement(Users user) {
        VendorPaymentManagement page = new VendorPaymentManagement(user);
        page.setVisible(true);
    }
    
    public void toComplaints(Users user) {
        VendorComplaints page = new VendorComplaints(user);
        page.setVisible(true);
    }
    
    public void toPaymentHistory(Users user) {
        VendorPaymentHistory page = new VendorPaymentHistory(user);
        page.setVisible(true);
    }
    
    public void toInvoice(Users user) {
        VendorInvoice page = new VendorInvoice(user);
        page.setVisible(true);
    }
    
    public void toInvoiceReceipt(Users user, String invoiceNo) {
        VendorInvoiceReceipt page = new VendorInvoiceReceipt(user, invoiceNo);
        page.setVisible(true);
    }
    
    public void toInvoicePayment(String invoiceNo, Users user, String feeTypes) {
        VendorInvoicePayment page = new VendorInvoicePayment(invoiceNo, user, feeTypes);
        page.setVisible(true);
    }
    
    public void toViewPaidInvoice(Users user, String invoiceNo, String feeTypes) {
        VendorViewPaidInvoice page = new VendorViewPaidInvoice(invoiceNo, user, feeTypes);
        page.setVisible(true);
    }
    
    public void toStatement(Users user) {
        VendorStatement page = new VendorStatement(user);
        page.setVisible(true);
    }
    
    public void toStatementReport(Users user, String monthNyear) {
        VendorStatementReport page = new VendorStatementReport(user, monthNyear);
        page.setVisible(true);
    }
    
    public void toViewProfile(Users user) {
        VendorProfile page = new VendorProfile(user);
        page.setVisible(true);
    }
    
    public void toPaymentCredential(Users user, String totalAmount, ArrayList itemId) {
        VendorPaymentCredential page = new VendorPaymentCredential(user, totalAmount, itemId);
        page.setVisible(true);
    }
}

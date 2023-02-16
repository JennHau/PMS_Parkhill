/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package residentANDtenant;

import java.awt.Color;
import java.time.LocalTime;
import pms_parkhill_residence.CRUD;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import pms_parkhill_residence.Complaint;
import pms_parkhill_residence.Facility;
import pms_parkhill_residence.FileHandling;
import pms_parkhill_residence.Invoice;
import pms_parkhill_residence.PMS_DateTimeFormatter;
import pms_parkhill_residence.Payment;
import pms_parkhill_residence.TextFiles;
import pms_parkhill_residence.Users;

/**
 *
 * @author Winson
 */
public class ResidentTenant extends Users {
    FileHandling fh = new FileHandling();
    TextFiles TF = new TextFiles();
    CRUD crud = new CRUD();
    PMS_DateTimeFormatter DTF = new PMS_DateTimeFormatter();
    Complaint CP = new Complaint();
    Payment PYM = new Payment();
    
    private String unitNo;
    
    public String[] visitorPassStatus = {"Registered", "Checked-In", "Checked-Out"};
    
    public ResidentTenant() {}
    
    public ResidentTenant(String userID, String email, String password, String firstName,
                 String lastName, String identificationNo, String gender,
                 String phoneNo, String unitNo) {
        
        super(userID, email, password, firstName,
             lastName, identificationNo, gender,
             phoneNo);
        
        this.unitNo = unitNo;
    }
    
    public void setTableRow(DefaultTableModel table, ArrayList arrayList) {
        table.setRowCount(0);
        
        for (int rowCount = 0; rowCount < arrayList.size(); rowCount++) {
            String[] rowDetails = String.valueOf(arrayList.get(rowCount)).split(TF.sp);
            table.addRow(rowDetails);
        }
    }
    
    public ArrayList getCurrentRTvisitor(String currentRTid) {
        ArrayList<String> registeredVisitor = new ArrayList<>();
        
        List<String> visitorFiles = fh.fileRead(TF.visitorPass);
        for (String eachVis : visitorFiles) {
            String rtID = eachVis.split(TF.sp)[10];
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
    
//    public ArrayList<ArrayList> getInvoiceAndPayment(String unitNo) {
//        ArrayList<ArrayList> combinedList = new ArrayList<>();
//        ArrayList<Invoice> incompList = new ArrayList<>();
//        ArrayList<Payment> compList = new ArrayList<>();
//        
//        ArrayList<Invoice> invoiceList = PYM.getCurrentUnitInvoice(unitNo);
//        ArrayList<Payment> paymentList = PYM.getCurrentUnitPayment(unitNo);
//        
//        for (Invoice eachInv : invoiceList) {
//            String[] inv = {eachInv.getInvoiceNo(), eachInv.getFeeType()};
//            String invKey = concatenateKey(inv);
//            
//            boolean found = false;
//            for (Payment eachPm : paymentList) {
//                String[] pay = {eachPm.getInvoiceNo(), eachPm.getFeeType()};
//                String payKey = concatenateKey(pay);
//                
//                if (invKey.equals(payKey)) {
//                    compList.add(eachPm);
//                    found = true;
//                }
//            }
//            
//            if (!found) {
//                incompList.add(eachInv);
//            }
//        }
//        
//        combinedList.add(incompList);
//        combinedList.add(compList);
//        
//        return combinedList;
//    }
    
//    public ArrayList getCurrentUnitIssuedInvoice(String unitNo) {
//        ArrayList<String> issuedInvoice = new ArrayList<>();
//        
//        List<String> invoiceList = fh.fileRead(TF.invoiceFile);
//        for (String eachInv : invoiceList) {
//            String uNo = eachInv.split(TF.sp)[1];
//            if (uNo.equals(unitNo)) {
//                issuedInvoice.add(eachInv);
//            }
//        }
//        
//        return issuedInvoice;
//    }
//    
//    public ArrayList<ArrayList> getCurrentUnitInvoice(String unitNo) {
//        ArrayList<ArrayList> combineList = new ArrayList<>();
//        ArrayList<String> incompleteInvoice = new ArrayList<>();
//        ArrayList<String> completeInvoice = new ArrayList<>();        
//        
//        List<String> invoiceFile = fh.fileRead(TF.invoiceFile);
//        List<String> paymentFile = fh.fileRead(TF.paymentFile);
//        for (String eachInv : invoiceFile) {
//            String[] invDet = eachInv.split(TF.sp);
//            String uNo = invDet[1];
//            if (uNo.equals(unitNo)) {
//                String deleteID = invDet[invDet.length-1];
//                if (deleteID.equals(TF.empty)) {
//                    String invNo = invDet[0];
//                    String feeType = invDet[2];
//                    String[] combine = {invNo, feeType};
//                    String invoiceKey = concatenateKey(combine);
//
//                    boolean unpaid = true;
//                    for (String eachPay : paymentFile) {
//                        String[] payDet = eachPay.split(TF.sp);
//                        String[] payCom = {payDet[0], payDet[2]};
//                        String paymentKey = concatenateKey(payCom);
//
//                        if (paymentKey.equals(invoiceKey)) {
//                            completeInvoice.add(eachPay);
//                            unpaid = false;
//                        }
//                    }
//
//                    if (unpaid) {
//                        incompleteInvoice.add(eachInv);
//                    }
//                }
//            }
//        }
//        
//        combineList.add(incompleteInvoice);
//        combineList.add(completeInvoice);
//        
//        return combineList;
//    }
//    
//    public ArrayList getCurrentUnitPaymentHistory(String unitNo) {
//        ArrayList<String> paymentHistory = new ArrayList<>();
//        List<String> paymentFile = fh.fileRead(TF.paymentFile);
//        
//        for (String eachPay : paymentFile) {
//            String[] payDet = eachPay.split(TF.sp);
//            String uNo = payDet[1];
//            if (uNo.equals(unitNo)) {
//                String deleteID = payDet[payDet.length-1];
//                if (deleteID.equals(TF.empty)) {
//                    paymentHistory.add(eachPay);
//                }
//            }
//        }
//        
//        return paymentHistory;
//    }
    
//    public ArrayList getCurrentUnitFacilityPayment(String unitNo) {
//        ArrayList<String> facilityPay = new ArrayList<>();
//        List<String> facilityBooking = fh.fileRead(TF.facilityBookingFile);
//        List<String> facilityFile = fh.fileRead(TF.facilityFile);
//        ArrayList<String> reqPayFac = new ArrayList<>();
//        ArrayList<String> bookingList = new ArrayList<>();
//        
//        for (String eachFac : facilityFile) {
//            boolean paymentReq = Boolean.parseBoolean(eachFac.split(TF.sp)[3]);
//            if (paymentReq) {
//                reqPayFac.add(eachFac.split(TF.sp)[0]);
//            }
//        }
//        
//        for (String eachBooking : facilityBooking) {
//            String[] bookDet = eachBooking.split(TF.sp);
//            String uNo = bookDet[3];
//            
//            if (uNo.equals(unitNo)) {
//                String facilityId = bookDet[1];
//                if (reqPayFac.contains(facilityId)) {
//                    String bookId = bookDet[0];
//                    if (!bookingList.contains(bookId)) {
//                        String toAdd = "";
//                        String bookType = bookDet[2];
//                        String totalPrice = bookDet[8];
//                        String paidDate = bookDet[9];
//                        String[] data = {bookId.toUpperCase(), bookType, totalPrice, paidDate};
//                        for (String eachData : data) {
//                            toAdd = toAdd + eachData + TF.sp;
//                        }
//                        facilityPay.add(toAdd);
//                        bookingList.add(bookId);
//                    }
//                }
//            }
//        }
//        
//        return facilityPay;
//    }
    
    public ArrayList getCurrentUnitBookedFacility(String unitNo) {
        ArrayList<String> bookedFacility = new ArrayList<>();
        
        List<String> facilityBookingFile = fh.fileRead(TF.facilityBookingFile);
        
        for (String eachBooked : facilityBookingFile) {
            String[] bookedDet = eachBooked.split(TF.sp);
            String uNo = bookedDet[3];
            
            if (uNo.equals(unitNo)) {
                bookedFacility.add(eachBooked);
            }
        }
        
        return bookedFacility;
    }
    
//    public ArrayList getCurrentUnitMonthStatement(String monthNyear) throws ParseException{
//        ArrayList<String> statementList = new ArrayList<>();
//        ArrayList<String> monthStatement = new ArrayList<>();
//        
//        // Get all issued invoice
//        ArrayList<Invoice> invoiceList = PYM.getCurrentUnitInvoice(this.unitNo);
//        
//        // get all paid invoice
//        ArrayList<Payment> paymentList = PYM.getCurrentUnitPayment(this.unitNo);
//        
//        // get all facility payment
//        ArrayList<String> facilityBooking = getCurrentUnitFacilityPayment(this.unitNo);
//        
//        // Data Structure = Date, Transaction, Details, Amount, Payments
//        // to change the issued invoice list to same data structure
//        for (Invoice eachInv : invoiceList) {
//            String issuedDate = DTF.changeFormatDate(eachInv.getIssuedDate());
//            String[] data = {issuedDate, "Invoice", eachInv.getInvoiceNo() + " " + eachInv.getFeeType(), eachInv.getUnitPrice().toString(), "-"};
//            
//            String line = "";
//            for (String eachData : data) {
//                line = line + eachData + TF.sp;
//            }
//            
//            statementList.add(line);
//        } 
//        // change the paid invoice list to same data structure
//        for (Payment eachPm : paymentList) {
//            String date = DTF.changeFormatDate(eachPm.getPaymentDate());
//            String[] data = {date, "Invoice Payment", eachPm.getInvoiceNo() + " " + eachPm.getFeeType() + " - " + eachPm.getUnitPrice().toString() + " in excess payments.", "-", eachPm.getUnitPrice().toString()};
//            
//            String line = "";
//            for (String eachData : data) {
//                line = line + eachData + TF.sp;
//            }
//            statementList.add(line);
//        } 
//        
//        // Change the facility booking to same data structure
//        ArrayList<String> bookIdList = new ArrayList<>();
//        for (String eachBook : facilityBooking) {
//            String[] bookDet = eachBook.split(TF.sp);
//            String id = bookDet[0];
//            
//            if (!bookIdList.contains(id)) {
//                String date = bookDet[3];
//                String type = bookDet[1];
//                String amount = bookDet[2];
//                String[] data = {date, "Facility Booking", id.toUpperCase() + " - " + type, amount, "-"};
//                String[] data2 = {date, "Booking Payment", id.toUpperCase() + " - " + amount + " in excess payments.", "-", amount};
//                
//                String line = "";
//                for (String eachData : data) {
//                    line = line + eachData + TF.sp;
//                }
//                statementList.add(line);
//
//                line = "";
//                for (String eachData : data2) {
//                    line = line + eachData + TF.sp;
//                }
//                statementList.add(line);
//
//                bookIdList.add(id);
//            }
//        }
//        
//        LocalDate firstDay = DTF.formatDate(DTF.changeFormatDate("01/" + monthNyear));
//        LocalDate lastDay = firstDay.with(lastDayOfMonth());
//        
//        // Retrieve only the data that is between the selected month
//        for (String eachState : statementList) {
//            String[] stateDet = eachState.split(TF.sp);
//            LocalDate paymentDate = DTF.formatDate(stateDet[0]);
//            if ((paymentDate.isAfter(firstDay) || paymentDate.isEqual(firstDay)) && (paymentDate.isBefore(lastDay) || paymentDate.isEqual(lastDay))) {
//                monthStatement.add(eachState);
//            }
//        }
//        // change list to array
//        String[] monStateList = monthStatement.toArray(String[]::new);
//        
//        // sorting date method for the array
//        for (int count1 = 0; count1 < monStateList.length - 1; count1++) {
//            for (int count2 = count1+1; count2 < monStateList.length; count2++) {
//                String item1 = monStateList[count1];
//                String item2 = monStateList[count2];
//                
//                LocalDate date1 = DTF.formatDate(item1.split(TF.sp)[0]);
//                LocalDate date2 = DTF.formatDate(item2.split(TF.sp)[0]);
//                
//                if (date2.isBefore(date1)) {
//                    String tempItem = item1;
//                    monStateList[count1] = item2;
//                    monStateList[count2] = tempItem;
//                }
//            }
//        }
//        ArrayList<String> dateList = new ArrayList<>();
//        monthStatement = new ArrayList<>();
//        
//        // if have the same date as previous, make the particular row to have empty data for "Date" column
//        for (String eachState : monStateList) {
//            String[] stateDet = eachState.split(TF.sp);
//            String date = stateDet[0];
//            if (!dateList.contains(date)) {
//                monthStatement.add(eachState);
//                dateList.add(date);
//            }
//            else {
//                String stateDate = "";
//                String stateId = stateDet[1];
//                String stateType = stateDet[2];
//                String stateAmount = stateDet[3];
//                String statePayment = stateDet[4];
//                String[] data = {stateDate, stateId, stateType, stateAmount, statePayment};
//                
//                String stateItem = "";
//                for (String eachData : data) {
//                    stateItem = stateItem + eachData + TF.sp;
//                }
//                monthStatement.add(stateItem);
//            }
//        }
//        return monthStatement;
//    }
    
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
    
    public String getFacilityId(String bookingId) {
        List<String> facilityBookingFile = fh.fileRead(TF.facilityBookingFile);
        for (String eachBooking : facilityBookingFile) {
            String bId = eachBooking.split(TF.sp)[0];
            if (bId.equals(bookingId)) {
                return eachBooking.split(TF.sp)[1];
            }
        }
        
        return null;
    }
    
    public String[] getBookedStartAndEndTime(String bookingId) {
        String[] startEndTime = {"", ""};
        List<String> facilityBooking = fh.fileRead(TF.facilityBookingFile);
        
        for (String eachBooking : facilityBooking) {
            String[] bookingDet = eachBooking.split(TF.sp);
            String bookingID = bookingDet[0];
            
            if (bookingID.equals(bookingId)) {
                String startTime = bookingDet[5];
                String endTime = bookingDet[6];
                
                if (startEndTime[0].equals("")) {
                    startEndTime[0] = startTime;
                }
                if (startEndTime[1].equals("")) {
                    startEndTime[1] = endTime;
                }
                
                if (LocalTime.parse(startTime).isBefore(LocalTime.parse(startEndTime[0]))) {
                    startEndTime[0] = startTime;
                }
                if (LocalTime.parse(endTime).isAfter(LocalTime.parse(startEndTime[1]))) {
                    startEndTime[1] = endTime;
                }
            }
        }
        
        return startEndTime;
    }
    
//    public String concatenateKey(String[] keyList) {
//        String concatenatedKey = "";
//        for (String eachKey : keyList) {
//            concatenatedKey = concatenatedKey + eachKey + "-";
//        }
//        
//        return concatenatedKey;
//    }
    
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
    
    public double getTotalPricePerPayment(String invoiceId, ArrayList<String> dataList) {
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
    
    @Override
    public void modifySelfAccount() {
        String userID = this.getUserID().toLowerCase();
        String email = this.getEmail();
        String firstName = this.getFirstName();
        String lastName = this.getLastName();
        String password = this.getPassword();
        String identificationNo = this.getIdentificationNo();
        String gender = this.getGender();
        String phoneNo = this.getPhoneNo();
        
        List<String> userProfile = fh.fileRead("userProfile.txt");
        String[] userProfileArray = new String[userProfile.size()];
        userProfile.toArray(userProfileArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i<userProfile.size(); i++) {
            String[] userInfo = userProfileArray[i].split(";");
            String userID_temp = userInfo[0];
            
            if (userID_temp.equals(userID)) {
                newData.add(userID +";"+ email +";"+ password +";"+ firstName
                        +";"+ lastName +";"+ identificationNo +";"+ gender
                        +";"+ phoneNo +";"+ this.unitNo +";");
            } else {
                newData.add(userProfileArray[i]);
            }
        } fh.fileWrite("userProfile.txt", false, newData);
    }
  
    // Page Navigator
    public void toResidentTenantDashboard(ResidentTenant RT) {
        ResidentTenantMainPage page = new ResidentTenantMainPage(RT);
        page.setVisible(true);
    }
    
    public void toPaymentCredential(ResidentTenant RT, String totalAmount, ArrayList itemId, boolean forFacility, boolean modifyBooking, ArrayList<Invoice> invoiceList) {
        ResidentTenantPaymentCredential page = new ResidentTenantPaymentCredential(RT, totalAmount, itemId, forFacility, modifyBooking, invoiceList);
        page.setVisible(true);
    }
    
    public void toPaymentManagement(ResidentTenant RT) {
        ResidentTenantPaymentManagement page = new ResidentTenantPaymentManagement(RT);
        page.setVisible(true);
    }
    
    public void toPaymentHistory(ResidentTenant RT) {
        ResidentTenantPaymentHistory page = new ResidentTenantPaymentHistory(RT);
        page.setVisible(true);
    }
    
    public void toInvoice(ResidentTenant RT) {
        ResidentTenantInvoice page = new ResidentTenantInvoice(RT);
        page.setVisible(true);
    }
    
    public void toInvoicePayment(String invoiceNo, ResidentTenant RT, ArrayList<Invoice> feeTypes) {
        ResidentTenantInvoicePayment page = new ResidentTenantInvoicePayment(invoiceNo, RT, feeTypes);
        page.setVisible(true);
    }
    
    public void toStatement(ResidentTenant RT) {
        ResidentTenantStatement page = new ResidentTenantStatement(RT);
        page.setVisible(true);
    }
    
    public void toFacilityReceipt(ResidentTenant RT, String bookingId) {
        ResidentTenantFacilityBookingReceipt page = new ResidentTenantFacilityBookingReceipt(RT, bookingId);
        page.setVisible(true);
    }
    
    public void toBookedFacility(ResidentTenant RT) {
        ResidentTenantBookedFacility page = new ResidentTenantBookedFacility(RT);
        page.setVisible(true);
    }
    
    public void toManageBookedFacility(ResidentTenant RT, Facility fb, String bookingID, String date) {
        ResidentTenantManageBookedFacility page = new ResidentTenantManageBookedFacility(RT, fb, bookingID, date);
        page.setVisible(true);
    }
    
    public void toFacilityBookingManagement(ResidentTenant RT) {
        ResidentTenantFacilityBooking page = new ResidentTenantFacilityBooking(RT);
        page.setVisible(true);
    }
    
    public void toFacilityPreview(ResidentTenant RT, String facilityID) {
        ResidentTenantFacilityPreview page = new ResidentTenantFacilityPreview(RT, facilityID);
        page.setVisible(true);
    }
    
    public void toBookFacility(ResidentTenant RT, Facility fb) {
        ResidentTenantBookFacility page = new ResidentTenantBookFacility(RT, fb);
        page.setVisible(true);
    }
    
    public void toFacilityPaymentGateway(ResidentTenant RT, List<String> bookingList, Facility fb) {
        ResidentTenantFacilityPaymentGateway page = new ResidentTenantFacilityPaymentGateway(RT, bookingList, fb);
        page.setVisible(true);
    }
    
    public void toViewProfile(ResidentTenant RT) {
        ResidentTenantProfile page = new ResidentTenantProfile(RT);
        page.setVisible(true);
    }
    
    public void toVisitorPass(ResidentTenant RT) {
        ResidentTenantVisitorPass page = new ResidentTenantVisitorPass(RT);
        page.setVisible(true);
    }
    
    public void toComplaints(ResidentTenant RT) {
        ResidentTenantComplaints page = new ResidentTenantComplaints(RT);
        page.setVisible(true);
    }
    
    public void toInvoiceReceipt(ResidentTenant RT, String invoiceNo) {
        ResidentTenantInvoiceReceipt page = new ResidentTenantInvoiceReceipt(RT, invoiceNo);
        page.setVisible(true);
    }
    
    public void toViewPaidInvoice(ResidentTenant RT, String invoiceNo, ArrayList<Payment> paymentList) {
        ResidentTenantViewPaidInvoice page = new ResidentTenantViewPaidInvoice(invoiceNo, RT, paymentList);
        page.setVisible(true);
    }
    
    public void toStatementReport(ResidentTenant RT, String monthNyear) {
        ResidentTenantStatementReport page = new ResidentTenantStatementReport(RT, monthNyear);
        page.setVisible(true);
    }

    /**
     * @return the unitNo
     */
    public String getUnitNo() {
        return unitNo;
    }

    /**
     * @param unitNo the unitNo to set
     */
    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }
}

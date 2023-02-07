/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package residentANDtenant;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import pms_parkhill_residence.CRUD;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import pms_parkhill_residence.Complaints;
import pms_parkhill_residence.Facility;
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
    Complaints CP = new Complaints();
    
    public String[] visitorPassStatus = {"Registered", "Checked-In", "Checked-Out"};
    
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
                        String[] data = {bookId.toUpperCase(), bookType, totalPrice, paidDate};
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
    
    public ArrayList getCurrentUnitMonthStatement(Users user, String monthNyear) throws ParseException{
        ArrayList<String> statementList = new ArrayList<>();
        ArrayList<String> monthStatement = new ArrayList<>();
        
        // Get all issued invoice
        ArrayList<String> invoiceList = getCurrentUnitIssuedInvoice(user.getUnitNo());
        
        // get all paid invoice
        ArrayList<String> completedInvoice = getCurrentUnitPaymentHistory(user.getUnitNo());
        
        // get all facility payment
        ArrayList<String> facilityBooking = getCurrentUnitFacilityPayment(user.getUnitNo());
        
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
        // Change the facility booking to same data structure
        ArrayList<String> bookIdList = new ArrayList<>();
        for (String eachBook : facilityBooking) {
            String[] bookDet = eachBook.split(TF.sp);
            String id = bookDet[0];
            
            if (!bookIdList.contains(id)) {
                String date = bookDet[3];
                String type = bookDet[1];
                String amount = bookDet[2];
                String[] data = {date, "Facility Booking", id.toUpperCase() + " - " + type, amount, "-"};
                String[] data2 = {date, "Booking Payment", id.toUpperCase() + " - " + amount + " in excess payments.", "-", amount};
                
                String line = "";
                for (String eachData : data) {
                    line = line + eachData + TF.sp;
                }
                statementList.add(line);

                line = "";
                for (String eachData : data2) {
                    line = line + eachData + TF.sp;
                }
                statementList.add(line);

                bookIdList.add(id);
            }
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
  
    // Page Navigator
    public void toResidentTenantDashboard(Users user) {
        ResidentTenantMainPage page = new ResidentTenantMainPage(user);
        page.setVisible(true);
    }
    
    public void toPaymentCredential(Users user, String totalAmount, ArrayList itemId, boolean forFacility, boolean modifyBooking) {
        ResidentTenantPaymentCredential page = new ResidentTenantPaymentCredential(user, totalAmount, itemId, forFacility, modifyBooking);
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
    
    public void toInvoicePayment(String invoiceNo, Users user, String feeTypes) {
        ResidentTenantInvoicePayment page = new ResidentTenantInvoicePayment(invoiceNo, user, feeTypes);
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
    
    public void toBookedFacility(Users user) {
        ResidentTenantBookedFacility page = new ResidentTenantBookedFacility(user);
        page.setVisible(true);
    }
    
    public void toManageBookedFacility(Users user, Facility fb, String bookingID, String date) {
        ResidentTenantManageBookedFacility page = new ResidentTenantManageBookedFacility(user, fb, bookingID, date);
        page.setVisible(true);
    }
    
    public void toFacilityBookingManagement(Users user) {
        ResidentTenantFacilityBooking page = new ResidentTenantFacilityBooking(user);
        page.setVisible(true);
    }
    
    public void toFacilityPreview(Users user, String facilityID) {
        ResidentTenantFacilityPreview page = new ResidentTenantFacilityPreview(user, facilityID);
        page.setVisible(true);
    }
    
    public void toBookFacility(Users user, Facility fb) {
        ResidentTenantBookFacility page = new ResidentTenantBookFacility(user, fb);
        page.setVisible(true);
    }
    
    public void toFacilityPaymentGateway(Users user, List<String> bookingList, Facility fb) {
        ResidentTenantFacilityPaymentGateway page = new ResidentTenantFacilityPaymentGateway(user, bookingList, fb);
        page.setVisible(true);
    }
    
    public void toViewProfile(Users user) {
        ResidentTenantProfile page = new ResidentTenantProfile(user);
        page.setVisible(true);
    }
    
    public void toVisitorPass(Users user) {
        ResidentTenantVisitorPass page = new ResidentTenantVisitorPass(user);
        page.setVisible(true);
    }
    
    public void toComplaints(Users user) {
        ResidentTenantComplaints page = new ResidentTenantComplaints(user);
        page.setVisible(true);
    }
    
    public void toInvoiceReceipt(Users user, String invoiceNo) {
        ResidentTenantInvoiceReceipt page = new ResidentTenantInvoiceReceipt(user, invoiceNo);
        page.setVisible(true);
    }
    
    public void toViewPaidInvoice(Users user, String invoiceNo, String feeTypes) {
        ResidentTenantViewPaidInvoice page = new ResidentTenantViewPaidInvoice(invoiceNo, user, feeTypes);
        page.setVisible(true);
    }
    
    public void toStatementReport(Users user, String monthNyear) {
        ResidentTenantStatementReport page = new ResidentTenantStatementReport(user, monthNyear);
        page.setVisible(true);
    }
}

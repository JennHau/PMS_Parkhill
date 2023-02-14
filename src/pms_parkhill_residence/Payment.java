/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import java.util.ArrayList;
import java.util.List;
import residentANDtenant.ResidentTenant;

/**
 *
 * @author wongj
 */
public class Payment extends Invoice{
    
    private String paymentBy;
    private String paymentDate;
    private List<String> availablePayment;
    FileHandling fh = new FileHandling();
    
    public Payment() {}
    
    public Payment(String invoiceNo, String unitNo, String feeType,
            String unitCategory, String consumption, String unit, String unitPrice,
            String totalPrice, String period, String issuedDate, String deleteID,
            String paymentBy, String paymentDate) {
        
        super(invoiceNo, unitNo, feeType, unitCategory, consumption,
            unit, unitPrice, totalPrice, period, issuedDate, deleteID);
        this.paymentBy = paymentBy;
        this.paymentDate = paymentDate;
    }

    
    // extract all payments details for display
    public List<String> displayAllPayment(String status) {
        List<String> pendingPaymentList = new ArrayList<String>();
        try {
            // get all unpaid payments
            if (status.equals("PENDING")) {
                List<String> invoicesList = fh.fileRead("invoices.txt");
                String[] invoicesArray = new String[invoicesList.size()];
                invoicesList.toArray(invoicesArray);

                for (int i = 1; i < invoicesList.size(); i++) {
                    boolean check = true;
                    String[] invoiceDetails = invoicesArray[i].split(";");
                    String iInvoiceNo = invoiceDetails[0];
                    String iUnitNo = invoiceDetails[1];
                    String iFeeType = invoiceDetails[2];
                    String iTotalPrice = invoiceDetails[7];
                    String iGeneratedDate = invoiceDetails[9];

                    List<String> paymentList = fh.fileRead("payment.txt");
                    String[] paymentArray = new String[paymentList.size()];
                    paymentList.toArray(paymentArray);

                    for (int j = 1; j < paymentList.size(); j++) {
                        String[] paymentDetails = paymentArray[j].split(";");
                        String pInvoiceNo = paymentDetails[0];
                        String pFeeType = paymentDetails[2];
                        if (iInvoiceNo.equals(pInvoiceNo) && iFeeType.equals(pFeeType)) {
                            check = false;
                        }
                    }
                    if (check == true) {
                        String cPaymentDetails = iInvoiceNo + ";" + iGeneratedDate + ";"
                                + iUnitNo + ";" + iFeeType + ";" + iTotalPrice + ";";
                        pendingPaymentList.add(cPaymentDetails);
                    }
                }

            // get all paid payment
            } else if (status.equals("PAID")) {
                List<String> paymentList = fh.fileRead("payment.txt");
                String[] paymentArray = new String[paymentList.size()];
                paymentList.toArray(paymentArray);

                for (int i = 1; i < paymentList.size(); i++) {
                    String[] paymentDetails = paymentArray[i].split(";");
                    String invoiceNo = paymentDetails[0];
                    String unitNo = paymentDetails[1];
                    String feeType = paymentDetails[2];
                    String totalPrice = paymentDetails[7];
                    String paymentDate = paymentDetails[10];
                    String issuedDate = paymentDetails[11];
                    String cPaymentDetails = invoiceNo + ";" + issuedDate + ";"
                            + unitNo + ";" + feeType + ";" + totalPrice + ";"
                            + paymentDate + ";";
                    pendingPaymentList.add(cPaymentDetails);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pendingPaymentList;
    }
    
    // extract specific payment details
    public List<String> displayOnePayment(String invoiceNo) {
        List<String> paymentFees = new ArrayList<String>();
        try {
            List<String> invoicesList = fh.fileRead("invoices.txt");
            String[] invoicesArray = new String[invoicesList.size()];
            invoicesList.toArray(invoicesArray);
            
            List<String> paidList = fh.fileRead("payment.txt");
            
            // compare invoice data with payment data to prevent double payment
            for (int i = 1; i < invoicesList.size(); i++) {
                String[] feesDetails = invoicesArray[i].split(";");
                String einvoiceNo = feesDetails[0];
                String feeType = feesDetails[2];
                String issueDate = feesDetails[9];
                String consump = feesDetails[4];
                String unit = feesDetails[5];
                String unitPrice = feesDetails[6];
                String totalPrice = feesDetails[7];
                String cDetails = feeType + ";" + issueDate + ";" + consump + ";"
                        + unit + ";" + unitPrice + ";" + totalPrice + ";";

                if (einvoiceNo.equals(invoiceNo)) {
                    boolean check = true;
                    for(int j = 1; j < paidList.size(); j++) {
                        String[] paymentDetails = paidList.get(j).split(";");
                        String pInvoiceNo = paymentDetails[0];
                        String pFeeType = paymentDetails[2];
                        
                        if(einvoiceNo.equals(pInvoiceNo) && feeType.equals(pFeeType)) {
                            check = false;
                        }
                    }
                    if(check) {
                        paymentFees.add(cDetails);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentFees;
    }
    
    // method to store newly made payment
    public void storePayment(String invoiceNo, String userID) {
        try {
            List<String> invoicesList = fh.fileRead("invoices.txt");
            String[] invoicesArray = new String[invoicesList.size()];
            invoicesList.toArray(invoicesArray);

            List<String> newData = new ArrayList<>();

            for (int i = 1; i < invoicesList.size(); i++) {
                String[] invoiceDetails = invoicesArray[i].split(";");
                String einvoiceNo = invoiceDetails[0];
                String unitNo = invoiceDetails[1];
                String feeType = invoiceDetails[2];
                String target = invoiceDetails[3];
                String consump = invoiceDetails[4];
                String unit = invoiceDetails[5];
                String unitPrice = invoiceDetails[6];
                String totalPrice = invoiceDetails[7];
                String period = invoiceDetails[8];
                String generatedDate = invoiceDetails[9];

                if (einvoiceNo.equals(invoiceNo)) {
                    String todayDate = todayDate();
                    newData.add(einvoiceNo + ";" + unitNo + ";" + feeType + ";"
                            + target + ";" + consump + ";" + unit + ";"
                            + unitPrice + ";" + totalPrice + ";" + period + ";"
                            + userID.toLowerCase() + ";" + todayDate + ";" +
                            generatedDate + ";" + "-" + ";");
                }
            }
            fh.fileWrite("payment.txt", true, newData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // store newly issued receipt
    public void issueReceipt(List<String> receiptDetails) {
        String[] receiptDetailsArray = new String[receiptDetails.size()];
        receiptDetails.toArray(receiptDetailsArray);

        try {
            List<String> newData = new ArrayList<>();

            for (int i = 0; i < receiptDetails.size(); i++) {
                String[] receiptFullDetails = receiptDetailsArray[i].split(";");
                String invoiceNo = receiptFullDetails[0];
                String feeType = receiptFullDetails[1];

                List<String> paymentList = fh.fileRead("payment.txt");
                String[] paymentArray = new String[paymentList.size()];
                paymentList.toArray(paymentArray);

                for (int j = 1; j < paymentList.size(); j++) {
                    String[] paymentFullDetails = paymentArray[j].split(";");
                    String pInvoiceNo = paymentFullDetails[0];
                    String pFeeType = paymentFullDetails[2];

                    if (pInvoiceNo.equals(invoiceNo) && pFeeType.equals(feeType)) {
                        newData.add(paymentArray[j]);
                    }
                }
            }
            fh.fileWrite("receipt.txt", true, newData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // extract receipt details for display
    public List<String> displayReceipt(String status) {
        List<String> receiptList = new ArrayList<String>();
        try {
            List<String> eReceiptList = fh.fileRead("receipt.txt");
            String[] receiptArray = new String[eReceiptList.size()];
            eReceiptList.toArray(receiptArray);

            // in case user is deleted, system will search for active and inactive
            // user details
            List<String> usernameList = new ArrayList<>();
            List<String> userIDList = new ArrayList<>();

            List<String> usersList = fh.fileRead("userProfile.txt");
            for (int i = 1; i < usersList.size(); i++) {
                String[] userDetails = usersList.get(i).split(";");
                String euserID = userDetails[0];
                String euserName = userDetails[3] + " " + userDetails[4];
                usernameList.add(euserName);
                userIDList.add(euserID);
            }
            
            List<String> iUsersList = fh.fileRead("inactiveUserProfile.txt");
            for (int i = 1; i < iUsersList.size(); i++) {
                String[] userDetails = iUsersList.get(i).split(";");
                String euserID = userDetails[1];
                String euserName = userDetails[4] + " " + userDetails[5];
                usernameList.add(euserName);
                userIDList.add(euserID);
            } 

            // get receipt data that havent issue
            if (status.equals("PENDING")) {
                List<String> paymentList = fh.fileRead("payment.txt");
                String[] paymentArray = new String[paymentList.size()];
                paymentList.toArray(paymentArray);

                for (int i = 1; i < paymentList.size(); i++) {
                    boolean check = true;
                    String[] invoiceDetails = paymentArray[i].split(";");
                    String iInvoiceNo = invoiceDetails[0];
                    String iUnitNo = invoiceDetails[1];
                    String iFeeType = invoiceDetails[2];
                    String iTotalPrice = invoiceDetails[7];
                    String paidBy = invoiceDetails[9];
                    String paymentDate = invoiceDetails[10];

                    for (int j = 1; j < eReceiptList.size(); j++) {
                        String[] paymentDetails = receiptArray[j].split(";");
                        String pInvoiceNo = paymentDetails[0];
                        String pFeeType = paymentDetails[2];
                        if (iInvoiceNo.equals(pInvoiceNo) && iFeeType.equals(pFeeType)) {
                            check = false;
                        }
                    }
                    if (check == true) {
                        String paidByName = usernameList.get(userIDList.indexOf(paidBy));

                        String cReceiptDetails = iInvoiceNo + ";" + iUnitNo + ";"
                                + iFeeType + ";" + iTotalPrice + ";" + paymentDate + ";"
                                + paidByName + ";";
                        receiptList.add(cReceiptDetails);
                    }
                }

            // get issued receipt data
            } else if (status.equals("ISSUED")) {
                for (int i = 1; i < eReceiptList.size(); i++) {
                    String[] receiptDetails = receiptArray[i].split(";");
                    String invoiceNo = receiptDetails[0];
                    String unitNo = receiptDetails[1];
                    String feeType = receiptDetails[2];
                    String totalPrice = receiptDetails[7];
                    String paidBy = receiptDetails[9];
                    String paymentDate = receiptDetails[10];

                    String paidByName = usernameList.get(userIDList.indexOf(paidBy));

                    String cReceiptDetails = invoiceNo + ";" + unitNo + ";"
                            + feeType + ";" + totalPrice + ";" + paymentDate + ";"
                            + paidByName + ";";
                    receiptList.add(cReceiptDetails);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiptList;
    }
    
    // extract all receipt based on unitNo
    public ArrayList extractSingleReceiptData(String unitNo) {
        ArrayList<String> paymentHistory = new ArrayList<>();
        List<String> paymentFile = fh.fileRead("receipt.txt");
        
        for (String eachPay : paymentFile) {
            String[] payDet = eachPay.split(";");
            String uNo = payDet[1];
            if (uNo.equals(unitNo)) {
                String deleteID = payDet[payDet.length-1];
                if (deleteID.equals("-")) {
                    paymentHistory.add(eachPay);
                }
            }
        }
        return paymentHistory;
    }
    
    // calculate total outstanding fee of each unit based on monthYear
    public List<String> calculateTotalOutstanding(String monthYear) {
        List<String> availablePending = displayAllPayment("PENDING");
        String[] availablePendingArray = new String[availablePending.size()];
        availablePending.toArray(availablePendingArray);

        List<String> availableInvoiceNo = new ArrayList<>();
        for (int i = 0; i < availablePending.size(); i++) {
            String[] pendingPayment = availablePendingArray[i].split(";");
            String invoiceNo = pendingPayment[0];
            String unitNo = pendingPayment[2];
            if (!availableInvoiceNo.contains(invoiceNo)
                    && invoiceNo.equals(unitNo + monthYear)) {
                availableInvoiceNo.add(invoiceNo);
            }
        }

        List<String> availablePendingPayment = new ArrayList<>();
        for (int i = 0; i < availableInvoiceNo.size(); i++) {
            String invoiceNo = availableInvoiceNo.get(i);
            String tUnitNo = "";
            String tInvoiceNo = "";
            String tIssuedDate = "";
            float totalPrice = 0;

            for (int j = 0; j < availablePending.size(); j++) {
                String[] pendingPayment = availablePendingArray[j].split(";");
                String eInvoiceNo = pendingPayment[0];
                float eTotalPrice = Float.valueOf(pendingPayment[4]);

                // sum up all pending payment total figure
                if (invoiceNo.equals(eInvoiceNo)) {
                    totalPrice += eTotalPrice;
                    tUnitNo = pendingPayment[2];
                    tInvoiceNo = pendingPayment[0];
                    tIssuedDate = pendingPayment[1];
                }
            }
            DecimalFormat df = new DecimalFormat("0.00");
            String totalOut = df.format(totalPrice);
            
            String cDetials = tUnitNo + ";" + tInvoiceNo + ";" + tIssuedDate + ";"
                    + String.valueOf(totalOut) + ";";
            availablePendingPayment.add(cDetials);
        }
        return availablePendingPayment;
    }
    
    // store charges late payment fee into account
    public void chargeLatePaymentFee(String invoiceNo, String latePaymentFee) {
        try {
            List<String> invoicesList = fh.fileRead("invoices.txt");
            String[] invoicesArray = new String[invoicesList.size()];
            invoicesList.toArray(invoicesArray);
            String unitNo = "-";
            String period = "-";
            boolean check = false;

            // check whethe late payment charges are charged before
            for (int i = 1; i < invoicesList.size(); i++) {
                String[] invoiceDetails = invoicesArray[i].split(";");
                String eInvoiceNo = invoiceDetails[0];
                String eUnitNo = invoiceDetails[1];
                String feeType = invoiceDetails[2];
                String ePeriod = invoiceDetails[8];

                if (eInvoiceNo.equals(invoiceNo) && feeType.equals("Late Payment Charges")) {
                    check = true;
                }
                
                if (eInvoiceNo.equals(invoiceNo)) {
                    unitNo = eUnitNo; period = ePeriod;
                }
            }

            List<String> newData = new ArrayList<>();
            // modify to latest late payment charge is record existed
            if (check == true) {
                for (int i = 1; i < invoicesList.size(); i++) {
                    String[] invoiceDetails = invoicesArray[i].split(";");
                    String nInvoiceNo = invoiceDetails[0];
                    String nUnitNo = invoiceDetails[1];
                    String nFeeType = invoiceDetails[2];
                    String nPeriod = invoiceDetails[8];
                    if (nInvoiceNo.equals(invoiceNo)
                            && nFeeType.equals("Late Payment Charges")) {

                        String generatedDate = this.todayDate();
                        newData.add(invoiceNo + ";" + nUnitNo + ";" + nFeeType + ";" + "-" + ";"
                                + "-" + ";" + "-" + ";" + "-" + ";" + latePaymentFee + ";"
                                + nPeriod + ";" + generatedDate + ";" + "-" +";");
                    } else {
                        newData.add(invoicesArray[i]);
                    }
                }
                fh.fileWrite("invoices.txt", false, newData);

            // add new late payment charges if record not found
            } else if (check == false) {
                String generatedDate = this.todayDate();
                newData.add(invoiceNo + ";" + unitNo + ";" + "Late Payment Charges" + ";" + "-"
                        + ";" + "-" + ";" + "-" + ";" + "-" + ";" + latePaymentFee + ";"
                        + period + ";" + generatedDate + ";"+ "-" +";");

                fh.fileWrite("invoices.txt", true, newData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // store newly issued statement 
    public void issueStatement(List<String> availableStatement) {
        try {
            List<String> newData = new ArrayList<>();
            String todayDate = todayDate();
            for (String statements : availableStatement) {
                newData.add(statements + todayDate +";"+ "-" +";");
            }
            fh.fileWrite("statements.txt", true, newData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // extract specific unit statement details based on monthYear
    public ArrayList displayOneStatement(String unitNo, String monthNyear) throws ParseException{
        ResidentTenant RT = new ResidentTenant();
        PMS_DateTimeFormatter DTF = new PMS_DateTimeFormatter();
        TextFiles TF = new TextFiles();
        
        ArrayList<String> statementList = new ArrayList<>();
        ArrayList<String> monthStatement = new ArrayList<>();
        
        // Get all issued invoice
        ArrayList<String> invoiceList = RT.getCurrentUnitIssuedInvoice(unitNo);
        
        // get all paid invoice
        ArrayList<String> completedInvoice = extractSingleReceiptData(unitNo);
        
        // get all facility payment
        ArrayList<String> facilityBooking = RT.getCurrentUnitFacilityPayment(unitNo);
        
        // Data Structure = Date, Transaction, Details, Amount, Payments
        // to change the issued invoice list to same data structure
        for (String eachInv : invoiceList) {
            String[] invDet = eachInv.split(";");
            String issuedDate = DTF.changeFormatDate(invDet[9]);
            String id = invDet[0];
            String type = invDet[2];
            String amount = invDet[7];
            String[] data = {issuedDate, "Invoice", id + " " + type, amount, "-"};
            
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
            String[] data = {date, "Invoice Payment", id + " " + type + " - " + amount + " in excess payments.", "-", amount};
            
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
                String[] data = {date, "Facility Booking", id + "-" + type, amount, "-"};
                String[] data2 = {date, "Booking Payment", id + " - " + amount + " in excess payments.", "-", amount};
                
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
    
    /**
     * @return the paymentBy
     */
    public String getPaymentBy() {
        return paymentBy;
    }

    /**
     * @param paymentBy the paymentBy to set
     */
    public void setPaymentBy(String paymentBy) {
        this.paymentBy = paymentBy;
    }

    /**
     * @return the paymentDate
     */
    public String getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param paymentDate the paymentDate to set
     */
    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * @return the availablePayment
     */
    public List<String> getAvailablePayment() {
        return availablePayment;
    }

    /**
     * @param availablePayment the availablePayment to set
     */
    public void setAvailablePayment(List<String> availablePayment) {
        this.availablePayment = availablePayment;
    }
}

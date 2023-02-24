/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wongj
 */
public class Payment extends Invoice {
    private String paymentBy;
    private String paymentDate;
    private List<String> availablePayment;
    FileHandling fh = new FileHandling();
    PMS_DateTimeFormatter DTF = new PMS_DateTimeFormatter();
    
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
    
    public Payment(String[] paymentDet) {
        super(paymentDet[0], paymentDet[1], paymentDet[2], paymentDet[3], paymentDet[4],
            paymentDet[5], paymentDet[6], paymentDet[7], paymentDet[8], paymentDet[11], paymentDet[12]);
        
        this.paymentBy = paymentDet[9];
        this.paymentDate = paymentDet[10];
    }
    
    // extract all payments details for display
    public List<String> displayAllPayment(String status) {
        List<String> pendingPaymentList = new ArrayList<String>();
        try {
            // get all unpaid payments
            if (status.equals("PENDING")) {
                List<String> invoicesList = fh.fileRead("invoices.txt");

                for (int i = 1; i < invoicesList.size(); i++) {
                    boolean check = true;
                    String[] invoiceDetails = invoicesList.get(i).split(";");
                    
                    Invoice invoice = new Invoice(invoiceDetails);

                    List<String> paymentList = fh.fileRead("payment.txt");

                    for (int j = 1; j < paymentList.size(); j++) {
                        String[] paymentDetails = paymentList.get(j).split(";");
                        Payment payment = new Payment(paymentDetails);
                        
                        if (invoice.getInvoiceNo().equals(payment.getInvoiceNo()) 
                                && invoice.getFeeType().equals(payment.getFeeType())) {
                            check = false;
                        }
                    }
                    if (check == true) {
                        String cPaymentDetails = invoice.getInvoiceNo() + ";"
                                + invoice.getIssuedDate() + ";" 
                                + invoice.getUnitNo() + ";" + invoice.getFeeType()
                                + ";" + invoice.getTotalPrice() + ";";
                        pendingPaymentList.add(cPaymentDetails);
                    }
                }
            // get all paid payment
            } else if (status.equals("PAID")) {
                List<String> paymentList = fh.fileRead("payment.txt");

                for (int i = 1; i < paymentList.size(); i++) {
                    String[] paymentDetails = paymentList.get(i).split(";");
                    
                    Payment payment = new Payment(paymentDetails);
                    
                    String cPaymentDetails = payment.getInvoiceNo() + ";" + payment.getIssuedDate() + ";"
                            + payment.getUnitNo() + ";" + payment.getFeeType() + ";" + payment.getTotalPrice() + ";"
                            + payment.getPaymentDate() + ";";
                    pendingPaymentList.add(cPaymentDetails);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pendingPaymentList;
    }
    
    // method to store newly made payment
    public void storePayment(Invoice invoice, String userID) {
        ArrayList<String> newData = new ArrayList<>();

        String todayDate = todayDate();

        String[] invoiceData = {invoice.getInvoiceNo(), invoice.getUnitNo(), invoice.getFeeType(), invoice.getUnitCategory(), invoice.getConsumption(), invoice.getUnit(),
                                invoice.getUnitPrice().toString(), invoice.getTotalPrice().toString(), invoice.getPeriod(), userID.toLowerCase(), todayDate, invoice.getIssuedDate(), TF.empty};

        String dataLine = "";
        for (String eachInv : invoiceData) {
            dataLine = dataLine + eachInv + TF.sp;
        }

        newData.add(dataLine);

        fh.fileWrite(TF.paymentFile, true, newData);
    }
    
    public List<String> extractAllUserList(String type) {
        List<String> availableList = new ArrayList<>();
        
        List<String> usersList = fh.fileRead("userProfile.txt");
        for (int i = 1; i < usersList.size(); i++) {
            String[] userDetails = usersList.get(i).split(";");
            String euserID = userDetails[0];
            String euserName = userDetails[3] + " " + userDetails[4];
            if(type.equals("name")) {
                availableList.add(euserName);
            } else if(type.equals("id")) {
                availableList.add(euserID);
            }
            
        }
        List<String> iUsersList = fh.fileRead("inactiveUserProfile.txt");
        for (int i = 1; i < iUsersList.size(); i++) {
            String[] userDetails = iUsersList.get(i).split(";");
            String euserID = userDetails[1];
            String euserName = userDetails[4] + " " + userDetails[5];
            if(type.equals("name")) {
                availableList.add(euserName);
            } else if(type.equals("id")) {
                availableList.add(euserID);
            }
        } 
        return availableList;
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
            List<String> usernameList = extractAllUserList("name");
            List<String> userIDList = extractAllUserList("id");

            // get receipt data that havent issue
            if (status.equals("PENDING")) {
                List<String> paymentList = fh.fileRead("payment.txt");

                for (int i = 1; i < paymentList.size(); i++) {
                    boolean check = true;
                    String[] invoiceDetails = paymentList.get(i).split(";");
                    
                    Payment payment = new Payment(invoiceDetails);

                    for (int j = 1; j < eReceiptList.size(); j++) {
                        String[] paymentDetails = receiptArray[j].split(";");
                        String pInvoiceNo = paymentDetails[0];
                        String pFeeType = paymentDetails[2];
                        if (payment.getInvoiceNo().equals(pInvoiceNo) &&
                                payment.getFeeType().equals(pFeeType)) {
                            check = false;
                        }
                    }
                    if (check == true) {
                        String paidByName = usernameList.get(userIDList.indexOf
                                            (payment.getPaymentBy()));

                        String cReceiptDetails = payment.getInvoiceNo() + ";"
                                + payment.getUnitNo() + ";" + payment.getFeeType()
                                + ";" + payment.getTotalPrice() + ";" 
                                + payment.getPaymentDate() + ";" + paidByName + ";";
                        receiptList.add(cReceiptDetails);
                    }
                }

            // get issued receipt data
            } else if (status.equals("ISSUED")) {
                for (int i = 1; i < eReceiptList.size(); i++) {
                    String[] receiptDetails = receiptArray[i].split(";");
                    
                    Payment payment = new Payment(receiptDetails);

                    String paidByName = usernameList.get(userIDList.indexOf
                                                     (payment.getPaymentBy()));
                    String cReceiptDetails = payment.getInvoiceNo() + ";" 
                            + payment.getUnitNo() + ";" + payment.getFeeType()
                            + ";" + payment.getTotalPrice() + ";" 
                            + payment.getPaymentDate() + ";" + paidByName + ";";
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
    
    // extract all unit that having outstanding fee
    public boolean checkOutstandingFee(String unitNo) {
        List<String> oustandingFee = displayAllPayment("PENDING");
        String[] oustandingFeeArray = new String[oustandingFee.size()];
        oustandingFee.toArray(oustandingFeeArray);
        
        for (int i = 0; i < oustandingFee.size(); i++) {
            String[] feeDetails = oustandingFeeArray[i].split(";");
            String eUnitNo = feeDetails[2];
            
            if (unitNo.equals(eUnitNo)) {
                return false;
            }
        } return true;
    }
    
    // extract specific unit statement details based on monthYear
    public ArrayList displayOneStatement(String unitNo, String monthNyear) throws ParseException{
        ArrayList<String> statementList = new ArrayList<>();
        ArrayList<String> monthStatement = new ArrayList<>();
        
        // Get all issued invoice
        ArrayList<Invoice> invoiceList = super.getCurrentUnitInvoice(unitNo);
        
        // get all paid invoice
        ArrayList<Payment> paymentList = getCurrentUnitPayment(unitNo);
        
        // get all facility payment
        ArrayList<String> facilityBooking = getCurrentUnitFacilityPayment(unitNo);
        
        // Data Structure = Date, Transaction, Details, Amount, Payments
        // to change the issued invoice list to same data structure
        for (Invoice eachInv : invoiceList) {
            String issuedDate = DTF.changeFormatDate(eachInv.getIssuedDate());
            String[] data = {issuedDate, "Invoice", eachInv.getInvoiceNo() + " " + eachInv.getFeeType(), eachInv.getTotalPrice().toString(), "-"};
            
            String line = "";
            for (String eachData : data) {
                line = line + eachData + TF.sp;
            }
            
            statementList.add(line);
        } 
        // change the paid invoice list to same data structure
        for (Payment eachPm : paymentList) {
            String date = DTF.changeFormatDate(eachPm.getPaymentDate());
            String[] data = {date, "Invoice Payment", eachPm.getInvoiceNo() + " " + eachPm.getFeeType() + " - " + eachPm.getTotalPrice().toString() + " in excess payments.", "-", eachPm.getTotalPrice().toString()};
            
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
    
    // extract current unit facility payment
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
    
    // extract current unit payment
    public ArrayList<Payment> getCurrentUnitPayment(String unitNo) {
        ArrayList<Payment> currentUnitPayment = new ArrayList<>();
        
        List<String> paymentFiles = fh.fileRead(TF.paymentFile);
        
        boolean firstLine = true;
        for (String eachPay : paymentFiles) {
            if (!firstLine) {
                String[] payDet = eachPay.split(TF.sp);
            
                Payment payment = new Payment(payDet);

                if (payment.getUnitNo().equals(unitNo)) {
                    if (payment.getDeleteID().equals(TF.empty)) {
                        currentUnitPayment.add(payment);
                    }
                }
            }
            
            firstLine = false;
        }
        
        return currentUnitPayment;
    }
    
    // parent method of getting all invoices
    public ArrayList<Invoice> getInvoiceOriginalMethod(String unitNo) {
        return super.getCurrentUnitInvoice(unitNo);
    }
    
    // get the all the available invoice code for a unit
    public ArrayList getInvoiceCode(String unitNo) {
        ArrayList<String> invoiceCode = new ArrayList<>();

        ArrayList<Invoice> invoices = getInvoiceOriginalMethod(unitNo);
        
        for (Invoice eachInv : invoices) {
            if (!invoiceCode.contains(eachInv.getInvoiceNo())) {
                invoiceCode.add(eachInv.getInvoiceNo());
            }
        }
        
        return invoiceCode;
    }
    
    // get current unit issued statement
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
    
    @Override
    public ArrayList<Invoice> getCurrentUnitInvoice(String unitNo) {
        ArrayList<Invoice> incompList = new ArrayList<>();
        
        ArrayList<Invoice> invoiceList = getInvoiceOriginalMethod(unitNo);
        
        ArrayList<Payment> paymentList = getCurrentUnitPayment(unitNo);
        
        for (Invoice eachInv : invoiceList) {
            String[] inv = {eachInv.getInvoiceNo(), eachInv.getFeeType()};
            String invKey = concatenateKey(inv);

            boolean notFound = true;
            for (Payment eachPm : paymentList) {
                String[] pay = {eachPm.getInvoiceNo(), eachPm.getFeeType()};
                String payKey = concatenateKey(pay);

                if (invKey.equals(payKey)) {
                    notFound = false;
                    break;
                }
            }

            if (notFound) {
                incompList.add(eachInv);
            }
        }
        
        return incompList;
    }
    
    public String concatenateKey(String[] keyList) {
        String concatenatedKey = "";
        for (String eachKey : keyList) {
            concatenatedKey = concatenatedKey + eachKey + "-";
        }
        
        return concatenatedKey;
    }
    
    public double getTotalPricePerPayment(String invoiceId, ArrayList<Payment> dataList) {
        double totalAmount = 0;
        for (Payment eachPm : dataList) {
            
            if (eachPm.getInvoiceNo().equals(invoiceId)) {
                double eachPrice = eachPm.getTotalPrice();
                totalAmount += eachPrice;
            }
        }
        
        return totalAmount;
    }
    
    public double getTotalPricePerPayment(String invoiceId, String unitNo) {
        ArrayList<Payment> paymentList = getCurrentUnitPayment(unitNo);
        return getTotalPricePerPayment(invoiceId, paymentList);
    }
    
    public ArrayList<Invoice> getSameUnpaidInvoiceNo(String unitNo, String invoiceNo) {
        ArrayList<Invoice> unpaidSameInv = new ArrayList<>();
        
        ArrayList<Invoice> unitInvList = getCurrentUnitInvoice(unitNo);
        
        for (Invoice eachInv : unitInvList) {
            if (eachInv.getInvoiceNo().equals(invoiceNo)) {
                unpaidSameInv.add(eachInv);
            }
        }
        
        return unpaidSameInv;
    }
    
    public ArrayList<Payment> getSamePaidInvoiceNo(String unitNo, String invoiceNo) {
        ArrayList<Payment> paidSameInv = new ArrayList<>();
        
        ArrayList<Payment> unitPayList = getCurrentUnitPayment(unitNo);
        
        for (Payment eachPay : unitPayList) {
            if (eachPay.getInvoiceNo().equals(invoiceNo)) {
                paidSameInv.add(eachPay);
            }
        }
        
        return paidSameInv;
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

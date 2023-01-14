/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.util.List;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author wongj
 */
public class AccountExecutive extends Users{
    
    FileHandling fh = new FileHandling();
    
    public List<String> extractFeeTypes(String fileName) {
        List<String> feeTypes = new ArrayList<String>();
        
        List<String> feeTypesList = fh.fileRead(fileName);
        String[] feeTypesArray = new String[feeTypesList.size()];
        feeTypesList.toArray(feeTypesArray);
        
        for (int i = 1; i<feeTypesList.size(); i++){
            feeTypes.add(feeTypesArray[i]);
        } 
        return feeTypes;    
    }
    
    public boolean storeNewFeeTypes(String feeTypesName, String target, String unit,
            String unitPrice) {
        List<String> feeTypesList = fh.fileRead("feeTypes.txt");
        String[] feeTypesArray = new String[feeTypesList.size()];
        feeTypesList.toArray(feeTypesArray);
        
        for (int i = 1; i<feeTypesList.size(); i++){
            String[] feeDetails = feeTypesArray[i].split(";");
            String existingFeeName = feeDetails[0].toLowerCase();
            String existingTarget = feeDetails[1];
            if (existingFeeName.equals(feeTypesName.toLowerCase()) &&
                    existingTarget.equals(target)) {
                return false;
            }
        } 
        List<String> newData = new ArrayList<>();
        String date = todayDate();
        newData.add(feeTypesName+";"+target+";"+unit+";"+unitPrice+";"+date+";");
        fh.fileWrite("feeTypes.txt", true, newData);
        return true;
    }
    
    public void modifyFeeTypes(String feeTypesName, String target, String unit,
            String unitPrice) {
        List<String> feeTypesList = fh.fileRead("feeTypes.txt");
        String[] feeTypesArray = new String[feeTypesList.size()];
        feeTypesList.toArray(feeTypesArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i<feeTypesList.size(); i++) {
            String[] feeTypes = feeTypesArray[i].split(";");
            String eFeeTypesName = feeTypes[0];
            String eTarget = feeTypes[1];
            String createdDate = feeTypes[4];
            if(eFeeTypesName.equals(feeTypesName) && eTarget.equals(target)) {
                newData.add(eFeeTypesName +";"+ eTarget +";"+ unit +";"+
                        unitPrice +";"+ createdDate +";");
            } else {
                    newData.add(feeTypesArray[i]);
            }
        } 
        fh.fileWrite("feeTypes.txt", false, newData);
    }
    
    public void deleteFeeTypes(String feeTypesName, String target) {
        List<String> feeTypesList = fh.fileRead("feeTypes.txt");
        String[] feeTypesArray = new String[feeTypesList.size()];
        feeTypesList.toArray(feeTypesArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i<feeTypesList.size(); i++) {
            String[] feeTypes = feeTypesArray[i].split(";");
            String eFeeTypesName = feeTypes[0];
            String eTarget = feeTypes[1];
            if(eFeeTypesName.equals(feeTypesName) && eTarget.equals(target)) {
                
            } else {
                    newData.add(feeTypesArray[i]);
            }
        } 
        fh.fileWrite("feeTypes.txt", false, newData);
    }
    
    public String todayDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String str = formatter.format(date);
        return str;
    }
    
    public List<String> rangeOfMonthNYear(String feeTypeName, String target) {
        List<String> monthNYear = new ArrayList<String>();
        String todayDate = todayDate();
        
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy") ;
        LocalDate ld = LocalDate.parse(todayDate, f);
        int m = ld.getMonthValue();
        int y = ld.getYear() ;
        
        String referenceDate = "";
        List<String> feeTypesList = fh.fileRead("feeTypes.txt");
        String[] feeTypesArray = new String[feeTypesList.size()];
        feeTypesList.toArray(feeTypesArray);
        
        for (int i = 1; i<feeTypesList.size(); i++) {
            String[] feeTypes = feeTypesArray[i].split(";");
            String eFeeTypesName = feeTypes[0];
            String eTarget = feeTypes[1];
            String createdDate = feeTypes[4];
            if(eFeeTypesName.equals(feeTypeName) && eTarget.equals(target)) {
                referenceDate = createdDate;
            } 
        }
        
        LocalDate ld2 = LocalDate.parse(referenceDate, f);
        int rm = ld2.getMonthValue() ;
        int ry = ld2.getYear() ;
        int tempM = rm;
        for (int i=ry; i<(y+1); i++) {
            for (int j=1; j<13; j++) {
                if (i < y) {
                    if (tempM == j) {
                        monthNYear.add(tempM +"/"+ i);
                        tempM++;
                        if (tempM > 12) {
                            tempM = 1;
                        }
                    }
                } else {
                    if (tempM < m) {
                        monthNYear.add(tempM +"/"+ i);
                        tempM++;
                    }
                }
            }
        } return monthNYear;
    }
    
    public List<String> extractInvoiceDetails(String feeTypeName, String target,
            String status, String monthYear) {
        
        List<String> feeTypesList = fh.fileRead("feeTypes.txt");
        String[] feeTypesArray = new String[feeTypesList.size()];
        feeTypesList.toArray(feeTypesArray);
        
        String cUnit = ""; String cUnitPrice = "";
        for (int i = 1; i<feeTypesList.size(); i++) {
            
            String[] feeTypesDetails = feeTypesArray[i].split(";");
            String feeType = feeTypesDetails[0];
            String eTarget = feeTypesDetails[1];
            String unit = feeTypesDetails[2];
            String unitPrice = feeTypesDetails[3];
            
            if (feeType.equals(feeTypeName) && eTarget.equals(target)) {
                cUnit = unit; cUnitPrice = unitPrice;
            }
        }
        
        List<String> availableInvoices = new ArrayList<String>();
        if (status.equals("PENDING")) {
            List<String> propertyDetailsList = fh.fileRead("propertyDetails.txt");
            String[] propertyDetailsArray = new String[propertyDetailsList.size()];
            propertyDetailsList.toArray(propertyDetailsArray);
            
            String cMonthYear = monthYear.substring(0, monthYear.indexOf("/")) 
                    + monthYear.substring(monthYear.indexOf("/") + 1);
            
            for (int i = 1; i<propertyDetailsList.size(); i++) {
                boolean check = false;
                String[] propertyDetails = propertyDetailsArray[i].split(";");
                String eUnitNo = propertyDetails[0];
                String eTarget = propertyDetails[1];
                String squareFeet = propertyDetails[2];
                
                List<String> invoicesList = fh.fileRead("invoices.txt");
                String[] invoicesArray = new String[invoicesList.size()];
                invoicesList.toArray(invoicesArray);
                
                for (int j = 1; j<invoicesList.size(); j++) {
                    String[] invoiceDetails = invoicesArray[j].split(";");
                    String eInvoiceNo = invoiceDetails[0];
                    String eFeeType = invoiceDetails[2];
                    if (eInvoiceNo.equals(eUnitNo + cMonthYear) 
                         && eFeeType.equals(feeTypeName)) {
                        check = true;
                    } 
                } 
                if (check == false && eTarget.equals(target)) {
                    if (cUnit.equals("Square Feet")) {
                        DecimalFormat df = new DecimalFormat("0.00");
                        float totalPrice = Float.valueOf(squareFeet) * Float.valueOf(cUnitPrice);
                        String totalPrice2 = df.format(totalPrice);
                        String invoiceLine = eUnitNo +";"+ squareFeet +";"+ 
                                cUnit +";"+ cUnitPrice +";"+ String.valueOf(totalPrice2);
                        availableInvoices.add(invoiceLine);
                    } else {
                        float consump = (new Random().nextFloat() * (250.00f - 0.00f)) + 0.00f;
                        DecimalFormat df = new DecimalFormat("0.00");
                        float totalPrice = consump * Float.valueOf(cUnitPrice);
                        String totalPrice2 = df.format(totalPrice);
                        String consump2 = df.format(consump);
                        String invoiceLine = eUnitNo +";"+ consump2 +";"+ 
                                cUnit +";"+ cUnitPrice +";"+ String.valueOf(totalPrice2);
                        availableInvoices.add(invoiceLine);
                    }
                }
            } 
        } else if (status.equals("ISSUED")) {
            List<String> propertyDetailsList = fh.fileRead("propertyDetails.txt");
            String[] propertyDetailsArray = new String[propertyDetailsList.size()];
            propertyDetailsList.toArray(propertyDetailsArray);
            
            String cMonthYear = monthYear.substring(0, monthYear.indexOf("/")) 
                    + monthYear.substring(monthYear.indexOf("/") + 1);
            
            for (int i = 1; i<propertyDetailsList.size(); i++) {
                String[] propertyDetails = propertyDetailsArray[i].split(";");
                String eUnitNo = propertyDetails[0];
                String eTarget = propertyDetails[1];
                
                List<String> invoicesList = fh.fileRead("invoices.txt");
                String[] invoicesArray = new String[invoicesList.size()];
                invoicesList.toArray(invoicesArray);
                
                
                for (int j = 1; j<invoicesList.size(); j++) {
                    String[] invoiceDetails = invoicesArray[j].split(";");
                    String eInvoiceNo = invoiceDetails[0];
                    String unitNo = invoiceDetails[1];
                    String eFeeType = invoiceDetails[2];
                    String consump = invoiceDetails[4];
                    String unit = invoiceDetails[5];
                    String unitPrice = invoiceDetails[6];
                    String totalPrice = invoiceDetails[7];
                    
                    if (eInvoiceNo.equals(eUnitNo + cMonthYear) 
                         && eTarget.equals(target) && eFeeType.equals(feeTypeName)) {
                        String invoiceLine = unitNo +";"+ consump +";"+ 
                                unit +";"+ unitPrice +";"+ totalPrice;
                        availableInvoices.add(invoiceLine);
                    }
                } 
            } 
        } return availableInvoices;
    }
    
    public void issueInvoice(List<String> invoiceDetails) {
        String[] invoiceDetailsArray = new String[invoiceDetails.size()];
        invoiceDetails.toArray(invoiceDetailsArray);
        
        List<String> newData = new ArrayList<>();

        for (int i=0; i<invoiceDetails.size(); i++) {
            newData.add(invoiceDetailsArray[i]);
        } 
        fh.fileWrite("invoices.txt", true, newData);
    }
    
    public List<String> extractAllPayment(String status) {
        List<String> pendingPaymentList = new ArrayList<String>();
        try {
            if (status.equals("PENDING")) {
                List<String> invoicesList = fh.fileRead("invoices.txt");
                String[] invoicesArray = new String[invoicesList.size()];
                invoicesList.toArray(invoicesArray);

                for (int i=1; i<invoicesList.size(); i++) {
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
                    
                    
                    for (int j=1; j<paymentList.size(); j++) {
                        String[] paymentDetails = paymentArray[j].split(";");
                        String pInvoiceNo = paymentDetails[0];
                        String pFeeType = paymentDetails[2];
                        if (iInvoiceNo.equals(pInvoiceNo) && iFeeType.equals(pFeeType)) {
                            check = false;
                        }
                    } 
                    if (check == true) {
                        String cPaymentDetails = iInvoiceNo +";"+ iGeneratedDate +";"+ 
                                iUnitNo +";"+ iFeeType +";"+ iTotalPrice +";";
                        pendingPaymentList.add(cPaymentDetails);
                    }
                } 
                
            } else if (status.equals("PAID")) {
                List<String> paymentList = fh.fileRead("payment.txt");
                String[] paymentArray = new String[paymentList.size()];
                paymentList.toArray(paymentArray);

                for (int i=1; i<paymentList.size(); i++) {
                    String[] paymentDetails = paymentArray[i].split(";");
                    String invoiceNo = paymentDetails[0];
                    String unitNo = paymentDetails[1];
                    String feeType = paymentDetails[2];
                    String totalPrice = paymentDetails[7];
                    String paymentDate = paymentDetails[10];
                    String issuedDate = paymentDetails[11];
                    String cPaymentDetails = invoiceNo +";"+ issuedDate +";"+ 
                            unitNo +";"+ feeType +";"+ totalPrice +";"+ 
                            paymentDate +";";
                    pendingPaymentList.add(cPaymentDetails);
                } 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pendingPaymentList;
    }
    
    public List<String> extractPaymentFees(String invoiceNo) {
        List<String> paymentFees = new ArrayList<String>();
        try {
            List<String> invoicesList = fh.fileRead("invoices.txt");
            String[] invoicesArray = new String[invoicesList.size()];
            invoicesList.toArray(invoicesArray);
            
            for (int i=1; i<invoicesList.size(); i++) {
                String[] feesDetails = invoicesArray[i].split(";");
                String einvoiceNo = feesDetails[0];
                String feeType = feesDetails[2];
                String issueDate = feesDetails[9];
                String consump = feesDetails[4];
                String unit = feesDetails[5];
                String unitPrice = feesDetails[6];
                String totalPrice = feesDetails[7];
                String cDetails = feeType +";"+ issueDate +";"+ consump +";"+
                        unit +";"+ unitPrice +";"+ totalPrice +";";
                
                if(einvoiceNo.equals(invoiceNo)) {
                    paymentFees.add(cDetails);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } return paymentFees;
    }
    
    public List<String> extractUnitUsers(String unitNo) {
        List<String> userName = new ArrayList<String>();
        try {
            List<String> usersList = fh.fileRead("userProfile.txt");
            String[] usersArray = new String[usersList.size()];
            usersList.toArray(usersArray);
            
            for (int i=1; i<usersList.size(); i++) {
                String[] userDetails = usersArray[i].split(";");
                String firstName = userDetails[3];
                String lastName = userDetails[4];
                String eunitNo = userDetails[8];
                
                if(eunitNo.equals(unitNo)) {
                    userName.add(firstName +" "+ lastName);
                }
            } 
        } catch (Exception e) {
            e.printStackTrace();
        } return userName;
    }
    
    public List<String> extractUnitUsersDetails(String userName) {
        List<String> userFullDetails = new ArrayList<String>();
        try {
            List<String> usersList = fh.fileRead("userProfile.txt");
            String[] usersArray = new String[usersList.size()];
            usersList.toArray(usersArray);
            
            for (int i=1; i<usersList.size(); i++) {
                String[] userDetails = usersArray[i].split(";");
                String eUserName = userDetails[3] +" "+ userDetails[4];
                String userID = userDetails[0];
                String phoneNo = userDetails[7];
                String email = userDetails[1];
                String identificationNo = userDetails[5];
                if(userName.equals(eUserName)) {
                    userFullDetails.add(userID +";"+ phoneNo +";"+ email +";"+
                            identificationNo+";");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } return userFullDetails;
    }
    
    public void storePayment(String invoiceNo, String userID) {
        try {
            List<String> invoicesList = fh.fileRead("invoices.txt");
            String[] invoicesArray = new String[invoicesList.size()];
            invoicesList.toArray(invoicesArray);
            
            List<String> newData = new ArrayList<>();
            
            for (int i=1; i<invoicesList.size(); i++) {
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
                    newData.add(einvoiceNo +";"+ unitNo +";"+ feeType +";"+ target +";"+
                            consump +";"+ unit +";"+ unitPrice +";"+ totalPrice +";"+
                            period +";"+ userID +";"+ todayDate +";"+ generatedDate +";");
                }
            } fh.fileWrite("payment.txt", true, newData);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<String> extractReceipt(String status) {
        List<String> receiptList = new ArrayList<String>();
        try {
            List<String> eReceiptList = fh.fileRead("receipt.txt");
            String[] receiptArray = new String[eReceiptList.size()];
            eReceiptList.toArray(receiptArray);
            
            List<String> usersList = fh.fileRead("userProfile.txt");
            String[] usersArray = new String[usersList.size()];
            usersList.toArray(usersArray);
            
            if (status.equals("PENDING")) {
                List<String> paymentList = fh.fileRead("payment.txt");
                String[] paymentArray = new String[paymentList.size()];
                paymentList.toArray(paymentArray);
                
                for (int i=1; i<paymentList.size(); i++) {
                    boolean check = true;
                    String[] invoiceDetails = paymentArray[i].split(";");
                    String iInvoiceNo = invoiceDetails[0];
                    String iUnitNo = invoiceDetails[1];
                    String iFeeType = invoiceDetails[2];
                    String iTotalPrice = invoiceDetails[7];
                    String paidBy = invoiceDetails[9];
                    String paymentDate = invoiceDetails[10];
                    
                    for (int j=1; j<eReceiptList.size(); j++) {
                        String[] paymentDetails = receiptArray[j].split(";");
                        String pInvoiceNo = paymentDetails[0];
                        String pFeeType = paymentDetails[2];
                        if (iInvoiceNo.equals(pInvoiceNo) && iFeeType.equals(pFeeType)) {
                            check = false;
                        }
                    } 
                    if (check == true) {
                        String paidByName = null;
                        for (int j=1; j<usersList.size(); j++) {
                            String[] userDetails = usersArray[j].split(";");
                            String userID = userDetails[0].toUpperCase();
                            String userName = userDetails[3] +" "+ userDetails[4];
                            if(userID.equals(paidBy)) {
                                paidByName = userName;
                            }
                        } 
                        String cReceiptDetails = iInvoiceNo +";"+ iUnitNo +";"+ 
                                iFeeType +";"+ iTotalPrice +";"+ paymentDate +";"+ 
                                paidByName +";";
                        receiptList.add(cReceiptDetails);
                    }
                } 
                
            } else if (status.equals("ISSUED")) {
                for (int i=1; i<eReceiptList.size(); i++) {
                    String[] receiptDetails = receiptArray[i].split(";");
                    String invoiceNo = receiptDetails[0];
                    String unitNo = receiptDetails[1];
                    String feeType = receiptDetails[2];
                    String totalPrice = receiptDetails[7];
                    String paidBy = receiptDetails[9];
                    String paymentDate = receiptDetails[10];
                    
                    String paidByName = null;
                    for (int j=1; j<usersList.size(); j++) {
                        String[] userDetails = usersArray[j].split(";");
                        String userID = userDetails[0].toUpperCase();
                        String userName = userDetails[3] +" "+ userDetails[4];
                        if(userID.equals(paidBy)) {
                            paidByName = userName;
                        }
                    } 
                    String cReceiptDetails = invoiceNo +";"+ unitNo +";"+ 
                            feeType +";"+ totalPrice +";"+ paymentDate +";"+ 
                            paidByName +";";
                        receiptList.add(cReceiptDetails);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiptList;
    }
    
    public void issueReceipt(List<String> receiptDetails) {
        String[] receiptDetailsArray = new String[receiptDetails.size()];
        receiptDetails.toArray(receiptDetailsArray);
        
        try {
            List<String> newData = new ArrayList<>();

            for (int i=0; i <receiptDetails.size(); i++) {
                String[] receiptFullDetails = receiptDetailsArray[i].split(";");
                String invoiceNo = receiptFullDetails[0];
                String feeType = receiptFullDetails[1];
                
                List<String> paymentList = fh.fileRead("payment.txt");
                String[] paymentArray = new String[paymentList.size()];
                paymentList.toArray(paymentArray);
                
                for (int j=1; j<paymentList.size(); j++) {
                    String[] paymentFullDetails = paymentArray[j].split(";");
                    String pInvoiceNo = paymentFullDetails[0];
                    String pFeeType = paymentFullDetails[2];
                    
                    if (pInvoiceNo.equals(invoiceNo) && pFeeType.equals(feeType)) {
                        newData.add(paymentArray[j]);
                    }
                } 
            } fh.fileWrite("receipt.txt", true, newData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<String> calculateTotalOutstanding(String monthYear) {
        List<String> availablePending = extractAllPayment("PENDING");
        String[] availablePendingArray = new String[availablePending.size()];
        availablePending.toArray(availablePendingArray);
        
        List<String> availableInvoiceNo = new ArrayList<>();
        for (int i=0; i <availablePending.size(); i++) {
            String[] pendingPayment = availablePendingArray[i].split(";");
            String invoiceNo = pendingPayment[0];
            String unitNo = pendingPayment[2];
            if (!availableInvoiceNo.contains(invoiceNo) &&
                    invoiceNo.equals(unitNo+monthYear)) {
                availableInvoiceNo.add(invoiceNo);
            }
        }
        
        List<String> availablePendingPayment = new ArrayList<>();
        for (int i=0; i< availableInvoiceNo.size(); i++) {
            String invoiceNo = availableInvoiceNo.get(i);
            
            String tUnitNo = ""; String tInvoiceNo = "";
            String tIssuedDate = ""; float totalPrice = 0;
            
            for (int j=0; j<availablePending.size(); j++) {
                String[] pendingPayment = availablePendingArray[j].split(";");
                String eInvoiceNo = pendingPayment[0];
                float eTotalPrice = Float.valueOf(pendingPayment[4]);
                
                if (invoiceNo.equals(eInvoiceNo)) {
                    totalPrice += eTotalPrice;
                    tUnitNo = pendingPayment[2];
                    tInvoiceNo = pendingPayment[0];
                    tIssuedDate = pendingPayment[1];
                }
            } String cDetials = tUnitNo +";"+ tInvoiceNo +";"+ tIssuedDate +";"+
                    String.valueOf(totalPrice) + ";";
            availablePendingPayment.add(cDetials);
        } 
        return availablePendingPayment;
    }
    
    public List<String> getAllMonthYearInvoice() {
        List<String> availableMonthYear = new ArrayList<>();
        try {
            List<String> invoicesList = fh.fileRead("invoices.txt");
            String[] invoicesArray = new String[invoicesList.size()];
            invoicesList.toArray(invoicesArray);

            for (int i=1; i< invoicesList.size(); i++) {
                String[] invoiceDetails = invoicesArray[i].split(";");
                String period = invoiceDetails[8];
                if (!availableMonthYear.contains(period)) {
                    availableMonthYear.add(period);
                }
            } 
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return availableMonthYear;
    }
    
    public List<String> getAllMonthYearStatement() {
        List<String> monthNYear = new ArrayList<String>();
        String todayDate = todayDate();
        
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy") ;
        LocalDate ld = LocalDate.parse(todayDate, f);
        int m = ld.getMonthValue();
        int y = ld.getYear() ;
        
        int rm = 11;
        int ry = 2022;
        int tempM = rm;
        for (int i=ry; i<(y+1); i++) {
            for (int j=1; j<13; j++) {
                if (i < y) {
                    if (tempM == j) {
                        monthNYear.add(tempM +"/"+ i);
                        tempM++;
                        if (tempM > 12) {
                            tempM = 1;
                        }
                    }
                } else {
                    if (tempM < m) {
                        monthNYear.add(tempM +"/"+ i);
                        tempM++;
                    }
                }
            }
        } return monthNYear;
    }
    
    public String getInvoiceLatePayment(String invoiceNo) {
        String latePaymentFee = "";
        try{
            List<String> invoicesList = fh.fileRead("invoices.txt");
            String[] invoicesArray = new String[invoicesList.size()];
            invoicesList.toArray(invoicesArray);

            for (int i=1; i< invoicesList.size(); i++) {
                String[] invoiceDetails = invoicesArray[i].split(";");
                String eInvoiceNo = invoiceDetails[0];
                String feeType = invoiceDetails[2];
                String totalPrice = invoiceDetails[7];
                
                if (eInvoiceNo.equals(invoiceNo) && feeType.equals("Late Payment Charges")) {
                    latePaymentFee = totalPrice;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return latePaymentFee;
    }
    
    public void chargeLatePaymentFee(String invoiceNo, String latePaymentFee) {
        try{
            List<String> invoicesList = fh.fileRead("invoices.txt");
            String[] invoicesArray = new String[invoicesList.size()];
            invoicesList.toArray(invoicesArray);
            boolean check = false;
            
            for (int i=1; i< invoicesList.size(); i++) {
                String[] invoiceDetails = invoicesArray[i].split(";");
                String eInvoiceNo = invoiceDetails[0];
                String feeType = invoiceDetails[2];
                
                if (eInvoiceNo.equals(invoiceNo) && feeType.equals("Late Payment Charges")) {
                    check = true;
                }
            } 
            
            List<String> newData = new ArrayList<>();
            
            if (check == true) {
                for (int i=1; i< invoicesList.size(); i++) {
                    String[] invoiceDetails = invoicesArray[i].split(";");
                    String nInvoiceNo = invoiceDetails[0];
                    String nUnitNo = invoiceDetails[1];
                    String nFeeType = invoiceDetails[2];
                    String nPeriod = invoiceDetails[8];
                    if (nInvoiceNo.equals(invoiceNo) &&
                            nFeeType.equals("Late Payment Charges")) {
                        
                        String generatedDate = new AccountExecutive().todayDate();
                        newData.add(invoiceNo +";"+ nUnitNo +";"+ nFeeType +";"+ "-" +";"+
                                "-" +";"+ "-" +";"+ "-" +";"+ latePaymentFee +";"+
                                nPeriod +";"+ generatedDate+";");
                    } else {
                        newData.add(invoicesArray[i]);
                    }
                } fh.fileWrite("temp.txt", true, newData);
                
            } else if (check == false) {
                String nPeriod = invoiceNo.substring(invoiceNo.length()-6);
                String period = nPeriod.substring(0, 2) + "/" + nPeriod.substring(2);
                String nUnitNo = invoiceNo.substring(0, invoiceNo.length()-6);
                
                String generatedDate = new AccountExecutive().todayDate();
                newData.add(invoiceNo +";"+ nUnitNo +";"+ "Late Payment Charges" +";"+ "-" 
                        +";"+ "-" +";"+ "-" +";"+ "-" +";"+ latePaymentFee +";"+
                        period +";"+ generatedDate+";");
                
                fh.fileWrite("invoices.txt", true, newData);
            } 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<String> extractAllStatementUnit(String status, String monthYear) {
        List<String> availableUnit = new ArrayList<>();
        List<String> availableStatements = new ArrayList<>();
        try {
            List<String> invoicesList = fh.fileRead("invoices.txt");
            String[] invoicesArray = new String[invoicesList.size()];
            invoicesList.toArray(invoicesArray);
            
            if (status.equals("PENDING")) {
                for (int i=1; i< invoicesList.size(); i++) {
                    String[] invoiceDetails = invoicesArray[i].split(";");
                    String eInvoiceNo = invoiceDetails[0];
                    String unitNo = invoiceDetails[1];
                    String eIssuedDate = invoiceDetails[9];
                    String issuedDate = eIssuedDate.substring(eIssuedDate.length()-7);
                    
                    boolean check = false;
                    if (issuedDate.equals(monthYear)) {
                        List<String> statementsList = fh.fileRead("invoices.txt");
                        String[] statementsArray = new String[statementsList.size()];
                        statementsList.toArray(statementsArray);
                        
                        for (int j=1; j< statementsList.size(); j++) {
                            String[] statementDetails = statementsArray[j].split(";");
                            String sInvoiceNo = statementDetails[0];
                            if (sInvoiceNo.equals(eInvoiceNo)) {
                                check = true;
                            }
                        } 
                        if (check == false && !availableUnit.contains(unitNo)) {
                            availableUnit.add(unitNo);
                        }
                    }
                } 
                availableStatements = extractStatementUnitDetails(availableUnit);
                
            } else if (status.equals("ISSUED")) {
                for (int i=1; i< invoicesList.size(); i++) {
                    String[] statementDetails = invoicesArray[i].split(";");
                    String invoiceNo = statementDetails[0];
                    String sUnitNo = statementDetails[1];
                    
                    String mYear = invoiceNo.substring(invoiceNo.length()-6);
                    String period = mYear.substring(0, 2) +"/"+ mYear.substring(2);
                    
                    if (!availableUnit.contains(sUnitNo) && period.equals(monthYear)) {
                        availableUnit.add(sUnitNo);
                    }
                } 
                availableStatements = extractStatementUnitDetails(availableUnit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return availableStatements;
    }
    
    public List<String> extractStatementUnitDetails(List<String> availableUnit) {
        List<String> availableStatements = new ArrayList<>();
        
        try {
            for (String unitNo : availableUnit) {
                String owner = "-"; String resident = "-"; String target = "";
                List<String> invoicesList = fh.fileRead("invoices.txt");
                String[] invoicesArray = new String[invoicesList.size()];
                invoicesList.toArray(invoicesArray);
                
                for (int i=1; i< invoicesList.size(); i++) {
                    String[] invoiceDetails = invoicesArray[i].split(";");
                    String iUnitNo = invoiceDetails[1];
                    
                    if (iUnitNo.equals(unitNo) && iUnitNo.startsWith("S")) {
                        target = "Commercial";
                    } else if (iUnitNo.equals(unitNo)) {
                        target = "Residential";
                    }
                } 
                
                List<String> usersList = fh.fileRead("userProfile.txt");
                String[] usersArray = new String[usersList.size()];
                usersList.toArray(usersArray);

                for (int i=1; i< usersList.size(); i++) {
                    String[] userDetails = usersArray[i].split(";");
                    String userID = userDetails[0];
                    String firstName = userDetails[3];
                    String lastName = userDetails[4];
                    String uUnitNo = userDetails[8];
                    
                    if (uUnitNo.equals(unitNo) && userID.startsWith("rsd")) {
                        resident = firstName +" "+ lastName;
                    } else if (uUnitNo.equals(unitNo) && userID.startsWith("tnt")) {
                        owner = firstName +" "+ lastName;
                    } else if (uUnitNo.equals(unitNo) && userID.startsWith("vdr")) {
                        owner = firstName +" "+ lastName;
                    }
                } 
                availableStatements.add(unitNo +";"+ target +";"+ owner +";"+ resident);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return availableStatements;
    }
    
    public void issueStatement(List<String> availableStatement) {
        try {
            List<String> newData = new ArrayList<>();
            String todayDate = todayDate();
            for (String statements : availableStatement) {
                newData.add(statements + todayDate);
            } fh.fileWrite("statements.txt", true, newData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}    
    
   

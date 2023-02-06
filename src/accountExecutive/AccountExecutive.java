/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package accountExecutive;

import java.util.List;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import pms_parkhill_residence.FileHandling;
import pms_parkhill_residence.PMS_DateTimeFormatter;
import pms_parkhill_residence.TextFiles;
import pms_parkhill_residence.Users;
import residentANDtenant.ResidentTenant;

/**
 *
 * @author wongj
 */
public class AccountExecutive extends Users {

    FileHandling fh = new FileHandling();

    public List<String> extractFeeTypes(String fileName) {
        List<String> feeTypes = new ArrayList<>();

        List<String> feeTypesList = fh.fileRead(fileName);
        String[] feeTypesArray = new String[feeTypesList.size()];
        feeTypesList.toArray(feeTypesArray);

        for (int i = 1; i < feeTypesList.size(); i++) {
            feeTypes.add(feeTypesArray[i]);
        }
        return feeTypes;
    }

    public boolean storeNewFeeTypes(String feeTypesName, String target, String unit,
            String unitPrice, String category) {
        List<String> feeTypesList = fh.fileRead("feeTypes.txt");
        String[] feeTypesArray = new String[feeTypesList.size()];
        feeTypesList.toArray(feeTypesArray);

        for (int i = 1; i < feeTypesList.size(); i++) {
            String[] feeDetails = feeTypesArray[i].split(";");
            String existingFeeName = feeDetails[0].toLowerCase();
            String existingTarget = feeDetails[1];
            if (existingFeeName.equals(feeTypesName.toLowerCase())
                    && existingTarget.equals(target)) {
                return false;
            }
        }
        List<String> newData = new ArrayList<>();
        String date = todayDate();
        newData.add(feeTypesName + ";" + target + ";" + unit + ";" + unitPrice
                + ";" + date + ";"+ category + ";");
        fh.fileWrite("feeTypes.txt", true, newData);
        return true;
    }

    public void modifyFeeTypes(String feeTypesName, String target, String unit,
            String unitPrice, String category) {
        List<String> feeTypesList = fh.fileRead("feeTypes.txt");
        String[] feeTypesArray = new String[feeTypesList.size()];
        feeTypesList.toArray(feeTypesArray);

        List<String> newData = new ArrayList<>();

        for (int i = 0; i < feeTypesList.size(); i++) {
            String[] feeTypes = feeTypesArray[i].split(";");
            String eFeeTypesName = feeTypes[0];
            String eTarget = feeTypes[1];
            String createdDate = feeTypes[4];
            if (eFeeTypesName.equals(feeTypesName) && eTarget.equals(target)) {
                newData.add(eFeeTypesName + ";" + eTarget + ";" + unit + ";"
                        + unitPrice + ";" + createdDate + ";"+ category + ";");
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

        for (int i = 0; i < feeTypesList.size(); i++) {
            String[] feeTypes = feeTypesArray[i].split(";");
            String eFeeTypesName = feeTypes[0];
            String eTarget = feeTypes[1];
            if (eFeeTypesName.equals(feeTypesName) && eTarget.equals(target)) {

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

        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate ld = LocalDate.parse(todayDate, f);
        int m = ld.getMonthValue();
        int y = ld.getYear();

        String referenceDate = "";
        List<String> feeTypesList = fh.fileRead("feeTypes.txt");
        String[] feeTypesArray = new String[feeTypesList.size()];
        feeTypesList.toArray(feeTypesArray);

        for (int i = 1; i < feeTypesList.size(); i++) {
            String[] feeTypes = feeTypesArray[i].split(";");
            String eFeeTypesName = feeTypes[0];
            String eTarget = feeTypes[1];
            String createdDate = feeTypes[4];
            if (eFeeTypesName.equals(feeTypeName) && eTarget.equals(target)) {
                referenceDate = createdDate;
            }
        }

        LocalDate ld2 = LocalDate.parse(referenceDate, f);
        int rm = ld2.getMonthValue();
        int ry = ld2.getYear();
        int tempM = rm;
        for (int i = ry; i < (y + 1); i++) {
            for (int j = 1; j < 13; j++) {
                if (i < y) {
                    if (tempM == j) {
                        monthNYear.add(tempM + "/" + i);
                        tempM++;
                        if (tempM > 12) {
                            tempM = 1;
                        }
                    }
                } else {
                    if (tempM < m) {
                        monthNYear.add(tempM + "/" + i);
                        tempM++;
                    }
                }
            }
        }
        return monthNYear;
    }

    public List<String> extractInvoiceDetails(String feeTypeName, String target,
            String status, String monthYear) {

        List<String> feeTypesList = fh.fileRead("feeTypes.txt");
        String[] feeTypesArray = new String[feeTypesList.size()];
        feeTypesList.toArray(feeTypesArray);

        String cUnit = "";
        String cUnitPrice = "";
        for (int i = 1; i < feeTypesList.size(); i++) {

            String[] feeTypesDetails = feeTypesArray[i].split(";");
            String feeType = feeTypesDetails[0];
            String eTarget = feeTypesDetails[1];
            String unit = feeTypesDetails[2];
            String unitPrice = feeTypesDetails[3];

            if (feeType.equals(feeTypeName) && eTarget.equals(target)) {
                cUnit = unit;
                cUnitPrice = unitPrice;
            }
        }

        List<String> availableInvoices = new ArrayList<String>();
        if (status.equals("PENDING")) {
            List<String> propertyDetailsList = fh.fileRead("propertyDetails.txt");
            String[] propertyDetailsArray = new String[propertyDetailsList.size()];
            propertyDetailsList.toArray(propertyDetailsArray);
            String cMonthYear = monthYear.substring(0, monthYear.indexOf("/"))
                    + monthYear.substring(monthYear.indexOf("/") + 1);

            for (int i = 1; i < propertyDetailsList.size(); i++) {
                boolean check = false;
                String[] propertyDetails = propertyDetailsArray[i].split(";");
                String eUnitNo = propertyDetails[0];
                String eTarget = propertyDetails[1];
                String squareFeet = propertyDetails[2];

                List<String> invoicesList = fh.fileRead("invoices.txt");
                String[] invoicesArray = new String[invoicesList.size()];
                invoicesList.toArray(invoicesArray);

                for (int j = 1; j < invoicesList.size(); j++) {
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
                        String invoiceLine = eUnitNo + ";" + squareFeet + ";"
                                + cUnit + ";" + cUnitPrice + ";" + String.valueOf(totalPrice2);
                        availableInvoices.add(invoiceLine);
                    } else {
                        float consump = (new Random().nextFloat() * (250.00f - 0.00f)) + 0.00f;
                        DecimalFormat df = new DecimalFormat("0.00");
                        float totalPrice = consump * Float.valueOf(cUnitPrice);
                        String totalPrice2 = df.format(totalPrice);
                        String consump2 = df.format(consump);
                        String invoiceLine = eUnitNo + ";" + consump2 + ";"
                                + cUnit + ";" + cUnitPrice + ";" + String.valueOf(totalPrice2);
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

            for (int i = 1; i < propertyDetailsList.size(); i++) {
                String[] propertyDetails = propertyDetailsArray[i].split(";");
                String eUnitNo = propertyDetails[0];
                String eTarget = propertyDetails[1];

                List<String> invoicesList = fh.fileRead("invoices.txt");
                String[] invoicesArray = new String[invoicesList.size()];
                invoicesList.toArray(invoicesArray);

                for (int j = 1; j < invoicesList.size(); j++) {
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
                        String invoiceLine = unitNo + ";" + consump + ";"
                                + unit + ";" + unitPrice + ";" + totalPrice;
                        availableInvoices.add(invoiceLine);
                    }
                }
            }
        }
        return availableInvoices;
    }

    public void issueInvoice(List<String> invoiceDetails) {
        String[] invoiceDetailsArray = new String[invoiceDetails.size()];
        invoiceDetails.toArray(invoiceDetailsArray);

        List<String> newData = new ArrayList<>();

        for (int i = 0; i < invoiceDetails.size(); i++) {
            newData.add(invoiceDetailsArray[i]);
        }
        fh.fileWrite("invoices.txt", true, newData);
    }

    public void createUserTransactionLink(String period, String unitNo) {
        String transDate = todayDate().substring(4);
        String[] availableData = {period, transDate};
        
        List<String> userList = fh.fileRead("userProfile.txt");
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);

        String tenantID = "-";
        String residentID = "-";
        for (int i = 1; i < userList.size(); i++) {
            String[] userDetails = userArray[i].split(";");
            String eUserID = userDetails[0];
            String eUnitNo = userDetails[8];

            if (unitNo.equals(eUnitNo) && eUserID.startsWith("tnt")) {
                tenantID = eUserID;
            } else if (unitNo.equals(eUnitNo) && eUserID.startsWith("vdr")) {
                tenantID = eUserID;
            } else if (unitNo.equals(eUnitNo) && eUserID.startsWith("rsd")) {
                residentID = eUserID;
            }
        }

        for (String data : availableData) {
            List<String> uTLinkList = fh.fileRead("userTransactionLink.txt");
            String[] uTLinkArray = new String[uTLinkList.size()];
            uTLinkList.toArray(uTLinkArray);
            boolean check = false;
            for (int i = 1; i < uTLinkList.size(); i++) {
                String[] uTDetails = uTLinkArray[i].split(";");
                String lPeriod = uTDetails[0];
                String lUnitNo = uTDetails[1];
                if (lPeriod.equals(data) && lUnitNo.equals(unitNo)) {
                    check = true;
                }
            }
            if (check == false) {
                List<String> cDetails = new ArrayList<>();
                cDetails.add(data + ";" + unitNo + ";" + tenantID + ";" + residentID + ";");
                fh.fileWrite("userTransactionLink.txt", true, cDetails);
            }
        }
        
    }

    public List<String> extractAllPayment(String status) {
        List<String> pendingPaymentList = new ArrayList<String>();
        try {
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

    public List<String> extractPaymentFees(String invoiceNo) {
        List<String> paymentFees = new ArrayList<String>();
        try {
            List<String> invoicesList = fh.fileRead("invoices.txt");
            String[] invoicesArray = new String[invoicesList.size()];
            invoicesList.toArray(invoicesArray);
            
            List<String> paidList = fh.fileRead("payment.txt");
            
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

    public List<String> extractUnitUsers(String unitNo) {
        List<String> userName = new ArrayList<String>();
        try {
            List<String> usersList = fh.fileRead("userProfile.txt");
            String[] usersArray = new String[usersList.size()];
            usersList.toArray(usersArray);

            for (int i = 1; i < usersList.size(); i++) {
                String[] userDetails = usersArray[i].split(";");
                String firstName = userDetails[3];
                String lastName = userDetails[4];
                String eunitNo = userDetails[8];

                if (eunitNo.equals(unitNo)) {
                    userName.add(firstName + " " + lastName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userName;
    }

    public List<String> extractUnitUsersDetails(String userName) {
        List<String> userFullDetails = new ArrayList<String>();
        try {
            List<String> usersList = fh.fileRead("userProfile.txt");
            String[] usersArray = new String[usersList.size()];
            usersList.toArray(usersArray);

            for (int i = 1; i < usersList.size(); i++) {
                String[] userDetails = usersArray[i].split(";");
                String eUserName = userDetails[3] + " " + userDetails[4];
                String userID = userDetails[0];
                String phoneNo = userDetails[7];
                String email = userDetails[1];
                String identificationNo = userDetails[5];
                if (userName.equals(eUserName)) {
                    userFullDetails.add(userID + ";" + phoneNo + ";" + email + ";"
                            + identificationNo + ";");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userFullDetails;
    }

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

    public List<String> extractReceipt(String status) {
        List<String> receiptList = new ArrayList<String>();
        try {
            List<String> eReceiptList = fh.fileRead("receipt.txt");
            String[] receiptArray = new String[eReceiptList.size()];
            eReceiptList.toArray(receiptArray);

            List<String> usernameList = new ArrayList<>();
            List<String> userIDList = new ArrayList<>();

            List<String> usersList = fh.fileRead("userProfile.txt");
            String[] usersArray = new String[usersList.size()];
            usersList.toArray(usersArray);
            for (int i = 1; i < usersList.size(); i++) {
                
                String[] userDetails = usersArray[i].split(";");
                String euserID = userDetails[0];
                String euserName = userDetails[3] + " " + userDetails[4];
                usernameList.add(euserName);
                userIDList.add(euserID);
            }
            
            List<String> iUsersList = fh.fileRead("inactiveUserProfile.txt");
            String[] iUsersArray = new String[iUsersList.size()];
            iUsersList.toArray(iUsersArray);
            for (int i = 1; i < iUsersList.size(); i++) {
                String[] userDetails = iUsersArray[i].split(";");
                String euserID = userDetails[1].toUpperCase();
                String euserName = userDetails[4] + " " + userDetails[5];
                usernameList.add(euserName);
                userIDList.add(euserID);
            }

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

    public List<String> calculateTotalOutstanding(String monthYear) {
        List<String> availablePending = extractAllPayment("PENDING");
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

    public List<String> getAllMonthYearInvoice() {
        List<String> availableMonthYear = new ArrayList<>();
        try {
            List<String> invoicesList = fh.fileRead("invoices.txt");
            String[] invoicesArray = new String[invoicesList.size()];
            invoicesList.toArray(invoicesArray);

            for (int i = 1; i < invoicesList.size(); i++) {
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

        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate ld = LocalDate.parse(todayDate, f);
        int m = ld.getMonthValue();
        int y = ld.getYear();

        int rm = 11;
        int ry = 2022;
        int tempM = rm;
        for (int i = ry; i < (y + 1); i++) {
            for (int j = 1; j < 13; j++) {
                if (i < y) {
                    if (tempM == j) {
                        monthNYear.add(tempM + "/" + i);
                        tempM++;
                        if (tempM > 12) {
                            tempM = 1;
                        }
                    }
                } else {
                    if (tempM < m) {
                        monthNYear.add(tempM + "/" + i);
                        tempM++;
                    }
                }
            }
        }
        return monthNYear;
    }

    public String getInvoiceLatePayment(String invoiceNo) {
        String latePaymentFee = "";
        try {
            List<String> invoicesList = fh.fileRead("invoices.txt");
            String[] invoicesArray = new String[invoicesList.size()];
            invoicesList.toArray(invoicesArray);

            for (int i = 1; i < invoicesList.size(); i++) {
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
        try {
            List<String> invoicesList = fh.fileRead("invoices.txt");
            String[] invoicesArray = new String[invoicesList.size()];
            invoicesList.toArray(invoicesArray);
            String unitNo = "-";
            String period = "-";
            boolean check = false;

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

            if (check == true) {
                for (int i = 1; i < invoicesList.size(); i++) {
                    String[] invoiceDetails = invoicesArray[i].split(";");
                    String nInvoiceNo = invoiceDetails[0];
                    String nUnitNo = invoiceDetails[1];
                    String nFeeType = invoiceDetails[2];
                    String nPeriod = invoiceDetails[8];
                    if (nInvoiceNo.equals(invoiceNo)
                            && nFeeType.equals("Late Payment Charges")) {

                        String generatedDate = new AccountExecutive().todayDate();
                        newData.add(invoiceNo + ";" + nUnitNo + ";" + nFeeType + ";" + "-" + ";"
                                + "-" + ";" + "-" + ";" + "-" + ";" + latePaymentFee + ";"
                                + nPeriod + ";" + generatedDate + ";" + "-" +";");
                    } else {
                        newData.add(invoicesArray[i]);
                    }
                }
                fh.fileWrite("invoices.txt", false, newData);

            } else if (check == false) {
                String generatedDate = new AccountExecutive().todayDate();
                newData.add(invoiceNo + ";" + unitNo + ";" + "Late Payment Charges" + ";" + "-"
                        + ";" + "-" + ";" + "-" + ";" + "-" + ";" + latePaymentFee + ";"
                        + period + ";" + generatedDate + ";"+ "-" +";");

                fh.fileWrite("invoices.txt", true, newData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public List<String> extractAllStatementUnit(String status, String monthYear) {
//        List<String> availableInvoice = new ArrayList<>();
//        List<String> availableStatements = new ArrayList<>();
//        try {
//            List<String> invoicesList = fh.fileRead("invoices.txt");
//            String[] invoicesArray = new String[invoicesList.size()];
//            invoicesList.toArray(invoicesArray);
//            
//            List<String> statementsList = fh.fileRead("statements.txt");
//            String[] statementsArray = new String[statementsList.size()];
//            statementsList.toArray(statementsArray);
//
//            if (status.equals("PENDING")) {
//                for (int i = 1; i < invoicesList.size(); i++) {
//                    String[] invoiceDetails = invoicesArray[i].split(";");
//                    String eInvoiceNo = invoiceDetails[0];
//                    String eIssuedDate = invoiceDetails[9];
//                    String issuedDate_temp = eIssuedDate.substring(eIssuedDate.length() - 7);
//                    String issuedDate;
//                    if(issuedDate_temp.startsWith("0")) {
//                        issuedDate = issuedDate_temp.substring(1);
//                    } else {
//                        issuedDate = issuedDate_temp;
//                    }
//
//                    boolean check = false;
//                    if (issuedDate.equals(monthYear)) {
//                        for (int j = 1; j < statementsList.size(); j++) {
//                            String[] statementDetails = statementsArray[j].split(";");
//                            String sInvoiceNo = statementDetails[0];
//                            if (sInvoiceNo.equals(eInvoiceNo)) {
//                                check = true;
//                            }
//                        }
//                        if (check == false && !availableInvoice.contains(eInvoiceNo)) {
//                            availableInvoice.add(eInvoiceNo);
//                            
//                        }
//                    }
//                }
//                availableStatements = extractStatementUnitDetails(availableInvoice);
//
//            } else if (status.equals("ISSUED")) {
//                for (int i = 1; i < statementsList.size(); i++) {
//                    String[] statementDetails = statementsArray[i].split(";");
//                    String invoiceNo = statementDetails[0];
//                    if (!availableInvoice.contains(invoiceNo)) {
//                        availableInvoice.add(invoiceNo);
//                    }
//                }
//                availableStatements = extractStatementUnitDetails(availableInvoice);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return availableStatements;
//    }
    public List<String> extractAllStatementUnit(String status, String monthYear) {
//        List<String> transList = fh.fileRead("userTransactionLink.txt");
//        List<String> statementList = fh.fileRead("statements.txt");
//        
//        List<String> availableStatement = new ArrayList<>();
//        if(status.equals("PENDING")) {
//            for (int i = 1; i < transList.size(); i++) {
//                String[] unitDetails = transList.get(i).split(";");
//                String uPeriod = unitDetails[0];
//                String uUnitNo = unitDetails[1];
//                
//                if(uPeriod.equals(monthYear)) {
//                    boolean check = true;
//                    for (int j = 1; j < statementList.size(); j++) {
//                        String[] statementDetails = statementList.get(j).split(";");
//                        String period = statementDetails[0];
//                        String sUnitNo = statementDetails[1];
//                        if(uPeriod.equals(period) && uUnitNo.equals(sUnitNo)) {
//                            check = false;
//                        }
//                    }
//                    if(check) {
//                        availableStatement.add(monthYear +";"+ uUnitNo +";");
//                    }
//                }
//                
//            }
//        } else if(status.equals("ISSUED")) {
//            for (int i = 1; i < statementList.size(); i++) {
//                    String[] statementDetails = transList.get(i).split(";");
//                    String period = statementDetails[0];
//                    String sUnitNo = statementDetails[1];
//                    availableStatement.add(period +";"+ sUnitNo +";");
//                    
//                }
//        }
//        List<String> fullStatement = extractStatementUnitDetails
//                                        (availableStatement);
//        return fullStatement;
        
        List<String> transList = fh.fileRead("invoices.txt");
        List<String> statementList = fh.fileRead("statements.txt");
        
        List<String> availableStatement = new ArrayList<>();
        if(status.equals("PENDING")) {
            for (int i = 1; i < transList.size(); i++) {
                String[] unitDetails = transList.get(i).split(";");
                String uPeriod_temp = unitDetails[9].substring(3);
                String uPeriod;
                if(uPeriod_temp.startsWith("0")) {
                    uPeriod = uPeriod_temp.substring(1);
                } else {
                    uPeriod = uPeriod_temp;
                }
                String uUnitNo = unitDetails[1];
                if(uPeriod.equals(monthYear)) {
                    boolean check = true;
                    for (int j = 1; j < statementList.size(); j++) {
                        String[] statementDetails = statementList.get(j).split(";");
                        String period = statementDetails[0];
                        String sUnitNo = statementDetails[1];
                        if(uPeriod.equals(period) && uUnitNo.equals(sUnitNo)) {
                            check = false;
                        }
                    }
                    if(check) {
                        availableStatement.add(monthYear +";"+ uUnitNo +";");
                    }
                }
                
            }
        } else if(status.equals("ISSUED")) {
            for (int i = 1; i < statementList.size(); i++) {
                    String[] statementDetails = statementList.get(i).split(";");
                    String period = statementDetails[0];
                    String sUnitNo = statementDetails[1];
                    if(period.equals(monthYear)) {
                        availableStatement.add(period +";"+ sUnitNo +";");
                    }
            }
        }
        List<String> fullStatement = extractStatementUnitDetails
                                        (availableStatement);
        return fullStatement;
    }
    

    public List<String> extractStatementUnitDetails(List<String> statementUnit) {
        List<String> availableStatements = new ArrayList<>();
        
        List<String> usernameList = new ArrayList<>();
        List<String> userIDList = new ArrayList<>();

        List<String> usersList = fh.fileRead("userProfile.txt");
        String[] usersArray = new String[usersList.size()];
        usersList.toArray(usersArray);
        for (int i = 1; i < usersList.size(); i++) {
            String[] userDetails = usersArray[i].split(";");
            String euserID = userDetails[0].toUpperCase();
            String euserName = userDetails[3] + " " + userDetails[4];
            usernameList.add(euserName);
            userIDList.add(euserID);
        }

        List<String> iUsersList = fh.fileRead("inactiveUserProfile.txt");
        String[] iUsersArray = new String[iUsersList.size()];
        iUsersList.toArray(iUsersArray);
        for (int i = 1; i < iUsersList.size(); i++) {
            String[] userDetails = iUsersArray[i].split(";");
            String euserID = userDetails[1].toUpperCase();
            String euserName = userDetails[4] + " " + userDetails[5];
            usernameList.add(euserName);
            userIDList.add(euserID);
        }
        
        
        for (int j = 0; j<statementUnit.size(); j++) {
            String[] statementDetails = statementUnit.get(j).split(";");
            String sPeriod = statementDetails[0];
            String sUnitNo = statementDetails[1];
            
            String owner = "-";
            String resident = "-";
            String target = "";
            
            List<String> uTLinkList = fh.fileRead("userTransactionLink.txt");
            String[] uTLinkArray = new String[uTLinkList.size()];
            uTLinkList.toArray(uTLinkArray);

            for (int i = 1; i < uTLinkList.size(); i++) {
                String[] uTLinkDetails = uTLinkArray[i].split(";");
                String ePeriod = uTLinkDetails[0];
                String eUnitNo = uTLinkDetails[1];
                String eTenantID = uTLinkDetails[2];
                String eResidentID = uTLinkDetails[3];
                
                if (sPeriod.equals(ePeriod) && sUnitNo.equals(eUnitNo) &&
                        eUnitNo.startsWith("S")) {
                    target = "Commercial";
                    owner = usernameList.get(userIDList.indexOf(eTenantID.toUpperCase()));
                    if(!eResidentID.equals("-")) {
                        resident = usernameList.get(userIDList.indexOf(eResidentID.toUpperCase()));
                    }   
                } else if (sPeriod.equals(ePeriod) && sUnitNo.equals(eUnitNo)) {
                    target = "Residential";
                    owner = usernameList.get(userIDList.indexOf(eTenantID.toUpperCase()));
                    if(!eResidentID.equals("-")) {
                        resident = usernameList.get(userIDList.indexOf(eResidentID.toUpperCase()));
                    }
                }
            }
            if(!owner.equals("-")) {
                availableStatements.add(sUnitNo + ";" + target + ";" + owner + ";" + resident + ";");
            }
        }
        return availableStatements;
    }

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
    
    public void deleteTrans(String unitNo, String deleteID) {
        List<String> newData = new ArrayList<>();
        
        List<String> transList1 = fh.fileRead("invoices.txt");
        for(int i = 0; i<transList1.size(); i++) {
            String[] transDetails = transList1.get(i).split(";");
            String invoiceNo = transDetails[0];
            String eUnitNo = transDetails[1];
            String feeType = transDetails[2];
            String target = transDetails[3];
            String consumption = transDetails[4];
            String unit = transDetails[5];
            String unitPrice = transDetails[6];
            String totalPrice = transDetails[7];
            String period = transDetails[8];
            String generatedDate = transDetails[9];
            String eDeleteID = transDetails[10];
            
            if(unitNo.equals(eUnitNo) && eDeleteID.equals("-")) {
                String cDetails = invoiceNo +";"+ unitNo +";"+ feeType +";"+ target +";"+
                    consumption +";"+ unit +";"+ unitPrice +";"+ totalPrice +";"+
                    period +";"+ generatedDate +";"+ deleteID +";";
                newData.add(cDetails);
            } else {
                newData.add(transList1.get(i));
            }
        } fh.fileWrite("invoices.txt", false, newData);
        
        List<String> transList2 = fh.fileRead("payment.txt");
        newData.clear();
        for(int i = 0; i<transList2.size(); i++) {
            String[] transDetails = transList2.get(i).split(";");
            String invoiceNo = transDetails[0];
            String eUnitNo = transDetails[1];
            String feeType = transDetails[2];
            String target = transDetails[3];
            String consumption = transDetails[4];
            String unit = transDetails[5];
            String unitPrice = transDetails[6];
            String totalPrice = transDetails[7];
            String period = transDetails[8];
            String payee = transDetails[9];
            String paymentDate = transDetails[10];
            String generatedDate = transDetails[11];
            String eDeleteID = transDetails[12];
            
            if(unitNo.equals(eUnitNo) && eDeleteID.equals("-")) {
                String cDetails = invoiceNo +";"+ unitNo +";"+ feeType +";"+ target +";"+
                    consumption +";"+ unit +";"+ unitPrice +";"+ totalPrice +";"+
                    period +";"+ payee +";"+ paymentDate +";"+ generatedDate +";"+
                        deleteID +";";
                newData.add(cDetails);
            } else {
                newData.add(transList2.get(i));
            }
        } fh.fileWrite("payment.txt", false, newData);
        
        List<String> transList3 = fh.fileRead("receipt.txt");
        newData.clear();
        for(int i = 0; i<transList3.size(); i++) {
            String[] transDetails = transList3.get(i).split(";");
            String invoiceNo = transDetails[0];
            String eUnitNo = transDetails[1];
            String feeType = transDetails[2];
            String target = transDetails[3];
            String consumption = transDetails[4];
            String unit = transDetails[5];
            String unitPrice = transDetails[6];
            String totalPrice = transDetails[7];
            String period = transDetails[8];
            String payee = transDetails[9];
            String paymentDate = transDetails[10];
            String generatedDate = transDetails[11];
            String eDeleteID = transDetails[12];
            
            if(unitNo.equals(eUnitNo) && eDeleteID.equals("-")) {
                String cDetails = invoiceNo +";"+ unitNo +";"+ feeType +";"+ target +";"+
                    consumption +";"+ unit +";"+ unitPrice +";"+ totalPrice +";"+
                    period +";"+ payee +";"+ paymentDate +";"+ generatedDate +";"+
                        deleteID +";";
                newData.add(cDetails);
            } else {
                newData.add(transList3.get(i));
            }
        } fh.fileWrite("receipt.txt", false, newData);
        
        List<String> transList4 = fh.fileRead("statements.txt");
        newData.clear();
        for(int i = 0; i<transList4.size(); i++) {
            String[] transDetails = transList4.get(i).split(";");
            String period = transDetails[0];
            String eUnitNo = transDetails[1];
            String issuedDate = transDetails[2];
            String eDeleteID = transDetails[3];
            
            if(unitNo.equals(eUnitNo) && eDeleteID.equals("-")) {
                String cDetails = period +";"+ eUnitNo +";"+ issuedDate +";"+
                        deleteID +";";
                newData.add(cDetails);
            } else {
                newData.add(transList4.get(i));
            }
        } fh.fileWrite("statements.txt", false, newData);
    }
    
    public void restoreTrans(String deleteID) {
        List<String> newData = new ArrayList<>();
        
        List<String> transList1 = fh.fileRead("invoices.txt");
        for(int i = 0; i<transList1.size(); i++) {
            String[] transDetails = transList1.get(i).split(";");
            String invoiceNo = transDetails[0];
            String eUnitNo = transDetails[1];
            String feeType = transDetails[2];
            String target = transDetails[3];
            String consumption = transDetails[4];
            String unit = transDetails[5];
            String unitPrice = transDetails[6];
            String totalPrice = transDetails[7];
            String period = transDetails[8];
            String generatedDate = transDetails[9];
            String eDeleteID = transDetails[10];
            
            if(eDeleteID.equals(deleteID)) {
                String cDetails = invoiceNo +";"+ eUnitNo +";"+ feeType +";"+ target +";"+
                    consumption +";"+ unit +";"+ unitPrice +";"+ totalPrice +";"+
                    period +";"+ generatedDate +";"+ "-" +";";
                newData.add(cDetails);
            } else {
                newData.add(transList1.get(i));
            }
        } fh.fileWrite("invoices.txt", false, newData);
        
        List<String> transList2 = fh.fileRead("payment.txt");
        newData.clear();
        for(int i = 0; i<transList2.size(); i++) {
            String[] transDetails = transList2.get(i).split(";");
            String invoiceNo = transDetails[0];
            String eUnitNo = transDetails[1];
            String feeType = transDetails[2];
            String target = transDetails[3];
            String consumption = transDetails[4];
            String unit = transDetails[5];
            String unitPrice = transDetails[6];
            String totalPrice = transDetails[7];
            String period = transDetails[8];
            String payee = transDetails[9];
            String paymentDate = transDetails[10];
            String generatedDate = transDetails[11];
            String eDeleteID = transDetails[12];
            
            if(eDeleteID.equals(deleteID)) {
                String cDetails = invoiceNo +";"+ eUnitNo +";"+ feeType +";"+ target +";"+
                    consumption +";"+ unit +";"+ unitPrice +";"+ totalPrice +";"+
                    period +";"+ payee +";"+ paymentDate +";"+ generatedDate +";"+
                        "-" +";";
                newData.add(cDetails);
            } else {
                newData.add(transList2.get(i));
            }
        } fh.fileWrite("payment.txt", false, newData);
        
        List<String> transList3 = fh.fileRead("receipt.txt");
        newData.clear();
        for(int i = 0; i<transList3.size(); i++) {
            String[] transDetails = transList3.get(i).split(";");
            String invoiceNo = transDetails[0];
            String eUnitNo = transDetails[1];
            String feeType = transDetails[2];
            String target = transDetails[3];
            String consumption = transDetails[4];
            String unit = transDetails[5];
            String unitPrice = transDetails[6];
            String totalPrice = transDetails[7];
            String period = transDetails[8];
            String payee = transDetails[9];
            String paymentDate = transDetails[10];
            String generatedDate = transDetails[11];
            String eDeleteID = transDetails[12];
            
            if(eDeleteID.equals(deleteID)) {
                String cDetails = invoiceNo +";"+ eUnitNo +";"+ feeType +";"+ target +";"+
                    consumption +";"+ unit +";"+ unitPrice +";"+ totalPrice +";"+
                    period +";"+ payee +";"+ paymentDate +";"+ generatedDate +";"+
                        "-" +";";
                newData.add(cDetails);
            } else {
                newData.add(transList3.get(i));
            }
        } fh.fileWrite("receipt.txt", false, newData);
        
        List<String> transList4 = fh.fileRead("statements.txt");
        newData.clear();
        for(int i = 0; i<transList4.size(); i++) {
            String[] transDetails = transList4.get(i).split(";");
            String period = transDetails[0];
            String eUnitNo = transDetails[1];
            String issuedDate = transDetails[2];
            String eDeleteID = transDetails[3];
            
            if(eDeleteID.equals(deleteID)) {
                String cDetails = period +";"+ eUnitNo +";"+ issuedDate +";"+
                        "-" +";";
                newData.add(cDetails);
            } else {
                newData.add(transList4.get(i));
            }
        } fh.fileWrite("statements.txt", false, newData);
    }
    
    public ArrayList getCurrentUnitPaymentHistory(String unitNo) {
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
    
    public ArrayList getCurrentUnitMonthStatement(String unitNo, String monthNyear) throws ParseException{
        ResidentTenant RT = new ResidentTenant();
        PMS_DateTimeFormatter DTF = new PMS_DateTimeFormatter();
        TextFiles TF = new TextFiles();
        
        ArrayList<String> statementList = new ArrayList<>();
        ArrayList<String> monthStatement = new ArrayList<>();
        
        // Get all issued invoice
        ArrayList<String> invoiceList = RT.getCurrentUnitIssuedInvoice(unitNo);
        
        // get all paid invoice
        ArrayList<String> completedInvoice = getCurrentUnitPaymentHistory(unitNo);
        
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
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    
    public List<String> extractFeeTypes(String fileName) throws IOException {
        List<String> feeTypes = new ArrayList<String>();
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        br.readLine();
        for (String line = br.readLine(); line != null; line = br.readLine()){
            feeTypes.add(line);
        } br.close(); fr.close(); 
        return feeTypes;    
    }
    
    public boolean storeNewFeeTypes(String feeTypesName, String target, String unit,
            String unitPrice) throws IOException {
        FileReader fr = new FileReader("feeTypes.txt");
        BufferedReader br = new BufferedReader(fr);
        br.readLine();
        for (String line = br.readLine(); line != null; line = br.readLine()){
            String[] existingFee = line.split(";");
            String existingFeeName = existingFee[0].toLowerCase();
            String existingTarget = existingFee[1];
            if (existingFeeName.equals(feeTypesName.toLowerCase()) &&
                    existingTarget.equals(target)) {
                return false;
            }
        } br.close(); fr.close();
        
        FileWriter fw = new FileWriter("feeTypes.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        
        String date = todayDate();
        bw.write(feeTypesName+";"+target+";"+unit+";"+unitPrice+";"+date+";"+"\n");
        fw.flush(); bw.flush(); fw.close(); bw.close(); return true;
    }
    
    public void modifyFeeTypes(String feeTypesName, String target, String unit,
            String unitPrice) throws IOException {
        FileReader fr = new FileReader("feeTypes.txt");
        BufferedReader br = new BufferedReader(fr);
        
        FileWriter fw = new FileWriter("temp.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            String[] feeTypes = line.split(";");
            String eFeeTypesName = feeTypes[0];
            String eTarget = feeTypes[1];
            String createdDate = feeTypes[4];
            if(eFeeTypesName.equals(feeTypesName) && eTarget.equals(target)) {
                bw.write(eFeeTypesName +";"+ eTarget +";"+ unit +";"+
                        unitPrice +";"+ createdDate +";"+"\n");
            } else {
                    bw.write(line + "\n");
            }
        } 
        fw.flush(); bw.flush(); fw.close(); bw.close();
        br.close(); fr.close();
        
        File oldFile = new File("feeTypes.txt");
        oldFile.delete();
        
        File tempFile = new File("temp.txt");
        File rename = new File("feeTypes.txt");
        tempFile.renameTo(rename);
    }
    
    public void deleteFeeTypes(String feeTypesName, String target) throws IOException {
        FileReader fr = new FileReader("feeTypes.txt");
        BufferedReader br = new BufferedReader(fr);
        
        FileWriter fw = new FileWriter("temp.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            String[] feeTypes = line.split(";");
            String eFeeTypesName = feeTypes[0];
            String eTarget = feeTypes[1];
            if(eFeeTypesName.equals(feeTypesName) && eTarget.equals(target)) {
                
            } else {
                    bw.write(line + "\n");
            }
        } 
        fw.flush(); bw.flush(); fw.close(); bw.close();
        br.close(); fr.close();
        
        File oldFile = new File("feeTypes.txt");
        oldFile.delete();
        
        File tempFile = new File("temp.txt");
        File rename = new File("feeTypes.txt");
        tempFile.renameTo(rename);
    }
    
    public String todayDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String str = formatter.format(date);
        return str;
    }
    
    public List<String> rangeOfMonthNYear(String feeTypeName, String target)
            throws IOException {
        List<String> monthNYear = new ArrayList<String>();
        String todayDate = todayDate();
        
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy") ;
        LocalDate ld = LocalDate.parse(todayDate, f);
        int m = ld.getMonthValue();
        int y = ld.getYear() ;
        
        String referenceDate = "";
        FileReader fr = new FileReader("feeTypes.txt");
        BufferedReader br = new BufferedReader(fr);
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            String[] feeTypes = line.split(";");
            String eFeeTypesName = feeTypes[0];
            String eTarget = feeTypes[1];
            String createdDate = feeTypes[4];
            if(eFeeTypesName.equals(feeTypeName) && eTarget.equals(target)) {
                referenceDate = createdDate;
            } 
        } br.close(); fr.close();
        
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
            String status, String monthYear) throws IOException {
        FileReader fr0 = new FileReader("feeTypes.txt");
        BufferedReader br0 = new BufferedReader(fr0);
        br0.readLine(); String cUnit = ""; String cUnitPrice = "";
        for (String line0 = br0.readLine(); line0 != null; line0 = br0.readLine()) {
            
            String[] feeTypesDetails = line0.split(";");
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
            FileReader fr = new FileReader("propertyDetails.txt");
            BufferedReader br = new BufferedReader(fr);
            
            String cMonthYear = monthYear.substring(0, monthYear.indexOf("/")) 
                    + monthYear.substring(monthYear.indexOf("/") + 1);
            br.readLine(); 
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                boolean check = false;
                String[] propertyDetails = line.split(";");
                String eUnitNo = propertyDetails[0];
                String eTarget = propertyDetails[1];
                String squareFeet = propertyDetails[2];
                
                FileReader fr2 = new FileReader("invoices.txt");
                BufferedReader br2 = new BufferedReader(fr2);
                br2.readLine();
                
                for (String line2 = br2.readLine(); line2 != null; line2 = br2.readLine()) {
                    String[] invoiceDetails = line2.split(";");
                    String eInvoiceNo = invoiceDetails[0];
                    String eFeeType = invoiceDetails[2];
                    if (eInvoiceNo.equals(eUnitNo + cMonthYear) 
                         && eFeeType.equals(feeTypeName)) {
                        check = true;
                    } 
                } fr2.close(); br2.close();
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
            } fr.close(); br.close(); 
        } else if (status.equals("ISSUED")) {
            FileReader fr = new FileReader("propertyDetails.txt");
            BufferedReader br = new BufferedReader(fr);
            String cMonthYear = monthYear.substring(0, monthYear.indexOf("/")) 
                    + monthYear.substring(monthYear.indexOf("/") + 1);
            br.readLine(); 
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] propertyDetails = line.split(";");
                String eUnitNo = propertyDetails[0];
                String eTarget = propertyDetails[1];
                
                FileReader fr2 = new FileReader("invoices.txt");
                BufferedReader br2 = new BufferedReader(fr2);
                br2.readLine();
                for (String line2 = br2.readLine(); line2 != null; line2 = br2.readLine()) {
                    String[] invoiceDetails = line2.split(";");
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
                } fr2.close(); br2.close();
            } fr.close(); br.close(); 
        } return availableInvoices;
    }
    
    public void issueInvoice(List<String> invoiceDetails) {
        String[] invoiceDetailsArray = new String[invoiceDetails.size()];
        invoiceDetails.toArray(invoiceDetailsArray);
        
        try {
            FileWriter fw = new FileWriter("invoices.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i=0; i<invoiceDetails.size(); i++) {
                bw.write(invoiceDetailsArray[i] + "\n");
            } 
            fw.flush(); bw.flush(); fw.close(); bw.close(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<String> extractAllPayment(String status) {
        List<String> pendingPaymentList = new ArrayList<String>();
        try {
            if (status.equals("PENDING")) {
                FileReader fr = new FileReader("invoices.txt");
                BufferedReader br = new BufferedReader(fr);

                br.readLine(); 
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    boolean check = true;
                    String[] invoiceDetails = line.split(";");
                    String iInvoiceNo = invoiceDetails[0];
                    String iUnitNo = invoiceDetails[1];
                    String iFeeType = invoiceDetails[2];
                    String iTotalPrice = invoiceDetails[7];
                    String iGeneratedDate = invoiceDetails[9];

                    FileReader fr2 = new FileReader("payment.txt");
                    BufferedReader br2 = new BufferedReader(fr2);
                    br2.readLine();
                    for (String line2 = br2.readLine(); line2 != null; line2 = br2.readLine()) {
                        String[] paymentDetails = line2.split(";");
                        String pInvoiceNo = paymentDetails[0];
                        String pFeeType = paymentDetails[2];
                        if (iInvoiceNo.equals(pInvoiceNo) && iFeeType.equals(pFeeType)) {
                            check = false;
                        }
                    } fr2.close(); br2.close();
                    if (check == true) {
                        String cPaymentDetails = iInvoiceNo +";"+ iGeneratedDate +";"+ 
                                iUnitNo +";"+ iFeeType +";"+ iTotalPrice +";";
                        pendingPaymentList.add(cPaymentDetails);
                    }
                } fr.close(); br.close();
                
            } else if (status.equals("PAID")) {
                FileReader fr = new FileReader("payment.txt");
                BufferedReader br = new BufferedReader(fr);

                br.readLine(); 
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    String[] paymentDetails = line.split(";");
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
                } fr.close(); br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pendingPaymentList;
    }
    
    public List<String> extractPaymentFees(String invoiceNo) {
        List<String> paymentFees = new ArrayList<String>();
        try {
            FileReader fr = new FileReader("invoices.txt");
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] feesDetails = line.split(";");
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
            FileReader fr = new FileReader("userProfile.txt");
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] userDetails = line.split(";");
                String firstName = userDetails[3];
                String lastName = userDetails[4];
                String eunitNo = userDetails[8];
                
                if(eunitNo.equals(unitNo)) {
                    userName.add(firstName +" "+ lastName);
                }
            } fr.close(); br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } return userName;
    }
    
    public List<String> extractUnitUsersDetails(String userName) {
        List<String> userFullDetails = new ArrayList<String>();
        try {
            FileReader fr = new FileReader("userProfile.txt");
            BufferedReader br = new BufferedReader(fr);
            br.readLine();
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] userDetails = line.split(";");
                String eUserName = userDetails[3] +" "+ userDetails[4];
                String userID = userDetails[0];
                String phoneNo = userDetails[7];
                String email = userDetails[1];
                String identificationNo = userDetails[5];
                if(userName.equals(eUserName)) {
                    userFullDetails.add(userID +";"+ phoneNo +";"+ email +";"+
                            identificationNo+";");
                }
            } fr.close(); br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } return userFullDetails;
    }
    
    public void storePayment(String invoiceNo, String userID) {
        try {
            FileReader fr = new FileReader("invoices.txt");
            BufferedReader br = new BufferedReader(fr);
            
            FileWriter fw = new FileWriter("payment.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            
            br.readLine();
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] invoiceDetails = line.split(";");
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
                    bw.write(einvoiceNo +";"+ unitNo +";"+ feeType +";"+ target +";"+
                            consump +";"+ unit +";"+ unitPrice +";"+ totalPrice +";"+
                            period +";"+ userID +";"+ todayDate +";"+ generatedDate
                            +";"+ "\n");
                }
            } fw.flush(); bw.flush(); fw.close(); bw.close(); fr.close(); br.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<String> extractReceipt(String status) {
        List<String> receiptList = new ArrayList<String>();
        try {
            if (status.equals("PENDING")) {
                FileReader fr = new FileReader("payment.txt");
                BufferedReader br = new BufferedReader(fr);

                br.readLine(); 
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    boolean check = true;
                    String[] invoiceDetails = line.split(";");
                    String iInvoiceNo = invoiceDetails[0];
                    String iUnitNo = invoiceDetails[1];
                    String iFeeType = invoiceDetails[2];
                    String iTotalPrice = invoiceDetails[7];
                    String paidBy = invoiceDetails[9];
                    String paymentDate = invoiceDetails[10];

                    FileReader fr2 = new FileReader("receipt.txt");
                    BufferedReader br2 = new BufferedReader(fr2);
                    br2.readLine();
                    for (String line2 = br2.readLine(); line2 != null; line2 = br2.readLine()) {
                        String[] paymentDetails = line2.split(";");
                        String pInvoiceNo = paymentDetails[0];
                        String pFeeType = paymentDetails[2];
                        if (iInvoiceNo.equals(pInvoiceNo) && iFeeType.equals(pFeeType)) {
                            check = false;
                        }
                    } fr2.close(); br2.close();
                    if (check == true) {
                        FileReader fr3 = new FileReader("userProfile.txt");
                        BufferedReader br3 = new BufferedReader(fr3);
                        br3.readLine(); String paidByName = null;
                        for (String line3 = br3.readLine(); line3 != null; line3 = br3.readLine()) {
                            String[] userDetails = line3.split(";");
                            String userID = userDetails[0].toUpperCase();
                            String userName = userDetails[3] +" "+ userDetails[4];
                            if(userID.equals(paidBy)) {
                                paidByName = userName;
                            }
                        } fr3.close(); br3.close();
                        String cReceiptDetails = iInvoiceNo +";"+ iUnitNo +";"+ 
                                iFeeType +";"+ iTotalPrice +";"+ paymentDate +";"+ 
                                paidByName +";";
                        receiptList.add(cReceiptDetails);
                    }
                } fr.close(); br.close();
                
            } else if (status.equals("ISSUED")) {
                FileReader fr = new FileReader("receipt.txt");
                BufferedReader br = new BufferedReader(fr);

                br.readLine(); 
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    String[] receiptDetails = line.split(";");
                    String invoiceNo = receiptDetails[0];
                    String unitNo = receiptDetails[1];
                    String feeType = receiptDetails[2];
                    String totalPrice = receiptDetails[7];
                    String paidBy = receiptDetails[9];
                    String paymentDate = receiptDetails[10];
                    
                    FileReader fr3 = new FileReader("userProfile.txt");
                        BufferedReader br3 = new BufferedReader(fr3);
                        br3.readLine(); String paidByName = null;
                        for (String line3 = br3.readLine(); line3 != null; line3 = br3.readLine()) {
                            String[] userDetails = line3.split(";");
                            String userID = userDetails[0].toUpperCase();
                            String userName = userDetails[3] +" "+ userDetails[4];
                            if(userID.equals(paidBy)) {
                                paidByName = userName;
                            }
                        } fr3.close(); br3.close();
                        
                    String cReceiptDetails = invoiceNo +";"+ unitNo +";"+ 
                            feeType +";"+ totalPrice +";"+ paymentDate +";"+ 
                            paidByName +";";
                        receiptList.add(cReceiptDetails);
                } fr.close(); br.close();
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
            FileWriter fw = new FileWriter("receipt.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i=0; i <receiptDetails.size(); i++) {
                String[] receiptFullDetails = receiptDetailsArray[i].split(";");
                String invoiceNo = receiptFullDetails[0];
                String feeType = receiptFullDetails[1];
                
                FileReader fr = new FileReader("payment.txt");
                BufferedReader br = new BufferedReader(fr);
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    String[] paymentFullDetails = line.split(";");
                    String pInvoiceNo = paymentFullDetails[0];
                    String pFeeType = paymentFullDetails[2];
                    
                    if (pInvoiceNo.equals(invoiceNo) && pFeeType.equals(feeType)) {
                        bw.write(line + "\n");
                    }
                } fr.close(); br.close();
            } fw.flush(); bw.flush(); fw.close(); bw.close();
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
            FileReader fr = new FileReader("invoices.txt");
            BufferedReader br = new BufferedReader(fr);

            br.readLine(); 
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] invoiceDetails = line.split(";");
                String period = invoiceDetails[8];
                if (!availableMonthYear.contains(period)) {
                    availableMonthYear.add(period);
                }
            } fr.close(); br.close();
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
            FileReader fr = new FileReader("invoices.txt");
            BufferedReader br = new BufferedReader(fr);

            br.readLine(); 
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] invoiceDetails = line.split(";");
                String eInvoiceNo = invoiceDetails[0];
                String feeType = invoiceDetails[2];
                String totalPrice = invoiceDetails[7];
                
                if (eInvoiceNo.equals(invoiceNo) && feeType.equals("Late Payment Charges")) {
                    latePaymentFee = totalPrice;
                }
            } fr.close(); br.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return latePaymentFee;
    }
    
    public void chargeLatePaymentFee(String invoiceNo, String latePaymentFee) {
        try{
            FileReader fr = new FileReader("invoices.txt");
            BufferedReader br = new BufferedReader(fr);
            boolean check = false;
            
            br.readLine(); 
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                String[] invoiceDetails = line.split(";");
                String eInvoiceNo = invoiceDetails[0];
                String feeType = invoiceDetails[2];
                
                if (eInvoiceNo.equals(invoiceNo) && feeType.equals("Late Payment Charges")) {
                    check = true;
                }
            } fr.close(); br.close();
            
            if (check == true) {
                FileReader fr2 = new FileReader("invoices.txt");
                BufferedReader br2 = new BufferedReader(fr2);

                FileWriter fw = new FileWriter("temp.txt");
                BufferedWriter bw = new BufferedWriter(fw);

                for (String line2 = br2.readLine(); line2 != null; line2 = br2.readLine()) {
                    String[] invoiceDetails = line2.split(";");
                    String nInvoiceNo = invoiceDetails[0];
                    String nUnitNo = invoiceDetails[1];
                    String nFeeType = invoiceDetails[2];
                    String nPeriod = invoiceDetails[8];
                    if (nInvoiceNo.equals(invoiceNo) &&
                            nFeeType.equals("Late Payment Charges")) {
                        
                        String generatedDate = new AccountExecutive().todayDate();
                        bw.write(invoiceNo +";"+ nUnitNo +";"+ nFeeType +";"+ "-" +";"+
                                "-" +";"+ "-" +";"+ "-" +";"+ latePaymentFee +";"+
                                nPeriod +";"+ generatedDate+";");
                    } else {
                        bw.write(line2 + "\n");
                    }
                } fw.flush(); bw.flush(); fw.close(); bw.close();
                fr2.close(); br2.close();
                File oldFile = new File("invoices.txt");
                oldFile.delete();

                File tempFile = new File("temp.txt");
                File rename = new File("invoices.txt");
                tempFile.renameTo(rename);
                
            } else if (check == false) {
                FileWriter fw = new FileWriter("invoices.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                
                String nPeriod = invoiceNo.substring(invoiceNo.length()-6);
                String period = nPeriod.substring(0, 2) + "/" + nPeriod.substring(2);
                String nUnitNo = invoiceNo.substring(0, invoiceNo.length()-6);
                
                String generatedDate = new AccountExecutive().todayDate();
                bw.write(invoiceNo +";"+ nUnitNo +";"+ "Late Payment Charges" +";"+ "-" 
                        +";"+ "-" +";"+ "-" +";"+ "-" +";"+ latePaymentFee +";"+
                        period +";"+ generatedDate+";");
                
                fw.flush(); bw.flush(); fw.close(); bw.close();
            } 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<String> extractAllStatementUnit(String status, String monthYear) {
        List<String> availableUnit = new ArrayList<>();
        List<String> availableStatements = new ArrayList<>();
        try {
            if (status.equals("PENDING")) {
                FileReader fr = new FileReader("invoices.txt");
                BufferedReader br = new BufferedReader(fr);
                
                br.readLine(); 
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    String[] invoiceDetails = line.split(";");
                    String eInvoiceNo = invoiceDetails[0];
                    String unitNo = invoiceDetails[1];
                    String eIssuedDate = invoiceDetails[9];
                    String issuedDate = eIssuedDate.substring(eIssuedDate.length()-7);
                    
                    boolean check = false;
                    if (issuedDate.equals(monthYear)) {
                        FileReader fr2 = new FileReader("statements.txt");
                        BufferedReader br2 = new BufferedReader(fr2);
                        
                        br2.readLine(); 
                        for (String line2 = br2.readLine(); line2 != null; line2 = br2.readLine()) {
                            String[] statementDetails = line2.split(";");
                            String sInvoiceNo = statementDetails[0];
                            if (sInvoiceNo.equals(eInvoiceNo)) {
                                check = true;
                            }
                        } fr2.close(); br2.close();
                        if (check == false && !availableUnit.contains(unitNo)) {
                            availableUnit.add(unitNo);
                        }
                    }
                } fr.close(); br.close();
                availableStatements = extractStatementUnitDetails(availableUnit);
                
            } else if (status.equals("ISSUED")) {
                FileReader fr = new FileReader("statements.txt");
                BufferedReader br = new BufferedReader(fr);
                
                br.readLine(); 
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    String[] statementDetails = line.split(";");
                    String invoiceNo = statementDetails[0];
                    String sUnitNo = statementDetails[1];
                    
                    String mYear = invoiceNo.substring(invoiceNo.length()-6);
                    String period = mYear.substring(0, 2) +"/"+ mYear.substring(2);
                    
                    if (!availableUnit.contains(sUnitNo) && period.equals(monthYear)) {
                        availableUnit.add(sUnitNo);
                    }
                } fr.close(); br.close();
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
                FileReader fr = new FileReader("invoices.txt");
                BufferedReader br = new BufferedReader(fr);
                
                br.readLine();
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    String[] invoiceDetails = line.split(";");
                    String iUnitNo = invoiceDetails[1];
                    
                    if (iUnitNo.equals(unitNo) && iUnitNo.startsWith("S")) {
                        target = "Commercial";
                    } else if (iUnitNo.equals(unitNo)) {
                        target = "Residential";
                    }
                } fr.close(); br.close();
                
                FileReader fr2 = new FileReader("userProfile.txt");
                BufferedReader br2 = new BufferedReader(fr2);

                br2.readLine(); 
                for (String line2 = br2.readLine(); line2 != null; line2 = br2.readLine()) {
                    String[] userDetails = line2.split(";");
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
                } fr2.close(); br2.close();
                availableStatements.add(unitNo +";"+ target +";"+ owner +";"+ resident);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return availableStatements;
    }
    
    public void issueStatement(List<String> availableStatement) {
        try {
            FileWriter fw = new FileWriter("statements.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            String todayDate = todayDate();
            for (String statements : availableStatement) {
                bw.write(statements + todayDate +";\n");
            } fw.flush(); bw.flush(); fw.close(); bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}    
    
   

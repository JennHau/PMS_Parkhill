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
    
    public List<String> extractPendingPayment(String status) {
        List<String> pendingPaymentList = new ArrayList<String>();
        
        try {
            FileReader fr = new FileReader("invoices.txt");
            BufferedReader br = new BufferedReader(fr);
            
            FileReader fr2 = new FileReader("payment.txt");
            BufferedReader br2 = new BufferedReader(fr2);
            
            br.readLine(); br2.readLine();
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                boolean check = true;
                String[] invoiceDetails = line.split(";");
                String iInvoiceNo = invoiceDetails[0];
                String iUnitNo = invoiceDetails[1];
                String iFeeType = invoiceDetails[2];
                String iTotalPrice = invoiceDetails[7];
                String iGeneratedDate = invoiceDetails[9];
                
                for (String line2 = br2.readLine(); line2 != null; line2 = br2.readLine()) {
                    String[] paymentDetails = line2.split(";");
                    String pInvoiceNo = paymentDetails[0];
                    String pFeeType = paymentDetails[2];
                    
                    if (iInvoiceNo.equals(pInvoiceNo) && iFeeType.equals(pFeeType)) {
                        check = false;
                    }
                } if (check == true) {
                    String cPaymentDetails = iInvoiceNo +";"+ iGeneratedDate +";"+ 
                            iUnitNo +";"+ iFeeType +";"+ iTotalPrice +";";
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
//        List<String> paymentFees = new ArrayList<String>();
//        try {
//            FileReader fr = new FileReader("userProfile.txt");
//            BufferedReader br = new BufferedReader(fr);
//            br.readLine();
//            for (String line = br.readLine(); line != null; line = br.readLine()) {
//                String[] feesDetails = line.split(";");
//                String einvoiceNo = feesDetails[0];
//                String feeType = feesDetails[2];
//                String issueDate = feesDetails[9];
//                String consump = feesDetails[4];
//                String unit = feesDetails[5];
//                String unitPrice = feesDetails[6];
//                String totalPrice = feesDetails[7];
//                String cDetails = feeType +";"+ issueDate +";"+ consump +";"+
//                        unit +";"+ unitPrice +";"+ totalPrice +";";
//                
//                if(einvoiceNo.equals(invoiceNo)) {
//                    paymentFees.add(cDetails);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } return paymentFees;
    }
}    
    
   

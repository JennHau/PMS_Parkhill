/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package accountExecutive;

import java.awt.Color;
import java.util.List;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import pms_parkhill_residence.FileHandling;
import pms_parkhill_residence.Users;

/**
 *
 * @author wongj
 */
public class AccountExecutive extends Users {

    FileHandling fh = new FileHandling();

    public AccountExecutive() {}
    
    public AccountExecutive(String userID, String email, String password, String firstName,
                 String lastName, String identificationNo, String gender, String phoneNo) {
        super(userID, email, password,  firstName, lastName,  identificationNo,
                gender, phoneNo);
    }
    
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
    
    public String currencyFormat(float amount) {
        DecimalFormat df = new DecimalFormat("0.00");
        String unitPrice = df.format(amount);
        return unitPrice;
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
}

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
import classes.FileHandling;
import classes.Invoice;
import classes.PMS_DateTimeFormatter;
import classes.Payment;
import classes.User;

/**
 *
 * @author wongj
 */
public class AccountExecutive extends User {

    public FileHandling fh = new FileHandling();
    public Payment PYM = new Payment();
    public PMS_DateTimeFormatter DTF = new PMS_DateTimeFormatter();

    public AccountExecutive() {}
    
    // constructor for Account Executive
    public AccountExecutive(String userID, String email, String password, String firstName,
                 String lastName, String identificationNo, String gender, String phoneNo) {
        super(userID, email, password,  firstName, lastName,  identificationNo,
                gender, phoneNo);
    }
    
    // method to extra all available fee types
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

    // method to store new fee types into text files
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

    // method to make changes on available fee types details
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

    
    // method to delete available fee types
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

    // method to convert today's data into dd/MM/yyyy format
    public String todayDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String str = formatter.format(date);
        return str;
    }

    // method to get the start month till valid month for each fees
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
        // find the rest of the monthYear range based on start month
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

    // store new invoice into text file
    public void issueInvoice(Invoice INV) {
        List<String> newData = new ArrayList<>();
        newData.add(INV.getInvoiceNo() +";"+ INV.getUnitNo() +";"+ INV.getFeeType()
                +";"+ INV.getUnitCategory() +";"+ INV.getConsumption() +";"+
                INV.getUnit() +";"+ INV.getUnitPrice() +";"+ INV.getTotalPrice()
                +";"+ INV.getPeriod() +";"+ INV.getIssuedDate()+";"+ INV.getDeleteID() +";");
        fh.fileWrite("invoices.txt", true, newData);
    }
    
    // a text file to link userID and invoiceNo just in case ownership 
    // transfer in the future
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
                Invoice INV = new Invoice(invoiceDetails);
//                String eInvoiceNo = invoiceDetails[0];
//                String eUnitNo = invoiceDetails[1];
//                String feeType = invoiceDetails[2];
//                String ePeriod = invoiceDetails[8];

                if (INV.getInvoiceNo().equals(invoiceNo) &&
                        INV.getFeeType().equals("Late Payment Charges")) {
                    check = true;
                }
                
                if (INV.getInvoiceNo().equals(invoiceNo)) {
                    unitNo = INV.getUnitNo(); period = INV.getPeriod();
                }
            }

            List<String> newData = new ArrayList<>();
            // modify to latest late payment charge is record existed
            if (check == true) {
                newData.add(invoicesArray[0]);
                for (int i = 1; i < invoicesList.size(); i++) {
                    String[] invoiceDetails = invoicesArray[i].split(";");
                    Invoice INV = new Invoice(invoiceDetails);
//                    String nInvoiceNo = invoiceDetails[0];
//                    String nUnitNo = invoiceDetails[1];
//                    String nFeeType = invoiceDetails[2];
//                    String nPeriod = invoiceDetails[8];
                    if (INV.getInvoiceNo().equals(invoiceNo)
                            && INV.getFeeType().equals("Late Payment Charges")) {

                        String generatedDate = this.todayDate();
                        newData.add(invoiceNo + ";" + INV.getUnitNo() + ";" +
                                INV.getFeeType() + ";" + "-" + ";"+ "-" + ";" +
                                "-" + ";" + "0.00" + ";" + latePaymentFee + ";"
                                + INV.getPeriod() + ";" + generatedDate + ";" +
                                "-" +";");
                    } else {
                        newData.add(invoicesArray[i]);
                    }
                }
                fh.fileWrite("invoices.txt", false, newData);

            // add new late payment charges if record not found
            } else if (check == false) {
                String generatedDate = this.todayDate();
                newData.add(invoiceNo + ";" + unitNo + ";" + "Late Payment Charges" + ";" + "-"
                        + ";" + "-" + ";" + "-" + ";" + "0.00" + ";" + latePaymentFee + ";"
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
    
    // method to get both resident and tenant name based on unitNo
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

    // method to extract all personal details of a user
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

    // get all stored monthYear from available invoice in the text file
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

    // method to calculate the month that statement can be issued
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

    // method to get the charged late payment of an invoice
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
    
    // method to search all available unit to issue statement based on monthYear
    public List<String> extractAllStatementUnit(String status, String monthYear) {
        List<String> transList = fh.fileRead("invoices.txt");
        List<String> statementList = fh.fileRead("statements.txt");
        
        List<String> availableStatement = new ArrayList<>();
        // search for unit that statement hasn't issue
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
        // search for unit that statement has been issued
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
    

    // method to extract full details for the statement based on unitNo
    public List<String> extractStatementUnitDetails(List<String> statementUnit) {
        List<String> availableStatements = new ArrayList<>();
        
        // in case the owner is being removed, system will check both active and
        // inactive user text file for relavant data
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
        
        // get user data
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
    
    // when a user is remove, all their transactions will be added a deleteID
    public void deleteTrans(String unitNo, String deleteID) {
        List<String> newData = new ArrayList<>();
        
        // add deleteID for invoices
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
        
        // add deleteID for all paid payments
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
        
        
        // add deleteID for all receipts
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
        
        // add deleteID for all statements
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
    
    // method to restore all available transactions when specific user is restored
    public void restoreTrans(String deleteID) {
        List<String> newData = new ArrayList<>();
        
        // remove deleteID for invoices
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
        
        // remove deleteID for payments
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
        
        // remove deleteID for receipts
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
        
        // remove deleteID for statements
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
    
    // method to convert float in two decimal places
    public String currencyFormat(float amount) {
        DecimalFormat df = new DecimalFormat("0.00");
        String unitPrice = df.format(amount);
        return unitPrice;
    }
    
    // method to decorate all tables
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

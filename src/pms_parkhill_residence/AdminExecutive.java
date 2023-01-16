/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wongj
 */
public class AdminExecutive {
    
    FileHandling fh = new FileHandling();
    
    public List<String> extractAllProperties(String type) {
        List<String> propertiesList = fh.fileRead("propertyDetails.txt");
        String[] propertiesArray = new String[propertiesList.size()];
        propertiesList.toArray(propertiesArray);
        
        List<String> availableList = new ArrayList<>();
        
        for (int i = 1; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesArray[i].split(";");
            String unitNo = propertyDetails[0];
            String eTypes = propertyDetails[1].toLowerCase();
            String squareFoot = propertyDetails[2];
            String status = propertyDetails[3];
            String dateOfSold = propertyDetails[4];
            
            if (type.equals(eTypes)) {
                availableList.add(unitNo +";"+ squareFoot +";"+ status +";"+
                        dateOfSold +";");
            }
        } return availableList;
    }
    
    public boolean checkUnitAvailability(String unitNo) {
        List<String> propertiesList =  fh.fileRead("propertyDetails.txt");
        String[] propertiesArray = new String[propertiesList.size()];
        propertiesList.toArray(propertiesArray);
        
        for (int i = 1; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesArray[i].split(";");
            String eUnitNo = propertyDetails[0].toLowerCase();
            
            if (unitNo.equals(eUnitNo)) {
                return false;
            }
        }
        return true;
    }
    
    public void modifyUnitDetails(String unitNo, String type, String squareFoot) {
        List<String> propertiesList =  fh.fileRead("propertyDetails.txt");
        String[] propertiesArray = new String[propertiesList.size()];
        propertiesList.toArray(propertiesArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesArray[i].split(";");
            String eUnitNo = propertyDetails[0];
            String eTypes = propertyDetails[1];
            String status = propertyDetails[3];
            String dateOfSold = propertyDetails[4];
            
            if(eUnitNo.equals(unitNo) && eTypes.equals(type)) {
                newData.add(eUnitNo +";"+ eTypes +";"+ squareFoot +";"+ status
                +";"+ dateOfSold +";");
            } else {
                newData.add(propertiesArray[i]);
            }
        } fh.fileWrite("propertyDetails.txt", false, newData);
    }
    
    public boolean checkOutstandingFee(String unitNo) {
        AccountExecutive ae = new AccountExecutive();
        List<String> oustandingFee = ae.extractAllPayment("PENDING");
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
    
    public void deleteUnit(String unitNo) {
        List<String> propertiesList =  fh.fileRead("propertyDetails.txt");
        String[] propertiesArray = new String[propertiesList.size()];
        propertiesList.toArray(propertiesArray);
        
        List<String> newData1 = new ArrayList<>();
        List<String> newData2 = new ArrayList<>();
        
        for (int i = 0; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesArray[i].split(";");
            String eUnitNo = propertyDetails[0];
            
            if(eUnitNo.equals(unitNo)) {
                String currentDeleteID = getLatestDeleteID();
                newData2.add(currentDeleteID +";"+ propertiesArray[i]
                        + LocalDateTime.now() +";");
                
            } else {
                newData1.add(propertiesArray[i]);
            }
        } 
        fh.fileWrite("propertyDetails.txt", false, newData1);
        fh.fileWrite("inactivePropertyDetails.txt", true, newData2);
        deleteResident(unitNo);
    }
    
    public String getLatestDeleteID() {
        List<String> userList =  fh.fileRead("inactiveUserProfile.txt");
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        int largestDeleteID = 0;
        for (int i = 1; i < userList.size(); i++) {
            String[] userDetails = userArray[i].split(";");
            int deleteID = Integer.valueOf(userDetails[0].substring(3));
            
            if(deleteID > largestDeleteID) {
                largestDeleteID = deleteID;
            }
        } largestDeleteID++;
        int times = 6 - String.valueOf(largestDeleteID).length();
        
        String zero = "";
        
        
        switch (times) {
            case 0 ->                 {
                     zero = "";
                }
            case 1 ->                 {
                     zero = "0";
                }
            case 2 ->                 {
                     zero = "00";
                }
            case 3 ->                 {
                     zero = "000";
                }
            case 4 ->                 {
                     zero = "0000";
                }
            case 5 ->                 {
                     zero = "00000";
                }
            default -> {
            }
        }
        
        
        String currentUsableID = "dlt" + zero +String.valueOf(largestDeleteID);
        return currentUsableID;
    }
    
    public void deleteResident(String unitNo) {
        String currentDeleteID = getLatestDeleteID();
        
        List<String> userList =  fh.fileRead("userProfile.txt");
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        List<String> newData1 = new ArrayList<>();
        List<String> newData2 = new ArrayList<>();
        
        for (int i = 0; i < userList.size(); i++) {
            String[] userDetails = userArray[i].split(";");
            String eUnitNo = userDetails[8];
            
            if(eUnitNo.equals(unitNo)) {
                newData2.add(currentDeleteID +";"+ userArray[i]
                        + LocalDateTime.now() +";");
            } else {
                newData1.add(userArray[i]);
            }
        } 
        fh.fileWrite("userProfile.txt", false, newData1);
        fh.fileWrite("inactiveUserProfile.txt", true, newData2);
        
        
//        List<String> invoiceList =  fh.fileRead("invoices.txt");
//        String[] invoiceArray = new String[invoiceList.size()];
//        invoiceList.toArray(invoiceArray);
//        
//        newData1.clear();
//        for (int i = 0; i < invoiceList.size(); i++) {
//            String[] invoiceDetails = invoiceArray[i].split(";");
//            
//            String invoiceNo = invoiceDetails[0];
//            String eUnitNo = invoiceDetails[1];
//            String feeType = invoiceDetails[2];
//            String target = invoiceDetails[3];
//            String consumption = invoiceDetails[4];
//            String unit = invoiceDetails[5];
//            String unitPrice = invoiceDetails[6];
//            String totalPrice = invoiceDetails[7];
//            String period = invoiceDetails[8];
//            String generatedDate = invoiceDetails[9];
//            
//            if(eUnitNo.equals(unitNo)) {
//                newData1.add(invoiceNo +";"+ eUnitNo +";"+feeType +";"+ target+";"+ consumption
//                        +";"+ unit +";"+ unitPrice +";"+ totalPrice +";"+ period +";"+
//                        generatedDate +";"+ currentDeleteID + ";");
//            } else {
//                newData1.add(invoiceArray[i]);
//            }
//        } 
//        fh.fileWrite("invoices.txt", false, newData1);
//        
//        
//        List<String> paymentList =  fh.fileRead("payment.txt");
//        String[] paymentArray = new String[paymentList.size()];
//        paymentList.toArray(paymentArray);
//        
//        newData1.clear();
//        for (int i = 0; i < paymentList.size(); i++) {
//            String[] paymentDetails = paymentArray[i].split(";");
//            
//            String invoiceNo = paymentDetails[0];
//            String eUnitNo = paymentDetails[1];
//            String feeType = paymentDetails[2];
//            String target = paymentDetails[3];
//            String consumption = paymentDetails[4];
//            String unit = paymentDetails[5];
//            String unitPrice = paymentDetails[6];
//            String totalPrice = paymentDetails[7];
//            String period = paymentDetails[8];
//            String paymentBy = paymentDetails[9];
//            String paymentDate = paymentDetails[10];
//            String generatedDate = paymentDetails[11];
//            
//            if(eUnitNo.equals(unitNo)) {
//                newData1.add(invoiceNo +";"+ eUnitNo +";"+ feeType +";"+ target 
//                        +";"+ consumption +";"+ unit +";"+ unitPrice +";"+
//                        totalPrice +";"+ period +";"+ paymentBy +";"+ paymentDate
//                        +";"+ generatedDate +";"+ currentDeleteID + ";");
//            } else {
//                newData1.add(paymentArray[i]);
//            }
//        } 
//        fh.fileWrite("payment.txt", false, newData1);
//        
//        
//        List<String> receiptList =  fh.fileRead("receipt.txt");
//        String[] receiptArray = new String[receiptList.size()];
//        receiptList.toArray(receiptArray);
//        
//        newData1.clear();
//        for (int i = 0; i < receiptList.size(); i++) {
//            String[] receiptDetails = receiptArray[i].split(";");
//            
//            String invoiceNo = receiptDetails[0];
//            String eUnitNo = receiptDetails[1];
//            String feeType = receiptDetails[2];
//            String target = receiptDetails[3];
//            String consumption = receiptDetails[4];
//            String unit = receiptDetails[5];
//            String unitPrice = receiptDetails[6];
//            String totalPrice = receiptDetails[7];
//            String period = receiptDetails[8];
//            String paymentBy = receiptDetails[9];
//            String paymentDate = receiptDetails[10];
//            String generatedDate = receiptDetails[11];
//            
//            if(eUnitNo.equals(unitNo)) {
//                newData1.add(invoiceNo +";"+ eUnitNo +";"+ feeType +";"+ target 
//                        +";"+ consumption +";"+ unit +";"+ unitPrice +";"+
//                        totalPrice +";"+ period +";"+ paymentBy +";"+ paymentDate
//                        +";"+ generatedDate +";"+ currentDeleteID + ";");
//            } else {
//                newData1.add(receiptArray[i]);
//            }
//        } 
//        fh.fileWrite("receipt.txt", false, newData1);
//        
//        
//        List<String> statementList =  fh.fileRead("statements.txt");
//        String[] statementArray = new String[statementList.size()];
//        statementList.toArray(statementArray);
//        
//        newData1.clear();
//        for (int i = 0; i < statementList.size(); i++) {
//            String[] statementDetails = statementArray[i].split(";");
//            String invoiceNo = statementDetails[0];
//            String eUnitNo = statementDetails[1];
//            String generatedDate = statementDetails[2];
//            
//            if(eUnitNo.equals(unitNo)) {
//                newData1.add(invoiceNo +";"+ eUnitNo +";"+ generatedDate +";"+
//                        currentDeleteID + ";");
//            } else {
//                newData1.add(statementArray[i]);
//            }
//        } 
//        fh.fileWrite("statements.txt", false, newData1);
    }
    
    public List<String> extractAllPropertiesHistory(String type) {
        List<String> propertiesList = fh.fileRead("inactivePropertyDetails.txt");
        String[] propertiesArray = new String[propertiesList.size()];
        propertiesList.toArray(propertiesArray);
        
        List<String> availableList = new ArrayList<>();
        
        for (int i = 1; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesArray[i].split(";");
            String deleteID = propertyDetails[0];
            String unitNo = propertyDetails[1];
            String eTypes = propertyDetails[2].toLowerCase();
            String squareFoot = propertyDetails[3];
            String status = propertyDetails[4];
            String dateOfSold = propertyDetails[5];
            String deletedDateTime = propertyDetails[6];
            
            if (type.equals(eTypes)) {
                availableList.add(deleteID +";"+ unitNo +";"+ squareFoot +";"+
                        status +";"+ dateOfSold +";"+ deletedDateTime +";");
            }
        } return availableList;
    }
    
    public boolean restoreUnitValidation(String unitNo) {
        List<String> propertiesList = fh.fileRead("propertyDetails.txt");
        String[] propertiesArray = new String[propertiesList.size()];
        propertiesList.toArray(propertiesArray);
        
        for (int i = 1; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesArray[i].split(";");
            String eUnitNo = propertyDetails[0];
            if (unitNo.equals(eUnitNo)) {
                return false;
            }
        } return true;
    }
    
    public void restoreUnit(String deletionID) { 
        List<String> propertiesList = fh.fileRead("inactivePropertyDetails.txt");
        String[] propertiesArray = new String[propertiesList.size()];
        propertiesList.toArray(propertiesArray);
        
        List<String> newData1 = new ArrayList<>();
        List<String> newData2 = new ArrayList<>();
        
        for (int i = 0; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesArray[i].split(";");
            String deleteID = propertyDetails[0];
            String unitNo = propertyDetails[1];
            String eTypes = propertyDetails[2];
            String squareFoot = propertyDetails[3];
            String status = propertyDetails[4];
            String dateOfSold = propertyDetails[5];
            
            if (deletionID.equals(deleteID)) {
                newData1.add(unitNo +";"+ eTypes +";"+ squareFoot +";"+ status +";"+
                        dateOfSold +";");
            } else {
                newData2.add(propertiesArray[i]);
            }
        } fh.fileWrite("propertyDetails.txt", true, newData1);
        fh.fileWrite("inactivePropertyDetails.txt", false, newData2);
        restoreResidentTenant(deletionID, "ALL");
    }
    
    public void restoreResidentTenant(String deletionID, String status) {
        List<String> userList = fh.fileRead("inactiveUserProfile.txt");
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        List<String> newData1 = new ArrayList<>();
        List<String> newData2 = new ArrayList<>();
        
        if (status.equals("ALL")) {
            for (int i = 0; i < userList.size(); i++) {
                String[] userDetails = userArray[i].split(";");
                String deleteID = userDetails[0];
                String userID = userDetails[1];
                String email = userDetails[2];
                String password = userDetails[3];
                String firstName = userDetails[4];
                String lastName = userDetails[5];
                String identificationNo = userDetails[6];
                String gender = userDetails[7];
                String phoneNo = userDetails[8];
                String unitNo = userDetails[9];
                
                if (deletionID.equals(deleteID)) {
                    newData1.add(userID +";"+ email +";"+ password +";"+ firstName +";"+
                            lastName +";"+ identificationNo +";"+ gender +";"
                            + phoneNo +";"+ unitNo +";");
                } else {
                    newData2.add(userArray[i]);
                }
            } fh.fileWrite("userProfile.txt", true, newData1);
            fh.fileWrite("inactiveUserProfile.txt", false, newData2);
        }
    }
    
    public List<String> extractAllTenantResident(String type) {
        List<String> userList = fh.fileRead("userProfile.txt");
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        List<String> availableList = new ArrayList<>();
        
        for (int i = 1; i < userList.size(); i++) {
            String[] userDetails = userArray[i].split(";");
            String userID = userDetails[0].toUpperCase();
            
            if (type.equals("Residential")) {
                if (userID.startsWith("TNT") || userID.startsWith("RSD")) {
                availableList.add(userArray[i]);
                }
            } else if (type.equals("Commercial")) {
                if (userID.startsWith("VDR")) {
                    availableList.add(userArray[i]);
                }
                
            }
            
        } return availableList;
    }
    
    public String getLatestResidentTenantID(String type) {
        List<String> userList = fh.fileRead("userProfile.txt");
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        List<String> inactiveUserList = fh.fileRead("inactiveUserProfile.txt");
        String[] inactiveUserArray = new String[inactiveUserList.size()];
        inactiveUserList.toArray(inactiveUserArray);
        
        List<Integer> allUserID = new ArrayList<>();
        
        for (int i = 1; i < userList.size(); i++) {
            String[] userDetails = userArray[i].split(";");
            String sUserID = userDetails[0];
            if (type.equals("Tenant") && sUserID.startsWith("tnt")) {
                allUserID.add(Integer.valueOf(sUserID.substring(3)));
            } else if (type.equals("Resident") && sUserID.startsWith("rsd")) {
                allUserID.add(Integer.valueOf(sUserID.substring(3)));
            } else if (type.equals("Commercial") && sUserID.startsWith("vdr")) {
                allUserID.add(Integer.valueOf(sUserID.substring(3)));
            }
        }
        
        for (int i = 1; i < inactiveUserList.size(); i++) {
            String[] userDetails = inactiveUserArray[i].split(";");
            String sUserID = userDetails[1];
            if (type.equals("Tenant") && sUserID.startsWith("tnt")) {
                allUserID.add(Integer.valueOf(sUserID.substring(3)));
            } else if (type.equals("Resident") && sUserID.startsWith("rsd")) {
                allUserID.add(Integer.valueOf(sUserID.substring(3)));
            } else if (type.equals("Commercial") && sUserID.startsWith("vdr")) {
                allUserID.add(Integer.valueOf(sUserID.substring(3)));
            }
        }
        int largestID = 0;
        for (int i = 0; i < allUserID.size(); i++) {
            int userID = allUserID.get(i);
            
            if(userID > largestID) {
                largestID = userID;
            }
        } largestID ++;
        int times = 6 - String.valueOf(largestID).length();
        
        String zero = "";
        
        switch (times) {
            case 0 ->                 {
                     zero = "";
                }
            case 1 ->                 {
                     zero = "0";
                }
            case 2 ->                 {
                     zero = "00";
                }
            case 3 ->                 {
                     zero = "000";
                }
            case 4 ->                 {
                     zero = "0000";
                }
            case 5 ->                 {
                     zero = "00000";
                }
            default -> {
            }
        }
        String currentUsableID = null;
        switch (type) {
            case "Tenant" -> currentUsableID = "tnt" + zero +String.valueOf(largestID);
            case "Resident" -> currentUsableID = "rsd" + zero +String.valueOf(largestID);
            case "Commercial" -> currentUsableID = "vdr" + zero +String.valueOf(largestID);
            default -> {
            }
        }
        return currentUsableID;
    }
    
    public List<String> getAvailableUnit(String type) {
        List<String> availableList = new ArrayList<>();
        
        List<String> userList = fh.fileRead("userProfile.txt");
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        switch (type) {
            case "Commercial" -> {
                for (int i = 1; i < userList.size(); i++) {
                    String[] userDetails = userArray[i].split(";");
                    String userID = userDetails[0];
                    String uniNo = userDetails[8];
                    
                    if (userID.equals("Parkhill") && uniNo.startsWith("S")) {
                        availableList.add(uniNo);
                    }
                }
            }
            case "Tenant" -> {
                for (int i = 1; i < userList.size(); i++) {
                    String[] userDetails = userArray[i].split(";");
                    String userID = userDetails[0];
                    String uniNo = userDetails[8];
                    
                    if (userID.equals("Parkhill") && !uniNo.startsWith("S")) {
                        availableList.add(uniNo);
                    }
                }
            }
            case "Resident" -> {
                for (int i = 1; i < userList.size(); i++) {
                    int check = 0;
                    String[] userDetails = userArray[i].split(";");
                    String uniNo = userDetails[8];
                    
                    for (int j = 1; j < userList.size(); j++) {
                        String[] userDetails2 = userArray[j].split(";");
                        String uniNo2 = userDetails2[8];
                        if (uniNo.equals(uniNo2) && !uniNo.startsWith("S")) {
                            check++;
                        }
                    } if (check == 1) {
                        availableList.add(uniNo);
                    }
                }
            }
            default -> {
            } 
        } return availableList;
    }   
}

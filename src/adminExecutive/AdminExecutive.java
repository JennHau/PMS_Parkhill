/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminExecutive;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import accountExecutive.AccountExecutive;
import classes.Complaint;
import classes.Employee;
import java.awt.Color;
import java.io.File;
import java.time.LocalDate;
import java.util.Calendar;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import classes.FacilityBookingPaymentByBooking;
import classes.FacilityBookingPaymentByHour;
import classes.FileHandling;
import classes.PMS_DateTimeFormatter;
import classes.Payment;
import classes.PropertyUnit;
import classes.Users;
import java.time.LocalTime;
import residentANDtenant.ResidentTenant;

/**
 *
 * @author wongj
 */
public class AdminExecutive extends Users{
    
    FileHandling fh = new FileHandling();
    Payment PM = new Payment();
    PropertyUnit PU = new PropertyUnit();
    Complaint CP = new Complaint();
    PMS_DateTimeFormatter DTF = new PMS_DateTimeFormatter();
    Employee EMP = new Employee();
    
    public AdminExecutive(){}
    
    // constructor for admin executive
    public AdminExecutive(String userID, String email, String password, String firstName,
                 String lastName, String identificationNo, String gender, String phoneNo){
        super(userID, email, password,  firstName, lastName,  identificationNo,
                gender, phoneNo);
    }
    
    // method to convert today date to dd/MM/yyyy format
    public String todayDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String str = formatter.format(date);
        return str;
    }
    
//    // method to extract specific property unit details
//    public List<String> extractAllProperties(String type) {
//        List<String> propertiesList = fh.fileRead("propertyDetails.txt");
//        String[] propertiesArray = new String[propertiesList.size()];
//        propertiesList.toArray(propertiesArray);
//        
//        List<String> availableList = new ArrayList<>();
//        
//        for (int i = 1; i < propertiesList.size(); i++) {
//            String[] propertyDetails = propertiesArray[i].split(";");
//            String unitNo = propertyDetails[0];
//            String eTypes = propertyDetails[1].toLowerCase();
//            String squareFoot = propertyDetails[2];
//            String status = propertyDetails[3];
//            String dateOfSold = propertyDetails[4];
//            
//            if (type.equals(eTypes)) {
//                availableList.add(unitNo +";"+ squareFoot +";"+ status +";"+
//                        dateOfSold +";");
//            }
//        } return availableList;
//    }
    
    // method to extract all available property unit details 
    public List<String> extractAllProperties() {
        List<String> propertiesList = fh.fileRead("propertyDetails.txt");
        String[] propertiesArray = new String[propertiesList.size()];
        propertiesList.toArray(propertiesArray);
        
        List<String> availableList = new ArrayList<>();
        
        for (int i = 1; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesArray[i].split(";");
            String unitNo = propertyDetails[0];
            String squareFoot = propertyDetails[2];
            String status = propertyDetails[3];
            String dateOfSold = propertyDetails[4];
            
            availableList.add(unitNo +";"+ squareFoot +";"+ status +";"+
                    dateOfSold +";");
            
        } return availableList;
    }
    
//    // check whether newly added unitNo is existed
//    public boolean checkUnitAvailability(String unitNo) {
//        List<String> propertiesList =  fh.fileRead("propertyDetails.txt");
//        String[] propertiesArray = new String[propertiesList.size()];
//        propertiesList.toArray(propertiesArray);
//        
//        for (int i = 1; i < propertiesList.size(); i++) {
//            String[] propertyDetails = propertiesArray[i].split(";");
//            String eUnitNo = propertyDetails[0].toLowerCase();
//            
//            if (unitNo.equals(eUnitNo)) {
//                return false;
//            }
//        }
//        return true;
//    }
    
//    // method to modify specific unit details
//    public void modifyUnitDetails(String unitNo, String type, String squareFoot) {
//        List<String> propertiesList =  fh.fileRead("propertyDetails.txt");
//        String[] propertiesArray = new String[propertiesList.size()];
//        propertiesList.toArray(propertiesArray);
//        
//        List<String> newData = new ArrayList<>();
//        
//        for (int i = 0; i < propertiesList.size(); i++) {
//            String[] propertyDetails = propertiesArray[i].split(";");
//            String eUnitNo = propertyDetails[0];
//            String eTypes = propertyDetails[1];
//            String status = propertyDetails[3];
//            String dateOfSold = propertyDetails[4];
//            
//            if(eUnitNo.equals(unitNo) && eTypes.equals(type)) {
//                newData.add(eUnitNo +";"+ eTypes +";"+ squareFoot +";"+ status
//                +";"+ dateOfSold +";");
//            } else {
//                newData.add(propertiesArray[i]);
//            }
//        } fh.fileWrite("propertyDetails.txt", false, newData);
//    }
    
    
    
//    // delete a property unit
//    public void deleteUnit(String unitNo) {
//        List<String> propertiesList =  fh.fileRead("propertyDetails.txt");
//        String[] propertiesArray = new String[propertiesList.size()];
//        propertiesList.toArray(propertiesArray);
//        
//        // move deleted unit to inactive text file
//        List<String> newData1 = new ArrayList<>();
//        List<String> newData2 = new ArrayList<>();
//        String currentDeleteID = null;
//        
//        for (int i = 0; i < propertiesList.size(); i++) {
//            String[] propertyDetails = propertiesArray[i].split(";");
//            String eUnitNo = propertyDetails[0];
//            
//            if(eUnitNo.equals(unitNo)) {
//                currentDeleteID = getURTDeleteID();
//                newData2.add(currentDeleteID +";"+ propertiesArray[i]
//                        + LocalDateTime.now() +";");
//            } else {
//                newData1.add(propertiesArray[i]);
//            }
//        } 
//        fh.fileWrite("propertyDetails.txt", false, newData1);
//        fh.fileWrite("inactivePropertyDetails.txt", true, newData2);
//        deleteTenantResident(unitNo, currentDeleteID);
//    }
    
    // get any lastest ID
    public String getLatestID(String filename, String initial) {
        List<String> userList =  fh.fileRead(filename);
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        int largestID = 0;
        for (int i = 1; i < userList.size(); i++) {
            String[] userDetails = userArray[i].split(";");
            String id = userDetails[0];
            if(id.startsWith(initial)) {
                int existingID = Integer.valueOf(userDetails[0].substring(3));
                if(existingID > largestID) {
                    largestID = existingID;
                }
            }
            
        } largestID++;
        int times = 6 - String.valueOf(largestID).length();
        
        String zero = "";
        
        // ensure that all primary ID are having 6 numbers
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
        
        
        String currentUsableID = initial + zero +String.valueOf(largestID);
        return currentUsableID;
    }
    
    // method to delete Tenant (together with Resident)
    public void deleteTenantResident(String unitNo, String deleteID) {
        String currentDeleteID;
        if(deleteID == null) {
            currentDeleteID = getLatestID("inactiveUserProfile.txt", "dlt");
        } else {
            currentDeleteID = deleteID;
        }
        
        List<String> propertiesList = fh.fileRead("propertyDetails.txt");
        String[] propertiesArray = new String[propertiesList.size()];
        propertiesList.toArray(propertiesArray);
        
        List<String> availableList = new ArrayList<>();
        
        for (int i = 1; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesArray[i].split(";");
            String uniNo = propertyDetails[0];
            availableList.add(uniNo);
        }
        
        // move deleted user to inactive text file
        List<String> userList =  fh.fileRead("userProfile.txt");
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        List<String> newData1 = new ArrayList<>();
        List<String> newData2 = new ArrayList<>();
        
        for (int i = 0; i < userList.size(); i++) {
            String[] userDetails = userArray[i].split(";");
            String userID = userDetails[0];
            String eUnitNo = userDetails[8];
            
            if(eUnitNo.equals(unitNo)) {
                newData2.add(currentDeleteID +";"+ userArray[i]
                        + LocalDateTime.now() +";");
            } else {
                newData1.add(userArray[i]);
            }
            
            if((userID.startsWith("tnt") && eUnitNo.equals(unitNo) 
                    && availableList.contains(eUnitNo)) || (userID.startsWith("vdr")
                    && eUnitNo.equals(unitNo) && availableList.contains(eUnitNo))) {
                newData1.add("Parkhill;parkhill@gmail.com;Parkhill@123;Parkill;"
                            + "Residence;-;-;-;" + eUnitNo.toUpperCase() + ";");
            }
        } 
        fh.fileWrite("userProfile.txt", false, newData1);
        fh.fileWrite("inactiveUserProfile.txt", true, newData2);
        PU.updatePropertySoldStatus(unitNo, "unsold");
        
        AccountExecutive acce = new AccountExecutive();
        acce.deleteTrans(unitNo, currentDeleteID);
    }
    
    // method to delete Resident only
    public void deleteResident(String unitNo) {
        String currentDeleteID = getLatestID("inactiveUserProfile.txt", "dlt");
        
        List<String> userList =  fh.fileRead("userProfile.txt");
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        List<String> newData1 = new ArrayList<>();
        List<String> newData2 = new ArrayList<>();
        
        for (int i = 0; i < userList.size(); i++) {
            String[] userDetails = userArray[i].split(";");
            String userID = userDetails[0];
            String eUnitNo = userDetails[8];
            
            
            if(eUnitNo.equals(unitNo) && userID.startsWith("rsd")) {
                newData2.add(currentDeleteID +";"+ userArray[i]
                        + LocalDateTime.now() +";");
            } else {
                newData1.add(userArray[i]);
            }
        } 
        fh.fileWrite("userProfile.txt", false, newData1);
        fh.fileWrite("inactiveUserProfile.txt", true, newData2);
    }
    
    //get specific user details
    public String[] extractRTDetails(String userID) {
        List<String> userProfile = fh.fileRead("userProfile.txt");
        
        for (int i = 0; i<userProfile.size(); i++) {
            String[] userInfo2 = userProfile.get(i).split(";");
            String userID_temp = userInfo2[0];
            
            if (userID_temp.equals(userID.toLowerCase())) {
                return userInfo2;
            }
        } return null;
    }
    
//    // admin modify resident tenant account
//    public void modifyRTAccount(ResidentTenant RT) {
//        List<String> userProfile = fh.fileRead("userProfile.txt");
//        String[] userProfileArray = new String[userProfile.size()];
//        userProfile.toArray(userProfileArray);
//        
//        List<String> newData = new ArrayList<>();
//        
//        for (int i = 0; i<userProfile.size(); i++) {
//            String[] userInfo = userProfileArray[i].split(";");
//            String userID_temp = userInfo[0];
//            String password_temp = userInfo[2];
//            
//            if (userID_temp.equals(RT.getUserID())) {
//                newData.add(RT.getUserID() +";"+ RT.getEmail() +";"+ password_temp
//                        +";"+ RT.getFirstName() +";"+ RT.getLastName() +";"+
//                        RT.getIdentificationNo() +";"+ RT.getGender()+";"+
//                        RT.getPhoneNo()+";"+ RT.getUnitNo() +";");
//            } else {
//                newData.add(userProfileArray[i]);
//            }
//        } fh.fileWrite("userProfile.txt", false, newData);
//    }
    
//    // extract all deleted property unit details
//    public List<String> extractAllPropertiesHistory(String type) {
//        List<String> propertiesList = fh.fileRead("inactivePropertyDetails.txt");
//        String[] propertiesArray = new String[propertiesList.size()];
//        propertiesList.toArray(propertiesArray);
//        
//        List<String> availableList = new ArrayList<>();
//        
//        for (int i = 1; i < propertiesList.size(); i++) {
//            String[] propertyDetails = propertiesArray[i].split(";");
//            String deleteID = propertyDetails[0];
//            String unitNo = propertyDetails[1];
//            String eTypes = propertyDetails[2].toLowerCase();
//            String squareFoot = propertyDetails[3];
//            String status = propertyDetails[4];
//            String dateOfSold = propertyDetails[5];
//            String deletedDateTime = propertyDetails[6];
//            
//            if (type.equals(eTypes)) {
//                availableList.add(deleteID +";"+ unitNo +";"+ squareFoot +";"+
//                        status +";"+ dateOfSold +";"+ deletedDateTime +";");
//            }
//        } return availableList;
//    }
//    
//    // validate whether a same unitNo is existed before restore deleted property unit
//    public boolean restoreUnitValidation(String unitNo) {
//        List<String> propertiesList = fh.fileRead("propertyDetails.txt");
//        String[] propertiesArray = new String[propertiesList.size()];
//        propertiesList.toArray(propertiesArray);
//        
//        for (int i = 1; i < propertiesList.size(); i++) {
//            String[] propertyDetails = propertiesArray[i].split(";");
//            String eUnitNo = propertyDetails[0];
//            if (unitNo.equals(eUnitNo)) {
//                return false;
//            }
//        } return true;
//    }
//    
//    // method to restore deleted property unit
//    public void restoreUnit(String deletionID) { 
//        List<String> propertiesList = fh.fileRead("inactivePropertyDetails.txt");
//        String[] propertiesArray = new String[propertiesList.size()];
//        propertiesList.toArray(propertiesArray);
//        
//        List<String> newData1 = new ArrayList<>();
//        List<String> newData2 = new ArrayList<>();
//        
//        for (int i = 0; i < propertiesList.size(); i++) {
//            String[] propertyDetails = propertiesArray[i].split(";");
//            String deleteID = propertyDetails[0];
//            String unitNo = propertyDetails[1];
//            String eTypes = propertyDetails[2];
//            String squareFoot = propertyDetails[3];
//            String status = propertyDetails[4];
//            String dateOfSold = propertyDetails[5];
//            
//            if (deletionID.equals(deleteID)) {
//                newData1.add(unitNo +";"+ eTypes +";"+ squareFoot +";"+ status +";"+
//                        dateOfSold +";");
//            } else {
//                newData2.add(propertiesArray[i]);
//            }
//        } fh.fileWrite("propertyDetails.txt", true, newData1);
//        fh.fileWrite("inactivePropertyDetails.txt", false, newData2);
//        restoreResidentTenant(deletionID);
//    }
    
    // method to restore deleted Resident Tenant
    public void restoreResidentTenant(String deletionID) {
        List<String> userList = fh.fileRead("inactiveUserProfile.txt");
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        List<String> newData1 = new ArrayList<>();
        List<String> newData2 = new ArrayList<>();
        
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
            String eUnitNo = userDetails[9];

            if (deletionID.equals(deleteID)) {
                removeDefaultUserAccount(userID);
                newData1.add(userID +";"+ email +";"+ password +";"+ firstName +";"+
                        lastName +";"+ identificationNo +";"+ gender +";"
                        + phoneNo +";"+ eUnitNo +";");
                if (!userID.equals("Parkhill")) {
                    PU.updatePropertySoldStatus(eUnitNo, "sold");
                }
            } else {
                newData2.add(userArray[i]);
            }
            
        } 
            
        fh.fileWrite("userProfile.txt", true, newData1);
        fh.fileWrite("inactiveUserProfile.txt", false, newData2);
        
        AccountExecutive acce = new AccountExecutive();
        acce.restoreTrans(deletionID);
    }
    
    // remove default account (when property unit is unsold)
    public void removeDefaultUserAccount(String unitNo) {
        List<String> userList = fh.fileRead("userProfile.txt");
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i < userList.size(); i++) {
            String[] userDetails = userArray[i].split(";");
            String userID = userDetails[0];
            String eUnitNo = userDetails[8];
            
            if (!userID.equals("Parkhill") && !eUnitNo.equals(unitNo)) {
                newData.add(userArray[i]);
            }
        } fh.fileWrite("userProfile.txt", false, newData);
    }
    
    // extract all deleted user data
    public List<String> extractAllUserHistory(String type) {
        List<String> userList = fh.fileRead("inactiveUserProfile.txt");
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        List<String> availableList = new ArrayList<>();

        for (int i = 1; i < userList.size(); i++) {
            String[] userDetails = userArray[i].split(";");
            String deleteID = userDetails[0];
            String userID = userDetails[1].toUpperCase();
            String unitNo = userDetails[9];
            String name = userDetails[4] +" "+ userDetails[5];
            String deleteDT = userDetails[10];
            
            if(type.equals("Residential") && (userID.startsWith("RSD") 
                    || userID.startsWith("TNT"))) {
                availableList.add(deleteID +";"+ userID +";"+ unitNo +";"+
                                name +";"+ deleteDT +";");
            } else if (type.equals("Commercial") && userID.startsWith("VDR")) {
                availableList.add(deleteID +";"+ userID +";"+ unitNo +";"+
                                name +";"+ deleteDT +";");
            }
        } return availableList;
    }
    
    // validate whether the deleted user previous unit is occupied
    public int restoreUserValidation(String unitNo) {
        List<String> propertiesList = fh.fileRead("propertyDetails.txt");
        String[] propertiesArray = new String[propertiesList.size()];
        propertiesList.toArray(propertiesArray);
        
        int check = -1;
        for (int i = 1; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesArray[i].split(";");
            String eUnitNo = propertyDetails[0];
            
            if(eUnitNo.equals(unitNo)) {
                check ++;
                List<String> userList =  fh.fileRead("userProfile.txt");
                String[] userArray = new String[userList.size()];
                userList.toArray(userArray);

                for (int j = 0; j < userList.size(); j++) {
                    
                    String[] userDetails = userArray[j].split(";");
                    String userID = userDetails[0];
                    String uUnitNo = userDetails[8];
                    if (uUnitNo.equals(unitNo) && !userID.equals("Parkhill")) {
                        check ++;
                    }
                }
            }
        } return check;
    }
    
    // register new resident tenant
    public void RTRegistration(ResidentTenant RT) {
        removeDefaultUserAccount(RT.getUnitNo());
        
        List<String> newData = new ArrayList<>();
        newData.add(RT.getUserID() +";"+ RT.getEmail() +";"+ RT.getPassword()
                        +";"+ RT.getFirstName() +";"+ RT.getLastName() +";"+
                        RT.getIdentificationNo() +";"+ RT.getGender()+";"+
                        RT.getPhoneNo()+";"+ RT.getUnitNo() +";");
        
        fh.fileWrite("userProfile.txt", true, newData);
        
        PU.updatePropertySoldStatus(RT.getUnitNo(), "sold");
    }
    
    // get all tenant resident details based on type 
    public List<String> extractAllTenantResident(String type) {
        List<String> userList = fh.fileRead("userProfile.txt");
//        String[] userArray = new String[userList.size()];
//        userList.toArray(userArray);
        
        List<String> availableList = new ArrayList<>();
        
        for (int i = 1; i < userList.size(); i++) {
            String[] userDetails = userList.get(i).split(";");
            String userID = userDetails[0].toUpperCase();
            
            if (type.equals("Residential")) {
                if (userID.startsWith("TNT") || userID.startsWith("RSD")) {
                availableList.add(userList.get(i));
                }
            } else if (type.equals("Commercial")) {
                if (userID.startsWith("VDR")) {
                    availableList.add(userList.get(i));
                }
            }
            
        } return availableList;
    }
    
    // method to count the latest usable resident/tenant ID
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
    
//    // get all unsold unit
//    public List<String> getAvailableUnit(String type) {
//        List<String> availableList = new ArrayList<>();
//        
//        List<String> userList = fh.fileRead("userProfile.txt");
//        String[] userArray = new String[userList.size()];
//        userList.toArray(userArray);
//        
//        switch (type) {
//            case "Commercial" -> {
//                for (int i = 1; i < userList.size(); i++) {
//                    String[] userDetails = userArray[i].split(";");
//                    String userID = userDetails[0];
//                    String uniNo = userDetails[8];
//                    
//                    if (userID.equals("Parkhill") && uniNo.startsWith("S")) {
//                        availableList.add(uniNo);
//                    }
//                }
//            }
//            case "Tenant" -> {
//                for (int i = 1; i < userList.size(); i++) {
//                    String[] userDetails = userArray[i].split(";");
//                    String userID = userDetails[0];
//                    String uniNo = userDetails[8];
//                    
//                    if (userID.equals("Parkhill") && !uniNo.startsWith("S")) {
//                        availableList.add(uniNo);
//                    }
//                }
//            }
//            case "Resident" -> {
//                for (int i = 1; i < userList.size(); i++) {
//                    int check = 0;
//                    String[] userDetails = userArray[i].split(";");
//                    String uniNo = userDetails[8];
//                    
//                    for (int j = 1; j < userList.size(); j++) {
//                        String[] userDetails2 = userArray[j].split(";");
//                        String userID = userDetails2[0];
//                        String uniNo2 = userDetails2[8];
//                        if (uniNo.equals(uniNo2) && !uniNo.startsWith("S") 
//                                && !userID.equals("Parkhill")) {
//                            check++;
//                        }
//                    // check == 1 (consists tenant) / check == 2 (consists RT)    
//                    } if (check == 1) {
//                        availableList.add(uniNo);
//                    }
//                }
//            }
//            default -> {
//            } 
//        } return availableList;
//    }   
//    
//    // method to change property unit status
//    public void updatePropertySoldStatus(String unitNo, String status) {
//        List<String> propertyList = fh.fileRead("propertyDetails.txt");
//        String[] propertyArray = new String[propertyList.size()];
//        propertyList.toArray(propertyArray);
//        
//        List<String> newData = new ArrayList<>();
//        
//        for (int i = 0; i < propertyList.size(); i++) {
//            String[] propertyDetails = propertyArray[i].split(";");
//            String eUnitNo = propertyDetails[0];
//            String eTarget = propertyDetails[1];
//            String eSquareF = propertyDetails[2];
//            
//            if (unitNo.equals(eUnitNo) && status.equals("sold")) {
//                newData.add(eUnitNo +";"+ eTarget +";"+ eSquareF +";"+ "sold"
//                        +";"+ todayDate() +";");
//            } else if (unitNo.equals(eUnitNo) && status.equals("unsold")) {
//                newData.add(eUnitNo +";"+ eTarget +";"+ eSquareF +";"+ "unsold"
//                        +";"+ "-" +";");
//            } else {
//                newData.add(propertyArray[i]);
//            }
//            
//        } fh.fileWrite("propertyDetails.txt", false, newData);
//    }
    
    // extract all complaint details
    public List<String> extractComplaintDetails() {
        List<String> availableList = new ArrayList<>();
        
        List<String> complaintList = fh.fileRead("complaints.txt");
        List<String> userList = fh.fileRead("userProfile.txt");
        
        for (int i = 1; i < complaintList.size(); i++) {
            String[] complaintDetails = complaintList.get(i).split(";");
            Complaint CP = new Complaint(complaintDetails);
//            String complaintId = complaintDetails[0];
//            String complainerId = complaintDetails[1];
//            String complaintDesc = complaintDetails[2];
//            String date = complaintDetails[3];
//            String time = complaintDetails[4];
//            String status = complaintDetails[5];
            
            for (int j = 1; j < userList.size(); j++) {
                String[] userDetails = userList.get(j).split(";");
                String userID = userDetails[0];
                String unitNo = userDetails[8];
                if (userID.equals(CP.getComplainerID())) {
                    availableList.add(CP.getComplaintID() +";"+ CP.getComplainerID()
                            +";"+ unitNo +";"+ CP.getComplaintDetails() +";"+
                            CP.getComplaintDate() +";"+ CP.getComplaintTime()
                            +";"+ CP.getComplaintStatus());
                }
            }
        } return availableList;
    }
    
    // extract resident and tenant details based on unitNo
    public List<String> extractAvailableComplainer(String unitNo) {
        List<String> userList = fh.fileRead("userProfile.txt");
        
        List<String> availableComplainer = new ArrayList<>();
        
        for (int i = 1; i < userList.size(); i++) {
            String[] userDetails = userList.get(i).split(";");
            String userID = userDetails[0];
            String name = userDetails[3] +" "+ userDetails[4];
            String eUnitNo = userDetails[8];
            
            if (eUnitNo.equals(unitNo)) {
                availableComplainer.add(userID.toUpperCase() +" - "+ name);
            }
        } return availableComplainer;
    }
    
    
//    public String getLatestComplaintID() {
//        List<String> complaintList =  fh.fileRead("complaints.txt");
//        
//        int largestComplaintID = 0;
//        for (int i = 1; i < complaintList.size(); i++) {
//            String[] complaintDetails = complaintList.get(i).split(";");
//            int complaintID = Integer.valueOf(complaintDetails[0].substring(3));
//            
//            if(complaintID > largestComplaintID) {
//                largestComplaintID = complaintID;
//            }
//        } largestComplaintID++;
//        int times = 6 - String.valueOf(largestComplaintID).length();
//        
//        String zero = "";
//        
//        
//        switch (times) {
//            case 0 ->                 {
//                     zero = "";
//                }
//            case 1 ->                 {
//                     zero = "0";
//                }
//            case 2 ->                 {
//                     zero = "00";
//                }
//            case 3 ->                 {
//                     zero = "000";
//                }
//            case 4 ->                 {
//                     zero = "0000";
//                }
//            case 5 ->                 {
//                     zero = "00000";
//                }
//            default -> {
//            }
//        }
//        
//        
//        String currentUsableID = "cmp" + zero +String.valueOf(largestComplaintID);
//        return currentUsableID;
//    }
    
//    // store new complaint into text file
//    public void fileComplaint(String complainerID, String desc) {
//        
//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String todayDate = formatter.format(date);
//        
//        String currentTime = new SimpleDateFormat("HH:mm:ss").format
//                            (Calendar.getInstance().getTime());
//        
//        LocalDateTime recordedDT = LocalDateTime.now();
//        
//        String recordedPersonID = "";
//        List<String> rp =  fh.fileRead("currentSession.txt");
//        for (int i = 1; i < rp.size(); i++) {
//            String[] userDetails = rp.get(i).split(";");
//            String userID = userDetails[0];
//            recordedPersonID = userID;
//        }
//            
//        List<String> newData = new ArrayList<>();
//        newData.add(getLatestID("complaints.txt", "cmp") +";"+ complainerID +";"+ desc +";"+ 
//                    todayDate +";"+ currentTime +";"+ "Pending" +";"+ recordedPersonID
//                    +";"+ recordedDT +";");
//        
//        fh.fileWrite("complaints.txt", true, newData);
//    }
    
    // get complainer details
    public List<String> getComplainerUnitIDName(String complainerID) {
        List<String> userList = fh.fileRead("userProfile.txt");
        
        List<String> availableComplainer = new ArrayList<>();
        
        for (int i = 1; i < userList.size(); i++) {
            String[] complaintDetails = userList.get(i).split(";");
            String userID = complaintDetails[0];
            String name = complaintDetails[3] +" "+ complaintDetails[4];
            String unitNo = complaintDetails[8];
            
            if (userID.equals(complainerID)) {
                availableComplainer.add(unitNo +";"+ userID +" - "+ name);
            }
            
        } return availableComplainer;
    }
    
//    // modify complaint description
//    public void modifyComplaint(String complaintID, String complainerID, String desc) {
//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String todayDate = formatter.format(date);
//        
//        String currentTime = new SimpleDateFormat("HH:mm:ss").format
//                            (Calendar.getInstance().getTime());
//        
//        LocalDateTime recordedDT = LocalDateTime.now();
//        
//        String recordedPersonID = "";
//        List<String> rp =  fh.fileRead("currentSession.txt");
//        for (int i = 1; i < rp.size(); i++) {
//            String[] userDetails = rp.get(i).split(";");
//            String userID = userDetails[0];
//            recordedPersonID = userID;
//        }
//        
//        List<String> newData = new ArrayList<>();
//        newData.add(complaintID +";"+ complainerID +";"+ desc +";"+ 
//                    todayDate +";"+ currentTime +";"+ "Pending" +";"+ recordedPersonID
//                    +";"+ recordedDT +";");
//        
//        deleteComplaint(complaintID);
//        fh.fileWrite("complaints.txt", true, newData);
//    }
//    
//    // delete available complaint
//    public void deleteComplaint(String complaintID) {
//        List<String> complaintList =  fh.fileRead("complaints.txt");
//        
//        List<String> newData = new ArrayList<>();
//        
//        for (int i = 0; i < complaintList.size(); i++) {
//            String[] complaintDetails = complaintList.get(i).split(";");
//            String eComplaintID = complaintDetails[0];
//            
//            if (!complaintID.equals(eComplaintID)) {
//                newData.add(complaintList.get(i));
//            }
//        } fh.fileWrite("complaints.txt", false, newData);
//    }
    
    // extract all employees details
//    public List<String> extractEmployeeDetails() {
//        List<String> employeeList =  fh.fileRead("employeeList.txt");
//        
//        List<String> availableList = new ArrayList<>();
//        
//        for (int i = 1; i < employeeList.size(); i++) {
//            String[] employeeDetails = employeeList.get(i).split(";");
//            String id = employeeDetails[0];
//            String email = employeeDetails[1];
//            String name = employeeDetails[2];
//            String phoneNo = employeeDetails[3];
//            String position = employeeDetails[4];
//            
//            availableList.add(id +";"+ name +";"+ email +";"+ phoneNo +";"+ position);
//        } return availableList;
//    }
    
    // extract available employee types
    public List<String> extractEmployeeType() {
        List<String> employeeTypeList =  fh.fileRead("employeeType.txt");
        
        List<String> availableList = new ArrayList<>();
        
        for (int i = 1; i < employeeTypeList.size(); i++) {
            availableList.add(employeeTypeList.get(i));
        } return availableList;
    }
    
    // validate whether new added employee type is existed
    public boolean addEmployeeTypeValidation(String employeeType, String initialise) {
        List<String> employeeTypeList =  fh.fileRead("employeeType.txt");
        
        for (int i = 1; i < employeeTypeList.size(); i++) {
            String[] employeeTypeDetails = employeeTypeList.get(i).split(";");
            String position = employeeTypeDetails[0].toLowerCase();
            String eInitialise = employeeTypeDetails[1];
            if (position.equals(employeeType)) {
                return false;
            } else if (initialise.equals(eInitialise)) {
                return false;
            } else if (position.equals(employeeType) && initialise.equals(eInitialise)) {
                return false;
            }
        } return true;
    }
    
    // method to store new employee type
    public void addEmployeeType(String employeeType, String initialise) {
        List<String> newData = new ArrayList<>();
        newData.add(employeeType +";"+ initialise +";");
        
        fh.fileWrite("employeeType.txt", true, newData);
    }
    
    // method to delete available employee type
    public void deleteEmployeeType(String employeeType) {
        List<String> employeeTypeList =  fh.fileRead("employeeType.txt");
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i < employeeTypeList.size(); i++) {
            String[] employeeTypeDetails = employeeTypeList.get(i).split(";");
            String position = employeeTypeDetails[0].toLowerCase();
            
            if (!position.equals(employeeType)) {
                newData.add(employeeTypeList.get(i));
            }
        } fh.fileWrite("employeeType.txt", false, newData);
    }
    
    // method to add new employee
    public void addEmployee(Employee EMP) {
        List<String> newData = new ArrayList<>();
        newData.add(EMP.getEmpID() +";"+ EMP.getEmpEmail() +";"+ EMP.getEmpName() 
                +";"+ EMP.getPhoneNo() +";"+ EMP.getPosition() +";"+ EMP.getIcNo()
                +";"+ EMP.getGender() +";");
        
        fh.fileWrite("employeeList.txt", true, newData);
        
        // if employee is security guard need to add account @ userProfile.txt
        if (EMP.getEmpID().startsWith("scg")) {
            List<String> newData2 = new ArrayList<>();
            String firstName = EMP.getEmpName().substring(0, EMP.getEmpName().indexOf(" "));
            String lastName = EMP.getEmpName().substring(EMP.getEmpName().indexOf(" ") + 1);
            newData2.add(EMP.getEmpID() +";"+ EMP.getEmpEmail() +";"+ "Parkhill@1234" +";"+
                    firstName +";"+ lastName +";"+ EMP.getIcNo() +";"+ EMP.getGender() +";"+
                    EMP.getPhoneNo() +";"+ "-" +";");
            fh.fileWrite("userProfile.txt", true, newData2);
        }
    }
    
    // method to delete available employee
    public void deleteEmployee(Employee EMP) {
        List<String> employeeTypeList =  fh.fileRead("employeeList.txt");
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i < employeeTypeList.size(); i++) {
            String[] employeeTypeDetails = employeeTypeList.get(i).split(";");
            String id = employeeTypeDetails[0];
            
            if (!id.equals(EMP.getEmpID())) {
                newData.add(employeeTypeList.get(i));
            }
        } fh.fileWrite("employeeList.txt", false, newData);
        
        // if employee is security guard need to delete account @ userProfile.txt
        if(EMP.getEmpID().startsWith("scg")) {
            List<String> userProfileList =  fh.fileRead("userProfile.txt");
        
            newData.clear();

            for (int i = 0; i < userProfileList.size(); i++) {
                String[] userDetails = userProfileList.get(i).split(";");
                String id = userDetails[0];

                if (!id.equals(EMP.getEmpID())) {
                    newData.add(userProfileList.get(i));
                }
            } fh.fileWrite("userProfile.txt", false, newData);
        }
    }
    
    // validate whether newly added facility is existed
    public boolean checkAddFacilityValidation(String facilityName, String fctID) {
        List<String> availableList = fh.fileRead("facility.txt");
        
        for (int i = 1; i < availableList.size(); i++) {
            String[] employeeDetails = availableList.get(i).split(";");
            String eFctID = employeeDetails[0].toLowerCase();
            String eFacilityName = employeeDetails[1].toLowerCase();
            
            if (eFacilityName.equals(facilityName.toLowerCase()) 
                    && !fctID.equals(eFctID)) {
                return false;
            } 
        } return true;
    }
    
    // method to delete available facility
    public void deleteFacility(String facilityID, String facilityName) {
        List<String> facilityList =  fh.fileRead("facility.txt");
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i < facilityList.size(); i++) {
            String[] facilityDetails = facilityList.get(i).split(";");
            String id = facilityDetails[0];
            
            if (!id.equals(facilityID)) {
                newData.add(facilityList.get(i));
            }
        } fh.fileWrite("facility.txt", false, newData);
        String newImgName = "src\\images\\" + facilityName + ".jpg";
        File newImageName = new File(newImgName);
        newImageName.delete();
    }
    
    // extract specific facility bookings
    public List<String> extractFacilityBooking(String facilityID, String status) {
        List<String> bookingList =  fh.fileRead("facilityBooking.txt");
        LocalDate today = LocalDate.now();
        
        List<String> availableList = new ArrayList<>();
        
        for (int i = 1; i < bookingList.size(); i++) {
            String[] bookingDetails = bookingList.get(i).split(";");
            String bookingID = bookingDetails[0];
            String fctID = bookingDetails[1];
            String facilityName = bookingDetails[2];
            String unitNo = bookingDetails[3];
            LocalDate date = LocalDate.parse(bookingDetails[4]);
            String startTime = bookingDetails[5];
            String endTime = bookingDetails[6];
            String payment = bookingDetails[7];
            
            if(facilityID.toLowerCase().equals(fctID)) {
                if(status.equals("UPCOMING") && (date.isEqual(today) || date.isAfter(today))) {
                    availableList.add(bookingID +";"+ fctID +";"+ facilityName +";"+
                            unitNo +";"+ date +";"+ startTime +";"+ endTime +";"+ payment);
                } else if (status.equals("HISTORY") && date.isBefore(today)) {
                    availableList.add(bookingID +";"+ fctID +";"+ facilityName +";"+
                            unitNo +";"+ date +";"+ startTime +";"+ endTime +";"+ payment);
                }
            }
        } return availableList;
    }
    
    // extract specific facility available timeslot
    public List<String> extractFacilityTimeSlot(String facilityID, String variation,
        String date, String bookingID) 
    {
        FileHandling fh = new FileHandling();
        List<String> availableList = fh.fileRead("facility.txt");
        List<String> availableBooking = fh.fileRead("facilityBooking.txt");
        
        List<String> timeSlot = new ArrayList<>();
        
        for (int i = 1; i < availableList.size(); i++) {
            String[] employeeDetails = availableList.get(i).split(";");
            String eFacilityID = employeeDetails[0];
            String startTime = employeeDetails[6];
            String endTime = employeeDetails[7];
            
            // based on facility start and end time create time slot (1 hr interval)
            int firstSlot = Integer.valueOf(startTime.substring(0, 2));
            int lastSlot = Integer.valueOf(endTime.substring(0, 2));
            if (eFacilityID.equals(facilityID)) {
                for (int j = firstSlot; j < lastSlot+1; j++) {
                    
                    String slotValue = String.valueOf(j);
                    slotValue = (slotValue.length() != 2) ? "0" + slotValue : slotValue;
                    String cStartTime = slotValue + ":00";
                    
                    boolean check = true;
                    for (int k = 1; k < availableBooking.size(); k++) {
                        String[] bookingDetails = availableBooking.get(k).split(";");
                        String bBookingID = bookingDetails[0];
                        String bFctName = bookingDetails[2];
                        String bBookBy = bookingDetails[3];
                        String bDate = bookingDetails[4];
                        String bStartTime = bookingDetails[5];
                        String bEndTime = bookingDetails[6];
                        if(bFctName.equals(variation) && bDate.equals(date) &&
                                bStartTime.equals(cStartTime) && bBookingID.equals(bookingID)) {
                            timeSlot.add(variation +";"+ cStartTime +";"+ bEndTime +";"+ bBookBy +";"+ "SELECTED");
                            check = false;
                        } else if(bFctName.equals(variation) && bDate.equals(date) && bStartTime.equals(cStartTime)) {
                            timeSlot.add(variation +";"+ cStartTime +";"+ bEndTime +";"+ bBookBy +";"+ "BOOKED");
                            check = false;
                        }
                    } 
                    if(check){
                        timeSlot.add(variation +";"+ cStartTime +";"+ String.valueOf(j+1) + ":00" +";"+ "-" +";"+ "SELECT");
                    }
                }
            }
        } return timeSlot;
    }
    
    // calculate facility booking fee
    public List<String> extractFacilityBookingFee(String facilityID, int hour) {
        List<String> availableList = fh.fileRead("facility.txt");
        List<String> feeData = new ArrayList<>();
        
        for (int i = 1; i < availableList.size(); i++) {
            String[] bookingDetails = availableList.get(i).split(";");
            String eFacilityID = bookingDetails[0];
            boolean payment = Boolean.valueOf(bookingDetails[3]);
            String price = bookingDetails[4];
            String priceUnit = bookingDetails[5];
            
            if(facilityID.equals(eFacilityID)) {
                // calculation for booking by per hour
                if(payment == true && priceUnit.equals("Per Hour")) {
                    FacilityBookingPaymentByHour fb = new FacilityBookingPaymentByHour();
                    fb.Facility(facilityID);
                    fb.setHour(hour); fb.calculateBookingFee();
                    feeData.add(price +";"+ fb.getTotalPrice());
                // calculation for booking by per booking    
                } else if(payment && priceUnit.equals("Per Booking")) {
                    FacilityBookingPaymentByBooking fb = new FacilityBookingPaymentByBooking();
                    fb.Facility(facilityID); fb.calculateBookingFee(); 
                    feeData.add("-" +";"+ fb.getTotalPrice());
                } else {
                    feeData.add("-" +";"+ "0.00");
                }
            } 
        } return feeData;
    }
    
    // in case user modify previous booking, recalculate booking fee
    public String calculateFacilityAdvancedPayment(String bookingID) {
        List<String> availableList = fh.fileRead("facilityBooking.txt");
        
        for (int i = 1; i < availableList.size(); i++) {
            String[] bookingDetails = availableList.get(i).split(";");
            String eBookingID = bookingDetails[0];
            String totalPrice = bookingDetails[8];
            
            if(bookingID.equals(eBookingID)) {
                return totalPrice;
            }
        } return "0.00";
    }
    
    // get facility booking unit
    public String extractFacilityBookingUnit(String bookingID) {
        List<String> availableList = fh.fileRead("facilityBooking.txt");
        
        for (int i = 1; i < availableList.size(); i++) {
            String[] bookingDetails = availableList.get(i).split(";");
            String eBookingID = bookingDetails[0];
            String unitNo = bookingDetails[3];
            
            if(eBookingID.equals(bookingID)) {
                return unitNo;
            }
        } return null;
    }
    
    // method to delete booking
    public void deleteFacilityBooking(String bookingID) {
        List<String> availableList = fh.fileRead("facilityBooking.txt");
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i < availableList.size(); i++) {
            String[] bookingDetails = availableList.get(i).split(";");
            String eBookingID = bookingDetails[0];
            
            if(!eBookingID.equals(bookingID)) {
                newData.add(availableList.get(i));
            }
        } fh.fileWrite("facilityBooking.txt", false, newData);
    }
    
    // method for design all tables
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

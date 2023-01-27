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
import com.sun.source.tree.NewArrayTree;
import java.io.File;
import java.time.LocalDate;
import java.util.Calendar;
import pms_parkhill_residence.FileHandling;

/**
 *
 * @author wongj
 */
public class AdminExecutive {
    
    FileHandling fh = new FileHandling();
    
    public String todayDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String str = formatter.format(date);
        return str;
    }
    
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
                String currentDeleteID = getLatestID("inactiveUserProfile.txt", "dlt");
                newData2.add(currentDeleteID +";"+ propertiesArray[i]
                        + LocalDateTime.now() +";");
                
            } else {
                newData1.add(propertiesArray[i]);
            }
        } 
        fh.fileWrite("propertyDetails.txt", false, newData1);
        fh.fileWrite("inactivePropertyDetails.txt", true, newData2);
        deleteTenantResident(unitNo);
    }
    
    public String getLatestID(String filename, String initial) {
        List<String> userList =  fh.fileRead(filename);
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        int largestID = 0;
        for (int i = 1; i < userList.size(); i++) {
            String[] userDetails = userArray[i].split(";");
            String id = userDetails[0];
            int existingID = Integer.valueOf(userDetails[0].substring(3));
            
            if(existingID > largestID && id.startsWith(initial)) {
                largestID = existingID;
            }
        } largestID++;
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
        
        
        String currentUsableID = initial + zero +String.valueOf(largestID);
        return currentUsableID;
    }
    
    public void deleteTenantResident(String unitNo) {
        String currentDeleteID = getLatestID("inactiveUserProfile.txt", "dlt");
        
        List<String> propertiesList = fh.fileRead("propertyDetails.txt");
        String[] propertiesArray = new String[propertiesList.size()];
        propertiesList.toArray(propertiesArray);
        
        List<String> availableList = new ArrayList<>();
        
        for (int i = 1; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesArray[i].split(";");
            String uniNo = propertyDetails[0];
            availableList.add(uniNo);
        }
        
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
        updatePropertySoldStatus(unitNo, "unsold");
    }
    
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
        restoreResidentTenant(deletionID);
    }
    
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

            if (!userID.equals("Parkhill")) {
                if (deletionID.equals(deleteID)) {
                    removeDefaultUserAccount(userID);
                    newData1.add(userID +";"+ email +";"+ password +";"+ firstName +";"+
                            lastName +";"+ identificationNo +";"+ gender +";"
                            + phoneNo +";"+ eUnitNo +";");
                    updatePropertySoldStatus(eUnitNo, "sold");
                } else {
                    newData2.add(userArray[i]);
                }
            }
        } 
            
        fh.fileWrite("userProfile.txt", true, newData1);
        fh.fileWrite("inactiveUserProfile.txt", false, newData2);
    }
    
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
    
    public void userRegistration(String userID, String email, String password, 
            String firstName, String lastName, String identificationNo, String gender,
            String phoneNo, String unitNo) {
        
        List<String> newData = new ArrayList<>();
        newData.add(userID +";"+ email +";"+password +";"+ firstName +";"+ lastName
                +";"+ identificationNo +";"+ gender +";"+ phoneNo +";"+ unitNo +";");
        
        fh.fileWrite("userProfile.txt", true, newData);
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
    
    public void updatePropertySoldStatus(String unitNo, String status) {
        List<String> propertyList = fh.fileRead("propertyDetails.txt");
        String[] propertyArray = new String[propertyList.size()];
        propertyList.toArray(propertyArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i < propertyList.size(); i++) {
            String[] propertyDetails = propertyArray[i].split(";");
            String eUnitNo = propertyDetails[0];
            String eTarget = propertyDetails[1];
            String eSquareF = propertyDetails[2];
            
            if (unitNo.equals(eUnitNo) && status.equals("sold")) {
                newData.add(eUnitNo +";"+ eTarget +";"+ eSquareF +";"+ "sold"
                        +";"+ todayDate() +";");
            } else if (unitNo.equals(eUnitNo) && status.equals("unsold")) {
                newData.add(eUnitNo +";"+ eTarget +";"+ eSquareF +";"+ "unsold"
                        +";"+ "-" +";");
            } else {
                newData.add(propertyArray[i]);
            }
            
        } fh.fileWrite("propertyDetails.txt", false, newData);
    }
    
    public List<String> extractComplaintDetails() {
        List<String> availableList = new ArrayList<>();
        
        List<String> complaintList = fh.fileRead("complaints.txt");
        List<String> userList = fh.fileRead("userProfile.txt");
        
        for (int i = 1; i < complaintList.size(); i++) {
            String[] complaintDetails = complaintList.get(i).split(";");
            String complaintId = complaintDetails[0];
            String complainerId = complaintDetails[1];
            String complaintDesc = complaintDetails[2];
            String date = complaintDetails[3];
            String time = complaintDetails[4];
            String status = complaintDetails[5];
            
            for (int j = 1; j < userList.size(); j++) {
                String[] userDetails = userList.get(j).split(";");
                String userID = userDetails[0];
                String unitNo = userDetails[8];
                if (userID.equals(complainerId)) {
                    availableList.add(complaintId +";"+ complainerId +";"+ unitNo +";"+
                            complaintDesc +";"+ date +";"+ time +";"+ status);
                }
            }
        } return availableList;
    }
    
    public List<String> extractAvailableComplainer(String unitNo) {
        List<String> userList = fh.fileRead("userProfile.txt");
        
        List<String> availableComplainer = new ArrayList<>();
        
        for (int i = 1; i < userList.size(); i++) {
            String[] userDetails = userList.get(i).split(";");
            String userID = userDetails[0];
            String name = userDetails[3] +" "+ userDetails[4];
            String eUnitNo = userDetails[8];
            
            if (eUnitNo.equals(unitNo)) {
                availableComplainer.add(userID +" - "+ name);
            }
        } return availableComplainer;
    }
    
    public String getLatestComplaintID() {
        List<String> complaintList =  fh.fileRead("complaints.txt");
        
        int largestComplaintID = 0;
        for (int i = 1; i < complaintList.size(); i++) {
            String[] complaintDetails = complaintList.get(i).split(";");
            int complaintID = Integer.valueOf(complaintDetails[0].substring(3));
            
            if(complaintID > largestComplaintID) {
                largestComplaintID = complaintID;
            }
        } largestComplaintID++;
        int times = 6 - String.valueOf(largestComplaintID).length();
        
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
        
        
        String currentUsableID = "cmp" + zero +String.valueOf(largestComplaintID);
        return currentUsableID;
    }
    
    public void fileComplaint(String complainerID, String desc) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = formatter.format(date);
        
        String currentTime = new SimpleDateFormat("HH:mm:ss").format
                            (Calendar.getInstance().getTime());
        
        LocalDateTime recordedDT = LocalDateTime.now();
        
        String recordedPersonID = "";
        List<String> rp =  fh.fileRead("currentSession.txt");
        for (int i = 1; i < rp.size(); i++) {
            String[] userDetails = rp.get(i).split(";");
            String userID = userDetails[0];
            recordedPersonID = userID;
        }
            
            
        
        List<String> newData = new ArrayList<>();
        newData.add(getLatestComplaintID() +";"+ complainerID +";"+ desc +";"+ 
                    todayDate +";"+ currentTime +";"+ "Pending" +";"+ recordedPersonID
                    +";"+ recordedDT +";");
        
        fh.fileWrite("complaints.txt", true, newData);
    }
    
    public List<String> getComplainerUnitIDName(String complainerID) {
        List<String> userList = fh.fileRead("userProfile.txt");
        
        List<String> availableComplainer = new ArrayList<>();
        
        for (int i = 1; i < userList.size(); i++) {
            String[] complaintDetails = userList.get(i).split(";");
            String userID = complaintDetails[0];
            String name = complaintDetails[3] + complaintDetails[4];
            String unitNo = complaintDetails[8];
            
            if (userID.equals(complainerID)) {
                availableComplainer.add(unitNo +";"+ userID +" - "+ name);
            }
            
        } return availableComplainer;
    }
    
    public void modifyComplaint(String complaintID, String complainerID, String desc) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = formatter.format(date);
        
        String currentTime = new SimpleDateFormat("HH:mm:ss").format
                            (Calendar.getInstance().getTime());
        
        LocalDateTime recordedDT = LocalDateTime.now();
        
        String recordedPersonID = "";
        List<String> rp =  fh.fileRead("currentSession.txt");
        for (int i = 1; i < rp.size(); i++) {
            String[] userDetails = rp.get(i).split(";");
            String userID = userDetails[0];
            recordedPersonID = userID;
        }
            
            
        
        List<String> newData = new ArrayList<>();
        newData.add(complaintID +";"+ complainerID +";"+ desc +";"+ 
                    todayDate +";"+ currentTime +";"+ "Pending" +";"+ recordedPersonID
                    +";"+ recordedDT +";");
        
        deleteComplaint(complaintID);
        fh.fileWrite("complaints.txt", true, newData);
    }
    
    public void deleteComplaint(String complaintID) {
        List<String> complaintList =  fh.fileRead("complaints.txt");
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i < complaintList.size(); i++) {
            String[] complaintDetails = complaintList.get(i).split(";");
            String eComplaintID = complaintDetails[0];
            
            if (!complaintID.equals(eComplaintID)) {
                newData.add(complaintList.get(i));
            }
        } fh.fileWrite("complaints.txt", false, newData);
    }
    
    public List<String> extractEmployeeDetails() {
        List<String> employeeList =  fh.fileRead("employeeList.txt");
        
        List<String> availableList = new ArrayList<>();
        
        for (int i = 1; i < employeeList.size(); i++) {
            String[] employeeDetails = employeeList.get(i).split(";");
            String id = employeeDetails[0];
            String email = employeeDetails[1];
            String name = employeeDetails[2];
            String phoneNo = employeeDetails[3];
            String position = employeeDetails[4];
            
            availableList.add(id +";"+ name +";"+ email +";"+ phoneNo +";"+ position);
        } return availableList;
    }
    
    public List<String> extractEmployeeType() {
        List<String> employeeTypeList =  fh.fileRead("employeeType.txt");
        
        List<String> availableList = new ArrayList<>();
        
        for (int i = 1; i < employeeTypeList.size(); i++) {
            availableList.add(employeeTypeList.get(i));
        } return availableList;
    }
    
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
    
    public void addEmployeeType(String employeeType, String initialise) {
        List<String> newData = new ArrayList<>();
        newData.add(employeeType +";"+ initialise +";");
        
        fh.fileWrite("employeeType.txt", true, newData);
    }
    
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
    
    public void addEmployee(String employeeID, String email, String firstName,
            String lastName, String phoneNo, String position, String idNo, String gender) {
        List<String> newData = new ArrayList<>();
        newData.add(employeeID +";"+ email +";"+ firstName +" "+ lastName +";"+
                phoneNo +";"+ position +";"+ idNo +";"+ gender +";");
        
        fh.fileWrite("employeeList.txt", true, newData);
        
        if (employeeID.startsWith("scg")) {
            List<String> newData2 = new ArrayList<>();
            newData2.add(employeeID +";"+ email +";"+ "Parkhill@1234" +";"+
                    firstName +" "+ lastName +";"+ idNo +";"+ gender +";"+
                    phoneNo +";"+ "-" +";");
            fh.fileWrite("userProfile.txt", true, newData2);
        }
    }
    
    public void deleteEmployee(String employeeID) {
        List<String> employeeTypeList =  fh.fileRead("employeeList.txt");
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i < employeeTypeList.size(); i++) {
            String[] employeeTypeDetails = employeeTypeList.get(i).split(";");
            String id = employeeTypeDetails[0];
            
            if (!id.equals(employeeID)) {
                newData.add(employeeTypeList.get(i));
            }
        } fh.fileWrite("employeeList.txt", false, newData);
    }
    
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
    
    public void deleteFacility(String facilityID, String facilityName) {
        System.out.println(facilityID +" "+ facilityName);
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
}

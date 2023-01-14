/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import static java.time.LocalDate.now;
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
                newData2.add(propertiesArray[i] + LocalDateTime.now() +";");
                
            } else {
                newData1.add(propertiesArray[i]);
            }
        } 
        fh.fileWrite("propertyDetails.txt", false, newData1);
        fh.fileWrite("inactivePropertyDetails.txt", true, newData2);
        deleteUser(unitNo);
    }
    
    public void deleteUser(String unitNo) {
        List<String> userList =  fh.fileRead("userProfile.txt");
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        List<String> newData1 = new ArrayList<>();
        List<String> newData2 = new ArrayList<>();
        
        for (int i = 0; i < userList.size(); i++) {
            String[] propertyDetails = userArray[i].split(";");
            String eUnitNo = propertyDetails[8];
            
            if(eUnitNo.equals(unitNo)) {
                newData2.add(userArray[i] + LocalDateTime.now() +";");
                
            } else {
                newData1.add(userArray[i]);
            }
        } 
        fh.fileWrite("userProfile.txt", false, newData1);
        fh.fileWrite("inactiveUserProfile.txt", true, newData2);
    }
}

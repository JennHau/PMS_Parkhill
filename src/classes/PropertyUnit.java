/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import adminExecutive.AdminExecutive;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author wongj
 */
public class PropertyUnit {
    private String unitNo;
    private String target;
    private int squareFeet;
    private String status;
    private String dateOfSold;
    FileHandling fh = new FileHandling();
    
    public PropertyUnit() {}
    
    public PropertyUnit(String unitNo, String target, int squareFeet,
            String status, String dateOfSold) {
        this.unitNo = unitNo;
        this.target = target;
        this.squareFeet = squareFeet;
        this.status = status;
        this.dateOfSold = dateOfSold;
    }
    
    public PropertyUnit(String[] details) {
        this.unitNo = details[0];
        this.target = details[1];
        this.squareFeet = Integer.valueOf(details[2]);
        this.status = details[3];
        this.dateOfSold = details[4];
    }

    // method to extract specific property unit details
    public List<PropertyUnit> extractAllProperties(String type) {
        List<String> propertiesList = fh.fileRead("propertyDetails.txt");
        
        List<PropertyUnit> unitList = new ArrayList<>();
        
        for (int i = 1; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesList.get(i).split(";");
            String eTypes = propertyDetails[1].toLowerCase();
            if (type.equals(eTypes)) {
                PropertyUnit propertyUnit = new PropertyUnit(propertyDetails);
                unitList.add(propertyUnit);
            }
        } return unitList;
    }
    
    // check whether newly added unitNo is existed
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
    
    public void addUnit() {
        List<String> newData1 = new ArrayList<>();
        newData1.add(this.unitNo +";"+ this.target +";"+ 
                this.squareFeet +";"+ this.getStatus() +";"+ this.getDateOfSold() +";");
        fh.fileWrite("propertyDetails.txt", true, newData1);

        List<String> newData2 = new ArrayList<>();
        newData2.add("Parkhill;parkhill@gmail.com;Parkhill@123;Parkill;"
                + "Residence;-;-;-;" + this.unitNo + ";");             
        fh.fileWrite("userProfile.txt", true, newData2);
    }
    
    // method to modify specific unit details
    public void modifyUnit(String unitNo, String type, String squareFoot) {
        List<String> propertiesList =  fh.fileRead("propertyDetails.txt");
//        String[] propertiesArray = new String[propertiesList.size()];
//        propertiesList.toArray(propertiesArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesList.get(i).split(";");
            String eUnitNo = propertyDetails[0];
            String eTypes = propertyDetails[1];
            String status = propertyDetails[3];
            String dateOfSold = propertyDetails[4];
            
            if(eUnitNo.equals(unitNo) && eTypes.equals(type)) {
                newData.add(eUnitNo +";"+ eTypes +";"+ squareFoot +";"+ status
                +";"+ dateOfSold +";");
            } else {
                newData.add(propertiesList.get(i));
            }
        } fh.fileWrite("propertyDetails.txt", false, newData);
    }
    
    // delete a property unit
    public void deleteUnit(String unitNo) {
        
        List<String> propertiesList =  fh.fileRead("propertyDetails.txt");
        String[] propertiesArray = new String[propertiesList.size()];
        propertiesList.toArray(propertiesArray);
        
        // move deleted unit to inactive text file
        List<String> newData1 = new ArrayList<>();
        List<String> newData2 = new ArrayList<>();
        String currentDeleteID = null;
        
        for (int i = 0; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesArray[i].split(";");
            String eUnitNo = propertyDetails[0];
            
            if(eUnitNo.equals(unitNo)) {
                currentDeleteID = getURTDeleteID();
                newData2.add(currentDeleteID +";"+ propertiesArray[i]
                        + LocalDateTime.now() +";");
            } else {
                newData1.add(propertiesArray[i]);
            }
        } 
        fh.fileWrite("propertyDetails.txt", false, newData1);
        fh.fileWrite("inactivePropertyDetails.txt", true, newData2);
        AdminExecutive AE = new AdminExecutive();
        AE.deleteTenantResident(unitNo, currentDeleteID);
    }
    
    public String getURTDeleteID() {
        Integer tempID; 
        AdminExecutive AE = new AdminExecutive();
        Integer currentDeleteID1 = Integer.parseInt(AE.getLatestID
                    ("inactiveUserProfile.txt", "dlt").substring(3));
        Integer currentDeleteID2 = Integer.parseInt(AE.getLatestID
                    ("inactivePropertyDetails.txt", "dlt").substring(3));

        if(currentDeleteID1>currentDeleteID2) {
            tempID = currentDeleteID1;
        } else {
            tempID = currentDeleteID2;
        }
                
        int times = 6 - String.valueOf(tempID).length();
        
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
        String currentUsableID = "dlt" + zero +String.valueOf(tempID);
        return currentUsableID;
    }
    
    // extract all deleted property unit details
    public List<String> extractAllPropertiesHistory(String type) {
        List<String> propertiesList = fh.fileRead("inactivePropertyDetails.txt");
        
        List<String> availableList = new ArrayList<>();
        
        for (int i = 1; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesList.get(i).split(";");
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
    
    // validate whether a same unitNo is existed before restore deleted property unit
    public boolean restoreUnitValidation(String unitNo) {
        List<String> propertiesList = fh.fileRead("propertyDetails.txt");
        
        for (int i = 1; i < propertiesList.size(); i++) {
            String[] propertyDetails = propertiesList.get(i).split(";");
            String eUnitNo = propertyDetails[0];
            if (unitNo.equals(eUnitNo)) {
                return false;
            }
        } return true;
    }
    
    // method to restore deleted property unit
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
        AdminExecutive AE = new AdminExecutive();
        AE.restoreResidentTenant(deletionID);
    }
    
    // get all unsold unit
    public List<String> getAvailableUnit(String type) {
        List<String> availableList = new ArrayList<>();
        
        List<String> userList = fh.fileRead("userProfile.txt");
        
        switch (type) {
            case "Commercial" -> {
                for (int i = 1; i < userList.size(); i++) {
                    String[] userDetails = userList.get(i).split(";");
                    String userID = userDetails[0];
                    String unitNo = userDetails[8];
                    
                    if (userID.equals("Parkhill") && unitNo.startsWith("S")) {
                        availableList.add(unitNo);
                    }
                }
            }
            case "Tenant" -> {
                for (int i = 1; i < userList.size(); i++) {
                    String[] userDetails = userList.get(i).split(";");
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
                    String[] userDetails = userList.get(i).split(";");
                    String uniNo = userDetails[8];
                    
                    for (int j = 1; j < userList.size(); j++) {
                        String[] userDetails2 = userList.get(i).split(";");
                        String userID = userDetails2[0];
                        String uniNo2 = userDetails2[8];
                        if (uniNo.equals(uniNo2) && !uniNo.startsWith("S") 
                                && !userID.equals("Parkhill")) {
                            check++;
                        }
                    // check == 1 (consists tenant) / check == 2 (consists RT)    
                    } if (check == 1) {
                        availableList.add(uniNo);
                    }
                }
            }
            default -> {
            } 
        } return availableList;
    }   
    
    // method to change property unit status
    public void updatePropertySoldStatus(String unitNo, String status) {
        List<String> propertyList = fh.fileRead("propertyDetails.txt");
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i < propertyList.size(); i++) {
            String[] propertyDetails = propertyList.get(i).split(";");
            PropertyUnit PU = new PropertyUnit(propertyDetails);
            String eUnitNo = PU.getUnitNo();
            String eTarget = PU.getTarget();
            String eSquareF = String.valueOf(PU.getSquareFeet());
            
            if (unitNo.equals(eUnitNo) && status.equals("sold")) {
                newData.add(eUnitNo +";"+ eTarget +";"+ eSquareF +";"+ "sold"
                        +";"+ todayDate() +";");
            } else if (unitNo.equals(eUnitNo) && status.equals("unsold")) {
                newData.add(eUnitNo +";"+ eTarget +";"+ eSquareF +";"+ "unsold"
                        +";"+ "-" +";");
            } else {
                newData.add(propertyList.get(i));
            }
            
        } fh.fileWrite("propertyDetails.txt", false, newData);
    }
    
    // method to convert today date to dd/MM/yyyy format
    public String todayDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String str = formatter.format(date);
        return str;
    }
    
    /**
     * @return the unitNo
     */
    public String getUnitNo() {
        return unitNo;
    }

    /**
     * @param unitNo the unitNo to set
     */
    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    /**
     * @return the target
     */
    public String getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * @return the squareFeet
     */
    public int getSquareFeet() {
        return squareFeet;
    }

    /**
     * @param squareFeet the squareFeet to set
     */
    public void setSquareFeet(int squareFeet) {
        this.squareFeet = squareFeet;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the dateOfSold
     */
    public String getDateOfSold() {
        return dateOfSold;
    }

    /**
     * @param dateOfSold the dateOfSold to set
     */
    public void setDateOfSold(String dateOfSold) {
        this.dateOfSold = dateOfSold;
    }
}

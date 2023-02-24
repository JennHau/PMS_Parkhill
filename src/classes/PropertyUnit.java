/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import java.text.SimpleDateFormat;
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

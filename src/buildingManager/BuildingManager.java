/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buildingManager;

import accountExecutive.AccountExecutive;
import buildingExecutive.BuildingExecutive;
import java.awt.Color;
import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
public class BuildingManager extends Users{
    
    FileHandling fh = new FileHandling();
    
    // constructor for Building Manager
    public BuildingManager (String userID, String email, String password, String firstName,
                 String lastName, String identificationNo, String gender, String phoneNo){
        super(userID, email, password,  firstName, lastName,  identificationNo,
                gender, phoneNo);
    }
    
    // extract all user based on user type
    public List<String> extractAllUser(String type) {
        List<String> userList = fh.fileRead("userProfile.txt");
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        List<String> availableList = new ArrayList<>();
        
        for (int i = 1; i < userList.size(); i++) {
            String[] userDetails = userArray[i].split(";");
            String userID = userDetails[0];
            
            if (type.equals("Account Executive")) {
                if (userID.startsWith("ace")) {
                availableList.add(userArray[i]);
                }
            } else if (type.equals("Building Executive")) {
                if (userID.startsWith("bde")) {
                    availableList.add(userArray[i]);
                }
            }
        } return availableList;
    }
    
    // get lastest usable primary ID
    public String getLatestID(String filename, String initial) {
        List<String> userList =  fh.fileRead(filename);
        String[] userArray = new String[userList.size()];
        userList.toArray(userArray);
        
        int largestID = 0;
        for (int i = 1; i < userList.size(); i++) {
            String[] userDetails = userArray[i].split(";");
            String id = userDetails[0];
            if(id.startsWith(initial.toLowerCase())) {   
                int existingID = Integer.valueOf(userDetails[0].substring(3));
            
                if(existingID > largestID) {
                    largestID = existingID;
                }
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
    
    // method to store new Account Executive account
    public void addAccountExecutive(AccountExecutive ACE) {
        List<String> newData = new ArrayList<>();
        newData.add(ACE.getUserID() +";"+ ACE.getEmail() +";"+ACE.getPassword()
                +";"+ ACE.getFirstName() +";"+ ACE.getLastName()
                +";"+ ACE.getIdentificationNo()+";"+ ACE.getGender()
                +";"+ ACE.getPhoneNo() +";"+ "-" +";");
        
        fh.fileWrite("userProfile.txt", true, newData);
    }
    
    // method to store new Building Executive account
    public void addBuildingExecutive(BuildingExecutive BE) {
        List<String> newData = new ArrayList<>();
        newData.add(BE.getUserID() +";"+ BE.getEmail() +";"+BE.getPassword()
                +";"+ BE.getFirstName() +";"+ BE.getLastName()
                +";"+ BE.getIdentificationNo()+";"+ BE.getGender()
                +";"+ BE.getPhoneNo() +";"+ "-" +";");
        
        fh.fileWrite("userProfile.txt", true, newData);
    }
    
    // method to delete user
    public void deleteUser(String userID) {
        List<String> employeeTypeList =  fh.fileRead("userProfile.txt");
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i < employeeTypeList.size(); i++) {
            String[] employeeTypeDetails = employeeTypeList.get(i).split(";");
            String id = employeeTypeDetails[0];
            
            if (!id.equals(userID)) {
                newData.add(employeeTypeList.get(i));
            }
        } fh.fileWrite("userProfile.txt", false, newData);
    }
    
    // method to get specific user details
    public String[] extractEmployeeDetails(String userID) {
        List<String> userProfile = fh.fileRead("userProfile.txt");
        String[] userProfileArray = new String[userProfile.size()];
        userProfile.toArray(userProfileArray);
        
        for (int i = 0; i<userProfile.size(); i++) {
            String[] userInfo2 = userProfileArray[i].split(";");
            String userID_temp = userInfo2[0];
            
            if (userID_temp.equals(userID.toLowerCase())) {
                return userInfo2;
            }
        } return null;
    }
    
    // method to modify Account Executive account
    public void modifyACEAccount(AccountExecutive ACE) {
        List<String> userProfile = fh.fileRead("userProfile.txt");
        String[] userProfileArray = new String[userProfile.size()];
        userProfile.toArray(userProfileArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i<userProfile.size(); i++) {
            String[] userInfo = userProfileArray[i].split(";");
            String userID_temp = userInfo[0];
            String password_temp = userInfo[2];
            
            if (userID_temp.equals(ACE.getUserID())) {
                newData.add(ACE.getUserID() +";"+ ACE.getEmail() +";"+
                        password_temp +";"+ ACE.getFirstName() +";"+ 
                        ACE.getLastName() +";"+ ACE.getIdentificationNo() +";"+
                        ACE.getGender() +";"+ ACE.getPhoneNo() +";"+ "-" +";");
            } else {
                newData.add(userProfileArray[i]);
            }
        } fh.fileWrite("userProfile.txt", false, newData);
    }
    
    // modify Building Executive account
    public void modifyBEAccount(BuildingExecutive BE) {
        List<String> userProfile = fh.fileRead("userProfile.txt");
        String[] userProfileArray = new String[userProfile.size()];
        userProfile.toArray(userProfileArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i<userProfile.size(); i++) {
            String[] userInfo = userProfileArray[i].split(";");
            String userID_temp = userInfo[0];
            String password_temp = userInfo[2];
            
            if (userID_temp.equals(BE.getUserID())) {
                newData.add(BE.getUserID() +";"+ BE.getEmail() +";"+
                        password_temp +";"+ BE.getFirstName() +";"+ 
                        BE.getLastName() +";"+ BE.getIdentificationNo() +";"+
                        BE.getGender() +";"+ BE.getPhoneNo() +";"+ "-" +";");
            } else {
                newData.add(userProfileArray[i]);
            }
        } fh.fileWrite("userProfile.txt", false, newData);
    }
    
    // check whether specific image file exits
    public boolean checkImageFile(String imageRPath) {
        String imageFile = "src//images//"+imageRPath+".jpg";
        File f = new File(imageFile);
        File imagePath = new File(f.getAbsolutePath());
        return(imagePath.exists());
    }
    
    // modify team structure role person
    public void modifyTeamStructureSlot(String role, String name) {
        List<String> roleList = fh.fileRead("teamStructure.txt");
        List<String> newData = new ArrayList<>();
        
        for(int i=0; i<roleList.size(); i++){
            String[] roleDetails = roleList.get(i).split(";");
            String type = roleDetails[0];
            String eRole = roleDetails[1];
            
            if(type.equals("slot") && eRole.equals(role)) {
                newData.add(type +";"+ eRole +";"+ name +";");
            } else {
                newData.add(roleList.get(i));
            }
        } fh.fileWrite("teamStructure.txt", false, newData);
    }
    
    // delete person from team structure
    public void deleteTeamStructureSlot(String role) {
        List<String> roleList = fh.fileRead("teamStructure.txt");
        List<String> newData = new ArrayList<>();
        
        for(int i=0; i<roleList.size(); i++){
            String[] roleDetails = roleList.get(i).split(";");
            String type = roleDetails[0];
            String eRole = roleDetails[1];
            
            if(type.equals("slot") && eRole.equals(role)) {
                newData.add(type +";"+ eRole +";"+ "-" +";");
            } else {
                newData.add(roleList.get(i));
            }
        } fh.fileWrite("teamStructure.txt", false, newData);
        String imageFile = "src\\images\\"+role+".jpg";
        File image = new File(imageFile);
        image.delete();
    }
    
    // delete committee 
    public void deleteTeamStructureOther(String role, String name) {
        List<String> roleList = fh.fileRead("teamStructure.txt");
        List<String> newData = new ArrayList<>();
        
        for(int i=0; i<roleList.size(); i++){
            String[] roleDetails = roleList.get(i).split(";");
            String type = roleDetails[0];
            String eRole = roleDetails[1];
            String eName = roleDetails[2];
            
            if(type.equals("other") && eRole.equals(role) && eName.equals(name)) {
            } else {
                newData.add(roleList.get(i));
            }
        } fh.fileWrite("teamStructure.txt", false, newData);
    }
    
    // add committee
    public void addTeamStructureOther(String role, String name) {
        List<String> newData = new ArrayList<>();
        newData.add("other" +";"+ role +";"+ name +";");
        fh.fileWrite("teamStructure.txt", true, newData);
    }
    
    // validate whether user with the role is existed
    public boolean addTeamStructureOtherValidate(String role, String name) {
        List<String> roleList = fh.fileRead("teamStructure.txt");
        
        for(int i=1; i<roleList.size(); i++){
            String[] roleDetails = roleList.get(i).split(";");
            String type = roleDetails[0];
            String eRole = roleDetails[1].toLowerCase();
            String eName = roleDetails[2].toLowerCase();
            
            if(type.equals("other") && eRole.equals(role) && eName.equals(name)) {
                return false;
            } 
        } return true;
    }
    
    // convert float into two decimal places
    public String currencyFormat(float amount) {
        DecimalFormat df = new DecimalFormat("0.00");
        String unitPrice = df.format(amount);
        return unitPrice;
    }
    
    // calculate total allocated capital
    public String calculateTotalCapital() {
        List<String> financialCapital = fh.fileRead("financialCapital.txt");
        Float totalCapital = 0.00f;
        for (int i = 1; i < financialCapital.size(); i++) {
            String[] capitalDetails = financialCapital.get(i).split(";");
            float capital = Float.parseFloat(capitalDetails[0]);
            totalCapital = totalCapital + capital;
        }
        String tCapital = currencyFormat(totalCapital);
        return tCapital;
    }
    
    // calculate whole income from utilities/ rental fee
    public String calculateTotalMothlyIncome() {
        List<String> facilityPayment = fh.fileRead("payment.txt");
        Float total = 0.00f;
        
        
        for (int i = 1; i < facilityPayment.size(); i++) {
            String[] facilityDetails = facilityPayment.get(i).split(";");
            Float price = Float.parseFloat(facilityDetails[7]);
            total = total + price;
        }
        String tCapital = currencyFormat(total);
        return tCapital;
    }
    
    // calculate all income from facility booking
    public String calculateTotalFacilityIncome() {
        List<String> payment = fh.fileRead("facilityBooking.txt");
        Float total = 0.00f;
        
        List<String> addedBooking = new ArrayList<>();
        
        for (int i = 1; i < payment.size(); i++) {
            String[] paymentDetails = payment.get(i).split(";");
            String bookingID = paymentDetails[0];
            String temp_price = paymentDetails[8];
            
            if(!addedBooking.contains(bookingID) && !temp_price.equals("-")) {
                Float price = Float.parseFloat(temp_price);
                total = total + price;
                addedBooking.add(bookingID);
            }
        }
        String tCapital = currencyFormat(total);
        return tCapital;
    }
    
    // calculate all allocated budget
    public String calculateAllocatedBudget() {
        List<String> allocationList = fh.fileRead("budgetAllocation.txt");
        Float total = 0.00f;
        
        for (int i = 1; i < allocationList.size(); i++) {
            String[] allocationDetails = allocationList.get(i).split(";");
            Float price = Float.parseFloat(allocationDetails[3]);
            total = total + price;
        }
        String tCapital = currencyFormat(total);
        return tCapital;
    }
    
    // method to delete allocated capital
    public void deleteFinancialCapital(String amount, String date) {
        List<String> allocationList = fh.fileRead("financialCapital.txt");
        List<String> newData = new ArrayList<>();
        
        int check = 0;
        for (int i = 0; i < allocationList.size(); i++) {
            String[] allocationDetails = allocationList.get(i).split(";");
            String eAmount = allocationDetails[0];
            String eDate = allocationDetails[1];
            
            if(amount.equals(eAmount) && date.equals(eDate) && check==0) {
                check++;
            } else {
                newData.add(allocationList.get(i));
            }
        } fh.fileWrite("financialCapital.txt", false, newData);
    }
    
    // method to store newly added financial capital
    public void addFinancialCapital(String amount) {
        List<String> newData = new ArrayList<>();
        newData.add(amount +";"+ String.valueOf(LocalDate.now()) +";");
        fh.fileWrite("financialCapital.txt", true, newData);
    }
    
    // method to delete allocated budget
    public void deleteBudgetAllocation(String allocationID) {
        List<String> allocationList = fh.fileRead("budgetAllocation.txt");
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i < allocationList.size(); i++) {
            String[] allocationDetails = allocationList.get(i).split(";");
            String eAllocationID = allocationDetails[0];
            
            if(!eAllocationID.equals(allocationID)) {
                newData.add(allocationList.get(i));
            }
        } fh.fileWrite("budgetAllocation.txt", false, newData);
    }
    
    // get current date and time
    public String currentDateTime() {
        LocalDateTime dt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String currentDateTime = dtf.format(dt);
        return currentDateTime;
    }
    
    // extract all property unit data for report generation
    public List<String> propertyUnitReport() {
        List<String> unitList = fh.fileRead("propertyDetails.txt");
        List<String> userList = fh.fileRead("userProfile.txt");
        List<String> addedUser = new ArrayList<>();
        
        List<String> availableList = new ArrayList<>();
        
        for(int i=1; i<unitList.size(); i++) {
            String[] unitDetails = unitList.get(i).split(";");
            String unitNo = unitDetails[0];
            String category = unitDetails[1];
            String status = unitDetails[3];
            
            for(int j=1; j<userList.size(); j++) {
                String[] userDetails = userList.get(i).split(";");
                String userID = userDetails[0];
                String name = userDetails[3] +" "+ userDetails[4];
                String uUnitNo = userDetails[8];
                
                if(unitNo.equals(uUnitNo) && (userID.startsWith("tnt") 
                        || userID.startsWith("vdr")) && !addedUser.contains(userID)) {
                    availableList.add(unitNo +";"+ category +";"+ status +";"+
                            userID +";"+ "Tenant" +";"+ name);
                    addedUser.add(userID);
                } else if(unitNo.equals(uUnitNo) && userID.startsWith("rsd")
                        && !addedUser.contains(userID)) {
                    availableList.add(unitNo +";"+ category +";"+ status +";"+
                            userID +";"+ "Resident" +";"+ name);
                    addedUser.add(userID);
                }
            }
        } return availableList;
    }
    
    // method to design all tables
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

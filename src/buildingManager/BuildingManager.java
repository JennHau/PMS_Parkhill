/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buildingManager;

import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import pms_parkhill_residence.FileHandling;
import pms_parkhill_residence.Users;

/**
 *
 * @author wongj
 */
public class BuildingManager extends Users{
    
    FileHandling fh = new FileHandling();
    
    public BuildingManager (String userID, String email, String password, String firstName,
                 String lastName, String identificationNo, String gender, String phoneNo){
        super(userID, email, password,  firstName, lastName,  identificationNo,
                gender, phoneNo);
    }
    
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
    
    public void userRegistration(String userID, String email, String password, 
            String firstName, String lastName, String identificationNo, String gender,
            String phoneNo) {
        
        List<String> newData = new ArrayList<>();
        newData.add(userID +";"+ email +";"+password +";"+ firstName +";"+ lastName
                +";"+ identificationNo +";"+ gender +";"+ phoneNo +";"+ "-" +";");
        
        fh.fileWrite("userProfile.txt", true, newData);
    }
    
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
    
    public void modifyOthersAccount(String userID, String email, String password,
            String firstName, String lastName, String identificationNo,
            String gender, String phoneNo, String unitNo) {
        List<String> userProfile = fh.fileRead("userProfile.txt");
        String[] userProfileArray = new String[userProfile.size()];
        userProfile.toArray(userProfileArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i<userProfile.size(); i++) {
            String[] userInfo = userProfileArray[i].split(";");
            String userID_temp = userInfo[0];
            String password_temp = userInfo[2];
            
            if (userID_temp.equals(userID)) {
                newData.add(userID +";"+ email +";"+ password_temp +";"+ firstName
                        +";"+ lastName +";"+ identificationNo +";"+ gender
                        +";"+ phoneNo +";"+ unitNo +";");
            } else {
                newData.add(userProfileArray[i]);
            }
        } fh.fileWrite("userProfile.txt", false, newData);
    }
    
    public boolean checkImageFile(String imageRPath) {
        String imageFile = "src//images//"+imageRPath+".jpg";
        File f = new File(imageFile);
        File imagePath = new File(f.getAbsolutePath());
        return(imagePath.exists());
    }
    
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
    
    public void addTeamStructureOther(String role, String name) {
        List<String> newData = new ArrayList<>();
        newData.add("other" +";"+ role +";"+ name +";");
        fh.fileWrite("teamStructure.txt", true, newData);
    }
    
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
    
    public String currencyFormat(float amount) {
        DecimalFormat df = new DecimalFormat("0.00");
        String unitPrice = df.format(amount);
        return unitPrice;
    }
    
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
    
    public void addFinancialCapital(String amount) {
        List<String> newData = new ArrayList<>();
        newData.add(amount +";"+ String.valueOf(LocalDate.now()) +";");
        fh.fileWrite("financialCapital.txt", true, newData);
    }
    
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
    
    public String currentDateTime() {
        LocalDateTime dt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String currentDateTime = dtf.format(dt);
        return currentDateTime;
    }
    
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
}

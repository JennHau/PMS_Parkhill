/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buildingManager;

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
}

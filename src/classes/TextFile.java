/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 *
 * @author Winson
 */
public class TextFile {
    FileHandling FH = new FileHandling();
    
    // Text file default splitter
    public String sp = ";";
    
    // Empty data cell symbol
    public String empty = "-";
    
    // user profile text file link
    public String userProfile = "userProfile.txt";
    
    // inactivated account text file link
    public String inactiveAcc = "inactiveAcc.txt";
    
    // employee name list link
    public String fullEmployeeList = "employeeList.txt";
    
    // employee job file link
    public String employeeJobFile = "employeeJobFile.txt";
    
    // selected Employee record
    public String recordSelectedEmployee = "recordSelectedEmployee.txt";
    
    // job list link
    public String jobListFile = "jobList.txt";
    
    // complaint files link
    public String complaintFiles = "complaints.txt";
    
    // patrolling shcedule file
    public String patrollingScheduleFile = "patrollingSchedule.txt";
    
    // File schedule patrolling default time table
    public String fixFile = "patrollingDefaultSchedule.txt";
    
    // File each date schedule setting file
    public String patScheduleModRec = "patScheduleModRec.txt";
    
    // File to store history file
    public String jobFileHistory = "jobFileHistory.txt";
    
    // File to store visitor pass
    public String visitorPass = "visitorPass.txt";
    
    // File for invoice
    public String invoiceFile = "invoices.txt";
    
    // File for payment
    public String paymentFile = "payment.txt";
    
    // File for storing facility booking (Including payment)
    public String facilityBookingFile = "facilityBooking.txt";
    
    // File to store invoice receipt
    public String receiptFile = "receipt.txt";
    
    // File to store issued statement
    public String statementFile = "statements.txt";
    
    // File to store the available facilities
    public String facilityFile = "facility.txt";
    
    // temporary usage file
    public String tempFile = "tempPatFile.txt";
    
    public String getLatestID(String filename, String initial) {
        List<String> userList =  FH.fileRead(filename);
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
    
    public boolean replaceFile(String fileLocation, String destination) throws IOException {
        File dest = new File(destination);
        File fileLoc = new File(fileLocation);
        
        if (dest.exists()) {
            dest.delete();
        }
        
        Files.copy(fileLoc.toPath(), dest.toPath());
        
        return dest.exists();
    }
}

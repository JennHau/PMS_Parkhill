/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buildingExecutive;

import buildingExecutive.BuildingExecutive;
import java.util.ArrayList;
import java.util.List;
import pms_parkhill_residence.FileHandling;

/**
 *
 * @author Winson
 */
public class CRUD extends FileHandling {
    BuildingExecutive BE = new BuildingExecutive();
    
    public boolean create(String fileName, List<String> newData) {
        this.fileWrite(fileName, true, newData);
        return true;
    }
    
    public String read(String fileName, String Id) {
        List<String> fileItems = this.fileRead(fileName);
        
        for (String eachItem : fileItems) {
            String[] itemDetails = eachItem.split(BE.sp);
            String itemId = itemDetails[0];
            if (itemId.equals(Id)) {
                return eachItem;
            }
        }
        
        return null;
    }
    
    public boolean update(String fileName, String Id, String updatedData, int idCol) {
        ArrayList<String> updatedList = new ArrayList<>();
        List<String> fileItems = this.fileRead(fileName);
        
        boolean itemUpdated = false;
        
        for (String eachItem : fileItems) {
            String[] itemDetails = eachItem.split(BE.sp);
            String itemId = itemDetails[idCol];
            if (itemId.equals(Id)) {
                updatedList.add(updatedData);
                itemUpdated = true;
            }
            else {
                updatedList.add(eachItem);
            }
        }
        
        this.fileWrite(fileName, false, updatedList);
        
        return itemUpdated;
    }
    
    public boolean delete(String fileName, String Id, int idCol) {
        ArrayList<String> removedList = new ArrayList<>();
        List<String> fileItems = this.fileRead(fileName);
        
        boolean itemRemoved = false;
        
        for (String eachItem : fileItems) {
            String[] itemDetails = eachItem.split(BE.sp);
            String itemId = itemDetails[idCol];
            if (!itemId.equals(Id)) {
                removedList.add(eachItem);
            }
            else {
                itemRemoved = true;
            }
        }
        
        this.fileWrite(fileName, false, removedList);
        
        return itemRemoved;
    }
}

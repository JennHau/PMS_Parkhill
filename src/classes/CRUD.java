/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Winson
 */
public class CRUD extends FileHandling {
    TextFile TF = new TextFile();
    
    // add new item into text file method (file name, list of data to add)
    public boolean create(String fileName, List<String> newData) {
        this.fileWrite(fileName, true, newData);
        return true;
    }
    
    // get item from text file (file name to retrieve, item id)
    public String read(String fileName, String Id) {
        List<String> fileItems = this.fileRead(fileName);
        
        for (String eachItem : fileItems) {
            String[] itemDetails = eachItem.split(TF.sp);
            String itemId = itemDetails[0];
            if (itemId.equals(Id)) {
                return eachItem;
            }
        }
        
        return null;
    }
    
    // update item in text file (file name, item id, new data to update, id column)
    public boolean update(String fileName, String Id, String updatedData, int idCol) {
        ArrayList<String> updatedList = new ArrayList<>();
        List<String> fileItems = this.fileRead(fileName);
        
        boolean itemUpdated = false;
        
        for (String eachItem : fileItems) {
            String[] itemDetails = eachItem.split(TF.sp);
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
    
    // delete item from text file (file name, item id, id column)
    public boolean delete(String fileName, String Id, int idCol) {
        ArrayList<String> removedList = new ArrayList<>();
        List<String> fileItems = this.fileRead(fileName);
        
        boolean itemRemoved = false;
        
        for (String eachItem : fileItems) {
            String[] itemDetails = eachItem.split(TF.sp);
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

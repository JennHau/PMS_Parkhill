/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wongj
 */
public class FileHandling {
    
    public List<String> fileRead (String filename) {
        
        List<String> data = new ArrayList<>();
        
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            
            for (String line = br.readLine(); line != null; line = br.readLine()){
                data.add(line);
            }
            br.close(); fr.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    } 
    
    public void fileWrite (String filename, boolean append, List<String> newData) {
        try {
            FileWriter fw = new FileWriter(filename, append);
            BufferedWriter bw = new BufferedWriter(fw);
            
            for (String newDataLine : newData) {
                bw.write(newDataLine + "\n");
            }
            fw.flush(); bw.flush(); fw.close(); bw.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    
}

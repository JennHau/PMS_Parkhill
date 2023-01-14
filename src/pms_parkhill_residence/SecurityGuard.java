/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pms_parkhill_residence;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author andre
 */
public class SecurityGuard extends Users {

    public static void main(String[] args) throws IOException {
        displayTable("test-table.txt");

    }

//    class TwoArrays {
//
//        public String[] header;
//        public String[] row;
//
//        public TwoArrays(String[] a, String[] b) {
//            this.header = a;
//            this.row = b;
//        }
//    }
    public static Object[] displayTable(String f) {
        String filepath = f;
        File file = new File(filepath);
        Object[] tableline = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();

            tableline = br.lines().toArray();
            System.out.println(tableline);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SecurityGuard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SecurityGuard.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tableline;

    }

    public static void insertdatatofile(String f, String[] A) throws IOException {
        String filepath = f;
        String[] fileArray = A;
        BufferedWriter bw = new BufferedWriter(new FileWriter(filepath, true));

        for (int i = 0; i < fileArray.length; i++) {
            bw.write(fileArray[i] + ";");

        }
        bw.newLine();
    }
    


};

//---------------------------------------------------
// only want specific data
//   
//    public List<String>gettabledata(String f) throws  IOException{
//        List<String> tabledata =new ArrayList<String>();
//        FileReader fr = new FileReader(f);
//        BufferedReader br =new BufferedReader(fr);
////        br.readLine();
//        
//        for (String line=br.readLine();line!=null;line=br.readLine()){
//         tabledata.add(line);
//        
//        }
//        br.close();
//        fr.close();
//        
//        return tabledata;
//    }


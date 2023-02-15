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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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

    public static LocalDate currentdate() {
        LocalDate Current = LocalDate.now();
//        DateTimeFormatter format = DateTimeFormatter.ofPattern(" MMMM YYYY");
//        String F_Current = Current.format(format);
        return Current;
    }

    ;
    
    public static String convertdate(String f) throws ParseException {
        String datestring =f;
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = inputFormat.parse(datestring);
        String outputString = outputFormat.format(date);

        System.out.println(outputString); // Output: "2023-02-24"
        return outputString;
    }

    
    
        public static String currenttime() {
        LocalTime Current = LocalTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        String F_Current = Current.format(format);

        return F_Current;
    }

    ;
        
        public static String coverttime(String f) {
        LocalTime Current = LocalTime.parse(f);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mm a");
        String F_Current = Current.format(format);
        return F_Current;
    }

    ;
        
        

    public static void insertdatatofile(String f, String[] A) throws IOException {
        String filepath = f;
        String[] fileArray = A;
        BufferedWriter bw = new BufferedWriter(new FileWriter(filepath, true));

        for (int i = 0; i < fileArray.length; i++) {
            bw.write(fileArray[i] + ";");

        }
        bw.newLine();
    }

    public void modified_SearchVisitor_data(List<String> m) {
        List<String> modified_data = new ArrayList<>();
        List<String> lst = new ArrayList<String>();
//        lst.add(ids + ";" + names + ";" + u_no + ";" + icno + ";" + CID + ";" + CIT + ";" + COD + "; " + COT + ";" + box + ";");
        String[] lst_ary = new String[m.size()];
        m.toArray(lst_ary);
//        System.out.println(lst);
        for (int x = 0; x < lst_ary.length; x++) {
            String[] ls = lst_ary[x].split(";");
            String pid = ls[0];
            String name = ls[1];
            String u_num = ls[2];
            String icnum = ls[3];
            String cid = ls[4];
            String cod = ls[5];

            List<String> read = fh.fileRead("visitorpass.txt");
            String[] read_data = new String[read.size()];
            read.toArray(read_data);

            for (int j = 0; j < read.size(); j++) {
                String[] readdata = read_data[j].split(";");
                String o_id = readdata[0];
                String o_name = readdata[1];
                String o_u_num = readdata[2];
                String o_icnum = readdata[3];
                String o_cid = readdata[4];
                String o_cod = readdata[5];
//                System.out.println(read_data[j]);
                if (o_id.equalsIgnoreCase(pid)) {
                    o_id = pid;
                    o_name = name;
                    o_u_num = u_num;
                    o_icnum = icnum;
                    o_cid = cid;
                    o_cod = cod;
                    read_data[j] = (o_id + ";" + o_name + ";" + o_u_num + ";" + o_icnum + ";" + o_cid + ";" + o_cod + ";");
                }
                modified_data.add(read_data[j]);

            }

        }
//        System.out.println(modified_data);

        fh.fileWrite("visitorpass.txt", false, modified_data);
        JOptionPane.showMessageDialog(null, "Visitor Pass Updated Successfully");

    }

    public void modified_manageIncident(List<String> m) {
        List<String> modified_data = new ArrayList<>();
        List<String> lst = new ArrayList<String>();
//        lst.add(ids + ";" + names + ";" + u_no + ";" + icno + ";" + CID + ";" + CIT + ";" + COD + "; " + COT + ";" + box + ";");
        String[] lst_ary = new String[m.size()];
        m.toArray(lst_ary);
//        System.out.println(lst);
        for (int x = 0; x < lst_ary.length; x++) {
            String[] ls = lst_ary[x].split(";");
            String ids = ls[0];
            String rcd = ls[1];
            String icd = ls[2];
            String i_date = ls[3];
            String i_status = ls[4];

            List<String> read = fh.fileRead("SG_Incident.txt");
            String[] read_data = new String[read.size()];
            read.toArray(read_data);

            for (int j = 0; j < read.size(); j++) {
                String[] readdata = read_data[j].split(";");
                String o_id = readdata[0];
                String o_rcd = readdata[1];
                String o_icd = readdata[2];
                String o_date = readdata[3];
                String o_status = readdata[4];
//                System.out.println(read_data[j]);
                if (o_id.equalsIgnoreCase(ids)) {
                    o_id = ids;
                    o_rcd = rcd;
                    o_icd = icd;
                    o_date = i_date;
                    o_status=i_status;
                    read_data[j] = (o_id + ";" + o_rcd + ";" + o_icd + ";" + o_date + ";"+ o_status + ";");
                }
                modified_data.add(read_data[j]);

            }

        }
//        System.out.println(modified_data);

        fh.fileWrite("SG_Incident.txt", false, modified_data);
        JOptionPane.showMessageDialog(null, "Incident Updated Successfully");

    }

    public void change_status_to_checkin(List<String> m) {
        List<String> modified_data = new ArrayList<>();
        List<String> lst = new ArrayList<String>();
        String[] lst_ary = new String[m.size()];
        m.toArray(lst_ary);
//        System.out.println(lst);
        for (int x = 0; x < lst_ary.length; x++) {
            String[] ls = lst_ary[x].split(";");
            String ids = ls[0];
            String u_id = ls[1];
            String rcd = ls[2];
            String icd = ls[3];
            String i_date = ls[4];
            String i_time = ls[5];

            List<String> read = fh.fileRead("SG_Incident.txt");
            String[] read_data = new String[read.size()];
            read.toArray(read_data);

            for (int j = 0; j < read.size(); j++) {
                String[] readdata = read_data[j].split(";");
                String o_id = readdata[0];
                String o_uid = readdata[1];
                String o_rcd = readdata[2];
                String o_icd = readdata[3];
                String o_date = readdata[4];
                String o_time = readdata[5];
//                System.out.println(read_data[j]);
                if (o_id.equals(ids)) {
                    o_id = ids;
                    o_uid = u_id;
                    o_rcd = rcd;
                    o_icd = icd;
                    o_date = i_date;
                    o_time = i_time;
                    read_data[j] = (o_id + ";" + o_uid + ";" + o_rcd + ";" + o_icd + ";" + o_date + ";" + o_time + ";");
                }
                modified_data.add(read_data[j]);

            }

        }
//        System.out.println(modified_data);

        fh.fileWrite("SG_Incident.txt", false, modified_data);
        JOptionPane.showMessageDialog(null, "Incident Updated Successfully");

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


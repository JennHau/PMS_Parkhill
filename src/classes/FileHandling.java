/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    
    public String browseImage() {
        // choose file
        JFileChooser fc = new JFileChooser();
        // limit file extention type
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg");
        fc.setFileFilter(filter);
        int result = fc.showOpenDialog(null);
        
        String oriName="";
        
        // make sure that a file was chosen, else exit
        if (result != JFileChooser.APPROVE_OPTION) {

        } else {
            // get file path
            String path = fc.getSelectedFile().getAbsolutePath();
            // get file name
            oriName = fc.getSelectedFile().getName();
            // create a file in the specific directory
            File source = new File(path);
            
            try {
                //copy file conventional way using Stream
                copyFileUsingStream(source, new File("src\\images\\temp.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            } 
        } return oriName;
    }
    
    public void deleteTempImage() {
        //declare variable to store temporary image file name
        String tmpImgFile = "src\\images\\temp.jpg";
        // create a file in the specific directory
        File tempimage = new File(tmpImgFile);
        // delete temporary image file
        tempimage.delete();
    }
    
    private void copyFileUsingStream(File source, File dest) throws IOException {
        // set input and output stream to null
        InputStream is = null;
        OutputStream os = null;
        try {
            // create InputStream from source
            is = new FileInputStream(source);
            // write it to the destination file using OutputStream
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            // declare length 
            int length;
            // while buffer of size >= one byte (get the exact buffer size)
            while ((length = is.read(buffer)) > 0) {
                // write to source based on the buffer size
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
    
    public void checkUploadedImage(String imageName) {
        //declare variable to store temporary image file name
        String tmpImgFile = "src\\images\\temp.jpg";
        // declare newImgName to store new directory
        String newImgName = "src\\images\\" + imageName + ".jpg";
        // create a file in the specific directory
        File tempimage = new File(tmpImgFile);
        // create a file in the specific directory
        File newImageName = new File(newImgName);
        
        if(tempimage.exists()) {
            // delete old image
            newImageName.delete();
            // rename temporary image to the old image name
            tempimage.renameTo(newImageName);  
        }
    }
}

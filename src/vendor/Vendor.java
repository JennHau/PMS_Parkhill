/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vendor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import classes.CRUD;
import classes.Complaint;
import classes.FileHandling;
import classes.Invoice;
import classes.PMS_DateTimeFormatter;
import classes.Payment;
import classes.TextFile;
import classes.Users;

/**
 *
 * @author Winson
 */
public class Vendor extends Users{
    FileHandling fh = new FileHandling();
    TextFile TF = new TextFile();
    CRUD crud = new CRUD();
    PMS_DateTimeFormatter DTF = new PMS_DateTimeFormatter();
    Complaint CP = new Complaint();
    Payment PYM = new Payment();
    
    private String unitNo;
    
    public Vendor(String userID, String email, String password, String firstName,
                 String lastName, String identificationNo, String gender,
                 String phoneNo, String unitNo) {
        
        super(userID, email, password, firstName,
             lastName, identificationNo, gender,
             phoneNo);
        
        this.unitNo = unitNo;
    }
    
    // Main method
    public void setTableRow(DefaultTableModel table, ArrayList arrayList) {
        table.setRowCount(0);
        
        for (int rowCount = 0; rowCount < arrayList.size(); rowCount++) {
            String[] rowDetails = String.valueOf(arrayList.get(rowCount)).split(TF.sp);
            table.addRow(rowDetails);
        }
    }
    
    // validate table selection
    public String validateTableSelectionAndGetValue(DefaultTableModel table, int selectedColumn, int selectedRow, int expectedColumn, int getValueColumn) {
        if (selectedColumn == expectedColumn) {
            String data = (String) table.getValueAt(selectedRow, getValueColumn);
            return data;
        }
        
        return null;
    }
    
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
    
    // modify self account
    @Override
    public void modifySelfAccount() {
        String userID = this.getUserID().toLowerCase();
        String email = this.getEmail();
        String firstName = this.getFirstName();
        String lastName = this.getLastName();
        String password = this.getPassword();
        String identificationNo = this.getIdentificationNo();
        String gender = this.getGender();
        String phoneNo = this.getPhoneNo();
        
        List<String> userProfile = fh.fileRead("userProfile.txt");
        String[] userProfileArray = new String[userProfile.size()];
        userProfile.toArray(userProfileArray);
        
        List<String> newData = new ArrayList<>();
        
        for (int i = 0; i<userProfile.size(); i++) {
            String[] userInfo = userProfileArray[i].split(";");
            String userID_temp = userInfo[0];
            
            if (userID_temp.equals(userID)) {
                newData.add(userID +";"+ email +";"+ password +";"+ firstName
                        +";"+ lastName +";"+ identificationNo +";"+ gender
                        +";"+ phoneNo +";"+ this.unitNo +";");
            } else {
                newData.add(userProfileArray[i]);
            }
        } fh.fileWrite("userProfile.txt", false, newData);
    }
    
    // Page navigator
    public void toVendorDashboard(Vendor VD) {
        VendorDashboard page = new VendorDashboard(VD);
        page.setVisible(true);
    }
    public void toPaymentManagement(Vendor VD) {
        VendorPaymentManagement page = new VendorPaymentManagement(VD);
        page.setVisible(true);
    }
    public void toComplaints(Vendor VD) {
        VendorComplaints page = new VendorComplaints(VD);
        page.setVisible(true);
    }
    public void toPaymentHistory(Vendor VD) {
        VendorPaymentHistory page = new VendorPaymentHistory(VD);
        page.setVisible(true);
    }
    public void toInvoice(Vendor VD) {
        VendorInvoice page = new VendorInvoice(VD);
        page.setVisible(true);
    }
    public void toInvoiceReceipt(Vendor VD, String invoiceNo) {
        VendorInvoiceReceipt page = new VendorInvoiceReceipt(VD, invoiceNo);
        page.setVisible(true);
    }
    public void toInvoicePayment(String invoiceNo, Vendor VD, ArrayList<Invoice> invoiceList) {
        VendorInvoicePayment page = new VendorInvoicePayment(invoiceNo, VD, invoiceList);
        page.setVisible(true);
    }
    public void toViewPaidInvoice(Vendor VD, String invoiceNo, ArrayList<Payment> invoiceList) {
        VendorViewPaidInvoice page = new VendorViewPaidInvoice(invoiceNo, VD, invoiceList);
        page.setVisible(true);
    }
    public void toStatement(Vendor VD) {
        VendorStatement page = new VendorStatement(VD);
        page.setVisible(true);
    }
    public void toStatementReport(Vendor VD, String monthNyear) {
        VendorStatementReport page = new VendorStatementReport(VD, monthNyear);
        page.setVisible(true);
    }
    public void toViewProfile(Vendor VD) {
        VendorProfile page = new VendorProfile(VD);
        page.setVisible(true);
    }
    public void toPaymentCredential(Vendor VD, String totalAmount, ArrayList<Invoice> itemId) {
        VendorPaymentCredential page = new VendorPaymentCredential(VD, totalAmount, itemId);
        page.setVisible(true);
    }

    
    /**
     * @return the unitNo
     */
    public String getUnitNo() {
        return unitNo;
    }

    /**
     * @param unitNo the unitNo to set
     */
    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package residentANDtenant;

import java.awt.Color;
import java.time.LocalTime;
import pms_parkhill_residence.CRUD;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import pms_parkhill_residence.Complaint;
import pms_parkhill_residence.Facility;
import pms_parkhill_residence.FileHandling;
import pms_parkhill_residence.Invoice;
import pms_parkhill_residence.PMS_DateTimeFormatter;
import pms_parkhill_residence.Payment;
import pms_parkhill_residence.TextFile;
import pms_parkhill_residence.Users;
import pms_parkhill_residence.VisitorPass;

/**
 *
 * @author Winson
 */
public class ResidentTenant extends Users {
    FileHandling FH = new FileHandling();
    TextFile TF = new TextFile();
    CRUD crud = new CRUD();
    PMS_DateTimeFormatter DTF = new PMS_DateTimeFormatter();
    Complaint CP = new Complaint();
    Payment PYM = new Payment();
    VisitorPass VP = new VisitorPass();
    
    private String unitNo;
    
    public ResidentTenant() {}
    
    public ResidentTenant(String userID, String email, String password, String firstName,
                 String lastName, String identificationNo, String gender,
                 String phoneNo, String unitNo) {
        
        super(userID, email, password, firstName,
             lastName, identificationNo, gender,
             phoneNo);
        
        this.unitNo = unitNo;
    }
    
    public String getNewPassID() {
        return VP.generateNewPassId();
    }
    
    public void setTableRow(DefaultTableModel table, ArrayList arrayList) {
        table.setRowCount(0);
        
        for (int rowCount = 0; rowCount < arrayList.size(); rowCount++) {
            String[] rowDetails = String.valueOf(arrayList.get(rowCount)).split(TF.sp);
            table.addRow(rowDetails);
        }
    }
    
    public String getVisitorDetails(String passId) {
        List<String> visitorFile = FH.fileRead(TF.visitorPass);
        for (String eachVis : visitorFile) {
            String pID = eachVis.split(TF.sp)[0];
            if (pID.equals(passId)) {
                return eachVis;
            }
        }
        
        return null;
    }
    
    public ArrayList getCurrentUnitBookedFacility(String unitNo) {
        ArrayList<String> bookedFacility = new ArrayList<>();
        
        List<String> facilityBookingFile = FH.fileRead(TF.facilityBookingFile);
        
        for (String eachBooked : facilityBookingFile) {
            String[] bookedDet = eachBooked.split(TF.sp);
            String uNo = bookedDet[3];
            
            if (uNo.equals(unitNo)) {
                bookedFacility.add(eachBooked);
            }
        }
        
        return bookedFacility;
    }
    
    public String getFacilityId(String bookingId) {
        List<String> facilityBookingFile = FH.fileRead(TF.facilityBookingFile);
        for (String eachBooking : facilityBookingFile) {
            String bId = eachBooking.split(TF.sp)[0];
            if (bId.equals(bookingId)) {
                return eachBooking.split(TF.sp)[1];
            }
        }
        
        return null;
    }
    
    public String[] getBookedStartAndEndTime(String bookingId) {
        String[] startEndTime = {"", ""};
        List<String> facilityBooking = FH.fileRead(TF.facilityBookingFile);
        
        for (String eachBooking : facilityBooking) {
            String[] bookingDet = eachBooking.split(TF.sp);
            String bookingID = bookingDet[0];
            
            if (bookingID.equals(bookingId)) {
                String startTime = bookingDet[5];
                String endTime = bookingDet[6];
                
                if (startEndTime[0].equals("")) {
                    startEndTime[0] = startTime;
                }
                if (startEndTime[1].equals("")) {
                    startEndTime[1] = endTime;
                }
                
                if (LocalTime.parse(startTime).isBefore(LocalTime.parse(startEndTime[0]))) {
                    startEndTime[0] = startTime;
                }
                if (LocalTime.parse(endTime).isAfter(LocalTime.parse(startEndTime[1]))) {
                    startEndTime[1] = endTime;
                }
            }
        }
        
        return startEndTime;
    }
    
    public String validateTableSelectionAndGetValue(DefaultTableModel table, int selectedColumn, int selectedRow, int expectedColumn, int getValueColumn) {
        if (selectedColumn == expectedColumn) {
            String data = (String) table.getValueAt(selectedRow, getValueColumn);
            return data;
        }
        
        return null;
    }
    
    public String getSpecificUser(String userId) {
        List<String> userProfile = FH.fileRead(TF.userProfile);
        for (String eachUser : userProfile) {
            String uId = eachUser.split(TF.sp)[0];
            if (uId.equals(userId)) {
                return eachUser;
            }
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
        
        List<String> userProfile = FH.fileRead("userProfile.txt");
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
        } FH.fileWrite("userProfile.txt", false, newData);
    }
  
    // Page Navigator
    public void toResidentTenantDashboard(ResidentTenant RT) {
        ResidentTenantMainPage page = new ResidentTenantMainPage(RT);
        page.setVisible(true);
    }
    
    public void toPaymentCredential(ResidentTenant RT, String totalAmount, ArrayList itemId, boolean forFacility, boolean modifyBooking, ArrayList<Invoice> invoiceList) {
        ResidentTenantPaymentCredential page = new ResidentTenantPaymentCredential(RT, totalAmount, itemId, forFacility, modifyBooking, invoiceList);
        page.setVisible(true);
    }
    
    public void toPaymentManagement(ResidentTenant RT) {
        ResidentTenantPaymentManagement page = new ResidentTenantPaymentManagement(RT);
        page.setVisible(true);
    }
    
    public void toPaymentHistory(ResidentTenant RT) {
        ResidentTenantPaymentHistory page = new ResidentTenantPaymentHistory(RT);
        page.setVisible(true);
    }
    
    public void toInvoice(ResidentTenant RT) {
        ResidentTenantInvoice page = new ResidentTenantInvoice(RT);
        page.setVisible(true);
    }
    
    public void toInvoicePayment(String invoiceNo, ResidentTenant RT, ArrayList<Invoice> feeTypes) {
        ResidentTenantInvoicePayment page = new ResidentTenantInvoicePayment(invoiceNo, RT, feeTypes);
        page.setVisible(true);
    }
    
    public void toStatement(ResidentTenant RT) {
        ResidentTenantStatement page = new ResidentTenantStatement(RT);
        page.setVisible(true);
    }
    
    public void toFacilityReceipt(ResidentTenant RT, String bookingId) {
        ResidentTenantFacilityBookingReceipt page = new ResidentTenantFacilityBookingReceipt(RT, bookingId);
        page.setVisible(true);
    }
    
    public void toBookedFacility(ResidentTenant RT) {
        ResidentTenantBookedFacility page = new ResidentTenantBookedFacility(RT);
        page.setVisible(true);
    }
    
    public void toManageBookedFacility(ResidentTenant RT, Facility fb, String bookingID, String date) {
        ResidentTenantManageBookedFacility page = new ResidentTenantManageBookedFacility(RT, fb, bookingID, date);
        page.setVisible(true);
    }
    
    public void toFacilityBookingManagement(ResidentTenant RT) {
        ResidentTenantFacilityBooking page = new ResidentTenantFacilityBooking(RT);
        page.setVisible(true);
    }
    
    public void toFacilityPreview(ResidentTenant RT, String facilityID) {
        ResidentTenantFacilityPreview page = new ResidentTenantFacilityPreview(RT, facilityID);
        page.setVisible(true);
    }
    
    public void toBookFacility(ResidentTenant RT, Facility fb) {
        ResidentTenantBookFacility page = new ResidentTenantBookFacility(RT, fb);
        page.setVisible(true);
    }
    
    public void toFacilityPaymentGateway(ResidentTenant RT, List<String> bookingList, Facility fb) {
        ResidentTenantFacilityPaymentGateway page = new ResidentTenantFacilityPaymentGateway(RT, bookingList, fb);
        page.setVisible(true);
    }
    
    public void toViewProfile(ResidentTenant RT) {
        ResidentTenantProfile page = new ResidentTenantProfile(RT);
        page.setVisible(true);
    }
    
    public void toVisitorPass(ResidentTenant RT) {
        ResidentTenantVisitorPass page = new ResidentTenantVisitorPass(RT);
        page.setVisible(true);
    }
    
    public void toComplaints(ResidentTenant RT) {
        ResidentTenantComplaints page = new ResidentTenantComplaints(RT);
        page.setVisible(true);
    }
    
    public void toInvoiceReceipt(ResidentTenant RT, String invoiceNo) {
        ResidentTenantInvoiceReceipt page = new ResidentTenantInvoiceReceipt(RT, invoiceNo);
        page.setVisible(true);
    }
    
    public void toViewPaidInvoice(ResidentTenant RT, String invoiceNo, ArrayList<Payment> paymentList) {
        ResidentTenantViewPaidInvoice page = new ResidentTenantViewPaidInvoice(invoiceNo, RT, paymentList);
        page.setVisible(true);
    }
    
    public void toStatementReport(ResidentTenant RT, String monthNyear) {
        ResidentTenantStatementReport page = new ResidentTenantStatementReport(RT, monthNyear);
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

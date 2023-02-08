/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package residentANDtenant;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import pms_parkhill_residence.Dashboard;
import pms_parkhill_residence.Users;

/**
 *
 * @author wongj
 */
public class ResidentTenantMainPage extends javax.swing.JFrame {
    private final ResidentTenant RT;
    Dashboard DB = new Dashboard();
    
    /**
     * Creates new form homePage
     * @param RT
     */
    public ResidentTenantMainPage(ResidentTenant RT) {
        this.RT = RT;
        
        initComponents();
        runDefaultSetUp();
    }
    
    public void runDefaultSetUp() {
        setCurrentUserProfile();
        setWindowIcon();
        setData();
        yearCBsetUp();
    }
    
    private void setCurrentUserProfile() {
        userNameLabel.setText(RT.getFirstName() + " " + RT.getLastName());
    }
    
    private void setData() {
        // Set Complaints
        ArrayList<ArrayList> complaints = RT.CP.getComplaints(RT.getUserID());
        ArrayList<String> pendingComplaints = complaints.get(0);
        ArrayList<String> completedComplaints = complaints.get(1);
        
        pendingComplaints.addAll(completedComplaints);
        int compNo = pendingComplaints.size();
        complaintsLabel.setText(String.valueOf(compNo));
        
        // Set today coming visitor number
        List<String> visitor = RT.fh.fileRead(RT.TF.visitorPass);
        int countVisitor = 0;
        for (String eachPass : visitor) {
            String[] passDet = eachPass.split(RT.TF.sp);
            String rsdId = passDet[10];
            
            if (rsdId.equals(RT.getUserID())) {
                LocalDate passDate = RT.DTF.formatDate(passDet[5]);
                if (passDate.isEqual(LocalDate.now())) {
                    countVisitor++;
                }
            }
        }
        visitorLabel.setText(String.valueOf(countVisitor));
        
        ArrayList<ArrayList> pendingFee = RT.getCurrentUnitInvoice(RT.getUserID());
        ArrayList<String> unpaidInv = pendingFee.get(0);
        double totalAmount = 0;
        for (String eachFee : unpaidInv) {
            double feeTotal = Double.parseDouble(eachFee.split(RT.TF.sp)[7]);
            totalAmount += feeTotal;
        }
        pendingFeeLabel.setText(String.format("%.02f", totalAmount));
        
        ArrayList<String> facilities = RT.getCurrentUnitBookedFacility(RT.getUserID());
        int bookedFacility = facilities.size();
        facilityLabel.setText(String.valueOf(bookedFacility));
    }
    
    private void yearCBsetUp(){
        ArrayList<String> invYear = new ArrayList<>();
        
        List<String> invoices = RT.fh.fileRead(RT.TF.invoiceFile);
        boolean firstLine = true;
        for (String eachInv : invoices) {
            if (!firstLine) {
                int year = Integer.valueOf(eachInv.split(RT.TF.sp)[8].split("/")[1]);

                if (!invYear.contains(String.valueOf(year))) {
                    invYear.add(String.valueOf(year));
                }
            }
            
            firstLine = false;
        }
        
        for (int count1 = 0; count1 < invYear.size()-1; count1++) {
            for (int count2 = count1+1; count2 < invYear.size(); count2++) {
                int year1 = Integer.valueOf(invYear.get(count1));
                int year2 = Integer.valueOf(invYear.get(count2));
                
                if (year2 > year1) {
                    invYear.set(count1, String.valueOf(year2));
                    invYear.set(count2, String.valueOf(year1));
                }
            }
        }
        
        yearCB.removeAllItems();
        for (String eachYear : invYear) {
            yearCB.addItem(eachYear);
        }
    }
    
    private void setChart() {
        String selYear = (yearCB.getSelectedItem() != null) ? yearCB.getSelectedItem().toString() : null;
        
        List<String> monthValue = new ArrayList<>();
        List<String> monthData = new ArrayList<>();
        ArrayList<String> selYearInv = new ArrayList<>();
        
        if (selYear != null) {
            List<String> invoices = RT.fh.fileRead(RT.TF.invoiceFile);
            
            boolean firstLine = true;
            for (String eachInv : invoices) {
                if (!firstLine) {
                    String[] invDet = eachInv.split(RT.TF.sp);
                    String unitNo = invDet[1];
                    
                    if (unitNo.equals(RT.getUnitNo())) {
                        String invYear = invDet[8].split("/")[1];
                        
                        if (invYear.equals(selYear)) {
                            selYearInv.add(eachInv);

                            String invMonth = String.valueOf(Month.of(Integer.valueOf(invDet[8].split("/")[0])));

                            if (!monthValue.contains(invMonth)) {
                                monthValue.add(invMonth);
                            }
                        }
                    }
                }
                
                firstLine = false;
            }
            
            for (String eachMonth : monthValue) {
                double totalFees = 0;
                
                for (String eachInv : selYearInv) {
                    String[] invDet = eachInv.split(RT.TF.sp);
                    String invMonth = String.valueOf(Month.of(Integer.valueOf(invDet[8].split("/")[0])));

                    if (invMonth.equals(eachMonth)) {
                        double amount = Double.parseDouble(invDet[7]);
                        totalFees += amount;
                    }
                }
                
                monthData.add(String.format("%.02f", totalFees));
            }
            
            JFreeChart chart = DB.createBarChart(monthValue, monthData, "Consumption Fees for Year " + selYear, "Month", "Consumption Amount (RM)");
            
            ChartPanel barChartPanel = new ChartPanel(chart);
            chartPanel.removeAll();
            chartPanel.add(barChartPanel, BorderLayout.CENTER);
            chartPanel.validate();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        userNameLabel = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        complaintsLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        visitorLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        pendingFeeLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        facilityLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel24 = new javax.swing.JLabel();
        yearCB = new javax.swing.JComboBox<>();
        chartPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        dashboardOuterTab = new javax.swing.JPanel();
        dashBoardInnerTab = new javax.swing.JLabel();
        paymentManagementOuterTab = new javax.swing.JPanel();
        paymentManagementInnerTab = new javax.swing.JLabel();
        facilityBookingOuterTab = new javax.swing.JPanel();
        facilityBookingInnerTab = new javax.swing.JLabel();
        complaintsOuterTab = new javax.swing.JPanel();
        complaintsInnerTab = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        visitorPassOuterTab = new javax.swing.JPanel();
        visitorPassInnerTab = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PARKHILL RESIDENCE");
        setBackground(new java.awt.Color(13, 24, 42));
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setResizable(false);
        setSize(new java.awt.Dimension(1280, 720));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(153, 153, 153), null, null));

        jLabel2.setFont(new java.awt.Font("Britannic Bold", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(13, 24, 42));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("PARKHILL RESIDENCE RESIDENT & TENANT");

        userNameLabel.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        userNameLabel.setForeground(new java.awt.Color(102, 102, 102));
        userNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        userNameLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/profileIcon.jpg"))); // NOI18N
        userNameLabel.setText("USERNAME");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                .addGap(19, 19, 19))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userNameLabel))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(226, 226, 226));

        jLabel14.setFont(new java.awt.Font("Myanmar Text", 1, 36)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("DASHBOARD");
        jLabel14.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jPanel4.setBackground(new java.awt.Color(13, 50, 79));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(13, 24, 42));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("TOTAL COMPLAINTS");

        complaintsLabel.setFont(new java.awt.Font("MS PGothic", 1, 36)); // NOI18N
        complaintsLabel.setForeground(new java.awt.Color(13, 50, 79));
        complaintsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        complaintsLabel.setText("50");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(complaintsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(complaintsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("COMPLAINTS");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(13, 50, 79));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(13, 24, 42));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("COMING TODAY");

        visitorLabel.setFont(new java.awt.Font("MS PGothic", 1, 36)); // NOI18N
        visitorLabel.setForeground(new java.awt.Color(13, 50, 79));
        visitorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        visitorLabel.setText("50");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(visitorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(visitorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel6.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("VISITOR");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel11.setBackground(new java.awt.Color(13, 50, 79));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(13, 24, 42));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("TOTAL AMOUNT");

        pendingFeeLabel.setFont(new java.awt.Font("MS PGothic", 1, 36)); // NOI18N
        pendingFeeLabel.setForeground(new java.awt.Color(13, 50, 79));
        pendingFeeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pendingFeeLabel.setText("50");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pendingFeeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pendingFeeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("PENDING FEE");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel16.setBackground(new java.awt.Color(13, 50, 79));

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(13, 24, 42));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("TODAY BOOKED");

        facilityLabel.setFont(new java.awt.Font("MS PGothic", 1, 36)); // NOI18N
        facilityLabel.setForeground(new java.awt.Color(13, 50, 79));
        facilityLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        facilityLabel.setText("50");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(facilityLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(facilityLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("FACILITY");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jSeparator1.setBackground(new java.awt.Color(13, 50, 79));
        jSeparator1.setForeground(new java.awt.Color(13, 50, 79));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator2.setBackground(new java.awt.Color(13, 50, 79));
        jSeparator2.setForeground(new java.awt.Color(13, 50, 79));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator3.setBackground(new java.awt.Color(13, 50, 79));
        jSeparator3.setForeground(new java.awt.Color(13, 50, 79));
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel24.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 51, 51));
        jLabel24.setText("Year:");

        yearCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        yearCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearCBActionPerformed(evt);
            }
        });

        chartPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(79, 79, 79)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(yearCB, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(chartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE)
                                    .addComponent(jSeparator4))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(yearCB)
                    .addComponent(jLabel24))
                .addGap(8, 8, 8)
                .addComponent(chartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        jPanel2.setBackground(new java.awt.Color(13, 24, 42));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/parkhillLogo.png"))); // NOI18N

        dashboardOuterTab.setBackground(new java.awt.Color(13, 50, 79));
        dashboardOuterTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashboardOuterTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dashboardOuterTabMouseEntered(evt);
            }
        });

        dashBoardInnerTab.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        dashBoardInnerTab.setForeground(new java.awt.Color(255, 255, 255));
        dashBoardInnerTab.setText("Dashboard");
        dashBoardInnerTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashBoardInnerTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dashBoardInnerTabMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout dashboardOuterTabLayout = new javax.swing.GroupLayout(dashboardOuterTab);
        dashboardOuterTab.setLayout(dashboardOuterTabLayout);
        dashboardOuterTabLayout.setHorizontalGroup(
            dashboardOuterTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dashboardOuterTabLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(dashBoardInnerTab, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122))
        );
        dashboardOuterTabLayout.setVerticalGroup(
            dashboardOuterTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardOuterTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dashBoardInnerTab)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paymentManagementOuterTab.setBackground(new java.awt.Color(13, 24, 42));
        paymentManagementOuterTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paymentManagementOuterTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                paymentManagementOuterTabMouseEntered(evt);
            }
        });

        paymentManagementInnerTab.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        paymentManagementInnerTab.setForeground(new java.awt.Color(255, 255, 255));
        paymentManagementInnerTab.setText("Payment Management");
        paymentManagementInnerTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paymentManagementInnerTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                paymentManagementInnerTabMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout paymentManagementOuterTabLayout = new javax.swing.GroupLayout(paymentManagementOuterTab);
        paymentManagementOuterTab.setLayout(paymentManagementOuterTabLayout);
        paymentManagementOuterTabLayout.setHorizontalGroup(
            paymentManagementOuterTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paymentManagementOuterTabLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(paymentManagementInnerTab)
                .addGap(58, 58, 58))
        );
        paymentManagementOuterTabLayout.setVerticalGroup(
            paymentManagementOuterTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentManagementOuterTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(paymentManagementInnerTab)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        facilityBookingOuterTab.setBackground(new java.awt.Color(13, 24, 42));
        facilityBookingOuterTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                facilityBookingOuterTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                facilityBookingOuterTabMouseEntered(evt);
            }
        });

        facilityBookingInnerTab.setBackground(new java.awt.Color(13, 24, 42));
        facilityBookingInnerTab.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        facilityBookingInnerTab.setForeground(new java.awt.Color(255, 255, 255));
        facilityBookingInnerTab.setText("Facility Booking");
        facilityBookingInnerTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                facilityBookingInnerTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                facilityBookingInnerTabMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout facilityBookingOuterTabLayout = new javax.swing.GroupLayout(facilityBookingOuterTab);
        facilityBookingOuterTab.setLayout(facilityBookingOuterTabLayout);
        facilityBookingOuterTabLayout.setHorizontalGroup(
            facilityBookingOuterTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, facilityBookingOuterTabLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(facilityBookingInnerTab, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        facilityBookingOuterTabLayout.setVerticalGroup(
            facilityBookingOuterTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(facilityBookingOuterTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(facilityBookingInnerTab)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        complaintsOuterTab.setBackground(new java.awt.Color(13, 24, 42));
        complaintsOuterTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                complaintsOuterTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                complaintsOuterTabMouseEntered(evt);
            }
        });

        complaintsInnerTab.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        complaintsInnerTab.setForeground(new java.awt.Color(255, 255, 255));
        complaintsInnerTab.setText("Complaints");
        complaintsInnerTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                complaintsInnerTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                complaintsInnerTabMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout complaintsOuterTabLayout = new javax.swing.GroupLayout(complaintsOuterTab);
        complaintsOuterTab.setLayout(complaintsOuterTabLayout);
        complaintsOuterTabLayout.setHorizontalGroup(
            complaintsOuterTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, complaintsOuterTabLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(complaintsInnerTab, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        complaintsOuterTabLayout.setVerticalGroup(
            complaintsOuterTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(complaintsOuterTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(complaintsInnerTab)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(13, 24, 42));

        jLabel10.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoutIcon.png"))); // NOI18N
        jLabel10.setText("LOGOUT");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(13, 24, 42));
        jPanel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel13MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel13MouseEntered(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/viewProfileIcon.png"))); // NOI18N
        jLabel12.setText("VIEW PROFILE");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel12MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.setBackground(new java.awt.Color(13, 24, 42));

        jLabel15.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoutIcon.png"))); // NOI18N
        jLabel15.setText("LOGOUT");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        visitorPassOuterTab.setBackground(new java.awt.Color(13, 24, 42));
        visitorPassOuterTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                visitorPassOuterTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                visitorPassOuterTabMouseEntered(evt);
            }
        });

        visitorPassInnerTab.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        visitorPassInnerTab.setForeground(new java.awt.Color(255, 255, 255));
        visitorPassInnerTab.setText("Visitor Pass");
        visitorPassInnerTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                visitorPassInnerTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                visitorPassInnerTabMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout visitorPassOuterTabLayout = new javax.swing.GroupLayout(visitorPassOuterTab);
        visitorPassOuterTab.setLayout(visitorPassOuterTabLayout);
        visitorPassOuterTabLayout.setHorizontalGroup(
            visitorPassOuterTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(visitorPassOuterTabLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(visitorPassInnerTab)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        visitorPassOuterTabLayout.setVerticalGroup(
            visitorPassOuterTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(visitorPassOuterTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(visitorPassInnerTab)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(paymentManagementOuterTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(facilityBookingOuterTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(complaintsOuterTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dashboardOuterTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(visitorPassOuterTab, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(dashboardOuterTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymentManagementOuterTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(facilityBookingOuterTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(visitorPassOuterTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(complaintsOuterTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(681, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(11, 11, 11)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void dashBoardInnerTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashBoardInnerTabMouseClicked
        // TODO add your handling code here:
        RT.toResidentTenantDashboard(RT);
        this.dispose();
    }//GEN-LAST:event_dashBoardInnerTabMouseClicked

    private void dashBoardInnerTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashBoardInnerTabMouseEntered
        // TODO add your handling code here:
        dashBoardInnerTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_dashBoardInnerTabMouseEntered

    private void dashboardOuterTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardOuterTabMouseClicked
        // TODO add your handling code here:
        RT.toResidentTenantDashboard(RT);
        this.dispose();
    }//GEN-LAST:event_dashboardOuterTabMouseClicked

    private void dashboardOuterTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardOuterTabMouseEntered
        // TODO add your handling code here:
        dashboardOuterTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_dashboardOuterTabMouseEntered

    private void paymentManagementInnerTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentManagementInnerTabMouseClicked
        // TODO add your handling code here:
        RT.toPaymentManagement(RT);
        this.dispose();
    }//GEN-LAST:event_paymentManagementInnerTabMouseClicked

    private void paymentManagementInnerTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentManagementInnerTabMouseEntered
        // TODO add your handling code here:
        paymentManagementInnerTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_paymentManagementInnerTabMouseEntered

    private void paymentManagementOuterTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentManagementOuterTabMouseClicked
        // TODO add your handling code here:
        RT.toPaymentManagement(RT);
        this.dispose();
    }//GEN-LAST:event_paymentManagementOuterTabMouseClicked

    private void paymentManagementOuterTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentManagementOuterTabMouseEntered
        // TODO add your handling code here:
        paymentManagementOuterTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_paymentManagementOuterTabMouseEntered

    private void facilityBookingInnerTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_facilityBookingInnerTabMouseClicked
        // TODO add your handling code here:
        RT.toBookedFacility(RT);
        this.dispose();
    }//GEN-LAST:event_facilityBookingInnerTabMouseClicked

    private void facilityBookingInnerTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_facilityBookingInnerTabMouseEntered
        // TODO add your handling code here:
        facilityBookingInnerTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_facilityBookingInnerTabMouseEntered

    private void facilityBookingOuterTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_facilityBookingOuterTabMouseClicked
        // TODO add your handling code here:
        RT.toBookedFacility(RT);
        this.dispose();
    }//GEN-LAST:event_facilityBookingOuterTabMouseClicked

    private void facilityBookingOuterTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_facilityBookingOuterTabMouseEntered
        // TODO add your handling code here:
        facilityBookingOuterTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_facilityBookingOuterTabMouseEntered

    private void complaintsInnerTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_complaintsInnerTabMouseClicked
        // TODO add your handling code here:
        RT.toComplaints(RT);
        this.dispose();
    }//GEN-LAST:event_complaintsInnerTabMouseClicked

    private void complaintsInnerTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_complaintsInnerTabMouseEntered
        // TODO add your handling code here:
        complaintsInnerTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_complaintsInnerTabMouseEntered

    private void complaintsOuterTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_complaintsOuterTabMouseClicked
        // TODO add your handling code here:
        RT.toComplaints(RT);
        this.dispose();
    }//GEN-LAST:event_complaintsOuterTabMouseClicked

    private void complaintsOuterTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_complaintsOuterTabMouseEntered
        // TODO add your handling code here:
        complaintsOuterTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_complaintsOuterTabMouseEntered

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        // TODO add your handling code here:
        RT.toViewProfile(RT);
        this.dispose();
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jLabel12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseEntered
        // TODO add your handling code here:
        jLabel12.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jLabel12MouseEntered

    private void jPanel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseClicked
        // TODO add your handling code here:
        RT.toViewProfile(RT);
        this.dispose();
    }//GEN-LAST:event_jPanel13MouseClicked

    private void jPanel13MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseEntered
        // TODO add your handling code here:
        jPanel13.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jPanel13MouseEntered

    private void visitorPassInnerTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visitorPassInnerTabMouseClicked
        // TODO add your handling code here:
        RT.toVisitorPass(RT);
        this.dispose();
    }//GEN-LAST:event_visitorPassInnerTabMouseClicked

    private void visitorPassInnerTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visitorPassInnerTabMouseEntered
        // TODO add your handling code here:
        visitorPassInnerTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_visitorPassInnerTabMouseEntered

    private void visitorPassOuterTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visitorPassOuterTabMouseClicked
        // TODO add your handling code here:
        RT.toVisitorPass(RT);
        this.dispose();
    }//GEN-LAST:event_visitorPassOuterTabMouseClicked

    private void visitorPassOuterTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visitorPassOuterTabMouseEntered
        // TODO add your handling code here:
        visitorPassOuterTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_visitorPassOuterTabMouseEntered

    private void yearCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearCBActionPerformed
        // TODO add your handling code here:
        setChart();
    }//GEN-LAST:event_yearCBActionPerformed

    private void setWindowIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/windowIcon.png")));
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ResidentTenantMainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ResidentTenantMainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ResidentTenantMainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ResidentTenantMainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ResidentTenantMainPage(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel chartPanel;
    private javax.swing.JLabel complaintsInnerTab;
    private javax.swing.JLabel complaintsLabel;
    private javax.swing.JPanel complaintsOuterTab;
    private javax.swing.JLabel dashBoardInnerTab;
    private javax.swing.JPanel dashboardOuterTab;
    private javax.swing.JLabel facilityBookingInnerTab;
    private javax.swing.JPanel facilityBookingOuterTab;
    private javax.swing.JLabel facilityLabel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel paymentManagementInnerTab;
    private javax.swing.JPanel paymentManagementOuterTab;
    private javax.swing.JLabel pendingFeeLabel;
    private javax.swing.JLabel totalUpcomingJob1;
    private javax.swing.JLabel userNameLabel;
    private javax.swing.JLabel visitorLabel;
    private javax.swing.JLabel visitorPassInnerTab;
    private javax.swing.JPanel visitorPassOuterTab;
    private javax.swing.JComboBox<String> yearCB;
    // End of variables declaration//GEN-END:variables

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vendor;

import residentANDtenant.*;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import pms_parkhill_residence.PMS_DateTimeFormatter;
import pms_parkhill_residence.Users;

/**
 *
 * @author wongj
 */
public class VendorPaymentHistory extends javax.swing.JFrame {
    private Users user;
    Vendor VD = new Vendor();
    PMS_DateTimeFormatter DTF = new PMS_DateTimeFormatter();
    
    DefaultTableModel payHisTab;
    
    
    /**
     * Creates new form homePage
     * @param user
     */
    public VendorPaymentHistory(Users user) {
        initComponents();
        runDefaultSetUp(user);
    }
    
    private void runDefaultSetUp(Users user) {
        payHisTab = (DefaultTableModel) paymentHistoryTable.getModel();
        this.user = user;
        setWindowIcon();
        try {
            paymentHistoryTableSetUp();
        } catch (ParseException ex) {
            Logger.getLogger(VendorPaymentHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void paymentHistoryTableSetUp() throws ParseException {
        ArrayList<String> receiptList = VD.getCurrentUnitIssuedReceipt(this.user.getUnitNo());
        ArrayList<String> sortedList;
        
        ArrayList<String> arrangedList = new ArrayList<>();
        ArrayList<String> payment = new ArrayList<>();
        
        ArrayList<String> existedIdList = new ArrayList<>();
                
        for (String eachHist : receiptList) {
            String[] histData = eachHist.split(VD.TF.sp);
            String invoiceNo = histData[0];
            
            LocalDate latestDate = null;
            String feeTypes = "";
            if (!existedIdList.contains(invoiceNo)) {
                for (String hist : receiptList) {
                    String[] eachHistData = hist.split(VD.TF.sp);
                    String histInv = eachHistData[0];
                    if (histInv.equals(invoiceNo)) {
                        String type = eachHistData[2];
                        feeTypes = feeTypes + type + ",";
                        
                        LocalDate paymentDate = VD.DTF.formatDate2(eachHistData[10]);
                        if (latestDate != null) {
                            if (paymentDate.isAfter(latestDate)) {
                                latestDate = paymentDate;
                            }
                        }
                        else {
                            latestDate = paymentDate;
                        }
                    }
                }
                String toAdd = "";
            
                String feeType = "Invoice - " + feeTypes.substring(0, feeTypes.length()-1);
                double totalPrice = VD.getTotalPricePerInvoice(invoiceNo, receiptList);
                String paidDate = String.valueOf(latestDate);
                String[] data = {invoiceNo.toUpperCase(), feeType, String.format("%.02f", totalPrice), paidDate, "RECEIPT"};

                for (String eachData : data) {
                    toAdd = toAdd + eachData + VD.TF.sp;
                }
                payment.add(toAdd);
                existedIdList.add(invoiceNo);
            }
        }
        
        String[] payHist = payment.toArray(String[]::new);
        
        for (int compFirst = 0; compFirst < payHist.length - 1; compFirst++) {
            for (int compSec = compFirst+1; compSec < payHist.length; compSec++) {
                String itemFirst = payHist[compFirst];
                String itemSec = payHist[compSec];
                LocalDate firstDate = DTF.formatDate(itemFirst.split(VD.TF.sp)[3]);
                LocalDate secDate = DTF.formatDate(itemSec.split(VD.TF.sp)[3]);
                
                if (secDate.isAfter(firstDate)) {
                    String tempLine = itemFirst;
                    payHist[compFirst] = itemSec;
                    payHist[compSec] = tempLine;
                }
            }
        }
        
        sortedList = new ArrayList<>(Arrays.asList(payHist));
        
        int itemNo = 1;
        for (String eachItem : sortedList) {
            arrangedList.add(itemNo + VD.TF.sp + eachItem);
            itemNo++;
        }
        
        VD.setTableRow(payHisTab, arrangedList);
    }
    
    private void setCurrentUserProfile() {
        userNameLabel.setText(user.getFirstName() + " " + user.getLastName());
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
        jSeparator1 = new javax.swing.JSeparator();
        pendingFeeLabel = new javax.swing.JLabel();
        statementLabel = new javax.swing.JLabel();
        invoiceLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        paymentHistoryTable = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        paymentHistLabel = new javax.swing.JLabel();
        paymentHistLine = new javax.swing.JTextField();
        viewStatementBTN = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        dashboardOuterTab2 = new javax.swing.JPanel();
        dashBoardInnerTab2 = new javax.swing.JLabel();
        paymentManagementOuterTab = new javax.swing.JPanel();
        paymentManagementInnerTab = new javax.swing.JLabel();
        complaintsOuterTab = new javax.swing.JPanel();
        complaintsInnerTab = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();

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
                .addComponent(userNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
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

        pendingFeeLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        pendingFeeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pendingFeeLabel.setText("Pending Fee");
        pendingFeeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pendingFeeLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pendingFeeLabelMouseEntered(evt);
            }
        });

        statementLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        statementLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statementLabel.setText("Statement");
        statementLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                statementLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                statementLabelMouseEntered(evt);
            }
        });

        invoiceLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        invoiceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        invoiceLabel.setText("Invoice");
        invoiceLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                invoiceLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                invoiceLabelMouseEntered(evt);
            }
        });

        paymentHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "NO.", "ITEM ID", "ITEM DESCRIPTION", "AMOUNT PAID", "PAID DATE", "RECEIPT"
            }
        ));
        paymentHistoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paymentHistoryTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(paymentHistoryTable);

        jLabel23.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 51, 51));
        jLabel23.setText("Payment History:");

        paymentHistLabel.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        paymentHistLabel.setForeground(new java.awt.Color(13, 24, 42));
        paymentHistLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        paymentHistLabel.setText("Payment History");
        paymentHistLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paymentHistLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                paymentHistLabelMouseEntered(evt);
            }
        });

        paymentHistLine.setBackground(new java.awt.Color(13, 24, 42));
        paymentHistLine.setForeground(new java.awt.Color(13, 24, 42));
        paymentHistLine.setText("jTextField1");

        viewStatementBTN.setText("View Statement");
        viewStatementBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewStatementBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(pendingFeeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(paymentHistLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(paymentHistLine))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(invoiceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(statementLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 977, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(448, 448, 448)
                        .addComponent(viewStatementBTN)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(paymentHistLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(paymentHistLine, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pendingFeeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(statementLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(invoiceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addGap(4, 4, 4)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(viewStatementBTN, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        jPanel7.setBackground(new java.awt.Color(13, 24, 42));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/parkhillLogo.png"))); // NOI18N

        dashboardOuterTab2.setBackground(new java.awt.Color(13, 24, 42));
        dashboardOuterTab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashboardOuterTab2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dashboardOuterTab2MouseEntered(evt);
            }
        });

        dashBoardInnerTab2.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        dashBoardInnerTab2.setForeground(new java.awt.Color(255, 255, 255));
        dashBoardInnerTab2.setText("Dashboard");
        dashBoardInnerTab2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashBoardInnerTab2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dashBoardInnerTab2MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout dashboardOuterTab2Layout = new javax.swing.GroupLayout(dashboardOuterTab2);
        dashboardOuterTab2.setLayout(dashboardOuterTab2Layout);
        dashboardOuterTab2Layout.setHorizontalGroup(
            dashboardOuterTab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dashboardOuterTab2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(dashBoardInnerTab2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122))
        );
        dashboardOuterTab2Layout.setVerticalGroup(
            dashboardOuterTab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardOuterTab2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dashBoardInnerTab2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paymentManagementOuterTab.setBackground(new java.awt.Color(13, 50, 79));
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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(paymentManagementOuterTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(complaintsOuterTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(dashboardOuterTab2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(dashboardOuterTab2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymentManagementOuterTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(complaintsOuterTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void invoiceLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invoiceLabelMouseClicked
        // TODO add your handling code here:
        VD.toInvoice(user);
        this.dispose();
    }//GEN-LAST:event_invoiceLabelMouseClicked

    private void viewStatementBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewStatementBTNActionPerformed
        // TODO add your handling code here:
        VD.toStatement(user);
        this.dispose();
    }//GEN-LAST:event_viewStatementBTNActionPerformed

    private void paymentHistoryTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentHistoryTableMouseClicked
        // TODO add your handling code here:
        int selectedCol = paymentHistoryTable.getSelectedColumn();
        int selectedRow = paymentHistoryTable.getSelectedRow();
        
        String itemID = VD.validateTableSelectionAndGetValue(payHisTab, selectedCol, selectedRow, 5, 1);
        
        List<String> receiptFile = VD.fh.fileRead(VD.TF.receiptFile);
        
        if (itemID != null) {
            boolean notFound = true;
            for (String eachReceipt : receiptFile) {
                String[] recDet = eachReceipt.split(VD.TF.sp);
                String recInvNo = recDet[0];

                if (recInvNo.equals(itemID)) {
                    notFound = false;

                    VD.toInvoiceReceipt(user, recInvNo);
                    break;
                }
            }
        }
    }//GEN-LAST:event_paymentHistoryTableMouseClicked

    private void pendingFeeLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pendingFeeLabelMouseEntered
        // TODO add your handling code here:
        pendingFeeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_pendingFeeLabelMouseEntered

    private void pendingFeeLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pendingFeeLabelMouseClicked
        // TODO add your handling code here:
        VD.toPaymentManagement(user);
        this.dispose();
    }//GEN-LAST:event_pendingFeeLabelMouseClicked

    private void invoiceLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invoiceLabelMouseEntered
        // TODO add your handling code here:
        invoiceLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_invoiceLabelMouseEntered

    private void statementLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statementLabelMouseClicked
        // TODO add your handling code here:
        VD.toStatement(user);
        this.dispose();
    }//GEN-LAST:event_statementLabelMouseClicked

    private void statementLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statementLabelMouseEntered
        // TODO add your handling code here:
        statementLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_statementLabelMouseEntered

    private void paymentHistLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentHistLabelMouseClicked
        // TODO add your handling code here:
        VD.toPaymentHistory(user);
        this.dispose();
    }//GEN-LAST:event_paymentHistLabelMouseClicked

    private void paymentHistLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentHistLabelMouseEntered
        // TODO add your handling code here:
        pendingFeeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_paymentHistLabelMouseEntered

    private void dashBoardInnerTab2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashBoardInnerTab2MouseClicked
        // TODO add your handling code here:
        VD.toResidentTenantDashboard(user);
        this.dispose();
    }//GEN-LAST:event_dashBoardInnerTab2MouseClicked

    private void dashBoardInnerTab2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashBoardInnerTab2MouseEntered
        // TODO add your handling code here:
        dashBoardInnerTab2.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_dashBoardInnerTab2MouseEntered

    private void dashboardOuterTab2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardOuterTab2MouseClicked
        // TODO add your handling code here:
        VD.toResidentTenantDashboard(user);
        this.dispose();
    }//GEN-LAST:event_dashboardOuterTab2MouseClicked

    private void dashboardOuterTab2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardOuterTab2MouseEntered
        // TODO add your handling code here:
        dashboardOuterTab2.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_dashboardOuterTab2MouseEntered

    private void paymentManagementInnerTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentManagementInnerTabMouseClicked
        // TODO add your handling code here:
        VD.toPaymentManagement(user);
        this.dispose();
    }//GEN-LAST:event_paymentManagementInnerTabMouseClicked

    private void paymentManagementInnerTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentManagementInnerTabMouseEntered
        // TODO add your handling code here:
        paymentManagementInnerTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_paymentManagementInnerTabMouseEntered

    private void paymentManagementOuterTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentManagementOuterTabMouseClicked
        // TODO add your handling code here:
        VD.toPaymentManagement(user);
        this.dispose();
    }//GEN-LAST:event_paymentManagementOuterTabMouseClicked

    private void paymentManagementOuterTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentManagementOuterTabMouseEntered
        // TODO add your handling code here:
        paymentManagementOuterTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_paymentManagementOuterTabMouseEntered

    private void complaintsInnerTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_complaintsInnerTabMouseClicked
        // TODO add your handling code here:
        VD.toComplaints(user);
        this.dispose();
    }//GEN-LAST:event_complaintsInnerTabMouseClicked

    private void complaintsInnerTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_complaintsInnerTabMouseEntered
        // TODO add your handling code here:
        complaintsInnerTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_complaintsInnerTabMouseEntered

    private void complaintsOuterTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_complaintsOuterTabMouseClicked
        // TODO add your handling code here:
        VD.toComplaints(user);
        this.dispose();
    }//GEN-LAST:event_complaintsOuterTabMouseClicked

    private void complaintsOuterTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_complaintsOuterTabMouseEntered
        // TODO add your handling code here:
        complaintsOuterTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_complaintsOuterTabMouseEntered

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        // TODO add your handling code here:
        VD.toViewProfile(user);
        this.dispose();
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jLabel12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseEntered
        // TODO add your handling code here:
        jLabel12.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jLabel12MouseEntered

    private void jPanel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseClicked
        // TODO add your handling code here:
        VD.toViewProfile(user);
        this.dispose();
    }//GEN-LAST:event_jPanel13MouseClicked

    private void jPanel13MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseEntered
        // TODO add your handling code here:
        jPanel13.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jPanel13MouseEntered

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
            java.util.logging.Logger.getLogger(VendorPaymentHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VendorPaymentHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VendorPaymentHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VendorPaymentHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new VendorPaymentHistory(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel complaintsInnerTab;
    private javax.swing.JPanel complaintsOuterTab;
    private javax.swing.JLabel dashBoardInnerTab2;
    private javax.swing.JPanel dashboardOuterTab2;
    private javax.swing.JLabel invoiceLabel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel paymentHistLabel;
    private javax.swing.JTextField paymentHistLine;
    private javax.swing.JTable paymentHistoryTable;
    private javax.swing.JLabel paymentManagementInnerTab;
    private javax.swing.JPanel paymentManagementOuterTab;
    private javax.swing.JLabel pendingFeeLabel;
    private javax.swing.JLabel statementLabel;
    private javax.swing.JLabel userNameLabel;
    private javax.swing.JButton viewStatementBTN;
    // End of variables declaration//GEN-END:variables

}

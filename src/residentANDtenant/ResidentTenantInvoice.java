/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package residentANDtenant;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import classes.Invoice;
import classes.Payment;
import pms_parkhill_residence.HomePage;

/**
 *
 * @author wongj
 */
public class ResidentTenantInvoice extends javax.swing.JFrame {
    private final ResidentTenant RT;
    
    DefaultTableModel invIncompTab;
    DefaultTableModel invCompTab;
    
    
    /**
     * Creates new form homePage
     * @param RT
     */
    public ResidentTenantInvoice(ResidentTenant RT) {
        this.RT = RT;

        initComponents();
        runDefaultSetUp();
    }
    
    private void runDefaultSetUp() {
        invIncompTab = (DefaultTableModel) invoiceIncompleteTable.getModel();
        invCompTab = (DefaultTableModel) invoiceCompleteTable.getModel();
        
        invoiceTableSetUp();
        invoiceComboBoxSetUp();
        
        setWindowIcon();
        
        setCurrentUserProfile();
    }
    
    private void invoiceTableSetUp() {
        ArrayList<String> incompleteInvoice = new ArrayList<>();
        ArrayList<String> completeInvoice = new ArrayList<>();
        
        ArrayList<Invoice> incompList = RT.PYM.getCurrentUnitInvoice(this.RT.getUnitNo());
        ArrayList<Payment> compList = RT.PYM.getCurrentUnitPayment(this.RT.getUnitNo());
        
        ArrayList<String> incompInvCode = new ArrayList<>();
        ArrayList<String> compInvCode = new ArrayList<>();
        
        for (Invoice eachInv : incompList) {
            String invNo = eachInv.getInvoiceNo();
            
            String feeType = "";
            if (!incompInvCode.contains(invNo)) {
                for (Invoice secIncomp : incompList) {
                    String incompInvNo = secIncomp.getInvoiceNo();
                    
                    if (incompInvNo.equals(invNo)) {
                        String type = secIncomp.getFeeType();
                        feeType = feeType + type + ",";
                    }
                }
                
                feeType = feeType.substring(0, feeType.length()-1);
                    
                String incompletedLine = "";
                
                double totalAmount = RT.PYM.getTotalPricePerInvoice(invNo, incompList);

                String[] tableData = {invNo.toUpperCase(), feeType, String.format("%.02f", totalAmount), "PAY"};
                for (String eachData : tableData) {
                    incompletedLine = incompletedLine + eachData + RT.TF.sp;
                }

                incompleteInvoice.add(incompletedLine);
                
                incompInvCode.add(invNo);
            }
        }
        
        for (Payment eachPm : compList) {
            String invNo = eachPm.getInvoiceNo();
            
            String feeType = "";
            if (!compInvCode.contains(invNo)) {
                for (Payment newEachPm : compList) {
                    String compInvNo = newEachPm.getInvoiceNo();
                    if (compInvNo.equals(invNo)) {
                        String type = newEachPm.getFeeType();
                        feeType = feeType + type + ",";
                    }
                }
                
                feeType = feeType.substring(0, feeType.length()-1);
                
                String completedLine = "";
                
                double totalAmount = RT.PYM.getTotalPricePerPayment(invNo, compList);
                
                String[] tableData = {invNo.toUpperCase(), feeType, String.format("%.02f", totalAmount), eachPm.getPaymentBy().toUpperCase(), "VIEW"};
                for (String eachData : tableData) {
                    completedLine = completedLine + eachData + RT.TF.sp;
                }

                completeInvoice.add(completedLine);
                compInvCode.add(invNo);
            }
        }
        
        RT.setTableRow(invIncompTab, incompleteInvoice);
        RT.setTableRow(invCompTab, completeInvoice);
        
        tableDesignSetUp();
    }
    
    private void tableDesignSetUp() {
        int[] columnIgnore = {1};
        int[] columnLength = {245, 366, 183, 183};
        RT.setTableDesign(invoiceIncompleteTable, jLabel2, columnLength, columnIgnore);
        
        int[] columnLength2 = {180, 367, 150, 150, 130};
        RT.setTableDesign(invoiceCompleteTable, jLabel2, columnLength2, columnIgnore);
    }
    
    private void invoiceComboBoxSetUp() {
        ArrayList<String> invoiceCode = RT.PYM.getInvoiceCode(RT.getUnitNo());
        
        invoiceNoCB.removeAllItems();
        invoiceNoCB.addItem("All");
        for (String eachInv : invoiceCode) {
            invoiceNoCB.addItem(eachInv);
        }
    }
    
    private void setCurrentUserProfile() {
        userNameLabel.setText(RT.getFirstName() + " " + RT.getLastName());
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
        invoiceLine = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        invoiceIncompleteTable = new javax.swing.JTable()
        {
            @Override

            public Component prepareRenderer (TableCellRenderer renderer, int rowIndex, int columnIndex){
                Component componenet = super.prepareRenderer(renderer, rowIndex, columnIndex);

                Object value = getModel().getValueAt(rowIndex,columnIndex);

                if(columnIndex == 3){
                    componenet.setBackground(new Color(0,70,126));
                    componenet.setForeground(new Color(255, 255, 255));
                }

                else {
                    if (rowIndex%2 == 0) {
                        componenet.setBackground(new Color(249, 249, 249));
                        componenet.setForeground(new Color (102, 102, 102));
                    } else {
                        componenet.setBackground(new Color(225, 225, 225));
                        componenet.setForeground(new Color (102, 102, 102));
                    }

                }

                return componenet;
            }
        }
        ;
        jLabel23 = new javax.swing.JLabel();
        paymentHistLabel = new javax.swing.JLabel();
        invoiceNoCB = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        invoiceCompleteTable = new javax.swing.JTable()
        {
            @Override

            public Component prepareRenderer (TableCellRenderer renderer, int rowIndex, int columnIndex){
                Component componenet = super.prepareRenderer(renderer, rowIndex, columnIndex);

                Object value = getModel().getValueAt(rowIndex,columnIndex);

                if(columnIndex == 4){
                    componenet.setBackground(new Color(0,70,126));
                    componenet.setForeground(new Color(255, 255, 255));
                }

                else {
                    if (rowIndex%2 == 0) {
                        componenet.setBackground(new Color(249, 249, 249));
                        componenet.setForeground(new Color (102, 102, 102));
                    } else {
                        componenet.setBackground(new Color(225, 225, 225));
                        componenet.setForeground(new Color (102, 102, 102));
                    }

                }

                return componenet;
            }
        }
        ;
        jLabel25 = new javax.swing.JLabel();
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
        jPanel13 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        visitorPassOuterTab = new javax.swing.JPanel();
        visitorPassInnerTab = new javax.swing.JLabel();
        logoutPanel2 = new javax.swing.JPanel();
        logoutLabel2 = new javax.swing.JLabel();

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

        invoiceLabel.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        invoiceLabel.setForeground(new java.awt.Color(13, 24, 42));
        invoiceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        invoiceLabel.setText("Invoice");

        invoiceLine.setBackground(new java.awt.Color(13, 24, 42));
        invoiceLine.setForeground(new java.awt.Color(13, 24, 42));
        invoiceLine.setText("jTextField1");

        invoiceIncompleteTable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        invoiceIncompleteTable.setForeground(new java.awt.Color(51, 51, 51));
        invoiceIncompleteTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "INVOICE NO.", "FEE TYPE", "TOTAL PRICE (RM)", "ACTION"
            }
        ));
        invoiceIncompleteTable.setIntercellSpacing(new java.awt.Dimension(1, 1));
        invoiceIncompleteTable.setRowHeight(25);
        invoiceIncompleteTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                invoiceIncompleteTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(invoiceIncompleteTable);
        if (invoiceIncompleteTable.getColumnModel().getColumnCount() > 0) {
            invoiceIncompleteTable.getColumnModel().getColumn(2).setResizable(false);
        }

        jLabel23.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 51, 51));
        jLabel23.setText("Invoice: ");

        paymentHistLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
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

        invoiceNoCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        invoiceNoCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceNoCBActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 51, 51));
        jLabel24.setText("Incomplete Invoice:  ");

        invoiceCompleteTable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        invoiceCompleteTable.setForeground(new java.awt.Color(51, 51, 51));
        invoiceCompleteTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "INVOICE NO.", "FEE TYPE", "TOTAL PRICE (RM)", "PAID BY", "ACTION"
            }
        ));
        invoiceCompleteTable.setIntercellSpacing(new java.awt.Dimension(1, 1));
        invoiceCompleteTable.setRowHeight(25);
        invoiceCompleteTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                invoiceCompleteTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(invoiceCompleteTable);

        jLabel25.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(51, 51, 51));
        jLabel25.setText("Completed Invoice:  ");

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
                        .addComponent(paymentHistLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(invoiceLine, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(invoiceLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(7, 7, 7)
                        .addComponent(statementLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 977, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 977, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(invoiceNoCB, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel24))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paymentHistLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pendingFeeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(statementLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(invoiceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(invoiceLine, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(invoiceNoCB))
                .addGap(8, 8, 8)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jPanel2.setBackground(new java.awt.Color(13, 24, 42));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/parkhillLogo.png"))); // NOI18N

        dashboardOuterTab.setBackground(new java.awt.Color(13, 24, 42));
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

        logoutPanel2.setBackground(new java.awt.Color(13, 24, 42));
        logoutPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutPanel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutPanel2MouseEntered(evt);
            }
        });

        logoutLabel2.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        logoutLabel2.setForeground(new java.awt.Color(255, 255, 255));
        logoutLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoutIcon.png"))); // NOI18N
        logoutLabel2.setText("LOGOUT");
        logoutLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutLabel2MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout logoutPanel2Layout = new javax.swing.GroupLayout(logoutPanel2);
        logoutPanel2.setLayout(logoutPanel2Layout);
        logoutPanel2Layout.setHorizontalGroup(
            logoutPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, logoutPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logoutLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        logoutPanel2Layout.setVerticalGroup(
            logoutPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoutPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoutLabel2)
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
                            .addComponent(dashboardOuterTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(visitorPassOuterTab, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logoutPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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
                .addComponent(logoutPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
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

    private void invoiceNoCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceNoCBActionPerformed
        // TODO add your handling code here:
        String selectedNo = (invoiceNoCB.getSelectedItem() != null) ? invoiceNoCB.getSelectedItem().toString() : "All";
        if (!selectedNo.equals("All")) {
            updateInvoiceTable(selectedNo);
        }
        else {
            invoiceTableSetUp();
        }
    }//GEN-LAST:event_invoiceNoCBActionPerformed

    private void invoiceIncompleteTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invoiceIncompleteTableMouseClicked
        // TODO add your handling code here:
        int selCol = invoiceIncompleteTable.getSelectedColumn();
        int selRow = invoiceIncompleteTable.getSelectedRow();
        
        String invoiceNo = RT.validateTableSelectionAndGetValue(invIncompTab, selCol, selRow, 3, 0);

        if (invoiceNo != null) {
            ArrayList<Invoice> invoiceList = RT.PYM.getSameUnpaidInvoiceNo(this.RT.getUnitNo(), invoiceNo);
            
            RT.toInvoicePayment(invoiceNo, RT, invoiceList);
            this.dispose();
        }
    }//GEN-LAST:event_invoiceIncompleteTableMouseClicked

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

    private void pendingFeeLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pendingFeeLabelMouseClicked
        // TODO add your handling code here:
        RT.toPaymentManagement(RT);
        this.dispose();
    }//GEN-LAST:event_pendingFeeLabelMouseClicked

    private void pendingFeeLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pendingFeeLabelMouseEntered
        // TODO add your handling code here:
        pendingFeeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_pendingFeeLabelMouseEntered

    private void paymentHistLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentHistLabelMouseClicked
        // TODO add your handling code here:
        RT.toPaymentHistory(RT);
        this.dispose();
    }//GEN-LAST:event_paymentHistLabelMouseClicked

    private void paymentHistLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentHistLabelMouseEntered
        // TODO add your handling code here:
        paymentHistLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_paymentHistLabelMouseEntered

    private void statementLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statementLabelMouseClicked
        // TODO add your handling code here:
        RT.toStatement(RT);
        this.dispose();
    }//GEN-LAST:event_statementLabelMouseClicked

    private void statementLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statementLabelMouseEntered
        // TODO add your handling code here:
        statementLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_statementLabelMouseEntered

    private void invoiceCompleteTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invoiceCompleteTableMouseClicked
        // TODO add your handling code here:
        int selCol = invoiceCompleteTable.getSelectedColumn();
        int selRow = invoiceCompleteTable.getSelectedRow();
        
        String invoiceNo = RT.validateTableSelectionAndGetValue(invCompTab, selCol, selRow, 4, 0);
        
        if (invoiceNo != null) {
            ArrayList<Payment> paymentList = RT.PYM.getSamePaidInvoiceNo(this.RT.getUnitNo(), invoiceNo);
            
            RT.toViewPaidInvoice(RT, invoiceNo, paymentList);
            this.dispose();
        }
    }//GEN-LAST:event_invoiceCompleteTableMouseClicked

    private void logoutLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutLabel2MouseClicked
        // TODO add your handling code here:
        this.dispose();
        new HomePage().setVisible(true);
    }//GEN-LAST:event_logoutLabel2MouseClicked

    private void logoutLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutLabel2MouseEntered
        // TODO add your handling code here:
        logoutLabel2.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_logoutLabel2MouseEntered

    private void logoutPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPanel2MouseClicked
        // TODO add your handling code here:
        this.dispose();
        new HomePage().setVisible(true);
    }//GEN-LAST:event_logoutPanel2MouseClicked

    private void logoutPanel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPanel2MouseEntered
        // TODO add your handling code here:
        logoutPanel2.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_logoutPanel2MouseEntered

    // update resident invoice table
    private void updateInvoiceTable(String invoiceNo){
        invoiceTableSetUp();
        
        // Update data in incomplete invoice table 
        for (int rowCount = 0; rowCount < invoiceIncompleteTable.getRowCount(); rowCount++) {
            String invNo = invoiceIncompleteTable.getValueAt(rowCount, 0).toString();
            if (!invNo.equals(invoiceNo)) {
                invIncompTab.removeRow(rowCount);
            }
        }
        
        // Update data in complete invoice table
        for (int rowCount = 0; rowCount < invoiceCompleteTable.getRowCount(); rowCount++) {
            String invNo = invoiceCompleteTable.getValueAt(rowCount, 0).toString();
            if (!invNo.equals(invoiceNo)) {
                invCompTab.removeRow(rowCount);
            }
        }
    }
    
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
            java.util.logging.Logger.getLogger(ResidentTenantInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ResidentTenantInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ResidentTenantInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ResidentTenantInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ResidentTenantInvoice(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel complaintsInnerTab;
    private javax.swing.JPanel complaintsOuterTab;
    private javax.swing.JLabel dashBoardInnerTab;
    private javax.swing.JPanel dashboardOuterTab;
    private javax.swing.JLabel facilityBookingInnerTab;
    private javax.swing.JPanel facilityBookingOuterTab;
    private javax.swing.JTable invoiceCompleteTable;
    private javax.swing.JTable invoiceIncompleteTable;
    private javax.swing.JLabel invoiceLabel;
    private javax.swing.JTextField invoiceLine;
    private javax.swing.JComboBox<String> invoiceNoCB;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel logoutLabel2;
    private javax.swing.JPanel logoutPanel2;
    private javax.swing.JLabel paymentHistLabel;
    private javax.swing.JLabel paymentManagementInnerTab;
    private javax.swing.JPanel paymentManagementOuterTab;
    private javax.swing.JLabel pendingFeeLabel;
    private javax.swing.JLabel statementLabel;
    private javax.swing.JLabel userNameLabel;
    private javax.swing.JLabel visitorPassInnerTab;
    private javax.swing.JPanel visitorPassOuterTab;
    // End of variables declaration//GEN-END:variables

}

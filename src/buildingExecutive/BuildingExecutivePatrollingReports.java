/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package buildingExecutive;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author wongj
 */
public class BuildingExecutivePatrollingReports extends javax.swing.JFrame {
    private final BuildingExecutive BE;
    
    DefaultTableModel patRepTab;
    String patrollingScheduleFile;
    String fileDefaultFormat = "patrollingScheduleFiles/patrollingFile_";
    /**
     * Creates new form homePage
     * @param BE
     */
    public BuildingExecutivePatrollingReports(BuildingExecutive BE) {
        this.BE = BE;
        initComponents();
        
        runDefaultSetUp();
    }
    
    private void runDefaultSetUp() {
        patRepTab = (DefaultTableModel) patrollingReportTable.getModel();
        comboBoxSetUp();
        
        setWindowIcon();
        
        setPatrollingReportTableDesign();
        
        setUserProfile();
    }
    
    private void tableSetup() {
        ArrayList<String> toTableItems = new ArrayList<>();
        
        String selectedDate = (dateCB.getSelectedItem() != null) ? dateCB.getSelectedItem().toString() : null;
        
        if (selectedDate != null) {
            patrollingScheduleFile = fileDefaultFormat + selectedDate + ".txt";
            List<String> patrollingSched = BE.fh.fileRead(patrollingScheduleFile);
            
            int itemNo = 1;
            boolean firstLine = true;
            for (String eachSched : patrollingSched) {
                if (!firstLine) {
                    String[] schedItem = eachSched.split(BE.TF.sp);
                    String scgId = schedItem[6];
                    if (!scgId.equals(BE.TF.empty)) {
                        String[] items = {String.valueOf(itemNo), schedItem[1], schedItem[2] + " - " + schedItem[3], schedItem[4], schedItem[5], scgId.toUpperCase(), schedItem[7], schedItem[9], schedItem[10]};

                        String addItem = "";
                        for (String eachItem : items) {
                            addItem = addItem + eachItem + BE.TF.sp;
                        }

                        toTableItems.add(addItem);

                        itemNo++;
                    }
                }
                
                firstLine = false;
            }
            
            if (toTableItems.isEmpty()) {
                warningMsg.setText("The selected month has no record found...");
                viewBTN.setEnabled(false);
            }else {
                viewBTN.setEnabled(true);
                warningMsg.setText("");
            }
            
            BE.setTableRow(patRepTab, toTableItems);  
        }
    }
    
    private void comboBoxSetUp() {
        ArrayList<String> availableScheduleRec = new ArrayList<>();
        
        List<String> patScheRecord = BE.fh.fileRead(BE.TF.patScheduleModRec);
        
        boolean firstLine = true;
        for (String eachRec : patScheRecord) {
            if (!firstLine) {
                String dateId = eachRec.split(BE.TF.sp)[0];
                if (!availableScheduleRec.contains(dateId)) {
                    availableScheduleRec.add(dateId);
                }
            }
            
            firstLine = false;
        }
        
        for (int item1 = 0; item1 < availableScheduleRec.size() - 1; item1++) {
            for (int item2 = item1+1; item2 < availableScheduleRec.size(); item2++) {
                LocalDate date1 = BE.DTF.formatDate(availableScheduleRec.get(item1));
                LocalDate date2 = BE.DTF.formatDate(availableScheduleRec.get(item2));
                
                if (date2.isAfter(date1)) {
                    availableScheduleRec.set(item1, date2.toString());
                    availableScheduleRec.set(item2, date1.toString());
                }
            }
        }
        
        dateCB.removeAllItems();
        for (String eachDate : availableScheduleRec) {
            dateCB.addItem(eachDate);
        }
        
        dateCB.setSelectedItem(0);
    }
    
    private void setPatrollingReportTableDesign() {
        int[] columnIgnore = {6};
        int[] columnLength = {60, 110, 131, 100, 115, 100, 177, 120, 90};
        BE.setTableDesign(patrollingReportTable, jLabel2, columnLength, columnIgnore);
    }
    
    private void setUserProfile() {
        // get current BE details
        // Set text field
        if (BE.getUserID() != null) {
            String beName = BE.getFirstName() + " " + BE.getLastName();
            jLabel7.setText(beName);
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
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        patrollingLabel = new javax.swing.JLabel();
        pendingFeeLine = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        complaintsLabel = new javax.swing.JLabel();
        employeeJobLabel = new javax.swing.JLabel();
        dateCB = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        viewBTN = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        patrollingReportTable = new javax.swing.JTable()
        {
            @Override

            public Component prepareRenderer (TableCellRenderer renderer, int rowIndex, int columnIndex){
                Component componenet = super.prepareRenderer(renderer, rowIndex, columnIndex);

                if (rowIndex%2 == 0) {
                    componenet.setBackground(new Color(249, 249, 249));
                    componenet.setForeground(new Color (102, 102, 102));
                } else {
                    componenet.setBackground(new Color(225, 225, 225));
                    componenet.setForeground(new Color (102, 102, 102));
                }

                return componenet;
            }

        }
        ;
        warningMsg = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jobAssignationTab = new javax.swing.JPanel();
        jobAssignationInnerTab = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        BEdashboardOuterPanel = new javax.swing.JPanel();
        BEdashboardInnerPanel = new javax.swing.JLabel();

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
        jLabel2.setText("PARKHILL RESIDENCE BUILDING EXECUTIVE");

        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/profileIcon.jpg"))); // NOI18N
        jLabel7.setText("USERNAME");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addGap(19, 19, 19))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(226, 226, 226));

        patrollingLabel.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        patrollingLabel.setForeground(new java.awt.Color(13, 24, 42));
        patrollingLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        patrollingLabel.setText("Patrolling");
        patrollingLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                patrollingLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                patrollingLabelMouseEntered(evt);
            }
        });

        pendingFeeLine.setBackground(new java.awt.Color(13, 24, 42));
        pendingFeeLine.setForeground(new java.awt.Color(13, 24, 42));
        pendingFeeLine.setText("jTextField1");

        complaintsLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        complaintsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        complaintsLabel.setText("Complaints");
        complaintsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                complaintsLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                complaintsLabelMouseEntered(evt);
            }
        });

        employeeJobLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        employeeJobLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        employeeJobLabel.setText("Employee Job");
        employeeJobLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                employeeJobLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                employeeJobLabelMouseEntered(evt);
            }
        });

        dateCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        dateCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateCBActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 51, 51));
        jLabel23.setText("Patrolling Report");

        jLabel24.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 51, 51));
        jLabel24.setText("Date: ");

        viewBTN.setText("View");
        viewBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewBTNActionPerformed(evt);
            }
        });

        patrollingReportTable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        patrollingReportTable.setForeground(new java.awt.Color(51, 51, 51));
        patrollingReportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NO.", "SLOT", "BLOCK & LEVEL", "CHECKPOINT", "CHECK BEFORE", "SECURITY ID", "SECURITY NAME", "STATUS", "CHECKED AT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        patrollingReportTable.setIntercellSpacing(new java.awt.Dimension(2, 2));
        patrollingReportTable.setRowHeight(30);
        patrollingReportTable.setSelectionForeground(new java.awt.Color(51, 51, 51));
        jScrollPane1.setViewportView(patrollingReportTable);
        if (patrollingReportTable.getColumnModel().getColumnCount() > 0) {
            patrollingReportTable.getColumnModel().getColumn(1).setResizable(false);
            patrollingReportTable.getColumnModel().getColumn(2).setResizable(false);
            patrollingReportTable.getColumnModel().getColumn(3).setResizable(false);
            patrollingReportTable.getColumnModel().getColumn(4).setResizable(false);
            patrollingReportTable.getColumnModel().getColumn(5).setResizable(false);
            patrollingReportTable.getColumnModel().getColumn(6).setResizable(false);
            patrollingReportTable.getColumnModel().getColumn(7).setResizable(false);
            patrollingReportTable.getColumnModel().getColumn(8).setResizable(false);
        }

        warningMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        warningMsg.setText("jLabel4");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pendingFeeLine, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(patrollingLabel)
                                .addGap(8, 8, 8)
                                .addComponent(complaintsLabel)))
                        .addGap(8, 8, 8)
                        .addComponent(employeeJobLabel)
                        .addGap(0, 777, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel24)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(dateCB, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(110, 110, 110)
                                        .addComponent(warningMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 283, Short.MAX_VALUE))))))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(458, 458, 458)
                .addComponent(viewBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(patrollingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(complaintsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(pendingFeeLine, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(employeeJobLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel23)
                .addGap(5, 5, 5)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(dateCB)
                    .addComponent(warningMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(viewBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        jPanel1.setBackground(new java.awt.Color(13, 24, 42));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/parkhillLogo.png"))); // NOI18N

        jobAssignationTab.setBackground(new java.awt.Color(13, 24, 42));
        jobAssignationTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jobAssignationTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jobAssignationTabMouseEntered(evt);
            }
        });

        jobAssignationInnerTab.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jobAssignationInnerTab.setForeground(new java.awt.Color(255, 255, 255));
        jobAssignationInnerTab.setText("Job Assignation");
        jobAssignationInnerTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jobAssignationInnerTabMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jobAssignationTabLayout = new javax.swing.GroupLayout(jobAssignationTab);
        jobAssignationTab.setLayout(jobAssignationTabLayout);
        jobAssignationTabLayout.setHorizontalGroup(
            jobAssignationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jobAssignationTabLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jobAssignationInnerTab, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jobAssignationTabLayout.setVerticalGroup(
            jobAssignationTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jobAssignationTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jobAssignationInnerTab)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(13, 24, 42));
        jPanel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel8MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel8MouseEntered(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(13, 24, 42));
        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Complaints");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(13, 50, 79));
        jPanel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel10MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel10MouseEntered(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Reports");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
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

        jLabel11.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/viewProfileIcon.png"))); // NOI18N
        jLabel11.setText("VIEW PROFILE");
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(13, 24, 42));
        jPanel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel9MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel9MouseEntered(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Patrolling Management");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        BEdashboardOuterPanel.setBackground(new java.awt.Color(13, 24, 42));
        BEdashboardOuterPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BEdashboardOuterPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BEdashboardOuterPanelMouseEntered(evt);
            }
        });

        BEdashboardInnerPanel.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        BEdashboardInnerPanel.setForeground(new java.awt.Color(255, 255, 255));
        BEdashboardInnerPanel.setText("Dashboard");
        BEdashboardInnerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BEdashboardInnerPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BEdashboardInnerPanelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout BEdashboardOuterPanelLayout = new javax.swing.GroupLayout(BEdashboardOuterPanel);
        BEdashboardOuterPanel.setLayout(BEdashboardOuterPanelLayout);
        BEdashboardOuterPanelLayout.setHorizontalGroup(
            BEdashboardOuterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BEdashboardOuterPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(BEdashboardInnerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122))
        );
        BEdashboardOuterPanelLayout.setVerticalGroup(
            BEdashboardOuterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BEdashboardOuterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BEdashboardInnerPanel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jobAssignationTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BEdashboardOuterPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(BEdashboardOuterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jobAssignationTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    private void patrollingLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_patrollingLabelMouseEntered
        // TODO add your handling code here:
        patrollingLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_patrollingLabelMouseEntered
    
    private void complaintsLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_complaintsLabelMouseEntered
        // TODO add your handling code here:
        complaintsLabel.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_complaintsLabelMouseEntered
    
    private void employeeJobLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeeJobLabelMouseEntered
        // TODO add your handling code here:
        employeeJobLabel.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_employeeJobLabelMouseEntered

    private void patrollingLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_patrollingLabelMouseClicked
        // TODO add your handling code here:
        BE.toPatrollingReports(this, BE);
    }//GEN-LAST:event_patrollingLabelMouseClicked

    private void complaintsLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_complaintsLabelMouseClicked
        // TODO add your handling code here:
        BE.toComplaintsReports(BE);
        this.dispose();
    }//GEN-LAST:event_complaintsLabelMouseClicked

    private void dateCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateCBActionPerformed
        tableSetup();
    }//GEN-LAST:event_dateCBActionPerformed

    private void viewBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewBTNActionPerformed
        // TODO add your handling code here:
        ArrayList<String> tableData = BE.getTableData(patRepTab);
        String reportTitle = "Patrolling Report - " + dateCB.getSelectedItem().toString();
        
        if (!tableData.isEmpty()) {
            BE.toAllReportsPage(BE, reportTitle, tableData);
        }
        else {
            JOptionPane.showMessageDialog (null, "The report has no data...", 
                                                    "GENERATE REPORT", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_viewBTNActionPerformed

    private void employeeJobLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeeJobLabelMouseClicked
        // TODO add your handling code here:
        BE.toJobReports(BE);
        this.dispose();
    }//GEN-LAST:event_employeeJobLabelMouseClicked

    private void jobAssignationInnerTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jobAssignationInnerTabMouseClicked
        // TODO add your handling code here:
        BE.toJobManagement(this, BE, null, false);
    }//GEN-LAST:event_jobAssignationInnerTabMouseClicked

    private void jobAssignationTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jobAssignationTabMouseClicked
        // TODO add your handling code here:
        BE.toJobManagement(this, BE, null, false);
    }//GEN-LAST:event_jobAssignationTabMouseClicked

    private void jobAssignationTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jobAssignationTabMouseEntered
        // TODO add your handling code here:
        jobAssignationTab.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jobAssignationTabMouseEntered

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:
        BE.toComplaints(this, BE);
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jPanel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseClicked
        // TODO add your handling code here:
        BE.toComplaints(this, BE);
    }//GEN-LAST:event_jPanel8MouseClicked

    private void jPanel8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseEntered
        // TODO add your handling code here:
        jPanel8.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jPanel8MouseEntered

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        // TODO add your handling code here:
        BE.toPatrollingReports(this, BE);
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jPanel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel10MouseClicked
        // TODO add your handling code here:
        BE.toPatrollingReports(this, BE);
    }//GEN-LAST:event_jPanel10MouseClicked

    private void jPanel10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel10MouseEntered
        // TODO add your handling code here:
        jPanel10.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jPanel10MouseEntered

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        // TODO add your handling code here:
        BE.toProfile(BE);
        this.dispose();
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jPanel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseClicked
        // TODO add your handling code here:
        BE.toProfile(BE);
        this.dispose();
    }//GEN-LAST:event_jPanel13MouseClicked

    private void jPanel13MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel13MouseEntered
        // TODO add your handling code here:
        jPanel13.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jPanel13MouseEntered

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        try {
            // TODO add your handling code here:
            BE.toPatrollingManagement(this, BE, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jPanel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseClicked
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            BE.toPatrollingManagement(this, BE, null);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_jPanel9MouseClicked

    private void jPanel9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseEntered
        // TODO add your handling code here:
        jPanel9.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jPanel9MouseEntered

    private void BEdashboardInnerPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BEdashboardInnerPanelMouseClicked
        // TODO add your handling code here:
        BE.toDashboard(this, BE);
    }//GEN-LAST:event_BEdashboardInnerPanelMouseClicked

    private void BEdashboardInnerPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BEdashboardInnerPanelMouseEntered
        // TODO add your handling code here:
        BEdashboardInnerPanel.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_BEdashboardInnerPanelMouseEntered

    private void BEdashboardOuterPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BEdashboardOuterPanelMouseClicked
        // TODO add your handling code here:
        BE.toDashboard(this, BE);
    }//GEN-LAST:event_BEdashboardOuterPanelMouseClicked

    private void BEdashboardOuterPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BEdashboardOuterPanelMouseEntered
        // TODO add your handling code here:
        BEdashboardOuterPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_BEdashboardOuterPanelMouseEntered

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
            java.util.logging.Logger.getLogger(BuildingExecutivePatrollingReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuildingExecutivePatrollingReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuildingExecutivePatrollingReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuildingExecutivePatrollingReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BuildingExecutivePatrollingReports(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BEdashboardInnerPanel;
    private javax.swing.JPanel BEdashboardOuterPanel;
    private javax.swing.JLabel complaintsLabel;
    private javax.swing.JComboBox<String> dateCB;
    private javax.swing.JLabel employeeJobLabel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel jobAssignationInnerTab;
    private javax.swing.JPanel jobAssignationTab;
    private javax.swing.JLabel patrollingLabel;
    private javax.swing.JTable patrollingReportTable;
    private javax.swing.JTextField pendingFeeLine;
    private javax.swing.JButton viewBTN;
    private javax.swing.JLabel warningMsg;
    // End of variables declaration//GEN-END:variables
}

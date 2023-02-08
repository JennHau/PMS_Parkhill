/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package buildingExecutive;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import pms_parkhill_residence.FileHandling;

/**
 *
 * @author wongj
 */
public class BuildingExecutivePatrollingManagement extends javax.swing.JFrame {
    public static BuildingExecutivePatrollingManagement BEpatrollingManagement;
    private final BuildingExecutive BE;
    
    FileHandling fh = new FileHandling();
    
    String patrollingScheduleFile;
    String patrollingScheduleFileFormat = "patrollingScheduleFiles/patrollingFile_";
    DefaultTableModel scheduleTable;
    
    private String currentBEid;
    private LocalDate todayDate;
    private LocalDate inputDate;
    private LocalTime inputTime;
    
    private String dayOfToday;
    private String patID;
    
    /**
     * Creates new form homePage
     * @param BE
     * @throws java.io.IOException
     */
    public BuildingExecutivePatrollingManagement(BuildingExecutive BE) throws IOException {
        this.BE = BE;
        this.setCurrentBEid(BE.getUserID());
        
        initComponents();
        runDefaultSetUp();
        BEpatrollingManagement = this;
    }
    
    private void runDefaultSetUp() throws IOException {
        scheduleTable = (DefaultTableModel) patrollingScheduleTable.getModel();
        
        setWindowIcon();
        getTodayDay();
        patrollingScheduleTableSetUp();
        fieldAction(false);
        
        removeEmpBTN.setEnabled(false);
        updateBTN.setEnabled(false);
        removeSlotBTN.setEnabled(false);
        securityIdComboBox.removeAllItems();
        
        disableManageButton();
    }
    
    private void getTodayDay() {
        todayDate = LocalDate.now();
        inputDate = todayDate;
        inputTime = LocalTime.now();
        dateLabel.setText(todayDate.toString());
        datePicker.setDate(todayDate);
        this.dayOfToday =  todayDate.getDayOfWeek().toString();
    }
    
    private void patrollingScheduleTableSetUp() {
        ArrayList<String> forTableSetup = new ArrayList<>();
        
        setSelectedDateFile();
        
        List<String> patrollingSchedule = fh.fileRead(patrollingScheduleFile);
        
        boolean firstLine = true;
        for (String slots : patrollingSchedule) {
            if (!firstLine) {
                String[] slotDetails = slots.split(BE.TF.sp);
                String patSlot = slotDetails[1];
                String patBlock = slotDetails[2];
                String patLevel = slotDetails[3];
                String patCheckPoints = slotDetails[4];
                String patCheckBef = slotDetails[5];
                String securityID = slotDetails[6].toUpperCase();
                String securityName = slotDetails[7];
                String patStatus = slotDetails[8];
                String assignee = slotDetails[9].toUpperCase();
                
                
                LocalTime slotTime = BE.DTF.formatTime(patSlot);
                
                LocalDateTime inputDateTime = LocalDateTime.of(inputDate, slotTime);
                LocalDateTime dateTimeNow = LocalDateTime.now();
                
                String action = "ASSIGN";
                
                if (inputDateTime.isBefore(dateTimeNow)) {
                    action = "PAST";
                }
                    
                forTableSetup.add(patSlot + BE.TF.sp + patBlock + BE.TF.sp + 
                                  patLevel + BE.TF.sp + patCheckPoints + BE.TF.sp + 
                                  patCheckBef + BE.TF.sp + securityID + BE.TF.sp + 
                                  securityName + BE.TF.sp + patStatus + BE.TF.sp +
                                  assignee + BE.TF.sp + action + BE.TF.sp);
            }
            
            firstLine = false;
        }
        
        BE.setTableRow(scheduleTable, forTableSetup);
    }
    
    private void setSelectedDateFile() {
        patrollingScheduleFile = patrollingScheduleFileFormat+inputDate+".txt";
        List<String> addNewRec = new ArrayList<>();
        
        File file = new File(patrollingScheduleFile);
        if (!file.exists()) {
            List<String> defaultSchedule = fh.fileRead(BE.TF.fixFile);
            fh.fileWrite(patrollingScheduleFile, false, defaultSchedule);
            
            addNewRec.add(inputDate + BE.TF.sp + "8" + BE.TF.sp + "5" + BE.TF.sp + "1" + BE.TF.sp + getCurrentBEid() + BE.TF.sp + BE.DTF.currentDateTime() + BE.TF.sp);
            fh.fileWrite(BE.TF.patScheduleModRec, true, addNewRec);
        }
    }
    
    private void fieldAction(boolean enable) {
        securityIdComboBox.setEnabled(enable);
        remarksTA.setEnabled(enable);
    }
    
    private void cleanField() {
        slotTF.setText("");
        levelTF.setText("");
        blockTF.setText("");
        checkpointsTF.setText("");
        timeCheckedPicker.setText("");
        statusTF.setText("");
        securityIdComboBox.removeAllItems();
        securityNameTF.setText("");
        contactNoTF.setText("");
        remarksTA.setText("");
    }
    
    private void setPatrollingScheduleTableDesign() {
        // design for the table header
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(13, 24, 42));
//        headerRenderer.setHorizontalAlignment(jLabel13.CENTER);
        headerRenderer.setForeground(new Color(255, 255, 255));
        for (int i = 0; i < patrollingScheduleTable.getModel().getColumnCount(); i++) {
            patrollingScheduleTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        // design for the table row
        DefaultTableCellRenderer rowRenderer = new DefaultTableCellRenderer();
//        rowRenderer.setHorizontalAlignment(jLabel13.CENTER);
        for (int i = 0; i < patrollingScheduleTable.getModel().getColumnCount(); i++) {
            if (i != 0) {
                patrollingScheduleTable.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
            }
        }
        
        TableColumnModel columnModel = patrollingScheduleTable.getColumnModel();
        // set first column width of the table to suitable value
        columnModel.getColumn(0).setMaxWidth(150);
        columnModel.getColumn(0).setMinWidth(150);
        columnModel.getColumn(0).setPreferredWidth(150);

        columnModel.getColumn(1).setMaxWidth(90);
        columnModel.getColumn(1).setMinWidth(90);
        columnModel.getColumn(1).setPreferredWidth(90);

        columnModel.getColumn(2).setMaxWidth(120);
        columnModel.getColumn(2).setMinWidth(120);
        columnModel.getColumn(2).setPreferredWidth(120);

        columnModel.getColumn(3).setMaxWidth(80);
        columnModel.getColumn(3).setMinWidth(80);
        columnModel.getColumn(3).setPreferredWidth(80);

        columnModel.getColumn(4).setMaxWidth(120);
        columnModel.getColumn(4).setMinWidth(120);
        columnModel.getColumn(4).setPreferredWidth(120);
        
        columnModel.getColumn(5).setMaxWidth(40);
        columnModel.getColumn(5).setMinWidth(40);
        columnModel.getColumn(5).setPreferredWidth(40);
        
        columnModel.getColumn(6).setMaxWidth(40);
        columnModel.getColumn(6).setMinWidth(40);
        columnModel.getColumn(6).setPreferredWidth(40);
        
        columnModel.getColumn(7).setMaxWidth(40);
        columnModel.getColumn(7).setMinWidth(40);
        columnModel.getColumn(7).setPreferredWidth(40);
        
        columnModel.getColumn(8).setMaxWidth(40);
        columnModel.getColumn(8).setMinWidth(40);
        columnModel.getColumn(8).setPreferredWidth(40);
        
        columnModel.getColumn(9).setMaxWidth(40);
        columnModel.getColumn(9).setMinWidth(40);
        columnModel.getColumn(9).setPreferredWidth(40);
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
        jLabel24 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        patrollingScheduleTable = new javax.swing.JTable()
        {
            @Override

            public Component prepareRenderer (TableCellRenderer renderer, int rowIndex, int columnIndex){
                Component componenet = super.prepareRenderer(renderer, rowIndex, columnIndex);

                Object value = getModel().getValueAt(rowIndex,columnIndex);

                if(columnIndex == 9){

                    if(value.equals("ASSIGN")){
                        componenet.setBackground(new Color(0,70,126));
                        componenet.setForeground(new Color(255, 255, 255));
                    }
                    else {
                        componenet.setBackground(new Color(255,0,0));
                        componenet.setForeground(new Color(255, 255, 255));
                    }

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
        jLabel26 = new javax.swing.JLabel();
        removeEmpBTN = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel28 = new javax.swing.JLabel();
        contactNoTF = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        manageScheduleBTN = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        updateBTN = new javax.swing.JButton();
        blockTF = new javax.swing.JTextField();
        slotTF = new javax.swing.JTextField();
        levelTF = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        securityNameTF = new javax.swing.JTextField();
        securityIdComboBox = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        checkpointsTF = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        remarksTA = new javax.swing.JTextArea();
        jSeparator2 = new javax.swing.JSeparator();
        timeCheckedPicker = new com.github.lgooddatepicker.components.TimePicker();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        datePicker = new com.github.lgooddatepicker.components.DatePicker();
        dateLabel = new javax.swing.JLabel();
        selectDateBTN = new javax.swing.JButton();
        addSlotBTN = new javax.swing.JButton();
        removeSlotBTN = new javax.swing.JButton();
        statusTF = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PARKHILL RESIDENCE");
        setBackground(new java.awt.Color(13, 24, 42));
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setResizable(false);
        setSize(new java.awt.Dimension(1280, 720));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(153, 153, 153), null, null));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("PARKHILL RESIDENCE BUILDING EXECUTIVE");
        jLabel2.setFont(new java.awt.Font("Britannic Bold", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(13, 24, 42));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/profileIcon.jpg"))); // NOI18N
        jLabel7.setText("USERNAME");
        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jLabel24.setText("Patrolling Schedule:");
        jLabel24.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 51, 51));

        patrollingScheduleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SLOT", "BLOCK", "LEVEL", "CHECKPOINT", "CHECK BEFORE", "SECURITY ID", "SECURITY NAME", "STATUS", "ASSIGNEE ID", "ACTION"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        patrollingScheduleTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                patrollingScheduleTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(patrollingScheduleTable);
        if (patrollingScheduleTable.getColumnModel().getColumnCount() > 0) {
            patrollingScheduleTable.getColumnModel().getColumn(0).setResizable(false);
            patrollingScheduleTable.getColumnModel().getColumn(1).setResizable(false);
            patrollingScheduleTable.getColumnModel().getColumn(2).setResizable(false);
            patrollingScheduleTable.getColumnModel().getColumn(3).setResizable(false);
            patrollingScheduleTable.getColumnModel().getColumn(4).setResizable(false);
            patrollingScheduleTable.getColumnModel().getColumn(5).setResizable(false);
            patrollingScheduleTable.getColumnModel().getColumn(6).setResizable(false);
            patrollingScheduleTable.getColumnModel().getColumn(7).setResizable(false);
            patrollingScheduleTable.getColumnModel().getColumn(8).setResizable(false);
            patrollingScheduleTable.getColumnModel().getColumn(9).setResizable(false);
        }

        jLabel26.setText("Slot: ");
        jLabel26.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(51, 51, 51));

        removeEmpBTN.setText("Remove Employee");
        removeEmpBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeEmpBTNActionPerformed(evt);
            }
        });

        jLabel30.setText("Management: ");
        jLabel30.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(51, 51, 51));

        jLabel28.setText("Contact No.: ");
        jLabel28.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(51, 51, 51));

        contactNoTF.setEnabled(false);

        jLabel32.setText("Block: ");
        jLabel32.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(51, 51, 51));

        manageScheduleBTN.setText("Manage Schedule");
        manageScheduleBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageScheduleBTNActionPerformed(evt);
            }
        });

        jLabel33.setText("Level: ");
        jLabel33.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(51, 51, 51));

        updateBTN.setText("Update");
        updateBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBTNActionPerformed(evt);
            }
        });

        blockTF.setEnabled(false);

        slotTF.setEnabled(false);

        levelTF.setEnabled(false);

        jLabel31.setText("Security Name: ");
        jLabel31.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(51, 51, 51));

        securityNameTF.setEnabled(false);

        securityIdComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "EMPTY" }));
        securityIdComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                securityIdComboBoxActionPerformed(evt);
            }
        });

        jLabel34.setText("Checkpoints: ");
        jLabel34.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(51, 51, 51));

        checkpointsTF.setEnabled(false);

        jLabel36.setText("Status: ");
        jLabel36.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(51, 51, 51));

        jLabel35.setText("Checked At: ");
        jLabel35.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(51, 51, 51));

        jLabel37.setText("Remarks: ");
        jLabel37.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(51, 51, 51));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        remarksTA.setColumns(20);
        remarksTA.setLineWrap(true);
        remarksTA.setRows(5);
        remarksTA.setWrapStyleWord(true);
        jScrollPane2.setViewportView(remarksTA);

        timeCheckedPicker.setEnabled(false);

        jLabel38.setText("Today: ");
        jLabel38.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(51, 51, 51));

        jLabel39.setText("Security ID: ");
        jLabel39.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(51, 51, 51));

        jLabel40.setText("Date: ");
        jLabel40.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(51, 51, 51));

        dateLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dateLabel.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        dateLabel.setForeground(new java.awt.Color(51, 51, 51));

        selectDateBTN.setText("Select");
        selectDateBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectDateBTNActionPerformed(evt);
            }
        });

        addSlotBTN.setText("Add Slot");
        addSlotBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSlotBTNActionPerformed(evt);
            }
        });

        removeSlotBTN.setText("Remove Slot");
        removeSlotBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSlotBTNActionPerformed(evt);
            }
        });

        statusTF.setEnabled(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(slotTF, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(levelTF, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(blockTF, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(statusTF))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(timeCheckedPicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(checkpointsTF))))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel31))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(securityNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(contactNoTF, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(securityIdComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(80, 80, 80)))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2)))
                    .addComponent(jSeparator2)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(653, 653, 653)
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addSlotBTN))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectDateBTN)
                        .addGap(653, 653, 653)
                        .addComponent(manageScheduleBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(361, 361, 361)
                .addComponent(removeSlotBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(removeEmpBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(updateBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(manageScheduleBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectDateBTN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(addSlotBTN))
                .addGap(2, 2, 2)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(securityIdComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(securityNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(contactNoTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane2)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(slotTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(checkpointsTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(levelTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(blockTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(timeCheckedPicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(19, 19, 19)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(statusTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 5, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(removeEmpBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeSlotBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(13, 24, 42));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/parkhillLogo.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(13, 24, 42));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel2MouseEntered(evt);
            }
        });

        jLabel1.setText("Dashboard");
        jLabel1.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jobAssignationTab.setBackground(new java.awt.Color(13, 24, 42));
        jobAssignationTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jobAssignationTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jobAssignationTabMouseEntered(evt);
            }
        });

        jobAssignationInnerTab.setText("Job Assignation");
        jobAssignationInnerTab.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jobAssignationInnerTab.setForeground(new java.awt.Color(255, 255, 255));
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jLabel5.setText("Complaints");
        jLabel5.setBackground(new java.awt.Color(13, 24, 42));
        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jPanel10.setBackground(new java.awt.Color(13, 24, 42));
        jPanel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel10MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel10MouseEntered(evt);
            }
        });

        jLabel8.setText("Reports");
        jLabel8.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoutIcon.png"))); // NOI18N
        jLabel10.setText("LOGOUT");
        jLabel10.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));

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

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/viewProfileIcon.png"))); // NOI18N
        jLabel11.setText("VIEW PROFILE");
        jLabel11.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
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

        jPanel9.setBackground(new java.awt.Color(13, 50, 79));
        jPanel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel9MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel9MouseEntered(evt);
            }
        });

        jLabel6.setText("Patrolling Management");
        jLabel6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
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
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jobAssignationTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            
    private void patrollingScheduleTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_patrollingScheduleTableMouseClicked
        // TODO: add checking to see is the schedule over
        int selectedCol = patrollingScheduleTable.getSelectedColumn();
        int selectedRow = patrollingScheduleTable.getSelectedRow();
        
        boolean correctSel = (selectedCol == 9);
        
        String securityID = (!patrollingScheduleTable.getValueAt(selectedRow, 5).equals("-")) ? (patrollingScheduleTable.getValueAt(selectedRow, 5)).toString() : null;
        
        Object[] rowData = new Object[scheduleTable.getColumnCount()];
        
        if (correctSel) {
            String actionText = patrollingScheduleTable.getValueAt(selectedRow, selectedCol).toString();
            
            if (actionText.equals("ASSIGN")) {
                List<String> scheduleFile = fh.fileRead(patrollingScheduleFile);
                patID = scheduleFile.get(selectedRow+1).split(BE.TF.sp)[0];
                String checkedAt = scheduleFile.get(selectedRow+1).split(BE.TF.sp)[10];

                for (int data = 0; data < scheduleTable.getColumnCount(); data++) {
                    rowData[data] = scheduleTable.getValueAt(selectedRow, data);
                }

                inputTime = BE.formatTime(rowData[0].toString());
                slotTF.setText((String) rowData[0]);
                blockTF.setText((String) rowData[1]);
                levelTF.setText((String) rowData[2]);
                checkpointsTF.setText((String) rowData[3]);

                statusTF.setText(rowData[7].toString());


                if (!checkedAt.equals("-")) {
                    timeCheckedPicker.setTime(BE.formatTime(checkedAt));
                }
                else {
                    timeCheckedPicker.setText("-");
                }


                try {
                    comboBoxSetUp();
                } catch (IOException ex) {
                    Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (securityID != null) {
                    try {
                        String[] empDet = BE.getEmployeeDetails(securityID);

                        securityIdComboBox.addItem(securityID);
                        securityIdComboBox.setSelectedItem(rowData[5]);
                        securityNameTF.setText((String) rowData[6]);
                        contactNoTF.setText(empDet[3]);
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    removeEmpBTN.setEnabled(true);
                }
                else {
                    removeEmpBTN.setEnabled(false);
                    securityIdComboBox.setSelectedIndex(0);
                    securityNameTF.setText("");
                    contactNoTF.setText("");
                    remarksTA.setText("");
                }

                fieldAction(true);
                removeSlotBTN.setEnabled(true);
                updateBTN.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog (null, "The time slot has pass the time now... Please choose another slot.", 
                                        "PATROLLING ASSIGNATION ERROR", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_patrollingScheduleTableMouseClicked
    
    private void updateBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBTNActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(null,"Are you sure to "
                + "update this slot?", "PATROLLING SCHEDULE",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);
        
        if(result == JOptionPane.YES_OPTION){
            ArrayList<String> toUpdateList = new ArrayList<>();

            String slot = slotTF.getText();
            String checkpoint = checkpointsTF.getText();
            String securityId = securityIdComboBox.getSelectedItem().toString();
            String securityName = securityNameTF.getText();
            String remarks = (!remarksTA.getText().equals("")) ? remarksTA.getText() : " ";

            List<String> scheduleList = fh.fileRead(patrollingScheduleFile);

            String checkBef = null;
            for (String eachSched : scheduleList) {
                String[] schedules = eachSched.split(BE.TF.sp);
                String id = schedules[0];
                checkBef = schedules[5];

                if (id.equals(patID)) {
                    toUpdateList.add(id + BE.TF.sp + schedules[1] + BE.TF.sp + schedules[2] + BE.TF.sp + schedules[3] + BE.TF.sp +
                            checkpoint + BE.TF.sp + checkBef + BE.TF.sp + securityId + BE.TF.sp + securityName + BE.TF.sp + 
                            remarks + BE.TF.sp + schedules[8] + BE.TF.sp + " " + BE.TF.sp + this.getCurrentBEid() + BE.TF.sp + 
                            BE.DTF.currentDateTime() + BE.TF.sp);
                }
                else {
                    toUpdateList.add(eachSched);
                }
            }

            fh.fileWrite(patrollingScheduleFile, false, toUpdateList);

            String timeNeeded = String.valueOf(BE.formatTime(checkBef).compareTo(BE.formatTime(slot)));

            patrollingScheduleTableSetUp();
            fieldAction(false);

            deletePatScheduleFromJobFile();

            String newTaskId = null;
            try {
                newTaskId = BE.getNewTaskId(BE.TF.employeeJobFile, 0);
            } catch (IOException ex) {
                Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                comboBoxSetUp();
                updateBTN.setEnabled(false);
                removeSlotBTN.setEnabled(false);
                removeEmpBTN.setEnabled(false);
                cleanField();
            } catch (IOException ex) {
                Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(Level.SEVERE, null, ex);
            }

            ArrayList<String> addJob = new ArrayList<>();
            if (newTaskId != null) {
                addJob.add(newTaskId + BE.TF.sp + securityId + BE.TF.sp + "null" + BE.TF.sp + 
                        "PT" + BE.TF.sp + 0 + BE.TF.sp + timeNeeded+"h" + BE.TF.sp + this.inputDate + BE.TF.sp + slot + BE.TF.sp + this.inputDate + " " + checkBef + 
                        BE.TF.sp + "null" + BE.TF.sp + remarks + BE.TF.sp + "null" + BE.TF.sp + 
                        BE.DTF.currentDateTime() + 
                        BE.TF.sp + inputDate + " " + patID + BE.TF.sp);
            }
            fh.fileWrite(BE.TF.employeeJobFile, true, addJob);
        }
    }//GEN-LAST:event_updateBTNActionPerformed
    
    private void removeEmpBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeEmpBTNActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(null,"Are you sure to "
                + "remove this employee?", "PATROLLING SCHEDULE",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);
        
        if(result == JOptionPane.YES_OPTION){
            ArrayList<String> toRemove = new ArrayList<>();
            List<String> oldSchedule = fh.fileRead(patrollingScheduleFile);

            for (String eachSche : oldSchedule) {
                String[] scheDet = eachSche.split(BE.TF.sp);
                String slotId = scheDet[0];
                if (slotId.equals(patID)) {
                    toRemove.add(scheDet[0] + BE.TF.sp + scheDet[1] + BE.TF.sp + scheDet[2] + BE.TF.sp + 
                                 scheDet[3] + BE.TF.sp + scheDet[4] + BE.TF.sp + scheDet[5] + BE.TF.sp + 
                                 " " + BE.TF.sp + " " + BE.TF.sp + " " + BE.TF.sp + " " + BE.TF.sp + " " + BE.TF.sp + 
                                 BE.combineStringDateTime(LocalDate.now().toString(), LocalTime.now().toString()) + 
                                 BE.TF.sp + " ");
                }
                else {
                    toRemove.add(eachSche);
                }
            }

            fh.fileWrite(patrollingScheduleFile, false, toRemove);

            patrollingScheduleTableSetUp();

            deletePatScheduleFromJobFile();

            cleanField();
        }
    }//GEN-LAST:event_removeEmpBTNActionPerformed
    
    private void securityIdComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_securityIdComboBoxActionPerformed
        // TODO add your handling code here:
        String securityId = (String) securityIdComboBox.getSelectedItem();
        try {
            String[] securityDet = BE.getEmployeeDetails(securityId);
            if (securityDet != null) {
                String securityName = securityDet[2];
                String contact = securityDet[3];
                securityNameTF.setText(securityName);
                contactNoTF.setText(contact);
                updateBTN.setEnabled(true);
            }
            else {
                securityNameTF.setText("");
                contactNoTF.setText("");
                updateBTN.setEnabled(false);
            }
        } catch (IOException ex) {
            Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_securityIdComboBoxActionPerformed
    
    private void selectDateBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectDateBTNActionPerformed
        // TODO add your handling code here:
        inputDate = datePicker.getDate();
        
        disableManageButton();
        patrollingScheduleTableSetUp();
    }//GEN-LAST:event_selectDateBTNActionPerformed

    private void manageScheduleBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageScheduleBTNActionPerformed
        // TODO add your handling code here:
        BE.toScheduleModification(this.BE, patrollingScheduleFile, inputDate);
        this.dispose();
    }//GEN-LAST:event_manageScheduleBTNActionPerformed

    private void removeSlotBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSlotBTNActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(null,"Are you sure to "
                + "remove this slot?", "PATROLLING SCHEDULE",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

        if(result == JOptionPane.YES_OPTION){
            ArrayList<String> updatedSched = new ArrayList<>();
            List<String> allSlots = fh.fileRead(patrollingScheduleFile);
            for (String eachSlot : allSlots) {
                String slotId = eachSlot.split(BE.TF.sp)[0];
                if (!slotId.equals(patID)) {
                    updatedSched.add(eachSlot);
                }
            }

            fh.fileWrite(patrollingScheduleFile, false, updatedSched);

            deletePatScheduleFromJobFile();

            patrollingScheduleTableSetUp();

            fieldAction(false);
            updateBTN.setEnabled(false);
            removeSlotBTN.setEnabled(false);
            removeEmpBTN.setEnabled(false);
            cleanField();
        }
    }//GEN-LAST:event_removeSlotBTNActionPerformed

    private void addSlotBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSlotBTNActionPerformed
        // TODO add your handling code here:
        ScheduleActionPage page = new ScheduleActionPage(BE, inputDate, patrollingScheduleFile);
        page.setVisible(true);
    }//GEN-LAST:event_addSlotBTNActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jPanel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseEntered
        // TODO add your handling code here:
        jPanel2.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jPanel2MouseEntered

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
            BE.toPatrollingManagement(this, BE);
        } catch (IOException ex) {
            Logger.getLogger(BuildingExecutiveMainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jPanel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseClicked
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            BE.toPatrollingManagement(this, BE);
        } catch (IOException ex) {
            Logger.getLogger(BuildingExecutiveMainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jPanel9MouseClicked

    private void jPanel9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseEntered
        // TODO add your handling code here:
        jPanel9.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jPanel9MouseEntered
    
    private void deletePatScheduleFromJobFile() {
        ArrayList<String> removeFromJobFile = new ArrayList<>();
        
        String dayCode  = inputDate + " " + patID;
        
        List<String> getEmployeeList = fh.fileRead(BE.TF.employeeJobFile);
        for (String eachJob : getEmployeeList) {
            String[] jobDet = eachJob.split(BE.TF.sp);
            String patCode = jobDet[jobDet.length - 1];
            
            if (!patCode.equals(dayCode)) {
                removeFromJobFile.add(eachJob);
            }
        }
        
        fh.fileWrite(BE.TF.employeeJobFile, false, removeFromJobFile);
        
        removeEmpBTN.setEnabled(false);
        updateBTN.setEnabled(false);
        fieldAction(false);
    }
    
    private void comboBoxSetUp() throws IOException {
        ArrayList<String> assignedEmployee = BE.getSpecificStatusEmployeeList(inputDate, inputTime, "Security Guard", null, BE.assignedEmployee);
        ArrayList<String> unassignedEmployee = BE.getSpecificStatusEmployeeList(inputDate, inputTime, "Security Guard", null, BE.unassignedEmployee);
        ArrayList<String> canPatrollId = new ArrayList<>();
        
        for (String unassign : unassignedEmployee) {
            assignedEmployee.add(unassign);
        }
        
        for (String allEmp : assignedEmployee) {
            String empId = allEmp.split(BE.TF.sp)[0];
            
            ArrayList<String> jobList = BE.getAssignedJobForSpecificEmployee(empId);
            
            boolean cannotPatroll = false;
            if (!jobList.isEmpty()) {
                for (String eachJob : jobList) {
                    String[] jobDetails = eachJob.split(BE.TF.sp);
                    String assignedJobCode = jobDetails[3];
                    if (assignedJobCode.equals("NS") || assignedJobCode.equals("MS") || assignedJobCode.equals("PT")) {
                        int repitition = Integer.valueOf(jobDetails[4]);

                        String timeNeeded = jobDetails[5];
                        LocalDate workingDate = null;
                        LocalTime workingTime = BE.formatTime(jobDetails[7]);
                        String[] workingEndDateTime = jobDetails[8].split(" ");

                        LocalTime workingStartTime2;
                        LocalTime workingEndTime2;

                        ArrayList<String> dateData = BE.compareJobDate(repitition, workingEndDateTime, jobDetails, timeNeeded, dayOfToday, inputDate, workingTime, workingDate);

                        if (!dateData.isEmpty()) {
                            workingDate = BE.formatDate(dateData.get(0));
                            workingTime = BE.formatTime(dateData.get(1));

                            workingStartTime2 = (!dateData.get(2).equals("null")) ? BE.formatTime(dateData.get(2)) : null;
                            workingEndTime2 = (!dateData.get(3).equals("null")) ? BE.formatTime(dateData.get(3)) : null;

                            workingEndDateTime = dateData.get(4).split(" ");

                            if (workingDate != null) {
                                cannotPatroll = BE.compareDateTime(inputDate, inputTime, workingDate, workingTime, workingEndDateTime, workingStartTime2, workingEndTime2);
                            }
                        }

                        if (cannotPatroll) {
                            break;
                        }
                    }
                }
            }
            
            if (!cannotPatroll && !canPatrollId.contains(empId)) {
                canPatrollId.add(empId.toUpperCase());
            }
        }
        
        securityIdComboBox.removeAllItems();
        securityIdComboBox.addItem("Not Selected");
        
        for (String eachId : canPatrollId) {
            securityIdComboBox.addItem(eachId);
        }
    }
    
    private void disableManageButton() {
        boolean setAction = (!todayDate.isEqual(inputDate) && !inputDate.isBefore(todayDate));
        manageScheduleBTN.setEnabled(setAction);
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
            java.util.logging.Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new BuildingExecutivePatrollingManagement(null).setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addSlotBTN;
    private javax.swing.JTextField blockTF;
    private javax.swing.JTextField checkpointsTF;
    private javax.swing.JTextField contactNoTF;
    private javax.swing.JLabel dateLabel;
    private com.github.lgooddatepicker.components.DatePicker datePicker;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel jobAssignationInnerTab;
    private javax.swing.JPanel jobAssignationTab;
    private javax.swing.JTextField levelTF;
    private javax.swing.JButton manageScheduleBTN;
    private javax.swing.JTable patrollingScheduleTable;
    private javax.swing.JTextArea remarksTA;
    private javax.swing.JButton removeEmpBTN;
    private javax.swing.JButton removeSlotBTN;
    private javax.swing.JComboBox<String> securityIdComboBox;
    private javax.swing.JTextField securityNameTF;
    private javax.swing.JButton selectDateBTN;
    private javax.swing.JTextField slotTF;
    private javax.swing.JTextField statusTF;
    private com.github.lgooddatepicker.components.TimePicker timeCheckedPicker;
    private javax.swing.JButton updateBTN;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the currentBEid
     */
    public String getCurrentBEid() {
        return currentBEid;
    }

    /**
     * @param currentBEid the currentBEid to set
     */
    public void setCurrentBEid(String currentBEid) {
        this.currentBEid = currentBEid;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package buildingExecutive;

import classes.AssignedJob;
import classes.Employee;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import classes.FileHandling;
import classes.Patrolling;
import pms_parkhill_residence.HomePage;

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
    
    private Patrolling patrolling;
    private AssignedJob assignedJob;
    
    /**
     * Creates new form homePage
     * @param BE
     * @param selectedDate
     * @throws java.io.IOException
     */
    public BuildingExecutivePatrollingManagement(BuildingExecutive BE, LocalDate selectedDate) throws IOException {
        this.BE = BE;
        this.setCurrentBEid(BE.getUserID());
        
        this.inputDate = selectedDate;
        
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
        
        setPatrollingScheduleTableDesign();
        
        setUserProfile();
    }
    
    // get today's day
    private void getTodayDay() {
        todayDate = LocalDate.now();
        inputTime = LocalTime.now();
        dateLabel.setText(todayDate.toString());
        this.dayOfToday =  todayDate.getDayOfWeek().toString();
        
        if (inputDate == null) {
            inputDate = todayDate;
        }
        
        datePicker.setDate(inputDate);
    }
    
    // set up patrolling schedule
    private void patrollingScheduleTableSetUp() {
        ArrayList<String> forTableSetup = new ArrayList<>();
        
        setSelectedDateFile();
        
        ArrayList<Patrolling> patrollingSchedule = BE.PT.extractAllPatrolling(patrollingScheduleFile);
        
        boolean firstLine = true;
        for (Patrolling slots : patrollingSchedule) {
            if (!firstLine) {
                String patSlot = slots.getSlot();
                String patBlock = slots.getBlock();
                String patLevel = slots.getLevel();
                String patCheckPoints = slots.getCheckpoints();
                String patCheckBef = slots.getCheckBefore();
                String securityID = slots.getPatSecID().toUpperCase();
                String securityName = slots.getPatSecName();
                String patStatus = slots.getStatus();
                String assignee = slots.getUpdatedBy().toUpperCase();
                
                LocalTime slotTime = BE.DTF.formatTime(patSlot);
                
                LocalDateTime inputDateTime = LocalDateTime.of(inputDate, slotTime);
                LocalDateTime dateTimeNow = LocalDateTime.now();
                
                String action = "ASSIGN";
                
                if (!securityID.equals(BE.TF.empty)){
                    action = "MODIFY";
                }
                
                if (inputDateTime.isBefore(dateTimeNow)) {
                    action = "PAST";
                }
                
                if (patStatus.equals("Checked")) {
                    action = "VIEW";
                }
                
                if (action.equals("PAST")) {
                    patStatus = "Unassign";
                }
                
                if (action.equals("PAST") && !securityID.equals(BE.TF.empty) && !patStatus.equals("Checked")) {
                    patStatus = "Uncheck";
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
    
    // set file path according to selected date
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
    
    // text field action
    private void fieldAction(boolean enable) {
        securityIdComboBox.setEnabled(enable);
        remarksTA.setEnabled(enable);
    }
    
    // clean field
    private void cleanField() {
        slotTF.setText("");
        levelTF.setText("");
        blockTF.setText("");
        checkpointComboBox.removeAllItems();
        checkpointComboBox.setEnabled(false);
        timeCheckedPicker.setText("");
        statusTF.setText("");
        securityIdComboBox.removeAllItems();
        securityIdComboBox.setEnabled(false);
        securityNameTF.setText("");
        contactNoTF.setText("");
        remarksTA.setText("");
    }
    
    // set up current building executive profile
    private void setUserProfile() throws IOException {
        // get current BE details
        // Set text field
        if (currentBEid != null) {
            String beName = BE.getFirstName() + " " + BE.getLastName();
            jLabel7.setText(beName);
        }
    }
    
    // set up table design
    private void setPatrollingScheduleTableDesign() {
        int[] columnIgnore = {6};
        int[] columnLength = {80, 70, 120, 100, 110, 100, 172, 120, 100, 100};
        BE.setTableDesign(patrollingScheduleTable, jLabel2, columnLength, columnIgnore);
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

                    if(value.equals("ASSIGN") || value.equals("MODIFY")){
                        componenet.setBackground(new Color(0,70,126));
                        componenet.setForeground(new Color(255, 255, 255));
                    }
                    else if (value.equals("VIEW")) {
                        componenet.setBackground(new Color(0, 153, 51));
                        componenet.setForeground(new Color(255, 255, 255));
                    }
                    else {
                        componenet.setBackground(new Color(255, 0, 0));
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
        checkpointComboBox = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jobAssignationTab = new javax.swing.JPanel();
        jobAssignationInnerTab = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        BEdashboardOuterPanel = new javax.swing.JPanel();
        BEdashboardInnerPanel = new javax.swing.JLabel();
        logoutPanel1 = new javax.swing.JPanel();
        logoutLabel1 = new javax.swing.JLabel();

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
                "SLOT", "BLOCK", "LEVEL", "CHECKPOINT", "CHECK BEFORE", "SECURITY ID", "SECURITY NAME", "STATUS", "UPDATE BY", "ACTION"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        patrollingScheduleTable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        patrollingScheduleTable.setForeground(new java.awt.Color(51, 51, 51));
        patrollingScheduleTable.setIntercellSpacing(new java.awt.Dimension(1, 1));
        patrollingScheduleTable.setRowHeight(25);
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

        checkpointComboBox.setEnabled(false);
        checkpointComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkpointComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
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
                                    .addComponent(checkpointComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                        .addComponent(manageScheduleBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
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
                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(checkpointComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(19, 19, 19)
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
                .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 4, Short.MAX_VALUE)
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

        BEdashboardOuterPanel.setBackground(new java.awt.Color(13, 24, 42));
        BEdashboardOuterPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BEdashboardOuterPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BEdashboardOuterPanelMouseEntered(evt);
            }
        });

        BEdashboardInnerPanel.setText("Dashboard");
        BEdashboardInnerPanel.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        BEdashboardInnerPanel.setForeground(new java.awt.Color(255, 255, 255));
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        logoutPanel1.setBackground(new java.awt.Color(13, 24, 42));
        logoutPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutPanel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutPanel1MouseEntered(evt);
            }
        });

        logoutLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoutIcon.png"))); // NOI18N
        logoutLabel1.setText("LOGOUT");
        logoutLabel1.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        logoutLabel1.setForeground(new java.awt.Color(255, 255, 255));
        logoutLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutLabel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutLabel1MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout logoutPanel1Layout = new javax.swing.GroupLayout(logoutPanel1);
        logoutPanel1.setLayout(logoutPanel1Layout);
        logoutPanel1Layout.setHorizontalGroup(
            logoutPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, logoutPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logoutLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        logoutPanel1Layout.setVerticalGroup(
            logoutPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoutPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoutLabel1)
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
                    .addComponent(BEdashboardOuterPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logoutPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 352, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logoutPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        
        String securityID = (!patrollingScheduleTable.getValueAt(selectedRow, 5).equals(BE.TF.empty)) ? (patrollingScheduleTable.getValueAt(selectedRow, 5)).toString() : null;
        
        if (correctSel) {
            String actionText = patrollingScheduleTable.getValueAt(selectedRow, selectedCol).toString();
            String statusText = patrollingScheduleTable.getValueAt(selectedRow, 7).toString();
            
            if (actionText.equals("ASSIGN") || actionText.equals("MODIFY") || actionText.equals("VIEW") || (actionText.equals("PAST") && statusText.equals("Uncheck"))) {
                List<String> scheduleFile = fh.fileRead(patrollingScheduleFile);
                String[] patDet = scheduleFile.get(selectedRow+1).split(BE.TF.sp);
                
                patrolling = new Patrolling(patDet);
                
                String checkedAt = patrolling.getCheckedAt();

                inputTime = BE.DTF.formatTime(patrolling.getSlot());
                slotTF.setText(patrolling.getSlot());
                blockTF.setText(patrolling.getBlock());
                levelTF.setText(patrolling.getLevel());
                checkpointComboBoxSetUp(patrolling.getLevel());
                checkpointComboBox.setSelectedItem(patrolling.getCheckpoints());
                statusTF.setText(patrolling.getStatus());

                if (!checkedAt.equals(BE.TF.empty)) {
                    timeCheckedPicker.setTime(BE.DTF.formatTime(checkedAt));
                }
                else {
                    timeCheckedPicker.setText(BE.TF.empty);
                }

                try {
                    employeeIdComboBoxSetUp();
                } catch (IOException ex) {
                    Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (securityID != null) {
                    Employee employee = new Employee(securityID.toLowerCase());
                    
                    assignedJob = new AssignedJob(String.valueOf(inputDate), patrolling.getPatID());

                    securityIdComboBox.addItem(securityID);
                    securityIdComboBox.setSelectedItem(securityID);
                    securityNameTF.setText(employee.getEmpName());
                    contactNoTF.setText(employee.getPhoneNo());
                    
                    removeEmpBTN.setEnabled(true);
                }
                else {
                    assignedJob = null;
                    
                    removeEmpBTN.setEnabled(false);
                    securityIdComboBox.setSelectedIndex(0);
                    securityNameTF.setText("");
                    contactNoTF.setText("");
                    remarksTA.setText("");
                }

                fieldAction(true);
                removeSlotBTN.setEnabled(true);
                updateBTN.setEnabled(false);
                checkpointComboBox.setEnabled(true);
                
                if (actionText.equals("VIEW") || (actionText.equals("PAST") && statusText.equals("Pending"))) {
                    updateBTN.setEnabled(false);
                    removeSlotBTN.setEnabled(false);
                    securityIdComboBox.setEnabled(false);
                    removeEmpBTN.setEnabled(false);
                    remarksTA.setEnabled(false);
                    checkpointComboBox.setEnabled(false);
                }
            } else {
                cleanField();
                removeSlotBTN.setEnabled(false);
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
            String securityId = (!securityIdComboBox.getSelectedItem().toString().equals("Not Selected")) ? securityIdComboBox.getSelectedItem().toString() : BE.TF.empty;
            String securityName = (!securityNameTF.getText().equals("")) ? securityNameTF.getText() : BE.TF.empty;
            String remarks = (!remarksTA.getText().equals("")) ? remarksTA.getText() : " ";
            String level = checkpointComboBox.getSelectedItem().toString();
            
            patrolling.setPatSecID(securityId.toLowerCase());
            patrolling.setPatSecName(securityName);
            patrolling.setPatRemarks(remarks);
            
            if (!securityId.equals(BE.TF.empty)) {
                patrolling.setStatus("Pending");
            }
            
            patrolling.setUpdatedBy(this.BE.getUserID());
            patrolling.setCheckpoints(level);
            patrolling.setLastUpdate(BE.DTF.currentDateTime());

            BE.updatePatrolling(patrollingScheduleFile, patrolling);
            
            fieldAction(false);
            
            patrollingScheduleTableSetUp();

            try {
                updatePatShceduleFromJobFile();
                employeeIdComboBoxSetUp();
                updateBTN.setEnabled(false);
                removeSlotBTN.setEnabled(false);
                removeEmpBTN.setEnabled(false);
                cleanField();
                
                patrolling = null;
                assignedJob = null;
            } catch (IOException ex) {
                Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_updateBTNActionPerformed
    
    private void removeEmpBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeEmpBTNActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(null,"Are you sure to "
                + "remove this employee?", "PATROLLING SCHEDULE",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);
        
        if(result == JOptionPane.YES_OPTION){
            
            patrolling.setPatSecID(BE.TF.empty);
            patrolling.setPatSecName(BE.TF.empty);
            patrolling.setStatus("Unassign");
            patrolling.setPatRemarks(BE.TF.empty);
            patrolling.setUpdatedBy(BE.getUserID());
            patrolling.setLastUpdate(BE.DTF.currentDateTime());
            
            BE.updatePatrolling(patrollingScheduleFile, patrolling);
            
            patrollingScheduleTableSetUp();
            
            try {
                deletePatScheduleFromJobFile();
            } catch (IOException ex) {
                Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            cleanField();
            
            removeSlotBTN.setEnabled(false);
            assignedJob = null;
            patrolling = null;
        }
    }//GEN-LAST:event_removeEmpBTNActionPerformed
    
    private void securityIdComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_securityIdComboBoxActionPerformed
        // TODO add your handling code here:
        String securityId = (String) securityIdComboBox.getSelectedItem();
        
        if (securityId != null && !securityId.equals("Not Selected")) {
            Employee employee = new Employee(securityId.toLowerCase());

            if (employee.isEmployee()) {
                String securityName = employee.getEmpName();
                String contact = employee.getPhoneNo();
                securityNameTF.setText(securityName);
                contactNoTF.setText(contact);
                updateBTN.setEnabled(true);

                patrolling.setPatSecID(securityId);
                patrolling.setPatSecName(securityName);
            }
            else {
                securityNameTF.setText("");
                contactNoTF.setText("");
                updateBTN.setEnabled(false);
            }
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
        this.setAlwaysOnTop(false);
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

            try {
                deletePatScheduleFromJobFile();
            } catch (IOException ex) {
                Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(Level.SEVERE, null, ex);
            }

            patrollingScheduleTableSetUp();

            fieldAction(false);
            updateBTN.setEnabled(false);
            removeSlotBTN.setEnabled(false);
            removeEmpBTN.setEnabled(false);
            cleanField();
            
            assignedJob = null;
            patrolling = null;
        }
    }//GEN-LAST:event_removeSlotBTNActionPerformed

    private void addSlotBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSlotBTNActionPerformed
        // TODO add your handling code here:
        ScheduleActionPage page = new ScheduleActionPage(BE, inputDate, patrollingScheduleFile);
        page.setVisible(true);
    }//GEN-LAST:event_addSlotBTNActionPerformed

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
            Logger.getLogger(BuildingExecutiveMainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jPanel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseClicked
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            BE.toPatrollingManagement(this, BE, null);
        } catch (IOException ex) {
            Logger.getLogger(BuildingExecutiveMainPage.class.getName()).log(Level.SEVERE, null, ex);
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

    private void logoutLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutLabel1MouseClicked
        // TODO add your handling code here:
        this.dispose();
        new HomePage().setVisible(true);
    }//GEN-LAST:event_logoutLabel1MouseClicked

    private void logoutLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutLabel1MouseEntered
        // TODO add your handling code here:
        logoutLabel1.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_logoutLabel1MouseEntered

    private void logoutPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPanel1MouseClicked
        // TODO add your handling code here:
        this.dispose();
        new HomePage().setVisible(true);
    }//GEN-LAST:event_logoutPanel1MouseClicked

    private void logoutPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPanel1MouseEntered
        // TODO add your handling code here:
        logoutPanel1.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_logoutPanel1MouseEntered

    private void checkpointComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkpointComboBoxActionPerformed
        // TODO add your handling code here:
        updateBTN.setEnabled(true);
    }//GEN-LAST:event_checkpointComboBoxActionPerformed
    
    // checkpoint combobox data
    private void checkpointComboBoxSetUp(String patrolLevel) {
        checkpointComboBox.removeAllItems();
        
        String levelRange = patrolLevel.replace("Level ", "");
        String[] eachLevel = levelRange.split(BE.TF.empty);
        int firstLv = Integer.parseInt(eachLevel[0]);
        int secLv = Integer.parseInt(eachLevel[1]);
        
        for (int countLv = firstLv; countLv < secLv + 1; countLv++) {
            checkpointComboBox.addItem("Level " + countLv);
        }
    }
    
    // delete patrolling in job file
    private void deletePatScheduleFromJobFile() throws IOException {
        assignedJob.updateJobTextFile(assignedJob.deleteItem);
        
        removeEmpBTN.setEnabled(false);
        updateBTN.setEnabled(false);
        fieldAction(false);
    }
    
    // update patrolling in job file
    private void updatePatShceduleFromJobFile() throws IOException {
        if (assignedJob != null) {
            assignedJob.setTaskEmpID(patrolling.getPatSecID());
            assignedJob.setRemarks(patrolling.getPatRemarks());
            assignedJob.setUpdateBy(patrolling.getUpdatedBy());
            assignedJob.setUpdatedTime(patrolling.getLastUpdate());
            
            assignedJob.updateJobTextFile(assignedJob.updateItem);
        }
        else {
            String newTaskId = BE.getNewTaskId(BE.TF.employeeJobFile, 0);
            String expectedEndDateTime = String.valueOf(inputDate) + " " + patrolling.getCheckBefore();
            String patCode = String.valueOf(inputDate) + " " + patrolling.getPatID();
            String timeNeeded = String.valueOf(BE.DTF.formatTime(patrolling.getCheckBefore()).compareTo(BE.DTF.formatTime(patrolling.getSlot())));
            
            assignedJob = new AssignedJob(newTaskId, patrolling.getPatSecID(), BE.TF.empty, "PT", 0, 
                                          timeNeeded+"h", String.valueOf(inputDate), patrolling.getSlot(), 
                                          expectedEndDateTime, BE.TF.empty, patrolling.getPatRemarks(), 
                                          BE.getUserID(), BE.DTF.currentDateTime(), patCode);
            
            assignedJob.updateJobTextFile(assignedJob.addItem);
        }
    }
    
    // set up employee id combo box
    private void employeeIdComboBoxSetUp() throws IOException {
        ArrayList<Employee> employeeList = new ArrayList<>();
        ArrayList<String> canPatrollId = new ArrayList<>();

        List<String> getEmployeeList = BE.fh.fileRead(BE.TF.fullEmployeeList);
        
        boolean firstLine = true;
        for (String eachEmp : getEmployeeList) {
            if (!firstLine) {
                String employeeId = eachEmp.split(BE.TF.sp)[0];
                
                Employee emp = new Employee(employeeId);
                
                if (emp.getPositionCode().equals(emp.empCode[0])) {
                    employeeList.add(emp);
                }
            }
            
            firstLine = false;
        }
        
        firstLine = true;
        for (Employee eachEmp : employeeList) {
            if (!firstLine) {
                ArrayList<AssignedJob> jobList = eachEmp.getEmployeeJobList();

                boolean cannotPatroll = false;
                if (!jobList.isEmpty()) {
                    for (AssignedJob eachJob : jobList) {
                        String assignedJobCode = eachJob.getJobID();

                        if (!assignedJobCode.equals("NS") && !assignedJobCode.equals("MS")) {
                            int repitition = eachJob.getRepetition();

                            String timeNeeded = eachJob.getExpectedTimeRequired();
                            LocalDate workingDate = null;
                            LocalTime workingTime = BE.DTF.formatTime(eachJob.getStartTime());
                            String[] workingEndDateTime = eachJob.getExpectedEndDateTime().split(" ");

                            LocalTime workingStartTime2;
                            LocalTime workingEndTime2;

                            ArrayList<String> dateData = BE.compareJobDate(repitition, workingEndDateTime, eachJob, timeNeeded, dayOfToday, inputDate, workingTime, workingDate);

                            if (!dateData.isEmpty()) {
                                workingDate = BE.DTF.formatDate(dateData.get(0));
                                workingTime = BE.DTF.formatTime(dateData.get(1));

                                workingStartTime2 = (!dateData.get(2).equals("null")) ? BE.DTF.formatTime(dateData.get(2)) : null;
                                workingEndTime2 = (!dateData.get(3).equals("null")) ? BE.DTF.formatTime(dateData.get(3)) : null;

                                workingEndDateTime = dateData.get(4).split(" ");

                                if (workingDate != null) {
                                    LocalTime checkB = BE.DTF.formatTime(patrolling.getCheckBefore());
                                    LocalTime slot = BE.DTF.formatTime(patrolling.getSlot());
                                    
                                    int patTimeNeeded = checkB.compareTo(slot) * 60;
                                    
                                    cannotPatroll = BE.compareDateTime(inputDate, inputTime, patTimeNeeded, workingDate, workingTime, workingEndDateTime, workingStartTime2, workingEndTime2);
                                }
                            }

                            if (cannotPatroll) {
                                break;
                            }
                        }
                    }
                }
                
                if (!cannotPatroll && !canPatrollId.contains(eachEmp.getEmpID())) {
                    canPatrollId.add(eachEmp.getEmpID().toUpperCase());
                }
            }
            
            firstLine = false;
        }
        
        securityIdComboBox.removeAllItems();
        securityIdComboBox.addItem("Not Selected");
        
        for (String eachId : canPatrollId) {
            securityIdComboBox.addItem(eachId);
        }
    }
    
    // manage button enable or disable
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
                    new BuildingExecutivePatrollingManagement(null, null).setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(BuildingExecutivePatrollingManagement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BEdashboardInnerPanel;
    private javax.swing.JPanel BEdashboardOuterPanel;
    private javax.swing.JButton addSlotBTN;
    private javax.swing.JTextField blockTF;
    private javax.swing.JComboBox<String> checkpointComboBox;
    private javax.swing.JTextField contactNoTF;
    private javax.swing.JLabel dateLabel;
    private com.github.lgooddatepicker.components.DatePicker datePicker;
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
    private javax.swing.JPanel jPanel13;
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
    private javax.swing.JLabel logoutLabel1;
    private javax.swing.JPanel logoutPanel1;
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

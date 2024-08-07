/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package buildingExecutive;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import classes.FileHandling;

/**
 *
 * @author Winson
 */
public class PatrollingScheduleModification extends javax.swing.JFrame {
    private final BuildingExecutive BE;
    
    public static PatrollingScheduleModification patrollingSchedule;
    ArrayList<String> blockList;

    FileHandling fh = new FileHandling();
    DefaultTableModel scheduleTable;
    
    private String currentBEid;
    
    private String patrollingScheduleFile;
    private LocalDate inputDate;
    
    private String timeIntervalSet;
    private String levelIntervalSet;
    private String timeRequest;
    
    private String scheduleID;
    
    /**
     * Creates new form EmployeeJobAssignation
     * @param BE
     * @param file
     * @param inputDate
     */
    public PatrollingScheduleModification(BuildingExecutive BE, String file, LocalDate inputDate) {
        this.BE = BE;
        this.setCurrentBEid(BE.getUserID());
        this.setInputDate(inputDate);

        initComponents();
        runDefaultSetUp(file);
    }
    
    private void runDefaultSetUp(String file){
        scheduleTable = (DefaultTableModel) scheduleJT.getModel();
        setWindowIcon();
        
        fileSetUp(file);
        getIntervalSet();
        cleanField();
        
        setEachComboBox();
                
        setPatrollingReportTableDesign();
        
        patrollingSchedule = this;
    }
    
    // set up current patrolling file based on the selected date (default today date)
    private void fileSetUp(String file) {
        this.patrollingScheduleFile = file;
        List<String> currentFile = fh.fileRead(this.patrollingScheduleFile);
        
        if (!new File(BE.TF.tempFile).exists()) {
            fh.fileWrite(BE.TF.tempFile, false, currentFile);
        }
    }
    
    private void tableSetUp() {
        ArrayList<String> toTable = new ArrayList<>();
        List<String> fileDet = fh.fileRead(BE.TF.tempFile);
        boolean firstLine = true;
        for (String eachSche : fileDet) {
            if (!firstLine) {
                String[] sche = eachSche.split(BE.TF.sp);
                toTable.add(sche[1] + BE.TF.sp + sche[2] + BE.TF.sp + sche[3] + BE.TF.sp + sche[4] + BE.TF.sp + sche[5] + BE.TF.sp + "MODIFY" + BE.TF.sp);
            }
            
            firstLine = false;
        }
        BE.setTableRow(scheduleTable, toTable);
    }
    
    // get the set interval(time, level, time needed)
    private void getIntervalSet() {
        List<String> getSchedSetting = fh.fileRead(BE.TF.patScheduleModRec);
        for (String eachSet : getSchedSetting) {
            String getDate = eachSet.split(BE.TF.sp)[0];
            if (getDate.equals(inputDate.toString())) {
                timeIntervalSet = eachSet.split(BE.TF.sp)[1];
                levelIntervalSet = eachSet.split(BE.TF.sp)[2];
                timeRequest = eachSet.split(BE.TF.sp)[3];
            }
        }
    }
    
    // setup the available block combo box
    private void blockComboBoxSetUp() {
        blockTF.removeAllItems();
        blockList = BE.getAvailableBlock();
        
        for (String eachBlock : blockList) {
            blockTF.addItem(eachBlock);
        }
    }
    
    // setup the available level combo box
    private void levelComboBoxSetUp(String blockSelected) {
        levelTF.removeAllItems();
        
        if (blockSelected.equals("S")) {
            for (String eachLv : BE.lvS) {
                levelTF.addItem(eachLv);
            }
        }
        else {
            for (String eachLv : BE.resLv) {
                levelTF.addItem(eachLv);
            }
        }
    }
    
    // set up each combo box
    private void setEachComboBox() {
        hourComboBox.setSelectedItem(timeIntervalSet);
        levelIntervalComboBox.setSelectedItem(levelIntervalSet);
        timeNeededCB.setSelectedItem(timeRequest);
    }
    
    // check point combo box set up
    private void checkPointSetUp(String lvSelected) {
        checkPTF.removeAllItems();
        String[] splitLv = lvSelected.split(" ");
        String[] lvData = (splitLv[1].length()>1) ? splitLv[1].split("-") : null;
        
        if (lvData != null) {
            int firstLv = Integer.valueOf(lvData[0]);
            int secLv = Integer.valueOf(lvData[1]);
            
            int lvDiff = secLv - firstLv + 1;
            
            for (int loop = 1; loop < lvDiff + 1; loop++) {
                checkPTF.addItem("Level " + String.valueOf(firstLv));
                firstLv++;
            }
        }
        else {
            checkPTF.addItem(lvSelected);
        }
    }
    
    // set up the form based on selected item
    private void formSetUp() {
        List<String> getRec = fh.fileRead(BE.TF.tempFile);
        for (String eachRec : getRec) {
            String[] recDet = eachRec.split(BE.TF.sp);
            String recID = recDet[0];
            if (recID.equals(scheduleID)) {
                String slot = recDet[1];
                String block = recDet[2];
                String level = recDet[3];
                String checkpoints = recDet[4];
                String checkBef = recDet[5];
                int timeDiff = BE.DTF.formatTime(checkBef).compareTo(BE.DTF.formatTime(slot));
                
                slotTimePicker.setTime(BE.DTF.formatTime(slot));
                endTimeTF.setText(checkBef);
                timeSpinner.setSelectedItem(timeDiff);
                blockTF.setSelectedItem(block);
                levelTF.setSelectedItem(level);
                checkPTF.setSelectedItem(checkpoints);
            }
        }
    }
    
    private void fieldEnabled(boolean enabled) {
        slotTimePicker.setEnabled(enabled);
        blockTF.setEnabled(enabled);
        timeSpinner.setEnabled(enabled);
    }
    
    private String[] getEachField() {
        String slot = slotTimePicker.getTime().toString();
        String block = blockTF.getSelectedItem().toString();
        String level = levelTF.getSelectedItem().toString();
        String checkPoint = checkPTF.getSelectedItem().toString();
        String timeNeeded = timeSpinner.getSelectedItem().toString();
        String endTime = endTimeTF.getText();
        
        String[] fieldInfo = {slot, block, level, checkPoint, timeNeeded, endTime};
        return fieldInfo;
    }
    
    private void cleanField() {
        slotTimePicker.setText("");
        blockTF.removeAllItems();
        levelTF.removeAllItems();
        checkPTF.removeAllItems();
        timeSpinner.setSelectedIndex(0);
        endTimeTF.setText("");
        
        slotTimePicker.setEnabled(false);
        blockTF.setEnabled(false);
        levelTF.setEnabled(false);
        checkPTF.setEnabled(false);
        timeSpinner.setEnabled(false);
    }
    
    private void setWindowIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/windowIcon.png")));
    }
    
    private void setPatrollingReportTableDesign() {
        int[] columnIgnore = {};
        int[] columnLength = {130, 100, 157, 140, 130, 140};
        BE.setTableDesign(scheduleJT, jLabel2, columnLength, columnIgnore);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        addRowBTN = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        scheduleJT = new javax.swing.JTable()
        {
            @Override

            public Component prepareRenderer (TableCellRenderer renderer, int rowIndex, int columnIndex){
                Component componenet = super.prepareRenderer(renderer, rowIndex, columnIndex);

                Object value = getModel().getValueAt(rowIndex,columnIndex);

                if(columnIndex == 5){
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
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        defaultBTN = new javax.swing.JButton();
        backBTN = new javax.swing.JButton();
        saveBTN = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        hourComboBox = new javax.swing.JComboBox<>();
        levelIntervalComboBox = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        blockTF = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        levelTF = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        endTimeTF = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        checkPTF = new javax.swing.JComboBox<>();
        deleteBTN = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel30 = new javax.swing.JLabel();
        updateBTN = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        slotTimePicker = new com.github.lgooddatepicker.components.TimePicker();
        timeSpinner = new javax.swing.JComboBox<>();
        timeNeededCB = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PARKHILL RESIDENCE");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setText(" Schedule: ");
        jLabel24.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 51, 51));

        addRowBTN.setText("Add Row");
        addRowBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRowBTNActionPerformed(evt);
            }
        });

        jLabel26.setText(" Slot Gap By: ");
        jLabel26.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(51, 51, 51));

        scheduleJT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "SLOT", "BLOCK", "LEVEL", "CHECKPOINTS", "CHECK BEFORE", "ACTION"
            }
        ));
        scheduleJT.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        scheduleJT.setForeground(new java.awt.Color(51, 51, 51));
        scheduleJT.setIntercellSpacing(new java.awt.Dimension(1, 1));
        scheduleJT.setRowHeight(25);
        scheduleJT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scheduleJTMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(scheduleJT);

        jLabel27.setText("Hour(s) ");
        jLabel27.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(51, 51, 51));

        jLabel28.setText(" Level Interval By: ");
        jLabel28.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(51, 51, 51));

        jLabel29.setText("Level(s)");
        jLabel29.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(51, 51, 51));

        defaultBTN.setText("Reset to Default");
        defaultBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultBTNActionPerformed(evt);
            }
        });

        backBTN.setText("Back");
        backBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBTNActionPerformed(evt);
            }
        });

        saveBTN.setText("Save");
        saveBTN.setEnabled(false);
        saveBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBTNActionPerformed(evt);
            }
        });

        jLabel25.setText(" Schedule Modification: ");
        jLabel25.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(51, 51, 51));

        hourComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2", "4", "6", "8", "10", "12" }));
        hourComboBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hourComboBoxMouseClicked(evt);
            }
        });
        hourComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hourComboBoxActionPerformed(evt);
            }
        });

        levelIntervalComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "5", "10", "15" }));
        levelIntervalComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                levelIntervalComboBoxActionPerformed(evt);
            }
        });

        jLabel32.setText("Time Needed: ");
        jLabel32.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(51, 51, 51));

        jLabel33.setText("Hour(s) ");
        jLabel33.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(51, 51, 51));

        jLabel23.setText("Slot:");
        jLabel23.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 51, 51));

        jLabel16.setText("Block: ");
        jLabel16.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));

        blockTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blockTFActionPerformed(evt);
            }
        });

        jLabel18.setText("Level: ");
        jLabel18.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));

        levelTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                levelTFActionPerformed(evt);
            }
        });

        jLabel19.setText("Check Before: ");
        jLabel19.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 51, 51));

        endTimeTF.setEditable(false);
        endTimeTF.setEnabled(false);

        jLabel17.setText("Time Needed: ");
        jLabel17.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));

        jLabel14.setText("Checkpoint: ");
        jLabel14.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));

        deleteBTN.setText("Delete");
        deleteBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBTNActionPerformed(evt);
            }
        });

        jLabel30.setText("Hour(s) ");
        jLabel30.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(51, 51, 51));

        updateBTN.setText("Update");
        updateBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBTNActionPerformed(evt);
            }
        });

        jLabel31.setText(" Slot Modification: ");
        jLabel31.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(51, 51, 51));

        slotTimePicker.setEnabled(false);

        timeSpinner.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));
        timeSpinner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeSpinnerActionPerformed(evt);
            }
        });

        timeNeededCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));
        timeNeededCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeNeededCBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(addRowBTN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(defaultBTN))
                    .addComponent(jSeparator2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(levelIntervalComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeNeededCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(backBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saveBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(133, 133, 133)
                                .addComponent(deleteBTN))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(slotTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(blockTF, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(36, 36, 36)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(levelTF, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(checkPTF, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(timeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel30))
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(endTimeTF)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)))
                            .addComponent(updateBTN)))
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addRowBTN)
                    .addComponent(defaultBTN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(levelIntervalComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeNeededCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(blockTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(levelTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(checkPTF, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(timeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(slotTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(31, 31, 31))))
                            .addGap(1, 1, 1)))
                    .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(endTimeTF, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteBTN)
                    .addComponent(updateBTN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(13, 24, 42));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("PATROLLING SCHEDULE MANAGEMENT");
        jLabel2.setFont(new java.awt.Font("Britannic Bold", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/parkhillLogo.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(16, 16, 16))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel2)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void saveBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBTNActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(null,"Are you sure to "
                + "change the format of this patrolling schedule?", "PATROLLING SCHEDULE",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

        if(result == JOptionPane.YES_OPTION){
            String timeGap = hourComboBox.getSelectedItem().toString();
            String levelInterval = levelIntervalComboBox.getSelectedItem().toString();
            String timeNeeded = timeNeededCB.getSelectedItem().toString();
            
            String[] item = {inputDate.toString(), timeGap, levelInterval, timeNeeded, this.currentBEid, BE.DTF.currentDateTime()};
            
            String data = "";
            for (String eachItem : item) {
                data = data + eachItem + BE.TF.sp;
            }
            
            BE.crud.update(BE.TF.patScheduleModRec, this.inputDate.toString(), data, 0);
            
            File patFile = new File(patrollingScheduleFile);
            if (patFile.exists()) {
                patFile.delete();
            }

            File temp = new File(BE.TF.tempFile);
            temp.renameTo(patFile);

            ArrayList<String> removePat = new ArrayList<>();
            List<String> jobFile = fh.fileRead(BE.TF.employeeJobFile);
            boolean firstLine = true;
            for (String eachJob : jobFile) {
                if (!firstLine) {
                    String[] jobDet = eachJob.split(BE.TF.sp);
                    String patCode = jobDet[jobDet.length-1];
                    if (!patCode.equals(BE.TF.empty)) {
                        String[] patDate = patCode.split(" ");
                        if (!BE.DTF.formatDate(patDate[0]).equals(inputDate)) {
                            removePat.add(eachJob);
                        }
                    }
                    else {
                        removePat.add(eachJob);
                    }
                }
                else {
                    removePat.add(eachJob);
                }

                firstLine = false;
            }
            
            fh.fileWrite(BE.TF.employeeJobFile, false, removePat);

            if (BuildingExecutivePatrollingManagement.BEpatrollingManagement != null) {
                BuildingExecutivePatrollingManagement.BEpatrollingManagement.dispose();
            }
            
            try {
                BE.toPatrollingManagement(this, BE, inputDate);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(PatrollingScheduleModification.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            
            saveBTN.setEnabled(false);
        }
    }//GEN-LAST:event_saveBTNActionPerformed

    private void hourComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hourComboBoxActionPerformed
        // TODO add your handling code here:
        timeIntervalSet = (hourComboBox.getSelectedItem() != null) ? hourComboBox.getSelectedItem().toString() : null;
        if (timeIntervalSet != null) {
            BE.tableSettingUpdate(this.timeIntervalSet, this.levelIntervalSet, Integer.valueOf(this.timeRequest), false);
            tableSetUp();
        }
        saveBTN.setEnabled(true);
    }//GEN-LAST:event_hourComboBoxActionPerformed

    private void defaultBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultBTNActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(null,"Are you sure to "
                + "reset the schedule to default?", "PATROLLING SCHEDULE",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

        if(result == JOptionPane.YES_OPTION){
            List<String> defaultSetting = fh.fileRead(BE.TF.fixFile);
            fh.fileWrite(BE.TF.tempFile, false, defaultSetting);
            tableSetUp();
        }
    }//GEN-LAST:event_defaultBTNActionPerformed

    private void levelIntervalComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_levelIntervalComboBoxActionPerformed
        // TODO add your handling code here:
        levelIntervalSet = (levelIntervalComboBox.getSelectedItem() != null) ? levelIntervalComboBox.getSelectedItem().toString() : null;
        if (levelIntervalSet != null) {
            BE.tableSettingUpdate(this.timeIntervalSet, this.levelIntervalSet, Integer.valueOf(this.timeRequest), false);
            tableSetUp();
        }
        saveBTN.setEnabled(true);
    }//GEN-LAST:event_levelIntervalComboBoxActionPerformed
    
    private void scheduleJTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scheduleJTMouseClicked
        // TODO add your handling code here:
        int selectedRow = scheduleJT.getSelectedRow();
        int selectedCol = scheduleJT.getSelectedColumn();
        
        if (selectedCol == 5) {
            slotTimePicker.setEnabled(false);
            deleteBTN.setEnabled(true);
            updateBTN.setEnabled(true);
            checkPTF.setEnabled(true);
            timeSpinner.setEnabled(true);
            
            List<String> getSchedule = fh.fileRead(BE.TF.tempFile);
            scheduleID = getSchedule.get(selectedRow+1).split(BE.TF.sp)[0];
            
            blockComboBoxSetUp();
            formSetUp();
            
            blockTF.setEnabled(false);
            levelTF.setEnabled(false);
        }
    }//GEN-LAST:event_scheduleJTMouseClicked

    private void addRowBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRowBTNActionPerformed
        // TODO add your handling code here:
        scheduleID = BE.getNewId(BE.TF.tempFile, 0);
        deleteBTN.setEnabled(false);
        updateBTN.setEnabled(true);
        
        slotTimePicker.setEnabled(true);
        timeSpinner.setEnabled(true);
        
        fieldEnabled(true);
        levelTF.setEnabled(false);
        checkPTF.setEnabled(false);
        
        saveBTN.setEnabled(true);
        
        blockComboBoxSetUp();
    }//GEN-LAST:event_addRowBTNActionPerformed

    private void blockTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blockTFActionPerformed
        // TODO add your handling code here:
        String item = (blockTF.getSelectedItem() != null) ? (blockTF.getSelectedItem().toString()) : null;
        if (item!=null) {
            levelComboBoxSetUp(blockTF.getSelectedItem().toString());
        }
        
        levelTF.setEnabled(true);
    }//GEN-LAST:event_blockTFActionPerformed

    private void levelTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_levelTFActionPerformed
        // TODO add your handling code here:
        if (levelTF.getSelectedItem()!=null) {
            checkPointSetUp(levelTF.getSelectedItem().toString());
            checkPTF.setEnabled(true);
        }
        else {
            levelTF.setEnabled(false);
            checkPTF.setEnabled(false);
        }
    }//GEN-LAST:event_levelTFActionPerformed

    private void deleteBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBTNActionPerformed
        // TODO add your handling code here:
        ArrayList<String> updatedList = new ArrayList<>();

        List<String> scheduleFile = fh.fileRead(BE.TF.tempFile);
        for (String eachLine : scheduleFile) {
            String id = eachLine.split(BE.TF.sp)[0];
            if (!id.equals(scheduleID)) {
                updatedList.add(eachLine);
            }
        }
        
        fh.fileWrite(BE.TF.tempFile, false, updatedList);
        
        tableSetUp();
        cleanField();
        deleteBTN.setEnabled(false);
        updateBTN.setEnabled(false);
    }//GEN-LAST:event_deleteBTNActionPerformed

    private void updateBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBTNActionPerformed
        // TODO add your handling code here:
        String[] eachField = getEachField();
        
        ArrayList<String> updatedList = new ArrayList<>();
        ArrayList<String> code = new ArrayList<>();

        List<String> scheduleFile = fh.fileRead(BE.TF.tempFile);
        for (String eachLine : scheduleFile) {
            String id = eachLine.split(BE.TF.sp)[0];
            code.add(id);
            if (id.equals(scheduleID)) {
                updatedList.add(eachLine.split(BE.TF.sp)[0] + BE.TF.sp + 
                                eachLine.split(BE.TF.sp)[1] + BE.TF.sp +
                                eachLine.split(BE.TF.sp)[2] + BE.TF.sp +
                                eachLine.split(BE.TF.sp)[3] + BE.TF.sp +
                                eachField[3] + BE.TF.sp + eachField[5] + BE.TF.sp +
                                BE.TF.empty + BE.TF.sp + BE.TF.empty + BE.TF.sp + BE.TF.empty +
                                BE.TF.sp + "Unassign" + BE.TF.sp + BE.TF.empty + BE.TF.sp + 
                                BE.TF.empty + BE.TF.sp + BE.TF.empty + BE.TF.sp);
            }
            else {
                updatedList.add(eachLine);
            }
        }
        
        if (!code.contains(scheduleID)){
            updatedList.add(scheduleID + BE.TF.sp + eachField[0] + BE.TF.sp +
                            eachField[1] + BE.TF.sp + eachField[2] + BE.TF.sp +
                            eachField[3] + BE.TF.sp + eachField[5] + BE.TF.sp +
                            BE.TF.empty + BE.TF.sp + BE.TF.empty + BE.TF.sp + BE.TF.empty +
                            BE.TF.sp + "Unassign" + BE.TF.sp + BE.TF.empty + BE.TF.sp + 
                            BE.TF.empty + BE.TF.sp + BE.TF.empty + BE.TF.sp);
        }
        
        fh.fileWrite(BE.TF.tempFile, false, updatedList);
        
        tableSetUp();
        cleanField();
        updateBTN.setEnabled(false);
        deleteBTN.setEnabled(false);
    }//GEN-LAST:event_updateBTNActionPerformed

    private void timeSpinnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeSpinnerActionPerformed
        // TODO add your handling code here:
        int selHour = Integer.valueOf(timeSpinner.getSelectedItem().toString());
        
        if (slotTimePicker.getTime()!= null) {
            endTimeTF.setText(slotTimePicker.getTime().plusHours(selHour).toString());
        }
    }//GEN-LAST:event_timeSpinnerActionPerformed

    private void hourComboBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hourComboBoxMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_hourComboBoxMouseClicked

    private void backBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBTNActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.YES_OPTION;
        
        if (saveBTN.isEnabled()) {
            result = JOptionPane.showConfirmDialog(null,"Your changes are not saved. Are you sure to"
                + " discard the changes?", "PATROLLING SCHEDULE",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        }

        if(result == JOptionPane.YES_OPTION){
            if (new File(BE.TF.tempFile).exists()) {
                new File(BE.TF.tempFile).delete();
            }

            if (BuildingExecutivePatrollingManagement.BEpatrollingManagement != null) {
                BuildingExecutivePatrollingManagement.BEpatrollingManagement.dispose();
            }
            
            try {
                BE.toPatrollingManagement(this, BE, inputDate);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(PatrollingScheduleModification.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_backBTNActionPerformed

    private void timeNeededCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeNeededCBActionPerformed
        // TODO add your handling code here:
        timeRequest = (timeNeededCB.getSelectedItem() != null) ? timeNeededCB.getSelectedItem().toString() : null;
        if (timeRequest != null) {
            BE.tableSettingUpdate(this.timeIntervalSet, this.levelIntervalSet, Integer.valueOf(this.timeRequest), false);
            tableSetUp();
        }
    }//GEN-LAST:event_timeNeededCBActionPerformed

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
            java.util.logging.Logger.getLogger(PatrollingScheduleModification.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PatrollingScheduleModification.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PatrollingScheduleModification.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PatrollingScheduleModification.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PatrollingScheduleModification(null, null, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addRowBTN;
    private javax.swing.JButton backBTN;
    private javax.swing.JComboBox<String> blockTF;
    private javax.swing.JComboBox<String> checkPTF;
    private javax.swing.JButton defaultBTN;
    private javax.swing.JButton deleteBTN;
    private javax.swing.JTextField endTimeTF;
    private javax.swing.JComboBox<String> hourComboBox;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JComboBox<String> levelIntervalComboBox;
    private javax.swing.JComboBox<String> levelTF;
    private javax.swing.JButton saveBTN;
    private javax.swing.JTable scheduleJT;
    private com.github.lgooddatepicker.components.TimePicker slotTimePicker;
    private javax.swing.JComboBox<String> timeNeededCB;
    private javax.swing.JComboBox<String> timeSpinner;
    private javax.swing.JButton updateBTN;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the inputDate
     */
    public LocalDate getInputDate() {
        return inputDate;
    }

    /**
     * @param inputDate the inputDate to set
     */
    public void setInputDate(LocalDate inputDate) {
        this.inputDate = inputDate;
    }

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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package buildingExecutive;

import pms_parkhill_residence.CRUD;
import java.awt.Toolkit;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import pms_parkhill_residence.Complaints;
import pms_parkhill_residence.FileHandling;
import pms_parkhill_residence.Users;

/**
 *
 * @author Winson
 */
public class JobModificationPage extends javax.swing.JFrame {
    private Users user;
    private String currentBEid;
    private Complaints complaint;
    
    DefaultTableModel jobTable;
    BuildingExecutive BE = new BuildingExecutive();
    FileHandling fileHandling = new FileHandling();
    CRUD crud = new CRUD();
    
    private String selectedId;
    
    private String jobId;
    private String complaintId;
    private String employeeId;
    private String positionCode;
    
    /**
     * Creates new form EmployeeJobAssignation
     * @param user
     * @param positionCode employee position code
     * @param jobID
     * @param complaint
     * @param employeeID
     * @throws java.io.IOException
     */
    public JobModificationPage(Users user, String positionCode, String jobID, Complaints complaint, String employeeID) throws IOException {
        initComponents();
        jobTable = (DefaultTableModel) jobsTableUI.getModel();
        runDefaultSetUp(user, positionCode, jobID, complaint, employeeID);
    }
    
    private void runDefaultSetUp(Users user, String positionCode, String jobID, Complaints complaint, String employeeID) throws IOException {
        this.user = user;
        this.setCurrentBEid(this.user.getUserID());
        
        this.setJobId(jobID);
        this.setComplaintId(complaint.getComplaintID());
        this.setEmployeeId(employeeID);
        this.setPositionCode(positionCode);
        
        setWindowIcon();
        comboBoxSetUp(this.positionCode);
        tableJobSetUp(this.positionCode);
        
        deleteBTN.setEnabled(false);
        updateBTN.setEnabled(false);
        
        jobTF.setText("");
    }
    
    private void tableJobSetUp(String positionCode) throws IOException {
        List<String> readJobLists = fileHandling.fileRead(BE.TF.jobListFile);
        ArrayList<String> jobList = new ArrayList<>();
        
        int numberOfItem = 1;
        for (String readLine : readJobLists) {
            String[] jobDetails = readLine.split(BE.TF.sp);
            String roleCode = jobDetails[0];
            
            if (roleCode.equals(positionCode)) {
                jobList.add(numberOfItem + BE.TF.sp + jobDetails[2] + BE.TF.sp + jobDetails[3] + BE.TF.sp + jobDetails[4]);
                numberOfItem++;
            }
        }
        
        BE.setTableRow(jobTable, jobList);
    }
    
    private void setWindowIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/windowIcon.png")));
    }
    
    private void comboBoxSetUp(String positionCode) {
        String employeeRoleName = BE.showEmployeeFullRoleName(positionCode);
        employeeRoleComboBox.setSelectedItem(employeeRoleName);
    }
    
    private String getAllField(boolean add) {
        ArrayList<String> dataField = new ArrayList<>();
        String toLine = "";
        
        dataField.add(this.positionCode);
        
        if (add) {
            dataField.add(BE.getNewId(BE.TF.jobListFile, 1));
        }
        else {
            dataField.add(this.selectedId);
        }
        
        if (jobTF.getText() != null) {
            if (!jobTitleExist(jobTF.getText())) {
                dataField.add(jobTF.getText());
                
                if (Integer.valueOf(timeNeededSpinner.getValue().toString()) != 0 && timeValueCB.getSelectedItem()!=null) {
                    String timeValue = (timeValueCB.getSelectedItem().toString().equals("hrs")) ? "h" : "m";
                    
                    dataField.add(timeNeededSpinner.getValue().toString()+timeValue);

                    if (startTimePicker.getTime()!=null) {
                        dataField.add(BE.formatTime(startTimePicker.getTime().toString()).toString());

                        for (String eachData :  dataField) {
                            toLine = toLine + eachData + BE.TF.sp;
                        }
                        
                        return toLine;
                    }
                }
            }
        }
        
        return null;
    }
    
    private boolean jobTitleExist(String jobTitle) {
        List<String> jobFile = fileHandling.fileRead(BE.TF.jobListFile);
        for (String eachJob : jobFile) {
            String title = eachJob.split(BE.TF.sp)[2];
            if (title.equals(jobTitle)) {
                return true;
            }
        }
        
        return false;
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
        jLabel16 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jobsTableUI = new javax.swing.JTable();
        employeeRoleComboBox = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        jobTF = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        timeNeededSpinner = new javax.swing.JSpinner();
        jLabel19 = new javax.swing.JLabel();
        startTimePicker = new com.github.lgooddatepicker.components.TimePicker();
        addBTN = new javax.swing.JButton();
        updateBTN = new javax.swing.JButton();
        deleteBTN = new javax.swing.JButton();
        clearBTN = new javax.swing.JButton();
        timeValueCB = new javax.swing.JComboBox<>();
        backBTN = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("PARKHILL RESIDENCE");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setText(" Job Lists: ");
        jLabel16.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));

        jobsTableUI.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No.", "Task", "Time Required", "Start Time", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jobsTableUI.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jobsTableUIMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jobsTableUI);
        if (jobsTableUI.getColumnModel().getColumnCount() > 0) {
            jobsTableUI.getColumnModel().getColumn(0).setResizable(false);
            jobsTableUI.getColumnModel().getColumn(1).setResizable(false);
            jobsTableUI.getColumnModel().getColumn(2).setResizable(false);
            jobsTableUI.getColumnModel().getColumn(3).setResizable(false);
            jobsTableUI.getColumnModel().getColumn(4).setResizable(false);
        }

        employeeRoleComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Technician", "Cleaner", "Security Guard" }));
        employeeRoleComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeRoleComboBoxActionPerformed(evt);
            }
        });

        jLabel17.setText(" Job Title: ");
        jLabel17.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));

        jLabel18.setText("Time Needed: ");
        jLabel18.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));

        jLabel19.setText("Start Time: ");
        jLabel19.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 51, 51));

        addBTN.setText("Add");
        addBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBTNActionPerformed(evt);
            }
        });

        updateBTN.setText("Update");
        updateBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBTNActionPerformed(evt);
            }
        });

        deleteBTN.setText("Delete");
        deleteBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBTNActionPerformed(evt);
            }
        });

        clearBTN.setText("Clear");
        clearBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBTNActionPerformed(evt);
            }
        });

        timeValueCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "hrs", "mins" }));
        timeValueCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeValueCBActionPerformed(evt);
            }
        });

        backBTN.setText("Back");
        backBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(employeeRoleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jobTF, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(timeNeededSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(timeValueCB, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(startTimePicker, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(backBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(clearBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(deleteBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(updateBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(addBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(employeeRoleComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(startTimePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jobTF, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(timeNeededSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addComponent(timeValueCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(13, 24, 42));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("JOB MODIFICATION");
        jLabel2.setFont(new java.awt.Font("Britannic Bold", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/parkhillLogo.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(154, 154, 154)
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

    private void employeeRoleComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeRoleComboBoxActionPerformed
        // TODO add your handling code here:
        String selectedRole = (String) employeeRoleComboBox.getSelectedItem();
        try {
            String roleCode = BE.getEmployeePositionCode(null, selectedRole);
            tableJobSetUp(roleCode);
        } catch (IOException ex) {
            
            Logger.getLogger(JobModificationPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_employeeRoleComboBoxActionPerformed

    private void jobsTableUIMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jobsTableUIMouseClicked
        // TODO add your handling code here:
        int selectedCol = jobsTableUI.getSelectedColumn();
        int selectedRow = jobsTableUI.getSelectedRow();
        
        String jobDesc = BE.validateTableSelectionAndGetValue(jobTable, selectedCol, selectedRow, 4, 1);
        
        List<String> readJobList = fileHandling.fileRead(BE.TF.jobListFile);
        for (String jobLine : readJobList) {
            String[] jobDetails = jobLine.split(BE.TF.sp);
            String jobTitle = jobDetails[2];
            String jobID = jobDetails[1];
            
            if (jobTitle.equals(jobDesc)) {
                this.setSelectedId(jobID);
                jobTF.setText(jobTitle);
                
                String timeNeededValue = jobDetails[3];
                String timeValue = timeNeededValue.substring(timeNeededValue.length() - 1);
                
                timeValue = (timeValue.equals("h")) ? "hrs" : "mins";
                timeValueCB.setSelectedItem(timeValue);
                
                String timeNeeded = "0";
                if (!timeNeededValue.equals("0")) {
                    timeNeeded = timeNeededValue.substring(0, timeNeededValue.length() - 1);
                }
                
                timeNeededSpinner.setValue(Integer.valueOf(timeNeeded));
                
                String startTime = jobDetails[4];
                if (!startTime.equals(" ")) {
                    LocalTime jobStartTime = BE.getTimeCategory(BE.formatTime(startTime));
                    startTimePicker.setTime(jobStartTime);
                    startTimePicker.setEnabled(true);
                    timeNeededSpinner.setEnabled(true);
                    timeValueCB.setEnabled(true);
                    
                    addBTN.setEnabled(false);
                    deleteBTN.setEnabled(true);
                    updateBTN.setEnabled(true);
                }
                else {
                    startTimePicker.setTime(null);
                    startTimePicker.setEnabled(false);
                    timeNeededSpinner.setEnabled(false);    
                    timeValueCB.setEnabled(false);
                    jobTF.setEnabled(false);
                    
                    addBTN.setEnabled(false);
                    deleteBTN.setEnabled(false);
                    updateBTN.setEnabled(false);
                }
            }
        }
    }//GEN-LAST:event_jobsTableUIMouseClicked

    private void clearBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBTNActionPerformed
        // TODO add your handling code here:
        clearField();
    }//GEN-LAST:event_clearBTNActionPerformed

    private void deleteBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBTNActionPerformed
        // TODO add your handling code here:
//        ArrayList<String> removedItem = new ArrayList<>();
//
//        List<String> oldVer = fileHandling.fileRead(BE.TF.jobListFile);
//        for (String eachJob : oldVer) {
//            String taskId = eachJob.split(BE.TF.sp)[1];
//            if (!taskId.equals(this.selectedId)) {
//                removedItem.add(eachJob);
//            }
//        }
//        
//        fileHandling.fileWrite(BE.TF.jobListFile, false, removedItem);

        crud.delete(BE.TF.jobListFile, this.selectedId, 1);
        
        clearField();
        
        try {
            tableJobSetUp(positionCode);
        } catch (IOException ex) {
            Logger.getLogger(JobModificationPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_deleteBTNActionPerformed

    private void updateBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBTNActionPerformed
        // TODO add your handling code here:
//        ArrayList<String> itemUpdate = new ArrayList<>();
        
//        String[] eachData = dataLine.split(BE.TF.sp);
//        
//        List<String> oldVer = fileHandling.fileRead(BE.TF.jobListFile);
//        for (String eachJob : oldVer) {
//            String taskId = eachJob.split(BE.TF.sp)[1];
//            if (taskId.equals(eachData[1])) {
//                itemUpdate.add(dataLine);
//            }
//            else {
//                itemUpdate.add(eachJob);
//            }
//        }
//        
//        fileHandling.fileWrite(BE.TF.jobListFile, false, itemUpdate);

        String dataLine = this.getAllField(false);

        crud.update(BE.TF.jobListFile, this.selectedId, dataLine, 1);
        
        clearField();
        
        try {
            tableJobSetUp(positionCode);
        } catch (IOException ex) {
            Logger.getLogger(JobModificationPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_updateBTNActionPerformed

    private void addBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBTNActionPerformed
        // TODO add your handling code here:
        String dataLine = this.getAllField(true);
        
        List<String> addItem = new ArrayList<>();
        addItem.add(dataLine);
        
        crud.create(BE.TF.jobListFile, addItem);
        clearField();
        
        try {
            tableJobSetUp(positionCode);
        } catch (IOException ex) {
            Logger.getLogger(JobModificationPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addBTNActionPerformed

    private void timeValueCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeValueCBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_timeValueCBActionPerformed

    private void backBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBTNActionPerformed
        // TODO add your handling code here:
        if (EmployeeJobAssignation.employeeJobAssignation != null) {
            EmployeeJobAssignation.employeeJobAssignation.dispose();
        }
        
        BE.toEmployeeJobAssignation(this.user, this.employeeId, this.jobId, this.complaint, false);
        this.dispose();
    }//GEN-LAST:event_backBTNActionPerformed
    
    private void clearField() {
        addBTN.setEnabled(true);
        jobTF.setEnabled(true);
        timeNeededSpinner.setEnabled(true);
        startTimePicker.setEnabled(true);
        timeValueCB.setEnabled(true);
        deleteBTN.setEnabled(false);
        updateBTN.setEnabled(false);
        
        jobTF.setText("");
        timeNeededSpinner.setValue(0);
        timeValueCB.setSelectedIndex(0);
        startTimePicker.setTime(null);
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
            java.util.logging.Logger.getLogger(JobModificationPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JobModificationPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JobModificationPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JobModificationPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new JobModificationPage(null, null, null, null, null).setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(JobModificationPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBTN;
    private javax.swing.JButton backBTN;
    private javax.swing.JButton clearBTN;
    private javax.swing.JButton deleteBTN;
    private javax.swing.JComboBox<String> employeeRoleComboBox;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jobTF;
    private javax.swing.JTable jobsTableUI;
    private com.github.lgooddatepicker.components.TimePicker startTimePicker;
    private javax.swing.JSpinner timeNeededSpinner;
    private javax.swing.JComboBox<String> timeValueCB;
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

    /**
     * @return the jobId
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * @param jobId the jobId to set
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * @return the complaintId
     */
    public String getComplaintId() {
        return complaintId;
    }

    /**
     * @param complaintId the complaintId to set
     */
    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    /**
     * @return the employeeId
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the selectedId
     */
    public String getSelectedId() {
        return selectedId;
    }

    /**
     * @param selectedId the selectedId to set
     */
    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    /**
     * @return the positionCode
     */
    public String getPositionCode() {
        return positionCode;
    }

    /**
     * @param positionCode the positionCode to set
     */
    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

}

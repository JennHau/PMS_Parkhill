/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package buildingExecutive;

import buildingExecutive.BuildingExecutive;
import java.awt.Toolkit;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import pms_parkhill_residence.FileHandling;

/**
 *
 * @author Winson
 */
public class EmployeeJobAssignation extends javax.swing.JFrame {
    BuildingExecutive BE = new BuildingExecutive();
    FileHandling fileHandling = new FileHandling();
    
    // Remove unnecessary data
    private String jobID;
    private String currentBEid;
    private String selectedEmployeeId;
    private String selectedEmployeePosCode;
    private String complaintsId;
    private boolean fromComplaintsPage = false;
    private boolean isSame = true;
    
    private final int deleteItem = 0;    
    private final int updateItem = 1;
    private final int addItem = 2;
    
    private DefaultTableModel employeeJobTable;
    /**
     * Creates new form EmployeeJobAssignation
     * @param userID
     * @param employeeID
     * @param jobId
     * @param complaintId
     * @param complaintsPage
     * @throws java.io.IOException
     */
    public EmployeeJobAssignation(String userID, String employeeID, String jobId, String complaintId, boolean complaintsPage) throws IOException {
        initComponents();
        runDefaultSetUp(userID, employeeID, jobId, complaintId, complaintsPage);
    }

    private void runDefaultSetUp(String userID, String employeeID, String jobId, String complaintId, boolean complaintsPage) throws IOException {
        employeeJobTable = (DefaultTableModel) assignedJobTable.getModel();
        
        setWindowIcon();
        setSelectedEmployee(employeeID, jobId);
        setCurrentBE(userID);
        setComplaintsId(complaintId);
        setFromComplaintsPage(complaintsPage);
        jobComboBoxSetUp();
        setJobFormTable();
    }
    
    private void setJobFormTable() throws IOException {
        formInitialSetUp();
        formAdditionalDetailsSetUp();
        setJobTable();
    }

    private void setWindowIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/windowIcon.png")));
    }

    private void setSelectedEmployee(String employeeID, String jobId) throws IOException {
        this.selectedEmployeeId = employeeID;
        this.jobID = jobId;
        this.selectedEmployeePosCode = BE.getEmployeePositionCode(employeeID, null);
    }

    private void setCurrentBE(String beID) {
        this.currentBEid = beID;
    }

    // employeeDetails[userID, email, fullName, phoneNo, position]
    private void formInitialSetUp() throws IOException {
        String[] employeeDetails = BE.getEmployeeDetails(selectedEmployeeId);
        
        if (employeeDetails != null) {
            String emplyName = employeeDetails[2];
            String emplyPhoneNo = employeeDetails[3];
            String emplyPos = employeeDetails[4];

            employeeIdTF.setText(this.selectedEmployeeId);
            employeeNameTF.setText(emplyName);
            contNoTF.setText(emplyPhoneNo);
            posTF.setText(emplyPos);
        }
        
        assignedJobTable.setEnabled(!fromComplaintsPage);
    }
    
    private void formAdditionalDetailsSetUp() throws IOException {
        if (jobID != null) {
            String[] jobDetails = BE.getSpecificJobDetails(selectedEmployeeId, jobID);
            
            this.complaintsId = (jobDetails[2].equals("null")) ? null : jobDetails[2];
            
            String jobToAssign = jobDetails[3];
            int repitition = Integer.valueOf(jobDetails[4]);
            String timeNeeded = jobDetails[5];
            LocalTime startTime = BE.formatTime(jobDetails[7]);
            String dayToRepeat = jobDetails[9];
            String remarks = jobDetails[10];

            String timePlace = timeNeeded.substring(timeNeeded.length()-1);
            timeNeeded = timeNeeded.substring(0, timeNeeded.length()-1);
            
            if (timePlace.equals("m")) {
                hrsMinsComboBox.setSelectedItem("mins");
            }
            else {
                hrsMinsComboBox.setSelectedItem("hrs");
            }
            
            jobComboBox.setSelectedItem(jobToAssign);
            timeNeededSpinner.setValue(Integer.valueOf(timeNeeded));
            dateTimePicker1.timePicker.setTime(startTime);
            remarksTA.setText(remarks);
            
            if (repitition == BE.repititionON) {
                repititionCheckBox.setSelected(true);
                dateTimePicker1.datePicker.clear();
                dateTimePicker1.datePicker.setEnabled(false);
                dayCheckBoxAction(true, false);
                String[] days = dayToRepeat.split(",");
                dayCheckBox(days);
            }
            else if (repitition == BE.repititionOFF) {
                repititionCheckBox.setSelected(false);
                LocalDate startDate = BE.formatDate(jobDetails[6]);
                dateTimePicker1.datePicker.setDate(startDate);
                dateTimePicker1.datePicker.setEnabled(true);
                dayCheckBoxAction(false, false);
            }
            
            btnAction(false, true, true);
        }
        else {
            jobComboBox.setSelectedIndex(0);
            repititionCheckBox.setSelected(false);
            timeNeededSpinner.setValue(0);
            hrsMinsComboBox.setSelectedIndex(0);
            dateTimePicker1.datePicker.setDateToToday();
            dateTimePicker1.datePicker.setEnabled(true);
            LocalTime timeNow = BE.getTimeCategory(LocalTime.now());
            dateTimePicker1.timePicker.setTime(timeNow);
            remarksTA.setText("");
            dayCheckBoxAction(false, false);
            btnAction(true, false, false);
        }
        
        if (getComplaintsId() != null) {
            jobComboBox.setSelectedItem("Complaints");
            complaintIdTF.setText(getComplaintsId());
            repititionCheckBox.setEnabled(false);
            jobComboBox.setEnabled(false);
        }
        else {
            repititionCheckBox.setEnabled(true);
        }
    }
    
    private void setJobTable() throws IOException {
        ArrayList<String> jobList = BE.getAssignedJobForSpecificEmployee(selectedEmployeeId);
        ArrayList<String> listForTable = new ArrayList<>();
        
        String combineDate;
        
        for (String eachJob : jobList) {
            String[] jobDetails = eachJob.split(BE.sp);
            String jobId = jobDetails[0];
            String jobDesc = jobDetails[3];
            int repitition = Integer.valueOf(jobDetails[4]);
            String startDate = jobDetails[6];
            String startTime = jobDetails[7];
            
            String[] endDateTime = jobDetails[8].split(" ");
            
            LocalDateTime workingEndDateTime = null;
            if (!endDateTime[0].equals("null")) {
                workingEndDateTime = BE.combineStringDateTime(endDateTime[0], endDateTime[1]);
            }
            
            String dayToRepeat = jobDetails[9];
            String assigneeId = jobDetails[11];
            
            String eachDay = "";
            if (!dayToRepeat.equals("null")) {
                for (String day : dayToRepeat.split(",")) {
                    eachDay += day.toUpperCase()+ " ";
                }
            }
                     
            combineDate = startDate + " " + startTime;
            
            if (repitition == BE.repititionON) {
                combineDate = eachDay + startTime;
            }
            
            LocalDateTime dateTimeNow = LocalDateTime.now();
            
            boolean add = true;
            if ((workingEndDateTime != null && dayToRepeat.equals("null"))) {
                if (workingEndDateTime.isBefore(dateTimeNow)) {
                    add = false;
                }
            }
            
            if (add) {
                listForTable.add(jobId + BE.sp + jobDesc + BE.sp + combineDate + BE.sp + assigneeId + BE.sp);
            }
        }
        
        BE.setTableRow(employeeJobTable, listForTable);
    }
    
    private void jobComboBoxSetUp() throws IOException {
        jobComboBox.removeAllItems();
        
        ArrayList<String> jobList = BE.getAvailableJobs(selectedEmployeeId, this.complaintsId);
        
        if (jobList!=null) {
            for (String jobs : jobList) {
                String[] jobDetails = jobs.split(BE.sp);
                String jobItem = jobDetails[2];
                jobComboBox.addItem(jobItem);
            }
        }
    }
    
    private void dayCheckBox (String[] days) {
        for (String day : days) {
            switch (day) {
                case "monday" -> mondayCheckBox.setSelected(true);
                case "tuesday" -> tuesCheckBox.setSelected(true);
                case "wednesday" -> wedCheckBox.setSelected(true);
                case "thursday" -> thursCheckBox.setSelected(true);
                case "friday" -> friCheckBox.setSelected(true);
                case "saturday" -> satCheckBox.setSelected(true);
                case "sunday" -> sunCheckBox.setSelected(true);
            }
        }
    }
    
    private void dayCheckBoxAction (boolean enabled, boolean selected) {
        mondayCheckBox.setEnabled(enabled);
        mondayCheckBox.setSelected(selected);
        
        tuesCheckBox.setEnabled(enabled);
        tuesCheckBox.setSelected(selected);        
        
        wedCheckBox.setEnabled(enabled);        
        wedCheckBox.setSelected(selected);
        
        thursCheckBox.setEnabled(enabled);        
        thursCheckBox.setSelected(selected);
        
        friCheckBox.setEnabled(enabled);
        friCheckBox.setSelected(selected);
        
        satCheckBox.setEnabled(enabled);
        satCheckBox.setSelected(selected);
        
        sunCheckBox.setEnabled(enabled);
        sunCheckBox.setSelected(selected);
    }
    
    private void btnAction(boolean addBtn, boolean updtBtn, boolean dltBtn) {
        addBTN.setEnabled(addBtn);
        updateBTN.setEnabled(updtBtn);
        deleteBTN.setEnabled(dltBtn);
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
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        employeeNameTF = new javax.swing.JTextField();
        employeeIdTF = new javax.swing.JTextField();
        posTF = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        contNoTF = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jobComboBox = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        mondayCheckBox = new javax.swing.JCheckBox();
        tuesCheckBox = new javax.swing.JCheckBox();
        wedCheckBox = new javax.swing.JCheckBox();
        friCheckBox = new javax.swing.JCheckBox();
        thursCheckBox = new javax.swing.JCheckBox();
        sunCheckBox = new javax.swing.JCheckBox();
        satCheckBox = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        remarksTA = new javax.swing.JTextArea();
        jLabel22 = new javax.swing.JLabel();
        clearBTN = new javax.swing.JButton();
        addBTN = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        complaintIdTF = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        assignedJobTable = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        updateBTN = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        messagesTF = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        timeNeededSpinner = new javax.swing.JSpinner();
        repititionCheckBox = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        dateTimePicker1 = new com.github.lgooddatepicker.components.DateTimePicker();
        hrsMinsComboBox = new javax.swing.JComboBox<>();
        deleteBTN = new javax.swing.JButton();
        editJobBTN = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PARKHILL RESIDENCE");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setText("Employee Name:");
        jLabel14.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));

        jLabel15.setText("Employee ID:");
        jLabel15.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 51));

        jLabel16.setText("Position:");
        jLabel16.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));

        employeeNameTF.setEnabled(false);

        employeeIdTF.setEnabled(false);

        posTF.setEnabled(false);

        jLabel17.setText("Contact No.:");
        jLabel17.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));

        contNoTF.setEnabled(false);

        jLabel19.setText("Job To Assign:");
        jLabel19.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 51, 51));

        jobComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hi", "Electrical checkup" }));
        jobComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jobComboBoxActionPerformed(evt);
            }
        });

        jLabel20.setText("Repitition:");
        jLabel20.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 51, 51));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        mondayCheckBox.setText("Monday");
        mondayCheckBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(13, 24, 42)));
        mondayCheckBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        mondayCheckBox.setForeground(new java.awt.Color(51, 51, 51));

        tuesCheckBox.setText("Tuesday");
        tuesCheckBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(13, 24, 42)));
        tuesCheckBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        tuesCheckBox.setForeground(new java.awt.Color(51, 51, 51));

        wedCheckBox.setText("Wednesday");
        wedCheckBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(13, 24, 42)));
        wedCheckBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        wedCheckBox.setForeground(new java.awt.Color(51, 51, 51));

        friCheckBox.setText("Friday");
        friCheckBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(13, 24, 42)));
        friCheckBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        friCheckBox.setForeground(new java.awt.Color(51, 51, 51));
        friCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                friCheckBoxActionPerformed(evt);
            }
        });

        thursCheckBox.setText("Thursday");
        thursCheckBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(13, 24, 42)));
        thursCheckBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        thursCheckBox.setForeground(new java.awt.Color(51, 51, 51));

        sunCheckBox.setText("Sunday");
        sunCheckBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(13, 24, 42)));
        sunCheckBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        sunCheckBox.setForeground(new java.awt.Color(51, 51, 51));
        sunCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sunCheckBoxActionPerformed(evt);
            }
        });

        satCheckBox.setText("Saturday");
        satCheckBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(13, 24, 42)));
        satCheckBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        satCheckBox.setForeground(new java.awt.Color(51, 51, 51));
        satCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                satCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mondayCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tuesCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(wedCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(thursCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(friCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(satCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sunCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(mondayCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tuesCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(wedCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(thursCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(friCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(satCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(sunCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        remarksTA.setColumns(20);
        remarksTA.setLineWrap(true);
        remarksTA.setRows(3);
        remarksTA.setTabSize(4);
        remarksTA.setWrapStyleWord(true);
        jScrollPane2.setViewportView(remarksTA);

        jLabel22.setText("Remark(s):");
        jLabel22.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 51, 51));

        clearBTN.setText("Clear");
        clearBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBTNActionPerformed(evt);
            }
        });

        addBTN.setText("Add");
        addBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBTNActionPerformed(evt);
            }
        });

        jLabel23.setText("Complaint ID: ");
        jLabel23.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 51, 51));

        complaintIdTF.setEnabled(false);

        assignedJobTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Job ID", "Job Title", "Date & Time", "Assignee ID", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        assignedJobTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                assignedJobTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(assignedJobTable);
        if (assignedJobTable.getColumnModel().getColumnCount() > 0) {
            assignedJobTable.getColumnModel().getColumn(1).setResizable(false);
            assignedJobTable.getColumnModel().getColumn(2).setResizable(false);
            assignedJobTable.getColumnModel().getColumn(3).setResizable(false);
            assignedJobTable.getColumnModel().getColumn(4).setResizable(false);
        }

        jLabel18.setText("Assigned Job(s):");
        jLabel18.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));

        jTextField6.setEditable(false);
        jTextField6.setText("jTextField6");
        jTextField6.setBackground(new java.awt.Color(13, 24, 42));
        jTextField6.setForeground(new java.awt.Color(13, 24, 42));
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        updateBTN.setText("Update");
        updateBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBTNActionPerformed(evt);
            }
        });

        jLabel25.setText("Start Time:");
        jLabel25.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(51, 51, 51));

        messagesTF.setText("jLabel1");
        messagesTF.setFont(new java.awt.Font("Yu Gothic UI", 2, 12)); // NOI18N

        jLabel28.setText("Time Needed: ");
        jLabel28.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(51, 51, 51));

        repititionCheckBox.setText("Repeat");
        repititionCheckBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        repititionCheckBox.setForeground(new java.awt.Color(51, 51, 51));
        repititionCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repititionCheckBoxActionPerformed(evt);
            }
        });

        jLabel21.setText("Start Date: ");
        jLabel21.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 51, 51));

        jLabel26.setText("Day to Repeat: ");
        jLabel26.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(51, 51, 51));

        hrsMinsComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "hrs", "mins" }));

        deleteBTN.setText("Delete");
        deleteBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBTNActionPerformed(evt);
            }
        });

        editJobBTN.setText("Edit Job");
        editJobBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editJobBTNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jobComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(posTF, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(complaintIdTF, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(33, 33, 33)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(employeeIdTF)
                                                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))))
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(employeeNameTF)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(contNoTF)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(messagesTF, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(clearBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deleteBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(updateBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(addBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(56, 56, 56)
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(265, 265, 265)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(repititionCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel20)))
                                .addComponent(jScrollPane2)
                                .addComponent(dateTimePicker1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(timeNeededSpinner)
                                .addGap(3, 3, 3)
                                .addComponent(hrsMinsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editJobBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField6)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(complaintIdTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(employeeIdTF, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(employeeNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(posTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(contNoTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, 0)
                                                .addComponent(jobComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, 0)
                                                .addComponent(repititionCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(31, 31, 31)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(timeNeededSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(hrsMinsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(editJobBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(158, 158, 158)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addComponent(dateTimePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(0, 0, 0)
                                .addComponent(messagesTF, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(clearBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(updateBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(deleteBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(13, 13, 13))
        );

        jPanel1.setBackground(new java.awt.Color(13, 24, 42));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("EMPLOYEE JOB ASSIGNATION");
        jLabel2.setFont(new java.awt.Font("Britannic Bold", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/parkhillLogo.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(343, 343, 343)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(343, Short.MAX_VALUE))
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

    private void friCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_friCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_friCheckBoxActionPerformed

    private void sunCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sunCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sunCheckBoxActionPerformed

    private void satCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_satCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_satCheckBoxActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void clearBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBTNActionPerformed
        try {
            // TODO add your handling code here:
            this.jobID = null;
            setJobFormTable();
            setJobFormTable();
        } catch (IOException ex) {
            Logger.getLogger(EmployeeJobAssignation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_clearBTNActionPerformed

    private void addBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBTNActionPerformed
        try {
            // TODO add your handling code here:
            performButtonForAddUpdateDelete(this.addItem);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_addBTNActionPerformed

    private void deleteBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBTNActionPerformed
        try {
            // TODO add your handling code here:
            performButtonForAddUpdateDelete(this.deleteItem);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_deleteBTNActionPerformed

    private void updateBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBTNActionPerformed
        // TODO add your handling code here:
        if (!isSame) {
            try {
                performButtonForAddUpdateDelete(this.updateItem);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else {
            messagesTF.setText("The content is not modified");
        }
    }//GEN-LAST:event_updateBTNActionPerformed

    private void repititionCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repititionCheckBoxActionPerformed
        // TODO add your handling code here:
        if (repititionCheckBox.isSelected()) {
            dayCheckBoxAction(true, false);
            dateTimePicker1.datePicker.setEnabled(false);
        }
        else {
            dayCheckBoxAction(false, false);
            dateTimePicker1.datePicker.setEnabled(true);
        }
    }//GEN-LAST:event_repititionCheckBoxActionPerformed

    private void editJobBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editJobBTNActionPerformed
        try {
            // TODO add your handling code here:
            BE.toJobModificationPage(selectedEmployeePosCode);
        } catch (IOException ex) {
            Logger.getLogger(EmployeeJobAssignation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_editJobBTNActionPerformed

    private void assignedJobTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_assignedJobTableMouseClicked
        // TODO add your handling code here:
        int selectedCol = assignedJobTable.getSelectedColumn();
        int selectedRow = assignedJobTable.getSelectedRow();
        
        this.jobID = BE.validateTableSelectionAndGetValue(employeeJobTable, selectedCol, selectedRow, 4, 0);
        
        try {
            setJobFormTable();
        } catch (IOException ex) {
            Logger.getLogger(EmployeeJobAssignation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_assignedJobTableMouseClicked

    private void jobComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jobComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jobComboBoxActionPerformed

    private void updateJobTextFile(String[] jobItems, int action) throws IOException {
        List<String> readJobFile = fileHandling.fileRead(BE.employeeJobFile);
        List<String> newItemLists = new ArrayList<>();
        
        String itemID = jobItems[0];
        
        // make the string[] to an array list
        String specificItem = "";
        for (String arrayItem : jobItems) {
            specificItem += arrayItem + BE.sp;
        }
        
        for (String fileLine : readJobFile) {
            String[] jobLine = fileLine.split(BE.sp);
            String jobID = jobLine[0];

            if (jobID.equals(itemID)) {
                if (action == updateItem) {
                    newItemLists.add(specificItem);
                }
            }
            else {
                newItemLists.add(fileLine);
            }
        }
        
        if (action == this.addItem) {
            newItemLists.add(specificItem);
        }
        
        fileHandling.fileWrite(BE.employeeJobFile, false, newItemLists);
    }
    
    private String[] getAllFields(int action) throws IOException {
        ArrayList<String> latestFields;
        
        // job Id
        String fieldJobId = this.jobID;
        if (action == this.addItem) {
            fieldJobId = BE.getNewId(BE.employeeJobFile, 0);
        }
        
        // employeeId
        String employeeId = this.selectedEmployeeId;
        // complaintId
        this.complaintsId = this.getComplaintsId();
        // job assigned
        String assignedJob = jobComboBox.getSelectedItem().toString();
        String jobDet = BE.findJobDescriptionORid(null, assignedJob);
        String jobId = jobDet.split(BE.sp)[1];
        
        // repitition on or off
        String repitition;
        boolean repitionCheck = repititionCheckBox.isSelected();
        if (repitionCheck) {
            repitition = String.valueOf(BE.repititionON);
        }
        else {
            repitition = String.valueOf(BE.repititionOFF);
        }
        
        // expected time required
        String expectedTimeRequired;
        int timeNeeded = (int) timeNeededSpinner.getValue();
        String hrORmin = hrsMinsComboBox.getSelectedItem().toString();
        if (hrORmin.equals("hrs")) {
            expectedTimeRequired =  timeNeeded + "h";
            timeNeeded = timeNeeded *= 60;
        }
        else {
            expectedTimeRequired =  timeNeeded + "m";
        }
        
        // start date of the job
        String startDate = (dateTimePicker1.datePicker.getDate() != null) 
                            ? (String.valueOf(BE.formatDate(String.valueOf(dateTimePicker1.datePicker.getDate())))) 
                            : "null";
        
        // start time of the job
        LocalTime jobStartTime = BE.formatTime(String.valueOf(dateTimePicker1.timePicker.getTime()));
        String startTime = String.valueOf(jobStartTime);
        
        // expected end time of the job
        String expectedEndTime;
        if (!startDate.equals("null")) {
            LocalDateTime dateTimeEnd = LocalDateTime.of(BE.formatDate(startDate), BE.formatTime(startTime));
            expectedEndTime = String.valueOf(dateTimeEnd.plusMinutes(timeNeeded)).replace("T", " ");
        }
        else {
            expectedEndTime = "null " + String.valueOf(jobStartTime.plusMinutes(timeNeeded));
        }
        
        // day to repeat
        String dayToRepeat = "";
        boolean mon = mondayCheckBox.isSelected();
        boolean tues = tuesCheckBox.isSelected();
        boolean wed = wedCheckBox.isSelected();
        boolean thurs = thursCheckBox.isSelected();
        boolean fri = friCheckBox.isSelected();
        boolean sat = satCheckBox.isSelected();
        boolean sun = sunCheckBox.isSelected();
        
        boolean[] allBool = {mon, tues, wed, thurs, fri, sat, sun};
        
        int dayCount = 1;
        for (boolean eachBool : allBool) {
            if (eachBool) {
                switch (dayCount) {
                    case 1 -> dayToRepeat += "monday,";
                    case 2 -> dayToRepeat += "tuesday,";
                    case 3 -> dayToRepeat += "wednesday,";
                    case 4 -> dayToRepeat += "thursday,";
                    case 5 -> dayToRepeat += "friday,";
                    case 6 -> dayToRepeat += "saturday,";
                    case 7 -> dayToRepeat += "sunday,";
                }
            }
            dayCount++;
        }
        
        // get remarks
        String remarks = remarksTA.getText();
        
        String[] jobItemDetails = {fieldJobId, employeeId, complaintsId, 
            jobId, repitition, expectedTimeRequired, 
            startDate, startTime, expectedEndTime, 
            dayToRepeat, remarks};
        
        // last updated by
        String updatedBy = this.currentBEid;

        // last updated time
        String updatedTime = BE.getDateTimeNow();
        
        int newItemDetailsLength = jobItemDetails.length;
        
        if (action == this.updateItem) {
            String[] oldJobDetails = BE.getSpecificJobDetails(employeeId, fieldJobId);
            for (int checkTimes = 0; checkTimes < newItemDetailsLength; checkTimes++) {
                if (!jobItemDetails[checkTimes].equals(oldJobDetails[checkTimes])) {
                    isSame = false;
                }
            }
            
            if (isSame) {
                updatedBy = oldJobDetails[newItemDetailsLength];
                updatedTime = oldJobDetails[newItemDetailsLength + 1];
            }
        }
        
        latestFields = new ArrayList<>(Arrays.asList(jobItemDetails));
        latestFields.add(updatedBy);
        latestFields.add(updatedTime);
        latestFields.add("null");
        
        jobItemDetails = latestFields.toArray(jobItemDetails);
        
        return jobItemDetails;
    }
    
    private void performButtonForAddUpdateDelete (int action) throws IOException {
        String[] jobItems = getAllFields(action);
        ArrayList<String> clashList = new ArrayList<>();
        
        if (action != this.deleteItem) {
            clashList = checkJobClash(jobItems);
        }
        
        if (!clashList.isEmpty()) {
            JOptionPane.showConfirmDialog(this, "This job has clashed with " + clashList.toString(), "Clash Alert", JOptionPane.OK_OPTION);
        }
        else {
            updateJobTextFile(jobItems, action);
            this.jobID = null;
            setJobFormTable();
        }
    }
    
    private ArrayList<String> checkJobClash(String[] jobItems) throws IOException{
        ArrayList<String> clashJobId = new ArrayList<>();
        
        // get the start and end date time from the new job
        LocalDate dateInput = BE.formatDate(jobItems[6]);
        LocalTime timeInput = BE.formatTime(jobItems[7]);
        
        // Get all the job assigned to the specific employee
        ArrayList<String> employeeJobList = BE.getAssignedJobForSpecificEmployee(this.selectedEmployeeId);
        
        // loop for all assigned job
        for (String eachJob : employeeJobList) {
            String[] jobDetails = eachJob.split(BE.sp);
            // get the details of the job
            int repitition = Integer.valueOf(jobDetails[4]);
            LocalDate jobStartDate = null;
            LocalTime jobStartTime = BE.formatTime(jobDetails[7]);
            String jobId = jobDetails[0];
            String jobDesc = jobDetails[3];
            String timeNeeded = jobDetails[5];
            
            String jobCode = null;
            List<String> jobList = fileHandling.fileRead(BE.jobListFile);
            for (String job : jobList) {
                String[] jobInfo = job.split(BE.sp);
                if (jobInfo[2].equals(jobDesc)) {
                    jobCode = jobInfo[1];
                }
            }
            
            boolean ignore = false;
            if (jobCode.equals("MS") || jobCode.equals("NS")){
                ignore = true;
            }
            
            String dayOfWeek = dateInput.getDayOfWeek().toString().toLowerCase();
            
            if (!ignore) {
                String[] jobEndDateTime = jobDetails[8].split(" ");
                
                LocalTime workingStartTime2;
                LocalTime workingEndTime2;
                
                ArrayList<String> dateData = BE.compareJobDate(repitition, jobEndDateTime, jobDetails, timeNeeded, dayOfWeek, dateInput, jobStartTime, jobStartDate);
                
                jobStartDate = BE.formatDate(dateData.get(0));
                jobStartTime = BE.formatTime(dateData.get(1));
                
                workingStartTime2 = (!dateData.get(2).equals("null")) ? BE.formatTime(dateData.get(2)) : null;
                workingEndTime2 = (!dateData.get(3).equals("null")) ? BE.formatTime(dateData.get(3)) : null;
                
                jobEndDateTime = dateData.get(4).split(" ");
                
//                if (repitition == BE.repititionON) {
//                    int overnightDay = BE.checkOvernight(BE.formatTime(jobEndDateTime[1]), timeNeeded);
//                    boolean isOvernight = (overnightDay != 0);
//                    ArrayList<String> dayToRepeat = new ArrayList<>(Arrays.asList(jobDetails[9].split(",")));
//                    
//                    boolean foundToday = false;
//                    if (dayToRepeat.contains(dayOfWeek)) {
//                        foundToday = true;
//                    }
//                    
//                    boolean foundYesterday = false;
//                    if (isOvernight) {
//                        if (dayToRepeat.contains(dateInput.getDayOfWeek().plus(overnightDay).toString().toLowerCase())) {
//                            foundYesterday = true;
//                        }
//                    }
//                    
//                    if (isOvernight && foundYesterday || 
//                        isOvernight && foundToday || 
//                        isOvernight && foundYesterday ||
//                        !isOvernight && foundToday && !foundYesterday) 
//                    {
//                        jobStartDate = dateInput;
//                        jobEndDateTime[0] = dateInput.toString();
//                    }
//                    
//                    if (isOvernight && foundToday && foundYesterday) {
//                        workingStartTime2 = timeInput;
//                        timeInput = LocalTime.parse("00:00:00");
//                        workingEndTime2 = LocalTime.parse("00:00:00");
//                    }
//                    else if (isOvernight && foundToday && !foundYesterday) {
//                        jobEndDateTime[1] = "00:00:00";
//                    }
//                    else if (isOvernight && !foundToday && foundYesterday) {
//                        timeInput =  LocalTime.parse("00:00:00");
//                    }
//                }
//                else {
//                    jobStartDate = BE.formatDate(jobDetails[6]);
//                }
                
                if (jobStartDate != null) {
                    
                    boolean clash = BE.compareDateTime(dateInput, timeInput, jobStartDate, jobStartTime, jobEndDateTime, workingStartTime2, workingEndTime2);
//                    LocalDateTime selectedDateTime = LocalDateTime.of(dateInput, timeInput);
//                    LocalDateTime startDateTime = LocalDateTime.of(jobStartDate, jobStartTime);
//                    LocalDateTime endDateTime = LocalDateTime.of(BE.formatDate(jobEndDateTime[0]), BE.formatTime(jobEndDateTime[1]));
//                    
//                    boolean clash = false;
//                    if ((selectedDateTime.equals(startDateTime) || selectedDateTime.isAfter(startDateTime)) && 
//                        (selectedDateTime.equals(endDateTime) || selectedDateTime.isBefore(endDateTime))) {
//                        clash = true;
//                    }
//                    else if (workingStartTime2 != null && workingEndTime2 != null) {
//                        LocalDateTime startDateTime2 = LocalDateTime.of(jobStartDate, workingStartTime2);
//                        LocalDateTime endDateTime2 = LocalDateTime.of(jobStartDate, workingEndTime2);
//
//                        if ((selectedDateTime.equals(startDateTime2) || selectedDateTime.isAfter(startDateTime2)) &&
//                            (selectedDateTime.equals(endDateTime2) || selectedDateTime.isBefore(endDateTime2))) {
//                            clash = true;
//                        }
//                    }

                    if (clash) {
                        clashJobId.add(jobId);
                    }
                }
            }
        }
        
        return clashJobId;
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
            java.util.logging.Logger.getLogger(EmployeeJobAssignation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeJobAssignation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeJobAssignation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeJobAssignation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new EmployeeJobAssignation(null, null, null, null, false).setVisible(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBTN;
    private javax.swing.JTable assignedJobTable;
    private javax.swing.JButton clearBTN;
    private javax.swing.JTextField complaintIdTF;
    private javax.swing.JTextField contNoTF;
    private com.github.lgooddatepicker.components.DateTimePicker dateTimePicker1;
    private javax.swing.JButton deleteBTN;
    private javax.swing.JButton editJobBTN;
    private javax.swing.JTextField employeeIdTF;
    private javax.swing.JTextField employeeNameTF;
    private javax.swing.JCheckBox friCheckBox;
    private javax.swing.JComboBox<String> hrsMinsComboBox;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JComboBox<String> jobComboBox;
    private javax.swing.JLabel messagesTF;
    private javax.swing.JCheckBox mondayCheckBox;
    private javax.swing.JTextField posTF;
    private javax.swing.JTextArea remarksTA;
    private javax.swing.JCheckBox repititionCheckBox;
    private javax.swing.JCheckBox satCheckBox;
    private javax.swing.JCheckBox sunCheckBox;
    private javax.swing.JCheckBox thursCheckBox;
    private javax.swing.JSpinner timeNeededSpinner;
    private javax.swing.JCheckBox tuesCheckBox;
    private javax.swing.JButton updateBTN;
    private javax.swing.JCheckBox wedCheckBox;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the complaintsId
     */
    public String getComplaintsId() {
        return complaintsId;
    }

    /**
     * @param complaintsId the complaintsId to set
     */
    public void setComplaintsId(String complaintsId) {
        this.complaintsId = (complaintsId != null) ? complaintsId : null;
    }

    /**
     * @return the fromComplaintsPage
     */
    public boolean isFromComplaintsPage() {
        return fromComplaintsPage;
    }

    /**
     * @param fromComplaintsPage the fromComplaintsPage to set
     */
    public void setFromComplaintsPage(boolean fromComplaintsPage) {
        this.fromComplaintsPage = fromComplaintsPage;
    }
}

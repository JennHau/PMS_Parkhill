/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package buildingExecutive;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import classes.Complaint;
import classes.FileHandling;

/**
 *
 * @author Winson
 */
public class EmployeeJobAssignation extends javax.swing.JFrame {
    public static EmployeeJobAssignation employeeJobAssignation;
    private final BuildingExecutive BE;
    FileHandling fileHandling = new FileHandling();
    
    private Complaint complaint;
    
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
     * @param BE
     * @param employeeID
     * @param jobId
     * @param complaint
     * @param complaintsPage
     * @throws java.io.IOException
     */
    public EmployeeJobAssignation(BuildingExecutive BE, String employeeID, String jobId, Complaint complaint, boolean complaintsPage) throws IOException {
        employeeJobAssignation = this;
        this.BE = BE;
        this.complaint = complaint;
        setSelectedEmployee(employeeID, jobId);
        setFromComplaintsPage(complaintsPage);
        
        initComponents();
        runDefaultSetUp();
    }

    private void runDefaultSetUp() throws IOException {
        employeeJobTable = (DefaultTableModel) assignedJobTable.getModel();
        
        setWindowIcon();
        setCurrentBE();
        if (complaint!=null) {
            setComplaintsId(complaint.getComplaintID());
        }
        
        jobComboBoxSetUp();
        setJobFormTable();
    }
    
    private void setJobFormTable() throws IOException {
        formInitialSetUp();
        formAdditionalDetailsSetUp();
        setJobTable();
        setJobListTableDesign();
    }

    private void setWindowIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/windowIcon.png")));
    }

    private void setSelectedEmployee(String employeeID, String jobId) throws IOException {
        this.selectedEmployeeId = employeeID;
        this.jobID = jobId;
        this.selectedEmployeePosCode = BE.getEmployeePositionCode(employeeID, null);
    }

    private void setCurrentBE() {
        this.currentBEid = BE.getUserID();
    }

    // employeeDetails[userID, email, fullName, phoneNo, position]
    private void formInitialSetUp() throws IOException {
        String[] employeeDetails = BE.getEmployeeDetails(selectedEmployeeId);
        
        if (employeeDetails != null) {
            String emplyName = employeeDetails[2];
            String emplyPhoneNo = employeeDetails[3];
            String emplyPos = employeeDetails[4];

            employeeIdTF.setText(this.selectedEmployeeId.toUpperCase());
            employeeNameTF.setText(emplyName);
            contNoTF.setText(emplyPhoneNo);
            posTF.setText(emplyPos);
        }
        
        assignedJobTable.setEnabled(!fromComplaintsPage);
        editJobBTN.setEnabled(!fromComplaintsPage);
    }
    
    private void formAdditionalDetailsSetUp() throws IOException {
        if (jobID != null) {
            String[] jobDetails = BE.getSpecificJobDetails(selectedEmployeeId, jobID);
            
            this.complaintsId = (jobDetails[2].equals(BE.TF.empty)) ? null : jobDetails[2];
            
            String jobToAssign = jobDetails[3];
            int repitition = Integer.valueOf(jobDetails[4]);
            String timeNeeded = jobDetails[5];
            LocalTime startTime = BE.DTF.formatTime(jobDetails[7]);
            String dayToRepeat = jobDetails[9];
            String remarks = jobDetails[10];

            String timePlace = timeNeeded.substring(timeNeeded.length()-1);
            timeNeeded = timeNeeded.substring(0, timeNeeded.length()-1);
            
            timePlace = (timePlace.equals("m")) ? "mins" : "hrs";
            hrsMinsComboBox.setSelectedItem(timePlace);
            
            String jobInfo = BE.findJobDetailsUsingDescriptionOrId(jobToAssign, null);
            String jobDesc = jobInfo.split(BE.TF.sp)[2];
            
            if (jobToAssign.equals("PT")) {
                jobComboBox.addItem("Patrolling");
            }
            
            jobComboBox.setSelectedItem(jobDesc);
            timeNeededSpinner.setValue(Integer.valueOf(timeNeeded));
            dateTimePicker1.timePicker.setTime(startTime);
            remarksTA.setText(remarks);
            
            if (!jobToAssign.equals("PT")) {
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
                    LocalDate startDate = BE.DTF.formatDate(jobDetails[6]);
                    dateTimePicker1.datePicker.setDate(startDate);
                    dateTimePicker1.datePicker.setEnabled(true);
                    dayCheckBoxAction(false, false);
                }

                repititionCheckBox.setEnabled(true);
                timeNeededSpinner.setEnabled(true);
                hrsMinsComboBox.setEnabled(true);
                btnAction(false, true, true);
            } 
            else {
                updateBTN.setEnabled(false);
                deleteBTN.setEnabled(false);
                repititionCheckBox.setEnabled(false);
                timeNeededSpinner.setEnabled(false);
                dateTimePicker1.setEnabled(false);
                hrsMinsComboBox.setEnabled(false);
                
                dayCheckBoxAction(false, false);
            }
        }
        else {
            jobComboBox.setSelectedIndex(0);
            jobComboBox.setEnabled(true);
            repititionCheckBox.setSelected(false);
            repititionCheckBox.setEnabled(true);
            timeNeededSpinner.setValue(0);
            hrsMinsComboBox.setSelectedIndex(0);
            dateTimePicker1.datePicker.setDateToToday();
            dateTimePicker1.datePicker.setEnabled(true);
            LocalTime timeNow = BE.DTF.getTimeCategory(LocalTime.now());
            dateTimePicker1.timePicker.setTime(timeNow);
            remarksTA.setText("");
            dayCheckBoxAction(false, false);
            btnAction(true, false, false);
        }
        
        if (getComplaintsId() != null) {
            jobComboBox.setSelectedItem("Complaints");
            complaintIdTF.setText(getComplaintsId().toUpperCase());
            repititionCheckBox.setEnabled(false);
            jobComboBox.setEnabled(false);
        }
    }
    
    private void setJobTable() throws IOException {
        ArrayList<String> jobList = BE.getAssignedJobForSpecificEmployee(selectedEmployeeId);
        ArrayList<String> listForTable = new ArrayList<>();
        
        String combineDate;
        
        for (String eachJob : jobList) {
            String[] jobDetails = eachJob.split(BE.TF.sp);
            String jobId = jobDetails[0];
            String jobDesc = BE.findJobDetailsUsingDescriptionOrId(jobDetails[3], null).split(BE.TF.sp)[2];
            int repitition = Integer.valueOf(jobDetails[4]);
            String startDate = jobDetails[6];
            String startTime = jobDetails[7];
            
            String[] endDateTime = jobDetails[8].split(" ");
            
            LocalDateTime workingEndDateTime = null;
            if (!endDateTime[0].equals(BE.TF.empty)) {
                workingEndDateTime = BE.DTF.combineStringDateTime(endDateTime[0], endDateTime[1]);
            }
            
            String dayToRepeat = jobDetails[9];
            String assigneeId = jobDetails[11];
            
            String eachDay = "";
            if (!dayToRepeat.equals(BE.TF.empty)) {
                for (String day : dayToRepeat.split(",")) {
                    eachDay += day.substring(0, 3).toUpperCase()+ " ";
                }
            }
                     
            combineDate = startDate + " " + startTime;
            
            if (repitition == BE.repititionON) {
                combineDate = eachDay + startTime;
            }
            
            LocalDateTime dateTimeNow = LocalDateTime.now();
            
            boolean add = true;
            if ((workingEndDateTime != null && dayToRepeat.equals(BE.TF.empty))) {
                if (workingEndDateTime.isBefore(dateTimeNow)) {
                    add = false;
                }
            }
            
            if (add) {
                listForTable.add(jobId.toUpperCase() + BE.TF.sp + jobDesc + BE.TF.sp + combineDate + BE.TF.sp + assigneeId.toUpperCase() + BE.TF.sp + "MODIFY" + BE.TF.sp);
            }
        }
        
        BE.setTableRow(employeeJobTable, listForTable);
    }
    
    private void setJobListTableDesign() {
        int[] columnIgnore = {2};
        int[] columnLength = {80, 165, 265, 80, 80};
        BE.setTableDesign(assignedJobTable, jLabel2, columnLength, columnIgnore);
    }
    
    private void jobComboBoxSetUp() throws IOException {
        jobComboBox.removeAllItems();
        
        ArrayList<String> jobList = BE.getAvailableJobs(selectedEmployeeId, this.complaintsId);
        
        if (jobList!=null) {
            for (String jobs : jobList) {
                String[] jobDetails = jobs.split(BE.TF.sp);
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
        assignedJobTable = new javax.swing.JTable()
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
        backBTN = new javax.swing.JButton();
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
                "JOB ID", "JOB TITLE", "DATE & TIME", "ASSIGNEE", "ACTION"
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
        assignedJobTable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        assignedJobTable.setIntercellSpacing(new java.awt.Dimension(1, 1));
        assignedJobTable.setName(""); // NOI18N
        assignedJobTable.setRowHeight(25);
        assignedJobTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                assignedJobTableMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                assignedJobTableMouseEntered(evt);
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(backBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(clearBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(updateBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(479, 479, 479)
                        .addComponent(editJobBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(editJobBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
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
                                    .addComponent(deleteBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(backBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))))
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
                .addContainerGap(523, Short.MAX_VALUE))
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
            messagesTF.setText("");
            jobComboBox.setEnabled(true);
            jobComboBox.removeItem("Patrolling");
            repititionCheckBox.setEnabled(true);
            timeNeededSpinner.setEnabled(true);
            hrsMinsComboBox.setEnabled(true);
        } catch (IOException ex) {
            Logger.getLogger(EmployeeJobAssignation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_clearBTNActionPerformed

    private void addBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBTNActionPerformed
        int result = JOptionPane.showConfirmDialog(null,"Are you sure to "
                + "add this job?", "FACILITY BOOKING",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

        if(result == JOptionPane.YES_OPTION){
            try {
                // TODO add your handling code here:
                performButtonForAddUpdateDelete(this.addItem);
                if (fromComplaintsPage) {
                    fromComplaintsPage = false;
                    complaintsId = null;
                    complaint = null;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_addBTNActionPerformed

    private void deleteBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBTNActionPerformed
        int result = JOptionPane.showConfirmDialog(null,"Are you sure to "
                + "remove this job?", "FACILITY BOOKING",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

            if(result == JOptionPane.YES_OPTION){
            try {
                // TODO add your handling code here:
                performButtonForAddUpdateDelete(this.deleteItem);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_deleteBTNActionPerformed

    private void updateBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBTNActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(null,"Are you sure to "
                + "update this job?", "FACILITY BOOKING",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

        if(result == JOptionPane.YES_OPTION){
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
        }
    }//GEN-LAST:event_updateBTNActionPerformed

    private void repititionCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repititionCheckBoxActionPerformed
        // TODO add your handling code here:
        if (repititionCheckBox.isSelected()) {
            dayCheckBoxAction(true, false);
            dateTimePicker1.datePicker.setEnabled(false);
            dateTimePicker1.datePicker.setDate(null);
        }
        else {
            dayCheckBoxAction(false, false);
            dateTimePicker1.datePicker.setEnabled(true);
            dateTimePicker1.datePicker.setDate(LocalDate.now());
        }
    }//GEN-LAST:event_repititionCheckBoxActionPerformed

    private void editJobBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editJobBTNActionPerformed
        try {
            // TODO add your handling code here:
            BE.toJobModificationPage(this.BE, selectedEmployeePosCode, this.jobID, this.complaint, this.selectedEmployeeId);
        } catch (IOException ex) {
            Logger.getLogger(EmployeeJobAssignation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_editJobBTNActionPerformed

    private void assignedJobTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_assignedJobTableMouseClicked
        // TODO add your handling code here:
        jobComboBox.removeItem("Patrolling");

        int selectedCol = assignedJobTable.getSelectedColumn();
        int selectedRow = assignedJobTable.getSelectedRow();
        
        this.jobID = BE.validateTableSelectionAndGetValue(employeeJobTable, selectedCol, selectedRow, 4, 0);
        
        if (this.jobID != null) {
            try {
                this.jobID = this.jobID.toLowerCase();
                setJobFormTable();
            } catch (IOException ex) {
                Logger.getLogger(EmployeeJobAssignation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_assignedJobTableMouseClicked

    private void jobComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jobComboBoxActionPerformed
        // TODO add your handling code here:
        String selectedJob = (jobComboBox.getSelectedItem()!=null) ? jobComboBox.getSelectedItem().toString() : null;
        if (selectedJob!=null){
            if (selectedJob.equals("Patrolling")) {
                addBTN.setEnabled(false);
                jobComboBox.setEnabled(false);
                hrsMinsComboBox.setEnabled(false);
                
                messagesTF.setText("Assign this job at Patrolling Management. Thank you.");
            }
            else {
                if (jobID!=null) {
                    List<String> jobFile = fileHandling.fileRead(BE.TF.jobListFile);
                    for (String eachJob : jobFile) {
                        String[] jobDet = eachJob.split(BE.TF.sp);
                        if  (jobDet[2].equals(selectedJob)) {
                            String timeNeeded = jobDet[3];
                            String timePlace = timeNeeded.substring(timeNeeded.length()-1);
                            timeNeeded = timeNeeded.substring(0, timeNeeded.length()-1);

                            timePlace = (timePlace.equals("m")) ? "mins" : "hrs";
                            hrsMinsComboBox.setSelectedItem(timePlace);
                            
                            timeNeededSpinner.setValue(Integer.valueOf(timeNeeded));
                            
                            String startTime = jobDet[4];
                            dateTimePicker1.timePicker.setTime(BE.DTF.formatTime(startTime));
                        }
                    }
                    
                    jobComboBox.setEnabled(true);
                    hrsMinsComboBox.setEnabled(true);
                }
                
                messagesTF.setText("");
            }
        }
    }//GEN-LAST:event_jobComboBoxActionPerformed

    private void backBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBTNActionPerformed
        // TODO add your handling code here:
        if (BuildingExecutiveJobManagement.BEjobManagement != null) {
            BuildingExecutiveJobManagement.BEjobManagement.dispose();
        }
        BE.toJobManagement(this, BE, this.complaint, fromComplaintsPage);
    }//GEN-LAST:event_backBTNActionPerformed

    private void assignedJobTableMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_assignedJobTableMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_assignedJobTableMouseEntered

    private void updateJobTextFile(String[] jobItems, int action) throws IOException {
        List<String> readJobFile = fileHandling.fileRead(BE.TF.employeeJobFile);
        List<String> newItemLists = new ArrayList<>();
        
        String itemID = jobItems[0];
        
        // make the string[] to an array list
        String specificItem = "";
        for (String arrayItem : jobItems) {
            specificItem += arrayItem + BE.TF.sp;
        }
        
        for (String fileLine : readJobFile) {
            String[] jobLine = fileLine.split(BE.TF.sp);
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
        
        fileHandling.fileWrite(BE.TF.employeeJobFile, false, newItemLists);
        
        if (complaintsId!=null) {
            updateComplaintsFile(action);
        }
    }
    
    private void updateComplaintsFile(int action){
        String status = complaint.getComplaintStatus();
        if (action == this.addItem) {
            status = Complaint.cptStatus.Progressing.toString();
        }
        else if (action == this.deleteItem) {
            status = Complaint.cptStatus.Pending.toString();
        }
        
        complaint.setComplaintStatus(status);
        complaint.setStatusUpdatedBy(this.BE.getUserID());
        complaint.setLastUpdateDateTime(BE.DTF.currentDateTime());
        
        complaint.updateComplaintStatus(complaint);
        
//        ArrayList<String> updateComFile = new ArrayList<>();
//        List<String> complaintFile = fileHandling.fileRead(BE.TF.complaintFiles);
//        for (String eachComp : complaintFile) {
//            String[] compDet = eachComp.split(BE.TF.sp);
//            String compId = compDet[0];
//            
//            if (compId.equals(this.complaintsId)) {
//                String updateLine =  "";
//                for (int data = 0; data < compDet.length; data++) {
//                    if (data == 5) {
//                        String status = null;
//                        if (action == this.addItem) {
//                            status = Complaint.cptStatus.Progressing.toString();
//                        }
//                        else if (action == this.deleteItem) {
//                            status = Complaint.cptStatus.Pending.toString();
//                        }
//                        
//                        updateLine = updateLine + status + BE.TF.sp;
//                    }
//                    else {
//                        updateLine = updateLine + compDet[data] + BE.TF.sp;
//                    }
//                }
//                
//                updateComFile.add(updateLine);
//            }
//            else {
//                updateComFile.add(eachComp);
//            }
//        }
//        
//        fileHandling.fileWrite(BE.TF.complaintFiles, false, updateComFile);
    }
    
    private String[] getAllFields(int action) throws IOException {
        ArrayList<String> latestFields;
        
        // job Id
        String fieldJobId = this.jobID;
        if (action == this.addItem) {
            fieldJobId = BE.getNewTaskId(BE.TF.employeeJobFile, 0);
        }
        
        // employeeId
        String employeeId = this.selectedEmployeeId;
        // complaintId
        String compId = (this.getComplaintsId() != null) ? this.getComplaintsId() : BE.TF.empty;
        // job assigned
        String assignedJob = jobComboBox.getSelectedItem().toString();
        String jobDet = BE.findJobDetailsUsingDescriptionOrId(null, assignedJob);
        String jobId = jobDet.split(BE.TF.sp)[1];
        
        // repitition on or off
        String repitition;
        boolean repititionCheck = repititionCheckBox.isSelected();
        if (repititionCheck) {
            repitition = String.valueOf(BE.repititionON);
        }
        else {
            repitition = String.valueOf(BE.repititionOFF);
        }
        
        // expected time required
        String expectedTimeRequired;
        int timeNeeded = (int) timeNeededSpinner.getValue();
        
        if (timeNeeded > 0) {
            String hrORmin = hrsMinsComboBox.getSelectedItem().toString();
            if (hrORmin.equals("hrs")) {
                expectedTimeRequired =  timeNeeded + "h";
                timeNeeded = timeNeeded *= 60;
            }
            else {
                expectedTimeRequired =  timeNeeded + "m";
            }
        }
        else {
            JOptionPane.showMessageDialog (null, "Please select the expected time needed.", 
                                                    "EMPLOYEE JOB ASSIGNATION", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        
        // start date of the job
        String startDate = (dateTimePicker1.datePicker.getDate() != null) 
                            ? (String.valueOf(BE.DTF.formatDate(String.valueOf(dateTimePicker1.datePicker.getDate())))) 
                            : BE.TF.empty;
        
        if (!startDate.equals(BE.TF.empty) && BE.DTF.formatDate(startDate).isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog (null, "The selected date is a past. Please choose an upcoming or today's date", 
                                                    "EMPLOYEE JOB ASSIGNATION", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        
        // start time of the job
        LocalTime jobStartTime = BE.DTF.formatTime(String.valueOf(dateTimePicker1.timePicker.getTime()));
        String startTime = String.valueOf(jobStartTime);
        
        if (!startDate.equals(BE.TF.empty)) {
            if (BE.DTF.combineStringDateTime(startDate, startTime).isBefore(LocalDateTime.now())) {
                JOptionPane.showMessageDialog (null, "The selected time has past. Please choose an upcoming time", 
                                                        "EMPLOYEE JOB ASSIGNATION", JOptionPane.INFORMATION_MESSAGE);
                return null;
            }
        }
        
        // expected end time of the job
        String expectedEndTime;
        if (!startDate.equals(BE.TF.empty)) {
            LocalDateTime dateTimeEnd = LocalDateTime.of(BE.DTF.formatDate(startDate), BE.DTF.formatTime(startTime));
            expectedEndTime = String.valueOf(dateTimeEnd.plusMinutes(timeNeeded)).replace("T", " ");
        }
        else {
            expectedEndTime = BE.TF.empty + " " + String.valueOf(jobStartTime.plusMinutes(timeNeeded));
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
        
        dayToRepeat = (dayToRepeat.equals("")) ? BE.TF.empty : dayToRepeat;
        
        if (repititionCheck && dayToRepeat.equals(BE.TF.empty)) {
            JOptionPane.showMessageDialog (null, "Please select the day to repeat this job", 
                                                    "EMPLOYEE JOB ASSIGNATION", JOptionPane.INFORMATION_MESSAGE);
        }
        
        // get remarks
        String remarks = (remarksTA.getText().equals("")) ? BE.TF.empty : remarksTA.getText();
        
        String[] jobItemDetails = {fieldJobId, employeeId, compId, 
            jobId, repitition, expectedTimeRequired, 
            startDate, startTime, expectedEndTime, 
            dayToRepeat, remarks};
        
        // last updated by
        String updatedBy = this.currentBEid;

        // last updated time
        String updatedTime = BE.DTF.currentDateTime();
        
        int newItemDetailsLength = jobItemDetails.length;
        
        int count = 0;
        if (action == this.updateItem) {
            String[] oldJobDetails = BE.getSpecificJobDetails(employeeId, fieldJobId);
            for (int checkTimes = 0; checkTimes < newItemDetailsLength; checkTimes++) {
                if (!jobItemDetails[checkTimes].equals(oldJobDetails[checkTimes])) {
                    count++;
                }
            }
            
            if (count == newItemDetailsLength) {
                isSame = true;
                updatedBy = oldJobDetails[newItemDetailsLength];
                updatedTime = oldJobDetails[newItemDetailsLength + 1];
            }
        }
        
        latestFields = new ArrayList<>(Arrays.asList(jobItemDetails));
        latestFields.add(updatedBy);
        latestFields.add(updatedTime);
        latestFields.add(BE.TF.empty);
        
        jobItemDetails = latestFields.toArray(jobItemDetails);
        
        return jobItemDetails;
    }
    
    private void performButtonForAddUpdateDelete (int action) throws IOException {
        String[] jobItems = getAllFields(action);
        ArrayList<String> clashList = new ArrayList<>();
        
        if (jobItems != null) {
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
    }
    
    private ArrayList<String> checkJobClash(String[] jobItems) throws IOException{
        ArrayList<String> clashJobId = new ArrayList<>();
        
        // get the start and end date time from the new job
        LocalDate dateInput = (jobItems[6].equals(BE.TF.empty)) ? null : BE.DTF.formatDate(jobItems[6]);
        LocalTime timeInput = BE.DTF.formatTime(jobItems[7]);
        
        // Get all the job assigned to the specific employee
        ArrayList<String> employeeJobList = BE.getAssignedJobForSpecificEmployee(this.selectedEmployeeId);
        
        if (dateInput != null) {
            // loop for all assigned job
            for (String eachJob : employeeJobList) {
                String[] jobDetails = eachJob.split(BE.TF.sp);
                // get the details of the job
                int repitition = Integer.valueOf(jobDetails[4]);
                LocalDate jobStartDate = null;
                LocalTime jobStartTime = BE.DTF.formatTime(jobDetails[7]);
                String jobId = jobDetails[0];
                String jobDesc = jobDetails[3];
                String timeNeeded = jobDetails[5];
                
//                String jobCode = null;
//                List<String> jobList = fileHandling.fileRead(BE.TF.jobListFile);
//                for (String job : jobList) {
//                    String[] jobInfo = job.split(BE.TF.sp);
//                    if (jobInfo[2].equals(jobDesc)) {
//                        jobCode = jobInfo[1];
//                    }
//                }

                boolean ignore = false;
                if (jobDesc.equals("MS") || jobDesc.equals("NS")){
                    ignore = true;
                }

                String dayOfWeek = dateInput.getDayOfWeek().toString().toLowerCase();

                if (!ignore) {
                    String[] jobEndDateTime = jobDetails[8].split(" ");

                    LocalTime workingStartTime2;
                    LocalTime workingEndTime2;

                    ArrayList<String> dateData = BE.compareJobDate(repitition, jobEndDateTime, jobDetails, timeNeeded, dayOfWeek, dateInput, jobStartTime, jobStartDate);

                    jobStartDate = BE.DTF.formatDate(dateData.get(0));
                    jobStartTime = BE.DTF.formatTime(dateData.get(1));

                    workingStartTime2 = (!dateData.get(2).equals("null")) ? BE.DTF.formatTime(dateData.get(2)) : null;
                    workingEndTime2 = (!dateData.get(3).equals("null")) ? BE.DTF.formatTime(dateData.get(3)) : null;

                    jobEndDateTime = dateData.get(4).split(" ");

                    if (jobStartDate != null) {

                        boolean clash = BE.compareDateTime(dateInput, timeInput, jobStartDate, jobStartTime, jobEndDateTime, workingStartTime2, workingEndTime2);

                        if (clash) {
                            clashJobId.add(jobId.toUpperCase());
                        }
                    }
                }
            }
        }
        else {
            String[] eachRepeatDay = jobItems[9].split(",");
            ArrayList<String> newDays = new ArrayList<>(Arrays.asList(eachRepeatDay));
            
            for (String eachJob : employeeJobList) {
                String[] jobDet = eachJob.split(BE.TF.sp);
                if (!jobDet[9].equals(BE.TF.empty)) {
                    String[] assignedDays = jobDet[9].split(",");
                    for (String eachDay : assignedDays) {
                        if (newDays.contains(eachDay)) {
                            int timeNeeded = Integer.valueOf(jobDet[5].substring(0, jobDet[5].length()-1));
                            String timeValue = jobDet[5].substring(jobDet[5].length()-1);
                            
                            timeNeeded = (timeValue.equals("h")) ? timeNeeded * 60 : timeNeeded;
                            
                            String jobTime = jobDet[7];
                            LocalDateTime jobStartDateTime = BE.DTF.combineStringDateTime("2000-01-01", jobTime);
                            LocalDateTime jobEndDateTime = jobStartDateTime.plusMinutes(timeNeeded);
                            
                            int newTimeNeeded = Integer.valueOf(jobItems[5].substring(0, jobItems[5].length()-1));
                            String newTimeValue = jobItems[5].substring(jobDet[5].length()-1);
                            
                            newTimeNeeded = (newTimeValue.equals("h")) ? newTimeNeeded * 60 : newTimeNeeded;
                            
                            LocalDateTime inputDateTime = BE.DTF.combineStringDateTime("2000-01-01", timeInput.toString());
                            LocalDateTime inputEndDateTime = inputDateTime.plusMinutes(newTimeNeeded);
                            
                            if ((inputDateTime.isAfter(jobStartDateTime) || inputDateTime.isEqual(jobStartDateTime)) 
                             && (inputDateTime.isBefore(jobEndDateTime) || inputDateTime.isEqual(jobEndDateTime))) {
                                if (clashJobId.contains(jobDet[0].toUpperCase())) {
                                    clashJobId.add(jobDet[0].toUpperCase());                                
                                }
                            } 
                            
                            if ((inputEndDateTime.isAfter(jobStartDateTime) || inputEndDateTime.isEqual(jobStartDateTime)) 
                             && (inputEndDateTime.isBefore(jobEndDateTime) || inputEndDateTime.isEqual(jobEndDateTime))) {
                                if (clashJobId.contains(jobDet[0].toUpperCase())) {
                                    clashJobId.add(jobDet[0].toUpperCase());                                
                                }
                            }
                        }
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
    private javax.swing.JButton backBTN;
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

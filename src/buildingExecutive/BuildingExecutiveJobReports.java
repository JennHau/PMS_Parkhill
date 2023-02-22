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
import pms_parkhill_residence.HomePage;

/**
 *
 * @author wongj
 */
public class BuildingExecutiveJobReports extends javax.swing.JFrame {
    private final BuildingExecutive BE;
    DefaultTableModel jobTab;
    /**
     * Creates new form homePage
     * @param BE
     */
    public BuildingExecutiveJobReports(BuildingExecutive BE) {
        this.BE = BE;
        
        initComponents();
        
        jobTab = (DefaultTableModel) jobTable.getModel();
        setWindowIcon();
        
        monthYearComboBoxSetUp();
        employeeComboBoxSetUp();
    }
    
    // get available date range for job reports
    private ArrayList getDateRange() {
        List<String> allJob = BE.getAllAssignedJob();
        
        LocalDate earliestDate = null;
        LocalDate latestDate = null;
        boolean firstLine = true;
        for (String eachJob : allJob) {
            if (!firstLine) {
                String[] jobDet = eachJob.split(BE.TF.sp);
                String assignedJob = jobDet[4];
                
                if (!assignedJob.equals("PT")) {
                    String date = jobDet[6];
                    if (!date.equals(BE.TF.empty)) {
                        LocalDate jobDate = BE.DTF.formatDate(date);

                        if (earliestDate != null) {
                            if (jobDate.isBefore(earliestDate)) {
                                earliestDate = jobDate;
                            }
                        }
                        else {
                            earliestDate = jobDate;
                        }

                        if (latestDate != null) {
                            if (jobDate.isAfter(latestDate)) {
                                latestDate = jobDate;
                            }
                        }
                        else {
                            latestDate = jobDate;
                        }
                    }
                }
            }
            
            firstLine = false;
        }
        
        ArrayList<String> complaintsDateRange = new ArrayList<>();
        
        if (earliestDate != null && latestDate != null) {
            earliestDate = earliestDate.withDayOfMonth(1);
            latestDate = latestDate.withDayOfMonth(1);

            while (latestDate.isAfter(earliestDate) || latestDate.isEqual(earliestDate)) {
                complaintsDateRange.add(String.valueOf(latestDate.getMonthValue() + "-" + latestDate.getYear()));
                latestDate = latestDate.minusMonths(1);
            }
        }
        
        return complaintsDateRange;
    }
    
    // set up month and year combo box
    private void monthYearComboBoxSetUp() {
        ArrayList<String> dateRange = getDateRange();
        
        monthCB.removeAllItems();
        for (String eachDate : dateRange) {
            monthCB.addItem(eachDate);
        }
        
        monthCB.setSelectedItem(0);
    }
    
    // set up employee combo box
    private void employeeComboBoxSetUp() {
        employeeIdCB.removeAllItems();
        
        employeeIdCB.addItem("ALL");
        
        ArrayList<String> allEmpId = new ArrayList<>();
        
        List<String> employeeFile = BE.fh.fileRead(BE.TF.fullEmployeeList);
        
        boolean firstLine = true;
        for (String eachEmp : employeeFile) {
            if (!firstLine) {
                String empId = eachEmp.split(BE.TF.sp)[0];
                if (!allEmpId.contains(empId)) {
                    employeeIdCB.addItem(empId.toUpperCase());
                    allEmpId.add(empId);
                }
            }
            
            firstLine = false;
        }
        employeeIdCB.setSelectedItem(0);
    }
    
    // table set up
    private void tableSetUp() {
        String selectedMonthYear = (monthCB.getSelectedItem() != null) ? monthCB.getSelectedItem().toString() : null;
        String selectedEmployee = (employeeIdCB.getSelectedItem() != null) ? employeeIdCB.getSelectedItem().toString() : null;
        
        List<String> allJobs = BE.getAllAssignedJob();
        
        ArrayList<String> employeeJobs = new ArrayList<>();
        ArrayList<String> dateJobs = new ArrayList<>();
        
        boolean firstLine = true;
        
        for (String eachJob : allJobs) {
            if (!firstLine) {
                String[] jobDet = eachJob.split(BE.TF.sp);
                String jobDesc = jobDet[3];

                if (!jobDesc.equals("PT")) {
                    String empId = jobDet[1];

                    if (selectedEmployee != null) {
                        if (!selectedEmployee.equals("ALL")) {
                            if (empId.equals(selectedEmployee.toLowerCase())) {
                                employeeJobs.add(eachJob);
                            }
                        }
                        else {
                            employeeJobs.add(eachJob);
                        }
                    }
                }
            }
            
            firstLine = false;
        }
        
        for (String eachJob : employeeJobs) {
            if (selectedMonthYear != null) {
                String[] monthYear = selectedMonthYear.split("-");
                LocalDate firstDay = LocalDate.of(Integer.valueOf(monthYear[1]), Integer.valueOf(monthYear[0]), 1);
                int totalDays = firstDay.lengthOfMonth();
                LocalDate lastDay = firstDay.withDayOfMonth(totalDays);

                String[] jobDet = eachJob.split(BE.TF.sp);

                String jobDate = jobDet[6];

                if (!jobDate.equals(BE.TF.empty)) {
                    LocalDate compDate = BE.DTF.formatDate(jobDate);

                    if ((compDate.isEqual(firstDay) || compDate.isAfter(firstDay)) && 
                        (compDate.isEqual(lastDay) || compDate.isBefore(lastDay))) {

                        String jobId = jobDet[0];
                        String userId = jobDet[1];
                        String compId = jobDet[2];
                        String assignedJob = BE.findJobDetailsUsingDescriptionOrId(jobDet[3], null).split(BE.TF.sp)[2];
                        String startTime = jobDet[7];
                        String endDateTime = jobDet[8];
                        String updatedBy = jobDet[11];

                        String[] data = {jobDate, jobId.toUpperCase(), userId.toUpperCase(), compId.toUpperCase(), assignedJob,
                                         startTime, endDateTime, updatedBy.toUpperCase()};
                        String line = "";
                        for (String eachData : data) {
                            line = line + eachData + BE.TF.sp;
                        }
                        dateJobs.add(line);
                    }
                }
            }
        }


        for (int item1 = 0; item1 < dateJobs.size()-1; item1++) {
            for (int item2 = item1+1; item2 < dateJobs.size(); item2++) {
                String comp1 = dateJobs.get(item1);
                String comp2 = dateJobs.get(item2);

                LocalDate date1 = BE.DTF.formatDate(comp1.split(BE.TF.sp)[0]);
                LocalDate date2 = BE.DTF.formatDate(comp2.split(BE.TF.sp)[0]);

                if (date2.isBefore(date1)) {
                    dateJobs.set(item1, comp2);
                    dateJobs.set(item2, comp1);
                }
            }
        }

        BE.setTableRow(jobTab, dateJobs);

        setJobReportsTableDesign();
    }
    
    
    // table design
    private void setJobReportsTableDesign() {
        int[] columnIgnore = {4};
        int[] columnLength = {100, 100, 110, 110, 235, 90, 130, 100};
        BE.setTableDesign(jobTable, jLabel2, columnLength, columnIgnore);
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
        jSeparator1 = new javax.swing.JSeparator();
        complaintsLabel = new javax.swing.JLabel();
        employeeJobLabel = new javax.swing.JLabel();
        pendingFeeLine2 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        monthCB = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jobTable = new javax.swing.JTable()
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
        jButton1 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        employeeIdCB = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
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

        patrollingLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
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

        complaintsLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        complaintsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        complaintsLabel.setText("Complaints");
        complaintsLabel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                complaintsLabelMouseDragged(evt);
            }
        });
        complaintsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                complaintsLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                complaintsLabelMouseEntered(evt);
            }
        });

        employeeJobLabel.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        employeeJobLabel.setForeground(new java.awt.Color(13, 24, 42));
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

        pendingFeeLine2.setBackground(new java.awt.Color(13, 24, 42));
        pendingFeeLine2.setForeground(new java.awt.Color(13, 24, 42));
        pendingFeeLine2.setText("jTextField1");

        jLabel23.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 51, 51));
        jLabel23.setText("Job Report");

        jLabel24.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 51, 51));
        jLabel24.setText("Month/Year: ");

        monthCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthCBActionPerformed(evt);
            }
        });

        jobTable.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jobTable.setForeground(new java.awt.Color(51, 51, 51));
        jobTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "START DATE", "JOB ID", "EMPLOYEE ID", "COMPLAINT ID", "JOB DESCRIPTION", "START TIME", "END DATE TIME", "UPDATE BY"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jobTable.setIntercellSpacing(new java.awt.Dimension(2, 2));
        jobTable.setRowHeight(30);
        jobTable.setSelectionForeground(new java.awt.Color(51, 51, 51));
        jScrollPane1.setViewportView(jobTable);
        if (jobTable.getColumnModel().getColumnCount() > 0) {
            jobTable.getColumnModel().getColumn(0).setResizable(false);
            jobTable.getColumnModel().getColumn(1).setResizable(false);
            jobTable.getColumnModel().getColumn(2).setResizable(false);
            jobTable.getColumnModel().getColumn(3).setResizable(false);
            jobTable.getColumnModel().getColumn(4).setResizable(false);
            jobTable.getColumnModel().getColumn(5).setResizable(false);
            jobTable.getColumnModel().getColumn(6).setResizable(false);
            jobTable.getColumnModel().getColumn(7).setResizable(false);
        }

        jButton1.setText("View");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(51, 51, 51));
        jLabel25.setText("Employee ID: ");

        employeeIdCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeIdCBActionPerformed(evt);
            }
        });

        jButton2.setText("Repetitive Job");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(438, 438, 438)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(monthCB, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(28, 28, 28)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(employeeIdCB, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton2))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 975, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(patrollingLabel)
                                .addGap(8, 8, 8)
                                .addComponent(complaintsLabel)
                                .addGap(8, 8, 8)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(employeeJobLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(pendingFeeLine2))
                                .addGap(0, 761, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(patrollingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(complaintsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(employeeJobLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(pendingFeeLine2, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel23)
                .addGap(5, 5, 5)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel25)
                        .addComponent(employeeIdCB)
                        .addComponent(jButton2))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(monthCB)))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
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

        logoutPanel1.setBackground(new java.awt.Color(13, 24, 42));
        logoutPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutPanel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutPanel1MouseEntered(evt);
            }
        });

        logoutLabel1.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        logoutLabel1.setForeground(new java.awt.Color(255, 255, 255));
        logoutLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoutIcon.png"))); // NOI18N
        logoutLabel1.setText("LOGOUT");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void patrollingLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_patrollingLabelMouseEntered
        // TODO add your handling code here:
        patrollingLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_patrollingLabelMouseEntered

    private void complaintsLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_complaintsLabelMouseEntered
        // TODO add your handling code here:
        complaintsLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_complaintsLabelMouseEntered

    private void employeeJobLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeeJobLabelMouseEntered
        // TODO add your handling code here:
        employeeJobLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_employeeJobLabelMouseEntered

    private void patrollingLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_patrollingLabelMouseClicked
        // TODO add your handling code here:
        BE.toPatrollingReports(this, BE);
    }//GEN-LAST:event_patrollingLabelMouseClicked

    private void monthCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthCBActionPerformed
        if (monthCB.getSelectedItem() != null && employeeIdCB.getSelectedItem() != null) {
            tableSetUp();
        }
    }//GEN-LAST:event_monthCBActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ArrayList<String> tableData = BE.getTableData(jobTab);
        String reportTitle = monthCB.getSelectedItem().toString() + " - " + employeeIdCB.getSelectedItem().toString();
        
        if (!tableData.isEmpty()) {
            new BuildingExecutiveJobReportPage(BE, reportTitle, tableData).setVisible(true);
        }
        else {
            JOptionPane.showMessageDialog (null, "The report has no data...", 
                                                    "GENERATE REPORT", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void employeeIdCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeIdCBActionPerformed
        // TODO add your handling code here:
        if (monthCB.getSelectedItem() != null && employeeIdCB.getSelectedItem() != null) {
            tableSetUp();
        }
    }//GEN-LAST:event_employeeIdCBActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        BE.toRepetitiveJob(BE);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

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

    private void complaintsLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_complaintsLabelMouseClicked
        // TODO add your handling code here:
        BE.toComplaintsReports(BE);
        this.dispose();
    }//GEN-LAST:event_complaintsLabelMouseClicked

    private void complaintsLabelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_complaintsLabelMouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_complaintsLabelMouseDragged

    private void employeeJobLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeeJobLabelMouseClicked
        // TODO add your handling code here:
        BE.toJobReports(BE);
        this.dispose();
    }//GEN-LAST:event_employeeJobLabelMouseClicked

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
            java.util.logging.Logger.getLogger(BuildingExecutiveJobReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuildingExecutiveJobReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuildingExecutiveJobReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuildingExecutiveJobReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BuildingExecutiveJobReports(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BEdashboardInnerPanel;
    private javax.swing.JPanel BEdashboardOuterPanel;
    private javax.swing.JLabel complaintsLabel;
    private javax.swing.JComboBox<String> employeeIdCB;
    private javax.swing.JLabel employeeJobLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel jobAssignationInnerTab;
    private javax.swing.JPanel jobAssignationTab;
    private javax.swing.JTable jobTable;
    private javax.swing.JLabel logoutLabel1;
    private javax.swing.JPanel logoutPanel1;
    private javax.swing.JComboBox<String> monthCB;
    private javax.swing.JLabel patrollingLabel;
    private javax.swing.JTextField pendingFeeLine2;
    // End of variables declaration//GEN-END:variables
}

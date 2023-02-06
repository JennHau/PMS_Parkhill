/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package buildingExecutive;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import pms_parkhill_residence.Users;

/**
 *
 * @author wongj
 */
public class BuildingExecutiveJobReports extends javax.swing.JFrame {
    BuildingExecutive BE = new BuildingExecutive();
    private final Users user;
    DefaultTableModel jobTab;
    /**
     * Creates new form homePage
     * @param user
     */
    public BuildingExecutiveJobReports(Users user) {
        this.user = user;
        initComponents();
        jobTab = (DefaultTableModel) jobTable.getModel();
        setWindowIcon();
        
        monthYearComboBoxSetUp();
        employeeComboBoxSetUp();
    }
    
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
        
        System.out.println(complaintsDateRange);
        return complaintsDateRange;
    }
    
    private void monthYearComboBoxSetUp() {
        ArrayList<String> dateRange = getDateRange();
        
        monthCB.removeAllItems();
        for (String eachDate : dateRange) {
            monthCB.addItem(eachDate);
        }
        
        monthCB.setSelectedItem(0);
    }
    
    private void employeeComboBoxSetUp() {
        employeeIdCB.removeAllItems();
        
        employeeIdCB.addItem("- ALL -");
        
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
                        if (!selectedEmployee.equals("- ALL -")) {
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
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        pendingFeeLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        pendingFeeLabel1 = new javax.swing.JLabel();
        pendingFeeLabel2 = new javax.swing.JLabel();
        pendingFeeLine2 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        monthCB = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jobTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        employeeIdCB = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PARKHILL RESIDENCE");
        setBackground(new java.awt.Color(13, 24, 42));
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setResizable(false);
        setSize(new java.awt.Dimension(1280, 720));

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

        jPanel7.setBackground(new java.awt.Color(13, 24, 42));

        jLabel4.setText("Job Assignation");
        jLabel4.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(13, 24, 42));

        jLabel5.setText("Complaints");
        jLabel5.setBackground(new java.awt.Color(13, 24, 42));
        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));

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

        jPanel10.setBackground(new java.awt.Color(13, 50, 79));

        jLabel8.setText("Reports");
        jLabel8.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));

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

        jPanel11.setBackground(new java.awt.Color(13, 24, 42));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/notificationIcon.png"))); // NOI18N
        jLabel9.setText(" NOTIFICATIONS");
        jLabel9.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(13, 24, 42));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoutIcon.png"))); // NOI18N
        jLabel13.setText("LOGOUT");
        jLabel13.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(13, 24, 42));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/viewProfileIcon.png"))); // NOI18N
        jLabel11.setText("VIEW PROFILE");
        jLabel11.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));

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

        jPanel14.setBackground(new java.awt.Color(13, 24, 42));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoutIcon.png"))); // NOI18N
        jLabel12.setText("LOGOUT");
        jLabel12.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(13, 24, 42));

        jLabel6.setText("Patrolling Management");
        jLabel6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));

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
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(681, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(11, 11, 11)))
        );

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

        pendingFeeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pendingFeeLabel.setText("Patrolling");
        pendingFeeLabel.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        pendingFeeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pendingFeeLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pendingFeeLabelMouseEntered(evt);
            }
        });

        pendingFeeLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pendingFeeLabel1.setText("Complaints");
        pendingFeeLabel1.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        pendingFeeLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pendingFeeLabel1MouseEntered(evt);
            }
        });

        pendingFeeLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pendingFeeLabel2.setText("Employee Job");
        pendingFeeLabel2.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        pendingFeeLabel2.setForeground(new java.awt.Color(13, 24, 42));
        pendingFeeLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pendingFeeLabel2MouseEntered(evt);
            }
        });

        pendingFeeLine2.setText("jTextField1");
        pendingFeeLine2.setBackground(new java.awt.Color(13, 24, 42));
        pendingFeeLine2.setForeground(new java.awt.Color(13, 24, 42));

        jLabel23.setText("Job Report");
        jLabel23.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 51, 51));

        jLabel24.setText("Month/Year: ");
        jLabel24.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 51, 51));

        monthCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        monthCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthCBActionPerformed(evt);
            }
        });

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

        employeeIdCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        employeeIdCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeIdCBActionPerformed(evt);
            }
        });

        jButton2.setText("Repetitive Job");

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
                                .addComponent(pendingFeeLabel)
                                .addGap(8, 8, 8)
                                .addComponent(pendingFeeLabel1)
                                .addGap(8, 8, 8)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(pendingFeeLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(pendingFeeLine2))
                                .addGap(0, 772, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pendingFeeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pendingFeeLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(pendingFeeLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(pendingFeeLine2, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel23)
                .addGap(5, 5, 5)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(monthCB)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel25)
                        .addComponent(employeeIdCB)
                        .addComponent(jButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
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

    private void jPanel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel2MouseEntered

    private void pendingFeeLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pendingFeeLabelMouseEntered
        // TODO add your handling code here:
        pendingFeeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_pendingFeeLabelMouseEntered

    private void pendingFeeLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pendingFeeLabel1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_pendingFeeLabel1MouseEntered

    private void pendingFeeLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pendingFeeLabel2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_pendingFeeLabel2MouseEntered

    private void pendingFeeLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pendingFeeLabelMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_pendingFeeLabelMouseClicked

    private void monthCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthCBActionPerformed
        tableSetUp();
    }//GEN-LAST:event_monthCBActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void employeeIdCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeIdCBActionPerformed
        // TODO add your handling code here:
        tableSetUp();
    }//GEN-LAST:event_employeeIdCBActionPerformed

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
    private javax.swing.JComboBox<String> employeeIdCB;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jobTable;
    private javax.swing.JComboBox<String> monthCB;
    private javax.swing.JLabel pendingFeeLabel;
    private javax.swing.JLabel pendingFeeLabel1;
    private javax.swing.JLabel pendingFeeLabel2;
    private javax.swing.JTextField pendingFeeLine2;
    // End of variables declaration//GEN-END:variables
}

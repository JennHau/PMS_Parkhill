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
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import pms_parkhill_residence.Complaints;

/**
 * 
 * @author wongj
 */
public class BuildingExecutiveJobManagement extends javax.swing.JFrame {
    public static BuildingExecutiveJobManagement BEjobManagement;
    
    private final BuildingExecutive BE;
    private Complaints complaint;
    
    private DefaultTableModel assignedEmployeeTable;
    private DefaultTableModel unassignedEmployeeTable;
    
    private String currentBEid;
    private LocalDate localDate;
    private LocalTime localTime;
    private String selectedRole;
    private String searchText;
    private String selectedEmployeeId;
    private String jobId;
    private String complaintId;
    private boolean fromComplaintPage;
    
    /**
     * Creates new form homePage
     * @param BE
     * @param complaint
     * @param fromComplaintsPage
     * @throws java.io.IOException
     */
    public BuildingExecutiveJobManagement(BuildingExecutive BE, Complaints complaint, boolean fromComplaintsPage) throws IOException 
    {
        BEjobManagement = this;
        this.BE = BE;
        this.complaint = complaint;
        
        this.setCurrentBEid(BE.getUserID());
        
        initComponents();
        runDefaultSetUp(fromComplaintsPage);
    }
    
    // Method to run all default setting when page is open
    private void runDefaultSetUp(boolean fromComplaintsPage) throws IOException {
        assignedEmployeeTable = (DefaultTableModel) assignedEmplyTable.getModel();
        unassignedEmployeeTable = (DefaultTableModel) unassignedEmplyTable.getModel();
        
        setWindowIcon();
        
        // employee job table set up (both table)
        employeeJobTableSetup();
        
        // set BE profile
        setUserProfile();
        
        // check is from complaint page
        setFromComplaintPage(fromComplaintsPage);
        
        // set complaint ID
        if (this.complaint!=null) {
            setComplaintId(this.complaint.getComplaintID());
        }
        
        // if is from complaint page (run this action)
        fromComplaintPageAction(fromComplaintsPage);
    }
    
    private void fromComplaintPageAction(boolean fromComplaintsPage) {
        if (fromComplaintsPage) {
            // if from complaint page (Cannot access assigned job table)
            assignedEmplyTable.setEnabled(!fromComplaintsPage);
            
            if (this.complaint != null) {
                complaintIdTF.setText(complaint.getComplaintID());
            }
        }
        else {
            assignedEmplyTable.setEnabled(!fromComplaintsPage);
            complaintIdTF.setText("-");
        }
    }
        
    private void setUnassignedTableDesign() {
        // design for the table header
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(13, 24, 42));
//        headerRenderer.setHorizontalAlignment(jLabel13.CENTER);
        headerRenderer.setForeground(new Color(255, 255, 255));
        for (int i = 0; i < unassignedEmplyTable.getModel().getColumnCount(); i++) {
            unassignedEmplyTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        // design for the table row
        DefaultTableCellRenderer rowRenderer = new DefaultTableCellRenderer();
//        rowRenderer.setHorizontalAlignment(jLabel13.CENTER);
        for (int i = 0; i < unassignedEmplyTable.getModel().getColumnCount(); i++) {
            if (i != 0) {
                unassignedEmplyTable.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
            }
        }
        
        TableColumnModel columnModel = unassignedEmplyTable.getColumnModel();
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
    }
    
    private void setAssignedTableDesign() {
        // design for the table header
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(13, 24, 42));
//        headerRenderer.setHorizontalAlignment(jLabel13.CENTER);
        headerRenderer.setForeground(new Color(255, 255, 255));
        for (int i = 0; i < assignedEmplyTable.getModel().getColumnCount(); i++) {
            assignedEmplyTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        // design for the table row
        DefaultTableCellRenderer rowRenderer = new DefaultTableCellRenderer();
//        rowRenderer.setHorizontalAlignment(jLabel13.CENTER);
        for (int i = 0; i < assignedEmplyTable.getModel().getColumnCount(); i++) {
            if (i != 0) {
                assignedEmplyTable.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
            }
        }
        
        TableColumnModel columnModel = assignedEmplyTable.getColumnModel();
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
    }
    
    // To set the window Icon
    private void setWindowIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/windowIcon.png")));
    }
    
    // Setup employee job table
    private void employeeJobTableSetup() throws IOException {
        // Set the current date and time for the table results
        setCurrentDateTime();
        
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        dateTimePicker.setDateTimePermissive(localDateTime);
        
        // Update table
        updateTable();
    }
    
    // Method to set current BE (Business Executive) profile
    private void setUserProfile() throws IOException {
        // get current BE details
        // Set text field
        if (currentBEid != null) {
            String beName = BE.getFirstName() + " " + BE.getLastName();
            usernameLabel.setText(beName);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        BEdashboardOuterPanel = new javax.swing.JPanel();
        BEdashboardInnerPanel = new javax.swing.JLabel();
        jobAssignationOuterPanel = new javax.swing.JPanel();
        jobAssignationInnerPanel = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        employeeIdTF = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        unassignedEmplyTable = new javax.swing.JTable()
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
        };
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        assignedEmplyTable = new javax.swing.JTable()
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
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        roleComboBox = new javax.swing.JComboBox<>();
        clearBTN = new javax.swing.JButton();
        clearBTN1 = new javax.swing.JButton();
        dateTimePicker = new com.github.lgooddatepicker.components.DateTimePicker();
        complaintIdTF = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PARKHILL RESIDENCE");
        setBackground(new java.awt.Color(13, 24, 42));
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setResizable(false);
        setSize(new java.awt.Dimension(1280, 720));

        jPanel1.setBackground(new java.awt.Color(13, 24, 42));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/parkhillLogo.png"))); // NOI18N

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
                .addContainerGap(32, Short.MAX_VALUE)
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

        jobAssignationOuterPanel.setBackground(new java.awt.Color(13, 50, 79));
        jobAssignationOuterPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jobAssignationOuterPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jobAssignationOuterPanelMouseEntered(evt);
            }
        });

        jobAssignationInnerPanel.setText("Job Assignation");
        jobAssignationInnerPanel.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jobAssignationInnerPanel.setForeground(new java.awt.Color(255, 255, 255));
        jobAssignationInnerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jobAssignationInnerPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jobAssignationInnerPanelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jobAssignationOuterPanelLayout = new javax.swing.GroupLayout(jobAssignationOuterPanel);
        jobAssignationOuterPanel.setLayout(jobAssignationOuterPanelLayout);
        jobAssignationOuterPanelLayout.setHorizontalGroup(
            jobAssignationOuterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jobAssignationOuterPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jobAssignationInnerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jobAssignationOuterPanelLayout.setVerticalGroup(
            jobAssignationOuterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jobAssignationOuterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jobAssignationInnerPanel)
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
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel5MouseEntered(evt);
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

        jPanel9.setBackground(new java.awt.Color(13, 24, 42));
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

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logoutIcon.png"))); // NOI18N
        jLabel17.setText("LOGOUT");
        jLabel17.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
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
                            .addComponent(BEdashboardOuterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jobAssignationOuterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(BEdashboardOuterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jobAssignationOuterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        usernameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        usernameLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/profileIcon.jpg"))); // NOI18N
        usernameLabel.setText("USERNAME");
        usernameLabel.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        usernameLabel.setForeground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(19, 19, 19))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usernameLabel))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(226, 226, 226));

        employeeIdTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeIdTFActionPerformed(evt);
            }
        });
        employeeIdTF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                employeeIdTFKeyReleased(evt);
            }
        });

        jLabel13.setText(" Employee ID: ");
        jLabel13.setBackground(new java.awt.Color(51, 51, 51));
        jLabel13.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));

        unassignedEmplyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "EMPLOYEE ID", "EMPLOYEE NAME", "POSITION", "ACTION"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        unassignedEmplyTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                unassignedEmplyTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(unassignedEmplyTable);
        if (unassignedEmplyTable.getColumnModel().getColumnCount() > 0) {
            unassignedEmplyTable.getColumnModel().getColumn(0).setResizable(false);
            unassignedEmplyTable.getColumnModel().getColumn(1).setResizable(false);
            unassignedEmplyTable.getColumnModel().getColumn(2).setResizable(false);
            unassignedEmplyTable.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel14.setText("  Unassigned Employee:");
        jLabel14.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));

        jLabel15.setText("  Assigned Employee:");
        jLabel15.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 51));

        jLabel16.setText("Role:");
        jLabel16.setBackground(new java.awt.Color(51, 51, 51));
        jLabel16.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));

        assignedEmplyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "EMPLOYEE ID", "EMPLOYEE NAME", "POSITION", "JOB ID", "JOB ASSIGNED", "ACTION"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        assignedEmplyTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                assignedEmplyTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(assignedEmplyTable);
        if (assignedEmplyTable.getColumnModel().getColumnCount() > 0) {
            assignedEmplyTable.getColumnModel().getColumn(0).setResizable(false);
            assignedEmplyTable.getColumnModel().getColumn(1).setResizable(false);
            assignedEmplyTable.getColumnModel().getColumn(2).setResizable(false);
            assignedEmplyTable.getColumnModel().getColumn(3).setResizable(false);
            assignedEmplyTable.getColumnModel().getColumn(4).setResizable(false);
            assignedEmplyTable.getColumnModel().getColumn(5).setResizable(false);
        }

        jTextField2.setEditable(false);
        jTextField2.setText("jTextField2");
        jTextField2.setBackground(new java.awt.Color(13, 24, 42));
        jTextField2.setForeground(new java.awt.Color(13, 24, 42));

        jTextField3.setText("jTextField3");
        jTextField3.setBackground(new java.awt.Color(13, 24, 42));
        jTextField3.setForeground(new java.awt.Color(13, 24, 42));

        jLabel18.setText("Date & Time: ");
        jLabel18.setBackground(new java.awt.Color(51, 51, 51));
        jLabel18.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));

        roleComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Technician", "Cleaner", "Security Guard" }));
        roleComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roleComboBoxActionPerformed(evt);
            }
        });

        clearBTN.setText("Clear");
        clearBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBTNActionPerformed(evt);
            }
        });

        clearBTN1.setText("Search");
        clearBTN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBTN1ActionPerformed(evt);
            }
        });

        complaintIdTF.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        complaintIdTF.setBackground(new java.awt.Color(51, 51, 51));
        complaintIdTF.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        complaintIdTF.setForeground(new java.awt.Color(51, 51, 51));

        jLabel20.setText("Complaint ID:");
        jLabel20.setBackground(new java.awt.Color(51, 51, 51));
        jLabel20.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 51, 51));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField3)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(employeeIdTF, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(roleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearBTN1)
                        .addGap(0, 38, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel20)
                                .addGap(0, 0, 0)
                                .addComponent(complaintIdTF, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2))))
                .addGap(10, 10, 10))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(employeeIdTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel18)
                        .addComponent(roleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(clearBTN)
                        .addComponent(clearBTN1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(complaintIdTF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 565, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 565, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8))
                            .addComponent(jTextField2))
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void BEdashboardOuterPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BEdashboardOuterPanelMouseEntered
        // TODO add your handling code here:
        BEdashboardOuterPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_BEdashboardOuterPanelMouseEntered

    private void employeeIdTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeIdTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employeeIdTFActionPerformed

    private void roleComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roleComboBoxActionPerformed
        // TODO add your handling code here:
        String role = String.valueOf(roleComboBox.getSelectedItem());
        setSelectedRole(role);
        
        try {
            updateTable();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_roleComboBoxActionPerformed

    private void employeeIdTFKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_employeeIdTFKeyReleased
        // TODO add your handling code here:
        setSearchText(employeeIdTF.getText());
        
        try {
            updateTable();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_employeeIdTFKeyReleased

    private void clearBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBTNActionPerformed
        // TODO add your handling code here:
        employeeIdTF.setText("");
        this.searchText = "";
        try {
            updateTable();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_clearBTNActionPerformed

    private void jobAssignationInnerPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jobAssignationInnerPanelMouseClicked
        // TODO add your handling code here:
        BE.toJobManagement(this, BE, null, false);
    }//GEN-LAST:event_jobAssignationInnerPanelMouseClicked

    private void jobAssignationOuterPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jobAssignationOuterPanelMouseClicked
        // TODO add your handling code here:
        BE.toJobManagement(this, BE, null, false);
    }//GEN-LAST:event_jobAssignationOuterPanelMouseClicked

    private void BEdashboardInnerPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BEdashboardInnerPanelMouseClicked
        // TODO add your handling code here:
        BE.toDashboard(this, BE);
    }//GEN-LAST:event_BEdashboardInnerPanelMouseClicked

    private void BEdashboardOuterPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BEdashboardOuterPanelMouseClicked
        // TODO add your handling code here:
        BE.toDashboard(this, BE);
    }//GEN-LAST:event_BEdashboardOuterPanelMouseClicked

    private void jobAssignationOuterPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jobAssignationOuterPanelMouseEntered
        // TODO add your handling code here:
        jobAssignationOuterPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jobAssignationOuterPanelMouseEntered

    private void unassignedEmplyTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_unassignedEmplyTableMouseClicked
        // TODO add your handling code here:
        int selectedCol = unassignedEmplyTable.getSelectedColumn();
        int selectedRow = unassignedEmplyTable.getSelectedRow();
        
        String employeeData = BE.validateTableSelectionAndGetValue(unassignedEmployeeTable, selectedCol, selectedRow, 3, 0);
        
        if (employeeData != null) {
            setSelectedEmployee(employeeData.toLowerCase());
            BE.toEmployeeJobAssignation(this.BE, selectedEmployeeId, jobId, complaint, fromComplaintPage);
        }
    }//GEN-LAST:event_unassignedEmplyTableMouseClicked

    private void clearBTN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBTN1ActionPerformed
        // TODO add your handling code here:
        this.localDate = dateTimePicker.datePicker.getDate();
        this.localTime = dateTimePicker.timePicker.getTime();
        
        LocalDateTime dateTimeSelected = BE.DTF.combineStringDateTime(String.valueOf(localDate), String.valueOf(localTime));
        LocalDateTime dateTimeNow = LocalDateTime.now();
        
        if (dateTimeSelected.isAfter(dateTimeNow)) {
            try {
                updateTable();
            } catch (IOException ex) {
               ex.printStackTrace();
            }
        }
        else {
            JOptionPane.showMessageDialog (null, "The selected date and time is past... Please select a new date and time", 
                                        "JOB ASSIGNATION ERROR", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_clearBTN1ActionPerformed

    private void assignedEmplyTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_assignedEmplyTableMouseClicked
        // TODO add your handling code here:
        int selectedCol = assignedEmplyTable.getSelectedColumn();
        int selectedRow = assignedEmplyTable.getSelectedRow();
        
        String employeeData = BE.validateTableSelectionAndGetValue(assignedEmployeeTable, selectedCol, selectedRow, 5, 0);
        
        if (employeeData != null) {
            setSelectedEmployee(employeeData);
            this.jobId = (String) assignedEmployeeTable.getValueAt(selectedRow, 3);
            this.selectedEmployeeId = (String) assignedEmployeeTable.getValueAt(selectedRow, 0);
            BE.toEmployeeJobAssignation(this.BE, selectedEmployeeId, jobId, complaint, fromComplaintPage);
        }
    }//GEN-LAST:event_assignedEmplyTableMouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:
        BE.toComplaints(this, this.BE);
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jPanel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseClicked
        // TODO add your handling code here:
        BE.toComplaints(this, this.BE);
    }//GEN-LAST:event_jPanel8MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        try {
            // TODO add your handling code here:
            BE.toPatrollingManagement(this, BE);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jPanel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseClicked
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            BE.toPatrollingManagement(this, BE);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_jPanel9MouseClicked

    private void BEdashboardInnerPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BEdashboardInnerPanelMouseEntered
        // TODO add your handling code here:
        BEdashboardInnerPanel.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_BEdashboardInnerPanelMouseEntered

    private void jobAssignationInnerPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jobAssignationInnerPanelMouseEntered
        // TODO add your handling code here:
        jobAssignationInnerPanel.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jobAssignationInnerPanelMouseEntered

    private void jLabel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseEntered

    private void jPanel8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseEntered
        // TODO add your handling code here:
        jPanel8.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jPanel8MouseEntered

    private void jPanel9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel9MouseEntered
        // TODO add your handling code here:
        jPanel9.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jPanel9MouseEntered

    private void jPanel10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel10MouseEntered
        // TODO add your handling code here:
        jPanel10.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jPanel10MouseEntered

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        // TODO add your handling code here:
        BE.toPatrollingReports(this, BE);
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jPanel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel10MouseClicked
        // TODO add your handling code here:
        BE.toPatrollingReports(this, BE);
    }//GEN-LAST:event_jPanel10MouseClicked

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

    private void updateTable() throws IOException {
        // Red Error from here
        ArrayList<String> unassignedEmplyList = getAssignORunassignList(BE.unassignedEmployee);
        ArrayList<String> assignedEmplyList = getAssignORunassignList(BE.assignedEmployee);
        
        BE.setTableRow(unassignedEmployeeTable, unassignedEmplyList);
        BE.setTableRow(assignedEmployeeTable, assignedEmplyList);
    }
    
    private ArrayList getAssignORunassignList(int assignStatus) throws IOException {
        return BE.getSpecificStatusEmployeeList(localDate, localTime, selectedRole, searchText, assignStatus);
    }
    
    public void setCurrentDateTime() {
        this.localDate = LocalDate.now();
        this.localTime = BE.getTimeCategory(LocalTime.now());
    }
    
    public void setDateTime(LocalDate date, LocalTime time) {
        this.localDate = date;
        this.localTime = time;
    }
    
    private void setSelectedRole (String role) {
        this.selectedRole = role;
    }
    
    private void setSearchText (String text) {
        this.searchText = text;
    }
    
    private void setSelectedEmployee (String employeeID) {
        this.selectedEmployeeId = employeeID;
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
        } 
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuildingExecutiveJobManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new BuildingExecutiveJobManagement(null, null, false).setVisible(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BEdashboardInnerPanel;
    private javax.swing.JPanel BEdashboardOuterPanel;
    private javax.swing.JTable assignedEmplyTable;
    private javax.swing.JButton clearBTN;
    private javax.swing.JButton clearBTN1;
    private javax.swing.JLabel complaintIdTF;
    private com.github.lgooddatepicker.components.DateTimePicker dateTimePicker;
    private javax.swing.JTextField employeeIdTF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel jobAssignationInnerPanel;
    private javax.swing.JPanel jobAssignationOuterPanel;
    private javax.swing.JComboBox<String> roleComboBox;
    private javax.swing.JTable unassignedEmplyTable;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the localDate
     */
    public LocalDate getDate() {
        return localDate;
    }

    /**
     * @return the localTime
     */
    public LocalTime getTime() {
        return localTime;
    }

    /**
     * @param date the localDate to set
     */
    public void setDate(LocalDate date) {
        this.localDate = date;
    }

    /**
     * @param time the localTime to set
     */
    public void setTime(LocalTime time) {
        this.localTime = time;
    }

    /**
     * @return the complaintId
     */
    public String getComplaintId() {
        return complaintId;
    }

    /**
     * @param complaintID
     */
    public void setComplaintId(String complaintID) {
        this.complaintId = complaintID;
    }

    /**
     * @return the fromComplaintPage
     */
    public boolean isFromComplaintPage() {
        return fromComplaintPage;
    }

    /**
     * @param fromComplaintPage the fromComplaintPage to set
     */
    public void setFromComplaintPage(boolean fromComplaintPage) {
        this.fromComplaintPage = fromComplaintPage;
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

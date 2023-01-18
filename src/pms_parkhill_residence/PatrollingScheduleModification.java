/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pms_parkhill_residence;

import java.awt.Toolkit;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Winson
 */
public class PatrollingScheduleModification extends javax.swing.JFrame {
    FileHandling fh = new FileHandling();
    BuildingExecutive BE = new BuildingExecutive();
    DefaultTableModel scheduleTable;
    
    private String currentBEid;
    
    private String patrollingScheduleFile;
    private final String tempFile = "tempPatFile.txt";
    private LocalDate inputDate;
    
    private String timeIntervalSet;
    private String levelIntervalSet;
    
    /**
     * Creates new form EmployeeJobAssignation
     * @param file
     * @param inputDate
     */
    public PatrollingScheduleModification(String file, LocalDate inputDate) {
        initComponents();
        runDefaultSetUp(file, inputDate);
    }
    
    private void runDefaultSetUp(String file, LocalDate inputDate){
        scheduleTable = (DefaultTableModel) scheduleJT.getModel();
        setWindowIcon();
        this.setInputDate(inputDate);
        fileSetUp(file);
        tableSetUp();
        getIntervalSet();
        setEachComboBox();
    }
    
    private void fileSetUp(String file) {
        this.patrollingScheduleFile = file;
        List<String> currentFile = fh.fileRead(patrollingScheduleFile);
        
        fh.fileWrite(tempFile, false, currentFile);
    }
    
    private void tableSetUp() {
        ArrayList<String> toTable = new ArrayList<>();
        List<String> fileDet = fh.fileRead(tempFile);
        boolean firstLine = true;
        for (String eachSche : fileDet) {
            if (!firstLine) {
                String[] sche = eachSche.split(BE.sp);
                toTable.add(sche[1] + BE.sp + sche[2] + BE.sp + sche[3] + BE.sp + sche[4] + BE.sp);
            }
            
            firstLine = false;
        }
        BE.setTableRow(scheduleTable, toTable);
    }
    
    private void getIntervalSet() {
        List<String> getSchedSetting = fh.fileRead(BE.patScheduleModRec);
        for (String eachSet : getSchedSetting) {
            String getDate = eachSet.split(BE.sp)[1];
            if (getDate.equals(inputDate.toString())) {
                timeIntervalSet = eachSet.split(BE.sp)[2];
                levelIntervalSet = eachSet.split(BE.sp)[3];
            }
        }
    }
    
    private void setEachComboBox() {
        hourComboBox.setSelectedItem(timeIntervalSet);
        levelIntervalComboBox.setSelectedItem(levelIntervalSet);
    }
    
    private void tableSettingUpdate() {
        String[] blockList = {"A", "B"};
        ArrayList<String> newSched = new ArrayList<>();
        LocalTime tempTime = LocalTime.of(0, 0, 0);
        
        int timeSequence = (24%Integer.valueOf(timeIntervalSet)!=0) ? (24/Integer.valueOf(timeIntervalSet) + 1) : (24/Integer.valueOf(timeIntervalSet));
        System.out.println(timeSequence);
        
        ArrayList<String> levelSequence = getLevelSequence(Integer.valueOf(levelIntervalSet));
        
        List<String> toGetHeader = fh.fileRead(tempFile);
        newSched.add(toGetHeader.get(0));
        
        int newNo = 1;
        for (int time = 0; time < timeSequence; time++ ) {
            String thisTime = tempTime.toString();
            for (int block = 0; block < 2; block++) {
                String currentBlock = blockList[block];
                for (String eachSeq : levelSequence) {
                    String[] levelANDcheck = eachSeq.split(BE.sp);
                    newSched.add(newNo + BE.sp + thisTime + BE.sp + currentBlock + BE.sp +
                                 levelANDcheck[0] + BE.sp + levelANDcheck[1] + BE.sp + 
                                 BE.formatTime(thisTime).plusHours(1).toString() + BE.sp +
                                 " " + BE.sp + " " + BE.sp + " " + BE.sp + " " + BE.sp + " " +
                                 BE.sp + " " + BE.sp);
                    newNo++;
                }
            }
            
            tempTime = tempTime.plusHours(Integer.valueOf(timeIntervalSet));
        }
        
        fh.fileWrite(tempFile, false, newSched);
        tableSetUp();
    }
    
    private ArrayList getLevelSequence(int selectedLevel) {
        ArrayList<String> levelValue = new ArrayList<>();
        switch (selectedLevel) {
            case 5 -> {
                levelValue.add("Level 1-5;5");
                levelValue.add("Level 6-10;10");
                levelValue.add("Level 11-15;15");
            }
            case 10 -> {
                levelValue.add("Level 1-10;10");
                levelValue.add("Level 10-15;15");
            }
            case 15 -> {
                levelValue.add("Level 1-15;15");
            }
        }
        
        return levelValue;
    }
    
    private void setWindowIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/windowIcon.png")));
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
        jButton1 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        scheduleJT = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        defaultBTN = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        hourComboBox = new javax.swing.JComboBox<>();
        levelIntervalComboBox = new javax.swing.JComboBox<>();
        checkpointComboBox = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PARKHILL RESIDENCE");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setText(" Schedule: ");
        jLabel24.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 51, 51));

        jButton1.setText("Add Row");

        jLabel26.setText(" Slot Gap By: ");
        jLabel26.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(51, 51, 51));

        scheduleJT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Slot", "Block", "Level", "Checkpoints", "Action"
            }
        ));
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

        jLabel30.setText("Checkpoint Level: ");
        jLabel30.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(51, 51, 51));

        jButton3.setText("Back");

        jButton4.setText("Save");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel25.setText(" Modification: ");
        jLabel25.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(51, 51, 51));

        hourComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2", "4", "6", "8", "10", "12" }));
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

        checkpointComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "5", "10", "15" }));

        jLabel31.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(51, 51, 51));
        jLabel31.setText("Level(s)");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(defaultBTN))
                    .addComponent(jSeparator1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(levelIntervalComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkpointComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel31)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(300, 300, 300)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(jButton1)
                    .addComponent(defaultBTN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jLabel25)
                .addGap(4, 4, 4)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkpointComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(hourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(levelIntervalComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
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

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void hourComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hourComboBoxActionPerformed
        // TODO add your handling code here:
        timeIntervalSet = hourComboBox.getSelectedItem().toString();
        tableSettingUpdate();
    }//GEN-LAST:event_hourComboBoxActionPerformed

    private void defaultBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultBTNActionPerformed
        // TODO add your handling code here:
        List<String> defaultSetting = fh.fileRead(BE.fixFile);
        fh.fileWrite(tempFile, false, defaultSetting);
        tableSetUp();
    }//GEN-LAST:event_defaultBTNActionPerformed

    private void levelIntervalComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_levelIntervalComboBoxActionPerformed
        // TODO add your handling code here:
        levelIntervalSet = levelIntervalComboBox.getSelectedItem().toString();
        tableSettingUpdate();
    }//GEN-LAST:event_levelIntervalComboBoxActionPerformed

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
                new PatrollingScheduleModification(null, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> checkpointComboBox;
    private javax.swing.JButton defaultBTN;
    private javax.swing.JComboBox<String> hourComboBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JComboBox<String> levelIntervalComboBox;
    private javax.swing.JTable scheduleJT;
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
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package buildingExecutive;

import java.awt.Toolkit;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pms_parkhill_residence.FileHandling;
import pms_parkhill_residence.Users;

/**
 *
 * @author Winson
 */
public class ScheduleActionPage extends javax.swing.JFrame {
    private Users user;
    FileHandling fh = new FileHandling();
    BuildingExecutive BE = new BuildingExecutive();
    
    private final String patrollingScheduleFile;
    
    private LocalDate inputDate;
    private String currentBEid;
    
    /**
     * Creates new form EmployeeJobAssignation
     * @param users
     * @param scheduleId
     * @param inputDate
     * @param file
     * @throws java.io.IOException
     */
    public ScheduleActionPage(Users users, LocalDate inputDate, String file){
        initComponents();
        runDefaultSetUp(users, inputDate);
        this.patrollingScheduleFile = file;
    }
    
    private void runDefaultSetUp(Users users, LocalDate inputDate) {
        this.user = users;
        this.setCurrentBEid(user.getUserID());
        
        setWindowIcon();
        setInputDate(inputDate);
        
        blockComboBoxSetUp();
        
        levelTF.setEnabled(false);
        checkPTF.setEnabled(false);
        timeSpinner.setEnabled(false);
    }
    
    private void blockComboBoxSetUp() {
        ArrayList<String> blockList = BE.getAvailableBlock();
        for (String eachBlock : blockList) {
            blockTF.addItem(eachBlock);
        }
    }
    
    private void levelComboBoxSetUp(String blockSelected) {
        String[] lvS = {"Level 1", "Level 2", "Level 1-2"};
        
        String[] resLv = {"Level 1", "Level 2", "Level 3", "Level 4", 
                         "Level 5", "Level 6", "Level 7", "Level 8", 
                         "Level 9", "Level 10", "Level 11", "Level 12", 
                         "Level 13", "Level 14", "Level 15", "Level 1-5", 
                         "Level 6-10", "Level 11-15", "Level 1-10", "Level 1-15"};
        
        levelTF.removeAllItems();
        
        if (blockSelected.equals("S")) {
            for (String eachLv : lvS) {
                levelTF.addItem(eachLv);
            }
        }
        else {
            for (String eachLv : resLv) {
                levelTF.addItem(eachLv);
            }
        }
    }
    
    private void checkPointSetUp(String lvSelected) {
        checkPTF.removeAllItems();
        
        String[] splitLv = lvSelected.split(" ");
        String[] lvData = (splitLv[1].length()>1) ? splitLv[1].split("-") : null;
        if (lvData != null) {
            int firstLv = Integer.valueOf(lvData[0]);
            int secLv = Integer.valueOf(lvData[1]);
            
            int lvDiff = secLv - firstLv + 1;
            
            for (int loop = firstLv; loop < lvDiff + 1; loop++) {
                checkPTF.addItem("Level " + String.valueOf(loop));
            }
        }
        else {
            checkPTF.addItem(lvSelected);
        }
    }

    private void setWindowIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/windowIcon.png")));
    }
    
    private void fieldAction(boolean enable) {
        levelTF.setEnabled(enable);
        checkPTF.setEnabled(enable);
        timeSpinner.setEnabled(enable);
        
        slotTimePicker.setText("");
        blockTF.setSelectedIndex(0);
        levelTF.removeAllItems();
        checkPTF.removeAllItems();
        timeSpinner.setSelectedIndex(0);
        endTimeTF.setText("");
    }
    
    private ArrayList getField() {
        ArrayList<String> fieldItems = new ArrayList<>();
        
        String newId = BE.getNewId(patrollingScheduleFile, 0);
        fieldItems.add(newId);
        
        if (slotTimePicker.getTime()!=null) {
            fieldItems.add(slotTimePicker.getTime().toString());
            
            if (blockTF.getSelectedItem()!=null) {
                fieldItems.add(blockTF.getSelectedItem().toString());
                
                if (levelTF.getSelectedItem()!=null) {
                    fieldItems.add(levelTF.getSelectedItem().toString());
                    
                    if (checkPTF.getSelectedItem()!= null) {
                        fieldItems.add(checkPTF.getSelectedItem().toString());
                        
                        if (endTimeTF.getText()!=null) {
                            fieldItems.add(endTimeTF.getText());
                            return fieldItems;
                        }
                    }
                }
            }
        }
        
        return null;
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
        jLabel16 = new javax.swing.JLabel();
        backBTN = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        checkPTF = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        endTimeTF = new javax.swing.JTextField();
        blockTF = new javax.swing.JComboBox<>();
        levelTF = new javax.swing.JComboBox<>();
        saveBTN = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        slotTimePicker = new com.github.lgooddatepicker.components.TimePicker();
        timeSpinner = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PARKHILL RESIDENCE");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("Checkpoint: ");

        jLabel16.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("Block: ");

        backBTN.setText("Back");
        backBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBTNActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 51, 51));
        jLabel23.setText("Slot:");

        checkPTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkPTFActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));
        jLabel17.setText("Time Needed: ");

        jLabel18.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));
        jLabel18.setText("Level: ");

        endTimeTF.setEnabled(false);

        blockTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blockTFActionPerformed(evt);
            }
        });

        levelTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                levelTFActionPerformed(evt);
            }
        });

        saveBTN.setText("Save");
        saveBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBTNActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 51, 51));
        jLabel19.setText("Check Before: ");

        timeSpinner.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));
        timeSpinner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeSpinnerActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 51, 51));
        jLabel24.setText("Hrs");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(checkPTF, javax.swing.GroupLayout.Alignment.LEADING, 0, 95, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(slotTimePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(blockTF, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(timeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(endTimeTF)
                    .addComponent(levelTF, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 3, Short.MAX_VALUE))
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                    .addComponent(saveBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(blockTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(slotTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(levelTF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkPTF, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endTimeTF, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backBTN)
                    .addComponent(saveBTN))
                .addGap(8, 8, 8))
        );

        jPanel1.setBackground(new java.awt.Color(13, 24, 42));

        jLabel2.setFont(new java.awt.Font("Britannic Bold", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("ADD SLOT");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/parkhillLogo.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void backBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBTNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_backBTNActionPerformed

    private void saveBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBTNActionPerformed
        // TODO add your handling code here:
        ArrayList<String> allFields = getField();
        String newSlot = "";
        if (allFields!=null) {
            for (String eachItem : allFields) {
                newSlot = newSlot + eachItem + BE.sp;
            }
            
            ArrayList<String> newItem = new ArrayList<>();
            
            newSlot = newSlot + " " + BE.sp + " " + BE.sp + " " + BE.sp + " " + BE.sp + " " + BE.sp + 
                      currentBEid + BE.sp + BE.combineStringDateTime(LocalDate.now().toString(), LocalTime.now().toString());
            
            newItem.add(newSlot);
            
            fh.fileWrite(patrollingScheduleFile, true, newItem);
            
            fieldAction(false);
        }
        
        try {
            if (BuildingExecutivePatrollingManagement.BEpatrollingManagement != null) {
                BuildingExecutivePatrollingManagement.BEpatrollingManagement.dispose();
            }
            BE.toPatrollingManagement(this, user);
        } catch (IOException ex) {
            Logger.getLogger(ScheduleActionPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveBTNActionPerformed

    private void blockTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blockTFActionPerformed
        // TODO add your handling code here:
        if (blockTF.getSelectedItem()!= null) {
            levelComboBoxSetUp(blockTF.getSelectedItem().toString());
            levelTF.setEnabled(true);
        }
    }//GEN-LAST:event_blockTFActionPerformed

    private void levelTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_levelTFActionPerformed
        // TODO add your handling code here:
        if (levelTF.getSelectedItem()!=null) {
            checkPointSetUp(levelTF.getSelectedItem().toString());
            checkPTF.setEnabled(true);
        }
    }//GEN-LAST:event_levelTFActionPerformed

    private void timeSpinnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeSpinnerActionPerformed
        // TODO add your handling code here:
        int selHour = Integer.valueOf(timeSpinner.getSelectedItem().toString());
        
        if (slotTimePicker.getTime()!= null) {
            endTimeTF.setText(slotTimePicker.getTime().plusHours(selHour).plusSeconds(1).toString());
            endTimeTF.setEnabled(false);
        }
    }//GEN-LAST:event_timeSpinnerActionPerformed

    private void checkPTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkPTFActionPerformed
        // TODO add your handling code here:
        timeSpinner.setEnabled(true);
    }//GEN-LAST:event_checkPTFActionPerformed

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
            java.util.logging.Logger.getLogger(ScheduleActionPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScheduleActionPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScheduleActionPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScheduleActionPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScheduleActionPage(null, null, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBTN;
    private javax.swing.JComboBox<String> blockTF;
    private javax.swing.JComboBox<String> checkPTF;
    private javax.swing.JTextField endTimeTF;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox<String> levelTF;
    private javax.swing.JButton saveBTN;
    private com.github.lgooddatepicker.components.TimePicker slotTimePicker;
    private javax.swing.JComboBox<String> timeSpinner;
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package securityGuard;

import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import classes.FileHandling;

/**
 *
 * @author andre
 */
public class SecurityGuard_Add_new_incident extends javax.swing.JFrame {

    /**
     * Creates new form SecurityGuard_Add_new_incident
     */
    FileHandling fh = new FileHandling();
    SecurityGuard sg = new SecurityGuard();
    private final SecurityGuard SG;

    public SecurityGuard_Add_new_incident(SecurityGuard SG) {
        initComponents();
        displayIncidentId();
        incidentstatus.setText("PENDING");
        DATE.setText(sg.currentdate() + " " + sg.currenttime());
        this.SG = SG;
        recordedby.setText(SG.getUserID());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jFrame2 = new javax.swing.JFrame();
        button1 = new java.awt.Button();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        incidentstatus = new javax.swing.JTextField();
        jfield = new javax.swing.JLabel();
        jfield1 = new javax.swing.JLabel();
        recordedby = new javax.swing.JTextField();
        jfield2 = new javax.swing.JLabel();
        jfield3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        incident_textarea = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jfield4 = new javax.swing.JLabel();
        incident_no = new javax.swing.JTextField();
        cancel = new javax.swing.JButton();
        DATE = new javax.swing.JTextField();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame2Layout = new javax.swing.GroupLayout(jFrame2.getContentPane());
        jFrame2.getContentPane().setLayout(jFrame2Layout);
        jFrame2Layout.setHorizontalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame2Layout.setVerticalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        button1.setLabel("button1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(13, 24, 42));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Add New Incident");
        jLabel2.setFont(new java.awt.Font("Britannic Bold", 3, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        incidentstatus.setBackground(new java.awt.Color(204, 204, 204));
        incidentstatus.setForeground(new java.awt.Color(0, 0, 0));
        incidentstatus.setEnabled(false);
        incidentstatus.setHighlighter(null);
        incidentstatus.setOpaque(true);
        incidentstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incidentstatusActionPerformed(evt);
            }
        });
        incidentstatus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                incidentstatusKeyReleased(evt);
            }
        });

        jfield.setText("Status :");
        jfield.setFont(new java.awt.Font("SamsungOneUILatin 700C", 1, 14)); // NOI18N
        jfield.setForeground(new java.awt.Color(153, 153, 153));
        jfield.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jfield1.setText("Recorded By :");
        jfield1.setFont(new java.awt.Font("SamsungOneUILatin 700C", 1, 14)); // NOI18N
        jfield1.setForeground(new java.awt.Color(153, 153, 153));
        jfield1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        recordedby.setBackground(new java.awt.Color(204, 204, 204));
        recordedby.setEnabled(false);
        recordedby.setHighlighter(null);
        recordedby.setOpaque(true);
        recordedby.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recordedbyActionPerformed(evt);
            }
        });
        recordedby.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                recordedbyKeyReleased(evt);
            }
        });

        jfield2.setText("Date and Time :");
        jfield2.setFont(new java.awt.Font("SamsungOneUILatin 700C", 1, 14)); // NOI18N
        jfield2.setForeground(new java.awt.Color(153, 153, 153));
        jfield2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jfield3.setText("Incident :");
        jfield3.setFont(new java.awt.Font("SamsungOneUILatin 700C", 1, 14)); // NOI18N
        jfield3.setForeground(new java.awt.Color(153, 153, 153));
        jfield3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        incident_textarea.setColumns(20);
        incident_textarea.setRows(5);
        incident_textarea.setBackground(new java.awt.Color(204, 204, 204));
        incident_textarea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                incident_textareaKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(incident_textarea);

        jButton1.setText("Submit");
        jButton1.setBackground(new java.awt.Color(13, 24, 42));
        jButton1.setEnabled(false);
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jfield4.setText("Incident No :");
        jfield4.setFont(new java.awt.Font("SamsungOneUILatin 700C", 1, 14)); // NOI18N
        jfield4.setForeground(new java.awt.Color(153, 153, 153));
        jfield4.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        incident_no.setBackground(new java.awt.Color(204, 204, 204));
        incident_no.setEnabled(false);
        incident_no.setForeground(new java.awt.Color(0, 0, 0));
        incident_no.setHighlighter(null);
        incident_no.setOpaque(true);
        incident_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incident_noActionPerformed(evt);
            }
        });
        incident_no.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                incident_noKeyReleased(evt);
            }
        });

        cancel.setText("Cancel");
        cancel.setBackground(new java.awt.Color(13, 24, 42));
        cancel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cancel.setForeground(new java.awt.Color(255, 255, 255));
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        DATE.setBackground(new java.awt.Color(204, 204, 204));
        DATE.setEnabled(false);
        DATE.setHighlighter(null);
        DATE.setOpaque(true);
        DATE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DATEActionPerformed(evt);
            }
        });
        DATE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DATEKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jfield4, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jfield3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jfield2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jfield1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jfield, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(recordedby)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                            .addComponent(incidentstatus)
                            .addComponent(incident_no)
                            .addComponent(DATE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jfield4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(incident_no, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jfield, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(incidentstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jfield1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(recordedby, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jfield2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DATE, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jfield3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void incidentstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incidentstatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_incidentstatusActionPerformed

    private void recordedbyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordedbyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_recordedbyActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
//        create a new incident

        String incident = incident_textarea.getText().trim();
        String status = incidentstatus.getText();
        String record = recordedby.getText();
        String no = incident_no.getText().toLowerCase();
        String date = DATE.getText();
        SecurityGuard sg = new SecurityGuard();

        String[] ary = {no + ";" + record + ";" + incident + ";" + date + ";" + status + ";"};
        List<String> arylist = Arrays.asList(ary);
        fh.fileWrite("SG_Incident.txt", true, arylist);

        int result = JOptionPane.showConfirmDialog(null,
                "Incident has been Created Successfully !", "Be ok!", JOptionPane.DEFAULT_OPTION);
        if (result == 0) {
            dispose();
            SecurityGuard_ManageIncident add = new SecurityGuard_ManageIncident(SG);
            add.setVisible(true);
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    public void displayIncidentId() {

//        Always display the latest incident ID
        List<String> row = fh.fileRead("SG_Incident.txt");
        String[] rowary = new String[row.size()];
        row.toArray(rowary);
        String idformat = "icd";
        int maxnum = 0;
        int latest, temp;
        for (int i = 1; i < rowary.length; i++) {
            String s = rowary[i].toString().trim();
            String[] line_split = s.split(";");
            String Iid = line_split[0];
            Iid = Iid.replace("icd", "");
            int int_id = Integer.parseInt(Iid);
            int[] maxx = {int_id};
            maxnum = maxx[0];
            for (int x = 0; x < maxx.length; x++) {
                if (maxx[x] > maxnum) {
                    maxnum = maxx[x];
                }
            }
        }
        maxnum += 1;
        String show = idformat + String.valueOf(maxnum);
        incident_no.setText(show.toUpperCase());
    }

//    ENABLE BUTTON IF TEXT AREA IS FILL
    public void enablebutton() {
        if (incident_textarea.getText().isBlank()) {
            jButton1.setEnabled(false);
        } else {
            jButton1.setEnabled(true);
        }
    }

    private void incident_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incident_noActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_incident_noActionPerformed

    private void incident_noKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_incident_noKeyReleased
        // TODO add your handling code here:
        enablebutton();
    }//GEN-LAST:event_incident_noKeyReleased

    private void incidentstatusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_incidentstatusKeyReleased
        // TODO add your handling code here:
        enablebutton();
    }//GEN-LAST:event_incidentstatusKeyReleased

    private void recordedbyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_recordedbyKeyReleased
        // TODO add your handling code here:
        enablebutton();
    }//GEN-LAST:event_recordedbyKeyReleased

    private void dateTimePicker2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateTimePicker2KeyReleased
        // TODO add your handling code here:
        enablebutton();
    }//GEN-LAST:event_dateTimePicker2KeyReleased

    private void incident_textareaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_incident_textareaKeyReleased
        // TODO add your handling code here:
        enablebutton();
    }//GEN-LAST:event_incident_textareaKeyReleased

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        // TODO add your handling code here:

        dispose();
        new SecurityGuard_ManageIncident(SG).setVisible(true);

    }//GEN-LAST:event_cancelActionPerformed

    private void DATEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DATEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DATEActionPerformed

    private void DATEKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DATEKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_DATEKeyReleased

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
            java.util.logging.Logger.getLogger(SecurityGuard_Add_new_incident.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SecurityGuard_Add_new_incident.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SecurityGuard_Add_new_incident.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SecurityGuard_Add_new_incident.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SecurityGuard_Add_new_incident(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DATE;
    private java.awt.Button button1;
    private javax.swing.JButton cancel;
    private javax.swing.JTextField incident_no;
    private javax.swing.JTextArea incident_textarea;
    private javax.swing.JTextField incidentstatus;
    private javax.swing.JButton jButton1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jfield;
    private javax.swing.JLabel jfield1;
    private javax.swing.JLabel jfield2;
    private javax.swing.JLabel jfield3;
    private javax.swing.JLabel jfield4;
    private javax.swing.JTextField recordedby;
    // End of variables declaration//GEN-END:variables
}

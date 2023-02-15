/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pms_parkhill_residence;

import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wongj
 */
public class AccountExecutiveAddFeeType extends javax.swing.JFrame {

    /**
     * Creates new form homePage
     */
    public AccountExecutiveAddFeeType() {
        initComponents();
        setWindowIcon();
        setTable();
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
        jLabel3 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        feeTypeNameTF = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        targetCB = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        unitTF = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        unitPriceTF = new javax.swing.JTextField();
        addBt = new javax.swing.JButton();
        cancelBt = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel14 = new javax.swing.JLabel();
        squareFeetOption = new javax.swing.JCheckBox();
        warningMessage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PARKHILL RESIDENCE");
        setBackground(new java.awt.Color(13, 24, 42));
        setResizable(false);
        setSize(new java.awt.Dimension(770, 498));

        jPanel3.setBackground(new java.awt.Color(13, 24, 42));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(153, 153, 153), null, null));

        jLabel2.setBackground(new java.awt.Color(13, 24, 42));
        jLabel2.setFont(new java.awt.Font("Britannic Bold", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("ADD FEE TYPE");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/parkhillLogo.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(409, 409, 409))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        feeTypeNameTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                feeTypeNameTFActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Fee Type Name:");

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Target:");

        targetCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-PLEASE SELECT-", "Residential", "Commercial" }));

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("Unit:");

        unitTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unitTFActionPerformed(evt);
            }
        });

        jLabel13.setForeground(new java.awt.Color(153, 153, 153));
        jLabel13.setText("e.g. kWh, KM, CM");

        jLabel6.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Unit Price (RM):");

        unitPriceTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unitPriceTFActionPerformed(evt);
            }
        });
        unitPriceTF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                unitPriceTFKeyTyped(evt);
            }
        });

        addBt.setBackground(new java.awt.Color(0, 204, 0));
        addBt.setText("ADD");
        addBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtActionPerformed(evt);
            }
        });

        cancelBt.setText("BACK");
        cancelBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NO", "FEE TYPE", "TARGET", "UNIT", "UNIT PRICE (RM)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jSeparator1.setForeground(new java.awt.Color(153, 153, 153));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel14.setForeground(new java.awt.Color(153, 153, 153));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("Existing Fee Type Details:");

        squareFeetOption.setForeground(new java.awt.Color(102, 102, 102));
        squareFeetOption.setText("Square Feet");
        squareFeetOption.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                squareFeetOptionMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                squareFeetOptionMouseReleased(evt);
            }
        });

        warningMessage.setForeground(new java.awt.Color(255, 0, 51));
        warningMessage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        warningMessage.setPreferredSize(new java.awt.Dimension(138, 17));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(targetCB, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addComponent(unitPriceTF, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(unitTF, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(squareFeetOption, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addGap(86, 86, 86)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(74, 74, 74)
                                .addComponent(cancelBt, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addBt, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1)
                            .addComponent(feeTypeNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(warningMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(warningMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addBt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cancelBt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(feeTypeNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(targetCB, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(unitTF, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(squareFeetOption))
                                .addGap(0, 0, 0)
                                .addComponent(jLabel13)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(unitPriceTF, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void feeTypeNameTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_feeTypeNameTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_feeTypeNameTFActionPerformed

    private void unitTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unitTFActionPerformed

    private void unitPriceTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitPriceTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unitPriceTFActionPerformed

    private void addBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtActionPerformed
        // TODO add your handling code here:
        String sfcheck = String.valueOf(squareFeetOption.isSelected());
        if(feeTypeNameTF.getText().equals("") || 
                targetCB.getSelectedItem().equals("-PLEASE SELECT-") ||
                (unitTF.getText().equals("") && sfcheck.equals("false"))
                || unitPriceTF.getText().equals("")) {
            warningMessage.setText("Please complete the form!");
        
        } else {
            String feeTypesName = feeTypeNameTF.getText();
            String target = String.valueOf(targetCB.getSelectedItem());
            String unit;
            if (sfcheck.equals("true")) {
                unit = squareFeetOption.getText();
            } else {
                unit = unitTF.getText();
            }
            
            DecimalFormat df = new DecimalFormat("0.00");
            float unitP = Float.parseFloat(unitPriceTF.getText());
            String unitPrice = df.format(unitP);
            
            try {
                AccountExecutive ae = new AccountExecutive();
                boolean check = ae.storeNewFeeTypes(feeTypesName, target, unit, unitPrice);

                if (check){
                    setTable();
                    warningMessage.setText("");
                    JOptionPane.showMessageDialog (null, "Fee Type has been added!", 
                                "ADD FEE TYPE", JOptionPane.INFORMATION_MESSAGE);
                    AccountExecutiveIssueInvoice aei = new AccountExecutiveIssueInvoice();
                    aei.setVisible(true);
                    dispose();
                } else {
                    warningMessage.setText("Fee Type is existed, please proceed to setting to make changes.");
                }
            } catch (Exception e) {
                e.printStackTrace();
        }   
    } 
            
    }//GEN-LAST:event_addBtActionPerformed

    private void cancelBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtActionPerformed
        // TODO add your handling code here:
        dispose();
        AccountExecutiveIssueInvoice aei = new AccountExecutiveIssueInvoice();
        aei.setVisible(true);
    }//GEN-LAST:event_cancelBtActionPerformed

    private void unitPriceTFKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitPriceTFKeyTyped
        // TODO add your handling code here:
        // get value from text field
        String enteredAmt = unitPriceTF.getText();
        //declare decimalPlace variable to store the index of "."
        int decimalPlace = enteredAmt.indexOf(".");
        //declare nDecimalPlace to 0
        int nDecimalPlace=0;
        // if value from text field contains "."

        if (enteredAmt.contains(".")) {
            // count number of decimal place that allowed
            nDecimalPlace = decimalPlace - enteredAmt.length();
        }
        // not allowed for letter and value with 3 decimal places
        if (Character.isLetter(evt.getKeyChar()) || nDecimalPlace == -3)
            evt.consume();
        // try to convert string to double
        try {
            Double.parseDouble(unitPriceTF.getText()+evt.getKeyChar());
        } catch (NumberFormatException e) {
            // to prevent any action on text field
            evt.consume();
        }
    }//GEN-LAST:event_unitPriceTFKeyTyped

    private void squareFeetOptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_squareFeetOptionMouseClicked
        // TODO add your handling code here:
        if (squareFeetOption.isSelected()) {
            unitTF.setEnabled(false);
            unitTF.setText("");
        } else {
            unitTF.setEnabled(true);
        }
    }//GEN-LAST:event_squareFeetOptionMouseClicked

    private void squareFeetOptionMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_squareFeetOptionMouseReleased
        // TODO add your handling code here:
        if (squareFeetOption.isSelected()) {
            unitTF.setEnabled(false);
        } else {
            unitTF.setEnabled(true);
        }
    }//GEN-LAST:event_squareFeetOptionMouseReleased

    public void setTable() {
        try{
            DefaultTableModel tableModel = (DefaultTableModel)jTable1.getModel();
            tableModel.setRowCount(0);
            AccountExecutive ae = new AccountExecutive();
            List<String> feeTypesList = ae.extractFeeTypes("feeTypes.txt");
            String[] feeTypes = new String[feeTypesList.size()];
            feeTypesList.toArray(feeTypes);
            for (int i=0; i<feeTypesList.size(); i++) {
                String[] feeTypesDetail = feeTypes[i].split(";");
                String feeType = feeTypesDetail[0];
                String target = feeTypesDetail[1];
                String unit = feeTypesDetail[2];
                String unitPrice = feeTypesDetail[3];
                String tbData[] = {String.valueOf(i+1), feeType, target, unit,
                    unitPrice};
                tableModel.addRow(tbData);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
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
            java.util.logging.Logger.getLogger(AccountExecutiveAddFeeType.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AccountExecutiveAddFeeType.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AccountExecutiveAddFeeType.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AccountExecutiveAddFeeType.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AccountExecutiveAddFeeType().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBt;
    private javax.swing.JButton cancelBt;
    private javax.swing.JTextField feeTypeNameTF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JCheckBox squareFeetOption;
    private javax.swing.JComboBox<String> targetCB;
    private javax.swing.JTextField unitPriceTF;
    private javax.swing.JTextField unitTF;
    private javax.swing.JLabel warningMessage;
    // End of variables declaration//GEN-END:variables
}

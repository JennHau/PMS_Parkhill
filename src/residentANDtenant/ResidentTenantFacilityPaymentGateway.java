/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package residentANDtenant;

import adminExecutive.*;
import java.awt.Toolkit;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import pms_parkhill_residence.Facility;
import pms_parkhill_residence.FileHandling;
import pms_parkhill_residence.Users;

/**
 *
 * @author wongj
 */
public class ResidentTenantFacilityPaymentGateway extends javax.swing.JFrame {
    public static ResidentTenantFacilityPaymentGateway rtFacPay;
    ResidentTenant RT = new ResidentTenant();
    private final Users user;
    private final Facility fb;
    /**
     * Creates new form homePage
     * @param user
     * @param bookingList
     * @param fb
     */
    public ResidentTenantFacilityPaymentGateway(Users user, List<String> bookingList, Facility fb) {
        this.user = user;
        this.fb = fb;
        initComponents();
        setWindowIcon();
        this.bookingList = bookingList;
        setDefault();
        
        rtFacPay = this;
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
        jLabel4 = new javax.swing.JLabel();
        bookBt = new javax.swing.JButton();
        cancelBT = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        warningLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        totalLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        bookingIDLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        facilityIDLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        unitNoLabel = new javax.swing.JLabel();

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
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("FACILITY BOOKING");

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
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(222, 222, 222))
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

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Booking ID:");

        bookBt.setBackground(new java.awt.Color(13, 50, 79));
        bookBt.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bookBt.setForeground(new java.awt.Color(255, 255, 255));
        bookBt.setText("BOOK");
        bookBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookBtActionPerformed(evt);
            }
        });

        cancelBT.setText("CANCEL");
        cancelBT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBTActionPerformed(evt);
            }
        });

        jLabel14.setForeground(new java.awt.Color(153, 153, 153));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("Booking Details:");

        warningLabel.setForeground(new java.awt.Color(255, 0, 0));
        warningLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        warningLabel.setPreferredSize(new java.awt.Dimension(152, 16));

        jPanel2.setBackground(new java.awt.Color(13, 50, 79));

        totalLabel.setFont(new java.awt.Font("Stencil", 0, 18)); // NOI18N
        totalLabel.setForeground(new java.awt.Color(255, 255, 255));
        totalLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalLabel.setText("TOTAL: RM 0.00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("Booking Unit No:");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "FACILITY NAME", "START TIME", "END TIME", "PRICE (RM)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        bookingIDLabel.setFont(new java.awt.Font("Yu Gothic", 1, 14)); // NOI18N
        bookingIDLabel.setForeground(new java.awt.Color(153, 153, 153));
        bookingIDLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        bookingIDLabel.setText("FBK000000");

        jLabel6.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Facility ID:");

        facilityIDLabel.setFont(new java.awt.Font("Yu Gothic", 1, 14)); // NOI18N
        facilityIDLabel.setForeground(new java.awt.Color(153, 153, 153));
        facilityIDLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        facilityIDLabel.setText("FCT000000");

        jLabel7.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("Booking Date:");

        dateLabel.setFont(new java.awt.Font("Yu Gothic", 1, 14)); // NOI18N
        dateLabel.setForeground(new java.awt.Color(153, 153, 153));
        dateLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dateLabel.setText("0000-00-00");

        unitNoLabel.setFont(new java.awt.Font("Yu Gothic", 1, 14)); // NOI18N
        unitNoLabel.setForeground(new java.awt.Color(153, 153, 153));
        unitNoLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        unitNoLabel.setText("A-01-01");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(warningLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 696, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 696, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(bookingIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(facilityIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(71, 71, 71)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(unitNoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(23, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(289, 289, 289)
                .addComponent(cancelBT, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bookBt, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(warningLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(jLabel6))
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bookingIDLabel)
                            .addComponent(facilityIDLabel)
                            .addComponent(dateLabel)
                            .addComponent(unitNoLabel)))
                    .addComponent(jLabel7))
                .addGap(26, 26, 26)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bookBt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelBT, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    AdminExecutive ae = new AdminExecutive();
    FileHandling fh = new FileHandling();
    List<String> bookingList = new ArrayList<>();
    String bookingID; String facilityID; String facilityName;
    String unitPrice; String totalPrice; String date;
    
    private void setDefault() {
        DefaultTableModel tableModel = (DefaultTableModel)jTable1.getModel();
        for(int i = 0; i<bookingList.size(); i++) {
            String[] bookingDetails = bookingList.get(i).split(";");
            this.bookingID = bookingDetails[0];
            this.facilityID = bookingDetails[1];
            this.facilityName = bookingDetails[2];
            String startTime = bookingDetails[3];
            String endTime = bookingDetails[4];
            this.date = bookingDetails[5];
            String[] tbData = {facilityName, startTime, endTime};
            tableModel.addRow(tbData);
        }
        
        List<String> feeData = ae.extractFacilityBookingFee(facilityID.toLowerCase(),
                    bookingList.size());
        for(int i = 0; i<feeData.size(); i++) {
            String[] feeDetails = feeData.get(i).split(";");
            unitPrice = feeDetails[0];
            totalPrice = feeDetails[1];
        }
        
        for(int i = 0; i<bookingList.size(); i++) {
            tableModel.setValueAt(unitPrice, i, 3);
        }
        
        bookingIDLabel.setText(bookingID); facilityIDLabel.setText(facilityID);
        totalLabel.setText("TOTAL: RM " + totalPrice); dateLabel.setText(date);
//        unitNoLabel.setText(this.user.getUnitNo());
        unitNoLabel.setText("OKOK");
    }
    
    private void cancelBTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBTActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cancelBTActionPerformed

    private void bookBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookBtActionPerformed
        int result = JOptionPane.showConfirmDialog(null,"Are you sure to "
                + "proceed with this booking?", "FACILITY BOOKING",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

        if(result == JOptionPane.YES_OPTION){
            ArrayList<String> newData = new ArrayList<>();
            for(int i = 0; i<bookingList.size(); i++) {
                String[] bookingDetails = bookingList.get(i).split(";");
                String startTime = bookingDetails[3];
                String endTime = bookingDetails[4];
                newData.add(bookingID.toLowerCase() +";"+ facilityID.toLowerCase()
                        +";"+ facilityName +";"+ this.user.getUnitNo()
                        +";"+ date +";"+ startTime +";"+ endTime +";"+ unitPrice
                        +";"+ totalPrice +";"+ String.valueOf(LocalDate.now()) +";");
            } 
            
            RT.toPaymentCredential(user, totalPrice, newData, true, false);
        }
    }//GEN-LAST:event_bookBtActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

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
            java.util.logging.Logger.getLogger(ResidentTenantFacilityPaymentGateway.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ResidentTenantFacilityPaymentGateway.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ResidentTenantFacilityPaymentGateway.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ResidentTenantFacilityPaymentGateway.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ResidentTenantFacilityPaymentGateway(null, null, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bookBt;
    private javax.swing.JLabel bookingIDLabel;
    private javax.swing.JButton cancelBT;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel facilityIDLabel;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JLabel unitNoLabel;
    private javax.swing.JLabel warningLabel;
    // End of variables declaration//GEN-END:variables
}

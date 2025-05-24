/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.BillController;
import controller.UserController;
import dao.BillDAO;
import dao.UserDAO;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import model.Bill;
import model.User;

/**
 *
 * @author hieuk
 */
public class BillManagement extends javax.swing.JFrame {

    /**
     * Creates new form BillManager
     */
    private static final long serialVersionUID = 1L;
    private BillController billController;
    private BillDAO billDAO;
    private Timer searchTimer;

    public BillManagement(User user) {
        initComponents();
        this.setIconImage(
	Toolkit.getDefaultToolkit().getImage(BillManagement.class.getResource("/resources/bill-icon.png")));
        setResizable(false);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                new Home(user).setVisible(true);
            }
        });
        billDAO = new BillDAO();
        billController = new BillController(billDAO, this);
        try {
            updateBillTable(billController.getAllBills());
        } catch (ClassNotFoundException | IOException e) {
        }
        initSearchFunctionality();

    }

    private List<Bill> getDataFromTable() {
        List<Bill> bills = new ArrayList<>();
        DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
        int rowCount = dtm.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            String id = String.valueOf(dtm.getValueAt(i, 0));
            try {
                Bill bill = billController.getBillById(id);
                bills.add(bill);
            } catch (ClassNotFoundException | IOException e) {
            }
        }
        return bills;
    }

    public void updateBillTable(List<Bill> bills) {
        DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
        dtm.setRowCount(0);
        UserDAO userDao = new UserDAO();
        UserController userController = new UserController(userDao);
        bills.forEach(bill -> {
            try {
                User user = userController.getUserById(bill.getAdminId());
                if (user == null) {
                    JOptionPane.showMessageDialog(this, "Không có thông tin người dùng");
                    return;
                }
                dtm.addRow(new Object[]{bill.getId(), bill.getName(), user.getUsername(), bill.getDate(),
                    bill.getTotal()});
            } catch (ClassNotFoundException | IOException e) {
            }
        });
    }

    public Bill getSelectedBill() {
        DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
        int row = table.getSelectedRow();
        if (row == -1) {
            return null;
        }
        String id = String.valueOf(dtm.getValueAt(row, 0));
        try {
            Bill bill = billController.getBillById(id);
            return bill;
        } catch (ClassNotFoundException | IOException e) {
        }
        return null;
    }

    private void initSearchFunctionality() {
        searchTimer = new Timer(300, e -> {
            performSearch();
        });
        searchTimer.setRepeats(false);

        txtFind.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                restartTimer();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                restartTimer();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                restartTimer();
            }

            private void restartTimer() {
                if (searchTimer.isRunning()) {
                    searchTimer.restart();
                } else {
                    searchTimer.start();
                }
            }
        });
    }

    private void performSearch() {
        String keyword = txtFind.getText().trim();
        try {
            if (!keyword.isEmpty()) {
                int searchOption = sortComboBox.getSelectedIndex();
                billController.searchBills(getDataFromTable(), keyword, searchOption);
            } else {
                updateBillTable(billController.getAllBills());
            }
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + ex.getMessage());
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        txtDetail = new javax.swing.JButton();
        txtFind = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        sortComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HaUI Grocery Management");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUẢN LÍ HOÁ ĐƠN");

        table.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã hoá đơn", "Tên khách hàng", "Người tạo", "Ngày tạo", "Tổng tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setMinWidth(100);
            table.getColumnModel().getColumn(0).setMaxWidth(150);
        }

        txtDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/detail-icon.png"))); // NOI18N
        txtDetail.setText("XEM CHI TIẾT");
        txtDetail.setContentAreaFilled(false);
        txtDetail.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        txtDetail.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        txtDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDetailActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Tìm kiếm");

        sortComboBox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        sortComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã hoá đơn", "Ngày tạo" }));
        sortComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(txtDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(52, 52, 52))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDetail, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sortComboBox)
                            .addComponent(txtFind, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                        .addGap(23, 23, 23)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDetailActionPerformed
        // TODO add your handling code here:
        Bill bill = getSelectedBill();
        if (bill == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hoá đơn", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        new BillDetail(bill).setVisible(true);

    }//GEN-LAST:event_txtDetailActionPerformed

    private void sortComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sortComboBoxActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(BillManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(BillManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(BillManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(BillManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new BillManagement().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> sortComboBox;
    private javax.swing.JTable table;
    private javax.swing.JButton txtDetail;
    private javax.swing.JTextField txtFind;
    // End of variables declaration//GEN-END:variables
}

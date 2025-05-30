/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.CategoryController;
import dao.CategoryDAO;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import model.Category;
import model.User;

/**
 *
 * @author hieuk
 */
public class CategoryManagement extends javax.swing.JFrame {

    /**
     * Creates new form CategoryManager
     */
    private static final long serialVersionUID = 1L;
    private CategoryController categoryController;
    private CategoryDAO categoryDAO;
    private Timer searchTimer;

    public CategoryManagement(User user) {
        try {
            initComponents();
            this.setIconImage(Toolkit.getDefaultToolkit()
            .getImage(CategoryManagement.class.getResource("/resources/product-management1.png")));
            addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    new Home(user).setVisible(true);
                }
            });

            categoryDAO = new CategoryDAO();
            categoryController = new CategoryController(categoryDAO, this);
            setResizable(false);
            setLocationRelativeTo(null);
            updateCategoryTable(categoryController.getAllCategories());
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(CategoryManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        initSearchFunctionality();
    }

    public void updateCategoryTable(List<Category> categories) {
        DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
        dtm.setRowCount(0);
        categories.forEach(category -> {
            dtm.addRow(
                    new Object[]{String.valueOf(category.getId()), category.getName(), category.getDescription()});
        });
    }

    public Category getSelectedCategory() {
        DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
        int row = table.getSelectedRow();
        if (row == -1) {
            return null;
        }
        int id = Integer.parseInt(String.valueOf(dtm.getValueAt(row, 0)));
        try {
            Category category = categoryController.getCategoryById(id);
            return category;
        } catch (ClassNotFoundException | IOException e) {
        }
        return null;
    }

    private void initSearchFunctionality() {
        searchTimer = new Timer(500, e -> {
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
                categoryController.searchCategories(keyword);
            } else {
                updateCategoryTable(categoryController.getAllCategories());
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
        btnAdd = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnReplace = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();
        txtFind = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HaUI Grocery Management");
        setMinimumSize(new java.awt.Dimension(800, 700));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUẢN LÝ LOẠI SẢN PHẨM");

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Tên loại sản phẩm", "Mô tả"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setMaxWidth(40);
        }

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/add-icon.png"))); // NOI18N
        btnAdd.setText("THÊM");
        btnAdd.setContentAreaFilled(false);
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/detail-icon.png"))); // NOI18N
        btnView.setText("XEM CHI TIẾT");
        btnView.setContentAreaFilled(false);
        btnView.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnView.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        btnDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/delete-icon.png"))); // NOI18N
        btnDel.setText("XOÁ");
        btnDel.setContentAreaFilled(false);
        btnDel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });

        btnReplace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/edit-icon.png"))); // NOI18N
        btnReplace.setText("SỬA");
        btnReplace.setContentAreaFilled(false);
        btnReplace.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReplace.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnReplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReplaceActionPerformed(evt);
            }
        });

        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/export-to-pdf-icon.png"))); // NOI18N
        btnExport.setText("XUẤT RA PDF");
        btnExport.setContentAreaFilled(false);
        btnExport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        txtFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFindActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("TÌM KIẾM");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReplace, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnView)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtFind, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                                .addGap(24, 24, 24))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnReplace, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        new AddCategory(categoryController, null).setVisible(true);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReplaceActionPerformed

        // TODO add your handling code here:
        Category category = getSelectedCategory();
        if (category == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        new EditCategory(categoryController, category).setVisible(true);

    }//GEN-LAST:event_btnReplaceActionPerformed

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        // TODO add your handling code here:
        Category category = getSelectedCategory();
        if (category == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa không?", "XOÁ", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            try {
                if (!categoryController.deleteCategory(category)) {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (HeadlessException | ClassNotFoundException | IOException e1) {
            }

            try {
                updateCategoryTable(categoryController.getAllCategories());
            } catch (ClassNotFoundException | IOException e1) {
            }
            JOptionPane.showMessageDialog(this, "Xóa thành công");
        }
    }//GEN-LAST:event_btnDelActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        Category category = getSelectedCategory();
        if (category == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        new ProductByCategory(category).setVisible(true);
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        // TODO add your handling code here:
        MessageFormat header = new MessageFormat(jLabel1.getText());
        MessageFormat footer = new MessageFormat("HaUI Grocery");
        try {
            PrintRequestAttributeSet set = new HashPrintRequestAttributeSet();
            set.add(OrientationRequested.PORTRAIT);
            table.print(JTable.PrintMode.FIT_WIDTH, header, footer, true, set, true);
            JOptionPane.showMessageDialog(null, "Print successfully");
        } catch (HeadlessException | PrinterException e1) {
        }
    }//GEN-LAST:event_btnExportActionPerformed

    private void txtFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFindActionPerformed

    }//GEN-LAST:event_txtFindActionPerformed

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
//            java.util.logging.Logger.getLogger(CategoryManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(CategoryManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(CategoryManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(CategoryManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new CategoryManagement().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnReplace;
    private javax.swing.JButton btnView;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtFind;
    // End of variables declaration//GEN-END:variables
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.CategoryController;
import controller.ProductController;
import dao.CategoryDAO;
import dao.ProductDAO;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
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
import model.Product;
import model.User;
import service.ProductSelection;

/**
 *
 * @author hieuk
 */
public class ProductManagement extends javax.swing.JFrame {

    /**
     * Creates new form CategoryManager
     */
    private static final long serialVersionUID = 1L;
    private CategoryController categoryController;
    private ProductController productController;
    private ProductSelection ps;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private Timer searchTimer;
    private Timer sortTimer;
    private boolean isBuying;

    public ProductManagement(User user) {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                new Home(user).setVisible(true);
            }
        });
        try {
            initComponents();
            categoryDAO = new CategoryDAO();
            productDAO = new ProductDAO();
            categoryController = new CategoryController(categoryDAO, null);
            productController = new ProductController(productDAO, this);
            setResizable(false);
            setLocationRelativeTo(null);
            updateProductTable(productDAO.getAll());
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(ProductManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        initSearchFunctionality();
        initSortFunctionality();
    }

    public ProductManagement(boolean isBuying, ProductSelection listener) {
        try {
            initComponents();
            this.ps = listener;
            this.isBuying = isBuying;
            productDAO = new ProductDAO();
            categoryDAO = new CategoryDAO();
            categoryController = new CategoryController(categoryDAO, null);
            productController = new ProductController(productDAO, this);
            setResizable(false);
            setLocationRelativeTo(null);
            updateProductTable(productDAO.getAll());
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(ProductManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        initSearchFunctionality();
        initSortFunctionality();
        checkBuy();
    }

    public void updateProductTable(List<Product> products) {
        DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
        dtm.setRowCount(0);
        products.forEach(product -> {
            try {
                dtm.addRow(new Object[]{String.valueOf(product.getId()), product.getName(),
                    categoryController.getCategoryById(product.getCategoryId()).getName(),
                    String.valueOf(product.getPrice()), String.valueOf(product.getQuantity())});
            } catch (ClassNotFoundException | IOException e) {
            }
        });
    }

    public Product getSelectedProduct() {
        DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
        int row = table.getSelectedRow();
        if (row == -1) {
            return null;
        }
        int id = Integer.parseInt(String.valueOf(dtm.getValueAt(row, 0)));
        try {
            Product product = productController.getProductById(id);
            return product;
        } catch (ClassNotFoundException | IOException e) {
        }
        return null;
    }

    public List<Product> getDataFromTable() {
        List<Product> products = new ArrayList<>();
        DefaultTableModel dtm = (DefaultTableModel) this.table.getModel();
        int rowCount = dtm.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            int id = Integer.parseInt(dtm.getValueAt(i, 0).toString());
            try {
                Product product = productController.getProductById(id);
                products.add(product);
            } catch (ClassNotFoundException | IOException e) {
            }
        }
        return products;
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
                productController.searchProducts(keyword);
            } else {
                updateProductTable(productController.getAllProducts());
            }
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + ex.getMessage());
        }
    }

    private void initSortFunctionality() {
        sortTimer = new Timer(300, e -> {
            int selectedIndex = sortComboBox.getSelectedIndex();

            switch (selectedIndex) {
                case 0 -> productController.sortProductsById(getDataFromTable(), true);
                case 1 -> productController.sortProductsByPrice(getDataFromTable(), true);
                case 2 -> productController.sortProductsByPrice(getDataFromTable(), false);
                default -> {
                }
            }
        });
        sortTimer.setRepeats(false);
        sortComboBox.addActionListener(e -> {
            if (sortTimer.isRunning()) {
                sortTimer.restart();
            } else {
                sortTimer.start();
            }
        });

    }

    public void checkBuy() {
        if (isBuying) {
            btnAdd.setVisible(false);
            btnReplace.setVisible(false);
            btnDel.setVisible(false);
            btnExport.setVisible(false);

            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                    if (e.getClickCount() == 2) {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            int productId = Integer.parseInt((String) (dtm.getValueAt(selectedRow, 0)));
                            int productQuantity = Integer.parseInt((String) (dtm.getValueAt(selectedRow, 4)));

                            int buyQuantity = Integer.parseInt(JOptionPane.showInputDialog("Nhập số lượng: "));
                            if (buyQuantity > productQuantity) {
                                JOptionPane.showMessageDialog(ProductManagement.this, "Không đủ số lượng", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            Product selectedProduct;
                            try {
                                selectedProduct = productController.getProductById(productId);
                                ps.onProductSelected(selectedProduct, buyQuantity);
                                dispose();
                            } catch (ClassNotFoundException | IOException e1) {
                            }
                        }
                    }
                }
            });
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
        btnDel = new javax.swing.JButton();
        btnReplace = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();
        txtFind = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        sortComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 700));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUẢN LÝ SẢN PHẨM");

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Tên loại sản phẩm", "Loại sản phẩm", "Đơn giá", "Số lượng còn"
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
            table.getColumnModel().getColumn(0).setMaxWidth(40);
            table.getColumnModel().getColumn(1).setMinWidth(200);
            table.getColumnModel().getColumn(2).setMinWidth(200);
            table.getColumnModel().getColumn(4).setMinWidth(100);
            table.getColumnModel().getColumn(4).setMaxWidth(100);
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

        sortComboBox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        sortComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Không sắp xếp", "Tăng dần", "Giảm dần" }));
        sortComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortComboBoxActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("SẮP XẾP THEO GIÁ");

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
                        .addComponent(btnExport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtFind, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))))
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
                    .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        new AddProduct(productController, null).setVisible(true);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReplaceActionPerformed
        Product product = getSelectedProduct();
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        new EditProduct(productController, product).setVisible(true);
    }//GEN-LAST:event_btnReplaceActionPerformed

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        Product product = getSelectedProduct();
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa không?", "XOÁ", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            try {
                if (!productController.deleteProduct(product)) {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (HeadlessException | ClassNotFoundException | IOException e1) {
            }

            try {
                updateProductTable(productController.getAllProducts());
            } catch (ClassNotFoundException | IOException e1) {
            }
            JOptionPane.showMessageDialog(this, "Xóa thành công");
        }

    }//GEN-LAST:event_btnDelActionPerformed

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
        // TODO add your handling code here:
        searchTimer = new Timer(100, (ActionEvent e) -> {
            String name1 = txtFind.getText();
            try {
                categoryController.searchCategories(name1);
            }catch (ClassNotFoundException | IOException e1) {
            }
        });
        searchTimer.setRepeats(false);
        txtFind.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                restartTimer();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
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
    }//GEN-LAST:event_txtFindActionPerformed

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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> sortComboBox;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtFind;
    // End of variables declaration//GEN-END:variables
}

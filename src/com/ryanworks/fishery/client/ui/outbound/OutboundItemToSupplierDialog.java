package com.ryanworks.fishery.client.ui.outbound;

import com.ryanworks.fishery.client.delegate.CustomerDelegate;
import com.ryanworks.fishery.client.delegate.InTransactionDelegate;
import com.ryanworks.fishery.client.delegate.SalesDelegate;
import com.ryanworks.fishery.client.ui.outbound.listener.OutboundItemToSupplierDialogListener;
import com.ryanworks.fishery.client.ui.shared.ProgressModalDialog;
import com.ryanworks.fishery.client.ui.shared.table.SalesSupplierSourceTableModel;
import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.InTransactionLineBean;
import com.ryanworks.fishery.shared.bean.SalesBean;
import com.ryanworks.fishery.shared.bean.SalesBucketBean;
import com.ryanworks.fishery.shared.bean.SalesLineBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import com.ryanworks.fishery.shared.util.SwingUtil;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class OutboundItemToSupplierDialog 
    extends javax.swing.JDialog 
    implements ListSelectionListener
{    
    private final OutboundItemToSupplierDialogListener listener;
    private SalesBean salesObj;    
    private List<SalesLineBean> salesLineList;
    private String bucketNo;
    private String supplierId;
    
    private final HashMap<String, SalesLineBean> SalesSupplierSourceResultMap = new HashMap();
    private final SalesSupplierSourceTableModel salesLineTableModel = new SalesSupplierSourceTableModel(SalesSupplierSourceResultMap);
    
    public OutboundItemToSupplierDialog(
            java.awt.Frame parent, 
            boolean modal, 
            OutboundItemToSupplierDialogListener listener, 
            SalesBean salesBean, 
            List<SalesLineBean> salesLineList,
            String supplierId) 
    {
        super(parent, modal);
        this.listener = listener;
        
        if (salesBean == null)
            this.salesObj = new SalesBean();
        else
            this.salesObj = salesBean;
        
        this.salesLineList = salesLineList;
        if (salesLineList==null || salesLineList.size()==0 || salesLineList.get(0)==null)
            this.bucketNo = "";
        else
            this.bucketNo = salesLineList.get(0).getBucketNo();
        this.supplierId = supplierId;
        
        initComponents();
        myInit();
    }
    
    private void myInit()
    {                
        this.jTableSalesLine.getSelectionModel().addListSelectionListener(this);
        this.jTableSalesLine.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSalesLine.setRowHeight(30);
        this.jTableSalesLine.getColumn("").setMinWidth( 80 );
        this.jTableSalesLine.getColumn("").setMaxWidth( 80 );
        this.jTableSalesLine.getColumn("id").setMinWidth( 200 );
        this.jTableSalesLine.getColumn("id").setMaxWidth( 200 );
        
        fillForm();
    }
    
    private void fillForm()
    {
        double totalWeight = 0.0d;
        
        this.salesLineTableModel.removeAll();
        for (SalesLineBean lineBean : salesLineList)
        {
            // Process grouped salesline
            if (this.supplierId != null && "SL".equals(this.supplierId.trim()))
            {
                totalWeight += (lineBean.getWeight() + lineBean.getAddWeight());
                this.salesLineTableModel.addBean(lineBean);
            }
            else
            if (this.supplierId != null && "".equals(this.supplierId.trim()))
            {
                if (lineBean.getSupplierId() == null || lineBean.getSupplierId().length() == 0)
                {
                    totalWeight += (lineBean.getWeight() + lineBean.getAddWeight());
                    this.salesLineTableModel.addBean(lineBean);
                }
            }
            // With single supplier ID
            else
            {
                if (lineBean.getSupplierId() != null && lineBean.getSupplierId().length() > 2)
                {
                    totalWeight += (lineBean.getWeight() + lineBean.getAddWeight());
                    this.salesLineTableModel.addBean(lineBean);
                }
            }
            
        }
        
        if (salesLineList.size() > 0 && salesLineList.get(0)!=null)
            this.jTextFieldItem.setText( salesLineList.get(0).getItemNewName() );
        else
            this.jTextFieldItem.setText("");
        this.jTextFieldTotalWeight.setText( String.valueOf(totalWeight) );
        
        this.jTextFieldCustomerId.setText( "" );
        this.jTextFieldCustomerName.setText( "" );
    }
    
    private boolean fillBean()
    {
        return true;
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) 
    {        
        if (this.jTableSalesLine.getSelectedRow() != -1)
        {
            this.jTextFieldCustomerId.setEditable( true );
        }
        else
        {
            this.jTextFieldCustomerId.setEditable( false );
        }
    }
    
    private void fillSalesList(String customerId)
    {        
        if (customerId == null || customerId.trim().length()==0)
            return;
        
        SalesBean salesObj = SalesDelegate.getInstance().getSalesByCustomerAndDate(customerId, this.salesObj.getDateTime());  
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabelItemName = new javax.swing.JLabel();
        jTextFieldItem = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldTotalWeight = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableSalesLine = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldCustomerId = new javax.swing.JTextField();
        jTextFieldCustomerName = new javax.swing.JTextField();
        jButtonTransfer = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1400, 800));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "船户来货鱼类重量分析", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 1, 18), new java.awt.Color(51, 102, 255))); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 480));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabelItemName.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabelItemName.setText("鱼类:");
        jPanel3.add(jLabelItemName);

        jTextFieldItem.setEditable(false);
        jTextFieldItem.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldItem.setPreferredSize(new java.awt.Dimension(150, 40));
        jPanel3.add(jTextFieldItem);

        jLabel2.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel2.setText("总重量 (KG):");
        jPanel3.add(jLabel2);

        jTextFieldTotalWeight.setEditable(false);
        jTextFieldTotalWeight.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldTotalWeight.setPreferredSize(new java.awt.Dimension(150, 40));
        jPanel3.add(jTextFieldTotalWeight);

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jTableSalesLine.setFont(new java.awt.Font("KaiTi", 0, 20)); // NOI18N
        jTableSalesLine.setModel(salesLineTableModel);
        jTableSalesLine.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(jTableSalesLine);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabel1.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel1.setText("转移给客户帐号:");
        jPanel6.add(jLabel1);

        jTextFieldCustomerId.setEditable(false);
        jTextFieldCustomerId.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldCustomerId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldCustomerId.setPreferredSize(new java.awt.Dimension(100, 40));
        jTextFieldCustomerId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldCustomerIdKeyReleased(evt);
            }
        });
        jPanel6.add(jTextFieldCustomerId);

        jTextFieldCustomerName.setEditable(false);
        jTextFieldCustomerName.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldCustomerName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldCustomerName.setPreferredSize(new java.awt.Dimension(300, 40));
        jPanel6.add(jTextFieldCustomerName);

        jButtonTransfer.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonTransfer.setText("转移");
        jButtonTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTransferActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonTransfer);

        jButtonDelete.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonDelete.setText("删除");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonDelete);

        jPanel4.add(jPanel6, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jButtonOk.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonOk.setText("关闭");
        jButtonOk.setPreferredSize(new java.awt.Dimension(120, 37));
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonOk);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldCustomerIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCustomerIdKeyReleased
        // Clear text on jTextFieldCustomerName
        this.jTextFieldCustomerName.setText( "" );
        
        // Retrieve selected InTransactionLineBean object from jTable
        SalesLineBean salesLine = 
                this.salesLineTableModel.getBeanById( this.jTableSalesLine.getValueAt(this.jTableSalesLine.getSelectedRow(), 0).toString() );
        
        String customerId = this.jTextFieldCustomerId.getText().trim();        
        if ("".equalsIgnoreCase(customerId)) 
        {
            return;
        }
                    
        CustomerBean customerBean = CustomerDelegate.getInstance().getCustomerByCustomerId(customerId);
        
        if (customerBean==null)
        {            
            // do nothing
        }
        else
        {
            this.jTextFieldCustomerName.setText( customerBean.getName() );
            
            this.fillSalesList(customerId);            
        }
    }//GEN-LAST:event_jTextFieldCustomerIdKeyReleased

    private void jButtonTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTransferActionPerformed

        ProgressModalDialog dialog = new ProgressModalDialog(null, true, "转移中...");
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
                @Override
                protected Void doInBackground() throws Exception 
                {
                    String customerName = jTextFieldCustomerName.getText().trim();
                    if (customerName.length()==0)
                    {
                        JOptionPane.showMessageDialog(
                                OutboundItemToSupplierDialog.this, 
                                "请输入客户账号。", 
                                "输入错误", 
                                JOptionPane.ERROR_MESSAGE);
                        jTextFieldCustomerId.requestFocus();
                        return null;
                    }

                    List<SalesLineBean> salesLineList = salesLineTableModel.getCheckedList();
                    if (salesLineList.isEmpty())
                    {
                        JOptionPane.showMessageDialog(
                                OutboundItemToSupplierDialog.this, 
                                "请选择至少一项出货记录。", 
                                "输入错误", 
                                JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                
                    int flag = JOptionPane.showConfirmDialog(
                                OutboundItemToSupplierDialog.this,
                                "<html><h2>您确定要转移至:</h2><br/><h3>" + customerName + "</h3>",
                                "确认转移",
                                JOptionPane.YES_NO_OPTION);

                    if (flag == JOptionPane.YES_OPTION)
                    {
                        CustomerBean newAssignedCustomer = CustomerDelegate.getInstance().getCustomerByCustomerId(jTextFieldCustomerId.getText().trim());

                        for (SalesLineBean salesLineObj : salesLineList)
                        {
                            // Update Table UI
                            salesLineTableModel.deleteBean(salesLineObj);
                            
                            // Change at Inbound Transaction Line object
                            // - Update new Customer ID
                            // - Update new Customer Name
                            if (salesLineObj.getSupplierId() != null && salesLineObj.getSupplierId().length() > 0)
                            {
                                InTransactionLineBean inTranxLineBean = 
                                        InTransactionDelegate.getInstance().getInTransactionLineBySalesLine( salesLineObj.getId() );

                                inTranxLineBean.setCustomerId( newAssignedCustomer.getId() );
                                inTranxLineBean.setCustomerName( newAssignedCustomer.getName() );
                                inTranxLineBean.setUnitPrice( 0.0d );
                                inTranxLineBean.setSaving( 0.0d );

                                InTransactionDelegate.getInstance().saveTransactionLine(inTranxLineBean);
                            }

                            // Check if there is SalesObject exist for the customer on that day.
                            SalesBean existingSalesObj = SalesDelegate.getInstance().getSalesByCustomerAndDate(newAssignedCustomer.getId(), salesLineObj.getDateTime());

                            if (existingSalesObj == null)
                            {
                                // Create new sales object
                                SalesBean salesBean = createNewSales( newAssignedCustomer );

                                // Re-assign existing sales line object
                                salesLineObj.setCustomerId( newAssignedCustomer.getId() );
                                salesLineObj.setSalesId( salesBean.getId() );
                                salesBean.addLine(salesLineObj);

                                // Create new sales bucket object
                                createNewSalesBucket(salesBean, salesLineObj);

                                SalesDelegate.getInstance().saveOrUpdateSales(salesBean);       
                            }
                            else
                            {
                                // Assign to existing sales objct, first retrieve the sales object from db,
                                // then create new sales line and bucket for it. 

                                // Re-assign existing sales line object
                                salesLineObj.setCustomerId( newAssignedCustomer.getId() );
                                salesLineObj.setSalesId( existingSalesObj.getId() );
                                existingSalesObj.addLine(salesLineObj);

                                SalesBucketBean salesBucketBean = 
                                        SalesDelegate.getInstance().getBucketByNumberDateCustomer(salesLineObj.getBucketNo(), salesLineObj.getDateTime(), newAssignedCustomer.getId());
                                if (salesBucketBean == null)
                                    createNewSalesBucket(existingSalesObj, salesLineObj);

                                SalesDelegate.getInstance().saveOrUpdateSales(existingSalesObj);
                            }
                        }
                    }

                    // Refresh list
                    salesLineList = SalesDelegate.getInstance().getSalesLineByGroup(
                                        salesObj.getId(), 
                                        jTextFieldItem.getText().trim(), 
                                        bucketNo);
                    fillForm();
                    return null;
                }

                @Override
                protected void done() {          
                    dialog.dispose();//close the modal dialog
                }
        };

        swingWorker.execute(); // this will start the processing on a separate thread
        SwingUtil.centerDialogOnScreen( dialog );
    }//GEN-LAST:event_jButtonTransferActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        MyLogger.logInfo(getClass(), "Close Button (BOTTOM)");
        this.setVisible(false);
        this.dispose();        
        this.listener.itemToSupplierDialogClosed();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        
        ProgressModalDialog dialog = new ProgressModalDialog(null, true, "删除中...");
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
                @Override
                protected Void doInBackground() throws Exception 
                {
                    int tableRowCount = salesLineTableModel.getList().size();
        
                    for (int row=0 ; row<tableRowCount ; row++) {
                        Boolean selectedRow = Boolean.valueOf(salesLineTableModel.getValueAt(row, 0).toString());

                        if (selectedRow==true) {
                            String salesLineId = String.valueOf(salesLineTableModel.getValueAt(row, 1));

                            for (SalesLineBean lineObj : salesLineList) {
                                if (salesLineId.equals(lineObj.getId())) {
                                    // Delete sales line
                                    SalesDelegate.getInstance().deleteSalesLine(lineObj);
                                 
                                    // Update InTransactionLine
                                    InTransactionLineBean inTranxLineObj = 
                                            InTransactionDelegate.getInstance().getInTransactionLineBySalesLine(lineObj.getId());
                                    
                                    if (inTranxLineObj != null) {                                   
                                        inTranxLineObj.setSalesId("");
                                        inTranxLineObj.setSalesLineId("");
                                        inTranxLineObj.setCustomerId("");
                                        inTranxLineObj.setCustomerName("");

                                        InTransactionDelegate.getInstance().saveTransactionLine(inTranxLineObj);
                                    }
                                }
                            }
                        }            
                    }

                    // Refresh list
                    salesLineList = SalesDelegate.getInstance().getSalesLineByGroup(
                                        salesObj.getId(), 
                                        jTextFieldItem.getText().trim(), 
                                        bucketNo);
                    
                    fillForm(); 
                    return null;
                }

                @Override
                protected void done() {          
                    dialog.dispose();//close the modal dialog
                }
        };

        swingWorker.execute(); // this will start the processing on a separate thread
        SwingUtil.centerDialogOnScreen( dialog );
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        MyLogger.logInfo(getClass(), "Window Closing");
        this.setVisible(false);
        this.dispose();        
        this.listener.itemToSupplierDialogClosed();
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        MyLogger.logInfo(getClass(), "Window Closed");
        this.setVisible(false);
        this.dispose();        
        this.listener.itemToSupplierDialogClosed();
    }//GEN-LAST:event_formWindowClosed

    private SalesBean createNewSales(CustomerBean customer)
    {
        SalesBean salesBean = new SalesBean();
        
        salesBean.setCustomerId( customer.getId() );
        salesBean.setCustomerName( customer.getName() );
        //salesBean.setCustomerTrip( CustomerDelegate.getInstance().getTripNoByCustomerIdAndDate(customer.getId(), this.salesObj.getDateTime()) );
        salesBean.setCustomerTrip( 1 );
        salesBean.setDateTime( this.salesObj.getDateTime() );
        
        return salesBean;
    }
    
    private SalesLineBean createNewSalesLine(SalesBean salesBean, SalesLineBean oldSalesLineBean)
    {
        SalesLineBean salesLineBean = new SalesLineBean();
        salesLineBean.setSalesId( salesBean.getId() );
        salesLineBean.setItemId( oldSalesLineBean.getItemId() );
        salesLineBean.setItemName( oldSalesLineBean.getItemName() );
        salesLineBean.setItemNewName( oldSalesLineBean.getItemNewName() );
        salesLineBean.setItemNameBm( oldSalesLineBean.getItemNameBm() );
        salesLineBean.setBucketNo( "0" );
        salesLineBean.setWeight( oldSalesLineBean.getWeight() );
        salesLineBean.setDateTime( salesBean.getDateTime() );
        salesLineBean.setSupplierId( oldSalesLineBean.getSupplierId() );
        salesLineBean.setSupplierName( oldSalesLineBean.getSupplierName() );
        salesLineBean.setCustomerId( oldSalesLineBean.getCustomerId() );
        salesLineBean.setUnitPrice( oldSalesLineBean.getUnitPrice() );
        salesBean.addLine( salesLineBean );

        return salesLineBean;
    }
        
    private SalesBucketBean createNewSalesBucket(SalesBean salesBean, SalesLineBean salesLineBean)
    {
        SalesBucketBean bucket = 
                SalesDelegate.getInstance().getBucketByNumberDateCustomer(
                        salesLineBean.getBucketNo(), salesBean.getDateTime(), salesLineBean.getCustomerId());
        
        if (bucket != null)            
        {
            return bucket;
        }
        
        SalesBucketBean salesBucketBean = new SalesBucketBean();
        salesBucketBean.setSalesId( salesBean.getId());
        salesBucketBean.setBucketNo( salesLineBean.getBucketNo() );
        salesBucketBean.setDateTime( salesBean.getDateTime() );
        salesBucketBean.setCustomerId( salesLineBean.getCustomerId() );
        salesBean.addBucket(salesBucketBean);

        return salesBucketBean;
    }    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonTransfer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelItemName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableSalesLine;
    private javax.swing.JTextField jTextFieldCustomerId;
    private javax.swing.JTextField jTextFieldCustomerName;
    private javax.swing.JTextField jTextFieldItem;
    private javax.swing.JTextField jTextFieldTotalWeight;
    // End of variables declaration//GEN-END:variables

}

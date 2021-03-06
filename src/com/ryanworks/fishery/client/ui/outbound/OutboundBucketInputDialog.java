package com.ryanworks.fishery.client.ui.outbound;

import com.ryanworks.fishery.client.delegate.SalesDelegate;
import com.ryanworks.fishery.shared.bean.SalesBean;
import com.ryanworks.fishery.shared.bean.SalesBucketBean;
import com.ryanworks.fishery.shared.swing.listener.JTextFieldFocusListener;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;

public class OutboundBucketInputDialog extends javax.swing.JDialog 
{
    private final OutboundSalesPanel parentPanel;
    private final SalesBean salesObj;
    
    public OutboundBucketInputDialog(java.awt.Frame parent, boolean modal, OutboundSalesPanel parentPanel, SalesBean salesObj) 
    {
        super(parent, modal);
        
        this.parentPanel = parentPanel;
        this.salesObj = salesObj;
        
        initComponents();
        myInit();
    }
    
    private void myInit()
    {
        this.jTextFieldBucketNo.addFocusListener( new JTextFieldFocusListener() );
        this.jTextFieldBucketNo.requestFocus();
        this.jTextFieldBucketNo.selectAll();
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
        jPanel1 = new javax.swing.JPanel();
        jTextFieldBucketNo = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel1.setText("输入新桶号");
        getContentPane().add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jTextFieldBucketNo.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jTextFieldBucketNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBucketNoActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldBucketNo, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jButtonOk.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonOk.setText("确定");
        jButtonOk.setPreferredSize(new java.awt.Dimension(120, 35));
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonOk);

        jButtonCancel.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonCancel.setText("取消");
        jButtonCancel.setPreferredSize(new java.awt.Dimension(120, 35));
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldBucketNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBucketNoActionPerformed
        this.jButtonOk.doClick();
    }//GEN-LAST:event_jTextFieldBucketNoActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        
        String bucketNo = this.jTextFieldBucketNo.getText().trim();
        if (bucketNo.length()==0)
        {
            JOptionPane.showMessageDialog(this, "请输入完整的桶号.", "输入错误", JOptionPane.ERROR_MESSAGE);
            
            this.jTextFieldBucketNo.requestFocus();
            this.jTextFieldBucketNo.selectAll();
        }
        else
        if ("0".equals(bucketNo))
        {
            JOptionPane.showMessageDialog(this, "请输入0以外的桶号.", "输入错误", JOptionPane.ERROR_MESSAGE);
            
            this.jTextFieldBucketNo.requestFocus();
            this.jTextFieldBucketNo.selectAll();
        }
        else
        {
            Calendar salesdate = Calendar.getInstance();
            salesdate.setTimeInMillis(salesObj.getDateTime());
            
            List<SalesBucketBean> bucketList = SalesDelegate.getInstance().getBucketListByDate(salesdate.getTimeInMillis());
            boolean duplicatedBucketFound = false;
            for (SalesBucketBean bucketObj : bucketList) {
                if (bucketObj.getBucketNo().trim().equals(bucketNo)) {
                    duplicatedBucketFound = true;
                    break;
                }
            }
            
            if (duplicatedBucketFound) {
                JOptionPane.showMessageDialog(this, "发现重复桶号，请输入其他桶号。", "输入错误", JOptionPane.ERROR_MESSAGE);            
                this.jTextFieldBucketNo.requestFocus();
                this.jTextFieldBucketNo.selectAll();
            } else {            
                this.parentPanel.bucketInputDialogClosed( bucketNo );
                this.setVisible(false);
                this.dispose();
            }
        }
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextFieldBucketNo;
    // End of variables declaration//GEN-END:variables
}

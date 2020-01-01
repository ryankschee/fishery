package com.ryanworks.fishery.client.ui.control;

import com.ryanworks.fishery.client.ui.control.listener.SupplierCashEditorDialogListener;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.bean.SupplierCashBean;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import com.ryanworks.fishery.shared.swing.listener.JDateFieldFocusListener;
import com.ryanworks.fishery.shared.swing.listener.JTextFieldFocusListener;
import java.text.DecimalFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Ryan C
 */
public class SupplierCashEditorDialog 
    extends javax.swing.JDialog 
{
    public final static int EDITOR_MODE_NEW     = 1;
    public final static int EDITOR_MODE_EDIT    = 2;
    private final int[] DAYS_IN_MONTH = {31,29,31,30,31,30,31,31,30,31,30,31};
    
    private final DecimalFormat twoPlacesFormatter = new DecimalFormat("0.00");
    
    private int editingMode;
    private SupplierBean supplierObj;
    private SupplierCashBean cashObj;
    private final SupplierCashEditorDialogListener listener;
    
    /**
     * Creates new form ItemEditorDialog
     * @param parent
     * @param modal
     * @param listener
     * @param cashBean
     */
    public SupplierCashEditorDialog(java.awt.Frame parent, boolean modal, SupplierCashEditorDialogListener listener, SupplierBean supplier, SupplierCashBean cashBean) 
    {
        super(parent, modal);
        this.supplierObj = supplier;
        this.cashObj = cashBean;
        this.listener = listener;
        
        initComponents();
        myInit();
    }
    
    private void myInit()
    {
        if (this.cashObj == null)
        {
            this.editingMode = EDITOR_MODE_NEW;
            this.cashObj = new SupplierCashBean();
            this.cashObj.setSupplierId( this.supplierObj.getId() );
        }
        else
        {
            this.editingMode = EDITOR_MODE_EDIT;
        }
                
        this.jTextFieldDate.addFocusListener( new JDateFieldFocusListener() );
        this.jTextFieldDesc.addFocusListener( new JTextFieldFocusListener() );
        this.jTextFieldAmount.addFocusListener( new JTextFieldFocusListener() );
        
        this.fillForm();
    }

    private void fillForm()
    {
        if (this.editingMode == SupplierCashEditorDialog.EDITOR_MODE_NEW)
        {
            this.jTextFieldId.setText( this.cashObj.getId() );
            this.jTextFieldSupplierId.setText( this.cashObj.getSupplierId() );
            this.jTextFieldDate.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTimeInMillis()) );
        }
        else
        if (this.editingMode == SupplierCashEditorDialog.EDITOR_MODE_EDIT)
        {
            this.jTextFieldId.setText( this.cashObj.getId() );
            this.jTextFieldSupplierId.setText( this.cashObj.getSupplierId() );
            this.jTextFieldDate.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format(this.cashObj.getCashDate()) );
            this.jTextFieldDesc.setText( this.cashObj.getCashDesc());
            this.jTextFieldAmount.setText( twoPlacesFormatter.format( this.cashObj.getCashAmount()) );
        }
    }
    
    private long getSelectedDateInMillis(JTextField jTextField)
    {
        String dateInString = jTextField.getText().trim();
        
        if (dateInString.length()==0)
            return 0;
        
        String dayInString = dateInString.substring(0, dateInString.indexOf("/")).trim();
        String monthInString = dateInString.substring(dateInString.indexOf("/")+1, dateInString.lastIndexOf("/")).trim();
        String yearInString = dateInString.substring(dateInString.lastIndexOf("/")+1).trim();
                        
        try {
            int day = Integer.parseInt(dayInString);
            int month = Integer.parseInt(monthInString);
            int year = Integer.parseInt(yearInString);
            
            if (0 > (month-1) || (month-1) > 11)
                return -1;
            if (year < 1990 || year > 2099)
                return -1;
            if (day < 1 || day > DAYS_IN_MONTH[month-1])
                return -1;
            
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, day);
            calendar.set(Calendar.MONTH, month-1);
            calendar.set(Calendar.YEAR, year);
            
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.AM_PM, Calendar.AM);
                        
            return calendar.getTimeInMillis();
        }
        catch (NumberFormatException e) {
            return -1;
        }
        catch (Exception e) {
            return -1;
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldDesc = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldAmount = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldSupplierId = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldDate = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 153, 255));

        jLabel1.setFont(new java.awt.Font("KaiTi", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("海春鱼行");
        jPanel1.add(jLabel1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "来银交易", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N

        jLabel2.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabel2.setText("ID:");

        jTextFieldId.setEditable(false);
        jTextFieldId.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabel3.setText("摘要：");

        jTextFieldDesc.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jTextFieldDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDescActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabel5.setText("银额：");

        jTextFieldAmount.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jTextFieldAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldAmountActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabel6.setText("船户：");

        jTextFieldSupplierId.setEditable(false);
        jTextFieldSupplierId.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabel4.setText("日期：");

        jTextFieldDate.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jTextFieldDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldId)
                            .addComponent(jTextFieldSupplierId)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel4))
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldDesc)
                            .addComponent(jTextFieldAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                            .addComponent(jTextFieldDate))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldSupplierId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextFieldAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jButtonOk.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonOk.setText("确定");
        jButtonOk.setPreferredSize(new java.awt.Dimension(100, 29));
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonOk);

        jButtonCancel.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonCancel.setText("取消");
        jButtonCancel.setPreferredSize(new java.awt.Dimension(100, 29));
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCancel);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        try 
        {
            long dateInMillis = this.getSelectedDateInMillis( this.jTextFieldDate );
            
            if (dateInMillis ==0)
            {
                this.jTextFieldDate.requestFocus();
                this.jTextFieldDate.selectAll();

                JOptionPane.showMessageDialog(
                        this, 
                        "Invalid 'Date' format. Please insert a valid date.", 
                        "Invalid Input", 
                        JOptionPane.ERROR_MESSAGE);
                
                return;
            }
            
            double amount = Double.parseDouble( this.jTextFieldAmount.getText() );
            
            this.cashObj.setCashDate(dateInMillis);
            this.cashObj.setCashDesc(this.jTextFieldDesc.getText().trim());
            this.cashObj.setCashAmount(amount);
            
            this.listener.supplierCashEditorDialogClosed( this.cashObj );
            this.setVisible( false );
            this.dispose();
        }
        catch (NumberFormatException e)
        {
            this.jTextFieldAmount.requestFocus();
            this.jTextFieldAmount.selectAll();
            
            JOptionPane.showMessageDialog(
                    this, 
                    "Invalid 'Amount' format. Must be decimal value.", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
       this.setVisible( false );
       this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jTextFieldDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDescActionPerformed
        this.jTextFieldAmount.requestFocus();
        this.jTextFieldAmount.selectAll();
    }//GEN-LAST:event_jTextFieldDescActionPerformed

    private void jTextFieldAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldAmountActionPerformed
        this.jButtonOk.doClick();
    }//GEN-LAST:event_jTextFieldAmountActionPerformed

    private void jTextFieldDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDateActionPerformed
        long dateInMillis = this.getSelectedDateInMillis( this.jTextFieldDate );
            
        if (dateInMillis ==0)
        {
            this.jTextFieldDate.requestFocus();
            this.jTextFieldDate.selectAll();

            JOptionPane.showMessageDialog(
                    this, 
                    "Invalid 'Date' format. Please insert a valid date.", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);

            return;
        }
        else
        {
            this.jTextFieldDesc.requestFocus();
            this.jTextFieldDesc.selectAll();
        }
    }//GEN-LAST:event_jTextFieldDateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextFieldAmount;
    private javax.swing.JTextField jTextFieldDate;
    private javax.swing.JTextField jTextFieldDesc;
    private javax.swing.JTextField jTextFieldId;
    private javax.swing.JTextField jTextFieldSupplierId;
    // End of variables declaration//GEN-END:variables
}

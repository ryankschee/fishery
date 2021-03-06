package com.ryanworks.fishery.client.ui.control;

import com.ryanworks.fishery.client.delegate.CustomerDelegate;
import com.ryanworks.fishery.client.ui.control.listener.CustomerEditorDialogListener;
import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.swing.listener.JTextAreaFocusListener;
import com.ryanworks.fishery.shared.swing.listener.JTextFieldFocusListener;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

public class CustomerEditorDialog 
    extends javax.swing.JDialog 
{
    public final static int EDITOR_MODE_NEW     = 1;
    public final static int EDITOR_MODE_EDIT    = 2;
    
    private final DecimalFormat twoPlacesFormatter = new DecimalFormat("0.00");
    
    private int editingMode;
    private CustomerBean customerObj;
    private final CustomerEditorDialogListener listener;
    
    public CustomerEditorDialog(java.awt.Frame parent, boolean modal, CustomerEditorDialogListener listener, CustomerBean customerBean) 
    {
        super(parent, modal);
        this.customerObj = customerBean;
        this.listener = listener;
        
        initComponents();
        myInit();
    }
    
    public CustomerEditorDialog(javax.swing.JDialog parent, boolean modal, CustomerEditorDialogListener listener, CustomerBean customerBean) 
    {
        super(parent, modal);
        this.customerObj = customerBean;
        this.listener = listener;
        
        initComponents();
        myInit();
    }
    
    private void myInit()
    {
        if (this.customerObj == null)
        {
            this.editingMode = EDITOR_MODE_NEW;
            this.customerObj = new CustomerBean();
            this.jTextFieldId.addFocusListener( new JTextFieldFocusListener() ); 
        }
        else
        {
            this.editingMode = EDITOR_MODE_EDIT;
        }
                
        this.jTextFieldName.addFocusListener( new JTextFieldFocusListener() );
        this.jTextAreaNotes.addFocusListener( new JTextAreaFocusListener() );
        
        this.fillForm();
    }

    private void fillForm()
    {
        if (this.editingMode == CustomerEditorDialog.EDITOR_MODE_NEW)
        {
            this.jTextFieldId.setEditable(true);
            this.jTextFieldId.setText( this.customerObj.getId() );
            this.jTextFieldId.requestFocus();
            this.jTextFieldId.selectAll();
        }
        else
        if (this.editingMode == CustomerEditorDialog.EDITOR_MODE_EDIT)
        {            
            this.jTextFieldId.setEditable(false);
            this.jTextFieldId.setText( this.customerObj.getId() );
            this.jTextFieldName.setText( this.customerObj.getName() );
            this.jTextAreaNotes.setText( this.customerObj.getNotes() );
            
            this.jTextFieldName.requestFocus();
            this.jTextFieldName.selectAll();
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
        jTextFieldName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaNotes = new javax.swing.JTextArea();
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "客户资料", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N

        jLabel2.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabel2.setText("* ID:");

        jTextFieldId.setEditable(false);
        jTextFieldId.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabel3.setText("客户名字:");

        jTextFieldName.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jTextFieldName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNameActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabel5.setText("其他：");

        jTextAreaNotes.setColumns(20);
        jTextAreaNotes.setRows(5);
        jScrollPane1.setViewportView(jTextAreaNotes);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel2))
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldId)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                    .addComponent(jTextFieldName))
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
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
                .addContainerGap())
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
        
        // If new object request, check the ID
        if (this.editingMode == CustomerEditorDialog.EDITOR_MODE_NEW)
        {
            String customerId = this.jTextFieldId.getText().trim();
            if (customerId.length()==0)
            {
                JOptionPane.showMessageDialog(
                     this, 
                     "ID 格式错误，请您输入正确的客户 ID。", 
                     "输入错误", 
                     JOptionPane.ERROR_MESSAGE);
                this.jTextFieldId.requestFocus();

                return;
            }
            
            CustomerBean tmpBean = CustomerDelegate.getInstance().getCustomerByCustomerId( customerId );
            if (tmpBean != null)
            {
                JOptionPane.showMessageDialog(
                     this, 
                     "发现　ID 重复，请您输入正确的客户 ID。", 
                     "输入错误", 
                     JOptionPane.ERROR_MESSAGE);
                this.jTextFieldId.requestFocus();

                return;
            }
        }

         this.customerObj.setId(this.jTextFieldId.getText().trim());
         this.customerObj.setName(this.jTextFieldName.getText().trim());
         this.customerObj.setNotes(this.jTextAreaNotes.getText().trim());

         this.listener.customerEditorDialogClosed( this.customerObj );
         this.setVisible( false );
         this.dispose();        
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
       this.setVisible( false );
       this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jTextFieldNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNameActionPerformed
        this.jTextAreaNotes.requestFocus();
        this.jTextAreaNotes.selectAll();
    }//GEN-LAST:event_jTextFieldNameActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaNotes;
    private javax.swing.JTextField jTextFieldId;
    private javax.swing.JTextField jTextFieldName;
    // End of variables declaration//GEN-END:variables
}

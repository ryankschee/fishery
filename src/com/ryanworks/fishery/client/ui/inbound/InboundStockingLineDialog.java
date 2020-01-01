package com.ryanworks.fishery.client.ui.inbound;

import com.ryanworks.fishery.client.delegate.CustomerDelegate;
import com.ryanworks.fishery.client.delegate.InTransactionDelegate;
import com.ryanworks.fishery.client.delegate.SalesDelegate;
import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.InTransactionBean;
import com.ryanworks.fishery.shared.bean.InTransactionLineBean;
import com.ryanworks.fishery.shared.bean.SalesBean;
import com.ryanworks.fishery.shared.bean.SalesBucketBean;
import com.ryanworks.fishery.shared.bean.SalesLineBean;
import com.ryanworks.fishery.shared.util.MyLogger;
import java.awt.Color;
import javax.swing.JOptionPane;

public class InboundStockingLineDialog 
    extends javax.swing.JDialog 
{
    private final InboundStockingPanel parentPanel;
    private final InTransactionBean inTransactionObj;
    private final InTransactionLineBean lineBean;
    private SalesBean salesObj;
    private boolean isCustomerIdValid = true;
    private boolean isNewLineBean = false;
    
    /**
     * Creates new form JDialog
     */
    public InboundStockingLineDialog(
            java.awt.Frame parent, 
            boolean modal, 
            InboundStockingPanel parentPanel, 
            InTransactionBean inTransactionObj, 
            InTransactionLineBean lineBean,
            boolean newLineBean) 
    {
        super(parent, modal);
        this.parentPanel = parentPanel;
        this.inTransactionObj = inTransactionObj;
        
        if (lineBean == null) {
            this.isNewLineBean = newLineBean;
            this.lineBean = new InTransactionLineBean();
            this.salesObj = null;
        } else {
            this.isNewLineBean = newLineBean;
            this.lineBean = lineBean;
            this.salesObj = null;
        }
        
        initComponents();
        myInit();
    }
    
    private void myInit()
    {
        fillForm();
                
        this.jTextFieldWeight.requestFocus();
        this.jTextFieldWeight.selectAll();
    }
    
    private void fillForm()
    {
        this.jTextFieldItemId.setText( lineBean.getItemId() );
        this.jTextFieldItemName.setText( lineBean.getItemNewName() );
        this.jTextFieldWeight.setText( String.valueOf(lineBean.getWeight()) );
        this.jTextFieldCustomerId.setText( lineBean.getCustomerId() );
        this.jTextFieldCustomerName.setText( lineBean.getCustomerName() );
        this.jTextFieldBucketNo.setText( lineBean.getBucketNo() );
        
        if (this.isNewLineBean)
            jTextFieldCustomerId.setEditable(true);
        else
            jTextFieldCustomerId.setEditable(false);
    }
    
    private boolean fillBean()
    {
        try {
            // Store initial Sales ID for later use
            String originalSalesId = this.lineBean.getSalesId();

            // Set Weight
            this.lineBean.setWeight( Double.parseDouble( this.jTextFieldWeight.getText().trim() ) );
            this.lineBean.setItemNewName( this.jTextFieldItemName.getText().trim() );
                        
            // If invalid customer ID found, then do followings.
            if (this.isCustomerIdValid == false)
            {
                // If customer ID is not empty, then use it to create a new account.
                if ("".equalsIgnoreCase(this.jTextFieldCustomerId.getText().trim())==false)
                {
                    CustomerBean customerObj = new CustomerBean();
                    customerObj.setId( this.jTextFieldCustomerId.getText().trim() );
                    customerObj.setName( "新客户" );
                    
                    CustomerDelegate.getInstance().saveOrUpdateCustomer(customerObj);

                    this.lineBean.setCustomerId( customerObj.getId() );
                    this.lineBean.setCustomerName( customerObj.getName() );
                }            
            }
            else
            {
                this.lineBean.setCustomerId( this.jTextFieldCustomerId.getText().trim() );
                this.lineBean.setCustomerName( this.jTextFieldCustomerName.getText().trim() );
            }
                    
            // Set bucket no.
            if ("".equalsIgnoreCase(this.jTextFieldBucketNo.getText().trim()))
            {
                this.lineBean.setBucketNo("0");
            }
            else
            {
                this.lineBean.setBucketNo(this.jTextFieldBucketNo.getText().trim());
            }
            
            // Set Sales ID, if customer ID not empty
            if ("".equals(this.jTextFieldCustomerId.getText().trim()) == false)
            {                
                salesObj = SalesDelegate.getInstance().getSalesByCustomerAndDate(
                                this.jTextFieldCustomerId.getText().trim(),
                                this.inTransactionObj.getDateTime());
                // No sales bean found for the customer on that day. 
                if (salesObj == null)
                {
                    // Create new sales object into database.
                    salesObj = new SalesBean();
                    salesObj.setCustomerId( this.lineBean.getCustomerId() );
                    salesObj.setCustomerName( this.lineBean.getCustomerName() );
                    salesObj.setCustomerTrip(1);
                    salesObj.setDateTime(this.inTransactionObj.getDateTime());
                    
                    SalesDelegate.getInstance().saveOrUpdateSales(salesObj);
                    
                    // Set Sales ID into line bean
                    this.lineBean.setSalesId(salesObj.getId());
                }
                else
                {
                    // Set Sales ID into line bean
                    this.lineBean.setSalesId( salesObj.getId() );
                }
                
                if (this.lineBean.getSalesLineId() != null && "".equalsIgnoreCase(this.lineBean.getSalesLineId())==false)
                {
                    // Update corresponding SalesLineBean
                    SalesLineBean salesLine = SalesDelegate.getInstance().getSalesLineById( this.lineBean.getSalesLineId() );
                    if (salesLine != null) {
                        salesLine.setSalesId( salesObj.getId() );
                        SalesDelegate.getInstance().saveOrUpdateSalesLine(salesLine);
                    }
                    
                    // Update corresponding SalesBucketBean
                    SalesBucketBean salesBucket = SalesDelegate.getInstance().getSalesBucketBySalesIdAndBucketNo(originalSalesId, this.lineBean.getBucketNo());
                    if (salesBucket != null) {
                        salesBucket.setSalesId( salesObj.getId() );
                        SalesDelegate.getInstance().saveOrUpdateSalesBucket(salesBucket);
                    }
                }
                
                // Update corresponding InTransactionLineBean
                InTransactionDelegate.getInstance().saveTransactionLine(lineBean);
            }
            return true;
        }
        catch (NumberFormatException e) {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            
            JOptionPane.showMessageDialog(this, "您输入的重量有错误。", "输入错误", JOptionPane.ERROR_MESSAGE);
            this.jTextFieldWeight.requestFocus();
            this.jTextFieldWeight.selectAll();
            
            return false;
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

        jPanelCenter = new javax.swing.JPanel();
        jPanelSeafoodType = new javax.swing.JPanel();
        jLabelSeafoodType = new javax.swing.JLabel();
        jPanelSeafoodTypeButtons = new javax.swing.JPanel();
        jButtonSeafoodTypeBig = new javax.swing.JButton();
        jButtonSeafoodTypeSmall = new javax.swing.JButton();
        jButtonSeafoodTypeMiddle = new javax.swing.JButton();
        jButtonSeafoodTypeTiny = new javax.swing.JButton();
        jButtonSeafoodTypeShang = new javax.swing.JButton();
        jButtonSeafoodTypeFarm = new javax.swing.JButton();
        jButtonSeafoodTypeMeat = new javax.swing.JButton();
        jButtonSeafoodTypeHead = new javax.swing.JButton();
        jButtonSeafoodTypeSpecial = new javax.swing.JButton();
        jButtonSeafoodTypeCold = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTextFieldItemId = new javax.swing.JTextField();
        jTextFieldItemName = new javax.swing.JTextField();
        jPanelWeight = new javax.swing.JPanel();
        jLabelWeight = new javax.swing.JLabel();
        jTextFieldWeight = new javax.swing.JTextField();
        jPanelWeightButtons = new javax.swing.JPanel();
        jPanelCustomer = new javax.swing.JPanel();
        jLabelClientNo = new javax.swing.JLabel();
        jPanelClientNoButtons = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTextFieldCustomerId = new javax.swing.JTextField();
        jTextFieldCustomerName = new javax.swing.JTextField();
        jPanelBucketNo = new javax.swing.JPanel();
        jLabelBucketNo = new javax.swing.JLabel();
        jTextFieldBucketNo = new javax.swing.JTextField();
        jPanelBucketNoButtons = new javax.swing.JPanel();
        jPanelAction = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("来货海鲜细节");
        setPreferredSize(new java.awt.Dimension(600, 720));
        setResizable(false);

        jPanelCenter.setLayout(new java.awt.GridLayout(4, 1, 0, 3));

        jPanelSeafoodType.setLayout(new java.awt.BorderLayout(3, 3));

        jLabelSeafoodType.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jLabelSeafoodType.setText("海鲜类别");
        jPanelSeafoodType.add(jLabelSeafoodType, java.awt.BorderLayout.PAGE_START);

        jPanelSeafoodTypeButtons.setPreferredSize(new java.awt.Dimension(240, 125));
        jPanelSeafoodTypeButtons.setLayout(new java.awt.GridLayout(5, 2, 3, 3));

        jButtonSeafoodTypeBig.setBackground(new java.awt.Color(255, 51, 0));
        jButtonSeafoodTypeBig.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jButtonSeafoodTypeBig.setText("大");
        jButtonSeafoodTypeBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeafoodTypeBigActionPerformed(evt);
            }
        });
        jPanelSeafoodTypeButtons.add(jButtonSeafoodTypeBig);

        jButtonSeafoodTypeSmall.setBackground(new java.awt.Color(255, 204, 0));
        jButtonSeafoodTypeSmall.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jButtonSeafoodTypeSmall.setText("小");
        jButtonSeafoodTypeSmall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeafoodTypeSmallActionPerformed(evt);
            }
        });
        jPanelSeafoodTypeButtons.add(jButtonSeafoodTypeSmall);

        jButtonSeafoodTypeMiddle.setBackground(new java.awt.Color(255, 102, 0));
        jButtonSeafoodTypeMiddle.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jButtonSeafoodTypeMiddle.setText("中");
        jButtonSeafoodTypeMiddle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeafoodTypeMiddleActionPerformed(evt);
            }
        });
        jPanelSeafoodTypeButtons.add(jButtonSeafoodTypeMiddle);

        jButtonSeafoodTypeTiny.setBackground(new java.awt.Color(255, 255, 0));
        jButtonSeafoodTypeTiny.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jButtonSeafoodTypeTiny.setText("仔");
        jButtonSeafoodTypeTiny.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeafoodTypeTinyActionPerformed(evt);
            }
        });
        jPanelSeafoodTypeButtons.add(jButtonSeafoodTypeTiny);

        jButtonSeafoodTypeShang.setBackground(new java.awt.Color(255, 153, 0));
        jButtonSeafoodTypeShang.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jButtonSeafoodTypeShang.setText("上");
        jButtonSeafoodTypeShang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeafoodTypeShangActionPerformed(evt);
            }
        });
        jPanelSeafoodTypeButtons.add(jButtonSeafoodTypeShang);

        jButtonSeafoodTypeFarm.setBackground(new java.awt.Color(204, 255, 0));
        jButtonSeafoodTypeFarm.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jButtonSeafoodTypeFarm.setText("养");
        jButtonSeafoodTypeFarm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeafoodTypeFarmActionPerformed(evt);
            }
        });
        jPanelSeafoodTypeButtons.add(jButtonSeafoodTypeFarm);

        jButtonSeafoodTypeMeat.setBackground(new java.awt.Color(153, 255, 0));
        jButtonSeafoodTypeMeat.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jButtonSeafoodTypeMeat.setText("肉");
        jButtonSeafoodTypeMeat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeafoodTypeMeatActionPerformed(evt);
            }
        });
        jPanelSeafoodTypeButtons.add(jButtonSeafoodTypeMeat);

        jButtonSeafoodTypeHead.setBackground(new java.awt.Color(0, 255, 0));
        jButtonSeafoodTypeHead.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jButtonSeafoodTypeHead.setText("头");
        jButtonSeafoodTypeHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeafoodTypeHeadActionPerformed(evt);
            }
        });
        jPanelSeafoodTypeButtons.add(jButtonSeafoodTypeHead);

        jButtonSeafoodTypeSpecial.setBackground(new java.awt.Color(51, 255, 0));
        jButtonSeafoodTypeSpecial.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jButtonSeafoodTypeSpecial.setText("特");
        jButtonSeafoodTypeSpecial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeafoodTypeSpecialActionPerformed(evt);
            }
        });
        jPanelSeafoodTypeButtons.add(jButtonSeafoodTypeSpecial);

        jButtonSeafoodTypeCold.setBackground(new java.awt.Color(51, 255, 0));
        jButtonSeafoodTypeCold.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jButtonSeafoodTypeCold.setText("冻");
        jButtonSeafoodTypeCold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeafoodTypeColdActionPerformed(evt);
            }
        });
        jPanelSeafoodTypeButtons.add(jButtonSeafoodTypeCold);

        jPanelSeafoodType.add(jPanelSeafoodTypeButtons, java.awt.BorderLayout.LINE_END);

        jPanel1.setLayout(new java.awt.GridLayout(2, 1, 0, 3));

        jTextFieldItemId.setEditable(false);
        jTextFieldItemId.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldItemId.setFont(new java.awt.Font("KaiTi", 1, 36)); // NOI18N
        jPanel1.add(jTextFieldItemId);

        jTextFieldItemName.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldItemName.setFont(new java.awt.Font("KaiTi", 0, 36)); // NOI18N
        jPanel1.add(jTextFieldItemName);

        jPanelSeafoodType.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanelCenter.add(jPanelSeafoodType);

        jPanelWeight.setLayout(new java.awt.BorderLayout(3, 3));

        jLabelWeight.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jLabelWeight.setText("重量 (KG)");
        jPanelWeight.add(jLabelWeight, java.awt.BorderLayout.PAGE_START);

        jTextFieldWeight.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jTextFieldWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldWeightFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldWeightFocusLost(evt);
            }
        });
        jTextFieldWeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWeightActionPerformed(evt);
            }
        });
        jPanelWeight.add(jTextFieldWeight, java.awt.BorderLayout.CENTER);

        jPanelWeightButtons.setPreferredSize(new java.awt.Dimension(240, 125));

        javax.swing.GroupLayout jPanelWeightButtonsLayout = new javax.swing.GroupLayout(jPanelWeightButtons);
        jPanelWeightButtons.setLayout(jPanelWeightButtonsLayout);
        jPanelWeightButtonsLayout.setHorizontalGroup(
            jPanelWeightButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );
        jPanelWeightButtonsLayout.setVerticalGroup(
            jPanelWeightButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 178, Short.MAX_VALUE)
        );

        jPanelWeight.add(jPanelWeightButtons, java.awt.BorderLayout.LINE_END);

        jPanelCenter.add(jPanelWeight);

        jPanelCustomer.setLayout(new java.awt.BorderLayout(3, 3));

        jLabelClientNo.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jLabelClientNo.setText("客户号码");
        jPanelCustomer.add(jLabelClientNo, java.awt.BorderLayout.PAGE_START);

        jPanelClientNoButtons.setPreferredSize(new java.awt.Dimension(240, 125));
        jPanelClientNoButtons.setLayout(new java.awt.BorderLayout());
        jPanelCustomer.add(jPanelClientNoButtons, java.awt.BorderLayout.LINE_END);

        jPanel2.setLayout(new java.awt.GridLayout(2, 1, 0, 3));

        jTextFieldCustomerId.setFont(new java.awt.Font("KaiTi", 1, 36)); // NOI18N
        jTextFieldCustomerId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldCustomerIdFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldCustomerIdFocusLost(evt);
            }
        });
        jTextFieldCustomerId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCustomerIdActionPerformed(evt);
            }
        });
        jTextFieldCustomerId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldCustomerIdKeyReleased(evt);
            }
        });
        jPanel2.add(jTextFieldCustomerId);

        jTextFieldCustomerName.setEditable(false);
        jTextFieldCustomerName.setBackground(new java.awt.Color(204, 204, 204));
        jTextFieldCustomerName.setFont(new java.awt.Font("KaiTi", 1, 36)); // NOI18N
        jPanel2.add(jTextFieldCustomerName);

        jPanelCustomer.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanelCenter.add(jPanelCustomer);

        jPanelBucketNo.setLayout(new java.awt.BorderLayout(3, 3));

        jLabelBucketNo.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jLabelBucketNo.setText("桶子号码");
        jPanelBucketNo.add(jLabelBucketNo, java.awt.BorderLayout.PAGE_START);

        jTextFieldBucketNo.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/ryanworks/fishery/client/ui/inbound/Bundle"); // NOI18N
        jTextFieldBucketNo.setText(bundle.getString("JDialogTransactionDetails.jTextFieldBucket.text")); // NOI18N
        jTextFieldBucketNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldBucketNoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldBucketNoFocusLost(evt);
            }
        });
        jTextFieldBucketNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBucketNoActionPerformed(evt);
            }
        });
        jPanelBucketNo.add(jTextFieldBucketNo, java.awt.BorderLayout.CENTER);

        jPanelBucketNoButtons.setPreferredSize(new java.awt.Dimension(240, 125));

        javax.swing.GroupLayout jPanelBucketNoButtonsLayout = new javax.swing.GroupLayout(jPanelBucketNoButtons);
        jPanelBucketNoButtons.setLayout(jPanelBucketNoButtonsLayout);
        jPanelBucketNoButtonsLayout.setHorizontalGroup(
            jPanelBucketNoButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );
        jPanelBucketNoButtonsLayout.setVerticalGroup(
            jPanelBucketNoButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 178, Short.MAX_VALUE)
        );

        jPanelBucketNo.add(jPanelBucketNoButtons, java.awt.BorderLayout.LINE_END);

        jPanelCenter.add(jPanelBucketNo);

        getContentPane().add(jPanelCenter, java.awt.BorderLayout.CENTER);

        jButtonSave.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonSave.setText("储存");
        jButtonSave.setPreferredSize(new java.awt.Dimension(120, 37));
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        jPanelAction.add(jButtonSave);

        jButtonDelete.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonDelete.setText("删除");
        jButtonDelete.setPreferredSize(new java.awt.Dimension(120, 37));
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        jPanelAction.add(jButtonDelete);

        jButtonCancel.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonCancel.setText("取消");
        jButtonCancel.setPreferredSize(new java.awt.Dimension(120, 37));
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanelAction.add(jButtonCancel);

        getContentPane().add(jPanelAction, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSeafoodTypeBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeafoodTypeBigActionPerformed
        
        this.lineBean.setItemNewName(this.jButtonSeafoodTypeBig.getText() + jTextFieldItemName.getText().trim());
        this.jTextFieldItemName.setText( this.lineBean.getItemNewName() );
        
        this.jTextFieldWeight.requestFocus();
        this.jTextFieldWeight.selectAll();
    }//GEN-LAST:event_jButtonSeafoodTypeBigActionPerformed

    private void jButtonSeafoodTypeSmallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeafoodTypeSmallActionPerformed
        
        this.lineBean.setItemNewName(this.jButtonSeafoodTypeSmall.getText() + jTextFieldItemName.getText().trim());
        this.jTextFieldItemName.setText( this.lineBean.getItemNewName() );
        
        this.jTextFieldWeight.requestFocus();
        this.jTextFieldWeight.selectAll();
    }//GEN-LAST:event_jButtonSeafoodTypeSmallActionPerformed

    private void jButtonSeafoodTypeMiddleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeafoodTypeMiddleActionPerformed
        
        this.lineBean.setItemNewName(this.jButtonSeafoodTypeMiddle.getText() + jTextFieldItemName.getText().trim());
        this.jTextFieldItemName.setText( this.lineBean.getItemNewName() );
        
        this.jTextFieldWeight.requestFocus();
        this.jTextFieldWeight.selectAll();
    }//GEN-LAST:event_jButtonSeafoodTypeMiddleActionPerformed

    private void jButtonSeafoodTypeTinyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeafoodTypeTinyActionPerformed
        
        this.lineBean.setItemNewName(jTextFieldItemName.getText().trim() + this.jButtonSeafoodTypeTiny.getText() );
        this.jTextFieldItemName.setText( this.lineBean.getItemNewName() );
        
        this.jTextFieldWeight.requestFocus();
        this.jTextFieldWeight.selectAll();
    }//GEN-LAST:event_jButtonSeafoodTypeTinyActionPerformed

    private void jButtonSeafoodTypeShangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeafoodTypeShangActionPerformed
        
        this.lineBean.setItemNewName(this.jButtonSeafoodTypeShang.getText() + jTextFieldItemName.getText().trim());
        this.jTextFieldItemName.setText( this.lineBean.getItemNewName() );
        
        this.jTextFieldWeight.requestFocus();
        this.jTextFieldWeight.selectAll();
    }//GEN-LAST:event_jButtonSeafoodTypeShangActionPerformed

    private void jTextFieldWeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWeightActionPerformed
        
        String weightString = this.jTextFieldWeight.getText().trim();
        
        try 
        {
            double weight = Double.parseDouble(weightString);
            this.lineBean.setWeight(weight);
            
            this.jTextFieldCustomerId.requestFocus();
            this.jTextFieldCustomerId.selectAll();
        }
        catch (NumberFormatException e) 
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            
            JOptionPane.showMessageDialog(this, "您输入的重量有错误。", "输入错误", JOptionPane.ERROR_MESSAGE);
            this.jTextFieldWeight.requestFocus();
            this.jTextFieldWeight.selectAll();
        }
    }//GEN-LAST:event_jTextFieldWeightActionPerformed

    private void jTextFieldWeightFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldWeightFocusGained
        this.jTextFieldWeight.setBackground(Color.YELLOW);
        this.jTextFieldWeight.selectAll();
    }//GEN-LAST:event_jTextFieldWeightFocusGained

    private void jTextFieldWeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldWeightFocusLost
        this.jTextFieldWeight.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTextFieldWeightFocusLost

    private void jTextFieldCustomerIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldCustomerIdFocusGained
        if (this.isNewLineBean) {
            this.jTextFieldCustomerId.setBackground(Color.YELLOW);
            this.jTextFieldCustomerId.selectAll();
        }
    }//GEN-LAST:event_jTextFieldCustomerIdFocusGained

    private void jTextFieldCustomerIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldCustomerIdFocusLost
        if (this.isNewLineBean) {
            this.jTextFieldCustomerId.setBackground(Color.WHITE);
        }
    }//GEN-LAST:event_jTextFieldCustomerIdFocusLost

    private void jTextFieldBucketNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldBucketNoFocusLost
        this.jTextFieldBucketNo.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTextFieldBucketNoFocusLost

    private void jTextFieldBucketNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldBucketNoFocusGained
        this.jTextFieldBucketNo.setBackground(Color.YELLOW);
        this.jTextFieldBucketNo.selectAll();
    }//GEN-LAST:event_jTextFieldBucketNoFocusGained

    private void jTextFieldCustomerIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCustomerIdActionPerformed
        if (this.isNewLineBean) {
            this.jTextFieldBucketNo.requestFocus();
            this.jTextFieldBucketNo.selectAll();
        }
    }//GEN-LAST:event_jTextFieldCustomerIdActionPerformed

    private void jTextFieldBucketNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBucketNoActionPerformed
        this.jButtonSave.doClick();
    }//GEN-LAST:event_jTextFieldBucketNoActionPerformed

    private void jTextFieldCustomerIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCustomerIdKeyReleased
        if (this.isNewLineBean) {        
            // Clear text on jTextFieldCustomerName
            this.jTextFieldCustomerName.setText( "" );

            String customerId = this.jTextFieldCustomerId.getText().trim();    
            System.err.println("customer ID: " + customerId);
            if ("".equalsIgnoreCase(customerId)) 
            {
                this.lineBean.setCustomerId("");
                this.lineBean.setCustomerName("");
                return;
            }

            CustomerBean customerBean = CustomerDelegate.getInstance().getCustomerByCustomerId(customerId);

            if (customerBean==null)
            {               
                System.err.println("customer is NULL.");
                this.lineBean.setCustomerId(customerId);
                this.lineBean.setCustomerName("");

                this.isCustomerIdValid = false;
            }
            else
            {
                System.err.println("customer is " + customerBean.getName());
                this.lineBean.setCustomerId(customerBean.getId());
                this.lineBean.setCustomerName(customerBean.getName());
                this.jTextFieldCustomerName.setText( this.lineBean.getCustomerName() );

                this.isCustomerIdValid = true;
            }
        }
    }//GEN-LAST:event_jTextFieldCustomerIdKeyReleased

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        
        SaveThread thread = new SaveThread("Save to DB Thread");
        thread.start();
        
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    /**
     * Thread to save transaction asyn.
     */
    private class SaveThread extends Thread 
    {
        public SaveThread(String name) 
        {
            super(name);
        }
        
        @Override
        public void run()
        {     
            if (fillBean()==false)
                return;

            InboundStockingLineDialog.this.parentPanel.stockingLineDialogClosed(
                InboundStockingLineDialog.this.lineBean, 
                InboundStockingLineDialog.this.salesObj, 
                InboundStockingLineDialog.this.isNewLineBean);
        }
    }
    
    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        
        int flag = JOptionPane.showConfirmDialog(this, "您确定要删除这项交易记录？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (flag == JOptionPane.NO_OPTION)
            return;
                
        this.setVisible(false);
        
        fillBean();
        this.parentPanel.deleteTransactionLine(this.lineBean);
                    
        this.dispose();
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonSeafoodTypeFarmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeafoodTypeFarmActionPerformed
        this.lineBean.setItemNewName(this.jButtonSeafoodTypeFarm.getText() + jTextFieldItemName.getText().trim());
        this.jTextFieldItemName.setText( this.lineBean.getItemNewName() );
        
        this.jTextFieldWeight.requestFocus();
        this.jTextFieldWeight.selectAll();
    }//GEN-LAST:event_jButtonSeafoodTypeFarmActionPerformed

    private void jButtonSeafoodTypeMeatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeafoodTypeMeatActionPerformed
        this.lineBean.setItemNewName(jTextFieldItemName.getText().trim() + this.jButtonSeafoodTypeMeat.getText() );
        this.jTextFieldItemName.setText( this.lineBean.getItemNewName() );
        
        this.jTextFieldWeight.requestFocus();
        this.jTextFieldWeight.selectAll();
    }//GEN-LAST:event_jButtonSeafoodTypeMeatActionPerformed

    private void jButtonSeafoodTypeHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeafoodTypeHeadActionPerformed
        this.lineBean.setItemNewName(jTextFieldItemName.getText().trim() + this.jButtonSeafoodTypeHead.getText() );
        this.jTextFieldItemName.setText( this.lineBean.getItemNewName() );
        
        this.jTextFieldWeight.requestFocus();
        this.jTextFieldWeight.selectAll();
    }//GEN-LAST:event_jButtonSeafoodTypeHeadActionPerformed

    private void jButtonSeafoodTypeSpecialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeafoodTypeSpecialActionPerformed
        this.lineBean.setItemNewName( this.jButtonSeafoodTypeSpecial.getText() + jTextFieldItemName.getText().trim() );
        this.jTextFieldItemName.setText( this.lineBean.getItemNewName() );
        
        this.jTextFieldWeight.requestFocus();
        this.jTextFieldWeight.selectAll();
    }//GEN-LAST:event_jButtonSeafoodTypeSpecialActionPerformed

    private void jButtonSeafoodTypeColdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeafoodTypeColdActionPerformed
        this.lineBean.setItemNewName( this.jButtonSeafoodTypeCold.getText() + jTextFieldItemName.getText().trim() );
        this.jTextFieldItemName.setText( this.lineBean.getItemNewName() );
        
        this.jTextFieldWeight.requestFocus();
        this.jTextFieldWeight.selectAll();
    }//GEN-LAST:event_jButtonSeafoodTypeColdActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSeafoodTypeBig;
    private javax.swing.JButton jButtonSeafoodTypeCold;
    private javax.swing.JButton jButtonSeafoodTypeFarm;
    private javax.swing.JButton jButtonSeafoodTypeHead;
    private javax.swing.JButton jButtonSeafoodTypeMeat;
    private javax.swing.JButton jButtonSeafoodTypeMiddle;
    private javax.swing.JButton jButtonSeafoodTypeShang;
    private javax.swing.JButton jButtonSeafoodTypeSmall;
    private javax.swing.JButton jButtonSeafoodTypeSpecial;
    private javax.swing.JButton jButtonSeafoodTypeTiny;
    private javax.swing.JLabel jLabelBucketNo;
    private javax.swing.JLabel jLabelClientNo;
    private javax.swing.JLabel jLabelSeafoodType;
    private javax.swing.JLabel jLabelWeight;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelAction;
    private javax.swing.JPanel jPanelBucketNo;
    private javax.swing.JPanel jPanelBucketNoButtons;
    private javax.swing.JPanel jPanelCenter;
    private javax.swing.JPanel jPanelClientNoButtons;
    private javax.swing.JPanel jPanelCustomer;
    private javax.swing.JPanel jPanelSeafoodType;
    private javax.swing.JPanel jPanelSeafoodTypeButtons;
    private javax.swing.JPanel jPanelWeight;
    private javax.swing.JPanel jPanelWeightButtons;
    private javax.swing.JTextField jTextFieldBucketNo;
    private javax.swing.JTextField jTextFieldCustomerId;
    private javax.swing.JTextField jTextFieldCustomerName;
    private javax.swing.JTextField jTextFieldItemId;
    private javax.swing.JTextField jTextFieldItemName;
    private javax.swing.JTextField jTextFieldWeight;
    // End of variables declaration//GEN-END:variables
}

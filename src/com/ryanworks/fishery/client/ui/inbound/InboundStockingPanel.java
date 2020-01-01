package com.ryanworks.fishery.client.ui.inbound;

import com.ryanworks.fishery.client.delegate.*;
import com.ryanworks.fishery.client.print.InboundPDFBoxPrinter;
import com.ryanworks.fishery.client.print.RootPDFBoxPrinter;
import com.ryanworks.fishery.client.ui.JFrameMain;
import com.ryanworks.fishery.client.ui.inbound.listener.*;
import com.ryanworks.fishery.client.ui.shared.ProgressModalDialog;
import com.ryanworks.fishery.shared.bean.*;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import com.ryanworks.fishery.shared.swing.listener.JTextFieldFocusListener;
import com.ryanworks.fishery.shared.util.MyLogger;
import com.ryanworks.fishery.shared.util.SwingUtil;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;

public class InboundStockingPanel 
    extends javax.swing.JPanel 
    implements InboundWizardDialogListener, InboundStockingLineDialogListener, InboundSearchDialogListener, ActionListener, MouseListener, KeyListener
{   
    private final String saveToPath = RootPDFBoxPrinter.PDF_PRINTOUT_FOLDER;
    
    private final JFrame parent;
    private InTransactionBean transactionObj;
    private SupplierBean supplierObj;
    private DefaultListModel listModelCategory;
    private DefaultListModel listModelItem;
    private boolean showingPrice = false;    
    
    private final JLabel dataHeader1[] = {new JLabel("客户"), new JLabel("鱼类"), new JLabel("桶号"), new JLabel("重量"), new JLabel("储蓄"), new JLabel("")};
    private final JLabel dataHeader2[] = {new JLabel("客户"), new JLabel("鱼类"), new JLabel("桶号"), new JLabel("重量"), new JLabel("储蓄"), new JLabel("")};
    private final JLabel dataHeader3[] = {new JLabel("客户"), new JLabel("鱼类"), new JLabel("桶号"), new JLabel("重量"), new JLabel("储蓄"), new JLabel("")};
    private final JTextField dataRow1[] = new JTextField[60];
    private final JTextField dataRow2[] = new JTextField[60];
    private final JTextField dataRow3[] = new JTextField[60];
    private final JTextField dataRow4[] = new JTextField[60];
    private final JTextField dataRow5[] = new JTextField[60];
    private final JButton buttonRow[] = new JButton[60];
    
    private final DecimalFormat currencyFormatter = new DecimalFormat("0.00");
    private final DecimalFormat decimalFormatter = new DecimalFormat("0.000");
    
    public InboundStockingPanel(JFrame parent) 
    {
        this.parent = parent;
        
        initComponents();
        myInit();
    }
    
    private void myInit() 
    {        
        for (int i = 0 ; i < dataHeader1.length ; i++)
        {
            dataHeader1[i].setFont( new Font("Kaiti", Font.BOLD, 21) );
            dataHeader2[i].setFont( new Font("Kaiti", Font.BOLD, 21) );
            dataHeader3[i].setFont( new Font("Kaiti", Font.BOLD, 21) );
            
            dataHeader1[i].setHorizontalAlignment( SwingConstants.CENTER );
            dataHeader2[i].setHorizontalAlignment( SwingConstants.CENTER );
            dataHeader3[i].setHorizontalAlignment( SwingConstants.CENTER );
            
            this.jPanelLineList1.add(dataHeader1[i]);
            this.jPanelLineList2.add(dataHeader2[i]);
            this.jPanelLineList3.add(dataHeader3[i]);
        }
                
        initDataRowPanel(this.jPanelLineList1, 0, 20);
        initDataRowPanel(this.jPanelLineList2, 20, 40);
        initDataRowPanel(this.jPanelLineList3, 40, 60);
        
        listModelCategory = new DefaultListModel();
        this.jListCategory.setModel(listModelCategory);
                
        listModelItem = new DefaultListModel();
        this.jListItem.setModel(listModelItem);
    }
    
    private void initDataRowPanel(JPanel mainPanel, int startIndex, int endIndex)
    {
        for (int i = startIndex ; i < endIndex ; i++) 
        {
            dataRow1[i] = new JTextField();
            dataRow2[i] = new JTextField();
            dataRow3[i] = new JTextField();
            dataRow4[i] = new JTextField();
            dataRow5[i] = new JTextField();
            buttonRow[i] = new JButton("X");
            
            dataRow1[i].setFont( new Font("Kaiti", Font.PLAIN, 18) );
            dataRow2[i].setFont( new Font("Kaiti", Font.PLAIN, 18) );
            dataRow3[i].setFont( new Font("Kaiti", Font.PLAIN, 18) );
            dataRow4[i].setFont( new Font("Kaiti", Font.PLAIN, 18) );
            dataRow5[i].setFont( new Font("Kaiti", Font.PLAIN, 18) );
            buttonRow[i].setFont( new Font("Kaiti", Font.BOLD, 18) );
            
            dataRow3[i].addFocusListener( new JTextFieldFocusListener(Color.BLACK, Color.GREEN) );
            dataRow4[i].addFocusListener( new JTextFieldFocusListener() );
            dataRow5[i].addFocusListener( new JTextFieldFocusListener(Color.BLACK, Color.GREEN) );
            
            dataRow1[i].addActionListener( this ); 
            dataRow3[i].addActionListener( this );
            dataRow4[i].addActionListener( this );   
            dataRow5[i].addActionListener( this );
            buttonRow[i].addActionListener( this );   
             
            dataRow2[i].addMouseListener( this );
            
            dataRow1[i].addKeyListener(this);
            dataRow3[i].addKeyListener(this);
            dataRow4[i].addKeyListener(this);
            dataRow5[i].addKeyListener( this );  
            
            dataRow2[i].setEditable( false );
            dataRow3[i].setEditable( false );
            dataRow5[i].setEditable( false );
            
            dataRow1[i].setHorizontalAlignment( SwingConstants.CENTER );
            dataRow2[i].setHorizontalAlignment( SwingConstants.CENTER );
            dataRow3[i].setHorizontalAlignment( SwingConstants.CENTER );
            dataRow4[i].setHorizontalAlignment( SwingConstants.TRAILING );
            dataRow5[i].setHorizontalAlignment( SwingConstants.TRAILING );
            
            mainPanel.add(dataRow1[i]);
            mainPanel.add(dataRow2[i]);
            mainPanel.add(dataRow3[i]);
            mainPanel.add(dataRow4[i]);  
            mainPanel.add(dataRow5[i]);      
            mainPanel.add(buttonRow[i]);  
        }
    }
    
    private void fillForm()
    {
        if (transactionObj == null)
            transactionObj = new InTransactionBean();
        
        String shipNameVal = "";
        if (!"".equalsIgnoreCase(transactionObj.getSupplierId()))
        {
            supplierObj = SupplierDelegate.getInstance().getSupplierById(transactionObj.getSupplierId());
            if (supplierObj == null)
                shipNameVal = "-- / --";
            else
                shipNameVal = supplierObj.getShipNumber() + " / " + supplierObj.getName();
        }
        
        this.jTextFieldShipName.setText( shipNameVal );
        this.jTextFieldTripNumber.setText( String.valueOf(transactionObj.getSupplierTrip()) );
        this.jTextFieldDate.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format(transactionObj.getDateTime()) );
        this.jTextFieldInvoiceNumber.setText( transactionObj.getTransactionNo() );
        this.jLabelTotalAmountValue.setText( currencyFormatter.format(transactionObj.getTotalPrice()) );
        this.jLabelTotalSavingValue.setText( currencyFormatter.format(transactionObj.getTotalBonus()) );
         
        // Fill in category list
        List<CategoryBean> categoryList = SystemDataDelegate.getInstance().getAllCategories();
        for (CategoryBean category : categoryList) 
        {
            listModelCategory.addElement( category );
        }
                
        // Re-fill the 3 lists
        updateDataRowsFromServer(true);        
    }
    
    public void reset()
    {
        // Reset
        this.showingPrice = false;
        this.transactionObj = null;
        this.supplierObj = null;
        
        this.jTextFieldShipName.setText( "" );
        this.jTextFieldDate.setText( "" );
        this.jTextFieldTripNumber.setText( "" );
        this.jTextFieldInvoiceNumber.setText( "" );
        this.jButtonPrice.setText("显示价格");
        this.dataHeader1[2].setText( "桶号" );
        this.dataHeader2[2].setText( "桶号" );
        this.dataHeader3[2].setText( "桶号" );
        this.jLabelTotalAmountValue.setText("0.00");
        this.jLabelTotalSavingValue.setText("0.00");
        this.jLabelTotalBonusValue.setText( "0.00" );
        
        this.listModelCategory.removeAllElements();
        this.listModelItem.removeAllElements();
        
        for (int i=0 ; i<60 ; i++)
        {
            this.dataRow1[i].setText( "" );
            this.dataRow2[i].setText( "" );
            this.dataRow3[i].setText( "" );
            this.dataRow4[i].setText( "" );
            this.dataRow5[i].setText( "" );
                        
            this.dataRow3[i].setBackground( Color.WHITE );
            this.dataRow3[i].setForeground( Color.BLACK );
            this.dataRow5[i].setBackground( Color.WHITE );
            this.dataRow5[i].setForeground( Color.BLACK );
        }
        
        this.jButtonSave.setEnabled( false );
        this.jButtonDelete.setEnabled( false );
        this.jButtonPrice.setEnabled( false );
        this.jButtonPrintReceiptChinese.setEnabled( false );
        this.jButtonPrintReceiptMalay.setEnabled( false );
        this.jButtonPrintSaving.setEnabled( false );
        this.jButtonPrintSavingMalay.setEnabled( false );
        this.jButtonRecalculation.setEnabled( false );
    }
    
    /**
     * Update data rows based on local object (not request from server).
     */
    private void updateDataRows(boolean refreshRowData)
    {
        int lineIndex = 0;
        double totalAmount = 0.0d;
        double totalBonus = 0.0d;
        double totalSaving = 0.0d;
        
        for (InTransactionLineBean lineBean : this.transactionObj.getLineList())
        {
            if (refreshRowData == true)
            {
                this.dataRow1[lineIndex].setText( lineBean.getCustomerId() );
                this.dataRow2[lineIndex].setText( lineBean.getItemNewName() );
                this.dataRow4[lineIndex].setText( decimalFormatter.format(lineBean.getWeight()) );

                if ("显示价格".equalsIgnoreCase(this.jButtonPrice.getText()))
                {
                    this.dataRow3[lineIndex].setText( lineBean.getBucketNo() );
                    this.dataRow5[lineIndex].setText( "" );
                }
                else
                {
                    this.dataRow3[lineIndex].setText( currencyFormatter.format( lineBean.getUnitPrice() ) );        
                    this.dataRow5[lineIndex].setText( currencyFormatter.format( lineBean.getSaving() ) );
                }
            }
            
            lineIndex++;
            totalAmount += (lineBean.getUnitPrice() * lineBean.getWeight());
            totalSaving += (lineBean.getSaving() * lineBean.getWeight());
        }
        
        this.transactionObj.setTotalPrice( totalAmount );
        this.jLabelTotalAmountValue.setText( currencyFormatter.format(transactionObj.getTotalPrice()) );   
        
        this.transactionObj.setTotalSaving( totalSaving );
        this.jLabelTotalSavingValue.setText( currencyFormatter.format(transactionObj.getTotalSaving()) );   
        
        totalBonus = totalAmount * (this.supplierObj.getPercentage() / 100);
        this.transactionObj.setTotalBonus( totalBonus );
        this.jLabelTotalBonusValue.setText( currencyFormatter.format(transactionObj.getTotalBonus()) );
    }
    
    /**
     * Request new set of data from server, then update locally.
     */
    private void updateDataRowsFromServer(boolean readFromServer)
    {        
        // REMOVE ALL DATA IN LIST
        for (int i = 0 ; i < 60 ; i++)
        {
            this.dataRow1[i].setText( "" );            
            this.dataRow2[i].setText( "" );
            this.dataRow3[i].setText( "" );
            this.dataRow3[i].setBackground(Color.WHITE);
            this.dataRow3[i].setForeground(Color.BLACK);
            this.dataRow4[i].setText( "" );
            this.dataRow5[i].setText( "" );
        }
        
        this.updateDataRows(true);
    }
    
    /** This method has same algorithm as InboundStockingLineDialog.fillBean() **/
    private void refillSingleTransactionLineBean(InTransactionLineBean lineBean, CustomerBean customer)
    {
        try {
            // Store initial Sales ID for later use
            String originalSalesId = lineBean.getSalesId();
 
            lineBean.setCustomerId( customer.getId() );
            lineBean.setCustomerName( customer.getName() );
              
            SalesBean salesObj = 
                    SalesDelegate.getInstance().getSalesByCustomerAndDate(
                            customer.getId(),
                            lineBean.getDateTime());
            
            // No sales bean found for the customer on that day. 
            if (salesObj == null)
            {
                // Create new sales object into database.
                salesObj = new SalesBean();
                salesObj.setCustomerId( lineBean.getCustomerId() );
                salesObj.setCustomerName( lineBean.getCustomerName() );
                salesObj.setCustomerTrip(1);
                salesObj.setDateTime(lineBean.getDateTime());
                SalesDelegate.getInstance().saveOrUpdateSales(salesObj);

                // Set Sales ID into line bean
                if (lineBean.getCustomerId().trim().length() != 0)
                    lineBean.setSalesId(salesObj.getId());
            }
            else
            {
                // Set Sales ID into line bean
                if (lineBean.getCustomerId().trim().length() != 0)
                    lineBean.setSalesId( salesObj.getId() );
            }

            if (lineBean.getSalesLineId() != null && "".equalsIgnoreCase(lineBean.getSalesLineId())==false)
            {
                // Update corresponding SalesLineBean
                SalesLineBean salesLine = SalesDelegate.getInstance().getSalesLineById( lineBean.getSalesLineId() );
                if (salesLine != null) {
                    salesLine.setSalesId( salesObj.getId() );
                    SalesDelegate.getInstance().saveOrUpdateSalesLine(salesLine);
                }

                // Update corresponding SalesBucketBean
                SalesBucketBean salesBucket = SalesDelegate.getInstance().getSalesBucketBySalesIdAndBucketNo(originalSalesId, lineBean.getBucketNo());
                if (salesBucket != null) {
                    salesBucket.setSalesId( salesObj.getId() );
                    SalesDelegate.getInstance().saveOrUpdateSalesBucket(salesBucket);
                }
            }

            // Update corresponding InTransactionLineBean
            InTransactionDelegate.getInstance().saveTransactionLine(lineBean);
        }
        catch (Exception e) {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
        }
    }
        
    /**
     * Assign this lineBean to customer with id, customerId.
     * 
     * @param lineBean
     * @param customerId 
     */
    private void assignLineToCustomer(InTransactionLineBean lineBean, String customerId) 
    {
        // Condition #1: Same customer
        if (lineBean.getCustomerId().equalsIgnoreCase(customerId))
            return;
        
        // Condition #2: Assign to NO customer
        if ("".equalsIgnoreCase(customerId.trim()))
        {
            this.assignLineToCustomer_deleteSalesLine(lineBean);
            return;
        }        
                
        // Condition#3: Assign to other customer
        if (lineBean.getCustomerId().equalsIgnoreCase(customerId) == false)
        {
            // IF current lineBean has NO customer
            // - Assume no corresponding sales object, and sales line.
            if ("".equalsIgnoreCase(lineBean.getCustomerId().trim()) == true)
            {
                this.assignLineToCustomer_createSalesLine(lineBean, customerId);
                return;
            }
            
            // IF current lineBean has customer
            if (lineBean.getCustomerId().length() > 0)
            {
                this.assignLineToCustomer_deleteSalesLine(lineBean);                
                this.assignLineToCustomer_createSalesLine(lineBean, customerId);
            }
        }
    }
    
    /**
     * Subroutine to create new sales line
     */
    private void assignLineToCustomer_createSalesLine(InTransactionLineBean lineBean, String customerId)
    {
        if (customerId == null || customerId.trim().length() == 0)
            return;
            
        // Get customer object from database
        CustomerBean customer = CustomerDelegate.getInstance().getCustomerByCustomerId(customerId);
        
        // Set new customer id before create new set of sales object
        lineBean.setCustomerId( customer.getId() );
        lineBean.setCustomerName( customer.getName() );

        SalesBean salesBean = 
            SalesDelegate.getInstance().getSalesByCustomerAndDate(customer.getId(), lineBean.getDateTime());
        
        // Create new set of sales object and sales line
        if (salesBean == null)
        {
            salesBean = this.createNewSales(lineBean);
        }
        SalesLineBean salesLineBean = this.createNewSalesLine(salesBean, lineBean);
        salesLineBean.setUnitPrice( 0.0d );
        SalesBucketBean salesBucketBean = this.createNewSalesBucket(salesBean, lineBean);
        
        // Assign new sales id and sales line id
        lineBean.setSalesId(salesBean.getId());
        lineBean.setSalesLineId(salesLineBean.getId());

        // Save the sales object completely
        SalesDelegate.getInstance().saveOrUpdateSales(salesBean);
    } // .end of assignLineToCustomer_createSalesLine
    
    /**
     * Subroutine to delete sales line.
     */
    private void assignLineToCustomer_deleteSalesLine(InTransactionLineBean lineBean)
    {
        // No sales line bean or sales bean found, so stop here.
        if ( "".equalsIgnoreCase( lineBean.getSalesLineId().trim() ) || "".equalsIgnoreCase( lineBean.getSalesId().trim() ) )
            return;
              
        // Delete old sales line
        SalesLineBean salesLineBean = 
                SalesDelegate.getInstance().getSalesLineById( lineBean.getSalesLineId() );  
        SalesDelegate.getInstance().deleteSalesLine(salesLineBean);

        // Retrieve sales object and sales bucket list
        SalesBean salesBean = 
                SalesDelegate.getInstance().getSalesById( lineBean.getSalesId() );
        
        List<SalesBucketBean> salesBucketList =
            SalesDelegate.getInstance().getBucketListBySalesId( lineBean.getSalesId() );

        // Delete sales object, if no more salesline
        if (salesBean.getLineList().isEmpty() && salesBucketList.size()==1)
        {
            SalesDelegate.getInstance().deleteSales(salesBean);
            for (SalesBucketBean bucket : salesBucketList)
            {
                SalesDelegate.getInstance().deleteSalesBucket(bucket);
            }
        }
        
        lineBean.setCustomerId( "" );
        lineBean.setCustomerName( "" );
        lineBean.setSalesId( "" );
        lineBean.setSalesLineId( "" );
    }
    
    /**
     * Helper method to Save DataRow1 (Assigned Customer ID)     * 
     */
    private void updateDataRow1(JTextField textFieldSource, boolean userPressedEnter)
    {
        int rowIndex = 0;
        for (JTextField tf : dataRow1)
        {
            if (textFieldSource == tf)
            {
                // Check if current row contain record
                if (this.transactionObj == null || 
                    this.transactionObj.getLineList()==null || 
                    rowIndex > this.transactionObj.getLineList().size()-1)
                    return;
                
                String customerId = this.dataRow1[rowIndex].getText().trim();
                CustomerBean customer = CustomerDelegate.getInstance().getCustomerByCustomerId(customerId);

                // Assume Remove customer 
                if (customerId.length() == 0) {
                    customer = new CustomerBean();
                    
                    // Delete sales line, if any
                    InTransactionLineBean lineObj = this.transactionObj.getLineList().get(rowIndex);
                    SalesLineBean salesLineObj = SalesDelegate.getInstance().getSalesLineById(lineObj.getSalesLineId());
                    if (salesLineObj != null) {
                        SalesBean salesObj = SalesDelegate.getInstance().getSalesById(salesLineObj.getSalesId());
                        List<SalesBucketBean> bucketList = SalesDelegate.getInstance().getBucketListBySalesId(salesLineObj.getSalesId());
                        
                        // delete salesLine
                        SalesDelegate.getInstance().deleteSalesLine(salesLineObj);
                    
                        // Delete sales object, if no more salesline
                        if (salesObj != null && salesObj.getLineList().isEmpty())
                        {
                            SalesDelegate.getInstance().deleteSales(salesObj);
                            for (SalesBucketBean bucket : bucketList)
                            {
                                SalesDelegate.getInstance().deleteSalesBucket(bucket);
                            }
                        }
                    }
                }
                    
                if (customer==null)
                {
                    JOptionPane.showMessageDialog(
                            this, 
                            "您输入的客户账号有错误。", 
                            "输入错误",
                            JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    // Delete existing sales object and salesLine object from old customer, whichever applicable.
                    assignLineToCustomer(this.transactionObj.getLineList().get(rowIndex), customerId);
                    
                    // Set new customer id into current transaction object
                    this.refillSingleTransactionLineBean(this.transactionObj.getLineList().get(rowIndex), customer);     
                    // Update Row Summary on screen
                    this.updateDataRows(false);                       
              
                    if (userPressedEnter == true)
                    {
                        int nextRowIndex = 0;
                        if (rowIndex == this.transactionObj.getLineList().size()-1)
                            nextRowIndex = 0;
                        else
                            nextRowIndex = rowIndex + 1;
                        
                        this.dataRow1[nextRowIndex].requestFocus();
                        this.dataRow1[nextRowIndex].selectAll();
                    }
                }
                    
                break;
            }

            rowIndex++;
        }
    }
    
    /**
     * Helper method to update data row 3 (Unit Price) 
     */
    private void updateDataRow3(JTextField textFieldSource, boolean userPressedEnter)
    {
        int rowIndex=0;
        for (JTextField tf : dataRow3)
        {
            if (textFieldSource == tf)
            {
                try 
                {
                    double unitPrice = 0.0d;
                    if ( "".equals(this.dataRow3[rowIndex].getText().trim()))
                        unitPrice = 0.0d;
                    else
                        unitPrice = Double.parseDouble( this.dataRow3[rowIndex].getText().trim() );
                    
                    InTransactionLineBean lineBean =  this.transactionObj.getLineList().get(rowIndex);
                    lineBean.setUnitPrice( unitPrice );
                    
                    this.updateDataRows( false );
                    
                    if (userPressedEnter == true)
                    {
                        int nextRowIndex = 0;
                        if (rowIndex == this.transactionObj.getLineList().size()-1)
                            nextRowIndex = 0;
                        else
                            nextRowIndex = rowIndex + 1;
                        
                        this.dataRow3[nextRowIndex].requestFocus();
                        this.dataRow3[nextRowIndex].selectAll();
                    }
                }
                catch (NumberFormatException e)
                {
                    MyLogger.logError(getClass(), "exception: " + e.getMessage());
                    JOptionPane.showMessageDialog(
                            this, 
                            "您输入的价格有错误。", 
                            "输入错误",
                            JOptionPane.ERROR_MESSAGE);
                }
                
                break;
            }

            rowIndex++;
        }
    }
    
    /**
     * Helper method to save data row 4 (Weight)
     * @param textFieldSource 
     */
    private void updateDataRow4(JTextField textFieldSource, boolean userPressedEnter)
    {
        int rowIndex = 0;
        for (JTextField tf : dataRow4)
        {
            if (textFieldSource == tf)
            { 
                // Check if current row contain record
                if (this.transactionObj == null || this.transactionObj.getLineList()==null || rowIndex > this.transactionObj.getLineList().size()-1)
                    return;
                
                try 
                {
                    this.transactionObj.getLineList().get(rowIndex).setWeight( Double.parseDouble( this.dataRow4[rowIndex].getText().trim() ) );
                    this.updateDataRows(false);

                    if (userPressedEnter == true)
                    {
                        int nextRowIndex = 0;
                        if (rowIndex == this.transactionObj.getLineList().size()-1)
                            nextRowIndex = 0;
                        else
                            nextRowIndex = rowIndex + 1;
                        
                        // Set focus on next row
                        this.dataRow4[nextRowIndex].requestFocus();
                        this.dataRow4[nextRowIndex].selectAll();
                    }
                }
                catch (NumberFormatException e)
                {
                    MyLogger.logError(getClass(), "exception: " + e.getMessage());
                    JOptionPane.showMessageDialog(
                            this, 
                            "您输入的重量有错误。", 
                            "输入错误",
                            JOptionPane.ERROR_MESSAGE);
                }
                
                break;
            }

            rowIndex++;
        }
    }
    
    /**
     * Helper method to save Data Row 5 (Saving)
     * @param textFieldSource 
     */
    private void updateDataRow5(JTextField textFieldSource, boolean userPressedEnter)
    {
        int rowIndex=0;
        for (JTextField tf : dataRow5)
        {
            if (textFieldSource == tf)
            {
                try 
                {
                    this.transactionObj.getLineList().get(rowIndex).setSaving(Double.parseDouble( this.dataRow5[rowIndex].getText().trim() ) );
                    this.updateDataRows(false);

                    if (userPressedEnter == true)
                    {
                        int nextRowIndex = 0;
                        if (rowIndex == this.transactionObj.getLineList().size()-1)
                            nextRowIndex = 0;
                        else
                            nextRowIndex = rowIndex+1;

                        this.dataRow5[nextRowIndex].requestFocus();
                        this.dataRow5[nextRowIndex].selectAll();
                    }
                }
                catch (NumberFormatException e)
                {
                    MyLogger.logError(getClass(), "exception: " + e.getMessage());
                    JOptionPane.showMessageDialog(
                            this, 
                            "您输入的储蓄有错误。", 
                            "输入错误",
                            JOptionPane.ERROR_MESSAGE);
                }
                
                break;
            }

            rowIndex++;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        if (this.transactionObj == null || this.jTextFieldShipName.getText().length()==0)
            return;
        
        // Response to button 'X' action
        if (ae.getSource() instanceof JButton)
        {
            JButton button = (JButton) ae.getSource();
            
            int k=0;
            for (JButton btn : buttonRow)
            {
                if (button == btn && dataRow2[k].getText().trim().length() > 0)
                {
                    int flag = JOptionPane.showConfirmDialog( this, "您确定要删除此交易记录？", "确认删除", JOptionPane.YES_NO_OPTION );
                    if (JOptionPane.YES_OPTION == flag)
                    {                 
                        this.deleteTransactionLine( this.transactionObj.getLineList().get(k) );
                        this.jButtonSave.doClick();                        
                    }
                    
                    break;
                }

                k++;
            }
        }
        else
        {        
            // Retrieve source of JTextField that trigger the event
            JTextField textField = (JTextField) ae.getSource();
            
            // Call to update Data Row 1, if the textField belong to it.
            updateDataRow1( textField, true );            

            // Call to update Data Row 3, if the textField belong to it.
            if (this.showingPrice == true)
            {
                updateDataRow3( textField, true );
            }

            // Call to update Data Row 4, if the textField belong to it.
            updateDataRow4( textField, true ); 

            // Call to update Data Row 5, if the textField belong to it.
            updateDataRow5( textField, true ); 
        }
    }        
    
    @Override
    public void keyTyped(KeyEvent ke) 
    {
        // do nothing
    }

    @Override
    public void keyPressed(KeyEvent ke) 
    {
        // do nothing
    }

    @Override
    public void keyReleased(KeyEvent ke) 
    {        
        if (this.transactionObj == null || this.jTextFieldShipName.getText().length()==0)
            return;
        
        JTextField textField = (JTextField) ke.getSource();
        
        // Call to update Data Row 1, if the textField belong to it.
        updateDataRow1( textField, false );            

        // Call to update Data Row 3, if the textField belong to it.
        if (this.showingPrice == true)
        {
            updateDataRow3( textField, false );
        }

        // Call to update Data Row 4, if the textField belong to it.
        updateDataRow4( textField, false ); 

        // Call to update Data Row 5, if the textField belong to it.
        updateDataRow5( textField, false );         
    }
    
    @Override
    public void mouseClicked(MouseEvent me) 
    {
        if (this.transactionObj == null || this.jTextFieldShipName.getText().length()==0)
            return;
        
        JTextField textField = (JTextField) me.getSource();

        int i=0;
        for (JTextField tf : dataRow1)
        {
            if (textField == tf)
            {
                // Open Item Dialog
                break;
            }

            i++;
        }

        int j=0;
        for (JTextField tf : dataRow2)
        {
            if (textField == tf)
            {
                if (j >=  this.transactionObj.getLineList().size())
                    break;
                
                InboundStockingLineDialog dialog = 
                        new InboundStockingLineDialog(
                                this.parent, 
                                true, 
                                this, 
                                this.transactionObj,
                                this.transactionObj.getLineList().get(j),
                                false);        
                SwingUtil.centerDialogOnScreen(dialog);

                break;
            }

            j++;
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        // do nothing
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        // do nothing
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        // do nothing
    }

    @Override
    public void mouseExited(MouseEvent me) {
        // do nothing
    }
    
    @Override
    public void wizardDialogClosed(SupplierBean supplier, long dateInMillis, int supplierTripNo) 
    {        
        this.saveTransaction();
        
        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "设置数据中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {    
                reset();
        
                supplierObj = supplier;

                // Instantiate new transaction bean
                transactionObj = new InTransactionBean();
                transactionObj.setSupplierId(supplier.getId());
                transactionObj.setSupplierName(supplier.getName());
                transactionObj.setSupplierTrip(supplierTripNo);
                transactionObj.setDateTime(dateInMillis);

                fillForm();

                // Make component jListCategory gains the focus.
                if (jListCategory.getModel().getSize() > 0) 
                {
                    jListCategory.requestFocus();
                    jListCategory.setSelectedIndex(0);
                }

                jButtonSave.setEnabled( false );
                jButtonDelete.setEnabled( true );
                jButtonPrice.setEnabled( true );
                jButtonPrintReceiptChinese.setEnabled( true );
                jButtonPrintReceiptMalay.setEnabled( true );
                jButtonPrintSaving.setEnabled( true );
                jButtonPrintSavingMalay.setEnabled( true );
                jButtonRecalculation.setEnabled( true );
                jButtonSave.setEnabled(true);
                
                return null;
            }

            @Override
            protected void done() {
                dialog.dispose();//close the modal dialog
            }
        };

        swingWorker.execute(); // this will start the processing on a separate thread
        SwingUtil.centerDialogOnScreen( dialog );
    }
    
    private SalesBean createNewSales(InTransactionLineBean lineBean)
    {
        SalesBean salesBean = new SalesBean();
        
        salesBean.setCustomerId( lineBean.getCustomerId() );
        salesBean.setCustomerName( lineBean.getCustomerName() );
        //salesBean.setCustomerTrip( CustomerDelegate.getInstance().getTripNoByCustomerIdAndDate(lineBean.getCustomerId(), transactionObj.getDateTime()) );
        salesBean.setCustomerTrip( 1 );
        salesBean.setDateTime( transactionObj.getDateTime() );
        
        return salesBean;
    }
    
    private SalesLineBean createNewSalesLine(SalesBean salesBean, InTransactionLineBean lineBean)
    {
        SalesLineBean salesLineBean = new SalesLineBean();
        salesLineBean.setSalesId(salesBean.getId());
        salesLineBean.setItemId(lineBean.getItemId());
        salesLineBean.setItemName(lineBean.getItemName());
        salesLineBean.setItemNewName(lineBean.getItemNewName());
        salesLineBean.setItemNameBm(lineBean.getItemNameBm());
        salesLineBean.setBucketNo(lineBean.getBucketNo());
        salesLineBean.setWeight(lineBean.getWeight());
        salesLineBean.setDateTime(salesBean.getDateTime());
        salesLineBean.setSupplierId( transactionObj.getSupplierId() );
        salesLineBean.setSupplierName( transactionObj.getSupplierName() );
        salesLineBean.setCustomerId(lineBean.getCustomerId());
        salesLineBean.setUnitPrice( 0.0d );
        salesBean.addLine(salesLineBean);

        return salesLineBean;
    }
        
    private SalesBucketBean createNewSalesBucket(SalesBean salesBean, InTransactionLineBean lineBean)
    {
        SalesBucketBean bucket = 
                SalesDelegate.getInstance().getBucketByNumberDateCustomer(
                        lineBean.getBucketNo(), salesBean.getDateTime(), lineBean.getCustomerId());
        
        if (bucket != null)            
        {
            return bucket;
        }
        
        SalesBucketBean salesBucketBean = new SalesBucketBean();
        salesBucketBean.setSalesId( salesBean.getId());
        salesBucketBean.setBucketNo( lineBean.getBucketNo() );
        salesBucketBean.setDateTime( salesBean.getDateTime() );
        salesBucketBean.setCustomerId(lineBean.getCustomerId());
        salesBean.addBucket(salesBucketBean);

        return salesBucketBean;
    }
    
    @Override
    public void stockingLineDialogClosed(InTransactionLineBean lineBean, SalesBean salesObj, boolean newLineBean)
    {   
        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "设置数据中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {    
                // Update UI first, calculation will continue and refresh the UI again later.
                if (newLineBean)
                    transactionObj.addLineBean(lineBean);
                
                updateDataRows(true);

                // This line of item assigns to NO CUSTOMER
                if (lineBean.getCustomerId() == null || lineBean.getCustomerId().length() == 0)
                {
                    // Add line object into current transaction object, and save into db.
                    addOrUpdateLineIntoTransactionObject( lineBean );
                    // Delete salesline, if exist            
                    deleteOldSalesLineIfExist(lineBean);
                }
                // This line of item assigns to ONE CUSTOMER
                else
                {            
                    // New inTransactionLine (without salesLine created)
                    if (lineBean.getSalesLineId()==null || "".equals(lineBean.getSalesLineId()))
                    {
                        // create new sales line, and add it into the existing sales object
                        SalesLineBean salesLineBean = createNewSalesLine(salesObj, lineBean);
                        salesLineBean.setUnitPrice( 0.0d );
                        // create new sales bucket, if any
                        createNewSalesBucket(salesObj, lineBean);

                        // Assign sales id to transaction line object.
                        lineBean.setSalesId( salesObj.getId() );        
                        lineBean.setSalesLineId( salesLineBean.getId() );

                        // Add line object into current transaction object, and save into db.
                        addOrUpdateLineIntoTransactionObject( lineBean );

                        // Save Sales
                        SalesDelegate.getInstance().saveOrUpdateSales(salesObj);  
                        // Save Transaction
                        //this.saveTransaction();

                        return null;
                    }

                    // retrieve sales line object from database, which bind with InTransactionLineBean. 
                    // * the lineBean object contains sale line id from previous save.
                    SalesLineBean salesLineBean = SalesDelegate.getInstance().getSalesLineById( lineBean.getSalesLineId() );

                    // Same Customer
                    // * lineBean (InTransactionLineBean) contains currently entered Customer ID
                    // * salesLineBean contains previously saved Customer ID.
                    if (salesLineBean!=null && lineBean.getCustomerId().equalsIgnoreCase( salesLineBean.getCustomerId() ))
                    {
                        // Same Sales ID
                        if (lineBean.getSalesId().equalsIgnoreCase( salesLineBean.getSalesId()))
                        {
                            // Add line object into current transaction object, and save into db.
                            addOrUpdateLineIntoTransactionObject( lineBean );
                            // Save Transaction
                            //this.saveTransaction();
                        }
                        // Different Sales ID
                        else
                        {
                            deleteOldSalesLineIfExist(lineBean);

                            // Create new Sales object
                            SalesBean newSalesBean = createNewSales( lineBean );
                            SalesLineBean salesLineBean2 = createNewSalesLine(newSalesBean, lineBean);
                            salesLineBean2.setUnitPrice( 0.0d );
                            createNewSalesBucket(newSalesBean, lineBean);

                            // Assign sales id to transaction line object.
                            lineBean.setSalesId(newSalesBean.getId());        
                            lineBean.setSalesLineId( salesLineBean2.getId() );

                            // Save Sales
                            SalesDelegate.getInstance().saveOrUpdateSales(newSalesBean); 
                            // Save Transaction
                            //this.saveTransaction();
                        }
                    }
                    // Different Customer
                    else
                    {                
                        // save Transaction
                        //this.saveTransaction();
                    }
                }

                // Update UI: Refresh the list of data on screen table.
                updateDataRowsFromServer(true);
                
                return null;
            }

            @Override
            protected void done() {
                dialog.dispose();//close the modal dialog
            }
        };

        swingWorker.execute(); // this will start the processing on a separate thread
        SwingUtil.centerDialogOnScreen( dialog );
    } // .end of stockingLineDialogClosed
     
    private void deleteOldSalesLineIfExist(InTransactionLineBean lineBean)
    {
        // retrieve sales line object from database, which bind with InTransactionLineBean. 
        // * the lineBean object contains sale line id from previous save.
        SalesLineBean salesLineBean = SalesDelegate.getInstance().getSalesLineById( lineBean.getSalesLineId() );
        
        // Since this requires new Sales object, so delete existing Sales Line.
        if (salesLineBean != null) 
        {
            // Delete sales line from db
            SalesDelegate.getInstance().deleteSalesLine(salesLineBean);
            // Retrieve sales object from db
            SalesBean salesBean = SalesDelegate.getInstance().getSalesById( salesLineBean.getSalesId() );
            
            if (salesBean != null)
            {
                // check if current sales bean has any other sales line.
                List<SalesLineBean> salesLineList = SalesDelegate.getInstance().getSalesLineBySalesId(salesBean.getId());
                if (salesLineList.size() == 0)
                {                
                    List<SalesBucketBean> bucketList = SalesDelegate.getInstance().getBucketListBySalesId( salesBean.getId() );

                    for (SalesBucketBean bucket : bucketList)
                        SalesDelegate.getInstance().deleteSalesBucket(bucket);

                    SalesDelegate.getInstance().deleteSales(salesBean);
                }                
            }            
        }            
    }
    
    @Override
    public void searchDialogClosed( InTransactionBean inTransactionBean )
    {
        this.saveTransaction();
        
        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "设置数据中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {    
                reset();
        
                transactionObj = inTransactionBean;
                fillForm();

                jButtonSave.setEnabled( true );
                jButtonDelete.setEnabled( true );
                jButtonPrice.setEnabled( true );
                jButtonPrintReceiptChinese.setEnabled( true );
                jButtonPrintReceiptMalay.setEnabled( true );
                jButtonPrintSaving.setEnabled( true );
                jButtonPrintSavingMalay.setEnabled( true );
                jButtonRecalculation.setEnabled( true );
                
                return null;
            }

            @Override
            protected void done() {
                dialog.dispose();//close the modal dialog
            }
        };

        swingWorker.execute(); // this will start the processing on a separate thread
        SwingUtil.centerDialogOnScreen( dialog );
    }
    
    protected void addOrUpdateLineIntoTransactionObject(InTransactionLineBean lineBean)
    {
        if (lineBean != null)
        {
            // Check if current line list full (max 60)
            if (this.transactionObj.getLineList().size() >= 60)
            {                
                JOptionPane.showMessageDialog(
                    this.getParent(), 
                    "每张进货单最多能接受60个交易。", 
                    "交易超额", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
                
            boolean found = false;
            for (InTransactionLineBean bean : this.transactionObj.getLineList())
            {
                if (bean.getId().equalsIgnoreCase( lineBean.getId() ))
                {
                    // remove identical lineBean, later add in again.
                    this.transactionObj.removeLineBean( bean );                    
                    found = true;
                    break;
                }
            }
            
            // Add new line object into list
            this.transactionObj.getLineList().add( lineBean );
            
            // Update SalesLineBean, if any
            if (lineBean.getSalesId() != null && lineBean.getSalesId().trim().length() > 0)
            {
                SalesBean salesBean = SalesDelegate.getInstance().getSalesById( lineBean.getSalesId() );
                if (salesBean != null) {
                    for (SalesLineBean salesLine : salesBean.getLineList())
                    {
                        if (salesLine.getId().equalsIgnoreCase(lineBean.getSalesLineId()))
                        {
                            salesLine.setItemNewName( lineBean.getItemNewName() );
                            salesLine.setWeight( lineBean.getWeight() );

                            break;
                        }
                    }

                    SalesDelegate.getInstance().saveOrUpdateSales(salesBean);
                }
            }
                        
            // Update total amount
            double totalAmount = 0.0d;
            for (InTransactionLineBean line : this.transactionObj.getLineList())
            {
                totalAmount = totalAmount + (line.getWeight() * line.getUnitPrice());
                System.err.println("total amount = " + totalAmount + " (added " + line.getItemNewName() + ")");
            }
            this.transactionObj.setTotalPrice(totalAmount);
        }
    }
    
    public void deleteTransactionLine(InTransactionLineBean lineBean)
    {
        // Update UI first
        int index = -1;
        for (InTransactionLineBean lineObj : this.transactionObj.getLineList()) {
            index++;
            if (lineObj.getId().equals(lineBean.getId()))
            {
                break;
            }
        }
        this.transactionObj.getLineList().remove(index);
        this.updateDataRows(true);
        
        // If the object does not exist, then quit from this process.
        if (index == -1)
        {
            return;
        }
        else
        {            
            this.deleteOldSalesLineIfExist(lineBean);
            
            // Remove this line of transaction from database
            InTransactionDelegate.getInstance().deleteTransactionLine(lineBean);            
            // Refill the display list
            this.updateDataRowsFromServer(true);
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

        jLabelTitle = new javax.swing.JLabel();
        jPanelCenter = new javax.swing.JPanel();
        jPanelAction = new javax.swing.JPanel();
        jPanelActionLeft = new javax.swing.JPanel();
        jPanelShipName = new javax.swing.JPanel();
        jLabelShipName = new javax.swing.JLabel();
        jTextFieldShipName = new javax.swing.JTextField();
        jPanelTripNumber = new javax.swing.JPanel();
        jLabelTripNumber = new javax.swing.JLabel();
        jTextFieldTripNumber = new javax.swing.JTextField();
        jPanelDate = new javax.swing.JPanel();
        jLabelDate = new javax.swing.JLabel();
        jTextFieldDate = new javax.swing.JTextField();
        jPanelInvoiceNumber = new javax.swing.JPanel();
        jLabelInvoiceNumber = new javax.swing.JLabel();
        jTextFieldInvoiceNumber = new javax.swing.JTextField();
        jPanelActionRight = new javax.swing.JPanel();
        jButtonEnterDate = new javax.swing.JButton();
        jButtonSearch = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonLogout = new javax.swing.JButton();
        jPanelContent = new javax.swing.JPanel();
        jPanelContentLeft = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListCategory = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListItem = new javax.swing.JList();
        jPanel7 = new javax.swing.JPanel();
        jPanelContentRight = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jButtonPrice = new javax.swing.JButton();
        jButtonPrintReceiptChinese = new javax.swing.JButton();
        jButtonPrintReceiptMalay = new javax.swing.JButton();
        jButtonPrintSaving = new javax.swing.JButton();
        jButtonPrintSavingMalay = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jButtonRecalculation = new javax.swing.JButton();
        jLabelTotalAmountTitle = new javax.swing.JLabel();
        jLabelTotalAmountValue = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabelTotalSavingValue = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabelTotalBonusValue = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanelLineList1 = new javax.swing.JPanel();
        jPanelLineList2 = new javax.swing.JPanel();
        jPanelLineList3 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jLabelTitle.setBackground(new java.awt.Color(51, 51, 51));
        jLabelTitle.setFont(new java.awt.Font("KaiTi", 1, 48)); // NOI18N
        jLabelTitle.setForeground(new java.awt.Color(255, 204, 0));
        jLabelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitle.setText("海春鱼行进货系统");
        jLabelTitle.setOpaque(true);
        jLabelTitle.setPreferredSize(new java.awt.Dimension(441, 60));
        add(jLabelTitle, java.awt.BorderLayout.PAGE_START);

        jPanelCenter.setLayout(new java.awt.BorderLayout());

        jPanelAction.setPreferredSize(new java.awt.Dimension(20, 100));
        jPanelAction.setLayout(new java.awt.BorderLayout());

        jPanelActionLeft.setBackground(new java.awt.Color(255, 204, 102));
        jPanelActionLeft.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanelShipName.setPreferredSize(new java.awt.Dimension(350, 90));
        jPanelShipName.setLayout(new java.awt.BorderLayout());

        jLabelShipName.setBackground(new java.awt.Color(51, 51, 51));
        jLabelShipName.setFont(new java.awt.Font("KaiTi", 1, 14)); // NOI18N
        jLabelShipName.setForeground(new java.awt.Color(255, 255, 255));
        jLabelShipName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelShipName.setText("船户号码 / 名字");
        jLabelShipName.setOpaque(true);
        jPanelShipName.add(jLabelShipName, java.awt.BorderLayout.PAGE_START);

        jTextFieldShipName.setEditable(false);
        jTextFieldShipName.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jTextFieldShipName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanelShipName.add(jTextFieldShipName, java.awt.BorderLayout.CENTER);

        jPanelActionLeft.add(jPanelShipName);

        jPanelTripNumber.setPreferredSize(new java.awt.Dimension(90, 90));
        jPanelTripNumber.setLayout(new java.awt.BorderLayout());

        jLabelTripNumber.setBackground(new java.awt.Color(51, 51, 51));
        jLabelTripNumber.setFont(new java.awt.Font("KaiTi", 1, 14)); // NOI18N
        jLabelTripNumber.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTripNumber.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTripNumber.setText("来货次数");
        jLabelTripNumber.setOpaque(true);
        jPanelTripNumber.add(jLabelTripNumber, java.awt.BorderLayout.PAGE_START);

        jTextFieldTripNumber.setEditable(false);
        jTextFieldTripNumber.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jTextFieldTripNumber.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanelTripNumber.add(jTextFieldTripNumber, java.awt.BorderLayout.CENTER);

        jPanelActionLeft.add(jPanelTripNumber);

        jPanelDate.setPreferredSize(new java.awt.Dimension(180, 90));
        jPanelDate.setLayout(new java.awt.BorderLayout());

        jLabelDate.setBackground(new java.awt.Color(51, 51, 51));
        jLabelDate.setFont(new java.awt.Font("KaiTi", 1, 14)); // NOI18N
        jLabelDate.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDate.setText("来货日期");
        jLabelDate.setOpaque(true);
        jPanelDate.add(jLabelDate, java.awt.BorderLayout.PAGE_START);

        jTextFieldDate.setEditable(false);
        jTextFieldDate.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jTextFieldDate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanelDate.add(jTextFieldDate, java.awt.BorderLayout.CENTER);

        jPanelActionLeft.add(jPanelDate);

        jPanelInvoiceNumber.setPreferredSize(new java.awt.Dimension(160, 90));
        jPanelInvoiceNumber.setLayout(new java.awt.BorderLayout());

        jLabelInvoiceNumber.setBackground(new java.awt.Color(51, 51, 51));
        jLabelInvoiceNumber.setFont(new java.awt.Font("KaiTi", 1, 14)); // NOI18N
        jLabelInvoiceNumber.setForeground(new java.awt.Color(255, 255, 255));
        jLabelInvoiceNumber.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelInvoiceNumber.setText("单据号码");
        jLabelInvoiceNumber.setOpaque(true);
        jPanelInvoiceNumber.add(jLabelInvoiceNumber, java.awt.BorderLayout.PAGE_START);

        jTextFieldInvoiceNumber.setEditable(false);
        jTextFieldInvoiceNumber.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jTextFieldInvoiceNumber.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldInvoiceNumber.setText("- - - -");
        jPanelInvoiceNumber.add(jTextFieldInvoiceNumber, java.awt.BorderLayout.CENTER);

        jPanelActionLeft.add(jPanelInvoiceNumber);

        jPanelAction.add(jPanelActionLeft, java.awt.BorderLayout.CENTER);

        jPanelActionRight.setBackground(new java.awt.Color(255, 204, 102));
        jPanelActionRight.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButtonEnterDate.setBackground(new java.awt.Color(102, 204, 0));
        jButtonEnterDate.setFont(new java.awt.Font("KaiTi", 1, 28)); // NOI18N
        jButtonEnterDate.setText("新单");
        jButtonEnterDate.setPreferredSize(new java.awt.Dimension(100, 90));
        jButtonEnterDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEnterDateActionPerformed(evt);
            }
        });
        jPanelActionRight.add(jButtonEnterDate);

        jButtonSearch.setBackground(new java.awt.Color(204, 0, 0));
        jButtonSearch.setFont(new java.awt.Font("KaiTi", 1, 28)); // NOI18N
        jButtonSearch.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSearch.setText("搜索旧单");
        jButtonSearch.setPreferredSize(new java.awt.Dimension(160, 90));
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });
        jPanelActionRight.add(jButtonSearch);

        jButtonSave.setBackground(new java.awt.Color(204, 0, 0));
        jButtonSave.setFont(new java.awt.Font("KaiTi", 1, 28)); // NOI18N
        jButtonSave.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSave.setText("储存");
        jButtonSave.setEnabled(false);
        jButtonSave.setPreferredSize(new java.awt.Dimension(100, 90));
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        jPanelActionRight.add(jButtonSave);

        jButtonDelete.setBackground(new java.awt.Color(204, 0, 0));
        jButtonDelete.setFont(new java.awt.Font("KaiTi", 1, 28)); // NOI18N
        jButtonDelete.setForeground(new java.awt.Color(255, 255, 255));
        jButtonDelete.setText("删除");
        jButtonDelete.setEnabled(false);
        jButtonDelete.setPreferredSize(new java.awt.Dimension(100, 90));
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        jPanelActionRight.add(jButtonDelete);

        jButtonLogout.setBackground(new java.awt.Color(204, 0, 0));
        jButtonLogout.setFont(new java.awt.Font("KaiTi", 1, 28)); // NOI18N
        jButtonLogout.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLogout.setText("退出");
        jButtonLogout.setPreferredSize(new java.awt.Dimension(100, 90));
        jButtonLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogoutActionPerformed(evt);
            }
        });
        jPanelActionRight.add(jButtonLogout);

        jPanelAction.add(jPanelActionRight, java.awt.BorderLayout.EAST);

        jPanelCenter.add(jPanelAction, java.awt.BorderLayout.NORTH);

        jPanelContent.setLayout(new java.awt.BorderLayout());

        jPanelContentLeft.setPreferredSize(new java.awt.Dimension(400, 100));
        jPanelContentLeft.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.GridLayout(1, 2));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "分类", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jListCategory.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListCategory.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jListCategoryKeyPressed(evt);
            }
        });
        jListCategory.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListCategoryValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListCategory);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "鱼类", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel2.setLayout(new java.awt.BorderLayout());

        jListItem.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListItem.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListItemMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jListItem);

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel2);

        jPanelContentLeft.add(jPanel6, java.awt.BorderLayout.CENTER);
        jPanelContentLeft.add(jPanel7, java.awt.BorderLayout.EAST);

        jPanelContent.add(jPanelContentLeft, java.awt.BorderLayout.EAST);

        jPanelContentRight.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButtonPrice.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonPrice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/billing.png"))); // NOI18N
        jButtonPrice.setText("显示价格");
        jButtonPrice.setEnabled(false);
        jButtonPrice.setPreferredSize(new java.awt.Dimension(150, 50));
        jButtonPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPriceActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonPrice);

        jButtonPrintReceiptChinese.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonPrintReceiptChinese.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/print.png"))); // NOI18N
        jButtonPrintReceiptChinese.setText("打印中文");
        jButtonPrintReceiptChinese.setToolTipText("");
        jButtonPrintReceiptChinese.setEnabled(false);
        jButtonPrintReceiptChinese.setPreferredSize(new java.awt.Dimension(150, 50));
        jButtonPrintReceiptChinese.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintReceiptChineseActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonPrintReceiptChinese);

        jButtonPrintReceiptMalay.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonPrintReceiptMalay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/print.png"))); // NOI18N
        jButtonPrintReceiptMalay.setText("打印马来文");
        jButtonPrintReceiptMalay.setToolTipText("");
        jButtonPrintReceiptMalay.setEnabled(false);
        jButtonPrintReceiptMalay.setPreferredSize(new java.awt.Dimension(160, 50));
        jButtonPrintReceiptMalay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintReceiptMalayActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonPrintReceiptMalay);

        jButtonPrintSaving.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonPrintSaving.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/print.png"))); // NOI18N
        jButtonPrintSaving.setText("打印储蓄");
        jButtonPrintSaving.setToolTipText("");
        jButtonPrintSaving.setEnabled(false);
        jButtonPrintSaving.setPreferredSize(new java.awt.Dimension(150, 50));
        jButtonPrintSaving.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintSavingActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonPrintSaving);

        jButtonPrintSavingMalay.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonPrintSavingMalay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/print.png"))); // NOI18N
        jButtonPrintSavingMalay.setText("打印马来文储蓄");
        jButtonPrintSavingMalay.setToolTipText("");
        jButtonPrintSavingMalay.setEnabled(false);
        jButtonPrintSavingMalay.setPreferredSize(new java.awt.Dimension(210, 50));
        jButtonPrintSavingMalay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintSavingMalayActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonPrintSavingMalay);

        jPanel4.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jButtonRecalculation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/7087_refresh.png"))); // NOI18N
        jButtonRecalculation.setEnabled(false);
        jButtonRecalculation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRecalculationActionPerformed(evt);
            }
        });
        jPanel9.add(jButtonRecalculation);

        jLabelTotalAmountTitle.setFont(new java.awt.Font("KaiTi", 1, 40)); // NOI18N
        jLabelTotalAmountTitle.setForeground(new java.awt.Color(0, 102, 255));
        jLabelTotalAmountTitle.setText("总额:");
        jPanel9.add(jLabelTotalAmountTitle);

        jLabelTotalAmountValue.setFont(new java.awt.Font("KaiTi", 1, 40)); // NOI18N
        jLabelTotalAmountValue.setForeground(new java.awt.Color(0, 102, 255));
        jLabelTotalAmountValue.setText("RM0.00");
        jPanel9.add(jLabelTotalAmountValue);

        jPanel8.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jLabel1.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jLabel1.setText("储蓄：RM");
        jPanel10.add(jLabel1);

        jLabelTotalSavingValue.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelTotalSavingValue.setText("0.00");
        jPanel10.add(jLabelTotalSavingValue);

        jLabel2.setFont(new java.awt.Font("KaiTi", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("红利：RM");
        jPanel10.add(jLabel2);

        jLabelTotalBonusValue.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabelTotalBonusValue.setForeground(new java.awt.Color(255, 0, 0));
        jLabelTotalBonusValue.setText("0.00");
        jPanel10.add(jLabelTotalBonusValue);

        jPanel8.add(jPanel10, java.awt.BorderLayout.NORTH);

        jPanel4.add(jPanel8, java.awt.BorderLayout.EAST);

        jPanelContentRight.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.GridLayout(1, 3));

        jPanelLineList1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "列表（一）", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanelLineList1.setLayout(new java.awt.GridLayout(21, 5, 1, 1));
        jPanel3.add(jPanelLineList1);

        jPanelLineList2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "列表（二）", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanelLineList2.setLayout(new java.awt.GridLayout(21, 5, 1, 1));
        jPanel3.add(jPanelLineList2);

        jPanelLineList3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "列表（三）", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanelLineList3.setLayout(new java.awt.GridLayout(21, 5, 1, 1));
        jPanel3.add(jPanelLineList3);

        jPanelContentRight.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanelContent.add(jPanelContentRight, java.awt.BorderLayout.CENTER);

        jPanelCenter.add(jPanelContent, java.awt.BorderLayout.CENTER);

        add(jPanelCenter, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed
        
        jButtonSave.doClick();
        
        ((JFrameMain)this.parent).switchToMainPanel();
    }//GEN-LAST:event_jButtonLogoutActionPerformed

    private void jButtonEnterDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEnterDateActionPerformed
                
        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "开启中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            InboundWizardDialog wizardDialog;
            @Override
            protected Void doInBackground() throws Exception 
            {    
                wizardDialog = new InboundWizardDialog(parent, true, InboundStockingPanel.this); 
                return null;
            }

            @Override
            protected void done() {
                dialog.dispose();//close the modal dialog
                SwingUtil.centerDialogOnScreen(wizardDialog);
            }
        };

        swingWorker.execute(); // this will start the processing on a separate thread
        SwingUtil.centerDialogOnScreen( dialog );
    }//GEN-LAST:event_jButtonEnterDateActionPerformed

    private void jListCategoryValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListCategoryValueChanged
        
        if ( this.jListCategory.getSelectedIndex() == -1 )
            return;
        
        Object categoryObj = 
                this.jListCategory.getModel().getElementAt( this.jListCategory.getSelectedIndex() );
        
        if (categoryObj == null)
            return;
        
        // Clear item list
        this.listModelItem.removeAllElements();
        
        CategoryBean selectedCategory = (CategoryBean) categoryObj;        
        List<ItemBean> itemList = 
                SystemDataDelegate.getInstance().getItemsByCategoryId(selectedCategory.getId());
        
        for (ItemBean item : itemList) {
            this.listModelItem.addElement(item);
        }
    }//GEN-LAST:event_jListCategoryValueChanged

    private void jListCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jListCategoryKeyPressed

        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "开启中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {    
                // Response to Key Type event from ENTER (code:10)
                if (evt.getKeyCode() == 10) 
                {
                    Object categoryObj = 
                            jListCategory.getModel().getElementAt( jListCategory.getSelectedIndex() );

                    if (categoryObj == null)
                        return null;

                    if (jListItem.getModel().getSize() > 0)
                    {
                        jListItem.requestFocus();
                        jListItem.setSelectedIndex(0);
                    }
                } 
                return null;
            }

            @Override
            protected void done() {
                dialog.dispose();//close the modal dialog
            }
        };

        swingWorker.execute(); // this will start the processing on a separate thread
        SwingUtil.centerDialogOnScreen( dialog );
        
    }//GEN-LAST:event_jListCategoryKeyPressed
    
    public void closing()
    {
        saveTransaction();
    } // .end of closing
    
    private void saveTransaction()
    {
        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "储存数据中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {    
                if (transactionObj == null)
                    return null;

                // Check if this is empty transaction.
                // * Condition: DO NOT SAVE EMPTY TRANSACTION
                if (transactionObj.getLineList().isEmpty())
                    return null;                

                jButtonRecalculation.doClick(); 
                // Proceed to Save Transaction
                InTransactionDelegate.getInstance().saveTransaction(transactionObj);
                MyLogger.logInfo(getClass(), "Sales object saved.");
                
                return null;
            }

            @Override
            protected void done() {
                dialog.dispose();//close the modal dialog
            }
        };

        swingWorker.execute(); // this will start the processing on a separate thread
        SwingUtil.centerDialogOnScreen( dialog );        
    }
    
    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        saveTransaction();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        InboundSearchDialog dialog = new InboundSearchDialog(this.parent, true, this);        
        SwingUtil.centerDialogOnScreen(dialog);
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jListItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListItemMouseClicked
        int selectedItemIndex = this.jListItem.getSelectedIndex();
        if (selectedItemIndex == -1) {
            JOptionPane.showMessageDialog(this, "请选择一项鱼类。", "选择错误", JOptionPane.WARNING_MESSAGE);
            return;            
        }
        
        Object itemObj = this.jListItem.getModel().getElementAt( selectedItemIndex );
        if (itemObj == null) {
            JOptionPane.showMessageDialog(this, "请选择一项鱼类。", "选择错误", JOptionPane.WARNING_MESSAGE);
            return;            
        }
        
        ItemBean itemBean = (ItemBean) itemObj;
        InTransactionLineBean lineBean = new InTransactionLineBean();
        lineBean.setItemId( itemBean.getId() );
        lineBean.setItemName( itemBean.getName() );
        lineBean.setItemNewName( itemBean.getName() );
        lineBean.setItemNameBm( itemBean.getNameBm() );
        lineBean.setUnitPrice( itemBean.getPrice() );
        lineBean.setDateTime( this.transactionObj.getDateTime() );
        lineBean.setSupplierId( this.transactionObj.getSupplierId() );
        lineBean.setInTransactionId( this.transactionObj.getId() );
        
        InboundStockingLineDialog dialog = 
                new InboundStockingLineDialog(
                        this.parent, 
                        true, 
                        this, 
                        this.transactionObj,
                        lineBean,
                        true);        
        SwingUtil.centerDialogOnScreen(dialog);
    }//GEN-LAST:event_jListItemMouseClicked

    private void jButtonPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPriceActionPerformed
        
        if (this.transactionObj == null || this.transactionObj.getLineList() == null)
            return;
        
        if (this.jButtonPrice.getText().equalsIgnoreCase( "显示价格" ))
        {
            this.showingPrice = true;
            dataHeader1[2].setText( "价格" );
            dataHeader2[2].setText( "价格" );
            dataHeader3[2].setText( "价格" );
                
            int i = 0;
            for (InTransactionLineBean lineBean : this.transactionObj.getLineList())
            {                
                this.dataRow3[i].setEditable(true);
                this.dataRow3[i].setBackground( Color.BLACK );
                this.dataRow3[i].setForeground( Color.GREEN );
                this.dataRow3[i].setText( currencyFormatter.format(this.transactionObj.getLineList().get(i).getUnitPrice()) );
                
                this.dataRow5[i].setEditable(true);
                this.dataRow5[i].setBackground( Color.BLACK );
                this.dataRow5[i].setForeground( Color.GREEN );
                this.dataRow5[i].setText( currencyFormatter.format(this.transactionObj.getLineList().get(i).getSaving()) );
                
                i++;
            }
            
            this.jButtonPrice.setText( "隐藏价格" );
        }
        else
        {
            this.showingPrice = false;
            dataHeader1[2].setText( "桶号" );
            dataHeader2[2].setText( "桶号" );
            dataHeader3[2].setText( "桶号" );
            
            int i = 0;
            for (InTransactionLineBean lineBean : this.transactionObj.getLineList())
            {
                this.dataRow3[i].setEditable(false);
                this.dataRow3[i].setBackground( Color.WHITE );
                this.dataRow3[i].setForeground( Color.BLACK );
                this.dataRow3[i].setText( this.transactionObj.getLineList().get(i).getBucketNo() );
                
                this.dataRow5[i].setEditable(false);
                this.dataRow5[i].setBackground( Color.WHITE );
                this.dataRow5[i].setForeground( Color.BLACK );
                this.dataRow5[i].setText( "" );
                
                i++;
            }
            
            this.jButtonPrice.setText( "显示价格" );
        }
    }//GEN-LAST:event_jButtonPriceActionPerformed

    private void jButtonPrintReceiptChineseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintReceiptChineseActionPerformed
        try 
        {            
            this.saveTransaction();
            
            int priceFlag = JOptionPane.showConfirmDialog(this, "一并打印价格？", "确认打印", JOptionPane.YES_NO_CANCEL_OPTION);
            if (priceFlag == JOptionPane.CANCEL_OPTION)
                return;
            
            String filePath = this.saveToPath + Calendar.getInstance().getTimeInMillis() + ".pdf";
            ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "准备报告 PDF 文档中...");

            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception 
                {
                    // Disable buttons
                    jButtonPrintReceiptChinese.setEnabled(false);
                    jButtonPrintReceiptMalay.setEnabled(false);
                    jButtonPrintSaving.setEnabled(false);
                    jButtonPrintSavingMalay.setEnabled(false);
                    
                    // DO NOT PRINT for empty transaction
                    if (InboundStockingPanel.this.transactionObj==null)
                    {
                        return null;
                    }

                    // No transaction id, so create a new one
                    if (InboundStockingPanel.this.transactionObj.getTransactionNo() == null || 
                        InboundStockingPanel.this.transactionObj.getTransactionNo().length() == 0)
                    {
                        InboundStockingPanel.this.transactionObj.setTransactionNo(
                            SystemDataDelegate.getInstance().getNextInTransactionId());

                        jTextFieldInvoiceNumber.setText( InboundStockingPanel.this.transactionObj.getTransactionNo() );

                        InTransactionDelegate.getInstance().saveTransaction(transactionObj);
                    }

                    SupplierBean supplier = 
                        SupplierDelegate.getInstance().getSupplierById( 
                                InboundStockingPanel.this.transactionObj.getSupplierId() );

                    // Send to Printer
                    InboundPDFBoxPrinter.getInstance().printReportSupplierTransactionReportChinese(
                            filePath, supplier, InboundStockingPanel.this.transactionObj, JOptionPane.YES_OPTION==priceFlag);

                    return null;
                }

                @Override
                protected void done() {                    
                    // Enable buttons
                    jButtonPrintReceiptChinese.setEnabled(true);
                    jButtonPrintReceiptMalay.setEnabled(true);
                    jButtonPrintSaving.setEnabled(true);
                    jButtonPrintSavingMalay.setEnabled(true);
                    
                    dialog.dispose();//close the modal dialog
                }
            };

            swingWorker.execute(); // this will start the processing on a separate thread
            SwingUtil.centerDialogOnScreen( dialog );

            //JOptionPane.showMessageDialog(this.getParent(), "PDF 文档已被储存至 " + filePath);
            // Open created PDF on fly
            if (RootPDFBoxPrinter.OPEN_PDF_ON_FLY == true)
                InboundPDFBoxPrinter.getInstance().openPDFonFly(filePath);

            Desktop desktop = Desktop.getDesktop();
            desktop.print(new File(filePath));
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
        }
    }//GEN-LAST:event_jButtonPrintReceiptChineseActionPerformed

    private void jButtonPrintSavingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintSavingActionPerformed
        try 
        {
            this.saveTransaction();
            
            int priceFlag = JOptionPane.showConfirmDialog(this, "一并打印价格？", "确认打印", JOptionPane.YES_NO_CANCEL_OPTION);
            if (priceFlag == JOptionPane.CANCEL_OPTION)
                return;
            
            String filePath = this.saveToPath + Calendar.getInstance().getTimeInMillis() + ".pdf";
            ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "准备报告 PDF 文档中...");

            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception 
                {
                    // Disable buttons
                    jButtonPrintReceiptChinese.setEnabled(false);
                    jButtonPrintReceiptMalay.setEnabled(false);
                    jButtonPrintSaving.setEnabled(false);
                    jButtonPrintSavingMalay.setEnabled(false);
                    
                    // DO NOT PRINT for empty transaction
                    if (InboundStockingPanel.this.transactionObj==null)
                    {
                        return null;
                    }

                    // No transaction id, so create a new one
                    if (InboundStockingPanel.this.transactionObj.getTransactionNo() == null || 
                        InboundStockingPanel.this.transactionObj.getTransactionNo().length() == 0)
                    {
                        InboundStockingPanel.this.transactionObj.setTransactionNo(
                            SystemDataDelegate.getInstance().getNextInTransactionId());

                        jTextFieldInvoiceNumber.setText( InboundStockingPanel.this.transactionObj.getTransactionNo() );

                        InTransactionDelegate.getInstance().saveTransaction(transactionObj);
                    }

                    InTransactionBean bean = 
                            InTransactionDelegate.getInstance().getTransactionById( 
                                InboundStockingPanel.this.transactionObj.getId() );

                    SupplierBean supplier = SupplierDelegate.getInstance().getSupplierById( bean.getSupplierId() );

                    // SEND TO Printer
                    InboundPDFBoxPrinter.getInstance().printReportSupplierTransactionReportSaving(
                            filePath, supplier, bean, JOptionPane.YES_OPTION==priceFlag);

                    return null;
                }

                @Override
                protected void done() {
                    // Disable buttons
                    jButtonPrintReceiptChinese.setEnabled(true);
                    jButtonPrintReceiptMalay.setEnabled(true);
                    jButtonPrintSaving.setEnabled(true);
                    jButtonPrintSavingMalay.setEnabled(true);
                    
                    dialog.dispose();//close the modal dialog
                }
            };

            swingWorker.execute(); // this will start the processing on a separate thread
            SwingUtil.centerDialogOnScreen( dialog );

            // Open created PDF on fly
            if (RootPDFBoxPrinter.OPEN_PDF_ON_FLY == true)
                InboundPDFBoxPrinter.getInstance().openPDFonFly(filePath);

            Desktop desktop = Desktop.getDesktop();
            desktop.print(new File(filePath));
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
        }
    }//GEN-LAST:event_jButtonPrintSavingActionPerformed

    private void jButtonPrintReceiptMalayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintReceiptMalayActionPerformed
        try 
        {
            this.saveTransaction();
            
            int priceFlag = JOptionPane.showConfirmDialog(this, "一并打印价格？", "确认打印", JOptionPane.YES_NO_CANCEL_OPTION);
            if (priceFlag == JOptionPane.CANCEL_OPTION)
                return;
            
            String filePath = this.saveToPath + Calendar.getInstance().getTimeInMillis() + ".pdf";
            ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "准备报告 PDF 文档中...");

            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception 
                {
                    // Disable buttons
                    jButtonPrintReceiptChinese.setEnabled(false);
                    jButtonPrintReceiptMalay.setEnabled(false);
                    jButtonPrintSaving.setEnabled(false);
                    jButtonPrintSavingMalay.setEnabled(false);
                    
                    // DO NOT PRINT for empty transaction
                    if (InboundStockingPanel.this.transactionObj==null)
                    {
                        return null;
                    }

                    // No transaction id, so create a new one
                    if (InboundStockingPanel.this.transactionObj.getTransactionNo() == null || 
                        InboundStockingPanel.this.transactionObj.getTransactionNo().length() == 0)
                    {
                        InboundStockingPanel.this.transactionObj.setTransactionNo(
                            SystemDataDelegate.getInstance().getNextInTransactionId());

                        jTextFieldInvoiceNumber.setText( InboundStockingPanel.this.transactionObj.getTransactionNo() );

                        InTransactionDelegate.getInstance().saveTransaction(transactionObj);
                    }

                    SupplierBean supplier = 
                        SupplierDelegate.getInstance().getSupplierById( 
                                InboundStockingPanel.this.transactionObj.getSupplierId() );

                    // SEND to Printer
                    InboundPDFBoxPrinter.getInstance().printReportSupplierTransactionReportMalay(
                            filePath, supplier, InboundStockingPanel.this.transactionObj, JOptionPane.YES_OPTION==priceFlag);

                    return null;
                }

                @Override
                protected void done() {
                    // Disable buttons
                    jButtonPrintReceiptChinese.setEnabled(true);
                    jButtonPrintReceiptMalay.setEnabled(true);
                    jButtonPrintSaving.setEnabled(true);
                    jButtonPrintSavingMalay.setEnabled(true);
                    
                    dialog.dispose();//close the modal dialog
                }
            };

            swingWorker.execute(); // this will start the processing on a separate thread
            SwingUtil.centerDialogOnScreen( dialog );

            // Open created PDF on fly
            if (RootPDFBoxPrinter.OPEN_PDF_ON_FLY == true)
                InboundPDFBoxPrinter.getInstance().openPDFonFly(filePath);

            Desktop desktop = Desktop.getDesktop();
            desktop.print(new File(filePath));
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
        }
    }//GEN-LAST:event_jButtonPrintReceiptMalayActionPerformed

    private void jButtonPrintSavingMalayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintSavingMalayActionPerformed
        try 
        {
            this.saveTransaction();
            
            int priceFlag = JOptionPane.showConfirmDialog(this, "一并打印价格？", "确认打印", JOptionPane.YES_NO_CANCEL_OPTION);            
            if (priceFlag == JOptionPane.CANCEL_OPTION)
                return;
            
            String filePath = this.saveToPath + Calendar.getInstance().getTimeInMillis() + ".pdf";
            ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "准备报告 PDF 文档中...");

            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception 
                {
                    // Disable buttons
                    jButtonPrintReceiptChinese.setEnabled(false);
                    jButtonPrintReceiptMalay.setEnabled(false);
                    jButtonPrintSaving.setEnabled(false);
                    jButtonPrintSavingMalay.setEnabled(false);
                    
                    // DO NOT PRINT for empty transaction
                    if (InboundStockingPanel.this.transactionObj==null)
                    {
                        return null;
                    }

                    // No transaction id, so create a new one
                    if (InboundStockingPanel.this.transactionObj.getTransactionNo() == null || 
                        InboundStockingPanel.this.transactionObj.getTransactionNo().length() == 0)
                    {
                        InboundStockingPanel.this.transactionObj.setTransactionNo(
                            SystemDataDelegate.getInstance().getNextInTransactionId());

                        jTextFieldInvoiceNumber.setText( InboundStockingPanel.this.transactionObj.getTransactionNo() );

                        InTransactionDelegate.getInstance().saveTransaction(transactionObj);
                    }

                    InTransactionBean bean = 
                            InTransactionDelegate.getInstance().getTransactionById( 
                                InboundStockingPanel.this.transactionObj.getId() );

                    SupplierBean supplier = SupplierDelegate.getInstance().getSupplierById( bean.getSupplierId() );

                    // SEND TO Printer
                    InboundPDFBoxPrinter.getInstance().printReportSupplierTransactionReportSavingMalay(
                            filePath, supplier, bean, JOptionPane.YES_OPTION==priceFlag);

                    return null;
                }

                @Override
                protected void done() {
                    // Enable buttons
                    jButtonPrintReceiptChinese.setEnabled(true);
                    jButtonPrintReceiptMalay.setEnabled(true);
                    jButtonPrintSaving.setEnabled(true);
                    jButtonPrintSavingMalay.setEnabled(true);
                    
                    dialog.dispose();//close the modal dialog
                }
            };

            swingWorker.execute(); // this will start the processing on a separate thread
            SwingUtil.centerDialogOnScreen( dialog );

            // Open created PDF on fly
            if (RootPDFBoxPrinter.OPEN_PDF_ON_FLY == true)
                InboundPDFBoxPrinter.getInstance().openPDFonFly(filePath);

            Desktop desktop = Desktop.getDesktop();
            desktop.print(new File(filePath));
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
        }
    }//GEN-LAST:event_jButtonPrintSavingMalayActionPerformed

    private void jButtonRecalculationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRecalculationActionPerformed
        
        int rowIndex = 0;
        for (InTransactionLineBean lineObj : this.transactionObj.getLineList()) {
            
            double unitPrice = 0;
            double weight = 0;
            double saving = 0;
            
            try {
                if (showingPrice)
                    unitPrice = Double.parseDouble( this.dataRow3[rowIndex].getText().trim() );
                else
                    unitPrice = lineObj.getUnitPrice();
            } catch (NumberFormatException e) {
                unitPrice = 0;
            }
            
            try {
                if (showingPrice)
                    weight = Double.parseDouble( this.dataRow4[rowIndex].getText().trim() );
                else
                    weight = lineObj.getWeight();
            } catch (NumberFormatException e) {
                weight = 0;
            }
            
            try {
                if (showingPrice)
                    saving = Double.parseDouble( this.dataRow5[rowIndex].getText().trim() );
                else
                    saving = lineObj.getSaving();
            } catch (NumberFormatException e) {
                saving = 0;
            }
            
            this.transactionObj.getLineList().get(rowIndex).setUnitPrice(unitPrice);
            this.transactionObj.getLineList().get(rowIndex).setWeight(weight);
            this.transactionObj.getLineList().get(rowIndex).setSaving(saving);
            
            rowIndex++;
        }
        
        this.updateDataRows(false);
    }//GEN-LAST:event_jButtonRecalculationActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        if (this.transactionObj != null) {
            int flag = JOptionPane.showConfirmDialog(this, "删除此单？", "确认删除", JOptionPane.YES_NO_CANCEL_OPTION);            
            if (flag == JOptionPane.CANCEL_OPTION)
                return;
            
            for (InTransactionLineBean lineObj : transactionObj.getLineList()) {
                InTransactionDelegate.getInstance().deleteTransactionLine(lineObj);
            }
            
            InTransactionDelegate.getInstance().deleteTransaction(transactionObj);
            
            this.transactionObj = null;
            
            reset();
        }
    }//GEN-LAST:event_jButtonDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonEnterDate;
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonPrice;
    private javax.swing.JButton jButtonPrintReceiptChinese;
    private javax.swing.JButton jButtonPrintReceiptMalay;
    private javax.swing.JButton jButtonPrintSaving;
    private javax.swing.JButton jButtonPrintSavingMalay;
    private javax.swing.JButton jButtonRecalculation;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelInvoiceNumber;
    private javax.swing.JLabel jLabelShipName;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelTotalAmountTitle;
    private javax.swing.JLabel jLabelTotalAmountValue;
    private javax.swing.JLabel jLabelTotalBonusValue;
    private javax.swing.JLabel jLabelTotalSavingValue;
    private javax.swing.JLabel jLabelTripNumber;
    private javax.swing.JList jListCategory;
    private javax.swing.JList jListItem;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelAction;
    private javax.swing.JPanel jPanelActionLeft;
    private javax.swing.JPanel jPanelActionRight;
    private javax.swing.JPanel jPanelCenter;
    private javax.swing.JPanel jPanelContent;
    private javax.swing.JPanel jPanelContentLeft;
    private javax.swing.JPanel jPanelContentRight;
    private javax.swing.JPanel jPanelDate;
    private javax.swing.JPanel jPanelInvoiceNumber;
    private javax.swing.JPanel jPanelLineList1;
    private javax.swing.JPanel jPanelLineList2;
    private javax.swing.JPanel jPanelLineList3;
    private javax.swing.JPanel jPanelShipName;
    private javax.swing.JPanel jPanelTripNumber;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextFieldDate;
    private javax.swing.JTextField jTextFieldInvoiceNumber;
    private javax.swing.JTextField jTextFieldShipName;
    private javax.swing.JTextField jTextFieldTripNumber;
    // End of variables declaration//GEN-END:variables

}

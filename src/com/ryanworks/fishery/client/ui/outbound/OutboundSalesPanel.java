package com.ryanworks.fishery.client.ui.outbound;

import com.ryanworks.fishery.client.delegate.*;
import com.ryanworks.fishery.client.print.OutboundPDFBoxPrinter;
import com.ryanworks.fishery.client.print.RootPDFBoxPrinter;
import com.ryanworks.fishery.client.ui.JFrameMain;
import com.ryanworks.fishery.client.ui.outbound.listener.*;
import com.ryanworks.fishery.client.ui.shared.ProgressModalDialog;
import com.ryanworks.fishery.shared.bean.*;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import com.ryanworks.fishery.shared.swing.listener.JTextFieldFocusListener;
import com.ryanworks.fishery.shared.swing.listener.JTextFieldSelectAllFocusListener;
import com.ryanworks.fishery.shared.util.MyLogger;
import com.ryanworks.fishery.shared.util.SwingUtil;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;

public class OutboundSalesPanel 
    extends javax.swing.JPanel 
    implements OutboundWizardDialogListener, OutboundSalesLineDialogListener, OutboundSearchDialogListener, OutboundBucketInputDialogListener, 
               OutboundItemToSupplierDialogListener, ActionListener, MouseListener, ItemListener, KeyListener
{    
    private final String saveToPath = RootPDFBoxPrinter.PDF_PRINTOUT_FOLDER;
    
    private final JFrame parent;
    private DefaultListModel listModelCategory;
    private DefaultListModel listModelItem;
    private boolean bucketChanged = false;
    private int displayListSize = 0;
    private boolean showingPrice = false;
        
    private final JLabel dataHeader1[] = {new JLabel("船户"), new JLabel("鱼类"), new JLabel("价格"), new JLabel("重量"), new JLabel("")};
    private final JLabel dataHeader2[] = {new JLabel("船户"), new JLabel("鱼类"), new JLabel("价格"), new JLabel("重量"), new JLabel("")};
    private final JLabel dataHeader3[] = {new JLabel("船户"), new JLabel("鱼类"), new JLabel("价格"), new JLabel("重量"), new JLabel("")};
    private final JTextField dataRow1[] = new JTextField[60];
    private final JTextField dataRow2[] = new JTextField[60];
    private final JTextField dataRow3[] = new JTextField[60];
    private final JTextField dataRow4[] = new JTextField[60];
    private final JButton buttonRow[] = new JButton[60];
    
    private final DecimalFormat currencyFormatter = new DecimalFormat("0.00");
    private final DecimalFormat decimalFormatter = new DecimalFormat("0.000");
    
    private SalesBean salesObj;
    
    public OutboundSalesPanel(JFrame parent) 
    {
        this.parent = parent;
        
        initComponents();
        myInit();
    }
    
    private void myInit() 
    {        
        for (int i=0 ; i<dataHeader1.length ; i++)
        {
            dataHeader1[i].setHorizontalAlignment( SwingConstants.CENTER );
            dataHeader2[i].setHorizontalAlignment( SwingConstants.CENTER );
            dataHeader3[i].setHorizontalAlignment( SwingConstants.CENTER );
            
            dataHeader1[i].setFont( new Font("Kaiti", Font.PLAIN, 21) );
            dataHeader2[i].setFont( new Font("Kaiti", Font.PLAIN, 21) );
            dataHeader3[i].setFont( new Font("Kaiti", Font.PLAIN, 21) );
            
            this.jPanelLineList1.add(dataHeader1[i]);
            this.jPanelLineList2.add(dataHeader2[i]);
            this.jPanelLineList3.add(dataHeader3[i]);
        }
        
        initDataRowPanel(jPanelLineList1, 0, 20);
        initDataRowPanel(jPanelLineList2, 20, 40);
        initDataRowPanel(jPanelLineList3, 40, 60);
        
        ((JLabel)this.jComboBoxBucketNo.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
                
        listModelCategory = new DefaultListModel();
        this.jListCategory.setModel(listModelCategory);
                        
        listModelItem = new DefaultListModel();
        this.jListItem.setModel(listModelItem);
        
        List<CategoryBean> categoryList = SystemDataDelegate.getInstance().getAllCategories();
        for (CategoryBean category : categoryList) 
        {
            listModelCategory.addElement( category );
        }
    }    
    
    private void initDataRowPanel(JPanel mainPanel, int startIndex, int endIndex)
    {
        for (int i = startIndex ; i<endIndex ; i++) 
        {
            dataRow1[i] = new JTextField();
            dataRow2[i] = new JTextField();
            dataRow3[i] = new JTextField();
            dataRow4[i] = new JTextField();
            buttonRow[i] = new JButton("X");
            
            dataRow1[i].setFont( new Font("Kaiti", Font.PLAIN, 18) );
            dataRow2[i].setFont( new Font("Kaiti", Font.PLAIN, 18) );
            dataRow3[i].setFont( new Font("Kaiti", Font.PLAIN, 18) );
            dataRow4[i].setFont( new Font("Kaiti", Font.PLAIN, 18) );
            buttonRow[i].setFont( new Font("Kaiti", Font.BOLD, 18) );
            
            dataRow3[i].addFocusListener( new JTextFieldSelectAllFocusListener() );
            dataRow4[i].addFocusListener( new JTextFieldFocusListener() );
            
            dataRow1[i].addMouseListener( this );  
            dataRow2[i].addMouseListener( this );
            
            dataRow3[i].addActionListener( this ); 
            dataRow4[i].addActionListener( this );      
            buttonRow[i].addActionListener( this );    
            
            dataRow3[i].addKeyListener( this );      
            dataRow4[i].addKeyListener( this );
            
            dataRow1[i].setEditable( false );
            dataRow2[i].setEditable( false );
            dataRow3[i].setEditable( false );
            dataRow4[i].setEditable( false );
            
            dataRow1[i].setHorizontalAlignment( SwingConstants.CENTER );
            dataRow2[i].setHorizontalAlignment( SwingConstants.CENTER );
            dataRow3[i].setHorizontalAlignment( SwingConstants.TRAILING );
            dataRow4[i].setHorizontalAlignment( SwingConstants.TRAILING );
            
            mainPanel.add(dataRow1[i]);
            mainPanel.add(dataRow2[i]);
            mainPanel.add(dataRow3[i]);
            mainPanel.add(dataRow4[i]); 
            mainPanel.add(buttonRow[i]);                 
        }     
    }
    
    private void fillForm()
    {                        
        // NEW SalesBean object (after search, or after wizard closed)
        if (bucketChanged == false)
        {
            this.jTextFieldCustomerName.setText( this.salesObj.getCustomerId()+ " / " + this.salesObj.getCustomerName());
            this.jTextFieldDate.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format(this.salesObj.getDateTime()) );
            this.jTextFieldInvoiceNumber.setText( this.salesObj.getInvoiceNo());
            this.jLabelSubTotalAmountValue.setText( currencyFormatter.format(this.salesObj.getSubTotalPrice()) );
            this.jLabelTotalAmountValue.setText( currencyFormatter.format(this.salesObj.getTotalPrice()) );
            this.jTextFieldTripCount.setText( String.valueOf(this.salesObj.getBucketList().size()) );
            
            if (this.salesObj.getBucketList().size() > 1) {
                this.jTextFieldTripCount.setForeground(Color.red);
                this.jTextFieldTripCount.setFont(new Font("Verdana", Font.BOLD, 48));
            } else {
                this.jTextFieldTripCount.setForeground(Color.black);
                this.jTextFieldTripCount.setFont(new Font("Verdana", Font.BOLD, 24));
            }

            this.jListCategory.setEnabled(true);            
            // Make component jListCategory gains the focus.
            if (this.listModelCategory.getSize() > 0) 
            {
                jListCategory.requestFocus();
                jListCategory.setSelectedIndex(0);
            }

            this.jComboBoxBucketNo.removeItemListener( this );
            this.jComboBoxBucketNo.removeAllItems();
            
            // To Check if no bucket exist, create one '0'
            if (this.salesObj.getBucketList().isEmpty())
            {
                SalesBucketBean bucketBean = new SalesBucketBean();
                bucketBean.setBucketNo("0");
                bucketBean.setDateTime(this.salesObj.getDateTime());
                bucketBean.setCustomerId(this.salesObj.getCustomerId());
                bucketBean.setSalesId(this.salesObj.getId());
                this.salesObj.addBucket(bucketBean);
            }
            
            for (SalesBucketBean bucketBean : this.salesObj.getBucketList())
            {
                this.jComboBoxBucketNo.addItem( bucketBean );                
            }
            this.jComboBoxBucketNo.addItemListener( this );
            
            // Re-fill the 3 lists
            refillSalesLineList();  
        }
        // Bucket # changed. 
        else
        {   
            // Re-fill the 3 lists
            refillSalesLineList();  
            
            this.bucketChanged = false;
        } 
    }

    private boolean fillBean()
    {       
        return true;
    }
    
    public void reset()
    {
        this.showingPrice = false;
        this.salesObj = new SalesBean();
        this.bucketChanged = false;
        this.displayListSize = 0;
        
        // Reset
        this.jTextFieldCustomerName.setText( "" );
        this.jTextFieldDate.setText( "" );
        this.jComboBoxBucketNo.removeAllItems();
        this.jTextFieldInvoiceNumber.setText( "" );
        this.jTextFieldTripCount.setText( "" );
        this.jButtonPrice.setText("显示价格");
        this.jLabelSubTotalAmountValue.setText("0.00");
        this.jLabelTotalAmountValue.setText("0.00");
        
        this.jTextFieldTripCount.setForeground(Color.black);
        this.jTextFieldTripCount.setFont(new Font("Verdana", Font.BOLD, 24));

        this.jListCategory.setEnabled(false);
        this.jListItem.setEnabled(false);        
        
        for (int i=0 ; i<60 ; i++)
        {
            this.dataRow1[i].setText( "" );
            this.dataRow2[i].setText( "" );
            this.dataRow3[i].setText( "" );
            this.dataRow4[i].setText( "" );
                        
            this.dataRow3[i].setBackground( Color.WHITE );
            this.dataRow3[i].setForeground( Color.BLACK );
        }
        
        this.jButtonSave.setEnabled( false );
        this.jButtonDelete.setEnabled(false);
        this.jButtonAddBucket.setEnabled(false);
        this.jButtonRemoveBucket.setEnabled(false);
        this.jButtonPrice.setEnabled(false);
        this.jButtonPrintChinese.setEnabled(false);
        this.jButtonPrintMalay.setEnabled(false);
        this.jButtonRecalculation.setEnabled(false);
    }
    
    private boolean isNullString(String obj)
    {
        if (obj==null)
            return true;
        if (obj.trim().length()==0)
            return true;
        
        return false;
    }
    
    /**
     * Convert unfiltered list into grouped list.
     * - items with same name will group together (SL)
     */
    private List<SalesLineBean> toDisplayableSalesLine(String bucketNo) {
        
        List<SalesLineBean> list1 = new ArrayList<SalesLineBean>();
        List<SalesLineBean> list2 = new ArrayList<SalesLineBean>();
        List<SalesLineBean> list3 = new ArrayList<SalesLineBean>();
        List<SalesLineBean> processedList = new ArrayList<SalesLineBean>();
        
        for (SalesLineBean lineObj : this.salesObj.getLineList()) {
            if (bucketNo.equals(lineObj.getBucketNo())) {
                list1.add(lineObj);
                list2.add(lineObj);
            }
        }
        
        for (SalesLineBean lineObj1: list1) {
            
            boolean processed = false;
            for (SalesLineBean processedObj: processedList) {
                if (lineObj1.getId().equals(processedObj.getId())) {
                    processed = true;
                }
            }
            
            if (processed)
                continue;
            
            if (isNullString(lineObj1.getSupplierId())) {
                list3.add(lineObj1);
                processedList.add(lineObj1);
            } else {
                SalesLineBean groupedSalesLine = new SalesLineBean();
                groupedSalesLine.setId( lineObj1.getId() );
                groupedSalesLine.setSupplierId( lineObj1.getSupplierId() );
                groupedSalesLine.setSupplierName( lineObj1.getSupplierName() );
                groupedSalesLine.setSaveable(true);
                groupedSalesLine.setBucketNo( bucketNo );
                groupedSalesLine.setCustomerId( lineObj1.getCustomerId() );
                groupedSalesLine.setItemId( lineObj1.getItemId() );
                groupedSalesLine.setItemName( lineObj1.getItemName() );
                groupedSalesLine.setItemNameBm( lineObj1.getItemNameBm() );
                groupedSalesLine.setItemNewName( lineObj1.getItemNewName() );                    
                groupedSalesLine.setSalesId( lineObj1.getSalesId() );
                groupedSalesLine.setUnitPrice( lineObj1.getUnitPrice() );                    
                groupedSalesLine.setAddWeight( lineObj1.getAddWeight() );
                groupedSalesLine.setWeight( lineObj1.getWeight() );   
                groupedSalesLine.setDateTime( lineObj1.getDateTime() );
                groupedSalesLine.groupIdList.add(lineObj1.getId());
                
                processedList.add(lineObj1);
                
                // Find other line object with same supplier ID & item name
                for (SalesLineBean lineObj2 : list2) {
                    
                    if (!isNullString(lineObj2.getSupplierId())) {
                        boolean processed2 = false;
                        for (SalesLineBean processedObj2: processedList) {
                            if (lineObj2.getId().equals(processedObj2.getId())) {
                                processed2 = true;
                            }
                        }

                        if (processed2)
                            continue;
                        
                        if (lineObj1.getItemNewName().equals(lineObj2.getItemNewName()))
                        {
                            groupedSalesLine.setSupplierId( "SL" );
                            groupedSalesLine.setSupplierName( "SL" );
                            groupedSalesLine.setSaveable(false);
                            groupedSalesLine.setAddWeight( groupedSalesLine.getAddWeight() + lineObj2.getAddWeight() );
                            groupedSalesLine.setWeight( groupedSalesLine.getWeight() + lineObj2.getWeight() );  
                            groupedSalesLine.groupIdList.add(lineObj2.getId());
                            processedList.add(lineObj2);
                        }
                    }
                }
                
                list3.add(groupedSalesLine);
            }
        }
        
        return list3;
    }
    
    private void refillSalesLineList()
    {
        // REMOVE ALL DATA IN LIST
        for (int i=0 ; i<60 ; i++)
        {
            this.dataRow1[i].setText( "" );
            this.dataRow2[i].setText( "" );
            this.dataRow3[i].setText( "" );
            this.dataRow4[i].setText( "" );
            this.dataRow4[i].setEditable(false);
                        
            this.dataRow3[i].setForeground(Color.BLACK);
            this.dataRow3[i].setBackground(Color.WHITE);
            
            this.dataRow4[i].setForeground(Color.BLACK);
            this.dataRow4[i].setBackground(Color.WHITE);
        }
        
        // Reset        
        this.showingPrice = false;
        this.jButtonPrice.setText("显示价格");
        
        // No bucket, means No sales object
        if (this.jComboBoxBucketNo.getSelectedIndex() == -1)
            return;
        
        this.salesObj.setLineList( 
                this.toDisplayableSalesLine(this.jComboBoxBucketNo.getSelectedItem().toString()) );
        
        int lineIndex = 0;
        double totalAmount = 0.0d;
        this.displayListSize = 0;
        for (SalesLineBean lineBean : this.salesObj.getLineList())
        {
            this.dataRow1[lineIndex].setText( lineBean.getSupplierName());
            this.dataRow2[lineIndex].setText( lineBean.getItemNewName() );
            this.dataRow3[lineIndex].setText( "" );
            this.dataRow4[lineIndex].setText( decimalFormatter.format(lineBean.getWeight()+lineBean.getAddWeight()) );
            
            // If got supplier ship, then cannot change the weight anymore
            if (lineBean.getSupplierId()==null || lineBean.getSupplierId().length()==0)
                this.dataRow4[lineIndex].setEditable(true);
            else
                this.dataRow4[lineIndex].setEditable(false);
            this.displayListSize++;
            
            lineIndex++;
            totalAmount+=(lineBean.getUnitPrice()*(lineBean.getWeight()+lineBean.getAddWeight()));
        }
        
        updateTotalPrice();      
    }
        
    private void updateTotalPrice()
    {
        // Update panel
        double subTotalAmount = 0.0d;
        double totalAmount = 0.0d;
        String bucketNo = ((SalesBucketBean)this.jComboBoxBucketNo.getSelectedItem()).getBucketNo();
        
        List<SalesLineBean> allSalesLines = SalesDelegate.getInstance().getSalesLineBySalesId(this.salesObj.getId());
        for (SalesLineBean lineObj : allSalesLines) {
            double lineAmount = lineObj.getUnitPrice() * (lineObj.getWeight() + lineObj.getAddWeight());
            // Total Amount (exclude current bucket)
            if (!bucketNo.equals(lineObj.getBucketNo()))
                totalAmount += lineAmount;            
        }
        
        for (SalesLineBean lineObj : this.salesObj.getLineList()) {        
            double lineAmount = lineObj.getUnitPrice() * (lineObj.getWeight() + lineObj.getAddWeight());            
            // Sub Total Amount
            if (bucketNo.equals(lineObj.getBucketNo()))
                subTotalAmount += lineAmount;
        }
        
        totalAmount += subTotalAmount;
        
        System.err.println("subTotalAmount="+subTotalAmount);
        System.err.println("totalAmount="+totalAmount);
        
        this.salesObj.setSubTotalPrice(subTotalAmount);
        this.salesObj.setTotalPrice(totalAmount);
        // Update UI
        this.jLabelSubTotalAmountValue.setText( currencyFormatter.format(salesObj.getSubTotalPrice()) ); 
        this.jLabelTotalAmountValue.setText( currencyFormatter.format(salesObj.getTotalPrice()) ); 
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        // do nothing for empty list
        if (this.salesObj==null || this.salesObj.getLineList().isEmpty())
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
                    int flag = JOptionPane.showConfirmDialog( 
                                    this, 
                                    "您确定要删除此交易记录 - [" + dataRow2[k].getText() + "]？", 
                                    "确认删除", 
                                    JOptionPane.YES_NO_OPTION );
                    if (JOptionPane.YES_OPTION == flag)
                    {
                        SalesLineBean lineObj = salesObj.getLineList().get(k);
                        
                        // Delete Salesline
                        this.deleteSalesLine( lineObj );                        
                    }
                    
                    break;
                }

                k++;
            }
        }
        else
        if (ae.getSource() instanceof JTextField)
        {
            JTextField textField = (JTextField) ae.getSource();

            int k=0;
            // Response to Column 3 'Price'
            for (JTextField tf : dataRow3)
            {
                if (textField == tf)
                {
                    if (k == this.salesObj.getLineList().size()-1)
                    {
                        this.dataRow3[0].requestFocus();
                        this.dataRow3[0].selectAll();
                    }
                    else
                    {
                        this.dataRow3[k+1].requestFocus();
                        this.dataRow3[k+1].selectAll();
                    }
                    break;
                }

                k++;
            }

            int i=0;
            // Response to Column 4 'weight'
            for (JTextField tf : dataRow4)
            {
                if (textField == tf)
                {
                    if (i == this.salesObj.getLineList().size()-1)
                    {
                        this.dataRow4[0].requestFocus();
                        this.dataRow4[0].selectAll();
                    }
                    else
                    {
                        this.dataRow4[i+1].requestFocus();
                        this.dataRow4[i+1].selectAll();
                    }
                    break;
                }

                i++;
            }
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
        JTextField textField = (JTextField) ke.getSource();
        
        if (showingPrice == true)
        {
            int k=0;
            // Response to Column 3 'Price'
            for (JTextField tf : dataRow3)
            {
                if (textField == tf)
                {
                    try 
                    {           
                        double unitPrice = 0.0d;
                        
                        // Set to 0.0d if empty input ("") found.
                        if ("".equalsIgnoreCase(this.dataRow3[k].getText().trim()))
                            unitPrice = 0.0d;
                        else
                            unitPrice = Double.parseDouble( this.dataRow3[k].getText().trim() );
                        
                        this.salesObj.getLineList().get(k).setUnitPrice( unitPrice );
                        
                        // Update panel
                        this.updateTotalPrice();

                        //SalesDelegate.getInstance().saveOrUpdateSalesLine(this.salesObj.getLineList().get(k));
                    }
                    catch (NumberFormatException e)
                    {
                        MyLogger.logError(getClass(), e.getMessage());
                        JOptionPane.showMessageDialog(
                                this, 
                                "您输入的价格有错误。", 
                                "输入错误 (Key Released 1-" + k + ")",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    
                    break;
                }
                k++;
            }
        }
        
        int i=0;
        // Response to Column 4 'weight'
        for (JTextField tf : dataRow4)
        {
            if (textField == tf)
            {
                if (i == this.salesObj.getLineList().size()-1)
                {
                    try 
                    {
                        this.salesObj.getLineList().get(i).setWeight( Double.parseDouble( this.dataRow4[i].getText().trim() ) );
                                                
                        // Update panel
                        this.updateTotalPrice();
                    }
                    catch (NumberFormatException e)
                    {
                        MyLogger.logError(getClass(), e.getMessage());
                        JOptionPane.showMessageDialog(
                                this, 
                                "您输入的重量有错误。", 
                                "I输入错误",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    try 
                    {
                        this.salesObj.getLineList().get(i).setWeight( Double.parseDouble( this.dataRow4[i].getText().trim() ) );
                        
                        // Update panel
                        this.updateTotalPrice();
                    }
                    catch (NumberFormatException e)
                    {
                        MyLogger.logError(getClass(), e.getMessage());
                        JOptionPane.showMessageDialog(
                                this, 
                                "您输入的重量有错误。", 
                                "输入错误",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            }

            i++;
        }
    }

    private double getUnitPrice(int row)
    {
        return this.salesObj.getLineList().get(row).getUnitPrice();
    }
    
    @Override
    public void mouseClicked(MouseEvent me) 
    {
        try {
            // Do nothing on empty list
            if (this.salesObj == null || this.salesObj.getLineList().isEmpty())
                return;

            JTextField textField = (JTextField) me.getSource();

            // Iterate through the list of JTextField component and identify which line of object.
            int i=0;
            for (JTextField tf : dataRow1)
            {
                if (textField == tf )
                {
                    String supplierId = this.dataRow1[i].getText().trim();  
                    double unitPrice = getUnitPrice(i);               
                    
                    List<SalesLineBean> salesLineList = 
                            SalesDelegate.getInstance().getSalesLineBySalesId(this.salesObj.getId());
                    
                    String itemName = this.dataRow2[i].getText().trim();
                    String bucketNo = ((SalesBucketBean) this.jComboBoxBucketNo.getSelectedItem()).getBucketNo();
                    
                    List<SalesLineBean> filteredList = new ArrayList<SalesLineBean>(); 
                    for (SalesLineBean lineObj : salesLineList) {
                        if (lineObj.getSupplierId() != null && !"".equals(lineObj.getSupplierId().trim())) {
                            if (bucketNo.equals(lineObj.getBucketNo())) {
                                if (itemName.equals(lineObj.getItemNewName())) {
                                    filteredList.add(lineObj);
                                }
                            }
                        }
                    }

                    this.saveTransaction();
            
                    OutboundItemToSupplierDialog dialog = 
                            new OutboundItemToSupplierDialog(this.parent, true, this, this.salesObj, filteredList, supplierId);        
                    SwingUtil.centerDialogOnScreen(dialog);

                    break;
                }

                i++;
            }

            int j=0;
            for (JTextField tf : dataRow2)
            {
                if (textField == tf)
                {
                    this.saveTransaction();
            
                    OutboundSalesLineDialog dialog = 
                            new OutboundSalesLineDialog(this.parent, true, this, this.salesObj.getLineList().get(j));        
                    SwingUtil.centerDialogOnScreen(dialog);

                    break;
                }

                j++;
            }
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), e.getMessage());
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
    public void itemStateChanged(ItemEvent ie) 
    {
        if (ie.getSource() == this.jComboBoxBucketNo)
        {
            this.jButtonSave.doClick();
            
            ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "读取资料中...");            
            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception 
                {   
                    // Set state
                    bucketChanged = true;
                    String bucketNo = jComboBoxBucketNo.getSelectedItem().toString();
                    // Get line list
                    List<SalesLineBean> lineList = SalesDelegate.getInstance().getSalesLineBySalesIdAndBucketNo(salesObj.getId(), bucketNo);
                    salesObj.setLineList( lineList );
                    // Update UI
                    fillForm();
                    return null;
                }

                @Override
                protected void done() {
                    // Disable Buttons
                    jButtonPrintChinese.setEnabled(true);
                    jButtonPrintMalay.setEnabled(true);
                    
                    dialog.dispose();//close the modal dialog
                }
            };

            swingWorker.execute(); // this will start the processing on a separate thread
            SwingUtil.centerDialogOnScreen( dialog );
        }
    }
    
    @Override
    public void wizardDialogClosed(SalesBean salesBean, CustomerBean customer, long dateInMillis, int customerTripNo) 
    {
        this.showingPrice = false;
        this.jButtonPrice.setText("显示价格");
        this.bucketChanged = false;
        this.saveTransaction();
        
        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "设置数据中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {    
                // search if this customer has made order for the date.
                SalesBean existingSalesObj = SalesDelegate.getInstance().getSalesByCustomerAndDate(customer.getId(), dateInMillis);

                if (existingSalesObj==null)
                {        
                    if (salesBean == null)
                        salesObj = new SalesBean();
                    else
                        salesObj = salesBean;

                    salesObj.setCustomerId(customer.getId());
                    salesObj.setCustomerName(customer.getName());
                    salesObj.setCustomerTrip(customerTripNo);
                    salesObj.setDateTime(dateInMillis);        

                    fillForm();

                    jButtonSave.setEnabled( true );
                    jButtonDelete.setEnabled( true );
                    jButtonAddBucket.setEnabled( true );
                    jButtonRemoveBucket.setEnabled( true );
                    jButtonPrice.setEnabled( true );
                    jButtonPrice.setText("显示价格");
                    jButtonPrintChinese.setEnabled( true );
                    jButtonPrintMalay.setEnabled( true );
                    jButtonRecalculation.setEnabled( true );
                    jButtonSave.setEnabled(true);
                }
                else
                {
                    setSalesBean(existingSalesObj);
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
    } // .end of wizardDialogClosed
    
    @Override
    public void salesLineDialogClosed( SalesLineBean lineBean )
    {
        this.addLineIntoSales(lineBean);
    } // .end of salesLineDialogClosed
    
    @Override
    public void searchDialogClosed(SalesBean salesBean)
    {
        this.showingPrice = false;
        this.jButtonPrice.setText("显示价格");
        this.bucketChanged = false;
        this.saveTransaction();
        
        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "设置数据中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {    
                setSalesBean(salesBean);                
                return null;
            }

            @Override
            protected void done() {
                dialog.dispose();//close the modal dialog
            }
        };

        swingWorker.execute(); // this will start the processing on a separate thread
        SwingUtil.centerDialogOnScreen( dialog );
    } // .end of searchDialogClosed
    
    private void setSalesBean(SalesBean salesBean) {
        salesObj = salesBean;

        fillForm();

        jButtonSave.setEnabled( true );
        jButtonDelete.setEnabled( true );
        jButtonAddBucket.setEnabled( true );
        jButtonRemoveBucket.setEnabled( true );
        jButtonPrice.setText( "显示价格" );
        jButtonPrice.setEnabled( true );
        jButtonPrintChinese.setEnabled( true );
        jButtonPrintMalay.setEnabled( true );
        jButtonRecalculation.setEnabled( true );
        jButtonSave.setEnabled( true );
    }
    
    /**
     *
     * @param bucketNo
     */
    @Override
    public void bucketInputDialogClosed( String bucketNo )
    {        
        this.jButtonSave.doClick();
        
        SalesBucketBean bucketBean = new SalesBucketBean();
        bucketBean.setBucketNo(bucketNo);
        bucketBean.setDateTime(this.salesObj.getDateTime());
        bucketBean.setCustomerId(this.salesObj.getCustomerId());
        bucketBean.setSalesId(this.salesObj.getId());
        
        this.salesObj.addBucket( bucketBean );
        
        this.jComboBoxBucketNo.addItem( bucketBean );
        this.jComboBoxBucketNo.setSelectedItem( bucketBean );
        this.jTextFieldTripCount.setText( String.valueOf(this.salesObj.getBucketList().size()) );
        
        if (this.salesObj.getBucketList().size() > 1) {
            this.jTextFieldTripCount.setForeground(Color.red);
            this.jTextFieldTripCount.setFont(new Font("Verdana", Font.BOLD, 48));
        } else {
            this.jTextFieldTripCount.setForeground(Color.black);
            this.jTextFieldTripCount.setFont(new Font("Verdana", Font.BOLD, 24));
        }
    }
    
    @Override
    public void itemToSupplierDialogClosed()
    {
        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "更新数据中...");
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
                @Override
                protected Void doInBackground() throws Exception 
                {
                    // Get updated sales object from DB
                    salesObj = SalesDelegate.getInstance().getSalesById(salesObj.getId());
                    // Update UI
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
    }
    
    protected void addLineIntoSales(SalesLineBean lineBean)
    {
        if (lineBean != null)
        {
            // Check if current line list full (max 60)
            if (this.salesObj.getLineList().size() >= 60)
            {                
                JOptionPane.showMessageDialog(
                    this.getParent(), 
                    "每张进货单最多能接受60个交易。", 
                    "交易超额", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
                        
            // Add new line object into list
            this.salesObj.addLine( lineBean );
            
            // Refresh the list of data
            this.refillSalesLineList();
        }
    }
    
    public void deleteSalesLine(SalesLineBean lineObj)
    {
        if (lineObj.groupIdList.size() > 0) { 
            
            // Delete from DB
            for (String salesLineId : lineObj.groupIdList) {
                SalesLineBean salesLineObj = SalesDelegate.getInstance().getSalesLineById(salesLineId);
                SalesDelegate.getInstance().deleteSalesLine(salesLineObj);
            }
            SalesDelegate.getInstance().deleteSalesLine(lineObj);
            // Remove from cached object
            this.salesObj.deleteLineBean(lineObj);
       
            for (String salesLineId : lineObj.groupIdList) {
                // Update InTransactionLine
                InTransactionLineBean inTranxLineObj = 
                        InTransactionDelegate.getInstance().getInTransactionLineBySalesLine(salesLineId);
                if (inTranxLineObj != null) {
                    inTranxLineObj.setSalesId("");
                    inTranxLineObj.setSalesLineId("");
                    inTranxLineObj.setCustomerId("");
                    inTranxLineObj.setCustomerName("");
                    InTransactionDelegate.getInstance().saveTransactionLine(inTranxLineObj);
                }
            }
        } else {
            // Delete from DB
            SalesDelegate.getInstance().deleteSalesLine(lineObj);
            // Remove from cached object
            this.salesObj.deleteLineBean(lineObj);
        }
        
        // Update UI
        refillSalesLineList();
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
        jTextFieldCustomerName = new javax.swing.JTextField();
        jPanelTripNumber = new javax.swing.JPanel();
        jLabelTripNumber = new javax.swing.JLabel();
        jComboBoxBucketNo = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        jButtonAddBucket = new javax.swing.JButton();
        jButtonRemoveBucket = new javax.swing.JButton();
        jPanelTripCount = new javax.swing.JPanel();
        jLabelTripCount = new javax.swing.JLabel();
        jTextFieldTripCount = new javax.swing.JTextField();
        jPanelDate = new javax.swing.JPanel();
        jLabelDate = new javax.swing.JLabel();
        jTextFieldDate = new javax.swing.JTextField();
        jPanelInvoiceNumber = new javax.swing.JPanel();
        jLabelInvoiceNumber = new javax.swing.JLabel();
        jTextFieldInvoiceNumber = new javax.swing.JTextField();
        jPanelActionRight = new javax.swing.JPanel();
        jButtonNewOrder = new javax.swing.JButton();
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
        jButtonPrintChinese = new javax.swing.JButton();
        jButtonPrintMalay = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jButtonRecalculation = new javax.swing.JButton();
        jLabelTotalAmountTitle = new javax.swing.JLabel();
        jLabelSubTotalAmountValue = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabelTotalAmountTitle1 = new javax.swing.JLabel();
        jLabelTotalAmountValue = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanelLineList1 = new javax.swing.JPanel();
        jPanelLineList2 = new javax.swing.JPanel();
        jPanelLineList3 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jLabelTitle.setBackground(new java.awt.Color(51, 51, 51));
        jLabelTitle.setFont(new java.awt.Font("KaiTi", 1, 48)); // NOI18N
        jLabelTitle.setForeground(new java.awt.Color(0, 204, 255));
        jLabelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitle.setText("海春鱼行出货系统");
        jLabelTitle.setToolTipText("");
        jLabelTitle.setOpaque(true);
        jLabelTitle.setPreferredSize(new java.awt.Dimension(441, 60));
        add(jLabelTitle, java.awt.BorderLayout.PAGE_START);

        jPanelCenter.setLayout(new java.awt.BorderLayout());

        jPanelAction.setPreferredSize(new java.awt.Dimension(20, 100));
        jPanelAction.setLayout(new java.awt.BorderLayout());

        jPanelActionLeft.setBackground(new java.awt.Color(0, 153, 255));
        jPanelActionLeft.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanelShipName.setPreferredSize(new java.awt.Dimension(300, 90));
        jPanelShipName.setLayout(new java.awt.BorderLayout());

        jLabelShipName.setBackground(new java.awt.Color(51, 51, 51));
        jLabelShipName.setFont(new java.awt.Font("KaiTi", 1, 14)); // NOI18N
        jLabelShipName.setForeground(new java.awt.Color(255, 255, 255));
        jLabelShipName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelShipName.setText("客户号码 / 名字");
        jLabelShipName.setOpaque(true);
        jPanelShipName.add(jLabelShipName, java.awt.BorderLayout.PAGE_START);

        jTextFieldCustomerName.setEditable(false);
        jTextFieldCustomerName.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jTextFieldCustomerName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanelShipName.add(jTextFieldCustomerName, java.awt.BorderLayout.CENTER);

        jPanelActionLeft.add(jPanelShipName);

        jPanelTripNumber.setPreferredSize(new java.awt.Dimension(280, 90));
        jPanelTripNumber.setLayout(new java.awt.BorderLayout());

        jLabelTripNumber.setBackground(new java.awt.Color(51, 51, 51));
        jLabelTripNumber.setFont(new java.awt.Font("KaiTi", 1, 14)); // NOI18N
        jLabelTripNumber.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTripNumber.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTripNumber.setText("桶号码");
        jLabelTripNumber.setOpaque(true);
        jPanelTripNumber.add(jLabelTripNumber, java.awt.BorderLayout.PAGE_START);

        jComboBoxBucketNo.setFont(new java.awt.Font("KaiTi", 0, 48)); // NOI18N
        jComboBoxBucketNo.setPreferredSize(new java.awt.Dimension(120, 62));
        jComboBoxBucketNo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxBucketNoItemStateChanged(evt);
            }
        });
        jComboBoxBucketNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxBucketNoActionPerformed(evt);
            }
        });
        jPanelTripNumber.add(jComboBoxBucketNo, java.awt.BorderLayout.CENTER);

        jPanel9.setLayout(new java.awt.GridLayout(1, 2));

        jButtonAddBucket.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButtonAddBucket.setText("+");
        jButtonAddBucket.setEnabled(false);
        jButtonAddBucket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddBucketActionPerformed(evt);
            }
        });
        jPanel9.add(jButtonAddBucket);

        jButtonRemoveBucket.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButtonRemoveBucket.setText("-");
        jButtonRemoveBucket.setEnabled(false);
        jButtonRemoveBucket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveBucketActionPerformed(evt);
            }
        });
        jPanel9.add(jButtonRemoveBucket);

        jPanelTripNumber.add(jPanel9, java.awt.BorderLayout.EAST);

        jPanelActionLeft.add(jPanelTripNumber);

        jPanelTripCount.setPreferredSize(new java.awt.Dimension(90, 90));
        jPanelTripCount.setLayout(new java.awt.BorderLayout());

        jLabelTripCount.setBackground(new java.awt.Color(51, 51, 51));
        jLabelTripCount.setFont(new java.awt.Font("KaiTi", 1, 14)); // NOI18N
        jLabelTripCount.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTripCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTripCount.setText("桶数量 ");
        jLabelTripCount.setToolTipText("");
        jLabelTripCount.setOpaque(true);
        jPanelTripCount.add(jLabelTripCount, java.awt.BorderLayout.PAGE_START);

        jTextFieldTripCount.setEditable(false);
        jTextFieldTripCount.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jTextFieldTripCount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldTripCount.setText("2");
        jPanelTripCount.add(jTextFieldTripCount, java.awt.BorderLayout.CENTER);

        jPanelActionLeft.add(jPanelTripCount);

        jPanelDate.setPreferredSize(new java.awt.Dimension(180, 90));
        jPanelDate.setLayout(new java.awt.BorderLayout());

        jLabelDate.setBackground(new java.awt.Color(51, 51, 51));
        jLabelDate.setFont(new java.awt.Font("KaiTi", 1, 14)); // NOI18N
        jLabelDate.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDate.setText("日期");
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
        jPanelInvoiceNumber.add(jTextFieldInvoiceNumber, java.awt.BorderLayout.CENTER);

        jPanelActionLeft.add(jPanelInvoiceNumber);

        jPanelAction.add(jPanelActionLeft, java.awt.BorderLayout.CENTER);

        jPanelActionRight.setBackground(new java.awt.Color(0, 153, 255));
        jPanelActionRight.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButtonNewOrder.setBackground(new java.awt.Color(51, 204, 0));
        jButtonNewOrder.setFont(new java.awt.Font("KaiTi", 1, 28)); // NOI18N
        jButtonNewOrder.setText("新单");
        jButtonNewOrder.setPreferredSize(new java.awt.Dimension(100, 90));
        jButtonNewOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewOrderActionPerformed(evt);
            }
        });
        jPanelActionRight.add(jButtonNewOrder);

        jButtonSearch.setBackground(new java.awt.Color(204, 255, 255));
        jButtonSearch.setFont(new java.awt.Font("KaiTi", 1, 28)); // NOI18N
        jButtonSearch.setText("搜索旧单");
        jButtonSearch.setPreferredSize(new java.awt.Dimension(160, 90));
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });
        jPanelActionRight.add(jButtonSearch);

        jButtonSave.setBackground(new java.awt.Color(204, 255, 255));
        jButtonSave.setFont(new java.awt.Font("KaiTi", 1, 28)); // NOI18N
        jButtonSave.setText("储存");
        jButtonSave.setEnabled(false);
        jButtonSave.setPreferredSize(new java.awt.Dimension(100, 90));
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        jPanelActionRight.add(jButtonSave);

        jButtonDelete.setBackground(new java.awt.Color(204, 255, 255));
        jButtonDelete.setFont(new java.awt.Font("KaiTi", 1, 28)); // NOI18N
        jButtonDelete.setEnabled(false);
        jButtonDelete.setLabel("删除");
        jButtonDelete.setPreferredSize(new java.awt.Dimension(100, 90));
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        jPanelActionRight.add(jButtonDelete);

        jButtonLogout.setBackground(new java.awt.Color(204, 255, 255));
        jButtonLogout.setFont(new java.awt.Font("KaiTi", 1, 28)); // NOI18N
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

        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

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

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

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

        jButtonPrintChinese.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonPrintChinese.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/print.png"))); // NOI18N
        jButtonPrintChinese.setText("打印中文");
        jButtonPrintChinese.setEnabled(false);
        jButtonPrintChinese.setPreferredSize(new java.awt.Dimension(150, 50));
        jButtonPrintChinese.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintChineseActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonPrintChinese);

        jButtonPrintMalay.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonPrintMalay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/print.png"))); // NOI18N
        jButtonPrintMalay.setText("打印马来文");
        jButtonPrintMalay.setEnabled(false);
        jButtonPrintMalay.setPreferredSize(new java.awt.Dimension(160, 50));
        jButtonPrintMalay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrintMalayActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonPrintMalay);

        jPanel4.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jButtonRecalculation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/7087_refresh.png"))); // NOI18N
        jButtonRecalculation.setEnabled(false);
        jButtonRecalculation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRecalculationActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonRecalculation);

        jLabelTotalAmountTitle.setFont(new java.awt.Font("KaiTi", 1, 36)); // NOI18N
        jLabelTotalAmountTitle.setForeground(new java.awt.Color(0, 102, 255));
        jLabelTotalAmountTitle.setText("桶额:");
        jPanel10.add(jLabelTotalAmountTitle);

        jLabelSubTotalAmountValue.setFont(new java.awt.Font("KaiTi", 1, 36)); // NOI18N
        jLabelSubTotalAmountValue.setForeground(new java.awt.Color(0, 102, 255));
        jLabelSubTotalAmountValue.setText("RM0.00");
        jPanel10.add(jLabelSubTotalAmountValue);

        jPanel8.add(jPanel10, java.awt.BorderLayout.NORTH);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jLabelTotalAmountTitle1.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jLabelTotalAmountTitle1.setForeground(new java.awt.Color(255, 51, 0));
        jLabelTotalAmountTitle1.setText("总额:");
        jPanel11.add(jLabelTotalAmountTitle1);

        jLabelTotalAmountValue.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jLabelTotalAmountValue.setForeground(new java.awt.Color(255, 51, 0));
        jLabelTotalAmountValue.setText("RM0.00");
        jPanel11.add(jLabelTotalAmountValue);

        jPanel8.add(jPanel11, java.awt.BorderLayout.SOUTH);

        jPanel4.add(jPanel8, java.awt.BorderLayout.EAST);

        jPanelContentRight.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.GridLayout(1, 3));

        jPanelLineList1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "列表（一）", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanelLineList1.setLayout(new java.awt.GridLayout(21, 6, 2, 2));
        jPanel3.add(jPanelLineList1);

        jPanelLineList2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "列表（二）", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanelLineList2.setLayout(new java.awt.GridLayout(21, 6, 2, 2));
        jPanel3.add(jPanelLineList2);

        jPanelLineList3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "列表（三）", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanelLineList3.setLayout(new java.awt.GridLayout(21, 6, 2, 2));
        jPanel3.add(jPanelLineList3);

        jPanelContentRight.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanelContent.add(jPanelContentRight, java.awt.BorderLayout.CENTER);

        jPanelCenter.add(jPanelContent, java.awt.BorderLayout.CENTER);

        add(jPanelCenter, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNewOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewOrderActionPerformed
        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "开启中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            OutboundWizardDialog wizardDialog;
            @Override
            protected Void doInBackground() throws Exception 
            {    
                wizardDialog = new OutboundWizardDialog(parent, true, OutboundSalesPanel.this); 
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
    }//GEN-LAST:event_jButtonNewOrderActionPerformed

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed
        saveTransaction();
        
        // Clear item list
        this.listModelItem.clear();
        // De-select category
        this.jListCategory.removeSelectionInterval(0, this.listModelCategory.size()-1);
        
        ((JFrameMain)this.parent).switchToMainPanel();        
    }//GEN-LAST:event_jButtonLogoutActionPerformed

    private void jListCategoryValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListCategoryValueChanged
        
        if (this.jListCategory.getSelectedIndex()==-1)
            return;
        
        Object categoryObj = 
                this.jListCategory.getModel().getElementAt( this.jListCategory.getSelectedIndex() );
        
        if (categoryObj == null)
            return;
        
        // Clear item list
        this.listModelItem.clear();
        
        CategoryBean selectedCategory = (CategoryBean) categoryObj;        
        List<ItemBean> itemList = 
                SystemDataDelegate.getInstance().getItemsByCategoryId(selectedCategory.getId());
        
        for (ItemBean item : itemList) {
            this.listModelItem.addElement(item);
        }
        
        this.jListItem.setEnabled(true);
    }//GEN-LAST:event_jListCategoryValueChanged

    private void jListCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jListCategoryKeyPressed
        
        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "设置数据中...");            
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
                if (salesObj == null)
                    return null;
                //if (dataChanged == false)
                //    return null;
                
                // Check if this is empty transaction.
                // * Condition: DO NOT SAVE EMPTY TRANSACTION
                if (salesObj.getLineList().isEmpty())
                    return null;                

                jButtonRecalculation.doClick();
                
                if (fillBean()==true) {
                    SalesDelegate.getInstance().saveOrUpdateSales(salesObj);
                    MyLogger.logInfo(getClass(), "Sales object saved.");
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
    } // .end of saveTransaction
    
    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        saveTransaction();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        OutboundSearchDialog dialog = new OutboundSearchDialog(this.parent, true, this);        
        SwingUtil.centerDialogOnScreen(dialog);
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jListItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListItemMouseClicked
        int selectedItemIndex = this.jListItem.getSelectedIndex();
        if (selectedItemIndex == -1) {
            JOptionPane.showMessageDialog(this, "请选择一项鱼类。", "错误选项", JOptionPane.WARNING_MESSAGE);
            return;            
        }
        
        Object itemObj = this.jListItem.getModel().getElementAt( selectedItemIndex );
        if (itemObj == null) {
            JOptionPane.showMessageDialog(this, "请选择一项鱼类。", "错误选项", JOptionPane.WARNING_MESSAGE);
            return;            
        }
        
        ItemBean itemBean = (ItemBean) itemObj;
        SalesLineBean lineBean = new SalesLineBean();
        lineBean.setItemId( itemBean.getId() );
        lineBean.setItemName( itemBean.getName() );
        lineBean.setItemNewName( itemBean.getName() );
        lineBean.setItemNameBm( itemBean.getNameBm() );
        lineBean.setDateTime( this.salesObj.getDateTime() );
        lineBean.setSalesId(this.salesObj.getId() );
        lineBean.setBucketNo(this.jComboBoxBucketNo.getSelectedItem()==null ? "0" : this.jComboBoxBucketNo.getSelectedItem().toString());
        lineBean.setUnitPrice( 0.0d );
        lineBean.setCustomerId(this.salesObj.getCustomerId());
        
        OutboundSalesLineDialog dialog = new OutboundSalesLineDialog(this.parent, true, this, lineBean);        
        SwingUtil.centerDialogOnScreen(dialog);
    }//GEN-LAST:event_jListItemMouseClicked

    private void jButtonPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPriceActionPerformed

        if (salesObj.getLineList().isEmpty()) {
            JOptionPane.showMessageDialog(this, "系统检查到无销售项目。", "无销售记录", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (this.jButtonPrice.getText().equalsIgnoreCase( "显示价格" ))
        {
            int i = 0;
            for (SalesLineBean lineBean : this.salesObj.getLineList())
            {
                this.dataRow3[i].setEditable(true);
                this.dataRow3[i].setBackground( Color.BLACK );
                this.dataRow3[i].setForeground( Color.GREEN );
                this.dataRow3[i].setHorizontalAlignment(JTextField.CENTER);
                //this.dataRow3[i].addFocusListener(new JTextFieldFocusListener(Color.BLACK, Color.GREEN));
                if (this.salesObj.getLineList().get(i).getUnitPrice()==0.0d)
                    this.dataRow3[i].setText("");
                else
                    this.dataRow3[i].setText( currencyFormatter.format(this.salesObj.getLineList().get(i).getUnitPrice()) );                                
                
                i++;
            }

            this.jButtonPrice.setText( "隐藏价格" );
            this.showingPrice = true;
        }
        else
        {
            int i = 0;
            for (SalesLineBean lineBean : this.salesObj.getLineList())
            {
                this.dataRow3[i].setEditable(false);
                this.dataRow3[i].setBackground( Color.WHITE );
                this.dataRow3[i].setForeground( Color.BLACK );
                this.dataRow3[i].setHorizontalAlignment(JTextField.RIGHT);
                //this.dataRow3[i].addFocusListener(new JTextFieldFocusListener());
                this.dataRow3[i].setText( "" );
                i++;
            }

            this.jButtonPrice.setText( "显示价格" );
            this.showingPrice = false;
        }
    }//GEN-LAST:event_jButtonPriceActionPerformed

    private void jButtonAddBucketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddBucketActionPerformed
        OutboundBucketInputDialog dialog = new OutboundBucketInputDialog(this.parent, true, this, this.salesObj);        
        SwingUtil.centerDialogOnScreen(dialog);
    }//GEN-LAST:event_jButtonAddBucketActionPerformed

    private void jButtonPrintChineseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintChineseActionPerformed
        try 
        {
            if (salesObj.getLineList().isEmpty()) {
                JOptionPane.showMessageDialog(this, "系统检查到无销售项目。", "无销售记录", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            this.jButtonSave.doClick();
            
            int flag = JOptionPane.showConfirmDialog(this, "一并打印价格？", "确认打印", JOptionPane.YES_NO_CANCEL_OPTION);            
            if (flag == JOptionPane.CANCEL_OPTION)
                return;
            
            if (flag == JOptionPane.YES_OPTION) {
                int zeroUnitPriceCount = 0;
                for (SalesLineBean lineObj : this.salesObj.getLineList()) {
                    if (lineObj.getUnitPrice() == 0)
                        zeroUnitPriceCount++;
                }
                
                if (zeroUnitPriceCount > 0) {                    
                    JOptionPane.showMessageDialog(this, "系统检查到 " + zeroUnitPriceCount + " 条零价格项目。", "价格不完整", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            String filePath = this.saveToPath + Calendar.getInstance().getTimeInMillis() + ".pdf";
            ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "准备报告 PDF 文档中...");
            
            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception 
                {
                    // Disable Buttons
                    jButtonPrintChinese.setEnabled(false);
                    jButtonPrintMalay.setEnabled(false);
                    
                    SalesBean salesObj4P = SalesDelegate.getInstance().getSalesById(salesObj.getId());
                    if (salesObj4P==null)
                    {
                        return null;
                    }
                    
                    // No transaction id, so create a new one
                    if (salesObj4P.getInvoiceNo()== null || salesObj4P.getInvoiceNo().trim().length() == 0)
                    {
                        salesObj4P.setInvoiceNo(SystemDataDelegate.getInstance().getNextSalesId());
                        salesObj4P.setStatus(SalesBean.STATUS_COMPLETED);                        
                        SalesDelegate.getInstance().saveOrUpdateSales(salesObj4P);
                        
                        jTextFieldInvoiceNumber.setText( salesObj4P.getInvoiceNo() );
                    }
                                                    
                    // Update customer balance before print report
                    CustomerDelegate.getInstance().updateCustomerBalance( salesObj4P.getCustomerId() );
                    
                    // retrieve latest customer object
                    CustomerBean customer = 
                            CustomerDelegate.getInstance().getCustomerByCustomerId( salesObj4P.getCustomerId() );
                    
                    OutboundPDFBoxPrinter.getInstance().printReportCustomerSalesReportChinese(
                            filePath, customer, salesObj4P, JOptionPane.YES_OPTION==flag ? true : false);
                    
                    return null;
                }

                @Override
                protected void done() {
                    // Disable Buttons
                    jButtonPrintChinese.setEnabled(true);
                    jButtonPrintMalay.setEnabled(true);                    
                    dialog.dispose();//close the modal dialog
                }
            };

            swingWorker.execute(); // this will start the processing on a separate thread
            SwingUtil.centerDialogOnScreen( dialog );
            
            // Open created PDF on fly
            if (RootPDFBoxPrinter.OPEN_PDF_ON_FLY == true)
                OutboundPDFBoxPrinter.getInstance().openPDFonFly(filePath);
                
            Desktop desktop = Desktop.getDesktop();
            desktop.print(new File(filePath));
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), e.getMessage());
        }
    }//GEN-LAST:event_jButtonPrintChineseActionPerformed

    private void jButtonRemoveBucketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveBucketActionPerformed
        
        SalesBucketBean bucket = (SalesBucketBean) this.jComboBoxBucketNo.getSelectedItem();
        if ("0".equalsIgnoreCase(bucket.getBucketNo()))
        {
            JOptionPane.showMessageDialog(
                    this, 
                    "无法删除此桶号[" + bucket.getBucketNo() + "]。", 
                    "删除错误", 
                    JOptionPane.WARNING_MESSAGE);
            
            return;
        }
        
        List<SalesLineBean> salesLineList = 
                SalesDelegate.getInstance().getSalesLineBySalesIdAndBucketNo(this.salesObj.getId(), bucket.getBucketNo());
        
        int flag = JOptionPane.showConfirmDialog(
                        this, 
                        "您确定要删除此桶号["+ bucket.getBucketNo() + "]吗？ （有" + salesLineList.size() + "项交易记录）", 
                        "确认删除", 
                        JOptionPane.YES_NO_OPTION);
        
        if (flag == JOptionPane.YES_OPTION) 
        {
            SalesDelegate.getInstance().deleteSalesBucket( bucket );
            
            for (SalesLineBean salesLine : salesLineList)
                SalesDelegate.getInstance().deleteSalesLine( salesLine );
            
            int bucketIndex = -1, index = 0;
            for (SalesBucketBean bucketObj : this.salesObj.getBucketList()) {
                if (bucketObj.getBucketNo().equals( bucket.getBucketNo() ))
                    bucketIndex = index;
                index++;
            }
            
            if (bucketIndex >= 0) {
                this.salesObj.getBucketList().remove(bucketIndex);                
            }
            
            this.jTextFieldTripCount.setText( String.valueOf(this.salesObj.getBucketList().size()) );
            this.jComboBoxBucketNo.removeItem( bucket );
            this.refillSalesLineList();
            
            this.jButtonSave.doClick();
        }        
    }//GEN-LAST:event_jButtonRemoveBucketActionPerformed

    private void jButtonPrintMalayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrintMalayActionPerformed
        try 
        {            
            if (salesObj.getLineList().isEmpty()) {
                JOptionPane.showMessageDialog(this, "系统检查到无销售项目。", "无销售记录", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            this.jButtonSave.doClick();
            
            int flag = JOptionPane.showConfirmDialog(this, "一并打印价格？", "确认打印", JOptionPane.YES_NO_CANCEL_OPTION);            
            if (flag == JOptionPane.CANCEL_OPTION)
                return;
            
            if (flag == JOptionPane.YES_OPTION) {
                int zeroUnitPriceCount = 0;
                for (SalesLineBean lineObj : this.salesObj.getLineList()) {
                    if (lineObj.getUnitPrice() == 0)
                        zeroUnitPriceCount++;
                }
                
                if (zeroUnitPriceCount > 0) {                    
                    JOptionPane.showMessageDialog(this, "系统检查到 " + zeroUnitPriceCount + " 条零价格项目。", "价格不完整", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            String filePath = this.saveToPath + Calendar.getInstance().getTimeInMillis() + ".pdf";
            ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "准备报告 PDF 文档中...");
            
            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception 
                {
                    // Disable Buttons
                    jButtonPrintChinese.setEnabled(false);
                    jButtonPrintMalay.setEnabled(false);
                    
                    SalesBean salesObj4P = SalesDelegate.getInstance().getSalesById(OutboundSalesPanel.this.salesObj.getId());
                    if (salesObj4P==null)
                        return null;
                    
                    // No transaction id, so create a new one
                    if (salesObj4P.getInvoiceNo()== null || salesObj4P.getInvoiceNo().trim().length() == 0)
                    {
                        salesObj4P.setInvoiceNo(SystemDataDelegate.getInstance().getNextSalesId());
                        salesObj4P.setStatus(SalesBean.STATUS_COMPLETED);                        
                        SalesDelegate.getInstance().saveOrUpdateSales(salesObj4P);
                        
                        jTextFieldInvoiceNumber.setText( salesObj4P.getInvoiceNo());
                    }
                                   
                    // Update customer balance before print report
                    CustomerDelegate.getInstance().updateCustomerBalance( salesObj4P.getCustomerId() );
                    
                    // retrieve latest customer object
                    CustomerBean customer = 
                            CustomerDelegate.getInstance().getCustomerByCustomerId( salesObj4P.getCustomerId() );
                    
                    OutboundPDFBoxPrinter.getInstance().printReportCustomerSalesReportMalay(
                            filePath, customer, salesObj4P, JOptionPane.YES_OPTION==flag ? true : false);
                    
                    return null;
                }

                @Override
                protected void done() {
                    // Disable Buttons
                    jButtonPrintChinese.setEnabled(true);
                    jButtonPrintMalay.setEnabled(true);
                    
                    dialog.dispose();//close the modal dialog
                }
            };

            swingWorker.execute(); // this will start the processing on a separate thread
            SwingUtil.centerDialogOnScreen( dialog );
            
            // Open created PDF on fly
            if (RootPDFBoxPrinter.OPEN_PDF_ON_FLY == true)
                OutboundPDFBoxPrinter.getInstance().openPDFonFly(filePath);
                
            Desktop desktop = Desktop.getDesktop();
            desktop.print(new File(filePath));
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), e.getMessage());
        }
    }//GEN-LAST:event_jButtonPrintMalayActionPerformed

    private void jButtonRecalculationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRecalculationActionPerformed
        
        int rowIndex = 0;
        for (SalesLineBean lineObj : salesObj.getLineList()) {     
            double unitPrice = lineObj.getUnitPrice();
            double weight = lineObj.getWeight();
            
            try {              
                if (showingPrice) {
                    if (this.dataRow3[rowIndex].getText()==null || this.dataRow3[rowIndex].getText().trim() == "")
                        unitPrice = 0;
                    else
                        unitPrice = Double.parseDouble( this.dataRow3[rowIndex].getText().trim() );
                } else {
                    unitPrice = lineObj.getUnitPrice();
                }
            } catch (NumberFormatException e) {
                MyLogger.logError(getClass(), "Unable to parse unitPrice to numeric value.");
            }
            
            try {        
                if (showingPrice) {         
                    if (this.dataRow4[rowIndex].getText() == null || this.dataRow4[rowIndex].getText().trim() == "")
                        weight = 0;
                    else
                        weight = Double.parseDouble( this.dataRow4[rowIndex].getText().trim() );
                } else {
                    weight = lineObj.getWeight();
                }
            } catch (NumberFormatException e) {
                MyLogger.logError(getClass(), "Unable to parse weight to numeric value.");
            }
            
            this.salesObj.getLineList().get(rowIndex).setUnitPrice( unitPrice );
            this.salesObj.getLineList().get(rowIndex).setWeight( weight );
            
            rowIndex++;
        }
        
        // Update panel
        this.updateTotalPrice();            
    }//GEN-LAST:event_jButtonRecalculationActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        if (salesObj != null) {
            int flag = JOptionPane.showConfirmDialog(this, "删除此单？", "确认删除", JOptionPane.YES_NO_CANCEL_OPTION);            
            if (flag == JOptionPane.CANCEL_OPTION || flag == JOptionPane.NO_OPTION)
                return;
            
            for (SalesLineBean lineObj : salesObj.getLineList()) {
                SalesDelegate.getInstance().deleteSalesLine(lineObj);
            }
            
            for (SalesBucketBean bucketObj : salesObj.getBucketList()) {
                SalesDelegate.getInstance().deleteSalesBucket(bucketObj);
            }
            
            SalesDelegate.getInstance().deleteSales(salesObj);
            
            this.salesObj = null;
            
            reset();
        }
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jComboBoxBucketNoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxBucketNoItemStateChanged
        //fillForm();
    }//GEN-LAST:event_jComboBoxBucketNoItemStateChanged

    private void jComboBoxBucketNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxBucketNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxBucketNoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddBucket;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonNewOrder;
    private javax.swing.JButton jButtonPrice;
    private javax.swing.JButton jButtonPrintChinese;
    private javax.swing.JButton jButtonPrintMalay;
    private javax.swing.JButton jButtonRecalculation;
    private javax.swing.JButton jButtonRemoveBucket;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JComboBox jComboBoxBucketNo;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelInvoiceNumber;
    private javax.swing.JLabel jLabelShipName;
    private javax.swing.JLabel jLabelSubTotalAmountValue;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelTotalAmountTitle;
    private javax.swing.JLabel jLabelTotalAmountTitle1;
    private javax.swing.JLabel jLabelTotalAmountValue;
    private javax.swing.JLabel jLabelTripCount;
    private javax.swing.JLabel jLabelTripNumber;
    private javax.swing.JList jListCategory;
    private javax.swing.JList jListItem;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
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
    private javax.swing.JPanel jPanelTripCount;
    private javax.swing.JPanel jPanelTripNumber;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextFieldCustomerName;
    private javax.swing.JTextField jTextFieldDate;
    private javax.swing.JTextField jTextFieldInvoiceNumber;
    private javax.swing.JTextField jTextFieldTripCount;
    // End of variables declaration//GEN-END:variables

}

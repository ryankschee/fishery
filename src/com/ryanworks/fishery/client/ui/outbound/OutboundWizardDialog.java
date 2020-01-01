package com.ryanworks.fishery.client.ui.outbound;

import com.ryanworks.fishery.client.delegate.CustomerDelegate;
import com.ryanworks.fishery.client.delegate.SalesDelegate;
import com.ryanworks.fishery.client.ui.control.CustomerEditorDialog;
import com.ryanworks.fishery.client.ui.control.listener.CustomerEditorDialogListener;
import com.ryanworks.fishery.client.ui.shared.ProgressModalDialog;
import com.ryanworks.fishery.client.ui.shared.table.CustomerSelectionTableModel;
import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.SalesBean;
import com.ryanworks.fishery.shared.bean.SalesBucketBean;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import com.ryanworks.fishery.shared.util.MyLogger;
import com.ryanworks.fishery.shared.util.SwingUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

public class OutboundWizardDialog 
    extends javax.swing.JDialog
    implements CustomerEditorDialogListener
{
    public static long lastAccessDate = 0L;
    
    private final OutboundSalesPanel parentPanel;
    private SalesBean salesObj;
    private MyDatePicker datePicker;
    private final DefaultListModel listModelVisitedCustomers = new DefaultListModel();
    private final DefaultListModel listModelUnavailable = new DefaultListModel();
    private final DefaultListModel listModelSelected = new DefaultListModel();
    
    private final HashMap<String, CustomerBean> customerResultMap = new HashMap();
    private final CustomerSelectionTableModel customerSelectionTableModel = new CustomerSelectionTableModel(customerResultMap);
    private TableRowSorter<CustomerSelectionTableModel> sorter;
    
    private final int[] DAYS_IN_MONTH = {31,29,31,30,31,30,31,31,30,31,30,31};
    
    public OutboundWizardDialog(java.awt.Frame parent, boolean modal, OutboundSalesPanel parentPanel) 
    {
        super(parent, modal);
        this.parentPanel = parentPanel;
        
        initComponents();       
        myInit();   
    }
    
    private void myInit() 
    {
        if (this.salesObj == null)
        {
            this.salesObj = new SalesBean();
        }
        
        this.lastAccessDate = Calendar.getInstance().getTimeInMillis();
        this.jTextFieldDate.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format( lastAccessDate ) );
        this.jTextFieldCenter2Filter.requestFocus();
        
        // Refresh list of visited customer
        refreshVisitedCustomerList();
        
        refreshAllCustomerList();
            
        refreshAllCustomerList();
    }
    
    private void fillForm()
    {
        long dateTime = this.getSelectedDateInMillis();
        
        if (dateTime != 0)
        {
            refreshVisitedCustomerList();
            
            refreshAllCustomerList();
            
            refreshUsedBucketList();
        }
    }
    
    private void refreshVisitedCustomerList()
    {
        this.listModelVisitedCustomers.removeAllElements();
                
        List<CustomerBean> customerList = CustomerDelegate.getInstance().getCustomersByDate( this.getSelectedDateInMillis() );
        Collections.sort(customerList);
        for (int i=0 ; i<customerList.size() ; i++)
        {
            this.listModelVisitedCustomers.add(i, customerList.get(i));
        }
    }
    
    private void refreshAllCustomerList()
    {
        this.customerSelectionTableModel.removeAll();
        
        // Retrieve all customers from database and insert into table
        List<CustomerBean> customerList2 = CustomerDelegate.getInstance().getAllCustomers();
        Collections.sort(customerList2);
        for (CustomerBean bean : customerList2) {
            this.customerSelectionTableModel.addBean(bean);
        }
        
        // Create sorter for table of customer
        sorter = new TableRowSorter<>(customerSelectionTableModel);
        this.jTableCustomer.setRowSorter(sorter);
        this.jTableCustomer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.jTableCustomer.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 20));
        this.jTableCustomer.setRowHeight(30);
        this.createKeybindings(this.jTableCustomer);
        
        // Sort table of customer programmatically, order by column index 0.
        ArrayList list = new ArrayList();
        list.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        sorter.setSortKeys(list);
        sorter.sort();
                
        // Add listener into table of customer
        TableSelectionListener listener = new TableSelectionListener(this.jTableCustomer);
        this.jTableCustomer.getSelectionModel().addListSelectionListener(listener);
        this.jTableCustomer.getColumnModel().getSelectionModel().addListSelectionListener(listener);
        
        //Whenever filterText changes, invoke newFilter.
        this.jTextFieldCenter2Filter.getDocument().addDocumentListener(
            new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    newFilter();
                }
                @Override
                public void insertUpdate(DocumentEvent e) {
                    newFilter();
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    newFilter();
                }
            });
    }
    
    private void refreshUsedBucketList()
    {
        this.listModelUnavailable.removeAllElements();
                
        List<SalesBucketBean> bucketList = SalesDelegate.getInstance().getBucketListByDate(this.getSelectedDateInMillis() );
        for (int i=0 ; i<bucketList.size() ; i++)
        {
            // Ignore '0' bucket, which is default bucket if user select none.
            if ("0".equalsIgnoreCase(bucketList.get(i).getBucketNo()))
                continue;
            
            this.listModelUnavailable.add(i, bucketList.get(i));
        }
    }
    
    private void newFilter() 
    {
        RowFilter<CustomerSelectionTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(this.jTextFieldCenter2Filter.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    
    private long getSelectedDateInMillis()
    {
        String dateInString = this.jTextFieldDate.getText().trim();
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
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return -1;
        }
        catch (Exception e) {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return -1;
        }
    }
    
    private CustomerBean getSelectedCustomer()
    {
        return this.customerSelectionTableModel.getCustomerById( this.jTextFieldCustomerId.getText().trim() );
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelTop = new javax.swing.JPanel();
        jPanelDatePicker = new javax.swing.JPanel();
        jLabelDatePicker = new javax.swing.JLabel();
        jButtonDatePicker = new javax.swing.JButton();
        jTextFieldDate = new javax.swing.JTextField();
        jPanelStatus = new javax.swing.JPanel();
        jLabelStatus = new javax.swing.JLabel();
        jPanelCenter = new javax.swing.JPanel();
        jPanelCenter1 = new javax.swing.JPanel();
        jScrollPaneCenter2 = new javax.swing.JScrollPane();
        jListCustomerAttended = new javax.swing.JList();
        jPanelCenter2 = new javax.swing.JPanel();
        jTextFieldCenter2Filter = new javax.swing.JTextField();
        jScrollPaneCenter1 = new javax.swing.JScrollPane();
        jTableCustomer = new javax.swing.JTable();
        jPanelCenter3 = new javax.swing.JPanel();
        jPanelCenter3Create = new javax.swing.JPanel();
        jButtonNewBucket = new javax.swing.JButton();
        jTextFieldNewBucket = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPaneCenter3Buckets = new javax.swing.JScrollPane();
        jListBucketListUnavailable = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jLabelBucketStatus = new javax.swing.JLabel();
        jPanelCenter4 = new javax.swing.JPanel();
        jLabelCustomerId = new javax.swing.JLabel();
        jTextFieldCustomerId = new javax.swing.JTextField();
        jLabelCustomerName = new javax.swing.JLabel();
        jTextFieldCustomerName = new javax.swing.JTextField();
        jLabelBucketNo = new javax.swing.JLabel();
        jLabelCustomerTripNo = new javax.swing.JLabel();
        jTextFieldCustomerTripNo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListBucketListSelected = new javax.swing.JList();
        jButtonDeleteBucket = new javax.swing.JButton();
        jPanelBottom = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButtonAddNewCustomer = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1400, 800));

        jPanelTop.setLayout(new java.awt.BorderLayout(0, 3));

        jPanelDatePicker.setLayout(new java.awt.BorderLayout(3, 3));

        jLabelDatePicker.setBackground(new java.awt.Color(0, 153, 255));
        jLabelDatePicker.setFont(new java.awt.Font("KaiTi", 0, 30)); // NOI18N
        jLabelDatePicker.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDatePicker.setText("请输入日期");
        jLabelDatePicker.setOpaque(true);
        jPanelDatePicker.add(jLabelDatePicker, java.awt.BorderLayout.PAGE_START);

        jButtonDatePicker.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonDatePicker.setText("选择日期");
        jButtonDatePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDatePickerActionPerformed(evt);
            }
        });
        jPanelDatePicker.add(jButtonDatePicker, java.awt.BorderLayout.LINE_START);

        jTextFieldDate.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jTextFieldDate.setText("01/01/2014");
        jTextFieldDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldDateFocusLost(evt);
            }
        });
        jTextFieldDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldDateKeyReleased(evt);
            }
        });
        jPanelDatePicker.add(jTextFieldDate, java.awt.BorderLayout.CENTER);

        jPanelTop.add(jPanelDatePicker, java.awt.BorderLayout.CENTER);

        jPanelStatus.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabelStatus.setFont(new java.awt.Font("KaiTi", 0, 14)); // NOI18N
        jLabelStatus.setText("状态：");
        jPanelStatus.add(jLabelStatus);

        jPanelTop.add(jPanelStatus, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanelTop, java.awt.BorderLayout.PAGE_START);

        jPanelCenter.setPreferredSize(new java.awt.Dimension(1100, 483));
        jPanelCenter.setLayout(new java.awt.GridLayout(1, 4));

        jPanelCenter1.setBackground(new java.awt.Color(255, 153, 153));
        jPanelCenter1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "当天已来的客户", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 14))); // NOI18N
        jPanelCenter1.setLayout(new java.awt.BorderLayout(0, 3));

        jListCustomerAttended.setFont(new java.awt.Font("KaiTi", 0, 20)); // NOI18N
        jListCustomerAttended.setModel(listModelVisitedCustomers);
        jListCustomerAttended.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListCustomerAttended.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListCustomerAttendedMouseClicked(evt);
            }
        });
        jListCustomerAttended.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jListCustomerAttendedKeyTyped(evt);
            }
        });
        jListCustomerAttended.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListCustomerAttendedValueChanged(evt);
            }
        });
        jScrollPaneCenter2.setViewportView(jListCustomerAttended);

        jPanelCenter1.add(jScrollPaneCenter2, java.awt.BorderLayout.CENTER);

        jPanelCenter.add(jPanelCenter1);

        jPanelCenter2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "客户号码　/ 名字", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 14))); // NOI18N
        jPanelCenter2.setLayout(new java.awt.BorderLayout(0, 3));

        jTextFieldCenter2Filter.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldCenter2Filter.setPreferredSize(new java.awt.Dimension(6, 36));
        jTextFieldCenter2Filter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCenter2FilterActionPerformed(evt);
            }
        });
        jPanelCenter2.add(jTextFieldCenter2Filter, java.awt.BorderLayout.PAGE_START);

        jTableCustomer.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jTableCustomer.setModel(customerSelectionTableModel);
        jScrollPaneCenter1.setViewportView(jTableCustomer);

        jPanelCenter2.add(jScrollPaneCenter1, java.awt.BorderLayout.CENTER);

        jPanelCenter.add(jPanelCenter2);

        jPanelCenter3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "桶子", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 14))); // NOI18N
        jPanelCenter3.setLayout(new java.awt.BorderLayout(0, 3));

        jPanelCenter3Create.setLayout(new java.awt.BorderLayout(3, 0));

        jButtonNewBucket.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonNewBucket.setText("创键新桶子");
        jButtonNewBucket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewBucketActionPerformed(evt);
            }
        });
        jPanelCenter3Create.add(jButtonNewBucket, java.awt.BorderLayout.EAST);

        jTextFieldNewBucket.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldNewBucket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNewBucketActionPerformed(evt);
            }
        });
        jTextFieldNewBucket.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldNewBucketKeyReleased(evt);
            }
        });
        jPanelCenter3Create.add(jTextFieldNewBucket, java.awt.BorderLayout.CENTER);

        jPanelCenter3.add(jPanelCenter3Create, java.awt.BorderLayout.PAGE_START);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPaneCenter3Buckets.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "已选桶号", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 16), new java.awt.Color(255, 0, 0))); // NOI18N

        jListBucketListUnavailable.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListBucketListUnavailable.setForeground(new java.awt.Color(255, 0, 0));
        jListBucketListUnavailable.setModel(listModelUnavailable);
        jListBucketListUnavailable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListBucketListUnavailable.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListBucketListUnavailableValueChanged(evt);
            }
        });
        jScrollPaneCenter3Buckets.setViewportView(jListBucketListUnavailable);

        jPanel1.add(jScrollPaneCenter3Buckets, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabelBucketStatus.setFont(new java.awt.Font("KaiTi", 0, 14)); // NOI18N
        jLabelBucketStatus.setForeground(new java.awt.Color(255, 0, 0));
        jLabelBucketStatus.setPreferredSize(new java.awt.Dimension(49, 30));
        jPanel2.add(jLabelBucketStatus, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanelCenter3.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanelCenter.add(jPanelCenter3);

        jPanelCenter4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "已选择客户", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 14), new java.awt.Color(153, 0, 51))); // NOI18N

        jLabelCustomerId.setFont(new java.awt.Font("KaiTi", 0, 14)); // NOI18N
        jLabelCustomerId.setText("客户号码：");

        jTextFieldCustomerId.setEditable(false);
        jTextFieldCustomerId.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N

        jLabelCustomerName.setFont(new java.awt.Font("KaiTi", 0, 14)); // NOI18N
        jLabelCustomerName.setText("名字：");

        jTextFieldCustomerName.setEditable(false);
        jTextFieldCustomerName.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N

        jLabelBucketNo.setFont(new java.awt.Font("KaiTi", 0, 14)); // NOI18N
        jLabelBucketNo.setText("桶子号码:");

        jLabelCustomerTripNo.setFont(new java.awt.Font("KaiTi", 0, 14)); // NOI18N
        jLabelCustomerTripNo.setText("今天已来次数：");

        jTextFieldCustomerTripNo.setEditable(false);
        jTextFieldCustomerTripNo.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N

        jListBucketListSelected.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListBucketListSelected.setModel(listModelSelected);
        jScrollPane1.setViewportView(jListBucketListSelected);

        jButtonDeleteBucket.setFont(new java.awt.Font("KaiTi", 0, 14)); // NOI18N
        jButtonDeleteBucket.setText("删除桶号");
        jButtonDeleteBucket.setPreferredSize(new java.awt.Dimension(73, 30));
        jButtonDeleteBucket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteBucketActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCenter4Layout = new javax.swing.GroupLayout(jPanelCenter4);
        jPanelCenter4.setLayout(jPanelCenter4Layout);
        jPanelCenter4Layout.setHorizontalGroup(
            jPanelCenter4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCenter4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCenter4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jTextFieldCustomerId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                    .addComponent(jTextFieldCustomerName, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldCustomerTripNo, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelCenter4Layout.createSequentialGroup()
                        .addGroup(jPanelCenter4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelBucketNo)
                            .addComponent(jLabelCustomerId)
                            .addComponent(jLabelCustomerName)
                            .addComponent(jLabelCustomerTripNo))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButtonDeleteBucket, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelCenter4Layout.setVerticalGroup(
            jPanelCenter4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCenter4Layout.createSequentialGroup()
                .addComponent(jLabelCustomerId)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldCustomerId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCustomerName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCustomerTripNo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldCustomerTripNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelBucketNo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDeleteBucket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelCenter.add(jPanelCenter4);

        getContentPane().add(jPanelCenter, java.awt.BorderLayout.CENTER);

        jButtonOk.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonOk.setText("确定");
        jButtonOk.setPreferredSize(new java.awt.Dimension(150, 37));
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanelBottom.add(jButtonOk);

        jButtonCancel.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonCancel.setText("取消");
        jButtonCancel.setPreferredSize(new java.awt.Dimension(150, 37));
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanelBottom.add(jButtonCancel);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setMinimumSize(new java.awt.Dimension(30, 50));
        jSeparator1.setPreferredSize(new java.awt.Dimension(10, 40));
        jPanelBottom.add(jSeparator1);

        jButtonAddNewCustomer.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonAddNewCustomer.setForeground(new java.awt.Color(0, 0, 102));
        jButtonAddNewCustomer.setText("新增客户");
        jButtonAddNewCustomer.setPreferredSize(new java.awt.Dimension(150, 37));
        jButtonAddNewCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddNewCustomerActionPerformed(evt);
            }
        });
        jPanelBottom.add(jButtonAddNewCustomer);

        getContentPane().add(jPanelBottom, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jListBucketListUnavailableValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListBucketListUnavailableValueChanged
        Object selectedObj = this.jListBucketListUnavailable.getSelectedValue();
        
        if (selectedObj != null)
        {
            
        }
    }//GEN-LAST:event_jListBucketListUnavailableValueChanged

    private void jTextFieldNewBucketKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNewBucketKeyReleased
        String inputText = this.jTextFieldNewBucket.getText().trim();
        
        if (inputText.length() > 0)
        {
            if (jListContainsBucketNo(jListBucketListUnavailable, inputText) != null)
            {
                this.jLabelBucketStatus.setText( "桶号 [" + inputText + "] 已经被使用。" );
                this.jTextFieldNewBucket.setBackground( Color.YELLOW );
                this.jTextFieldNewBucket.requestFocus();
                this.jTextFieldNewBucket.selectAll();
                
                return;
            }
            else
            {
                this.jLabelBucketStatus.setText( "" );
                this.jTextFieldNewBucket.setBackground( Color.WHITE );
            }
        }
    }//GEN-LAST:event_jTextFieldNewBucketKeyReleased

    private void jButtonNewBucketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewBucketActionPerformed
        
        String inputText = this.jTextFieldNewBucket.getText().trim();
        if (inputText.length() > 0)
        {
            if ( jListContainsBucketNo(jListBucketListUnavailable, inputText) != null )
            {
                this.jLabelBucketStatus.setText( "桶号 [" + inputText + "] 已经被使用。" );
                this.jTextFieldNewBucket.setBackground( Color.YELLOW );
                this.jTextFieldNewBucket.requestFocus();
                this.jTextFieldNewBucket.selectAll();
                
                return;
            }
            else
            {
                this.jLabelBucketStatus.setText( "" );
                SalesBucketBean bucketBean = this.salesObj.addBucket(inputText, this.getSelectedDateInMillis());
                
                ((DefaultListModel)this.jListBucketListSelected.getModel()).addElement( bucketBean );
                ((DefaultListModel)this.jListBucketListUnavailable.getModel()).addElement( bucketBean );        
            }
        }
        else
        {
            this.jLabelBucketStatus.setText( "桶号 [" + inputText + "] 不正确。" );
        }
    }//GEN-LAST:event_jButtonNewBucketActionPerformed

    private void jTextFieldDateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDateKeyReleased
        int keyCode = (int) evt.getKeyChar();
        
        // If user press ENTER, response.
        if (keyCode==10)
        {
            int caretPosition = this.jTextFieldDate.getCaretPosition();
            if (caretPosition==2)
            {
                this.jTextFieldDate.setCaretPosition( caretPosition+1 );
                this.jTextFieldDate.select(caretPosition+1, caretPosition+3);
            }
            
            if (caretPosition==5)
            {
                this.jTextFieldDate.setCaretPosition(caretPosition+1);
                this.jTextFieldDate.select(caretPosition+1, caretPosition+5);
            }
            
            if (caretPosition==10)
            {
                this.jTextFieldCenter2Filter.requestFocus();
            }            
        }
        
        if ((lastAccessDate=getSelectedDateInMillis()) != 0)
        {
            fillForm();
        }
    }//GEN-LAST:event_jTextFieldDateKeyReleased

    private void jTextFieldDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDateFocusGained
        this.jTextFieldDate.setBackground(Color.YELLOW);
    }//GEN-LAST:event_jTextFieldDateFocusGained

    private void jTextFieldDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDateFocusLost
        this.jTextFieldDate.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTextFieldDateFocusLost

    private void jTextFieldCenter2FilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCenter2FilterActionPerformed
        if (this.jTableCustomer.getRowCount() > 0) 
        {
            this.jTableCustomer.requestFocus();
            this.jTableCustomer.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_jTextFieldCenter2FilterActionPerformed

    private void jTextFieldNewBucketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNewBucketActionPerformed
        
        /**
         * Assumptions on User Behavior.
         * - Usually, user select only one bucket in this wizard.
         * - Once user key in bucket number in textfield and press enter, the system assumes
         *   only one bucket and save it immediately.
         */
        
        // First call add new bucket
        this.jButtonNewBucket.doClick();
        
        // Second call confirm new order
        this.jButtonOk.doClick();
    }//GEN-LAST:event_jTextFieldNewBucketActionPerformed

    private void jButtonDeleteBucketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteBucketActionPerformed
        Object bucketObject = this.jListBucketListSelected.getModel().getElementAt( this.jListBucketListSelected.getSelectedIndex() );
        
        if (bucketObject != null)
        {
            SalesBucketBean bucketBean = (SalesBucketBean) bucketObject;
            
            this.salesObj.deleteBucketBean(bucketBean);
            
            for (int i=0 ; i < this.listModelUnavailable.size() ; i++)
            {
                if (((SalesBucketBean)this.listModelUnavailable.get(i)).getBucketNo().equalsIgnoreCase(
                        bucketBean.getBucketNo()))
                {
                    this.listModelUnavailable.remove(i);
                    break;
                }
            }
            
            for (int i=0 ; i < this.listModelSelected.size() ; i++)
            {
                if (((SalesBucketBean)this.listModelSelected.get(i)).getBucketNo().equalsIgnoreCase(
                        bucketBean.getBucketNo()))
                {
                    this.listModelSelected.remove(i);
                    break;
                }
            }
        }
    }//GEN-LAST:event_jButtonDeleteBucketActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        long selectedDateInMillis;
        // Check if date format valid
        if ((selectedDateInMillis=this.getSelectedDateInMillis())== -1) 
        {
            JOptionPane.showMessageDialog(this.parentPanel, "日期格式错误", "您输入的日期格式有错误。 [dd/mm/yyyy]", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if supplier selected.
        CustomerBean customer = this.getSelectedCustomer();
        if (customer == null) 
        {
            JOptionPane.showMessageDialog(this.parentPanel, "客户选择错误", "请选择正确的客户资料。", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // If no bucket # assigned, then assign a default bucket '0'
        if (this.salesObj.getBucketList().size() == 0)
            this.salesObj.addBucket("0", this.getSelectedDateInMillis());
        
        this.setVisible(false);
        this.dispose();
        
        // Call main panel for data population
        this.parentPanel.wizardDialogClosed( this.salesObj, customer, selectedDateInMillis, Integer.parseInt(this.jTextFieldCustomerTripNo.getText())+1 );
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jListCustomerAttendedKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jListCustomerAttendedKeyTyped
        
        if (evt.getKeyCode()==0)
        {
            OutboundWizardDialog.this.jButtonOk.doClick();
        }
    }//GEN-LAST:event_jListCustomerAttendedKeyTyped

    private void jListCustomerAttendedValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListCustomerAttendedValueChanged
        ProgressModalDialog dialog = new ProgressModalDialog(null, true, "读取数据中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {    
                if (jListCustomerAttended.getSelectedIndex() != -1)
                {
                    String cellValue = jListCustomerAttended.getSelectedValue().toString();
                    String selectedCustomerId = cellValue.substring(0, cellValue.indexOf(" - ")).trim();

                    CustomerBean customer = 
                            customerSelectionTableModel.getCustomerById(selectedCustomerId);
                    jTextFieldCustomerId.setText(customer==null? "错误" : customer.getId());
                    jTextFieldCustomerId.setCaretPosition(0);

                    jTextFieldCustomerName.setText(customer==null? "错误" : customer.getName());
                    jTextFieldCustomerName.setCaretPosition(0);

                    jTextFieldCustomerTripNo.setText( 
                            String.valueOf(SalesDelegate.getInstance().getTripNoByCustomerIdAndDate(
                                    selectedCustomerId, 
                                    getSelectedDateInMillis()) ));
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
    }//GEN-LAST:event_jListCustomerAttendedValueChanged

    private void jListCustomerAttendedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListCustomerAttendedMouseClicked
        ProgressModalDialog dialog = new ProgressModalDialog(null, true, "读取数据中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {    
                if (jListCustomerAttended.getSelectedIndex() != -1)
                {
                    String cellValue = jListCustomerAttended.getSelectedValue().toString();
                    String selectedCustomerId = cellValue.substring(0, cellValue.indexOf(" - ")).trim();

                    CustomerBean customer = 
                            customerSelectionTableModel.getCustomerById(selectedCustomerId);
                    jTextFieldCustomerId.setText(customer==null? "错误" : customer.getId());
                    jTextFieldCustomerId.setCaretPosition(0);

                    jTextFieldCustomerName.setText(customer==null? "错误" : customer.getName());
                    jTextFieldCustomerName.setCaretPosition(0);

                    jTextFieldCustomerTripNo.setText( 
                            String.valueOf(SalesDelegate.getInstance().getTripNoByCustomerIdAndDate(
                                    selectedCustomerId, 
                                    getSelectedDateInMillis()) ));
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
    }//GEN-LAST:event_jListCustomerAttendedMouseClicked

    private void jButtonAddNewCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddNewCustomerActionPerformed
        SwingUtil.centerDialogOnScreen( new CustomerEditorDialog(this, true, this, null) );
    }//GEN-LAST:event_jButtonAddNewCustomerActionPerformed

    private void jButtonDatePickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDatePickerActionPerformed
        if (this.datePicker==null) {
            this.datePicker = new MyDatePicker(null);
        }
        else {
            this.datePicker.showDialog();
        }
        
        if (this.datePicker.isOk()) {
            Calendar date = this.datePicker.getSelectedDate();
            date.set(Calendar.HOUR_OF_DAY, 23);
            date.set(Calendar.MINUTE, 59);
            date.set(Calendar.SECOND, 59);
            
            this.jTextFieldDate.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format(date.getTime()) );
            
            // Request focus back for Date jTextField
            this.jTextFieldDate.requestFocus();
            this.jTextFieldDate.setCaretPosition(0);
            this.jTextFieldDate.select(0, 2);
            
            if (getSelectedDateInMillis() != 0)
                refreshVisitedCustomerList();
        }
    }//GEN-LAST:event_jButtonDatePickerActionPerformed

    private void createKeybindings(JTable table) 
    {
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
            table.getActionMap().put("Enter", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    //do something on JTable enter pressed
                    OutboundWizardDialog.this.jButtonOk.doClick();
                }
            });
    }
    
    private SalesBucketBean jListContainsBucketNo(JList jList, String bucketNo)
    {        
        DefaultListModel listModel = (DefaultListModel) jList.getModel() ;
        for (int i=0 ; i<listModel.size() ; i++)
        {
            Object bucketObject = listModel.get(i);
            if (bucketNo.equalsIgnoreCase( ((SalesBucketBean)bucketObject).getBucketNo()))
            {
                return (SalesBucketBean) bucketObject;
            }
        }
        
        return null;
    }

    @Override
    public void customerEditorDialogClosed(CustomerBean customerBean) {
        if (customerBean != null)
        {
            CustomerDelegate.getInstance().saveOrUpdateCustomer(customerBean);
            
            refreshAllCustomerList();
        }
    }
    
    class TableSelectionListener implements ListSelectionListener
    {
        JTable jTable;
        
        TableSelectionListener(JTable table)
        {
            jTable = table;
        }
        
        @Override
        public void valueChanged(ListSelectionEvent e) 
        {
            ProgressModalDialog dialog = new ProgressModalDialog(null, true, "读取数据中...");            
            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception 
                {    
                    int selectedRow = OutboundWizardDialog.this.jTableCustomer.getSelectedRow();
                    if (selectedRow == -1)
                        return null;

                    String cellValue = OutboundWizardDialog.this.jTableCustomer.getValueAt(selectedRow, 0).toString().trim();
                    String selectedCustomerId = cellValue.substring(0, cellValue.indexOf(" - ")).trim();

                    CustomerBean customer = 
                            customerSelectionTableModel.getCustomerById(selectedCustomerId);
                    jTextFieldCustomerId.setText(customer==null? "错误" : customer.getId());
                    jTextFieldCustomerId.setCaretPosition(0);

                    jTextFieldCustomerName.setText(customer==null? "错误" : customer.getName());
                    jTextFieldCustomerName.setCaretPosition(0);

                    jTextFieldCustomerTripNo.setText( 
                            String.valueOf(SalesDelegate.getInstance().getTripNoByCustomerIdAndDate(
                                    selectedCustomerId, 
                                    getSelectedDateInMillis()) ));    

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
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddNewCustomer;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDatePicker;
    private javax.swing.JButton jButtonDeleteBucket;
    private javax.swing.JButton jButtonNewBucket;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabelBucketNo;
    private javax.swing.JLabel jLabelBucketStatus;
    private javax.swing.JLabel jLabelCustomerId;
    private javax.swing.JLabel jLabelCustomerName;
    private javax.swing.JLabel jLabelCustomerTripNo;
    private javax.swing.JLabel jLabelDatePicker;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JList jListBucketListSelected;
    private javax.swing.JList jListBucketListUnavailable;
    private javax.swing.JList jListCustomerAttended;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelBottom;
    private javax.swing.JPanel jPanelCenter;
    private javax.swing.JPanel jPanelCenter1;
    private javax.swing.JPanel jPanelCenter2;
    private javax.swing.JPanel jPanelCenter3;
    private javax.swing.JPanel jPanelCenter3Create;
    private javax.swing.JPanel jPanelCenter4;
    private javax.swing.JPanel jPanelDatePicker;
    private javax.swing.JPanel jPanelStatus;
    private javax.swing.JPanel jPanelTop;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneCenter1;
    private javax.swing.JScrollPane jScrollPaneCenter2;
    private javax.swing.JScrollPane jScrollPaneCenter3Buckets;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTableCustomer;
    private javax.swing.JTextField jTextFieldCenter2Filter;
    private javax.swing.JTextField jTextFieldCustomerId;
    private javax.swing.JTextField jTextFieldCustomerName;
    private javax.swing.JTextField jTextFieldCustomerTripNo;
    private javax.swing.JTextField jTextFieldDate;
    private javax.swing.JTextField jTextFieldNewBucket;
    // End of variables declaration//GEN-END:variables
}

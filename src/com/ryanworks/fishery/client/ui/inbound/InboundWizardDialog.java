package com.ryanworks.fishery.client.ui.inbound;

import com.ryanworks.fishery.client.delegate.InTransactionDelegate;
import com.ryanworks.fishery.client.delegate.SupplierDelegate;
import com.ryanworks.fishery.client.ui.shared.ProgressModalDialog;
import com.ryanworks.fishery.client.ui.shared.table.SupplierSelectionTableModel;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import com.ryanworks.fishery.shared.util.MyLogger;
import com.ryanworks.fishery.shared.util.SwingUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

public class InboundWizardDialog extends javax.swing.JDialog 
{
    public static long lastAccessDate = 0L;
    
    private SupplierBean supplierObj;
    
    private final InboundStockingPanel parentPanel;
    private MyDatePicker datePicker;
    private final DefaultListModel visitedSupplierListModel = new DefaultListModel();
    private final DefaultListModel frequentSupplierListModel = new DefaultListModel();
    
    private final HashMap<String, SupplierBean> supplierResultMap = new HashMap();
    private final SupplierSelectionTableModel supplierSelectionTableModel = new SupplierSelectionTableModel(supplierResultMap);
    private TableRowSorter<SupplierSelectionTableModel> sorter;
    
    private final int[] DAYS_IN_MONTH = {31,29,31,30,31,30,31,31,30,31,30,31};
    
    public InboundWizardDialog(java.awt.Frame parent, boolean modal, InboundStockingPanel parentPanel) 
    {
        super(parent, modal);
        this.parentPanel = parentPanel;
        
        initComponents();     
        myInit();
    }
    
    private void myInit() 
    {
        this.lastAccessDate = Calendar.getInstance().getTimeInMillis();        
        this.jTextFieldDate.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format( lastAccessDate ) );        
        this.jTextFieldCenter3Filter.requestFocus();        
        this.jTableSupplier.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 20));
        this.jTableSupplier.setRowHeight(30);
        
        this.refreshVisitedSupplierList();
        this.refreshFrequentSupplierList();
        this.refreshNonFrequentSupplierList();
        
        // Whenever filterText changes, invoke newFilter.
        this.jTextFieldCenter3Filter.getDocument().addDocumentListener(
            new DocumentListener() 
            {
                public void changedUpdate(DocumentEvent e) {
                    newFilter();
                }
                public void insertUpdate(DocumentEvent e) {
                    newFilter();
                }
                public void removeUpdate(DocumentEvent e) {
                    newFilter();
                }
            });
    }

    private void refreshVisitedSupplierList()
    {
        this.visitedSupplierListModel.removeAllElements();
                
        List<SupplierBean> supplierList = SupplierDelegate.getInstance().getSuppliersByDate( this.getSelectedDateInMillis() );
        Collections.sort(supplierList);
        for (int i=0 ; i<supplierList.size() ; i++)
        {
            this.visitedSupplierListModel.add(i, supplierList.get(i));
        }
    }
    
    private void refreshFrequentSupplierList()
    {
        this.frequentSupplierListModel.removeAllElements();
                
        List<SupplierBean> supplierList = SupplierDelegate.getInstance().getFrequentSuppliers();
        Collections.sort(supplierList);
        for (int i=0 ; i<supplierList.size() ; i++)
        {
            this.frequentSupplierListModel.add(i, supplierList.get(i));
        }
    }
    
    private void refreshNonFrequentSupplierList()
    {
        List<SupplierBean> supplierList2 = SupplierDelegate.getInstance().getNonFrequentSuppliers();
        Collections.sort(supplierList2);
        for (SupplierBean bean : supplierList2) 
        {
            this.supplierSelectionTableModel.addBean(bean);
        }
        
        sorter = new TableRowSorter<SupplierSelectionTableModel>(supplierSelectionTableModel);
        this.jTableSupplier.setRowSorter(sorter);
        this.jTableSupplier.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.createKeybindings(this.jTableSupplier);
        
        TableSelectionListener listener = new TableSelectionListener(this.jTableSupplier);
        this.jTableSupplier.getSelectionModel().addListSelectionListener(listener);
        this.jTableSupplier.getColumnModel().getSelectionModel().addListSelectionListener(listener);
        
        sorter.sort();
    }
    
    private void newFilter() 
    {
        RowFilter<SupplierSelectionTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(this.jTextFieldCenter3Filter.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            MyLogger.logError(getClass(), e.getMessage());
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
            MyLogger.logError(getClass(), e.getMessage());
            return -1;
        }
        catch (Exception e) {
            MyLogger.logError(getClass(), e.getMessage());
            return -1;
        }
    }
    
    private SupplierBean getSelectedSupplier()
    {
        SupplierBean supplier = 
                this.supplierSelectionTableModel.getSupplierById( this.jTextFieldSupplierId.getText().trim() );
        
        return supplier;
    }
    
    private void createKeybindings(JTable table) 
    {
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
            table.getActionMap().put("Enter", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    //do something on JTable enter pressed
                    InboundWizardDialog.this.jButtonOk.doClick();
                }
            });
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
        jScrollPaneCenter1 = new javax.swing.JScrollPane();
        jListCenter1 = new javax.swing.JList();
        jPanelCenter2 = new javax.swing.JPanel();
        jScrollPaneCenter2 = new javax.swing.JScrollPane();
        jListCenter2 = new javax.swing.JList();
        jPanelCenter3 = new javax.swing.JPanel();
        jTextFieldCenter3Filter = new javax.swing.JTextField();
        jScrollPaneCenter3 = new javax.swing.JScrollPane();
        jTableSupplier = new javax.swing.JTable();
        jPanelCenter4 = new javax.swing.JPanel();
        jLabelSupplierId = new javax.swing.JLabel();
        jTextFieldSupplierId = new javax.swing.JTextField();
        jLabelSupplierName = new javax.swing.JLabel();
        jTextFieldSupplierName = new javax.swing.JTextField();
        jLabelSupplierTripNo = new javax.swing.JLabel();
        jTextFieldSupplierTripNo = new javax.swing.JTextField();
        jPanelBottom = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setFont(new java.awt.Font("KaiTi", 0, 10)); // NOI18N
        setPreferredSize(new java.awt.Dimension(1400, 800));

        jPanelTop.setLayout(new java.awt.BorderLayout(0, 3));

        jPanelDatePicker.setLayout(new java.awt.BorderLayout(3, 3));

        jLabelDatePicker.setBackground(new java.awt.Color(0, 102, 255));
        jLabelDatePicker.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
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

        jPanelCenter.setLayout(new java.awt.GridLayout(1, 4));

        jPanelCenter1.setBackground(new java.awt.Color(51, 153, 255));
        jPanelCenter1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "今天已来的船户", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanelCenter1.setLayout(new java.awt.BorderLayout(0, 3));

        jListCenter1.setFont(new java.awt.Font("KaiTi", 0, 20)); // NOI18N
        jListCenter1.setModel(visitedSupplierListModel);
        jListCenter1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListCenter1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jListCenter1KeyTyped(evt);
            }
        });
        jListCenter1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListCenter1ValueChanged(evt);
            }
        });
        jScrollPaneCenter1.setViewportView(jListCenter1);

        jPanelCenter1.add(jScrollPaneCenter1, java.awt.BorderLayout.CENTER);

        jPanelCenter.add(jPanelCenter1);

        jPanelCenter2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "经常来的船户", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanelCenter2.setLayout(new java.awt.BorderLayout(0, 3));

        jListCenter2.setFont(new java.awt.Font("KaiTi", 0, 20)); // NOI18N
        jListCenter2.setModel(frequentSupplierListModel);
        jListCenter2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListCenter2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jListCenter2KeyTyped(evt);
            }
        });
        jListCenter2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListCenter2ValueChanged(evt);
            }
        });
        jScrollPaneCenter2.setViewportView(jListCenter2);

        jPanelCenter2.add(jScrollPaneCenter2, java.awt.BorderLayout.CENTER);

        jPanelCenter.add(jPanelCenter2);

        jPanelCenter3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "其他船户号码 / 名字", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanelCenter3.setLayout(new java.awt.BorderLayout(0, 3));

        jTextFieldCenter3Filter.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jTextFieldCenter3Filter.setPreferredSize(new java.awt.Dimension(6, 36));
        jTextFieldCenter3Filter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldCenter3FilterFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldCenter3FilterFocusLost(evt);
            }
        });
        jTextFieldCenter3Filter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldCenter3FilterKeyTyped(evt);
            }
        });
        jPanelCenter3.add(jTextFieldCenter3Filter, java.awt.BorderLayout.PAGE_START);

        jTableSupplier.setFont(new java.awt.Font("KaiTi", 0, 20)); // NOI18N
        jTableSupplier.setModel(supplierSelectionTableModel);
        jScrollPaneCenter3.setViewportView(jTableSupplier);

        jPanelCenter3.add(jScrollPaneCenter3, java.awt.BorderLayout.CENTER);

        jPanelCenter.add(jPanelCenter3);

        jPanelCenter4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "您选择的船户", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18), new java.awt.Color(153, 0, 51))); // NOI18N

        jLabelSupplierId.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabelSupplierId.setText("船户号码：");

        jTextFieldSupplierId.setEditable(false);
        jTextFieldSupplierId.setFont(new java.awt.Font("KaiTi", 0, 20)); // NOI18N
        jTextFieldSupplierId.setPreferredSize(new java.awt.Dimension(6, 40));

        jLabelSupplierName.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabelSupplierName.setText("船户名字：");

        jTextFieldSupplierName.setEditable(false);
        jTextFieldSupplierName.setFont(new java.awt.Font("KaiTi", 0, 20)); // NOI18N
        jTextFieldSupplierName.setPreferredSize(new java.awt.Dimension(6, 40));

        jLabelSupplierTripNo.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabelSupplierTripNo.setText("船户今天来货次数：");

        jTextFieldSupplierTripNo.setEditable(false);
        jTextFieldSupplierTripNo.setFont(new java.awt.Font("KaiTi", 0, 20)); // NOI18N
        jTextFieldSupplierTripNo.setPreferredSize(new java.awt.Dimension(6, 40));

        javax.swing.GroupLayout jPanelCenter4Layout = new javax.swing.GroupLayout(jPanelCenter4);
        jPanelCenter4.setLayout(jPanelCenter4Layout);
        jPanelCenter4Layout.setHorizontalGroup(
            jPanelCenter4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCenter4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanelCenter4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldSupplierTripNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldSupplierName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldSupplierId, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addGroup(jPanelCenter4Layout.createSequentialGroup()
                        .addGroup(jPanelCenter4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelSupplierTripNo)
                            .addComponent(jLabelSupplierName)
                            .addComponent(jLabelSupplierId))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelCenter4Layout.setVerticalGroup(
            jPanelCenter4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCenter4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabelSupplierId)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldSupplierId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelSupplierName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldSupplierName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelSupplierTripNo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldSupplierTripNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelCenter.add(jPanelCenter4);

        getContentPane().add(jPanelCenter, java.awt.BorderLayout.CENTER);

        jButtonOk.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonOk.setText("确定");
        jButtonOk.setPreferredSize(new java.awt.Dimension(160, 37));
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanelBottom.add(jButtonOk);

        jButtonCancel.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonCancel.setText("取消");
        jButtonCancel.setPreferredSize(new java.awt.Dimension(160, 37));
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanelBottom.add(jButtonCancel);

        getContentPane().add(jPanelBottom, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        
        long selectedDateInMillis;
        // Check if date format valid
        if ((selectedDateInMillis=this.getSelectedDateInMillis())== -1) 
        {
            JOptionPane.showMessageDialog(this.parentPanel, "日期格式错误", "您输入的日期格式有错误。 [dd/mm/yyyy]", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if supplier selected.
        if (supplierObj == null) 
        {
            JOptionPane.showMessageDialog(this.parentPanel, "船户选择错误", "请您选择正确的船户资料。", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        this.parentPanel.wizardDialogClosed( supplierObj, selectedDateInMillis, Integer.parseInt(this.jTextFieldSupplierTripNo.getText())+1 );
        
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

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
                refreshVisitedSupplierList();
        }
    }//GEN-LAST:event_jButtonDatePickerActionPerformed

    private void jTextFieldCenter3FilterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldCenter3FilterKeyTyped
        
        int keyCode = (int) evt.getKeyChar();
        
        if (keyCode==10) {
            
            if (this.jTableSupplier.getRowCount() > 0) {
                this.jTableSupplier.requestFocus();
                this.jTableSupplier.setRowSelectionInterval(0, 0);
            }
        }
    }//GEN-LAST:event_jTextFieldCenter3FilterKeyTyped

    private void jTextFieldDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDateFocusGained
        this.jTextFieldDate.setBackground(Color.YELLOW);
    }//GEN-LAST:event_jTextFieldDateFocusGained

    private void jTextFieldDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDateFocusLost
        this.jTextFieldDate.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTextFieldDateFocusLost

    private void jTextFieldCenter3FilterFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldCenter3FilterFocusGained
        this.jTextFieldCenter3Filter.setBackground(Color.YELLOW);
    }//GEN-LAST:event_jTextFieldCenter3FilterFocusGained

    private void jTextFieldCenter3FilterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldCenter3FilterFocusLost
        this.jTextFieldCenter3Filter.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTextFieldCenter3FilterFocusLost

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
                this.jTextFieldCenter3Filter.requestFocus();
            }            
        }
        
        if ((lastAccessDate=getSelectedDateInMillis()) != 0)
            refreshVisitedSupplierList();
    }//GEN-LAST:event_jTextFieldDateKeyReleased

    private void jListCenter1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListCenter1ValueChanged
        
        ProgressModalDialog dialog = new ProgressModalDialog(null, true, "设置数据中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {    
                if (jListCenter1.getSelectedIndex()==-1)
                    return null;

                String selectedValue = jListCenter1.getSelectedValue().toString();
                String supplierId = selectedValue.substring(1, selectedValue.indexOf(']')).trim();

                supplierObj = SupplierDelegate.getInstance().getSupplierById(supplierId);
                jTextFieldSupplierId.setText(supplierObj==null? "错误" : supplierObj.getId());
                jTextFieldSupplierId.setCaretPosition(0);

                jTextFieldSupplierName.setText(supplierObj==null? "错误" : supplierObj.getName());
                jTextFieldSupplierName.setCaretPosition(0);

                jTextFieldSupplierTripNo.setText( 
                        String.valueOf(InTransactionDelegate.getInstance().getTripNoBySupplierIdAndDate(
                                supplierId, 
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
    }//GEN-LAST:event_jListCenter1ValueChanged

    private void jListCenter2ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListCenter2ValueChanged
                
        ProgressModalDialog dialog = new ProgressModalDialog(null, true, "设置数据中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {    
                if (jListCenter2.getSelectedIndex()==-1)
                    return null;

                String selectedValue = jListCenter2.getSelectedValue().toString();
                String supplierId = selectedValue.substring(1, selectedValue.indexOf(']')).trim();

                supplierObj = SupplierDelegate.getInstance().getSupplierById(supplierId);
                jTextFieldSupplierId.setText(supplierObj==null? "错误" : supplierObj.getId());
                jTextFieldSupplierId.setCaretPosition(0);

                jTextFieldSupplierName.setText(supplierObj==null? "错误" : supplierObj.getName());
                jTextFieldSupplierName.setCaretPosition(0);

                jTextFieldSupplierTripNo.setText( 
                        String.valueOf(InTransactionDelegate.getInstance().getTripNoBySupplierIdAndDate(
                                supplierId, 
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
    }//GEN-LAST:event_jListCenter2ValueChanged

    private void jListCenter2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jListCenter2KeyTyped
        int keyCode = (int) evt.getKeyChar();
        
        if (keyCode==10) 
        {
            if (this.jTextFieldSupplierId.getText().length() > 0)
                this.jButtonOk.doClick();
        }
    }//GEN-LAST:event_jListCenter2KeyTyped

    private void jListCenter1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jListCenter1KeyTyped
        int keyCode = (int) evt.getKeyChar();
        
        if (keyCode==10) 
        {
            if (this.jTextFieldSupplierId.getText().length() > 0)
                this.jButtonOk.doClick();
        }
    }//GEN-LAST:event_jListCenter1KeyTyped

    class TableSelectionListener implements ListSelectionListener
    {
        JTable jTable;
        
        TableSelectionListener(JTable table)
        {
            jTable = table;
        }
        
        public void valueChanged(ListSelectionEvent e) 
        {            
            ProgressModalDialog dialog = new ProgressModalDialog(null, true, "读取数据中...");            
            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception 
                {    
                    jListCenter1.clearSelection();
                    jListCenter2.clearSelection();

                    int selectedRow = jTableSupplier.getSelectedRow();
                    if (selectedRow == -1)
                        return null;

                    String cellValue = jTableSupplier.getValueAt(selectedRow, 0).toString().trim();
                    String selectedSupplierId = cellValue.substring(cellValue.indexOf("(")+1, cellValue.length()-1).trim();

                    supplierObj = supplierSelectionTableModel.getSupplierById(selectedSupplierId);
                    jTextFieldSupplierId.setText(supplierObj==null? "错误" : supplierObj.getId());
                    jTextFieldSupplierId.setCaretPosition(0);

                    jTextFieldSupplierName.setText(supplierObj==null? "错误" : supplierObj.getName());
                    jTextFieldSupplierName.setCaretPosition(0);

                    jTextFieldSupplierTripNo.setText( 
                            String.valueOf(InTransactionDelegate.getInstance().getTripNoBySupplierIdAndDate(
                                    selectedSupplierId, 
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
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDatePicker;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabelDatePicker;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelSupplierId;
    private javax.swing.JLabel jLabelSupplierName;
    private javax.swing.JLabel jLabelSupplierTripNo;
    private javax.swing.JList jListCenter1;
    private javax.swing.JList jListCenter2;
    private javax.swing.JPanel jPanelBottom;
    private javax.swing.JPanel jPanelCenter;
    private javax.swing.JPanel jPanelCenter1;
    private javax.swing.JPanel jPanelCenter2;
    private javax.swing.JPanel jPanelCenter3;
    private javax.swing.JPanel jPanelCenter4;
    private javax.swing.JPanel jPanelDatePicker;
    private javax.swing.JPanel jPanelStatus;
    private javax.swing.JPanel jPanelTop;
    private javax.swing.JScrollPane jScrollPaneCenter1;
    private javax.swing.JScrollPane jScrollPaneCenter2;
    private javax.swing.JScrollPane jScrollPaneCenter3;
    private javax.swing.JTable jTableSupplier;
    private javax.swing.JTextField jTextFieldCenter3Filter;
    private javax.swing.JTextField jTextFieldDate;
    private javax.swing.JTextField jTextFieldSupplierId;
    private javax.swing.JTextField jTextFieldSupplierName;
    private javax.swing.JTextField jTextFieldSupplierTripNo;
    // End of variables declaration//GEN-END:variables
}

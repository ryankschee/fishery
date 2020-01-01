package com.ryanworks.fishery.client.ui.inbound;

import com.ryanworks.fishery.client.delegate.InTransactionDelegate;
import com.ryanworks.fishery.client.ui.inbound.listener.InboundSearchDialogListener;
import com.ryanworks.fishery.client.ui.shared.ProgressModalDialog;
import com.ryanworks.fishery.client.ui.shared.table.InTransactionTableModel;
import com.ryanworks.fishery.shared.bean.InTransactionBean;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import com.ryanworks.fishery.shared.util.MyLogger;
import com.ryanworks.fishery.shared.util.SwingUtil;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

public class InboundSearchDialog 
    extends javax.swing.JDialog 
{   
    public static long lastAccessDate = 0L;
    
    private final int[] DAYS_IN_MONTH = {31,29,31,30,31,30,31,31,30,31,30,31};
    
    private MyDatePicker datePicker;
    private InboundSearchDialogListener listener;
    
    private HashMap<String, InTransactionBean> resultMap = new HashMap();
    private InTransactionTableModel transactionTableModel = new InTransactionTableModel(resultMap);
    private TableRowSorter<InTransactionTableModel> sorter;
        
    private boolean uiInitialized = false;
    private boolean fillingForm = false;
    
    public InboundSearchDialog(java.awt.Frame parent, boolean modal, InboundSearchDialogListener listener) 
    {
        super(parent, modal);
        this.listener = listener;
        this.uiInitialized = false;
        
        initComponents();
        myInit();
    }
        
    private void myInit()
    {
        if (lastAccessDate==0L)
            lastAccessDate = Calendar.getInstance().getTimeInMillis();
        
        this.jTextFieldDate.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format( lastAccessDate ) );
        this.jTextFieldDate.requestFocus();
        this.jTextFieldDate.setCaretPosition(0);
        this.jTextFieldDate.select(0, 2);
        
        sorter = new TableRowSorter<InTransactionTableModel>(transactionTableModel);        
        this.jTableResult.setRowSorter(sorter);
        this.jTableResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        
        
        this.jTableResult.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableResult.setRowHeight(30);
        this.jTableResult.getColumn("id").setMinWidth( 50 );
        this.jTableResult.getColumn("id").setMaxWidth( 50 );
                
        Calendar now = Calendar.getInstance();
        this.jComboBoxMonth.setSelectedIndex(now.get(Calendar.MONTH));
        this.jComboBoxYear.setSelectedItem(String.valueOf(now.get(Calendar.YEAR)))  ;
        
        //Whenever filterText changes, invoke newFilter.
        this.jTextFieldFilter.getDocument().addDocumentListener(
            new DocumentListener() {
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
        
        this.jRadioButtonByMonth.setSelected(false);
        this.jRadioButtonByDay.setSelected(false);
        
        fillForm();
        
        this.uiInitialized = true;
    }
    
    private void fillForm()
    {        
        this.fillingForm = true;
        
        if (this.jRadioButtonByMonth.isSelected())
        {
            int monthIndex = this.jComboBoxMonth.getSelectedIndex();
            int year = Integer.parseInt(this.jComboBoxYear.getSelectedItem().toString());
            
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            
            from.set(Calendar.YEAR, year);
            from.set(Calendar.MONTH, monthIndex);
            from.set(Calendar.DATE, 1);
            from.set(Calendar.HOUR_OF_DAY, 0);
            from.set(Calendar.MINUTE, 1);
            
            to.set(Calendar.YEAR, year);
            to.set(Calendar.MONTH, monthIndex);
            to.set(Calendar.DATE, to.getActualMaximum(Calendar.DATE));
            to.set(Calendar.HOUR_OF_DAY, 23);
            to.set(Calendar.MINUTE, 59);
                        
            List<InTransactionBean> transactionList = 
                    InTransactionDelegate.getInstance().getTransactionListByDateRange(from.getTimeInMillis(), to.getTimeInMillis(), false);
            this.transactionTableModel.removeAll();

            if (transactionList != null && transactionList.size() > 0) {
                Collections.sort(transactionList);
                for(InTransactionBean bean : transactionList)
                {
                    this.transactionTableModel.addBean(bean);
                }
            }
        }
        else
        if (this.jRadioButtonByDay.isSelected())
        {
            if (this.getSelectedDateInMillis() <= 0) {
                return;
            }

            List<InTransactionBean> transactionList = 
                    InTransactionDelegate.getInstance().getTransactionsByDate(this.getSelectedDateInMillis(), false);
            this.transactionTableModel.removeAll();

            if (transactionList != null && transactionList.size() > 0) {
                Collections.sort(transactionList);
                for(InTransactionBean bean : transactionList)
                {
                    this.transactionTableModel.addBean(bean);
                }
            }
        }
        
        this.fillingForm = false;
    }
    
    private void newFilter() 
    {
        List<RowFilter<InTransactionTableModel, Object>> filters = new ArrayList<>(2);
        
        RowFilter<InTransactionTableModel, Object> rf = null;
        RowFilter<InTransactionTableModel, Object> rf1 = null;
        RowFilter<InTransactionTableModel, Object> rf2 = null;
        //If current expression doesn't parse, don't update.
        try {
            rf1 = RowFilter.regexFilter(this.jTextFieldFilter.getText(), 1);
            rf2 = RowFilter.regexFilter(this.jTextFieldFilter.getText(), 2);
            
            filters.add(rf1);
            filters.add(rf2);
            
            rf = RowFilter.andFilter(filters);
            
        } catch (java.util.regex.PatternSyntaxException e) {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            return;
        }
        sorter.setRowFilter(rf1);
    }
    
    class TableSelectionListener 
        implements ListSelectionListener
    {
        JTable jTable;
        
        TableSelectionListener(JTable table)
        {
            jTable = table;
        }
        
        public void valueChanged(ListSelectionEvent e) 
        {
            int selectedRow = InboundSearchDialog.this.jTableResult.getSelectedRow();
            if (selectedRow == -1)
                return;
            
            // Transaction ID on table cell
            String cellValue = InboundSearchDialog.this.jTableResult.getValueAt(selectedRow, 0).toString().trim();
        }
    }

    private long getSelectedDateInMillis()
    {
        String dateInString = this.jTextFieldDate.getText().trim();
        String dayInString = dateInString.substring(0, dateInString.indexOf("/")).trim();
        String monthInString = dateInString.substring(dateInString.indexOf("/")+1, dateInString.lastIndexOf("/")).trim();
        String yearInString = dateInString.substring(dateInString.lastIndexOf("/")+1).trim();
                
        try {
            if (dayInString.length()==0 || monthInString.length()==0 || yearInString.length()==0)
                return -1;
            
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
            
            return calendar.getTimeInMillis();
        }
        catch (NumberFormatException e) {
            MyLogger.logError(getClass(), "NumberFormatException: " + e.getMessage());
            return -1;
        }
        catch (Exception e) {
            MyLogger.logError(getClass(), "Exception: " + e.getMessage());
            return -1;
        }
    }
    
    private void showResult() {
        if (this.uiInitialized == false || this.fillingForm == true)
            return;
        if (this.jRadioButtonByDay.isSelected() && this.getSelectedDateInMillis()<=0) {
            return;
        }
        
        ProgressModalDialog dialog = new ProgressModalDialog(null, true, "读取数据中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {    
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
    } // .end of showResult
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanelTop = new javax.swing.JPanel();
        jPanelSelection = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jRadioButtonByMonth = new javax.swing.JRadioButton();
        jButtonDatePicker1 = new javax.swing.JButton();
        jComboBoxYear = new javax.swing.JComboBox();
        jComboBoxMonth = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jRadioButtonByDay = new javax.swing.JRadioButton();
        jButtonDatePicker = new javax.swing.JButton();
        jTextFieldDate = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldFilter = new javax.swing.JTextField();
        jScrollPane = new javax.swing.JScrollPane();
        jTableResult = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButtonOpen = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("搜寻来货单据");
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        jPanelTop.setLayout(new java.awt.BorderLayout());

        jPanelSelection.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "选择", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanelSelection.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.GridLayout(2, 1));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        buttonGroup1.add(jRadioButtonByMonth);
        jRadioButtonByMonth.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadioButtonByMonth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonByMonthItemStateChanged(evt);
            }
        });
        jPanel4.add(jRadioButtonByMonth);

        jButtonDatePicker1.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonDatePicker1.setText("月份");
        jButtonDatePicker1.setPreferredSize(new java.awt.Dimension(100, 45));
        jPanel4.add(jButtonDatePicker1);

        jComboBoxYear.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jComboBoxYear.setMaximumRowCount(20);
        jComboBoxYear.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025" }));
        jComboBoxYear.setPreferredSize(new java.awt.Dimension(120, 40));
        jComboBoxYear.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxYearItemStateChanged(evt);
            }
        });
        jPanel4.add(jComboBoxYear);

        jComboBoxMonth.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jComboBoxMonth.setMaximumRowCount(12);
        jComboBoxMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" }));
        jComboBoxMonth.setPreferredSize(new java.awt.Dimension(120, 40));
        jComboBoxMonth.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxMonthItemStateChanged(evt);
            }
        });
        jPanel4.add(jComboBoxMonth);

        jPanel5.add(jPanel4);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        buttonGroup1.add(jRadioButtonByDay);
        jRadioButtonByDay.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadioButtonByDay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonByDayItemStateChanged(evt);
            }
        });
        jPanel1.add(jRadioButtonByDay);

        jButtonDatePicker.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonDatePicker.setText("日期");
        jButtonDatePicker.setPreferredSize(new java.awt.Dimension(100, 45));
        jButtonDatePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDatePickerActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonDatePicker);

        jTextFieldDate.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldDate.setPreferredSize(new java.awt.Dimension(245, 40));
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
        jPanel1.add(jTextFieldDate);

        jPanel5.add(jPanel1);

        jPanelSelection.add(jPanel5, java.awt.BorderLayout.CENTER);

        jButton1.setFont(new java.awt.Font("KaiTi", 0, 36)); // NOI18N
        jButton1.setText("搜索");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanelSelection.add(jButton1, java.awt.BorderLayout.LINE_END);

        jPanelTop.add(jPanelSelection, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("过滤");
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 45));
        jPanel2.add(jLabel1, java.awt.BorderLayout.LINE_START);

        jTextFieldFilter.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldFilter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldFilterFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldFilterFocusLost(evt);
            }
        });
        jTextFieldFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldFilterKeyTyped(evt);
            }
        });
        jPanel2.add(jTextFieldFilter, java.awt.BorderLayout.CENTER);

        jPanelTop.add(jPanel2, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanelTop, java.awt.BorderLayout.PAGE_START);

        jTableResult.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jTableResult.setModel(transactionTableModel);
        jTableResult.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableResultMouseClicked(evt);
            }
        });
        jScrollPane.setViewportView(jTableResult);

        getContentPane().add(jScrollPane, java.awt.BorderLayout.CENTER);

        jButtonOpen.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonOpen.setText("打开");
        jButtonOpen.setPreferredSize(new java.awt.Dimension(100, 45));
        jButtonOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonOpen);

        jButtonCancel.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonCancel.setText("取消");
        jButtonCancel.setPreferredSize(new java.awt.Dimension(100, 45));
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonCancel);

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDateFocusGained
        this.jTextFieldDate.setBackground(Color.YELLOW);
        this.fillForm();
    }//GEN-LAST:event_jTextFieldDateFocusGained

    private void jTextFieldDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDateFocusLost
        this.jTextFieldDate.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTextFieldDateFocusLost

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenActionPerformed
        
        int selectedRowIndex = this.jTableResult.getSelectedRow();
        if (selectedRowIndex == -1) 
        {
            return;
        }
        
        Object transactionIdObj = this.jTableResult.getValueAt(selectedRowIndex, 0);
        if (transactionIdObj == null)
        {
            return;
        }
            
        String transactionId = transactionIdObj.toString();
        InTransactionBean transactionBean = InTransactionDelegate.getInstance().getTransactionById(transactionId);
        if (transactionBean == null)
        {
            return;
        }
        
        this.listener.searchDialogClosed(transactionBean);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonOpenActionPerformed

    private void jTextFieldDateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDateKeyReleased
        int keyCode = (int) evt.getKeyChar();
        
        // Set day selected
        if (uiInitialized)
            this.jRadioButtonByDay.setSelected(true);
        
        // If user press ENTER, response.
        if (keyCode==10) {
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
                this.jTextFieldFilter.requestFocus();
                this.jTextFieldFilter.selectAll();
            }            
        }
        
        if ((lastAccessDate=this.getSelectedDateInMillis())!=0) {
            this.showResult();
        }
    }//GEN-LAST:event_jTextFieldDateKeyReleased

    private void jTableResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableResultMouseClicked
        if (evt.getClickCount() > 1)
            this.jButtonOpen.doClick();
    }//GEN-LAST:event_jTableResultMouseClicked

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
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);

            this.jTextFieldDate.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format(date.getTime()) );

            // Request focus back for Date jTextField
            this.jTextFieldDate.requestFocus();
            this.jTextFieldDate.setCaretPosition(0);
            this.jTextFieldDate.select(0, 2);

            this.showResult();       
        }
    }//GEN-LAST:event_jButtonDatePickerActionPerformed

    private void jComboBoxYearItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxYearItemStateChanged
        if (uiInitialized) 
            this.jRadioButtonByMonth.setSelected(true);
        
        this.showResult();
    }//GEN-LAST:event_jComboBoxYearItemStateChanged

    private void jComboBoxMonthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxMonthItemStateChanged
        
        if (uiInitialized) 
            this.jRadioButtonByMonth.setSelected(true);
        
        this.showResult();        
    }//GEN-LAST:event_jComboBoxMonthItemStateChanged

    private void jRadioButtonByMonthItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonByMonthItemStateChanged
        this.showResult();      
    }//GEN-LAST:event_jRadioButtonByMonthItemStateChanged

    private void jRadioButtonByDayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonByDayItemStateChanged
        this.showResult();
    }//GEN-LAST:event_jRadioButtonByDayItemStateChanged

    private void jTextFieldFilterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFilterKeyTyped
        this.showResult();
    }//GEN-LAST:event_jTextFieldFilterKeyTyped

    private void jTextFieldFilterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldFilterFocusLost
        this.jTextFieldFilter.setBackground(Color.WHITE);
    }//GEN-LAST:event_jTextFieldFilterFocusLost

    private void jTextFieldFilterFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldFilterFocusGained
        this.jTextFieldFilter.setBackground(Color.YELLOW);
    }//GEN-LAST:event_jTextFieldFilterFocusGained

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.showResult();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDatePicker;
    private javax.swing.JButton jButtonDatePicker1;
    private javax.swing.JButton jButtonOpen;
    private javax.swing.JComboBox jComboBoxMonth;
    private javax.swing.JComboBox jComboBoxYear;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelSelection;
    private javax.swing.JPanel jPanelTop;
    private javax.swing.JRadioButton jRadioButtonByDay;
    private javax.swing.JRadioButton jRadioButtonByMonth;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTable jTableResult;
    private javax.swing.JTextField jTextFieldDate;
    private javax.swing.JTextField jTextFieldFilter;
    // End of variables declaration//GEN-END:variables
}

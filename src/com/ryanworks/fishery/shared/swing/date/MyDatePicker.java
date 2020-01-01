 package com.ryanworks.fishery.shared.swing.date;
 
import com.ryanworks.fishery.shared.util.SwingUtil;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
 
public class MyDatePicker extends JDialog
{    
    public static final Locale LOCALE = new Locale("may", "MY");
    public static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(2, LOCALE);
    public static final DateFormat TIME_HHMMSS_FORMAT = DateFormat.getTimeInstance(2, LOCALE);
    public static final DateFormat TIME_HHMM_FORMAT = DateFormat.getTimeInstance(3, LOCALE);
    
    public static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final DateFormat FULL_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    
    private DatePickerTableModel tableModel = new DatePickerTableModel();
    private int year = 0;
    private int month = 0;
    private int day = 0;
    private boolean retOk = false;
    private int row = 0;
    private int col = 0;
    private JButton jButton_ok;
    private JComboBox jComboBox_month;
    private JPanel jPanel;
    private JScrollPane jScrollPane;
    private JSpinner jSpinner_year;
    private JTable jTable;
 
    public MyDatePicker(Frame parent) {
        super(parent, true);
        
        initComponents();
        initOthers();
    }
  
    /**
     * Update current day value, month value and year value into class-local 
     * variables. 
     */
    private void dayChanged() {
        if ((this.row == -1) || (this.col == -1))
            return;
     
        try {
            String value = this.tableModel.getValueAt(this.row, this.col).toString();
            if (value.trim().length() == 0) 
                return;
 
            this.day = Integer.parseInt(value);
            this.month = this.jComboBox_month.getSelectedIndex();
            this.year = Integer.parseInt(this.jSpinner_year.getValue().toString());
        }
        catch (NumberFormatException e) {
            e.printStackTrace();    
        }
    }
 
    /**
     * Return currently selected date as Calendar object.
     * 
     * @return 
     */
    public Calendar getSelectedDate() {
        if (!isOk()) 
            return null;
 
        // Get latest day/month/year values. 
        dayChanged();
        
        Calendar c = Calendar.getInstance();
        c.set(this.year, this.month, this.day);
 
        return c;
    }
 
    /**
     * Return date value in String format YYYY-MM-DD
     */
    public String getDateInString() {
        Calendar c = getSelectedDate();
        if (c == null) 
            return "";
 
        StringBuilder dateInStr = new StringBuilder();
        this.month = (c.get(Calendar.MONTH) + 1);
        this.day = c.get(Calendar.DAY_OF_MONTH);
 
        dateInStr.append( this.jSpinner_year.getValue() );
        dateInStr.append( "-" );
        dateInStr.append( this.month < 10 ? "0" + this.month : Integer.valueOf(this.month) );
        dateInStr.append( "-" );
        dateInStr.append( this.day < 10 ? "0" + this.day : Integer.valueOf(this.day) );
 
        return dateInStr.toString();
    }
 
    /**
     * Initialize components.
     */
    private void initOthers() {
        clearDayValues();
 
        this.jTable.setModel(this.tableModel);
        this.jTable.setCellSelectionEnabled(true);
        this.jTable.setSelectionMode(0);

        jComboBox_month.setMaximumRowCount(12);    
    
        Calendar now = Calendar.getInstance();
        //this.jSpinner_year.setValue(Integer.valueOf(now.get(1)));
        this.jComboBox_month.setSelectedIndex(now.get(2));
        
        repaintTable();
 
        SwingUtil.centerDialogOnScreen(this);
        this.jSpinner_year.grabFocus();
    }
 
    /**
     * Check if there is valid date values being selected. 
     */
    public boolean isOk() {
        return (this.retOk) && (this.jTable.getSelectedColumn() != -1) && (this.jTable.getSelectedRow() != -1);
    }
 
    /**
     * Reload current DatePicker display. 
     */
    private void repaintTable() {
        Calendar current = Calendar.getInstance();
  
        //Date spinnerValue = (Date) this.jSpinner_year.getValue();
        //Calendar spinnerCal = Calendar.getInstance();        
        //spinnerCal.setTimeInMillis(spinnerValue.getTime());
        
        // Set current date to LAST DAY of the month (e.g. 31 for January).
        current.set(Calendar.YEAR, ((Integer)this.jSpinner_year.getValue()).intValue());
        current.set(Calendar.MONTH, this.jComboBox_month.getSelectedIndex() + 1);
        current.set(Calendar.DAY_OF_MONTH, 0); 
 
        int totalDaysInMonth = current.get(Calendar.DAY_OF_MONTH);
 
        // Set it back to 1st day of Month
        current.set(Calendar.DAY_OF_MONTH, 1);
 
        int currentDayInWeek = current.get(Calendar.DAY_OF_WEEK);
        int currentRowIndex = 0;
 
        // Clear day values in table
        clearDayValues();
        
        // Start to write day value back into table cells based on currently
        // selected month/year.
        for (int i = 1; i <= totalDaysInMonth; i++) {
            this.tableModel.setValueAt(Integer.valueOf(i), currentRowIndex, currentDayInWeek - 1);
            currentDayInWeek++;
       
            if (currentDayInWeek > 7) {
                currentDayInWeek = 1;
                currentRowIndex++;
            }
        }
    
        // Update day/month/year values
        dayChanged();
   }
 
    /**
     * Remove all day values from table. 
     */
    private void clearDayValues() {
        
        this.tableModel.clear();
        for (int i = 0; i < 6; i++) {
            java.util.List rowSet = new ArrayList();
            for (int j = 0; j < 7; j++)
                rowSet.add("");
                this.tableModel.addRow(rowSet);
        }
    }
 
    /**
     * Check if any day selected.
     */
    protected boolean isDaySelected() {
        int selectedRow = this.jTable.getSelectedRow();
        int selectedCol = this.jTable.getSelectedColumn();
 
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select day.", "No day selected", 0); 
            return false;
        }
 
        this.row = selectedRow;
        this.col = selectedCol;
 
        String selectedValue = this.jTable.getValueAt(this.row, this.col).toString();
 
        if (selectedValue.trim().length() == 0) {
            JOptionPane.showMessageDialog(this, "Please select day.", "No day selected", 0); 
            return false;
        }
 
        dayChanged();
 
        return true;
    }
 
    /**
     * Display DatePicker dialog on user command. 
     */
    public void showDialog() 
    {
        this.retOk = false;
        setVisible(true);
    }
 
    private void initComponents() 
    {
        // Instantiate each UI components
        this.jPanel = new JPanel();
        this.jComboBox_month = new JComboBox();
        this.jSpinner_year = new JSpinner();
        this.jScrollPane = new JScrollPane();
        this.jTable = new JTable();
        this.jButton_ok = new JButton();

        // Set dialog's properties
        setDefaultCloseOperation(2);
        setTitle("Date Picker");
        setAlwaysOnTop(true);
        setModal(true);
        setName("datePicker");
        setResizable(false);
        
        this.jPanel.setLayout(new AbsoluteLayout()); 
        this.jPanel.setPreferredSize(new Dimension(300, 310));
        this.jPanel.setBackground(new Color(51, 51, 51));
        
        this.jComboBox_month.setFont(new Font("Tahoma", 0, 16));
        //this.jComboBox_month.setBackground(new Color(51, 51, 51));
        this.jComboBox_month.setModel(new DefaultComboBoxModel(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));
        this.jComboBox_month.setNextFocusableComponent(this.jSpinner_year);
        this.jComboBox_month.setPreferredSize(new Dimension(27, 20));
        this.jComboBox_month.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                MyDatePicker.this.stateChanged(evt);
            }
        });
        this.jComboBox_month.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                MyDatePicker.this.jComboBox_monthKeyTyped(evt);
            }
        });
        this.jPanel.add(this.jComboBox_month, new AbsoluteConstraints(10, 20, 120, 32));
         
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.YEAR, -50);
        Date startDate = cal.getTime();
        cal.add(Calendar.YEAR, 100);
        Date endDate = cal.getTime();

        this.jSpinner_year.setFont(new Font("Tahoma", 0, 16));
        this.jSpinner_year.setBackground(new Color(51, 51, 51));
        //this.jSpinner_year.setModel(new SpinnerDateModel(now, startDate, endDate, Calendar.YEAR));
        this.jSpinner_year.setModel(new SpinnerNumberModel(Calendar.getInstance().get(Calendar.YEAR), 1900, 2100, 1));
        this.jSpinner_year.setNextFocusableComponent(this.jTable);
        this.jSpinner_year.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                MyDatePicker.this.yearStateChanged(evt);
            }
            });
        this.jSpinner_year.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                MyDatePicker.this.jSpinner_yearKeyTyped(evt);
            }
        });
        this.jPanel.add(this.jSpinner_year, new AbsoluteConstraints(140, 20, 100, 32));
 
        this.jScrollPane.setHorizontalScrollBarPolicy(31);
        this.jScrollPane.setVerticalScrollBarPolicy(21);
        this.jTable.setFont(new Font("Tahoma", 0, 16));
        this.jTable.setPreferredSize(new Dimension(300, 300));
        this.jTable.setModel(
                new DefaultTableModel(
                        new Object[][] { { null, null, null, null }, 
                                         { null, null, null, null }, 
                                         { null, null, null, null }, 
                                         { null, null, null, null } },                        
                        new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        jTable.setDefaultRenderer(String.class, centerRenderer);
        jTable.setDefaultRenderer(Integer.class, centerRenderer);
        jTable.setRowHeight(30);
        
        this.jTable.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                MyDatePicker.this.jTableKeyTyped(evt);
            }
        });
        this.jTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                MyDatePicker.this.jTableMouseClicked(evt);
            }
        });
        this.jScrollPane.setViewportView(this.jTable); 
        this.jPanel.add(this.jScrollPane, new AbsoluteConstraints(10, 55, 280, 207));
 
        this.jButton_ok.setText("OK");
        this.jButton_ok.setFont(new Font("Tahoma", 0, 14));
        //this.jButton_ok.setBackground(new Color(51, 51, 51));
        this.jButton_ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                MyDatePicker.this.jButton_okActionPerformed(evt);
            }
        });
        this.jPanel.add(this.jButton_ok, new AbsoluteConstraints(10, 265, 280, 36));
 
        getContentPane().add(this.jPanel, "Center"); 
        pack();
    }
 
    private void jButton_okActionPerformed(ActionEvent evt) {
        if (!isDaySelected()) {
            return;
        }
     
        setVisible(false);
        dispose();
        this.retOk = true;
    }
    
    private void jTableKeyTyped(KeyEvent evt) {
        if (evt.getKeyChar() == '\n') {
            int c = this.jTable.getSelectedColumn();
            int r = this.jTable.getSelectedRow() - 1;
            if (r == -1) {
                return;
            }
       
            this.jTable.setColumnSelectionInterval(c, c);
            this.jTable.setRowSelectionInterval(r, r);
 
            if (this.tableModel.getValueAt(r, c).toString().equals("")) {
                return;
            }
        }
     
        if (!isDaySelected()) {
            return;
        }
 
        if (evt.getKeyChar() == '\n') {
            setVisible(false);
            dispose();
            this.retOk = true;
        }
    }
 
    private void jSpinner_yearKeyTyped(KeyEvent evt) {
        if (evt.getKeyChar() == '\n')
            this.jTable.grabFocus();
    }
 
    private void jComboBox_monthKeyTyped(KeyEvent evt) {
        if (evt.getKeyChar() == '\n')
            this.jSpinner_year.grabFocus();
    }
 
    private void jTableMouseClicked(MouseEvent evt) {
        this.row = this.jTable.getSelectedRow();    
        this.col = this.jTable.getSelectedColumn();
        dayChanged();
        
        if (evt.getClickCount() >= 2)
            this.jButton_ok.doClick();
    }
 
    private void yearStateChanged(ChangeEvent evt) {
        repaintTable();
    }
 
    private void stateChanged(ItemEvent evt) {
        repaintTable();
    }
}

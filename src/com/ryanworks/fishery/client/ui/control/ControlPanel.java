package com.ryanworks.fishery.client.ui.control;

import com.ryanworks.fishery.client.delegate.*;
import com.ryanworks.fishery.client.print.ControlPDFBoxPrinter;
import com.ryanworks.fishery.client.print.RootPDFBoxPrinter;
import com.ryanworks.fishery.client.ui.JFrameMain;
import com.ryanworks.fishery.client.ui.control.listener.*;
import com.ryanworks.fishery.client.ui.shared.ProgressModalDialog;
import com.ryanworks.fishery.client.ui.shared.table.*;
import com.ryanworks.fishery.shared.bean.*;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import com.ryanworks.fishery.shared.swing.listener.JDateFieldFocusListener;
import com.ryanworks.fishery.shared.swing.listener.JTextFieldFocusListener;
import com.ryanworks.fishery.shared.swing.table.ButtonColumn;
import com.ryanworks.fishery.shared.util.MyLogger;
import com.ryanworks.fishery.shared.util.SwingUtil;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableRowSorter;

public class ControlPanel 
    extends javax.swing.JPanel
    implements ListSelectionListener, ItemEditorDialogListener, SupplierEditorDialogListener, CustomerEditorDialogListener,
               SupplierChequeEditorDialogListener, SupplierFuelEditorDialogListener, SupplierMiscEditorDialogListener, SupplierCashEditorDialogListener, SupplierWithdrawalEditorDialogListener, CustomerPaymentEditorDialogListener, ItemPrefixDialogListener
{
    private final int[] DAYS_IN_MONTH = {31,29,31,30,31,30,31,31,30,31,30,31};
    private final DecimalFormat currencyFormatter = new DecimalFormat("0.00");
    
    private final int TAB1 = 0;
    private final int TAB2 = 1;
    private final int TAB3 = 2;
    private final int TAB4 = 3;
    private final int TAB5 = 4;
    private final int TAB6 = 5;
    private final int TAB7 = 6;
    private final int TAB8 = 7;
    private final int TAB9 = 8;
    private final int TAB10 = 9;
    private final int TAB11 = 10;
    private final int TAB12 = 11;
    private final int TAB13 = 12;
    private final int TAB14 = 13;
    
    private final JFrame parent;
    private MyDatePicker datePicker;
    private final String saveToPath = RootPDFBoxPrinter.PDF_PRINTOUT_FOLDER;
    
    private DefaultListModel listModelItemTab1Category;
    private DefaultListModel listModelItemTab2Category;
    private DefaultListModel listModelItemTab2Item;
    private DefaultListModel listModelSupplierTab2Supplier;
    private DefaultListModel listModelSupplierTab3Supplier;
    private DefaultListModel listModelSupplierTab4Supplier;
    private DefaultListModel listModelSupplierTab5Supplier;
    private DefaultListModel listModelSupplierTab6Supplier;
    private DefaultListModel listModelSupplierTab7Supplier;
    private DefaultListModel<CustomerBean> listModelCustomerTab2Customer;
    private DefaultListModel listModelReportTab2Category;
    private DefaultListModel listModelReportTab2Item;
    private DefaultListModel listModelReportTab2ItemSelection;
    
    private ItemBean tab2_selectedItemObj;
    
    private final HashMap<String, ItemBean> itemTab1ItemResultMap = new HashMap();
    private final ItemTableModel itemTab1ItemTableModel = new ItemTableModel(itemTab1ItemResultMap);
    
    private final HashMap<String, InTransactionLineBean> itemTab2PriceChangeResultMap = new HashMap();
    private final ItemPriceChangeTableModel itemTab2PriceChangeTableModel = new ItemPriceChangeTableModel(itemTab2PriceChangeResultMap);
    
    private final HashMap<String, CustomerBean> customerTab1CustomerResultMap = new HashMap();
    private final CustomerTableModel customerTab1CustomerTableModel = new CustomerTableModel(customerTab1CustomerResultMap);
    private TableRowSorter<CustomerTableModel> customerTab1CustomerTableSorter;
    
    private final HashMap<String, SupplierBean> supplierTab1SupplierResultMap = new HashMap();
    private final SupplierTableModel supplierTab1SupplierTableModel = new SupplierTableModel(supplierTab1SupplierResultMap);
    private TableRowSorter<SupplierTableModel> supplierTab1SupplierTableSorter;
    
    private final HashMap<String, SupplierChequeBean> supplierTab2ChequeResultMap = new HashMap();
    private final SupplierChequeTableModel supplierTab2ChequeTableModel = new SupplierChequeTableModel(supplierTab2ChequeResultMap);
    private TableRowSorter<SupplierChequeTableModel> supplierTab2ChequeTableSorter;
    
    private final HashMap<String, SupplierFuelBean> supplierTab3FuelResultMap = new HashMap();
    private final SupplierFuelTableModel supplierTab3FuelTableModel = new SupplierFuelTableModel(supplierTab3FuelResultMap);
    private TableRowSorter<SupplierFuelTableModel> supplierTab3FuelTableSorter;
    
    private final HashMap<String, SupplierMiscBean> supplierTab4MiscResultMap = new HashMap();
    private final SupplierMiscTableModel supplierTab4MiscTableModel = new SupplierMiscTableModel(supplierTab4MiscResultMap);
    private TableRowSorter<SupplierMiscTableModel> supplierTab4MiscTableSorter;
    
    private final HashMap<String, SupplierCashBean> supplierTab5CashResultMap = new HashMap();
    private final SupplierCashTableModel supplierTab5CashTableModel = new SupplierCashTableModel(supplierTab5CashResultMap);
    private TableRowSorter<SupplierCashTableModel> supplierTab5CashTableSorter;
            
    private final HashMap<String, SupplierWithdrawalBean> supplierTab6WithdrawalResultMap = new HashMap();
    private final SupplierWithdrawalTableModel supplierTab6WithdrawalTableModel = new SupplierWithdrawalTableModel(supplierTab6WithdrawalResultMap);
    private TableRowSorter<SupplierWithdrawalTableModel> supplierTab6WithdrawalTableSorter;
    
    private final HashMap<String, InTransactionBean> supplierTab7StockResultMap = new HashMap();
    private final InTransactionTableModel supplierTab7StockTableModel = new InTransactionTableModel(supplierTab7StockResultMap);
    private TableRowSorter<InTransactionTableModel> supplierTab7StockTableSorter;
    
    private final HashMap<String, SupplierChequeBean> supplierTab7ChequeResultMap = new HashMap();
    private final SupplierChequeTableModel supplierTab7ChequeTableModel = new SupplierChequeTableModel(supplierTab7ChequeResultMap);
    private TableRowSorter<SupplierChequeTableModel> supplierTab7ChequeTableSorter;
    
    private final HashMap<String, SupplierFuelBean> supplierTab7FuelResultMap = new HashMap();
    private final SupplierFuelTableModel supplierTab7FuelTableModel = new SupplierFuelTableModel(supplierTab7FuelResultMap);
    private TableRowSorter<SupplierFuelTableModel> supplierTab7FuelTableSorter;
    
    private final HashMap<String, SupplierMiscBean> supplierTab7MiscResultMap = new HashMap();
    private final SupplierMiscTableModel supplierTab7MiscTableModel = new SupplierMiscTableModel(supplierTab7MiscResultMap);
    private TableRowSorter<SupplierMiscTableModel> supplierTab7MiscTableSorter;
    
    private final HashMap<String, SupplierCashBean> supplierTab7CashResultMap = new HashMap();
    private final SupplierCashTableModel supplierTab7CashTableModel = new SupplierCashTableModel(supplierTab7CashResultMap);
    private TableRowSorter<SupplierCashTableModel> supplierTab7CashTableSorter;
    
    private final HashMap<String, InTransactionBean> supplierTab7SavingResultMap = new HashMap();
    private final InTransactionSavingTableModel supplierTab7SavingTableModel = new InTransactionSavingTableModel(supplierTab7SavingResultMap);
    private TableRowSorter<InTransactionSavingTableModel> supplierTab7SavingTableSorter;
    
    private final HashMap<String, SupplierWithdrawalBean> supplierTab7WithdrawalResultMap = new HashMap();
    private final SupplierWithdrawalTableModel supplierTab7WithdrawalTableModel = new SupplierWithdrawalTableModel(supplierTab7WithdrawalResultMap);
    private TableRowSorter<SupplierWithdrawalTableModel> supplierTab7WithdrawalTableSorter;
       
    private final HashMap<String, SupplierSalesCheckBean> supplierTab8SalesCheckResultMap = new HashMap();
    private final SupplierSalesCheckTableModel supplierTab8SalesCheckTableModel = new SupplierSalesCheckTableModel(supplierTab8SalesCheckResultMap);
    private TableRowSorter<SupplierSalesCheckTableModel> supplierTab8SalesCheckTableSorter;
    
    private final HashMap<String, CustomerPaymentBean> customerTab2PaymentResultMap = new HashMap();
    private final CustomerPaymentTableModel customerTab2PaymentTableModel = new CustomerPaymentTableModel(customerTab2PaymentResultMap);
    private TableRowSorter<CustomerPaymentTableModel> customerTab2PaymentTableSorter;
    
    private final HashMap<String, CustomerTransactionBean> customerTab2SalesResultMap = new HashMap();
    private final CustomerMonthlyTransactionTableModel customerTab2SalesTableModel = new CustomerMonthlyTransactionTableModel(customerTab2SalesResultMap);
    private TableRowSorter<CustomerMonthlyTransactionTableModel> customerTab2SalesTableSorter;
            
    private final HashMap<String, CustomerSalesCheckBean> customerTab4SalesCheckResultMap = new HashMap();
    private final CustomerSalesCheckTableModel customerTab4SalesCheckTableModel = new CustomerSalesCheckTableModel(customerTab4SalesCheckResultMap);
    private TableRowSorter<CustomerSalesCheckTableModel> customerTab4SalesCheckTableSorter;
    
    public ControlPanel(JFrame parent) 
    {
        this.parent = parent;
        
        initComponents();
        myInit();
    }

    private void myInit()
    {        
        //***** Initialization for JTabbedPane *****//
        jTabbedPane.setTitleAt(0, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>鱼类资料</body></html>");
        jTabbedPane.setTitleAt(1, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>鱼类价格</body></html>");
        jTabbedPane.setTitleAt(2, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>船户资料</body></html>");
        jTabbedPane.setTitleAt(3, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>船户支银</body></html>");
        jTabbedPane.setTitleAt(4, "<html><body leftmargin=15 topmargin=8  marginwidth=15 marginheight=5>船户柴油</body></html>");
        jTabbedPane.setTitleAt(5, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>船户什费</body></html>");
        jTabbedPane.setTitleAt(6, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>船户来银</body></html>");
        jTabbedPane.setTitleAt(7, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>船户提款</body></html>");
        jTabbedPane.setTitleAt(8, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>船户结单</body></html>");
        jTabbedPane.setTitleAt(9, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>船户检查</body></html>");
        jTabbedPane.setTitleAt(10, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>客户资料</body></html>");
        jTabbedPane.setTitleAt(11, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>客户来往</body></html>");
        jTabbedPane.setTitleAt(12, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>客户检查</body></html>");
        jTabbedPane.setTitleAt(13, "<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>存货报表</body></html>");
    
        this.initItemTab1();
        this.initItemTab2();
        this.initSupplierTab1();
        this.initSupplierTab2();
        this.initSupplierTab3();
        this.initSupplierTab4();
        this.initSupplierTab5();
        this.initSupplierTab6();
        this.initSupplierTab7();
        this.initSupplierTab8();
        this.initCustomerTab1();
        this.initCustomerTab2();
        this.initCustomerTab4();
        this.initReportTab2();
        
        fillForm();
    }
    
    public void setActive(boolean state)
    {
        if (state==true)
        {            
            /*this.initItemTab1();
            this.initItemTab2();
            this.initSupplierTab1();
            this.initSupplierTab2();
            this.initSupplierTab3();
            this.initSupplierTab4();
            this.initSupplierTab5();
            this.initSupplierTab6();
            this.initSupplierTab7();
            this.initCustomerTab1();
            this.initCustomerTab2();
            this.initCustomerTab4();
            this.initReportTab2();

            fillForm();*/
        }
    }
    
    private void initItemTab1()
    {
        this.listModelItemTab1Category = new DefaultListModel();
        this.jListItemTab1Category.setModel(this.listModelItemTab1Category);
        
        // Add list selection listener to table
        this.jTableItemTab1Item.getSelectionModel().addListSelectionListener(this);
        this.jTableItemTab1Item.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableItemTab1Item.setRowHeight(30);
        this.jTableItemTab1Item.getColumn("id").setMinWidth( 200 );
        this.jTableItemTab1Item.getColumn("id").setMaxWidth( 200 );
    }
    
    private void initItemTab2()
    {
        this.jTextFieldItemTab2Date.addFocusListener( new JDateFieldFocusListener() );
        
        listModelItemTab2Category = new DefaultListModel();
        this.jListItemTab2Category.setModel(listModelItemTab2Category);
        
        listModelItemTab2Item = new DefaultListModel();
        this.jListItemTab2Item.setModel(listModelItemTab2Item);
        
        this.jTableItemTab2SupplierItem.getSelectionModel().addListSelectionListener(this);
        this.jTableItemTab2SupplierItem.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableItemTab2SupplierItem.setRowHeight(30);
        
        this.jTableItemTab2SupplierItem.getColumn("").setMinWidth( 36 );
        this.jTableItemTab2SupplierItem.getColumn("").setMaxWidth( 36 );
        this.jTableItemTab2SupplierItem.getColumn("id").setMinWidth( 200 );
        this.jTableItemTab2SupplierItem.getColumn("id").setMaxWidth( 200 );
        
        this.jTextFieldItemTab2NewPrice.addFocusListener( new JTextFieldFocusListener() );   
        
        jTextFieldItemTab2Date.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTimeInMillis()) );

        // Request focus back for Date jTextField
        jTextFieldItemTab2Date.requestFocus();
        jTextFieldItemTab2Date.setCaretPosition(0);
        jTextFieldItemTab2Date.select(0, 2);
    }
    
    private void initSupplierTab1()
    {
        this.jTableSupplierTab1Supplier.getSelectionModel().addListSelectionListener(this);
        this.jTableSupplierTab1Supplier.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSupplierTab1Supplier.setRowHeight(30);
        this.jTableSupplierTab1Supplier.getColumn("id").setMinWidth( 200 );
        this.jTableSupplierTab1Supplier.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of supplier
        supplierTab1SupplierTableSorter = new TableRowSorter<>(supplierTab1SupplierTableModel);
        this.jTableSupplierTab1Supplier.setRowSorter(supplierTab1SupplierTableSorter);
        this.jTableSupplierTab1Supplier.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList supplierList = new ArrayList();
        supplierList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        supplierTab1SupplierTableSorter.setSortKeys(supplierList);
        supplierTab1SupplierTableSorter.sort();
                
        //Whenever filterText changes, invoke newFilter.
        this.jTextFieldSupplierTab1Filter.getDocument().addDocumentListener(
            new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    supplierTableNewFilter(jTextFieldSupplierTab1Filter, supplierTab1SupplierTableSorter);
                }
                @Override
                public void insertUpdate(DocumentEvent e) {
                    supplierTableNewFilter(jTextFieldSupplierTab1Filter, supplierTab1SupplierTableSorter);
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    supplierTableNewFilter(jTextFieldSupplierTab1Filter, supplierTab1SupplierTableSorter);
                }
            });
    }
    
    private void initSupplierTab2()
    {
        this.listModelSupplierTab2Supplier = new DefaultListModel();
        this.jListSupplierTab2Supplier.setModel(listModelSupplierTab2Supplier);
        
        this.fillSupplierList( listModelSupplierTab2Supplier );
        
        this.jTableSupplierTab2ChequeTransaction.getSelectionModel().addListSelectionListener(this);
        this.jTableSupplierTab2ChequeTransaction.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSupplierTab2ChequeTransaction.setRowHeight(30);
        this.jTableSupplierTab2ChequeTransaction.getColumn("id").setMinWidth( 200 );
        this.jTableSupplierTab2ChequeTransaction.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of supplier
        this.supplierTab2ChequeTableSorter = new TableRowSorter<>(this.supplierTab2ChequeTableModel);
        this.jTableSupplierTab2ChequeTransaction.setRowSorter(this.supplierTab2ChequeTableSorter);
        this.jTableSupplierTab2ChequeTransaction.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList chequeList = new ArrayList();
        chequeList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.supplierTab2ChequeTableSorter.setSortKeys(chequeList);
        this.supplierTab2ChequeTableSorter.sort();
        
        // Show current month during initialization
        this.showDateRangeHalfMonth(Calendar.getInstance().getTimeInMillis(), this.jTextFieldSupplierTab2DateFrom, this.jTextFieldSupplierTab2DateTo);   
    }
    
    private void initSupplierTab3()
    {
        this.listModelSupplierTab3Supplier = new DefaultListModel();
        this.jListSupplierTab3Supplier.setModel(listModelSupplierTab3Supplier);
        
        this.fillSupplierList( listModelSupplierTab3Supplier );
        
        this.jTableSupplierTab3FuelTransaction.getSelectionModel().addListSelectionListener(this);
        this.jTableSupplierTab3FuelTransaction.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSupplierTab3FuelTransaction.setRowHeight(30);
        this.jTableSupplierTab3FuelTransaction.getColumn("id").setMinWidth( 200 );
        this.jTableSupplierTab3FuelTransaction.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of supplier
        this.supplierTab3FuelTableSorter = new TableRowSorter<>(this.supplierTab3FuelTableModel);
        this.jTableSupplierTab3FuelTransaction.setRowSorter(this.supplierTab3FuelTableSorter);
        this.jTableSupplierTab3FuelTransaction.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList fuelList = new ArrayList();
        fuelList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.supplierTab3FuelTableSorter.setSortKeys(fuelList);
        this.supplierTab3FuelTableSorter.sort();
        
        // Show current month during initialization
        this.showDateRangeHalfMonth(Calendar.getInstance().getTimeInMillis(), this.jTextFieldSupplierTab3DateFrom, this.jTextFieldSupplierTab3DateTo);   
    }
    
    private void initSupplierTab4()
    {
        this.listModelSupplierTab4Supplier = new DefaultListModel();
        this.jListSupplierTab4Supplier.setModel(listModelSupplierTab4Supplier);
        
        this.fillSupplierList( listModelSupplierTab4Supplier );
        
        this.jTableSupplierTab4MiscTransaction.getSelectionModel().addListSelectionListener(this);
        this.jTableSupplierTab4MiscTransaction.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSupplierTab4MiscTransaction.setRowHeight(30);
        this.jTableSupplierTab4MiscTransaction.getColumn("id").setMinWidth( 200 );
        this.jTableSupplierTab4MiscTransaction.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of supplier
        this.supplierTab4MiscTableSorter = new TableRowSorter<>(this.supplierTab4MiscTableModel);
        this.jTableSupplierTab4MiscTransaction.setRowSorter(this.supplierTab4MiscTableSorter);
        this.jTableSupplierTab4MiscTransaction.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList miscList = new ArrayList();
        miscList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.supplierTab4MiscTableSorter.setSortKeys(miscList);
        this.supplierTab4MiscTableSorter.sort();
        
        // Show current month during initialization
        this.showDateRangeHalfMonth(Calendar.getInstance().getTimeInMillis(), this.jTextFieldSupplierTab4DateFrom, this.jTextFieldSupplierTab4DateTo);   
    }
    
    private void initSupplierTab5()
    {
        this.listModelSupplierTab5Supplier = new DefaultListModel();
        this.jListSupplierTab5Supplier.setModel(listModelSupplierTab5Supplier);
        
        this.fillSupplierList( listModelSupplierTab5Supplier );
        
        this.jTableSupplierTab5CashTransaction.getSelectionModel().addListSelectionListener(this);
        this.jTableSupplierTab5CashTransaction.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSupplierTab5CashTransaction.setRowHeight(30);
        this.jTableSupplierTab5CashTransaction.getColumn("id").setMinWidth( 200 );
        this.jTableSupplierTab5CashTransaction.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of supplier
        this.supplierTab5CashTableSorter = new TableRowSorter<>(this.supplierTab5CashTableModel);
        this.jTableSupplierTab5CashTransaction.setRowSorter(this.supplierTab5CashTableSorter);
        this.jTableSupplierTab5CashTransaction.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList miscList = new ArrayList();
        miscList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.supplierTab5CashTableSorter.setSortKeys(miscList);
        this.supplierTab5CashTableSorter.sort();
        
        // Show current month during initialization
        this.showDateRangeHalfMonth(Calendar.getInstance().getTimeInMillis(), this.jTextFieldSupplierTab5DateFrom, this.jTextFieldSupplierTab5DateTo);   
    }
    
    private void initSupplierTab6()
    {
        this.listModelSupplierTab6Supplier = new DefaultListModel();
        this.jListSupplierTab6Supplier.setModel(listModelSupplierTab6Supplier);
        
        this.fillSupplierList( listModelSupplierTab6Supplier );
        
        this.jTableSupplierTab6WithdrawalTransaction.getSelectionModel().addListSelectionListener(this);
        this.jTableSupplierTab6WithdrawalTransaction.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSupplierTab6WithdrawalTransaction.setRowHeight(30);
        this.jTableSupplierTab6WithdrawalTransaction.getColumn("id").setMinWidth( 200 );
        this.jTableSupplierTab6WithdrawalTransaction.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of supplier
        this.supplierTab6WithdrawalTableSorter = new TableRowSorter<>(this.supplierTab6WithdrawalTableModel);
        this.jTableSupplierTab6WithdrawalTransaction.setRowSorter(this.supplierTab6WithdrawalTableSorter);
        this.jTableSupplierTab6WithdrawalTransaction.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList miscList = new ArrayList();
        miscList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.supplierTab6WithdrawalTableSorter.setSortKeys(miscList);
        this.supplierTab6WithdrawalTableSorter.sort();
        
        // Show current month during initialization
        this.showDateRangeHalfMonth(Calendar.getInstance().getTimeInMillis(), this.jTextFieldSupplierTab6DateFrom, this.jTextFieldSupplierTab6DateTo);   
    }
    
    private void initSupplierTab7()
    {
        this.listModelSupplierTab7Supplier = new DefaultListModel();
        this.jListSupplierTab7Supplier.setModel(listModelSupplierTab7Supplier);
        
        this.fillSupplierList( listModelSupplierTab7Supplier );
        
        this.jTableSupplierTab7Stock.getSelectionModel().addListSelectionListener(this);
        this.jTableSupplierTab7Stock.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSupplierTab7Stock.setRowHeight(30);
        this.jTableSupplierTab7Stock.getColumn("id").setMinWidth( 200 );
        this.jTableSupplierTab7Stock.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of supplier
        this.supplierTab7StockTableSorter = new TableRowSorter<>(this.supplierTab7StockTableModel);
        this.jTableSupplierTab7Stock.setRowSorter(this.supplierTab7StockTableSorter);
        this.jTableSupplierTab7Stock.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList stockList = new ArrayList();
        stockList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.supplierTab7StockTableSorter.setSortKeys(stockList);
        this.supplierTab7StockTableSorter.sort();
        
        this.jTableSupplierTab7Cash.getSelectionModel().addListSelectionListener(this);
        this.jTableSupplierTab7Cash.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSupplierTab7Cash.setRowHeight(30);
        this.jTableSupplierTab7Cash.getColumn("id").setMinWidth( 200 );
        this.jTableSupplierTab7Cash.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of supplier
        this.supplierTab7CashTableSorter = new TableRowSorter<>(this.supplierTab7CashTableModel);
        this.jTableSupplierTab7Cash.setRowSorter(this.supplierTab7CashTableSorter);
        this.jTableSupplierTab7Cash.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList cashList = new ArrayList();
        cashList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.supplierTab7CashTableSorter.setSortKeys(cashList);
        this.supplierTab7CashTableSorter.sort();
        
        this.jTableSupplierTab7Fuel.getSelectionModel().addListSelectionListener(this);
        this.jTableSupplierTab7Fuel.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSupplierTab7Fuel.setRowHeight(30);
        this.jTableSupplierTab7Fuel.getColumn("id").setMinWidth( 200 );
        this.jTableSupplierTab7Fuel.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of supplier
        this.supplierTab7FuelTableSorter = new TableRowSorter<>(this.supplierTab7FuelTableModel);
        this.jTableSupplierTab7Fuel.setRowSorter(this.supplierTab7FuelTableSorter);
        this.jTableSupplierTab7Fuel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList fuelList = new ArrayList();
        fuelList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.supplierTab7FuelTableSorter.setSortKeys(fuelList);
        this.supplierTab7FuelTableSorter.sort();
        
        this.jTableSupplierTab7Cheque.getSelectionModel().addListSelectionListener(this);
        this.jTableSupplierTab7Cheque.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSupplierTab7Cheque.setRowHeight(30);
        this.jTableSupplierTab7Cheque.getColumn("id").setMinWidth( 200 );
        this.jTableSupplierTab7Cheque.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of supplier
        this.supplierTab7ChequeTableSorter = new TableRowSorter<>(this.supplierTab7ChequeTableModel);
        this.jTableSupplierTab7Cheque.setRowSorter(this.supplierTab7ChequeTableSorter);
        this.jTableSupplierTab7Cheque.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList chequeList = new ArrayList();
        chequeList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.supplierTab7ChequeTableSorter.setSortKeys(chequeList);
        this.supplierTab7ChequeTableSorter.sort();
        
        this.jTableSupplierTab7Misc.getSelectionModel().addListSelectionListener(this);
        this.jTableSupplierTab7Misc.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSupplierTab7Misc.setRowHeight(30);
        this.jTableSupplierTab7Misc.getColumn("id").setMinWidth( 200 );
        this.jTableSupplierTab7Misc.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of supplier
        this.supplierTab7MiscTableSorter = new TableRowSorter<>(this.supplierTab7MiscTableModel);
        this.jTableSupplierTab7Misc.setRowSorter(this.supplierTab7MiscTableSorter);
        this.jTableSupplierTab7Misc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList miscList = new ArrayList();
        miscList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.supplierTab7MiscTableSorter.setSortKeys(miscList);
        this.supplierTab7MiscTableSorter.sort();
        
        this.jTableSupplierTab7Saving.getSelectionModel().addListSelectionListener(this);
        this.jTableSupplierTab7Saving.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSupplierTab7Saving.setRowHeight(30);
        this.jTableSupplierTab7Saving.getColumn("id").setMinWidth( 200 );
        this.jTableSupplierTab7Saving.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of supplier
        this.supplierTab7SavingTableSorter = new TableRowSorter<>(this.supplierTab7SavingTableModel);
        this.jTableSupplierTab7Saving.setRowSorter(this.supplierTab7SavingTableSorter);
        this.jTableSupplierTab7Saving.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList savingList = new ArrayList();
        savingList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.supplierTab7SavingTableSorter.setSortKeys(savingList);
        this.supplierTab7SavingTableSorter.sort();
                
        this.jTableSupplierTab7Withdrawal.getSelectionModel().addListSelectionListener(this);
        this.jTableSupplierTab7Withdrawal.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSupplierTab7Withdrawal.setRowHeight(30);
        this.jTableSupplierTab7Withdrawal.getColumn("id").setMinWidth( 200 );
        this.jTableSupplierTab7Withdrawal.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of supplier
        this.supplierTab7WithdrawalTableSorter = new TableRowSorter<>(this.supplierTab7WithdrawalTableModel);
        this.jTableSupplierTab7Withdrawal.setRowSorter(this.supplierTab7WithdrawalTableSorter);
        this.jTableSupplierTab7Withdrawal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList withdrawalList = new ArrayList();
        savingList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.supplierTab7WithdrawalTableSorter.setSortKeys(savingList);
        this.supplierTab7WithdrawalTableSorter.sort();
        
        // Show current month during initialization
        this.showDateRangeHalfMonth(Calendar.getInstance().getTimeInMillis(), this.jTextFieldSupplierTab7DateFrom, this.jTextFieldSupplierTab7DateTo);   
    }
    
    private void initSupplierTab8()
    {   
        this.jTextFieldSupplierTab8Year.addFocusListener(new JTextFieldFocusListener());
        
        this.jTableSupplierTab8Result.getSelectionModel().addListSelectionListener(this);
        this.jTableSupplierTab8Result.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableSupplierTab8Result.setRowHeight(30);
        
        // Create sorter for table of supplier
        this.supplierTab8SalesCheckTableSorter = new TableRowSorter<>(this.supplierTab8SalesCheckTableModel);
        this.jTableSupplierTab8Result.setRowSorter(this.supplierTab8SalesCheckTableSorter);
        this.jTableSupplierTab8Result.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        Action buttonResponse = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JTable table = (JTable)e.getSource();
                int modelRow = Integer.valueOf( e.getActionCommand() );
        
                String buttonText = ControlPanel.this.jTableSupplierTab8Result.getValueAt(modelRow, 3).toString();
                
                String salesId = buttonText.substring( buttonText.indexOf('[')+1, buttonText.lastIndexOf( "]") );
                System.err.println("Button Clicked " + salesId);
                
                ((JFrameMain)ControlPanel.this.parent)
                        .switchToInboundPanel().searchDialogClosed( 
                                InTransactionDelegate.getInstance().getTransactionById(salesId) );
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(jTableSupplierTab8Result, buttonResponse, 3);
        //buttonColumn.setMnemonic(KeyEvent.VK_D);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList paymentList = new ArrayList();
        paymentList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.supplierTab8SalesCheckTableSorter.setSortKeys(paymentList);
        this.supplierTab8SalesCheckTableSorter.sort();
        
        this.fillSupplierTab7Table();
    }
    
    private void initCustomerTab1()
    {
        this.jTableCustomerTab1Customer.getSelectionModel().addListSelectionListener(this);
        this.jTableCustomerTab1Customer.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableCustomerTab1Customer.setRowHeight(30);
        this.jTableCustomerTab1Customer.getColumn("id").setMinWidth( 200 );
        this.jTableCustomerTab1Customer.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of customer
        this.customerTab1CustomerTableSorter = new TableRowSorter<>(customerTab1CustomerTableModel);
        this.jTableCustomerTab1Customer.setRowSorter(this.customerTab1CustomerTableSorter);
        this.jTableCustomerTab1Customer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList customerList = new ArrayList();
        customerList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.customerTab1CustomerTableSorter.setSortKeys(customerList);
        this.customerTab1CustomerTableSorter.sort();
        
        //Whenever filterText changes, invoke newFilter.
        this.jTextFieldCustomerTab1Filter.getDocument().addDocumentListener(
            new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent e) {
                    customerTableNewFilter();
                }
                @Override
                public void insertUpdate(DocumentEvent e) {
                    customerTableNewFilter();
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    customerTableNewFilter();
                }
            });
    }
    
    private void initCustomerTab2()
    {        
        this.listModelCustomerTab2Customer = new DefaultListModel();
        this.jListCustomerTab2Customer.setModel(this.listModelCustomerTab2Customer);
                
        this.jTableCustomerTab2Payment.getSelectionModel().addListSelectionListener(this);
        this.jTableCustomerTab2Payment.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableCustomerTab2Payment.setRowHeight(30);
        this.jTableCustomerTab2Payment.getColumn("id").setMinWidth( 200 );
        this.jTableCustomerTab2Payment.getColumn("id").setMaxWidth( 200 );
        
        this.jTableCustomerTab2Report.getSelectionModel().addListSelectionListener(this);
        this.jTableCustomerTab2Report.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableCustomerTab2Report.setRowHeight(30);
        this.jTableCustomerTab2Report.getColumn("id").setMinWidth( 200 );
        this.jTableCustomerTab2Report.getColumn("id").setMaxWidth( 200 );
        
        // Create sorter for table of supplier
        this.customerTab2PaymentTableSorter = new TableRowSorter<>(this.customerTab2PaymentTableModel);
        this.jTableCustomerTab2Payment.setRowSorter(this.customerTab2PaymentTableSorter);
        this.jTableCustomerTab2Payment.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList paymentList = new ArrayList();
        paymentList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.customerTab2PaymentTableSorter.setSortKeys(paymentList);
        this.customerTab2PaymentTableSorter.sort();
        
        // Create sorter for table of customer
        this.customerTab2SalesTableSorter = new TableRowSorter<>(this.customerTab2SalesTableModel);
        this.jTableCustomerTab2Report.setRowSorter(this.customerTab2SalesTableSorter);
        this.jTableCustomerTab2Report.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sort table of customer programmatically, order by column index 0.
        ArrayList salesList = new ArrayList();
        salesList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.customerTab2SalesTableSorter.setSortKeys(salesList);
        this.customerTab2SalesTableSorter.sort();
        
        // Show current month during initialization
        this.showDateRangeFullMonth(Calendar.getInstance().getTimeInMillis(), this.jTextFieldCustomerTab2DateFrom, this.jTextFieldCustomerTab2DateTo);   
    }
    
    private void initCustomerTab4()
    {   
        this.jTextFieldCustomerTab4Year.addFocusListener(new JTextFieldFocusListener());
        
        this.jTableCustomerTab4Result.getSelectionModel().addListSelectionListener(this);
        this.jTableCustomerTab4Result.getTableHeader().setFont(new Font("Kaiti", Font.PLAIN, 24));
        this.jTableCustomerTab4Result.setRowHeight(30);
        
        // Create sorter for table of supplier
        this.customerTab4SalesCheckTableSorter = new TableRowSorter<>(this.customerTab4SalesCheckTableModel);
        this.jTableCustomerTab4Result.setRowSorter(this.customerTab4SalesCheckTableSorter);
        this.jTableCustomerTab4Result.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        Action buttonResponse = new AbstractAction()
        {   
            @Override
            public void actionPerformed(ActionEvent e)
            { 
                JTable table = (JTable)e.getSource();
                int modelRow = Integer.valueOf( e.getActionCommand() );

                String buttonText = ControlPanel.this.jTableCustomerTab4Result.getValueAt(modelRow, 5).toString();

                String salesId = buttonText.substring( buttonText.indexOf('[')+1, buttonText.lastIndexOf( "]") );

                ((JFrameMain)ControlPanel.this.parent)
                        .switchToOutboundPanel().searchDialogClosed( 
                                SalesDelegate.getInstance().getSalesById(salesId) );
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(jTableCustomerTab4Result, buttonResponse, 5);
        
        // Sort table of supplier programmatically, order by column index 0.
        ArrayList paymentList = new ArrayList();
        paymentList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        this.customerTab4SalesCheckTableSorter.setSortKeys(paymentList);
        this.customerTab4SalesCheckTableSorter.sort();
        
        this.fillCustomerTab4Table();        
    }
        
    private void initReportTab2()
    {
        this.jTextFieldReportTab2Date.addFocusListener( new JDateFieldFocusListener() );
        
        listModelReportTab2Category = new DefaultListModel();
        this.jListReportTab2Category.setModel(listModelReportTab2Category);
        
        listModelReportTab2Item = new DefaultListModel();
        this.jListReportTab2Item.setModel(listModelReportTab2Item);
        
        listModelReportTab2ItemSelection = new DefaultListModel();
        this.jListReportTab2ItemSelection.setModel(listModelReportTab2ItemSelection);      
        
        jTextFieldReportTab2Date.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTimeInMillis()) );

        // Request focus back for Date jTextField
        jTextFieldReportTab2Date.requestFocus();
        jTextFieldReportTab2Date.setCaretPosition(0);
        jTextFieldReportTab2Date.select(0, 2);
    }
    
    private void fillForm()
    {
        // Fill in list of category into Tab 1
        List<CategoryBean> categoryList = SystemDataDelegate.getInstance().getAllCategories();
        for (CategoryBean category : categoryList)
        {
            this.listModelItemTab1Category.addElement( category );
            this.listModelItemTab2Category.addElement( category );
            this.listModelReportTab2Category.addElement( category );
        }
        
        refillSupplierTable();
        
        refillCustomerTable();
        
        this.jTextFieldItemTab2Date.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format( Calendar.getInstance().getTimeInMillis() ) );
        this.jTextFieldReportTab2Date.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format( Calendar.getInstance().getTimeInMillis() ) );
                
         // Fill in list of category into Tab 1
        List<CustomerBean> customerList = CustomerDelegate.getInstance().getAllCustomers();
        Collections.sort( customerList );
        for (CustomerBean customer : customerList)
        {
            this.listModelCustomerTab2Customer.addElement( customer );
        }
    }
    
    private void supplierTableNewFilter(JTextField jTextField, TableRowSorter<SupplierTableModel> tableRowSorter) 
    {
        RowFilter<SupplierTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(jTextField.getText().toUpperCase(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        tableRowSorter.setRowFilter(rf);
    }
    
    private void customerTableNewFilter() 
    {
        RowFilter<CustomerTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(this.jTextFieldCustomerTab1Filter.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        customerTab1CustomerTableSorter.setRowFilter(rf);
    }
    
    private void fillSupplierTab2Table()
    {
        if (this.jTextFieldSupplierTab2DateFrom.getText().trim().length() > 0 &&
            this.jTextFieldSupplierTab2DateTo.getText().trim().length() > 0 )
        {
            this.jListSupplierTab2SupplierValueChanged(null);
        }
    }
    
    private void fillSupplierTab3Table()
    {
        if (this.jTextFieldSupplierTab3DateFrom.getText().trim().length() > 0 &&
            this.jTextFieldSupplierTab3DateTo.getText().trim().length() > 0 )
        {
            this.jListSupplierTab3SupplierValueChanged(null);
        }
    }
    
    private void fillSupplierTab4Table()
    {
        if (this.jTextFieldSupplierTab4DateFrom.getText().trim().length() > 0 &&
            this.jTextFieldSupplierTab4DateTo.getText().trim().length() > 0 )
        {
            this.jListSupplierTab4SupplierValueChanged(null);
        }
    }
    
    private void fillSupplierTab5Table()
    {
        if (this.jTextFieldSupplierTab5DateFrom.getText().trim().length() > 0 &&
            this.jTextFieldSupplierTab5DateTo.getText().trim().length() > 0 )
        {
            this.jListSupplierTab5SupplierValueChanged(null);
        }
    }
    
    private void fillSupplierTab6Table()
    {
        if (this.jTextFieldSupplierTab7DateFrom.getText().trim().length() > 0 &&
            this.jTextFieldSupplierTab7DateTo.getText().trim().length() > 0 )
        {
            this.jListSupplierTab6SupplierValueChanged(null);
        }
    }
    
    private void fillCustomerTab2Table()
    {
        if (this.jTextFieldCustomerTab2DateFrom.getText().trim().length() > 0 &&
            this.jTextFieldCustomerTab2DateTo.getText().trim().length() > 0 )
        {
            this.jListCustomerTab2CustomerValueChanged(null);
        }
    }
    
    private void fillCustomerTab3Table()
    {
    }
    
    private void fillCustomerTab4Table()
    {
        Calendar today = Calendar.getInstance();
        
        this.jTextFieldCustomerTab4Year.setText(String.valueOf(today.get(Calendar.YEAR)));
        this.jComboBoxCustomerTab4Month.setSelectedIndex(today.get(Calendar.MONTH));
    }
    
    private void fillSupplierTab7Table()
    {
        Calendar today = Calendar.getInstance();
        
        this.jTextFieldSupplierTab8Year.setText(String.valueOf(today.get(Calendar.YEAR)));
        this.jComboBoxSupplierTab8Month.setSelectedIndex(today.get(Calendar.MONTH));
    }
    
    private void refillSupplierTable()
    {
        this.supplierTab1SupplierTableModel.removeAll();
        
        // Fill in list of supplier into Tab 2
        List<SupplierBean> supplierList = SupplierDelegate.getInstance().getAllSuppliers();
        Collections.sort(supplierList);
        for (SupplierBean supplier : supplierList)
        {
            supplierTab1SupplierTableModel.addBean( supplier );
        }
    }
    
    private void refillCustomerTable()
    {
        this.customerTab1CustomerTableModel.removeAll();
        this.listModelCustomerTab2Customer.removeAllElements();
        
        // Fill in list of supplier into Tab 3
        List<CustomerBean> customerList = CustomerDelegate.getInstance().getAllCustomers();
        Collections.sort(customerList);
        for (CustomerBean customer : customerList)
        {
            this.customerTab1CustomerTableModel.addBean( customer );
            this.listModelCustomerTab2Customer.addElement( customer );
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent lse) 
    {
        if (lse.getSource() == this.jTableItemTab1Item.getSelectionModel()) 
        {
            if (jTableItemTab1Item.getSelectedRow()==-1) 
            {
                jButtonItemTab1Edit.setEnabled( false );
                jButtonItemTab1Delete.setEnabled( false );
            }            
            else 
            {
                jButtonItemTab1Edit.setEnabled( true );
                jButtonItemTab1Delete.setEnabled( true );              
            }
        }
        
        if (lse.getSource() == this.jTableItemTab2SupplierItem.getSelectionModel()) 
        {
            if (this.jTableItemTab2SupplierItem.getSelectedRowCount() > 0)
            {
                String selectedId = 
                    this.jTableItemTab2SupplierItem.getValueAt(
                        this.jTableItemTab2SupplierItem.getSelectedRow(), 1).toString();
                InTransactionLineBean lineBean = 
                    this.itemTab2PriceChangeTableModel.getBeanById(selectedId);
                
                this.jTextFieldItemTab2ItemName.setText( lineBean.getItemNewName() );
                this.jTextFieldItemTab2OldPrice.setText( currencyFormatter.format( lineBean.getUnitPrice() ) );
                this.jTextFieldItemTab2NewPrice.setText( "" );
            }            
        }
        
        if (lse.getSource() == this.jTableSupplierTab1Supplier.getSelectionModel()) 
        {
            if (jTableSupplierTab1Supplier.getSelectedRow()==-1) 
            {
                this.jButtonSupplierTab1Edit.setEnabled( false );
                this.jButtonSupplierTab1Delete.setEnabled( false );
            }            
            else 
            {
                this.jButtonSupplierTab1Edit.setEnabled( true );
                this.jButtonSupplierTab1Delete.setEnabled( true );              
            }
        }
        
        if (lse.getSource() == this.jTableSupplierTab2ChequeTransaction.getSelectionModel()) 
        {
            if (jTableSupplierTab2ChequeTransaction.getSelectedRow()==-1) 
            {
                this.jButtonSupplierTab2Edit.setEnabled( false );
                this.jButtonSupplierTab2Remove.setEnabled( false );
            }            
            else 
            {
                this.jButtonSupplierTab2Edit.setEnabled( true );
                this.jButtonSupplierTab2Remove.setEnabled( true );              
            }
        }
        
        if (lse.getSource() == this.jTableSupplierTab3FuelTransaction.getSelectionModel()) 
        {
            if (jTableSupplierTab3FuelTransaction.getSelectedRow()==-1) 
            {
                this.jButtonSupplierTab3Edit.setEnabled( false );
                this.jButtonSupplierTab3Remove.setEnabled( false );
            }            
            else 
            {
                this.jButtonSupplierTab3Edit.setEnabled( true );
                this.jButtonSupplierTab3Remove.setEnabled( true );              
            }
        }
        
        if (lse.getSource() == this.jTableSupplierTab4MiscTransaction.getSelectionModel()) 
        {
            if (jTableSupplierTab4MiscTransaction.getSelectedRow()==-1) 
            {
                this.jButtonSupplierTab4Edit.setEnabled( false );
                this.jButtonSupplierTab4Remove.setEnabled( false );
            }            
            else 
            {
                this.jButtonSupplierTab4Edit.setEnabled( true );
                this.jButtonSupplierTab4Remove.setEnabled( true );              
            }
        }
        
        if (lse.getSource() == this.jTableSupplierTab5CashTransaction.getSelectionModel()) 
        {
            if (jTableSupplierTab5CashTransaction.getSelectedRow()==-1) 
            {
                this.jButtonSupplierTab5Edit.setEnabled( false );
                this.jButtonSupplierTab5Remove.setEnabled( false );
            }            
            else 
            {
                this.jButtonSupplierTab5Edit.setEnabled( true );
                this.jButtonSupplierTab5Remove.setEnabled( true );              
            }
        }
        
        if (lse.getSource() == this.jTableSupplierTab6WithdrawalTransaction.getSelectionModel()) 
        {
            if (jTableSupplierTab6WithdrawalTransaction.getSelectedRow()==-1) 
            {
                this.jButtonSupplierTab6Edit.setEnabled( false );
                this.jButtonSupplierTab6Remove.setEnabled( false );
            }            
            else 
            {
                this.jButtonSupplierTab6Edit.setEnabled( true );
                this.jButtonSupplierTab6Remove.setEnabled( true );              
            }
        }
        
        if (lse.getSource() == this.jTableCustomerTab1Customer.getSelectionModel()) 
        {
            if (jTableCustomerTab1Customer.getSelectedRow()==-1) 
            {
                this.jButtonCustomerTab1Edit.setEnabled( false );
                this.jButtonCustomerTab1Delete.setEnabled( false );
            }            
            else 
            {
                this.jButtonCustomerTab1Edit.setEnabled( true );
                this.jButtonCustomerTab1Delete.setEnabled( true );              
            }
        }
        
        if (lse.getSource() == this.jTableCustomerTab2Payment.getSelectionModel()) 
        {
            if (jTableCustomerTab2Payment.getSelectedRow()==-1) 
            {
                this.jButtonCustomerTab2Edit.setEnabled( false );
                this.jButtonCustomerTab2Remove.setEnabled( false );
            }            
            else 
            {
                this.jButtonCustomerTab2Edit.setEnabled( true );
                this.jButtonCustomerTab2Remove.setEnabled( true );              
            }
        }
    }
    
    @Override
    public void itemEditorDialogClosed(ItemBean itemBean) 
    {
        SystemDataDelegate.getInstance().saveOrUpdateItem(itemBean);
        
        JOptionPane.showMessageDialog(
            this, 
            "鱼类 '" + itemBean.getName() + "' 已被储存成功。", 
            "储存成功", 
            JOptionPane.INFORMATION_MESSAGE);
        
        if (  this.jListItemTab1Category.getSelectedIndex() == -1)
            return;
        
        Object categoryObj = 
                this.jListItemTab1Category.getModel().getElementAt( this.jListItemTab1Category.getSelectedIndex() );
        
        if (categoryObj == null)
            return;
        
        CategoryBean selectedCategory = (CategoryBean) categoryObj;  
        
        if (itemBean.getCategoryId().equalsIgnoreCase(selectedCategory.getId()))
        {        
            // Clear item list
            this.itemTab1ItemTableModel.removeAll();

            List<ItemBean> itemList = 
                    SystemDataDelegate.getInstance().getItemsByCategoryId(selectedCategory.getId());

            for (ItemBean item : itemList) {
                this.itemTab1ItemTableModel.addBean(item);
            }
        }
    }
    
    @Override
    public void supplierChequeEditorDialogClosed(SupplierChequeBean bean) 
    {
        SupplierDelegate.getInstance().saveOrUpdateCheque(bean);
        
        this.fillSupplierTab2Table();
        
        JOptionPane.showMessageDialog(
            this, 
            "支票 '" + bean.getChequeNo() + "' 已被储存成功。", 
            "储存成功", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void supplierFuelEditorDialogClosed(SupplierFuelBean bean) 
    {
        SupplierDelegate.getInstance().saveOrUpdateFuel(bean);
        
        this.fillSupplierTab3Table();
        
        JOptionPane.showMessageDialog(
            this, 
            "柴油交易已被储存成功。", 
            "储存成功", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void supplierMiscEditorDialogClosed(SupplierMiscBean bean) 
    {
        SupplierDelegate.getInstance().saveOrUpdateMisc(bean);
        
        this.fillSupplierTab4Table();
        
        JOptionPane.showMessageDialog(
            this, 
            "什费交易已被储存成功。", 
            "储存成功", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void supplierCashEditorDialogClosed(SupplierCashBean bean) 
    {
        SupplierDelegate.getInstance().saveOrUpdateCash(bean);
        
        this.fillSupplierTab5Table();
        
        JOptionPane.showMessageDialog(
            this, 
            "现金交易已被储存成功。", 
            "储存成功", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void supplierEditorDialogClosed(SupplierBean supplierBean) 
    {
        SupplierDelegate.getInstance().saveOrUpdateSupplier(supplierBean);
        
        this.refillSupplierTable();
        
        JOptionPane.showMessageDialog(
            this, 
            "船户 '" + supplierBean.getName() + "' 已被储存成功。", 
            "储存成功", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void customerEditorDialogClosed(CustomerBean customerBean) 
    {
        CustomerDelegate.getInstance().saveOrUpdateCustomer(customerBean);
        
        this.refillCustomerTable();
        
        JOptionPane.showMessageDialog(
            this, 
            "客户 '" + customerBean.getName() + "' 已被储存成功。", 
            "储存成功", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void customerPaymentEditorDialogClosed(CustomerPaymentBean bean) 
    {
        CustomerDelegate.getInstance().saveOrUpdatePayment(bean);
        
        // Update UI
        long startDate = this.getSelectedDateInMillis(this.jTextFieldCustomerTab2DateFrom);
        long endDate = this.getSelectedDateInMillis(this.jTextFieldCustomerTab2DateTo);        
        if (startDate==0 || endDate==0)
            return;
        
        CustomerBean customer = (CustomerBean) this.jListCustomerTab2Customer.getSelectedValue();
        List<CustomerPaymentBean> list = 
            CustomerDelegate.getInstance().getPaymentsByCustomerAndDateRange(customer.getId(), startDate, endDate);
        
        double totalAmount = 0.0d;
        this.customerTab2PaymentTableModel.removeAll();
        for (CustomerPaymentBean payment : list)
        {
            this.customerTab2PaymentTableModel.addBean( payment );
            totalAmount = totalAmount + payment.getAmount();
        }
        // Show total payment
        this.jLabelCustomerTab2TotalAmount.setText( currencyFormatter.format( totalAmount ) );
        // Re-Calculate
        this.jButtonCustomerTab3ShowSummary2.doClick();
    }
    
    private void showFisheryItemByCategory(JList jListCategory, DefaultListModel<ItemBean> listModel)
    {
        if (jListCategory.getSelectedIndex()==-1)
            return;
        
        Object categoryObj = 
                jListCategory.getModel().getElementAt( jListCategory.getSelectedIndex() );
        
        if (categoryObj == null)
            return;
        
        // Clear item list
        listModel.clear();
        
        CategoryBean selectedCategory = (CategoryBean) categoryObj;        
        List<ItemBean> itemList = 
                SystemDataDelegate.getInstance().getItemsByCategoryId(selectedCategory.getId());
        
        for (ItemBean item : itemList) {
            listModel.addElement(item);
        }
    }
    
    private void fillSupplierList(DefaultListModel<SupplierBean> listModel)
    {
        List<SupplierBean> list = SupplierDelegate.getInstance().getAllSuppliers();
        Collections.sort(list);
        for (SupplierBean supplier : list)
        {
            listModel.addElement(supplier);
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
            MyLogger.logError(getClass(), e.getMessage());
            return -1;
        }
        catch (Exception e) {
            MyLogger.logError(getClass(), e.getMessage());
            return -1;
        }
    }
    
    private void dateFieldKeyReleasedListener(java.awt.event.KeyEvent evt, JTextField dateField)
    {
        int keyCode = (int) evt.getKeyChar();
        
        // If user press ENTER, response.
        if (keyCode==10)
        {
            int caretPosition = dateField.getCaretPosition();
            if (caretPosition==2)
            {
                dateField.setCaretPosition( caretPosition+1 );
                dateField.select(caretPosition+1, caretPosition+3);
            }
            
            if (caretPosition==5)
            {
                dateField.setCaretPosition(caretPosition+1);
                dateField.select(caretPosition+1, caretPosition+5);
            }
            
            if (caretPosition==10)
            {
                dateField.setCaretPosition( 0 );
                dateField.select(0, 2);
            }            
        }
    }
    
    private long showDatePicker(JTextField dateField)
    {
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
            
            if (dateField != null)
            {
                dateField.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format(date.getTime()) );

                // Request focus back for Date jTextField
                dateField.requestFocus();
                dateField.setCaretPosition(0);
                dateField.select(0, 2);
            }
            
            return date.getTimeInMillis();
        }
        else
        {
            return 0;
        }
    }
        
    private void showDateRangeHalfMonth(long timeInMillis, JTextField dateFieldFrom, JTextField dateFieldTo)
    {
        if (timeInMillis==0)
            return;
        
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(timeInMillis);
        
        int startDay = 1;
        int endDay = 1;
        
        if (selectedDate.get(Calendar.DAY_OF_MONTH) <= 15)
        {
            startDay = 1;
            endDay = 15;
        }
        else 
        {
            startDay = 16;
            endDay = selectedDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(timeInMillis);
        startDate.set(Calendar.DAY_OF_MONTH, startDay);
        
        Calendar endDate = Calendar.getInstance();
        endDate.setTimeInMillis(timeInMillis);
        endDate.set(Calendar.DAY_OF_MONTH, endDay);
        
        dateFieldFrom.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format(startDate.getTime()) );
        dateFieldTo.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format(endDate.getTime()) );
    }
    
    private void showDateRangeFullMonth(long timeInMillis, JTextField dateFieldFrom, JTextField dateFieldTo)
    {
        if (timeInMillis==0)
            return;
        
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTimeInMillis(timeInMillis);
        
        int startDay = 1;
        int endDay = selectedDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(timeInMillis);
        startDate.set(Calendar.DAY_OF_MONTH, startDay);
        
        Calendar endDate = Calendar.getInstance();
        endDate.setTimeInMillis(timeInMillis);
        endDate.set(Calendar.DAY_OF_MONTH, endDay);
        
        dateFieldFrom.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format(startDate.getTime()) );
        dateFieldTo.setText( MyDatePicker.SIMPLE_DATE_FORMAT.format(endDate.getTime()) );
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
        jButtonExit = new javax.swing.JButton();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelItemTab1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListItemTab1Category = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableItemTab1Item = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jButtonItemTab1Add = new javax.swing.JButton();
        jButtonItemTab1Edit = new javax.swing.JButton();
        jButtonItemTab1Delete = new javax.swing.JButton();
        jPanelItemTab2 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldItemTab2Date = new javax.swing.JTextField();
        jButtonItemTab2DatePicker = new javax.swing.JButton();
        jPanel29 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jListItemTab2Category = new javax.swing.JList();
        jPanel32 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jListItemTab2Item = new javax.swing.JList();
        jPanel30 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldItemTab2ItemName = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldItemTab2OldPrice = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldItemTab2NewPrice = new javax.swing.JTextField();
        jButtonItemTab2SaveChange = new javax.swing.JButton();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTableItemTab2SupplierItem = new javax.swing.JTable();
        jPanelSupplierTab1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jTextFieldSupplierTab1Filter = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableSupplierTab1Supplier = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jButtonSupplierTab1Add = new javax.swing.JButton();
        jButtonSupplierTab1Edit = new javax.swing.JButton();
        jButtonSupplierTab1Delete = new javax.swing.JButton();
        jPanelSupplierTab2 = new javax.swing.JPanel();
        jPanel75 = new javax.swing.JPanel();
        jPanel76 = new javax.swing.JPanel();
        jButtonSupplierTab2DateChooser = new javax.swing.JButton();
        jTextFieldSupplierTab2DateFrom = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jTextFieldSupplierTab2DateTo = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jPanel77 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabelSupplierTab2TotalAmount = new javax.swing.JLabel();
        jPanel78 = new javax.swing.JPanel();
        jPanel79 = new javax.swing.JPanel();
        jScrollPane24 = new javax.swing.JScrollPane();
        jListSupplierTab2Supplier = new javax.swing.JList();
        jPanel80 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabelSupplierTab2Name = new javax.swing.JLabel();
        jPanel82 = new javax.swing.JPanel();
        jButtonSupplierTab2Add = new javax.swing.JButton();
        jButtonSupplierTab2Edit = new javax.swing.JButton();
        jButtonSupplierTab2Remove = new javax.swing.JButton();
        jPanel81 = new javax.swing.JPanel();
        jScrollPane25 = new javax.swing.JScrollPane();
        jTableSupplierTab2ChequeTransaction = new javax.swing.JTable();
        jPanelSupplierTab3 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jButtonSupplierTab3DateChooser = new javax.swing.JButton();
        jTextFieldSupplierTab3DateFrom = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jTextFieldSupplierTab3DateTo = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jPanel47 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabelSupplierTab3TotalAmount = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        jListSupplierTab3Supplier = new javax.swing.JList();
        jPanel45 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabelSupplierTab3Name = new javax.swing.JLabel();
        jPanel50 = new javax.swing.JPanel();
        jButtonSupplierTab3Add = new javax.swing.JButton();
        jButtonSupplierTab3Edit = new javax.swing.JButton();
        jButtonSupplierTab3Remove = new javax.swing.JButton();
        jPanel49 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTableSupplierTab3FuelTransaction = new javax.swing.JTable();
        jPanelSupplierTab4 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jPanel52 = new javax.swing.JPanel();
        jButtonSupplierTab4DateChooser = new javax.swing.JButton();
        jTextFieldSupplierTab4DateFrom = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jTextFieldSupplierTab4DateTo = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jPanel53 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabelSupplierTab4TotalAmount = new javax.swing.JLabel();
        jPanel54 = new javax.swing.JPanel();
        jPanel55 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        jListSupplierTab4Supplier = new javax.swing.JList();
        jPanel56 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabelSupplierTab4Name = new javax.swing.JLabel();
        jPanel58 = new javax.swing.JPanel();
        jButtonSupplierTab4Add = new javax.swing.JButton();
        jButtonSupplierTab4Edit = new javax.swing.JButton();
        jButtonSupplierTab4Remove = new javax.swing.JButton();
        jPanel57 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTableSupplierTab4MiscTransaction = new javax.swing.JTable();
        jPanelSupplierTab5 = new javax.swing.JPanel();
        jPanel59 = new javax.swing.JPanel();
        jPanel60 = new javax.swing.JPanel();
        jButtonSupplierTab5DateChooser = new javax.swing.JButton();
        jTextFieldSupplierTab5DateFrom = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jTextFieldSupplierTab5DateTo = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jPanel61 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabelSupplierTab5TotalAmount = new javax.swing.JLabel();
        jPanel62 = new javax.swing.JPanel();
        jPanel63 = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        jListSupplierTab5Supplier = new javax.swing.JList();
        jPanel64 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabelSupplierTab5Name = new javax.swing.JLabel();
        jPanel66 = new javax.swing.JPanel();
        jButtonSupplierTab5Add = new javax.swing.JButton();
        jButtonSupplierTab5Edit = new javax.swing.JButton();
        jButtonSupplierTab5Remove = new javax.swing.JButton();
        jPanel65 = new javax.swing.JPanel();
        jScrollPane21 = new javax.swing.JScrollPane();
        jTableSupplierTab5CashTransaction = new javax.swing.JTable();
        jPanelSupplierTab6 = new javax.swing.JPanel();
        jPanel88 = new javax.swing.JPanel();
        jPanel89 = new javax.swing.JPanel();
        jButtonSupplierTab6DateChooser = new javax.swing.JButton();
        jTextFieldSupplierTab6DateFrom = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jTextFieldSupplierTab6DateTo = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jPanel90 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jLabelSupplierTab6TotalAmount = new javax.swing.JLabel();
        jPanel91 = new javax.swing.JPanel();
        jPanel92 = new javax.swing.JPanel();
        jScrollPane38 = new javax.swing.JScrollPane();
        jListSupplierTab6Supplier = new javax.swing.JList();
        jPanel93 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jLabelSupplierTab6Name = new javax.swing.JLabel();
        jPanel96 = new javax.swing.JPanel();
        jButtonSupplierTab6Add = new javax.swing.JButton();
        jButtonSupplierTab6Edit = new javax.swing.JButton();
        jButtonSupplierTab6Remove = new javax.swing.JButton();
        jPanel105 = new javax.swing.JPanel();
        jScrollPane39 = new javax.swing.JScrollPane();
        jTableSupplierTab6WithdrawalTransaction = new javax.swing.JTable();
        jPanelSupplierTab7 = new javax.swing.JPanel();
        jPanel67 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        jButtonSupplierTab7DateChooser = new javax.swing.JButton();
        jTextFieldSupplierTab7DateFrom = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jTextFieldSupplierTab7DateTo = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jPanel69 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jLabelSupplierTab7TotalAmount = new javax.swing.JLabel();
        jPanel70 = new javax.swing.JPanel();
        jPanel71 = new javax.swing.JPanel();
        jScrollPane22 = new javax.swing.JScrollPane();
        jListSupplierTab7Supplier = new javax.swing.JList();
        jPanel72 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabelSupplierTab7Name = new javax.swing.JLabel();
        jPanel74 = new javax.swing.JPanel();
        jButtonSupplierTab7Stock = new javax.swing.JButton();
        jButtonSupplierTab7Cash = new javax.swing.JButton();
        jButtonSupplierTab7Fuel = new javax.swing.JButton();
        jButtonSupplierTab7Cheque = new javax.swing.JButton();
        jButtonSupplierTab7Misc = new javax.swing.JButton();
        jButtonSupplierTab7Saving = new javax.swing.JButton();
        jButtonSupplierTab7Withdrawal = new javax.swing.JButton();
        jTextFieldSupplierTab7Stock = new javax.swing.JTextField();
        jTextFieldSupplierTab7Cash = new javax.swing.JTextField();
        jTextFieldSupplierTab7Fuel = new javax.swing.JTextField();
        jTextFieldSupplierTab7Cheque = new javax.swing.JTextField();
        jTextFieldSupplierTab7Misc = new javax.swing.JTextField();
        jTextFieldSupplierTab7Saving = new javax.swing.JTextField();
        jTextFieldSupplierTab7Withdrawal = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jTabbedPaneSupplierTab7 = new javax.swing.JTabbedPane();
        jPanelSupplierTab7Stock = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTableSupplierTab7Stock = new javax.swing.JTable();
        jPanelSupplierTab7Cash = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTableSupplierTab7Cash = new javax.swing.JTable();
        jPanelSupplierTab7Fuel = new javax.swing.JPanel();
        jScrollPane23 = new javax.swing.JScrollPane();
        jTableSupplierTab7Fuel = new javax.swing.JTable();
        jPanelSupplierTab7Cheque = new javax.swing.JPanel();
        jScrollPane26 = new javax.swing.JScrollPane();
        jTableSupplierTab7Cheque = new javax.swing.JTable();
        jPanelSupplierTab7Misc = new javax.swing.JPanel();
        jScrollPane27 = new javax.swing.JScrollPane();
        jTableSupplierTab7Misc = new javax.swing.JTable();
        jPanelSupplierTab7Saving = new javax.swing.JPanel();
        jScrollPane30 = new javax.swing.JScrollPane();
        jTableSupplierTab7Saving = new javax.swing.JTable();
        jPanelSupplierTab7Withdrawal = new javax.swing.JPanel();
        jScrollPane35 = new javax.swing.JScrollPane();
        jTableSupplierTab7Withdrawal = new javax.swing.JTable();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldSupplierTab7LastOwe = new javax.swing.JTextField();
        jPanel114 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldSupplierTab7LastSaving = new javax.swing.JTextField();
        jPanel38 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldSupplierTab7Balance = new javax.swing.JTextField();
        jPanel115 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldSupplierTab7TotalSaving = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jButtonSupplierTab7PrintAll = new javax.swing.JButton();
        jButtonSupplierTab7PrintSingle = new javax.swing.JButton();
        jButtonSupplierTab7PrintSaving = new javax.swing.JButton();
        jPanelSupplierTab8 = new javax.swing.JPanel();
        jPanel106 = new javax.swing.JPanel();
        jPanel107 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jTextFieldSupplierTab8Year = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jComboBoxSupplierTab8Month = new javax.swing.JComboBox();
        jButtonSupplierTab8ShowSummary = new javax.swing.JButton();
        jPanel108 = new javax.swing.JPanel();
        jPanel110 = new javax.swing.JPanel();
        jPanel111 = new javax.swing.JPanel();
        jScrollPane36 = new javax.swing.JScrollPane();
        jTableSupplierTab8Result = new javax.swing.JTable();
        jPanelCustomerTab1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jTextFieldCustomerTab1Filter = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableCustomerTab1Customer = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jButtonCustomerTab1Add = new javax.swing.JButton();
        jButtonCustomerTab1Edit = new javax.swing.JButton();
        jButtonCustomerTab1Delete = new javax.swing.JButton();
        jPanelCustomerTab2 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jListCustomerTab2Customer = new javax.swing.JList();
        jPanel73 = new javax.swing.JPanel();
        jPanel83 = new javax.swing.JPanel();
        jButtonCustomerTab2DateChooser = new javax.swing.JButton();
        jTextFieldCustomerTab2DateFrom = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jTextFieldCustomerTab2DateTo = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jPanel84 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jLabelCustomerTab2TotalAmount = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel85 = new javax.swing.JPanel();
        jPanel86 = new javax.swing.JPanel();
        jScrollPane28 = new javax.swing.JScrollPane();
        jTableCustomerTab2Payment = new javax.swing.JTable();
        jPanel27 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jPanel87 = new javax.swing.JPanel();
        jButtonCustomerTab2Add = new javax.swing.JButton();
        jButtonCustomerTab2Edit = new javax.swing.JButton();
        jButtonCustomerTab2Remove = new javax.swing.JButton();
        jPanel112 = new javax.swing.JPanel();
        jScrollPane37 = new javax.swing.JScrollPane();
        jTableCustomerTab2Report = new javax.swing.JTable();
        jPanel113 = new javax.swing.JPanel();
        jPanel116 = new javax.swing.JPanel();
        jButtonCustomerTab3ShowSummary1 = new javax.swing.JButton();
        jButtonCustomerTab3ShowSummary2 = new javax.swing.JButton();
        jPanel117 = new javax.swing.JPanel();
        jButtonCustomerTab2MonthlyReport2 = new javax.swing.JButton();
        jPanelCustomerTab4 = new javax.swing.JPanel();
        jPanel94 = new javax.swing.JPanel();
        jPanel95 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jTextFieldCustomerTab4Year = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jComboBoxCustomerTab4Month = new javax.swing.JComboBox();
        jButtonCustomerTab4ShowSummary = new javax.swing.JButton();
        jLabelCustomerTab4RecordSize = new javax.swing.JLabel();
        jPanel97 = new javax.swing.JPanel();
        jPanel98 = new javax.swing.JPanel();
        jScrollPane31 = new javax.swing.JScrollPane();
        jTableCustomerTab4Result = new javax.swing.JTable();
        jPanelReportTab2 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldReportTab2Date = new javax.swing.JTextField();
        jButtonReportTab2DatePicker = new javax.swing.JButton();
        jPanel41 = new javax.swing.JPanel();
        jButtonReportTab2ShowReport = new javax.swing.JButton();
        jPanel42 = new javax.swing.JPanel();
        jPanel99 = new javax.swing.JPanel();
        jScrollPane29 = new javax.swing.JScrollPane();
        jListReportTab2ItemSelection = new javax.swing.JList();
        jPanel100 = new javax.swing.JPanel();
        jButtonReportTab2RemoveSelection = new javax.swing.JButton();
        jButtonReportTab2RemoveAllSelections = new javax.swing.JButton();
        jPanel101 = new javax.swing.JPanel();
        jScrollPane32 = new javax.swing.JScrollPane();
        jListReportTab2Category = new javax.swing.JList();
        jPanel102 = new javax.swing.JPanel();
        jScrollPane33 = new javax.swing.JScrollPane();
        jListReportTab2Item = new javax.swing.JList();
        jPanel103 = new javax.swing.JPanel();
        jPanel104 = new javax.swing.JPanel();
        jScrollPane34 = new javax.swing.JScrollPane();
        jTextAreaReportTab2 = new javax.swing.JTextArea();
        jPanel109 = new javax.swing.JPanel();
        jPanel118 = new javax.swing.JPanel();
        jPanel119 = new javax.swing.JPanel();
        jButtonPatchSupplier = new javax.swing.JButton();
        jButtonPatchCustomer = new javax.swing.JButton();
        jPanel120 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextAreaPatchLog = new javax.swing.JTextArea();
        jPanel36 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1081, 80));

        jLabel1.setFont(new java.awt.Font("KaiTi", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("海春鱼行系统控制中心");

        jButtonExit.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonExit.setText("退出");
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 827, Short.MAX_VALUE)
                .addComponent(jButtonExit)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jTabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane.setToolTipText("");
        jTabbedPane.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneStateChanged(evt);
            }
        });

        jPanelItemTab1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "鱼类类别", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 158));

        jListItemTab1Category.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListItemTab1Category.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListItemTab1Category.setPreferredSize(new java.awt.Dimension(120, 155));
        jListItemTab1Category.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListItemTab1CategoryValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListItemTab1Category);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanelItemTab1.add(jPanel2, java.awt.BorderLayout.LINE_END);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTableItemTab1Item.setAutoCreateRowSorter(true);
        jTableItemTab1Item.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTableItemTab1Item.setModel(itemTab1ItemTableModel);
        jTableItemTab1Item.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableItemTab1Item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableItemTab1ItemMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableItemTab1Item);

        jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jButtonItemTab1Add.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonItemTab1Add.setText("新增");
        jButtonItemTab1Add.setPreferredSize(new java.awt.Dimension(120, 37));
        jButtonItemTab1Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonItemTab1AddActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonItemTab1Add);

        jButtonItemTab1Edit.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonItemTab1Edit.setText("修改");
        jButtonItemTab1Edit.setEnabled(false);
        jButtonItemTab1Edit.setPreferredSize(new java.awt.Dimension(120, 37));
        jButtonItemTab1Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonItemTab1EditActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonItemTab1Edit);

        jButtonItemTab1Delete.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonItemTab1Delete.setText("删除");
        jButtonItemTab1Delete.setEnabled(false);
        jButtonItemTab1Delete.setPreferredSize(new java.awt.Dimension(120, 37));
        jButtonItemTab1Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonItemTab1DeleteActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonItemTab1Delete);

        jPanel3.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jPanelItemTab1.add(jPanel3, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("鱼类资料", jPanelItemTab1);

        jPanelItemTab2.setPreferredSize(new java.awt.Dimension(1117, 800));
        jPanelItemTab2.setLayout(new java.awt.BorderLayout());

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabel14.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel14.setText("日期：");
        jPanel28.add(jLabel14);

        jTextFieldItemTab2Date.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jTextFieldItemTab2Date.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldItemTab2Date.setPreferredSize(new java.awt.Dimension(180, 35));
        jTextFieldItemTab2Date.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldItemTab2DateKeyReleased(evt);
            }
        });
        jPanel28.add(jTextFieldItemTab2Date);

        jButtonItemTab2DatePicker.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonItemTab2DatePicker.setText("选择");
        jButtonItemTab2DatePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonItemTab2DatePickerActionPerformed(evt);
            }
        });
        jPanel28.add(jButtonItemTab2DatePicker);

        jPanelItemTab2.add(jPanel28, java.awt.BorderLayout.PAGE_START);

        jPanel29.setPreferredSize(new java.awt.Dimension(400, 932));
        jPanel29.setLayout(new java.awt.GridLayout(1, 2));

        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "1.鱼类类别", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel31.setLayout(new java.awt.BorderLayout());

        jListItemTab2Category.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListItemTab2Category.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { " " };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jListItemTab2Category.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListItemTab2Category.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListItemTab2CategoryValueChanged(evt);
            }
        });
        jScrollPane11.setViewportView(jListItemTab2Category);

        jPanel31.add(jScrollPane11, java.awt.BorderLayout.CENTER);

        jPanel29.add(jPanel31);

        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "2.鱼类", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel32.setLayout(new java.awt.BorderLayout());

        jListItemTab2Item.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListItemTab2Item.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListItemTab2Item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListItemTab2ItemMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(jListItemTab2Item);

        jPanel32.add(jScrollPane12, java.awt.BorderLayout.CENTER);

        jPanel29.add(jPanel32);

        jPanelItemTab2.add(jPanel29, java.awt.BorderLayout.LINE_END);

        jPanel30.setLayout(new java.awt.BorderLayout());

        jPanel33.setPreferredSize(new java.awt.Dimension(743, 220));

        jLabel13.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel13.setText("鱼类名称：");

        jTextFieldItemTab2ItemName.setEditable(false);
        jTextFieldItemTab2ItemName.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N

        jLabel15.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel15.setText("鱼类原价：");

        jTextFieldItemTab2OldPrice.setEditable(false);
        jTextFieldItemTab2OldPrice.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldItemTab2OldPrice.setToolTipText("");

        jLabel16.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel16.setText("鱼类新价：");

        jTextFieldItemTab2NewPrice.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N

        jButtonItemTab2SaveChange.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonItemTab2SaveChange.setText("确定更改");
        jButtonItemTab2SaveChange.setToolTipText("");
        jButtonItemTab2SaveChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonItemTab2SaveChangeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldItemTab2ItemName))
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldItemTab2OldPrice))
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel33Layout.createSequentialGroup()
                                .addComponent(jButtonItemTab2SaveChange)
                                .addGap(0, 626, Short.MAX_VALUE))
                            .addComponent(jTextFieldItemTab2NewPrice))))
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextFieldItemTab2ItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jTextFieldItemTab2OldPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jTextFieldItemTab2NewPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonItemTab2SaveChange)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel30.add(jPanel33, java.awt.BorderLayout.PAGE_START);

        jTableItemTab2SupplierItem.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jTableItemTab2SupplierItem.setModel(itemTab2PriceChangeTableModel);
        jScrollPane13.setViewportView(jTableItemTab2SupplierItem);

        jPanel30.add(jScrollPane13, java.awt.BorderLayout.CENTER);

        jPanelItemTab2.add(jPanel30, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("鱼类价格", jPanelItemTab2);

        jPanelSupplierTab1.setLayout(new java.awt.BorderLayout());

        jPanel7.setLayout(new java.awt.BorderLayout(5, 5));

        jTextFieldSupplierTab1Filter.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldSupplierTab1Filter.setPreferredSize(new java.awt.Dimension(138, 45));
        jPanel7.add(jTextFieldSupplierTab1Filter, java.awt.BorderLayout.CENTER);

        jLabel2.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabel2.setText("过滤（id）");
        jPanel7.add(jLabel2, java.awt.BorderLayout.LINE_START);

        jPanelSupplierTab1.add(jPanel7, java.awt.BorderLayout.NORTH);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "船户", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel5.setLayout(new java.awt.BorderLayout());

        jTableSupplierTab1Supplier.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTableSupplierTab1Supplier.setModel(supplierTab1SupplierTableModel);
        jTableSupplierTab1Supplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableSupplierTab1SupplierMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableSupplierTab1Supplier);

        jPanel5.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanelSupplierTab1.add(jPanel5, java.awt.BorderLayout.CENTER);

        jButtonSupplierTab1Add.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonSupplierTab1Add.setText("新增");
        jButtonSupplierTab1Add.setPreferredSize(new java.awt.Dimension(120, 37));
        jButtonSupplierTab1Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab1AddActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonSupplierTab1Add);

        jButtonSupplierTab1Edit.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonSupplierTab1Edit.setText("修改");
        jButtonSupplierTab1Edit.setEnabled(false);
        jButtonSupplierTab1Edit.setPreferredSize(new java.awt.Dimension(120, 37));
        jButtonSupplierTab1Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab1EditActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonSupplierTab1Edit);

        jButtonSupplierTab1Delete.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonSupplierTab1Delete.setText("删除");
        jButtonSupplierTab1Delete.setEnabled(false);
        jButtonSupplierTab1Delete.setPreferredSize(new java.awt.Dimension(120, 37));
        jButtonSupplierTab1Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab1DeleteActionPerformed(evt);
            }
        });
        jPanel6.add(jButtonSupplierTab1Delete);

        jPanelSupplierTab1.add(jPanel6, java.awt.BorderLayout.SOUTH);

        jTabbedPane.addTab("船户资料", jPanelSupplierTab1);

        jPanelSupplierTab2.setLayout(new java.awt.BorderLayout());

        jPanel75.setLayout(new java.awt.GridLayout(1, 2));

        jPanel76.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jButtonSupplierTab2DateChooser.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonSupplierTab2DateChooser.setText("选择日期");
        jButtonSupplierTab2DateChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab2DateChooserActionPerformed(evt);
            }
        });
        jPanel76.add(jButtonSupplierTab2DateChooser);

        jTextFieldSupplierTab2DateFrom.setEditable(false);
        jTextFieldSupplierTab2DateFrom.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldSupplierTab2DateFrom.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSupplierTab2DateFrom.setPreferredSize(new java.awt.Dimension(200, 35));
        jTextFieldSupplierTab2DateFrom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldSupplierTab2DateFromMouseClicked(evt);
            }
        });
        jPanel76.add(jTextFieldSupplierTab2DateFrom);

        jLabel40.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel40.setText("至");
        jPanel76.add(jLabel40);

        jTextFieldSupplierTab2DateTo.setEditable(false);
        jTextFieldSupplierTab2DateTo.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldSupplierTab2DateTo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSupplierTab2DateTo.setPreferredSize(new java.awt.Dimension(200, 35));
        jTextFieldSupplierTab2DateTo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldSupplierTab2DateFromMouseClicked(evt);
            }
        });
        jPanel76.add(jTextFieldSupplierTab2DateTo);

        jLabel41.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel41.setText("止");
        jPanel76.add(jLabel41);

        jPanel75.add(jPanel76);

        jPanel77.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jLabel42.setFont(new java.awt.Font("KaiTi", 0, 32)); // NOI18N
        jLabel42.setText("总额:  RM");
        jPanel77.add(jLabel42);

        jLabelSupplierTab2TotalAmount.setFont(new java.awt.Font("KaiTi", 0, 32)); // NOI18N
        jLabelSupplierTab2TotalAmount.setText("0.00");
        jPanel77.add(jLabelSupplierTab2TotalAmount);

        jPanel75.add(jPanel77);

        jPanelSupplierTab2.add(jPanel75, java.awt.BorderLayout.PAGE_START);

        jPanel78.setPreferredSize(new java.awt.Dimension(500, 953));
        jPanel78.setLayout(new java.awt.BorderLayout());

        jPanel79.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "船户名单", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel79.setLayout(new java.awt.BorderLayout());

        jListSupplierTab2Supplier.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListSupplierTab2Supplier.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListSupplierTab2Supplier.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListSupplierTab2SupplierValueChanged(evt);
            }
        });
        jScrollPane24.setViewportView(jListSupplierTab2Supplier);

        jPanel79.add(jScrollPane24, java.awt.BorderLayout.CENTER);

        jPanel78.add(jPanel79, java.awt.BorderLayout.CENTER);

        jPanelSupplierTab2.add(jPanel78, java.awt.BorderLayout.LINE_START);

        jPanel80.setLayout(new java.awt.BorderLayout());

        jPanel12.setLayout(new java.awt.GridLayout(1, 2));

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabelSupplierTab2Name.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jPanel13.add(jLabelSupplierTab2Name);

        jPanel12.add(jPanel13);

        jPanel82.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jButtonSupplierTab2Add.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab2Add.setText("新增交易");
        jButtonSupplierTab2Add.setEnabled(false);
        jButtonSupplierTab2Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab2AddActionPerformed(evt);
            }
        });
        jPanel82.add(jButtonSupplierTab2Add);

        jButtonSupplierTab2Edit.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab2Edit.setText("更改交易");
        jButtonSupplierTab2Edit.setEnabled(false);
        jButtonSupplierTab2Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab2EditActionPerformed(evt);
            }
        });
        jPanel82.add(jButtonSupplierTab2Edit);

        jButtonSupplierTab2Remove.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab2Remove.setText("删除交易");
        jButtonSupplierTab2Remove.setEnabled(false);
        jButtonSupplierTab2Remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab2RemoveActionPerformed(evt);
            }
        });
        jPanel82.add(jButtonSupplierTab2Remove);

        jPanel12.add(jPanel82);

        jPanel80.add(jPanel12, java.awt.BorderLayout.NORTH);

        jPanel81.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "支银交易", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel81.setLayout(new java.awt.BorderLayout());

        jTableSupplierTab2ChequeTransaction.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTableSupplierTab2ChequeTransaction.setModel(supplierTab2ChequeTableModel);
        jTableSupplierTab2ChequeTransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableSupplierTab2ChequeTransactionMouseClicked(evt);
            }
        });
        jScrollPane25.setViewportView(jTableSupplierTab2ChequeTransaction);

        jPanel81.add(jScrollPane25, java.awt.BorderLayout.CENTER);

        jPanel80.add(jPanel81, java.awt.BorderLayout.CENTER);

        jPanelSupplierTab2.add(jPanel80, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("船户支银", jPanelSupplierTab2);

        jPanelSupplierTab3.setLayout(new java.awt.BorderLayout());

        jPanel43.setLayout(new java.awt.GridLayout(1, 2));

        jPanel46.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jButtonSupplierTab3DateChooser.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonSupplierTab3DateChooser.setText("选择日期");
        jButtonSupplierTab3DateChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab3DateChooserActionPerformed(evt);
            }
        });
        jPanel46.add(jButtonSupplierTab3DateChooser);

        jTextFieldSupplierTab3DateFrom.setEditable(false);
        jTextFieldSupplierTab3DateFrom.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldSupplierTab3DateFrom.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSupplierTab3DateFrom.setPreferredSize(new java.awt.Dimension(200, 35));
        jTextFieldSupplierTab3DateFrom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldSupplierTab3DateToMouseClicked(evt);
            }
        });
        jPanel46.add(jTextFieldSupplierTab3DateFrom);

        jLabel24.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel24.setText("至");
        jPanel46.add(jLabel24);

        jTextFieldSupplierTab3DateTo.setEditable(false);
        jTextFieldSupplierTab3DateTo.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldSupplierTab3DateTo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSupplierTab3DateTo.setPreferredSize(new java.awt.Dimension(200, 35));
        jTextFieldSupplierTab3DateTo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldSupplierTab3DateToMouseClicked(evt);
            }
        });
        jPanel46.add(jTextFieldSupplierTab3DateTo);

        jLabel25.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel25.setText("止");
        jPanel46.add(jLabel25);

        jPanel43.add(jPanel46);

        jPanel47.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jLabel27.setFont(new java.awt.Font("KaiTi", 0, 32)); // NOI18N
        jLabel27.setText("总额:  RM");
        jPanel47.add(jLabel27);

        jLabelSupplierTab3TotalAmount.setFont(new java.awt.Font("KaiTi", 0, 32)); // NOI18N
        jLabelSupplierTab3TotalAmount.setText("0.00");
        jPanel47.add(jLabelSupplierTab3TotalAmount);

        jPanel43.add(jPanel47);

        jPanelSupplierTab3.add(jPanel43, java.awt.BorderLayout.PAGE_START);

        jPanel44.setPreferredSize(new java.awt.Dimension(500, 953));
        jPanel44.setLayout(new java.awt.BorderLayout());

        jPanel48.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "船户名单", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel48.setLayout(new java.awt.BorderLayout());

        jListSupplierTab3Supplier.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListSupplierTab3Supplier.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListSupplierTab3Supplier.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListSupplierTab3SupplierValueChanged(evt);
            }
        });
        jScrollPane16.setViewportView(jListSupplierTab3Supplier);

        jPanel48.add(jScrollPane16, java.awt.BorderLayout.CENTER);

        jPanel44.add(jPanel48, java.awt.BorderLayout.CENTER);

        jPanelSupplierTab3.add(jPanel44, java.awt.BorderLayout.LINE_START);

        jPanel45.setLayout(new java.awt.BorderLayout());

        jPanel14.setLayout(new java.awt.GridLayout(1, 2));

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabelSupplierTab3Name.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jPanel15.add(jLabelSupplierTab3Name);

        jPanel14.add(jPanel15);

        jPanel50.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jButtonSupplierTab3Add.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab3Add.setText("新增交易");
        jButtonSupplierTab3Add.setEnabled(false);
        jButtonSupplierTab3Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab3AddActionPerformed(evt);
            }
        });
        jPanel50.add(jButtonSupplierTab3Add);

        jButtonSupplierTab3Edit.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab3Edit.setText("更改交易");
        jButtonSupplierTab3Edit.setEnabled(false);
        jButtonSupplierTab3Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab3EditActionPerformed(evt);
            }
        });
        jPanel50.add(jButtonSupplierTab3Edit);

        jButtonSupplierTab3Remove.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab3Remove.setText("删除交易");
        jButtonSupplierTab3Remove.setEnabled(false);
        jButtonSupplierTab3Remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab3RemoveActionPerformed(evt);
            }
        });
        jPanel50.add(jButtonSupplierTab3Remove);

        jPanel14.add(jPanel50);

        jPanel45.add(jPanel14, java.awt.BorderLayout.NORTH);

        jPanel49.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "柴油交易", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel49.setLayout(new java.awt.BorderLayout());

        jTableSupplierTab3FuelTransaction.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTableSupplierTab3FuelTransaction.setModel(supplierTab3FuelTableModel);
        jTableSupplierTab3FuelTransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableSupplierTab3FuelTransactionMouseClicked(evt);
            }
        });
        jScrollPane17.setViewportView(jTableSupplierTab3FuelTransaction);

        jPanel49.add(jScrollPane17, java.awt.BorderLayout.CENTER);

        jPanel45.add(jPanel49, java.awt.BorderLayout.CENTER);

        jPanelSupplierTab3.add(jPanel45, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("船户柴油", jPanelSupplierTab3);

        jPanelSupplierTab4.setToolTipText("");
        jPanelSupplierTab4.setLayout(new java.awt.BorderLayout());

        jPanel51.setLayout(new java.awt.GridLayout(1, 2));

        jPanel52.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jButtonSupplierTab4DateChooser.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonSupplierTab4DateChooser.setText("选择日期");
        jButtonSupplierTab4DateChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab4DateChooserActionPerformed(evt);
            }
        });
        jPanel52.add(jButtonSupplierTab4DateChooser);

        jTextFieldSupplierTab4DateFrom.setEditable(false);
        jTextFieldSupplierTab4DateFrom.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldSupplierTab4DateFrom.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSupplierTab4DateFrom.setPreferredSize(new java.awt.Dimension(200, 35));
        jTextFieldSupplierTab4DateFrom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldSupplierTab4DateFromMouseClicked(evt);
            }
        });
        jPanel52.add(jTextFieldSupplierTab4DateFrom);

        jLabel29.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel29.setText("至");
        jPanel52.add(jLabel29);

        jTextFieldSupplierTab4DateTo.setEditable(false);
        jTextFieldSupplierTab4DateTo.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldSupplierTab4DateTo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSupplierTab4DateTo.setPreferredSize(new java.awt.Dimension(200, 35));
        jTextFieldSupplierTab4DateTo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldSupplierTab4DateFromMouseClicked(evt);
            }
        });
        jPanel52.add(jTextFieldSupplierTab4DateTo);

        jLabel30.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel30.setText("止");
        jPanel52.add(jLabel30);

        jPanel51.add(jPanel52);

        jPanel53.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jLabel31.setFont(new java.awt.Font("KaiTi", 0, 32)); // NOI18N
        jLabel31.setText("总额:  RM");
        jPanel53.add(jLabel31);

        jLabelSupplierTab4TotalAmount.setFont(new java.awt.Font("KaiTi", 0, 32)); // NOI18N
        jLabelSupplierTab4TotalAmount.setText("0.00");
        jPanel53.add(jLabelSupplierTab4TotalAmount);

        jPanel51.add(jPanel53);

        jPanelSupplierTab4.add(jPanel51, java.awt.BorderLayout.PAGE_START);

        jPanel54.setPreferredSize(new java.awt.Dimension(500, 953));
        jPanel54.setLayout(new java.awt.BorderLayout());

        jPanel55.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "船户名单", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel55.setLayout(new java.awt.BorderLayout());

        jListSupplierTab4Supplier.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListSupplierTab4Supplier.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListSupplierTab4Supplier.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListSupplierTab4SupplierValueChanged(evt);
            }
        });
        jScrollPane18.setViewportView(jListSupplierTab4Supplier);

        jPanel55.add(jScrollPane18, java.awt.BorderLayout.CENTER);

        jPanel54.add(jPanel55, java.awt.BorderLayout.CENTER);

        jPanelSupplierTab4.add(jPanel54, java.awt.BorderLayout.LINE_START);

        jPanel56.setLayout(new java.awt.BorderLayout());

        jPanel16.setLayout(new java.awt.GridLayout(1, 2));

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabelSupplierTab4Name.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jPanel17.add(jLabelSupplierTab4Name);

        jPanel16.add(jPanel17);

        jPanel58.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jButtonSupplierTab4Add.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab4Add.setText("新增交易");
        jButtonSupplierTab4Add.setEnabled(false);
        jButtonSupplierTab4Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab4AddActionPerformed(evt);
            }
        });
        jPanel58.add(jButtonSupplierTab4Add);

        jButtonSupplierTab4Edit.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab4Edit.setText("更改交易");
        jButtonSupplierTab4Edit.setEnabled(false);
        jButtonSupplierTab4Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab4EditActionPerformed(evt);
            }
        });
        jPanel58.add(jButtonSupplierTab4Edit);

        jButtonSupplierTab4Remove.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab4Remove.setText("删除交易");
        jButtonSupplierTab4Remove.setEnabled(false);
        jButtonSupplierTab4Remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab4RemoveActionPerformed(evt);
            }
        });
        jPanel58.add(jButtonSupplierTab4Remove);

        jPanel16.add(jPanel58);

        jPanel56.add(jPanel16, java.awt.BorderLayout.NORTH);

        jPanel57.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "什费交易", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel57.setLayout(new java.awt.BorderLayout());

        jTableSupplierTab4MiscTransaction.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTableSupplierTab4MiscTransaction.setModel(supplierTab4MiscTableModel);
        jTableSupplierTab4MiscTransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableSupplierTab4MiscTransactionMouseClicked(evt);
            }
        });
        jScrollPane19.setViewportView(jTableSupplierTab4MiscTransaction);

        jPanel57.add(jScrollPane19, java.awt.BorderLayout.CENTER);

        jPanel56.add(jPanel57, java.awt.BorderLayout.CENTER);

        jPanelSupplierTab4.add(jPanel56, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("船户什费", jPanelSupplierTab4);

        jPanelSupplierTab5.setLayout(new java.awt.BorderLayout());

        jPanel59.setLayout(new java.awt.GridLayout(1, 2));

        jPanel60.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jButtonSupplierTab5DateChooser.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonSupplierTab5DateChooser.setText("选择日期");
        jButtonSupplierTab5DateChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab5DateChooserActionPerformed(evt);
            }
        });
        jPanel60.add(jButtonSupplierTab5DateChooser);

        jTextFieldSupplierTab5DateFrom.setEditable(false);
        jTextFieldSupplierTab5DateFrom.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldSupplierTab5DateFrom.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSupplierTab5DateFrom.setPreferredSize(new java.awt.Dimension(200, 35));
        jTextFieldSupplierTab5DateFrom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldSupplierTab5DateFromMouseClicked(evt);
            }
        });
        jPanel60.add(jTextFieldSupplierTab5DateFrom);

        jLabel32.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel32.setText("至");
        jPanel60.add(jLabel32);

        jTextFieldSupplierTab5DateTo.setEditable(false);
        jTextFieldSupplierTab5DateTo.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldSupplierTab5DateTo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSupplierTab5DateTo.setPreferredSize(new java.awt.Dimension(200, 35));
        jTextFieldSupplierTab5DateTo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldSupplierTab5DateFromMouseClicked(evt);
            }
        });
        jPanel60.add(jTextFieldSupplierTab5DateTo);

        jLabel33.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel33.setText("止");
        jPanel60.add(jLabel33);

        jPanel59.add(jPanel60);

        jPanel61.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jLabel34.setFont(new java.awt.Font("KaiTi", 0, 32)); // NOI18N
        jLabel34.setText("总额:  RM");
        jPanel61.add(jLabel34);

        jLabelSupplierTab5TotalAmount.setFont(new java.awt.Font("KaiTi", 0, 32)); // NOI18N
        jLabelSupplierTab5TotalAmount.setText("0.00");
        jPanel61.add(jLabelSupplierTab5TotalAmount);

        jPanel59.add(jPanel61);

        jPanelSupplierTab5.add(jPanel59, java.awt.BorderLayout.PAGE_START);

        jPanel62.setPreferredSize(new java.awt.Dimension(500, 953));
        jPanel62.setLayout(new java.awt.BorderLayout());

        jPanel63.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "船户名单", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel63.setLayout(new java.awt.BorderLayout());

        jListSupplierTab5Supplier.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListSupplierTab5Supplier.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListSupplierTab5Supplier.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListSupplierTab5SupplierValueChanged(evt);
            }
        });
        jScrollPane20.setViewportView(jListSupplierTab5Supplier);

        jPanel63.add(jScrollPane20, java.awt.BorderLayout.CENTER);

        jPanel62.add(jPanel63, java.awt.BorderLayout.CENTER);

        jPanelSupplierTab5.add(jPanel62, java.awt.BorderLayout.LINE_START);

        jPanel64.setLayout(new java.awt.BorderLayout());

        jPanel18.setLayout(new java.awt.GridLayout(1, 2));

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabelSupplierTab5Name.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jPanel19.add(jLabelSupplierTab5Name);

        jPanel18.add(jPanel19);

        jPanel66.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jButtonSupplierTab5Add.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab5Add.setText("新增交易");
        jButtonSupplierTab5Add.setEnabled(false);
        jButtonSupplierTab5Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab5AddActionPerformed(evt);
            }
        });
        jPanel66.add(jButtonSupplierTab5Add);

        jButtonSupplierTab5Edit.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab5Edit.setText("更改交易");
        jButtonSupplierTab5Edit.setEnabled(false);
        jButtonSupplierTab5Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab5EditActionPerformed(evt);
            }
        });
        jPanel66.add(jButtonSupplierTab5Edit);

        jButtonSupplierTab5Remove.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab5Remove.setText("删除交易");
        jButtonSupplierTab5Remove.setEnabled(false);
        jButtonSupplierTab5Remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab5RemoveActionPerformed(evt);
            }
        });
        jPanel66.add(jButtonSupplierTab5Remove);

        jPanel18.add(jPanel66);

        jPanel64.add(jPanel18, java.awt.BorderLayout.NORTH);

        jPanel65.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "来银交易", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel65.setLayout(new java.awt.BorderLayout());

        jTableSupplierTab5CashTransaction.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTableSupplierTab5CashTransaction.setModel(supplierTab5CashTableModel);
        jTableSupplierTab5CashTransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableSupplierTab5CashTransactionMouseClicked(evt);
            }
        });
        jScrollPane21.setViewportView(jTableSupplierTab5CashTransaction);

        jPanel65.add(jScrollPane21, java.awt.BorderLayout.CENTER);

        jPanel64.add(jPanel65, java.awt.BorderLayout.CENTER);

        jPanelSupplierTab5.add(jPanel64, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("船户来银", jPanelSupplierTab5);

        jPanelSupplierTab6.setLayout(new java.awt.BorderLayout());

        jPanel88.setLayout(new java.awt.GridLayout(1, 2));

        jPanel89.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jButtonSupplierTab6DateChooser.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonSupplierTab6DateChooser.setText("选择日期");
        jButtonSupplierTab6DateChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab6DateChooserActionPerformed(evt);
            }
        });
        jPanel89.add(jButtonSupplierTab6DateChooser);

        jTextFieldSupplierTab6DateFrom.setEditable(false);
        jTextFieldSupplierTab6DateFrom.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldSupplierTab6DateFrom.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSupplierTab6DateFrom.setPreferredSize(new java.awt.Dimension(200, 35));
        jTextFieldSupplierTab6DateFrom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldSupplierTab6DateFromMouseClicked(evt);
            }
        });
        jPanel89.add(jTextFieldSupplierTab6DateFrom);

        jLabel45.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel45.setText("至");
        jPanel89.add(jLabel45);

        jTextFieldSupplierTab6DateTo.setEditable(false);
        jTextFieldSupplierTab6DateTo.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldSupplierTab6DateTo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSupplierTab6DateTo.setPreferredSize(new java.awt.Dimension(200, 35));
        jTextFieldSupplierTab6DateTo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldSupplierTab6DateTojTextFieldSupplierTab2DateFromMouseClicked(evt);
            }
        });
        jPanel89.add(jTextFieldSupplierTab6DateTo);

        jLabel46.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel46.setText("止");
        jPanel89.add(jLabel46);

        jPanel88.add(jPanel89);

        jPanel90.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jLabel51.setFont(new java.awt.Font("KaiTi", 0, 32)); // NOI18N
        jLabel51.setText("总额:  RM");
        jPanel90.add(jLabel51);

        jLabelSupplierTab6TotalAmount.setFont(new java.awt.Font("KaiTi", 0, 32)); // NOI18N
        jLabelSupplierTab6TotalAmount.setText("0.00");
        jPanel90.add(jLabelSupplierTab6TotalAmount);

        jPanel88.add(jPanel90);

        jPanelSupplierTab6.add(jPanel88, java.awt.BorderLayout.PAGE_START);

        jPanel91.setPreferredSize(new java.awt.Dimension(500, 953));
        jPanel91.setLayout(new java.awt.BorderLayout());

        jPanel92.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "船户名单", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel92.setLayout(new java.awt.BorderLayout());

        jListSupplierTab6Supplier.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListSupplierTab6Supplier.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListSupplierTab6Supplier.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListSupplierTab6SupplierValueChanged(evt);
            }
        });
        jScrollPane38.setViewportView(jListSupplierTab6Supplier);

        jPanel92.add(jScrollPane38, java.awt.BorderLayout.CENTER);

        jPanel91.add(jPanel92, java.awt.BorderLayout.CENTER);

        jPanelSupplierTab6.add(jPanel91, java.awt.BorderLayout.LINE_START);

        jPanel93.setLayout(new java.awt.BorderLayout());

        jPanel25.setLayout(new java.awt.GridLayout(1, 2));

        jPanel39.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabelSupplierTab6Name.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jPanel39.add(jLabelSupplierTab6Name);

        jPanel25.add(jPanel39);

        jPanel96.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jButtonSupplierTab6Add.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab6Add.setText("新增交易");
        jButtonSupplierTab6Add.setEnabled(false);
        jButtonSupplierTab6Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab6AddActionPerformed(evt);
            }
        });
        jPanel96.add(jButtonSupplierTab6Add);

        jButtonSupplierTab6Edit.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab6Edit.setText("更改交易");
        jButtonSupplierTab6Edit.setEnabled(false);
        jButtonSupplierTab6Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab6EditActionPerformed(evt);
            }
        });
        jPanel96.add(jButtonSupplierTab6Edit);

        jButtonSupplierTab6Remove.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab6Remove.setText("删除交易");
        jButtonSupplierTab6Remove.setEnabled(false);
        jButtonSupplierTab6Remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab6RemoveActionPerformed(evt);
            }
        });
        jPanel96.add(jButtonSupplierTab6Remove);

        jPanel25.add(jPanel96);

        jPanel93.add(jPanel25, java.awt.BorderLayout.NORTH);

        jPanel105.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "提款交易", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel105.setLayout(new java.awt.BorderLayout());

        jTableSupplierTab6WithdrawalTransaction.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTableSupplierTab6WithdrawalTransaction.setModel(supplierTab6WithdrawalTableModel);
        jTableSupplierTab6WithdrawalTransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableSupplierTab6WithdrawalTransactionMouseClicked(evt);
            }
        });
        jScrollPane39.setViewportView(jTableSupplierTab6WithdrawalTransaction);

        jPanel105.add(jScrollPane39, java.awt.BorderLayout.CENTER);

        jPanel93.add(jPanel105, java.awt.BorderLayout.CENTER);

        jPanelSupplierTab6.add(jPanel93, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("船户提款", jPanelSupplierTab6);

        jPanelSupplierTab7.setLayout(new java.awt.BorderLayout());

        jPanel67.setLayout(new java.awt.GridLayout(1, 2));

        jPanel68.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jButtonSupplierTab7DateChooser.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonSupplierTab7DateChooser.setText("选择日期");
        jButtonSupplierTab7DateChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab7DateChooserActionPerformed(evt);
            }
        });
        jPanel68.add(jButtonSupplierTab7DateChooser);

        jTextFieldSupplierTab7DateFrom.setEditable(false);
        jTextFieldSupplierTab7DateFrom.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldSupplierTab7DateFrom.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSupplierTab7DateFrom.setPreferredSize(new java.awt.Dimension(200, 35));
        jTextFieldSupplierTab7DateFrom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldSupplierTab7DateFromMouseClicked(evt);
            }
        });
        jPanel68.add(jTextFieldSupplierTab7DateFrom);

        jLabel36.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel36.setText("至");
        jPanel68.add(jLabel36);

        jTextFieldSupplierTab7DateTo.setEditable(false);
        jTextFieldSupplierTab7DateTo.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldSupplierTab7DateTo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSupplierTab7DateTo.setPreferredSize(new java.awt.Dimension(200, 35));
        jTextFieldSupplierTab7DateTo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldSupplierTab7DateFromMouseClicked(evt);
            }
        });
        jPanel68.add(jTextFieldSupplierTab7DateTo);

        jLabel37.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel37.setText("止");
        jPanel68.add(jLabel37);

        jPanel67.add(jPanel68);

        jPanel69.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jLabel38.setFont(new java.awt.Font("KaiTi", 0, 32)); // NOI18N
        jLabel38.setText("总额:  RM");
        jPanel69.add(jLabel38);

        jLabelSupplierTab7TotalAmount.setFont(new java.awt.Font("KaiTi", 0, 32)); // NOI18N
        jLabelSupplierTab7TotalAmount.setText("0.00");
        jPanel69.add(jLabelSupplierTab7TotalAmount);

        jPanel67.add(jPanel69);

        jPanelSupplierTab7.add(jPanel67, java.awt.BorderLayout.PAGE_START);

        jPanel70.setPreferredSize(new java.awt.Dimension(500, 953));
        jPanel70.setLayout(new java.awt.BorderLayout());

        jPanel71.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "船户名单", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel71.setLayout(new java.awt.BorderLayout());

        jListSupplierTab7Supplier.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListSupplierTab7Supplier.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListSupplierTab7Supplier.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListSupplierTab7SupplierValueChanged(evt);
            }
        });
        jScrollPane22.setViewportView(jListSupplierTab7Supplier);

        jPanel71.add(jScrollPane22, java.awt.BorderLayout.CENTER);

        jPanel70.add(jPanel71, java.awt.BorderLayout.CENTER);

        jPanelSupplierTab7.add(jPanel70, java.awt.BorderLayout.LINE_START);

        jPanel72.setLayout(new java.awt.BorderLayout());

        jPanel20.setLayout(new java.awt.BorderLayout());

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabelSupplierTab7Name.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jPanel21.add(jLabelSupplierTab7Name);

        jPanel20.add(jPanel21, java.awt.BorderLayout.NORTH);

        jPanel74.setLayout(new java.awt.GridLayout(2, 7, 3, 3));

        jButtonSupplierTab7Stock.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab7Stock.setText("来货");
        jButtonSupplierTab7Stock.setEnabled(false);
        jButtonSupplierTab7Stock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab7StockActionPerformed(evt);
            }
        });
        jPanel74.add(jButtonSupplierTab7Stock);

        jButtonSupplierTab7Cash.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab7Cash.setText("来银");
        jButtonSupplierTab7Cash.setEnabled(false);
        jButtonSupplierTab7Cash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab7CashActionPerformed(evt);
            }
        });
        jPanel74.add(jButtonSupplierTab7Cash);

        jButtonSupplierTab7Fuel.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab7Fuel.setText("柴油");
        jButtonSupplierTab7Fuel.setEnabled(false);
        jButtonSupplierTab7Fuel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab7FuelActionPerformed(evt);
            }
        });
        jPanel74.add(jButtonSupplierTab7Fuel);

        jButtonSupplierTab7Cheque.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab7Cheque.setText("支银");
        jButtonSupplierTab7Cheque.setEnabled(false);
        jButtonSupplierTab7Cheque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab7ChequeActionPerformed(evt);
            }
        });
        jPanel74.add(jButtonSupplierTab7Cheque);

        jButtonSupplierTab7Misc.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab7Misc.setText("什费");
        jButtonSupplierTab7Misc.setEnabled(false);
        jButtonSupplierTab7Misc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab7MiscActionPerformed(evt);
            }
        });
        jPanel74.add(jButtonSupplierTab7Misc);

        jButtonSupplierTab7Saving.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab7Saving.setText("储蓄");
        jButtonSupplierTab7Saving.setEnabled(false);
        jButtonSupplierTab7Saving.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab7SavingActionPerformed(evt);
            }
        });
        jPanel74.add(jButtonSupplierTab7Saving);

        jButtonSupplierTab7Withdrawal.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonSupplierTab7Withdrawal.setText("提款");
        jButtonSupplierTab7Withdrawal.setToolTipText("");
        jButtonSupplierTab7Withdrawal.setEnabled(false);
        jButtonSupplierTab7Withdrawal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab7WithdrawalActionPerformed(evt);
            }
        });
        jPanel74.add(jButtonSupplierTab7Withdrawal);

        jTextFieldSupplierTab7Stock.setEditable(false);
        jTextFieldSupplierTab7Stock.setBackground(new java.awt.Color(0, 0, 0));
        jTextFieldSupplierTab7Stock.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextFieldSupplierTab7Stock.setForeground(new java.awt.Color(255, 255, 0));
        jTextFieldSupplierTab7Stock.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel74.add(jTextFieldSupplierTab7Stock);

        jTextFieldSupplierTab7Cash.setEditable(false);
        jTextFieldSupplierTab7Cash.setBackground(new java.awt.Color(0, 0, 0));
        jTextFieldSupplierTab7Cash.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextFieldSupplierTab7Cash.setForeground(new java.awt.Color(255, 255, 0));
        jTextFieldSupplierTab7Cash.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel74.add(jTextFieldSupplierTab7Cash);

        jTextFieldSupplierTab7Fuel.setEditable(false);
        jTextFieldSupplierTab7Fuel.setBackground(new java.awt.Color(0, 0, 0));
        jTextFieldSupplierTab7Fuel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextFieldSupplierTab7Fuel.setForeground(new java.awt.Color(255, 255, 0));
        jTextFieldSupplierTab7Fuel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel74.add(jTextFieldSupplierTab7Fuel);

        jTextFieldSupplierTab7Cheque.setEditable(false);
        jTextFieldSupplierTab7Cheque.setBackground(new java.awt.Color(0, 0, 0));
        jTextFieldSupplierTab7Cheque.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextFieldSupplierTab7Cheque.setForeground(new java.awt.Color(255, 255, 0));
        jTextFieldSupplierTab7Cheque.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel74.add(jTextFieldSupplierTab7Cheque);

        jTextFieldSupplierTab7Misc.setEditable(false);
        jTextFieldSupplierTab7Misc.setBackground(new java.awt.Color(0, 0, 0));
        jTextFieldSupplierTab7Misc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextFieldSupplierTab7Misc.setForeground(new java.awt.Color(255, 255, 0));
        jTextFieldSupplierTab7Misc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel74.add(jTextFieldSupplierTab7Misc);

        jTextFieldSupplierTab7Saving.setEditable(false);
        jTextFieldSupplierTab7Saving.setBackground(new java.awt.Color(0, 0, 0));
        jTextFieldSupplierTab7Saving.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextFieldSupplierTab7Saving.setForeground(new java.awt.Color(255, 255, 0));
        jTextFieldSupplierTab7Saving.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel74.add(jTextFieldSupplierTab7Saving);

        jTextFieldSupplierTab7Withdrawal.setEditable(false);
        jTextFieldSupplierTab7Withdrawal.setBackground(new java.awt.Color(0, 0, 0));
        jTextFieldSupplierTab7Withdrawal.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextFieldSupplierTab7Withdrawal.setForeground(new java.awt.Color(255, 255, 0));
        jTextFieldSupplierTab7Withdrawal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel74.add(jTextFieldSupplierTab7Withdrawal);

        jPanel20.add(jPanel74, java.awt.BorderLayout.CENTER);

        jPanel72.add(jPanel20, java.awt.BorderLayout.NORTH);

        jPanel22.setLayout(new java.awt.BorderLayout());

        jTabbedPaneSupplierTab7.setEnabled(false);
        jTabbedPaneSupplierTab7.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N

        jPanelSupplierTab7Stock.setLayout(new java.awt.BorderLayout());

        jTableSupplierTab7Stock.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jTableSupplierTab7Stock.setModel(supplierTab7StockTableModel);
        jScrollPane14.setViewportView(jTableSupplierTab7Stock);

        jPanelSupplierTab7Stock.add(jScrollPane14, java.awt.BorderLayout.CENTER);

        jTabbedPaneSupplierTab7.addTab("来货表", jPanelSupplierTab7Stock);

        jPanelSupplierTab7Cash.setLayout(new java.awt.BorderLayout());

        jTableSupplierTab7Cash.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTableSupplierTab7Cash.setModel(supplierTab7CashTableModel);
        jScrollPane15.setViewportView(jTableSupplierTab7Cash);

        jPanelSupplierTab7Cash.add(jScrollPane15, java.awt.BorderLayout.CENTER);

        jTabbedPaneSupplierTab7.addTab("来银表", jPanelSupplierTab7Cash);

        jPanelSupplierTab7Fuel.setLayout(new java.awt.BorderLayout());

        jTableSupplierTab7Fuel.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTableSupplierTab7Fuel.setModel(supplierTab7FuelTableModel);
        jScrollPane23.setViewportView(jTableSupplierTab7Fuel);

        jPanelSupplierTab7Fuel.add(jScrollPane23, java.awt.BorderLayout.CENTER);

        jTabbedPaneSupplierTab7.addTab("柴油表", jPanelSupplierTab7Fuel);

        jPanelSupplierTab7Cheque.setLayout(new java.awt.BorderLayout());

        jTableSupplierTab7Cheque.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTableSupplierTab7Cheque.setModel(supplierTab7ChequeTableModel);
        jScrollPane26.setViewportView(jTableSupplierTab7Cheque);

        jPanelSupplierTab7Cheque.add(jScrollPane26, java.awt.BorderLayout.CENTER);

        jTabbedPaneSupplierTab7.addTab("支银表", jPanelSupplierTab7Cheque);

        jPanelSupplierTab7Misc.setLayout(new java.awt.BorderLayout());

        jTableSupplierTab7Misc.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTableSupplierTab7Misc.setModel(supplierTab7MiscTableModel);
        jScrollPane27.setViewportView(jTableSupplierTab7Misc);

        jPanelSupplierTab7Misc.add(jScrollPane27, java.awt.BorderLayout.CENTER);

        jTabbedPaneSupplierTab7.addTab("什费表", jPanelSupplierTab7Misc);

        jPanelSupplierTab7Saving.setLayout(new java.awt.BorderLayout());

        jTableSupplierTab7Saving.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTableSupplierTab7Saving.setModel(supplierTab7SavingTableModel);
        jTableSupplierTab7Saving.setToolTipText("");
        jScrollPane30.setViewportView(jTableSupplierTab7Saving);

        jPanelSupplierTab7Saving.add(jScrollPane30, java.awt.BorderLayout.CENTER);

        jTabbedPaneSupplierTab7.addTab("储蓄", jPanelSupplierTab7Saving);

        jPanelSupplierTab7Withdrawal.setLayout(new java.awt.BorderLayout());

        jTableSupplierTab7Withdrawal.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTableSupplierTab7Withdrawal.setModel(supplierTab7WithdrawalTableModel);
        jTableSupplierTab7Withdrawal.setToolTipText("");
        jScrollPane35.setViewportView(jTableSupplierTab7Withdrawal);

        jPanelSupplierTab7Withdrawal.add(jScrollPane35, java.awt.BorderLayout.CENTER);

        jTabbedPaneSupplierTab7.addTab("提款", jPanelSupplierTab7Withdrawal);

        jPanel22.add(jTabbedPaneSupplierTab7, java.awt.BorderLayout.CENTER);

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "结算", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 1, 18))); // NOI18N
        jPanel23.setMinimumSize(new java.awt.Dimension(0, 100));
        jPanel23.setLayout(new java.awt.GridLayout(2, 2));

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabel4.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabel4.setText("上存/欠");
        jLabel4.setToolTipText("");
        jLabel4.setPreferredSize(new java.awt.Dimension(80, 21));
        jPanel24.add(jLabel4);

        jTextFieldSupplierTab7LastOwe.setEditable(false);
        jTextFieldSupplierTab7LastOwe.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextFieldSupplierTab7LastOwe.setPreferredSize(new java.awt.Dimension(200, 28));
        jPanel24.add(jTextFieldSupplierTab7LastOwe);

        jPanel23.add(jPanel24);

        jPanel114.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jLabel6.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("上存/欠");
        jLabel6.setToolTipText("");
        jLabel6.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel114.add(jLabel6);

        jTextFieldSupplierTab7LastSaving.setEditable(false);
        jTextFieldSupplierTab7LastSaving.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextFieldSupplierTab7LastSaving.setPreferredSize(new java.awt.Dimension(200, 28));
        jPanel114.add(jTextFieldSupplierTab7LastSaving);

        jPanel23.add(jPanel114);

        jPanel38.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabel5.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabel5.setText("总额");
        jLabel5.setToolTipText("");
        jLabel5.setPreferredSize(new java.awt.Dimension(80, 21));
        jPanel38.add(jLabel5);

        jTextFieldSupplierTab7Balance.setEditable(false);
        jTextFieldSupplierTab7Balance.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextFieldSupplierTab7Balance.setPreferredSize(new java.awt.Dimension(200, 28));
        jPanel38.add(jTextFieldSupplierTab7Balance);

        jPanel23.add(jPanel38);

        jPanel115.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jLabel7.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("储蓄总额");
        jLabel7.setToolTipText("");
        jLabel7.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel115.add(jLabel7);

        jTextFieldSupplierTab7TotalSaving.setEditable(false);
        jTextFieldSupplierTab7TotalSaving.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextFieldSupplierTab7TotalSaving.setPreferredSize(new java.awt.Dimension(200, 28));
        jPanel115.add(jTextFieldSupplierTab7TotalSaving);

        jPanel23.add(jPanel115);

        jPanel22.add(jPanel23, java.awt.BorderLayout.SOUTH);

        jPanel72.add(jPanel22, java.awt.BorderLayout.CENTER);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jButtonSupplierTab7PrintAll.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonSupplierTab7PrintAll.setText("全船打印");
        jButtonSupplierTab7PrintAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab7PrintAllActionPerformed(evt);
            }
        });
        jPanel34.add(jButtonSupplierTab7PrintAll);

        jButtonSupplierTab7PrintSingle.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonSupplierTab7PrintSingle.setText("单船打印");
        jButtonSupplierTab7PrintSingle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab7PrintSingleActionPerformed(evt);
            }
        });
        jPanel34.add(jButtonSupplierTab7PrintSingle);

        jButtonSupplierTab7PrintSaving.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonSupplierTab7PrintSaving.setText("储蓄打印");
        jButtonSupplierTab7PrintSaving.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab7PrintSavingActionPerformed(evt);
            }
        });
        jPanel34.add(jButtonSupplierTab7PrintSaving);

        jPanel72.add(jPanel34, java.awt.BorderLayout.PAGE_END);

        jPanelSupplierTab7.add(jPanel72, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("船户结单", jPanelSupplierTab7);

        jPanelSupplierTab8.setLayout(new java.awt.BorderLayout());

        jPanel106.setLayout(new java.awt.GridLayout(1, 2));

        jPanel107.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabel49.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel49.setText("年份");
        jPanel107.add(jLabel49);

        jTextFieldSupplierTab8Year.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldSupplierTab8Year.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSupplierTab8Year.setPreferredSize(new java.awt.Dimension(200, 35));
        jPanel107.add(jTextFieldSupplierTab8Year);

        jLabel50.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel50.setText("月份");
        jPanel107.add(jLabel50);

        jComboBoxSupplierTab8Month.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jComboBoxSupplierTab8Month.setMaximumRowCount(12);
        jComboBoxSupplierTab8Month.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" }));
        jComboBoxSupplierTab8Month.setPreferredSize(new java.awt.Dimension(200, 35));
        jPanel107.add(jComboBoxSupplierTab8Month);

        jButtonSupplierTab8ShowSummary.setFont(new java.awt.Font("KaiTi", 0, 20)); // NOI18N
        jButtonSupplierTab8ShowSummary.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/order.png"))); // NOI18N
        jButtonSupplierTab8ShowSummary.setText("列出");
        jButtonSupplierTab8ShowSummary.setPreferredSize(new java.awt.Dimension(150, 45));
        jButtonSupplierTab8ShowSummary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupplierTab8ShowSummaryActionPerformed(evt);
            }
        });
        jPanel107.add(jButtonSupplierTab8ShowSummary);

        jPanel106.add(jPanel107);

        jPanel108.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));
        jPanel106.add(jPanel108);

        jPanelSupplierTab8.add(jPanel106, java.awt.BorderLayout.PAGE_START);

        jPanel110.setLayout(new java.awt.BorderLayout());

        jPanel111.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel111.setLayout(new java.awt.BorderLayout());

        jTableSupplierTab8Result.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jTableSupplierTab8Result.setModel(supplierTab8SalesCheckTableModel);
        jTableSupplierTab8Result.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableSupplierTab8ResultMouseClicked(evt);
            }
        });
        jScrollPane36.setViewportView(jTableSupplierTab8Result);

        jPanel111.add(jScrollPane36, java.awt.BorderLayout.CENTER);

        jPanel110.add(jPanel111, java.awt.BorderLayout.CENTER);

        jPanelSupplierTab8.add(jPanel110, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("船户检查", jPanelSupplierTab8);

        jPanelCustomerTab1.setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new java.awt.BorderLayout(5, 5));

        jTextFieldCustomerTab1Filter.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldCustomerTab1Filter.setPreferredSize(new java.awt.Dimension(138, 45));
        jPanel8.add(jTextFieldCustomerTab1Filter, java.awt.BorderLayout.CENTER);

        jLabel3.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jLabel3.setText("过滤（id）");
        jPanel8.add(jLabel3, java.awt.BorderLayout.LINE_START);

        jPanelCustomerTab1.add(jPanel8, java.awt.BorderLayout.NORTH);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "客户", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel9.setLayout(new java.awt.BorderLayout());

        jTableCustomerTab1Customer.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTableCustomerTab1Customer.setModel(customerTab1CustomerTableModel);
        jTableCustomerTab1Customer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCustomerTab1CustomerMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableCustomerTab1Customer);

        jPanel9.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jPanelCustomerTab1.add(jPanel9, java.awt.BorderLayout.CENTER);

        jButtonCustomerTab1Add.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonCustomerTab1Add.setText("新增");
        jButtonCustomerTab1Add.setPreferredSize(new java.awt.Dimension(120, 37));
        jButtonCustomerTab1Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomerTab1AddActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonCustomerTab1Add);

        jButtonCustomerTab1Edit.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonCustomerTab1Edit.setText("修改");
        jButtonCustomerTab1Edit.setEnabled(false);
        jButtonCustomerTab1Edit.setPreferredSize(new java.awt.Dimension(120, 37));
        jButtonCustomerTab1Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomerTab1EditActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonCustomerTab1Edit);

        jButtonCustomerTab1Delete.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonCustomerTab1Delete.setText("删除");
        jButtonCustomerTab1Delete.setEnabled(false);
        jButtonCustomerTab1Delete.setPreferredSize(new java.awt.Dimension(120, 37));
        jButtonCustomerTab1Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomerTab1DeleteActionPerformed(evt);
            }
        });
        jPanel10.add(jButtonCustomerTab1Delete);

        jPanelCustomerTab1.add(jPanel10, java.awt.BorderLayout.SOUTH);

        jTabbedPane.addTab("客户资料", jPanelCustomerTab1);

        jPanelCustomerTab2.setLayout(new java.awt.BorderLayout());

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "客户名单", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel26.setPreferredSize(new java.awt.Dimension(450, 794));
        jPanel26.setLayout(new java.awt.BorderLayout());

        jListCustomerTab2Customer.setFont(new java.awt.Font("KaiTi", 0, 22)); // NOI18N
        jListCustomerTab2Customer.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListCustomerTab2Customer.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListCustomerTab2CustomerValueChanged(evt);
            }
        });
        jScrollPane10.setViewportView(jListCustomerTab2Customer);

        jPanel26.add(jScrollPane10, java.awt.BorderLayout.CENTER);

        jPanelCustomerTab2.add(jPanel26, java.awt.BorderLayout.LINE_START);

        jPanel73.setLayout(new java.awt.GridLayout(1, 2));

        jPanel83.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jButtonCustomerTab2DateChooser.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonCustomerTab2DateChooser.setText("选择日期");
        jButtonCustomerTab2DateChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomerTab2DateChooserActionPerformed(evt);
            }
        });
        jPanel83.add(jButtonCustomerTab2DateChooser);

        jTextFieldCustomerTab2DateFrom.setEditable(false);
        jTextFieldCustomerTab2DateFrom.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldCustomerTab2DateFrom.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldCustomerTab2DateFrom.setPreferredSize(new java.awt.Dimension(200, 35));
        jTextFieldCustomerTab2DateFrom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldCustomerTab2DateFromMouseClicked(evt);
            }
        });
        jPanel83.add(jTextFieldCustomerTab2DateFrom);

        jLabel39.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel39.setText("至");
        jPanel83.add(jLabel39);

        jTextFieldCustomerTab2DateTo.setEditable(false);
        jTextFieldCustomerTab2DateTo.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldCustomerTab2DateTo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldCustomerTab2DateTo.setPreferredSize(new java.awt.Dimension(200, 35));
        jTextFieldCustomerTab2DateTo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldCustomerTab2DateToMouseClicked(evt);
            }
        });
        jPanel83.add(jTextFieldCustomerTab2DateTo);

        jLabel43.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel43.setText("止");
        jPanel83.add(jLabel43);

        jPanel73.add(jPanel83);

        jPanel84.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jLabel44.setFont(new java.awt.Font("KaiTi", 0, 32)); // NOI18N
        jLabel44.setText("总额:  RM");
        jPanel84.add(jLabel44);

        jLabelCustomerTab2TotalAmount.setFont(new java.awt.Font("KaiTi", 0, 32)); // NOI18N
        jLabelCustomerTab2TotalAmount.setText("0.00");
        jPanel84.add(jLabelCustomerTab2TotalAmount);

        jPanel73.add(jPanel84);

        jPanelCustomerTab2.add(jPanel73, java.awt.BorderLayout.PAGE_START);

        jPanel11.setLayout(new java.awt.GridLayout(2, 1));

        jPanel85.setLayout(new java.awt.BorderLayout());

        jPanel86.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "付款交易", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel86.setLayout(new java.awt.BorderLayout());

        jTableCustomerTab2Payment.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jTableCustomerTab2Payment.setModel(customerTab2PaymentTableModel);
        jTableCustomerTab2Payment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCustomerTab2PaymentMouseClicked(evt);
            }
        });
        jScrollPane28.setViewportView(jTableCustomerTab2Payment);

        jPanel86.add(jScrollPane28, java.awt.BorderLayout.CENTER);

        jPanel85.add(jPanel86, java.awt.BorderLayout.CENTER);

        jPanel27.setLayout(new java.awt.GridLayout(1, 2));

        jPanel35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));
        jPanel27.add(jPanel35);

        jPanel87.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jButtonCustomerTab2Add.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonCustomerTab2Add.setText("新增交易");
        jButtonCustomerTab2Add.setEnabled(false);
        jButtonCustomerTab2Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomerTab2AddActionPerformed(evt);
            }
        });
        jPanel87.add(jButtonCustomerTab2Add);

        jButtonCustomerTab2Edit.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonCustomerTab2Edit.setText("更改交易");
        jButtonCustomerTab2Edit.setEnabled(false);
        jButtonCustomerTab2Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomerTab2EditActionPerformed(evt);
            }
        });
        jPanel87.add(jButtonCustomerTab2Edit);

        jButtonCustomerTab2Remove.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonCustomerTab2Remove.setText("删除交易");
        jButtonCustomerTab2Remove.setEnabled(false);
        jButtonCustomerTab2Remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomerTab2RemoveActionPerformed(evt);
            }
        });
        jPanel87.add(jButtonCustomerTab2Remove);

        jPanel27.add(jPanel87);

        jPanel85.add(jPanel27, java.awt.BorderLayout.NORTH);

        jPanel11.add(jPanel85);

        jPanel112.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel112.setLayout(new java.awt.BorderLayout());

        jTableCustomerTab2Report.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jTableCustomerTab2Report.setModel(customerTab2SalesTableModel);
        jTableCustomerTab2Report.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCustomerTab2ReportMouseClicked(evt);
            }
        });
        jScrollPane37.setViewportView(jTableCustomerTab2Report);

        jPanel112.add(jScrollPane37, java.awt.BorderLayout.CENTER);

        jPanel113.setLayout(new java.awt.GridLayout(1, 2));

        jPanel116.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButtonCustomerTab3ShowSummary1.setFont(new java.awt.Font("KaiTi", 0, 20)); // NOI18N
        jButtonCustomerTab3ShowSummary1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/order.png"))); // NOI18N
        jButtonCustomerTab3ShowSummary1.setText("列出月结表");
        jButtonCustomerTab3ShowSummary1.setPreferredSize(new java.awt.Dimension(180, 45));
        jButtonCustomerTab3ShowSummary1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomerTab3ShowSummary1ActionPerformed(evt);
            }
        });
        jPanel116.add(jButtonCustomerTab3ShowSummary1);

        jButtonCustomerTab3ShowSummary2.setFont(new java.awt.Font("KaiTi", 0, 20)); // NOI18N
        jButtonCustomerTab3ShowSummary2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/order.png"))); // NOI18N
        jButtonCustomerTab3ShowSummary2.setText("硬性列出");
        jButtonCustomerTab3ShowSummary2.setPreferredSize(new java.awt.Dimension(160, 45));
        jButtonCustomerTab3ShowSummary2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomerTab3ShowSummary2ActionPerformed(evt);
            }
        });
        jPanel116.add(jButtonCustomerTab3ShowSummary2);

        jPanel113.add(jPanel116);

        jPanel117.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButtonCustomerTab2MonthlyReport2.setFont(new java.awt.Font("KaiTi", 0, 20)); // NOI18N
        jButtonCustomerTab2MonthlyReport2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/print.png"))); // NOI18N
        jButtonCustomerTab2MonthlyReport2.setText("打印月结表");
        jButtonCustomerTab2MonthlyReport2.setPreferredSize(new java.awt.Dimension(180, 41));
        jButtonCustomerTab2MonthlyReport2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomerTab2MonthlyReport2ActionPerformed(evt);
            }
        });
        jPanel117.add(jButtonCustomerTab2MonthlyReport2);

        jPanel113.add(jPanel117);

        jPanel112.add(jPanel113, java.awt.BorderLayout.PAGE_START);

        jPanel11.add(jPanel112);

        jPanelCustomerTab2.add(jPanel11, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("客户来往", jPanelCustomerTab2);

        jPanelCustomerTab4.setLayout(new java.awt.BorderLayout());

        jPanel94.setLayout(new java.awt.BorderLayout());

        jPanel95.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabel47.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel47.setText("年份");
        jPanel95.add(jLabel47);

        jTextFieldCustomerTab4Year.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jTextFieldCustomerTab4Year.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldCustomerTab4Year.setPreferredSize(new java.awt.Dimension(200, 35));
        jPanel95.add(jTextFieldCustomerTab4Year);

        jLabel48.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel48.setText("月份");
        jPanel95.add(jLabel48);

        jComboBoxCustomerTab4Month.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jComboBoxCustomerTab4Month.setMaximumRowCount(12);
        jComboBoxCustomerTab4Month.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" }));
        jComboBoxCustomerTab4Month.setPreferredSize(new java.awt.Dimension(200, 35));
        jPanel95.add(jComboBoxCustomerTab4Month);

        jButtonCustomerTab4ShowSummary.setFont(new java.awt.Font("KaiTi", 0, 20)); // NOI18N
        jButtonCustomerTab4ShowSummary.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/order.png"))); // NOI18N
        jButtonCustomerTab4ShowSummary.setText("列出");
        jButtonCustomerTab4ShowSummary.setPreferredSize(new java.awt.Dimension(150, 45));
        jButtonCustomerTab4ShowSummary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomerTab4ShowSummaryActionPerformed(evt);
            }
        });
        jPanel95.add(jButtonCustomerTab4ShowSummary);

        jLabelCustomerTab4RecordSize.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jPanel95.add(jLabelCustomerTab4RecordSize);

        jPanel94.add(jPanel95, java.awt.BorderLayout.CENTER);

        jPanelCustomerTab4.add(jPanel94, java.awt.BorderLayout.PAGE_START);

        jPanel97.setLayout(new java.awt.BorderLayout());

        jPanel98.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel98.setLayout(new java.awt.BorderLayout());

        jTableCustomerTab4Result.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jTableCustomerTab4Result.setModel(customerTab4SalesCheckTableModel);
        jTableCustomerTab4Result.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCustomerTab4ResultMouseClicked(evt);
            }
        });
        jScrollPane31.setViewportView(jTableCustomerTab4Result);

        jPanel98.add(jScrollPane31, java.awt.BorderLayout.CENTER);

        jPanel97.add(jPanel98, java.awt.BorderLayout.CENTER);

        jPanelCustomerTab4.add(jPanel97, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("客户检查", jPanelCustomerTab4);

        jPanelReportTab2.setLayout(new java.awt.BorderLayout());

        jPanel37.setLayout(new java.awt.GridLayout(1, 2));

        jPanel40.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel9.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jLabel9.setText("日期：");
        jPanel40.add(jLabel9);

        jTextFieldReportTab2Date.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jTextFieldReportTab2Date.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldReportTab2Date.setPreferredSize(new java.awt.Dimension(180, 35));
        jTextFieldReportTab2Date.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldReportTab2DateKeyReleased(evt);
            }
        });
        jPanel40.add(jTextFieldReportTab2Date);

        jButtonReportTab2DatePicker.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jButtonReportTab2DatePicker.setText("选择");
        jButtonReportTab2DatePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportTab2DatePickerActionPerformed(evt);
            }
        });
        jPanel40.add(jButtonReportTab2DatePicker);

        jPanel37.add(jPanel40);

        jPanel41.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButtonReportTab2ShowReport.setFont(new java.awt.Font("KaiTi", 1, 24)); // NOI18N
        jButtonReportTab2ShowReport.setForeground(new java.awt.Color(0, 51, 255));
        jButtonReportTab2ShowReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ryanworks/fishery/client/ui/resources/order.png"))); // NOI18N
        jButtonReportTab2ShowReport.setText("列出报表");
        jButtonReportTab2ShowReport.setPreferredSize(new java.awt.Dimension(240, 45));
        jButtonReportTab2ShowReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportTab2ShowReportActionPerformed(evt);
            }
        });
        jPanel41.add(jButtonReportTab2ShowReport);

        jPanel37.add(jPanel41);

        jPanelReportTab2.add(jPanel37, java.awt.BorderLayout.NORTH);

        jPanel42.setPreferredSize(new java.awt.Dimension(510, 610));
        jPanel42.setLayout(new java.awt.GridLayout(1, 3, 3, 0));

        jPanel99.setBackground(new java.awt.Color(255, 153, 204));
        jPanel99.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "被选鱼类", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel99.setLayout(new java.awt.BorderLayout());

        jListReportTab2ItemSelection.setBackground(new java.awt.Color(51, 51, 51));
        jListReportTab2ItemSelection.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListReportTab2ItemSelection.setForeground(new java.awt.Color(51, 255, 0));
        jListReportTab2ItemSelection.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListReportTab2ItemSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListReportTab2ItemSelectionMouseClicked(evt);
            }
        });
        jScrollPane29.setViewportView(jListReportTab2ItemSelection);

        jPanel99.add(jScrollPane29, java.awt.BorderLayout.CENTER);

        jPanel100.setLayout(new java.awt.GridLayout(2, 1));

        jButtonReportTab2RemoveSelection.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonReportTab2RemoveSelection.setText("移除");
        jButtonReportTab2RemoveSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportTab2RemoveSelectionActionPerformed(evt);
            }
        });
        jPanel100.add(jButtonReportTab2RemoveSelection);

        jButtonReportTab2RemoveAllSelections.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonReportTab2RemoveAllSelections.setText("移除全部");
        jButtonReportTab2RemoveAllSelections.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportTab2RemoveAllSelectionsActionPerformed(evt);
            }
        });
        jPanel100.add(jButtonReportTab2RemoveAllSelections);

        jPanel99.add(jPanel100, java.awt.BorderLayout.NORTH);

        jPanel42.add(jPanel99);

        jPanel101.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "1.鱼类类别", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel101.setLayout(new java.awt.BorderLayout());

        jListReportTab2Category.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListReportTab2Category.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { " " };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jListReportTab2Category.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListReportTab2Category.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListReportTab2CategoryValueChanged(evt);
            }
        });
        jScrollPane32.setViewportView(jListReportTab2Category);

        jPanel101.add(jScrollPane32, java.awt.BorderLayout.CENTER);

        jPanel42.add(jPanel101);

        jPanel102.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "2.鱼类", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 18))); // NOI18N
        jPanel102.setLayout(new java.awt.BorderLayout());

        jListReportTab2Item.setFont(new java.awt.Font("KaiTi", 0, 24)); // NOI18N
        jListReportTab2Item.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListReportTab2Item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListReportTab2ItemMouseClicked(evt);
            }
        });
        jScrollPane33.setViewportView(jListReportTab2Item);

        jPanel102.add(jScrollPane33, java.awt.BorderLayout.CENTER);

        jPanel42.add(jPanel102);

        jPanelReportTab2.add(jPanel42, java.awt.BorderLayout.LINE_END);

        jPanel103.setLayout(new java.awt.BorderLayout());

        jPanel104.setLayout(new java.awt.BorderLayout());

        jScrollPane34.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "报告输出", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 24))); // NOI18N

        jTextAreaReportTab2.setColumns(20);
        jTextAreaReportTab2.setFont(new java.awt.Font("Monospaced", 0, 20)); // NOI18N
        jTextAreaReportTab2.setRows(5);
        jScrollPane34.setViewportView(jTextAreaReportTab2);

        jPanel104.add(jScrollPane34, java.awt.BorderLayout.CENTER);

        jPanel103.add(jPanel104, java.awt.BorderLayout.CENTER);
        jPanel103.add(jPanel109, java.awt.BorderLayout.SOUTH);

        jPanelReportTab2.add(jPanel103, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("存货报表", jPanelReportTab2);

        jPanel118.setLayout(new java.awt.BorderLayout());

        jPanel119.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButtonPatchSupplier.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonPatchSupplier.setText("执行船户补丁");
        jButtonPatchSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPatchSupplierActionPerformed(evt);
            }
        });
        jPanel119.add(jButtonPatchSupplier);

        jButtonPatchCustomer.setFont(new java.awt.Font("KaiTi", 0, 18)); // NOI18N
        jButtonPatchCustomer.setText("执行客户补丁");
        jButtonPatchCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPatchCustomerActionPerformed(evt);
            }
        });
        jPanel119.add(jButtonPatchCustomer);

        jPanel118.add(jPanel119, java.awt.BorderLayout.PAGE_START);

        jPanel120.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "执行 LOG", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("KaiTi", 0, 14))); // NOI18N
        jPanel120.setLayout(new java.awt.BorderLayout());

        jTextAreaPatchLog.setColumns(20);
        jTextAreaPatchLog.setRows(5);
        jScrollPane5.setViewportView(jTextAreaPatchLog);

        jPanel120.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        jPanel118.add(jPanel120, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("数据补丁", jPanel118);

        add(jTabbedPane, java.awt.BorderLayout.CENTER);
        jTabbedPane.getAccessibleContext().setAccessibleName("鱼类资料");

        jPanel36.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabel8.setText("System Ready");
        jPanel36.add(jLabel8);

        add(jPanel36, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void jListItemTab1CategoryValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListItemTab1CategoryValueChanged
        Object categoryObj = 
                this.jListItemTab1Category.getModel().getElementAt( this.jListItemTab1Category.getSelectedIndex() );
        
        if (categoryObj == null)
            return;
        
        // Clear item list
        this.itemTab1ItemTableModel.removeAll();
        
        CategoryBean selectedCategory = (CategoryBean) categoryObj;        
        List<ItemBean> itemList = 
                SystemDataDelegate.getInstance().getItemsByCategoryId(selectedCategory.getId());
        
        for (ItemBean item : itemList) {
            this.itemTab1ItemTableModel.addBean(item);
        }
        
        // DefaultRowSorter has the sort() method
        DefaultRowSorter sorter = ((DefaultRowSorter)this.jTableItemTab1Item.getRowSorter()); 
        ArrayList list = new ArrayList();
        list.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
        sorter.setSortKeys(list);
        sorter.sort();

    }//GEN-LAST:event_jListItemTab1CategoryValueChanged

    private void jButtonItemTab1AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonItemTab1AddActionPerformed
        String itemCategory = this.jListItemTab1Category.getSelectedValue().toString();
        SwingUtil.centerDialogOnScreen( new ItemEditorDialog(this.parent, true, this, null, itemCategory) );
    }//GEN-LAST:event_jButtonItemTab1AddActionPerformed

    private void jButtonItemTab1EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonItemTab1EditActionPerformed
        String selectedId = this.jTableItemTab1Item.getValueAt(this.jTableItemTab1Item.getSelectedRow(), 0).toString();
        if (selectedId==null || selectedId.length()==0)
            return;
        
        for (ItemBean item : this.itemTab1ItemTableModel.getList())
        {
            if (item.getId().equalsIgnoreCase(selectedId)) 
            {                
                                                               
                String itemCategory = this.jListItemTab1Category.getSelectedValue().toString();
                SwingUtil.centerDialogOnScreen( new ItemEditorDialog(this.parent, true, this, item, itemCategory) );
                break;
            }
        }        
    }//GEN-LAST:event_jButtonItemTab1EditActionPerformed

    private void jButtonItemTab1DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonItemTab1DeleteActionPerformed
        String selectedName = this.jTableItemTab1Item.getValueAt(this.jTableItemTab1Item.getSelectedRow(), 1).toString();
        if (selectedName==null || selectedName.length()==0)
            return;
                
        int flag = JOptionPane.showConfirmDialog(
                        this, 
                        "您确定要删除此项鱼类吗？ [" + selectedName + "]", 
                        "确认删除",
                        JOptionPane.YES_NO_OPTION);
        
        if (flag == JOptionPane.YES_OPTION)
        {
            String selectedId = this.jTableItemTab1Item.getValueAt(this.jTableItemTab1Item.getSelectedRow(), 0).toString();
            if (selectedId==null || selectedId.length()==0)
                return;

            for (ItemBean item : this.itemTab1ItemTableModel.getList())
            {
                // Found the selected item
                if (item.getId().equalsIgnoreCase(selectedId)) 
                {                
                    // Delete the item from database
                    SystemDataDelegate.getInstance().deleteItem(item);
                    
                    // Refresh the item list -----------------------------------
                    // Step 1: retrieve selected category
                    Object categoryObj = 
                        this.jListItemTab1Category.getModel().getElementAt( this.jListItemTab1Category.getSelectedIndex() );
        
                    if (categoryObj != null)
                    {
                        CategoryBean selectedCategory = (CategoryBean) categoryObj;  

                        if (item.getCategoryId().equalsIgnoreCase(selectedCategory.getId()))
                        {        
                            // Clear item list
                            this.itemTab1ItemTableModel.removeAll();

                            // Step 2: Retrieve new list of items from database
                            List<ItemBean> itemList = 
                                    SystemDataDelegate.getInstance().getItemsByCategoryId(selectedCategory.getId());
                            
                            // Step 3: Populate new list of items into JList
                            for (ItemBean item2 : itemList)                                 
                            {
                                this.itemTab1ItemTableModel.addBean(item2);
                            }
                        }
                    }
                    
                    break;
                }
            }
        }
    }//GEN-LAST:event_jButtonItemTab1DeleteActionPerformed

    private void jTableItemTab1ItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableItemTab1ItemMouseClicked
        this.jButtonItemTab1Edit.doClick();        
    }//GEN-LAST:event_jTableItemTab1ItemMouseClicked

    private void jButtonSupplierTab1AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab1AddActionPerformed
        SwingUtil.centerDialogOnScreen( new SupplierEditorDialog(this.parent, true, this, null) );
    }//GEN-LAST:event_jButtonSupplierTab1AddActionPerformed

    private void jButtonSupplierTab1EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab1EditActionPerformed
        String selectedId = this.jTableSupplierTab1Supplier.getValueAt(this.jTableSupplierTab1Supplier.getSelectedRow(), 0).toString();
        if (selectedId==null || selectedId.length()==0)
            return;
        
        for (SupplierBean supplier : this.supplierTab1SupplierTableModel.getList())
        {
            if (supplier.getId().equalsIgnoreCase(selectedId)) 
            {                
                SwingUtil.centerDialogOnScreen( new SupplierEditorDialog(this.parent, true, this, supplier) );
                break;
            }
        }
    }//GEN-LAST:event_jButtonSupplierTab1EditActionPerformed

    private void jButtonSupplierTab1DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab1DeleteActionPerformed
        String selectedName = this.jTableSupplierTab1Supplier.getValueAt(this.jTableSupplierTab1Supplier.getSelectedRow(), 1).toString();
        if (selectedName==null || selectedName.length()==0)
            return;
                
        int flag = JOptionPane.showConfirmDialog(
                        this, 
                        "Are you sure to delete this item '" + selectedName + "'?", 
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);
        
        if (flag == JOptionPane.YES_OPTION)
        {
            String selectedId = this.jTableSupplierTab1Supplier.getValueAt(this.jTableSupplierTab1Supplier.getSelectedRow(), 0).toString();
            if (selectedId==null || selectedId.length()==0)
                return;

            for (SupplierBean supplier : this.supplierTab1SupplierTableModel.getList())
            {
                if (supplier.getId().equalsIgnoreCase(selectedId)) 
                {                
                    new Thread( new Runnable() {
                        @Override
                        public void run() {
                            // Delete customer 
                            InTransactionDelegate.getInstance().deleteTransactionsBySupplier(supplier);
                        }
                    }).start();
                    
                    SupplierDelegate.getInstance().deleteSupplier(supplier);
                    
                    this.refillSupplierTable();
                    
                    break;
                }
            }
        }
    }//GEN-LAST:event_jButtonSupplierTab1DeleteActionPerformed

    private void jTableSupplierTab1SupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSupplierTab1SupplierMouseClicked
        this.jButtonSupplierTab1Edit.doClick();        
    }//GEN-LAST:event_jTableSupplierTab1SupplierMouseClicked

    private void jButtonCustomerTab1AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomerTab1AddActionPerformed
        SwingUtil.centerDialogOnScreen( new CustomerEditorDialog(this.parent, true, this, null) );
    }//GEN-LAST:event_jButtonCustomerTab1AddActionPerformed

    private void jButtonCustomerTab1EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomerTab1EditActionPerformed
        String selectedId = this.jTableCustomerTab1Customer.getValueAt(this.jTableCustomerTab1Customer.getSelectedRow(), 0).toString();
        if (selectedId==null || selectedId.length()==0)
            return;
        
        for (CustomerBean customer : this.customerTab1CustomerTableModel.getList())
        {
            if (customer.getId().equalsIgnoreCase(selectedId)) 
            {                
                SwingUtil.centerDialogOnScreen( new CustomerEditorDialog(this.parent, true, this, customer) );
                break;
            }
        }
    }//GEN-LAST:event_jButtonCustomerTab1EditActionPerformed

    private void jButtonCustomerTab1DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomerTab1DeleteActionPerformed
        String selectedName = this.jTableCustomerTab1Customer.getValueAt(this.jTableCustomerTab1Customer.getSelectedRow(), 1).toString();
        if (selectedName==null || selectedName.length()==0)
            return;
                
        int flag = JOptionPane.showConfirmDialog(
                        this, 
                        "您确定要删除此客户资料 - '" + selectedName + "'?", 
                        "确认删除",
                        JOptionPane.YES_NO_OPTION);
        
        if (flag == JOptionPane.YES_OPTION)
        {
            String selectedId = this.jTableCustomerTab1Customer.getValueAt(this.jTableCustomerTab1Customer.getSelectedRow(), 0).toString();
            if (selectedId==null || selectedId.length()==0)
                return;

            for (CustomerBean customer : this.customerTab1CustomerTableModel.getList())
            {
                if (customer.getId().equalsIgnoreCase(selectedId)) 
                {                
                    new Thread( new Runnable() {
                        @Override
                        public void run() {
                            // Delete customer 
                            SalesDelegate.getInstance().deleteSalesByCustomer(customer);
                        }
                    }).start();
                    
                    CustomerDelegate.getInstance().deleteCustomer(customer);
                    
                    
                    this.refillCustomerTable();
                    
                    break;
                }
            }
        }
    }//GEN-LAST:event_jButtonCustomerTab1DeleteActionPerformed

    private void jTableCustomerTab1CustomerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCustomerTab1CustomerMouseClicked
        this.jButtonCustomerTab1Edit.doClick();
    }//GEN-LAST:event_jTableCustomerTab1CustomerMouseClicked

    private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPaneStateChanged
        
        if (this.jTabbedPane.getSelectedComponent() == this.jPanelItemTab1)
        {
            
        }
        else
        if (this.jTabbedPane.getSelectedComponent() == this.jPanelItemTab2)
        {
            
        }
        else
        if (this.jTabbedPane.getSelectedComponent() == this.jPanelSupplierTab1)
        {
            this.jTextFieldSupplierTab1Filter.requestFocus();
            this.jTextFieldSupplierTab1Filter.selectAll();
        }
        else
        if (this.jTabbedPane.getSelectedComponent() == this.jPanelSupplierTab2)
        {
            
        }
        else
        if (this.jTabbedPane.getSelectedComponent() == this.jPanelSupplierTab3)
        {
            
        }
        else
        if (this.jTabbedPane.getSelectedComponent() == this.jPanelSupplierTab4)
        {
            
        }
        else
        if (this.jTabbedPane.getSelectedComponent() == this.jPanelSupplierTab5)
        {
            
        }
        else
        if (this.jTabbedPane.getSelectedComponent() == this.jPanelSupplierTab7)
        {
            jListSupplierTab7ValueChanged();
        }        
        else
        if (this.jTabbedPane.getSelectedComponent() == this.jPanelSupplierTab8)
        {
            
        }
        else
        if (this.jTabbedPane.getSelectedComponent() == this.jPanelCustomerTab1)
        {
            this.jTextFieldCustomerTab1Filter.requestFocus();
            this.jTextFieldCustomerTab1Filter.selectAll();
            
            this.customerTab1CustomerTableModel.removeAll();
            // Fill in list of supplier into Tab 3
            List<CustomerBean> customerList = CustomerDelegate.getInstance().getAllCustomers();
            Collections.sort(customerList);
            for (CustomerBean customer : customerList)
            {
                this.customerTab1CustomerTableModel.addBean( customer );
            }
        }
        else
        if (this.jTabbedPane.getSelectedComponent() == this.jPanelCustomerTab2)
        {
            this.listModelCustomerTab2Customer.removeAllElements();
            // Fill in list of supplier into Tab 3
            List<CustomerBean> customerList = CustomerDelegate.getInstance().getAllCustomers();
            Collections.sort(customerList);
            for (CustomerBean customer : customerList)
            {
                this.listModelCustomerTab2Customer.addElement( customer );
            }
        }
        else
        if (this.jTabbedPane.getSelectedComponent() == this.jPanelCustomerTab4)
        {
            this.customerTab4SalesCheckTableModel.removeAll();
        }
        else
        if (this.jTabbedPane.getSelectedComponent() == this.jPanelReportTab2)
        {
            this.jTextFieldReportTab2Date.requestFocus();
            this.jTextFieldReportTab2Date.setCaretPosition(0);
            this.jTextFieldReportTab2Date.select(0, 2);
            
            this.jTextAreaReportTab2.setText( "" );
            this.listModelReportTab2ItemSelection.removeAllElements();
            this.jListReportTab2Category.clearSelection();
            this.jListReportTab2Item.clearSelection();
        }
    }//GEN-LAST:event_jTabbedPaneStateChanged

    private void jListCustomerTab2CustomerValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListCustomerTab2CustomerValueChanged
        if (this.jListCustomerTab2Customer.getSelectedIndex()==-1)
            return;
        
        this.jButtonCustomerTab3ShowSummary2.doClick();
        
        this.jButtonCustomerTab2Add.setEnabled( true );
        
        long startDate = this.getSelectedDateInMillis(this.jTextFieldCustomerTab2DateFrom);
        long endDate = this.getSelectedDateInMillis(this.jTextFieldCustomerTab2DateTo);
        
        if (startDate==0 || endDate==0)
            return;
        
        CustomerBean customer = (CustomerBean) this.jListCustomerTab2Customer.getSelectedValue();
        List<CustomerPaymentBean> list = 
            CustomerDelegate.getInstance().getPaymentsByCustomerAndDateRange(customer.getId(), startDate, endDate);
        
        double totalAmount = 0.0d;
        this.customerTab2PaymentTableModel.removeAll();
        for (CustomerPaymentBean payment : list)
        {
            this.customerTab2PaymentTableModel.addBean( payment );
            totalAmount = totalAmount + payment.getAmount();
        }
        
        this.jLabelCustomerTab2TotalAmount.setText( currencyFormatter.format( totalAmount ) );
    }//GEN-LAST:event_jListCustomerTab2CustomerValueChanged

    private void jTextFieldItemTab2DateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldItemTab2DateKeyReleased
        this.dateFieldKeyReleasedListener(evt, this.jTextFieldItemTab2Date);
        refreshItemPriceList(tab2_selectedItemObj);
    }//GEN-LAST:event_jTextFieldItemTab2DateKeyReleased

    private void jButtonItemTab2DatePickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonItemTab2DatePickerActionPerformed
        this.showDatePicker( this.jTextFieldItemTab2Date );
        refreshItemPriceList(tab2_selectedItemObj);
    }//GEN-LAST:event_jButtonItemTab2DatePickerActionPerformed

    private void jListItemTab2CategoryValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListItemTab2CategoryValueChanged
        this.showFisheryItemByCategory(
                this.jListItemTab2Category,
                this.listModelItemTab2Item);
    }//GEN-LAST:event_jListItemTab2CategoryValueChanged

    private void refreshItemPriceList(ItemBean selectedItem)
    {
        if (selectedItem==null)
            return;
        
        if (this.getSelectedDateInMillis( this.jTextFieldItemTab2Date )==0)
        {
            JOptionPane.showMessageDialog(
                this, 
                "请选择正确日期。",
                "日期格式错误",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
                
        tab2_selectedItemObj = selectedItem;
        
        List<InTransactionLineBean> resultList = 
            InTransactionDelegate.getInstance().getTransactionLineByItemAndDate(
                selectedItem, 
                this.getSelectedDateInMillis(this.jTextFieldItemTab2Date));
                
        if (resultList == null || resultList.size()==0)
        {
            JOptionPane.showMessageDialog(
                this, 
                "这天没有此鱼类的资料。",
                "没有资料",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        else 
        {   
            itemTab2PriceChangeTableModel.removeAll();
            for (InTransactionLineBean lineBean : resultList) 
            {
                if (lineBean.getItemNewName().equalsIgnoreCase(selectedItem.getName()))
                    this.itemTab2PriceChangeTableModel.addBean(lineBean);
            }
        }
    }
    
    private void jButtonItemTab2SaveChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonItemTab2SaveChangeActionPerformed
        
        try 
        {
            double newPrice = Double.parseDouble( this.jTextFieldItemTab2NewPrice.getText() );
            double oldPrice = 0.0d;

            List<InTransactionLineBean> checkedList = itemTab2PriceChangeTableModel.getCheckedList();
            
            if (checkedList.size() > 0)
            {
                for (InTransactionLineBean lineBean : checkedList)
                {
                    oldPrice = lineBean.getUnitPrice();
                    lineBean.setUnitPrice( newPrice );

                    // Step1: Save new price into line
                    InTransactionDelegate.getInstance().saveTransactionLine(lineBean);

                    // Step 2: Re-calculate total amount for the transaction.
                    InTransactionBean transactionBean = 
                            InTransactionDelegate.getInstance().getTransactionById( lineBean.getInTransactionId() );    

                    double totalAmount = 0.0d;
                    for (InTransactionLineBean line : transactionBean.getLineList())
                    {
                        totalAmount = totalAmount + (line.getWeight() * line.getUnitPrice());
                    }
                    transactionBean.setTotalPrice(totalAmount);

                    // Step 3: Save the transaction object
                    InTransactionDelegate.getInstance().saveTransaction(transactionBean);
                }
                
                refreshItemPriceList(tab2_selectedItemObj);
                
                JOptionPane.showMessageDialog(
                    this, 
                    "'" + checkedList.get(0).getItemNewName() + "' 的价格已更改至 " + newPrice + "(旧价：" + oldPrice + ")",
                    "储存成功",
                    JOptionPane.INFORMATION_MESSAGE);
                
            }
        }
        catch (NumberFormatException e)
        {
            MyLogger.logError(getClass(), e.getMessage());
            
            JOptionPane.showMessageDialog(
                    this, 
                    "新价格格式错误，价格必须是数字。",
                    "输入错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonItemTab2SaveChangeActionPerformed

    private void jButtonSupplierTab2DateChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab2DateChooserActionPerformed
        long selectedTimeInMillis = this.showDatePicker(null);
        
        showDateRangeHalfMonth(selectedTimeInMillis, this.jTextFieldSupplierTab2DateFrom, this.jTextFieldSupplierTab2DateTo); 
        
        this.fillSupplierTab2Table();
    }//GEN-LAST:event_jButtonSupplierTab2DateChooserActionPerformed

    private void jButtonSupplierTab3DateChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab3DateChooserActionPerformed
        long selectedTimeInMillis = this.showDatePicker(null);
        
        showDateRangeHalfMonth(selectedTimeInMillis, this.jTextFieldSupplierTab3DateFrom, this.jTextFieldSupplierTab3DateTo);     
        
        this.fillSupplierTab3Table();
    }//GEN-LAST:event_jButtonSupplierTab3DateChooserActionPerformed

    private void jButtonSupplierTab4DateChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab4DateChooserActionPerformed
        long selectedTimeInMillis = this.showDatePicker(null);
        
        showDateRangeHalfMonth(selectedTimeInMillis, this.jTextFieldSupplierTab4DateFrom, this.jTextFieldSupplierTab4DateTo);   
        
        this.fillSupplierTab4Table();
    }//GEN-LAST:event_jButtonSupplierTab4DateChooserActionPerformed

    private void jButtonSupplierTab5DateChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab5DateChooserActionPerformed
        long selectedTimeInMillis = this.showDatePicker(null);
        
        showDateRangeHalfMonth(selectedTimeInMillis, this.jTextFieldSupplierTab5DateFrom, this.jTextFieldSupplierTab5DateTo);     
        
        this.fillSupplierTab5Table();
    }//GEN-LAST:event_jButtonSupplierTab5DateChooserActionPerformed

    private void jButtonSupplierTab7DateChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab7DateChooserActionPerformed
        long selectedTimeInMillis = this.showDatePicker(null);
        
        showDateRangeHalfMonth(selectedTimeInMillis, this.jTextFieldSupplierTab7DateFrom, this.jTextFieldSupplierTab7DateTo); 
        
        this.fillSupplierTab6Table();
    }//GEN-LAST:event_jButtonSupplierTab7DateChooserActionPerformed

    private void jListSupplierTab2SupplierValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListSupplierTab2SupplierValueChanged
        if (this.jListSupplierTab2Supplier.getSelectedIndex()==-1)
            return;
        
        this.jButtonSupplierTab2Add.setEnabled( true );
        
        long startDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab2DateFrom);
        long endDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab2DateTo);
        
        if (startDate==0 || endDate==0)
            return;
        
        SupplierBean supplier = (SupplierBean) this.jListSupplierTab2Supplier.getSelectedValue();
        List<SupplierChequeBean> list = 
            SupplierDelegate.getInstance().getChequeListByDateAndSupplier(
                startDate, endDate, supplier);
        
        double totalAmount = 0.0d;
        this.supplierTab2ChequeTableModel.removeAll();
        for (SupplierChequeBean cheque : list)
        {
            this.supplierTab2ChequeTableModel.addBean( cheque );
            totalAmount = totalAmount + cheque.getChequeAmount();
        }
        
        this.jLabelSupplierTab2Name.setText( supplier.getShipNumber() + ": " + supplier.getName() );
        this.jLabelSupplierTab2TotalAmount.setText( currencyFormatter.format( totalAmount ) );
    }//GEN-LAST:event_jListSupplierTab2SupplierValueChanged

    private void jListSupplierTab3SupplierValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListSupplierTab3SupplierValueChanged
        if (this.jListSupplierTab3Supplier.getSelectedIndex()==-1)
            return;
        
        this.jButtonSupplierTab3Add.setEnabled( true );
        
        long startDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab3DateFrom);
        long endDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab3DateTo);
        
        if (startDate==0 || endDate==0)
            return;
        
        SupplierBean supplier = (SupplierBean) this.jListSupplierTab3Supplier.getSelectedValue();
        List<SupplierFuelBean> list = 
            SupplierDelegate.getInstance().getFuelListByDateAndSupplier(
                startDate, endDate, supplier);
        
        double totalAmount = 0.0d;
        this.supplierTab3FuelTableModel.removeAll();
        for (SupplierFuelBean fuel : list)
        {
            this.supplierTab3FuelTableModel.addBean( fuel );
            totalAmount = totalAmount + fuel.getFuelTotalPrice();
        }
        
        this.jLabelSupplierTab3Name.setText( supplier.getShipNumber() + ": " + supplier.getName() );
        this.jLabelSupplierTab3TotalAmount.setText( currencyFormatter.format( totalAmount ) );
    }//GEN-LAST:event_jListSupplierTab3SupplierValueChanged

    private void jListSupplierTab4SupplierValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListSupplierTab4SupplierValueChanged
        if (this.jListSupplierTab4Supplier.getSelectedIndex()==-1)
            return;
                
        this.jButtonSupplierTab4Add.setEnabled( true );
        
        long startDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab4DateFrom);
        long endDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab4DateTo);
        
        if (startDate==0 || endDate==0)
            return;
        
        SupplierBean supplier = (SupplierBean) this.jListSupplierTab4Supplier.getSelectedValue();
        List<SupplierMiscBean> list = 
            SupplierDelegate.getInstance().getMiscListByDateAndSupplier(
                startDate, endDate, supplier);
        
        double totalAmount = 0.0d;
        this.supplierTab4MiscTableModel.removeAll();
        for (SupplierMiscBean misc : list)
        {
            this.supplierTab4MiscTableModel.addBean( misc );
            totalAmount = totalAmount + misc.getMiscAmount();
        }
        
        this.jLabelSupplierTab4Name.setText( supplier.getShipNumber() + ": " + supplier.getName() );
        this.jLabelSupplierTab4TotalAmount.setText( currencyFormatter.format( totalAmount ) );
    }//GEN-LAST:event_jListSupplierTab4SupplierValueChanged

    private void jListSupplierTab5SupplierValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListSupplierTab5SupplierValueChanged
        if (this.jListSupplierTab5Supplier.getSelectedIndex()==-1)
            return;
        
        this.jButtonSupplierTab5Add.setEnabled( true );
        
        long startDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab5DateFrom);
        long endDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab5DateTo);
        
        if (startDate==0 || endDate==0)
            return;
        
        SupplierBean supplier = (SupplierBean) this.jListSupplierTab5Supplier.getSelectedValue();
        List<SupplierCashBean> list = 
            SupplierDelegate.getInstance().getCashListByDateAndSupplier(
                startDate, endDate, supplier);
        
        double totalAmount = 0.0d;
        this.supplierTab5CashTableModel.removeAll();
        for (SupplierCashBean cash : list)
        {
            this.supplierTab5CashTableModel.addBean( cash );
            totalAmount = totalAmount + cash.getCashAmount();
        }
        
        this.jLabelSupplierTab5Name.setText( supplier.getShipNumber() + ": " + supplier.getName() );
        this.jLabelSupplierTab5TotalAmount.setText( currencyFormatter.format( totalAmount ) );
    }//GEN-LAST:event_jListSupplierTab5SupplierValueChanged

    private void jButtonSupplierTab2AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab2AddActionPerformed
        if (this.jListSupplierTab2Supplier.getSelectedIndex() != -1)
            SwingUtil.centerDialogOnScreen( new SupplierChequeEditorDialog(this.parent, true, this, (SupplierBean)this.jListSupplierTab2Supplier.getSelectedValue(), null) );
    }//GEN-LAST:event_jButtonSupplierTab2AddActionPerformed

    private void jButtonSupplierTab3AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab3AddActionPerformed
        if (this.jListSupplierTab3Supplier.getSelectedIndex() != -1)
            SwingUtil.centerDialogOnScreen( new SupplierFuelEditorDialog(this.parent, true, this, (SupplierBean)this.jListSupplierTab3Supplier.getSelectedValue(), null) );
    }//GEN-LAST:event_jButtonSupplierTab3AddActionPerformed

    private void jButtonSupplierTab4AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab4AddActionPerformed
        if (this.jListSupplierTab4Supplier.getSelectedIndex() != -1)
            SwingUtil.centerDialogOnScreen( new SupplierMiscEditorDialog(this.parent, true, this, (SupplierBean)this.jListSupplierTab4Supplier.getSelectedValue(), null) );
    }//GEN-LAST:event_jButtonSupplierTab4AddActionPerformed

    private void jButtonSupplierTab5AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab5AddActionPerformed
        if (this.jListSupplierTab5Supplier.getSelectedIndex() != -1)
            SwingUtil.centerDialogOnScreen( new SupplierCashEditorDialog(this.parent, true, this, (SupplierBean)this.jListSupplierTab5Supplier.getSelectedValue(), null) );
    }//GEN-LAST:event_jButtonSupplierTab5AddActionPerformed

    private void jButtonSupplierTab2EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab2EditActionPerformed
        if (this.jTableSupplierTab2ChequeTransaction.getSelectedRow()==-1)
            return;
        
        if (this.jListSupplierTab2Supplier.getSelectedIndex() == -1)
            return;
        
        String selectedId = this.jTableSupplierTab2ChequeTransaction.getValueAt(this.jTableSupplierTab2ChequeTransaction.getSelectedRow(), 0).toString();
        SupplierChequeBean cheque = this.supplierTab2ChequeTableModel.getBeanById(selectedId);
        
        SwingUtil.centerDialogOnScreen( new SupplierChequeEditorDialog(this.parent, true, this, null, cheque) );
    }//GEN-LAST:event_jButtonSupplierTab2EditActionPerformed

    private void jButtonSupplierTab3EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab3EditActionPerformed
        if (this.jTableSupplierTab3FuelTransaction.getSelectedRow()==-1)
            return;
        
        if (this.jListSupplierTab3Supplier.getSelectedIndex() == -1)
            return;
        
        String selectedId = this.jTableSupplierTab3FuelTransaction.getValueAt(this.jTableSupplierTab3FuelTransaction.getSelectedRow(), 0).toString();
        SupplierFuelBean fuel = this.supplierTab3FuelTableModel.getBeanById(selectedId);
        
        SwingUtil.centerDialogOnScreen( new SupplierFuelEditorDialog(this.parent, true, this, null, fuel) );
    }//GEN-LAST:event_jButtonSupplierTab3EditActionPerformed

    private void jButtonSupplierTab4EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab4EditActionPerformed
        if (this.jTableSupplierTab4MiscTransaction.getSelectedRow()==-1)
            return;
        
        if (this.jListSupplierTab4Supplier.getSelectedIndex() == -1)
            return;
        
        String selectedId = this.jTableSupplierTab4MiscTransaction.getValueAt(this.jTableSupplierTab4MiscTransaction.getSelectedRow(), 0).toString();
        SupplierMiscBean misc = this.supplierTab4MiscTableModel.getBeanById(selectedId);
        
        SwingUtil.centerDialogOnScreen( new SupplierMiscEditorDialog(this.parent, true, this, null, misc) );
    }//GEN-LAST:event_jButtonSupplierTab4EditActionPerformed

    private void jButtonSupplierTab5EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab5EditActionPerformed
        if (this.jTableSupplierTab5CashTransaction.getSelectedRow()==-1)
            return;
        
        if (this.jListSupplierTab5Supplier.getSelectedIndex() == -1)
            return;
        
        String selectedId = this.jTableSupplierTab5CashTransaction.getValueAt(this.jTableSupplierTab5CashTransaction.getSelectedRow(), 0).toString();
        SupplierCashBean cash = this.supplierTab5CashTableModel.getBeanById(selectedId);
        
        SwingUtil.centerDialogOnScreen( new SupplierCashEditorDialog(this.parent, true, this, null, cash) );
    }//GEN-LAST:event_jButtonSupplierTab5EditActionPerformed

    private void jButtonSupplierTab2RemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab2RemoveActionPerformed
        if (this.jTableSupplierTab2ChequeTransaction.getSelectedRow()==-1)
            return;
        
        if (this.jListSupplierTab2Supplier.getSelectedIndex() == -1)
            return;
        
        int flag = JOptionPane.showConfirmDialog(
                        this, 
                        "您确定要删除此项资料？", 
                        "确认删除", 
                        JOptionPane.YES_NO_OPTION);
        if (flag == JOptionPane.YES_OPTION)
        {
            String selectedId = this.jTableSupplierTab2ChequeTransaction.getValueAt(this.jTableSupplierTab2ChequeTransaction.getSelectedRow(), 0).toString();
            SupplierChequeBean cheque = this.supplierTab2ChequeTableModel.getBeanById(selectedId);
        
            SupplierDelegate.getInstance().deleteCheque( cheque );
            
            if (this.jListSupplierTab2Supplier.getSelectedIndex() != -1)
                this.jListSupplierTab2SupplierValueChanged(null);
        }
    }//GEN-LAST:event_jButtonSupplierTab2RemoveActionPerformed

    private void jButtonSupplierTab3RemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab3RemoveActionPerformed
        if (this.jTableSupplierTab3FuelTransaction.getSelectedRow()==-1)
            return;
        
        if (this.jListSupplierTab3Supplier.getSelectedIndex() == -1)
            return;
        
        int flag = JOptionPane.showConfirmDialog(this, "您确定要删除此项资料？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (flag == JOptionPane.YES_OPTION)
        {
            String selectedId = this.jTableSupplierTab3FuelTransaction.getValueAt(this.jTableSupplierTab3FuelTransaction.getSelectedRow(), 0).toString();
            SupplierFuelBean fuel = this.supplierTab3FuelTableModel.getBeanById(selectedId);
        
            SupplierDelegate.getInstance().deleteFuel( fuel );
                    
            if (this.jListSupplierTab3Supplier.getSelectedIndex() != -1)
                this.jListSupplierTab3SupplierValueChanged(null);
        }
    }//GEN-LAST:event_jButtonSupplierTab3RemoveActionPerformed

    private void jButtonSupplierTab4RemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab4RemoveActionPerformed
        if (this.jTableSupplierTab4MiscTransaction.getSelectedRow()==-1)
            return;
        
        if (this.jListSupplierTab4Supplier.getSelectedIndex() == -1)
            return;
        
        int flag = JOptionPane.showConfirmDialog(this, "您确定要删除此项资料？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (flag == JOptionPane.YES_OPTION)
        {
            String selectedId = this.jTableSupplierTab4MiscTransaction.getValueAt(this.jTableSupplierTab4MiscTransaction.getSelectedRow(), 0).toString();
            SupplierMiscBean misc = this.supplierTab4MiscTableModel.getBeanById(selectedId);
        
            SupplierDelegate.getInstance().deleteMisc( misc );
            
            if (this.jListSupplierTab4Supplier.getSelectedIndex() != -1)
                this.jListSupplierTab4SupplierValueChanged(null);
        }
    }//GEN-LAST:event_jButtonSupplierTab4RemoveActionPerformed

    private void jButtonSupplierTab5RemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab5RemoveActionPerformed
        if (this.jTableSupplierTab5CashTransaction.getSelectedRow()==-1)
            return;
        
        if (this.jListSupplierTab5Supplier.getSelectedIndex() == -1)
            return;
        
        int flag = JOptionPane.showConfirmDialog(this, "您确定要删除此项资料？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (flag == JOptionPane.YES_OPTION)
        {
            String selectedId = this.jTableSupplierTab5CashTransaction.getValueAt(this.jTableSupplierTab5CashTransaction.getSelectedRow(), 0).toString();
            SupplierCashBean cash = this.supplierTab5CashTableModel.getBeanById(selectedId);
        
            SupplierDelegate.getInstance().deleteCash( cash );
            
            if (this.jListSupplierTab5Supplier.getSelectedIndex() != -1)
                this.jListSupplierTab5SupplierValueChanged(null);
        }
    }//GEN-LAST:event_jButtonSupplierTab5RemoveActionPerformed

    private void jTableSupplierTab3FuelTransactionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSupplierTab3FuelTransactionMouseClicked
        if (evt.getClickCount() > 1)
            this.jButtonSupplierTab3Edit.doClick();
    }//GEN-LAST:event_jTableSupplierTab3FuelTransactionMouseClicked

    private void jTableSupplierTab4MiscTransactionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSupplierTab4MiscTransactionMouseClicked
        if (evt.getClickCount() > 1)
            this.jButtonSupplierTab4Edit.doClick();
    }//GEN-LAST:event_jTableSupplierTab4MiscTransactionMouseClicked

    private void jTableSupplierTab5CashTransactionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSupplierTab5CashTransactionMouseClicked
        if (evt.getClickCount() > 1)
            this.jButtonSupplierTab5Edit.doClick();
    }//GEN-LAST:event_jTableSupplierTab5CashTransactionMouseClicked

    private void jTextFieldSupplierTab5DateFromMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldSupplierTab5DateFromMouseClicked
        this.jButtonSupplierTab5DateChooser.doClick();
    }//GEN-LAST:event_jTextFieldSupplierTab5DateFromMouseClicked

    private void jTextFieldSupplierTab4DateFromMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldSupplierTab4DateFromMouseClicked
        this.jButtonSupplierTab4DateChooser.doClick();
    }//GEN-LAST:event_jTextFieldSupplierTab4DateFromMouseClicked

    private void jTextFieldSupplierTab3DateToMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldSupplierTab3DateToMouseClicked
        this.jButtonSupplierTab3DateChooser.doClick();
    }//GEN-LAST:event_jTextFieldSupplierTab3DateToMouseClicked

    private void jTextFieldSupplierTab2DateFromMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldSupplierTab2DateFromMouseClicked
        this.jButtonSupplierTab2DateChooser.doClick();
    }//GEN-LAST:event_jTextFieldSupplierTab2DateFromMouseClicked

    private void jTableSupplierTab2ChequeTransactionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSupplierTab2ChequeTransactionMouseClicked
        if (evt.getClickCount() > 1)
            this.jButtonSupplierTab2Edit.doClick();
    }//GEN-LAST:event_jTableSupplierTab2ChequeTransactionMouseClicked

    private void jListSupplierTab7ValueChanged()
    {
        if (this.jListSupplierTab7Supplier.getSelectedIndex()==-1)
            return;
        
        long startDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab7DateFrom);
        long endDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab7DateTo);
        
        if (startDate==0 || endDate==0)
            return;
                
        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "读取资料中...");
            
            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception 
                {       
                    ControlPanel.this.jButtonSupplierTab7Cash.setEnabled(true);
                    ControlPanel.this.jButtonSupplierTab7Cheque.setEnabled(true);
                    ControlPanel.this.jButtonSupplierTab7Fuel.setEnabled(true);
                    ControlPanel.this.jButtonSupplierTab7Misc.setEnabled(true);
                    ControlPanel.this.jButtonSupplierTab7Stock.setEnabled(true);
                    ControlPanel.this.jButtonSupplierTab7Saving.setEnabled(true);
                    ControlPanel.this.jButtonSupplierTab7Withdrawal.setEnabled(true);
                    ControlPanel.this.jTabbedPaneSupplierTab7.setEnabled(true);
                
                    Calendar startDay = Calendar.getInstance();
                    startDay.setTimeInMillis(startDate);
                    startDay.set(Calendar.HOUR_OF_DAY, 0);
                    startDay.set(Calendar.MINUTE, 0);
                    startDay.set(Calendar.SECOND, 0);         

                    Calendar endDay = Calendar.getInstance();
                    endDay.setTimeInMillis(endDate);
                    endDay.set(Calendar.HOUR_OF_DAY, 23);
                    endDay.set(Calendar.MINUTE, 59);
                    endDay.set(Calendar.SECOND, 59);         

                    String supplierId = ((SupplierBean) ControlPanel.this.jListSupplierTab7Supplier.getSelectedValue()).getId();
                    SupplierBean supplierObj = SupplierDelegate.getInstance().getSupplierById(supplierId);
                    
                    if (supplierObj.isChanged()) {
                        dialog.setStatus("计算来货数据中...");
                        SupplierDelegate.getInstance().updateSupplierSummary(supplierId);
                        if (supplierObj.isSavingAccount()) {
                            dialog.setStatus("计算储蓄数据中...");
                            SupplierDelegate.getInstance().updateSupplierSummarySaving(supplierId);
                        }
                    }
                    
                    // Set Supplier Info
                    ControlPanel.this.jLabelSupplierTab7Name.setText(supplierObj.getShipNumber() + ": " + supplierObj.getName());

                    dialog.setStatus("准备报表中...");
                    
                    // Check if Supplier has saving account
                    if (supplierObj.isSavingAccount()==true) {
                        jButtonSupplierTab7Saving.setVisible(true);
                        jTextFieldSupplierTab7Saving.setVisible(true);                        
                        jButtonSupplierTab7Withdrawal.setVisible(true);
                        jTextFieldSupplierTab7Withdrawal.setVisible(true);
                        
                        jTabbedPaneSupplierTab7.add(jPanelSupplierTab7Saving);
                        jTabbedPaneSupplierTab7.add(jPanelSupplierTab7Withdrawal);
                        jTabbedPaneSupplierTab7.setTitleAt(5, "储蓄");
                        jTabbedPaneSupplierTab7.setTitleAt(6, "提款");
                    } else {
                        jButtonSupplierTab7Saving.setVisible(false);
                        jTextFieldSupplierTab7Saving.setVisible(false);
                        jTabbedPaneSupplierTab7.remove(jPanelSupplierTab7Saving);
                        
                        jButtonSupplierTab7Withdrawal.setVisible(false);
                        jTextFieldSupplierTab7Withdrawal.setVisible(false);
                        jTabbedPaneSupplierTab7.remove(jPanelSupplierTab7Withdrawal);
                    }
                    
                    List<InTransactionBean> list1 = 
                        InTransactionDelegate.getInstance().getTransactionListByDateAndSupplier(
                            startDay.getTimeInMillis(), endDay.getTimeInMillis(), supplierObj, true);

                    List<SupplierCashBean> list2 = 
                        SupplierDelegate.getInstance().getCashListByDateAndSupplier(
                            startDay.getTimeInMillis(), endDay.getTimeInMillis(), supplierObj);

                    List<SupplierFuelBean> list3 = 
                        SupplierDelegate.getInstance().getFuelListByDateAndSupplier(
                            startDay.getTimeInMillis(), endDay.getTimeInMillis(), supplierObj);

                    List<SupplierChequeBean> list4 = 
                        SupplierDelegate.getInstance().getChequeListByDateAndSupplier(
                            startDay.getTimeInMillis(), endDay.getTimeInMillis(), supplierObj);

                    List<SupplierMiscBean> list5 = 
                        SupplierDelegate.getInstance().getMiscListByDateAndSupplier(
                            startDay.getTimeInMillis(), endDay.getTimeInMillis(), supplierObj);
                    
                    List<InTransactionBean> list6 = 
                        InTransactionDelegate.getInstance().getSavingListByDateAndSupplier(
                            startDay.getTimeInMillis(), endDay.getTimeInMillis(), supplierObj);
                    
                    List<SupplierWithdrawalBean> list7 = 
                        SupplierDelegate.getInstance().getWithdrawalListByDateAndSupplier(
                            startDay.getTimeInMillis(), endDay.getTimeInMillis(), supplierObj);
        
                    double totalAmount1 = 0.0d;
                    double totalSaving = 0.0d;
                    ControlPanel.this.supplierTab7StockTableModel.removeAll();
                    for (InTransactionBean stock : list1)
                    {
                        List<InTransactionLineBean> lineList = 
                            InTransactionDelegate.getInstance().getTransactionLineByTransactionId(stock.getId());
                        if (lineList == null || lineList.size()==0)
                            continue;
                        
                        ControlPanel.this.supplierTab7StockTableModel.addBean( stock );
                        totalAmount1 = totalAmount1 + stock.getTotalPrice();
                        totalSaving = totalSaving + stock.getTotalSaving();
                    }        
                    ControlPanel.this.jTextFieldSupplierTab7Stock.setText( currencyFormatter.format( totalAmount1 ) );

                    double totalAmount2 = 0.0d;
                    ControlPanel.this.supplierTab7CashTableModel.removeAll();
                    for (SupplierCashBean cash : list2)
                    {
                        ControlPanel.this.supplierTab7CashTableModel.addBean( cash );
                        totalAmount2 = totalAmount2 + cash.getCashAmount();
                    }        
                    ControlPanel.this.jTextFieldSupplierTab7Cash.setText( currencyFormatter.format( totalAmount2 ) );

                    double totalAmount3 = 0.0d;
                    ControlPanel.this.supplierTab7FuelTableModel.removeAll();
                    for (SupplierFuelBean fuel : list3)
                    {
                        ControlPanel.this.supplierTab7FuelTableModel.addBean( fuel );
                        totalAmount3 = totalAmount3 + fuel.getFuelTotalPrice();
                    }        
                    ControlPanel.this.jTextFieldSupplierTab7Fuel.setText( currencyFormatter.format( totalAmount3 ) );
        
                    double totalAmount4 = 0.0d;
                    ControlPanel.this.supplierTab7ChequeTableModel.removeAll();
                    for (SupplierChequeBean cheque : list4)
                    {
                        ControlPanel.this.supplierTab7ChequeTableModel.addBean( cheque );
                        totalAmount4 = totalAmount4 + cheque.getChequeAmount();
                    }        
                    ControlPanel.this.jTextFieldSupplierTab7Cheque.setText( currencyFormatter.format( totalAmount4 ) );

                    double totalAmount5 = 0.0d;
                    ControlPanel.this.supplierTab7MiscTableModel.removeAll();
                    for (SupplierMiscBean misc : list5)
                    {
                        ControlPanel.this.supplierTab7MiscTableModel.addBean( misc );
                        totalAmount5 = totalAmount5 + misc.getMiscAmount();
                    }        
                    ControlPanel.this.jTextFieldSupplierTab7Misc.setText( currencyFormatter.format( totalAmount5 ) );
                    
                    double totalAmount6 = 0.0d;
                    ControlPanel.this.supplierTab7SavingTableModel.removeAll();
                    for (InTransactionBean savingBean : list6)
                    {
                        ControlPanel.this.supplierTab7SavingTableModel.addBean( savingBean );
                        totalAmount6 = totalAmount6 + savingBean.getTotalSaving();
                    }        
                    ControlPanel.this.jTextFieldSupplierTab7Saving.setText( currencyFormatter.format( totalAmount6 ) );
                    
                    double totalWithdrawal = 0.0d;
                    ControlPanel.this.supplierTab7WithdrawalTableModel.removeAll();
                    for (SupplierWithdrawalBean withdrawalBean : list7)
                    {
                        ControlPanel.this.supplierTab7WithdrawalTableModel.addBean( withdrawalBean );
                        totalWithdrawal = totalWithdrawal + withdrawalBean.getCashAmount();
                    }        
                    ControlPanel.this.jTextFieldSupplierTab7Withdrawal.setText( currencyFormatter.format( totalWithdrawal ) );
                    
                    // Display total amount
                    double totalAmount = totalAmount1 + totalAmount2 - totalAmount3 - totalAmount4 - totalAmount5;
                    ControlPanel.this.jLabelSupplierTab7TotalAmount.setText( currencyFormatter.format( totalAmount ) );                    
                    
                    Calendar summary_startDay = Calendar.getInstance();
                    summary_startDay.setTimeInMillis(startDate);
                    summary_startDay.set(Calendar.DATE, summary_startDay.get(Calendar.DATE)-2);

                    SupplierSummaryBean summary = 
                            SupplierDelegate.getInstance().getSummaryBySupplierAndDate(supplierObj.getId(), summary_startDay.getTimeInMillis());
                    SupplierSummarySavingBean summarySaving = 
                            SupplierDelegate.getInstance().getSummarySavingBySupplierAndDate(supplierObj.getId(), summary_startDay.getTimeInMillis());

                    // -- DEBUG ONLY
                    Date date = new Date();
                    date.setTime(startDate);                    
                    System.err.println("1------------------------");
                    System.err.println("Supplier="+supplierObj.getId());
                    System.err.println("Start Date="+date);
                    System.err.println("Balance="+summary.getBalance());
                    // -- .end of DEBUG ONLY
                    
                    // Update Summary Panel
                    double oweAmount = summary.getBalance();
                    double balanceAmount = totalAmount + oweAmount;                    
                    ControlPanel.this.jTextFieldSupplierTab7LastOwe.setText(currencyFormatter.format( oweAmount ));
                    ControlPanel.this.jTextFieldSupplierTab7Balance.setText(currencyFormatter.format( balanceAmount ));
                    
                    // Update Summary Saving Panel
                    double lastSaving = summarySaving.getBalance();
                    double balanceSaving = lastSaving + totalSaving - totalWithdrawal; 
                    ControlPanel.this.jTextFieldSupplierTab7LastSaving.setText(currencyFormatter.format( lastSaving ));
                    ControlPanel.this.jTextFieldSupplierTab7TotalSaving.setText(currencyFormatter.format( balanceSaving ));
                    
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
    
    private void jListSupplierTab7SupplierValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListSupplierTab7SupplierValueChanged
        jListSupplierTab7ValueChanged();
    }//GEN-LAST:event_jListSupplierTab7SupplierValueChanged

    private void jButtonSupplierTab7StockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab7StockActionPerformed
        this.jTabbedPaneSupplierTab7.setSelectedIndex(0);
    }//GEN-LAST:event_jButtonSupplierTab7StockActionPerformed

    private void jButtonSupplierTab7CashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab7CashActionPerformed
        this.jTabbedPaneSupplierTab7.setSelectedIndex(1);
    }//GEN-LAST:event_jButtonSupplierTab7CashActionPerformed

    private void jButtonSupplierTab7FuelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab7FuelActionPerformed
        this.jTabbedPaneSupplierTab7.setSelectedIndex(2);
    }//GEN-LAST:event_jButtonSupplierTab7FuelActionPerformed

    private void jButtonSupplierTab7ChequeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab7ChequeActionPerformed
        this.jTabbedPaneSupplierTab7.setSelectedIndex(3);
    }//GEN-LAST:event_jButtonSupplierTab7ChequeActionPerformed

    private void jButtonSupplierTab7MiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab7MiscActionPerformed
        this.jTabbedPaneSupplierTab7.setSelectedIndex(4);
    }//GEN-LAST:event_jButtonSupplierTab7MiscActionPerformed

    private void jTextFieldSupplierTab7DateFromMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldSupplierTab7DateFromMouseClicked
        this.jButtonSupplierTab7DateChooser.doClick();
    }//GEN-LAST:event_jTextFieldSupplierTab7DateFromMouseClicked

    private void jButtonSupplierTab7PrintAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab7PrintAllActionPerformed
        try 
        {
            int flag = JOptionPane.showConfirmDialog(this, "您确定要打印全船资料？","确认打印",JOptionPane.YES_NO_OPTION);
            if (flag == JOptionPane.NO_OPTION || flag == JOptionPane.CANCEL_OPTION || flag == JOptionPane.CLOSED_OPTION)
                return;
            
            long startDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab7DateFrom);
            long endDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab7DateTo);
            if (startDate==0 || endDate==0)
            {
                JOptionPane.showMessageDialog(
                        this, 
                        "请选择正确日期。", 
                        "日期格式错误", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String filePath = this.saveToPath+Calendar.getInstance().getTimeInMillis()+".pdf";
            ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "正准备 PDF 文档...");
            
            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception 
                {                    
                    Map<String, Map> data = new HashMap<>();

                    List<InTransactionBean> inTransactionList = InTransactionDelegate.getInstance().getTransactionListByDateRange(startDate, endDate, true);
                    for (InTransactionBean inTransactionBean : inTransactionList)
                    {
                        if (inTransactionBean.getLineList().size()==0)
                            continue;
                        
                        //System.err.println("InTransactionBean - " + inTransactionBean.getSupplierId());
                        if (data.containsKey( inTransactionBean.getSupplierId() )==false)
                        {
                            data.put(inTransactionBean.getSupplierId(), new HashMap<String, Double>());
                        }

                        Map<String, Double> subset = data.get( inTransactionBean.getSupplierId() );
                        if (subset.containsKey("IN")==false)
                        {
                            subset.put("IN", inTransactionBean.getTotalPrice());
                            subset.put("IN-COUNT", 1.0);
                        }
                        else
                        {
                            subset.put("IN", subset.get("IN").doubleValue()+inTransactionBean.getTotalPrice());
                            subset.put("IN-COUNT", subset.get("IN-COUNT").doubleValue()+1.0);
                        }
                    }

                    List<SupplierCashBean> cashList = SupplierDelegate.getInstance().getCashListByDateRange(startDate, endDate);
                    for (SupplierCashBean cashBean : cashList)
                    {
                        if (data.containsKey( cashBean.getSupplierId() )==false)
                        {
                            data.put(cashBean.getSupplierId(), new HashMap<String, Double>());
                        }

                        Map<String, Double> subset = data.get( cashBean.getSupplierId() );
                        if (subset.containsKey("CASH")==false)
                        {
                            subset.put("CASH", cashBean.getCashAmount());
                        }
                        else
                        {
                            subset.put("CASH", subset.get("CASH").doubleValue()+cashBean.getCashAmount());
                        }
                    }

                    List<SupplierChequeBean> chequeList = SupplierDelegate.getInstance().getChequeListByDateRange(startDate, endDate);
                    for (SupplierChequeBean chequeBean : chequeList)
                    {
                        if (data.containsKey( chequeBean.getSupplierId() )==false)
                        {
                            data.put(chequeBean.getSupplierId(), new HashMap<String, Double>());
                        }

                        Map<String, Double> subset = data.get( chequeBean.getSupplierId() );
                        if (subset.containsKey("CHEQUE")==false)
                        {
                            subset.put("CHEQUE", chequeBean.getChequeAmount());
                        }
                        else
                        {
                            subset.put("CHEQUE", subset.get("CHEQUE").doubleValue()+chequeBean.getChequeAmount());
                        }
                    }

                    List<SupplierFuelBean> fuelList = SupplierDelegate.getInstance().getFuelListByDateRange(startDate, endDate);
                    for (SupplierFuelBean fuelBean : fuelList)
                    {
                        if (data.containsKey( fuelBean.getSupplierId() )==false)
                        {
                            data.put(fuelBean.getSupplierId(), new HashMap<String, Double>());
                        }

                        Map<String, Double> subset = data.get( fuelBean.getSupplierId() );
                        if (subset.containsKey("FUEL")==false)
                        {
                            subset.put("FUEL", fuelBean.getFuelTotalPrice());
                        }
                        else
                        {
                            subset.put("FUEL", subset.get("FUEL").doubleValue()+fuelBean.getFuelTotalPrice());
                        }
                    }

                    List<SupplierMiscBean> miscList = SupplierDelegate.getInstance().getMiscListByDateRange(startDate, endDate);
                    for (SupplierMiscBean miscBean : miscList)
                    {
                        if (data.containsKey( miscBean.getSupplierId() )==false)
                        {
                            data.put(miscBean.getSupplierId(), new HashMap<String, Double>());
                        }

                        Map<String, Double> subset = data.get( miscBean.getSupplierId() );
                        if (subset.containsKey("MISC")==false)
                        {
                            subset.put("MISC", miscBean.getMiscAmount());
                        }
                        else
                        {
                            subset.put("MISC", subset.get("MISC").doubleValue()+miscBean.getMiscAmount());
                        }
                    }

                    Set<String> supplierIds = data.keySet();
                    Object[][] tableData = new Object[supplierIds.size()][8];
                    int i=0;
                    for (String supplierId : supplierIds)
                    {
                        Map<String, Double> subset = data.get( supplierId );

                        SupplierBean supplierBean = SupplierDelegate.getInstance().getSupplierById(supplierId);

                        tableData[i][0] = supplierBean==null ? supplierId : supplierBean.getName();
                        tableData[i][1] = subset.containsKey("IN-COUNT")==false ? 0.0d : subset.get("IN-COUNT");
                        tableData[i][2] = subset.containsKey("IN")==false ? 0.0d : subset.get("IN");
                        tableData[i][3] = subset.containsKey("CASH")==false ? 0.0d : subset.get("CASH");
                        tableData[i][4] = subset.containsKey("FUEL")==false ? 0.0d : subset.get("FUEL");
                        tableData[i][5] = subset.containsKey("CHEQUE")==false ? 0.0d : subset.get("CHEQUE");
                        tableData[i][6] = subset.containsKey("MISC")==false ? 0.0d : subset.get("MISC");

                        tableData[i][7] = ((Double)tableData[i][2]).doubleValue() - ((Double)tableData[i][3]).doubleValue() - 
                                          ((Double)tableData[i][4]).doubleValue() - ((Double)tableData[i][5]).doubleValue() - 
                                          ((Double)tableData[i][6]).doubleValue();

                        i++;
                    }
            
                    ControlPDFBoxPrinter.getInstance().printReportFullSuppliers(filePath, startDate, endDate, tableData);
                    
                    return null;
                }

                @Override
                protected void done() 
                {
                    dialog.dispose();//close the modal dialog
                }
            };

            swingWorker.execute(); // this will start the processing on a separate thread
            SwingUtil.centerDialogOnScreen( dialog );
            
            // Open created PDF on fly
            if (RootPDFBoxPrinter.OPEN_PDF_ON_FLY == true)
                ControlPDFBoxPrinter.getInstance().openPDFonFly(filePath);
                
            Desktop desktop = Desktop.getDesktop();
            desktop.print(new File(filePath));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "系统错误：" + e.getMessage(), "系统错误", JOptionPane.ERROR_MESSAGE);
        } 
    }//GEN-LAST:event_jButtonSupplierTab7PrintAllActionPerformed

    private void jButtonSupplierTab7PrintSingleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab7PrintSingleActionPerformed
        try 
        {            
            SupplierBean supplier = (SupplierBean) this.jListSupplierTab7Supplier.getSelectedValue();
            if (supplier==null)
            {
                JOptionPane.showMessageDialog(this, "请选择正确船户。", "船户错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int flag = JOptionPane.showConfirmDialog(this, "您确定要打印单船资料？","确认打印",JOptionPane.YES_NO_OPTION);
            if (flag == JOptionPane.NO_OPTION || flag == JOptionPane.CANCEL_OPTION || flag == JOptionPane.CLOSED_OPTION)
                return;
                        
            long startDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab7DateFrom);
            long endDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab7DateTo);
            if (startDate==0 || endDate==0)
            {
                JOptionPane.showMessageDialog(this, "请选择日期期限。", "日期格式错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String filePath = this.saveToPath+Calendar.getInstance().getTimeInMillis()+".pdf";
            ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "正准备 PDF 文档...");
            
            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception 
                {
                    SupplierBean supplierObj = SupplierDelegate.getInstance().getSupplierById(supplier.getId());
                    if (supplierObj.isChanged()) {
                        dialog.setStatus("计算来货数据中...");
                        SupplierDelegate.getInstance().updateSupplierSummary(supplierObj.getId());
                        if (supplierObj.isSavingAccount()) {
                            dialog.setStatus("计算储蓄数据中...");
                            SupplierDelegate.getInstance().updateSupplierSummarySaving(supplierObj.getId());
                        }
                    }
                    dialog.setStatus("计算报表中...");
                    
                    List<InTransactionBean> tmpList = InTransactionDelegate.getInstance().getTransactionListByDateAndSupplier(startDate, endDate, supplierObj, true);
                    List<InTransactionBean> inTransactionList = new ArrayList<InTransactionBean>();
                    for (InTransactionBean transactionObj : tmpList) {
                        List<InTransactionLineBean> list = InTransactionDelegate.getInstance().getTransactionLineByTransactionId(transactionObj.getId());
                        if (list.isEmpty())
                            continue;
                        inTransactionList.add(transactionObj);
                    }
                    
                    List<SupplierCashBean> cashList = SupplierDelegate.getInstance().getCashListByDateAndSupplier(startDate, endDate, supplierObj);
                    List<SupplierChequeBean> chequeList = SupplierDelegate.getInstance().getChequeListByDateAndSupplier(startDate, endDate, supplierObj);
                    List<SupplierFuelBean> fuelList = SupplierDelegate.getInstance().getFuelListByDateAndSupplier(startDate, endDate, supplierObj);
                    List<SupplierMiscBean> miscList = SupplierDelegate.getInstance().getMiscListByDateAndSupplier(startDate, endDate, supplierObj);

                    ControlPDFBoxPrinter.getInstance().printReportSingleSupplier(filePath, startDate, endDate, supplierObj, inTransactionList, cashList, chequeList, fuelList, miscList);
                    
                    return null;
                }

                @Override
                protected void done() {
                    dialog.dispose();//close the modal dialog
                }
            };

            swingWorker.execute(); // this will start the processing on a separate thread
            SwingUtil.centerDialogOnScreen( dialog );

            // Open created PDF on fly
            if (RootPDFBoxPrinter.OPEN_PDF_ON_FLY == true)
                ControlPDFBoxPrinter.getInstance().openPDFonFly(filePath);
                
            Desktop desktop = Desktop.getDesktop();
            desktop.print(new File(filePath));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "系统错误: " + e.getMessage(), "系统错误", JOptionPane.ERROR_MESSAGE);
        } 
    }//GEN-LAST:event_jButtonSupplierTab7PrintSingleActionPerformed

    private void jButtonCustomerTab2DateChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomerTab2DateChooserActionPerformed
        long selectedTimeInMillis = this.showDatePicker(null);
        
        this.showDateRangeFullMonth(selectedTimeInMillis, this.jTextFieldCustomerTab2DateFrom, this.jTextFieldCustomerTab2DateTo);   
        
        this.fillCustomerTab2Table();
    }//GEN-LAST:event_jButtonCustomerTab2DateChooserActionPerformed

    private void jTextFieldCustomerTab2DateFromMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldCustomerTab2DateFromMouseClicked
        this.jButtonCustomerTab2DateChooser.doClick();
    }//GEN-LAST:event_jTextFieldCustomerTab2DateFromMouseClicked

    private void jTextFieldCustomerTab2DateToMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldCustomerTab2DateToMouseClicked
        this.jButtonCustomerTab2DateChooser.doClick();
    }//GEN-LAST:event_jTextFieldCustomerTab2DateToMouseClicked

    private void jTableCustomerTab2PaymentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCustomerTab2PaymentMouseClicked
        if (evt.getClickCount() > 1)
            this.jButtonCustomerTab2Edit.doClick();
    }//GEN-LAST:event_jTableCustomerTab2PaymentMouseClicked

    private void jButtonCustomerTab2AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomerTab2AddActionPerformed
        if (this.jListCustomerTab2Customer.getSelectedIndex() != -1)
            SwingUtil.centerDialogOnScreen( new CustomerPaymentEditorDialog(this.parent, true, this, (CustomerBean)this.jListCustomerTab2Customer.getSelectedValue(), null) );
    }//GEN-LAST:event_jButtonCustomerTab2AddActionPerformed

    private void jButtonCustomerTab2EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomerTab2EditActionPerformed
        if (this.jTableCustomerTab2Payment.getSelectedRow()==-1)
            return;
        
        if (this.jListCustomerTab2Customer.getSelectedIndex() == -1)
            return;
        
        String selectedId = this.jTableCustomerTab2Payment.getValueAt(this.jTableCustomerTab2Payment.getSelectedRow(), 0).toString();
        CustomerPaymentBean payment = this.customerTab2PaymentTableModel.getBeanById(selectedId);
        
        SwingUtil.centerDialogOnScreen( new CustomerPaymentEditorDialog(this.parent, true, this, null, payment) );
    }//GEN-LAST:event_jButtonCustomerTab2EditActionPerformed

    private void jButtonCustomerTab2RemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomerTab2RemoveActionPerformed
        if (this.jTableCustomerTab2Payment.getSelectedRow()==-1)
            return;
        
        if (this.jListCustomerTab2Customer.getSelectedIndex() == -1)
            return;
        
        int flag = JOptionPane.showConfirmDialog(this, "您确定要删除这资料？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (flag == JOptionPane.YES_OPTION)
        {
            String selectedId = this.jTableCustomerTab2Payment.getValueAt(this.jTableCustomerTab2Payment.getSelectedRow(), 0).toString();
            CustomerPaymentBean payment = this.customerTab2PaymentTableModel.getBeanById(selectedId);
        
            CustomerDelegate.getInstance().deletePayment( payment );
            
            if (this.jListCustomerTab2Customer.getSelectedIndex() != -1)
                this.jListCustomerTab2CustomerValueChanged(null);
            
            // Re-Calculate
            jButtonCustomerTab3ShowSummary2.doClick();  
        }
    }//GEN-LAST:event_jButtonCustomerTab2RemoveActionPerformed

    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
        ((JFrameMain)this.parent).switchToMainPanel();
    }//GEN-LAST:event_jButtonExitActionPerformed

    private void jTableCustomerTab4ResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCustomerTab4ResultMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableCustomerTab4ResultMouseClicked

    private void jButtonCustomerTab4ShowSummaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomerTab4ShowSummaryActionPerformed

        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "数据准备中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {       
                int year = Integer.parseInt( ControlPanel.this.jTextFieldCustomerTab4Year.getText().trim() );
                int month = ControlPanel.this.jComboBoxCustomerTab4Month.getSelectedIndex();
                Calendar dateStart = Calendar.getInstance();
                dateStart.set(Calendar.YEAR, year);
                dateStart.set(Calendar.MONTH, month);
                dateStart.set(Calendar.DAY_OF_MONTH, 1);
                
                Calendar dateEnd = Calendar.getInstance();
                dateEnd.set(Calendar.YEAR, year);
                dateEnd.set(Calendar.MONTH, month);
                dateEnd.set(Calendar.DAY_OF_MONTH, dateEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
                                
                ControlPanel.this.customerTab4SalesCheckTableModel.removeAll();

                List<SalesBean> salesList1 = 
                        SalesDelegate.getInstance().getSalesByStatusAndDateRange(SalesBean.STATUS_INCOMPLETE, dateStart.getTimeInMillis(), dateEnd.getTimeInMillis(), false);        
                List<SalesBean> salesList2 = 
                        SalesDelegate.getInstance().getSalesByStatusAndDateRange(SalesBean.STATUS_COMPLETED, dateStart.getTimeInMillis(), dateEnd.getTimeInMillis(), false);

                List<SalesBean> salesList = new ArrayList();
                for (SalesBean salesObj : salesList1)
                {
                    salesList.add(salesObj);
                }

                for (SalesBean salesObj : salesList2)
                {
                    salesList.add(salesObj);
                }

                Collections.sort( salesList ); 

                int recordSize = 0, processCount = 1;
                for (SalesBean salesObj : salesList)
                {
                    dialog.setStatus("检查出货单中［" + salesObj.getCustomerName() + "］... " + processCount++ + " of " + salesList.size());
                    
                    CustomerSalesCheckBean bean = new CustomerSalesCheckBean();
                    bean.setCustomerIdName(salesObj.getCustomerId() + " - " + salesObj.getCustomerName());
                    bean.setCustomer(CustomerDelegate.getInstance().getCustomerByCustomerId(salesObj.getCustomerId()));
                    bean.setSales(salesObj);
                    if (salesObj.hasInvoiceNo())
                        bean.setPrinted(true);
                    else {
                        bean.setPrinted(false);
                        bean.setErrorMessage("没打印，");
                    }
                    // Zero unit-price in SalesLine
                    List<SalesLineBean> salesLineList = SalesDelegate.getInstance().getSalesLineBySalesId(salesObj.getId());
                    if (salesLineList==null || salesLineList.isEmpty()) {
                        // continue;
                        bean.setErrorMessage(bean.getErrorMessage() + "无出货记录，");
                    }
                    
                    for (SalesLineBean salesLine : salesLineList)
                    {
                        if (salesLine.getUnitPrice() == 0.0d)
                        {
                            bean.setPriceInvalid(true);
                            bean.setErrorMessage(bean.getErrorMessage() + "总额不正确，");
                            break;
                        }
                    }

                    if (bean.isPriceInvalid() || bean.isPrinted()==false) {
                        ControlPanel.this.customerTab4SalesCheckTableModel.addBean( bean );
                        recordSize++;
                    }
                }

                jLabelCustomerTab4RecordSize.setText("搜索结果：" + recordSize + "份记录");
                
                return null;
            }

            @Override
            protected void done() 
            {
                dialog.dispose();//close the modal dialog
            }
        };
        swingWorker.execute(); // this will start the processing on a separate thread
        SwingUtil.centerDialogOnScreen( dialog ); 
    }//GEN-LAST:event_jButtonCustomerTab4ShowSummaryActionPerformed

    private void jTextFieldReportTab2DateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldReportTab2DateKeyReleased
        this.dateFieldKeyReleasedListener(evt, jTextFieldReportTab2Date);
    }//GEN-LAST:event_jTextFieldReportTab2DateKeyReleased

    private void jButtonReportTab2DatePickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReportTab2DatePickerActionPerformed
        this.showDatePicker(jTextFieldReportTab2Date);
    }//GEN-LAST:event_jButtonReportTab2DatePickerActionPerformed

    private void jButtonReportTab2ShowReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReportTab2ShowReportActionPerformed
        if (this.getSelectedDateInMillis(this.jTextFieldReportTab2Date)==-1)
        {
            JOptionPane.showMessageDialog(
                    this, 
                    "请输入正确日期。", 
                    "日期格式错误", 
                    JOptionPane.ERROR_MESSAGE);
            
            this.jTextFieldReportTab2Date.requestFocus();
            this.jTextFieldReportTab2Date.setCaretPosition(0);
            this.jTextFieldReportTab2Date.select(0, 2);
            
            return;
        }
        
        if (this.listModelReportTab2ItemSelection.getSize()==0)
        {
            JOptionPane.showMessageDialog(
                    this, 
                    "请选择正确鱼类。", 
                    "选择错误", 
                    JOptionPane.ERROR_MESSAGE);
            
            return;
        }
        
        if (this.listModelReportTab2Item.size() == 0)
        {
            JOptionPane.showMessageDialog(
                    this, 
                    "请选择正确鱼类。", 
                    "选择错误", 
                    JOptionPane.ERROR_MESSAGE);
            
            return;
        }
        
        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "数据准备中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {   
                // Remove all elements from report data
                jTextAreaReportTab2.setText( "" );

                // Get all selected items
                List<ItemBean> selectedItemList = new ArrayList<ItemBean>();
                for (int i=0 ; i<listModelReportTab2ItemSelection.size() ; i++)
                {
                    selectedItemList.add( (ItemBean) listModelReportTab2ItemSelection.getElementAt(i) );
                    System.err.println( "Selected item: " + (ItemBean) listModelReportTab2ItemSelection.getElementAt(i) );
                }

                List<ReportStockInBean> stockInList =
                        ReportDelegate.getInstance().getStockInByDateAndItem( 
                                getSelectedDateInMillis(jTextFieldReportTab2Date), 
                                selectedItemList);

                List<ReportStockOutBean> stockOutList =
                        ReportDelegate.getInstance().getStockOutByDateAndItem( 
                                getSelectedDateInMillis(jTextFieldReportTab2Date), 
                                selectedItemList);

                double stockInTotalWeight = 0.0d;  
                double stockInAssignedWeight = 0.0d;   
                double stockOutTotalWeight = 0.0d;

                if (stockInList.size() != 0)
                {
                    int iteration = 0;
                    boolean readyToPrintStockOut = false;

                    for (ReportStockInBean stockInBean : stockInList)            
                    {                                                  
                        jTextAreaReportTab2.append( stockInBean.getItemNewName() );
                        jTextAreaReportTab2.append( " " );
                        jTextAreaReportTab2.append( stockInBean.getSupplierId() );
                        jTextAreaReportTab2.append( " " );
                        jTextAreaReportTab2.append( stockInBean.getSupplierId().length()==0 ? "" : SupplierDelegate.getInstance().getSupplierById(stockInBean.getSupplierId()).getName() );
                        jTextAreaReportTab2.append( " " );
                        jTextAreaReportTab2.append( currencyFormatter.format( stockInBean.getWeight() ) );
                        jTextAreaReportTab2.append( " " );
                        jTextAreaReportTab2.append( (stockInBean.getCustomerId()==null || stockInBean.getCustomerId().length()==0) ? "" : "** " + stockInBean.getCustomerId() + " **" );
                        jTextAreaReportTab2.append( " " );
                        jTextAreaReportTab2.append( (stockInBean.getBucketNo()==null || stockInBean.getBucketNo().trim().length()==0) ? "" : "||" + stockInBean.getBucketNo() + "||" );
                        jTextAreaReportTab2.append( "\n" );

                        // Calculate stock in weight (only unassigned item)
                        if (stockInBean.getCustomerId()==null || stockInBean.getCustomerId().length()==0)
                            stockInTotalWeight += stockInBean.getWeight();
                        if (stockInBean.getCustomerId()!=null && stockInBean.getCustomerId().length()>0)
                            stockInAssignedWeight += stockInBean.getWeight();

                        ReportStockInBean nextStockInBean = null;
                        if (iteration != stockInList.size()-1) {
                            nextStockInBean = stockInList.get(iteration+1);
                        }

                        // Check if current stockInBean the last item in similar-list
                        // e.g. list{A,A,A,B,C,C}, list{A}, list{A,B}
                        if (iteration == stockInList.size()-1) {   // last item
                            readyToPrintStockOut = true;
                        }
                        else
                        {
                            if (stockInBean.getItemNewName().equalsIgnoreCase( nextStockInBean.getItemNewName() )==false)
                                readyToPrintStockOut = true;
                        }

                        if ( readyToPrintStockOut )
                        {
                            jTextAreaReportTab2.append( "\n" );
                            jTextAreaReportTab2.append( "来货总计: " );
                            jTextAreaReportTab2.append( currencyFormatter.format(stockInTotalWeight + stockInAssignedWeight) );
                            jTextAreaReportTab2.append( "\n" );

                            stockOutTotalWeight = 0.0d;
                            for (ReportStockOutBean stockOutBean : stockOutList)
                            {
                                //System.out.println("ControlPanel.showReport() - Compare " + previousItemNewName + " with " + stockOutBean.getItemNewName());
                                if (stockInBean.getItemNewName().equalsIgnoreCase( stockOutBean.getItemNewName() ))
                                {
                                    if (stockOutBean.getCustomerId().length()==0) {
                                        jTextAreaReportTab2.append( "\n\t" );
                                    } else {
                                        jTextAreaReportTab2.append( "\n" );
                                        jTextAreaReportTab2.append( stockOutBean.getCustomerId().length()==0 ? "" : CustomerDelegate.getInstance().getCustomerByCustomerId(stockOutBean.getCustomerId()).getName() );
                                        jTextAreaReportTab2.append( " " );                            
                                    }
                                    jTextAreaReportTab2.append( currencyFormatter.format( stockOutBean.getWeight() ) );
                                    jTextAreaReportTab2.append( " " );
                                    jTextAreaReportTab2.append( (stockOutBean.getCustomerId()==null || stockOutBean.getCustomerId().trim().length()==0) ? "" : "** " + stockOutBean.getCustomerId() + " **" );
                                    jTextAreaReportTab2.append( " " );
                                    jTextAreaReportTab2.append( (stockOutBean.getBucketNo()==null || stockOutBean.getBucketNo().trim().length()==0) ? "" : "||" + stockOutBean.getBucketNo() + "||" );

                                    stockOutTotalWeight += stockOutBean.getWeight();
                                }
                            }                    

                            jTextAreaReportTab2.append( "\n\n" );
                            jTextAreaReportTab2.append( "存货总计: " );
                            jTextAreaReportTab2.append( currencyFormatter.format(stockInTotalWeight-stockOutTotalWeight) );
                            jTextAreaReportTab2.append( "\n\n\n" );

                            stockInTotalWeight = 0.0d;
                            stockInAssignedWeight = 0.0d;

                            readyToPrintStockOut = false;
                        }

                        iteration++;
                    }

                    //System.err.println(jTextAreaReportTab2.getText());

                    /*
                    this.jTextAreaReportTab2.append( "\n" );
                    this.jTextAreaReportTab2.append( "总计: " );
                    this.jTextAreaReportTab2.append( currencyFormatter.format(stockInTotalWeight) );
                    this.jTextAreaReportTab2.append( "\n" );
                    this.jTextAreaReportTab2.append( "存货: " );
                    this.jTextAreaReportTab2.append( currencyFormatter.format(stockInTotalWeight-stockInAssignedWeight) );
                    this.jTextAreaReportTab2.append( "\n\n" );*/
                }
                
                return null;
            }

            @Override
            protected void done() 
            {
                dialog.dispose();//close the modal dialog
            }
        };
        swingWorker.execute(); // this will start the processing on a separate thread
        SwingUtil.centerDialogOnScreen( dialog );
    }//GEN-LAST:event_jButtonReportTab2ShowReportActionPerformed
    
    private void jButtonReportTab2RemoveSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReportTab2RemoveSelectionActionPerformed
        if (this.jListReportTab2ItemSelection.getSelectedIndex() != -1)
        {
            this.listModelReportTab2ItemSelection.removeElementAt( this.jListReportTab2ItemSelection.getSelectedIndex() );
        }
    }//GEN-LAST:event_jButtonReportTab2RemoveSelectionActionPerformed

    private void jButtonReportTab2RemoveAllSelectionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReportTab2RemoveAllSelectionsActionPerformed
        this.listModelReportTab2ItemSelection.removeAllElements();
    }//GEN-LAST:event_jButtonReportTab2RemoveAllSelectionsActionPerformed

    private void jListReportTab2CategoryValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListReportTab2CategoryValueChanged
        this.showFisheryItemByCategory(
                this.jListReportTab2Category,
                this.listModelReportTab2Item);
    }//GEN-LAST:event_jListReportTab2CategoryValueChanged

    private void jListReportTab2ItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListReportTab2ItemMouseClicked
        
        if (this.jListReportTab2Item.getSelectedIndex() != -1)
        {
            ItemBean selectedItem = (ItemBean) this.listModelReportTab2Item.getElementAt( this.jListReportTab2Item.getSelectedIndex() );
            this.listModelReportTab2ItemSelection.addElement( selectedItem );
            
            //SwingUtil.centerDialogOnScreen( new ItemPrefixDialog(this.parent, true, this, selectedItem) );                        
        }
    }//GEN-LAST:event_jListReportTab2ItemMouseClicked

    private void jButtonSupplierTab8ShowSummaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab8ShowSummaryActionPerformed
        
        int year = Integer.parseInt( this.jTextFieldSupplierTab8Year.getText().trim() );
        int month = this.jComboBoxSupplierTab8Month.getSelectedIndex();
        Calendar dateStart = Calendar.getInstance();
        dateStart.set(Calendar.YEAR, year);
        dateStart.set(Calendar.MONTH, month);
        dateStart.set(Calendar.DAY_OF_MONTH, 1);
        dateStart.set(Calendar.HOUR, 0);
        dateStart.set(Calendar.MINUTE, 0);
        dateStart.set(Calendar.SECOND, 1);
        
        Calendar dateEnd = Calendar.getInstance();
        dateEnd.set(Calendar.YEAR, year);
        dateEnd.set(Calendar.MONTH, month);
        dateEnd.set(Calendar.DAY_OF_MONTH, dateEnd.getActualMaximum(Calendar.DAY_OF_MONTH)-1);
        dateEnd.set(Calendar.HOUR, 23);
        dateEnd.set(Calendar.MINUTE, 59);
        dateEnd.set(Calendar.SECOND, 59);
        
        this.supplierTab8SalesCheckTableModel.removeAll();
        
        List<InTransactionBean> transactionList = 
                InTransactionDelegate.getInstance().getTransactionListByDateRange(dateStart.getTimeInMillis(), dateEnd.getTimeInMillis(), true);
        List<InTransactionBean> filterdList = new ArrayList<InTransactionBean>();
        
        // Check through transaction list, ignore those transaction with OK condition.
        for (InTransactionBean transactionBean : transactionList)
        {
            List<InTransactionLineBean> lineList = InTransactionDelegate.getInstance().getTransactionLineByTransactionId(transactionBean.getId());
            if (lineList == null || lineList.size() == 0)
                continue;
            
            if (transactionBean.getTransactionNo() != null && transactionBean.getTransactionNo().trim().length()==0)
                filterdList.add(transactionBean);
        }
                
        Collections.sort(filterdList);
        
        //System.err.println("Receive " + filterdList.size() + " sales records.");
        for (InTransactionBean transactionBean : filterdList)
        {                       
            SupplierSalesCheckBean bean = new SupplierSalesCheckBean();
            bean.setSupplier(SupplierDelegate.getInstance().getSupplierById(transactionBean.getSupplierId()));
            bean.setSales(transactionBean);
            bean.setPrinted(false);

            this.supplierTab8SalesCheckTableModel.addBean( bean );
        }
    }//GEN-LAST:event_jButtonSupplierTab8ShowSummaryActionPerformed

    private void jTableSupplierTab8ResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSupplierTab8ResultMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableSupplierTab8ResultMouseClicked

    private void jTableCustomerTab2ReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCustomerTab2ReportMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableCustomerTab2ReportMouseClicked

    private void jButtonCustomerTab3ShowSummary1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomerTab3ShowSummary1ActionPerformed
        this.jButtonCustomerTab3ShowSummary2.doClick();
        /*
        try 
        {          
            long startDate = this.getSelectedDateInMillis(this.jTextFieldCustomerTab2DateFrom);
            long endDate = this.getSelectedDateInMillis(this.jTextFieldCustomerTab2DateTo);
            if (startDate==0 || endDate==0)
            {
                JOptionPane.showMessageDialog(this, "请选择正确日期期限。", "日期格式错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (this.jListCustomerTab2Customer.getSelectedIndex()==-1)
            {
                //JOptionPane.showMessageDialog(this, "请选择客户。", "选项错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "读取资料中...");            
            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception 
                {                
                    dialog.setStatus("读取客户数据中...");
                    String customerId = 
                        ControlPanel.this.listModelCustomerTab2Customer.get( 
                                ControlPanel.this.jListCustomerTab2Customer.getSelectedIndex() ).getId();
                    CustomerBean customerObj = CustomerDelegate.getInstance().getCustomerByCustomerId(customerId);
                
                    if (customerObj.isChanged()) {
                        dialog.setStatus("计算出货数据中...");
                        // Recalculate summary for every month
                        CustomerDelegate.getInstance().updateSummaryByMonth(customerObj.getId());
                    }
            
                    // Get year/month from UI
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis( startDate );
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    
                    dialog.setStatus("准备数据报告中...");
                    CustomerSummaryBean summaryBean = 
                            CustomerDelegate.getInstance().getSummaryByCustomerAndDate(customerObj.getId(), year, month);
                    
                    // Update Sales status
                    SalesDelegate.getInstance().updateSalesStatus(startDate, endDate);
                    
                    List<CustomerTransactionBean> transactionList = new ArrayList<CustomerTransactionBean>();
                    
                    Calendar firstLineDate = Calendar.getInstance();
                    firstLineDate.setTimeInMillis(startDate);
                    firstLineDate.set(Calendar.HOUR, 0);
                    firstLineDate.set(Calendar.MINUTE, 0);
                    firstLineDate.set(Calendar.SECOND, 0);
                    
                    CustomerTransactionBean firstLine = new CustomerTransactionBean();
                    firstLine.setDate( firstLineDate.getTimeInMillis() );
                    firstLine.setDescription( "承接上月" );                        
                    firstLine.setTotal( summaryBean.getBalance() );
                    //transactionList.add(firstLine);
                    
                    int minuteCount = 1;
                    List<SalesBean> salesList = SalesDelegate.getInstance().getSalesByCustomerAndDateRange(customerObj.getId(), startDate, endDate, true);
                    for (SalesBean salesBean : salesList)
                    {
                        // SET earliest time for sales transaction, for sorting
                        Calendar calendarObj = Calendar.getInstance();
                        calendarObj.setTimeInMillis( salesBean.getDateTime() );
                        calendarObj.set(Calendar.HOUR_OF_DAY, 0);
                        calendarObj.set(Calendar.MINUTE, minuteCount++);
                                
                        CustomerTransactionBean bean = new CustomerTransactionBean();
                        bean.setDate( calendarObj.getTimeInMillis() );
                        bean.setCredit( salesBean.getTotalPrice() );
                        bean.setDescription( salesBean.getInvoiceNo() );
                        bean.setType( CustomerTransactionBean.TYPE_SALES );
                        bean.setCheckResult( checkSalesBean(salesBean) );
                        
                        transactionList.add(bean);
                    }
                    
                    List<CustomerPaymentBean> paymentList = CustomerDelegate.getInstance().getPaymentsByCustomerAndDateRange(customerObj.getId(), startDate, endDate);
                    for (CustomerPaymentBean paymentBean : paymentList)
                    {
                        // SET earliest time for sales transaction, for sorting
                        Calendar calendarObj = Calendar.getInstance();
                        calendarObj.setTimeInMillis( paymentBean.getDate() );
                        calendarObj.set(Calendar.HOUR_OF_DAY, 0);
                        calendarObj.set(Calendar.MINUTE, minuteCount++);
                        
                        CustomerTransactionBean bean = new CustomerTransactionBean();
                        bean.setDate( calendarObj.getTimeInMillis() );
                        bean.setDebit( paymentBean.getAmount() );
                        bean.setDescription( paymentBean.getTerm() + " - " + paymentBean.getRemarks() );
                        bean.setType( CustomerTransactionBean.TYPE_PAYMENT );
                        
                        transactionList.add(bean);
                    }
                                          
                    Collections.sort(transactionList);
                    
                    customerTab2SalesTableModel.removeAll();                    
                    customerTab2SalesTableModel.addBean(firstLine);
                    
                    int index = 0;
                    long lastDate = 0L;
                    double accumulativeTotal = summaryBean.getBalance();
                    for (CustomerTransactionBean bean : transactionList)
                    {   
                        // Skip line with ZERO credit AND debit
                        //if (index > 0 && bean.getCredit()==0 && bean.getDebit()==0)
                        //    continue;
                        
                        if (index > 1 && isSameDay(lastDate, bean.getDate())) 
                            continue;
                        
                        accumulativeTotal = accumulativeTotal - bean.getCredit() + bean.getDebit();
                        bean.setTotal( accumulativeTotal );
                        
                        customerTab2SalesTableModel.addBean(bean);
                        
                        System.err.println("Date: " + bean.getDate() + "(lastDate="+lastDate+")");
                        
                        index++;
                        lastDate = bean.getDate();
                    }
                    
                    return null;
                }

                @Override
                protected void done() 
                {
                    dialog.dispose();//close the modal dialog
                }
            };
            swingWorker.execute(); // this will start the processing on a separate thread
            SwingUtil.centerDialogOnScreen( dialog );            
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), e.getMessage());
            
            JOptionPane.showMessageDialog(this, "系统错误: " + e.getMessage(), "系统错误", JOptionPane.ERROR_MESSAGE);
        }
        */
    }//GEN-LAST:event_jButtonCustomerTab3ShowSummary1ActionPerformed

    private boolean isSameDay(long day1, long day2) {
        Calendar day1Obj = Calendar.getInstance();
        Calendar day2Obj = Calendar.getInstance();
        
        day1Obj.setTimeInMillis(day1);
        day2Obj.setTimeInMillis(day2);
        
        if (day1Obj.get(Calendar.YEAR)==day2Obj.get(Calendar.YEAR) &&
            day1Obj.get(Calendar.MONTH)==day2Obj.get(Calendar.MONTH) &&
            day1Obj.get(Calendar.DATE)==day2Obj.get(Calendar.DATE))
            return true;
        
        return false;
    }
    
    private void jButtonCustomerTab2MonthlyReport2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomerTab2MonthlyReport2ActionPerformed
        try 
        {
            int customerListSelectedIndex = this.jListCustomerTab2Customer.getSelectedIndex();
            CustomerBean customerBean;
            if (customerListSelectedIndex==-1)
            {
                JOptionPane.showMessageDialog(this, "请选择正确客户。", "客户错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else
            {
                customerBean = (CustomerBean) this.listModelCustomerTab2Customer.getElementAt( this.jListCustomerTab2Customer.getSelectedIndex() );
            }
                        
            long startDate = this.getSelectedDateInMillis(this.jTextFieldCustomerTab2DateFrom);
            long endDate = this.getSelectedDateInMillis(this.jTextFieldCustomerTab2DateTo);
            if (startDate==0 || endDate==0)
            {
                JOptionPane.showMessageDialog(this, "请选择正确日期期限。", "日期格式错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int flag = JOptionPane.showConfirmDialog(this, "您确定要打印月结表？","确认打印",JOptionPane.YES_NO_OPTION);
            if (flag == JOptionPane.NO_OPTION || flag == JOptionPane.CANCEL_OPTION || flag == JOptionPane.CLOSED_OPTION)
                return;
                        
            String filePath = this.saveToPath + Calendar.getInstance().getTimeInMillis() + ".pdf";
            ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "正准备 PDF 文档...");            
            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception 
                {                
                    dialog.setStatus("读取客户数据中...");
                    String customerId = 
                        ControlPanel.this.listModelCustomerTab2Customer.get( 
                                ControlPanel.this.jListCustomerTab2Customer.getSelectedIndex() ).getId();
                    CustomerBean customerObj = CustomerDelegate.getInstance().getCustomerByCustomerId(customerId);
                
                    if (customerObj.isChanged()) {
                        dialog.setStatus("计算数据中...");
                        // Recalculate summary for every month
                        CustomerDelegate.getInstance().updateSummaryByMonth(customerObj.getId());
                    }
            
                    // Get year/month from UI
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis( startDate );
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int lastMonth = month == 1 ? 12 : month - 1;
                    int lastYear = month == 1 ? year - 1 : year;
                    
                    dialog.setStatus("准备数据报告中...");
                    CustomerSummaryBean summaryBean = 
                            CustomerDelegate.getInstance().getSummaryByCustomerAndDate(customerObj.getId(), year, month);
                    CustomerSummaryBean lastMonthSummaryBean = 
                            CustomerDelegate.getInstance().getSummaryByCustomerAndDate(customerObj.getId(), lastYear, lastMonth);
                    
                    List<CustomerReportLineBean> reportLineList = new ArrayList<>();
                    reportLineList.add( new CustomerReportLineBean(summaryBean.getFirstDay(), lastMonthSummaryBean) );
            
                    List<SalesBean> salesList = SalesDelegate.getInstance().getSalesByCustomerAndDateRange(customerBean.getId(), startDate, endDate, true);
                    
                    for (SalesBean salesBean : salesList) {
                        if (salesBean.getLineList().size()==0)
                            continue;
                        
                        reportLineList.add( new CustomerReportLineBean(salesBean.getDateTime(), salesBean) );
                    }
            
                    List<CustomerPaymentBean> paymentList = CustomerDelegate.getInstance().getPaymentsByCustomerAndDateRange(customerBean.getId(), startDate, endDate);
                    
                    for (CustomerPaymentBean paymentBean : paymentList) {
                        reportLineList.add( new CustomerReportLineBean(paymentBean.getDate(), paymentBean) );
                    }
            
                    // Sort report line
                    Collections.sort(reportLineList);
                    // Prepare to print
                    dialog.setStatus("准备打印中...");
                    ControlPDFBoxPrinter.getInstance().printReportCustomerMonthlyReport(filePath, startDate, endDate, customerBean, reportLineList);
                    
                    return null;
                }

                @Override
                protected void done() 
                {
                    dialog.dispose();//close the modal dialog
                }
            };
            swingWorker.execute(); // this will start the processing on a separate thread
            SwingUtil.centerDialogOnScreen( dialog );
            
            // Open created PDF on fly
            if (RootPDFBoxPrinter.OPEN_PDF_ON_FLY == true)
                ControlPDFBoxPrinter.getInstance().openPDFonFly(filePath);
                
            Desktop desktop = Desktop.getDesktop();
            desktop.print(new File(filePath));
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), e.getMessage());
            
            JOptionPane.showMessageDialog(this, "系统错误: " + e.getMessage(), "系统错误", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonCustomerTab2MonthlyReport2ActionPerformed

    private void jButtonSupplierTab7SavingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab7SavingActionPerformed
        this.jTabbedPaneSupplierTab7.setSelectedIndex(5);
    }//GEN-LAST:event_jButtonSupplierTab7SavingActionPerformed

    private void jButtonSupplierTab7PrintSavingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab7PrintSavingActionPerformed
        try 
        {            
            SupplierBean supplier = (SupplierBean) this.jListSupplierTab7Supplier.getSelectedValue();
            if (supplier==null)
            {
                JOptionPane.showMessageDialog(this, "请选择正确船户。", "船户错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int flag = JOptionPane.showConfirmDialog(this, "您确定要打印单船资料？","确认打印",JOptionPane.YES_NO_OPTION);
            if (flag == JOptionPane.NO_OPTION || flag == JOptionPane.CANCEL_OPTION || flag == JOptionPane.CLOSED_OPTION)
                return;
                        
            long startDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab7DateFrom);
            long endDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab7DateTo);
            if (startDate==0 || endDate==0)
            {
                JOptionPane.showMessageDialog(this, "请选择日期期限。", "日期格式错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String filePath = this.saveToPath+Calendar.getInstance().getTimeInMillis()+".pdf";
            ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "正准备 PDF 文档...");
            
            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception 
                {
                    SupplierBean supplierObj = SupplierDelegate.getInstance().getSupplierById(supplier.getId());
                    if (supplierObj.isChanged()) {
                        dialog.setStatus("计算来货数据中...");
                        SupplierDelegate.getInstance().updateSupplierSummary(supplierObj.getId());
                        if (supplierObj.isSavingAccount()) {
                            dialog.setStatus("计算储蓄数据中...");
                            SupplierDelegate.getInstance().updateSupplierSummarySaving(supplierObj.getId());
                        }
                    }
                    dialog.setStatus("计算报表中...");
                    
                    //List<InTransactionBean> inTransactionList = InTransactionDelegate.getInstance().getTransactionListByDateAndSupplier(startDate, endDate, supplierBean);
                    List<SupplierCashBean> cashList = SupplierDelegate.getInstance().getCashListByDateAndSupplier(startDate, endDate, supplierObj);
                    List<SupplierChequeBean> chequeList = SupplierDelegate.getInstance().getChequeListByDateAndSupplier(startDate, endDate, supplierObj);
                    List<SupplierFuelBean> fuelList = SupplierDelegate.getInstance().getFuelListByDateAndSupplier(startDate, endDate, supplierObj);
                    List<SupplierMiscBean> miscList = SupplierDelegate.getInstance().getMiscListByDateAndSupplier(startDate, endDate, supplierObj);
                    List<InTransactionBean> savingList = InTransactionDelegate.getInstance().getSavingListByDateAndSupplier(startDate, endDate, supplierObj);
                    List<SupplierWithdrawalBean> withdrawalList = SupplierDelegate.getInstance().getWithdrawalListByDateAndSupplier(startDate, endDate, supplierObj);

                    ControlPDFBoxPrinter.getInstance().printReportSavingSupplier(filePath, startDate, endDate, supplierObj, cashList, chequeList, fuelList, miscList, savingList, withdrawalList);
                    
                    return null;
                }

                @Override
                protected void done() {
                    dialog.dispose();//close the modal dialog
                }
            };

            swingWorker.execute(); // this will start the processing on a separate thread
            SwingUtil.centerDialogOnScreen( dialog );

            // Open created PDF on fly
            if (RootPDFBoxPrinter.OPEN_PDF_ON_FLY == true)
                ControlPDFBoxPrinter.getInstance().openPDFonFly(filePath);
                
            Desktop desktop = Desktop.getDesktop();
            desktop.print(new File(filePath));
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), "exception: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "系统错误: " + e.getMessage(), "系统错误", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonSupplierTab7PrintSavingActionPerformed

    private void jListReportTab2ItemSelectionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListReportTab2ItemSelectionMouseClicked
        if (this.jListReportTab2ItemSelection.getSelectedIndex() != -1)
        {
            int selectedIndex = this.jListReportTab2ItemSelection.getSelectedIndex();
            ItemBean selectedItem = (ItemBean) this.listModelReportTab2ItemSelection.getElementAt( selectedIndex );
            
            SwingUtil.centerDialogOnScreen( new ItemPrefixDialog(this.parent, true, this, selectedItem, selectedIndex, TAB12) );                        
        }
    }//GEN-LAST:event_jListReportTab2ItemSelectionMouseClicked

    private void jListItemTab2ItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListItemTab2ItemMouseClicked
        if (this.jListItemTab2Item.getSelectedIndex() != -1)
        {
            int selectedIndex = this.jListItemTab2Item.getSelectedIndex();
            ItemBean selectedItem = (ItemBean) this.listModelItemTab2Item.getElementAt( selectedIndex );
            
            SwingUtil.centerDialogOnScreen( new ItemPrefixDialog(this.parent, true, this, selectedItem, selectedIndex, TAB2) );                        
        }
    }//GEN-LAST:event_jListItemTab2ItemMouseClicked

    private void jButtonSupplierTab7WithdrawalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab7WithdrawalActionPerformed
        this.jTabbedPaneSupplierTab7.setSelectedIndex(6);
    }//GEN-LAST:event_jButtonSupplierTab7WithdrawalActionPerformed

    private void jButtonSupplierTab6DateChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab6DateChooserActionPerformed
        long selectedTimeInMillis = this.showDatePicker(null);
        
        showDateRangeHalfMonth(selectedTimeInMillis, this.jTextFieldSupplierTab6DateFrom, this.jTextFieldSupplierTab6DateTo);     
        
        this.fillSupplierTab6Table();
    }//GEN-LAST:event_jButtonSupplierTab6DateChooserActionPerformed

    private void jTextFieldSupplierTab6DateFromMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldSupplierTab6DateFromMouseClicked
        this.jButtonSupplierTab6DateChooser.doClick();
    }//GEN-LAST:event_jTextFieldSupplierTab6DateFromMouseClicked

    private void jTextFieldSupplierTab6DateTojTextFieldSupplierTab2DateFromMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldSupplierTab6DateTojTextFieldSupplierTab2DateFromMouseClicked
        this.jButtonSupplierTab6DateChooser.doClick();
    }//GEN-LAST:event_jTextFieldSupplierTab6DateTojTextFieldSupplierTab2DateFromMouseClicked

    private void jListSupplierTab6SupplierValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListSupplierTab6SupplierValueChanged
        if (this.jListSupplierTab6Supplier.getSelectedIndex()==-1)
            return;
        
        this.jButtonSupplierTab6Add.setEnabled( true );
        
        long startDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab6DateFrom);
        long endDate = this.getSelectedDateInMillis(this.jTextFieldSupplierTab6DateTo);
        
        if (startDate==0 || endDate==0)
            return;
        
        SupplierBean supplier = (SupplierBean) this.jListSupplierTab6Supplier.getSelectedValue();
        List<SupplierWithdrawalBean> list = 
            SupplierDelegate.getInstance().getWithdrawalListByDateAndSupplier(
                startDate, endDate, supplier);
        
        double totalAmount = 0.0d;
        this.supplierTab6WithdrawalTableModel.removeAll();
        for (SupplierWithdrawalBean withdrawal : list)
        {
            this.supplierTab6WithdrawalTableModel.addBean( withdrawal );
            totalAmount = totalAmount + withdrawal.getCashAmount();
        }
        
        this.jLabelSupplierTab6Name.setText( supplier.getShipNumber() + ": " + supplier.getName() );
        this.jLabelSupplierTab6TotalAmount.setText( currencyFormatter.format( totalAmount ) );
    }//GEN-LAST:event_jListSupplierTab6SupplierValueChanged

    private void jButtonSupplierTab6AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab6AddActionPerformed
        if (this.jListSupplierTab6Supplier.getSelectedIndex() != -1)
            SwingUtil.centerDialogOnScreen( new SupplierWithdrawalEditorDialog(this.parent, true, this, (SupplierBean)this.jListSupplierTab6Supplier.getSelectedValue(), null) );
    }//GEN-LAST:event_jButtonSupplierTab6AddActionPerformed

    private void jButtonSupplierTab6EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab6EditActionPerformed
        if (this.jTableSupplierTab6WithdrawalTransaction.getSelectedRow()==-1)
            return;
        
        if (this.jListSupplierTab6Supplier.getSelectedIndex() == -1)
            return;
        
        String selectedId = this.jTableSupplierTab6WithdrawalTransaction.getValueAt(this.jTableSupplierTab6WithdrawalTransaction.getSelectedRow(), 0).toString();
        SupplierWithdrawalBean withdrawal = this.supplierTab6WithdrawalTableModel.getBeanById(selectedId);
        
        SwingUtil.centerDialogOnScreen( new SupplierWithdrawalEditorDialog(this.parent, true, this, null, withdrawal) );
    }//GEN-LAST:event_jButtonSupplierTab6EditActionPerformed

    private void jButtonSupplierTab6RemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupplierTab6RemoveActionPerformed
        if (this.jTableSupplierTab6WithdrawalTransaction.getSelectedRow()==-1)
            return;
        
        if (this.jListSupplierTab6Supplier.getSelectedIndex() == -1)
            return;
        
        int flag = JOptionPane.showConfirmDialog(this, "您确定要删除此项资料？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (flag == JOptionPane.YES_OPTION)
        {
            String selectedId = this.jTableSupplierTab6WithdrawalTransaction.getValueAt(this.jTableSupplierTab6WithdrawalTransaction.getSelectedRow(), 0).toString();
            SupplierWithdrawalBean withdrawal = this.supplierTab6WithdrawalTableModel.getBeanById(selectedId);
        
            SupplierDelegate.getInstance().deleteWithdrawal( withdrawal );
            
            if (this.jListSupplierTab6Supplier.getSelectedIndex() != -1)
                this.jListSupplierTab6SupplierValueChanged(null);
        }
    }//GEN-LAST:event_jButtonSupplierTab6RemoveActionPerformed

    private void jTableSupplierTab6WithdrawalTransactionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSupplierTab6WithdrawalTransactionMouseClicked
        if (evt.getClickCount() > 1)
            this.jButtonSupplierTab6Edit.doClick();
    }//GEN-LAST:event_jTableSupplierTab6WithdrawalTransactionMouseClicked

    private void jButtonCustomerTab3ShowSummary2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomerTab3ShowSummary2ActionPerformed
        try 
        {          
            long startDate = this.getSelectedDateInMillis(this.jTextFieldCustomerTab2DateFrom);
            long endDate = this.getSelectedDateInMillis(this.jTextFieldCustomerTab2DateTo);
            if (startDate==0 || endDate==0)
            {
                JOptionPane.showMessageDialog(this, "请选择正确日期期限。", "日期格式错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (this.jListCustomerTab2Customer.getSelectedIndex()==-1)
            {
                //JOptionPane.showMessageDialog(this, "请选择客户。", "选项错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "读取资料中...");            
            SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
            {
                @Override
                protected Void doInBackground() throws Exception 
                {                
                    dialog.setStatus("读取客户数据中...");
                    String customerId = 
                        ControlPanel.this.listModelCustomerTab2Customer.get( 
                                ControlPanel.this.jListCustomerTab2Customer.getSelectedIndex() ).getId();
                    CustomerBean customerObj = CustomerDelegate.getInstance().getCustomerByCustomerId(customerId);
                
                    dialog.setStatus("计算出货数据中...");
                    // Recalculate summary for every month
                    CustomerDelegate.getInstance().updateSummaryByMonth(customerObj.getId());
                    
                    // Get year/month from UI
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis( startDate );
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    
                    dialog.setStatus("准备数据报告中...");
                    int lastMonth = month == 1 ? 12 : month - 1;
                    int lastYear = month == 1 ? year - 1 : year;
                    
                    CustomerSummaryBean summaryBean = 
                            CustomerDelegate.getInstance().getSummaryByCustomerAndDate(customerObj.getId(), lastYear, lastMonth);
                    
                    // Update Sales status
                    SalesDelegate.getInstance().updateSalesStatus(startDate, endDate);
                    
                    List<CustomerTransactionBean> transactionList = new ArrayList<CustomerTransactionBean>();
                    
                    Calendar firstLineDate = Calendar.getInstance();
                    firstLineDate.setTimeInMillis(startDate);
                    firstLineDate.set(Calendar.HOUR, 0);
                    firstLineDate.set(Calendar.MINUTE, 0);
                    firstLineDate.set(Calendar.SECOND, 0);
                    
                    CustomerTransactionBean firstLine = new CustomerTransactionBean();
                    firstLine.setDate( firstLineDate.getTimeInMillis() );
                    firstLine.setDescription( "承接上月" );                        
                    firstLine.setTotal( summaryBean.getBalance() );
                    //transactionList.add(firstLine);
                    
                    int minuteCount = 1;
                    List<SalesBean> salesList = SalesDelegate.getInstance().getSalesByCustomerAndDateRange(customerObj.getId(), startDate, endDate, true);
                    for (SalesBean salesBean : salesList)
                    {
                        // SET earliest time for sales transaction, for sorting
                        Calendar calendarObj = Calendar.getInstance();
                        calendarObj.setTimeInMillis( salesBean.getDateTime() );
                        calendarObj.set(Calendar.HOUR_OF_DAY, 0);
                        calendarObj.set(Calendar.MINUTE, minuteCount++);
                                
                        CustomerTransactionBean bean = new CustomerTransactionBean();
                        bean.setDate( calendarObj.getTimeInMillis() );
                        bean.setCredit( salesBean.getTotalPrice() );
                        bean.setDescription( salesBean.getInvoiceNo() );
                        bean.setType( CustomerTransactionBean.TYPE_SALES );
                        bean.setCheckResult( checkSalesBean(salesBean) );
                        
                        transactionList.add(bean);
                    }
                    
                    List<CustomerPaymentBean> paymentList = CustomerDelegate.getInstance().getPaymentsByCustomerAndDateRange(customerObj.getId(), startDate, endDate);
                    for (CustomerPaymentBean paymentBean : paymentList)
                    {
                        // SET earliest time for sales transaction, for sorting
                        Calendar calendarObj = Calendar.getInstance();
                        calendarObj.setTimeInMillis( paymentBean.getDate() );
                        calendarObj.set(Calendar.HOUR_OF_DAY, 0);
                        calendarObj.set(Calendar.MINUTE, minuteCount++);
                        
                        CustomerTransactionBean bean = new CustomerTransactionBean();
                        bean.setDate( calendarObj.getTimeInMillis() );
                        bean.setDebit( paymentBean.getAmount() );
                        bean.setDescription( paymentBean.getTerm() + " - " + paymentBean.getRemarks() );
                        bean.setType( CustomerTransactionBean.TYPE_PAYMENT );
                        
                        transactionList.add(bean);
                    }
                                          
                    Collections.sort(transactionList);
                    
                    customerTab2SalesTableModel.removeAll();                    
                    customerTab2SalesTableModel.addBean(firstLine);
                    
                    double accumulativeTotal = summaryBean.getBalance();
                    for (CustomerTransactionBean bean : transactionList)
                    {   
                        accumulativeTotal = accumulativeTotal - bean.getCredit() + bean.getDebit();
                        bean.setTotal( accumulativeTotal );
                                                
                        customerTab2SalesTableModel.addBean(bean);
                    }
                    
                    return null;
                }

                @Override
                protected void done() 
                {
                    dialog.dispose();//close the modal dialog
                }
            };
            swingWorker.execute(); // this will start the processing on a separate thread
            SwingUtil.centerDialogOnScreen( dialog );            
        }
        catch (Exception e)
        {
            MyLogger.logError(getClass(), e.getMessage());
            
            JOptionPane.showMessageDialog(this, "系统错误: " + e.getMessage(), "系统错误", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonCustomerTab3ShowSummary2ActionPerformed

    private String checkSalesBean(SalesBean salesObj)
    {
        if (salesObj == null)
            return "";
                
        StringBuffer buffer = new StringBuffer();
        if (!salesObj.hasInvoiceNo()) {
            buffer.append("没打印，");
        }
        
        List<SalesLineBean> salesLineList = SalesDelegate.getInstance().getSalesLineBySalesId(salesObj.getId());
        if (salesLineList==null || salesLineList.isEmpty()) {
            // continue;
            buffer.append("无出货记录，");
        }
        
        for (SalesLineBean salesLine : salesLineList)
        {
            if (salesLine.getUnitPrice() == 0.0d)
            {
                buffer.append("总额不正确，");
                break;
            }
        }
        System.err.println("sales: " + salesObj.getInvoiceNo() + ", " + buffer.toString());
        
        return buffer.toString();
    }
    
    private void jButtonPatchSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPatchSupplierActionPerformed
        
        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "补丁准备中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {       
                String pattern = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                ControlPanel.this.jTextAreaPatchLog.setText(null);

                Calendar start = Calendar.getInstance();

                List<SupplierBean> supplierList = SupplierDelegate.getInstance().getAllSuppliers();
                ControlPanel.this.jTextAreaPatchLog.append("补丁开始：　" + simpleDateFormat.format(start.getTime()) + "\n");
                ControlPanel.this.jTextAreaPatchLog.append("船户数量：　" + supplierList.size() + "\n");

                for (SupplierBean supplierObj : supplierList) {
                    dialog.setStatus("[" + supplierObj.getName() + "] 处理船户中...");
                    ControlPanel.this.jTextAreaPatchLog.append("  [" + supplierObj.getName() + "] 处理船户中..." + "\n");
                    
                    SupplierDelegate.getInstance().updateSupplierSummary(supplierObj.getId());
                    SupplierDelegate.getInstance().updateSupplierSummarySaving(supplierObj.getId());
                    
                    dialog.setStatus("[" + supplierObj.getName() + "] 处理完毕");
                    ControlPanel.this.jTextAreaPatchLog.append("  [" + supplierObj.getName() + "] 处理完毕" + "\n");
                }

                Calendar end = Calendar.getInstance();
                ControlPanel.this.jTextAreaPatchLog.append("补丁结束：　" + simpleDateFormat.format(end.getTime()) + "\n");
                ControlPanel.this.jTextAreaPatchLog.append("船户补丁执行时间：　" + ((end.getTimeInMillis()-start.getTimeInMillis())/1000) + "s" + "\n");
                
                return null;
            }

            @Override
            protected void done() 
            {
                dialog.dispose();//close the modal dialog
            }
        };
        swingWorker.execute(); // this will start the processing on a separate thread
        SwingUtil.centerDialogOnScreen( dialog );       
    }//GEN-LAST:event_jButtonPatchSupplierActionPerformed

    private void jButtonPatchCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPatchCustomerActionPerformed
        ProgressModalDialog dialog = new ProgressModalDialog(this.parent, true, "补丁准备中...");            
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() throws Exception 
            {       
                String pattern = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                ControlPanel.this.jTextAreaPatchLog.setText(null);

                Calendar start = Calendar.getInstance();

                List<CustomerBean> customerList = CustomerDelegate.getInstance().getAllCustomers();
                ControlPanel.this.jTextAreaPatchLog.append("补丁开始：　" + simpleDateFormat.format(start.getTime()) + "\n");
                ControlPanel.this.jTextAreaPatchLog.append("客户数量：　" + customerList.size() + "\n");

                for (CustomerBean customerObj : customerList) {
                    dialog.setStatus("[" + customerObj.getName() + "] 处理客户中...");
                    ControlPanel.this.jTextAreaPatchLog.append("  [" + customerObj.getName() + "] 处理客户中..." + "\n");
                    
                    CustomerDelegate.getInstance().updateSummaryByMonth(customerObj.getId());
                    
                    dialog.setStatus("[" + customerObj.getName() + "] 处理完毕");
                    ControlPanel.this.jTextAreaPatchLog.append("  [" + customerObj.getName() + "] 处理完毕" + "\n");
                }

                Calendar end = Calendar.getInstance();
                ControlPanel.this.jTextAreaPatchLog.append("补丁结束：　" + simpleDateFormat.format(end.getTime()) + "\n");
                ControlPanel.this.jTextAreaPatchLog.append("客户补丁执行时间：　" + ((end.getTimeInMillis()-start.getTimeInMillis())/1000) + "s" + "\n");
                
                return null;
            }

            @Override
            protected void done() 
            {
                dialog.dispose();//close the modal dialog
            }
        };
        swingWorker.execute(); // this will start the processing on a separate thread
        SwingUtil.centerDialogOnScreen( dialog ); 
    }//GEN-LAST:event_jButtonPatchCustomerActionPerformed
    
    public void itemPrefixDialogClosed( ItemBean item, int indexInList, int callerTabIndex )
    {
        if (item==null)
            return;
        
        if (callerTabIndex == TAB2)
        {
            this.refreshItemPriceList( item );
        }
        else
        if (callerTabIndex == TAB12)
        {
            // Return, if duplicate item name found.
            for (int i=0 ; i<this.listModelReportTab2ItemSelection.getSize() ; i++)
            {
                ItemBean itemInList = (ItemBean) this.listModelReportTab2ItemSelection.getElementAt(i);
                if (itemInList.getName().equalsIgnoreCase( item.getName() ))
                    return;
            }

            // No duplication, so remove current old item in list
            this.listModelReportTab2ItemSelection.remove( indexInList );
            // Add new item into list
            this.listModelReportTab2ItemSelection.add( indexInList, item );
        }
    }
    
    @Override
    public void supplierWithdrawalEditorDialogClosed(SupplierWithdrawalBean bean) {
        SupplierDelegate.getInstance().saveOrUpdateWithdrawal(bean);
        
        this.fillSupplierTab6Table();
        
        JOptionPane.showMessageDialog(
            this, 
            "提款交易已被储存成功。", 
            "储存成功", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCustomerTab1Add;
    private javax.swing.JButton jButtonCustomerTab1Delete;
    private javax.swing.JButton jButtonCustomerTab1Edit;
    private javax.swing.JButton jButtonCustomerTab2Add;
    private javax.swing.JButton jButtonCustomerTab2DateChooser;
    private javax.swing.JButton jButtonCustomerTab2Edit;
    private javax.swing.JButton jButtonCustomerTab2MonthlyReport2;
    private javax.swing.JButton jButtonCustomerTab2Remove;
    private javax.swing.JButton jButtonCustomerTab3ShowSummary1;
    private javax.swing.JButton jButtonCustomerTab3ShowSummary2;
    private javax.swing.JButton jButtonCustomerTab4ShowSummary;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonItemTab1Add;
    private javax.swing.JButton jButtonItemTab1Delete;
    private javax.swing.JButton jButtonItemTab1Edit;
    private javax.swing.JButton jButtonItemTab2DatePicker;
    private javax.swing.JButton jButtonItemTab2SaveChange;
    private javax.swing.JButton jButtonPatchCustomer;
    private javax.swing.JButton jButtonPatchSupplier;
    private javax.swing.JButton jButtonReportTab2DatePicker;
    private javax.swing.JButton jButtonReportTab2RemoveAllSelections;
    private javax.swing.JButton jButtonReportTab2RemoveSelection;
    private javax.swing.JButton jButtonReportTab2ShowReport;
    private javax.swing.JButton jButtonSupplierTab1Add;
    private javax.swing.JButton jButtonSupplierTab1Delete;
    private javax.swing.JButton jButtonSupplierTab1Edit;
    private javax.swing.JButton jButtonSupplierTab2Add;
    private javax.swing.JButton jButtonSupplierTab2DateChooser;
    private javax.swing.JButton jButtonSupplierTab2Edit;
    private javax.swing.JButton jButtonSupplierTab2Remove;
    private javax.swing.JButton jButtonSupplierTab3Add;
    private javax.swing.JButton jButtonSupplierTab3DateChooser;
    private javax.swing.JButton jButtonSupplierTab3Edit;
    private javax.swing.JButton jButtonSupplierTab3Remove;
    private javax.swing.JButton jButtonSupplierTab4Add;
    private javax.swing.JButton jButtonSupplierTab4DateChooser;
    private javax.swing.JButton jButtonSupplierTab4Edit;
    private javax.swing.JButton jButtonSupplierTab4Remove;
    private javax.swing.JButton jButtonSupplierTab5Add;
    private javax.swing.JButton jButtonSupplierTab5DateChooser;
    private javax.swing.JButton jButtonSupplierTab5Edit;
    private javax.swing.JButton jButtonSupplierTab5Remove;
    private javax.swing.JButton jButtonSupplierTab6Add;
    private javax.swing.JButton jButtonSupplierTab6DateChooser;
    private javax.swing.JButton jButtonSupplierTab6Edit;
    private javax.swing.JButton jButtonSupplierTab6Remove;
    private javax.swing.JButton jButtonSupplierTab7Cash;
    private javax.swing.JButton jButtonSupplierTab7Cheque;
    private javax.swing.JButton jButtonSupplierTab7DateChooser;
    private javax.swing.JButton jButtonSupplierTab7Fuel;
    private javax.swing.JButton jButtonSupplierTab7Misc;
    private javax.swing.JButton jButtonSupplierTab7PrintAll;
    private javax.swing.JButton jButtonSupplierTab7PrintSaving;
    private javax.swing.JButton jButtonSupplierTab7PrintSingle;
    private javax.swing.JButton jButtonSupplierTab7Saving;
    private javax.swing.JButton jButtonSupplierTab7Stock;
    private javax.swing.JButton jButtonSupplierTab7Withdrawal;
    private javax.swing.JButton jButtonSupplierTab8ShowSummary;
    private javax.swing.JComboBox jComboBoxCustomerTab4Month;
    private javax.swing.JComboBox jComboBoxSupplierTab8Month;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelCustomerTab2TotalAmount;
    private javax.swing.JLabel jLabelCustomerTab4RecordSize;
    private javax.swing.JLabel jLabelSupplierTab2Name;
    private javax.swing.JLabel jLabelSupplierTab2TotalAmount;
    private javax.swing.JLabel jLabelSupplierTab3Name;
    private javax.swing.JLabel jLabelSupplierTab3TotalAmount;
    private javax.swing.JLabel jLabelSupplierTab4Name;
    private javax.swing.JLabel jLabelSupplierTab4TotalAmount;
    private javax.swing.JLabel jLabelSupplierTab5Name;
    private javax.swing.JLabel jLabelSupplierTab5TotalAmount;
    private javax.swing.JLabel jLabelSupplierTab6Name;
    private javax.swing.JLabel jLabelSupplierTab6TotalAmount;
    private javax.swing.JLabel jLabelSupplierTab7Name;
    private javax.swing.JLabel jLabelSupplierTab7TotalAmount;
    private javax.swing.JList jListCustomerTab2Customer;
    private javax.swing.JList jListItemTab1Category;
    private javax.swing.JList jListItemTab2Category;
    private javax.swing.JList jListItemTab2Item;
    private javax.swing.JList jListReportTab2Category;
    private javax.swing.JList jListReportTab2Item;
    private javax.swing.JList jListReportTab2ItemSelection;
    private javax.swing.JList jListSupplierTab2Supplier;
    private javax.swing.JList jListSupplierTab3Supplier;
    private javax.swing.JList jListSupplierTab4Supplier;
    private javax.swing.JList jListSupplierTab5Supplier;
    private javax.swing.JList jListSupplierTab6Supplier;
    private javax.swing.JList jListSupplierTab7Supplier;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel100;
    private javax.swing.JPanel jPanel101;
    private javax.swing.JPanel jPanel102;
    private javax.swing.JPanel jPanel103;
    private javax.swing.JPanel jPanel104;
    private javax.swing.JPanel jPanel105;
    private javax.swing.JPanel jPanel106;
    private javax.swing.JPanel jPanel107;
    private javax.swing.JPanel jPanel108;
    private javax.swing.JPanel jPanel109;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel110;
    private javax.swing.JPanel jPanel111;
    private javax.swing.JPanel jPanel112;
    private javax.swing.JPanel jPanel113;
    private javax.swing.JPanel jPanel114;
    private javax.swing.JPanel jPanel115;
    private javax.swing.JPanel jPanel116;
    private javax.swing.JPanel jPanel117;
    private javax.swing.JPanel jPanel118;
    private javax.swing.JPanel jPanel119;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel120;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanel74;
    private javax.swing.JPanel jPanel75;
    private javax.swing.JPanel jPanel76;
    private javax.swing.JPanel jPanel77;
    private javax.swing.JPanel jPanel78;
    private javax.swing.JPanel jPanel79;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel80;
    private javax.swing.JPanel jPanel81;
    private javax.swing.JPanel jPanel82;
    private javax.swing.JPanel jPanel83;
    private javax.swing.JPanel jPanel84;
    private javax.swing.JPanel jPanel85;
    private javax.swing.JPanel jPanel86;
    private javax.swing.JPanel jPanel87;
    private javax.swing.JPanel jPanel88;
    private javax.swing.JPanel jPanel89;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel90;
    private javax.swing.JPanel jPanel91;
    private javax.swing.JPanel jPanel92;
    private javax.swing.JPanel jPanel93;
    private javax.swing.JPanel jPanel94;
    private javax.swing.JPanel jPanel95;
    private javax.swing.JPanel jPanel96;
    private javax.swing.JPanel jPanel97;
    private javax.swing.JPanel jPanel98;
    private javax.swing.JPanel jPanel99;
    private javax.swing.JPanel jPanelCustomerTab1;
    private javax.swing.JPanel jPanelCustomerTab2;
    private javax.swing.JPanel jPanelCustomerTab4;
    private javax.swing.JPanel jPanelItemTab1;
    private javax.swing.JPanel jPanelItemTab2;
    private javax.swing.JPanel jPanelReportTab2;
    private javax.swing.JPanel jPanelSupplierTab1;
    private javax.swing.JPanel jPanelSupplierTab2;
    private javax.swing.JPanel jPanelSupplierTab3;
    private javax.swing.JPanel jPanelSupplierTab4;
    private javax.swing.JPanel jPanelSupplierTab5;
    private javax.swing.JPanel jPanelSupplierTab6;
    private javax.swing.JPanel jPanelSupplierTab7;
    private javax.swing.JPanel jPanelSupplierTab7Cash;
    private javax.swing.JPanel jPanelSupplierTab7Cheque;
    private javax.swing.JPanel jPanelSupplierTab7Fuel;
    private javax.swing.JPanel jPanelSupplierTab7Misc;
    private javax.swing.JPanel jPanelSupplierTab7Saving;
    private javax.swing.JPanel jPanelSupplierTab7Stock;
    private javax.swing.JPanel jPanelSupplierTab7Withdrawal;
    private javax.swing.JPanel jPanelSupplierTab8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane29;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JScrollPane jScrollPane31;
    private javax.swing.JScrollPane jScrollPane32;
    private javax.swing.JScrollPane jScrollPane33;
    private javax.swing.JScrollPane jScrollPane34;
    private javax.swing.JScrollPane jScrollPane35;
    private javax.swing.JScrollPane jScrollPane36;
    private javax.swing.JScrollPane jScrollPane37;
    private javax.swing.JScrollPane jScrollPane38;
    private javax.swing.JScrollPane jScrollPane39;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTabbedPane jTabbedPaneSupplierTab7;
    private javax.swing.JTable jTableCustomerTab1Customer;
    private javax.swing.JTable jTableCustomerTab2Payment;
    private javax.swing.JTable jTableCustomerTab2Report;
    private javax.swing.JTable jTableCustomerTab4Result;
    private javax.swing.JTable jTableItemTab1Item;
    private javax.swing.JTable jTableItemTab2SupplierItem;
    private javax.swing.JTable jTableSupplierTab1Supplier;
    private javax.swing.JTable jTableSupplierTab2ChequeTransaction;
    private javax.swing.JTable jTableSupplierTab3FuelTransaction;
    private javax.swing.JTable jTableSupplierTab4MiscTransaction;
    private javax.swing.JTable jTableSupplierTab5CashTransaction;
    private javax.swing.JTable jTableSupplierTab6WithdrawalTransaction;
    private javax.swing.JTable jTableSupplierTab7Cash;
    private javax.swing.JTable jTableSupplierTab7Cheque;
    private javax.swing.JTable jTableSupplierTab7Fuel;
    private javax.swing.JTable jTableSupplierTab7Misc;
    private javax.swing.JTable jTableSupplierTab7Saving;
    private javax.swing.JTable jTableSupplierTab7Stock;
    private javax.swing.JTable jTableSupplierTab7Withdrawal;
    private javax.swing.JTable jTableSupplierTab8Result;
    private javax.swing.JTextArea jTextAreaPatchLog;
    private javax.swing.JTextArea jTextAreaReportTab2;
    private javax.swing.JTextField jTextFieldCustomerTab1Filter;
    private javax.swing.JTextField jTextFieldCustomerTab2DateFrom;
    private javax.swing.JTextField jTextFieldCustomerTab2DateTo;
    private javax.swing.JTextField jTextFieldCustomerTab4Year;
    private javax.swing.JTextField jTextFieldItemTab2Date;
    private javax.swing.JTextField jTextFieldItemTab2ItemName;
    private javax.swing.JTextField jTextFieldItemTab2NewPrice;
    private javax.swing.JTextField jTextFieldItemTab2OldPrice;
    private javax.swing.JTextField jTextFieldReportTab2Date;
    private javax.swing.JTextField jTextFieldSupplierTab1Filter;
    private javax.swing.JTextField jTextFieldSupplierTab2DateFrom;
    private javax.swing.JTextField jTextFieldSupplierTab2DateTo;
    private javax.swing.JTextField jTextFieldSupplierTab3DateFrom;
    private javax.swing.JTextField jTextFieldSupplierTab3DateTo;
    private javax.swing.JTextField jTextFieldSupplierTab4DateFrom;
    private javax.swing.JTextField jTextFieldSupplierTab4DateTo;
    private javax.swing.JTextField jTextFieldSupplierTab5DateFrom;
    private javax.swing.JTextField jTextFieldSupplierTab5DateTo;
    private javax.swing.JTextField jTextFieldSupplierTab6DateFrom;
    private javax.swing.JTextField jTextFieldSupplierTab6DateTo;
    private javax.swing.JTextField jTextFieldSupplierTab7Balance;
    private javax.swing.JTextField jTextFieldSupplierTab7Cash;
    private javax.swing.JTextField jTextFieldSupplierTab7Cheque;
    private javax.swing.JTextField jTextFieldSupplierTab7DateFrom;
    private javax.swing.JTextField jTextFieldSupplierTab7DateTo;
    private javax.swing.JTextField jTextFieldSupplierTab7Fuel;
    private javax.swing.JTextField jTextFieldSupplierTab7LastOwe;
    private javax.swing.JTextField jTextFieldSupplierTab7LastSaving;
    private javax.swing.JTextField jTextFieldSupplierTab7Misc;
    private javax.swing.JTextField jTextFieldSupplierTab7Saving;
    private javax.swing.JTextField jTextFieldSupplierTab7Stock;
    private javax.swing.JTextField jTextFieldSupplierTab7TotalSaving;
    private javax.swing.JTextField jTextFieldSupplierTab7Withdrawal;
    private javax.swing.JTextField jTextFieldSupplierTab8Year;
    // End of variables declaration//GEN-END:variables

}

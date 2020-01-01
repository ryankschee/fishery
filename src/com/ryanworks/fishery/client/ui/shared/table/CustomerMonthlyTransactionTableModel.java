package com.ryanworks.fishery.client.ui.shared.table;

import com.ryanworks.fishery.shared.bean.CustomerTransactionBean;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public final class CustomerMonthlyTransactionTableModel
    extends AbstractTableModel {

    private final String[] columnNames = {"id", "摘要", "去项", "来项", "结存 / 欠", "检查"};
    private HashMap<String, CustomerTransactionBean> data;
    private List<CustomerTransactionBean> list;
    private DecimalFormat currencyFormatter = new DecimalFormat("0.00");

    public CustomerMonthlyTransactionTableModel(HashMap<String, CustomerTransactionBean> aMap) {
        setData( aMap );
    }

    public void addBean(CustomerTransactionBean bean) {
        if (bean == null)
            return;

        data.put( bean.getId(), bean );
        this.fireTableDataChanged();
    }

    public void updateBean(CustomerTransactionBean bean) {
        
        if (bean == null)
            return;

        // Replace the old address object
        this.data.put(bean.getId(), bean); 
        this.fireTableDataChanged();
    }

    public void deleteBean(CustomerTransactionBean bean) {

        if (bean == null)
            return;

        // Replace the old address object
        this.data.remove( bean.getId() );
        this.fireTableDataChanged();
    }
        
    public List<CustomerTransactionBean> getList() {
        return new ArrayList( data.values() );
    }
    
    public void removeAll() {
        this.setData( new HashMap() );
        this.fireTableDataChanged();
    }
    
    
    public void setData(HashMap<String, CustomerTransactionBean> data) {
        this.data = data;

        Iterator<CustomerTransactionBean> i = this.data.values().iterator();

        list = new ArrayList();
        while( i.hasNext() ) {
            list.add( i.next() );
        }
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        if (data==null)
            return 0;
        
        return data.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public CustomerTransactionBean getBeanById(String id) 
    {
        return this.data.get(id);
    }
    
    @Override
    public Object getValueAt(int row, int col) {

        if (data.isEmpty())
            return null;
        
        List<CustomerTransactionBean> list = getList();
        Collections.sort(list);
        CustomerTransactionBean bean = list.get( row );
        
        if (col==0)
            return MyDatePicker.SIMPLE_DATE_FORMAT.format( bean.getDate() );
        else
        if (col==1)
            return bean.getDescription();
        else
        if (col==2)
            return currencyFormatter.format( bean.getCredit() );
        else
        if (col==3)
            return currencyFormatter.format( bean.getDebit() );
        else
        if (col==4)
            return currencyFormatter.format( bean.getTotal() );
        else
        if (col==5)
            return (bean.getCheckResult()==null || "".equals(bean.getCheckResult())) ? "-" : bean.getCheckResult();
        else
            return null;
    }

    @Override
    public Class getColumnClass(int c) {

        if (data == null || data.isEmpty())
            return String.class;

        if (getValueAt(0,c)==null)
            return String.class;
        
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        return false;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        // do nothing
    }
}

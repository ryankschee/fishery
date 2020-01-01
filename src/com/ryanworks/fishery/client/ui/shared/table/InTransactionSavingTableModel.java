package com.ryanworks.fishery.client.ui.shared.table;

import com.ryanworks.fishery.shared.bean.InTransactionBean;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class InTransactionSavingTableModel
    extends AbstractTableModel {

    private String[] columnNames = {"id", "日期", "来货次数", "储蓄总额"};
    private HashMap<String, InTransactionBean> data;
    private List<InTransactionBean> list;
    private DecimalFormat currencyFormatter = new DecimalFormat("0.00");

    public InTransactionSavingTableModel(HashMap<String, InTransactionBean> aMap) 
    {
        setData( aMap );
    }

    public void addBean(InTransactionBean bean) 
    {
        if (bean == null)
            return;

        data.put( bean.getId(), bean );
        this.fireTableDataChanged();
    }

    public void updateBean(InTransactionBean bean) 
    {
        
        if (bean == null)
            return;

        // Replace the old address object
        this.data.put(bean.getId(), bean);        
        this.fireTableDataChanged();
    }

    public void deleteBean(InTransactionBean bean) 
    {

        if (bean == null)
            return;

        // Replace the old address object
        this.data.remove( bean.getId() );
        this.fireTableDataChanged();
    }
        
    public List<InTransactionBean> getList() 
    {
        return new ArrayList( data.values() );
    }
    
    public void removeAll() 
    {
        this.setData( new HashMap() );
        this.fireTableDataChanged();
    }
    
    
    public void setData(HashMap<String, InTransactionBean> data) 
    {
        this.data = data;

        Iterator<InTransactionBean> i = this.data.values().iterator();

        list = new ArrayList();
        while( i.hasNext() ) 
        {
            list.add( i.next() );
        }
    }

    public int getColumnCount() 
    {
        return columnNames.length;
    }

    public int getRowCount() 
    {
        if (data==null)
            return 0;
        
        return data.size();
    }

    @Override
    public String getColumnName(int col) 
    {
        return columnNames[col];
    }

    public InTransactionBean getInTransactionById(String transactionId) 
    {
        return this.data.get(transactionId);
    }
    
    public Object getValueAt(int row, int col) 
    {

        if (data.isEmpty())
            return null;

        InTransactionBean bean = getList().get( row );
        
        if (col==0)
            return bean.getId();
        else
        if (col==1)
            return MyDatePicker.SIMPLE_DATE_FORMAT.format(bean.getDateTime());
        else
        if (col==2)
            return String.valueOf(bean.getSupplierTrip());
        else
        if (col==3)
            return currencyFormatter.format(bean.getTotalSaving());
        else
            return null;
    }

    @Override
    public Class getColumnClass(int c) 
    {
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
    public boolean isCellEditable(int row, int col) 
    {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        return false;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    @Override
    public void setValueAt(Object value, int row, int col) 
    {
        // do nothing
    }
}

package com.ryanworks.fishery.client.ui.shared.table;

import com.ryanworks.fishery.shared.bean.SalesBean;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public final class SalesTableModel
    extends AbstractTableModel {

    private final String[] columnNames = {"id", "客户号码", "客户名字", "已来次数", "日期", "单据号码", "总额"};
    private HashMap<String, SalesBean> data;
    private List<SalesBean> list;
    
    private final DecimalFormat currencyFormatter = new DecimalFormat("0.00");

    public SalesTableModel(HashMap<String, SalesBean> aMap) 
    {
        setData( aMap );
    }

    public void addBean(SalesBean bean) 
    {
        if (bean == null)
            return;

        data.put( bean.getId(), bean );
        this.fireTableDataChanged();
    }

    public void updateBean(SalesBean bean) 
    {        
        if (bean == null)
            return;

        // Replace the old address object
        this.data.put(bean.getId(), bean);        
        this.fireTableDataChanged();
    }

    public void deleteBean(SalesBean bean) 
    {
        if (bean == null)
            return;

        // Replace the old address object
        this.data.remove( bean.getId() );
        this.fireTableDataChanged();
    }
        
    public List<SalesBean> getList() 
    {
        return new ArrayList( data.values() );
    }
    
    public void removeAll() 
    {
        this.setData( new HashMap() );
        this.fireTableDataChanged();
    }
    
    
    public void setData(HashMap<String, SalesBean> data) 
    {
        this.data = data;

        Iterator<SalesBean> i = this.data.values().iterator();

        list = new ArrayList();
        while( i.hasNext() ) 
        {
            list.add( i.next() );
        }
    }

    @Override
    public int getColumnCount() 
    {
        return columnNames.length;
    }

    @Override
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

    public SalesBean getSalesById(String salesId) 
    {
        return this.data.get(salesId);
    }
    
    @Override
    public Object getValueAt(int row, int col) 
    {

        if (data.isEmpty())
            return null;

        SalesBean bean = getList().get( row );
        
        if (col==0)
            return bean.getId();
        else
        if (col==1)
            return bean.getCustomerId();
        else
        if (col==2)
            return bean.getCustomerName();
        else
        if (col==3)
            return String.valueOf(bean.getCustomerTrip());
        else
        if (col==4)
            return MyDatePicker.SIMPLE_DATE_FORMAT.format(bean.getDateTime());
        else
        if (col==5)
            return bean.getInvoiceNo();
        else
        if (col==6)
            return currencyFormatter.format( bean.getTotalPrice() );
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

package com.ryanworks.fishery.client.ui.shared.table;

import com.ryanworks.fishery.shared.bean.SupplierSalesCheckBean;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public final class SupplierSalesCheckTableModel
    extends AbstractTableModel {

    private final String[] columnNames = {"日期","船号 / 名字", "有打印", "行动"};
    private HashMap<String, SupplierSalesCheckBean> data;
    private List<SupplierSalesCheckBean> list;

    public SupplierSalesCheckTableModel(HashMap<String, SupplierSalesCheckBean> aMap) {
        setData( aMap );
    }

    public void addBean(SupplierSalesCheckBean bean) {
        if (bean == null)
            return;

        data.put( bean.getId(), bean );
        this.fireTableDataChanged();
    }

    public void updateBean(SupplierSalesCheckBean bean) {
        
        if (bean == null)
            return;

        // Replace the old address object
        this.data.put(bean.getId(), bean);        
        this.fireTableDataChanged();
    }

    public void deleteBean(SupplierSalesCheckBean bean) {

        if (bean == null)
            return;

        // Replace the old address object
        this.data.remove( bean.getId() );
        this.fireTableDataChanged();
    }
        
    public List<SupplierSalesCheckBean> getList() {
        return new ArrayList( data.values() );
    }
    
    public void removeAll() {
        this.setData( new HashMap() );
        this.fireTableDataChanged();
    }
    
    
    public void setData(HashMap<String, SupplierSalesCheckBean> data) {
        this.data = data;

        Iterator<SupplierSalesCheckBean> i = this.data.values().iterator();

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

    public SupplierSalesCheckBean getBeanById(String id) 
    {
        return this.data.get(id);
    }
    
    @Override
    public Object getValueAt(int row, int col) {

        if (data.isEmpty())
            return null;

        SupplierSalesCheckBean bean = getList().get( row );
        
        if (col==0)
            return MyDatePicker.SIMPLE_DATE_FORMAT.format(bean.getSales().getDateTime());
        else
        if (col==1)
            return bean.getSupplier().getShipNumber() + " - " + bean.getSupplier().getName();
        else
        if (col==2)
            return bean.isPrinted() ? bean.getSales().getTransactionNo() : "没打印";
        else
        if (col==3)
        {
            return "打开进货单  [" + bean.getSales().getId() + "]";
        }
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

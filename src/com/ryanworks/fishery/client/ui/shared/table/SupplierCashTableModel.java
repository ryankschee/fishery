package com.ryanworks.fishery.client.ui.shared.table;

import com.ryanworks.fishery.shared.bean.SupplierCashBean;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public final class SupplierCashTableModel
    extends AbstractTableModel {

    private final String[] columnNames = {"id", "日期", "摘要", "银额"};
    private HashMap<String, SupplierCashBean> data;
    private List<SupplierCashBean> list;

    private final DecimalFormat twoPlacesFormatter = new DecimalFormat("0.00");
    
    public SupplierCashTableModel(HashMap<String, SupplierCashBean> aMap) {
        setData( aMap );
    }

    public void addBean(SupplierCashBean bean) {
        if (bean == null)
            return;

        data.put( bean.getId(), bean );
        this.fireTableDataChanged();
    }

    public void updateBean(SupplierCashBean bean) {
        
        if (bean == null)
            return;

        // Replace the old address object
        this.data.put(bean.getId(), bean);        
        this.fireTableDataChanged();
    }

    public void deleteBean(SupplierCashBean bean) {

        if (bean == null)
            return;

        // Replace the old address object
        this.data.remove( bean.getId() );
        this.fireTableDataChanged();
    }
        
    public List<SupplierCashBean> getList() {
        return new ArrayList( data.values() );
    }
    
    public void removeAll() {
        this.setData( new HashMap() );
        this.fireTableDataChanged();
    }
    
    
    public void setData(HashMap<String, SupplierCashBean> data) {
        this.data = data;

        Iterator<SupplierCashBean> i = this.data.values().iterator();

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

    public SupplierCashBean getBeanById(String supplierId) 
    {
        return this.data.get(supplierId);
    }
    
    @Override
    public Object getValueAt(int row, int col) {

        if (data.isEmpty())
            return null;

        SupplierCashBean bean = getList().get( row );
        
        if (col==0)
            return bean.getId();
        else        
        if (col==1)
            return MyDatePicker.SIMPLE_DATE_FORMAT.format(bean.getCashDate());
        else        
        if (col==2)
            return bean.getCashDesc();
        else        
        if (col==3)
            return twoPlacesFormatter.format(bean.getCashAmount());
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

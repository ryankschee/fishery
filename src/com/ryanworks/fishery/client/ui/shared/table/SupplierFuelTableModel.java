package com.ryanworks.fishery.client.ui.shared.table;

import com.ryanworks.fishery.shared.bean.SupplierFuelBean;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public final class SupplierFuelTableModel
    extends AbstractTableModel {

    private final String[] columnNames = {"id", "日期", "公升", "单价", "银额"};
    private HashMap<String, SupplierFuelBean> data;
    private List<SupplierFuelBean> list;

    private final DecimalFormat twoPlacesFormatter = new DecimalFormat("0.00");
    
    public SupplierFuelTableModel(HashMap<String, SupplierFuelBean> aMap) {
        setData( aMap );
    }

    public void addBean(SupplierFuelBean bean) {
        if (bean == null)
            return;

        data.put( bean.getId(), bean );
        this.fireTableDataChanged();
    }

    public void updateBean(SupplierFuelBean bean) {
        
        if (bean == null)
            return;

        // Replace the old address object
        this.data.put(bean.getId(), bean);        
        this.fireTableDataChanged();
    }

    public void deleteBean(SupplierFuelBean bean) {

        if (bean == null)
            return;

        // Replace the old address object
        this.data.remove( bean.getId() );
        this.fireTableDataChanged();
    }
        
    public List<SupplierFuelBean> getList() {
        return new ArrayList( data.values() );
    }
    
    public void removeAll() {
        this.setData( new HashMap() );
        this.fireTableDataChanged();
    }
    
    
    public void setData(HashMap<String, SupplierFuelBean> data) {
        this.data = data;

        Iterator<SupplierFuelBean> i = this.data.values().iterator();

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

    public SupplierFuelBean getBeanById(String supplierId) 
    {
        return this.data.get(supplierId);
    }
    
    @Override
    public Object getValueAt(int row, int col) {

        if (data.isEmpty())
            return null;

        SupplierFuelBean bean = getList().get( row );
        
        if (col==0)
            return bean.getId();
        else        
        if (col==1)
            return MyDatePicker.SIMPLE_DATE_FORMAT.format(bean.getFuelDate());
        else        
        if (col==2)
            return twoPlacesFormatter.format(bean.getFuelQuantity());
        else        
        if (col==3)
            return twoPlacesFormatter.format(bean.getFuelUnitPrice());
        else        
        if (col==4)
            return twoPlacesFormatter.format(bean.getFuelTotalPrice());
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

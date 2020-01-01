package com.ryanworks.fishery.client.ui.shared.table;

import com.ryanworks.fishery.shared.bean.SupplierBean;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public final class SupplierTableModel
    extends AbstractTableModel {

    private final String[] columnNames = {"id", "船号", "名字", "经常来", "有储蓄？", "回扣巴仙", "其他"};
    private HashMap<String, SupplierBean> data;
    private List<SupplierBean> list;

    private final DecimalFormat twoPlacesFormatter = new DecimalFormat("0.00");
    
    public SupplierTableModel(HashMap<String, SupplierBean> aMap) {
        setData( aMap );
    }

    public void addBean(SupplierBean bean) {
        if (bean == null)
            return;

        data.put( bean.getId(), bean );
        this.fireTableDataChanged();
    }

    public void updateBean(SupplierBean bean) {
        
        if (bean == null)
            return;

        // Replace the old address object
        this.data.put(bean.getId(), bean);        
        this.fireTableDataChanged();
    }

    public void deleteBean(SupplierBean bean) {

        if (bean == null)
            return;

        // Replace the old address object
        this.data.remove( bean.getId() );
        this.fireTableDataChanged();
    }
        
    public List<SupplierBean> getList() {
        return new ArrayList( data.values() );
    }
    
    public void removeAll() {
        this.setData( new HashMap() );
        this.fireTableDataChanged();
    }
    
    
    public void setData(HashMap<String, SupplierBean> data) {
        this.data = data;

        Iterator<SupplierBean> i = this.data.values().iterator();

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

    public SupplierBean getSupplierById(String supplierId) 
    {
        return this.data.get(supplierId);
    }
    
    @Override
    public Object getValueAt(int row, int col) {

        if (data.isEmpty())
            return null;

        SupplierBean bean = getList().get( row );
        
        if (col==0)
            return bean.getId();
        else        
        if (col==1)
            return bean.getShipNumber();
        else        
        if (col==2)
            return bean.getName();
        else        
        if (col==3)
            return new Boolean(bean.isFrequent());
        else        
        if (col==4)
            return new Boolean(bean.isSavingAccount());
        else        
        if (col==5)
            return twoPlacesFormatter.format(bean.getPercentage());
        else       
        if (col==6)
            return bean.getNotes();
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

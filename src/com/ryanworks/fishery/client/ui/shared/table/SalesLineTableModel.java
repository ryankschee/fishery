package com.ryanworks.fishery.client.ui.shared.table;

import com.ryanworks.fishery.shared.bean.SalesLineBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public final class SalesLineTableModel
    extends AbstractTableModel {

    private final String[] columnNames = {"id", "鱼类", "重量 (KG)", "船户", "价格"};
    private HashMap<String, SalesLineBean> data;
    private List<SalesLineBean> list;

    public SalesLineTableModel(HashMap<String, SalesLineBean> aMap) 
    {
        setData( aMap );
    }

    public void addBean(SalesLineBean bean) 
    {
        if (bean == null)
            return;

        data.put( bean.getId(), bean );
        this.fireTableDataChanged();
    }

    public void updateBean(SalesLineBean bean) 
    {
        
        if (bean == null)
            return;

        // Replace the old address object
        this.data.put(bean.getId(), bean);        
        this.fireTableDataChanged();
    }

    public void deleteBean(SalesLineBean bean) 
    {

        if (bean == null)
            return;

        // Replace the old address object
        this.data.remove( bean.getId() );
        this.fireTableDataChanged();
    }
        
    public List<SalesLineBean> getList() 
    {
        return new ArrayList( data.values() );
    }
    
    public void removeAll() 
    {
        this.setData( new HashMap() );
        this.fireTableDataChanged();
    }
    
    
    public void setData(HashMap<String, SalesLineBean> data) 
    {
        this.data = data;

        Iterator<SalesLineBean> i = this.data.values().iterator();

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

    @Override
    public Object getValueAt(int row, int col) 
    {

        if (data.isEmpty())
            return null;

        SalesLineBean bean = getList().get( row );
        
        if (col==0)
            return bean.getId();
        else
        if (col==1)
            return bean.getItemNewName();
        else
        if (col==2)
            return String.valueOf(bean.getWeight());
        else
        if (col==3)
            return bean.getSupplierName();
        else
        if (col==4)
            return bean.getUnitPrice();
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
        if (col==4)
            return true;
        
        return false;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    @Override
    public void setValueAt(Object value, int row, int col) 
    {                
        if (col==4)
        {
            try {       
                String id = this.getValueAt(row, 0).toString();
                SalesLineBean lineBean = this.data.get(id);
                lineBean.setUnitPrice( Double.parseDouble(value.toString()) );
                
                fireTableCellUpdated(row, col);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

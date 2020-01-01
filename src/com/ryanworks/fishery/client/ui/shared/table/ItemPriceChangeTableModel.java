package com.ryanworks.fishery.client.ui.shared.table;

import com.ryanworks.fishery.shared.bean.InTransactionLineBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ItemPriceChangeTableModel
    extends AbstractTableModel {

    private String[] columnNames = {"", "id", "有货船户", "鱼类", "价格"};
    private HashMap<String, InTransactionLineBean> data;
    private List<InTransactionLineBean> list;

    public ItemPriceChangeTableModel(HashMap<String, InTransactionLineBean> aMap) 
    {
        setData( aMap );
    }

    public void addBean(InTransactionLineBean bean) 
    {
        if (bean == null)
            return;

        data.put( bean.getId(), bean );
        this.fireTableDataChanged();
    }

    public void updateBean(InTransactionLineBean bean) 
    {
        
        if (bean == null)
            return;

        // Replace the old address object
        this.data.put(bean.getId(), bean);        
        this.fireTableDataChanged();
    }

    public void deleteBean(InTransactionLineBean bean) 
    {

        if (bean == null)
            return;

        // Replace the old address object
        this.data.remove( bean.getId() );
        this.fireTableDataChanged();
    }
        
    public InTransactionLineBean getBeanById(String id)
    {
        for (InTransactionLineBean lineBean : getList())
        {
            if (id.equalsIgnoreCase(lineBean.getId()))
                return lineBean;
        }
        
        return null;
    }
    
    public List<InTransactionLineBean> getList() 
    {
        return new ArrayList( data.values() );
    }
    
    public List<InTransactionLineBean> getCheckedList() {
        List<InTransactionLineBean> list = getList();
        List<InTransactionLineBean> checkedList = new ArrayList<>();
        
        for (InTransactionLineBean bean : list)
        {
            if (bean.isSelected())
                checkedList.add(bean);
        }
        
        return checkedList;
    }
    
    public void removeAll() 
    {
        this.setData( new HashMap() );
        this.fireTableDataChanged();
    }
    
    
    public void setData(HashMap<String, InTransactionLineBean> data) 
    {
        this.data = data;

        Iterator<InTransactionLineBean> i = this.data.values().iterator();

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
    
    @Override
    public Object getValueAt(int row, int col) 
    {

        if (data.isEmpty())
            return null;

        InTransactionLineBean bean = getList().get( row );
        
        if (col==0)
            return Boolean.valueOf(bean.isSelected());
        else        
        if (col==1)
            return bean.getId();
        else
        if (col==2)
            return bean.getSupplierId();
        else
        if (col==3)
            return bean.getItemNewName();
        else
        if (col==4)
            return String.valueOf(bean.getUnitPrice());
        else
            return null;
    }

    @Override
    public Class getColumnClass(int c) 
    {
        if (data == null || data.isEmpty())
            return String.class;
        
        if (c==0)
            return Boolean.class;
        else
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
        if (col==0)
            return true;
        
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
        if (value instanceof Boolean && col == 0) 
        {
            getList().get(row).setSelected( Boolean.parseBoolean(value.toString()) );
            fireTableCellUpdated(row, col);
        }
    }
}

package com.ryanworks.fishery.shared.swing.date;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class DatePickerTableModel
    extends AbstractTableModel {

    private String[] columnNames = { "S", "M", "T", "W", "T", "F", "S" };
    private List<List> data = new ArrayList();

    public DatePickerTableModel() {
        //setData( aMap );
    }

    public void addRow(List row) {
        this.data.add(row);
        fireTableDataChanged();
    }
    
    public void clear() {
        this.data.clear();
        fireTableDataChanged();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {

        if (row > getRowCount() - 1) 
            return null;
        
        Object rowObject = this.data.get(row);
        if ((rowObject == null) || (!(rowObject instanceof List))) 
            return null;
          
        List rowData = (List) rowObject;
      
        if (rowData.size() - 1 < col) 
            return null;
        
        return rowData.get(col);
    }

    @Override
    public Class getColumnClass(int c) {

        if (getColumnCount() - 1 < c) 
            return Object.class;
          
        Object obj = getValueAt(0, c);
        if (obj == null) 
            return Object.class;
          
        return obj.getClass();
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
        List rowSet = null;
        if (getRowCount() <= row) {
            rowSet = new ArrayList();
            this.data.add(row, rowSet);
        }
        else {
            rowSet = (List) this.data.get(row);
        }
        
        rowSet.add(col, value);
        fireTableCellUpdated(row, col);
    }
}

package rssDownloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class UpdateTableModel extends AbstractTableModel{
	List<RowData> rows = new ArrayList<RowData>();
	Map<String, RowData> lookup = new HashMap<String, RowData>();

	@Override
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	public String getColumnName(int col){
		switch(col){
		case 0:
			return "File Name";
		case 1:
			return "Status";
		case 2:
			return "Progress";
		}
		return "";
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		RowData row = rows.get(rowIndex);
		switch(columnIndex){
		case 0:
			return row.getName();
		case 1:
			return row.getStatus();
		case 2:
			return row.getProgress();
		}
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		RowData rowData = rows.get(rowIndex);
		switch (columnIndex) {
		case 1:
			if(aValue instanceof String){
				rowData.setStatus((String)aValue);
				break;
			}
		case 2:
			if (aValue instanceof Float) {
				rowData.setProgress((float) aValue);
			}
			break;
		}
	}
	
	public void addRow(String filename, String status, float progress) {
        RowData rowData = new RowData(filename, status, progress);
        lookup.put(filename, rowData);
        rows.add(rowData);
        fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
    }
	
	protected void updateProgress(String filename, int progress) {
        RowData rowData = lookup.get(filename);
        if (rowData != null) {
            int row = rows.indexOf(rowData);
            float p = (float) progress / 100f;
            setValueAt(p, row, 2);
            fireTableCellUpdated(row, 2);
        }
    }
	
	protected void updateStatus(String filename, String status){
		RowData rowData = lookup.get(filename);
        if (rowData != null) {
            int row = rows.indexOf(rowData);
            setValueAt(status, row, 1);
            fireTableCellUpdated(row, 1);
        }
	}

}

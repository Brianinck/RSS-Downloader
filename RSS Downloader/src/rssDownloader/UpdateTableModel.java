package rssDownloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

public class UpdateTableModel extends AbstractTableModel {
	// holds the strings to be displayed in the column headers of our table
	private final String[] columnNames = {"Filename", "File Size", "Status", "Progress"};

	// holds the data types for all our columns
	private final Class[] columnClasses = {String.class, String.class, String.class, JProgressBar.class};

	// holds our data
	private List<RowData> rows = new ArrayList<RowData>();
	private Map<String, RowData> lookup = new HashMap<String, RowData>();

	// adds a row
	public void addDownload(RowData d) {
		rows.add(d);
		lookup.put(d.getName(), d);

		// the table model is interested in changes of the rows
		fireTableRowsInserted(rows.size()-1, rows.size()-1);
	}

	// is called by a download object when its state changes
	public void update(Observable observable, Object o) {
		int index = rows.indexOf(o);
		if (index != -1)
			fireTableRowsUpdated(index, index);
	}

	public int getColumnCount() {
		return 4;
	}

	public int getRowCount() {
		return rows.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Class getColumnClass(int c) {
		return columnClasses[c];
	}

	public Object getValueAt(int row, int col) {
		RowData download = rows.get(row);
		switch(col){
		case 0:
			return download.getName();
		case 1:
			return download.getFileSize();
		case 2:
			return download.getStatus();
		case 3:
			return download.getProgress();
		default:
			return null;
		}
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		RowData rowData = rows.get(rowIndex);
		switch (columnIndex) {
		case 1:
			if(aValue instanceof String)
				rowData.setFileSize((String) aValue);
			break;
		case 2:
			if(aValue instanceof String)
				rowData.setStatus((String)aValue);
			break;
		case 3:
			if (aValue instanceof Float)
				rowData.setProgress((float) aValue);
			break;
		}
	}
	
	protected void updateProgress(String filename, int progress) {
        RowData rowData = lookup.get(filename);
        if (rowData != null) {
            int row = rows.indexOf(rowData);
            float p = (float) progress / 100f;
            setValueAt(p, row, 3);
            fireTableCellUpdated(row, 3);
        }
    }
	
	protected void updateStatus(String filename, String status){
		RowData rowData = lookup.get(filename);
		if (rowData != null) {
			int row = rows.indexOf(rowData);
            setValueAt(status, row, 2);
            fireTableCellUpdated(row, 2);
        }
	}
	
	protected void updateFileSize(String filename, int size){
		RowData rowData = lookup.get(filename);
		if (rowData != null) {
			int row = rows.indexOf(rowData);
            setValueAt(size+" MB", row, 1);
            fireTableCellUpdated(row, 1);
        }
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

}

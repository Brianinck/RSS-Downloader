package rssDownloader;

import java.awt.Component;

import javax.swing.BoundedRangeModel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ProgressCellRenderer extends JProgressBar implements TableCellRenderer {
	public ProgressCellRenderer() {
		super();
	}

	public ProgressCellRenderer(BoundedRangeModel newModel) {
		super(newModel);
	}

	public ProgressCellRenderer(int orient) {
		super(orient);
	}

	public ProgressCellRenderer(int min, int max) {
		super(min, max);
	}

	public ProgressCellRenderer(int orient, int min, int max) {
		super(orient, min, max);
	}
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		int progress = 0;
        if (value instanceof Float) {
            progress = Math.round(((Float) value) * 100f);
        } else if (value instanceof Integer) {
            progress = (int) value;
        }
        setValue(progress);
        return this;
	}


}

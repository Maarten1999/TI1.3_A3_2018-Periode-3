package agenda.gui;

import agenda.data.Artist;
import agenda.data.Performance;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class PerformanceModel extends AbstractTableModel {
    private ArrayList<Performance> performances;

    public PerformanceModel(ArrayList<Performance> performances) {
        this.performances = performances;
    }

    public void add(Performance performance) {
        performances.add(performance);
        fireTableRowsInserted(performances.size() - 1, performances.size() - 1);
    }

    public void remove(int index) {
        performances.remove(index);
        fireTableRowsDeleted(performances.size() - 1, performances.size() - 1);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    public int getRowCount() {
        return performances.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "Name";
        }
        return "popularity";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
//        Performance performance = performances.get(rowIndex);
//        if (columnIndex == 0) {
//            return performance.getName();
//        }
//        return performance.getPopularity();
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//        Artist artist = artists.get(rowIndex);
//        if (columnIndex == 0) {
//            artist.setName((String) aValue);
//        } else {
//            artist.setPopularity(Integer.parseInt((String) aValue));
//        }
//        artists.set(rowIndex, artist);
//        fireTableCellUpdated(rowIndex, columnIndex);
    }
}

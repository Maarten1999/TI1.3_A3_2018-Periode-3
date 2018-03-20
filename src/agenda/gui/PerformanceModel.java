package agenda.gui;

import agenda.data.Performance;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class PerformanceModel extends AbstractTableModel {

    private ArrayList<Performance> performances;

    PerformanceModel(ArrayList<Performance> performances) {
        this.performances = performances;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public int getRowCount() {
        return this.performances.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        String returnValue = "";
        switch (column) {
            case 0:
                returnValue = "Name";
                break;
            case 1:
                returnValue = "Artist";
                break;
            case 2:
                returnValue = "Stage";
                break;
            case 3:
                returnValue = "Start time";
                break;
            case 4:
                returnValue = "End time";
                break;
        }
        return returnValue;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String returnValue = "";
        switch (columnIndex) {
            case 0:
                returnValue = performances.get(rowIndex).getName();
                break;
            case 1:
                for(int i = 0; i < performances.get(rowIndex).getArtists().size(); i++){
                    returnValue += performances.get(rowIndex).getArtists().get(i).getName();
                    if(i != performances.get(rowIndex).getArtists().size() - 1){
                        returnValue += ", ";
                    }
                }
                break;
            case 2:
                returnValue = performances.get(rowIndex).getStage().getName();
                break;
            case 3:
                returnValue = performances.get(rowIndex).getStartTime().toString().substring(0, 5);
                break;
            case 4:
                returnValue = performances.get(rowIndex).getEndTime().toString().substring(0, 5);
                break;
        }
        return returnValue;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }
}

package agenda.gui;

import agenda.data.Artist;
import agenda.data.Performance;
import agenda.data.Schedule;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class ArtistModel extends AbstractTableModel {

    private Schedule schedule;
    private ArtistTab artistTab;

    public ArtistModel(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setArtistTab(ArtistTab artistTab){
        this.artistTab = artistTab;
    }

    public void add(Artist artist) {
        this.schedule.getArtists().add(artist);
        fireTableRowsInserted(this.schedule.getArtists().size() - 1,
                this.schedule.getArtists().size() - 1);
    }

    public void remove(Artist artist) {
        this.schedule.getArtists().remove(artist);
        String artistName = artist.getName();
        for (int i = 0; i < this.schedule.getPerformances().size(); i++) {
            Performance performance = this.schedule.getPerformances().get(i);
            for(int i2 = 0; i2 < performance.getArtists().size(); i2++){
                if(performance.getArtists().get(i2).getName().equals(artistName)){
                    performance.getArtists().remove(i2);
                    i2--;
                }
            }
            if(this.schedule.getPerformances().get(i).getArtists().size() < 1){
                this.schedule.getPerformances().remove(i);
                i--;
            }
        }
        this.schedule.getArtists().remove(artist);
        fireTableRowsDeleted(this.schedule.getArtists().size() - 1,
                this.schedule.getArtists().size() - 1);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    public int getRowCount() {
        return this.schedule.getArtists().size();
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
        return "Popularity";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Artist artist = this.schedule.getArtists().get(rowIndex);
        if (columnIndex == 0) {
            return artist.getName();
        }
        return artist.getPopularity();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Artist artist = this.schedule.getArtists().get(rowIndex);
        if (columnIndex == 0) {
            String artistName = (String) aValue;
            if(artistName.isEmpty()) {
                JOptionPane.showMessageDialog(artistTab,
                        "Last edited artist doesn't have a name!", "Error", JOptionPane.ERROR_MESSAGE);
            }else
                artist.setName(artistName);
        } else {
            String popularityString = (String) aValue;
            if(!popularityString.matches("[0-9]+")) {
                JOptionPane.showMessageDialog(artistTab,
                        "Last edited popularity contains non-numeric characters!", "Error", JOptionPane.ERROR_MESSAGE);
            }else if(Integer.parseInt(popularityString) < 0 || Integer.parseInt(popularityString) > 100){
                JOptionPane.showMessageDialog(artistTab,
                        "Last edited popularity should be between 0 and 100!", "Error", JOptionPane.ERROR_MESSAGE);
            }else
                artist.setPopularity(Integer.parseInt(popularityString));
        }
        this.schedule.getArtists().set(rowIndex, artist);
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}

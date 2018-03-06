package agenda.gui;

import agenda.data.Artist;
import agenda.data.Schedule;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ArtistModel extends AbstractTableModel {

    private Schedule schedule;

    ArtistModel(Schedule schedule) {
        this.schedule = schedule;
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
            outerloop:
            for (int i2 = 0; i2 < this.schedule.getPerformances().get(i).getArtists().size() - 1; i++) {
                ArrayList<Artist> artists = this.schedule.getPerformances().get(i).getArtists();
                for (Artist artist1 : artists) {
                    if (artist1.getName().equals(artistName)) {
                        this.schedule.getPerformances().remove(i);
                        i--;
                        break outerloop;
                    }
                }
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
            artist.setName((String) aValue);
        } else {
            artist.setPopularity(Integer.parseInt((String) aValue));
        }
        this.schedule.getArtists().set(rowIndex, artist);
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}

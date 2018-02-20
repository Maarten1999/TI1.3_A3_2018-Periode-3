package agenda.gui;

import agenda.data.Artist;
import agenda.data.Performance;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ArtistModel extends AbstractTableModel {

    private FestivalFrame frame;
    private ArrayList<Artist> artists;

    public ArtistModel(FestivalFrame frame) {
        this.frame = frame;
        this.artists = this.frame.getSchedule().getArtists();
    }

    public void add(Artist artist) {
        artists.add(artist);
        fireTableRowsInserted(artists.size() - 1, artists.size() - 1);
    }

    public void remove(int index) {
        String artistName = this.artists.get(index).getName();
        for (int i = 0; i < this.frame.getSchedule().getPerformances().size(); i++) {
            if (this.frame.getSchedule().getPerformances().get(i).getArtist().getName().equals(artistName)) {
                this.frame.getSchedule().getPerformances().remove(i);
                i--;
            }
        }
        this.frame.getSchedule().getArtists().remove(index);
        fireTableRowsDeleted(this.frame.getSchedule().getArtists().size() - 1,
                this.frame.getSchedule().getArtists().size() - 1);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    public int getRowCount() {
        return artists.size();
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
        Artist artist = artists.get(rowIndex);
        if (columnIndex == 0) {
            return artist.getName();
        }
        return artist.getPopularity();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Artist artist = artists.get(rowIndex);
        if (columnIndex == 0) {
            artist.setName((String) aValue);
        } else {
            artist.setPopularity(Integer.parseInt((String) aValue));
        }
        artists.set(rowIndex, artist);
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}

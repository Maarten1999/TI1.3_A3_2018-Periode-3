package agenda.gui;

import agenda.data.Artist;

import javax.swing.table.AbstractTableModel;

public class ArtistModel extends AbstractTableModel {

    private FestivalFrame frame;

    public ArtistModel(FestivalFrame frame) {
        this.frame = frame;
    }

    public void add(Artist artist) {
        this.frame.getSchedule().getArtists().add(artist);
        fireTableRowsInserted(this.frame.getSchedule().getArtists().size() - 1,
                this.frame.getSchedule().getArtists().size() - 1);
    }

    public void remove(Artist artist) {
        this.frame.getSchedule().getArtists().remove(artist);
        String artistName = artist.getName();
        for (int i = 0; i < this.frame.getSchedule().getPerformances().size(); i++) {
            if (this.frame.getSchedule().getPerformances().get(i).getArtist().getName().equals(artistName)) {
                this.frame.getSchedule().getPerformances().remove(i);
                i--;
            }
        }
        this.frame.getSchedule().getArtists().remove(artist);
        fireTableRowsDeleted(this.frame.getSchedule().getArtists().size() - 1,
                this.frame.getSchedule().getArtists().size() - 1);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    public int getRowCount() {
        return this.frame.getSchedule().getArtists().size();
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
        Artist artist = this.frame.getSchedule().getArtists().get(rowIndex);
        if (columnIndex == 0) {
            return artist.getName();
        }
        return artist.getPopularity();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Artist artist = this.frame.getSchedule().getArtists().get(rowIndex);
        if (columnIndex == 0) {
            artist.setName((String) aValue);
        } else {
            artist.setPopularity(Integer.parseInt((String) aValue));
        }
        this.frame.getSchedule().getArtists().set(rowIndex, artist);
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}

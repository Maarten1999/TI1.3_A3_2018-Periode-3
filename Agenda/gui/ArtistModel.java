package agenda.gui;

import agenda.data.Artist;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ArtistModel extends AbstractTableModel {

    private ArrayList<Artist> artists;

    public ArtistModel() {
        artists =  new ArrayList<>();
    }

    public void add(Artist artist){
        artists.add(artist);
        fireTableRowsInserted(artists.size()-1, artists.size()-1);
    }

    public void remove(int index){
        artists.remove(index);
        fireTableRowsDeleted(artists.size()-1, artists.size()-1);
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
        if(column ==0){
            return "Name";
        }
        return "popularity";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Artist artist = artists.get(rowIndex);
        if(columnIndex ==0){
            return artist.getName();
        }
        return artist.getPopularity();
    }
}

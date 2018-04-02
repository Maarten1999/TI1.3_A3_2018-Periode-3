package agenda.data;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

public class Performance implements Serializable {

    private String name;
    private ArrayList<Artist> artists;
    private Stage stage;
    private LocalTime startTime;
    private LocalTime endTime;

    public Performance(String name, ArrayList<Artist> artists, Stage stage, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.artists = artists;
        this.stage = stage;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Performance() {

    }

    public String getName() {
        return name;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    public void addArtist(Artist artist){
        artists.add(artist);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopularity(){
        int p = 0;
        for(Artist a : artists) {
            p += a.getPopularity();
        }
        return p;
    }
}

package agenda.data;

import java.time.LocalTime;

public class Performance {
    private Artist artist;
    private Stage stage;
    private LocalTime startTime;
    private LocalTime endTime;

    public Performance(Artist artist, Stage stage, LocalTime startTime, LocalTime endTime) {
        this.artist = artist;
        this.stage = stage;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
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
}

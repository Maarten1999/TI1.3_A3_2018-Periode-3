package agenda.data;

import java.time.LocalDate;
import java.util.ArrayList;

public class Schedule {
    private LocalDate date;
    private ArrayList<Artist> artists;
    private ArrayList<Stage> stages;
    private ArrayList<Performance> performances;
    private String name;

    public Schedule(int day, int month, int year, String name) {
        date = LocalDate.of(year, month, day);
        this.name = name;
        artists = new ArrayList<>();
        stages = new ArrayList<>();
        performances = new ArrayList<>();
    }

    public LocalDate getDate() {
        return date;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    public ArrayList<Stage> getStages() {
        return stages;
    }

    public ArrayList<Performance> getPerformances() {
        return performances;
    }

    public String getName() {
        return name;
    }

    public void addArtist(Artist artist) {
        artists.add(artist);
    }

    public void addStage(Stage stage) {
        stages.add(stage);
    }

    public void addPerformace(Performance performance) {
        performances.add(performance);
    }
}

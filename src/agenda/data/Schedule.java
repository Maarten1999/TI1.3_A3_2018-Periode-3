package agenda.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Schedule implements Serializable {
    private LocalDate date;
    private ArrayList<Artist> artists;
    private ArrayList<Stage> stages;
    private ArrayList<Performance> performances;
    private String name;

    public Schedule() {
        this.date = LocalDate.now();
        this.name = "New Festival";
        this.artists = new ArrayList<>();
        this.performances = new ArrayList<>();
        this.stages = new ArrayList<>();

    }

    public void load(Schedule schedule){
        artists.clear();
        stages.clear();
        performances.clear();
        performances.addAll(schedule.performances);
        stages.addAll(schedule.stages);
        artists.addAll(schedule.artists);
        this.name = schedule.name;
    }


    public void empty(){
        artists.clear();
        stages.clear();
        performances.clear();
        this.date = LocalDate.now();
        this.name = "New Festival";
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

    public void setName(String name) {
        this.name = name;
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

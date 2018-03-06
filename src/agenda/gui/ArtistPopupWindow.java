package agenda.gui;

import agenda.data.Artist;
import agenda.data.Schedule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class ArtistPopupWindow extends JDialog {
    private Schedule schedule;
    private JCheckBox[] allArtistsNames;
    private ArrayList<Artist> artistList;

    public ArtistPopupWindow(Schedule schedule, ArrayList<Artist> artistList) {
        super();
        setTitle("Select artist(s)");
        this.artistList = artistList;
        this.schedule = schedule;
        setModal(true);
        setSize(new Dimension(360, 340));
        setLocationRelativeTo(null);
        setResizable(false);
        JPanel mainPanel = initMainPanelEdit();
        setContentPane(mainPanel);
        setVisible(true);
    }

    private JPanel initMainPanelEdit() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //Show Checkboxes
        JPanel checkBoxPanel = new JPanel();
        ArrayList<Artist> allArtists = schedule.getArtists();
        allArtistsNames = new JCheckBox[allArtists.size()];

        checkBoxPanel.setLayout(new GridLayout(allArtists.size(), 1));

        for(int i = 0; i < allArtists.size(); i++){
            allArtistsNames[i] = new JCheckBox(allArtists.get(i).getName());
            if(artistList != null) {
                for (Artist a : artistList) {
                    if (a.getName().equals(allArtistsNames[i].getText())) {
                        allArtistsNames[i].setSelected(true);
                    }
                }
            }
            checkBoxPanel.add(allArtistsNames[i]);
        }

        //Scroller
        JScrollPane listScroller = new JScrollPane(checkBoxPanel);
        mainPanel.add(listScroller, BorderLayout.CENTER);

        //Add selected artists
        JPanel addPanel = new JPanel();
        JButton addButton = new JButton("Add");
        addPanel.add(addButton);
        addButton.addActionListener(e -> {
            artistList = getNewSelectedArtists();
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        mainPanel.add(addPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private ArrayList<Artist> getNewSelectedArtists(){
        ArrayList<Artist> selectedArtists = new ArrayList<>();

        for (int i = 0; i < allArtistsNames.length; i++) {
            if(allArtistsNames[i].isSelected()){
                selectedArtists.add(schedule.getArtists().get(i));
            }
        }
        return selectedArtists;
    }

    public ArrayList<Artist> getArtistList() {
        return artistList;
    }
}

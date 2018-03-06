package agenda.gui;

import agenda.data.Artist;
import agenda.data.Schedule;

import javax.swing.*;
import java.awt.*;

public class ArtistTab extends JPanel {

    private Schedule schedule;
    private JTable table;
    private ArtistModel model;
    private JScrollPane scroller;

    ArtistTab(Schedule schedule) {
        this.schedule = schedule;
        model = new ArtistModel(this.schedule);
        setLayout(new BorderLayout());
        initTable();
        initButtons();
    }

    private void initTable() {
        table = new JTable();
        table.getTableHeader().setReorderingAllowed(false);
        scroller = new JScrollPane(table);
        table.setModel(model);
        add(scroller, BorderLayout.CENTER);
        table.setAutoCreateRowSorter(true);
        table.getRowSorter().toggleSortOrder(0);
    }

    private void initButtons() {
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            model.add(new Artist("", 0));
            table.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
            scroller.validate();
            JScrollBar vertical = scroller.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            if (model.getRowCount() != 0) {
                int row = table.getSelectedRow();
                Artist artist = this.schedule.getArtists().get(table.convertRowIndexToModel(table.getSelectedRow()));
                model.remove(artist);
                if (model.getRowCount() != 0) {
                    if (row < model.getRowCount())
                        table.setRowSelectionInterval(row, row);
                    else
                        table.setRowSelectionInterval(row - 1, row - 1);
                }
            }
        });
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void refresh() {
        model.fireTableDataChanged();
    }
}

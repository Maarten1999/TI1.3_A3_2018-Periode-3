package agenda.gui;

import agenda.data.Artist;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ArtistTab extends JPanel {

    private JTable table;
    private ArtistModel model;
    private JScrollPane scroller;

    public ArtistTab() {
        setLayout(new BorderLayout());
        initTable();
        initButtons();
    }

    private void initTable() {
        table = new JTable();
        model = new ArtistModel();
        model.add(new Artist("Justin Bieber", -100));
        for (int i = 0; i <= 10; i++) {
            model.add(new Artist("Bob Marley", i));
        }
        table.getTableHeader().setReorderingAllowed(false);
        scroller = new JScrollPane(table);
        table.setModel(model);
        add(scroller, BorderLayout.CENTER);
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
                model.remove(row);
                if (model.getRowCount() != 0) {
                    if (row < model.getRowCount())
                        table.setRowSelectionInterval(row, row);
                    else
                        table.setRowSelectionInterval(row - 1, row - 1);
                }
            }
        });
        JButton saveButton = new JButton("Save");
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
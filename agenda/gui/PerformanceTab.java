package agenda.gui;

import agenda.data.Artist;
import agenda.data.Performance;
import agenda.data.Schedule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;

public class PerformanceTab extends JTable {
    private Schedule schedule;
    private JTable table;
    private PerformanceModel model;
    private JScrollPane scroller;
    private ComboBoxRenderer comboBoxRenderer;

    public PerformanceTab(Schedule schedule) {
        this.schedule = schedule;
        model = new PerformanceModel(schedule.getPerformances());
        setLayout(new BorderLayout());
        initTable();
        initButtons();
    }

    private void initTable() {
        table = new JTable();
        table.getTableHeader().setReorderingAllowed(false);
        scroller = new JScrollPane(table);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Artist");
        tableModel.addColumn("Stage");
        tableModel.addColumn("Start time");
        tableModel.addColumn("End time");
        tableModel.addRow(new Object[]{"hoi"});
        tableModel.addRow(new Object[]{"doei"});
        tableModel.addRow(new Object[]{"kill me"});
        table.setModel(tableModel);

        TableColumn ArtistColumn = table.getColumnModel().getColumn(1);
        comboBoxRenderer = new ComboBoxRenderer();
        ArtistColumn.setCellEditor(new DefaultCellEditor(comboBoxRenderer));

        table.setAutoCreateRowSorter(true);
        add(scroller, BorderLayout.CENTER);
    }

    public void refresh() {
        comboBoxRenderer.removeAllItems();
        ArrayList<Artist> artists = this.schedule.getArtists();
        for(Artist artist : artists) {
            comboBoxRenderer.addItem(artist.getName());
        }
    }

    private void initButtons() {
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            model.add(new Performance());
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
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}

class ComboBoxRenderer extends JComboBox{
}
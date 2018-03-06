package agenda.gui;

import agenda.data.Schedule;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.awt.*;

public class PerformanceTab extends JTable {
    private Schedule schedule;
    private PerformanceModel model;
    private JTable table;

    public PerformanceTab(Schedule schedule) {
        this.schedule = schedule;
        model = new PerformanceModel(this.schedule.getPerformances());
        setLayout(new BorderLayout());
        initTable();
    }

    private void initTable() {
        this.table = new JTable();
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroller = new JScrollPane(table);

        PerformanceModel model = new PerformanceModel(this.schedule.getPerformances());
        table.setModel(model);

        table.setAutoCreateRowSorter(true);
        table.getRowSorter().toggleSortOrder(0);
        add(scroller, BorderLayout.CENTER);
    }

    public void refresh() {
        this.table.tableChanged(new TableModelEvent(this.model));
        this.model.fireTableDataChanged();
        this.table.repaint();
    }
}
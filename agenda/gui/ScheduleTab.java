package agenda.gui;

import agenda.data.Artist;
import agenda.data.Performance;
import agenda.data.Schedule;
import agenda.data.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.time.LocalTime;
import java.util.ArrayList;

public class ScheduleTab extends JPanel implements MouseListener {

    private FestivalFrame frame;
    private Schedule schedule;
    private ArrayList<PerformanceBox> performanceBoxes;

    public ScheduleTab(FestivalFrame frame) {
        this.frame = frame;
        this.schedule = frame.getSchedule();
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();

        //New button
        JButton newButton = new JButton("New Performance");
        newButton.addActionListener(e -> {
            new PopupWindow(this.schedule);
        });

        addMouseListener(this);
        add(panel, BorderLayout.SOUTH);
        panel.add(newButton);

        addTestObjects();
        initBoxes();
    }

    private void addTestObjects() {
        this.schedule.addArtist(new Artist("Bennie", 100));
        this.schedule.addPerformace(new Performance("Performance Name",
                this.schedule.getArtists().get(0),
                this.schedule.getStages().get(1),
                LocalTime.of(14, 40),
                LocalTime.of(20, 20)));
    }

    private void initBoxes() {
        this.performanceBoxes = new ArrayList<>();
        ArrayList<Performance> performances = this.schedule.getPerformances();
        for (Performance performance : performances) {
            this.performanceBoxes.add(new PerformanceBox(performance));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("repaint");
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        drawStageBar(g2d);
        drawTimeLine(g2d);
        for (PerformanceBox performanceBox : this.performanceBoxes) {
            Stage boxStage = performanceBox.getPerformance().getStage();
            ArrayList<Stage> stages = this.schedule.getStages();

            int index = 0;
            for (Stage stage : stages) {
                if (stage == boxStage) {
                    performanceBox.drawBox(g2d, index);
                }
                index++;
            }
        }
    }

    public static Font font = new Font("Arial", Font.PLAIN, 12);
    private final Color BAR_COLOR = Color.LIGHT_GRAY;
    public static final int STAGE_BAR_HEIGHT = 50;
    public static final int STAGE_BAR_WIDTH = 195;

    public static final int TIMELINE_BAR_WIDTH = 100;
    public static final int TIMELINE_BAR_HEIGHT = 34;
    public static final int START_HOUR = 8;
    public static final int END_HOUR = 23;

    private void drawStageBar(Graphics2D g2d) {
        ArrayList<Stage> stages = this.schedule.getStages();

        g2d.setColor(BAR_COLOR);
        g2d.fill(new Rectangle2D.Double(0, 0, TIMELINE_BAR_WIDTH, STAGE_BAR_HEIGHT));

        int lineLength = (END_HOUR - START_HOUR + 1) * TIMELINE_BAR_HEIGHT;
        int x = TIMELINE_BAR_WIDTH;
        for (Stage stage : stages) {
            // Draw line
            g2d.setColor(new Color(200, 200, 200));
            g2d.drawLine(x + STAGE_BAR_WIDTH, STAGE_BAR_HEIGHT, x + STAGE_BAR_WIDTH, lineLength + STAGE_BAR_HEIGHT);

            // Draw rectangle
            g2d.setColor(BAR_COLOR);
            Rectangle2D rectangle2D = new Rectangle2D.Double(x, 0, STAGE_BAR_WIDTH, STAGE_BAR_HEIGHT);
            g2d.fill(rectangle2D);
            g2d.setColor(Color.BLACK);
            g2d.draw(rectangle2D);

            // Draw name
            g2d.setColor(Color.BLACK);
            String name = stage.getName();
            drawText(g2d, name, x + STAGE_BAR_WIDTH / 2, 30, true);
            x += STAGE_BAR_WIDTH;
        }
    }

    private void drawTimeLine(Graphics2D g2d) {
        int y = STAGE_BAR_HEIGHT;
        int lineLength = this.schedule.getStages().size() * STAGE_BAR_WIDTH + TIMELINE_BAR_WIDTH;
        for (int hour = START_HOUR; hour <= END_HOUR; hour++) {
            // Draw line
            g2d.setColor(new Color(200, 200, 200));
            g2d.drawLine(TIMELINE_BAR_WIDTH, y + TIMELINE_BAR_HEIGHT, lineLength, y + TIMELINE_BAR_HEIGHT);

            // Draw rectangle
            g2d.setColor(BAR_COLOR);
            Rectangle2D rectangle2D = new Rectangle2D.Double(0, y, TIMELINE_BAR_WIDTH, TIMELINE_BAR_HEIGHT);
            g2d.fill(rectangle2D);
            g2d.setColor(Color.BLACK);
            g2d.draw(rectangle2D);

            // Draw time
            g2d.setColor(Color.BLACK);
            String time = hour + ":00";
            drawText(g2d, time, TIMELINE_BAR_WIDTH / 2, y + 20, true);
            y += TIMELINE_BAR_HEIGHT;
        }
    }

    public static void drawText(Graphics2D g2d, String string, int x, int y, boolean center) {
        GlyphVector itemFontVector = font.createGlyphVector(g2d.getFontRenderContext(), string);
        AffineTransform item = new AffineTransform();
        Shape stringShape = itemFontVector.getOutline();
        if (center)
            item.translate(x - stringShape.getBounds2D().getCenterX(), y);
        else
            item.translate(x, y);
        stringShape = item.createTransformedShape(stringShape);
        g2d.fill(stringShape);
    }

    public void refresh() {
        this.schedule = this.frame.getSchedule();
        initBoxes();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (PerformanceBox performanceBox : this.performanceBoxes) {
            if (performanceBox.containsMouse(e.getPoint())) {
                PopupWindow popup = new PopupWindow(this.schedule, performanceBox.getPerformance());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}

class PerformanceBox {

    private Performance performance;
    private Rectangle2D box;
    private String name;
    private String artist;
    private String times;
    private int maxLength = 20;

    public PerformanceBox(Performance performance) {
        this.performance = performance;
    }

    public void drawBox(Graphics2D g2d, int index) {
        // Calculate x position
        int x = ScheduleTab.TIMELINE_BAR_WIDTH + ScheduleTab.STAGE_BAR_WIDTH * index;

        // Calculate y position and height
        int y;
        int height = 0;
        y = (this.performance.getStartTime().getHour() - ScheduleTab.START_HOUR) *
                ScheduleTab.TIMELINE_BAR_HEIGHT + ScheduleTab.STAGE_BAR_HEIGHT;
        y += ScheduleTab.TIMELINE_BAR_HEIGHT * (this.performance.getStartTime().getMinute() / 60.0);

        height += (this.performance.getEndTime().getHour() - ScheduleTab.START_HOUR) *
                ScheduleTab.TIMELINE_BAR_HEIGHT + ScheduleTab.STAGE_BAR_HEIGHT;
        height += ScheduleTab.TIMELINE_BAR_HEIGHT * (this.performance.getEndTime().getMinute() / 60.0);
        height -= y;

        // Draw box
        this.box = new Rectangle2D.Double(x, y, ScheduleTab.STAGE_BAR_WIDTH, height);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fill(box);
        g2d.setColor(Color.BLACK);
        g2d.draw(box);

        // Draw strings
        g2d.setColor(Color.BLACK);
        ScheduleTab.font = ScheduleTab.font.deriveFont((float)16);
        String nameString = this.performance.getName();
        nameString = shorten(nameString);
        ScheduleTab.drawText(g2d, nameString, x + ScheduleTab.STAGE_BAR_WIDTH / 2, y + 20, true);

        String artistString = this.performance.getArtist().getName();
        artistString = shorten(artistString);
        ScheduleTab.drawText(g2d, artistString, x + ScheduleTab.STAGE_BAR_WIDTH / 2, y + 40, true);

        String timeString = this.performance.getStartTime().toString().substring(0, 5) + " - " +
                this.performance.getEndTime().toString().substring(0, 5);
        timeString = shorten(timeString);
        ScheduleTab.drawText(g2d, timeString, x + ScheduleTab.STAGE_BAR_WIDTH / 2, y + 60, true);
    }

    private String shorten(String string) {
        String newString = string;
        newString = newString.substring(0, Math.min(string.length(), this.maxLength));
        if (newString.length() != string.length()) {
            newString += "...";
        }
        return newString;
    }

    public boolean containsMouse(Point2D point) {
        return this.box.contains(point);
    }

    public Performance getPerformance() {
        return performance;
    }
}
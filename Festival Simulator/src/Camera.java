import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class Camera {
    private AffineTransform transform;
    private double zoom;
    private double translateX, translateY;

    public Point2D getMousePoint() {
        try {
            return transform.inverseTransform(mousePoint, null);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setMousePoint(Point2D mousePoint) {
        this.mousePoint = mousePoint;
    }

    public void translate(double translateX, double translateY){
        transform.setToIdentity();
        transform.translate(translateX, translateY);
    }

    private Point2D mousePoint;

    public Camera() {
        transform = new AffineTransform();
        zoom = 1;
        mousePoint = new Point2D.Double(0,0);
    }

    public AffineTransform getTransform(){
        Point2D p2 = getMousePoint();
        transform.setToIdentity();
        transform.translate(mousePoint.getX(), mousePoint.getY());
        transform.scale(zoom, zoom);
        transform.translate(-p2.getX(), -p2.getY());
        return transform;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom){
        this.zoom += zoom;

    }
}

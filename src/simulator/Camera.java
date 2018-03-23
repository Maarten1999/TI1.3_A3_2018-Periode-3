package simulator;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class Camera {

    private double cameraCenterX;
    private double cameraCenterY;
    private double prevCX, prevCY;
    private double translateX, translateY;
    private double prevZoom, zoom;
    private double prevInverseX, prevInverseY;

    public Camera() {
        this.prevZoom = 1;
        this.zoom = 1;
    }

    public void translate(double translateX, double translateY) {
        this.translateX = translateX;
        this.translateY = translateY;
    }

    public void centerZoom(double cameraCenterX, double cameraCenterY) {
        this.prevCX = this.cameraCenterX;
        this.prevCY = this.cameraCenterY;
        this.cameraCenterX = cameraCenterX;
        this.cameraCenterY = cameraCenterY;
    }

    public void zoom(double zoom) {
        this.prevZoom = this.zoom;
        this.zoom += zoom;
    }

    public AffineTransform getTransformIV() {

        AffineTransform af = new AffineTransform();
        af.translate(translateX, translateY);
        return af;
    }

    public AffineTransform getTransformIVI() {
        AffineTransform af = new AffineTransform();
        af.translate(cameraCenterX, cameraCenterY);
        af.scale(zoom, zoom);
        af.translate(-prevInverseX, -prevInverseY);
        return af;
    }

    public AffineTransform getTransform() {
        if (prevZoom > 0) {
            AffineTransform af1 = new AffineTransform();
            af1.translate(prevCX, prevCY);
            af1.scale(prevZoom, prevZoom);
            af1.translate(-prevInverseX, -prevInverseY);
            try {
                Point2D prevInverse = af1.inverseTransform(new Point2D.Double(cameraCenterX, cameraCenterY), null);
                prevInverseX = prevInverse.getX();
                prevInverseY = prevInverse.getY();
            } catch (NoninvertibleTransformException e) {
                e.printStackTrace();
            }
        }

        AffineTransform af = new AffineTransform();
        prevCX = prevCY = prevZoom = 0;
        af.translate(cameraCenterX, cameraCenterY);
        af.translate(translateX, translateY);
        af.scale(zoom, zoom);
        af.translate(-prevInverseX, -prevInverseY);
        return af;
    }

    public double getZoom() {
        return zoom;
    }

    public double getTranslateX() {
        return translateX;
    }

    public double getTranslateY() {
        return translateY;
    }
}

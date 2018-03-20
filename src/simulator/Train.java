package simulator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Train {

    private BufferedImage spriteSheet;
    private ArrayList<BufferedImage> sprite = new ArrayList<>();
    private final int spriteHeight = 52;
    private final int spriteWidth = 100;
    private final int speed;
    private int currentFrame = 0;
    private int y;
    private int endX;
    private int startX;
    private int currentX;
    private boolean isFinished;

    public Train(int startX, int endX, int y, int speed) {
        try {
            this.spriteSheet = ImageIO.read(getClass().getResource("\\..\\train.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initSprite();
        this.speed = speed;
        this.startX = startX;
        this.endX = endX;
        this.y = y;
        this.currentX = this.startX;
    }

    private void initSprite() {
        for (int y = 0; y < this.spriteSheet.getHeight() / this.spriteHeight; y++) {
            BufferedImage image = this.spriteSheet.getSubimage(0,
                    y * this.spriteHeight, this.spriteWidth, this.spriteHeight);
            this.sprite.add(image);
        }
    }

    public void update(VisitorManager visitorManager) {
        this.currentFrame++;
        if (this.currentFrame >= this.sprite.size() - 1) {
            this.currentFrame = 0;
        }
        if (this.endX > this.currentX)
            this.currentX += this.speed;
        if (this.endX <= this.currentX)
            this.currentX -= this.speed;

        ArrayList<Visitor> visitors = visitorManager.getVisitors();
        for (int i = 0; i < visitors.size(); i++) {
            Rectangle2D trainBox = new Rectangle2D.Double(this.currentX, this.y, this.spriteWidth, this.spriteHeight);
            if (visitors.get(i).getCircle().intersects(trainBox)) {
                visitorManager.removeVisitor(i);
            }
        }
        if (Math.abs(this.currentX - this.endX) < this.speed) {
            this.isFinished = true;
        }
    }

    public void draw(Graphics2D g2d) {
        AffineTransform at = new AffineTransform();
        at.translate(this.currentX, this.y);
        g2d.drawImage(this.sprite.get(this.currentFrame), at, null);
    }

    public boolean isFinished() {
        return isFinished;
    }
}

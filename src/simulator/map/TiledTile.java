package simulator.map;

import java.awt.image.BufferedImage;

public class TiledTile {

    private BufferedImage image;

    TiledTile(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }
}

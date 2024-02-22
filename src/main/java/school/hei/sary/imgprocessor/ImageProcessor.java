package school.hei.sary.imgprocessor;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.net.URL;

@Service
public class ImageProcessor {

    public BufferedImage toBlackAndWhite(String imageUrl) throws IOException {
        BufferedImage originalImage = ImageIO.read(new URL(imageUrl));
        BufferedImage blackAndWhiteImage = new BufferedImage(
                originalImage.getWidth(), originalImage.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY);
        RescaleOp op = new RescaleOp(1f, -255f, null);
        op.filter(originalImage, blackAndWhiteImage);
        return blackAndWhiteImage;
    }
}

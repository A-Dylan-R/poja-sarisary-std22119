package school.hei.sary.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/black-and-white")
public class BlackAndWhite {

    private final Map<String, String> imageStore = new HashMap<>();

    @PutMapping("/{id}")
    public ResponseEntity<Void> uploadImage(@PathVariable String id, @RequestBody MultipartFile image) throws IOException {
        // Lecture de l'image dans un objet BufferedImage
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));

        // Création d'une image noire et blanche
        BufferedImage blackAndWhiteImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics = blackAndWhiteImage.createGraphics();
        graphics.drawImage(bufferedImage, 0, 0, null);
        graphics.dispose();

        // Conversion de l'image en tableau de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(blackAndWhiteImage, "jpg", baos);
        byte[] blackAndWhiteBytes = baos.toByteArray();

        // Sauvegarde de l'image transformée dans la map
        imageStore.put(id, Base64.getEncoder().encodeToString(blackAndWhiteBytes));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getImage(@PathVariable String id) {
        if (!imageStore.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> response = new HashMap<>();
        response.put("original_url", imageStore.get(id));
        response.put("transformed_url", "https://transformed.url"); // Ici, vous pouvez remplacer par l'URL de l'image transformée

        return ResponseEntity.ok(response);
    }
}

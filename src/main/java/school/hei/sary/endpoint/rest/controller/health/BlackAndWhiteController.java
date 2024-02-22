package school.hei.sary.endpoint.rest.controller.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import school.hei.sary.imgprocessor.ImageProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BlackAndWhiteController {

    private final Map<String, String> imageStore = new HashMap<>();

    @Autowired
    private ImageProcessor imageProcessor;

    @PutMapping("/black-and-white/{id}")
    public ResponseEntity<Void> uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile image) throws IOException {
        BufferedImage originalImage = ImageIO.read(image.getInputStream());
        BufferedImage blackAndWhiteImage = imageProcessor.toBlackAndWhite(String.valueOf(originalImage));
        imageStore.put(id, "https://transformed.url");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/black-and-white/{id}")
    public ResponseEntity<Map<String, String>> getImage(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        response.put("original_url", "https://original.url");
        response.put("transformed_url", imageStore.get(id));
        return ResponseEntity.ok(response);
    }
}

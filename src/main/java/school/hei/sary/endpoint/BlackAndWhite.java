package school.hei.sary.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BlackAndWhite {

    private final S3Client s3Client;

    public BlackAndWhite(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @PutMapping("/black-and-white/{id}")
    public ResponseEntity<Void> uploadImage(@PathVariable String id, @RequestBody MultipartFile image) throws IOException {
        byte[] bytes = image.getBytes();
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket("your-bucket-name")
                .key(id)
                .build();
        s3Client.putObject(request, (Path) new ByteArrayInputStream(bytes));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/black-and-white/{id}")
    public ResponseEntity<Map<String, String>> getImageUrls(@PathVariable String id) throws URISyntaxException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket("your-bucket-name")
                .key(id)
                .build();
        ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(request);
        String originalUrl = getPresignedUrl(id);
        String transformedUrl = getPresignedUrl(id + "-bw");
        Map<String, String> result = new HashMap<>();
        result.put("original_url", originalUrl);
        result.put("transformed_url", transformedUrl);
        return ResponseEntity.ok().body(result);
    }

    private String getPresignedUrl(String id) throws URISyntaxException {
        URL url = s3Client.utilities().getUrl(builder -> builder.bucket("your-bucket-name").key(id));
        return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null).toString();
    }
}

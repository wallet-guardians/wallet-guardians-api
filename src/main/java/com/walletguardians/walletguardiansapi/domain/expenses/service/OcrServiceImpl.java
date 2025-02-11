package com.walletguardians.walletguardiansapi.domain.expenses.service;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.OcrResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OcrServiceImpl implements OcrService{

    private static final String CLOVA_VERSION = "V2";
    private static final String CLOVA_TYPE = "receipt";

    private final WebClient webClient;

    @Value("${naver.service.base-url}")
    private String baseUrl;
    @Value("${naver.service.endpoint}")
    private String endpoint;
    @Value("${naver.service.secretKey}")
    private String secretKey;
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public OcrServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public OcrResponse sendOcrRequest(String contentType, String imagePath) throws IOException {
        Map<String, Object> imageData = setImageData(contentType, imagePath);
        Map<String, Object> requestBody = setRequestBody(imageData);

        return webClient.post()
                .uri(endpoint)
                .header("Content-Type", "application/json")
                .header("X-OCR-SECRET", secretKey)
                .body(Mono.just(requestBody), Map.class)
                .retrieve()
                .bodyToMono(OcrResponse.class)
                .block();
    }

    private Map<String, Object> setImageData(String contentType, String imagePath)
            throws IOException {
        Map<String, Object> imageData = new HashMap<>();
        imageData.put("format", contentType);
        imageData.put("data", encodeImage(imagePath));
        imageData.put("name", CLOVA_TYPE);
        return imageData;
    }

    private Map<String, Object> setRequestBody(Map<String, Object> imageData) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("version", CLOVA_VERSION);
        requestBody.put("requestId", createRequestId());
        requestBody.put("timestamp", System.currentTimeMillis());
        requestBody.put("images", new Map[]{imageData});
        return requestBody;
    }

    private String encodeImage(String imagePath) throws IOException {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Blob blob = storage.get(bucketName, imagePath);
        if (blob == null) {
            throw new FileNotFoundException("스토리지에서 파일을 찾을 수 없습니다.");
        }
        byte[] imageBytes =  blob.getContent();

        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private String createRequestId() {
        return UUID.randomUUID().toString();
    }
}
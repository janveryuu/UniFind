package com.example.system1.Service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Base64;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://generativelanguage.googleapis.com").build();
    }

    public String analyzeImage(byte[] bytes, String mimeType) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }

        try {
            // Convert file to Base64
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            
            if (mimeType == null || !mimeType.startsWith("image/")) {
                mimeType = "image/jpeg";
            }

            // Build JSON Request
            JSONObject root = new JSONObject();
            JSONArray contents = new JSONArray();
            JSONObject content = new JSONObject();
            JSONArray parts = new JSONArray();
            
            // Text Prompt
            JSONObject textPart = new JSONObject();
            textPart.put("text", "Analyze this image of a lost or found item. Return a comma-separated list of 3 to 5 concise tags or keywords describing it. Do not include any other text.");
            parts.put(textPart);

            // Image Part
            JSONObject inlineData = new JSONObject();
            inlineData.put("mimeType", mimeType);
            inlineData.put("data", base64Image);
            
            JSONObject imagePart = new JSONObject();
            imagePart.put("inlineData", inlineData);
            parts.put(imagePart);

            content.put("parts", parts);
            contents.put(content);
            root.put("contents", contents);

            // Call API
            String responseStr = webClient.post()
                    .uri("/v1beta/models/gemini-2.5-flash:generateContent?key={key}", apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(root.toString())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parse response
            JSONObject responseJson = new JSONObject(responseStr);
            JSONArray candidates = responseJson.optJSONArray("candidates");
            if (candidates != null && candidates.length() > 0) {
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONObject contentObj = firstCandidate.optJSONObject("content");
                if (contentObj != null) {
                    JSONArray respParts = contentObj.optJSONArray("parts");
                    if (respParts != null && respParts.length() > 0) {
                        return respParts.getJSONObject(0).optString("text", "").trim();
                    }
                }
            }
            return "";

        } catch (Exception e) {
            System.err.println("Gemini API Error: " + e.getMessage());
            return "AI Analysis Failed";
        }
    }
}

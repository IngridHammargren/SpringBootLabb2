package se.iths.springbootlabb2.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class TranslateService {

    private final RestClient restClient;

    public TranslateService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Retryable
    public String detectMessageLanguage(String text) {
        String result = restClient.post()
                .uri("http://localhost:5000/detect")
                .contentType(APPLICATION_JSON)
                .body(String.format("{\"q\":\"%s\"}", text))
                .retrieve()
                .body(String.class);
        if (result.contains("\"en\"")) return "en";
        else return "sv";
    }

    @Retryable
    public String translateMessage(String text) {
        String sourceLanguage = detectMessageLanguage(text);
        String targetLanguage = sourceLanguage.equals("en") ? "sv" : "en";

        try {
            String jsonString = String.format("{\"q\":\"%s\",\"source\":\"%s\",\"target\":\"%s\"}", text, sourceLanguage, targetLanguage);

            String translatedTextJson = Objects.requireNonNull(restClient.post()
                    .uri("http://localhost:5000/translate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonString)
                    .retrieve()
                    .body(String.class));


            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(translatedTextJson);
            String translatedText = jsonNode.get("translatedText").asText();

            return translatedText;

        } catch (RestClientResponseException e) {
            return "Translation failed";
        } catch (JsonProcessingException e) {
            return "Error processing json";
        }

    }
}






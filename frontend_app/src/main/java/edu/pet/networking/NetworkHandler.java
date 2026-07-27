package edu.pet.networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pet.dto.BugResponse;
import javafx.application.Platform;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class NetworkHandler {
    private final URI serverURI;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    private final String generalEndpoint = "/api/bugs";
    private final String specificEndpoint = "/api/bugs/";

    private final String headerType = "Content-Type";
    private final String headerBody = "application/json";


    public NetworkHandler(String serverURI) {
        this.serverURI = URI.create(serverURI);
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public List<BugResponse> getAll() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURI + generalEndpoint))
                .GET()
                .header(headerType, headerBody)
                .build();

        String result = "";
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        result = response.body();
        return mapper.readValue(result, new TypeReference<>() {});
    }

    public BugResponse getById(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURI + specificEndpoint + id))
                .GET()
                .header(headerType, headerBody)
                .build();
        String result = "";
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) {
            throw new RuntimeException("invalid id");
        }

        result = response.body();
        return mapper.readValue(result, new TypeReference<>() {});
    }

    public BugResponse markClose(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURI + specificEndpoint + id + "/close"))
                .header(headerType, headerBody)
                .method("POST", HttpRequest.BodyPublishers.noBody())
                .build();
        String result = "";
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) {
            throw new RuntimeException("invalid id");
        }
        result = response.body();
        return mapper.readValue(result, new TypeReference<>() {});
    }

    public BugResponse markOpen(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURI + specificEndpoint + id + "/open"))
                .header(headerType, headerBody)
                .method("POST", HttpRequest.BodyPublishers.noBody())
                .build();
        String result = "";
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) {
            throw new RuntimeException("invalid id");
        }
        result = response.body();
        return mapper.readValue(result, new TypeReference<>() {});
    }
}

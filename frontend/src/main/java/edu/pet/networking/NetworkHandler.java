package edu.pet.networking;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NetworkHandler {
    private final URI serverURI;
    private final HttpClient httpClient;

    private final String generalEndpoint = "/api/bugs";
    private final String specificEndpoint = "/api/bugs/";

    private final String headerType = "Content-Type";
    private final String headerBody = "application/json";

    private final String postBody = "{\"title\":\"{title}\",\"info\":\"{info}\",\"priority\":\"{priority}\",\"state\":\"state\"}";

    public NetworkHandler(String serverURI) {
        this.serverURI = URI.create(serverURI);
        this.httpClient = HttpClient.newHttpClient();
    }

    public String getAll() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURI + generalEndpoint))
                .GET()
                .header(headerType, headerBody)
                .build();

        String result = "";
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            result = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.printf("Caught %s! More info:\n\t%s\n", e, e.getLocalizedMessage());
        }
        return result;
    }

    public String getById(Long id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverURI + specificEndpoint + id))
                .GET()
                .header(headerType, headerBody)
                .build();
        String result = "";
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            result = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.printf("Caught %s! More info:\n\t%s\n", e, e.getLocalizedMessage());
        }
        return result;
    }
}

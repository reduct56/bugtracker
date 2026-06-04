package edu.pet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) throws InterruptedException, URISyntaxException, IOException {
        HttpClient client = HttpClient.newHttpClient();

        String postBody = "{\"title\" : \"did it\", \"info\" : \"some info\", \"priority\" : \"LOW\", \"state\" : \"OPEN\"}";

        HttpRequest requestNewTask = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/bugs"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(postBody))
                .build();

        HttpRequest requestGetAll = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/bugs"))
                .build();

        HttpResponse<String> response = client.send(requestGetAll, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
    }
}
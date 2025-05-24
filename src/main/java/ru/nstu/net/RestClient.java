package ru.nstu.net;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Timer;

public class RestClient {

    private HttpClient httpClient;

    private Timer timer;

    public RestClient() {
        httpClient = HttpClient.newHttpClient();
    }

    public String doGet(String uri, Integer clientId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api" + uri))
                .GET()
                .header("X-Client-Id", clientId.toString())
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new IOException(response.statusCode() + " " + response.body());
        }
    }

    public Integer register() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/register"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return Integer.parseInt(response.body());
        } else {
            throw new IOException(response.statusCode() + " " + response.body());
        }
    }

    public boolean doPost(String uri, Integer clientId, String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api" + uri))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("X-Client-Id", clientId.toString())
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return true;
        } else {
            throw new IOException(response.statusCode() + " " + response.body());
        }
    }

    public void close() {
        httpClient.close();
    }
}

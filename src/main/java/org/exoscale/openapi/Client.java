package org.exoscale.openapi;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.util.Optional;

import org.exoscale.openapi.model.CreateInstanceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.flogger.Flogger;

public class Client {
    private static final URI BASE_URI = URI.create("https://api-at-vie-1.exoscale.com");
    private static final String API_PREFIX = "/v2";
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final org.exoscale.openapi.Authorization authorization;

    public Client(String apiKey, String secret) throws InvalidKeyException {
        this.authorization = new org.exoscale.openapi.Authorization(apiKey, secret);
    }

    public HttpResponse<String> createInstance(CreateInstanceRequest instanceSpec) throws IOException, InterruptedException {
        return postRequest(API_PREFIX + "/instance", "", instanceSpec);
    }

    public HttpResponse<String> listInstanceTypes() throws IOException, InterruptedException {
        return getRequest(API_PREFIX + "/instance-type", "");
    }

    public HttpResponse<String> listDeployTargets() throws IOException, InterruptedException {
        return getRequest(API_PREFIX + "/deploy-target", "");
    }

    public HttpResponse<String> listSecurityGroups() throws IOException, InterruptedException {
        return getRequest(API_PREFIX + "/security-group", "");
    }

    public HttpResponse<String> listInstances() throws IOException, InterruptedException {
        return getRequest(API_PREFIX + "/instance", "");
    }

    private HttpResponse<String> postRequest(String path, String queryParams, Object requestBody) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(requestBody);

//        String params = queryParams != null ? "?" + queryParams : "";
        String params = "";

        HttpRequest httpPost = HttpRequest.newBuilder()
                .uri(BASE_URI.resolve(path))
                .header("Authorization", authorization.getAuthorizationHeader(HttpMethod.POST, path, queryParams, Optional.of(jsonBody), Optional.empty()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return httpClient.send(httpPost, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getRequest(String path, String queryParams) throws IOException, InterruptedException {
//        String params = queryParams != null ? "?" + queryParams : "";
        String params = "";
        HttpRequest httpPost = HttpRequest.newBuilder()
                .uri(BASE_URI.resolve(path))
                .header("Authorization", authorization.getAuthorizationHeader(HttpMethod.GET, path, queryParams, Optional.empty(), Optional.empty()))
                .GET()
                .build();

        return httpClient.send(httpPost, HttpResponse.BodyHandlers.ofString());
    }
}

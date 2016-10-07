package com.ibm.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class HttpRequestUtil {
    private String uri;
    private HttpClient client;

    public HttpRequestUtil(String uri) {
        this.uri = uri;
        client = HttpClientBuilder.create().build();
    }

    public String get(String path) throws URISyntaxException, IOException {
        HttpGet request = new HttpGet(uri + path);
        request.addHeader("Content-Type", "application/json");

        return execute(request);
    }

    public String post(String path, byte[] payload) throws URISyntaxException, IOException {
        HttpPost request = new HttpPost(uri + path);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new ByteArrayEntity(payload));

        return execute(request);
    }

    public String put(String path, byte[] payload) throws URISyntaxException, IOException {
        HttpPut request = new HttpPut(uri + path);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new ByteArrayEntity(payload));

        return execute(request);
    }

    private String execute(HttpUriRequest request) throws IOException {
        HttpResponse response = client.execute(request);

        return EntityUtils.toString(response.getEntity());
    }
}

/**
 * Copyright 2016 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.motorcorp.messagehub.consumer.util;

import okhttp3.*;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;


import java.io.IOException;

public class HttpClient {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static HttpClient instance;

    public static synchronized HttpClient getInstance() {
        if(instance == null) {
            instance = new HttpClient();
        }

        return instance;
    }

    private OkHttpClient httpClient;
    private HttpClient() {
        this.httpClient = new OkHttpClient();
    }

    public String post(String endpoint, Object payload) throws IOException {
        RequestBody body = RequestBody.create(JSON, payload.toString());

        Request request = new Request.Builder()
                .url(endpoint)
                .post(body)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            return response.body().string();
        }
    }
}

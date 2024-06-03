package com.mycode.pathpilotserver.intercom.gbt;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ChatGbtIntegration {

//    private static final String API_KEY = "sk-proj-T1vmX9xVWzcCPCnmz2T0T3BlbkFJ2bITu5XDn2p5cdqQjvnl";
    private static final String API_KEY= "sk-proj-1J9J9Z2Q9";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static String getOptimalRoute(String prompt) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = getJsonObject(prompt);

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            assert response.body() != null;
            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray choices = jsonResponse.getJSONArray("choices");

            return choices.getJSONObject(0).getJSONObject("message").getString("content");
        }
    }

    @NotNull
    private static JSONObject getJsonObject(String prompt) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("model", "gpt-4o");
        json.put("temperature", 1);
        json.put("max_tokens", 256);
        json.put("top_p", 1.0);
        json.put("frequency_penalty", 0.0);
        json.put("presence_penalty", 0.0);

        JSONArray messages = new JSONArray();
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        JSONArray contentArray = new JSONArray();
        JSONObject textObject = new JSONObject();
        textObject.put("type", "text");
        textObject.put("text", prompt);
        contentArray.put(textObject);
        userMessage.put("content", contentArray);
        messages.put(userMessage);
        json.put("messages", messages);
        return json;
    }
}

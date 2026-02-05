package com.example.myapplication;

import android.util.Log;
import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;

public class Supabase {

        private final String supUrl;
        private final String anonKey;
        private final OkHttpClient client = new OkHttpClient();

        public Supabase(String supUrl, String anonKey) {
                this.supUrl = supUrl;
                this.anonKey = anonKey;
        }


        public void signUpUser(User user, final ApiCallback callBack) {
                String url = supUrl + "/rest/v1/users";
                String jsonbody = "{" +
                        "\"login\": \"" + user.getLogin() + "\", " +
                        "\"password\": \"" + user.getPassword() + "\", " +
                        "\"name\": \"" + user.getName() + "\"" +
                        "}";

                sendPostRequest(url, jsonbody, callBack);
        }

        public void createGroup(String groupName, String rootUserId, final ApiCallback callBack) {
                String url = supUrl + "/rest/v1/groups";
                String jsonbody = "{" +
                        "\"group_name\": \"" + groupName + "\", " +
                        "\"root_user_id\": \"" + rootUserId + "\"" +
                        "}";
                sendPostRequest(url, jsonbody, callBack);
        }


        public void deleteGroup(long groupId, final ApiCallback callBack) {
                String url = supUrl + "/rest/v1/groups?id=eq." + groupId;
                Request request = new Request.Builder()
                        .url(url)
                        .headers(getCommonHeaders())
                        .delete()
                        .build();
                executeRequest(request, callBack);
        }

        private Headers getCommonHeaders() {
                return new Headers.Builder()
                        .add("apikey", anonKey)
                        .add("Authorization", "Bearer " + anonKey)
                        .add("Content-Type", "application/json")
                        .add("Accept", "application/json")
                        .build();
        }


        private void sendPostRequest(String url, String jsonBody, final ApiCallback callBack) {
                okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(
                        jsonBody, okhttp3.MediaType.parse("application/json; charset=utf-8"));

                Request request = new Request.Builder()
                        .url(url)
                        .headers(getCommonHeaders())
                        .addHeader("Prefer", "return=representation")
                        .post(requestBody)
                        .build();

                executeRequest(request, callBack);
        }


        private void executeRequest(Request request, final ApiCallback callBack) {
                client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                callBack.onFailure("Помилка мережі: " + e.getMessage());
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                String responseBody = response.body() != null ? response.body().string() : "";
                                if (response.isSuccessful()) {
                                        callBack.onSuccess(responseBody);
                                } else {
                                        Log.e("SUPABASE_DEBUG", "Code: " + response.code() + " Body: " + responseBody);
                                        callBack.onFailure("Помилка сервера " + response.code() + ": " + responseBody);
                                }
                        }
                });
        }



        public void addUserToGroupByUsername(long groupId, String username, final ApiCallback callBack) {
                String findUrl = supUrl + "/rest/v1/users?name=ilike." + username.trim() + "&select=id";

                Request request = new Request.Builder()
                        .url(findUrl)
                        .headers(getCommonHeaders())
                        .get()
                        .build();

                executeRequest(request, new ApiCallback() {
                        @Override
                        public void onSuccess(String resultJson) {
                                try {
                                        org.json.JSONArray array = new org.json.JSONArray(resultJson);
                                        if (array.length() > 0) {
                                                long friendId = array.getJSONObject(0).getLong("id");

                                                // додаємо в групу користувача
                                                Log.d("DEBUG_FLOW", " Ім'я " + username + ": " + friendId);
                                                addUserToGroup(groupId, friendId, callBack);
                                        } else {
                                                callBack.onFailure("Користувача з іменем '" + username + "' не знайдено");
                                        }
                                } catch (Exception e) {
                                        callBack.onFailure("Помилка JSON: " + e.getMessage());
                                }
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                                callBack.onFailure(errorMessage);
                        }
                });
        }

        // ПРИЙМАЄ long ID.
        public void addUserToGroup(long groupId, long userId, final ApiCallback callBack) {
                String url = supUrl + "/rest/v1/group_members";
                String jsonbody = "{\"groups_id\": " + groupId + ", \"user_id\": " + userId + ", \"role\": \"member\"}";
                sendPostRequest(url, jsonbody, callBack);
        }
}
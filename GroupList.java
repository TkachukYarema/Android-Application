package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GroupList extends AppCompatActivity {

    private EditText etGroupName, etFriendsUsername;
    private Button btnCreateGroup, btnContinue;
    private Supabase supaBaseClient;
    private long currentUserId;

    String SupUrl = "https://xdvqoufyfbqhyqdsthna.supabase.co";
    String AnonKey = "sb_publishable_pfOqNPwzinU_KL0Usgm1Cg_VoGO2Oex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

   
        etGroupName = findViewById(R.id.etGroupName);
        etFriendsUsername = findViewById(R.id.etFriendsUsername);
        btnCreateGroup = findViewById(R.id.btnCreateGroup);
        btnContinue = findViewById(R.id.btnContinueWithoutGroup);

        supaBaseClient = new Supabase(SupUrl, AnonKey);


        currentUserId = getIntent().getLongExtra("USER_ID", -1);


        btnContinue.setOnClickListener(v -> navigateToMainScreen());


        btnCreateGroup.setOnClickListener(v -> {
            String gName = etGroupName.getText().toString().trim();
            String fUsername = etFriendsUsername.getText().toString().trim();

            if (gName.isEmpty()) {
                Toast.makeText(this, "Enter group name", Toast.LENGTH_SHORT).show();
                return;
            }

            createGroupLogic(gName, fUsername);
        });
    }

    private void createGroupLogic(String groupName, String friendUsername) {
        Log.d("DEBUG_FLOW", "1.  " + groupName);

        supaBaseClient.createGroup(groupName, String.valueOf(currentUserId), new ApiCallback() {
            @Override
            public void onSuccess(String resultJson) {
                Log.d("DEBUG_FLOW", "2. Групу створено: " + resultJson);
                try {
                    JSONArray array = new JSONArray(resultJson);
                    if (array.length() > 0) {
                        long groupId = array.getJSONObject(0).getLong("id");
                        Log.d("DEBUG_FLOW", "3. ID групи: " + groupId);

                        if (!friendUsername.isEmpty()) {
                            Log.d("DEBUG_FLOW", "4.   " + friendUsername);
                            findAndAddFriend(groupId, friendUsername);
                        } else {
                            Log.d("DEBUG_FLOW", "4а. Друга не вказано, завершуємо.");
                            finishSuccess();
                        }
                    }
                } catch (JSONException e) {
                    Log.e("DEBUG_FLOW", "Помилка ID групи: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("DEBUG_FLOW", "Помилка створення групи: " + errorMessage);
            }
        });
    }

    private void findAndAddFriend(long groupId, String username) {
        Log.d("DEBUG_FLOW", "5. Викликаємо addUserToGroupByUsername для: " + username);

        supaBaseClient.addUserToGroupByUsername(groupId, username, new ApiCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d("DEBUG_FLOW", "6.  Друг доданий.");
                finishSuccess();
            }

            @Override
            public void onFailure(String error) {
                Log.e("DEBUG_FLOW", "6. Помилка на етапі додавання друга: " + error);
                finishSuccess();
            }
        });
    }
    private void finishSuccess() {
        runOnUiThread(() -> {
            navigateToMainScreen();
        });
    }

    private void navigateToMainScreen() {
        Intent intent = new Intent(GroupList.this, Screen1.class);
        startActivity(intent);
        finish();
    }
}
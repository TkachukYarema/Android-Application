package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText edEmail, edPassword, edUsername;
    private Button btnReg, btnSignIn;
    private Supabase SupaBaseClient;

    String AnonKeyclient = "sb_publishable_pfOqNPwzinU_KL0Usgm1Cg_VoGO2Oex";
    String SupUrlclient = "https://xdvqoufyfbqhyqdsthna.supabase.co";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        btnReg = findViewById(R.id.btnReg);
        edPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.tvSignIn);
        edEmail = findViewById(R.id.email);
        edUsername = findViewById(R.id.etUsername);

        SupaBaseClient = new Supabase(SupUrlclient, AnonKeyclient);

        btnSignIn.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, Login.class);
            startActivity(intent1);
        });

        btnReg.setOnClickListener(v -> {
            String login = edEmail.getText().toString().trim();
            String password = edPassword.getText().toString().trim();
            String name = edUsername.getText().toString().trim();


            if (login.isEmpty() || password.length() < 6 || name.isEmpty()) {
                Toast.makeText(MainActivity.this,
                        " Пароль має бути від 6 символів.",
                        Toast.LENGTH_SHORT).show();
                return;
            }


            User newUser = new User(login, password, name);

            SupaBaseClient.signUpUser(newUser, new ApiCallback() {
                @Override
                public void onSuccess(String resultJson) {
                    runOnUiThread(() -> {
                        try {
                            org.json.JSONArray jsonArray = new org.json.JSONArray(resultJson);
                            org.json.JSONObject newUser = jsonArray.getJSONObject(0);
                            long userId = newUser.getLong("id");

                            Intent intent = new Intent(MainActivity.this, GroupList.class);
                            intent.putExtra("USER_ID", userId);
                            startActivity(intent);
                            finish();
                        } catch (org.json.JSONException e) {
                            Log.e("JSON_ERROR", "Помилка  " + e.getMessage());
                        }
                    });
                }

                @Override
                public void onFailure(String errorMessage) {
                    runOnUiThread(() -> {
                        Log.e("SUPABASE_ERROR", errorMessage);

                    });
                }
            });
        });


    }

}
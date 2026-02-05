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

public class Login extends AppCompatActivity {


    String SupUrlclient = "https://xdvqoufyfbqhyqdsthna.supabase.co";
    String AnonKeyclient = "sb_publishable_pfOqNPwzinU_KL0Usgm1Cg_VoGO2Oex";

    private Button btnLogin;
    private EditText etEmail; // Використовується як логін
    private EditText etPassword;
    private Button btnSignUp;
    private Supabase SupaBaseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etEmail = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.tvSignUp);

    }
}




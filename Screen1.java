package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Screen1 extends AppCompatActivity {
    private ImageButton IBLoop = findViewById(R.id.btnSearch);
    private ImageButton IBGallery = findViewById(R.id.btnGallery);
    private ImageButton IBmenu = findViewById(R.id.btnMenu);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.mainscreen);
        IBLoop.setOnClickListener( v ->{
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                }


        );
        IBGallery.setOnClickListener( v ->{
                    Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                }


        );
       IBmenu.setOnClickListener( v ->{
                    Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                }


        );
    }
}
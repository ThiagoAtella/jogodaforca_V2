package com.example.jogodaforcav2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jogodaforcav2.databinding.ActivityTeamSelectionBinding;

public class TeamSelectionActivity extends AppCompatActivity {
    private ActivityTeamSelectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonFluminense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarJogo("FLUMINENSE");
            }
        });

        binding.buttonFlamengo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarJogo("FLAMENGO");
            }
        });
        binding.buttonVasco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarJogo("VASCO");
            }
        });
        binding.buttonBotafogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarJogo("BOTAFOGO");
            }
        });
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Volta para o menu
            }
        });
    }

    private void iniciarJogo(String time) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("TIME_ESCOLHIDO", time);
        startActivity(intent);
    }
}

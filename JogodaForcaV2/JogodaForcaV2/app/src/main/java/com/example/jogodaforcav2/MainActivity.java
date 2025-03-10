package com.example.jogodaforcav2;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.jogodaforcav2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ForcaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String timeEscolhido = getIntent().getStringExtra("TIME_ESCOLHIDO");
        if (timeEscolhido == null) {
            timeEscolhido = "FLUMINENSE";
        }

        viewModel = new ViewModelProvider(this, new ForcaViewModel.ForcaViewModelFactory(timeEscolhido))
                .get(ForcaViewModel.class);

        configurarObservadores();
        configurarBotoes();
        iniciarNovoJogo();

        // Configuração do Edge-To-Edge
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void configurarObservadores() {
        viewModel.getPalavraOculta().observe(this, palavra -> binding.textViewWord.setText(palavra));

        viewModel.getTentativas().observe(this, tentativas ->
                binding.textViewAttempts.setText("Tentativas restantes: " + tentativas)
        );

        viewModel.getStatus().observe(this, status -> {
            binding.textViewStatus.setText(status.getMessage());
            if (status.getAcerto() == Boolean.FALSE) {
                binding.textViewWord.setTextColor(Color.RED);
            } else if (status.getAcerto() == Boolean.TRUE) {
                binding.textViewWord.setTextColor(Color.GREEN);
            } else {
                binding.textViewWord.setTextColor(Color.BLACK);
            }
        });
    }

    private void configurarBotoes() {
        binding.buttonGuess.setOnClickListener(v -> {
            String letra = binding.inputLetter.getText().toString().trim().toUpperCase();
            if (!TextUtils.isEmpty(letra) && letra.length() == 1 && Character.isLetter(letra.charAt(0))) {
                viewModel.processarPalpite(letra.charAt(0));
            }
            binding.inputLetter.getText().clear();
        });

        binding.buttonRestart.setOnClickListener(v -> iniciarNovoJogo());
    }

    private void iniciarNovoJogo() {
        viewModel.iniciarNovoJogo();
        binding.textViewStatus.setText("");
        binding.textViewWord.setTextColor(Color.BLACK);
    }
}

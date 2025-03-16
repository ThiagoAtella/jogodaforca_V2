package com.example.jogodaforcav2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.jogodaforcav2.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ForcaViewModel viewModel;
    private final Map<Character, Button> keyboardButtons = new HashMap<>();

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
        criarTeclado();
        configurarBotoes();
        configurarBotaoVoltar();  // 🔹 Novo método para o botão de voltar
        iniciarNovoJogo();
    }

    private void configurarBotoes() {
        binding.buttonRestart.setOnClickListener(v -> iniciarNovoJogo());
    }

    private void configurarBotaoVoltar() {
        Button buttonBack = findViewById(R.id.buttonBackToTeamSelection);
        buttonBack.setOnClickListener(v -> {
            // Criar intenção para voltar para a tela de seleção de times
            Intent intent = new Intent(MainActivity.this, TeamSelectionActivity.class);
            startActivity(intent);
            finish(); // Fecha a MainActivity para evitar voltar para o jogo já iniciado
        });
    }

    private void configurarObservadores() {
        viewModel.getPalavraOculta().observe(this, palavra -> binding.textViewWord.setText(palavra));

        viewModel.getTentativas().observe(this, tentativas ->
                binding.textViewAttempts.setText("Tentativas restantes: " + tentativas)
        );

        viewModel.getStatus().observe(this, status -> {
            binding.textViewStatus.setText(status.getMessage());
            if (status.getAcerto() != null) {
                binding.textViewWord.setTextColor(status.getAcerto() ? Color.GREEN : Color.RED);
            } else {
                binding.textViewWord.setTextColor(Color.BLACK);
            }
        });
    }

    private void criarTeclado() {
        GridLayout keyboardLayout = binding.keyboardLayout;
        keyboardLayout.removeAllViews();

        keyboardLayout.setColumnCount(7);

        for (char letra = 'A'; letra <= 'Z'; letra++) {
            final char letraFinal = letra;
            Button btn = new Button(this);
            btn.setText(String.valueOf(letraFinal));
            btn.setTextSize(18);
            btn.setAllCaps(true);
            btn.setGravity(Gravity.CENTER);
            btn.setBackgroundColor(Color.LTGRAY);
            btn.setPadding(5, 5, 5, 5);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(5, 5, 5, 5);
            btn.setLayoutParams(params);

            btn.setOnClickListener(v -> processarPalpite(letraFinal));

            keyboardLayout.addView(btn);
            keyboardButtons.put(letraFinal, btn);
        }
    }

    private void processarPalpite(char letra) {
        viewModel.processarPalpite(letra);
        Button btn = keyboardButtons.get(letra);
        if (btn != null) {
            btn.setEnabled(false);
            btn.setBackgroundColor(Color.GRAY);
        }
    }

    private void iniciarNovoJogo() {
        viewModel.iniciarNovoJogo();
        binding.textViewStatus.setText("");
        binding.textViewWord.setTextColor(Color.BLACK);

        for (Button btn : keyboardButtons.values()) {
            btn.setEnabled(true);
            btn.setBackgroundColor(Color.LTGRAY);
        }
    }
}

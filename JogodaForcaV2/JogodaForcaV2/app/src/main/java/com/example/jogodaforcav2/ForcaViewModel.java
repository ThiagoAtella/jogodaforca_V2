package com.example.jogodaforcav2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ForcaViewModel extends ViewModel {
    private static final List<String> PALAVRAS_FLUMINENSE = Arrays.asList("FLUMINENSE", "CANO", "TRICOLOR", "CAMPEÃO", "ETERNO",
            "LARANJEIRAS", "CARTOLINHA", "COPARIO");
    private static final List<String> PALAVRAS_FLAMENGO = Arrays.asList("FLAMENGO", "GABIGOL", "MENGO", "HEXACAMPEÃO", "MARACANÃ",
            "URUBU", "RUBRO-NEGRO", "ZICO", "JORGEJESUS");
    private static final List<String> PALAVRAS_VASCO = Arrays.asList("VASCO", "ROMÁRIO", "GIGANTE", "CRUZMALTINO", "SAOJANUARIO",
            "ROBERTODINAMITE", "VASCÃO", "COLINA", "ALMIRANTE");
    private static final List<String> PALAVRAS_BOTAFOGO = Arrays.asList("BOTAFOGO", "LOCOABREU", "ESTRELA", "FOGÃO", "GLORIOSO",
            "MANEQUINHO", "ENGENHÃO", "NILTONSANTOS", "ZAGALLO");
    
    private final List<String> palavras;
    private String palavra;
    private char[] palavraOcultaArray;
    private int tentativasRestantes;
    private final Set<Character> letrasTentadas = new HashSet<>();

    private final MutableLiveData<String> _palavraOculta = new MutableLiveData<>();
    private final MutableLiveData<Integer> _tentativas = new MutableLiveData<>();
    private final MutableLiveData<GameStatus> _status = new MutableLiveData<>();

    public ForcaViewModel(String time) {
        switch (time) {
            case "FLAMENGO":
                this.palavras = PALAVRAS_FLAMENGO;
                break;
            case "VASCO":
                this.palavras = PALAVRAS_VASCO;
                break;
            case "BOTAFOGO":
                this.palavras = PALAVRAS_BOTAFOGO;
                break;
            default:
                this.palavras = PALAVRAS_FLUMINENSE; // Fluminense como padrão
        }
        iniciarNovoJogo();
    }

    public LiveData<String> getPalavraOculta() {
        return _palavraOculta;
    }

    public LiveData<Integer> getTentativas() {
        return _tentativas;
    }

    public LiveData<GameStatus> getStatus() {
        return _status;
    }

    public void iniciarNovoJogo() {
        palavra = palavras.get(new Random().nextInt(palavras.size()));
        palavraOcultaArray = new char[palavra.length()];
        Arrays.fill(palavraOcultaArray, '_');
        tentativasRestantes = 6;
        letrasTentadas.clear();
        _status.setValue(new GameStatus("Jogo iniciado!", null));
        atualizarUI();
    }

    private void processarPalpite(char letra) {
        viewModel.processarPalpite(letra);

        // Obtém o botão correspondente à letra
        Button btn = keyboardButtons.get(letra);
        if (btn != null) {
            btn.setEnabled(false);
            btn.setBackgroundColor(Color.GRAY);
        }

        // Observa se a letra foi acertada ou errada e toca o som correspondente
        viewModel.getStatus().observe(this, status -> {
            if (status.getAcerto() != null) {
                if (status.getAcerto()) {
                    somAcerto.start(); // Som de acerto 🎵
                } else {
                    somErro.start(); // Som de erro 🔊
                }
            }
        });
    }

        boolean acertou = false;
        for (int i = 0; i < palavra.length(); i++) {
            if (palavra.charAt(i) == letra) {
                palavraOcultaArray[i] = letra;
                acertou = true;
            }
        }

        if (!acertou) {
            tentativasRestantes--;
        }

        atualizarUI();
        verificarFimDeJogo(acertou);
    }

    private void atualizarUI() {
        _palavraOculta.setValue(new String(palavraOcultaArray));
        _tentativas.setValue(tentativasRestantes);
    }

    private void verificarFimDeJogo(boolean acertou) {
        if (tentativasRestantes <= 0) {
            _status.setValue(new GameStatus("Você perdeu! A palavra era: " + palavra, false));
            _palavraOculta.setValue(palavra);
        } else if (new String(palavraOcultaArray).indexOf('_') == -1) {
            _status.setValue(new GameStatus("Parabéns! Você ganhou!", true));
        } else {
            _status.setValue(new GameStatus("Continue jogando...", acertou));
        }
    }

    public static class ForcaViewModelFactory implements ViewModelProvider.Factory {
        private final String time;

        public ForcaViewModelFactory(String time) {
            this.time = time;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ForcaViewModel.class)) {
                return (T) new ForcaViewModel(time);
            }
            throw new IllegalArgumentException("Classe ViewModel desconhecida");
        }
    }
}

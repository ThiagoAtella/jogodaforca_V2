package com.example.jogodaforcav2;

public class GameStatus{
    private final String message;
    private final Boolean acerto;

    public GameStatus(String message, Boolean acerto) {
        this.message = message;
        this.acerto = acerto;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getAcerto() {
        return acerto;
    }
}
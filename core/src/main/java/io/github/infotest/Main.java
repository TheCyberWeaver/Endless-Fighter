package io.github.infotest;

import com.badlogic.gdx.Game;

public class Main extends Game {
    private String username;
    private String playerClass;
    private String serverUrl;
    public boolean isDevelopmentMode=true;
    @Override
    public void create() {
        setScreen(new StartScreen(this)); //set to start screen
    }

    public void startGame(String username, String playerClass,String selectedServerUrl) {
        this.username = username;
        this.playerClass = playerClass;
        this.serverUrl=selectedServerUrl;
        // switch to gaming screen
        setScreen(new MainGameScreen(this));
    }

    public String getUsername() {
        return username;
    }

    public String getPlayerClass() {
        return playerClass;
    }
}


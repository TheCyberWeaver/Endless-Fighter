package io.github.infotest.item;

import io.github.infotest.util.MyAssetManager;

public class Apple extends Item {
    public String color;
    private int healthRecovery;

    public Apple(MyAssetManager assetManager, int healthRecovery) {
        super("apple","just an apple", assetManager.getLoadingScreenTexture());
        this healthRecovery = healthRecovery;
    }

    public void eat (Actor actor){
        actor.heal (healthRecovery);
    }

    public int getHealthRecovery(){
        return healthRecovery;
    }
    
    public void render(){

    }
}

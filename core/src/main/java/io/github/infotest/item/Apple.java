package io.github.infotest.item;

import io.github.infotest.util.MyAssetManager;

public class Apple extends Item {
    public String color;
    private float healthRecovery;

    public Apple(MyAssetManager assetManager, float healthRecovery) {
        super("apple","just an apple", assetManager.getLoadingScreenTexture());
        this healthRecovery = healthRecovery;
    }

    public void eat (Actor actor){
        actor.heal (healthRecovery);
    }

    public float getHealthRecovery(){
        return healthRecovery;
    }
    
    public void render(){

    }
}

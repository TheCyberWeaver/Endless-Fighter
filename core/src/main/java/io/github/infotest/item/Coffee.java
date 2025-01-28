package io.github.infotest.item;

import io.github.infotest.util.MyAssetManager;

public class Coffee extends Item {
    public String color;
    private float ausdauerRegen;

    public Coffee (MyAssetManager assetManager, float ausdauerRegen) {
        super("coffee","just a coffee", assetManager.getLoadingScreenTexture());
        this ausdauerRegen = ausdauerRegen;
        
    }

    public float getAusdauerRegen(){
        return ausdauerRegen;
    }

    public void render(){
    }
}

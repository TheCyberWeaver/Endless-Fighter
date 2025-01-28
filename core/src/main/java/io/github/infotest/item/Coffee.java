package io.github.infotest.item;

import io.github.infotest.util.MyAssetManager;

public class Coffee extends Item {
    public String color;
    private float ausdauerRecovery;

    public Coffee (MyAssetManager assetManager, float ausdauerRecovery) {
        super("coffee","just a coffee", assetManager.getLoadingScreenTexture());
        this ausdauerRecovery = ausdauerRecovery;
    }

    public float getAusdauerRecovery(){
        return ausdauerRecovery;
    }

    public void render(){
    }
}

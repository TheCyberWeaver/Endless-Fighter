package io.github.infotest.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import io.github.infotest.character.Actor;
import io.github.infotest.util.MyAssetManager;

public class Apple extends Item {
    public String color;
    private int healthRecovery;

    public Apple(String id, String name,MyAssetManager assetManager, int healthRecovery) {
        super(id,"apple","just an apple", assetManager.getLoadingScreenTexture());
        this. healthRecovery = healthRecovery;
    }

    public void eat (Actor actor){
        actor.heal (healthRecovery);
    }

    public void render(){

    }
}

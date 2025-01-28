package io.github.infotest.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import io.github.infotest.character.Actor;
import io.github.infotest.util.MyAssetManager;

public class Apple extends Item {
    public String color;
    private float healthRegen;

    public Apple(String id, String name,MyAssetManager assetManager, float healthRegen) {
        super(id,"apple","just an apple", assetManager.getItemAssets()[0]);
        this. healthRegen = healthRegen;
    }

    public void eat (Actor actor){
        actor.heal (healthRegen);
        healthRegen = 0;
    }

    public float getHealthRegen(){
        return healthRegen;
    }

    public void render(){

    }
}

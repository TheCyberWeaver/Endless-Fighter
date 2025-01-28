package io.github.infotest.item;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.infotest.character.Player;
import io.github.infotest.item.Item;
import io.github.infotest.util.GameRenderer;
import io.github.infotest.util.MyAssetManager;

public class Potion extends Item {
    public static enum TYPE{
        MANA, HEALTH, AUSDAUER
    }
    private Animation<TextureRegion> animation;
    private float animationTimer  = 0f;

    TYPE type;
    float amount;

    public Potion(TYPE type, float amount, MyAssetManager assetManager) {
        super("01", "potion","just a potion");
        this.type = type;
        this.amount = amount;

        int typ = 0;
        switch (type){
            case MANA: typ = 1; break;
            case HEALTH: typ = 2; break;
            case AUSDAUER: typ = 3; break;
        }
        Texture animationSheet = assetManager.getItemAssets()[typ];
        animation = GameRenderer.sheetsToAnimation(4, 2, animationSheet, 0.1f);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void use (Player player){
        switch (type){
            case MANA:
                player.addMana(amount);
                amount = 0f;
                break;
            case HEALTH:
                player.heal((int) amount);
                amount = 0f;
                break;
            case AUSDAUER:
                player.addAusdauer(amount);
                amount = 0f;
                break;
        }
    }

    @Override
    public void render(Batch batch, float delta, float x, float y, float scale){
        batch.draw(animation.getKeyFrame(animationTimer), x, y, 80*scale, 80*scale);
        animationTimer += delta;
    }
}

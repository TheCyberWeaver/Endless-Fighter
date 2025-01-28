package io.github.infotest.item;

import io.github.infotest.character.Player;
import io.github.infotest.item.Item;
import io.github.infotest.util.MyAssetManager;

public class Potion extends Item {
    public String color;

    public Potion(MyAssetManager assetManager) {
        super("01", "potion","just a potion", assetManager.getLoadingScreenTexture());
    }

    public void use (Player player){
        player.setMana (player.getMaxMana());
    }

    public void render(){

    }
}

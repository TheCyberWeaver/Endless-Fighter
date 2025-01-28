package io.github.infotest.item;

import io.github.infotest.character.Player;
import io.github.infotest.util.MyAssetManager;

public class Coffee extends Item {
    public String color;


    public Coffee (MyAssetManager assetManager) {
        super("01", "coffee","just a coffee", assetManager.getLoadingScreenTexture());

    }

      public void use (Player player){
        player.setAusdauer (player.getMaxAusdauer());
    }

    public void render(){
    }
}

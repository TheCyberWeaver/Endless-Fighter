age io.github.infotest.item;

import io.github.infotest.util.MyAssetManager;

public class Potion extends Item {
    public String color;

    public Potion(MyAssetManager assetManager, float manaRegen, float duration) {
        super("potion","just a potion", assetManager.getLoadingScreenTexture());
    }

    public void use (Player player){
        player.setMana (player.getMaxMana());
    }

    public void render(){

    }
}

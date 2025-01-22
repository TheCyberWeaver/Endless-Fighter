age io.github.infotest.item;

import io.github.infotest.util.MyAssetManager;

public class Potion extends Item {
    public String color;

    public Potion(MyAssetManager assetManager) {
        super("potion","just a potion", assetManager.getLoadingScreenTexture());
    }
    public void render(){

    }
}

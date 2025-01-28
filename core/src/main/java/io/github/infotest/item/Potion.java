age io.github.infotest.item;

import io.github.infotest.util.MyAssetManager;

public class Potion extends Item {
    public String color;
    private float manaRegen;
    private float duration;

    public Potion(MyAssetManager assetManager, float manaRegen, float duration) {
        super("potion","just a potion", assetManager.getLoadingScreenTexture());
        this.manaRegen = manaRegen;
        this.duration = duration;
    }

    public float getManaRegen(){
        return manaRegen;
    }

    public float getDuration(){
        return duration;
    }

    public void render(){

    }
}

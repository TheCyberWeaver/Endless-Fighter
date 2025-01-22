age io.github.infotest.item;

import io.github.infotest.util.MyAssetManager;

public class Potion extends Item {
    public String color;
    private float manaRecovery;
    private float duration;

    public Potion(MyAssetManager assetManager, float manaRecovery, float duration) {
        super("potion","just a potion", assetManager.getLoadingScreenTexture());
        this.manaRecovery = manaRecovery; 
        this.duration = duration;
    }

    public float getManaRecovery(){
        return manaRecovery;
    }

    public float getDuration(){
        return duration;
    }
    
    public void render(){

    }
}

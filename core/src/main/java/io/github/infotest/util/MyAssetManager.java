package io.github.infotest.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import com.badlogic.gdx.math.Vector2;
import org.w3c.dom.Text;

import java.util.HashMap;

public class MyAssetManager {
    public final AssetManager manager = new AssetManager();

    // Helper: loads all strings in array as Textures
    private void loadTextures(String[] paths) {
        for (String path : paths) {
            manager.load(path, Texture.class);
        }
    }
    // Helper: gets an array of Textures for the given array of paths
    private Texture[] getTextures(String[] paths) {
        Texture[] result = new Texture[paths.length];
        for (int i = 0; i < paths.length; i++) {
            result[i] = manager.get(paths[i], Texture.class);
        }
        return result;
    // -----------
    // LOAD all methods
    // -----------
    public void loadAllMainGameAssets() {
        loadLoadingScreen();

        loadMapAssets();
        loadMapFadeAssets();
        loadMapDecoAssets();
        loadMapTreeAssets();
        loadMapWaterAssets();

        loadPlayerAssets();

        loadMageAssets();
        loadFireballAssets();
        loadFireballSymbol();
        loadBlackHoleAssets();

        loadHealthBarAssets();
        loadManaBarAssets();
        loadAusdauerBarAssets();
        loadSkillBarAsset();
        loadGoldBarAsset();




        loadNPCMaleAssets();
        loadNPCWomenAssets();
        loadNPCMarketAssets();

        loadGegnerAsset();

        loadSignsAssets();

        loadArrowAssets();

        loadItemBarAssets();
        loadItemAssets();

        loadAnimationObjectAssets();

        loadMainGameMusicAssets();
        loadStartGameMusicAssets();
    }


    // -----------
    // LOAD methods
    // -----------
    public void loadLoadingScreen() {
        manager.load(AssetPaths.LOADING_SCREEN, Texture.class);
    }

    public void loadMapAssets() {
        loadTextures(AssetPaths.MAP_ASSETS);
    }

    public void loadMapWaterAssets(){
        loadTextures(AssetPaths.MAP_WATER);
    }
    public void loadMapFadeAssets() {
        loadTextures(AssetPaths.MAP_FADE_ASSETS);
    }

    public void loadMapDecoAssets() {
        loadTextures(AssetPaths.MAP_DECO_ASSETS);
    }

    public void loadMapTreeAssets() {
        loadTextures(AssetPaths.MAP_TREE_ASSETS);
    }

    public void loadSkillBarAsset() {
        manager.load(AssetPaths.SKILL_BAR, Texture.class);
    }

    public void loadGoldBarAsset() {
        manager.load(AssetPaths.GOLD_BAR, Texture.class);
    }

    public void loadPlayerAssets() {
        manager.load(AssetPaths.PLAYER_ASSASSIN, Texture.class);
    }

    public void loadMageAssets() {
        loadTextures(AssetPaths.MAGE_ASSETS);
    }

    public void loadFireballAssets() {
        loadTextures(AssetPaths.FIREBALL_ASSETS);
    }

    public void loadFireballSymbol() {
        manager.load(AssetPaths.FIREBALL_SYMBOL, Texture.class);
    }

    public void loadBlackHoleAssets(){
        loadTextures(AssetPaths.BLACKHOLE_ASSETS);
    }

    public void loadHealthBarAssets() {
        loadTextures(AssetPaths.HEALTH_BAR);
    }

    public void loadManaBarAssets() {
        loadTextures(AssetPaths.MANA_BAR);
    }

    public void loadAusdauerBarAssets() {
        loadTextures(AssetPaths.AUSDAUER_BAR);
    }

    public void loadNPCMaleAssets() {
        loadTextures(AssetPaths.NPC_MALE);
    }

    public void loadNPCWomenAssets() {
        loadTextures(AssetPaths.NPC_WOMAN);
    }

    public void loadNPCMarketAssets() {
        loadTextures(AssetPaths.NPC_MARKET);
    }

    public void loadGegnerAsset() {
        manager.load(AssetPaths.GEGNER, Texture.class);
    }

    public void loadSignsAssets() {
        manager.load(AssetPaths.SIGN_INV_FULL, Texture.class);
    }

    public void loadArrowAssets() {
        manager.load(AssetPaths.ARROW, Texture.class);
    }

    public void loadItemBarAssets() {
        manager.load(AssetPaths.ITEM_BAR, Texture.class);
    }

    public void loadItemAssets() {
        loadTextures(AssetPaths.ITEMS);
    }

    private HashMap<Texture, Vector2> columnsRows = new HashMap<Texture, Vector2>();
    public void loadAnimationObjectAssets(){
        loadTextures(AssetPaths.ANIMATION_OBJECTS);
    }
    public void initAnimationObjectColumnsRows(){
        // x = columns; y = rows
        for(String path:AssetPaths.ANIMATION_OBJECTS){
            Vector2 vec=new Vector2(8,1);
            if(path.equals(AssetPaths.ANIMATION_OBJECTS[2])){
                vec=new Vector2(39,1);
            }
            columnsRows.put(manager.get(path),vec);
        }
    }
    public Vector2 getColumnsRows(Texture key){
        return columnsRows.get(key);
    }
    public Texture[] getAnimationObjectAssets(){
        Texture[] textures=new Texture[4];
        textures[0]=manager.get("animationObjects/decoFountain.png",Texture.class);
        textures[1]=manager.get("animationObjects/goldTrophy.png",Texture.class);
        textures[2]=manager.get("animationObjects/summoningChamber.png",Texture.class);
        textures[3]=manager.get("animationObjects/winTrophy.png",Texture.class);
        return textures;
    }



    // Music / Sound
    public void loadStartGameMusicAssets() {
        manager.load(AssetPaths.START_GAME_MUSIC, Music.class);
    }

    public void loadMainGameMusicAssets() {
        manager.load(AssetPaths.MAIN_GAME_MUSIC, Music.class);
        manager.load(AssetPaths.DEATH_SOUND,      Sound.class);
        manager.load(AssetPaths.FIREBALL_SOUND,   Sound.class);
        manager.load(AssetPaths.COME_BACK_SOUND,  Sound.class);
        manager.load(AssetPaths.NORMAL_ATTACK,    Sound.class);
        manager.load(AssetPaths.RUNNING_SOUND,    Music.class);
        manager.load(AssetPaths.TAUNT_SOUND,      Sound.class);
    }

    // -----------
    // GET methods
    // -----------
    public Texture getLoadingScreenTexture() {
        return manager.get(AssetPaths.LOADING_SCREEN, Texture.class);
    }

    public Texture[] getMapAssets() {
        return getTextures(AssetPaths.MAP_ASSETS);
    }

    public Texture[] getMapFadeAssets() {
        return getTextures(AssetPaths.MAP_FADE_ASSETS);
    }

    public Texture[] getMapDecoAssets() {
        return getTextures(AssetPaths.MAP_DECO_ASSETS);
    }

    public Texture[] getMapTreeAssets() {
        return getTextures(AssetPaths.MAP_TREE_ASSETS);
    }

    public Texture[] getMapWaterAssets(){
        return getTextures(AssetPaths.MAP_WATER);
    }


        public Texture getSkillBarAsset() {
        return manager.get(AssetPaths.SKILL_BAR, Texture.class);
    }

    public Texture getGoldBarAsset() {
        return manager.get(AssetPaths.GOLD_BAR, Texture.class);
    }

    public Texture getPlayerAssets() {
        return manager.get(AssetPaths.PLAYER_ASSASSIN, Texture.class);
    }

    public Texture[] getMageAssets() {
        return getTextures(AssetPaths.MAGE_ASSETS);
    }

    public Texture[] getFireballAssets() {
        return getTextures(AssetPaths.FIREBALL_ASSETS);
    }

    public Texture[] getBlackHoleAssets() {return getTextures(AssetPaths.BLACKHOLE_ASSETS);}

    public Texture getFireballSymbol() {
        return manager.get(AssetPaths.FIREBALL_SYMBOL, Texture.class);
    }

    public Texture[] getHealthBarAssets() {
        return getTextures(AssetPaths.HEALTH_BAR);
    }

    public Texture[] getManaBarAssets() {
        return getTextures(AssetPaths.MANA_BAR);
    }

    public Texture[] getAusdauerBarAssets() {
        return getTextures(AssetPaths.AUSDAUER_BAR);
    }

    public Texture[] getNPCMaleAssets() {
        return getTextures(AssetPaths.NPC_MALE);
    }

    public Texture[] getNPCWomenAssets() {
        return getTextures(AssetPaths.NPC_WOMAN);
    }
    public Texture[] getNPCMarketAssets() {
        return getTextures(AssetPaths.NPC_MARKET);
    }

    public Texture getGegnerAsset() {
        return manager.get(AssetPaths.GEGNER, Texture.class);
    }

    public Texture getSignsAssets() {
        return manager.get(AssetPaths.SIGN_INV_FULL, Texture.class);
    }

    public Texture getArrowAssets() {
        return manager.get(AssetPaths.ARROW, Texture.class);
    }

    public Texture getItemBarAssets() {
        return manager.get(AssetPaths.ITEM_BAR, Texture.class);
    }

    public Texture[] getItemAssets() {
        return getTextures(AssetPaths.ITEMS);
    }

    // Music / Sound
    public Music getStartGameMusicAssets() {
        return manager.get(AssetPaths.START_GAME_MUSIC, Music.class);
    }
    public Music getMainGameBackgroundMusic() {
        return manager.get(AssetPaths.MAIN_GAME_MUSIC, Music.class);
    }
    public Sound getDeathSound() {
        return manager.get(AssetPaths.DEATH_SOUND, Sound.class);
    }
    public Sound getCastFireballSound() {
        return manager.get(AssetPaths.FIREBALL_SOUND, Sound.class);
    }
    public Sound getComeBackSound() {
        return manager.get(AssetPaths.COME_BACK_SOUND, Sound.class);
    }
    public Sound getNormalAttackSound() {
        return manager.get(AssetPaths.NORMAL_ATTACK, Sound.class);
    }
    public Music getRunningSound() {
        return manager.get(AssetPaths.RUNNING_SOUND, Music.class);
    }
    public Sound getTauntSound() {
        return manager.get(AssetPaths.TAUNT_SOUND, Sound.class);
    }
}

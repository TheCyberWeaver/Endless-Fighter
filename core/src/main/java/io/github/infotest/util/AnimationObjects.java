package io.github.infotest.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.infotest.MainGameScreen;

import java.util.Arrays;

public class AnimationObjects {
    private Animation<TextureRegion> animation;
    private float animationTimer;
    private boolean destoryed = false;

    private Vector2 position;

    private AnimationObjects(Texture animation_sheet, int columns, int rows, float frameDuration, Animation.PlayMode playMode, Vector2 position) {
        animation = GameRenderer.sheetsToAnimation(columns, rows, animation_sheet, frameDuration);
        animation.setPlayMode(playMode);
        animationTimer = 0;

        this.position = position;
    }

    public void render(Batch batch, float delta) {
        if (!destoryed) {
            batch.draw(animation.getKeyFrame(animationTimer), position.x, position.y);
            animationTimer += delta;
            removeDecoAndTrees(position.x, position.y, animation.getKeyFrame(animationTimer).getRegionWidth(), animation.getKeyFrame(animationTimer).getRegionHeight());
        }
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }
    public float getAnimationTimer() {
        return animationTimer;
    }
    public void triggerAnimation() {
        animationTimer = 0f;
    }
    public Vector2 getPosition() {
        return position;
    }

    public static AnimationObjects createDecoFountain(float x, float y, MyAssetManager assetManager) {
        Texture animationSheet = assetManager.getAnimationObjectAssets()[0];
        Vector2 columnsRows = assetManager.getColumnsRows(animationSheet);
        AnimationObjects anim = new AnimationObjects(animationSheet, (int) columnsRows.x, (int) columnsRows.y,
            0.15f, Animation.PlayMode.LOOP,
            new Vector2(x, y));
        removeDecoAndTrees(x, y, animationSheet.getWidth()/columnsRows.x, animationSheet.getHeight()/columnsRows.y);
        return anim;
    }
    public static AnimationObjects createGoldTrophy(float x, float y, MyAssetManager assetManager) {
        Texture animationSheet = assetManager.getAnimationObjectAssets()[1];
        Vector2 columnsRows = assetManager.getColumnsRows(animationSheet);
        AnimationObjects anim =new AnimationObjects(animationSheet, (int) columnsRows.x, (int) columnsRows.y,
            0.1f, Animation.PlayMode.LOOP,
            new Vector2(x, y));
        removeDecoAndTrees(x, y, animationSheet.getWidth()/columnsRows.x, animationSheet.getHeight()/columnsRows.y);
        return anim;
    }
    public static AnimationObjects createSummoningChamber(float x, float y, MyAssetManager assetManager) {
        Texture animationSheet = assetManager.getAnimationObjectAssets()[2];
        Vector2 columnsRows = assetManager.getColumnsRows(animationSheet);
        AnimationObjects anim = new AnimationObjects(animationSheet, (int) columnsRows.x, (int) columnsRows.y,
            0.1f, Animation.PlayMode.NORMAL,
            new Vector2(x, y));
        removeDecoAndTrees(x, y, animationSheet.getWidth()/columnsRows.x, animationSheet.getHeight()/columnsRows.y);
        return anim;
    }
    public static AnimationObjects createWinTrophy(float x, float y, MyAssetManager assetManager) {
        Texture animationSheet = assetManager.getAnimationObjectAssets()[3];
        Vector2 columnsRows = assetManager.getColumnsRows(animationSheet);
        AnimationObjects anim = new AnimationObjects(animationSheet, (int) columnsRows.x, (int) columnsRows.y,
            0.15f, Animation.PlayMode.LOOP,
            new Vector2(x, y));
        removeDecoAndTrees(x, y, animationSheet.getWidth()/columnsRows.x, animationSheet.getHeight()/columnsRows.y);
        return anim;
    }

    public AnimationObjects destroy() {
        destoryed = true;
        restoreDecoAndTrees(position.x, position.y, animation.getKeyFrame(animationTimer).getRegionWidth(), animation.getKeyFrame(animationTimer).getRegionHeight());
        return this;
    }

    /**
     * Removes the deco out of the DECO_MAP in the Intervall [y -> y+a][x -> x+a]
     * @param xS X coordinate of the starting point  (in pixels)
     * @param yS Y coordinate of the starting point  (in pixels)
     * @param aX amount of pixels going right        (in pixels)
     * @param aY amount of pixels going up           (in pixels)
     */
    private static void removeDecoAndTrees(float xS, float yS, float aX, float aY) {
        int xC = (int) (xS/MainGameScreen.CELL_SIZE);
        int yC = (int) (yS/MainGameScreen.CELL_SIZE);
        int xC2 = (int) ((xS+aX)/MainGameScreen.CELL_SIZE);
        int yC2 = (int) ((yS+aY)/MainGameScreen.CELL_SIZE);

        for(int y=yC; y<yC2; y++) {
            for (int x=xC; x<xC2; x++) {
                MainGameScreen.DECO_MAP[y][x] = 0;
                if (MainGameScreen.GAME_MAP[y][x] == 2){
                    MainGameScreen.GAME_MAP[y][x] = 0;
                }
                if (MainGameScreen.GAME_MAP[y][x] == 3){
                    MainGameScreen.GAME_MAP[y][x] = 4;
                }
            }
        }
    }
    /**
     * Restores the deco of the DECO_MAP from DECO_MAP_BACKUP in the Intervall [y -> y+a][x -> x+a]
     * @param xS X coordinate of the starting point  (in pixels)
     * @param yS Y coordinate of the starting point  (in pixels)
     * @param aX amount of pixels going right        (in pixels)
     * @param aY amount of pixels going up           (in pixels)
     */
    private static void restoreDecoAndTrees(float xS, float yS, float aX, float aY) {
        int xC = (int) (xS/MainGameScreen.CELL_SIZE);
        int yC = (int) (yS/MainGameScreen.CELL_SIZE);
        int xC2 = (int) ((xS+aX)/MainGameScreen.CELL_SIZE);
        int yC2 = (int) ((yS+aY)/MainGameScreen.CELL_SIZE);

        for(int y=yC; y<yC2; y++) {
            for (int x=xC; x<xC2; x++) {
                MainGameScreen.DECO_MAP[y][x] = MainGameScreen.DECO_MAP_BACKUP[y][x];
                MainGameScreen.GAME_MAP[y][x] = MainGameScreen.GAME_MAP_BACKUP[y][x];
            }
        }
    }
}

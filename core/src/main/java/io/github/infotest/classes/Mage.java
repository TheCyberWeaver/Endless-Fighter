package io.github.infotest.classes;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import io.github.infotest.MainGameScreen;
import io.github.infotest.character.Player;
import io.github.infotest.util.GameRenderer;
import io.github.infotest.util.Logger;
import io.github.infotest.util.MyAssetManager;
import io.github.infotest.util.ServerConnection;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.github.infotest.MainGameScreen.allPlayers;

public class Mage extends Player {

    public final Animation<TextureRegion> ATTACK_1;
    public final Animation<TextureRegion> DEATH;
    public final Animation<TextureRegion> HIT;
    public final Animation<TextureRegion> IDLE;
    public final Animation<TextureRegion> RUN;

    protected Animation<TextureRegion> STATE;

    private MyAssetManager assetManager;

    private float fireballCost = 5f;
    private float fireballDamage = 16f;
    private float fireballCooldown;
    private float fireballSpeed = 20f;
    private float fireballScale = 3f;
    private float fireballLT = 2f; // lifetime with 0.5 second on start and 0.7 s on hit and 0.8 on end without hit

    private float flameThrowerCost = 20f;
    private float flameThrowerBaseDamage = 2f; // damage every sec
    private float flameThrowerCooldown = 15f;
    private float flameThrowerScaleWidth = 4f;
    private float flameThrowerScaleHeight = 1f;
    private float flameThrowerLT = 10f;

    private float blackHoleCost = 30f;
    private float blackHoleDamage = 2f;
    private float blackHoleCooldown = 1f; //25
    private float blackHoleScale = 1f;
    private float blackHoleLT = 4f; // lifetime with 0.5 second on start and 0.7 s on hit and 0.8 on end without hit

    Sound castFireballSound;

    public Mage(String id, String name, Vector2 playerPosition, MyAssetManager assetManager) {
        super(id, name, "Mage",60, 125, 50, playerPosition, 200);
        Texture[] animationSheets = assetManager.getMageAssets();
        ATTACK_1 = GameRenderer.sheetsToAnimation(8, 1, animationSheets[0], 0.1f);
        DEATH = GameRenderer.sheetsToAnimation(7, 1, animationSheets[1], 0.1f);
        HIT = GameRenderer.sheetsToAnimation(4, 1, animationSheets[2], 0.1f);
        IDLE = GameRenderer.sheetsToAnimation(6, 1, animationSheets[3], 0.1f);
        IDLE.setPlayMode(Animation.PlayMode.LOOP);
        RUN = GameRenderer.sheetsToAnimation(8, 1, animationSheets[4], 0.1f);
        RUN.setPlayMode(Animation.PlayMode.LOOP);

        fireballCooldown = ATTACK_1.getAnimationDuration()+0.5f;

        STATE = IDLE;

        this.assetManager=assetManager;

        castFireballSound= assetManager.getCastFireballSound();

        this.T1Cost = fireballCost;
        this.T1Damage = fireballDamage;
        this.T1Cooldown = fireballCooldown;
        this.T1Speed = fireballSpeed;
        this.T1Scale = fireballScale;
        this.T1LT =fireballLT;

        this.T3Cost = flameThrowerCost;
        this.T3Damage = flameThrowerBaseDamage;
        this.T3Cooldown = flameThrowerCooldown;
        this.T3ScaleWidth = flameThrowerScaleWidth;
        this.T3ScaleHeight = flameThrowerScaleHeight;
        this.T3LT =flameThrowerLT;

        this.T4Cost = blackHoleCost;
        this.T4Damage = blackHoleDamage;
        this.T4Cooldown = blackHoleCooldown;
        this.T4Scale = blackHoleScale;
        this.T4LT = blackHoleLT;
    }
    public long soundID=0;
    @Override
    public void castSkill(int skillID,ServerConnection serverConnection) {
        Player localPlayer=allPlayers.get(serverConnection.getMySocketId());
        switch(skillID) {
            case 1:
                if(timeSinceLastT1Skill >= fireballCooldown && drainMana(fireballCost) ||  localPlayer!=this) {
                    //castFireballSound.stop(soundID);
                    soundID =castFireballSound.play();

                    Logger.log("[Mage INFO]: Player ["+this.getName()+"] casts skill "+skillID);
                    timeSinceLastT1Skill = 0;

                    int xOffset = 0;
                    if (rotation.angleDeg() == 180) {
                        xOffset = -36;
                    } else {
                        xOffset = 47;
                    }
                    this.isAttacking = true;

                    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                    int finalXOffset = xOffset;
                    scheduler.schedule(() -> {
                        castFireball(this.position.x + finalXOffset, this.position.y + 46, rotation);
                        scheduler.shutdown(); // Scheduler nach Ausführung beenden
                    }, 400, TimeUnit.MILLISECONDS);

                    if(localPlayer==this){
                        serverConnection.sendCastSkill(this, "Fireball", new Vector2());
                    }
                }
                break;
            case 2: break;
            case 3:
                if (timeSinceLastT3Skill >= flameThrowerCooldown && mana >= T3Cost || localPlayer!=this) {
                    mana -= T3Cost;
                    Logger.log("[Mage INFO]: Player [" + this.getName() + "] casts skill " + skillID);
                    timeSinceLastT3Skill = 0;
                    MainGameScreen.isRenderingFlameThrower = true;
                    isAttacking3 = true;
                }
                break;
            case 4:
                if(timeSinceLastT4Skill >= blackHoleCooldown && mana >= T4Cost ||  localPlayer!=this) {
                    Logger.log("[Mage INFO]: Player [" + this.getName() + "] casts skill " + skillID);
                    timeSinceLastT4Skill = 0;
                    this.isAttacking4 = true;
                }
                break;
            default: break;
        }
    }

    @Override
    public Texture getMainSkillSymbol() {
        return assetManager.getFireballSymbol();
    }


    public void castFireball(float x, float y, Vector2 playerRot) {
        playerRot.nor();
        float velocityX = 1.5f * playerRot.x;
        float velocityY = 1.5f * playerRot.y;
        GameRenderer.fireball(x, y, velocityX, velocityY, playerRot, fireballScale, fireballDamage, fireballSpeed, fireballLT, this);
    }

    @Override
    public void render(Batch batch, float delta) {
        super.render(batch, delta);
        Vector2 predictedPosition = predictPosition();
        Animation<TextureRegion> oldState = STATE;
        if(isAttacking) {
            STATE = ATTACK_1;
        } else if(isHit){
            STATE = HIT;
        } else if(hasMoved){
            STATE = RUN;
        } else if(!isAlive){
            STATE = DEATH;
        } else {
            STATE = IDLE;
        }
        if(STATE!=oldState){
            animationTime = 0;
        }
        if(animationTime>=STATE.getAnimationDuration()){
            isAttacking = false;
            isHit = false;
        }
        Sprite currentFrame = new Sprite(STATE.getKeyFrame(animationTime));

        if (rotation.angleDeg() == 180) {
            currentFrame.flip(true, false);
        }

        currentFrame.setPosition(position.x-currentFrame.getWidth()/2f, position.y-currentFrame.getHeight()/2f);
        currentFrame.setOrigin(currentFrame.getWidth()/2, currentFrame.getHeight()/2);
        currentFrame.setScale(0.75f);
        currentFrame.draw(batch);

        MainGameScreen.isRenderingBlackHoleActivation = isAttacking4;

        animationTime += delta;
    }
    public String toString() {
        return "Mage: "+name;
    }
}

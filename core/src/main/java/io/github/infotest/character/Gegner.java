package io.github.infotest.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.infotest.MainGameScreen;
import io.github.infotest.util.GameRenderer;
import io.github.infotest.util.Logger;
import io.github.infotest.util.ServerConnection;

import static io.github.infotest.MainGameScreen.*;
import static io.github.infotest.util.Overlay.UI_Layer.whitePixel;

public class Gegner extends  Actor{

    public enum SPEZIES{
        GOBLIN, SKELETON, MUSHROOM, FLYING_EYE
    }

    public enum STATE{
        ATTACK, DEATH, HIT, IDLE, RUN
    }

    private boolean isAttacking = false;
    private boolean isMoving = false;
    private boolean isHit = false;

    private boolean isStuned = false;
    private float stunTimer = 0;

    private Animation<TextureRegion>[] animations;

    public SPEZIES spezies;
    public STATE state;
    private Animation<TextureRegion> anim;
    private float animTimer;

    private float killXP;
    private float killGold;
    private Player player;
    private float attackRange = 20; //Beispielzahl
    private float damage = 10f;
    private float attackCooldown = 2; // Beispielzahl Zeit zw Angriffen
    private float lastAttackTime = 0; //letzter Angriff
    private boolean isWaiting = false;
    private ServerConnection serverConnection;

    public Gegner(String id, SPEZIES spezies, Texture[] AnimationSheets, int maxHealthPoints, Vector2 initialPosition, float speed, float exp, float gold, ServerConnection serverConnection) {
        super(maxHealthPoints, initialPosition, speed, null);
        this.id = id;
        this.killXP = exp;
        this.killGold = gold;
        this.serverConnection = serverConnection;
        Player p = getClosestPlayer();
        if (p.getPosition().dst(position) <= 100f){
            this.player = p;
        }
        this.spezies = spezies;
        this.state = STATE.IDLE;
        switch (spezies){
            case GOBLIN:
                animations = new Animation[5];
                animations[0] = GameRenderer.sheetsToAnimation(8, 1, AnimationSheets[0], 0.1f);
                animations[0].setPlayMode(Animation.PlayMode.NORMAL);
                animations[1] = GameRenderer.sheetsToAnimation(4, 1, AnimationSheets[1], 0.4f);
                animations[1].setPlayMode(Animation.PlayMode.NORMAL);
                animations[2] = GameRenderer.sheetsToAnimation(4, 1, AnimationSheets[2], 0.4f);
                animations[2].setPlayMode(Animation.PlayMode.NORMAL);
                animations[3] = GameRenderer.sheetsToAnimation(4, 1, AnimationSheets[3], 0.1f);
                animations[3].setPlayMode(Animation.PlayMode.LOOP);
                animations[4] = GameRenderer.sheetsToAnimation(8, 1, AnimationSheets[4], 0.15f);
                animations[4].setPlayMode(Animation.PlayMode.LOOP);
                break;
            case SKELETON: break;
            case MUSHROOM: break;
            case FLYING_EYE: break;
        }
        anim = animations[3];
    }

    @Override
    public void render(Batch batch, float delta) {
        updateState();

        Vector2 predictedPosition = predictPosition();
            float texWidth = anim.getKeyFrame(animTimer, true).getRegionWidth();
            float texHeight = anim.getKeyFrame(animTimer, true).getRegionHeight();
            float drawX = predictedPosition.x - texWidth / 2f;
            float drawY = predictedPosition.y - texHeight / 2f;


            // 2) Draw the HP bar above the sprite
            //    Let's define a bar width/height in pixels:
            float barWidth = 50f;  // total width of the HP bar
            float barHeight = 5f;  // height of the HP bar
            float xCenterOffset = (texWidth - barWidth) / 2f; // center the bar horizontally over the sprite
            float barX = drawX + xCenterOffset;
            float barY = drawY + texHeight + 5f;  // 5px above the top of the sprite

            //    How much of the bar is filled?
            float hpRatio = healthPoints / maxHealthPoints;
            if (hpRatio < 0f) hpRatio = 0f;
            if (hpRatio > 1f) hpRatio = 1f;
            float filledWidth = barWidth * hpRatio;

            //    Draw background (black)
            batch.setColor(0, 0, 0, 1); // black
            batch.draw(whitePixel, barX, barY, barWidth, barHeight);

            //    Draw fill (green)
            batch.setColor(1, 0, 0, 1); // green
            batch.draw(whitePixel, barX, barY, filledWidth, barHeight);

            //    Reset the color to white for other draws
            batch.setColor(1, 1, 1, 1);

        if (player != null && player.getHealthPoints() <= 0){
            player = getClosestPlayer();
        }

        switch (state){
            case ATTACK: anim = animations[0]; break;
            case DEATH: anim = animations[1]; break;
            case HIT: anim = animations[2]; break;
            case IDLE: anim = animations[3]; break;
            case RUN: anim = animations[4]; break;
        }

//        if (isAlive) Logger.log("state "+state);
        batch.draw(anim.getKeyFrame(animTimer, true), position.x, position.y);

        if (state == STATE.HIT && animTimer >= anim.getAnimationDuration()) {
            isHit = false;
        }
        if (state == STATE.ATTACK && animTimer >= anim.getAnimationDuration()) {
            isAttacking = false;
        }
        animTimer += delta;
        if (isStuned) stunTimer += delta;
        if (isStuned && stunTimer >= 2f) {
            isStuned = false;
            stunTimer = 0f;
        }
    }

    public void update(float delta) {
        if (isStuned) Logger.log("isStuned is True; Timer "+stunTimer);
        if (player == null) player = getClosestPlayer();

        if (isAlive){
            if (lastAttackTime >= attackCooldown){
                isWaiting = false;
            }
            for (Player player : allPlayers.values()){
                float distanceToPlayer = position.dst(player.getPosition());
                if (distanceToPlayer < attackRange){
                    if (!isWaiting){
                        isAttacking = true;
                        if (animTimer >= anim.getFrameDuration() * 7f && state == STATE.ATTACK){
                            isWaiting = true;
                            isMoving = false;
                            lastAttackTime = 0;
                            attack(this.player, serverConnection);
                        }
                    }
                } else if (!isStuned){
                    isMoving = true;
                    Vector2 direction = player.getPosition().cpy().sub(position).nor();
                    position.add(direction.scl (speed * delta));
                }
            }
            if (isWaiting){
                lastAttackTime += delta;
            }
        }
    }

    private void updateState(){
        STATE oldState = state;
        if (!this.isAlive) {
            state = STATE.DEATH;
        } else if (this.isHit) {
            state = STATE.HIT;
        } else if (this.isStuned) {
            state = STATE.IDLE;
        } else if (this.isAttacking) {
            state = STATE.ATTACK;
        } else if (this.isMoving) {
            state = STATE.RUN;
        } else {
            state = STATE.IDLE;
        }
        if (!oldState.equals(state)) animTimer = 0f;
    }

    public void takeDamage(Player player, float damage, ServerConnection serverConnection) {
        this.isHit = true;
        this.isAttacking = false;
        this.isMoving = false;
        this.isStuned = true;
        Logger.log("stuned");
        stunTimer = 0f;
        healthPoints -= Math.max(healthPoints-damage, 0);
        serverConnection.sendAttackGegner(this, damage);
        if (healthPoints <= 0){
            kill(player, serverConnection);
        }
    }

    public void kill(Player killedBy, ServerConnection serverConnection){
        this.isAlive = false;
        killedBy.addGold(killGold);
        killedBy.gainExperience(killXP);
        allGegner.remove(this);
    }

    private Player getClosestPlayer(){
        Player closestPlayer = null;
        float distance = Float.MAX_VALUE;
        for (Player player : allPlayers.values()){
            float dst = position.dst(player.getPosition());
            if (dst < distance){
                distance = dst;
                closestPlayer = player;
            }
        }
        return closestPlayer;
    }

    public void attack (Player player, ServerConnection serverConnection){
        player.takeDamage(damage, serverConnection);
    }

    public void updateHPFromGegnerData(float hp){
        this.healthPoints=hp;
    }


    @Override
    public String toString() {
        return id+"-"+name+" "+ position.x+" "+position.y+", "+targetPosition.x+" "+targetPosition.y;
    }
}



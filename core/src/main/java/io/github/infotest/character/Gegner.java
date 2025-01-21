package io.github.infotest.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import static io.github.infotest.MainGameScreen.*;
public class Gegner extends  Actor{

    private final float killXP;
    public String id;
    public Gegner(int maxHealthPoints, Vector2 initialPosition, float speed, Texture texture, float exp) {
        super(maxHealthPoints, initialPosition, speed, texture);
        this.killXP = exp;
    }
    public Gegner(String id,int maxHealthPoints, Vector2 initialPosition, float speed, Texture texture, float exp) {

        super(maxHealthPoints, initialPosition, speed, texture);
        this.id=id;
        this.killXP = exp;
    }

    @Override
    public void render(Batch batch, float delta) {
        Vector2 predictedPosition = predictPosition();
        if (texture != null) {
            batch.draw(texture, predictedPosition.x, predictedPosition.y);
        }
    }

    public void getKilled(Player p){
        p.gainExperience(killXP);
    }


    public Player findPlayer(HashMap allPlayers){
//        if (allPlayers == null){
//            return null;
//        } else{
//            distance = position.dst (player.getPosition());
//        }
        Player closestPlayer = null;
        float shortestDistance = Float.MAX_VALUE;
        for (int i = 0; i < allPlayers.length; i++){
             Player player = allPlayers.get (i);
            float distance = position.dst (player.getPosition());

        if (distance < shortestDistance){
            shortestDistance = distance;
            closestPlayer = player;
        }
        }
        return closestPlayer;
    }

    public void moveTowards (Vector2 playerPosition, float delta) {
        Vector2 direction = playerPosition.sub (position);
        direction = direction.nor();
        Vector2 movement = direction.scl (20);
        position.add (movement);
    }

    @Override
    public void update(float delta) {
        Player closestPlayer = findPlayer(allPlayers);
        float distance = position.dst (closestPlayer) ;
        if (distance <= attackRange) {
            performAttack(playerPosition);
        } else {
            moveTowards (closestPlayer, delta);
    }
    }


    public abstract void performAttack(Player player);

    public void performAttack() {

    }
    @Override
    public String toString() {
        return "";
    }
}



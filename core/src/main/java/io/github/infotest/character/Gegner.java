package io.github.infotest.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import io.github.infotest.util.DataObjects.GegnerData;

import static io.github.infotest.MainGameScreen.*;
public class Gegner extends  Actor{

    public int type;

    private float killXP;
    private float killGold;


    public Gegner(String id,int maxHealthPoints, Vector2 initialPosition, float speed, Texture texture, float exp, float gold) {

        super(maxHealthPoints, initialPosition, speed, texture);
        this.id=id;
        this.killXP = exp;
        this.killGold = gold;
    }

    @Override
    public void render(Batch batch, float delta) {
        Vector2 predictedPosition = predictPosition();
        if (texture != null) {
            batch.draw(texture, predictedPosition.x-texture.getWidth()/2f, predictedPosition.y-texture.getHeight()/2f);
        }
    }

    public void getKilled(Player p){
        p.gainExperience(killXP);
    }


    @Override
    public void update(float delta) {
        return;
    }
    public void updateHPFromGegnerData(float hp){
        this.healthPoints=hp;
    }


    @Override
    public String toString() {
        return id+"-"+name+" "+ position.x+" "+position.y+" "+targetPosition.x+" "+targetPosition.y;
    }
}



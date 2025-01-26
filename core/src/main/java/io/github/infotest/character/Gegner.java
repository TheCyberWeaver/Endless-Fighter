package io.github.infotest.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import io.github.infotest.util.DataObjects.GegnerData;
import io.github.infotest.util.ServerConnection;

import static io.github.infotest.MainGameScreen.*;
import static io.github.infotest.util.Overlay.UI_Layer.whitePixel;

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
        super.render(batch, delta);
        Vector2 predictedPosition = predictPosition();
        if (texture != null) {
            float texWidth = texture.getWidth();
            float texHeight = texture.getHeight();
            float drawX = predictedPosition.x - texWidth / 2f;
            float drawY = predictedPosition.y - texHeight / 2f;

            // 1) Draw the Gegner sprite
            batch.draw(texture, drawX, drawY);

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
        }
    }


    @Override
    public void takeDamage(float damage, ServerConnection serverConnection) {
        super.takeDamage(damage);
        serverConnection.sendAttackGegner(this,damage);
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



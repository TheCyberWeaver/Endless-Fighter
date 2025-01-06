package io.github.infotest.character;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import io.github.infotest.item.Item;
import io.github.infotest.util.ItemFactory;
import io.github.infotest.util.ServerConnection;

import java.util.ArrayList;

public abstract class Player extends Actor{

    // basic things
    public String id;
    protected String name;
    protected String className;
    protected int level;
    protected float experience;
    protected ArrayList<Item> items;

    protected boolean devMode;

    protected float mana;
    protected float maxMana;
    protected float manaRegen = 2f;

    protected float ausdauer;
    protected float maxAusdauer;
    protected float ausdauerRegen = 3f;
    protected float ausdauerCost = 10f; //Ausdauer kosten pro Sekunde
    protected boolean isSprinting;
    protected float sprintingSpeed = speed*7/4;
    protected float normalSpeed;

    protected Vector2 spawnpoint;
    protected Vector2 lastDeathPos;

    protected float timeSinceLastT1Skill;


    public Player(String id, String name, String className, int maxHealthPoints, int maxMana, int maxAusdauer, Vector2 initialPosition, float speed, Texture t) {
        super(maxHealthPoints,initialPosition,speed, t);
        this.id = id;
        this.name = name;
        this.className = className;
        this.level = 1;
        this.experience = 0;
        items=new ArrayList<>();
        // use custom font
        // FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        // FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        // parameter.size = 16;
        // this.font = generator.generateFont(parameter);
        // generator.dispose();

        this.maxMana = maxMana;
        this.mana = maxMana;

        this.maxAusdauer = maxAusdauer;
        this.ausdauer = maxAusdauer;

        this.isSprinting = false;
        this.normalSpeed = speed;

        this.spawnpoint = initialPosition;

        this.timeSinceLastT1Skill = 0;

    }

    /// game logic
    @Override
    public void render(Batch batch) {
        Vector2 predictedPosition = predictPosition();
        if (texture != null) {
            batch.draw(texture, predictedPosition .x, predictedPosition .y,32,32);
        }
        //System.out.println(name);
        //calculate name width

        GlyphLayout layout = new GlyphLayout(font, name);
        float textWidth = layout.width;

        font.draw(batch, name, predictedPosition.x + 16 - (int)textWidth/2f, predictedPosition.y + 40);
    }

    @Override
    public void update(float delta){

//        if (mana < maxMana) {
//            mana += manaRegen * delta;
//            if (mana > maxMana) {
//                mana = maxMana;
//            }
//        }

        if (ausdauer < maxAusdauer && !isSprinting) {
            ausdauer += ausdauerRegen*delta;
            if (ausdauer > maxAusdauer) {
                ausdauer = maxAusdauer;
            }
        }

        timeSinceLastT1Skill += delta;
    }

    public void sprint(float delta, boolean isDevelopmentMode){
        devMode = isDevelopmentMode;
        if (ausdauer > 1f) {
            this.isSprinting = true;
            if (!devMode) {
                this.ausdauer -= ausdauerCost * delta;
            }
            this.speed = this.sprintingSpeed;
            if (devMode) {
                this.speed = 750f;
            }
        } else {
            stopSprint();
        }

    }
    public void stopSprint(){
        this.isSprinting = false;
        this.speed = this.normalSpeed;
        if (devMode) {
            this.speed = 500f;
        }
    }


    /// Abilities
    public void gainExperience(float exp) {
        experience += exp;
        // 这里设置一个简单的升级机制，比如经验超过 100*等级 就升级
        if (experience >= 100 * level) {
            levelUp();
        }
    }

    protected void levelUp() {
        level++;
        experience = 0;
        maxHealthPoints += 10;
        healthPoints = maxHealthPoints;
    }

    public abstract void castSkill(int skillID,ServerConnection serverConnection);

    @Override
    public void takeDamage(float damage, ServerConnection serverConnection) {
        takeDamage(damage);
        serverConnection.sendTakeDamage(this,damage);

    }
    @Override
    public void takeDamage(float damage) {
        super.takeDamage(damage);

        System.out.println("[Player INFO]: Player ["+this.name+"] took Damage! "+healthPoints+"/"+maxHealthPoints);
    }

    public boolean drainMana(float amount) {
        float tempMana = this.mana;
        this.mana -= amount;
        if (this.mana < 0) {
            this.mana = tempMana;
            return false;
        }
        return true;
    }


    /// Getter / Setter
    public String getClassName() {
        return className;
    }
    public String getName(){
        return name;
    }
    public int getLevel() {
        return level;
    }
    public float getExperience() {
        return experience;
    }
    public float getMana() {
        return mana;
    }
    public void setMana(float mana) {
        this.mana = mana;
    }
    public float getMaxMana() {
        return maxMana;
    }
    public float getManaRegen() {
        return manaRegen;
    }
    public float getAusdauer() {
        return ausdauer;
    }
    public float getMaxAusdauer() {
        return maxAusdauer;
    }
    public float getAusdauerRegen() {
        return ausdauerRegen;
    }
    public float getTimeSinceLastT1Skill() {
        return timeSinceLastT1Skill;
    }
    public ArrayList<Item> getItems() {
        return items;
    }
    public void clearInv(){
        items.clear();
    }
    public void setId(String id) {
        this.id = id;
    }
    public void updateItemFromPlayerData(String[] playerDataItems) {
        for (String itemName : playerDataItems) {
            Item item = ItemFactory.createItem(itemName);
            items.add(item);
        }
    }
    public void setRotation(Vector2 rotation) {
        this.rotation=rotation.cpy();
    }
    @Override
    public String toString(){
        return name+" "+className;
    }
    public boolean isSprinting() {
        return isSprinting;
    }
    public Vector2 getLastDeathPos(){
        return lastDeathPos;
    }
    public void setLastDeathPos(Vector2 lastDeathPos) {
        this.lastDeathPos = lastDeathPos;
    }
    public Vector2 getSpawnpoint(){
        return spawnpoint;
    }
    public void setAlive(){
        isAlive = true;
    }
    public void resetT1Timer(){
        timeSinceLastT1Skill = 0;
    }
}

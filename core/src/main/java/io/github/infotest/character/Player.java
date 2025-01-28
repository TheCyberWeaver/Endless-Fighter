package io.github.infotest.character;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import io.github.infotest.MainGameScreen;
import io.github.infotest.item.Item;
import io.github.infotest.util.Factory.ItemFactory;
import io.github.infotest.util.Logger;
import io.github.infotest.util.MyAssetManager;
import io.github.infotest.util.ServerConnection;

import java.util.ArrayList;

import static io.github.infotest.MainGameScreen.CELL_SIZE;

import static io.github.infotest.GameSettings.*;
import static io.github.infotest.MainGameScreen.localPlayer;
import static io.github.infotest.util.Overlay.UI_Layer.whitePixel;

public abstract class Player extends Actor{

    // basic things
    protected String className;
    protected int level;
    protected float experience;
    protected ArrayList<Item> items;


    protected float mana;
    protected float maxMana;
    protected float manaRegen = 2f;

    protected float gold=0;

    protected int INV_SIZE = 7;

    protected float ausdauer;
    protected float maxAusdauer;
    protected float ausdauerRegen = 3f;
    protected float ausdauerCost = 10f; //Ausdauer kosten pro Sekunde

    protected boolean isSprinting;

    protected boolean isFrozen;
    protected boolean hasMoved;
    protected  boolean isHit;

    protected boolean isAttacking;
    protected boolean isAttacking2;
    protected boolean isAttacking3;
    protected boolean isAttacking4;

    protected float animationTime = 0f;

    protected float sprintingSpeed = speed*7/4;
    protected float normalSpeed;

    protected Vector2 spawnpoint;
    protected Vector2 lastDeathPos;

    protected float T1Cost = 0f;
    protected float T1Damage = 0f;
    protected float T1Cooldown = 0f;
    protected float T1Speed = 0f;
    protected float T1Scale = 0f;
    protected float T1LT = 0f; // lifetime with 0.5 second on start and 0.7 s on hit and 0.8 on end without hit

    protected float T4Cost = 5f;
    protected float T4Damage = 16f;
    protected float T4Cooldown = 20f;
    protected float T4Scale = 3f;
    protected float T4LT = 2f; // lifetime with 0.5 second on start and 0.7 s on hit and 0.8 on end without hit



    //Assassin Att
    protected boolean seeAllActive = false;

    // Speech bubble fields
    private String speechBubbleMessage = null;
    private boolean isSpeechBubbleVisible = false;
    private float speechBubbleTimer = 0f;
    private float speechBubbleDuration = 4f;  // how many seconds to show
    // near top of Player class
    protected GlyphLayout glyphLayout = new GlyphLayout();


    protected float timeSinceLastT1Skill;
    protected float timeSinceLastT2Skill;
    protected float timeSinceLastT3Skill;
    protected float timeSinceLastT4Skill;

    public Player(String id, String name, String className, int maxHealthPoints, int maxMana, int maxAusdauer, Vector2 initialPosition, float speed) {
        super(maxHealthPoints,initialPosition,speed);
        this.id = id;
        this.name = name;
        this.className = className;
        this.level = 1;
        this.experience = 0;
        items=new ArrayList<>();
        for(int i=0;i<INV_SIZE;i++) {
            items.add(null);
        }
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
        this.isFrozen = false;
        this.isHit = false;
        this.isAttacking = false;
        this.normalSpeed = speed;

        this.spawnpoint = initialPosition;

        this.timeSinceLastT1Skill = 0;
        this.timeSinceLastT2Skill = 0;
        this.timeSinceLastT3Skill = 0;
        this.timeSinceLastT4Skill = 0;

    }

    /// game logic
    @Override
    public void render(Batch batch, float delta) {
        super.render(batch, delta);
        Vector2 predictedPosition = predictPosition();
        //1. draw texture if player has texture
        if (texture != null) {
            batch.draw(texture, predictedPosition .x-texture.getWidth()/2f, predictedPosition .y-texture.getHeight()/2f,32,32);
        }
        //2. render Player name
        GlyphLayout layout = new GlyphLayout(font, name);
        float textWidth = layout.width;
        font.draw(batch, name, predictedPosition.x - textWidth/2f  , predictedPosition.y + 50);

        // 3) Speech bubble logic
        if (isSpeechBubbleVisible && speechBubbleMessage != null) {
            // Update timer
            speechBubbleTimer += delta;
            if (speechBubbleTimer > speechBubbleDuration) {
                // Hide bubble
                isSpeechBubbleVisible = false;
            } else {
                // STILL VISIBLE, so draw it.

                // (A) Measure text
                glyphLayout.setText(font, speechBubbleMessage);
                float bubbleTextWidth = glyphLayout.width;
                float bubbleTextHeight = glyphLayout.height;

                // (B) Decide bubble position
                // Top-right of the player => offset from predictedPosition
                float offsetX = 40f; // shift to right
                float offsetY = 80f; // shift upwards from the player's sprite
                float bubbleX = predictedPosition.x + offsetX;
                float bubbleY = predictedPosition.y + offsetY;

                // (C) Define bubble rect size with some padding
                float padding = 8f;
                float bubbleWidth  = bubbleTextWidth + padding * 2;
                float bubbleHeight = bubbleTextHeight + padding * 2;

                // (D) Optional: draw a background shape using a 1×1 white texture with alpha
                // e.g., MyAssetManager.getWhitePixel()

                // Set color for tinted draw (e.g., semi‐transparent black)
                batch.setColor(0f, 0f, 0f, 0.7f); // black with 70% alpha
                batch.draw(whitePixel, bubbleX, bubbleY,
                    bubbleWidth, bubbleHeight);

                // Reset color so the subsequent text is not tinted
                batch.setColor(1f, 1f, 1f, 1f);

                // (E) Draw text inside bubble
                float textX = bubbleX + padding;
                float textY = bubbleY + bubbleHeight - padding;
                font.draw(batch, glyphLayout, textX, textY);
            }
        }

    }

    

    @Override
    public void update(float delta){

       if (mana < maxMana) {
            mana += manaRegen * delta;
            if (mana > maxMana) {
                mana = maxMana;
            }
        }

        if (ausdauer < maxAusdauer && !isSprinting) {
            ausdauer += ausdauerRegen*delta;
            if (ausdauer > maxAusdauer) {
                ausdauer = maxAusdauer;
            }
        }

        timeSinceLastT1Skill += delta;
        timeSinceLastT2Skill += delta;
        timeSinceLastT3Skill += delta;
        timeSinceLastT4Skill += delta;
    }

    public void sprint(float delta){
        if (ausdauer > 1f) {
            this.isSprinting = true;
            if (!isDevelopmentMode) {
                this.ausdauer -= ausdauerCost * delta;
            }
            float tileSpeedFactor;
            switch(tileIDUnder){
                case 0: tileSpeedFactor = speedFaktorOn0; break;
                case 1: tileSpeedFactor = speedFaktorOn1; break;
                case 2: tileSpeedFactor = speedFaktorOn2; break;
                case 3: tileSpeedFactor = speedFaktorOn3; break;
                case 4: tileSpeedFactor = speedFaktorOn4; break;
                case 5: tileSpeedFactor = speedFaktorOn5; break;
                default: tileSpeedFactor = 1; break;
            }

            this.speed = this.sprintingSpeed*tileSpeedFactor;
            if (isDevelopmentMode) {
                this.speed = 750f;
            }
        } else {
            stopSprint();
        }

    }
    public void stopSprint(){
        this.isSprinting = false;
        this.speed = this.normalSpeed;
        if (isDevelopmentMode) {
            this.speed = 500f;
        }
    }


    /// Abilities
    public void gainExperience(float exp) {
        experience += exp;
        if (experience >= MainGameScreen.neededExpForLevel(level)) {
            levelUp();
        }
    }

    protected void levelUp() {
        level++;
        experience = experience - MainGameScreen.neededExpForLevel(level);
        maxHealthPoints += 10;
        healthPoints = maxHealthPoints;
    }

    public abstract void castSkill(int skillID,ServerConnection serverConnection);

    @Override
    public void takeDamage(float damage, ServerConnection serverConnection) {
        takeDamage(damage);
        serverConnection.sendTakeDamage(this,damage);
        isHit = true;
    }
    @Override
    public void takeDamage(float damage) {
        super.takeDamage(damage);

        Logger.log("[Player INFO]: Player ["+this.name+"] took Damage! "+healthPoints+"/"+maxHealthPoints);
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

    public void respawn(){
        this.setLastDeathPos(this.getPosition());
        Vector2 spawnpoint = this.getSpawnpoint();
        this.setPosition(new Vector2(spawnpoint.x, spawnpoint.y));
        this.setAlive();

        this.setHealthPoints(this.getMaxHealthPoints());
        this.setMana(this.getMaxMana());

        this.resetT1Timer();
        this.resetT2Timer();
        this.resetT3Timer();
        this.resetT4Timer();

        this.resetAttacking();
        this.resetAttacking2();
        this.resetAttacking3();
        this.resetAttacking4();

        if (!keepInventory){
            for (Item i : this.getItems()){
                i.drop(this.getLastDeathPos().x,this.getLastDeathPos().y);
            }
            this.clearInv();
        }
    }
    public void showMessage(String message,ServerConnection serverConnection) {
        showMessage(message);
        serverConnection.sendShowPlayerMessage(this,message);
    }
    public void showMessage(String message) {
        // 1) Store the message
        this.speechBubbleMessage = message;
        this.isSpeechBubbleVisible = true;

        // 2) Reset the timer
        this.speechBubbleTimer = 0f;

        // For debugging
        System.out.println("[Player Debug]: " + name + " says: " + message);
    }

    public abstract Texture getMainSkillSymbol();

    /// Getter / Setter
    public float getT1SkillCoolDownTime(){
        return T1Cooldown;
    }
    public float getT1SkillCoolDownTimer(){
        return timeSinceLastT1Skill;
    }

//    public float getT2SkillCoolDownTime(){
//        return T2Cooldown;
//    }
    public float getT2SkillCoolDownTimer(){
        return timeSinceLastT2Skill;
    }

//    public float getT3SkillCoolDownTime(){
//        return T3Cooldown;
//    }
    public float getT3SkillCoolDownTimer(){
        return timeSinceLastT3Skill;
    }
    public float getT4SkillCoolDownTime(){
        return T4Cooldown;
    }
    public float getT4SkillCoolDownTimer(){
        return timeSinceLastT4Skill;
    }

    public float getT1Cost(){
        return T1Cost;
    }
    public float getT1Damage(){
        return T1Damage;
    }
    public float getT1Cooldown(){
        return T1Cooldown;
    }
    public float getT1Speed(){
        return T1Speed;
    }
    public float getT1Scale(){
        return T1Scale;
    }
    public float getT1LT(){
        return T1LT;
    }

    public float getT4Cost(){
        return T4Cost;
    }
    public float getT4Damage(){
        return T4Damage;
    }
    public float getT4Cooldown(){
        return T4Cooldown;
    }
    public float getT4Scale(){
        return T4Scale;
    }
    public float getT4LT(){
        return T4LT;
    }

    public boolean isAttacking(){
        return isAttacking;
    }
    public boolean isAttacking2(){
        return isAttacking2;
    }
    public boolean isAttacking3(){
        return isAttacking3;
    }
    public boolean isAttacking4(){
        return isAttacking4;
    }

    public void resetAttacking(){
        isAttacking = false;
    }
    public void resetAttacking2(){
        isAttacking2 = false;
    }
    public void resetAttacking3(){
        isAttacking3 = false;
    }
    public void resetAttacking4(){
        isAttacking4 = false;
    }

    public void resetT1Timer(){
        timeSinceLastT1Skill = 0;
    }
    public void resetT2Timer(){
        timeSinceLastT2Skill = 0;
    }
    public void resetT3Timer(){
        timeSinceLastT3Skill = 0;
    }
    public void resetT4Timer(){
        timeSinceLastT4Skill = 0;
    }

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
       this.mana = Math.min (mana, maxMana);  //Max Wert nicht überschreiten
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
    public void setAusdauer(float ausdauer) {
       this.ausdauer = Math.min (ausdauer, maxAusdauer);  //Max Wert nicht überschreiten
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
    public boolean addItem(Item item){
        for (int j=0;j<items.size();j++){
            Item i = items.get(j);
            if (i == null){
                items.set(j,item);
                Logger.log(items.toString());
                return true;
            }
        }
        return false;
    }
    public boolean addItem(Item item, int index){
        Item i = items.get(index);
        if (i == null){
            items.set(index,item);
            return true;
        }
        return false;
    }
    public void clearInv(){
        items.clear();
    }
    public void setId(String id) {
        this.id = id;
    }
    public void updateItems(ArrayList<String> itemIDs,MyAssetManager assetManager){
        for(int i = 0; i < itemIDs.size(); i++){
            items.set(i % INV_SIZE, ItemFactory.createItem(itemIDs.get(i), assetManager));
        }
        for(int i = itemIDs.size(); i < INV_SIZE; i++){
            items.set(i % INV_SIZE, null);
        }
    }
    public void kill(ServerConnection serverConnection) {
        super.kill();
        this.gold=0;
        serverConnection.sendPlayerDeath(this);
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
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
    public boolean isSeeAllActive() {
        return seeAllActive;
    }

    public float getGold() {
        return gold;
    }

    public void updateGold(float gold) {
        this.gold = gold;
    }
    public void addGold(float gold) {
        this.gold+=gold;
    }
    public void updateGold(float gold, ServerConnection serverConnection) {
        this.gold = gold;
        serverConnection.sendPlayerUpdateGold(localPlayer);
    }
    public void freeze(){
        isFrozen = true;
    }
    public void unfreeze(){
        isFrozen = false;
    }
    public boolean isFrozen() {
        return isFrozen;
    }
}

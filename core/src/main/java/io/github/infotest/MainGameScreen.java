package io.github.infotest;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.infotest.character.Gegner;
import io.github.infotest.character.NPC;
import io.github.infotest.character.Player;
import io.github.infotest.item.Item;
import io.github.infotest.util.MyAssetManager;
import io.github.infotest.util.Overlay.UI_Layer;
import io.github.infotest.util.Factory.PlayerFactory;
import io.github.infotest.util.ServerConnection;
import io.github.infotest.util.GameRenderer;
import io.github.infotest.util.MapCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainGameScreen implements Screen, InputProcessor, ServerConnection.SeedListener {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private UI_Layer uiLayer;
    private MyAssetManager assetManager;

    private Texture[] ausdauerbar;

    private Texture[] NPC_Male;
    private Texture[] NPC_Women;
    private Texture[] NPC_market;

    //Settings
    private boolean keepInventory;


    // Map data
    private int[][] map;
    private static final int CELL_SIZE = 32;
    private static final int INITIAL_SIZE = 3000;
    private static int numOfValidTextures = 4;

    // User character
    private Player player;
    private ServerConnection serverConnection;
    private boolean seedReceived = false;
    // Renderer
    private GameRenderer gameRenderer;

    // player list
    private HashMap<String, Player> players = new HashMap<>();
    private ArrayList<Gegner> allGegner = new ArrayList<>();
    private ArrayList<NPC> allNPC = new ArrayList<>();

    private Main game;
    public int globalSeed = 0;

    private float debugTimer=0;

    public MainGameScreen(Game game) {
        this.game = (Main) game;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.uiLayer = new UI_Layer(this);
        create();
    }

    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        assetManager = new MyAssetManager();

        assetManager.loadLoadingScreen();
        assetManager.loadMapAssets();
        assetManager.loadFireballAssets();
        assetManager.loadHealthBarAssets();
        assetManager.loadManaBarAssets();



        ausdauerbar = new Texture[4];
        ausdauerbar[0] = new Texture(Gdx.files.internal("ausdauerbar_full_start.png"));
        ausdauerbar[1] = new Texture(Gdx.files.internal("ausdauerbar_empty_start.png"));
        ausdauerbar[2] = new Texture(Gdx.files.internal("ausdauerbar_full_middle.png"));
        ausdauerbar[3] = new Texture(Gdx.files.internal("ausdauerbar_empty_middle.png"));

        NPC_Male = new Texture[8];
        NPC_Male[0] = new Texture(Gdx.files.internal("NPC_Male_1.png"));
        NPC_Male[1] = new Texture(Gdx.files.internal("NPC_Male_2.png"));
        NPC_Male[2] = new Texture(Gdx.files.internal("NPC_Male_3.png"));
        NPC_Male[3] = new Texture(Gdx.files.internal("NPC_Male_4.png"));
        NPC_Male[4] = new Texture(Gdx.files.internal("NPC_Male_5.png"));
        NPC_Male[5] = new Texture(Gdx.files.internal("NPC_Male_6.png"));
        NPC_Male[6] = new Texture(Gdx.files.internal("NPC_Male_7.png"));
        NPC_Male[7] = new Texture(Gdx.files.internal("NPC_Male_8.png"));

        NPC_Women = new Texture[8];
        NPC_Women[0] = new Texture(Gdx.files.internal("NPC_Women_1.png"));
        NPC_Women[1] = new Texture(Gdx.files.internal("NPC_Women_2.png"));
        NPC_Women[2] = new Texture(Gdx.files.internal("NPC_Women_3.png"));
        NPC_Women[3] = new Texture(Gdx.files.internal("NPC_Women_4.png"));
        NPC_Women[4] = new Texture(Gdx.files.internal("NPC_Women_5.png"));
        NPC_Women[5] = new Texture(Gdx.files.internal("NPC_Women_6.png"));
        NPC_Women[6] = new Texture(Gdx.files.internal("NPC_Women_7.png"));
        NPC_Women[7] = new Texture(Gdx.files.internal("NPC_Women_8.png"));

        NPC_market = new Texture[6];
        NPC_market[0] = new Texture(Gdx.files.internal("klein.png"));
        NPC_market[1] = new Texture(Gdx.files.internal("kiste.png"));
        NPC_market[2] = new Texture(Gdx.files.internal("besondereKiste.png"));
        NPC_market[3] = new Texture(Gdx.files.internal("tasche.png"));
        NPC_market[4] = new Texture(Gdx.files.internal("koffer.png"));
        NPC_market[5] = new Texture(Gdx.files.internal("besonders.png"));



        // connect to server
        //serverConnection = new ServerConnection("http://www.thomas-hub.com:9595", assassinTexture);
        serverConnection = new ServerConnection(game.getServerUrl(), assetManager);

        serverConnection.setSeedListener(this);
        serverConnection.connect();


        this.uiLayer = new UI_Layer(this,assetManager);
        Gdx.input.setInputProcessor(this);



        Vector2 spawnPosition = new Vector2(INITIAL_SIZE / 2f * CELL_SIZE, INITIAL_SIZE / 2f * CELL_SIZE);
        //System.out.println("class: "+ game.getPlayerClass());
        player = PlayerFactory.createPlayer(serverConnection.getMySocketId(),game.getUsername(),game.getPlayerClass(),spawnPosition,assetManager);
        //System.out.println("class: "+ player.getClass());

        // send initial position to server
        serverConnection.sendPlayerInit(player);

        camera.zoom = 1f;
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        if(game.isDevelopmentMode){
            player.setSpeed(500);
        }
        if (player.getHealthPoints() <= player.getMaxHealthPoints()) {

        }

        uiLayer.setHealthbar(healthbar);
        uiLayer.setManabar(manabar);
        uiLayer.setAusdauerbar(ausdauerbar);
    }
    @Override
    public void onSeedReceived(int seed) {
        // map initialization
        MapCreator mapCreator = new MapCreator(seed, INITIAL_SIZE, this, numOfValidTextures);
        globalSeed = seed;
        map = mapCreator.initializePerlinNoiseMap();

        seedReceived = true;

        gameRenderer = new GameRenderer(assetManager, map, CELL_SIZE);
        gameRenderer.initAnimations();

        System.out.println("[MainGameScreen INFO]: Map generated after receiving seed: " + seed);
    }
    @Override
    public void render(float delta) {

        // update player list
        this.players = serverConnection.getPlayers();
        if(serverConnection.getMySocketId()!=""){
            this.players.put(serverConnection.getMySocketId(), player);
        }

        //System.out.println(player);

        uiLayer.setPlayer(player);

        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(player!=null && gameRenderer!=null){
            // update camera position
            camera.position.set(player.getX(), player.getY(), 0);
            camera.update();
            batch.setProjectionMatrix(camera.combined);

            batch.begin();
            gameRenderer.renderMap(batch, camera.zoom, player.getPosition());
            gameRenderer.renderPlayers(batch, players, delta);
            gameRenderer.renderGegner(batch, allGegner, delta);
            gameRenderer.renderAnimations(batch,delta,shapeRenderer);
            batch.draw(assetManager.getPlayerAssets(), 0, 0, 0, 0, assetManager.getPlayerAssets().getWidth(), assetManager.getPlayerAssets().getWidth(), 32, 32);
            batch.end();

            for(Player p: players.values()){
                p.update(delta);
            }
            //player.update(delta);
            checkFireballCollisions();

            handleInput(delta);

            if (player.getHealthPoints() <= 0) {
                player.kill();
                respawn(player);
            }
        }
        else{
            batch.begin();
            batch.draw(assetManager.getLoadingScreenTexture(),
                0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();
        }
        debugTimer+=delta;
        uiLayer.render();
    }

    float tempTime = 0;
    private void handleInput(float delta) {
        boolean moved = false;
        float speed = player.getSpeed();

        tempTime += delta;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.setX(player.getX() - speed * delta);
            moved = true;
            player.setRotation(new Vector2(-1,0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.setX(player.getX() + speed * delta);
            moved = true;
            player.setRotation(new Vector2(1,0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.setY(player.getY() + speed * delta);
            moved = true;
            player.setRotation(new Vector2(0,1));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.setY(player.getY() - speed * delta);
            moved = true;
            player.setRotation(new Vector2(0,-1));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            player.castSkill(1,serverConnection);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            player.sprint(delta, game.isDevelopmentMode);
        } else if(player.isSprinting()){
            player.stopSprint();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.P) && game.isDevelopmentMode && debugTimer>=1){

            System.out.println("----------");
            for (Map.Entry<String, Player> stringPlayerEntry : players.entrySet()) {
                Player tmpPlayer=stringPlayerEntry.getValue();
                System.out.println(stringPlayerEntry.getKey()+" "+tmpPlayer.getName()+" "+tmpPlayer.getHealthPoints());
            }
            System.out.println("----------");
            debugTimer=0;
        }

        if (moved) {
            // update position
            serverConnection.sendPlayerPosition(player.getX(), player.getY(),player.getRotation().x,player.getRotation().y);
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }
    @Override
    public void hide() { }
    @Override
    public void pause() { }
    @Override
    public void resume() { }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        camera.zoom += amountY * 0.1f;
        if(!game.isDevelopmentMode){
            camera.zoom = Math.max(0.5f, Math.min(1.5f, camera.zoom));
        }
        if(game.isDevelopmentMode){
            camera.zoom = Math.max(0.01f, camera.zoom);
        }

        return true;
    }

    // InputProcessor empty implementations
    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchCancelled(int i, int i1, int i2, int i3) {return false;}
    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }

    public static int lvlToMaxHP(int lvl){
        return 50 + 5 * lvl;
    }

    public static int lvlToMaxMana(int lvl){
        return 25 + 5 * lvl;
    }

    public static int neededExpForLevel(int lvl){
        return 20 + 10 * lvl;
    }

    @Override
    public void dispose() {
        // end server connection
        serverConnection.disconnect();

        // release rendering resources
        batch.dispose();

        assetManager.manager.dispose();

        // gameRenderer.dispose();
    }

    /// GAME LOGIC
    public void checkFireballCollisions() {
        for (GameRenderer.FireballInstance fireball : gameRenderer.getActiveFireballs()) {
            for (Player p : players.values()){
                if (p.equals(fireball.getOwner())){
                    continue;
                }

                float dX = Math.abs(p.getX() - fireball.getX());
                float dY = Math.abs(p.getY() - fireball.getY());

                if (dX <= 16f && dY <= 16f && !fireball.hasHit()){
                    p.takeDamage(fireball.getDamage(),serverConnection);

                    fireball.setHit();
                }
            }

            for (Gegner gegner : allGegner){
                float dX = Math.abs(gegner.getX() - fireball.getX());
                float dY = Math.abs(gegner.getY() - fireball.getY());

                if (dX <= 7f && dY <= 7f){
                    gegner.takeDamage(fireball.getDamage(),serverConnection);
                    fireball.setHit();
                }
            }
        }
    }
    public void respawn(Player p){
        p.setLastDeathPos(p.getPosition());
        Vector2 spawnpoint = p.getSpawnpoint();
        p.setPosition(new Vector2(spawnpoint.x, spawnpoint.y));
        p.setAlive();

        p.setHealthPoints(p.getMaxHealthPoints());
        p.setMana(p.getMaxMana());

        p.resetT1Timer();

        if (!keepInventory){
            for (Item i : p.getItems()){
                i.drop(p.getLastDeathPos().x,p.getLastDeathPos().y);
            }
            p.clearInv();
        }
    }

    /// GETTER / SETTER
    public boolean hasSeedReceived(){
        return seedReceived;
    }

    public boolean isKeepInventory(){
        return keepInventory;
    }
}

package io.github.infotest;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.infotest.character.Gegner;
import io.github.infotest.character.NPC;
import io.github.infotest.character.Player;
import io.github.infotest.item.Item;
import io.github.infotest.util.*;
import io.github.infotest.util.Overlay.UI_Layer;
import io.github.infotest.util.Factory.PlayerFactory;
import io.github.infotest.util.ServerConnection;
import io.github.infotest.util.GameRenderer;
import io.github.infotest.util.MapCreator;

import java.util.*;
import com.badlogic.gdx.utils.Timer;

import static io.github.infotest.GameSettings.*;
import static io.github.infotest.MainGameScreen.localPlayer;

public class MainGameScreen implements Screen, InputProcessor, ServerConnection.SeedListener {
    private SpriteBatch batch;
    public static ShapeRenderer shapeRenderer;
    private MyAssetManager assetManager;
    private final OrthographicCamera camera;
    public static UI_Layer uiLayer;

    private Random random;

    public static Vector3 clickPos = null;
    public static boolean clicked = false;

    private boolean isRenderingWithNightShader = false;

    public static boolean isRenderingFlameThrower = false;
    public static boolean isRenderingBlackHoleActivation = false;


    // Map data
    public static int GLOBAL_SEED; // this will be assigned by the seed from server
    public static final int CELL_SIZE = 32;
    public static final int MAP_SIZE = 100;
    public static int numOfValidTextures = 6;
    public static int numOfValidDeco = 13;

    public static int[][] GAME_MAP=new int[MAP_SIZE][MAP_SIZE];
    public static int[][] GAME_MAP_BACKUP=new int[MAP_SIZE][MAP_SIZE];
    public static int[][] ROTATION_MAP=new int[MAP_SIZE][MAP_SIZE];
    public static String[][] FADE_MAP=new String[MAP_SIZE][MAP_SIZE];
    public static int[][] DECO_MAP=new int[MAP_SIZE][MAP_SIZE];
    public static int[][] DECO_MAP_BACKUP=new int[MAP_SIZE][MAP_SIZE];
    public static float[][] DECO_PROB = new float[numOfValidTextures][numOfValidDeco];
    public static float[][] DECO_SCALE_MAP=new float[MAP_SIZE][MAP_SIZE];
    public static Vector2[][] DECO_OFFSET_MAP=new Vector2[MAP_SIZE][MAP_SIZE];

    private static ArrayList<Dungeon> dungeons = new ArrayList<Dungeon>();

    // User character
    public static Player localPlayer;
    private ServerConnection serverConnection;

    // Renderer
    private GameRenderer gameRenderer;

    // player list
    public static HashMap<String, Player> allPlayers = new HashMap<>();
    public static ArrayList<Gegner> allGegner = new ArrayList<>();
    public static ArrayList<NPC> allNPCs = new ArrayList<>();
    private int numberOfNPCInTheLastFrame = 0;
    private NPC currentTradingToNPC;

    public static ArrayList<AnimationObjects> animationObjects = new ArrayList<>();

    private final Main game;
    public static boolean hasInitializedMap = false;

    //Timer
    private float debugTimer=0;
    private float survivalTimer=0;
    private float showMessageTimer=0;
    private float currentTradingToNPCTimer=0;
    private float cameraZoomTimer=5;

    //Settings
    private float waitAfterDeath=7f;

    private Music backgroundMusic;
    private Music runningSound;

    public MainGameScreen(Game game) {
        this.game = (Main) game;
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        initDeco_prob();
        create();
    }

    private void initDeco_prob(){
        DECO_PROB[0][0] = 0.1f;
        DECO_PROB[0][1] = 0.15f;
        DECO_PROB[0][2] = 0.05f;
        DECO_PROB[0][3] = 0.005f;
        DECO_PROB[0][4] = 0f;
        DECO_PROB[0][5] = 0f;
        DECO_PROB[0][6] = 0f;
        DECO_PROB[0][7] = 0f;
        DECO_PROB[0][8] = 0f;
        DECO_PROB[0][9] = 0f;
        DECO_PROB[0][10] = 0.1f;
        DECO_PROB[0][11] = 0.1f;
        DECO_PROB[0][12] = 0.1f;

        DECO_PROB[1][0] = 0.1f;
        DECO_PROB[1][1] = 0.15f;
        DECO_PROB[1][2] = 0.1f;
        DECO_PROB[1][3] = 0.1f;
        DECO_PROB[1][4] = 0.05f;
        DECO_PROB[1][5] = 0.05f;
        DECO_PROB[1][6] = 0.075f;
        DECO_PROB[1][7] = 0.075f;
        DECO_PROB[1][8] = 0.005f;
        DECO_PROB[1][9] = 0f;
        DECO_PROB[1][10] = 0.1f;
        DECO_PROB[1][11] = 0.2f;
        DECO_PROB[1][12] = 0.2f;

        DECO_PROB[2][0] = 0f;
        DECO_PROB[2][1] = 0f;
        DECO_PROB[2][2] = 0f;
        DECO_PROB[2][3] = 0f;
        DECO_PROB[2][4] = 0f;
        DECO_PROB[2][5] = 0f;
        DECO_PROB[2][6] = 0f;
        DECO_PROB[2][7] = 0f;
        DECO_PROB[2][8] = 0f;
        DECO_PROB[2][9] = 0f;
        DECO_PROB[2][10] = 0f;
        DECO_PROB[2][11] = 0f;
        DECO_PROB[2][12] = 0f;

        DECO_PROB[3][0] = 0f;
        DECO_PROB[3][1] = 0f;
        DECO_PROB[3][2] = 0f;
        DECO_PROB[3][3] = 0f;
        DECO_PROB[3][4] = 0f;
        DECO_PROB[3][5] = 0f;
        DECO_PROB[3][6] = 0f;
        DECO_PROB[3][7] = 0f;
        DECO_PROB[3][8] = 0f;
        DECO_PROB[3][9] = 0f;
        DECO_PROB[3][10] = 0f;
        DECO_PROB[3][11] = 0f;
        DECO_PROB[3][12] = 0f;

        DECO_PROB[4][0] = 0f;
        DECO_PROB[4][1] = 0f;
        DECO_PROB[4][2] = 0f;
        DECO_PROB[4][3] = 0f;
        DECO_PROB[4][4] = 0f;
        DECO_PROB[4][5] = 0f;
        DECO_PROB[4][6] = 0f;
        DECO_PROB[4][7] = 0f;
        DECO_PROB[4][8] = 0f;
        DECO_PROB[4][9] = 0f;
        DECO_PROB[4][10] = 0f;
        DECO_PROB[4][11] = 0f;
        DECO_PROB[4][12] = 0f;

        DECO_PROB[5][0] = 0f;
        DECO_PROB[5][1] = 0f;
        DECO_PROB[5][2] = 0f;
        DECO_PROB[5][3] = 0f;
        DECO_PROB[5][4] = 0f;
        DECO_PROB[5][5] = 0f;
        DECO_PROB[5][6] = 0f;
        DECO_PROB[5][7] = 0f;
        DECO_PROB[5][8] = 0f;
        DECO_PROB[5][9] = 0f;
        DECO_PROB[5][10] = 0f;
        DECO_PROB[5][11] = 0f;
        DECO_PROB[5][12] = 0f;
    }

    public void create() {
        //endScreen = new EndScreen(game, game.getScreen());

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        assetManager = new MyAssetManager();

        assetManager.loadLoadingScreen();

        assetManager.loadMapAssets();
        assetManager.loadMapWaterAssets();
        assetManager.loadSkillBarAsset();
        assetManager.loadGoldBarAsset();

        assetManager.loadMapFadeAssets();
        assetManager.loadMapDecoAssets();
        assetManager.loadMapTreeAssets();

        assetManager.loadAnimationObjectAssets();

        assetManager.loadPlayerAssets();

        assetManager.loadMageAssets();
        assetManager.loadFireballAssets();
        assetManager.loadFlameThrowerAssets();
        assetManager.loadFireballSymbol();
        assetManager.loadBlackHoleAssets();

        assetManager.loadHealthBarAssets();
        assetManager.loadManaBarAssets();
        assetManager.loadAusdauerBarAssets();

        assetManager.loadGegnerAssets();

        assetManager.loadNPCMaleAssets();
        assetManager.loadNPCWomenAssets();
        assetManager.loadNPCMarketAssets();

        assetManager.loadSignsAssets();
        assetManager.loadArrowAssets();

        assetManager.loadItemBarAssets();
        assetManager.loadItemAssets();

        assetManager.loadMainGameMusicAssets();

        assetManager.manager.finishLoading();

        runningSound= assetManager.getRunningSound();

        assetManager.initAnimationObjectColumnsRows();

        // connect to server
        //serverConnection = new ServerConnection("http://www.thomas-hub.com:9595", assassinTexture);
        serverConnection = new ServerConnection(game.getServerUrl(), assetManager, game.clientVersion);

        serverConnection.setSeedListener(this);
        serverConnection.connect();


        uiLayer = new UI_Layer(assetManager);
        Gdx.input.setInputProcessor(this);



        Vector2 spawnPosition = new Vector2(MAP_SIZE / 2f * CELL_SIZE, MAP_SIZE / 2f * CELL_SIZE);
        //Logger.log(""class: "+ game.getPlayerClass());
        localPlayer = PlayerFactory.createPlayer(serverConnection.getMySocketId(),game.getUsername(),game.getPlayerClass(),spawnPosition,assetManager);
        //Logger.log("class: "+ player.getClass());

        // send initial position to server
        if (localPlayer != null) {
            serverConnection.sendPlayerInit(localPlayer);
        }

        camera.zoom = 1f;
        if (localPlayer != null) {
            camera.position.set(localPlayer.getX(), localPlayer.getY(), 0);
        }
        camera.update();

        if(isDevelopmentMode){
            localPlayer.setSpeed(500);
        }

        currentTradingToNPC = null;

        backgroundMusic =assetManager.getMainGameBackgroundMusic();
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }
    @Override
    public void onSeedReceived(int seed) {
        // map initialization
        MapCreator mapCreator = new MapCreator(seed);
        //GLOBAL_SEED = seed;
        mapCreator.initializePerlinNoiseMap();

        localPlayer.setId(serverConnection.getMySocketId());
        allPlayers.put(serverConnection.getMySocketId(), localPlayer);

        hasInitializedMap = true;

        gameRenderer = new GameRenderer(this, assetManager, camera);
        gameRenderer.initAnimations();
        gameRenderer.initShaders();

        random = new Random(GLOBAL_SEED);

        Logger.log("[MainGameScreen INFO]: Map generated after receiving seed: " + seed);
    }
    @Override
    public void render(float delta) {

        //Logger.log(player);

        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(localPlayer !=null && gameRenderer!=null){
            // update camera position
            camera.position.set(localPlayer.getX(), localPlayer.getY(), 0);
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            shapeRenderer.setProjectionMatrix(camera.combined);
            Gdx.gl.glLineWidth(5); // make line width to 5 pixel
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            batch.begin();

            if(isRenderingWithNightShader){
                gameRenderer.activateNightShader(batch, camera);
            } else {
                batch.setShader(null);
            }

            gameRenderer.renderMap(batch, delta, camera.zoom, localPlayer.getPosition());
            gameRenderer.renderPlayers(batch, allPlayers, delta);
            gameRenderer.renderGegner(batch, allGegner, delta);
            handleInput(batch, delta);
            if (numberOfNPCInTheLastFrame < allNPCs.size()) {
                //Sort list based on y coordinate (dsc)
                allNPCs.sort(new Comparator<NPC>() {
                    @Override
                    public int compare(NPC npc1, NPC npc2) {
                        return Float.compare(npc2.getPosition().y, npc1.getPosition().y);
                    }
                });

            }

            for (AnimationObjects animationObject : animationObjects) {
                animationObject.render(batch, delta);
            }

            gameRenderer.renderNPCs(batch, delta);
            gameRenderer.renderAnimations(batch,delta,shapeRenderer);
            gameRenderer.renderTrees(batch, camera.zoom, localPlayer.getPosition());

            // Render Market and Items
            if (currentTradingToNPC != null) {
                uiLayer.renderMarket(batch, currentTradingToNPC.getMarketTexture());

//                String str="";
//                for(Item i:currentTradingToNPC.getMarket()){
//                    str+=i;
//                }
//                Logger.log("[MainGameScreen INFO 1]: " + str);
//
//                str="";
//                for(Item i:allNPCs.get(0).getMarket()){
//                    str+=i;
//                }
//                Logger.log("[MainGameScreen INFO 2]: " + str);

                uiLayer.renderMarketItems(batch, currentTradingToNPC.getMarket(), currentTradingToNPC.getNPC_marketMapValue(currentTradingToNPC.getMarketTextureID()));
                handleUIInput(batch, delta);
            }
            // Render INV_FULL sign
            if (uiLayer.isRenderingSign()){
                Vector3 worldCoordinates = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                float worldX = worldCoordinates.x;
                float worldY = worldCoordinates.y;
                float time = uiLayer.getSignTimer();
                if (time <= uiLayer.getDuration()){
                    batch.draw(assetManager.getSignsAssets(), worldX, worldY);
                } else if (time > uiLayer.getDuration() && time-uiLayer.getSignTimer()< uiLayer.getFadeDuration()) {
                    float alpha = MyMath.getExpValue(uiLayer.getBase(), uiLayer.getFadeDuration(), time-uiLayer.getDuration());
                    batch.setColor(1,1,1,alpha);
                    batch.draw(assetManager.getSignsAssets(), worldX, worldY);
                    batch.setColor(1,1,1,1);
                    if (Float.isNaN(alpha)){
                        uiLayer.resetTimer();
                        uiLayer.resetRenderingSign();
                    }

                }
                uiLayer.addSignTimer(delta);
            }

            if (isRenderingFlameThrower){
                if (!(localPlayer.getFlameThrowerActiveTimer() <= localPlayer.getT3LT())){
                    localPlayer.resetFlameThrowerActiveTimer();
                    localPlayer.resetAttacking3();
                }
                Vector3 mouseWorldPos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                Vector2 direction = new Vector2( localPlayer.getX() - mouseWorldPos.x, -(mouseWorldPos.y - localPlayer.getY()));
                GameRenderer.renderFlameThrower(batch, direction, localPlayer);
                checkFlameThrowerCollision(batch, direction, localPlayer, delta, mouseWorldPos);
            }

            float d = 32*32;
            if (isRenderingBlackHoleActivation && renderBlackHoleActivation(delta, d)) {
                if (clickPos != null) {
                    float dX = Math.abs(clickPos.x-localPlayer.getX());
                    float dY = Math.abs(clickPos.y-localPlayer.getY());
                    if (dX < d/2 && dY < d/2) {
                        GameRenderer.blackHole(clickPos.x, clickPos.y, localPlayer.getT4Scale(), localPlayer.getT4Damage(), localPlayer.getT4LT(), localPlayer);
                        localPlayer.drainMana(localPlayer.getT4Cost());
                        localPlayer.resetAttacking4();
                        localPlayer.unfreeze();
                    }
                }
            }


            batch.draw(assetManager.getPlayerAssets(), 0, 0, 0, 0, assetManager.getPlayerAssets().getWidth(), assetManager.getPlayerAssets().getWidth(), 32, 32);
            batch.setShader(null);
            batch.end();
            shapeRenderer.end();
            Gdx.gl.glLineWidth(1); // Reset the line width to 1 pixel

            doGameLogic(delta);
            gameRenderer.updateAllBlackHoles(delta);

        }
        else{
            batch.begin();
            batch.draw(assetManager.getLoadingScreenTexture(),
                0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();
        }

        uiLayer.render();
        clickPos = null;
        clicked = false;

    }
    private void doGameLogic(float delta) {

        for(Player p: allPlayers.values()){
            p.update(delta);
        }

        checkFireballCollisions();

        if (localPlayer.getHealthPoints() <= 0 && localPlayer.isAlive()) {
            localPlayer.kill(serverConnection);
            // Schedule a task to show the EndScreen after 3 seconds
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    backgroundMusic.stop();
                    // Switch to the EndScreen, passing player's survival time, etc.
                    game.endGame(survivalTimer);
                }
            }, waitAfterDeath); // 3 seconds delay
            //respawn(localPlayer);
        }

        debugTimer+=delta;
        survivalTimer += delta;
        showMessageTimer += delta;
        currentTradingToNPCTimer+=delta;
        cameraZoomTimer+=delta;

        numberOfNPCInTheLastFrame = allNPCs.size();
        clicked = false;
    }

    Vector3 oldPosition = null;
    private void handleUIInput(Batch batch, float delta) {
        if (clickPos == null) return;
        if (!clickPos.equals(oldPosition)) {
            oldPosition = clickPos;
            Vector2 clickPosition = new Vector2(clickPos.x, clickPos.y);
            for (int i = 0; i < currentTradingToNPC.getMarket().length; i++) {
                Vector2 itemPos = currentTradingToNPC.getItemPos(i, localPlayer, uiLayer.getNScale(), uiLayer.getWindowSize());
                if (MyMath.inInPixelRange(itemPos, clickPosition, 21)) {
                    //Player has Clicked on Item
                    currentTradingToNPC.trade(i, localPlayer,serverConnection);
                }
            }
        } else if (clickPos.equals(oldPosition) && clicked) {
            uiLayer.resetTimer();
        }
    }

    float tempTime = 0;
    private void handleInput(Batch batch, float delta) {
        boolean moved = false;
        float speed = localPlayer.getSpeed();

        tempTime += delta;
        if (localPlayer.isAlive()) {
            if (Gdx.input.isKeyPressed(Input.Keys.A) && !localPlayer.isFrozen()) {
                localPlayer.setX(localPlayer.getX() - speed * delta);
                moved = true;
                localPlayer.setRotation(new Vector2(-1, 0));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) && !localPlayer.isFrozen()) {
                localPlayer.setX(localPlayer.getX() + speed * delta);
                moved = true;
                localPlayer.setRotation(new Vector2(1, 0));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W) && !localPlayer.isFrozen()) {
                localPlayer.setY(localPlayer.getY() + speed * delta);
                moved = true;
                localPlayer.setRotation(new Vector2(0, 1));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) && !localPlayer.isFrozen()) {
                localPlayer.setY(localPlayer.getY() - speed * delta);
                moved = true;
                localPlayer.setRotation(new Vector2(0, -1));
            }

            if (Gdx.input.isKeyPressed(Input.Keys.E) && !localPlayer.isFrozen()) {
                localPlayer.castSkill(1, serverConnection);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.C) && !localPlayer.isFrozen()) {
                localPlayer.castSkill(3, serverConnection);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.Q) && !localPlayer.isFrozen()) {
                localPlayer.castSkill(4, serverConnection);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && moved && !localPlayer.isFrozen()) {
                localPlayer.sprint(delta);
                if(!runningSound.isPlaying()){
                    runningSound.play();
                }
            } else if (localPlayer.isSprinting()) {
                localPlayer.stopSprint();
                if(runningSound.isPlaying()){
                    runningSound.stop();
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (tempTime >= 0.5f) {
                    isRenderingWithNightShader = !isRenderingWithNightShader;
                    tempTime = 0;
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.F) && !localPlayer.isFrozen()) {
                if(currentTradingToNPC==null && currentTradingToNPCTimer>=0.3){
                    currentTradingToNPCTimer=0;
                    NPC cNpc = getClosestNPC();
                    if (cNpc != null) {
                        float distance = localPlayer.getPosition().dst(cNpc.getPosition());
                        if (currentTradingToNPC == null && distance <= 100 && !cNpc.isTrading()) {
                            cNpc.openMarket(batch);
                            currentTradingToNPC = cNpc;
                        }
                    }
                }
                else if(currentTradingToNPC!=null && currentTradingToNPCTimer>=0.3){
                    currentTradingToNPCTimer=0;
                    currentTradingToNPC.closeMarket();
                    currentTradingToNPC = null;
                }

            }
            if (moved && currentTradingToNPC != null) {
                currentTradingToNPC.closeMarket();
                currentTradingToNPC = null;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.K)) {
                localPlayer.kill(serverConnection);
                // Schedule a task to show the EndScreen after 3 seconds
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        // Switch to the EndScreen, passing player's survival time, etc.
                        game.endGame(survivalTimer);
                    }
                }, 3f); // 3 seconds delay
            }

            //Debug Player status
            if (Gdx.input.isKeyPressed(Input.Keys.P) && isDevelopmentMode && debugTimer >= 1) {

//                Logger.log("-----[Debug: showing player status]-----");
//                Logger.log("socketID | Name | HP | ItemsLength | alive");
//                for (Map.Entry<String, Player> stringPlayerEntry : allPlayers.entrySet()) {
//                    Player tmpPlayer = stringPlayerEntry.getValue();
//                    Logger.log(stringPlayerEntry.getKey() + " " + tmpPlayer.getName() + " " + tmpPlayer.getHealthPoints() + " " + tmpPlayer.getItems().size() + " " + tmpPlayer.isAlive());
//                    StringBuilder str = new StringBuilder();
//                    for (Item i : tmpPlayer.getItems()) {
//                        if (i == null) {
//                            str.append("null ");
//                        } else {
//                            str.append(i).append(" ");
//                        }
//                    }
//                    Logger.log("-> Items: " + str);
//                }
//                Logger.log("-----[Debug END]-----");
                Logger.log("-----[Debug: showing npc status]-----");
                for (NPC npc : allNPCs) {

                    Logger.log(npc.getName() + " " + npc.getHealthPoints() +" "+ npc.getMarket().length);
                    StringBuilder str = new StringBuilder();
                    for (Item i : npc.getMarket()) {
                        if (i == null) {
                            str.append("null ");
                        } else {
                            str.append(i.id).append(" ");
                        }
                    }
                    Logger.log("-> Items: " + str);
                }
                Logger.log("-----[Debug END]-----");

                debugTimer = 0;
            }


            if (Gdx.input.isKeyPressed(Input.Keys.T) && showMessageTimer >= 1) {

                localPlayer.showMessage(ToxicLines.getToxicLines(),serverConnection);
                //TODO show different messages
                showMessageTimer = 0;
            }


            localPlayer.setHasMoved(moved);
            if (moved && localPlayer.isAlive()) {
                // update position
                serverConnection.sendPlayerPosition(localPlayer.getX(), localPlayer.getY(), localPlayer.getRotation().x, localPlayer.getRotation().y);
            }

            if(!isDevelopmentMode){
                if(camera.zoom<=defaultCameraZoom && cameraZoomTimer>=2)camera.zoom+=0.005;
                if(camera.zoom>=defaultCameraZoom && cameraZoomTimer>=2 && !localPlayer.isAttacking4())camera.zoom-=0.005;
            }
        }
    }

    private boolean renderBlackHoleActivation(float delta, float radius) {
        localPlayer.freeze();
        if (camera.zoom < 1.2){
            camera.zoom += 4*delta;
            return false;
        } else {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(localPlayer.getX()-radius/2, localPlayer.getY()-radius/2, radius, radius);
            return true;
        }
    }


    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        uiLayer.resize(width, height);
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
        if (localPlayer.isAttacking4()) return false;
        camera.zoom += amountY * 0.1f;
        if(!isDevelopmentMode){
            camera.zoom = Math.max(0.25f, Math.min(1.5f, camera.zoom));
        }
        if(isDevelopmentMode){
            camera.zoom = Math.max(0.01f, camera.zoom);
        }
        cameraZoomTimer=0;
        return true;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        clickPos = camera.unproject(new Vector3(screenX, screenY, 0));
        clicked = true;
        return true;
    }

    // InputProcessor empty implementations
    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
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

    public NPC getClosestNPC(){
        NPC cNpc = null;
        float dist = Float.MAX_VALUE;
        Vector2 playerPos = localPlayer.getPosition();
        for (NPC npc : allNPCs){
            float distanceSq = npc.getPosition().dst2(playerPos);
            if (distanceSq < dist){
                dist = distanceSq;
                cNpc = npc;
            }
        }
        return cNpc;
    }

    @Override
    public void dispose() {
        // end server connection
        serverConnection.disconnect();

        // release rendering resources
        batch.dispose();

        assetManager.manager.dispose();
        shapeRenderer.dispose();

        // gameRenderer.dispose();
    }

    /// GAME LOGIC
    public void checkFireballCollisions() {
        for (GameRenderer.AbilityInstance fireball : gameRenderer.getActiveFireballs()) {
            for (Player p : allPlayers.values()){
                if (p.equals(fireball.getOwner())){
                    continue;
                }

                float dX = Math.abs(p.getX() - fireball.getX());
                float dY = Math.abs(p.getY() - fireball.getY());

                if (dX <= 32f && dY <= 64f && !fireball.hasHit()){
                    if(!p.equals(localPlayer)&&fireball.getOwner().equals(localPlayer)){
                        p.takeDamage(fireball.getDamage(),serverConnection);
                    }
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
    public void checkFlameThrowerCollision(Batch batch, Vector2 direction, Player localPlayer, float delta, Vector3 mousePos) {
        float distance = direction.len();
        Animation<TextureRegion> flameThrowerAnimation = GameRenderer.getFlameThrowerAnimation();
        float flameThrowerAnimationTime = GameRenderer.getFlameThrowerAnimationTime();
        float dstToWidth = flameThrowerAnimation.getKeyFrame(flameThrowerAnimationTime).getRegionWidth()/distance;
        float damageScale = Math.max(Math.min(dstToWidth,2f), 0.3f);
        float damage = localPlayer.getT3Damage()*(17f/20f) + (localPlayer.getT3Damage()-1)*damageScale;

        for (Player p : allPlayers.values()){
            if (p.equals(localPlayer)){
                continue;
            }

            float width = flameThrowerAnimation.getKeyFrame(flameThrowerAnimationTime).getRegionWidth();
            float height = flameThrowerAnimation.getKeyFrame(flameThrowerAnimationTime).getRegionHeight();

            Vector2 b = new Vector2(p.getX()-localPlayer.getX(), p.getY()-localPlayer.getY());
            Vector2 v = new Vector2(-direction.cpy().x, -direction.cpy().y);
            if (v.len() < width){
                v.nor().scl(width);
            }
            if (v.len() > width*localPlayer.getT3ScaleWidth()){
                v.nor().scl(width*localPlayer.getT3ScaleWidth());
            }

            Vector2 temp = v.cpy().nor();
            Vector2 u = new Vector2(temp.y*height/2f, -temp.x*height/2f);

            float n = (-(v.y*b.x-v.x*b.y))/(u.y*v.x-v.y*u.x);
            float m = (u.y*b.x-u.x*b.y)/(u.y*v.x-v.y*u.x);

            if (n>=-1 && n<=1 && m>=0 && m<=1){
                p.takeDamage(damage*delta, serverConnection);
            }

        }
    }

    public boolean isRenderingWithNightShader() {
        return isRenderingWithNightShader;
    }
    public void setRenderingWithNightShader(boolean renderingWithNightShader) {
        isRenderingWithNightShader = renderingWithNightShader;
    }
    public float getZoom(){
        return camera.zoom;
    }

    /// DUNGEON HELPER CLASS
    public class Dungeon{
        private int gold;
        private int xp;
        private Vector2 position;
        private AnimationObjects animationObject;
        private AnimationObjects trophy;
        private boolean isClaimed;


        public Dungeon(AnimationObjects animationObject, Vector2 position, int goldMin, int goldMax, int xpMin, int xpMax, Random random){
            this.gold = goldMin + random.nextInt(goldMax - goldMin);
            this.xp = xpMin + random.nextInt(xpMax - xpMin);
            this.position = position;
            this.animationObject = animationObject;

            float width = animationObject.getAnimation().getKeyFrame(animationObject.getAnimationTimer()).getRegionWidth();
            float height = animationObject.getAnimation().getKeyFrame(animationObject.getAnimationTimer()).getRegionHeight();

            this.trophy = AnimationObjects.createGoldTrophy(position.x+width/2f, position.y+height/2f, assetManager);
            isClaimed = false;
        }

        public void render(Batch batch, float delta){
            animationObject.render(batch, delta);
        }

        public void claim(Player p){
            p.gainExperience(xp);
            p.addGold(gold);
            this.isClaimed = true;
        }
    }
}


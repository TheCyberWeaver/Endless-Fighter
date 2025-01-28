package io.github.infotest.util;

import com.badlogic.gdx.math.Vector2;

import java.util.*;

import static io.github.infotest.MainGameScreen.*;

public class MapCreator {
    private final int seed;
    private final int stoneSeed;
    private final Perlin perlinClass;
    private final Random random;
    private final Random randomDeco;

    public MapCreator(int pSeed) {
        seed = pSeed;
        stoneSeed = pSeed+3;
        perlinClass = new Perlin();
        random = new Random(seed);
        randomDeco = new Random(stoneSeed);
    }

    public void initializePerlinNoiseMap(){
        // generate perlin noise based on seed (see Perlin Class)
        float[][] whiteNoise = Perlin.GenerateWhiteNoise(MAP_SIZE, MAP_SIZE, seed);
        float[][] perlinNoise = perlinClass.GeneratePerlinNoise(whiteNoise, 6);
        float[][] whiteNoiseStone = Perlin.GenerateWhiteNoise(MAP_SIZE, MAP_SIZE, stoneSeed);
        float[][] perlinNoiseStone = perlinClass.GeneratePerlinNoise(whiteNoiseStone, 4);


        // convert perlin noise to valid GAME_MAP
        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                float perlinNoiseValue = perlinNoise[y][x];
                if (perlinNoiseValue>0.75){
                    GAME_MAP[y][x] = numOfValidTextures-1;
                } else if (perlinNoiseValue<0.75 && perlinNoiseValue>0.65){
                    GAME_MAP[y][x] = numOfValidTextures-2;
                } else if (perlinNoiseValue<0.65 && perlinNoiseValue>0.40){
                    GAME_MAP[y][x] = numOfValidTextures-5;
                } else {
                    GAME_MAP[y][x] = numOfValidTextures-6;
                }
                float perlinNoiseStoneValue = perlinNoiseStone[y][x];
                if (perlinNoiseStoneValue>0.65) {
                    if (GAME_MAP[y][x] == 4 || GAME_MAP[y][x] == 5) {
                        GAME_MAP[y][x] = numOfValidTextures-3;
                    }
                    if (GAME_MAP[y][x] == 0 || GAME_MAP[y][x] == 1) {
                        GAME_MAP[y][x] = numOfValidTextures-4;
                    }
                }
                if (perlinNoiseStoneValue<0.65 && perlinNoiseStoneValue>0.55 && GAME_MAP[y][x]==numOfValidTextures-4) {
                    GAME_MAP[y][x] = numOfValidTextures-5;
                }

                ROTATION_MAP[y][x] = (int)(random.nextFloat()*5);
            }
        }

        initFadeMap();
        initDecoMap();
//        for (int i = 0; i < DECO_MAP.length; i++) {
//            Logger.log(Arrays.toString(DECO_MAP[i]));
//        }

        // Remove isolated blocks and pairs
        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                if (isIsolatedBlock(x, y)) {
                    GAME_MAP[y][x] = getRandomNeighbor(x, y);
                } else if (isIsolatedBlockPair(x, y)) {
                    GAME_MAP[y][x] = getRandomNeighbor(x, y);
                }
            }
        }
    }

    /**
    * Generiert für jede Zelle eine Liste (in Form eines Strings) der Typen der Nachbarzellen.
    */
    private void initFadeMap(){
        for (int y2 = 0; y2 < MAP_SIZE; y2++) {
            for (int x2 = 0; x2 < MAP_SIZE; x2++) {
                String str = "";
                int thisCell = GAME_MAP[y2][x2]; // type of this cell
                int topLeft; // für c1
                int top; // für t
                int topRight; // für c2
                int right; // für r
                int bottomRight; // für c3
                int bottom; // für b
                int bottomLeft; // für c4
                int left; // für l

                FADE_MAP[y2][x2] = new HashMap<>();

                for(int i =-1; i <= 1; i++){ // iteriere über alle Nachbarzellen
                    for(int j = -1; j<= 1; j++){
                        if(x2+i < 0 || x2+i >= MAP_SIZE || y2+j < 0 || y2 +j >= MAP_SIZE){
                            continue; // Nachbarzelle ist ausserhalb der Welt
                        }
                        if(GAME_MAP[y2][x2] >= GAME_MAP[y2+j][x2+i]){
                            continue; // Nachbarzelle kann fürs Fading ignoriert werden
                        }
                        FADE_MAP[y2][x2].put(new Vector2(i,j),GAME_MAP[y2+j][x2+i]);
                    }
                }
            }
        }
    }
    private void initDecoMap(){
        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                float r1 = random.nextFloat();
                if (r1 < 0.25) {
                    float r2 = random.nextFloat();
                    float p_old = 0;
                    for(int i = 0; i< numOfValidDeco; i++){
                        float p = p_old + DECO_PROB[GAME_MAP[y][x]][i];
                        if (p_old<r2 && r2<p) {
                            DECO_MAP[y][x] = i;
                            DECO_SCALE_MAP[y][x] = randomDeco.nextFloat()*0.5f+0.75f;
                            DECO_OFFSET_MAP[y][x] = new Vector2(randomDeco.nextFloat()*16, randomDeco.nextFloat()*16);
                        }
                        p_old = p;
                    }
                }
            }
        }
    }

    private boolean isIsolatedBlock(int x, int y) {
        int currentBlock = GAME_MAP[y][x];
        boolean hasSameNeighbor = false;

        // Check the 4 direct neighbors (up, down, left, right)
        if (x > 0 && GAME_MAP[y][x - 1] == currentBlock) hasSameNeighbor = true; // Left
        if (x < MAP_SIZE - 1 && GAME_MAP[y][x + 1] == currentBlock) hasSameNeighbor = true; // Right
        if (y > 0 && GAME_MAP[y - 1][x] == currentBlock) hasSameNeighbor = true; // Up
        if (y < MAP_SIZE - 1 && GAME_MAP[y + 1][x] == currentBlock) hasSameNeighbor = true; // Down

        return !hasSameNeighbor;
    }
    private boolean isIsolatedBlockPair(int x, int y) {
        int currentBlock = GAME_MAP[y][x];

        // Check horizontal pairs
        if (x < MAP_SIZE - 1 && GAME_MAP[y][x + 1] == currentBlock) {
            return !hasSameNeighborExceptPair(x, y, x + 1, y);
        }
        // Check vertical pairs
        if (y < MAP_SIZE - 1 && GAME_MAP[y + 1][x] == currentBlock) {
            return !hasSameNeighborExceptPair(x, y, x, y + 1);
        }

        return false;
    }
    private boolean hasSameNeighborExceptPair(int x1, int y1, int x2, int y2) {
        int currentBlock = GAME_MAP[y1][x1];

        // Check neighbors of the first block
        if (CheckEveryNeighbour(x1, y1, x2, y2, currentBlock)) return true; // Down

        // Check neighbors of the second block
        if (CheckEveryNeighbour(x2, y2, x1, y1, currentBlock)) return true;

        return false;
    }
    private boolean CheckEveryNeighbour(int x1, int y1, int x2, int y2, int currentBlock) {
        if (x1 > 0 && GAME_MAP[y1][x1 - 1] == currentBlock && !(x1 - 1 == x2 && y1 == y2)) return true;
        if (x1 < MAP_SIZE - 1 && GAME_MAP[y1][x1 + 1] == currentBlock && !(x1 + 1 == x2 && y1 == y2)) return true;
        if (y1 > 0 && GAME_MAP[y1 - 1][x1] == currentBlock && !(x1 == x2 && y1 - 1 == y2)) return true;
        if (y1 < MAP_SIZE - 1 && GAME_MAP[y1 + 1][x1] == currentBlock && !(x1 == x2 && y1 + 1 == y2)) return true;
        return false;
    }

    private int getRandomNeighbor(int x, int y) {
        List<Integer> neighbors = new ArrayList<>();

        // Collect all valid neighbors
        if (x > 0) neighbors.add( GAME_MAP[y][x - 1]); // Left
        if (x < MAP_SIZE - 1) neighbors.add( GAME_MAP[y][x + 1]); // Right
        if (y > 0) neighbors.add( GAME_MAP[y - 1][x]); // Up
        if (y < MAP_SIZE - 1) neighbors.add( GAME_MAP[y + 1][x]); // Down

        // Return a random neighbor
        return neighbors.get((int) (random.nextFloat() * neighbors.size()));
    }

}

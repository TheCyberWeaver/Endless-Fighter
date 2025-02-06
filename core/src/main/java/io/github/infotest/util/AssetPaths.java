package io.github.infotest.util;

import com.badlogic.gdx.graphics.Texture;

public class AssetPaths {

    // Single-file asset paths
    public static final String LOADING_SCREEN   = "ui/loadingscreen.png";
    public static final String SKILL_BAR        = "ui/skillbar.png";
    public static final String GOLD_BAR         = "ui/goldbar.png";
    public static final String FIREBALL_SYMBOL  = "player/mage/mageFireballSymbol.png";
    public static final String PLAYER_ASSASSIN  = "assassin.png";
    public static final String GEGNER           = "Gegner.png";
    public static final String SIGN_INV_FULL    = "Ur_Inv_Is_Full_Sign.png";
    public static final String ARROW            = "arrow.png";
    public static final String ITEM_BAR         = "ui/itembar.png";
    // etc.

    // Example: arrays for grouped assets
    public static final String[] MAP_ASSETS = {
        "worldTexture/Tile1.png",
        "worldTexture/Tile2.png",
        "worldTexture/TreeTile.png",
        "worldTexture/TreeTile_snow.png",
        "worldTexture/Tile4.png",
        "worldTexture/Tile5.png",
        "worldTexture/water_sheet.png"
    };

    public static final String[] MAP_FADE_ASSETS = {
        "worldTexture/fade/Tile1_corner.png",
        "worldTexture/fade/Tile1_bottom.png",
        "worldTexture/fade/Tile1_left.png",
        "worldTexture/fade/Tile1_right.png",
        "worldTexture/fade/Tile1_top.png",

        "worldTexture/fade/Tile2_corner.png",
        "worldTexture/fade/Tile2_bottom.png",
        "worldTexture/fade/Tile2_left.png",
        "worldTexture/fade/Tile2_right.png",
        "worldTexture/fade/Tile2_top.png",

        "worldTexture/fade/Tile4_corner.png",
        "worldTexture/fade/Tile4_bottom.png",
        "worldTexture/fade/Tile4_left.png",
        "worldTexture/fade/Tile4_right.png",
        "worldTexture/fade/Tile4_top.png",

        "worldTexture/fade/Tile5_corner.png",
        "worldTexture/fade/Tile5_bottom.png",
        "worldTexture/fade/Tile5_left.png",
        "worldTexture/fade/Tile5_right.png",
        "worldTexture/fade/Tile5_top.png"
    };

    public static final String[] MAP_DECO_ASSETS = {
        "worldTexture/deco/stone1.png",
        "worldTexture/deco/stone2.png",
        "worldTexture/deco/stone3.png",
        "worldTexture/deco/stone4.png",
        "worldTexture/deco/stone5.png",
        "worldTexture/deco/stone6.png",
        "worldTexture/deco/stone7.png",
        "worldTexture/deco/stone8.png",
        "worldTexture/deco/stone9.png",
        "worldTexture/deco/stone_big.png",
        "worldTexture/deco/fallenTree.png",
        "worldTexture/deco/mushroom1.png",
        "worldTexture/deco/mushroom2.png"
    };

    public static final String[] MAP_TREE_ASSETS = {
        "worldTexture/deco/tree/tree.png",
        "worldTexture/deco/tree/tree_bottom.png",
        "worldTexture/deco/tree/tree_top.png",
        "worldTexture/deco/tree/tree_snow.png",
        "worldTexture/deco/tree/tree_bottom_snow.png",
        "worldTexture/deco/tree/tree_top_snow.png"
    };

    // Example for your mage
    public static final String[] MAGE_ASSETS = {
        "player/mage/Attack1.png",
        "player/mage/Death.png",
        "player/mage/Hit.png",
        "player/mage/Idle.png",
        "player/mage/Run.png"
    };

    public static final String[] FIREBALL_ASSETS = {
        "player/mage/fireball/fireball_sheet_start.png",
        "player/mage/fireball/fireball_sheet_fly.png",
        "player/mage/fireball/fireball_sheet_endTime.png",
        "player/mage/fireball/fireball_sheet_endHit.png"
    };
    public static final String[] BLACKHOLE_ASSETS = {
        "player/mage/abilities/blackHole1_sheet.png",
        "player/mage/abilities/blackHole2_sheet.png"
    };

    // Bars
    public static final String[] HEALTH_BAR = {
        "ui/healthbar/healthbar_full_start.png",
        "ui/healthbar/healthbar_empty_start.png",
        "ui/healthbar/healthbar_full_middle.png",
        "ui/healthbar/healthbar_empty_middle.png"
    };

    public static final String[] MANA_BAR = {
        "ui/manabar/manabar_full_start.png",
        "ui/manabar/manabar_empty_start.png",
        "ui/manabar/manabar_full_middle.png",
        "ui/manabar/manabar_empty_middle.png"
    };

    public static final String[] AUSDAUER_BAR = {
        "ui/ausdauerbar/ausdauerbar_full_start.png",
        "ui/ausdauerbar/ausdauerbar_empty_start.png",
        "ui/ausdauerbar/ausdauerbar_full_middle.png",
        "ui/ausdauerbar/ausdauerbar_empty_middle.png"
    };

    // Gegner
    public static final String[] GOBLIN = {
        "gegner/goblin/goblin_attack.png",
        "gegner/goblin/goblin_death.png",
        "gegner/goblin/goblin_hit.png",
        "gegner/goblin/goblin_idle.png",
        "gegner/goblin/goblin_run.png",
    };

    // NPCs
    public static final String[] NPC_MALE = {
        "NPC/male/NPC_Male_1.png",
        "NPC/male/NPC_Male_2.png",
        "NPC/male/NPC_Male_3.png",
        "NPC/male/NPC_Male_4.png",
        "NPC/male/NPC_Male_5.png",
        "NPC/male/NPC_Male_6.png",
        "NPC/male/NPC_Male_7.png",
        "NPC/male/NPC_Male_8.png"
    };

    public static final String[] NPC_WOMAN = {
        "NPC/woman/NPC_Women_1.png",
        "NPC/woman/NPC_Women_2.png",
        "NPC/woman/NPC_Women_3.png",
        "NPC/woman/NPC_Women_4.png",
        "NPC/woman/NPC_Women_5.png",
        "NPC/woman/NPC_Women_6.png",
        "NPC/woman/NPC_Women_7.png",
        "NPC/woman/NPC_Women_8.png"
    };

    public static final String[] NPC_MARKET = {
        "NPC/market/klein.png",
        "NPC/market/kiste.png",
        "NPC/market/besondereKiste.png",
        "NPC/market/tasche.png",
        "NPC/market/koffer.png",
        "NPC/market/besonders.png"
    };
    public static final String[] ANIMATION_OBJECTS = {
        "animationObjects/decoFountain.png",
        "animationObjects/goldTrophy.png",
        "animationObjects/summoningChamber.png",
        "animationObjects/winTrophy.png"
    };
    public static final String[] MAP_WATER = {
        "worldTexture/waterEdge/bottomRight_water.png",
        "worldTexture/waterEdge/right_water.png",
        "worldTexture/waterEdge/topRight_water.png",
        "worldTexture/waterEdge/top_water.png",
        "worldTexture/waterEdge/topLeft_water.png",
        "worldTexture/waterEdge/left_water.png",
        "worldTexture/waterEdge/bottomLeft_water.png",
        "worldTexture/waterEdge/bottom_water.png",
    };

    // Items
    public static final String[] ITEMS = {
        "item/apple.png",
        "item/potion_blue.png",
        "item/potion_red.png",
        "item/potion_yellow.png"
        // add more item textures here
    };

    // Music & Sound
    public static final String START_GAME_MUSIC = "music/StartGameMusic_1.mp3";
    public static final String MAIN_GAME_MUSIC  = "music/mainGameMusic_1.mp3";
    public static final String DEATH_SOUND      = "music/death.mp3";
    public static final String FIREBALL_SOUND   = "music/castFireball.mp3";
    public static final String COME_BACK_SOUND  = "music/come_back_and_face_me.mp3";
    public static final String NORMAL_ATTACK    = "music/normalAttack.mp3";
    public static final String RUNNING_SOUND    = "music/running.mp3";
    public static final String TAUNT_SOUND      = "music/taunt.mp3";

    // etc. ...
}

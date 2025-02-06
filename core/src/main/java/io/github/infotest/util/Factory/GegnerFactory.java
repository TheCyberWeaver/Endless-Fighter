package io.github.infotest.util.Factory;

import com.badlogic.gdx.math.Vector2;
import io.github.infotest.character.Gegner;
import io.github.infotest.character.NPC;
import io.github.infotest.util.MyAssetManager;
import io.github.infotest.util.Overlay.UI_Layer;
import io.github.infotest.util.ServerConnection;

import java.util.Collections;
import java.util.Comparator;

public class GegnerFactory {
    public static Gegner createGegner(String id, Gegner.SPEZIES spezies, String gegnerName, int maxHP, Vector2 pos, int type, MyAssetManager assetManager, ServerConnection serverConnection) {
        // Serverconnection: NPC attributes: name, pos, gender(0 || 1), type(0-7), startItems für market;
        // WICHTIG: alle 5 oder 6 min market updaten mit neuen Items auf allen Clients
        return new Gegner(id, spezies, assetManager.getGoblinAssets(), maxHP, pos, 10, 100,50, serverConnection);
    }

}

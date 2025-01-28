package io.github.infotest.util.Factory;

import io.github.infotest.item.Apple;
import io.github.infotest.item.Item;
import io.github.infotest.item.Potion;
import io.github.infotest.util.MyAssetManager;

public class ItemFactory {
    /**
     * Creates an Item instance based on the itemName string.
     * You can expand this as needed for more item types.
     *
     * @param itemID The string representing the item (e.g., "Sword", "Shield", etc.)
     * @return An instance of a class extending Item
     * @throws IllegalArgumentException if the item name is not recognized
     */
    public static Item createItem(String itemID, MyAssetManager assetManager) {
        if(itemID==null)return null;
        String itemName = getNameFromId(itemID);
        switch (itemName) {
            case "Apple":
                return new Apple(itemID, "apple",assetManager,20);
            case "ManaPotion":
                return new Potion(Potion.TYPE.MANA, 40, assetManager);
            case "HealthPotion":
                return new Potion(Potion.TYPE.HEALTH, 40, assetManager);
            case "AusdauerPotion":
                return new Potion(Potion.TYPE.AUSDAUER, 40, assetManager);
            default:
                throw new IllegalArgumentException("Unknown item: " + itemName);
        }
    }
    public static String getNameFromId(String itemID) {
        if(itemID==null){
            return null;
        }
        String[] parts = itemID.split("_");
        if (parts.length > 0) {
            return parts[0];
        }
        return null;
    }

}

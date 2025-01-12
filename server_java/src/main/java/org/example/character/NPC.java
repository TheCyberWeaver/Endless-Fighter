package org.example.character;


import org.example.item.Item;
import org.example.util.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class NPC {

    public String name;
    // 1.: Gender: 0 = male, 1 = female
    // 2.: Type: 0-7 NPC Type
    public Vector2 genderType;
    public ArrayList<Item> market;

    public int maxHealthPoints;
    public int healthPoints;
    public Vector2 startPosition;


    public NPC(String name, int maxHealthPoints, Vector2 startPosition, int gender, int type) {
        this.name = name;
        this.maxHealthPoints = maxHealthPoints;
        this.healthPoints = maxHealthPoints;
        this.startPosition = new Vector2(startPosition);
        this.genderType = new Vector2(gender%2, type%8);
    }


    @Override
    public String toString() {
        return "NPC: " + name;
    }
}

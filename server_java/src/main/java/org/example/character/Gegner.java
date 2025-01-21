package org.example.character;


import org.example.util.Vector2;

import java.util.ArrayList;
import java.util.UUID;

public class Gegner {
    public String id;
    public String name;
    public int type;

    public int maxHP;
    public int hp;
    public Vector2 position;


    public Gegner(String name, int maxHealthPoints, Vector2 startPosition, int type) {
        this.name = name;
        this.id= UUID.randomUUID().toString();
        this.maxHP = maxHealthPoints;
        this.hp = maxHealthPoints;
        this.position = new Vector2(startPosition);
        this.type = type;
    }
    public void update()
    {

    }
    @Override
    public String toString() {
        return "NPC: " + name;
    }

}

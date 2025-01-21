package org.example.character;


import org.example.util.Vector2;

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

    public Player findPlayer(HashMap allPlayers){

        Player closestPlayer = null;
        float shortestDistance = Float.MAX_VALUE;
        for (int i = 0; i < allPlayers.length; i++){
            Player player = allPlayers.get (i);
            float distance = position.dst (player.getPosition());

            if (distance < shortestDistance){
                shortestDistance = distance;
                closestPlayer = player;
            }
        }
        return closestPlayer;
    }

    public void moveTowards (Player playerPosition, float delta) {
        Vector2 direction = playerPosition.sub (position);
        direction = direction.nor();
        Vector2 movement = direction.scl (20);
        position.add (movement);
    }

    public void update(float delta) {
        Player closestPlayer = findPlayer(allPlayers);
        float distance = position.dst (closestPlayer) ;
        if (distance <= attackRange) {
            performAttack(playerPosition);
        } else { moveTowards (closestPlayer, delta);
        }
    }

    public void performAttack(Player player) {
        player.takeDamage (5);
    }


    @Override
    public String toString() {
        return "NPC: " + name;
    }
}



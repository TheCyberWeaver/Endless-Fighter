package org.example.character;


import org.example.util.Vector2;

import java.util.UUID;

import static org.example.GameSocketServer.allPlayers;

public class Gegner {
    public String id;
    public String name;
    public int type;

    public int maxHP;
    public int hp;
    public Vector2 position;

    public float attackRange=10;


    public Gegner(String name, int maxHealthPoints, Vector2 startPosition, int type) {
        this.name = name;
        this.id= UUID.randomUUID().toString();
        this.maxHP = maxHealthPoints;
        this.hp = maxHealthPoints;
        this.position = new Vector2(startPosition);
        this.type = type;
    }

    public Player findPlayer(){

        Player closestPlayer = null;
        float shortestDistance = Float.MAX_VALUE;
        for (int i = 0; i < allPlayers.size(); i++){
            Player player = allPlayers.get (i);
            float distance = dst(position, player.position);

            if (distance < shortestDistance){
                shortestDistance = distance;
                closestPlayer = player;
            }
        }
        return closestPlayer;
    }
    public static float dst(Vector2 v1, Vector2 v2){
        float x=v1.x-v2.x;
        float y=v1.y-v2.y;
        return (float)Math.sqrt(x*x+y*y);
    }

    public void moveTowards (Player playerPosition, float delta) {
        Vector2 direction = playerPosition.position.sub(position);
        direction = direction.normal();
        Vector2 movement = direction.scale (20);
        position=position.add (movement);
    }

    public void update(float delta) {
        position=position.add(new Vector2(0.5f,0f));
//        Player closestPlayer = findPlayer();
//        if(closestPlayer != null){
//            float distance = dst(position, closestPlayer.position) ;
//            if (distance <= attackRange) {
//                performAttack(closestPlayer);
//            } else { moveTowards (closestPlayer, delta);
//            }
//        }
    }

    public void performAttack(Player player) {
        player.takeDamage (5);
    }


    @Override
    public String toString() {
        return "NPC: " + name;
    }
}



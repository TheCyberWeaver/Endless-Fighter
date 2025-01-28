package org.example.character;


import org.example.util.Vector2;

import java.util.Map;
import java.util.UUID;

import static org.example.GameSocketServer.*;

public class Gegner {
    public String id;
    public String name;
    public int type;

    public float maxHP;
    public float hp;
    public Vector2 position;

    public Player lastAttackedBy = null;

    private float attackRange=10;
    private float killGold=50;

    private float attackCoolDownTime=0.5f;
    private float attackCoolDownTimer=0;

    private boolean isAlive=true;

    public Gegner(String name, float maxHealthPoints, Vector2 startPosition, int type) {
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
        for (Map.Entry<String, Player> entry : allPlayers.entrySet()) {
            Player player = entry.getValue();
            if(player.isAlive){
                float distance = position.distance(player.position);

                if (distance < shortestDistance){
                    shortestDistance = distance;
                    closestPlayer = player;
                }
            }

        }
        return closestPlayer;
    }

    public void moveTowards (Player playerPosition, float delta) {
        Vector2 direction = playerPosition.position.sub(position);
        direction = direction.normal();
        Vector2 movement = direction.scale (1f);
        position=position.add (movement);
    }

    public void update(float delta) {
        //position=position.add(new Vector2(0.5f,0f));
        Player closestPlayer = findPlayer();
        if(closestPlayer != null){
            //System.out.println("Moving to "+closestPlayer.name );

            float distance =position.distance(closestPlayer.position) ;
            if (distance <= attackRange && attackCoolDownTimer>=attackCoolDownTime) {
                //performAttack(closestPlayer);
            } else {
                moveTowards (closestPlayer, delta);
            }
        }

        attackCoolDownTimer+=delta;
    }

    public void performAttack(Player player) {
        player.takeDamage (5);
        needPlayerUpdate=true;
        attackCoolDownTimer=0;
    }

    public void takeDamage(float damage, Player player) {
        lastAttackedBy=player;
        hp -= damage;
        hp=Math.max(0,hp);
        if(hp==0){
            killed();
        }
        System.out.println("Gegner["+name+"] Taking " + damage + " damage with remaining "+hp +"HP");
    }
    public void killed() {
        server.getBroadcastOperations().sendEvent("GegnerKilled", new getKilledData(id,lastAttackedBy.getId()));
        lastAttackedBy.gold+=killGold;
        isAlive=false;
        System.out.println("Gegner["+name+"] is killed by Player["+lastAttackedBy.name+"]");
    }
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public String toString() {
        return "NPC: " + name;
    }
    static class getKilledData {
        public String gegnerID;
        public String killedByPlayerID;
        public getKilledData(String gegnerID, String killedByPlayerID) {
            this.gegnerID = gegnerID;
            this.killedByPlayerID = killedByPlayerID;
        }
    }
}




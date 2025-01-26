package org.example.character;


import org.example.util.Vector2;
import static org.example.GameSocketServer.*;

import java.util.ArrayList;
import java.util.UUID;

public class NPC {
    public String id;
    public String name;
    // 1.: Gender: 0 = male, 1 = female
    // 2.: Type: 0-7 NPC Type
    public int gender;
    public int type;
    public int marketTextureID;
    //public ArrayList<Item> market;

    public int maxHP;
    public int hp;
    public Vector2 position;

    public ArrayList<String> itemIDs=new ArrayList<>();

    public NPC(String name, int maxHealthPoints, Vector2 startPosition, int gender, int type, int marketTextureID) {
        this.name = name;
        this.id= UUID.randomUUID().toString();
        this.maxHP = maxHealthPoints;
        this.hp = maxHealthPoints;
        this.position = new Vector2(startPosition);
        this.gender = gender%2;
        this.type = type%8;
        this.marketTextureID = marketTextureID;

        itemIDs.add(null);
        itemIDs.add(null);
        itemIDs.add(null);

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                for(int i=0;i<3;i++){
                    if(itemIDs.get(i)==null){
                        System.out.println("Items refilled");
                        itemIDs.set(i,"Apple_"+UUID.randomUUID().toString());
                    }
                }

                needNPCUpdate=true;
                try {
                    Thread.sleep(1000);  //
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public void pickItem(String itemID) {
        boolean picked=false;
        for(int i=0;i<itemIDs.size();i++) {
            if(itemIDs.get(i)==null) {
                itemIDs.set(i,itemID);
                picked=true;
            }
        }
        if(!picked) {
            itemIDs.add(itemID);
        }
    }

    public void dropItem(String itemID) {
        for(int i=0;i<itemIDs.size();i++) {
            if(itemIDs.get(i)==null) {continue;}
            if(itemIDs.get(i).equals(itemID)) {
                itemIDs.set(i,null);
            }
        }
        for(int i=0;i<itemIDs.size();i++) {
            System.out.println("[Status Debug] "+itemIDs.get(i));
        }
    }

    @Override
    public String toString() {
        return "NPC: " + name;
    }

}

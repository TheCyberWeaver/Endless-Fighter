package org.example.util;

public class Vector2 {
    public float x=0;
    public float y=0;
    public Vector2(Vector2 v) {
        x=v.x;
        y=v.y;
    }
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 v){
        return new Vector2(x+v.x, y+v.y);
    }
    public Vector2 sub(Vector2 v){
        return new Vector2(x-v.x, y-v.y);
    }
    public Vector2 normal(){
        float length= (float) Math.sqrt(x*x + y*y);
        return new Vector2(x/length, y/length);
    }
    public Vector2 scale(int v){
        return new Vector2(x*v, y*v);
    }
}

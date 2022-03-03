package com.game.game.Objects.Units;

import java.util.LinkedList;


import com.game.game.*;
import com.game.engine.*;
import com.game.engine.gfx.*;

import com.game.game.Objects.*;

//The worker class is both not used and not implemented. Honestly, you could probably even just not bother reading it.
//It was originally planned for me to create a 2x2 unit that was larger. In fact, its textures are still in the game. However
//the collision was weird and wasn't working, and the idea was eventually scrapped.

//The remaining amount of code here was to test the a* algorithm when trying to find different objecrts

public class Worker extends GUnit {
    ImageTile imageTile;

    int left = 0;
    float temp = 0f;

    public Worker(int x, int y) {
        owner = 1;
        this.x = x;
        this.y = y;
        this.width = GameManager.TILE_SIZE;
        this.height = GameManager.TILE_SIZE;
        this.size = 1;
        this.width = size*GameManager.TILE_SIZE;
        this.height = size * GameManager.TILE_SIZE;
        Path = new LinkedList<Integer>();
        
        maxHealth = 80;
        health = maxHealth;
        attackCycles = 100;
        armour = 0;
        speed = 5;
        attackRange = 1; leashRange = 6; visionRange = 10;
    }

    public void update(GameContainer gc, GameManager gm, float dt) {
        super.update(gc, gm, dt);
    }

    public void Build(GameContainer gc, GStructure Structure, int x, int y){

    }
    public void attack(GameManager gm, GObject target) {
        

    }
    


    

    public void render(GameContainer gc, GameManager gm, Renderer r) {
        if(gm.getShowHitbox())
            r.fillRect((CenterTile % gm.getLevelW()) * 16, (CenterTile / gm.getLevelW()) * 16, width, height, 0xff000000);
        r.fillRect(x, y, width, height, 0xffff0000);
        if(selectStatus != 0)
            r.fillRect(x, y, width, height, 0xffffa4a4);

        // r.drawImageTile(imageTile, x, y, (int)temp, left);
        if(Path != null)

        if(gm.getShowPath())
            for(int i : Path){
                r.drawRect(i % gm.getLevelW() * 16, (i / gm.getLevelW()) * 16, width, height, 0xffff0000);
            }
    }

    

    

    
    
}

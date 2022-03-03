package com.game.game.Objects;

import com.game.game.*;
import com.game.engine.*;
import com.game.engine.gfx.*;

//The group of DSmoke, death smoke, MFire, mage fire, and RSlash, Rogue slash, are particle effects that occur when something happens. 
//They extend GObject instead of GUnit, and don't have any health values or anything. Instead, they only have a picture, location, and lifespan attached
//They go through their animation, and simply disappear once their lifespan runs out

public class DSmoke extends GObject {
    ImageTile imageTile;
    int time;
    int lifeSpan;
    int x, y;

    public DSmoke(ImageTile imageTile, int lifeSpan, int x, int y){
        this.imageTile = imageTile;
        this.lifeSpan = lifeSpan;
        time = 0;
        this.x = x;
        this.y = y;
        
    }

    public void update(GameContainer gc, GameManager gm, float dt) {
        if(time == lifeSpan){
            this.setRemove(true);
        }
        time++;

    }
    
    @Override
    public void render(GameContainer gc, GameManager gm, Renderer r) {
      
        if(time < 7){
            r.drawImageTile(imageTile, x, y, 0, 0);
        } else if(time < 14){
            r.drawImageTile(imageTile, x, y, 1, 0);
        } else if (time < 20){
            r.drawImageTile(imageTile, x, y, 2, 0);
        } 
        else{
            r.drawImageTile(imageTile, x, y, 3,0);
        }
        
    }
    
}

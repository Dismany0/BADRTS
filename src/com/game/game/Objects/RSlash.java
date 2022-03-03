package com.game.game.Objects;

import com.game.game.*;
import com.game.engine.*;
import com.game.engine.gfx.*;

//Rogue slash animation. Only appears when the rogue kills someone. Thats about it

public class RSlash extends GObject {
    ImageTile imageTile;
    int time;
    int lifeSpan;
    int x, y;
    public RSlash(ImageTile imageTile, int lifeSpan, int x, int y){
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
        r.setzDepth(99);
        if(time < 3){
            r.drawImageTile(imageTile, x, y, 0, 0);
        } else if(time < 8){
            r.drawImageTile(imageTile, x, y, 1, 0);
        } else if (time < 30){
            r.drawImageTile(imageTile, x, y, 2, 0);
        }
        else{
            r.drawImageTile(imageTile, x, y, 3, 0);
        }
        r.setzDepth(0);
    }
    
}

package com.game.game.Objects;

import com.game.game.*;
import com.game.engine.*;
import com.game.engine.gfx.*;

//Mage Fire effect. Its got a few random animations, along with a burning sound to go with it

public class MFire extends GObject {
    ImageTile imageTile;
    int time;
    int lifeSpan;
    int x, y;
    int tempTime = 0;
    public MFire(ImageTile imageTile, int lifeSpan, int x, int y){
        GameManager.AttackSounds.get(3).play();
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

        if(time % 3 == 0){
            tempTime = (int)(Math.random()*4);
        }
        if(time < 3){
            r.drawImageTile(imageTile, x, y, 0, (int)Math.random() * 2);
        } else if(time < 8){
            r.drawImageTile(imageTile, x, y, 1, (int)Math.random() * 2);
        } else if (time < 15){
            r.drawImageTile(imageTile, x, y, 2, (int)Math.random() * 2);
        } else if (time < 23){
            r.drawImageTile(imageTile, x, y, 3, (int)Math.random() * 2);
        }
        else{
            r.drawImageTile(imageTile, x, y, tempTime,2);
        }
        r.setzDepth(0);
    }
    
}

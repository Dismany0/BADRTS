package com.game.game.Objects.Units;

import java.util.ArrayList;

import java.util.LinkedList;
import com.game.game.*;
import com.game.engine.*;
import com.game.engine.gfx.*;
import com.game.game.Objects.*;

//Remember the knight being special with the weird animation box? well not really. The mage is 16x24, being vertically taller due to flying
//Something cool to pay attention to is the mage's transparent shadow while flying. The details of the ground are still visible through the partially
//transparent shadow.

//The mage is unique from the other units in that it does not directly attack its target. Instead, it marks the tile the target is standing on,
//along with its surrounding tiles. After every unit has finished updating, anything standing on the marked tiles
//recieves damage.
//Basically, the mage attacks but with aoe instead.
public class Mage extends GUnit{
    Image image = new Image("/guy.png");
    ImageTile imageTile;

    public Mage(int x, int y){
        
        GameManager.MageSounds.get((int)(Math.random()*3)).play();
        tag = "Mage";
        imageTile = GameManager.Mage;
        this.x = x;
        this.y = y;
        this.width = GameManager.TILE_SIZE;
        this.height = GameManager.TILE_SIZE;
        this.size = 1;
        this.width = size * GameManager.TILE_SIZE;
        this.height = size * GameManager.TILE_SIZE;
        Path = new LinkedList<Integer>();

        maxHealth = 80;
        health = maxHealth;
        attackCycles = 100;
        armour = 0;
        speed = 5;
        attackRange = 5; leashRange = 10; visionRange = 10;
    }
    float walkcycle = 0;
    public void update(GameContainer gc, GameManager gm, float dt) {
        if(remove){
            GameManager.MageSounds.get((int)(Math.random()*3)+ 20).play();
        }
        super.update(gc,gm,dt);
        if(walking){
            walkcycle += dt * 4;
            
        }else{
            walkcycle += dt * 2;
        }
        if(walkcycle > 4){
            walkcycle = 0;
        }
    }
    public void render(GameContainer gc, GameManager gm, Renderer r) {
        super.render(gc, gm, r);
        

        // This shows the testing purple hitbox
        // r.fillRect(x, y, width, height, 0xffc934e0);
        // if(selectStatus != 0)
        // r.fillRect(x, y, width, height, 0xff9022a1);

        // This shows the little walky guy
        if(attacking){
            if(attackWindUp < 10)
                r.drawImageTile(imageTile, x, y-8, 0, direction + 2);
            else if(attackWindUp < 20)
                r.drawImageTile(imageTile, x, y-8, 1, direction + 2);
            else if(attackWindUp < 30)
                r.drawImageTile(imageTile, x, y-8, 2, direction + 2);
            else if(attackWindUp < 45)
                r.drawImageTile(imageTile, x, y-8, 3, direction + 2);
            else if(attackWindUp < 95)
                r.drawImageTile(imageTile, x, y-8, 4, direction + 2);
            else
                r.drawImageTile(imageTile, x, y-8, 5, direction + 2);
            
        }else{
        r.drawImageTile(imageTile, x, y-8, (int)walkcycle, direction);
        }

        if(attacking){
            if(attackWindUp >= 80){
                r.drawImageTile(imageTile, x, y-8, 3, 5);
            }
            if(attackWindUp < 60){}
            else if(attackWindUp < 63){
                r.drawImageTile(imageTile, x, y-8, 0, 5);
            }
            else if(attackWindUp < 66){
                r.drawImageTile(imageTile, x, y-8, 1, 5);
            }
            else if(attackWindUp < 72){
                r.drawImageTile(imageTile, x, y-8, 2, 5);
            }
            else if(attackWindUp < 75){
                r.drawImageTile(imageTile, x, y-8, 0, 4);
            }
            else if(attackWindUp < 77){
                r.drawImageTile(imageTile, x, y-8, 1, 4);
            }
            else if(attackWindUp < 79){
                r.drawImageTile(imageTile, x, y-8, 2, 4);
            }
            else if(attackWindUp < 90){
                r.drawImageTile(imageTile, x, y-8, 4, 5);
            }
            else if(attackWindUp < 95){
                r.drawImageTile(imageTile, x, y-8, 5, 5);
            }
                
            
        }



        if (Path != null)

            if (gm.getShowPath())
                for (int i : Path) {
                    r.drawRect(i % gm.getLevelW() * 16, (i / gm.getLevelW()) * 16, width, height, 0xff0000ff);
                }
    }

    @Override
    public void attack(GameManager gm, GObject target) {
        if(attackWindUp < attackCycles){
            attackWindUp++;
        } 
        else{
            attackWindUp = 0;
            GameManager.AttackSounds.get((int)(Math.random()*3) + 8).play();
            if((int)(Math.random()*5)== 0){
                GameManager.MageSounds.get((int)(Math.random()*6) + 14).play();
            }
            for(int i : getSurroundTiles(gm, ((GUnit) target).getCenterTile())){
                if(owner == 1){
                    if(!gm.getEdamageMap().contains(i)) {
                        gm.getObjects().add(new MFire(GameManager.Fire, 45, i % gm.getLevelW() * 16, i / gm.getLevelW() * 16));
                    }
                    gm.getEdamageMap().add(i);   
                }else{
                    if(!gm.getDamageMap().contains(i)){
                        gm.getObjects().add(new MFire(GameManager.Fire, 45, i % gm.getLevelW() * 16, i / gm.getLevelW() * 16));
                    }
                    gm.getDamageMap().add(i);  
                }
                
                    target.setOpponent(owner);
                
                 
                
            }
            
        }

    }
    public ArrayList<Integer> getSurroundTiles(GameManager gm, int current) {
        int cx = current % gm.getLevelW();
        int cy = current / gm.getLevelW();
        ArrayList<Integer> neighbours = new ArrayList<Integer>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (cx + x < 0 || cx + x > gm.getLevelW() || cy + y < 0 || cy + y > gm.getLevelH()) {
                    continue;
                }
                neighbours.add((cx + x) + (cy + y) * gm.getLevelW()); 
            }
        }
        return neighbours;
    }
}

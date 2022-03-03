package com.game.game.Objects.Units;


import java.util.LinkedList;

import com.game.game.*;
import com.game.engine.*;
import com.game.engine.gfx.*;
import com.game.game.Objects.*;
import com.game.game.Objects.Structures.*;

//Can you recognize the voice actors for all the characters so far? (:
//The miner is once again a little bit of a special unit. By standing on the golden tiles, it is able to provide you with income required to produce more units.
//The miner also has an additional effect where hitting a strucutre, a barrack or castle, deals massively increased damage


public class Miner extends GUnit{
    Image image = new Image("/guy.png");
    ImageTile imageTile;

    public Miner(int x, int y){
        
        GameManager.MinerSounds.get((int)(Math.random()*3)).play();
        tag = "Miner";
        this.x = x;
        this.y = y;
        imageTile = GameManager.Miner;
        this.width = GameManager.TILE_SIZE;
        this.height = GameManager.TILE_SIZE;
        this.size = 1;
        this.width = size * GameManager.TILE_SIZE;
        this.height = size * GameManager.TILE_SIZE;
        Path = new LinkedList<Integer>();

        maxHealth = 50;
        health = maxHealth;
        armour = 0;
        attackCycles = 70;
        speed = 5;
        attackRange = 1; leashRange = 6; visionRange = 12;
    }
    public Miner(int x, int y, int start){
        
        tag = "Miner";
        this.x = x;
        this.y = y;
        imageTile = GameManager.Miner;
        this.width = GameManager.TILE_SIZE;
        this.height = GameManager.TILE_SIZE;
        this.size = 1;
        this.width = size * GameManager.TILE_SIZE;
        this.height = size * GameManager.TILE_SIZE;
        Path = new LinkedList<Integer>();

        maxHealth = 50;
        health = maxHealth;
        armour = 0;
        attackCycles = 70;
        speed = 5;
        attackRange = 1; leashRange = 6; visionRange = 12;
    }

    float walkcycle = 0;
    public void update(GameContainer gc, GameManager gm, float dt) {
        if(remove){
            GameManager.MinerSounds.get((int)(Math.random()*4)+ 16).play();
        }
        super.update(gc,gm,dt);
        
        if(owner == 0)
        gm.getMinerMap()[CenterTile].add(this);
        else if(owner == 1)
        gm.getEMinerMap()[CenterTile].add(this);


        if(walking){
            walkcycle += dt * 10;
            
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
        

        // This shows the little walky guy
        if(attacking){
            if(attackWindUp < 5)
                r.drawImageTile(imageTile, x, y, 0, direction);
            else if(attackWindUp < 20)
                r.drawImageTile(imageTile, x, y, 1, direction + 2);
            else if(attackWindUp < 25)
                r.drawImageTile(imageTile, x, y, 2, direction + 2);
            else if(attackWindUp < 39)
                r.drawImageTile(imageTile, x, y, 3, direction + 2);
            else  if(attackWindUp < 45)
                r.drawImageTile(imageTile, x, y, 4, direction + 2);
            else{
                r.drawImageTile(imageTile, x, y, 5, direction + 2);
            }
        }
        else{
            if(gm.getTiles()[CenterTile] / 13 == 2){
                r.drawImageTile(imageTile, x-4, y-4, (int)(walkcycle) / 2 + 4, direction);
                walkcycle += 0.01;
            }else{
                r.drawImageTile(imageTile, x-4, y-4, (int)walkcycle, direction);
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
            GameManager.AttackSounds.get((int)(Math.random()*1)+10).play();
            attackWindUp = 0;
            ((GUnit) target).setHealth(((GUnit) target).getHealth() - 10);
            if(target instanceof CastleBox || target instanceof BarrackBox){
                ((GUnit) target).setHealth(((GUnit) target).getHealth() - 50);
            }
            if((int)(Math.random()*5)== 0){
                GameManager.MinerSounds.get((int)(Math.random()*3) + 13).play();
            }
            // System.out.println("Attacked " + target.getTag() + " " + owner+"|| "+ ((GUnit) target).getHealth() + "HP left");
            target.setOpponent(owner);
            
        }

    }
}

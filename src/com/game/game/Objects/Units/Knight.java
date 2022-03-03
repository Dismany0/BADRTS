package com.game.game.Objects.Units;


import java.util.LinkedList;
import com.game.game.*;
import com.game.engine.*;
import com.game.engine.gfx.*;
import com.game.game.Objects.*;


//The knight is the same as the other units, except much tankier and melee. It is only capable of fighting in melee range, but besides that, theres
//nothing special code wise in here.
//He has the same loop as every other unit to do his animations.
//Something that might be interesting is that his animation is not 16 by 16 like you would expect.
//Instead, it is 24 by 16 so he has room to swing his sword
public class Knight extends GUnit{
    Image image = new Image("/guy.png");
    ImageTile imageTile;

    public Knight(int x, int y){
        
        GameManager.KnightSounds.get((int)(Math.random()*4)).play();
        tag = "Knight";
        this.x = x;
        this.y = y;
        this.width = GameManager.TILE_SIZE;
        this.height = GameManager.TILE_SIZE;
        this.size = 1;
        this.width = size * GameManager.TILE_SIZE;
        this.height = size * GameManager.TILE_SIZE;
        Path = new LinkedList<Integer>();
        imageTile = GameManager.Knight;
        maxHealth = 200;
        health = maxHealth;
        armour = 20;
        attackCycles = 60;
        speed = 5;
        attackRange = 1; leashRange = 6; visionRange = 12;
    }

    float walkcycle = 0;
    public void update(GameContainer gc, GameManager gm, float dt) {
        if(remove){
            GameManager.KnightSounds.get((int)(Math.random()*3)+ 17).play();
        }
        super.update(gc,gm,dt);
        
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
        // r.fillRect(x, y, width, height, 0xff47d4ff);
        // if(selectStatus != 0)
        // r.fillRect(x, y, width, height, 0xff39b0d4);

        // This shows the little walky guy
        if(attacking){
            if(attackWindUp < 20)
                r.drawImageTile(imageTile, x, y, 0, direction);
            else if(attackWindUp < 45)
                r.drawImageTile(imageTile, x, y, 0, direction + 2);
            else if(attackWindUp < 48)
                r.drawImageTile(imageTile, x, y, 1, direction + 2);
            else if(attackWindUp < 51)
                r.drawImageTile(imageTile, x, y, 2, direction + 2);
            else
                r.drawImageTile(imageTile, x, y, 3, direction + 2);
            
        }
        else{
            r.drawImageTile(imageTile, x-4, y, (int)walkcycle, direction);
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
            ((GUnit) target).setHealth(((GUnit) target).getHealth() - 40);
            // System.out.println("Attacked " + target.getTag() + " " + owner+"|| "+ ((GUnit) target).getHealth() + "HP left");
            GameManager.AttackSounds.get((int)(Math.random()*4) + 4).play();
            if((int)(Math.random()*5)== 0){
                GameManager.KnightSounds.get((int)(Math.random()*4) + 14).play();
            }
            
                target.setOpponent(owner);
            
        }

    }
}

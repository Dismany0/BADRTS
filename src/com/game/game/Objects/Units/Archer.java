package com.game.game.Objects.Units;

//The archer is a cheap unit that is capable of striking quickly at a range. They are able to attack units without recieving damage back. 
//However, once in melee combat, they are quite weak and frail
import java.util.LinkedList;
import com.game.game.*;
import com.game.engine.*;
import com.game.engine.gfx.*;
import com.game.game.Objects.*;

public class Archer extends GUnit{
    Image image = new Image("/guy.png");
    ImageTile imageTile;

    public Archer(int x, int y){
        
        GameManager.ArcherSounds.get((int)(Math.random()*3)).play();
        tag = "Archer";
        this.x = x;
        this.y = y;
        imageTile = GameManager.Archer;
        this.width = GameManager.TILE_SIZE;
        this.height = GameManager.TILE_SIZE;
        this.size = 1;
        this.width = size * GameManager.TILE_SIZE;
        this.height = size * GameManager.TILE_SIZE;
        Path = new LinkedList<Integer>();

        maxHealth = 90;
        health = maxHealth;
        armour = 0;
        attackCycles = 40;
        speed = 8;
        attackRange = 6; leashRange = 10; visionRange = 15;
    }

    //Walkcycles describes their walking animation. I add dt, from game container, onto the walk cycle with each update. Using integer division
    //I can pull the correct sprites out of my imageTile
    float walkcycle = 0;
    public void update(GameContainer gc, GameManager gm, float dt) {
        if(remove){
            GameManager.ArcherSounds.get((int)(Math.random()*2)+ 16).play();
        }
        super.update(gc,gm,dt);
        if(walking){
            walkcycle += dt * 5;
            
        }else{
            walkcycle += dt * 2;
        }
        if(walkcycle > 4){
            walkcycle = 0;
        }
    }
    public void render(GameContainer gc, GameManager gm, Renderer r) {
        super.render(gc, gm, r);
        

        //The commented out hitbox was a testing hitbox.Using the image tile, and the amount of windup the archer has, I was able to create animations.

        // This shows the blue hitbox
        // r.fillRect(x, y, width, height, 0xff35ab80);
        // if(selectStatus != 0)
        // r.fillRect(x, y, width, height, 0xff226e52);

        // This shows the little walky guy
        if(attacking){
            if(attackWindUp < 8)
                r.drawImageTile(imageTile, x, y, 0, direction + 2);
            else if(attackWindUp < 11)
                r.drawImageTile(imageTile, x, y, 1, direction + 2);
            else if(attackWindUp < 37)
                r.drawImageTile(imageTile, x, y, 2, direction + 2);
            else if(attackWindUp < 38)
                r.drawImageTile(imageTile, x, y, 3, direction + 2);
            else if(attackWindUp < 39)
                r.drawImageTile(imageTile, x, y, 4, direction + 2);
            else
                r.drawImageTile(imageTile, x, y, 5, direction + 2);
            
        }else
            r.drawImageTile(imageTile, x, y, (int)walkcycle, direction);

        if (Path != null)

            if (gm.getShowPath())
                for (int i : Path) {
                    r.drawRect(i % gm.getLevelW() * 16, (i / gm.getLevelW()) * 16, width, height, 0xff0000ff);
                }
    }
    //When the unit is finally ready to attack, some sound effects play and the damage lands.
    @Override
    public void attack(GameManager gm, GObject target) {
        if(attackWindUp < attackCycles){
            attackWindUp++;
        } 
        else{
            attackWindUp = 0;
            ((GUnit) target).setHealth(((GUnit) target).getHealth() - 20);
            GameManager.AttackSounds.get((int)(Math.random()*3)).play();
            target.setOpponent(owner);
            if((int)(Math.random()*5)== 0){
                GameManager.ArcherSounds.get((int)(Math.random()*2) + 15).play();
            }
            // System.out.println("Attacked " + target.getTag() + " " + owner+"|| "+ ((GUnit) target).getHealth() + "HP left");
        }

    }
}

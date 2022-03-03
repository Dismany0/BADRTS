package com.game.game.Objects.Units;

import java.util.LinkedList;

import com.game.game.*;
import com.game.engine.*;
import com.game.engine.gfx.*;
import com.game.game.Objects.*;


//The rogue is a melee unit that attacks from the same range the archer does. However, when it attacks, it instantly blinks into melee range.
//I originally designed his ability in order to counter the mage. I thought that in the late game, spamming mages with aoe might end up being
//the most powerful strategy. So the rogue was created with the perfect stats to one shot the mage just a split second before she is able to cast

//Also this guy has so many frames of animation it hurts to type all those if statements
public class Rogue extends GUnit{
    Image image = new Image("/guy.png");
    ImageTile imageTile;

    public Rogue(int x, int y){
        
        GameManager.RogueSounds.get((int)(Math.random()*3)).play();
        tag = "Rogue";
        imageTile = GameManager.Rogue;
        this.x = x;
        this.y = y;
        this.width = GameManager.TILE_SIZE;
        this.height = GameManager.TILE_SIZE;
        this.size = 1;
        this.width = size * GameManager.TILE_SIZE;
        this.height = size * GameManager.TILE_SIZE;
        Path = new LinkedList<Integer>();

        maxHealth = 120;
        health = maxHealth;
        armour = 0;
        speed = 12;
        attackCycles = 120;
        attackRange = 7; leashRange = 8; visionRange = 10;
    }
    float walkcycle = 0;
    public void update(GameContainer gc, GameManager gm, float dt) {
        if(remove){
            GameManager.RogueSounds.get((int)(Math.random()*3)+ 16).play();
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
        // r.fillRect(x, y, width, height, 0xffbf322a);
        // if(selectStatus != 0)
        // r.fillRect(x, y, width, height, 0xff962923);

        // This shows the little walky guy

        if(attacking){
            if(attackWindUp < 10)
                r.drawImageTile(imageTile, x, y, 0, direction);
            else if(attackWindUp < 15)
                r.drawImageTile(imageTile, x, y, 0, direction + 2);
            else if(attackWindUp < 19)
                r.drawImageTile(imageTile, x, y, 1, direction + 2);
            else if(attackWindUp < 23)
                r.drawImageTile(imageTile, x, y, 2, direction + 2);
                else if(attackWindUp < 28)
                r.drawImageTile(imageTile, x, y, 3, direction + 2);
            else if (attackWindUp < 85)
                if(attackWindUp % 8 <= 4){
                    r.drawImageTile(imageTile, x, y, 4, direction + 2);
                }else{
                    r.drawImageTile(imageTile, x, y, 5, direction);
                }
            else if(attackWindUp < 90)
                r.drawImageTile(imageTile, x, y, 6, direction + 2);
                else if(attackWindUp < 95)
                r.drawImageTile(imageTile, x, y, 7, direction + 2);
                else if(attackWindUp < 100)
                r.drawImageTile(imageTile, x, y, 8, direction + 2);
                else if(attackWindUp < 105)
                r.drawImageTile(imageTile, x, y, 9, direction + 2);
                else
                r.drawImageTile(imageTile, x, y, 10, direction + 2);
                
            
        }
        else{
            if(walking)
                r.drawImageTile(imageTile, x, y, (int)walkcycle, direction);
            else{
                r.drawImageTile(imageTile, x, y, ((int)walkcycle% 2 * 4), direction);
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
        
        if (attackWindUp == 90) {
            target.setOpponent(owner);
            GameManager.AttackSounds.get((int)(Math.random()*2)+11).play();
            ((GUnit) target).setHealth(((GUnit) target).getHealth() - 80);
            this.x = target.getX();
            this.y = target.getY();

            if(((GUnit) target).getHealth() <= 0){
                if(target instanceof Mage){
                    if((int)(Math.random()*5)== 0){
                    GameManager.RogueSounds.get(15).play();}
                }else{
                    int test = (int)(Math.random() * 2) + 13;
                
                    GameManager.RogueSounds.get(test).play();
                }
                gm.getObjects().add(new RSlash(GameManager.RogueSlash, 38, target.getX(), target.getY()));
                
                    
                
            }
            attackWindUp++;
            
            // System.out.println("Attacked " + target.getTag() + " " + owner+"|| "+ ((GUnit) target).getHealth() + "HP left");
        }
        else if(attackWindUp < attackCycles){
            attackWindUp++;
        } 
        else if (attackWindUp == attackCycles){
            attackWindUp = 0;
            
            
        }

    }
}

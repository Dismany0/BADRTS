package com.game.game.Objects.Units;


import java.util.LinkedList;
import com.game.game.*;
import com.game.engine.*;
import com.game.engine.gfx.*;
import com.game.game.Objects.*;

//This unit is a bit outdated and uses different code than the rest of the units. However, he acted as a testing dummy with 1000 hp and crazy attack
//while i experiemented with the other, non joke stick figure units.

//He is in the game, fully animated and working, but theres no way to summon him without using some cheating. So technically, he is not in the game
public class guy extends GUnit {
    Image image = new Image("/guy.png");
    ImageTile imageTile;

    float temp = 0f;

    public guy(int x, int y) {
        tag = "Guy";
        owner = 1;
        attackWindUp = -1;
        this.x = x;
        this.y = y;
        this.width = GameManager.TILE_SIZE;
        this.height = GameManager.TILE_SIZE;
        this.size = 1;
        this.width = size * GameManager.TILE_SIZE;
        this.height = size * GameManager.TILE_SIZE;
        Path = new LinkedList<Integer>();
        imageTile = GameManager.guy;

        maxHealth = 1000;
        health = maxHealth;
        armour = 1000;
        attackCycles = 60;
        speed = 10;
        attackRange = 2; leashRange = 10; visionRange = 5;

    }


    public void update(GameContainer gc, GameManager gm, float dt) {
        super.update(gc,gm,dt);
        if(!Path.isEmpty()){
        temp += dt * 20;
        if(temp > 4){
            temp =0;
        }
        }
    }

    public void render(GameContainer gc, GameManager gm, Renderer r) {
        super.render(gc, gm, r);
        

        // This shows the blue hitbox
        // r.fillRect(x, y, width, height, 0xffff00ff);
        // if(selectStatus != 0)
        // r.fillRect(x, y, width, height, 0xffdc00dc);

        // This shows the little walky guy
        r.drawImageTile(imageTile, x, y, (int) temp, left);

        if (Path != null)

            if (gm.getShowPath())
                for (int i : Path) {
                    r.drawRect(i % gm.getLevelW() * 16, (i / gm.getLevelW()) * 16, width, height, 0xff0000ff);
                }
    }
    public void attack(GameManager gm, GObject target) {

        attackWindUp++;
        if(attackWindUp > 0){
            
            int r = (int)(Math.random() * 3);
            if(r == 0){
                ((GUnit) target).setHealth(((GUnit) target).getHealth() - 70);
                gm.getObjects().add(new RSlash(GameManager.RogueSlash, 38, target.getX(), target.getY()));
                attackWindUp = -30;
            }else if (r==1){
                ((GUnit) target).setHealth(((GUnit) target).getHealth() - 35);
                gm.getObjects().add(new DSmoke(GameManager.Smoke, 30, target.getX(), target.getY()));
                attackWindUp = -5;
            }
            else if(r == 2){
                int loc = ((target.getX() / 16) % gm.getLevelW()) + ((target.getY() / 16) / gm.getLevelW()) * gm.getLevelW();
                if(owner == 1){
                    gm.getObjects().add(new MFire(GameManager.Fire, 45, target.getX(), target.getY()));
                    gm.getEdamageMap().add(loc);   
                }else{
                    gm.getObjects().add(new MFire(GameManager.Fire, 45, target.getX(), target.getY()));
                    gm.getDamageMap().add(loc);  
                } 
                attackWindUp = -25;
            }
            
            target.setOpponent(owner);
                
        }
        
}

}

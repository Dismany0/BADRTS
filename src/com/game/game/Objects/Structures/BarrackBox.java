package com.game.game.Objects.Structures;
//This is a part of the barrack's hitbox. Originally, I planned to have structures and unit act as eseparate entities
//However, i found the unit code to be much more useful, as it provided collision boxes, and was mapped to a useful unit grid on the main map

//Therefore, since the barracks, all structures are now just composed of multiple invisible units standing together. These are the components of the barracks


import com.game.game.*;
import com.game.engine.*;
import com.game.game.Objects.*;


public class BarrackBox extends GUnit{
    //The barracks that they create is here
    Barracks barracks;

    public BarrackBox(int x, int y, Barracks barracks){
        this.x = x;
        this.y=y;
        this.barracks = barracks;
        this.owner = barracks.getOwner();
        this.opponent = barracks.getOwner();
        health = 10000;
        CenterTile = (int) (((x + width / 2) / 16) + ((y + height / 2) / 16) * 128);
        this.tag = "barrackbox";
    }


    public void update(GameContainer gc, GameManager gm, float dt) {
        
        CenterTile = (int) (((x + width / 2) / 16) + ((y + height / 2) / 16) * gm.getLevelW());
        if(owner == 0)
            gm.getUnitMap()[CenterTile].add(this);
        else if(owner == 1)
            gm.getEUnitMap()[CenterTile].add(this);
        else{
            gm.getEUnitMap()[CenterTile].add(this);
            gm.getUnitMap()[CenterTile].add(this);
        }

        //it sets the barracks' opponent to this opponent

        if(opponent != barracks.getOwner()){
            barracks.setOpponent(opponent);
        }
            // for(BarrackBox i : barracks.getHitboxes()){
            //     i.setOpponent(opponent);
            // }
            
            // barracks.setOpponent(opponent);
            //same with select status
        if(selectStatus == 1){
            barracks.setSelectStatus(selectStatus);
            selectStatus = 0;
        }
        

        //same with health
        if(barracks.getHealth() > 0){
            barracks.setHealth(barracks.getHealth() - (10000 - health));
            health = 10000;
        }else{
            health = 0;
        }
        
    }
    //testing code for rendering
    public void render(GameContainer gc, GameManager gm, Renderer r) {
        // r.drawRect(x,y, 16, 16, 0xffffffff);
    }
    public void attack(GameManager gm, GObject target) {
        return;

    }

    /**
     * @return the barracks
     */
    public Barracks getBarracks() {
        return barracks;
    }

    /**
     * @param barracks the barracks to set
     */
    public void setBarracks(Barracks barracks) {
        this.barracks = barracks;
    }
    
}

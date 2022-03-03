package com.game.game.Objects.Structures;

import com.game.game.*;
import com.game.engine.*;
import com.game.game.Objects.*;

//Similar to the hitboxes for the barracks, except this one is for the castle. There are 25 of them per castle because a castle is a 5x5 building 
//unlike the barrack's 3x3

public class CastleBox extends GUnit{
    Castle castle;

    public CastleBox(int x, int y, Castle castle){
        this.x = x;
        this.y=y;
        opponent = 0;
        this.castle = castle;
        this.owner = castle.getOwner();
        health = 10000;
        CenterTile = (int) (((x + width / 2) / 16) + ((y + height / 2) / 16) * 128);
        this.tag = "castlebox";
    }

    public void update(GameContainer gc, GameManager gm, float dt) {
        
        CenterTile = (int) (((x + width / 2) / 16) + ((y + height / 2) / 16) * gm.getLevelW());
        if(owner == 0)
            gm.getUnitMap()[CenterTile].add(this);
        else if(owner == 1)
            gm.getEUnitMap()[CenterTile].add(this);


        if(selectStatus == 1){
            castle.setSelectStatus(selectStatus);
            selectStatus = 0;
        }
        

        if(castle.getHealth() > 0){
            castle.setHealth(castle.getHealth() - (10000 - health));
            health = 10000;
        }else{
            health = 0;
        }
        
        
    }
    public void render(GameContainer gc, GameManager gm, Renderer r) {
        // r.drawRect(x,y, 16, 16, 0xffffffff);
    }
    public void attack(GameManager gm, GObject target) {
        return;

    }

    /**
     * @return the castle
     */
    public Castle getCastle() {
        return castle;
    }

    /**
     * @param castle the castle to set
     */
    public void setCastle(Castle castle) {
        this.castle = castle;
    }
    
}

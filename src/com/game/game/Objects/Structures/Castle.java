package com.game.game.Objects.Structures;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import com.game.game.*;
import com.game.engine.*;
import com.game.engine.gfx.*;
import com.game.game.Objects.*;
import com.game.game.Objects.Units.*;

public class Castle extends GStructure{
    float animation;
    ImageTile imageTile;
    CastleBox[] hitboxes = new CastleBox[25];
    int tileX; int tileY;
    LinkedList<GUnit> queue = new LinkedList<GUnit>();
    LinkedList<Integer>costQueue = new LinkedList<Integer>();
    
    public Castle(int tileX, int tileY, GameManager gm, int owner){
        tag = "castle";

        x = tileX * 16;
        y = tileY * 16;
        health = 10000;
        maxHealth = 10000;
        this.owner = owner;
        imageTile = GameManager.Castle;
        this.OccupiedTiles = new int[size*size];
        int counter = 0;
        for(int x = 0; x <5 ; x++){
            for(int y = 0; y < 5; y++){
                hitboxes[counter] = new CastleBox((tileX + x) * 16, (tileY+y) * 16, this);
                gm.getObjects().add(hitboxes[counter]);
                counter++;
            }
        }

        animation = 0;
        
        
    }

    public void update(GameContainer gc, GameManager gm, float dt) {
        super.update(gc, gm, dt);
        animation += dt * 10;

        
        if(animation > 4){
            animation = 0;
        }
        if(health <= 0){
            for(CastleBox i : hitboxes){
                i.setRemove(true);
                i.setHealth(0);
            }
            remove = true;
            
        }

        if(!gm.getGodSelect()){
            if(selectStatus == 1){
                if(gc.getInput().isKeyDown(KeyEvent.VK_B) && gm.getPlayerGold() >= 50 && queue.size() <= 5){
                    gm.setPlayerGold(gm.getPlayerGold()- 50);
                    queue.add(new Miner(x + 32, y + 80));
                    costQueue.add(10*60);
                } 
                else if(gc.getInput().isKeyDown(KeyEvent.VK_Z) && gm.getPlayerGold() >= 100 && queue.size() <= 5){
                    gm.setPlayerGold(gm.getPlayerGold()- 100);
                    queue.add(new Knight(x + 32, y + 80));
                    costQueue.add(12*60);
                }
                else if(gc.getInput().isKeyDown(KeyEvent.VK_X) && gm.getPlayerGold() >= 100 && queue.size() <= 5){
                    gm.setPlayerGold(gm.getPlayerGold()- 100);
                    queue.add(new Archer(x + 32, y + 80));
                    costQueue.add(12*60);
                }
                else if(gc.getInput().isKeyDown(KeyEvent.VK_C) && gm.getPlayerGold() >= 200 && queue.size() <= 5){
                    gm.setPlayerGold(gm.getPlayerGold()- 200);
                    queue.add(new Rogue(x + 32, y + 80));
                    costQueue.add(15*60);
                }
                else if(gc.getInput().isKeyDown(KeyEvent.VK_V) && gm.getPlayerGold() >= 200 && queue.size() <= 5){
                    gm.setPlayerGold(gm.getPlayerGold()- 200);
                    queue.add(new Mage(x + 32, y + 80));
                    costQueue.add(15*60);
                }
            }
        }

        if(!queue.isEmpty()){
            costQueue.set(0,costQueue.getFirst() - 1);
            if(costQueue.getFirst() <= 0){
                costQueue.removeFirst();
                queue.getFirst().setOwner(owner);
                gm.getObjects().add(queue.removeFirst());
            }
        }
    }
        
    
    public void render(GameContainer gc, GameManager gm, Renderer r) {
       r.drawImageTile(imageTile, x, y, (int)animation, owner);
       if(selectStatus == 1 && owner == 0){
        r.drawRect(x,y,80,80,0xff0000ff);
       }
       if(gm.getShowUnitHealth()){
        r.fillRect(x, y - 8, 80, 1, 0xff000000);
        r.fillRect(x, y - 8,(int)( 80 * ((float)health / (float)maxHealth)), 1, 0xffff2222);
       }
    // 
        

    }

    /**
     * @return the hitboxes
     */
    public CastleBox[] getHitboxes() {
        return hitboxes;
    }

    /**
     * @param hitboxes the hitboxes to set
     */
    public void setHitboxes(CastleBox[] hitboxes) {
        this.hitboxes = hitboxes;
    }

    /**
     * @return the tileX
     */
    public int getTileX() {
        return tileX;
    }

    /**
     * @param tileX the tileX to set
     */
    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    /**
     * @return the tileY
     */
    public int getTileY() {
        return tileY;
    }

    /**
     * @param tileY the tileY to set
     */
    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    /**
     * @return the queue
     */
    public LinkedList<GUnit> getQueue() {
        return queue;
    }

    /**
     * @param queue the queue to set
     */
    public void setQueue(LinkedList<GUnit> queue) {
        this.queue = queue;
    }

    /**
     * @return the costQueue
     */
    public LinkedList<Integer> getCostQueue() {
        return costQueue;
    }

    /**
     * @param costQueue the costQueue to set
     */
    public void setCostQueue(LinkedList<Integer> costQueue) {
        this.costQueue = costQueue;
    }
}

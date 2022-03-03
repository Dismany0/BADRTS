package com.game.game.Objects.Structures;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import com.game.game.*;
import com.game.engine.*;
import com.game.engine.gfx.*;
import com.game.game.Objects.*;
import com.game.game.Objects.Units.*;

public class Barracks extends GStructure{
    //The actual barracks themselves are the only neutral structure in the game. They are capable of acting almost as a second castle, and training troops
    ImageTile imageTile;
    BarrackBox[] hitboxes = new BarrackBox[9];
    int tileX; int tileY;
    LinkedList<GUnit> queue = new LinkedList<GUnit>();
    LinkedList<Integer>costQueue = new LinkedList<Integer>();
    int spawnTime = 0;
    public Barracks(int tileX, int tileY, GameManager gm, int owner){
        tag = "Barrack";

        x = tileX * 16;
        y = tileY * 16;
        this.tileX = tileX;
        this.tileY = tileY;

        health = 2500;
        maxHealth = 2500;
        this.owner = owner;
        imageTile = GameManager.Barracks;
        this.OccupiedTiles = new int[size*size];
        int counter = 0;
        for(int x = 0; x <3 ; x++){
            for(int y = 0; y < 3; y++){
                hitboxes[counter] = new BarrackBox((tileX + x) * 16, (tileY+y) * 16, this);
                gm.getObjects().add(hitboxes[counter]);
                counter++;
            }
        }
    }

    public Barracks(int tileX, int tileY, GameManager gm, int owner, LinkedList<GUnit> queue, LinkedList<Integer> costQueue){
        tag = "Barrack";
        this.queue = queue;
        this.costQueue = costQueue;
        x = tileX * 16;
        y = tileY * 16;
        this.tileX = tileX;
        this.tileY = tileY;

        health = 2500;
        maxHealth = 2500;
        this.owner = owner;
        imageTile = GameManager.Barracks;
        this.OccupiedTiles = new int[size*size];
        int counter = 0;
        for(int x = 0; x <3 ; x++){
            for(int y = 0; y < 3; y++){
                hitboxes[counter] = new BarrackBox((tileX + x) * 16, (tileY+y) * 16, this);
                gm.getObjects().add(hitboxes[counter]);
                counter++;
            }
        }
    }

    public void update(GameContainer gc, GameManager gm, float dt) {
        super.update(gc, gm, dt);
        
        //assuming the player does not have my developer cheats on, you are able to spawn units when selecting the barracks
        if(!gm.getGodSelect()){
            if(selectStatus == 1 && owner != -1){
                if(gc.getInput().isKeyDown(KeyEvent.VK_B) && gm.getPlayerGold() >= 50 && queue.size() <= 5){
                    gm.setPlayerGold(gm.getPlayerGold()- 50);
                    queue.add(new Miner(x + 16, y + 48));
                    costQueue.add(10*60);
                } 
                else if(gc.getInput().isKeyDown(KeyEvent.VK_Z) && gm.getPlayerGold() >= 100 && queue.size() <= 5){
                    gm.setPlayerGold(gm.getPlayerGold()- 100);
                    queue.add(new Knight(x + 16, y + 48));
                    costQueue.add(12*60);
                }
                else if(gc.getInput().isKeyDown(KeyEvent.VK_X) && gm.getPlayerGold() >= 100 && queue.size() <= 5){
                    gm.setPlayerGold(gm.getPlayerGold()- 100);
                    queue.add(new Archer(x + 16, y + 48));
                    costQueue.add(12*60);
                }
                else if(gc.getInput().isKeyDown(KeyEvent.VK_C) && gm.getPlayerGold() >= 200 && queue.size() <= 5){
                    gm.setPlayerGold(gm.getPlayerGold()- 200);
                    queue.add(new Rogue(x + 16, y + 48));
                    costQueue.add(15*60);
                }
                else if(gc.getInput().isKeyDown(KeyEvent.VK_V) && gm.getPlayerGold() >= 200 && queue.size() <= 5){
                    gm.setPlayerGold(gm.getPlayerGold()- 200);
                    queue.add(new Mage(x + 16, y + 48));
                    costQueue.add(15*60);
                }
            }
        }

        //The unit is finally added to the game after its timedown finishes
        if(!queue.isEmpty()){
            costQueue.set(0,costQueue.getFirst() - 1);
            if(costQueue.getFirst() <= 0){
                costQueue.removeFirst();
                queue.getFirst().setOwner(owner);
                gm.getObjects().add(queue.removeFirst());
            }
        }


        //destroys all its hitboxes when the unit's health reaches zero
        if(health <= 0){
            for(BarrackBox i : hitboxes){
                i.setRemove(true);
                i.setHealth(0);
                
            }
        
            //Because barracks do not die, but are rather stationary buildings, they switch owners when they die.
            remove = true;
            Barracks copy = new Barracks(x / 16, y / 16, gm, opponent, queue, costQueue);
            gm.getObjects().add(copy);
        }
        
        
    }
    public void render(GameContainer gc, GameManager gm, Renderer r) {
       r.drawImageTile(imageTile, x, y, 0, owner + 1);
       if(selectStatus == 1 && owner == 0){
            r.drawRect(x,y,48,48,0xff0000ff);
       }
       if(gm.getShowUnitHealth()){
        r.fillRect(x, y - 8, 48, 1, 0xff000000);
        r.fillRect(x, y - 8,(int)( 48 * ((float)health / (float)maxHealth)), 1, 0xffff2222);
       }
    // 
        

    }

    /**
     * @return the hitboxes
     */
    public BarrackBox[] getHitboxes() {
        return hitboxes;
    }

    /**
     * @param hitboxes the hitboxes to set
     */
    public void setHitboxes(BarrackBox[] hitboxes) {
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

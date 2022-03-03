package com.game.game.Objects;

import java.util.LinkedList;

import com.game.game.*;
import com.game.engine.*;

//The GStructure, despite being planned to be a very important class, actually went relatively unused. Currently, both the castle and the
//barracks still extend from the GStructure. However, the amount of code they borrow is relatively low, and likely could've been tweaked to extend 
//GObject, or even GUnit instead. However, I would've liked to be able to use more classes in order to better organize my code
//Theres honestly nothing in here that is interesting.
public class GStructure extends GObject{
    protected int[] OccupiedTiles;
    protected int health;
    protected int maxHealth;
    protected int size;

    public void update(GameContainer gc, GameManager gm, float dt) {
        for(int i : OccupiedTiles){
            gm.getStructureMap()[i] = this;
        }
        
    }
    public void render(GameContainer gc, GameManager gm, Renderer r) {

    }

    /**
     * @return the occupiedTiles
     */
    public int[] getOccupiedTiles() {
        return OccupiedTiles;
    }

    /**
     * @param occupiedTiles the occupiedTiles to set
     */
    public void setOccupiedTiles(int[] occupiedTiles) {
        OccupiedTiles = occupiedTiles;
    }

    /**
     * @return the health
     */
    public int getHealth() {
        return health;
    }

    /**
     * @param health the health to set
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * @return the maxHealth
     */
    public int getMaxHealth() {
        return maxHealth;
    }
    public LinkedList<GUnit> getQueue() {
        return null;
    }

    public LinkedList<Integer> getCostQueue(){
        return null;
    }
    /**
     * @param maxHealth the maxHealth to set
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }
}

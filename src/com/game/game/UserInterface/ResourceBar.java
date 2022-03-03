package com.game.game.UserInterface;

import com.game.game.*;
import com.game.engine.*;


import java.awt.Rectangle;

//once again, very nice a simple, a black bar that contains some text to display what your current gold is at. If you have your developer
//cheats enabled, you are also able to see your opponent's gold.
public class ResourceBar {
    private int width = 250;
    private int height = 20;
    private Rectangle hitbox = new Rectangle(30, 0, width, height);


    public ResourceBar( GameManager gm) {
       
    }

    public void update(GameContainer gc, GameManager gm, Float dt) {
        
    }

    public boolean intersects(int mouseX, int mouseY) {

        if (this.getHitbox().contains(mouseX, mouseY)) {
            return true;
        }
        return false;
    }

    public void render(GameManager gm, Renderer r) {

        r.fillRect(r.getCX() + 30,  r.getCY(), width, height, 0xff000000);
        r.drawText("Available gold: " + Math.abs(gm.getPlayerGold()) + " gold", r.getCX() + 50, 5 + r.getCY(), 0xffffde00);
        if(gm.getGodSelect())
        r.drawText("Enemy gold: " + gm.getEnemyGold() + " gold", r.getCX() + 50, 12 + r.getCY(), 0xffffde00);
    }

    /**
     * @return the hitbox
     */
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
     * @param hitbox the hitbox to set
     */
    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
}

package com.game.game.UserInterface;

import com.game.game.*;
import com.game.engine.*;


import java.awt.Rectangle;

//There are four elements that together create the user interface. The game timer is one of them. It is located at the top right corner and displays
//in minutes and seconds how long the game has been happening.
public class GameTimer {
    private int width = 250;
    private int height = 20;
    private Rectangle hitbox = new Rectangle(640-30-width, 0, width, height);

    private float CurrentTime = 0f;
    public GameTimer( GameManager gm) {
       
    }

    public void update(GameContainer gc, GameManager gm, Float dt) {
        CurrentTime += dt;
        // System.out.println(CurrentTime + " " + dt);
        
    }

    public boolean intersects(int mouseX, int mouseY) {

        if (this.getHitbox().contains(mouseX, mouseY)) {
            return true;
        }
        return false;
    }

    public void render(GameManager gm, Renderer r) {
        int seconds = (int) (CurrentTime) % 60;
        int minutes = (int) (CurrentTime) / 60;
        String time = String.format("%02d:%02d", minutes, seconds);
        r.fillRect(640 - width - 30 + r.getCX(),  r.getCY(), width, height, 0xff000000);
        r.drawText("Current Time is: " + time , 640 - width - 10 + r.getCX(), 3 + r.getCY(), 0xffffffff);
        r.drawText(" Enemy Difficulty: " + gm.getDifficulty() , 640 - width - 10 + r.getCX(), 10 + r.getCY(), 0xffffffff);
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

    /**
     * @return the currentTime
     */
    public float getCurrentTime() {
        return CurrentTime;
    }

    /**
     * @param currentTime the currentTime to set
     */
    public void setCurrentTime(float currentTime) {
        CurrentTime = currentTime;
    }
}

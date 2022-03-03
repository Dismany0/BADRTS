package com.game.game.UserInterface;

import com.game.game.*;
import com.game.engine.*;
import com.game.game.Objects.*;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

//The minimap, located at the bottom left of the screen, is perhaps the most useful one, as well as containing the most features. 
//Without touching anything, it shows a scaled down version of the map, complete with all the units and buildings. Clicking on the map, allows you to
//move your camera at a much higher pace. You are even able to control units through clicking on the minimap.
public class Minimap {
    private int width = 128;
    private int height = 128;
    private Rectangle hitbox = new Rectangle(0, 480 - height, width, height);


    public Minimap( GameManager gm) {
       
    }

    public void update(GameContainer gc, GameManager gm, Float dt) {
        if (gc.getInput().isButtonDown(MouseEvent.BUTTON1)) {

        }
        if (gc.getInput().isButton(MouseEvent.BUTTON1)) {
            if (this.intersects(gc.getInput().getMouseX(), gc.getInput().getMouseY())) {
                gm.getCamera().setCX(gc.getInput().getMouseX() * 16f);
                gm.getCamera().setCY((gc.getInput().getMouseY() - (480 - height)) * 16f);
            }
        }
    }

    public boolean intersects(int mouseX, int mouseY) {

        if (this.getHitbox().contains(mouseX, mouseY)) {
            return true;
        }
        return false;
    }

    public void render(GameManager gm, Renderer r) {

        // r.fillRect(r.getCX(), 480 - height + r.getCY(), gm.getLevelW(),
        // gm.getLevelH(), 0xffff0000);
        
        for(int y = 0; y < gm.getLevelH(); y++){
            for(int x = 0; x < gm.getLevelH(); x++){
               
                r.setPixel( x, (480 - height ) + y , gm.getLevel().getP()[x+y*gm.getLevelW()]);
                
            }
        }
        for (ArrayList<GUnit> unitList : gm.getUnitMap()) {
            for(GUnit unit : unitList){
                r.setPixel(unit.getCenterTile() % gm.getLevelW(), unit.getCenterTile() / gm.getLevelW()+(480-128), 0xff0000ff);
            }

        }
        for (ArrayList<GUnit> unitList : gm.getEUnitMap()) {
            for(GUnit unit : unitList){
                r.setPixel(unit.getCenterTile() % gm.getLevelW(), unit.getCenterTile() / gm.getLevelW()+(480-128), 0xffff0000);
            }

        }

        r.drawRect((int)(r.getCX() + r.getCX()/ 16),(int)((480 - height + r.getCY()) + r.getCY()/ 16), 640 / 16, 480 / 16, 0xff00ff00);
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

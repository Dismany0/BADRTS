package com.game.game;
import com.game.game.UserInterface.*;
import com.game.engine.GameContainer;
import com.game.engine.Renderer;
public class UI {
    Minimap minimap;
    ObjectBox objectBox;
    ResourceBar resourceBar;
    GameTimer gameTimer;
    

    //The UI object contains the other four objects that make up the components of the UI. It is held together in a single object for organization
    //It also allows it to share a few methods, such as the .intersects to check if the hitbox of the mouse is within the UI.
    public UI( GameManager gm){
        minimap = new Minimap( gm);
        objectBox = new ObjectBox();
        resourceBar = new ResourceBar(gm);
        gameTimer = new GameTimer(gm);
    }
    public void update(GameContainer gc, GameManager gm, Float dt){
        minimap.update(gc, gm, dt);
        objectBox.update(gc, gm, dt);
        resourceBar.update(gc,gm,dt);
        gameTimer.update(gc,gm,dt);
    }
    //Here, we make extreme use of the zdepth. Remember that values with a higher zdepth are always rendered on top. In this case, the UI will ALWAYS
    //be on top. So it is assigned a value of 65535.
    public void render(GameManager gm, Renderer r){
        r.setzDepth(65535);
        minimap.render(gm, r);
        objectBox.render(gm, r);
        resourceBar.render(gm,r);
        gameTimer.render(gm,r);
        r.setzDepth(0);
    }
    public boolean intersects(int mouseX, int mouseY){
        if(minimap.intersects(mouseX, mouseY))
            return true;
        if(objectBox.intersects(mouseX, mouseY))
            return true;
            if(resourceBar.intersects(mouseX, mouseY))
            return true;
            if(gameTimer.intersects(mouseX, mouseY))
            return true;

        return false;
    }

    /**
     * @return the minimap
     */
    public Minimap getMinimap() {
        return minimap;
    }

    /**
     * @param minimap the minimap to set
     */
    public void setMinimap(Minimap minimap) {
        this.minimap = minimap;
    }

    /**
     * @return the objectBox
     */
    public ObjectBox getObjectBox() {
        return objectBox;
    }

    /**
     * @param objectBox the objectBox to set
     */
    public void setObjectBox(ObjectBox objectBox) {
        this.objectBox = objectBox;
    }

    /**
     * @return the resourceBar
     */
    public ResourceBar getResourceBar() {
        return resourceBar;
    }

    /**
     * @param resourceBar the resourceBar to set
     */
    public void setResourceBar(ResourceBar resourceBar) {
        this.resourceBar = resourceBar;
    }

    /**
     * @return the gameTimer
     */
    public GameTimer getGameTimer() {
        return gameTimer;
    }

    /**
     * @param gameTimer the gameTimer to set
     */
    public void setGameTimer(GameTimer gameTimer) {
        this.gameTimer = gameTimer;
    }
}

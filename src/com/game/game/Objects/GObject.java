package com.game.game.Objects;

import com.game.game.*;
import com.game.engine.*;
import com.game.engine.gfx.*;

//The Big Gobject. The GObject, or game object, is what all of my other game objects come from. It is an abstract class, because I will never
//intend to create only a gobject, that comes with a few standard variables that I think the majority of objects in the game will have.
//Going down the list

public abstract class GObject {
    //owner 0 means it is friendly, owner 1 means it is enemy, owner -1 means it is neutral
    protected int opponent;
    protected int owner = 0;
    //These two are pictures for the object
    protected Image image;
    protected ImageTile imageTile;
    //The location and default size. Size is used for checking hitboxes
    protected int x, y;
    protected int width = 1, height = 1;
    //When remove is true, it is tossed out of the main arrayList on the next update cycle. 
    protected boolean remove = false;
    //A tag for testing purposes so i could identify which unit was being affected
    protected String tag = "";
    //And finally, whether or not it was selected
    protected int selectStatus;

    //Everything has a update and render. So I made these abstract so anyone can use them.
    public abstract void update(GameContainer gc, GameManager gm, float dt );
    public abstract void render(GameContainer gc, GameManager gm, Renderer r);

    public int getHealth(){
        return 0;
    }
    public int getMaxHealth(){
        return 0;
    }
    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the remove
     */
    public boolean isRemove() {
        return remove;
    }

    /**
     * @param remove the remove to set
     */
    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return the selectStatus
     */
    public int getSelectStatus() {
        return selectStatus;
    }

    /**
     * @param selectStatus the selectStatus to set
     */
    public void setSelectStatus(int selectStatus) {
        this.selectStatus = selectStatus;
    }

    /**
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return the imageTile
     */
    public ImageTile getImageTile() {
        return imageTile;
    }

    /**
     * @param imageTile the imageTile to set
     */
    public void setImageTile(ImageTile imageTile) {
        this.imageTile = imageTile;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the owner
     */
    public int getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(int owner) {
        this.owner = owner;
    }

    /**
     * @return the opponent
     */
    public int getOpponent() {
        return opponent;
    }

    /**
     * @param opponent the opponent to set
     */
    public void setOpponent(int opponent) {
        this.opponent = opponent;
    }
}

package com.game.engine;

//Making an abstract class seems to fix some issues i had with something about threads having static methods that could not be used in my game
//when I create an abstract class and create the game based on the abstract class, which is included in the game container, it works well.
public abstract class AbstractGame {
    public abstract void update(GameContainer gc, float dt );
    public abstract void render(GameContainer gc, Renderer r);

}

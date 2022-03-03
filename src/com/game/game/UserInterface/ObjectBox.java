package com.game.game.UserInterface;

import com.game.game.*;
import com.game.engine.*;
import com.game.engine.gfx.*;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import com.game.game.Objects.*;
import com.game.game.Objects.Structures.*;
import com.game.game.Objects.Units.*;

//The object box is a purely visual box in the bottom right that shows you your currently selected unit. In this game, when more than one unit is selected
//The most recently created unit is prioritized. When a unit or structure is selected, you will be able to see its hp, along with its queue if it is a structure.
//You also see the prices for units, and cna create them, when selecting a structure.
public class ObjectBox {
    private int width = 128;
    private int height = 128;
    private ImageTile imageTile = GameManager.SelectedUnit;
    private Rectangle hitbox = new Rectangle(640 - width, 480 - height, width, height);

    private boolean lastFrameNull = true;
    private GObject renderOb = null;

    public ObjectBox(){

    }
    public void update(GameContainer gc, GameManager gm, float dt) {


        if(renderOb != null){
            if(renderOb instanceof BarrackBox){
                renderOb = ((BarrackBox) renderOb).getBarracks();
            }else if(renderOb instanceof CastleBox){
                renderOb = ((CastleBox) renderOb).getCastle();
            }else if(renderOb instanceof GUnit){
            }else{
            }
            if(renderOb.getHealth() <=0){
                renderOb = null;
            }
            if(lastFrameNull){
                lastFrameNull = false;
                if((int)(Math.random() * 3) == 0)
                if(renderOb instanceof Knight){
                    GameManager.KnightSounds.get((int)(Math.random()*4)+4).play();
                }else if (renderOb instanceof Archer){
                    GameManager.ArcherSounds.get((int)(Math.random()*3)+3).play();
                }else if (renderOb instanceof Mage){
                    GameManager.MageSounds.get((int)(Math.random()*4)+3).play();
                }else if (renderOb instanceof Rogue){
                    GameManager.RogueSounds.get((int)(Math.random()*4)+3).play();
                }else if (renderOb instanceof Miner){
                    GameManager.MinerSounds.get((int)(Math.random()*3)+3).play();
                }
            }
        }else{
            lastFrameNull = true;
        }


        if(gc.getInput().isKeyDown(KeyEvent.VK_A)){
            if((int)(Math.random() * 3) == 0)
            if(renderOb instanceof Knight){
                GameManager.KnightSounds.get((int)(Math.random()*3)+11).play();
            }else if (renderOb instanceof Archer){
                GameManager.ArcherSounds.get((int)(Math.random()*5)+10).play();
            }else if (renderOb instanceof Mage){
                GameManager.MageSounds.get((int)(Math.random()*4)+10).play();
            }else if (renderOb instanceof Rogue){
                GameManager.RogueSounds.get((int)(Math.random()*3)+10).play();
            }else if (renderOb instanceof Miner){
                GameManager.MinerSounds.get((int)(Math.random()*3)+10).play();
            }
        }
        if(gc.getInput().isButtonDown(MouseEvent.BUTTON3)){
            if((int)(Math.random() * 3) == 0)
            if(renderOb instanceof Knight){
                GameManager.KnightSounds.get((int)(Math.random()*3)+8).play();
            }else if (renderOb instanceof Archer){
                GameManager.ArcherSounds.get((int)(Math.random()*4)+6).play();
            }else if (renderOb instanceof Mage){
                GameManager.MageSounds.get((int)(Math.random()*3)+7).play();
            }else if (renderOb instanceof Rogue){
                GameManager.RogueSounds.get((int)(Math.random()*3)+7).play();
            }else if (renderOb instanceof Miner){
                GameManager.MinerSounds.get((int)(Math.random()*4)+6).play();
            }
        }
    }

    
    public void render(GameManager gm, Renderer r) {
        r.setzDepth(9999999);
        if(gm.getAiControl()){
            r.drawImageTile(imageTile, 640-width + r.getCX(), 480-height + r.getCY(), 3, 1);
        }
        r.setzDepth(65535);
        if(renderOb instanceof Knight){
            r.drawImageTile(imageTile, 640-width + r.getCX(), 480-height + r.getCY(), 0, 0);
        }
        else if(renderOb instanceof Archer){
            r.drawImageTile(imageTile, 640-width + r.getCX(), 480-height + r.getCY(), 1, 0);
        }
        else if(renderOb instanceof Mage){
            r.drawImageTile(imageTile, 640-width + r.getCX(), 480-height + r.getCY(), 2, 0);
        }
        else if(renderOb instanceof Rogue){
            r.drawImageTile(imageTile, 640-width + r.getCX(), 480-height + r.getCY(), 3, 0);
        }
        else if(renderOb instanceof Miner){
            r.drawImageTile(imageTile, 640-width + r.getCX(), 480-height + r.getCY(), 4, 0);
        }
        else if(renderOb instanceof Castle){
            r.drawImageTile(imageTile, 640-width + r.getCX(), 480-height + r.getCY(), 0, 1);
        }
        else if(renderOb instanceof Barracks){
            r.drawImageTile(imageTile, 640-width + r.getCX(), 480-height + r.getCY(), 1, 1);
        }else{
            r.drawImageTile(imageTile, 640-width + r.getCX(), 480-height + r.getCY(), 2, 1);
            return;
        }
        


            if(renderOb instanceof GUnit){
                r.fillRect(640-width + r.getCX() + 40,480-height + r.getCY() + 78, 80, 10, 0xff000000);
                r.fillRect(640-width + r.getCX() + 40,480-height + r.getCY() + 78,(int)( 80 * ((float)renderOb.getHealth() / (float)renderOb.getMaxHealth())), 10, 0xffdd4545);
            }
            else if(renderOb instanceof Castle || renderOb instanceof Barracks){
                r.fillRect(640-width + r.getCX() + 12,480-height + r.getCY() + 32, 105, 5, 0xff660000);
                if(!((GStructure) renderOb).getQueue().isEmpty()){
                    if(((GStructure) renderOb).getCostQueue().size() >= 5){
                        r.drawText("Queue is full!", 640-width + r.getCX() + 65,480-height + r.getCY() + 15, 0xffffdddd);

                    }
                    r.drawText(""+((GStructure) renderOb).getCostQueue().size(), 640-width + r.getCX() + 60,480-height + r.getCY() + 15, 0xffffffff);


                    r.fillRect(640-width + r.getCX() + 12,480-height + r.getCY() + 32,(int)(105f * (((float)((GStructure) renderOb).getCostQueue().getFirst()) / (60f * 15f))), 5, 0xffdedede);
                }
                
                

                r.fillRect(640-width + r.getCX() + 40,480-height + r.getCY() + 47, 80, 10, 0xff000000);
                r.fillRect(640-width + r.getCX() + 40,480-height + r.getCY() + 47,(int)( 80 * ((float)renderOb.getHealth() / (float)renderOb.getMaxHealth())), 10, 0xffdd4545);
                

                if(gm.getPlayerGold() < 50){
                    r.fillRect(640-width + r.getCX() + 104,480-height + r.getCY() + 80, 14, 15, 0xaa111111);
                }
                if(gm.getPlayerGold() < 100){
                    r.fillRect(640-width + r.getCX() + 15,480-height + r.getCY() + 80, 14, 15, 0xaa111111);
                    r.fillRect(640-width + r.getCX() + 37,480-height + r.getCY() + 80, 14, 15, 0xaa111111);
                }
                if(gm.getPlayerGold() < 200){
                    r.fillRect(640-width + r.getCX() + 82,480-height + r.getCY() + 80, 14, 15, 0xaa111111);
                    r.fillRect(640-width + r.getCX() + 60,480-height + r.getCY() + 80, 14, 15, 0xaa111111);
                }
            }

            

        


    }
    public boolean intersects(int mouseX, int mouseY) {

        if (this.getHitbox().contains(mouseX, mouseY)) {
            return true;
        }
        return false;
    }

    private Rectangle getHitbox() {
        return hitbox;
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
     * @param hitbox the hitbox to set
     */
    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    /**
     * @return the renderOb
     */
    public GObject getRenderOb() {
        return renderOb;
    }

    /**
     * @param renderOb the renderOb to set
     */
    public void setRenderOb(GObject renderOb) {
        this.renderOb = renderOb;
    }
}
